
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 1, 'A1', 20, null WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 1);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 2, 'A2', 20, null WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 2);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 3, 'B1', 10, 20 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 3);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 4, 'C1', 10, 20 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 4);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 5, 'C2', 4, 10 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 5);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 6, 'D', 4, 10 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 6);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 7, 'E', 2, 4 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 7);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 8, 'Recusa', 0, 2 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 8);
SELECT setval('pes_classificacao_economica_id_seq', max(id)) FROM pes_classificacao_economica;

