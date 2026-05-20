# 🎯 Sistema de Cadastro e Gerenciamento de Eventos da Cidade

Aplicação desenvolvida em **Java (console)** utilizando **Programação Orientada a Objetos (POO)** e arquitetura em camadas inspirada no padrão **MVC**.

O sistema permite cadastrar usuários, registrar eventos urbanos, confirmar participação, cancelar inscrições e consultar eventos por status temporal (**futuros, em andamento ou já realizados**), com persistência automática dos dados em arquivos locais.

---

# 📌 Objetivo do Projeto

Este projeto foi desenvolvido como atividade acadêmica da disciplina de Programação Orientada a Objetos, com foco na aplicação prática de:

* Encapsulamento
* Abstração
* Organização em camadas
* Manipulação de arquivos
* Estruturas de dados (`List`, `Map`)
* Persistência manual
* Ordenação de dados
* Manipulação de datas com `LocalDateTime`

---

# 🚀 Funcionalidades Implementadas

## 👤 Usuários

* Cadastro de usuários
* Login com usuário existente
* Persistência em arquivo (`users.data`)
* Múltiplos usuários cadastrados simultaneamente

### Dados armazenados:

* Nome
* E-mail
* CPF
* Telefone
* Cidade

---

## 📅 Eventos

* Cadastro de eventos
* Consulta de eventos ordenados por data/hora
* Classificação temporal:

  * ✅ Agendado
  * 🔴 Ocorrendo agora
  * ⚫ Já ocorreu

### Dados armazenados:

* Nome do evento
* Endereço
* Categoria
* Data e horário
* Descrição

---

## 🎟️ Participações

* Confirmar participação em eventos
* Visualizar participações do usuário logado
* Cancelar participação
* Persistência das participações

---

## 💾 Persistência de Dados

Os dados são armazenados automaticamente em arquivos locais:

| Arquivo              | Função                          |
| -------------------- | ------------------------------- |
| `events.data`        | Armazena os eventos cadastrados |
| `users.data`         | Armazena os usuários            |
| `participacoes.data` | Armazena as participações       |

Os arquivos são carregados automaticamente ao iniciar o sistema e atualizados ao sair da aplicação.

---

# 🧱 Arquitetura do Projeto

O projeto segue uma organização inspirada em **MVC (Model-View-Controller)** para separar responsabilidades e facilitar manutenção futura.

```text
eventos-cidade/
├── src/main/java/com/eventos/
│
│   ├── model/
│   │   ├── Usuario.java
│   │   ├── Evento.java
│   │   └── CategoriaEvento.java
│   │
│   ├── view/
│   │   └── ConsoleView.java
│   │
│   ├── controller/
│   │   └── SistemaController.java
│   │
│   ├── service/
│   │   └── EventoService.java
│   │
│   ├── repository/
│   │   └── ArquivoRepository.java
│   │
│   └── util/
│       └── AppPaths.java
│
├── data/
│   ├── events.data
│   ├── users.data
│   └── participacoes.data
│
├── docs/
│   └── diagrama-classes.md
│
├── pom.xml
├── run.bat / run.sh
├── compile.bat / compile.sh
├── build.bat / build.sh
└── README.md
```

---

# 🛠️ Tecnologias Utilizadas

* Java 17+
* Maven (opcional)
* `java.time.LocalDateTime`
* Programação Orientada a Objetos
* Persistência em arquivo texto
* Arquitetura em camadas

---

# 📋 Requisitos

| Software | Versão mínima           | Obrigatório |
| -------- | ----------------------- | ----------- |
| Java JDK | 17+                     | ✅ Sim       |
| Maven    | 3.6+                    | ❌ Não       |
| Git      | Qualquer versão recente | ❌ Não       |

Verifique sua instalação:

```bash
java -version
javac -version
```

---

# ▶️ Como Executar o Projeto

## ✅ Opção 1 — Windows (recomendado)

Abra o terminal na pasta do projeto:

```bat
cd C:\Users\BR\Projects\eventos-cidade
.\run.bat
```

O script compila automaticamente e inicia a aplicação.

---

## ✅ Opção 2 — Linux/macOS

```bash
cd eventos-cidade
chmod +x run.sh compile.sh
./run.sh
```

---

## ✅ Opção 3 — Maven

```bash
mvn compile exec:java
```

---

## ✅ Opção 4 — IDE

### IntelliJ / Eclipse / NetBeans

