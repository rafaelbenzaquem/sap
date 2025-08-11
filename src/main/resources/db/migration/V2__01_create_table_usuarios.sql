CREATE TABLE usuarios
(
    hora_diaria INT         NOT NULL,
    id          INT         NOT NULL AUTO_INCREMENT,
    matricula   VARCHAR(15) NOT NULL,
    cracha      INT NOT NULL,
    nome        VARCHAR(50) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_usuario_matricula (matricula),
    UNIQUE KEY uk_usuario_cracha (cracha),
    CONSTRAINT usuarios_chk_1 CHECK (((hora_diaria >= 4) AND (hora_diaria <= 12)))
);

-- INSERT INTO usuarios
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (51, 'RR6203', 9898, 'ALANO PEREIRA NEVES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (37, 'RR4203', 5290, 'ALCY SOUZA DO NASCIMENTO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (1, 'RR20147', 5677, 'ALDEMIR SIMÃO DE MELO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (67, 'RR20067', 4420, 'ALDRIM ANHÃNHA PRATES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (19, 'RR20032', 1133, 'ALTINO DA SILVA NETO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (97, 'RR20098', 6220, 'AMANDA SHEULY  FONTELES PACHECO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (86, 'RR5103', 4826, 'ANA LUCIA DE OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (53, 'RR2203', 7292, 'ANGELO GONÇALVES DA ROCHA JUNIOR', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (2, 'RR20171', 9870, 'ANNE MAYNARA OLIVEIRA CRUZ', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (87, 'RR20007', 6182, 'ANTÔNIO SANTANA DE SOUSA JÚNIOR', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (82, 'RR20180', 5558, 'ARTUR FERREIRA DE CARVALHO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (20, 'RR20200', 43200, 'BRUNA FLORES DE MENEZES FERNANDES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (38, 'RR20099', 9698, 'BRUNO SALAZAR DE SOUZA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (98, 'RR20119', 3335, 'BRUNO SCACABAROSSI', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (39, 'RR15003', 9864, 'CLARISMAR DE ARAÚJO COSTA DE SOUSA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (99, 'RR20183', 6879, 'CLAUDIO PATRICK DE ALMEIDA LIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (21, 'RR20030', 8547, 'CRISTIANO AGUIAR PASSOS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (40, 'RR20112', 2722, 'DÁFNE TUAN ARAÚJO CORRÊA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (54, 'RR20121', 2820, 'DAIANA APARECIDA MABONI', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (68, 'RR20071', 2962, 'DAIANA DE PAULA OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (4, 'RR20097', 8998, 'DANILO RAFAEL FERREIRA BARBOSA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (22, 'RR20111', 6614, 'DANUBIA DOS SANTOS PEREIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (12, 'RR20105', 5353, 'DIANA BARBOSA FREITAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (88, 'RR20187', 16596, 'DIEGO BARBOSA FREITAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (55, 'RR17603', 4678, 'DIMAS DE ALMEIDA SOARES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (23, 'RR20157', 6695, 'EDIMILSON LAERCIO SILVA DE ALMEIDA NETO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (89, 'RR2103', 5228, 'EDNA MARTINS CORTES LEVEL', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (56, 'RR20165', 9896, 'ELISDAIRA MARILIA FERNANDES DA SILVA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (41, 'RR20150', 3523, 'ELTON BRUNO NUNES FEITOSA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (5, 'RR20074', 6996, 'FÁBIO SABINI', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (57, 'RR15503', 5503, 'FRANCISCO JORIS SOUZA MARTINS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (69, 'RR10003', 2525, 'FRANKLIN LOPES TRINDADE', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (100, 'RR20174', 6849, 'GEOVANI BARROS NUNES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (58, 'RR16003', 6985, 'GILSON JÂNIO CAMPOS DE AZEVEDO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (70, 'RR20155', 9914, 'GIOVANNI OLIVEIRA VANZO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (24, 'RR20197', 72268, 'GISELLE HOLANDA CARDOSO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (101, 'RR20156', 4250, 'INGRID REGIELLI MENEZES SEIBERLICK', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (59, 'RR14803', 4835, 'IRIS BRITO DOS SANTOS FERREIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (72, 'RR20185', 50862219, 'JAMILE ALEXANDRA SANTOS SANTIAGO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (60, 'RR20152', 8278, 'JANAINA DE CASTRO LUZ', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (102, 'RR20144', 1924, 'JARDEL DA SILVA AREIA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (61, 'RR20143', 6891, 'JOÃO ÁTILA BEZERRA DOS SANTOS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (73, 'RR16504', 5137, 'JOÃO BATISTA CARNEIRO DE MESQUITA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (90, 'RR20089', 4569, 'JOÃO CARLOS COELHO FILHO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (25, 'RR20195', 84250, 'JOÃO HOMERO DE SOUZA CRUZ CAMILO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (103, 'RR20149', 5911, 'JOAQUIM DA SILVA OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (42, 'RR20153', 4998, 'JOSÉ GOMES DE OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (43, 'RR20005', 5818, 'JOSÉ RAIMUNDO DOS SANTOS NETO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (6, 'RR20106', 9734, 'JOYCE LUIZA CORRÊA DE QUEIROZ', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (44, 'RR20173', 5946, 'KAMILA MORAIS MACHADO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (62, 'RR20169', 5929, 'KEVIN CHINELATTO MATHIAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (35, 'RR20193', 70592, 'LAÍS SILVA RABELO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (16, 'RR7103', 6458, 'LEOTÁVIA HELENA FRAXE DE QUEIROZ', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (81, 'RR20137', 8521, 'LISSANDRA MARTHA DOS SANTOS SILVA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (104, 'RR20059', 4249, 'LUIZ MARCELO BASTOS MOREIRA DE SOUZA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (63, 'RR20053', 4979, 'LUIZ MÁRIO BARBOSA VIANA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (64, 'RR20012', 1611, 'LUIZA CRISTINA FIRMINO DE FREITAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (26, 'RR18803', 6694, 'MAGNO MARTINS VIANA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (7, 'RR20108', 1121, 'MARCELLY GOMES DIAS DE LIMA BARRETO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (46, 'RR20086', 6895, 'MARCELO PEREIRA PAIVA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (27, 'RR14003', 6984, 'MARCELO TITO COSTA DE BRITO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (28, 'RR20011', 6248, 'MÁRCIA OLIVIA NEVES ESTEVES MARTINS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (47, 'RR2603', 9686, 'MARCO AURELIO SELLA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (65, 'RR20122', 6222, 'MARCOS PIRES DA SILVA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (74, 'RR20129', 1917, 'MARIANA MOREIRA ALMEIDA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (105, 'RR20114', 2877, 'MERILANE MOREIRA ALBUQUERQUE GOMES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (48, 'RR20006', 8956, 'MILENA EDWARDS CRUZ', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (49, 'RR14703', 5889, 'NANCIS TEREZA DANIELI LIMA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (29, 'RR20161', 6948, 'NATÁLIA AIRES OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (34, 'RR20102', 7845, 'NILTON DALL''AGNOL', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (8, 'RR20182', 5792, 'PABLO RAPHAEL DOS SANTOS IGREJA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (106, 'RR20130', 1225, 'PAULO RHUAN DE OLIVEIRA MELO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (17, 'RR20109', 6523, 'PEDRO ERNESTO LOPES JUSTEN', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (30, 'RR20178', 20178, 'RAFAEL BENZAQUEM NETO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (84, 'RR15703', 3157, 'RAIMUNDO ARNALDO SEVERO DE OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (75, 'RR18303', 8720, 'RAIMUNDO RARI PEREIRA DO NASCIMENTO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (9, 'RR20095', 8614, 'RAQUEL AQUINO COSTA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (31, 'RR20166', 2016, 'RODRIGO BARBOSA DA SILVA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (66, 'RR20081', 7954, 'ROSANA MARTA COSTA GONÇALVES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (107, 'RR20118', 118, 'ROSTAN PEREIRA GUEDES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (32, 'RR20096', 3894, 'RUDINEI SAN MARTINS BEHLING', 4);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (108, 'RR20181', 6778, 'RYULER DOS SANTOS MAIA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (36, 'RR20176', 6998, 'SABRICIA VIANA DE SOUZA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (76, 'RR20070', 8249, 'SANDRA SYOMARA FERRAZ DE FIGUEIREDO CHAVES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (92, 'RR16604', 8899, 'SINAIDA CASTRO RODRIGUES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (33, 'RR17403', 8942, 'STÉFANO DA SILVA TEIXEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (10, 'RR20101', 7924, 'SÚLIO ROLIM DE FREITAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (77, 'RR20027', 6510, 'TAINÁ AMORIM SANCHO', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (109, 'RR20184', 20184, 'TAYENNE PRISCILA FARIA OLIVEIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (50, 'RR20090', 6758, 'TELMO RODRIGUES BEZERRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (110, 'RR20128', 6187, 'THAYSA GOMES MARQUES PEREIRA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (85, 'RR20110', 6563, 'THIAGO ALVES SILVA LESSA', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (78, 'RR11503', 2358, 'VLADIMIR GUEDÊLHA DE FREITAS', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (79, 'RR20100', 5120, 'WASHINGTON DE SOUSA GOES', 7);
INSERT INTO usuarios (id, matricula, cracha, nome, hora_diaria) VALUES (11, 'RR20175', 9765, 'WERLEY DE OLIVEIRA E OLIVEIRA CRUZ', 7);