INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 1, 'IMPOSTO' WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo WHERE id = 1);
INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 2, 'ESTOQUE' WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo WHERE id = 2);
INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 3, 'PATRIMÔNIO' WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo WHERE id = 3);
SELECT setval('fin_conta_tipo_id_seq', max(id)) FROM fin_conta_tipo;

