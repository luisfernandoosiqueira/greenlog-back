
# Relatório Técnico — Sistema GreenLog Solutions

---

## Introdução

Este relatório técnico apresenta a arquitetura, os principais componentes, funcionalidades e decisões técnicas do sistema GreenLog, voltado à logística urbana de coleta seletiva. O documento é destinado a desenvolvedores, mantenedores, avaliadores e demais interessados na compreensão do modelo implementado, rastreabilidade dos requisitos, padrões utilizados, na política de versionamento e em suas baselines evolutivas.

GreenLog foi desenvolvido utilizando as melhores práticas do mercado, aplicando camadas bem definidas ― backend com Spring Boot (Java), frontend em Angular (TypeScript) ― e promovendo modularidade, escalabilidade e segurança.

---

## Backend

### Controllers

Os controllers expõem a API REST, sendo a interface entre frontend e backend.

- **AuthController**  
  Gerencia autenticação dos usuários, incluindo login e validação de credenciais.
- **BairroController**  
  Manipulação dos bairros cadastrados: criação, consulta, alteração, remoção.
- **CaminhaoController**  
  Gerenciamento dos caminhões, operações CRUD.
- **ItinerarioController**  
  Planejamento e gerenciamento dos itinerários de coleta.
- **MotoristaController**  
  Gestão dos motoristas do sistema.
- **PontoColetaController**  
  Manipulação dos pontos de coleta, cadastro e edição.
- **RotaController**  
  Gerenciamento e planejamento de rotas.
- **RuaConexaoController**  
  Gestão das conexões viárias (ruas) entre os bairros.
- **UsuarioController**  
  Administração dos usuários.

*Observação*: Cada controller expõe endpoints para as entidades correspondentes via DTO, realizando validações básicas e delegando regras complexas para os services.

### Services

Serviços centralizam regras de negócio, validações e integrações entre dados e lógica:

- **AuthService** — Lógica de autenticação, geração/validação de tokens.
- **BairroService** — Operações e validações relacionadas aos bairros.
- **CaminhaoService** — Gerenciamento dos caminhões.
- **DijkstraService** — Implementa o algoritmo de Dijkstra para rotas otimizadas.
- **GrafoService** — Operações sobre o grafo viário da cidade.
- **ItinerarioPadraoService** — Gestão de itinerários padrão, que são rotas pré-definidas para otimizar processos de coleta recorrente.
- **ItinerarioService** — Criação e consulta de itinerários personalizados.
- **MotoristaService** — Cadastro/consulta/atualização dos motoristas.
- **PontoColetaService** — CRUD dos pontos de coleta.
- **RotaService** — Gerencia as rotas, realizando cálculos, persistência e validações das trajetórias de coleta/CRUD das rotas.
- **RuaConexaoService** — Gerenciamento das ligações viárias.
- **UsuarioService** — Administração dos usuários do sistema.

### Entities

Cada entidade representa uma tabela do domínio:

- **tb_bairro**: `id`, `nome`.
- **tb_caminhao**: `id`, `placa`, `capacidade_kg`, `mororista_cpf`.
- **tb_caminhao_tipo_residuo**: `caminhao_id`, `tipo_residuo_id`, `tipo_residuo`.
- **tb_itinerario**: `id`, `data_agendamento`, `rota_id`, `distancia_total`, `ativo`.
- **tb_motorista**: `cpf`, `nome`, `data`, `status`, `telefone`.
- **tb_ponto_coleta**: `id`, `email`, `endereco`, `horario_funcionamento`, `nome`, `quantidade_residuos_kg`, `responsavel`, `telefone`, `bairro_id`, `hora_entrada`, `hora_saida`.
- **tb_ponto_coleta_tipo_residuo**: `ponto_coleta_id`, `tipo_residuo_id`, `tipo_residuo`.
- **tb_rota**: `id`, `data_criacao`, `distancia_total`, `nome`, `peso_total`, `caminhao_plava`, `tipo_residuo_id`, `tipo_residuo`.
- **tb_rota_ponto_coleta**: `rota_id`, `ponto_coleta_id`.
- **tb_ruaConexao**: `id`, `distancia_km`, `bairro_destino_id`, `bairro_origem_id`.
- **tb_tipo_residuo**: `id`, `tipo_residuo`.
- **tb_trecho_rota**: `id`, `destino_bairro`, `origem_bairro`, `rota_id`.
- **tb_trecho_rua_conexao**: `trecho_id`, `rua_conexao_id`.
- **tb_usuario**: `id`, `perfil`, `senha_hash`, `username`.

