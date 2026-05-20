package com.eventos.controller;

import com.eventos.model.CategoriaEvento;
import com.eventos.model.Evento;
import com.eventos.model.Usuario;
import com.eventos.service.EventoService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Controlador entre a view e os serviços de negócio.
 */
public class SistemaController {

    private final EventoService eventoService;

    public SistemaController(EventoService eventoService) {
        this.eventoService = eventoService;
    }

    public void inicializar() throws IOException {
        eventoService.carregarEventos();
        eventoService.carregarUsuarios();
    }

    public void encerrar() throws IOException {
        eventoService.salvarEventos();
        eventoService.salvarUsuarios();
    }

    public void cadastrarUsuario(String nome, String email, String cpf, String telefone, String cidade)
            throws IOException {
        Usuario usuario = new Usuario(nome, email, cpf, telefone, cidade);
        eventoService.cadastrarUsuario(usuario);
    }

    public void entrarComUsuario(String email) {
        eventoService.entrarComUsuario(email);
    }

    public List<Usuario> listarUsuariosCadastrados() {
        return eventoService.listarUsuariosCadastrados();
    }

    public boolean isUsuarioLogado() {
        return eventoService.isUsuarioLogado();
    }

    public Usuario getUsuarioLogado() {
        return eventoService.getUsuarioLogado();
    }

    public Evento cadastrarEvento(
            String nome,
            String endereco,
            CategoriaEvento categoria,
            LocalDateTime horario,
            String descricao
    ) throws IOException {
        return eventoService.cadastrarEvento(nome, endereco, categoria, horario, descricao);
    }

    public List<Evento> listarTodosEventos() {
        return eventoService.listarEventosOrdenados();
    }

    public List<Evento> listarEventosFuturos() {
        return eventoService.listarEventosFuturos(LocalDateTime.now());
    }

    public List<Evento> listarEventosEmAndamento() {
        return eventoService.listarEventosEmAndamento(LocalDateTime.now());
    }

    public List<Evento> listarEventosPassados() {
        return eventoService.listarEventosPassados(LocalDateTime.now());
    }

    public List<Evento> listarMinhasParticipacoes() {
        return eventoService.listarMinhasParticipacoes();
    }

    public void confirmarParticipacao(String idEvento) throws IOException {
        eventoService.confirmarParticipacao(idEvento);
    }

    public void cancelarParticipacao(String idEvento) throws IOException {
        eventoService.cancelarParticipacao(idEvento);
    }

    public Optional<Evento> buscarEvento(String idEvento) {
        return eventoService.buscarPorId(idEvento);
    }

    public CategoriaEvento[] listarCategorias() {
        return CategoriaEvento.values();
    }
}
