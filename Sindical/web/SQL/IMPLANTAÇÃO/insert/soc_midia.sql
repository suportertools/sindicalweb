
-- soc_midia
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- delete from soc_midia

INSERT INTO soc_midia (id, ds_descricao) SELECT 1, 'Jornal' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 1);
INSERT INTO soc_midia (id, ds_descricao) SELECT 2, 'Revista' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 2);
INSERT INTO soc_midia (id, ds_descricao) SELECT 3, 'Internet' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 3);
INSERT INTO soc_midia (id, ds_descricao) SELECT 4, 'Panfleto' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 4);
SELECT setval('soc_midia_id_seq', max(id)) FROM soc_midia;
