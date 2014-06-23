
INSERT INTO pes_raca (id, ds_descricao) SELECT 1, 'Amarela' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 1);
INSERT INTO pes_raca (id, ds_descricao) SELECT 2, 'Branca' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 2);
INSERT INTO pes_raca (id, ds_descricao) SELECT 3, 'Índigena' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 3);
INSERT INTO pes_raca (id, ds_descricao) SELECT 4, 'Parda' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 4);
INSERT INTO pes_raca (id, ds_descricao) SELECT 5, 'Preta' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 5);
SELECT setval('pes_raca_id_seq', max(id)) FROM pes_raca;

