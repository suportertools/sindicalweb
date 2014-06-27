
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 1, 'Analfabeto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 1);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 2, 'Sabe ler/escrever, mas não cursou escola' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 2);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 3, 'Até a 4ª série do ensino fundamental incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 3);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 4, 'Até a 4ª série do ensino fundamental completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 4);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 5, 'De 5ª à 8ª série do ensino fundamental/antigo/ginásio incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 5);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 6, 'De 5ª à 8ª série do ensino fundamental/antigo/ginásio completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 6);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 7, 'Ensino médio/colegial/1ª a 3ª série incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 7);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 8, 'Ensino médio/colegial/1ª a 3ª série completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 8);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 9, 'Superior incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 9);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 10, 'Superior completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 10);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 11, 'Pós-graduação / Mestrado / Doutorado' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 11);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 12, '(NR)' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 12);
SELECT setval('pes_escolaridade_id_seq', max(id)) FROM pes_escolaridade;

