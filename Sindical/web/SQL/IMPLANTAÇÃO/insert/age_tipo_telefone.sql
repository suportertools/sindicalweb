-- DELETE FROM age_tipo_telefone;

INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 1, 'Comercial'   WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 1);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 2, 'Contato'     WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 2);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 3, 'Celular'     WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 3);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 4, 'Fax'         WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 4);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 5, 'Fone/FAX'    WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 5);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 6, 'Residencial' WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 6);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 7, 'Outros'      WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 7);
SELECT setval('age_tipo_telefone_id_seq', max(id)) FROM age_tipo_telefone;