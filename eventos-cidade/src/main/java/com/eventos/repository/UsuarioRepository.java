package com.eventos.repository;

import com.eventos.model.Usuario;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsável pela persistência dos usuários em users.data.
 */
public class UsuarioRepository {

    private static final String ARQUIVO = "users.data";
    private static final String SEPARADOR = "\\|";

    private final Path caminhoArquivo;

    public UsuarioRepository() {
        this(Paths.get(ARQUIVO));
    }

    public UsuarioRepository(Path caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
    }

    public List<Usuario> carregarTodos() throws IOException {
        if (!Files.exists(caminhoArquivo)) {
            return new ArrayList<>();
        }

        List<Usuario> usuarios = new ArrayList<>();

        try (BufferedReader leitor = Files.newBufferedReader(caminhoArquivo, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                linha = linha.trim();
                if (linha.isEmpty() || linha.startsWith("#")) {
                    continue;
                }
                usuarios.add(deserializar(linha));
            }
        }

        return usuarios;
    }

    public void salvarTodos(List<Usuario> usuarios) throws IOException {
        try (BufferedWriter escritor = Files.newBufferedWriter(caminhoArquivo, StandardCharsets.UTF_8)) {
            escritor.write("# Formato: nome|email|cpf|telefone|cidade");
            escritor.newLine();
            for (Usuario usuario : usuarios) {
                escritor.write(serializar(usuario));
                escritor.newLine();
            }
        }
    }

    private String serializar(Usuario usuario) {
        return String.join("|",
                escapar(usuario.getNome()),
                escapar(usuario.getEmail()),
                escapar(usuario.getCpf()),
                escapar(usuario.getTelefone()),
                escapar(usuario.getCidade())
        );
    }

    private Usuario deserializar(String linha) {
        String[] partes = linha.split(SEPARADOR, 5);
        if (partes.length < 5) {
            throw new IllegalArgumentException("Linha inválida no arquivo de usuários: " + linha);
        }

        return new Usuario(
                desescapar(partes[0]),
                desescapar(partes[1]),
                desescapar(partes[2]),
                desescapar(partes[3]),
                desescapar(partes[4])
        );
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
