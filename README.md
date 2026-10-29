# Pokedex API

Esse é o projeto do **Praticando** das aulas A39 até A46 de Domain Driven Design Using Java (Prof. Gilberto).

A ideia foi pegar tudo que a gente fez no projeto da MegaFarma e repetir com uma Pokedex: uma API em Java com Spring Boot que cadastra, lista, busca, atualiza e apaga Pokémons em um banco Oracle. Depois coloquei CORS, um Front-End simples e fiz o deploy para ela ficar no ar.

- **API no ar:** https://pokedex-rm569415-d0864.containers.snapdeploy.app/pokedex
- **Front-End:** https://pokedex-rm569415-d0864.containers.snapdeploy.app
- **Repositório:** https://github.com/lehalmeidafc0/pokedex-api

> O plano gratuito "dorme" depois de 15 minutos sem uso. Se a página demorar na primeira vez, é só esperar uns segundos que ela acorda.

## Endpoints

Todos ficam em `/pokedex`:

| Verbo | Caminho | O que faz | Deu certo | Deu errado |
|---|---|---|---|---|
| GET | `/pokedex` | lista todos os Pokémons | 200 | 404 |
| GET | `/pokedex/{codigo}` | busca um Pokémon pelo código | 200 | 404 |
| POST | `/pokedex` | cadastra um Pokémon | 201 | 400 |
| PUT | `/pokedex/{codigo}` | atualiza um Pokémon | 201 | 400 |
| DELETE | `/pokedex/{codigo}` | apaga um Pokémon | 204 | 404 |

Também tem um `GET /health`, que só responde "OK". A plataforma do deploy usa ele para saber se a API está viva.

Exemplo de JSON para cadastrar:

```json
{
    "nome": "Squirtle",
    "tipo": "Água",
    "altura": 0.5,
    "peso": 9.0,
    "dataDaCaptura": "2025-08-15"
}
```

O `codigo` não precisa ser enviado, o próprio Oracle gera. A resposta do cadastro já volta com ele preenchido.

## Como o projeto está organizado

Segui a mesma divisão em pacotes que o professor usou:

- **to** → `PokemonTO`: representa um Pokémon (é a "cara" da tabela). Aqui ficam as validações do Bean Validation.
- **dao** → `PokemonDAO`: onde fica o SQL. `ConnectionFactory`: abre e fecha a conexão com o Oracle. `CriadorDeTabela`: cria a tabela sozinha se ela ainda não existir.
- **bo** → `PokemonBO`: regras de negócio. É ele que chama o DAO.
- **resource** → `PokemonResource`: os endpoints. `CorsFilter`: libera o acesso do Front-End. `ValidacaoHandler`: devolve mensagens de erro mais claras. `HealthResource`: o `/health`.

Outros arquivos:

- `src/main/resources/static/index.html` → o Front-End
- `sql/criar_tabela.sql` → script da tabela, caso queira criar na mão
- `json-exemplos/` → os JSONs que usei para testar (a dica do professor de deixar num .txt)
- `insomnia/Insomnia_Pokedex.json` → coleção do Insomnia pronta para importar
- `Dockerfile` → usado no deploy

## O que fiz em cada aula

| Aula | O que o Praticando pedia | Onde está |
|---|---|---|
| A39 | criar os pacotes e as classes `PokemonTO`, `PokemonDAO`, `PokemonBO` e `PokemonResource` | pacotes `to`, `dao`, `bo` e `resource` |
| A40 | driver do Oracle, `ConnectionFactory`, tabela, `save()` nas três camadas, validar que a data da captura está no passado, POST no Insomnia | `pom.xml`, `ConnectionFactory`, `sql/`, métodos `save()` |
| A41 | `findAll()` buscando do banco, `findByCodigo()`, Bean Validation, GETs no Insomnia | `PokemonDAO` e `PokemonTO` |
| A42 | `delete()` nas três camadas, DELETE no Insomnia | métodos `delete()` |
| A43 | `update()` nas três camadas, PUT no Insomnia | métodos `update()` |
| A44 | `CorsFilter`, build, deploy e um Front-End | `CorsFilter`, `Dockerfile` e `index.html` |

Algumas coisas que vale explicar:

