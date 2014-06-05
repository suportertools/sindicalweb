-- seg_evento
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO seg_evento (id, ds_descricao) SELECT 1, 'Inclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 1);
INSERT INTO seg_evento (id, ds_descricao) SELECT 2, 'Exclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 2);
INSERT INTO seg_evento (id, ds_descricao) SELECT 3, 'Alteração' WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 3);
INSERT INTO seg_evento (id, ds_descricao) SELECT 4, 'Consulta'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 4);
SELECT setval('seg_evento_id_seq', max(id)) FROM seg_evento;
