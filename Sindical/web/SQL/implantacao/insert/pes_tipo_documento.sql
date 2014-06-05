
-- pes_tipo_documento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 1, 'CPF' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 1);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 2, 'CNPJ' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 2);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 3, 'CEI' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 3);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 4, 'SEM DOCUMENTO' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 4);
SELECT setval('pes_tipo_documento_id_seq', max(id)) FROM pes_tipo_documento;
