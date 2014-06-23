-- hom_status;

-- DELETE FROM hom_status

INSERT INTO hom_status (id, ds_descricao) SELECT 1, 'Disponível'        WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 1);
INSERT INTO hom_status (id, ds_descricao) SELECT 2, 'Agendado'          WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 2);
INSERT INTO hom_status (id, ds_descricao) SELECT 3, 'Cancelado'         WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 3);
INSERT INTO hom_status (id, ds_descricao) SELECT 4, 'Homologado'        WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 4);
INSERT INTO hom_status (id, ds_descricao) SELECT 5, 'Atendimento'       WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 5);
INSERT INTO hom_status (id, ds_descricao) SELECT 6, 'Encaixe'           WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 6);
INSERT INTO hom_status (id, ds_descricao) SELECT 7, 'Não Compareceu'    WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 7);
SELECT setval('hom_status_id_seq', max(id)) FROM hom_status;