# Pokedex – API Restful com Spring Boot

Projeto do **Praticando** das aulas A39 a A46 (Domain Driven Design Using Java – Prof. Gilberto).
É uma API de cadastro de Pokémons com o CRUD completo, banco Oracle, Bean Validation, CORS,
Front-End e Deploy na nuvem (SnapDeploy).

**API publicada:** <https://pokedex-rm569415-d0864.containers.snapdeploy.app/pokedex>  
**Front-End:** <https://pokedex-rm569415-d0864.containers.snapdeploy.app>

**Endpoint:** `/pokedex`

| Verbo  | URI                 | O que faz                   | Sucesso          | Falha               |
|--------|---------------------|-----------------------------|------------------|---------------------|
| GET    | `/pokedex`          | Lista todos os Pokémons     | 200 OK           | 404 Not Found       |
| GET    | `/pokedex/{codigo}` | Busca um Pokémon pelo código| 200 OK           | 404 Not Found       |
| POST   | `/pokedex`          | Cadastra um Pokémon         | 201 Created      | 400 Bad Request     |
| PUT    | `/pokedex/{codigo}` | Atualiza um Pokémon         | 201 Created      | 400 Bad Request     |
| DELETE | `/pokedex/{codigo}` | Exclui um Pokémon           | 204 No Content   | 404 Not Found       |
| GET    | `/health`           | Health check do Deploy      | 200 OK           | -                   |

Exemplo de JSON:

```json
{
    "nome": "Squirtle",
    "tipo": "Água",
    "altura": 0.5,
    "peso": 9.0,
    "dataDaCaptura": "2025-08-15"
}
```

---

## Estrutura do projeto

```
pokedex/
├── Dockerfile                      -> usado no Deploy (SnapDeploy)
├── pom.xml                         -> dependências (Web, Validation, Oracle, DevTools)
├── sql/criar_tabela.sql            -> script da tabela DDD_POKEMONS
├── json-exemplos/                  -> JSONs de cadastro e atualização (.txt)
├── insomnia/Insomnia_Pokedex.json  -> coleção pronta para importar no Insomnia
└── src/main/
    ├── java/br/com/fiap/
    │   ├── PokedexApplication.java -> método main
    │   ├── to/PokemonTO.java       -> Transfer Object (representa a tabela)
    │   ├── dao/ConnectionFactory.java -> conexão com o Oracle (uma por requisição)
    │   ├── dao/PokemonDAO.java     -> SQL (findAll, findByCodigo, save, update, delete)
    │   ├── dao/CriadorDeTabela.java -> cria a tabela no Oracle se ela não existir
    │   ├── bo/PokemonBO.java       -> regras de negócio
    │   └── resource/
    │       ├── PokemonResource.java   -> controller (endpoints)
    │       ├── CorsFilter.java        -> libera o acesso do Front-End
    │       ├── HealthResource.java    -> GET /health (a plataforma usa para saber se a API está no ar)
    │       └── ValidacaoHandler.java  -> (extra) mensagens claras do Bean Validation
    └── resources/
        ├── application.properties
        └── static/index.html       -> Front-End da Pokedex
```

### O que foi feito em cada Praticando

| Aula | Praticando pedido | Onde está |
|------|-------------------|-----------|
| A39 | Pacotes, `PokemonTO`, `PokemonDAO`, `PokemonBO`, `PokemonResource` | pacotes `to`, `dao`, `bo`, `resource` |
| A40 | Driver Oracle, `ConnectionFactory`, tabela, `save()` nas 3 camadas, validar data da captura no passado, POST no Insomnia | `pom.xml`, `ConnectionFactory`, `sql/`, `save()` |
| A41 | `findAll()` buscando no banco, `findByCodigo()`, Bean Validation, GETs no Insomnia | `PokemonDAO`, `PokemonTO` (`@NotBlank`, `@Past`...) |
| A42 | `delete()` nas 3 camadas, DELETE no Insomnia | `delete()` |
| A43 | `update()` nas 3 camadas, PUT no Insomnia | `update()` |
| A44 | `CorsFilter`, Build, Deploy, Front-End | `CorsFilter`, `Dockerfile`, `static/index.html` |

> Sobre a regra da data (A40): ela foi feita primeiro no `PokemonBO` e, a partir da A41, passou
> para o Bean Validation (`@Past` no `PokemonTO`), igual ao professor fez com o `RemedioBO`.
> O código antigo ficou comentado no BO para mostrar a evolução.

> Sobre a `ConnectionFactory`: segue o mesmo modelo do professor (`getConnection()` e
> `closeConnection()`), mas guarda a conexão em um `ThreadLocal`. Assim cada requisição usa a
> sua própria conexão e, se duas chegarem ao mesmo tempo, uma não fecha a conexão da outra.

---

## Passo 1 – Criar a tabela no Oracle

**Não é obrigatório:** a classe `CriadorDeTabela` cria a tabela `DDD_POKEMONS` sozinha
(com 3 Pokémons de exemplo) na primeira vez que a API sobe, se ela ainda não existir.

Se quiser criar na mão:
1. Abra o **Oracle SQL Developer** e conecte no servidor da FIAP.
2. Abra o arquivo `sql/criar_tabela.sql` e execute tudo (F5).

