# Segurança e primeiro administrador

O cadastro `POST /usuario` exige `ROLE_ADMIN`. O login `POST /usuario/login` é público. Não existe endpoint público para criar administradores.

## Configuração

Defina `APP_JWT_SECRET` no ambiente antes de iniciar a aplicação. O valor deve ser uma chave aleatória codificada em Base64, com pelo menos 32 bytes após a decodificação. Use o mesmo valor em todas as instâncias da aplicação; trocar a chave invalida os tokens emitidos antes. Não coloque a chave no Git.

Para gerar uma chave no PowerShell:

```powershell
$bytes = [byte[]]::new(32)
[System.Security.Cryptography.RandomNumberGenerator]::Fill($bytes)
$env:APP_JWT_SECRET = [Convert]::ToBase64String($bytes)
```

## Primeiro ADMIN

Com a tabela de usuários vazia, defina temporariamente `APP_BOOTSTRAP_ADMIN_NAME`, `APP_BOOTSTRAP_ADMIN_EMAIL` e `APP_BOOTSTRAP_ADMIN_PASSWORD` no ambiente e inicie a aplicação uma vez. `AdminBootstrap` cria o usuário ADMIN com senha BCrypt. Depois remova as três variáveis e reinicie normalmente. Se faltar algum dos três valores ou já existir usuário, o bootstrap falha sem criar uma conta.

Se o banco já contém usuários, não use esse bootstrap. Escolha um usuário confiável para promover por um procedimento administrativo controlado antes de habilitar o gerenciamento por API.

O ADMIN pode cadastrar outros usuários em `POST /usuario`. O corpo aceita `nome`, `email`, `senha` e `tipoUsuariosEnums` (`ADMIN` ou `FUNCIONARIO`). A senha nunca é devolvida pela API.

## Acesso

- `/v3/api-docs/**`, `/swagger-ui/**` e `/swagger-ui.html`: públicos.
- `POST /usuario/login`: público.
- `/usuario` e `/usuario/**` restantes: ADMIN.
- `/vendas/**` e `/caixa/**`: ADMIN ou FUNCIONARIO.
- Demais rotas: usuário autenticado.

O JWT usa o email como subject e expira em uma hora. O usuário é consultado no banco em cada requisição; desativá-lo impede novos logins e invalida o acesso com tokens ainda não expirados.
