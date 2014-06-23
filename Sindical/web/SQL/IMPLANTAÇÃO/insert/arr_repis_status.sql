
-- arr_repis_status
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM arr_repis_status

INSERT INTO arr_repis_status (id, ds_descricao) SELECT 1, 'Andamento' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 1);
INSERT INTO arr_repis_status (id, ds_descricao) SELECT 2, 'Recusado' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 2);
INSERT INTO arr_repis_status (id, ds_descricao) SELECT 3, 'Autorizado' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 3);
SELECT setval('arr_motivo_inativacao_id_seq', max(id)) FROM arr_motivo_inativacao;

