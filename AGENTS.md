# AGENTS.md — Sistema de Gestão Comercial

## Objetivo do projeto
Este repositório contém um sistema web de gestão comercial para um pequeno comércio. O objetivo principal é reduzir a lentidão do caixa, automatizando identificação de produtos, cálculo de vendas, atualização de estoque e controle de caixa.

## Papel do Codex neste projeto
O desenvolvedor está aprendendo Java/Spring Boot e quer construir o projeto sozinho.

Ao ajudar:
- Atue como professor/mentor.
- Não entregue a implementação completa de imediato.
- Primeiro explique o conceito, faça perguntas, dê pistas e revise a tentativa do desenvolvedor.
- Ao encontrar erros, explique a causa, como interpretar a mensagem e como investigar.
- Só forneça código completo quando o desenvolvedor pedir explicitamente ou depois de ele tentar.
- Priorize aprendizado, debugging e entendimento das decisões arquiteturais.

## Stack
- Java 17
- Spring Boot
- Maven
- Spring Web / MVC
- Spring Data JPA
- PostgreSQL
- Bean Validation
- Springdoc OpenAPI / Swagger
- Lombok
- Spring Security (Sprint futura)
- JUnit 5 e Mockito (Sprint futura)
- Docker (Sprint futura)

## Arquitetura
Monólito modular, organizado por domínio.

Estrutura principal:

```text
com.gestaocomercial
├── produto
├── estoque
├── venda
├── caixa
├── usuario
├── relatorio
├── config
└── exception
```

Cada módulo pode conter, conforme necessário:

```text
controller/
dto/
  in/
  out/
entity/
enums/
repository/
service/
converter/
```

## Regras arquiteturais
- Controller: recebe/responde HTTP. Não deve concentrar regra de negócio.
- Service: concentra regras de negócio.
- Repository: acesso a dados com Spring Data JPA.
- Entity: representação persistida no banco.
- DTO Request: controla e valida dados de entrada.
- DTO Response: controla dados devolvidos pela API.
- Converter: converte DTO <-> Entity.
- Exceptions específicas representam violações de regras de negócio.
- GlobalExceptionHandler centraliza respostas de erro HTTP.

## Regras do domínio
- Produto pode ser vendido por `UNIDADE` ou `PESO`.
- Valores monetários e quantidades fracionadas usam `BigDecimal`.
- Código de barras é `String` e deve ser único.
- Produto novo nasce ativo.
- Data de cadastro do produto é preenchida automaticamente.
- Formas de pagamento do MVP: DINHEIRO, PIX e CARTAO.
- Fiado não faz parte do MVP.
- Não há cadastro de clientes no MVP.
- Usuários internos futuros: ADMIN e FUNCIONARIO.
- Produto não deve ser excluído fisicamente; deve ser desativado.

## Banco / entidades principais
- Usuario
- Produto
- Venda
- ItemVenda
- MovimentacaoEstoque
- Caixa

Relacionamentos principais:
- Usuario 1:N Venda
- Usuario 1:N Caixa
- Caixa 1:N Venda
- Venda 1:N ItemVenda
- Produto 1:N ItemVenda
- Produto 1:N MovimentacaoEstoque

## Git
Fluxo desejado:

```text
main -> versão estável
develop -> integração
feature/* -> funcionalidades
```

Branch atual do módulo Produto:
`feature/produto`

Preferir commits pequenos e coerentes, por exemplo:
- `feat: adiciona cadastro de produtos`
- `feat: adiciona consultas de produtos`
- `fix: trata codigo de barras duplicado`

## Estado atual
Sprint atual: **Sprint 1 — Produtos**.

Já implementado:
- Entity Produto
- enum TipoVenda
- ProdutoRepository
- ProdutoDTORequest
- ProdutoDTOResponse
- ProdutoConverter
- ProdutoService
- ProdutoController
- POST /produtos
- Bean Validation
- validação de código de barras duplicado
- CodigoBarrasDuplicationException
- retorno 201 Created
- cadastro testado via Swagger

Próximo passo atual:
- Criar `GlobalExceptionHandler`
- Tratar `CodigoBarrasDuplicationException` com HTTP 409 Conflict

Depois:
- GET /produtos
- GET /produtos/{id}
- GET /produtos/codigo-barras/{codigo}
- ProdutoNaoEncontradoException
- PUT /produtos/{id}
- desativação de produto
- revisão/testes Swagger
- PR feature/produto -> develop

## Roadmap
Consulte `ROADMAP.md` para o planejamento completo das Sprints 0 a 8.
