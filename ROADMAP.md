# Sistema de Gestão Comercial — Roadmap

## Sprint 0 — Setup e Fundamentos
Objetivo: preparar toda a base técnica do projeto.

- Documentar requisitos do MVP
- Modelar banco de dados
- Criar DER
- Criar repositório GitHub
- Criar README inicial
- Criar projeto Spring Boot
- Configurar PostgreSQL
- Configurar Spring Data JPA
- Configurar Bean Validation
- Configurar Swagger/OpenAPI
- Definir estrutura do monólito modular
- Definir padrão de exceptions
- Fazer primeira requisição HTTP
- Organizar `.gitignore`
- Fazer primeiro commit da base
- Enviar base para GitHub

## Sprint 1 — Produtos
Objetivo: construir todo o módulo de produtos.

### Estrutura
- Criar estrutura interna do módulo produto
- Criar Entity Produto
- Criar enum TipoVenda
- Criar ProdutoRepository
- Criar ProdutoDTORequest
- Criar ProdutoDTOResponse
- Criar ProdutoConverter
- Criar ProdutoService
- Criar ProdutoController

### Cadastro
- Implementar POST /produtos
- Adicionar Bean Validation
- Impedir código de barras duplicado
- Criar CodigoBarrasDuplicationException
- Retornar 201 Created
- Testar cadastro pelo Swagger

### Exceptions
- Criar GlobalExceptionHandler
- Retornar 409 Conflict para código de barras duplicado
- Criar ProdutoNaoEncontradoException
- Padronizar corpo das respostas de erro

### Consultas
- Implementar GET /produtos
- Implementar GET /produtos/{id}
- Implementar GET /produtos/codigo-barras/{codigo}

### Atualização
- Implementar PUT /produtos/{id}
- Atualizar dados do produto
- Validar código de barras durante edição

### Desativação
- Implementar desativação de produto
- Não excluir fisicamente o produto do banco

### Finalização
- Revisar módulo Produto
- Testar todos os endpoints pelo Swagger
- Fazer commits intermediários
- Push da branch feature/produto
- Abrir Pull Request feature/produto -> develop

## Sprint 2 — Estoque
Objetivo: controlar entradas, saídas e histórico do estoque.

- Criar módulo estoque
- Criar entidade MovimentacaoEstoque
- Criar enum TipoMovimentacao
- Criar MovimentacaoEstoqueRepository
- Criar DTOs de estoque
- Criar converter de estoque
- Criar EstoqueService
- Criar EstoqueController
- Registrar ENTRADA
- Registrar SAIDA
- Registrar AJUSTE
- Atualizar quantidadeEstoque do Produto
- Impedir estoque negativo
- Configurar estoque mínimo
- Listar produtos com estoque baixo
- Consultar histórico de movimentações
- Filtrar movimentações por produto
- Aplicar transações para manter consistência
- Testar regras pelo Swagger

## Sprint 3 — Vendas
Objetivo: implementar o fluxo completo de vendas.

- Criar entidade Venda
- Criar entidade ItemVenda
- Criar enum StatusVenda
- Criar enum FormaPagamento
- Criar repositories
- Criar DTOs
- Criar converters
- Criar services
- Criar controllers
- Abrir venda
- Adicionar produto pelo código de barras
- Adicionar produto por unidade
- Adicionar produto por peso
- Alterar quantidade
- Remover item
- Calcular subtotal
- Calcular total
- Pagamento em DINHEIRO
- Pagamento via PIX
- Pagamento via CARTAO
- Finalizar venda
- Baixar estoque automaticamente
- Criar movimentação de estoque
- Impedir venda sem estoque suficiente
- Cancelar venda
- Testar fluxo completo

## Sprint 4 — Caixa
Objetivo: controlar abertura, operação e fechamento do caixa.

- Criar entidade Caixa
- Criar enum StatusCaixa
- Criar CaixaRepository
- Criar DTOs
- Criar converter
- Criar CaixaService
- Criar CaixaController
- Abrir caixa
- Informar saldo inicial
- Consultar caixa aberto
- Associar venda ao caixa
- Impedir venda sem caixa aberto
- Totalizar vendas em dinheiro
- Totalizar vendas em PIX
- Totalizar vendas em cartão
- Calcular total vendido
- Fechar caixa
- Registrar valor final
- Consultar histórico de caixas
- Testar fluxo completo

## Sprint 5 — Usuários e Spring Security
Objetivo: implementar autenticação e autorização.

Perfis:
- ADMIN
- FUNCIONARIO

Tarefas:
- Criar entidade Usuario
- Criar enum Perfil
- Criar UsuarioRepository
- Criar DTOs
- Criar UsuarioService
- Configurar Spring Security
- Implementar login
- Armazenar senha com hash
- Criar perfil ADMIN
- Criar perfil FUNCIONARIO
- Proteger endpoints
- Permitir vendas ao FUNCIONARIO
- Restringir operações administrativas ao ADMIN
- Restringir gerenciamento de usuários ao ADMIN
- Ativar/desativar funcionário
- Associar funcionário à venda
- Associar funcionário ao caixa
- Tratar 401 Unauthorized
- Tratar 403 Forbidden
- Testar autorização

## Sprint 6 — Relatórios
Objetivo: entregar informações úteis para o administrador.

- Vendas do dia
- Vendas por período
- Faturamento diário
- Faturamento por período
- Vendas por forma de pagamento
- Produtos mais vendidos
- Produtos com estoque baixo
- Histórico de estoque
- Vendas por funcionário
- Resumo de caixa

## Sprint 7 — Testes e Qualidade
Objetivo: garantir que as principais regras do sistema funcionem corretamente.

- Configurar JUnit 5
- Configurar Mockito
- Testar ProdutoService
- Testar código de barras duplicado
- Testar EstoqueService
- Testar tentativa de estoque negativo
- Testar VendaService
- Testar cálculo de subtotal
- Testar produto por unidade
- Testar produto por peso
- Testar estoque insuficiente
- Testar baixa automática do estoque
- Testar CaixaService
- Testar abertura de caixa
- Testar fechamento de caixa
- Testar totais por forma de pagamento
- Testar permissões principais do Security
- Criar testes de integração
- Testar fluxos críticos
- Revisar cobertura dos casos importantes

## Sprint 8 — Infraestrutura e Produção
Objetivo: preparar e colocar a primeira versão em uso real.

- Criar Dockerfile
- Criar Docker Compose
- Containerizar Spring Boot
- Containerizar PostgreSQL
- Configurar variáveis de ambiente
- Remover credenciais do código
- Configurar backup
- Preparar ambiente de produção
- Configurar HTTPS
- Testar leitor de código de barras
- Testar produtos vendidos por peso
- Testar vendas reais
- Testar abertura e fechamento de caixa
- Validar sistema com o comerciante
- Corrigir bugs encontrados
- Publicar versão v1.0

## Fora do MVP
- Fiado
- Cadastro de clientes
- NFC-e / NF-e
- Múltiplas lojas
- SaaS / Multi-tenancy
- Aplicativo mobile
- Integrações avançadas
