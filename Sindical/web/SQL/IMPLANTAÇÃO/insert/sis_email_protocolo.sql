-- sis_email_protocolo 
-- Criate: 2014-03-25
-- Last edition: 2014-03-2 - by: Bruno Vieira

-- DELETE FROM sis_email_protocolo

INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 1,'NENHUMA' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 1);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 2,'STARTTLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 2);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 3,'SSL/TLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 3);
SELECT setval('sis_email_protocolo_id_seq', max(id)) FROM sis_email_protocolo;

