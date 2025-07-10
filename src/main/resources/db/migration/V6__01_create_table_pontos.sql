CREATE TABLE pontos
(
    dia               date         NOT NULL,
    indice            float        DEFAULT NULL,
    descricao         varchar(255) DEFAULT NULL,
    matricula         varchar(255) NOT NULL,
    ano_folha         int          DEFAULT NULL,
    mes_folha         int          DEFAULT NULL,
    id_servidor_folha int          DEFAULT NULL,
    PRIMARY KEY (dia, matricula),
    KEY               fk_folha_ponto (ano_folha,mes_folha,id_servidor_folha),
    CONSTRAINT fk_folha_ponto FOREIGN KEY (ano_folha, mes_folha, id_servidor_folha) REFERENCES folhas (ano, mes, id_servidor)
);