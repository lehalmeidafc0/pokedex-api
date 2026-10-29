# Roteiro da apresentação – API Pokedex (≈ 5 minutos)

**Antes de começar (5 min antes):**
- Abra o link do Deploy (`https://pokedex-rm569415-d0864.containers.snapdeploy.app/pokedex`) para "acordar" o serviço.
- Deixe abertos: o Insomnia com a coleção *API Pokedex* no ambiente **Deploy (SnapDeploy)**,
  o Front-End (`https://pokedex-rm569415-d0864.containers.snapdeploy.app`) e o IntelliJ com o projeto.
- Confira se existe pelo menos um Pokémon cadastrado e anote o código dele.

---

### 1. Apresentação rápida (30 s)
> "Essa é a API da Pokedex, feita em Java com Spring Boot. Ela faz o CRUD completo de Pokémons
> num banco Oracle e está publicada na nuvem (SnapDeploy)."

> Se perguntarem: "O professor usou o Render, mas ele passou a exigir cartão de crédito,
> então publiquei no SnapDeploy, que funciona do mesmo jeito: Dockerfile + variáveis de ambiente."

Mostre no IntelliJ a separação em camadas:
- `to` → **PokemonTO**: representa a tabela (nome, tipo, altura, peso, data da captura).
- `dao` → **PokemonDAO**: só o SQL. **ConnectionFactory**: conexão lendo as variáveis de ambiente.
- `bo` → **PokemonBO**: regras de negócio.
- `resource` → **PokemonResource**: os endpoints. **CorsFilter**: libera o Front-End.

### 2. Requisições no Insomnia, pelo Deploy (2 min)
Mostre a URL do Deploy no ambiente e execute, nesta ordem:

| # | Requisição | O que falar | Status esperado |
|---|-----------|-------------|-----------------|
| 1 | **findAll** – GET `/pokedex` | "Lista tudo que está no banco." | **200 OK** |
| 2 | **save** – POST `/pokedex` | "Cadastro via JSON no corpo. Volta o Pokémon já com o código gerado pelo Oracle." | **201 Created** |
| 3 | **save (inválido)** – POST | "Nome vazio, altura negativa e data no futuro: o Bean Validation barra e explica cada erro." | **400 Bad Request** |
| 4 | **findByCodigo** – GET `/pokedex/{codigo}` | "Busca pelo código usando `@PathVariable`." Depois troque por um código que não existe. | **200** / **404** |
| 5 | **update** – PUT `/pokedex/{codigo}` | "Atualiza o Pokémon (Pikachu evoluiu para Raichu)." | **201 Created** |
| 6 | **delete** – DELETE `/pokedex/{codigo}` | "Exclui. Se rodar de novo, como ele não existe mais, volta 404." | **204** / **404** |

### 3. Front-End consumindo a API (1 min 30 s)
Abra o link do Deploy no navegador (`https://pokedex-rm569415-d0864.containers.snapdeploy.app`):
- Mostre a lista de Pokémons (GET).
- Cadastre um novo pelo formulário (POST) — aparece na hora na lista.
- Tente cadastrar com a data no futuro → aparece a mensagem de validação (400).
- Clique em **Editar**, mude algo e salve (PUT).
- Busque por código (GET /{codigo}) e depois **Excluir** (DELETE).
- Aponte o painel **Requisições feitas**: mostra o verbo, a URL e o status de cada chamada.

### 4. Fechamento (30 s)
> "Então a API está publicada na nuvem, conectada no Oracle da FIAP com as credenciais em
> variáveis de ambiente (nada de senha no código), com validação dos dados, CORS liberado para
> o Front-End e todas as operações do CRUD funcionando."

---

**Se algo der errado na hora:**
- *Demora na primeira chamada* → é o container acordando (plano gratuito); espere uns segundos.
- *500 / lista vazia* → confira as variáveis `DB_URL`, `DB_USER`, `DB_PASSWORD` no painel do SnapDeploy.
- *404 no findByCodigo/update/delete* → o código usado na URL não existe; rode o findAll e pegue um válido.
