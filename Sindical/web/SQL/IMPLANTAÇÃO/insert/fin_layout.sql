-- fin_layout 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO fin_layout (id, ds_descricao, url) SELECT 1, 'SICOB', '/Relatorios/SICOB.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 1);
INSERT INTO fin_layout (id, ds_descricao, url) SELECT 2, 'SINDICAL', '/Relatorios/SINDICAL.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 2);
INSERT INTO fin_layout (id, ds_descricao, url) SELECT 3, 'SIGCB', '/Relatorios/SICOB.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 3);
