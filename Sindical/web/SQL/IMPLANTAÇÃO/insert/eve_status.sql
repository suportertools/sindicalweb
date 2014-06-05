
-- eve_status 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO eve_status (id, ds_descricao) SELECT 1, 'Disponível' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 1);
INSERT INTO eve_status (id, ds_descricao) SELECT 2, 'Reservado' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 2);
INSERT INTO eve_status (id, ds_descricao) SELECT 3, 'Vendido' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 3);
SELECT setval('eve_status_id_seq', max(id)) FROM eve_status;

