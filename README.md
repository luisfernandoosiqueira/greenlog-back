````markdown
# API — GreenLog PI (Planejamento de Coletas Seletivas)

Aplicação Spring Boot 3 desenvolvida para gerenciar bairros, pontos de coleta, caminhões, motoristas, rotas e itinerários de coletas seletivas, incluindo cálculo de rotas com múltiplas paradas e validação da capacidade de carga dos veículos.

---

## Tecnologias Utilizadas

- Java 17+
- Spring Boot 3  
  - Web  
  - Data JPA  
  - Validation
- PostgreSQL como banco de dados relacional
- Jackson para serialização/desserialização JSON
- OpenAPI / Swagger UI para documentação automática e interativa

---

## Executando o Projeto

### 1. Pré-requisitos

- Java 17 ou superior  
- Maven 3.8+  
- Instância do PostgreSQL disponível (local ou remota)

### 2. Clonar o repositório

```bash
git clone https://github.com/luisfernandoosiqueira/greenlog-back
````

### 3. Configurar o banco PostgreSQL

No arquivo `src/main/resources/application.properties` (ou `application.yml`), configurar:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/greenlog
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### 4. Executar o projeto

```bash
cd greenlog-back
mvn spring-boot:run
```

### 5. Acessar a aplicação

* Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Estrutura do Projeto

```text
src/
 ├── main/
 │   ├── java/app/
 │   │   ├── controller/   → Endpoints REST (bairros, ruas, pontos, caminhões, motoristas, rotas, itinerários, auth)
 │   │   ├── dto/          → Objetos de transferência de dados (requests/responses)
 │   │   ├── entity/       → Entidades JPA (Bairro, RuaConexao, PontoColeta, Caminhao, Motorista, Rota, Itinerario, etc.)
 │   │   ├── enums/        → Enumerações de domínio (StatusCaminhao, StatusMotorista, TipoResiduo, PerfilUsuario)
 │   │   ├── events/       → Eventos de domínio e listeners (Observer)
 │   │   ├── exceptions/   → Exceções de negócio e GlobalExceptionHandler
 │   │   ├── mapper/       → Conversão entre DTOs e Entidades
 │   │   ├── patterns/     → Implementações dos padrões (Adapter, Factory Method, Template Method, etc.)
 │   │   ├── repository/   → Interfaces Spring Data JPA
 │   │   ├── service/      → Regras de negócio (coletas, rotas, itinerários, autenticação)
 │   │   └── utils/        → Utilitários (ex.: HashUtil)
 │   └── resources/
 │       └── application.properties
 └── test/                 → Testes automatizados
```

---

## Padrões de Projeto e Boas Práticas

* Arquitetura em camadas (Controller → Service → Repository)
* Uso de DTOs para isolar a API das entidades JPA
* Tratamento centralizado de erros com GlobalExceptionHandler e `ErrorResponseDTO`
* Documentação automática com OpenAPI / Swagger
* Persistência via Spring Data JPA com PostgreSQL

### Padrões de Projeto Utilizados

* Adapter → abstração do cálculo de rota a partir do algoritmo de Dijkstra
* Factory Method → criação de rotas e trechos de rota a partir dos resultados do cálculo
* Template Method → fluxo padronizado para criação de itinerários (validações, montagem da rota, persistência)
* Observer → eventos de domínio (atualização do grafo, criação de itinerário) tratados por listeners dedicados
* Singleton → serviços de grafo reutilizados para o cálculo de rotas em toda a aplicação

```
```
