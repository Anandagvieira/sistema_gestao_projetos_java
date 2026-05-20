classDiagram
    direction TB

    class Main {
        +main(String[] args)$
    }

    class ConsoleView {
        -SistemaController controller
        -Scanner scanner
        +executar()
    }

    class SistemaController {
        -EventoService eventoService
        +inicializar()
        +encerrar()
        +cadastrarUsuario(...)
        +cadastrarEvento(...)
        +listarTodosEventos()
        +confirmarParticipacao(String)
        +cancelarParticipacao(String)
    }

    class EventoService {
        -EventoRepository repository
        -List~Evento~ eventos
        -Usuario usuarioLogado
        +carregarEventos()
        +salvarEventos()
        +cadastrarEvento(...)
        +listarEventosOrdenados()
        +confirmarParticipacao(String)
        +cancelarParticipacao(String)
    }

    class EventoRepository {
        -Path caminhoArquivo
        +carregar() List~Evento~
        +salvar(List~Evento~)
    }

    class Usuario {
        -String nome
        -String email
        -String cpf
        -String telefone
        -String cidade
        +getNome()
        +getEmail()
    }

    class Evento {
        -String id
        -String nome
        -String endereco
        -CategoriaEvento categoria
        -LocalDateTime horario
        -String descricao
        -Set~String~ participantes
        +getStatusTemporal(LocalDateTime)
        +confirmarParticipacao(String)
        +cancelarParticipacao(String)
    }

  class CategoriaEvento {
        <<enumeration>>
        FESTA
        ESPORTIVO
        SHOW
        CULTURAL
        FEIRA
        RELIGIOSO
        EDUCACIONAL
        GASTRONOMICO
        OUTROS
    }

    class StatusTemporal {
        <<enumeration>>
        FUTURO
        EM_ANDAMENTO
        PASSADO
    }

    Main --> ConsoleView : cria
    Main --> SistemaController : cria
    Main --> EventoService : cria
    Main --> EventoRepository : cria

    ConsoleView --> SistemaController : usa
    SistemaController --> EventoService : delega
    EventoService --> EventoRepository : persiste
    EventoService --> Evento : gerencia
    EventoService --> Usuario : sessão

    Evento --> CategoriaEvento : possui
    Evento --> StatusTemporal : calcula
    EventoRepository --> Evento : serializa
