-- fin_indice 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM fin_indice;

INSERT INTO fin_indice (id, ds_descricao) SELECT 1, 'ICV (Dieese)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 1);
INSERT INTO fin_indice (id, ds_descricao) SELECT 2, 'IGP-DI (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 2);
INSERT INTO fin_indice (id, ds_descricao) SELECT 3, 'IPC do IGP (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 3);
INSERT INTO fin_indice (id, ds_descricao) SELECT 4, 'IPC (Fipe)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 4); 
INSERT INTO fin_indice (id, ds_descricao) SELECT 5, 'CUB (Sinduscon)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 5); 
INSERT INTO fin_indice (id, ds_descricao) SELECT 6, 'IGP-M (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 6);
INSERT INTO fin_indice (id, ds_descricao) SELECT 7, 'IPCA (IBGE)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 7);
INSERT INTO fin_indice (id, ds_descricao) SELECT 8, 'INPC (IBGE)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 8);
INSERT INTO fin_indice (id, ds_descricao) SELECT 9, 'INCC-DI (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 9);
INSERT INTO fin_indice (id, ds_descricao) SELECT 10, 'IPC-r (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 10);
INSERT INTO fin_indice (id, ds_descricao) SELECT 11, 'IPC (IBGE)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 11);
INSERT INTO fin_indice (id, ds_descricao) SELECT 12, 'IPA-DI (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 12);
INSERT INTO fin_indice (id, ds_descricao) SELECT 13, 'IPA-M (FGV)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 13);
INSERT INTO fin_indice (id, ds_descricao) SELECT 14, 'IPCA-E (IBGE)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 14);
INSERT INTO fin_indice (id, ds_descricao) SELECT 15, 'TR (Bacen)' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 15);
INSERT INTO fin_indice (id, ds_descricao) SELECT 16, 'Não Contém' WHERE NOT EXISTS ( SELECT id FROM fin_indice WHERE id = 16);
SELECT setval('fin_indice_id_seq', max(id)) FROM fin_indice;

