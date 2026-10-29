-- Rode este script no Oracle SQL Developer (conectado ao oracle.fiap.com.br)

CREATE TABLE DDD_POKEMONS (
    CODIGO          NUMBER GENERATED ALWAYS AS IDENTITY,
    NOME            VARCHAR2(100),
    TIPO            VARCHAR2(50),
    ALTURA          NUMBER(5,2),
    PESO            NUMBER(6,2),
    DATA_DA_CAPTURA DATE,
    PRIMARY KEY (CODIGO)
);

-- (Opcional) alguns Pokémons para a lista não começar vazia
INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)
VALUES ('Pikachu', 'Elétrico', 0.4, 6.0, DATE '2025-03-10');
INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)
VALUES ('Bulbasaur', 'Planta', 0.7, 6.9, DATE '2025-04-22');
INSERT INTO DDD_POKEMONS (NOME, TIPO, ALTURA, PESO, DATA_DA_CAPTURA)
VALUES ('Charmander', 'Fogo', 0.6, 8.5, DATE '2025-06-05');
COMMIT;

-- Para conferir:
-- SELECT * FROM DDD_POKEMONS ORDER BY CODIGO;

-- Se precisar recriar do zero:
-- DROP TABLE DDD_POKEMONS;
