-- fin_status 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_status (id, ds_descricao) SELECT 1, 'EFETIVO'   WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 1);
INSERT INTO fin_status (id, ds_descricao) SELECT 2, 'PEDIDO'    WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 2);
INSERT INTO fin_status (id, ds_descricao) SELECT 3, 'BLOQUEADO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 3);
INSERT INTO fin_status (id, ds_descricao) SELECT 4, 'PROVISÃO'  WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 4);
INSERT INTO fin_status (id, ds_descricao) SELECT 5, 'ORÇAMENTO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 5);
INSERT INTO fin_status (id, ds_descricao) SELECT 6, 'TRANFERÊNCIA ENTRE CONTAS'    WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 6);
SELECT setval('fin_status_id_seq', max(id)) FROM fin_status;

-- seg_nivel 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira


INSERT INTO seg_nivel (id, ds_descricao) SELECT 1, 'Usuario'   WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 1);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 2, 'Usuário (Avançado)'    WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 2);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 3, 'Coordenador (Depto)' WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 3);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 4, 'Gerente'  WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 4);
INSERT INTO seg_nivel (id, ds_descricao) SELECT 5, 'Administrador' WHERE NOT EXISTS ( SELECT id FROM seg_nivel WHERE id = 5);
SELECT setval('seg_nivel_id_seq', max(id)) FROM seg_nivel;

-- arr_motivo_inativacao
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 1, 'FECHOU'           WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 1);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 2, 'SEM EMPREGADO'    WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 2);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 3, 'NÃO ENCONTRADA'   WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 3);
SELECT setval('arr_motivo_inativacao_id_seq', max(id)) FROM arr_motivo_inativacao;

-- arr_repis_status
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO arr_repis_status (id, ds_descricao) SELECT 1, 'Andamento' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 1);
INSERT INTO arr_repis_status (id, ds_descricao) SELECT 2, 'Recusado' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 2);
INSERT INTO arr_repis_status (id, ds_descricao) SELECT 3, 'Autorizado' WHERE NOT EXISTS ( SELECT id FROM arr_repis_status WHERE id = 3);
SELECT setval('arr_motivo_inativacao_id_seq', max(id)) FROM arr_motivo_inativacao;


-- arr_repis_status
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 1, 'Residencial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 1);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 2, 'Comercial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 2);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 3, 'Cobrança' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 3);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 4, 'Correspondência' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 4);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 5, 'Base Territorial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 5);
SELECT setval('pes_tipo_endereco_id_seq', max(id)) FROM pes_tipo_endereco;

-- soc_midia
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO soc_midia (id, ds_descricao) SELECT 1, 'Jornal' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 1);
INSERT INTO soc_midia (id, ds_descricao) SELECT 2, 'Revista' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 2);
INSERT INTO soc_midia (id, ds_descricao) SELECT 3, 'Internet' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 3);
INSERT INTO soc_midia (id, ds_descricao) SELECT 4, 'Panfleto' WHERE NOT EXISTS ( SELECT id FROM soc_midia WHERE id = 4);
SELECT setval('soc_midia_id_seq', max(id)) FROM soc_midia;

-- ate_operacao
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO ate_operacao (id, ds_descricao) SELECT 1, 'Calculos' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 1);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 2, 'Juridico' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 2);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 3, 'Colônia de Férias' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 3);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 4, 'Filiações' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 4);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 5, 'Outros' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 5);
SELECT setval('ate_operacao_id_seq', max(id)) FROM ate_operacao;

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

-- fin_indice 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

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

-- fin_tipo_servico 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 1, 'Principal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 1);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 2, 'Admissional' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 2);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 3, 'Complemento' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 3);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 4, 'Acordo' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 4);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 5, 'Taxa' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 5);
INSERT INTO fin_tipo_servico (id, ds_descricao) SELECT 6, 'Multa' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_servico WHERE id = 6);
SELECT setval('fin_tipo_servico_id_seq', max(id)) FROM fin_tipo_servico;

-- pes_tipo_centro_comercial 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 1, 'SHOPPING' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 1);
INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 2, 'GALERIA' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 2);
INSERT INTO pes_tipo_centro_comercial (id, ds_descricao) SELECT 3, 'RODOVIARIA' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_centro_comercial WHERE id = 3);
SELECT setval('pes_tipo_centro_comercial_id_seq', max(id)) FROM pes_tipo_centro_comercial;


-- pes_tipo_documento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 1, 'CPF' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 1);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 2, 'CNPJ' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 2);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 3, 'CEI' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 3);
INSERT INTO pes_tipo_documento (id, ds_descricao) SELECT 4, 'SEM DOCUMENTO' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_documento WHERE id = 4);
SELECT setval('pes_tipo_documento_id_seq', max(id)) FROM pes_tipo_documento;

