-- SQL script para teste de operações do arquivo existente
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;
-- garante tabela limpa
DELETE FROM ARQUIVOS;
-- insere um arquivo
INSERT INTO ARQUIVOS (ID, NOME, TIPO_DE_CONTEUDO, BYTES, DESCRICAO)
  VALUES ('1', 'file1', 'text/plain', X'0A0B', 'desc1');