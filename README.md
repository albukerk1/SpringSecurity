# Spring Security Experimentation

Este é um projeto de demonstração para o Spring Boot que implementa o Spring Security para autenticação e autorização de usuários.

## Tecnologias Utilizadas

  * Java 17
  * Spring Boot 3.5.0
  * Spring Security
  * Spring Data JPA
  * Maven
  * H2 Database (banco de dados em memória)
  * Lombok

## Funcionalidades

  * **Autenticação de Usuário:** Sistema de login com e-mail e senha.
  * **Autorização baseada em Roles:** O acesso aos endpoints é restrito com base nas roles do usuário (USER, ADMIN).
  * **Endpoints Públicos e Protegidos:** Alguns endpoints são públicos, enquanto outros exigem autenticação.
  * **Gerenciamento de Usuários (ADMIN):**
      * Visualizar todos os usuários.
      * Visualizar um usuário por ID.
      * Atualizar um usuário por ID.
      * Deletar um usuário por ID.
  * **Gerenciamento de Perfil (USER):**
      * Visualizar o próprio perfil.
      * Atualizar o próprio nome e senha.
  * **Salvar novos usuários:** Endpoint público para registrar novos usuários.

## Endpoints da API

A seguir estão os endpoints disponíveis na aplicação:

| Método HTTP | URL | Autorização | Descrição |
| :--- | :--- | :--- | :--- |
| `GET` | `/` | PÚBLICO | Retorna uma mensagem de boas-vindas. |
| `POST` | `/user/save` | PÚBLICO | Salva um novo usuário. |
| `GET` | `/users/all` | ADMIN | Retorna todos os usuários. |
| `GET` | `/users/me` | ADMIN, USER | Retorna os detalhes do usuário logado. |
| `PUT` | `/users/me` | ADMIN, USER | Atualiza o perfil do usuário logado. |
| `GET` | `/admin/users/{id}` | ADMIN | Retorna um usuário pelo ID. |
| `PUT` | `/admin/users/{id}` | ADMIN | Atualiza um usuário pelo ID. |
| `DELETE` | `/admin/users/{id}` | ADMIN | Deleta um usuário pelo ID. |

## Como Executar o Projeto

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/seu-usuario/springSecurity.git
    ```
2.  **Navegue até o diretório do projeto:**
    ```bash
    cd springSecurity
    ```
3.  **Execute o projeto com o Maven:**
    ```bash
    mvn spring-boot:run
    ```
    A aplicação estará disponível em `http://localhost:3030`.

## Como Usar

1.  **Crie um usuário:**

      * Envie uma requisição `POST` para `http://localhost:3030/user/save` com o seguinte corpo:
        ```json
        {
          "email": "user@example.com",
          "password": "password",
          "name": "User Name",
          "roles": "USER"
        }
        ```
      * Para criar um usuário com privilégios de administrador, defina `"roles": "ADMIN"`.

2.  **Acesse endpoints protegidos:**

      * Utilize as credenciais do usuário criado para se autenticar e acessar os endpoints protegidos. Por exemplo, para obter os detalhes do seu perfil, envie uma requisição `GET` para `http://localhost:3030/users/me` com autenticação básica.

## Documentação de Referência

Para mais referências, por favor, considere as seguintes seções:

  * [Documentação oficial do Apache Maven](https://maven.apache.org/guides/index.html)
  * [Guia de referência do plugin Spring Boot Maven](https://docs.spring.io/spring-boot/3.5.0/maven-plugin)
  * [Spring Web](https://docs.spring.io/spring-boot/3.5.0/reference/web/servlet.html)
  * [Spring Data JPA](https://docs.spring.io/spring-boot/3.5.0/reference/data/sql.html#data.sql.jpa-and-spring-data)
  * [Spring Security](https://docs.spring.io/spring-boot/3.5.0/reference/web/spring-security.html)
