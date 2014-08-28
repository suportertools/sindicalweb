-- DELETE FROM ate_status

INSERT INTO ate_status (id, ds_descricao) SELECT 1, 'Aguardando' WHERE NOT EXISTS ( SELECT id FROM ate_status WHERE id = 1);
INSERT INTO ate_status (id, ds_descricao) SELECT 2, 'Atendido' WHERE NOT EXISTS ( SELECT id FROM ate_status WHERE id = 2);
INSERT INTO ate_status (id, ds_descricao) SELECT 3, 'Atendimento Cancelado' WHERE NOT EXISTS ( SELECT id FROM ate_status WHERE id = 3);
INSERT INTO ate_status (id, ds_descricao) SELECT 4, 'Atendimento' WHERE NOT EXISTS ( SELECT id FROM ate_status WHERE id = 4);
SELECT setval('ate_status_id_seq', max(id)) FROM ate_status;