-- pes_tipo_endereco 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 1, 'Residencial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 1);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 2, 'Comercial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 2);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 3, 'Cobrança' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 3);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 4, 'Correspondência' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 4);
INSERT INTO pes_tipo_endereco (id, ds_descricao) SELECT 5, 'Base Territorial' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_endereco WHERE id = 5);
SELECT setval('pes_tipo_endereco_id_seq', max(id)) FROM pes_tipo_endereco;

-- arr_motivo_inativacao 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 1, 'FECHOU' WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 1);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 2, 'SEM EMPREGADO' WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 2);
INSERT INTO arr_motivo_inativacao (id, ds_descricao) SELECT 3, 'NÃO ENCONTRADA' WHERE NOT EXISTS ( SELECT id FROM arr_motivo_inativacao WHERE id = 3);
SELECT setval('arr_motivo_inativacao_id_seq', max(id)) FROM arr_motivo_inativacao;


-- eve_status 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO eve_status (id, ds_descricao) SELECT 1, 'Disponível' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 1);
INSERT INTO eve_status (id, ds_descricao) SELECT 2, 'Reservado' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 2);
INSERT INTO eve_status (id, ds_descricao) SELECT 3, 'Vendido' WHERE NOT EXISTS ( SELECT id FROM eve_status WHERE id = 3);
SELECT setval('eve_status_id_seq', max(id)) FROM eve_status;

-- fin_tipo_documento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 1,  'Nota Fiscal'              WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 1);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 2,  'Boleto'                   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 2);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 10, 'Transferência Bancária'   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 10);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 12, 'Recibo'                   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 12);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 13, 'Extrato Geral'            WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 13);
SELECT setval('fin_tipo_documento_id_seq', max(id)) FROM fin_tipo_documento;

-- fin_tipo_pagamento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 1, 'Nota Fiscal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 1);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 2, 'Boleto' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 2);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 3, 'Dinheiro' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 3);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 4, 'Cheque' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 4);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 5, 'Cheque-Pré' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 5);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 6, 'Cartão de Crédito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 6);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 7, 'Cartão de Débito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 7);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 8, 'Depósito Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 8);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 9, 'Doc Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 9);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 10, 'Trans. Bancária' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 10);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 11, 'Ticket' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 11);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 12, 'Recibo' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 12);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 13, 'Débito Automático' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 13);
SELECT setval('fin_tipo_pagamento_id_seq', max(id)) FROM fin_tipo_pagamento;

-- hom_demissao 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO hom_demissao (id, ds_descricao) SELECT 1, 'INICIATIVA DO EMPREGADO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 1);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 2, 'INICIATIVA DA EMPRESA' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 2);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 3, 'JUSTA CAUSA' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 3);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 4, 'CONTRATO DETERMINADO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 4);
INSERT INTO hom_demissao (id, ds_descricao) SELECT 5, 'FALECIMENTO' WHERE NOT EXISTS ( SELECT id FROM hom_demissao WHERE id = 5);
SELECT setval('hom_demissao_id_seq', max(id)) FROM hom_demissao;


-- esc_status 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO esc_status (id, ds_descricao) SELECT 1, 'Frequente' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 1);
INSERT INTO esc_status (id, ds_descricao) SELECT 2, 'Concluinte' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 2);
INSERT INTO esc_status (id, ds_descricao) SELECT 3, 'Desistente' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 3);
INSERT INTO esc_status (id, ds_descricao) SELECT 4, 'Trancado' WHERE NOT EXISTS ( SELECT id FROM esc_status WHERE id = 4);
SELECT setval('esc_status_id_seq', max(id)) FROM esc_status;

-- fin_condicao_pagamento 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO fin_condicao_pagamento (id, ds_descricao) SELECT 1, 'A vista' WHERE NOT EXISTS ( SELECT id FROM fin_condicao_pagamento WHERE id = 1);
INSERT INTO fin_condicao_pagamento (id, ds_descricao) SELECT 2, 'A prazo' WHERE NOT EXISTS ( SELECT id FROM fin_condicao_pagamento WHERE id = 2);
SELECT setval('fin_condicao_pagamento_id_seq', max(id)) FROM fin_condicao_pagamento;

-- fin_layout 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO fin_layout (id, ds_descricao, url) SELECT 1, 'SICOB', '/Relatorios/SICOB.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 1);
INSERT INTO fin_layout (id, ds_descricao, url) SELECT 2, 'SINDICAL', '/Relatorios/SINDICAL.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 2);
INSERT INTO fin_layout (id, ds_descricao, url) SELECT 3, 'SIGCB', '/Relatorios/SICOB.jasper' WHERE NOT EXISTS ( SELECT id FROM fin_layout WHERE id = 3);

-- pes_conselho 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

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

-- pes_porte 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pes_porte (id, ds_descricao) SELECT 1, 'ME' WHERE NOT EXISTS ( SELECT id FROM pes_porte WHERE id = 1);
INSERT INTO pes_porte (id, ds_descricao) SELECT 2, 'EPP' WHERE NOT EXISTS ( SELECT id FROM pes_porte WHERE id = 2);
SELECT setval('pes_porte_id_seq', max(id)) FROM pes_porte;

