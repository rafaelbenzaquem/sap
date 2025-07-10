CREATE TABLE folhas
(
    ano                     int NOT NULL,
    mes                     int NOT NULL,
    data_abertura           datetime(6) DEFAULT NULL,
    data_homologacao        datetime(6) DEFAULT NULL,
    id_servidor             int NOT NULL,
    id_servidor_homologador int DEFAULT NULL,
    PRIMARY KEY (ano, mes, id_servidor),
    KEY                     fk_servidor_folha (id_servidor),
    KEY                     fk_homologador_folha (id_servidor_homologador),
    CONSTRAINT fk_homologador_folha FOREIGN KEY (id_servidor_homologador) REFERENCES servidores (id),
    CONSTRAINT fk_servidor_folha FOREIGN KEY (id_servidor) REFERENCES servidores (id)
);
