CREATE TABLE ausencias_usuarios
(
    id         varchar(36) NOT NULL,
    descricao  varchar(255) DEFAULT NULL,
    fim        date         DEFAULT NULL,
    inicio     date         DEFAULT NULL,
    usuario_id int         NOT NULL,
    PRIMARY KEY (id),
    KEY        fk_ausencia_usuario (usuario_id),
    CONSTRAINT fk_ausencia_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);
