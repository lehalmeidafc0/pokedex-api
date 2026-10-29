# Roteiro da apresentação (uns 5 minutos)

## Antes de começar

- Uns 5 minutos antes, abrir https://pokedex-rm569415-d0864.containers.snapdeploy.app/pokedex para a API "acordar" (no plano grátis ela dorme depois de 15 minutos parada).
- Deixar aberto o Insomnia com a coleção **API Pokedex** no ambiente **Deploy (SnapDeploy)**.
- Deixar aberto o Front-End: https://pokedex-rm569415-d0864.containers.snapdeploy.app
- Deixar o projeto aberto no IntelliJ.
- Rodar o `findAll` uma vez e anotar o código de algum Pokémon para usar nas outras requisições.

## 1. Falar rapidinho do projeto (30 segundos)

> "Fiz a Pokedex do Praticando. É uma API em Java com Spring Boot que faz o CRUD de Pokémons no Oracle da FIAP. Ela está publicada na internet, então tudo que eu vou mostrar está rodando a partir do deploy, não do meu computador."

Mostrar no IntelliJ os pacotes:

- `to`: o `PokemonTO`, que representa o Pokémon (nome, tipo, altura, peso e data da captura) e tem as validações.
- `dao`: o `PokemonDAO` com o SQL e a `ConnectionFactory`, que pega o usuário e a senha do banco das variáveis de ambiente.
- `bo`: o `PokemonBO`, com as regras de negócio.
- `resource`: o `PokemonResource` com os endpoints e o `CorsFilter`.

Se perguntarem sobre o deploy:

> "O professor usou o Render, mas ele começou a pedir cartão de crédito. Então publiquei no SnapDeploy, que é de graça. O processo é o mesmo: GitHub, Dockerfile e variáveis de ambiente."

## 2. Insomnia (2 minutos)

Mostrar que a URL do ambiente é a do deploy e rodar nesta ordem:

1. **findAll** (GET `/pokedex`): "Aqui ele lista tudo que está no banco." → **200**
2. **save** (POST `/pokedex`): "Mando o JSON no corpo e ele cadastra. Repara que já volta com o código que o Oracle gerou." → **201**
3. **save (inválido)**: "Agora com nome vazio, altura negativa e data no futuro. O Bean Validation barra e diz o que está errado em cada campo." → **400**
4. **findByCodigo** (GET `/pokedex/{codigo}`): "Busca pelo código que vai na URL, com o `@PathVariable`." Depois trocar por um código que não existe para mostrar o **404**. → **200** / **404**
5. **update** (PUT `/pokedex/{codigo}`): "Atualizo o Pokémon, por exemplo o Pikachu evoluindo para Raichu." → **201**
6. **delete** (DELETE `/pokedex/{codigo}`): "Apaga. Se eu rodar de novo, como ele não existe mais, volta 404." → **204** / **404**

## 3. Front-End (1 minuto e meio)

Abrir o link do Front-End e:

- mostrar a lista carregando (GET);
- cadastrar um Pokémon pelo formulário (POST), ele aparece na lista na hora;
- tentar cadastrar com a data no futuro para aparecer a mensagem de erro (400);
- clicar em **Editar**, mudar alguma coisa e salvar (PUT);
- buscar pelo código e depois **Excluir** (DELETE);
- mostrar o quadro **Requisições feitas**, que mostra cada requisição e o status que voltou.

## 4. Fechar (30 segundos)

> "Então é isso: a API está no ar, conectada no Oracle da FIAP, com a senha em variável de ambiente e não no código, com validação dos dados, CORS liberado para o Front-End e todas as operações do CRUD funcionando."

## Se der algum problema na hora

- **Demorou na primeira requisição:** é a API acordando, espera uns segundos e tenta de novo.
- **Deu erro 500 ou a lista veio vazia:** conferir as variáveis `DB_URL`, `DB_USER` e `DB_PASSWORD` no painel do SnapDeploy.
- **Deu 404 no findByCodigo, update ou delete:** o código da URL não existe. Roda o `findAll` e pega um código que exista.
