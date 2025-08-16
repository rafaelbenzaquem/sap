-- Nova tabela para pedidos de alteração
CREATE TABLE pedidos_alteracoes
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    usuario_solicitante_id  INT          NOT NULL,
    usuario_aprovador_id    INT,
    ponto_dia               date         NOT NULL,
    ponto_matricula         varchar(255) NOT NULL,
    data_solicitacao        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_aprovacao          TIMESTAMP,
    status                  ENUM('PENDENTE', 'APROVADO', 'REJEITADO') DEFAULT 'PENDENTE',
    justificativa           TEXT,
    justificativa_aprovador TEXT,
    KEY                     fk_pa_ponto (ponto_dia,ponto_matricula),
    CONSTRAINT fk_pa_usuario_solicitante_id FOREIGN KEY (usuario_solicitante_id) REFERENCES usuarios (id),
    CONSTRAINT fk_pa_usuario_aprovador_id
        FOREIGN KEY (usuario_aprovador_id) REFERENCES usuarios (id),
    CONSTRAINT fk_pa_ponto
        FOREIGN KEY (ponto_dia, ponto_matricula) REFERENCES pontos (dia, matricula)
);