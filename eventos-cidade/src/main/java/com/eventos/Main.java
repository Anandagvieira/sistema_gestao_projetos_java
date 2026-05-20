package com.eventos;

import com.eventos.controller.SistemaController;
import com.eventos.repository.EventoRepository;
import com.eventos.repository.UsuarioRepository;
import com.eventos.service.EventoService;
import com.eventos.util.AppPaths;
import com.eventos.view.ConsoleView;

/**
 * Ponto de entrada da aplicação console.
 */
public class Main {

    public static void main(String[] args) {
        EventoRepository repository = new EventoRepository(AppPaths.getArquivoEventos());
        UsuarioRepository usuarioRepository = new UsuarioRepository(AppPaths.getArquivoUsuarios());
        EventoService service = new EventoService(repository, usuarioRepository);
        SistemaController controller = new SistemaController(service);
        ConsoleView view = new ConsoleView(controller);

        view.executar();
    }
}
