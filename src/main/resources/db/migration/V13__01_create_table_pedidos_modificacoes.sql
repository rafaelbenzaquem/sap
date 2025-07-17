CREATE TABLE pedidos_modicacoes
( 
    id         bigint NOT NULL AUTO_INCREMENT,
    acao       char(1) DEFAULT NULL,
    data_acao  datetime(6) NOT NULL,
    usuario_id int    NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_usuario_pedido_modificao FOREIGN KEY (usuario_id) REFERENCES usuarios (id)
);