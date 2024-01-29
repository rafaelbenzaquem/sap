create table IF NOT EXISTS usuario(
id		bigint NOT NULL AUTO_INCREMENT,
nome	varchar(255) DEFAULT NULL,
chave	varchar(255) DEFAULT NULL,
papel int(2) DEFAULT 1,
cpf		varchar(11) not null unique,

PRIMARY KEY (id));