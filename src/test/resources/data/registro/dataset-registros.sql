-- dataset for RegistroRepository tests
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;

DELETE
FROM REGISTROS;
DELETE
FROM PONTOS;

-- insert matching Ponto rows for FK constraint
INSERT INTO PONTOS (MATRICULA, DIA, INDICE, DESCRICAO)
VALUES ('M1', DATE '2023-01-01', 1.0, 'desc'),
       ('M1', DATE '2023-01-02', 1.0, 'desc'),
       ('M2', DATE '2023-01-01', 1.0, 'desc'),
       ('M2', DATE '2023-01-02', 1.0, 'desc');

-- insert Registro rows
INSERT INTO REGISTROS (ID, HORA, CODIGO_ACESSO, SENTIDO, ATIVO, PONTO_MATRICULA, PONTO_DIA)
VALUES (1, TIME '08:00:00', 100, 'E', TRUE, 'M1', '2023-01-01'),
       (2, TIME '15:00:00', 101, 'S', FALSE, 'M1', '2023-01-01'),
       (3, TIME '12:00:00', 200, 'E', TRUE, 'M1', '2023-01-02'),
       (4, TIME '19:00:00', 201, 'S', TRUE, 'M1', '2023-01-02'),
       (5, TIME '07:00:00', 300, 'E', TRUE, 'M2', '2023-01-01'),
       (6, TIME '18:00:00', 301, 'S', TRUE, 'M2', '2023-01-01'),
       (7, TIME '07:00:00', 400, 'E', TRUE, 'M2', '2023-01-02'),
       (8, TIME '18:00:00', 401, 'S', TRUE, 'M2', '2023-01-02');