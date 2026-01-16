CREATE TABLE feriados
(
    data            date            NOT NULL,
    tipo            int(1)          DEFAULT NULL,
    abrangencia     int(1)          DEFAULT NULL,
    descricao       varchar(255)    NOT NULL,
    PRIMARY KEY (data)
);