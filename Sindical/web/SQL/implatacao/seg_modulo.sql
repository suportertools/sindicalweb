-- seg_modulo
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO seg_modulo (id, ds_descricao) SELECT 1, 'Financeiro'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 1);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 2, 'Social'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 2);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 3, 'Arrecadação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 3);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 4, 'Homologação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 4);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 5, 'Jurídico'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 5);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 6, 'Clube'             WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 6);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 7, 'Academia'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 7);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 8, 'Escola'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 8);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 9, 'Cadastro Auxiliar' WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 9);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 10, 'Segurança'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 10);
SELECT setval('seg_modulo_id_seq', max(id)) FROM seg_modulo;

