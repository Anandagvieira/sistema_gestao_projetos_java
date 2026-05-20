package com.eventos.view;

import com.eventos.controller.SistemaController;
import com.eventos.model.CategoriaEvento;
import com.eventos.model.Evento;
import com.eventos.model.Usuario;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

/**
 * Interface de console do sistema de eventos.
 */
public class ConsoleView {

    private static final DateTimeFormatter FORMATO_ENTRADA =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final SistemaController controller;
    private final Scanner scanner;

    public ConsoleView(SistemaController controller) {
        this.controller = controller;
        this.scanner = new Scanner(System.in);
    }

    public void executar() {
        exibirCabecalho();

        try {
            controller.inicializar();
            exibirMensagem("Eventos carregados de events.data: "
                    + controller.listarTodosEventos().size() + " evento(s).");
            exibirMensagem("Usuários disponíveis em users.data: "
                    + controller.listarUsuariosCadastrados().size() + " cadastro(s).");
            exibirMensagem("Use a opção 1 para cadastrar um novo usuário ou a opção 8 para entrar.");
        } catch (IOException e) {
            exibirErro("Não foi possível carregar os arquivos de dados: " + e.getMessage());
        }

        boolean executando = true;
        while (executando) {
            exibirMenuPrincipal();
            int opcao = lerInteiro("Escolha uma opção: ");

            try {
                switch (opcao) {
                    case 1 -> cadastrarUsuario();
                    case 2 -> cadastrarEvento();
                    case 3 -> consultarEventos();
                    case 4 -> participarEvento();
                    case 5 -> visualizarMinhasParticipacoes();
                    case 6 -> cancelarParticipacao();
                    case 7 -> exibirEventosPorStatus();
                    case 8 -> entrarComUsuario();
                    case 0 -> {
                        encerrarSistema();
                        executando = false;
                    }
                    default -> exibirErro("Opção inválida. Tente novamente.");
                }
            } catch (IllegalArgumentException | IllegalStateException e) {
                exibirErro(e.getMessage());
            } catch (IOException e) {
                exibirErro("Erro ao salvar dados: " + e.getMessage());
            }

            if (executando) {
                pausar();
            }
        }
    }

    private void cadastrarUsuario() throws IOException {
        exibirTitulo("Cadastrar Novo Usuário");
        exibirMensagem("Preencha os dados abaixo. O novo usuário será salvo em users.data.");

        String nome = lerTextoObrigatorio("Nome completo: ");
        String email = lerTextoObrigatorio("E-mail: ");
        String cpf = lerTextoObrigatorio("CPF: ");
        String telefone = lerTextoObrigatorio("Telefone: ");
        String cidade = lerTextoObrigatorio("Cidade: ");

        controller.cadastrarUsuario(nome, email, cpf, telefone, cidade);
        exibirMensagem("Usuário cadastrado e logado com sucesso!");
        exibirMensagem(controller.getUsuarioLogado().toString());
    }

    private void entrarComUsuario() {
        exibirTitulo("Entrar com Usuário Existente");

        List<Usuario> usuarios = controller.listarUsuariosCadastrados();
        if (usuarios.isEmpty()) {
            exibirMensagem("Nenhum usuário cadastrado. Use a opção 1 para criar o primeiro.");
            return;
        }

        exibirMensagem("Usuários cadastrados:");
        for (int i = 0; i < usuarios.size(); i++) {
            Usuario usuario = usuarios.get(i);
            System.out.printf(
                    "%d) %s | %s | %s%n",
                    i + 1,
                    usuario.getNome(),
                    usuario.getEmail(),
                    usuario.getCidade()
            );
        }

        int opcao = lerInteiro("Escolha o número do usuário (ou 0 para informar o e-mail): ");

        if (opcao == 0) {
            String email = lerTextoObrigatorio("E-mail do usuário: ");
            controller.entrarComUsuario(email);
        } else if (opcao < 1 || opcao > usuarios.size()) {
            throw new IllegalArgumentException("Número de usuário inválido.");
        } else {
            controller.entrarComUsuario(usuarios.get(opcao - 1).getEmail());
        }

        exibirMensagem("Bem-vindo(a), " + controller.getUsuarioLogado().getNome() + "!");
    }

