-- dataset para testes de UsuarioJpaRepository
-- garante que o schema esteja criado e ativo
CREATE SCHEMA IF NOT EXISTS SISPONTODB;
SET SCHEMA SISPONTODB;

TRUNCATE TABLE USUARIOS;

INSERT INTO USUARIOS (NOME, MATRICULA, CRACHA, HORA_DIARIA)
VALUES ('Alice Wonderland', 'M001', 'C001', 8),
       ('Bob Builder', 'M002', 'C002', 6);
