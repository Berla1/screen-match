# Screen Match

Screen Match é um projeto desenvolvido em Java que permite realizar consultas sobre séries, exibindo informações detalhadas como título, gênero, descrição e outros dados relevantes.

## Funcionalidades

- **Consulta de Séries**: Pesquise séries pelo título e obtenha informações detalhadas.
- **Detalhes das Séries**: Visualize título, gênero, descrição e outros dados relevantes das séries.

## Tecnologias Utilizadas

- **Linguagem de Programação**: Java
- **Framework**: Spring Boot
- **Gerenciamento de Dependências**: Maven
- **Banco de Dados**: PostgreSQL

## Pré-requisitos

Antes de executar o projeto, é necessário ter as seguintes ferramentas instaladas:

- **Java 11 ou superior** 
- **Maven**
- **PostgreSQL** instalado e configurado

## Como Executar

### 1. Clone o repositório:
```bash
git clone https://github.com/Berla1/screen-match.git
```
### 2. Navegue até o diretório do projeto:
```bash
cd screen-match
```
### 3. Configure o banco de dados:
- #### Crie um banco de dados no PostgreSQL:
```bash
CREATE DATABASE screen_match;
```

- #### Atualize as configurações de acesso ao banco de dados no arquivo `application.properties`:
 ```bash
   spring.datasource.url=jdbc:postgresql://localhost:5432/screen_match
   spring.datasource.username=seu_usuario
   spring.datasource.password=sua_senha
  ```

## Estrutura do Projeto
- **/src**: Contém os arquivos principais do código-fonte da aplicação.
- **/resources**: Contém configurações e arquivos auxiliares.
Screen Match é uma ferramenta prática para realizar consultas de séries, fornecendo informações detalhadas para os usuários.