## Passo 2 – Rodar a API no IntelliJ

1. **File > Open** e escolha a pasta `pokedex`. Espere o Maven baixar as dependências.
   (Se aparecer o ícone do Maven no canto superior direito, clique em **Sync Maven Changes**.)
2. As credenciais do banco **não ficam no código**. Configure as variáveis de ambiente:
   **Run > Edit Configurations... > PokedexApplication > Environment variables** e cole
   (trocando pelo seu RM e senha):
   ```
   DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL;DB_USER=rmXXXXX;DB_PASSWORD=sua_senha
   ```
3. Rode a classe `PokedexApplication`. No console deve aparecer `Tomcat started on port 8080`.
4. Teste no navegador: <http://localhost:8080/pokedex> (JSON) e <http://localhost:8080> (Front-End).

## Passo 3 – Testar no Insomnia

1. No Insomnia: **Import** (ou *Create > Import*) e escolha `insomnia/Insomnia_Pokedex.json`.
2. Vai aparecer a coleção **API Pokedex** com as requisições:
   `findAll`, `findByCodigo`, `save`, `save (inválido - Bean Validation)`, `update` e `delete`.
3. No seletor de ambiente (canto superior esquerdo) escolha **Local** ou **Deploy (SnapDeploy)**.
   O ambiente *Deploy* já aponta para `https://pokedex-rm569415-d0864.containers.snapdeploy.app`.
4. Lembre de ajustar o código na URL (`/pokedex/1`) para um código que exista no seu banco.

## Passo 4 – Build (gerar o .jar)

No terminal do IntelliJ, dentro da pasta do projeto:

```
mvn clean -DskipTests package
java -jar target/pokedex-0.0.1-SNAPSHOT.jar
```

(Se o seu projeto tiver o `mvnw`, pode usar `./mvnw clean -DskipTests package` igual ao slide.
Também dá para fazer pela aba **Maven > Lifecycle > package** do IntelliJ.)
Para parar a aplicação: **Ctrl + C**. Para rodar o .jar, as variáveis de ambiente do banco
também precisam estar definidas no terminal.

## Passo 5 – Subir para o GitHub

Crie um repositório novo no GitHub (ex.: `pokedex-api`) e, na pasta do projeto:

```
git init
git add .
git commit -m "first commit"
git branch -M main
git remote add origin https://github.com/SUA_CONTA/pokedex-api
git push -u origin main
```

## Passo 6 – Deploy

O professor usou o **Render** nos slides (A44). Hoje o Render pede cartão de crédito para
verificar contas novas, mesmo no plano gratuito, então o deploy foi feito no **SnapDeploy**,
que tem plano gratuito sem cartão e usa o mesmo processo: **Dockerfile + variáveis de ambiente**.

1. Entre em <https://snapdeploy.dev> com a conta do GitHub.
2. **Containers > Deploy from GitHub** e escolha o repositório `pokedex-api` (branch `main`).
3. Configure:
   - **Container Name:** `pokedex-rm569415`
   - **Container Size:** `Small` (gratuito)
   - **Port (Advanced Options):** `8080`
4. Na janela de variáveis de ambiente, adicione:

   | Key           | Value                                              |
   |---------------|----------------------------------------------------|
   | `DB_URL`      | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`   |
   | `DB_USER`     | seu RM (ex.: `rm569415`)                           |
   | `DB_PASSWORD` | sua senha do Oracle                                |

5. Clique em **Deploy** e acompanhe o build. Quando o container ficar **Running**:
   - <https://pokedex-rm569415-d0864.containers.snapdeploy.app/pokedex> → JSON com os Pokémons
   - <https://pokedex-rm569415-d0864.containers.snapdeploy.app> → Front-End da Pokedex

> **Dica:** no plano gratuito o container "dorme" depois de 15 minutos sem uso. A primeira
> requisição pode demorar cerca de 1 minuto. Abra o link uns minutos **antes** de apresentar.

> **Se quiser usar o Render (como no slide):** New > Web Service > Public Git Repository
> (`https://github.com/lehalmeidafc0/pokedex-api`), Language `Docker`, plano `Free`, as mesmas
> 3 variáveis de ambiente e **Deploy Web Service** (vai pedir um cartão para verificação).

> **Sobre o Dockerfile:** é a mesma ideia do slide (Eclipse Temurin 21). Usamos a imagem oficial
> do Maven, que já vem com o Java 21 (Temurin): ela compila o projeto e roda o .jar. Assim o
> Deploy funciona mesmo que o arquivo `mvnw` não esteja no repositório.

---

## Front-End

O arquivo `src/main/resources/static/index.html` é servido pela própria API, então ele abre
direto no link do Deploy. Ele faz todas as requisições (GET, GET por código, POST, PUT, DELETE),
mostra as mensagens do Bean Validation e tem um painel **Requisições feitas** com o verbo,
a URL e o código de status de cada chamada — ótimo para mostrar na apresentação.

Também dá para abrir o `index.html` direto do computador (duplo clique) e digitar a URL da API
no campo **URL da API**. Nesse caso o Front está em outra origem, e quem permite a comunicação
é o `CorsFilter`.