    private void cadastrarEvento() throws IOException {
        validarUsuarioLogado();
        exibirTitulo("Cadastrar Novo Evento");
        exibirMensagem("O evento será salvo em events.data ao confirmar o cadastro.");

        String nome = lerTextoObrigatorio("Nome do evento: ");
        String endereco = lerTextoObrigatorio("Endereço: ");
        CategoriaEvento categoria = selecionarCategoria();
        LocalDateTime horario = lerDataHora("Horário (dd/MM/yyyy HH:mm): ");
        String descricao = lerTextoObrigatorio("Descrição: ");

        Evento evento = controller.cadastrarEvento(nome, endereco, categoria, horario, descricao);
        exibirMensagem("Evento cadastrado com sucesso!");
        exibirEventoDetalhado(evento, LocalDateTime.now());
    }

    private void consultarEventos() {
        exibirTitulo("Eventos Cadastrados (ordenados por horário)");
        List<Evento> eventos = controller.listarTodosEventos();

        if (eventos.isEmpty()) {
            exibirMensagem("Nenhum evento cadastrado até o momento.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        for (int i = 0; i < eventos.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, formatarResumoEvento(eventos.get(i), agora));
        }
    }

    private void participarEvento() throws IOException {
        validarUsuarioLogado();
        exibirTitulo("Confirmar Participação");

        List<Evento> eventos = controller.listarEventosFuturos();
        eventos.addAll(controller.listarEventosEmAndamento());

        if (eventos.isEmpty()) {
            exibirMensagem("Não há eventos disponíveis para participação.");
            return;
        }

        exibirListaNumerada(eventos);
        int indice = lerInteiro("Informe o número do evento: ") - 1;

        if (indice < 0 || indice >= eventos.size()) {
            throw new IllegalArgumentException("Número de evento inválido.");
        }

        Evento evento = eventos.get(indice);
        controller.confirmarParticipacao(evento.getId());
        exibirMensagem("Participação confirmada em: " + evento.getNome());
    }

    private void visualizarMinhasParticipacoes() {
        validarUsuarioLogado();
        exibirTitulo("Meus Eventos Confirmados");

        List<Evento> eventos = controller.listarMinhasParticipacoes();
        if (eventos.isEmpty()) {
            exibirMensagem("Você ainda não confirmou participação em nenhum evento.");
            return;
        }

        LocalDateTime agora = LocalDateTime.now();
        for (int i = 0; i < eventos.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, formatarResumoEvento(eventos.get(i), agora));
        }
    }

    private void cancelarParticipacao() throws IOException {
        validarUsuarioLogado();
        exibirTitulo("Cancelar Participação");

        List<Evento> eventos = controller.listarMinhasParticipacoes();
        if (eventos.isEmpty()) {
            exibirMensagem("Você não possui participações para cancelar.");
            return;
        }

        exibirListaNumerada(eventos);
        int indice = lerInteiro("Informe o número do evento para cancelar: ") - 1;

        if (indice < 0 || indice >= eventos.size()) {
            throw new IllegalArgumentException("Número de evento inválido.");
        }

        Evento evento = eventos.get(indice);
        controller.cancelarParticipacao(evento.getId());
        exibirMensagem("Participação cancelada em: " + evento.getNome());
    }

    private void exibirEventosPorStatus() {
        exibirTitulo("Eventos por Situação Temporal");
        LocalDateTime agora = LocalDateTime.now();

        exibirSubtitulo("Ocorrendo agora");
        imprimirGrupo(controller.listarEventosEmAndamento(), agora);

        exibirSubtitulo("Próximos eventos");
        imprimirGrupo(controller.listarEventosFuturos(), agora);

        exibirSubtitulo("Eventos que já ocorreram");
        imprimirGrupo(controller.listarEventosPassados(), agora);
    }

    private void imprimirGrupo(List<Evento> eventos, LocalDateTime referencia) {
        if (eventos.isEmpty()) {
            exibirMensagem("  (nenhum)");
            return;
        }

        for (Evento evento : eventos) {
            System.out.println("  - " + formatarResumoEvento(evento, referencia));
        }
    }

    private CategoriaEvento selecionarCategoria() {
        CategoriaEvento[] categorias = controller.listarCategorias();
        System.out.println("Categorias disponíveis:");
        for (int i = 0; i < categorias.length; i++) {
            System.out.printf("  %d - %s%n", i + 1, categorias[i].getDescricao());
        }

        int opcao = lerInteiro("Escolha a categoria: ");
        if (opcao < 1 || opcao > categorias.length) {
            throw new IllegalArgumentException("Categoria inválida.");
        }
        return categorias[opcao - 1];
    }

