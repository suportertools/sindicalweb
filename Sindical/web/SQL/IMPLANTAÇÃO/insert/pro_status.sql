-- pro_status 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pro_status (id, ds_descricao) SELECT 1, 'ABERTO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 1);
INSERT INTO pro_status (id, ds_descricao) SELECT 2, 'PROCESSO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 2);
INSERT INTO pro_status (id, ds_descricao) SELECT 3, 'PARADO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 3);
INSERT INTO pro_status (id, ds_descricao) SELECT 4, 'CONCLUÍDO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 4);
SELECT setval('pro_status_id_seq', max(id)) FROM pro_status;
