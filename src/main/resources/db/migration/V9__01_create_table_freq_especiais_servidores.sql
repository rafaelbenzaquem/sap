CREATE TABLE freq_especiais_servidores
(
    sei varchar(255) DEFAULT NULL,
    id  varchar(255) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_freq_especiais_servidores_ausencia FOREIGN KEY (id) REFERENCES ausencias_usuarios (id)
);