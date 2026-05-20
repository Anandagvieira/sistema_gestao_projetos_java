package com.eventos.util;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resolve caminhos de dados de forma portável (JAR, IDE ou linha de comando).
 */
public final class AppPaths {

    private AppPaths() {
    }

    public static Path getDiretorioBase() {
        String configurado = System.getProperty("eventos.dir");
        if (configurado != null && !configurado.isBlank()) {
            return Paths.get(configurado).toAbsolutePath().normalize();
        }

        Path diretorioJar = detectarDiretorioJar();
        if (diretorioJar != null) {
            return diretorioJar;
        }

        return Paths.get(System.getProperty("user.dir")).toAbsolutePath().normalize();
    }

    public static Path getArquivoEventos() {
        return getDiretorioBase().resolve("events.data");
    }

    public static Path getArquivoUsuarios() {
        return getDiretorioBase().resolve("users.data");
    }

    private static Path detectarDiretorioJar() {
        try {
            var localizacao = AppPaths.class.getProtectionDomain().getCodeSource().getLocation();
            if (localizacao == null) {
                return null;
            }

            Path caminho = Paths.get(localizacao.toURI());
            if (caminho.toString().endsWith(".jar")) {
                Path pai = caminho.getParent();
                return pai != null ? pai.toAbsolutePath().normalize() : null;
            }
        } catch (URISyntaxException | SecurityException ignored) {
            return null;
        }

        return null;
    }
}
