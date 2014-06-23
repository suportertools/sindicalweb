
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 1, 'Brasileiro' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 1);
SELECT setval('pes_nacionalidade_id_seq', max(id)) FROM pes_nacionalidade;