- **Data da captura:** na A40 a validação ficava no `PokemonBO`. Na A41, igual o professor fez com o `RemedioBO`, ela passou para o Bean Validation (`@Past`). Deixei o código antigo comentado no BO para dar para ver a mudança.
- **ConnectionFactory:** mantive o `getConnection()` e o `closeConnection()` do professor, mas tive que mudar uma coisa por dentro. Testando várias requisições ao mesmo tempo, algumas falhavam, porque uma fechava a conexão que a outra estava usando. E o Oracle da FIAP não deixa cada aluno abrir muitas conexões ao mesmo tempo (dá o erro `ORA-02391`). Então agora as requisições usam uma conexão por vez, numa fila: se duas chegam juntas, a segunda espera a primeira terminar.
- **CriadorDeTabela:** quando a API sobe, ela confere se a tabela `DDD_POKEMONS` existe e, se não existir, cria com 3 Pokémons de exemplo. Assim não precisa abrir o SQL Developer.

## Rodando no IntelliJ

1. Abra a pasta `pokedex` no IntelliJ (File > Open) e espere o Maven baixar tudo. Se aparecer o ícone do Maven no canto de cima, clique em **Sync Maven Changes**.
2. O usuário e a senha do banco não ficam no código, eles vêm de variáveis de ambiente. Vá em **Run > Edit Configurations > PokedexApplication > Environment variables** e coloque (com o seu RM e senha):
   ```
   DB_URL=jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL;DB_USER=rmXXXXX;DB_PASSWORD=sua_senha
   ```
3. Rode a classe `PokedexApplication`. Quando aparecer `Tomcat started on port 8080` no console, está funcionando.
4. Abra http://localhost:8080/pokedex para ver o JSON, ou http://localhost:8080 para ver o Front-End.

## Testando no Insomnia

1. No Insomnia, clique em **Import** e escolha o arquivo `insomnia/Insomnia_Pokedex.json`.
2. Vai aparecer a coleção **API Pokedex** com as requisições `findAll`, `findByCodigo`, `save`, `save (inválido - Bean Validation)`, `update` e `delete`.
3. No canto de cima, escolha o ambiente: **Local** (para testar no IntelliJ) ou **Deploy (SnapDeploy)** (para testar a API no ar).
4. Nas requisições que usam código (`/pokedex/1`), troque o número por um código que exista no banco.

## Build

Para gerar o .jar, no terminal dentro da pasta do projeto:

```
mvn clean -DskipTests package
java -jar target/pokedex-0.0.1-SNAPSHOT.jar
```

Também dá para fazer pela aba **Maven > Lifecycle > package** do IntelliJ. Para parar, Ctrl + C.

## Deploy

Nos slides o professor usou o **Render**. Só que hoje o Render pede cartão de crédito para verificar contas novas, até no plano grátis. Por isso fiz o deploy no **SnapDeploy**, que é gratuito e não pede cartão. O processo é o mesmo da aula: o código vai para o GitHub, a plataforma usa o `Dockerfile` para montar a aplicação, e o usuário e a senha do banco ficam em variáveis de ambiente.

Passo a passo:

1. Entrar em https://snapdeploy.dev com a conta do GitHub.
2. Ir em **Containers > Deploy from GitHub** e escolher o repositório `pokedex-api`, branch `main`.
3. Nome do container: `pokedex-rm569415`, tamanho **Small** (o gratuito) e, em Advanced Options, porta **8080**.
4. Na janela de variáveis de ambiente, adicionar:

   | Variável | Valor |
   |---|---|
   | `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
   | `DB_USER` | seu RM |
   | `DB_PASSWORD` | sua senha do Oracle |

5. Clicar em **Deploy** e esperar. Às vezes ele mostra "Failed" no começo e depois de 1 ou 2 minutos fica tudo certo, porque o Java demora um pouco para subir no plano grátis.

Sobre o `Dockerfile`: é a mesma ideia do slide (Java 21 da Eclipse Temurin), só que usei a imagem oficial do Maven, que já vem com esse Java. Ela compila o projeto e roda o .jar, então não depende do `mvnw` estar no repositório.

Se quiser fazer igual ao slide, no Render: New > Web Service > Public Git Repository, cola o link do repositório, escolhe Docker, plano Free, coloca as mesmas três variáveis e clica em Deploy Web Service (ele vai pedir o cartão).

## Front-End

O Front-End fica em `src/main/resources/static/index.html`. Como ele está dentro da API, abre direto no link do deploy. Dá para listar, buscar pelo código, cadastrar, editar e excluir. Quando os dados estão errados, ele mostra as mensagens do Bean Validation.

Embaixo do formulário tem um quadrinho chamado **Requisições feitas**, que mostra cada requisição que o Front fez, com o verbo, a URL e o status que a API devolveu. Ajuda bastante para mostrar na apresentação o que está acontecendo por trás.

Também dá para abrir o `index.html` direto do computador e colar o link da API no campo **URL da API**. Aí o Front está em outro endereço, e é o `CorsFilter` que deixa ele conversar com a API.
