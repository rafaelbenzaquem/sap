CREATE TABLE registros
(
    id                    bigint       NOT NULL AUTO_INCREMENT,
    ativo                 bit(1)  DEFAULT NULL,
    codigo_acesso         int     DEFAULT NULL,
    data_aprovacao        datetime(6) DEFAULT NULL,
    data_cadastro         datetime(6) DEFAULT NULL,
    hora                  time(6) DEFAULT NULL,
    sentido               char(1) DEFAULT NULL,
    ponto_dia             date         NOT NULL,
    ponto_matricula       varchar(255) NOT NULL,
    registro_novo_id      bigint  DEFAULT NULL,
    id_servidor_aprovador int     DEFAULT NULL,
    id_servidor_criador   int     DEFAULT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_registro_novo_id (registro_novo_id),
    KEY                   fk_registro_ponto (ponto_dia,ponto_matricula),
    KEY                   fk_aprovador_registro (id_servidor_aprovador),
    KEY                   fk_criador_registro (id_servidor_criador),
    CONSTRAINT fk_aprovador_registro FOREIGN KEY (id_servidor_aprovador) REFERENCES servidores (id) ON DELETE SET NULL,
    CONSTRAINT fk_criador_registro FOREIGN KEY (id_servidor_criador) REFERENCES servidores (id) ON DELETE CASCADE,
    CONSTRAINT fk_registro_novo FOREIGN KEY (registro_novo_id) REFERENCES registros (id),
    CONSTRAINT fk_registro_ponto FOREIGN KEY (ponto_dia, ponto_matricula) REFERENCES pontos (dia, matricula) ON DELETE CASCADE
);
