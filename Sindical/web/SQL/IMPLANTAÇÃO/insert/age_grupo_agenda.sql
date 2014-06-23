-- age_grupo_agenda;


-- DELETE FROM age_grupo_agenda;

INSERT INTO age_grupo_agenda (id, ds_descricao) SELECT 1, 'Trabalho'   WHERE NOT EXISTS ( SELECT id FROM age_grupo_agenda WHERE id = 1);
INSERT INTO age_grupo_agenda (id, ds_descricao) SELECT 2, 'Empresa'    WHERE NOT EXISTS ( SELECT id FROM age_grupo_agenda WHERE id = 2);
SELECT setval('age_grupo_agenda_id_seq', max(id)) FROM age_grupo_agenda;
