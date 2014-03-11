INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 6, 'Multa' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 6);
SELECT setval('fin_tipo_servico_id_seq', max(id)) FROM fin_tipo_servico;

INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 227, 'RESCISÃO DE CONTRATO', '"/Sindical/rescisaoMatriculaEscola.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 227);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;
