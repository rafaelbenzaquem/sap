CREATE TABLE pedidos_modificaoes_pontos
( 
    id  bigint NOT NULL,
    ponto_dia       date         NOT NULL,
    ponto_matricula varchar(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_pedidos_modificacoes_pontos FOREIGN KEY (id) REFERENCES pedidos_modicacoes (id),
    CONSTRAINT fk_ponto_notificao FOREIGN KEY (ponto_dia, ponto_matricula) REFERENCES pontos (dia, matricula) ON DELETE CASCADE
);