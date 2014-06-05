-- soc_motivo_inativacao 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 1, 'INADIMPLÊNCIA' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 1);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 2, 'SOLICITAÇÃO' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 2);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 3, 'OPOSIÇÃO' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 3);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 4, 'MUDANÇA DE CATEGORIA' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 4);
SELECT setval('soc_motivo_inativacao_id_seq', max(id)) FROM soc_motivo_inativacao;

