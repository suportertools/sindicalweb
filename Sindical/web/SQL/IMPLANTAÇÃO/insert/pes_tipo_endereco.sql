-- arr_repis_status
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM pes_tipo_endereco

INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 1, 'Residencial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 1);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 2, 'Comercial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 2);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 3, 'Cobrança' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 3);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 4, 'Correspondência' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 4);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 5, 'Base Territorial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 5);
SELECT setval('pes_tipo_endereco_id_seq', max(id)) FROM pes_tipo_endereco;