### Repositórios

É utilizado JPA para CRUD e queries customizadas por entidade:

- BairroRepository, CaminhaoRepository, ItinerarioRepository, MotoristaRepository,
- PontoColetaRepository, RotaRepository, RuaConexaoRepository, TipoResiduoModelRepository,
- TrechoRotaRepository, UsuarioRepository

### Arquivos e Configuração

- **application.properties**: Configurações do backend Spring Boot: datasource, logs, JPA.
- **pom.xml**: Gerenciamento de dependências Maven.

### Banco de Dados

A estrutura do banco de dados é gerenciada via configuração automática do JPA/Hibernate no `application.properties`.
O backend Spring cria as tabelas automaticamente de acordo com as entidades mapeadas no sistema.

Principais tabelas: tb_bairro, tb_caminhao, tb_motorista, tb_ponto_coleta, tb_rota, tb_itinerario, tb_rua_conexao, tb_tipo_residuo, tb_trecho_rota, tb_usuario.

---

## Frontend

### Segurança — Guards, AuthService

- **admin.guard.ts** — Restrição para perfis ADMIN.
- **auth.guard.ts** — Exige autenticação para rotas restritas.
- **operador.guard.ts** — Restringe acesso por perfil.
- **unsaved-changes.guard.ts** — Protege contra navegação acidental.
- **auth.service.ts** — Autenticação, sessão, perfis; integração com endpoints do backend.

### Components

- **box-bairro, nav-bar, nova-rota, nova-rua, novao-itinerario, novo-bairro, novo-caminhao, novo-motorista, novo-ponto**  
  *Componentes reutilizáveis e formulários para cadastro/edição das entidades principais.*

### Services

- Cada service comunica as entidades com o backend via HTTP:
  - bairro.service.ts, caminhao.service.ts, conexao.service.ts, itinerario.service.ts, motorista.service.ts, pontoColeta.service.ts, rota.service.ts

### Rotas

Definição clara dos fluxos via `app.routes.ts`, protegidos por guards.  
Exemplo:
- `/login`, `/home`, `/cadastro-usuario`, `/motorista`, `/caminhao`, `/bairro`, `/conexao`, `/rota`, `/itinerario`.

### Pages

Cada página agrupa UI e lógica de negócio:

- **bairro-ponto.page** — Gerencia bairros e pontos de coleta.
- **cadastro-usuario.page** — Cadastro de novos usuários.
- **caminhao.page** — Gerenciamento de caminhões.
- **conexao.page** — Gestão das conexões viárias.
- **home.page** — Dashboard principal.
- **itinerario.page** — Planejamento e gestão de itinerários.
- **login.page** — Autenticação do usuário.
- **motorista.page** — Gerenciamento dos motoristas.
- **rota.page** — Planejamento das rotas de coleta.

---

## Arquitetura e Configurações

- **Backend:** Java + Spring Boot, camadas (Controller, Service, Repository, Entity, DTO, Mapper, Exceptions, Patterns). Build via Maven (`pom.xml`).
- **Frontend:** Angular + TypeScript, camadas (pages, components, services, guards, interfaces), configuração via `angular.json`, ambiente em `environment.ts`/`package.json`.

SPA (Single Page Application): frontend consome backend RESTful. Separação backend/frontend garante escalabilidade e evolução independente.

---

## Baselines GreenLog

**BL0 — Inicial**  
**Objetivo:**  
- Marcar o início do projeto, com estrutura mínima validada e escopo aprovado.

**Composição:**
- Estrutura de diretórios dos projetos (*backend* e *frontend*), conforme diagramas apresentados.
- Dependências mínimas configuradas (Spring Boot, Angular, etc.).
- Arquivos de build presentes (`pom.xml`, `package.json`, `angular.json`).
- Configurações iniciais de ambiente:
  - `application.properties` no backend.
  - `environment.ts` no frontend.
