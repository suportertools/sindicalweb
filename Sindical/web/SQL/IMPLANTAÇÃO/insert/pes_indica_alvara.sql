-- pes_indicador_alvara
-- Criate: 2013-08-02
-- Last edition: 2014-06-25 - by: Bruno Vieira

-- DELETE FROM pes_indicador_alvara

INSERT INTO pes_indicador_alvara (id, ds_descricao) SELECT 1, 'O funcionário tem alvará judicial para trabalhar' WHERE NOT EXISTS ( SELECT id FROM pes_indicador_alvara WHERE id = 1);
INSERT INTO pes_indicador_alvara (id, ds_descricao) SELECT 2, 'O funcionário não tem alvará judicial para trabalhar' WHERE NOT EXISTS ( SELECT id FROM pes_indicador_alvara WHERE id = 2);
SELECT setval('pes_indicador_alvara_id_seq', max(id)) FROM pes_indicador_alvara;
