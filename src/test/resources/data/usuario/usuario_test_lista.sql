-- SQL script para teste de listagem de múltiplos usuários
-- Cria schema e insere três usuários distintos
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;
INSERT INTO USUARIOS (ID, NOME, MATRICULA, CRACHA, HORA_DIARIA)
  VALUES
    (2, 'Alice Silva',        'RR10001', '0000000000010001', 8),
    (3, 'Bob Santos',         'RR10002', '0000000000010002', 8),
    (4, 'Carlos Souza',       'RR10003', '0000000000010003', 6);