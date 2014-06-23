
-- ate_operacao
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira


-- DELETE FROM ate_operacao

INSERT INTO ate_operacao (id, ds_descricao) SELECT 1, 'Calculos' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 1);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 2, 'Juridico' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 2);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 3, 'Colônia de Férias' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 3);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 4, 'Filiações' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 4);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 5, 'Outros' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 5);
SELECT setval('ate_operacao_id_seq', max(id)) FROM ate_operacao;

