CREATE TABLE ferias_servidores (
                                     data_suspensao datetime(6) DEFAULT NULL,
                                     dias_gozados int DEFAULT NULL,
                                     flag tinyint DEFAULT NULL,
                                     id varchar(255) NOT NULL,
                                     PRIMARY KEY (`id`),
                                     CONSTRAINT `FKc7jyl44vpg6m2q0b56liloikl` FOREIGN KEY (`id`) REFERENCES `ausencias_usuarios` (`id`),
                                     CONSTRAINT `ferias_servidores_chk_1` CHECK ((`flag` between 0 and 3))
);
