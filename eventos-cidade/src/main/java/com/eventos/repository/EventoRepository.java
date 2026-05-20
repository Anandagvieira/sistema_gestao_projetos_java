package com.eventos.repository;

import com.eventos.model.CategoriaEvento;
import com.eventos.model.Evento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Responsável pela persistência dos eventos no arquivo events.data.
 */
public class EventoRepository {

    private static final String ARQUIVO = "events.data";
    private static final String SEPARADOR = "\\|";
    private static final DateTimeFormatter FORMATO_HORARIO = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Path caminhoArquivo;

    public EventoRepository() {
        this(Paths.get(ARQUIVO));
    }

    public EventoRepository(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public List<Evento> carregar() throws IOException {
        if (!Files.exists(caminhoArquivo)) {
            return new ArrayList<>();
        }

        List<Evento> eventos = new ArrayList<>();

        try (BufferedReader leitor = Files.newBufferedReader(caminhoArquivo, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                eventos.add(deserializar(linha));
            }
        }

        return eventos;
    }

    public void salvar(List<Evento> eventos) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(caminhoArquivo, StandardCharsets.UTF_8)) {
            escritor.write("# Formato: id|nome|endereco|categoria|horario|descricao|participantes");
            escritor.newLine();
            for (Evento evento : eventos) {
                escritor.write(serializar(evento));
                escritor.newLine();
            }
        }
    }

    private String serializar(Evento evento) {
        String participantes = evento.getParticipantes().stream()
                .collect(Collectors.joining(","));

        return String.join("|",
                escapar(evento.getId()),
                escapar(evento.getNome()),
                escapar(evento.getEndereco()),
                String.valueOf(evento.getCategoria().ordinal()),
                evento.getHorario().format(FORMATO_HORARIO),
                escapar(evento.getDescricao()),
                escapar(participantes)
        );
    }

    private Evento deserializar(String linha) {
        String[] partes = linha.split(SEPARADOR, 7);
        if (partes.length < 6) {
            throw new IllegalArgumentException("Linha inválida no arquivo de eventos: " + linha);
        }

        String id = desescapar(partes[0]);
        String nome = desescapar(partes[1]);
        String endereco = desescapar(partes[2]);
        CategoriaEvento categoria = CategoriaEvento.fromOrdinal(Integer.parseInt(partes[3]));
        LocalDateTime horario = LocalDateTime.parse(partes[4], FORMATO_HORARIO);
        String descricao = desescapar(partes[5]);

        Set<String> participantes = new HashSet<>();
        if (partes.length == 7 && !partes[6].isBlank()) {
            participantes.addAll(
                    Arrays.stream(desescapar(partes[6]).split(","))
                            .map(String::trim)
                            .filter(s -> !s.isEmpty())
                            .collect(Collectors.toSet())
            );
        }

        return new Evento(id, nome, endereco, categoria, horario, descricao, participantes);
    }

    private String escapar(String valor) {
        return valor.replace("\\", "\\\\").replace("|", "\\|").replace("\n", "\\n");
    }

    private String desescapar(String valor) {
        StringBuilder resultado = new StringBuilder();
        boolean escape = false;

        for (int i = 0; i < valor.length(); i++) {
            char atual = valor.charAt(i);
            if (escape) {
                if (atual == 'n') {
                    resultado.append('\n');
                } else {
                    resultado.append(atual);
                }
                escape = false;
            } else if (atual == '\\') {
                escape = true;
            } else {
                resultado.append(atual);
            }
        }

        return resultado.toString();
    }
}