    private void exibirListaNumerada(List<Evento> eventos) {
        LocalDateTime agora = LocalDateTime.now();
        for (int i = 0; i < eventos.size(); i++) {
            System.out.printf("%d) %s%n", i + 1, formatarResumoEvento(eventos.get(i), agora));
        }
    }

    private String formatarResumoEvento(Evento evento, LocalDateTime referencia) {
        Evento.StatusTemporal status = evento.getStatusTemporal(referencia);
        return String.format(
                "%s | %s | %s | %s | %s",
                evento.getNome(),
                evento.getCategoria().getDescricao(),
                evento.getHorarioFormatado(),
                status.getDescricao(),
                evento.getEndereco()
        );
    }

    private void exibirEventoDetalhado(Evento evento, LocalDateTime referencia) {
        Evento.StatusTemporal status = evento.getStatusTemporal(referencia);
        System.out.println("ID: " + evento.getId());
        System.out.println("Nome: " + evento.getNome());
        System.out.println("Endereço: " + evento.getEndereco());
        System.out.println("Categoria: " + evento.getCategoria().getDescricao());
        System.out.println("Horário: " + evento.getHorarioFormatado());
        System.out.println("Status: " + status.getDescricao());
        System.out.println("Descrição: " + evento.getDescricao());
        System.out.println("Participantes: " + evento.getParticipantes().size());
    }

    private LocalDateTime lerDataHora(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem);
            try {
                return LocalDateTime.parse(entrada, FORMATO_ENTRADA);
            } catch (DateTimeParseException e) {
                exibirErro("Formato inválido. Use dd/MM/yyyy HH:mm");
            }
        }
    }

    private String lerTextoObrigatorio(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem);
            if (!entrada.isBlank()) {
                return entrada;
            }
            exibirErro("Este campo é obrigatório.");
        }
    }

    private String lerTexto(String mensagem) {
        System.out.print(mensagem);
        return scanner.nextLine().trim();
    }

    private int lerInteiro(String mensagem) {
        while (true) {
            String entrada = lerTexto(mensagem);
            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                exibirErro("Informe um número inteiro válido.");
            }
        }
    }

    private void validarUsuarioLogado() {
        if (!controller.isUsuarioLogado()) {
            throw new IllegalStateException("Cadastre um usuário antes de realizar esta operação.");
        }
    }

    private void encerrarSistema() throws IOException {
        controller.encerrar();
        exibirMensagem("Dados salvos. Até logo!");
    }

    private void exibirCabecalho() {
        System.out.println("=================================================");
        System.out.println("   SISTEMA DE CADASTRO E NOTIFICAÇÃO DE EVENTOS   ");
        System.out.println("=================================================");
    }

    private void exibirMenuPrincipal() {
        System.out.println();
        System.out.println("---------------------- MENU ----------------------");
        if (controller.isUsuarioLogado()) {
            Usuario usuario = controller.getUsuarioLogado();
            System.out.println("Usuário: " + usuario.getNome() + " (" + usuario.getCidade() + ")");
        } else {
            System.out.println("Nenhum usuário cadastrado na sessão.");
        }
        System.out.println("1 - Cadastrar novo usuário");
        System.out.println("8 - Entrar com usuário existente");
        System.out.println("2 - Cadastrar novo evento");
        System.out.println("3 - Consultar eventos cadastrados");
        System.out.println("4 - Confirmar participação em evento");
        System.out.println("5 - Visualizar minhas participações");
        System.out.println("6 - Cancelar participação");
        System.out.println("7 - Eventos por status (agora / próximos / passados)");
        System.out.println("0 - Sair");
        System.out.println("--------------------------------------------------");
    }

    private void exibirTitulo(String titulo) {
        System.out.println();
        System.out.println(">>> " + titulo);
    }

    private void exibirSubtitulo(String titulo) {
        System.out.println();
        System.out.println(titulo + ":");
    }

    private void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    private void exibirErro(String mensagem) {
        System.out.println("[ERRO] " + mensagem);
    }

    private void pausar() {
        lerTexto("Pressione ENTER para continuar...");
    }
}