- README.md inicial (descrição do projeto, comandos básicos de build/run).
- Versionamento no controle de código-fonte (GitProvider, ex: GitHub).

**Critérios de estabilidade para BL0:**
- Projeto compila sem erros (build backend e frontend OK).
- Estrutura de pastas e arquivos corresponde ao modelo aprovado.
- Todos scripts de configuração (build/config/start) presentes.
- Documentação mínima criada (README, diagrama de arquitetura inicial).
- Nenhuma funcionalidade implementada.


**BL1 — Funcional**  
**Objetivo:**  
- Primeiro ponto de funcionalidade implementada e validada, servindo como referência para evolução.

**Composição:**
- Backend:
  - Controllers, services, repositories, entities, DTOs estruturados e em funcionamento.
  - API REST funcional para os principais domínios (Bairro, Motorista, Caminhão, Itinerário, Rota, Usuário, etc.).
  - Mappers, exceptions, patterns implementados conforme necessidade das regras.
  - Autenticação básica ativa.
  - Testes unitários (mínimos, nas principais regras).
  - Banco de dados operacional, scripts/migrations básicos.

- Frontend:
  - Estrutura de pages, components, services, guards, models funcional.
  - Rotas principais implementadas (login, home, cadastro, gerenciamento).
  - Integração com backend via HTTP (CRUD para entidades principais).
  - Validação de formulário, alertas, autenticação e controle de sessão.
  - Responsividade básica.
  - Arquivos de ambiente configurados para API real ou mock.

- Documentação:
  - README.md atualizado (instruções de uso, endpoints API, visão geral das telas).
  - Diagrama de arquitetura atualizado com base no implementado.
  - Registro das decisões técnicas relevantes.

**Critérios de estabilidade para BL1:**
- Funcionalidades principais testadas end-to-end (mínimo CRUD para entidades principais).
- API REST estável para escopo definido (sem bugs críticos, sem endpoints quebrados).
- Frontend navegável, formulários, lista e ações de cadastro/edição funcionando.
- Login, autenticação e controle de acesso ativos.
- Todas dependências configuradas, builds realizados sem erros ou warnings bloqueantes.
- Deploy local/review comprovado por equipe técnica.


Critérios: builds sem erro, funcionalidades testadas end-to-end, documentação/review atualizada.

---

## Política de Versionamento

