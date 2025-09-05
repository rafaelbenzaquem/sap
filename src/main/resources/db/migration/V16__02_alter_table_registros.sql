ALTER TABLE registros DROP FOREIGN KEY fk_registro_novo;

ALTER TABLE registros
    ADD CONSTRAINT fk_registro_novo
        FOREIGN KEY (registro_novo_id) REFERENCES registros (id) ON DELETE SET NULL;