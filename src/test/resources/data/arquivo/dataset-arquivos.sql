-- dataset para testes de ArquivoRepository e ArquivoServiceIntegrationTest
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;

-- limpa tabela antes de inserir
DELETE FROM ARQUIVOS;

INSERT INTO ARQUIVOS (ID, NOME, TIPO_DE_CONTEUDO, BYTES, DESCRICAO) VALUES
  ('id1', 'file1', 'text/plain', X'0102', 'desc1'),
  ('id2', 'file2', 'image/png', X'FFAA', 'desc2');