Adotado [Semantic Versioning (SemVer)](https://semver.org/): **MAJOR.MINOR.PATCH**

- **1.0.0** — Primeira versão funcional estável do back-end publicada.
- **1.1.0** — Adição de novas features: ajustes em DTOs, entidades, services, eventos e utils.
- **1.1.1** — Adição de novas features: implementação do controller e outros ajustes.
- **1.2.0** — Adição de novas features: implementar validação de CPF, ajuste na persistência do tipo de resíduo e separação de horário em ponto de coleta.
- **1.2.1** — Adição de novas features: implementação de alguns regex nas classes RequestDTO.
- **1.2.2** — Adição de novas features:  implementação de alguns regex nas classes RequestDTO.
- **1.3.0** — Adição de novas feature: Ajuste nas regras de negócios.
- **1.3.1** — Adição de novas features: Implementa validações em MotoristaService (+18) e RuaConexaoService (Impede conexão com mesma origem e destino).
- **2.0.0** — Primeira versão funcional estável do front-end publicada.
- **2.1.0** — Adição de novas features: implementação do guard e sweetalert2.
- **2.1.1** — Adição de novas features: Ajustes na atualização de bairros e ponto de coletas.
- **2.2.1** — Adição de novas features: Aplicação de documentação técnica.

---

## Requisitos

## Requisitos Funcionais

- **RF 1:** O sistema deve permitir a entrada de dados a partir da leitura de uma base de dados em arquivo no formato CSV.
- **RF 2:** Os bairros devem ser modelados e registrados no sistema como os vértices do grafo que representa a cidade.
- **RF 3:** O sistema deve permitir o cadastro e gerenciamento das ligações viárias (ruas), que são as arestas ponderadas (com distância em km) que conectam os bairros.
- **RF 4:** O sistema deve ser capaz de carregar os dados dos bairros a partir do arquivo fornecido (Bairros.csv) e inseri-los no banco de dados relacional.
- **RF 5:** Permitir o cadastro e gerenciamento de pontos de coleta de resíduos recicláveis (vértices).
- **RF 6:** Permitir o cadastro e gerenciamento das distâncias entre os pontos de coleta (arestas ponderadas).
- **RF 7:** Permitir descobrir, a qualquer momento, o caminho mais curto entre dois pontos da cidade, otimizando o consumo de combustível e tempo.
- **RF 8:** Permitir cadastrar os caminhões da empresa que irão executar as rotas planejadas para recolhimento da coleta seletiva.
- **RF 9:** Permitir manter um itinerário mensal para cada caminhão explicitando quais pontos de coleta ele visitou (ou irá visitar no mês), a rota traçada e a distância total até o ponto de coleta.
- **RF 10:** Permitir cadastrar e gerenciar caminhões.
- **RF 11:** Registrar para cada caminhão: Identificação (placa), Nome do motorista responsável, Capacidade máxima de carga, e Tipos de resíduos que o caminhão está habilitado a transportar.
- **RF 12:** Permitir cadastrar e gerenciar pontos de coletas.
- **RF 13:** Registrar para cada ponto de coleta: Nome do ponto de coleta, nome do responsável pelo ponto de coleta, informações de contato do responsável pelo ponto de coleta e listar os tipos de resíduos aceitos naquele ponto.
- **RF 14:** Criar rotas calculando o caminho mais curto entre dois pontos da cidade usando o algoritmo de Dijkstra.
- **RF 15:** Montar rotas contendo: uma sequência de ruas e bairros, respeitando a distância otimizada para atingir o ponto de coleta. Distância total do percurso, e tipos de resíduos atendidos.
- **RF 16:** Salvar essas rotas para reaproveitamento, facilitando o planejamento recorrente.
- **RF 17:** Criar itinerários associando uma rota previamente cadastrada, respeitando critérios de carga e tipos de resíduos, a um dia específico e a um caminhão, desde que este atenda aos requisitos de capacidade e tipo de resíduo.
- **RF 18:** Evitar sobreposição de rotas no mesmo dia para um mesmo caminhão.
- **RF 19:** Exibir uma visualização mensal organizada, com os dias e os respectivos itinerários programados.
- **RF 20:** Manter um itinerário mensal para cada caminhão (pontos visitados, rota traçada e distância total).
- **RF 21:** Permitir editar, excluir ou reagendar itinerários conforme disponibilidade.
- **RF 22:** Exibir uma visualização mensal organizada, com os dias e os respectivos itinerários programados.
- **RF 23:** Permitir a visualização do cronograma por: Caminhão, Data, e Ponto de coleta.
- **RF 24:** Permitir consultas avançadas, incluindo:
    - Consultar todos os pontos de coleta filtrando por tipo de resíduo ou bairro.
    - Buscar rotas entre dois pontos com visualização da sequência e distância total.
    - Listar todos os caminhões e seus tipos de resíduos compatíveis.
    - Consultar o cronograma mensal de um caminhão específico.
    - Listar os itinerários programados para um determinado dia.
    - Obter o histórico de coletas em um ponto específico.
- **RF 25:** Possuir uma funcionalidade de autenticação de usuários por login e senha.


## Requisitos Não Funcionais

- **RNF 1:** O sistema deverá ser obrigatoriamente na linguagem JAVA.
- **RNF 2:** As regras de negócio e conexão com o banco de dados deverão ser feitas em JAVA.
- **RNF 3:** Uso de um banco de dados relacional (PostgreSQL ou MySQL preferencialmente).
- **RNF 4:** Estruturação da API REST, camadas do sistema (Controller, Service, Repository) e integração com o banco usando Spring Boot.
- **RNF 5:** Desenvolvimento do frontend em TypeScript e Angular.
- **RNF 6:** O código-fonte completo deve ser organizado e versionado em repositório público no GitHub.
- **RNF 7:** Aplicar no mínimo 5 padrões de projeto.
- **RNF 8:** Otimizar o uso de combustível e reduzir tempo de deslocamento.
- **RNF 9:** Garantir que todos os pontos de coleta recebam atendimento dentro de um cronograma bem estruturado.


## Regras de Negócio

- **RN 1:** Senhas devem ser armazenadas de forma segura (hash, nunca em texto plano).
- **RN 2:** Usuários não autenticados não poderão acessar nenhuma funcionalidade do sistema.
- **RN 3:** Cada ponto de coleta deve possuir nome único, responsável identificado, meios de contato e endereço completo.
- **RN 4:** Não é permitida a duplicidade de nome de ponto.
- **RN 5:** O ponto deve estar associado a um ou mais tipos de resíduos.
- **RN 6:** O ponto só pode ser incluído em uma rota se houver caminhão compatível com todos os tipos de resíduo aceitos.
- **RN 7:** Cada caminhão deve possuir placa única.
- **RN 8:** Cada caminhão deve possuir placa única, capacidade máxima (peso ou volume), motorista designado e lista de tipos de resíduos que pode transportar.
- **RN 9:** Um caminhão só pode ser designado a uma rota se estiver habilitado para todos os resíduos presentes nos pontos dessa rota.
- **RN 10:** Um caminhão não pode ser designado a mais de um itinerário no mesmo dia.
- **RN 11:** Uma rota é composta por uma sequência ordenada de pontos de coleta.
- **RN 12:** O sistema deve validar que o caminhão está disponível na data e suporta todos os tipos de resíduos da rota.
- **RN 13:** Um itinerário é uma rota agendada para um caminhão em uma data específica. Um caminhão não pode ter dois itinerários no mesmo dia.
- **RN 14:** O sistema deve impedir a sobreposição de itinerários por caminhão no mesmo dia.
- **RN 15:** Toda operação que envolva rotas, itinerários ou planejamento deve validar a compatibilidade entre caminhão e tipos de resíduos da rota e a disponibilidade do caminhão na data.
- **RN 16:** Entidades relacionadas (ex: caminhão em rota, ponto em itinerário) não podem ser excluídas enquanto estiverem em uso ativo.
- **RN 17:** Qualquer tentativa de violar uma regra de negócio deverá ser bloqueada com mensagem clara ao usuário.


## Matriz de Rastreabilidade

**Requisitos Funcionais**

RF 1. Entrada de dados via CSV  
&nbsp;&nbsp;• Backend: CsvLoaderService.java  
RF 2. Bairros como vértice de grafo  
&nbsp;&nbsp;• Backend: BairroController.java, BairroService.java, GrafoService.java  
&nbsp;&nbsp;• Frontend: model/Bairro.ts, pages/bairro-ponto  
RF 3. Cadastro de ligações/ruas/arestas  
&nbsp;&nbsp;• Backend: RuaConexaoController.java, RuaConexaoService.java  
&nbsp;&nbsp;• Frontend: model/Rua.ts, pages/conexao  
RF 4. Carregar bairros do arquivo  
&nbsp;&nbsp;• Backend: CsvLoaderService.java, BairroRepository.java  
RF 5/RF 12. Cadastro de pontos de coleta  
&nbsp;&nbsp;• Backend: PontoColetaController.java  
&nbsp;&nbsp;• Frontend: model/PontoColeta.ts, pages/bairro-ponto  
RF 6. Cadastro distâncias entre pontos  
&nbsp;&nbsp;• Backend: RuaConexaoController.java  
&nbsp;&nbsp;• Frontend: model/Rua.ts, pages/conexao  
RF 7/RF 14. Caminho mais curto (Dijkstra)  
&nbsp;&nbsp;• Backend: DijkstraService.java, GrafoService.java, RotaService.java  
&nbsp;&nbsp;• Frontend: services/rota.service.ts, pages/rota  
RF 8/RF 10/RF 11. Cadastro/gestão de caminhões  
&nbsp;&nbsp;• Backend: CaminhaoController.java, CaminhaoService.java  
&nbsp;&nbsp;• Frontend: model/Caminhao.ts, pages/caminhao  
RF 9/RF 15/RF 16. Itinerário mensal, rotas salvas e gestão  
&nbsp;&nbsp;• Backend: ItinerarioController.java, RotaController.java  
&nbsp;&nbsp;• Frontend: model/Itinerario.ts, pages/itinerario  
RF 13. Detalhamento de ponto de coleta  
&nbsp;&nbsp;• Backend: PontoColeta.java  
&nbsp;&nbsp;• Frontend: model/PontoColeta.ts  
RF 17-RF 24. Compatibilidade, cronograma, consultas  
&nbsp;&nbsp;• Backend: ItinerarioService.java, RotaService.java  
&nbsp;&nbsp;• Frontend: pages/itinerario, pages/rota, buscas/filtros  
RF 25. Autenticação  
&nbsp;&nbsp;• Backend: AuthController.java, HashUtil.java  
&nbsp;&nbsp;• Frontend: auth.service.ts, guards

*Requisitos Não Funcionais e Regras de Negócio*  
- **RNF 1, 2, 3, 4** - Backend Java, banco relacional, Spring Boot RESTful:  - *Presente em toda a estrutura backend (app/*.java, application.properties).*
- **RNF 5, 6** - Frontend Angular/TypeScript, Versionamento público GitHub.  *Presente em toda a estrutura frontend, package.json, environment.ts, versionamento GitHub.*
- **RNF 7** —  Mínimo de 5 patterns de projeto (Singleton, Factory, Adapter, Template Method, Mapper).
- **RNF 8, 9** — Otimização de rotas/cronograma de atendimento, *GrafoService.java, DijkstraService.java, ItinerarioService.java, frontend nos módulos de itinerário e rota.*
- **RN 1, 2** —Segurança: senha hash `HashUtil.java, AuthService.java`, Usuário autenticado: `AuthGuard.java`, guards no Angular guards, Placa e ponto únicos: `validadores em services`, controllers e formulário/angular validators.

---

## RFC — Mudança Formal Implementada

**RFC-2024-002: Implementação de 5 Padrões de Projeto**  
Solicitada modernização arquitetural do projeto do último semestre: Singleton (`DijkstraSingleton.java`), Factory (`RotaFactory.java`), Adapter (`CalculadoraRotaAdapter.java`), Template Method (`AbstractItinerarioTemplate.java`), Mapper (`BairroMapper.java`, etc.), resultando em modularidade, testabilidade, manutenção e cumprimento do requisito não-funcional RNF 7.

**Versão aplicada:**  
- Major: `2`, Minor: `0`, Patch: `0` — Mudança estrutural.

**Impacto:**  
- **Backend:**  
  - Inclusão das classes mencionadas em `app.patterns` e `app.mapper`, além de revisões em outros serviços e controllers para utilizar tais padrões.
  - Melhor organização do código, redução de duplicidade, separação de lógica de apresentação e integração.
  - Facilitação de testes e futuras extensões.
- **Frontend:**
  - Potencial para adoção ou simulação de padrões em services/components conforme Angular (ex: Adapter, Facade - para services de API e apresentação).
  - Maior clareza na comunicação entre camadas, melhor rastreabilidade por modelos.

Status: Implantada e validada em BL1 (funcional) GreenLog, semestre corrente.  
Autores: Luiz Fernando, Pedro Spíndola, Ozeias Campos, Henrique Carvalho.

---

## Estrutura do Projeto (conceitual)
```
greenlog/
├── backend/
│   ├── src/main/java/com/seuprojeto/greenlog/
│   │   ├── controller/
│   │   │   ├── BairroController.java
│   │   │   ├── CaminhaoController.java
│   │   │   ├── MotoristaController.java
│   │   │   ├── ItinerarioController.java
│   │   │   ├── RotaController.java
│   │   │   ├── PontoColetaController.java
│   │   │   └── UsuarioController.java
│   │   ├── service/
│   │   │   ├── BairroService.java
│   │   │   ├── CaminhaoService.java
│   │   │   ├── MotoristaService.java
│   │   │   ├── ItinerarioService.java
│   │   │   ├── RotaService.java
│   │   │   ├── PontoColetaService.java
│   │   │   └── UsuarioService.java
│   │   ├── repository/
│   │   │   ├── BairroRepository.java
│   │   │   ├── CaminhaoRepository.java
│   │   │   ├── MotoristaRepository.java
│   │   │   ├── ItinerarioRepository.java
│   │   │   ├── RotaRepository.java
│   │   │   ├── PontoColetaRepository.java
│   │   │   └── UsuarioRepository.java
│   │   ├── model/entity/
│   │   │   ├── Bairro.java
│   │   │   ├── Caminhao.java
│   │   │   ├── Motorista.java
│   │   │   ├── Itinerario.java
│   │   │   ├── Rota.java
│   │   │   ├── PontoColeta.java
│   │   │   └── Usuario.java
│   │   └── exception/
│   │       ├── BusinessException.java
│   │       ├── ResourceNotFoundException.java
│   │       └── GlobalExceptionHandler.java
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── (migrations, scripts SQL)
│   ├── src/test/java/
│   │   └── (testes unitários)
│   └── pom.xml
│
└── frontend/
    ├── src/app/
    │   ├── app.routes.ts
    │   ├── app.config.ts
    │   ├── alert/
    │   │   ├── alert.component.ts
    │   │   ├── alert.component.html
    │   │   ├── alert.component.scss
    │   │   └── alert.service.ts
    │   ├── auth/
    │   │   ├── admin.guard.ts
    │   │   ├── auth.guard.ts
    │   │   ├── operador.guard.ts
    │   │   ├── unsaved-changes.guard.ts
    │   │   └── auth.service.ts
    │   ├── components/
    │   │   ├── box-bairro/
    │   │   ├── box-itinerario/
    │   │   ├── footer/
    │   │   ├── nav-bar/
    │   │   ├── nova-rota/
    │   │   ├── nova-rua/
    │   │   ├── novao-itinerario/
    │   │   ├── novo-bairro/
    │   │   ├── novo-caminhao/
    │   │   ├── novo-motorista/
    │   │   └── novo-ponto/
    │   ├── model/
    │   │   ├── enums/
    │   │   │   ├── StatusCaminhao.ts
    │   │   │   ├── StatusMotorista.ts
    │   │   │   └── TipoResiduo.ts
    │   │   ├── Bairro.ts
    │   │   ├── Caminhao.ts
    │   │   ├── Itinerario.ts
    │   │   ├── Motorista.ts
    │   │   ├── PontoColeta.ts
    │   │   ├── Rota.ts
    │   │   ├── Rua.ts
    │   │   └── Trecho.ts
    │   ├── pages/
    │   │   ├── bairro-ponto/
    │   │   ├── cadastro-usuario/
    │   │   ├── caminhao/
    │   │   ├── conexao/
    │   │   ├── home/
    │   │   ├── itinerario/
    │   │   ├── login/
    │   │   ├── motorista/
    │   │   └── rota/
    │   ├── services/
    │   │   ├── bairro.service.ts
    │   │   ├── caminhao.service.ts
    │   │   ├── conexao.service.ts
    │   │   ├── itinerario.service.ts
    │   │   ├── motorista.service.ts
    │   │   ├── pontoColeta.service.ts
    │   │   └── rota.service.ts
    │   ├── styles/
    │   │   ├── cor_modelo.scss
    │   │   └── styles.scss
    │   ├── app.component.ts
    │   ├── app.component.html
    │   ├── app.component.scss
    │   └── main.ts
    ├── environments/
    │   ├── environment.ts
    │   └── environment.prod.ts
    ├── index.html
    ├── angular.json
    ├── package.json
    └── package-lock.json
```

## Considerações Finais

O GreenLog é resultado de práticas modernas de desenvolvimento, com rápida rastreabilidade entre requisitos, código e documentação. Padrões adotados, separação de responsabilidades, uso de Baselines e controle de versão promovem confiabilidade e evolução contínua para necessidades reais da logística urbana.

```
