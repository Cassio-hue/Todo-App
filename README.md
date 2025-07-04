# Projeto: To-Do List

## Objetivo
Neste vasto mundinho do Java, temos diversos frameworks, e é comum iniciar já usando um. Porém, isso cria um problema: os desenvolvedores que se acostumam a trabalhar assim não fazem ideia do que acontece por debaixo dos panos. Você não será mais um desses. Para começar seu treinamento Jedi, iremos desenvolver um projeto de lista de tarefas (To-Do List) usando apenas coisas básicas neste primeiro momento, como JDBC, Servlet e muito Java puro.

## Configuração do Ambiente

### Bibliotecas Necessárias
1. H2 Database (versão mais recente)
2. Servlet API
3. JSON library (org.json)
4. Jetty Server
5. Jetty Servlet

### Como Adicionar Bibliotecas no IntelliJ IDEA
1. Vá em **File → Project Structure** (Ctrl + Alt + Shift + S)
2. Selecione **Modules → Dependencies → + → JARs or directories**
3. Navegue até o arquivo `.jar` e adicione

## Estrutura do Projeto
O projeto será desenvolvido em fases incrementais para melhor aprendizado:

### Fase 1: Fundamentos Java e JDBC
- Criação das classes básicas (`Task`)
- Implementação do CRUD usando JDBC puro
- Conexão com banco de dados H2 (em memória para desenvolvimento)
- Tratamento de exceções
- Padrão DAO (Data Access Object)

> **Tarefa:** crie uma implementação da `TaskDAO` utilizando JDBC (`TaskDaoJdbc`). Deixei um teste unitário criado (`TaskDaoTest`) para te auxiliar. Você precisará criar a conexão, criar tabelas e manipulá-las.

### Fase 2: Transformar em projeto Maven
- Criar um repositório pessoal no GitHub
- Criar um projeto Maven
- Migrar o código existente para este projeto, declarando dependências:
    - H2
    - JUnit 5
- O que deve funcionar?
    - Compilação via Maven
    - Execução dos testes
    - Utilizar Java 21

### Fase 3: Servlets e Web (listar)
- Configurar o Jetty Server programaticamente no método `main`
- Criar uma Servlet que liste em HTML as tarefas

### Fase 4: Servlets e Web (criar)
- Atualizar a Servlet para permitir adicionar uma nova tarefa

### Fase 5: Servlets e Web (deletar)
- Atualizar a Servlet para permitir deletar uma tarefa

### Fase 6: Servlets e Web (editar)
- Atualizar a Servlet para permitir editar uma tarefa

### Fase 7: MiniServlet MVC
- Implementar uma servlet que encaminhe requisições às páginas certas, ex: `/listar` → `ListarTaskPage.java`
- Retornar uma página 404 caso não haja correspondência

```java
public interface Page {
    String render(Map<String, Object> parameters);
}
```

### Fase 8: Autenticação utilizando Filter
- Implementar um filtro que garanta que o usuário esteja logado (caso contrário, solicitar BASIC Auth)
- Credenciais: `teste:teste`

### Fase 9: Conceitos de Reflection
- Criar anotação `@Rota("/tasks")`
- Fazer scan das classes usando [ClassGraph](https://github.com/classgraph/classgraph)
- Filtrar classes anotadas com `@Rota`
- Usar isso no MiniServer (Fase 7) para despachar as páginas

### Fase 10: Hibernate
- Renomear `TaskDaoImpl` para `TaskDaoJdbc`
- Ler introdução: https://docs.jboss.org/hibernate/orm/7.0/introduction/html_single/Hibernate_Introduction.html
- Instalar e configurar Hibernate (SessionFactory, etc.)
- Criar e implementar `TaskDaoHibernate` (atualizar teste)
- Criar classe `BeanFactory` com factory method estático para criar `TaskDao`

### Fase 11: Primeiro contato com Spring
- Ler overview: https://docs.spring.io/spring-framework/reference/overview.html
- Inicializar manualmente um ApplicationContext do Spring
- Inicializar manualmente o `DispatcherServlet`
- Configurar Thymeleaf
- Reimplementar telas usando Spring MVC
- Separar em dois contextos:
    - `/custom-mvc` (MiniServlet)
    - `/spring-mvc` (SpringMVC com `DispatcherServlet`)
- Implementar controllers, etc.

### Fase 12: Tudo é bean (💀)
- Remover `BeanFactory`
- Declarar `TaskDAO` em uma classe `@Configuration` do Spring com `@Bean`
- Transformar a MiniServlet em bean e injetar `TaskDAO`
- Remover ClassGraph
- Transformar as pages em beans e injetar uma lista de `Page` na MiniServlet
- Criar uma home em `/` com links para as implementações (Spring, Servlet)

### Fase 13: Spring Security (💀💀)
- Remover `AuthFilter`
- Configurar Spring Security
- Habilitar login via formulário (usuário e senha: `teste`)

### Fase 14: Wicket (💀💀💀)
- Instalar e configurar Wicket (via Maven)
- Recriar as tasks usando Wicket e adicionar essa implementação à home

### Fase 15: Cobertura (💀💀)
- Instalar e configurar Jacoco (plugin do Maven)
- Garantir cobertura de testes superior a 60%

### Fase 16: React (💀💀💀💀)
- Instalar e configurar React no projeto (usando `frontend-maven-plugin`)
- O React deve rodar na mesma porta do backend
- Script `watch` no `package.json`
- Implementar a interface de tasks em React e controllers RESTful (Spring MVC)

### Fase 17: Implantação (💀💀)
- Implantar em nuvem gratuita (AWS, Heroku, Azure, etc.)

## Tecnologias Utilizadas
- Java 21
- JDBC
- H2 Database (em memória)
- Servlet API
- Eclipse Jetty
- HTML/CSS
- ClassGraph
- Hibernate
- Spring
- Thymeleaf
- Wicket
- React