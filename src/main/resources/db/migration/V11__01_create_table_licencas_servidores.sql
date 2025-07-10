CREATE TABLE licencas_servidores
(
    sei varchar(255) DEFAULT NULL,
    id  varchar(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_licencas_servidores_ausencia FOREIGN KEY (id) REFERENCES ausencias_usuarios (id)
);