package com.eventos.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Representa um evento cadastrado na cidade.
 */
public class Evento implements Comparable<Evento> {

    private static final DateTimeFormatter FORMATO_EXIBICAO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final String id;
    private final String nome;
    private final String endereco;
    private final CategoriaEvento categoria;
    private final LocalDateTime horario;
    private final String descricao;
    private final Set<String> participantes;

    public Evento(
            String nome,
            String endereco,
            CategoriaEvento categoria,
            LocalDateTime horario,
            String descricao
    ) {
        this(UUID.randomUUID().toString(), nome, endereco, categoria, horario, descricao, new HashSet<>());
    }

    public Evento(
            String id,
            String nome,
            String endereco,
            CategoriaEvento categoria,
            LocalDateTime horario,
            String descricao,
            Set<String> participantes
    ) {
        this.id = Objects.requireNonNull(id, "ID é obrigatório");
        this.nome = Objects.requireNonNull(nome, "Nome é obrigatório").trim();
        this.endereco = Objects.requireNonNull(endereco, "Endereço é obrigatório").trim();
        this.categoria = Objects.requireNonNull(categoria, "Categoria é obrigatória");
        this.horario = Objects.requireNonNull(horario, "Horário é obrigatório");
        this.descricao = Objects.requireNonNull(descricao, "Descrição é obrigatória").trim();
        this.participantes = new HashSet<>(participantes != null ? participantes : Collections.emptySet());
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public CategoriaEvento getCategoria() {
        return categoria;
    }

    public LocalDateTime getHorario() {
        return horario;
    }

    public String getDescricao() {
        return descricao;
    }

    public Set<String> getParticipantes() {
        return Collections.unmodifiableSet(participantes);
    }

    public boolean isParticipante(String emailUsuario) {
        return participantes.contains(emailUsuario.toLowerCase());
    }

    public void confirmarParticipacao(String emailUsuario) {
        participantes.add(emailUsuario.toLowerCase());
    }

    public void cancelarParticipacao(String emailUsuario) {
        participantes.remove(emailUsuario.toLowerCase());
    }

    public StatusTemporal getStatusTemporal(LocalDateTime referencia) {
        LocalDateTime inicio = horario;
        LocalDateTime fim = horario.plusHours(3);

        if (referencia.isBefore(inicio)) {
            return StatusTemporal.FUTURO;
        }
        if (!referencia.isAfter(fim)) {
            return StatusTemporal.EM_ANDAMENTO;
        }
        return StatusTemporal.PASSADO;
    }

    public String getHorarioFormatado() {
        return horario.format(FORMATO_EXIBICAO);
    }

    @Override
    public int compareTo(Evento outro) {
        return this.horario.compareTo(outro.horario);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Evento)) {
            return false;
        }
        Evento outro = (Evento) obj;
        return id.equals(outro.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
                "[%s] %s | %s | %s | %s | Participantes: %d",
                id.substring(0, 8),
                nome,
                categoria.getDescricao(),
                getHorarioFormatado(),
                endereco,
                participantes.size()
        );
    }

    public enum StatusTemporal {
        FUTURO("Agendado"),
        EM_ANDAMENTO("Ocorrendo agora"),
        PASSADO("Já ocorreu");

        private final String descricao;

        StatusTemporal(String descricao) {
            this.descricao = descricao;
        }

        public String getDescricao() {
            return descricao;
        }
    }
}
