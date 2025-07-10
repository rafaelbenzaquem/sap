CREATE TABLE ausencias_usuarios
(
    id         varchar(36) NOT NULL,
    descricao  varchar(255) DEFAULT NULL,
    fim        date         DEFAULT NULL,
    inicio     date         DEFAULT NULL,
    usuario_id int         NOT NULL,
    PRIMARY KEY (id),
    KEY        fk_usuario_ausencia (usuario_id),
    CONSTRAINT fk_usuario_ausencia FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
