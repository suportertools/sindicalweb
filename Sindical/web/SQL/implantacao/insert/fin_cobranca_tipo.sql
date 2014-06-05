INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 1,'ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 1);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 2,'EMPRESA COM ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 2);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 3,'EMPRESA SEM ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 3);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 4,'EMAIL PARA OS ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 4);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 5,'EMAIL PARA AS EMPRESAS' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 5);
SELECT setval('fin_conbranca_tipo_id_seq', max(id)) FROM fin_conbranca_tipo;

