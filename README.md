# sistema_gestao_projetos_java
Trabalho Primeiro Semestre IBMR
# 🎯 Sistema de Gerenciamento de Eventos (Java)

## 📌 Descrição

Sistema desenvolvido em Java utilizando Programação Orientada a Objetos (POO), com execução em console, que permite o cadastro e gerenciamento de eventos e participação de usuários.

## 🚀 Funcionalidades

* Cadastro de eventos
* Listagem de eventos ordenados por data
* Identificação de eventos passados, atuais e futuros
* Participação em eventos
* Cancelamento de participação
* Persistência de dados em arquivos locais
* Carregamento automático ao iniciar o sistema

## 🧱 Estrutura do Projeto

```
model/
  Evento.java
service/
  Sistema.java
Main.java
data/
  eventos.data
  participacoes.data
```

## 💾 Persistência

Os dados são armazenados em arquivos `.data`:

* eventos.data → armazena os eventos cadastrados
* participacoes.data → armazena as participações dos usuários

## 🛠️ Tecnologias

* Java
* POO
* Manipulação de arquivos (FileReader / FileWriter)
* LocalDateTime

## ▶️ Como executar

1. Compile os arquivos:

```
javac Main.java
```

2. Execute:

```
java Main
```

## 🧠 Conceitos aplicados

* Encapsulamento
* Abstração
* Separação de responsabilidades
* Estrutura de dados (List, Map)
* Persistência manual

## ✨ Autor

Projeto desenvolvido por Nanda Araújo 💻