1. Abra a pasta do projeto
2. Configure o JDK 17+
3. Execute:

```text
com.eventos.Main
```

---

# 📦 Gerar JAR Executável

## Windows

```bat
.\build.bat
```

## Linux/macOS

```bash
chmod +x build.sh
./build.sh
```

O arquivo será gerado em:

```text
target/eventos-cidade.jar
```

Para executar em outra máquina:

```bash
java -jar eventos-cidade.jar
```

---

# 🧪 Dados de Exemplo

O projeto já inclui dados para testes.

## Usuários (`users.data`)

| Nome        | Cidade    |
| ----------- | --------- |
| Maria Silva | São Paulo |
| João Santos | São Paulo |

---

## Eventos (`events.data`)

O sistema possui eventos de exemplo com diferentes status:

* Festas
* Shows
* Eventos esportivos
* Eventos culturais
* Eventos religiosos
* Feiras
* Eventos gastronômicos

---

# 📌 Menu do Sistema

```text
1 - Cadastrar novo usuário
8 - Entrar com usuário existente

2 - Cadastrar novo evento
3 - Consultar eventos cadastrados

4 - Confirmar participação em evento
5 - Visualizar minhas participações
6 - Cancelar participação

7 - Eventos por status
0 - Sair
```

---

# 🕒 Formato de Data e Hora

Ao cadastrar eventos:

```text
dd/MM/yyyy HH:mm
```

### Exemplo:

```text
25/05/2026 20:00
```

---

# 🏷️ Categorias de Eventos

1. Festas
2. Eventos esportivos
3. Shows
4. Eventos culturais
5. Feiras e exposições
6. Eventos religiosos
7. Eventos educacionais
8. Eventos gastronômicos
9. Outros

---

# 🧪 Roteiro de Testes

| Passo | Ação                         | Resultado Esperado |
| ----- | ---------------------------- | ------------------ |
| 1     | Entrar com usuário existente | Login realizado    |
| 2     | Consultar eventos            | Eventos ordenados  |
| 3     | Ver eventos por status       | Separação correta  |
| 4     | Confirmar participação       | Participação salva |
| 5     | Ver participações            | Evento listado     |
| 6     | Cancelar participação        | Evento removido    |
| 7     | Encerrar sistema             | Dados persistidos  |

---

# 🔄 Teste de Persistência

1. Cadastre novos eventos e usuários
2. Saia pela opção `0`
3. Abra novamente o sistema
4. Verifique se os dados continuam disponíveis

---

# 🧠 Conceitos Aplicados

* Programação Orientada a Objetos
* Separação de responsabilidades
* Persistência de dados
* Estrutura de dados (`ArrayList`, `HashMap`)
* UUID para identificação única
* Ordenação com `Comparator`
* Manipulação de datas e horários
* Tratamento de exceções

---

# 📊 Diagrama de Classes

O diagrama UML está disponível em:

```text
docs/diagrama-classes.md
```

Compatível com Mermaid e visualização no GitHub.

---

# ⚠️ Solução de Problemas

## ❌ `'java' não é reconhecido`

Instale o JDK:

* Eclipse Temurin:

  * [https://adoptium.net](https://adoptium.net)

Adicione o Java ao `PATH`.

---

## ❌ Problemas de acentuação

Windows:

```bat
chcp 65001
```

---

## ❌ Dados não aparecem

Certifique-se de:

* Encerrar pelo menu (`0`)
* Manter os arquivos `.data` na mesma pasta do projeto

---

# ✨ Diferenciais do Projeto

* Persistência automática de dados
* Estrutura inspirada em MVC
* Código modularizado
* Projeto portável
* Compatível com Windows, Linux e macOS
* Organização profissional para GitHub

---

# 📚 Possíveis Evoluções Futuras

* Interface gráfica com JavaFX
* Banco de dados (MySQL/PostgreSQL)
* API REST
* Notificações automáticas
* Login com autenticação
* Sistema web

---

# 👨‍💻 Autor

Projeto desenvolvido por **Nanda Araújo** para atividade acadêmica de Programação Orientada a Objetos.

---

# 📌 Entrega Acadêmica

Para entrega:

* Publicar o projeto no GitHub
* Incluir:

  * Código-fonte
  * README
  * Arquivos `.data`
  * Diagrama de classes

Exemplo de estrutura esperada:

```text
README.md
src/
docs/
data/
pom.xml
```
