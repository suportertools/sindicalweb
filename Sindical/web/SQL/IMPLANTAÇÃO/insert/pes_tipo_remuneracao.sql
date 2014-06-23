

INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 1, 'Mensalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 1);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 2, 'Quinzenalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 2);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 3, 'Semanalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 3);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 4, 'Diarista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 4);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 5, 'Horista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 5);
SELECT setval('fin_tipo_remuneracao_id_seq', max(id)) FROM fin_tipo_remuneracao;






