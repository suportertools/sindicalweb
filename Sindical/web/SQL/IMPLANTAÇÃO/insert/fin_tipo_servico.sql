-- fin_tipo_servico 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 1, 'Principal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 1);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 2, 'Admissional' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 2);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 3, 'Complemento' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 3);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 4, 'Acordo' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 4);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 5, 'Taxa' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 5);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 6, 'Multa' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 6);
SELECT setval('fin_tipo_servico_id_seq', max(id)) FROM fin_tipo_servico;
