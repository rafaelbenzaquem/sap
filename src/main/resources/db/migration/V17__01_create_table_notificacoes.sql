CREATE TABLE notificacoes
(
    id         BIGINT PRIMARY KEY,
    usuario_id INT          NOT NULL,
    titulo     VARCHAR(255) NOT NULL,
    mensagem   TEXT         NOT NULL,
    tipo       ENUM('INFO', 'WARNING', 'ERROR', 'SUCCESS') DEFAULT 'INFO',
    foi_lida   BOOLEAN   DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    metadata   JSON
);

CREATE INDEX idx_notificacoes_usuario_id ON notificacoes (usuario_id);
CREATE INDEX idx_notificacoes_usuario_naolida ON notificacoes (usuario_id, foi_lida);