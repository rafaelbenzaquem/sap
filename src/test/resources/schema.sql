-- noinspection SqlNoDataSourceInspectionForFile

DROP TABLE if EXISTS usuario;
CREATE TABLE usuario(
    id    bigint       NOT NULL AUTO_INCREMENT,
    nome  VARCHAR(255) DEFAULT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    chave VARCHAR(255) NOT NULL,
    papel SMALLINT     DEFAULT 1,
    cpf   VARCHAR(11)  NOT NULL UNIQUE,
    PRIMARY KEY (id)
);