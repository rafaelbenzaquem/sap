CREATE TABLE fechamentos_folhas
(
    id                       bigint NOT NULL AUTO_INCREMENT,
    ano                      int    NOT NULL,
    data_fechamento          datetime(6) NOT NULL,
    horas_esperadas_minutos  bigint NOT NULL,
    horas_executadas_minutos bigint NOT NULL,
    mes                      int    NOT NULL,
    prazo_compensacao        datetime(6) NOT NULL,
    saldo_minutos            bigint NOT NULL,
    id_servidor              int    NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_fechamento_folha (id_servidor,mes, ano),
    CONSTRAINT fk_servidor_fechamento FOREIGN KEY (id_servidor) REFERENCES servidores (id)
);
