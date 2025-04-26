-- SQL script para teste de busca de usuário não existente
-- Garante que não haja usuários na tabela
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;
DELETE FROM USUARIOS;