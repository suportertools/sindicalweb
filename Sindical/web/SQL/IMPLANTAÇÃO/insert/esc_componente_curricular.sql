

-- esc_componente_curricular 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 1, 'Matemática' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 1);
INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 2, 'Português' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 2);
INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 3, 'Inglês' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 3);
INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 4, 'Windows' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 4);
INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 5, 'Word' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 5);
INSERT INTO esc_componente_curricular (id, ds_descricao) SELECT 6, 'Excel' WHERE NOT EXISTS ( SELECT id FROM esc_componente_curricular WHERE id = 6);
SELECT setval('esc_componente_curricular_id_seq', max(id)) FROM esc_componente_curricular;
