package com.eventos.service;

import com.eventos.model.CategoriaEvento;
import com.eventos.model.Evento;
import com.eventos.model.Usuario;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.UsuarioRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Camada de regras de negócio do sistema de eventos.
 */
public class EventoService {

    private final EventoRepository repository;
    private final UsuarioRepository usuarioRepository;
    private final List<Evento> eventos;
    private final List<Usuario> usuariosCadastrados;
    private Usuario usuarioLogado;

    public EventoService(EventoRepository repository, UsuarioRepository usuarioRepository) {
        this.repository = repository;
        this.usuarioRepository = usuarioRepository;
        this.eventos = new ArrayList<>();
        this.usuariosCadastrados = new ArrayList<>();
    }

    public void carregarEventos() throws IOException {
        eventos.clear();
        eventos.addAll(repository.carregar());
        ordenarPorHorario();
    }

    public void carregarUsuarios() throws IOException {
        usuariosCadastrados.clear();
        usuariosCadastrados.addAll(usuarioRepository.carregarTodos());
    }

    public void salvarEventos() throws IOException {
        repository.salvar(eventos);
    }

    public void salvarUsuarios() throws IOException {
        usuarioRepository.salvarTodos(usuariosCadastrados);
    }

    public void cadastrarUsuario(Usuario usuario) throws IOException {
        String email = usuario.getEmail().toLowerCase();

        boolean emailJaExiste = usuariosCadastrados.stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(email));

        if (emailJaExiste) {
            throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
        }

        usuariosCadastrados.add(usuario);
        salvarUsuarios();
        this.usuarioLogado = usuario;
    }

    public void entrarComUsuario(String email) {
        Usuario usuario = usuariosCadastrados.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email.trim()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado com o e-mail informado."));

        this.usuarioLogado = usuario;
    }

    public List<Usuario> listarUsuariosCadastrados() {
        return Collections.unmodifiableList(new ArrayList<>(usuariosCadastrados));
    }

    public boolean isUsuarioLogado() {
        return usuarioLogado != null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public Evento cadastrarEvento(
            String nome,
            String endereco,
            CategoriaEvento categoria,
            LocalDateTime horario,
            String descricao
    ) throws IOException {
        Evento evento = new Evento(nome, endereco, categoria, horario, descricao);
        eventos.add(evento);
        ordenarPorHorario();
        salvarEventos();
        return evento;
    }

    public List<Evento> listarEventosOrdenados() {
        return Collections.unmodifiableList(new ArrayList<>(eventos));
    }

    public List<Evento> listarEventosFuturos(LocalDateTime referencia) {
        return eventos.stream()
                .filter(e -> e.getStatusTemporal(referencia) == Evento.StatusTemporal.FUTURO)
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosEmAndamento(LocalDateTime referencia) {
        return eventos.stream()
                .filter(e -> e.getStatusTemporal(referencia) == Evento.StatusTemporal.EM_ANDAMENTO)
                .collect(Collectors.toList());
    }

    public List<Evento> listarEventosPassados(LocalDateTime referencia) {
        return eventos.stream()
                .filter(e -> e.getStatusTemporal(referencia) == Evento.StatusTemporal.PASSADO)
                .collect(Collectors.toList());
    }

    public List<Evento> listarMinhasParticipacoes() {
        validarUsuarioLogado();
        return eventos.stream()
                .filter(e -> e.isParticipante(usuarioLogado.getEmail()))
                .collect(Collectors.toList());
    }

    public void confirmarParticipacao(String idEvento) throws IOException {
        validarUsuarioLogado();
        Evento evento = buscarEventoObrigatorio(idEvento);

        if (evento.getStatusTemporal(LocalDateTime.now()) == Evento.StatusTemporal.PASSADO) {
            throw new IllegalStateException("Não é possível participar de um evento que já ocorreu.");
        }

        if (evento.isParticipante(usuarioLogado.getEmail())) {
            throw new IllegalStateException("Você já confirmou participação neste evento.");
        }

        evento.confirmarParticipacao(usuarioLogado.getEmail());
        salvarEventos();
    }

    public void cancelarParticipacao(String idEvento) throws IOException {
        validarUsuarioLogado();
        Evento evento = buscarEventoObrigatorio(idEvento);

        if (!evento.isParticipante(usuarioLogado.getEmail())) {
            throw new IllegalStateException("Você não está inscrito neste evento.");
        }

        evento.cancelarParticipacao(usuarioLogado.getEmail());
        salvarEventos();
    }

    public Optional<Evento> buscarPorId(String idEvento) {
        return eventos.stream()
                .filter(e -> e.getId().equals(idEvento) || e.getId().startsWith(idEvento))
                .findFirst();
    }

    private Evento buscarEventoObrigatorio(String idEvento) {
        return buscarPorId(idEvento)
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado: " + idEvento));
    }

    private void validarUsuarioLogado() {
        if (usuarioLogado == null) {
            throw new IllegalStateException("É necessário cadastrar ou entrar com um usuário primeiro.");
        }
    }

    private void ordenarPorHorario() {
        eventos.sort(Comparator.naturalOrder());
    }
}
