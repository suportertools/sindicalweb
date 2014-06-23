
-- DELETE FROM est_tipo

INSERT INTO est_tipo (id, ds_descricao) SELECT 1,'CONSUMO' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 1);
INSERT INTO est_tipo (id, ds_descricao) SELECT 2,'VENDAS' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 2);
INSERT INTO est_tipo (id, ds_descricao) SELECT 3,'BRINDES' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 3);
SELECT setval('est_tipo_id_seq', max(id)) FROM est_tipo;

