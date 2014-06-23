-- DROP TABLE sis_semana;
-- sis_semana
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO sis_semana (id, ds_descricao) SELECT 1, 'Domingo'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 1 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 2, 'Segunda'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 2 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 3, 'Terça'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 3 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 4, 'Quarta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 4 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 5, 'Quinta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 5 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 6, 'Sexta'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 6 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 7, 'Sábado'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 7 );

SELECT setval('sis_semana_id_seq', max(id)) FROM sis_semana;
