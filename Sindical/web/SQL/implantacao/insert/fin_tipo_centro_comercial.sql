-- pes_tipo_centro_comercial 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 1, 'SHOPPING' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 1);
INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 2, 'GALERIA' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 2);
INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 3, 'RODOVIARIA' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 3);
SELECT setval('pes_tipo_centro_comercial_id_seq', max(id)) FROM pes_tipo_centro_comercial;

