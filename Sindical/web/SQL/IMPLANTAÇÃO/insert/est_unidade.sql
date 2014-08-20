
INSERT INTO est_unidade (id, ds_descricao) SELECT 1, 'UN' WHERE NOT EXISTS ( SELECT id FROM est_unidade WHERE id = 1);
INSERT INTO est_unidade (id, ds_descricao) SELECT 2, 'FD' WHERE NOT EXISTS ( SELECT id FROM est_unidade WHERE id = 2);
INSERT INTO est_unidade (id, ds_descricao) SELECT 3, 'CX' WHERE NOT EXISTS ( SELECT id FROM est_unidade WHERE id = 3);
INSERT INTO est_unidade (id, ds_descricao) SELECT 4, 'KL' WHERE NOT EXISTS ( SELECT id FROM est_unidade WHERE id = 4);
INSERT INTO est_unidade (id, ds_descricao) SELECT 5, 'MT' WHERE NOT EXISTS ( SELECT id FROM est_unidade WHERE id = 5);
SELECT setval('est_unidade_id_seq', max(id)) FROM est_unidade;

