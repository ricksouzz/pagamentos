# API de Pagamentos

Solução para o desafio técnico. O objetivo foi criar uma API REST simples para receber pagamentos e gerenciar a mudança de status, sem muita complexiddae.

## Stack utilizada:
* Java 17 | Spring Boot 4.x

## Como rodar o projeto:
1. Clone o repositório: `git clone https://github.com/ricksouzz/pagamentos.git`
2. Acesse a pasta: `cd pagamentos`
3. Suba a aplicação: `mvn spring-boot:run`

* **API:** `http://localhost:8080`
* **Banco H2:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:pagamentosdb`, usuaário: `sa`, sem senha)

## Endpoints
* `POST /api/pagamentos` - Cadastra um pagamento (Status: PENDENTE_PROCESSAMENTO).
* `PATCH /api/pagamentos/{id}/status` - Atualiza o status (já valida regras de negócio).
* `GET /api/pagamentos` - Lista pagamentos ativos. Aceita filtros: `?codigoDebito=`, `?cpfCnpj=`, `?status=`.
* `DELETE /api/pagamentos/{id}` - Exclusão lógica (apenas se o status for pendente).

## Teste unitário:
Os testes foram criados na camada PagamentoService, utilizando JUnit + Mockito para verificarmos se as regras e validações foram implementadas corretamente. 
Rodar testes: `mvn test`

## Melhorias que poderiam ser aplicadas:
Para estar de acordo com o pedido a respeito da simplicidade e não complexidade, deixei as seguintes melhorias que poderiam melhorar ainda mais as consultas:
* **Criar uma busca dinâmica (unificar os filtros):** Permitir o envio de vários parâmetros na mesma requisição (ex: buscar por cpfCnpj e status simultaneamente). 
* **Usar specifications:** Para evitar poluir o service, a implementação ideal seria utilizar o criteria do JPA através do Specification, deixando as buscas mais flexíveis e fáceis de manter.
