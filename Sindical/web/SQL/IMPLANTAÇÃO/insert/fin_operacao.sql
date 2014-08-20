INSERT INTO fin_operacao (id, ds_descricao) SELECT 1, 'Receita' WHERE NOT EXISTS ( SELECT id FROM fin_operacao WHERE id = 1);
INSERT INTO fin_operacao (id, ds_descricao) SELECT 2, 'Despesa' WHERE NOT EXISTS ( SELECT id FROM fin_operacao WHERE id = 2);
INSERT INTO fin_operacao (id, ds_descricao) SELECT 3, 'Empréstimos' WHERE NOT EXISTS ( SELECT id FROM fin_operacao WHERE id = 3);
INSERT INTO fin_operacao (id, ds_descricao) SELECT 4, 'Bens' WHERE NOT EXISTS ( SELECT id FROM fin_operacao WHERE id = 4);
INSERT INTO fin_operacao (id, ds_descricao) SELECT 5, 'Estoque' WHERE NOT EXISTS ( SELECT id FROM fin_operacao WHERE id = 5);
SELECT setval('fin_operacao_id_seq', max(id)) FROM fin_operacao;
