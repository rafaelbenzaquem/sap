-- SQL script para teste de listagem de múltiplos arquivos
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;
DELETE FROM ARQUIVOS;
INSERT INTO ARQUIVOS (ID, NOME, TIPO_DE_CONTEUDO, BYTES, DESCRICAO) VALUES
  ('1', 'file1', 'text/plain', X'01', 'd1'),
  ('2', 'file2', 'text/plain', X'02', 'd2'),
  ('3', 'file3', 'text/plain', X'03', 'd3');