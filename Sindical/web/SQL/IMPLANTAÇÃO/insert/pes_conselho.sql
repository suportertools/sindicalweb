
-- pes_conselho 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

-- DELETE FROM pes_conselho

INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 1, 'CREA', 'Conselho Regional de Arquitetura' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 1);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 2, 'CRM', 'Conselho Regional de Medicina' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 2);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 3, 'CRP', 'Conselho Regional de Psicologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 3);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 4, 'OAB', 'Ordem dos Advogados do Brasil' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 4);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 5, 'CRC', 'Conselho Regional de Contabilidade' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 5);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 6, 'CRA', 'Conselho Regional de Administração' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 6);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 7, 'CRB', 'Conselho Regional de Biblioteconomia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 7);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 8, 'CRBio', 'Conselho Regional de Biologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 8);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 9, 'CORECON', 'Conselho Regional de Economia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 9);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 10, 'COREN', 'Conselho Regional de Enfermagem' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 10);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 11, 'CREA', 'Conselho Regional de Engenharia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 11);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 12, 'CRF', 'Conselho Regional de Farmácia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 12);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 13, 'CREFITO', 'Conselho Regional de Fisioterapia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 13);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 14, 'CRFa', 'Conselho Regional de Fonoaudiologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 14);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 15, 'CRM', 'Conselho Regional de Medicina' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 15);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 16, 'COREM', 'Conselho Regional de Museologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 16);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 17, 'CRN', 'Conselho Regional de Nutrição' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 17);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 18, 'CRO', 'Conselho Regional de Odontologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 18);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 19, 'CRP', 'Conselho Regional de Psicologia' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 19);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 20, 'CRQ', 'Conselho Regional de Química' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 20);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 21, 'CONRERP', 'Conselho Regional de Relações Públicas' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 21);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 22, 'CRV', 'Conselho Regional de Veterinária' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 22);
INSERT INTO pes_conselho (id, ds_conselho, ds_tipo_conselho) SELECT 23, 'OAB', 'Ordem dos Advogados do Brasil' WHERE NOT EXISTS ( SELECT id FROM pes_conselho WHERE id = 23);
SELECT setval('pes_conselho_id_seq', max(id)) FROM pes_conselho;
