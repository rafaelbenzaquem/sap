-- Tabela para snapshots dos registros (antes/depois)
CREATE TABLE alteracoes_registros
(
    id                   BIGINT PRIMARY KEY AUTO_INCREMENT,
    pedido_alteracao_id  BIGINT NOT NULL,
    registro_original_id BIGINT, -- Pode ser NULL para registros novos
    registro_novo_id     BIGINT, -- Pode ser NULL para registros removidos
    acao                 ENUM('CRIAR', 'ALTERAR', 'REMOVER','DESATIVAR','ATIVAR') NOT NULL,
    CONSTRAINT fk_ar_pedido_alteracao_id
        FOREIGN KEY (pedido_alteracao_id) REFERENCES pedidos_alteracoes (id) ON DELETE CASCADE,
    CONSTRAINT fk_ar_registro_original_id
        FOREIGN KEY (registro_original_id) REFERENCES registros (id) ON DELETE SET NULL,
    CONSTRAINT fk_ar_registro_novo_id
        FOREIGN KEY (registro_novo_id) REFERENCES registros (id) ON DELETE SET NULL
);