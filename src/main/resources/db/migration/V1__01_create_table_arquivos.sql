CREATE TABLE arquivos
(
    id               varchar(36) NOT NULL,
    descricao        text,
    nome             varchar(50) NOT NULL,
    tipo_de_conteudo varchar(50) NOT NULL,
    bytes            blob        NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_arquivo_nome (nome)
);