-- pro_prioridade 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pro_prioridade (id, ds_descricao) SELECT 1, 'ALTA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 1);
INSERT INTO pro_prioridade (id, ds_descricao) SELECT 2, 'MÉDIA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 2);
INSERT INTO pro_prioridade (id, ds_descricao) SELECT 3, 'BAIXA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 3);
SELECT setval('pro_prioridade_id_seq', max(id)) FROM pro_prioridade;

