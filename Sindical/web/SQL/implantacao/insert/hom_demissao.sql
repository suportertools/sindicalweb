
-- hom_demissao 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO hom_demissao (id, ds_descricao) SELECT 1, 'INICIATIVA DO EMPREGADO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 1);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 2, 'INICIATIVA DA EMPRESA' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 2);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 3, 'JUSTA CAUSA' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 3);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 4, 'CONTRATO DETERMINADO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 4);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 5, 'FALECIMENTO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 5);
SELECT setval('hom_demissao_id_seq', max(id)) FROM hom_demissao;

