
-- arr_motivo_inativacao
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM arr_motivo_inativacao;


INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 1, 'FECHOU'           WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 1);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 2, 'SEM EMPREGADO'    WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 2);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 3, 'NÃO ENCONTRADA'   WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 3);
SELECT setval('arr_motivo_inativacao_id_seq', max(id)) FROM arr_motivo_inativacao;