-- pro_prioridade 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pro_prioridade (id, ds_descricao) SELECT 1, 'ALTA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 1);
INSERT INTO pro_prioridade (id, ds_descricao) SELECT 2, 'MÉDIA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 2);
INSERT INTO pro_prioridade (id, ds_descricao) SELECT 3, 'BAIXA' WHERE NOT EXISTS ( SELECT id FROM pro_prioridade WHERE id = 3);
SELECT setval('pro_prioridade_id_seq', max(id)) FROM pro_prioridade;

-- pro_status 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO pro_status (id, ds_descricao) SELECT 1, 'ABERTO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 1);
INSERT INTO pro_status (id, ds_descricao) SELECT 2, 'PROCESSO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 2);
INSERT INTO pro_status (id, ds_descricao) SELECT 3, 'PARADO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 3);
INSERT INTO pro_status (id, ds_descricao) SELECT 4, 'CONCLUÍDO' WHERE NOT EXISTS ( SELECT id FROM pro_status WHERE id = 4);
SELECT setval('pro_status_id_seq', max(id)) FROM pro_status;

-- soc_motivo_inativacao 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 1, 'INADIMPLÊNCIA' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 1);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 2, 'SOLICITAÇÃO' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 2);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 3, 'OPOSIÇÃO' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 3);
INSERT INTO soc_motivo_inativacao (id, ds_descricao) SELECT 4, 'MUDANÇA DE CATEGORIA' WHERE NOT EXISTS ( SELECT id FROM soc_motivo_inativacao WHERE id = 4);
SELECT setval('soc_motivo_inativacao_id_seq', max(id)) FROM soc_motivo_inativacao;


-- seg_registro 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO seg_registro (
            id, ds_mensagem_bloqueio_boleto_web, ds_url_path, ds_tipo_empresa, 
            ds_obs_ficha_social, ds_senha, meses_inadimplentes_agenda, bloquear_homologacao, 
            dias_bloqueia_atrasados_web, meses_inadimplentes_impressao_boletos, 
            is_email_autenticado, baixa_vencimento, is_senha_homologacao, 
            ds_documento_homologacao, ds_smtp, ds_forma_pagamento_homologacao, 
            ds_tipo_entidade, is_agendar_sem_horario_web, ds_email, is_enviar_email_anexo, 
            is_bloqueia_atrasados_web, dt_atualiza_homologacao, carteirinha_dependente, 
            id_filial)
    SELECT 1, null, null, '', 
            null, null, 0, null, 
            null, null, 
            false, null, false, 
            null, null, null, 
            null, false, null, false, 
            false, '1900-01-01', null, 
            1 WHERE NOT EXISTS ( SELECT id FROM seg_registro WHERE id = 1);

INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 1,'ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 1);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 2,'EMPRESA COM ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 2);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 3,'EMPRESA SEM ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 3);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 4,'EMAIL PARA OS ESCRITÓRIO' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 4);
INSERT INTO fin_conbranca_tipo (id, ds_descricao) SELECT 5,'EMAIL PARA AS EMPRESAS' WHERE NOT EXISTS ( SELECT id FROM fin_conbranca_tipo WHERE id = 5);
SELECT setval('fin_conbranca_tipo_id_seq', max(id)) FROM fin_conbranca_tipo;

-- sis_email_protocolo 
-- Criate: 2014-03-25
-- Last edition: 2014-03-2 - by: Bruno Vieira

INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 1,'NENHUMA' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 1);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 2,'STARTTLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 2);
INSERT INTO sis_email_protocolo (id, ds_descricao) SELECT 3,'SSL/TLS' WHERE NOT EXISTS ( SELECT id FROM sis_email_protocolo WHERE id = 3);
SELECT setval('sis_email_protocolo_id_seq', max(id)) FROM sis_email_protocolo;


INSERT INTO est_tipo (id, ds_descricao) SELECT 1,'CONSUMO' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 1);
INSERT INTO est_tipo (id, ds_descricao) SELECT 2,'VENDAS' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 2);
INSERT INTO est_tipo (id, ds_descricao) SELECT 3,'BRINDES' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 3);
SELECT setval('est_tipo_id_seq', max(id)) FROM est_tipo;

-- sis_email_prioridade
-- Criate: 2014-03-25
-- Last edition: 2014-03-2 - by: Bruno Vieira

INSERT INTO sis_email_prioridade (id, ds_descricao) SELECT 1,'NORMAL' WHERE NOT EXISTS ( SELECT id FROM sis_email_prioridade WHERE id = 1);
INSERT INTO sis_email_prioridade (id, ds_descricao) SELECT 2,'BAIXA' WHERE NOT EXISTS ( SELECT id FROM sis_email_prioridade WHERE id = 2);
INSERT INTO sis_email_prioridade (id, ds_descricao) SELECT 3,'ALTA' WHERE NOT EXISTS ( SELECT id FROM sis_email_prioridade WHERE id = 3);
SELECT setval('sis_email_prioridade_id_seq', max(id)) FROM sis_email_prioridade;