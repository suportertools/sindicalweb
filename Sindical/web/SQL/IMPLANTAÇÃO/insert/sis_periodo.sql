-- DELETE FROM sis_periodo

-- sis_periodo
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 1, 'DIÁRIO', 1 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 1 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 2, 'SEMANAL', 7 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 2 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 3, 'MENSAL', 30 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 3 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 4, 'BIMESTRAL', 60  WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 4 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 5, 'TRIMESTRAL', 90 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 5 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 6, 'SEMESTRAL', 180 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 6 );
INSERT INTO sis_periodo (id, ds_descricao, nr_dias) SELECT 7, 'ANUAL', 365 WHERE NOT EXISTS ( SELECT id FROM sis_periodo WHERE id = 7 );
SELECT setval('sis_periodo_id_seq', max(id)) FROM sis_periodo;
