

-- esc_status 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO esc_status (id, ds_descricao) SELECT 1, 'Frequente' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 1);
INSERT INTO esc_status (id, ds_descricao) SELECT 2, 'Concluinte' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 2);
INSERT INTO esc_status (id, ds_descricao) SELECT 3, 'Desistente' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 3);
INSERT INTO esc_status (id, ds_descricao) SELECT 4, 'Trancado' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 4);
SELECT setval('esc_status_id_seq', max(id)) FROM esc_status;
