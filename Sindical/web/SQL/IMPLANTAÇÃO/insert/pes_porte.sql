
-- pes_porte 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pes_porte (id, ds_descricao) SELECT 1, 'ME' WHERE NOT EXISTS ( SELECT id FROM pes_porte WHERE id = 1);
INSERT INTO pes_porte (id, ds_descricao) SELECT 2, 'EPP' WHERE NOT EXISTS ( SELECT id FROM pes_porte WHERE id = 2);
SELECT setval('pes_porte_id_seq', max(id)) FROM pes_porte;
