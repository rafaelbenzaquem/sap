CREATE TABLE lotacoes
(
    id             int         NOT NULL AUTO_INCREMENT,
    sigla          varchar(50) NOT NULL,
    descricao      varchar(100) NOT NULL,
    id_lotacao_pai int DEFAULT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_lotacoes_pai FOREIGN KEY (id_lotacao_pai) REFERENCES lotacoes (id) ON DELETE SET NULL
);

INSERT INTO sispontodb.lotacoes(id, sigla, descricao)
VALUES (1, 'SJTRF1AR', 'JUSTICA FEDERAL DA 1a REGIAO');
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (2, 'TRF1AR', 'TRIBUNAL REGIONAL FEDERAL DA PRIMEIRA REGIÃO', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (3, 'SJAC', 'SECAO JUDICIARIA DO ACRE', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (4, 'SJAM', 'SECAO JUDICIARIA DO AMAZONAS', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (5, 'SJAP', 'SECAO JUDICIARIA DO AMAPA', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (6, 'SJBA', 'SECAO JUDICIARIA DA BAHIA', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (7, 'SJDF', 'SECAO JUDICIARIA DO DISTRITO FEDERAL', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (8, 'SJGO', 'SECAO JUDICIARIA DE GOIAS', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (9, 'SJMA', 'SECAO JUDICIARIA DO MARANHAO', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (10, 'SJMG', 'SECAO JUDICIARIA DE MINAS GERAIS', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (11, 'SJMT', 'SECAO JUDICIARIA DE MATO GROSSO', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (12, 'SJPA', 'SECAO JUDICIARIA DO PARA', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (13, 'SJPI', 'SECAO JUDICIARIA DO PIAUI', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (14, 'SJRO', 'SECAO JUDICIARIA DE RONDONIA', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (15, 'SJRR', 'SECAO JUDICIARIA DE RORAIMA', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (16, 'SJTO', 'SECAO JUDICIARIA DE TOCANTINS', 1);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (17, 'SSJILH', 'SUBSECAO JUDICIARIA DE ILHEUS', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (18, 'SSJITZ', 'SUBSECAO JUDICIARIA DE IMPERATRIZ', 9);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (19, 'SSJJFA', 'SUBSECAO JUDICIARIA DE JUIZ DE FORA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (20, 'SSJURA', 'SUBSECAO JUDICIARIA DE UBERABA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (21, 'SSJULA', 'SUBSECAO JUDICIARIA DE UBERLANDIA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (22, 'SSJMBA', 'SUBSECAO JUDICIARIA DE MARABA', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (23, 'SSJSRM', 'SUBSECAO JUDICIARIA DE SANTAREM', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (24, 'SSJTBN', 'SUBSEÇÃO JUDICIÁRIA DE TABATINGA', 4);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (25, 'SSJPSO', 'SUBSEÇAO JUDICIARIA DE PASSOS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (26, 'SSJCXS', 'SUBSEÇAO JUDICIARIA DE CAXIAS', 9);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (27, 'SSJBES', 'SUBSEÇAO JUDICIARIA DE BARREIRAS', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (28, 'SSJCFS', 'SUBSEÇAO JUDICIARIA DE CAMPO FORMOSO', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (29, 'SSJEUS', 'SUBSEÇAO JUDICIARIA DE EUNAPOLIS', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (30, 'SSJITB', 'SUBSEÇAO JUDICIARIA DE ITABUNA', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (31, 'SSJJEE', 'SUBSEÇAO JUDICIARIA DE JEQUIE', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (32, 'SSJPAF', 'SUBSEÇAO JUDICIARIA DE PAULO AFONSO', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (33, 'SSJJUO', 'SUBSEÇAO JUDICIARIA DE JUAZEIRO', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (34, 'SSJGNB', 'SUBSEÇAO JUDICIARIA DE GUANAMBI', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (35, 'SSJFSA', 'SUBSEÇAO JUDICIARIA DE FEIRA DE SANTANA', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (36, 'SSJVCA', 'SUBSEÇAO JUDICIARIA DE VITORIA DA CONQUISTA', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (37, 'SSJRVD', 'SUBSEÇAO JUDICIARIA DE RIO VERDE', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (38, 'SSJACG', 'SUBSEÇAO JUDICIARIA DE APARECIDA DE GOIANIA', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (39, 'SSJLZA', 'SUBSEÇAO JUDICIARIA DE LUZIANIA', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (40, 'SSJANS', 'SUBSEÇAO JUDICIARIA DE ANAPOLIS', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (41, 'SSJROI', 'SUBSEÇAO JUDICIARIA DE RONDONOPOLIS', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (42, 'SSJSNO', 'SUBSEÇAO JUDICIARIA DE SINOP', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (43, 'SSJCCS', 'SUBSEÇAO JUDICIARIA DE CACERES', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (44, 'SSJMCL', 'SUBSEÇAO JUDICIARIA DE MONTES CLAROS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (45, 'SSJIIG', 'SUBSEÇAO JUDICIARIA DE IPATINGA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (46, 'SSJVGA', 'SUBSEÇAO JUDICIARIA DE VARGINHA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (47, 'SSJSLA', 'SUBSEÇAO JUDICIARIA DE SETE LAGOAS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (48, 'SSJGVS', 'SUBSEÇAO JUDICIARIA DE GOVERNADOR VALADARES', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (49, 'SSJPSA', 'SUBSEÇAO JUDICIARIA DE POUSO ALEGRE', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (50, 'SSJPMS', 'SUBSEÇAO JUDICIARIA DE PATOS DE MINAS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (51, 'SSJSOE', 'SUBSEÇAO JUDICIARIA DE SAO JOAO DEL REY', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (52, 'SSJSSP', 'SUBSEÇAO JUDICIARIA DE SAO SEBASTIAO DO PARAÍSO', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (53, 'SSJLAV', 'SUBSEÇAO JUDICIARIA DE LAVRAS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (54, 'SSJATM', 'SUBSEÇAO JUDICIARIA DE ALTAMIRA', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (55, 'SSJCAH', 'SUBSEÇAO JUDICIARIA DE CASTANHAL', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (56, 'SSJPIZ', 'SUBSEÇAO JUDICIARIA DE PICOS', 13);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (57, 'SSJJIP', 'SUBSEÇAO JUDICIARIA DE JI-PARANA', 14);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (58, 'SSJDVL', 'SUBSEÇAO JUDICIARIA DE DIVINOPOLIS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (59, 'SERIN', 'SERVIDOR INATIVO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (60, 'SERAF', 'SERVIDOR AFASTADO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (61, 'SERDE', 'SERVIDOR DESLIGADO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (62, 'SERSL', 'SERVIDOR SEM LOTAÇÃO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (63, 'DIREH', 'SERVIDOR A DISPOSIÇÃO DA SECRETARIA DE RECURSOS HUMANOS', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (64, 'SERGE', 'SERVIDORA EM LICENÇA GESTANTE EXERCENDO FUNÇÃO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (65, 'ESTDE', 'ESTAGIÁRIO DESLIGADO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (66, 'DJDES', 'JUIZ DESLIGADO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (67, 'DJINA', 'JUIZ INATIVO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (68, 'DJSLT', 'JUIZ SEM LOTAÇÃO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (69, 'OUTORG', 'OUTROS ÓRGÃOS', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (70, 'DIREF', 'DIRETORIA DO FORO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (71, 'ASJUR', 'ASSISTÊNCIA JURÍDICA/DIREF', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (72, 'SEBIB', 'SEÇÃO DE BIBLIOTECA', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (73, 'SECOI', 'SEÇÃO DE CONTROLE INTERNO', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (74, 'EXTIN', 'SETOR DE CONTABILIDADE', 73);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (75, 'EXTIN', 'SETOR DE ANAL. ESCRIT. CONTABIL', 73);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (76, 'SEAPA', 'SEÇÃO DE APOIO ADMINISTRATIVO/DIREF', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (77, 'SECAD', 'SECRETARIA ADMINISTRATIVA', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (78, 'SEAPA', 'SEÇÃO DE APOIO ADMINISTRATIVO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (79, 'SEPCE', 'SEÇÃO DE PROTOCOLO E CERTIDÕES', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (80, 'SECLA', 'SEÇÃO DE CLASSIFICAÇÃO E DISTRIBUIÇÃO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (81, 'SECOT', 'SEÇÃO DE CONTADORIA', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (82, 'SEDAJ', 'SEÇÃO DE DEPÓSITO E ARQUIVO JUDICIAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (83, 'CEMAN', 'CENTRAL DE MANDADOS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (84, 'SEPAG', 'SEÇÃO DE PAGAMENTO DE PESSOAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (85, 'SECAP', 'SEÇÃO DE CADASTRO DE PESSOAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (86, 'SELEP', 'SEÇÃO DE LEGISLAÇÃO DE PESSOAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (87, 'SEDER', 'SEÇÃO DE DESENVOLVIMENTO E AVALIAÇÃO DE RECURSOS HUMANOS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (88, 'SEBES', 'SEÇÃO DE PROGRAMAS E BENEFÍCIOS SOCIAIS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (89, 'SEMAD', 'SEÇÃO DE MODERNIZAÇÃO ADMINISTRATIVA', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (90, 'SEINF', 'SEÇÃO DE INFORMÁTICA', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (91, 'SEPOF', 'SEÇÃO DE PROGRAMAÇÃO E EXECUÇÃO ORÇAMENTÁRIA E FINANCEIRA', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (92, 'SEMAP', 'SEÇÃO DE MATERIAL E PATRIMÔNIO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (93, 'SECOM', 'SEÇÃO DE COMPRAS E LICITAÇÕES', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (94, 'SESEG', 'SEÇÃO DE SERVIÇOS GERAIS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (95, 'SECAM', 'SEÇÃO DE COMUNICAÇÕES E ARQUIVO ADMINISTRATIVO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (96, 'SEVIT', 'SEÇÃO DE SEGURANÇA, VIGILÂNCIA E TRANSPORTE', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (97, 'EXTIN', 'SEÇÃO DE BIBLIOTECA (EXTINTA)', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (98, 'EXTIN', 'SEÇÃO DE COMPRAS (EXTINTA)', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (99, 'EXTIN', 'SEÇÃO ADM. SERVI. GERAIS (EXTINTA)', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (100, 'EXTIN', 'SETOR. COM E ARQ. ADMINIST (EXTINTA', 99);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (101, 'EXTIN', 'SETOR MAN. CONSERV. LIMP.(EXTINTA)', 99);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (102, 'EXTIN', 'SETOR SEG. VIG. TRANSP. (EXTINTA)', 99);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (103, 'EXTIN', 'SEÇÃO CONT. INTERNO (EXTINTA)', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (104, 'EXTIN', 'SEÇÃO LEG.DES.REC.HUMANOS (EXTINTA(', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (105, 'EXTIN', 'SEÇÃO ADM.DES.REC. HUMANOS(EXTINTA)', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (106, 'EXTIN', 'SETOR PAG. PESSOAL (EXTINTA)', 105);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (107, 'EXTIN', 'SETOR CADASTRO PESSOAL (EXTINTA)', 105);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (108, 'EXTIN', 'SETOR LEGISL. PESSOAL (EXTINTA)', 105);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (109, 'EXTIN', 'SETOR DES.AVAL.REC.HUMANOS (EXTINTA', 105);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (110, 'COJEF', 'COORDENAÇÃO DO JUIZADO ESPECIAL FEDERAL', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (111, 'SECAAT', 'SEC. ATEND. E ATERMAÇÃO', 110);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (112, '1ª VARA', '1ª VARA DA SJRR', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (113, 'GABJU', 'GABINETE DO JUIZ FEDERAL 1ªVARA', 112);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (114, 'GAJUS', 'GAB. JUIZ FEDERAL SUBSTITUTO', 112);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (115, 'SECVA', 'SECRETARIA DE VARA', 112);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (116, 'SEXEC', 'SEÇÃO DE EXECUÇÕES', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (117, 'SEPIP', 'SEÇÃO DE PROTOCOLO E INFORMAÇÕES PROCESSUAIS', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (118, 'SEAPA', 'SEC. APOIO ADMINISTRATIVO', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (119, 'SEPOD', 'SEÇÃO DE PROCESSAMENTO E PROCEDIMENTOS DIVERSOS', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (120, 'EXTIN', 'SEÇÃO PROC. CIVEIS', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (121, 'EXTIN', 'SEÇÃO CRIMINAL', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (122, 'EXTIN', 'SEÇÃO EXEC.CRIMINAIS', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (123, 'EXTIN', 'SEÇ.ATEND.INF.PROCES', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (124, '2ª VARA', '2ª VARA DA SJRR', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (125, 'GABJU', 'GAB.JUIZ FEDERAL', 124);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (126, 'GAJUS', 'GAB.JUIZ FEDERAL SUBSTITUTO', 124);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (127, 'SECVA', 'SECRETARIA DE VARA', 124);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (128, 'SEXEC', 'SEÇÃO DE EXECUÇÕES', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (129, 'SEPIP', 'SEÇÃO DE PROTOCOLO E INFORMAÇÕES PROCESSUAIS', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (130, 'SEAPA', 'SEC.APOIO ADMINISTRATIVO', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (131, 'SEPOD', 'SEÇÃO DE PROCESSAMENTO E PROCEDIMENTOS DIVERSOS', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (132, 'EXTIN', 'SEÇÃO.PROC.CIVEIS', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (133, 'EXTIN', 'SEÇÃO CRIMINAL', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (134, 'EXTIN', 'SEÇÃO EXEC.CRIMINAIS', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (135, 'EXTIN', 'SEÇ.ATEND,INF.PROCES', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (136, '3ª VARA', '3ª VARA (JEF) DA SJRR', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (137, 'GABJU', 'GAB. JUIZ FEDERAL', 136);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (138, 'GAJUS', 'GAB. JUIZ FEDERAL SUBSTITUTO', 136);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (140, 'SECVA', 'SECRETARIA DE VARA', 136);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (141, 'SEAPJ', 'SEÇÃO DE APOIO AOS JULGAMENTOS', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (142, 'SEPOD', 'SEÇÃO DE PROCESSAMENTO E PROCEDIMENTOS DIVERSOS', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (143, 'SEINP', 'SEÇÃO DE INFORMAÇÕES PROCESSUAIS', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (144, 'SERTRA', 'SERVIDORES EM TRANSITO', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (145, 'TURRES', 'TURMA RECURSAL DOS JUIZADOS ESPECIAIS', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (146, 'GABIN', 'GABINETE DA TURMA RECURSAL', 145);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (147, 'SECTUR', 'SECRETARIA DA TURMA RECURSAL', 145);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (148, 'GABIN', '1º GABINETE DA TURMA RECURSAL', 146);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (149, 'GABIN', '2º GABINETE DA TURMA RECURSAL', 146);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (150, 'GABIN', '3º GABINETE DA TURMA RECURSAL', 146);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (151, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (152, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (153, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (154, 'SERDI', 'SERVIÇO DE DIGITALIZAÇÃO JUDICIAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (155, 'SECJU', 'SEÇÃO DE CÁLCULOS JUDICIAIS', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (156, 'SEBES', 'SEÇÃO DE BEM-ESTAR SOCIAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (157, 'SEINF', 'SEÇÃO DE TECNOLOGIA DA INFORMACAO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (158, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (159, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 115);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (160, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (161, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 127);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (162, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (163, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (164, 'NUCOD', 'NÚCLEO DE APOIO À COORDENAÇÃO', 110);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (165, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 110);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (199, 'TURREC', 'TURMAS RECURSAIS DA SJ RORAIMA', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (166, 'SECTUR', 'SECRETARIA ÚNICA DA TURMA RECURSAL', 199);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (167, 'NUTUR', 'NÚCLEO DE APOIO À TURMA RECURSAL', 166);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (168, 'APORE', 'APOIO AOS RELATORES', 166);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (169, 'SSJPTU', 'SUBSEÇÃO JUDICIÁRIA DE PARACATU', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (170, 'SSJDIO', 'SUBSEÇÃO JUDICIÁRIA DE DIAMANTINO', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (171, 'SSJPNA', 'SUBSEÇÃO JUDICIÁRIA DE PARNAÍBA', 13);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (172, 'SSJFRM', 'SUBSEÇÃO JUDICIÁRIA DE FORMOSA', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (173, 'SSJURC', 'SUBSEÇÃO JUDICIÁRIA DE URUAÇU', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (174, 'SSJUNI', 'SUBSEÇÃO JUDICIÁRIA DE UNAÍ', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (175, 'SSJIEE', 'SUBSEÇÃO JUDICIÁRIA DE IRECÊ', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (176, 'SSJTOT', 'SUBSEÇÃO JUDICIÁRIA DE TEÓFILO OTONI', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (177, 'SSJARN', 'SUBSEÇÃO JUDICIÁRIA DE ARAGUAÍNA', 16);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (178, 'SSJGUM', 'SUBSEÇÃO JUDICIÁRIA DE GUAJARÁ-MIRIM', 14);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (179, 'SSJLJI', 'SUBSEÇÃO JUDICIÁRIA DE LARANJAL DO JARI', 5);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (180, 'SSJBBL', 'SUBSEÇÃO JUDICIÁRIA DE BACABAL', 9);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (181, 'SSJOPQ', 'SUBSEÇÃO JUDICIÁRIA DE OIAPOQUE', 5);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (182, 'SSJCEM', 'SUBSEÇÃO JUDICIÁRIA DE CONTAGEM', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (183, 'SSJRDO', 'SUBSEÇÃO JUDICIÁRIA DE REDENÇÃO', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (184, 'SSJGUR', 'SUBSEÇÃO JUDICIÁRIA DE GURUPI', 16);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (185, 'SSJJTI', 'SUBSEÇÃO JUDICIÁRIA DE JATAÍ', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (186, 'SSJMNC', 'SUBSEÇÃO JUDICIÁRIA DE MANHUAÇU', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (187, 'SSJMRE', 'SUBSEÇÃO JUDICIÁRIA DE MURIAÉ', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (188, 'SSJTAF', 'SUBSEÇÃO JUDICIÁRIA DE TEIXEIRA DE FREITAS', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (189, 'SSJBAG', 'SUBSEÇÃO JUDICIÁRIA DE BARRA DO GARÇAS', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (190, 'SSJFLO', 'SUBSEÇÃO JUDICIÁRIA DE FLORIANO', 13);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (191, 'SSJTFE', 'SUBSEÇÃO JUDICIÁRIA DE TEFÉ', 4);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (192, 'SSJPGN', 'SUBSEÇÃO JUDICIÁRIA DE PARAGOMINAS', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (193, 'SSJALH', 'SUBSEÇÃO JUDICIÁRIA DE ALAGOINHAS', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (194, 'SSJIUB', 'SUBSEÇÃO JUDICIÁRIA DE ITUMBIARA', 8);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (195, 'SSJPNV', 'SUBSEÇÃO JUDICIÁRIA DE PONTE NOVA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (196, 'SSJVCS', 'SUBSEÇÃO JUDICIÁRIA DE VIÇOSA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (197, 'SSJTUU', 'SUBSEÇÃO JUDICIÁRIA DE TUCURUÍ', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (198, 'SSJIAB', 'SUBSEÇÃO JUDICIÁRIA DE ITAITUBA', 12);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (200, 'TUREC1', 'TURMA RECURSAL ÚNICA DA SJ RORAIMA', 199);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (201, 'RLTR101', 'PRIMEIRA RELATORIA DA  TURMA RECURSAL ÚNICA DA SJ RORAIMA', 200);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (202, 'RLTR102', 'SEGUNDA RELATORIA DA  TURMA RECURSAL ÚNICA DA SJ RORAIMA', 200);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (203, 'RLTR103', 'TERCEIRA RELATORIA DA  TURMA RECURSAL ÚNICA DA SJ RORAIMA', 200);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (204, 'SSJJNA', 'SUBSEÇÃO JUDICIÁRIA DE JUÍNA', 11);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (205, 'SECON', 'SEÇÃO DE APOIO AO NÚCLEO ESTADUAL DE MÉTODOS CONSENSUAIS DE SOLUÇÕES DE CONFLITOS E CIDADANIA', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (206, 'SSJVHA', 'SUBSEÇÃO JUDICIÁRIA DE VILHENA', 14);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (207, 'SSJBMP', 'SUBSEÇÃO JUDICIÁRIA DE BOM JESUS DA LAPA', 6);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (208, 'SSJSRN', 'SUBSEÇÃO JUDICIÁRIA DE SÃO RAIMUNDO NONATO', 13);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (209, 'SSJJUA', 'SUBSEÇÃO JUDICIÁRIA DE JANAÚBA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (210, 'SSJIUA', 'SUBSEÇÃO JUDICIÁRIA DE ITUIUTABA', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (211, 'SSJCZU', 'SUBSEÇÃO JUDICIÁRIA DE CRUZEIRO DO SUL', 3);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (212, 'SSJBLA', 'SUBSEÇÃO JUDICIÁRIA DE BALSAS', 9);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (213, 'SSJPCS', 'SUBSEÇÃO JUDICIÁRIA DE POÇOS DE CALDAS', 10);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (214, 'SSJCNT', 'SUBSEÇÃO JUDICIÁRIA DE CORRENTE', 13);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (215, '4ª VARA', '4ª VARA DA SJRR', 15);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (216, 'GABJU', 'GABINETE DE JUIZ FEDERAL', 215);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (217, 'GAJUS', 'GABINETE DE JUIZ FEDERAL SUBSTITUTO', 215);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (218, 'SECVA', 'SECRETARIA DA VARA', 215);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (219, 'SAD', 'SERVIÇO DE ATIVIDADES DESTACADAS', 218);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (220, 'SESUD', 'SEÇÃO DE SUPORTE ADMINISTRATIVO', 218);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (221, 'SEPOD', 'SEÇÃO DE PROCESSAMENTO E PROCEDIMENTOS DIVERSOS', 218);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (222, 'SEXEC', 'SEÇÃO DE EXECUÇÕES', 218);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (223, 'SEPIP', 'SEÇÃO DE PROTOCOLO E INFORMAÇÕES PROCESSUAIS', 218);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (224, 'NUCJU', 'NÚCLEO JUDICIÁRIO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (225, 'SECON', 'SEÇÃO DE APOIO AO NÚCLEO ESTADUAL DE MÉTODOS CONSENSUAIS DE SOLUÇÕES DE CONFLITOS E CIDADANIA', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (226, 'CEJUC', 'CENTRO JUDICIÁRIO DE CONCILIAÇÃO', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (227, 'NUCAD', 'NÚCLEO DE ADMINISTRAÇÃO', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (228, 'SEPAG', 'SEÇÃO DE PAGAMENTO DE PESSOAL', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (229, 'SECAP', 'SEÇÃO DE CADASTRO DE PESSOAL', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (230, 'SELEP', 'SEÇÃO DE LEGISLAÇÃO DE PESSOAL', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (231, 'SEDER', 'SEÇÃO DE DESENVOLVIMENTO E AVALIAÇÃO DE RECURSOS HUMANOS', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (232, 'SEBES', 'SEÇÃO DE BEM-ESTAR SOCIAL', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (233, 'SEINF', 'SEÇÃO DE TECNOLOGIA DA INFORMAÇÃO', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (234, 'SEPOF', 'SEÇÃO DE PROGRAMAÇÃO E EXECUÇÃO ORÇAMENTÁRIA E FINANCEIRA', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (235, 'SEMAP', 'SEÇÃO DE MATERIAL E PATRIMÔNIO', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (236, 'SELIT', 'SEÇÃO DE COMPRAS E LICITAÇÕES', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (237, 'SESEG', 'SEÇÃO DE SERVIÇOS GERAIS', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (238, 'SECAM', 'SEÇÃO DE COMUNICAÇÕES E ARQUIVO ADMINISTRATIVO', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (239, 'SEVIT', 'SEÇÃO DE SEGURANÇA, VIGILÂNCIA E TRANSPORTE', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (240, 'SEPCE', 'SEÇÃO DE PROTOCOLO E CERTIDÕES', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (241, 'SECLA', 'SEÇÃO DE CLASSIFICAÇÃO E DISTRIBUIÇÃO', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (242, 'SECAJ', 'SEÇÃO DE CÁLCULOS JUDICIAIS', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (243, 'SEDAJ', 'SEÇÃO DE DEPÓSITO E ARQUIVOS JUDICIAL E ADMINISTRATIVO', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (244, 'CEMAN', 'CENTRAL DE MANDADOS', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (245, 'ASJUR', 'ASSESSORIA JURÍDICA E LEGISLAÇÃO DE PESSOAL', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (246, 'SEAUD', 'SEÇÃO DE AUDITORIA INTERNA', 70);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (247, 'SEPVI', 'SEÇÃO DE CUMPRIMENTO DE CARTAS PRECATÓRIAS E VIDEOCONFERÊNCIAS', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (248, 'SEATE', 'SEÇÃO DE ATENDIMENTO, PROTOCOLO, CERTIDÕES E CARTAS PRECATÓRIAS', 224);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (249, 'SERAT', 'SERVIÇO DE ATENDIMENTO', 248);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (250, 'SERPAG', 'SERVIÇO DE PAGAMENTO DE PESSOAL', 228);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (251, 'SERSUT', 'SERVIÇO DE SUPORTE TÉCNICO AOS USUÁRIOS', 233);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (252, 'SEFIC', 'SEÇÃO DE FISCALIZAÇÃO DE CONTRATOS', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (253, 'NUCAF', 'NÚCLEO DE ADMINISTRAÇÃO ORÇAMENTÁRIA, FINANCEIRA E PATRIMONIAL', 77);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (254, 'SEOFI', 'SEÇÃO DE EXECUÇÃO ORÇAMENTÁRIA E FINANCEIRA', 253);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (255, 'SEMAP', 'SEÇÃO DE MATERIAL E PATRIMÔNIO', 253);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (256, 'SERCON', 'SERVIÇO DE CONTABILIDADE', 253);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (257, 'GABEX', 'GABINETE EXECUTIVO DE APOIO AO CENTRO JUDICIÁRIO DE CONCILIAÇÃO', 226);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (258, 'SEPCON', 'SETOR DE PROCESSAMENTO E PROCEDIMENTOS DE CONCILIAÇÃO', 257);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (259, 'SEXEC', 'SEÇÃO DE EXECUÇÕES', 140);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (260, 'SEPOL', 'SEÇÃO DE POLÍCIA JUDICIAL', 227);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (261, 'SERSIN', 'SERVIÇO DE INTELIGÊNCIA', 260);
INSERT INTO sispontodb.lotacoes(id, sigla, descricao, id_lotacao_pai)
VALUES (262, 'SERINP', 'SERVIÇO DE INFORMAÇÕES PROCESSUAIS', 140);