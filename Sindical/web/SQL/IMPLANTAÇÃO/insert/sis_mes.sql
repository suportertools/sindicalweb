-- DELETE FROM SIS_MES

-- sis_mes
-- Criate: 2014-05-22
-- Last edition: 2014-05-22 - by: Bruno Vieira

INSERT INTO sis_mes (id, ds_descricao) SELECT 1, 'Janeiro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 1 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 2, 'Fevereiro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 2 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 3, 'Março' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 3 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 4, 'Abril' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 4 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 5, 'Maio' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 5 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 6, 'Junho' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 6 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 7, 'Julho' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 7 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 8, 'Agosto' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 8 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 9, 'Setembro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 9 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 10, 'Outubro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 10 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 11, 'Novembro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 11 );
INSERT INTO sis_mes (id, ds_descricao) SELECT 12, 'Dezembro' WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 12 );
SELECT setval('sis_mes_id_seq', max(id)) FROM sis_mes;