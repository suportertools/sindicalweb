-- age_tipo_telefone;

INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 1, 'Comercial'   WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 1);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 2, 'Contato'     WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 2);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 3, 'Celular'     WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 3);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 4, 'Fax'         WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 4);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 5, 'Fone/FAX'    WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 5);
INSERT INTO age_tipo_telefone (id, ds_descricao) SELECT 6, 'Residencial' WHERE NOT EXISTS ( SELECT id FROM age_tipo_telefone WHERE id = 6);
SELECT setval('age_tipo_telefone_id_seq', max(id)) FROM age_tipo_telefone;

-- age_grupo_agenda;

INSERT INTO age_grupo_agenda (id, ds_descricao) SELECT 1, 'Trabalho'   WHERE NOT EXISTS ( SELECT id FROM age_grupo_agenda WHERE id = 1);
INSERT INTO age_grupo_agenda (id, ds_descricao) SELECT 2, 'Empresa'    WHERE NOT EXISTS ( SELECT id FROM age_grupo_agenda WHERE id = 2);
SELECT setval('age_grupo_agenda_id_seq', max(id)) FROM age_grupo_agenda;

-- hom_status;

INSERT INTO hom_status (id, ds_descricao) SELECT 1, 'Disponível'        WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 1);
INSERT INTO hom_status (id, ds_descricao) SELECT 2, 'Agendado'          WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 2);
INSERT INTO hom_status (id, ds_descricao) SELECT 3, 'Cancelado'         WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 3);
INSERT INTO hom_status (id, ds_descricao) SELECT 4, 'Homologado'        WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 4);
INSERT INTO hom_status (id, ds_descricao) SELECT 5, 'Atendimento'       WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 5);
INSERT INTO hom_status (id, ds_descricao) SELECT 6, 'Encaixe'           WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 6);
INSERT INTO hom_status (id, ds_descricao) SELECT 7, 'Não Compareceu'    WHERE NOT EXISTS ( SELECT id FROM hom_status WHERE id = 7);
SELECT setval('hom_status_id_seq', max(id)) FROM hom_status;

-- sis_semana
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO sis_semana (id, ds_descricao) SELECT 1, 'Domingo'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 1 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 2, 'Segunda'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 2 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 3, 'Terça'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 3 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 4, 'Quarta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 4 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 5, 'Quinta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 5 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 6, 'Sexta'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 6 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 7, 'Sábado'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 7 );
SELECT setval('sis_semana_id_seq', max(id)) FROM sis_semana;

-- seg_modulo
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO seg_modulo (id, ds_descricao) SELECT 1, 'Financeiro'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 1);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 2, 'Social'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 2);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 3, 'Arrecadação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 3);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 4, 'Homologação'       WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 4);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 5, 'Jurídico'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 5);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 6, 'Clube'             WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 6);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 7, 'Academia'          WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 7);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 8, 'Escola'            WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 8);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 9, 'Cadastro Auxiliar' WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 9);
INSERT INTO seg_modulo (id, ds_descricao) SELECT 10, 'Segurança'        WHERE NOT EXISTS ( SELECT id FROM seg_modulo WHERE id = 10);
SELECT setval('seg_modulo_id_seq', max(id)) FROM seg_modulo;

-- seg_evento
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO seg_evento (id, ds_descricao) SELECT 1, 'Inclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 1);
INSERT INTO seg_evento (id, ds_descricao) SELECT 2, 'Exclusão'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 2);
INSERT INTO seg_evento (id, ds_descricao) SELECT 3, 'Alteração' WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 3);
INSERT INTO seg_evento (id, ds_descricao) SELECT 4, 'Consulta'  WHERE NOT EXISTS ( SELECT id FROM seg_evento WHERE id = 4);
SELECT setval('seg_evento_id_seq', max(id)) FROM seg_evento;

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


-- seg_rotina
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 1, 'CAIXA BANCO', '/Sindical/caixaBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 1);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 2, 'Inátivo (2)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 2);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 3, 'Inátivo (3)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 3);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 4, 'ARRECADAÇÃO', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 4);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 5, 'RELATÓRIO CONTRIBUINTES', '"/Sindical/relatorioContribuintes.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 5);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 6, 'RELATÓRIO CONTABILIDADES', '"/Sindical/relatorioContabilidades.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 6);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 7, 'MENU PRINCIPAL', '"/Sindical/menuPrincipal.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 7);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 8, 'MENU FINANCEIRO', '"/Sindical/menuFinanceiro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 8);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 9, 'MENU ARRECADAÇÃO', '"/Sindical/menuArrecadacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 9);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 10, 'MENU SOCIAL', '"/Sindical/menuSocial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 10);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 11, 'MENU HOMOLOGAÇÃO', '"/Sindical/menuHomologacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 11);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 12, 'MENU ACADEMIA', '"/Sindical/menuAcademia.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 12);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 13, 'MENU ESCOLA', '"/Sindical/menuEscola.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 13);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 14, 'MENU CLUBE', '"/Sindical/menuClube.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 14);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 15, 'PESQUISA BAIRRO', '"/Sindical/pesquisaBairro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 15);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 16, 'PESQUISA CIDADE', '"/Sindical/pesquisaCidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 16);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 17, 'PESQUISA DESCRIÇÃO ENDEREÇO', '"/Sindical/pesquisaDescricaoEndereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 17);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 18, 'PESQUISA LOGRADOURO', '"/Sindical/pesquisaLogradouro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 18);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 19, 'PESQUISA ENDEREÇO', '"/Sindical/pesquisaEndereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 19);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 20, 'PESQUISA TIPO ENDEREÇO', '"/Sindical/pesquisaTipoEndereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 20);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 21, 'PESQUISA PROFISSÃO', '"/Sindical/pesquisaProfissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 21);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 22, 'PESQUISA FILIAL', '"/Sindical/pesquisaFilial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 22);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 23, 'PESQUISA CONSELHO', '"/Sindical/pesquisaConselho.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 23);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 24, 'PESQUISA CNAE', '"/Sindical/pesquisaCnae.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 24);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 25, 'PESQUISA USUÁRIO', '"/Sindical/pesquisaUsuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 25);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 26, 'PESQUISA DEPARTAMENTO', '"/Sindical/pesquisaDepartamento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 26);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 27, 'PESQUISA NÍVEL', '"/Sindical/pesquisaNivel.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 27);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 28, 'PESQUISA PESSOA', '"/Sindical/pesquisaPessoa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 28);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 29, 'PESQUISA PESSOA FISÍCA', '"/Sindical/pesquisaPessoaFisica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 29);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 30, 'PESQUISA TIPO DOCUMENTO', '"/Sindical/pesquisaTipoDocumento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 30);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 31, 'PESQUISA PESSOA JURIDICA', '"/Sindical/pesquisaPessoaJuridica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 31);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 32, 'PESQUISA JURIDICA FILIAL', '"/Sindical/pesquisaJuridicaFilial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 32);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 33, 'PESQUISA PLANO', '"/Sindical/pesquisaPlano.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 33);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 34, 'PESQUISA PLANO 2', '"/Sindical/pesquisaPlano2.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 34);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 35, 'PESQUISA PLANO 3', '"/Sindical/pesquisaPlano3.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 35);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 36, 'PESQUISA PLANO 4', '"/Sindical/pesquisaPlano4.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 36);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 37, 'PESQUISA PLANO 5', '"/Sindical/pesquisaPlano5.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 37);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 38, 'Inátivo (38)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 38);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 39, 'PESQUISA TRANSFERÊNCIA', '"/Sindical/pesquisaTransferencia.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 39);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 40, 'PESQUISA GRUPO AGENDA', '"/Sindical/pesquisaGrupoAgenda.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 40);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 41, 'PESQUISA MOTIVO INATIVAÇÃO', '"/Sindical/pesquisaMotivoInativacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 41);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 42, 'PESQUISA CONVENÇÃO', '"/Sindical/pesquisaConvencao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 42);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 43, 'PESQUISA SERVIÇOS', '"/Sindical/pesquisaServicos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 43);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 44, 'PESQUISA TIPO SERVIÇO', '"/Sindical/pesquisaTipoServico.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 44);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 45, 'PESQUISA SERVIÇO VALOR', '"/Sindical/pesquisaServicoValor.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 45);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 46, 'PESQUISA CONTA COBRANÇA', '"/Sindical/pesquisaContaCobranca.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 46);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 47, 'PESQUISA CONTA BANCO', '"/Sindical/pesquisaContaBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 47);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 48, 'PESQUISA ROTINA', '"/Sindical/pesquisaRotina.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 48);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 49, 'PESQUISA EVENTO', '"/Sindical/pesquisaEvento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 49);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 50, 'PESQUISA MÓDULO', '"/Sindical/pesquisaModulo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 50);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 51, 'PESQUISA PERMISSÃO', '"/Sindical/pesquisaPermissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 51);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 52, 'PESQUISA CONTABILIDADE', '"/Sindical/pesquisaContabilidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 52);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 53, 'PESQUISA GRUPO CIDADE', '"/Sindical/pesquisaGrupoCidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 53);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 54, 'PESQUISA ÍNDICE', '"/Sindical/pesquisaIndice.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 54);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 55, 'PERMISSÃO DEPARTAMENTO', '"/Sindical/permissaoDepartamento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 55);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 56, 'MOTIVO INATIVAÇÃO', '"/Sindical/motivoInativacao.jsf"', 'MotivoInativacao', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 56);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 57, 'CONVENÇÃO', '"/Sindical/convencao.jsf"', 'Convencao', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 57);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 58, 'SERVIÇOS', '"/Sindical/servicos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 58);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 59, 'TIPO SERVIÇO', '"/Sindical/tipoServico.jsf"', 'TipoServico', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 59);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 60, 'SERVIÇO VALOR', '"/Sindical/servicoValor.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 60);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 61, 'CONTA COBRANÇA', '"/Sindical/contaCobranca.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 61);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 62, 'ROTINA', '"/Sindical/rotina.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 62);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 63, 'EVENTO', '"/Sindical/evento.jsf"', 'Evento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 63);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 64, 'MÓDULO', '"/Sindical/modulo.jsf"', 'Modulo', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 64);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 65, 'PERMISSÃO', '"/Sindical/permissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 65);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 66, 'CONSELHO', '"/Sindical/conselho.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 66);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 67, 'CNAE', '"/Sindical/cnae.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 67);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 68, 'USUÁRIO', '"/Sindical/usuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 68);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 69, 'DEPARTAMENTO', '"/Sindical/departamento.jsf"', 'Departamento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 69);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 70, 'NÍVEL', '"/Sindical/nivel.jsf"', 'Nivel', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 70);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 71, 'PESSOA FÍSICA', '"/Sindical/pessoaFisica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 71);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 72, 'USUÁRIO FALHOU', '"/Sindical/usuarioFalhou.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 72);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 73, 'ACESSO NEGADO', '"/Sindical/acessoNegado.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 73);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 74, 'BAIRRO', '"/Sindical/bairro.jsf"', 'Bairro', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 74);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 75, 'CIDADE', '"/Sindical/cidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 75);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 76, 'DESCRIÇÃO ENDEREÇO', '"/Sindical/descricaoEndereco.jsf"', 'DescricaoEndereco', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 76);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 77, 'LOGRADOURO', '"/Sindical/logradouro.jsf"', 'Logradouro', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 77);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 78, 'ENDEREÇO', '"/Sindical/endereco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 78);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 79, 'TIPO ENDEREÇO', '"/Sindical/tipoEndereco.jsf"', 'TipoEndereco', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 79);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 80, 'PROFISSÃO', '"/Sindical/profissao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 80);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 81, 'FILIAL', '"/Sindical/filial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 81);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 82, 'PESSOA JURÍDICA', '"/Sindical/pessoaJuridica.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 82);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 83, 'TIPO DOCUMENTO', '"/Sindical/tipoDocumento.jsf"', 'TipoDocumento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 83);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 84, 'PLANO', '"/Sindical/plano.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 84);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 85, 'CAIXA BANCO', '"/Sindical/caixaBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 85);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 86, 'Inátivo (86)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 86);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 87, 'TRANSFERÊNCIA ENTRE CONTAS', '"/Sindical/transferenciaEntreContas.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 87);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 88, 'AGENDA', '"/Sindical/agenda.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 88);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 89, 'GRUPO AGENDA', '"/Sindical/grupoAgenda.jsf"', 'GrupoAgenda', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 89);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 90, 'IMPRESSÃO BOLETOS', '"/Sindical/impressaoBoletos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 90);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 91, 'CONTA BANCO', '"/Sindical/contaBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 91);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 92, 'DESCONTO EMPREGADO', '"/Sindical/descontoEmpregado.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 92);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 93, 'SERVIÇO CONTA COBRANÇA', '"/Sindical/servicoContaCobranca.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 93);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 94, 'CNAE CONVENÇÃO', '"/Sindical/cnaeConvencao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 94);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 95, 'MOVIMENTOS RECEBER', '"/Sindical/movimentosReceber.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 95);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 96, 'EXTRATO TELA', '"/Sindical/extratoTela.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 96);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 97, 'CONTRIBUIÇÃO', '"/Sindical/contribuicao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 97);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 98, 'INATIVAÇÃO CONTRIBUINTES', '"/Sindical/inativacaoContribuintes.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 98);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 99, 'MENSAGEM', '"/Sindical/mensagem.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 99);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 101, 'ARQUIVO BANCO', '"/Sindical/arquivoBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 101);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 102, 'GRUPO CIDADE', '"/Sindical/grupoCidade.jsf"', 'GrupoCidade', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 102);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 103, 'GRUPO CIDADES', '"/Sindical/grupoCidades.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 103);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 104, 'BAIXA GERAL', '"/Sindical/baixaGeral.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 104);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 105, 'ACORDO', '"/Sindical/acordo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 105);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 106, 'PROCESSAMENTO INDIVIDUAL', '"/Sindical/processamentoIndividual.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 106);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 107, 'ÍNDICE MENSAL', '"/Sindical/indiceMensal.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 107);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 108, 'ÍNDICE', '"/Sindical/indice.jsf"', 'Indice', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 108);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 109, 'CORREÇÃO', '"/Sindical/correcao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 109);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 110, 'RELATÓRIO MOVIMENTOS', '"/Sindical/relatorioMovimentos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 110);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 111, 'REGISTRO EMPRESARIAL', '"/Sindical/registroEmpresarial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 111);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 112, 'ENVIAR EMAIL', '"/Sindical/enviarEmail.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 112);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 113, 'AGENDAMENTO', '"/Sindical/agendamento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 113);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 114, 'HOMOLOGAÇÃO', '"/Sindical/homologacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 114);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 115, 'FERIÁDOS', '"/Sindical/feriados.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 115);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 116, 'HORÁRIOS', '"/Sindical/horarios.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 116);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 117, 'RETORNO BANCO', '"/Sindical/retornoBanco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 117);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 118, 'CONTRIBUIÇÃO ASSOCIATIVO', '"/Sindical/contribuicaoSocio.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 118);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 119, 'CONTRIBUIÇÃO POR SOCIO', '"/Sindical/contribuicaoPorSocio.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 119);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 120, 'MATRÍCULA SÓCIO', '"/Sindical/socios.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 120);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 121, 'MATRÍCULA CONVÊNIO MÉDICO', '"/Sindical/convenioMedico.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 121);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 122, 'MATRÍCULA ACADEMIA', '"/Sindical/academia.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 122);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 123, 'CONVÊNIO', '"/Sindical/convenio.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 123);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 124, 'SUB GRUPO CONVÊNIO', '"/Sindical/subGrupoConvenio.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 124);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 125, 'GRUPO CONVÊNIO', '"/Sindical/grupoConvenio.jsf"', 'GrupoConvenio', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 125);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 126, 'SERVIÇO ROTINA', '"/Sindical/servicoRotina.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 126);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 127, 'PARENTESCO', '"/Sindical/parentesco.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 127);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 128, 'SUSPENÇÃO', '"/Sindical/suspencao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 128);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 129, 'GRUPO CATEGORIA', '"/Sindical/grupoCategoria.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 129);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 130, 'CATEGORIA', '"/Sindical/categoria.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 130);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 131, 'LANÇAMENTO INDIVIDUAL', '"/Sindical/lancamentoIndividual.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 131);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 132, 'EMISSÃO DE GUIAS', '"/Sindical/emissaoGuias.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 132);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 133, 'EMISSÃO DE CARTEIRINHA', '"/Sindical/emissaoCarteirinha.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 133);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 134, 'EXTORNAR BOLETO', '"/Sindical/extratoTela1.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 134);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 135, 'EXCLUIR ACORDO', '"/Sindical/extratoTela2.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 135);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 136, 'ALTERAR DATA DE VENCIMENTO', '"/Sindical/extratoTela3.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 136);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 137, 'BAIXA POR BOLETO', '"/Sindical/baixaBoleto.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 137);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 138, 'AGENDAMENTO CARAVANA', '"/Sindical/caravana.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 138);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 139, 'AGENDA BAILE', '"/Sindical/baile.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 139);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 140, 'CADASTRO BANDAS', '"/Sindical/bandas.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 140);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 141, 'VENDAS BAILE', '"/Sindical/vendasBaile.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 141);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 142, 'VENDAS CARAVANA', '"/Sindical/vendasCaravana.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 142);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 143, 'DESCRIÇÃO EVENTO', '"/Sindical/descricaoEvento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 143);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 144, 'Inátivo (144)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 144);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 145, 'Inátivo (145)', '', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 145);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 146, 'VENDEDOR', '"/Sindical/vendedor.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 146);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 147, 'EFINANCEIRO', '"/Sindical/eFinanceiro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 147);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 148, 'PROFESSOR', '"/Sindical/professor.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 148);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 149, 'COMPONENTE CURRICULAR', '"/Sindical/componenteCurricular.jsf"', 'ComponenteCurricular', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 149);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 150, 'TURMA', '"/Sindical/turma.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 150);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 151, 'MATRÍCULA ESCOLA', '"/Sindical/matriculaEscola.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 151);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 152, 'PESQUISA SOCIOS', '"/Sindical/pesquisaSocios.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 152);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 153, 'LISTA FILTRO EXTRATO', '"/Sindical/extratoTela4.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 153);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 154, 'CADASTRO SIMPLES', '"/Sindical/simples.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 154);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 155, 'TIPO PAGAMENTO', '"/Sindical/tipoPagamento.jsf"', 'TipoPagamento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 155);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 156, 'GRUPO EVENTO', '"/Sindical/grupoEvento.jsf"', 'GrupoEvento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 156);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 157, 'MAC FILIAL', '"/Sindical/macFilial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 157);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 158, 'CATÁLOGO FILME', '"/Sindical/catalogoFilme"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 158);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 159, 'MÍDIA', '"/Sindical/midia"', 'Midia', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 159);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 160, 'TÍTULO', '"/Sindical/titulo"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 160);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 161, 'PESQUISA TÍTULOS', '"/Sindical/pesquisaTitulo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 161);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 162, 'GENERO', '"/Sindical/genero.jsf"', 'Genero', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 162);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 163, 'OPOSIÇÃO', '"/Sindical/oposicao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 163);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 164, 'REGISTRO PATRONAL', '"/Sindical/registroPatronal.jsf"', 'Patronal', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 164);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 165, 'VALOR EXTRATO TELA', '"/Sindical/extratoTela5.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 165);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 166, 'ATENDIMENTO OPERAÇÃO', '"/Sindical/atendimentoOperacao.jsf"', 'AteOperacao', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 166);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 167, 'ATENDIMENTO PESSOA', '', 'AtePessoa', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 167);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 168, 'ATENDIMENTO MOVIMENTO', '"/Sindical/atendimento.jsf"', 'AteMovimento', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 168);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 169, 'RECEPÇÃO', '"/Sindical/recepcao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 169);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 170, 'CARTÃO SOCIAL', '"/Sindical/cartaoSocial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 170);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 171, 'RELATÓRIO DE SÓCIOS', '"/Sindical/relatorioSocios.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 171);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 172, 'PERÍODO CONVENÇÃO', '"/Sindical/periodoConvencao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 172);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 173, 'FECHAMENTO COMISSÃO', '"/Sindical/fechamentoComissaoAcordo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 173);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 174, 'AGENDA TELEFÔNICA', '"/Sindical/agendaTelefone.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 174);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 175, 'ENVIO ARQUIVOS CONTABILIDADE', '"/Sindical/enviarArquivosContabilidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 175);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 176, 'ENVIO ARQUIVOS CONTRIBUINTE', '"/Sindical/enviarArquivosContribuinte.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 176);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 177, 'RELATÓRIO DE HOMOLOGAÇÃO', '"/Sindical/relatorioHomologacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 177);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 178, 'PESQUISA OPOSIÇÃO', '"/Sindical/pesquisaOposicao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 178);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 179, 'CENTRO COMERCIAL', '"/Sindical/centroComercial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 179);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 180, 'RETORNO PADRÃO', '"/Sindical/retornoPadrao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 180);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 181, 'NOTIFICAÇÃO', '"/Sindical/notificacao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 181);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 182, 'CANCELAR HORÁRIO', '"/Sindical/cancelarHorario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 182);
-- INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 183, 'PESQUISA AGENDA TELEFÔNICA', '"/Sindical/pesquisaAgendaTelefone.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 183);

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

-- end_logradouro 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 0, "' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 0); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 1, 'CAMPO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 1); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 2, 'RUA PROJETADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 2); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 3, 'RUA VELHA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 3); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 4, 'TREVO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 4); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 5, '16ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 5); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 6, '3ª VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 6); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 7, 'AVENIDA VELHA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 7); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 8, 'PASSAGEM' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 8); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 9, 'PASSEIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 9); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 10, 'PROLONGAMENTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 10); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 11, 'RODO ANEL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 11); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 12, 'RUELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 12); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 13, 'CAMPUS' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 13); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 14, 'SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 14); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 15, 'FERROVIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 15); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 16, '2ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 16); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 17, 'COLÔNIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 17); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 18, 'BURACO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 18); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 19, 'ESCADARIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 19); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 20, 'PASSEIO PÚBLICO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 20); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 21, 'ENTRE QUADRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 21); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 22, 'ÁREA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 22); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 23, 'ESTRADA DE LIGAÇÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 23); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 24, 'VIA PRINCIPAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 24); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 25, 'COMPLEXO VIÁRIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 25); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 26, 'CONJUNTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 26); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 27, 'PARQUE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 27); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 28, 'VALE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 28); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 29, '12ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 29); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 30, 'ADRO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 30); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 31, 'CALÇADÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 31); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 32, '4ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 32); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 33, '2ª PARALELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 33); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 34, 'BELVEDERE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 34); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 35, '6ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 35); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 36, '1ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 36); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 37, 'JARDINETE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 37); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 38, '2º ALTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 38); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 39, 'MARINA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 39); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 40, 'GALERIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 40); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 41, 'VIA COSTEIRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 41); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 42, 'ESTRADA PARTICULAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 42); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 43, 'PRAIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 43); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 44, '5ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 44); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 45, 'NÚCLEO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 45); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 46, 'VIA LATERAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 46); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 47, 'BAIXA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 47); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 48, 'TRECHO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 48); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 49, '4ª VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 49); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 50, '3º ALTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 50); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 51, '4ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 51); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 52, '3º BECO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 52); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 53, '1ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 53); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 54, 'RUA PRINCIPAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 54); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 55, 'RUA DE LIGAÇÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 55); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 56, 'RAMAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 56); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 57, 'PARALELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 57); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 58, 'ENTRADA PARTICULAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 58); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 59, 'COMUNIDADE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 59); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 60, 'CHÁCARA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 60); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 61, 'BALÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 61); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 62, 'PASSAGEM SUBTERRÂNEA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 62); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 63, '7ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 63); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 64, 'VIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 64); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 65, 'ESTRADA INTERMUNICIPAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 65); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 66, 'VIA DE ACESSO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 66); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 67, 'VIA COLETORA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 67); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 68, 'RUA DE PEDESTRE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 68); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 69, 'CORREDOR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 69); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 70, 'VIADUTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 70); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 71, '9ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 71); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 72, '13ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 72); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 73, 'AVENIDA MARGINAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 73); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 74, 'AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 74); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 75, 'ELEVADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 75); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 76, 'JARDIM' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 76); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 77, 'TERMINAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 77); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 78, '2ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 78); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 79, 'GRANJA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 79); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 80, '1ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 80); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 81, 'CAMINHO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 81); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 82, 'RECANTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 82); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 83, 'QUINTA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 83); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 84, 'ESCADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 84); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 85, 'RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 85); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 86, 'CICLOVIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 86); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 87, 'RAMPA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 87); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 88, '5ª VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 88); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 89, 'QUADRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 89); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 90, 'TRAVESSA PARTICULAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 90); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 91, '5ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 91); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 92, 'EIXO INDUSTRIAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 92); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 93, 'FAVELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 93); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 94, 'RETORNO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 94); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 95, 'ESTRADA ESTADUAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 95); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 96, 'ACESSO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 96); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 97, 'PRAÇA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 97); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 98, 'CANAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 98); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 99, 'AEROPORTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 99); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 100, 'ALAMEDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 100); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 101, '4ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 101); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 102, 'BULEVAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 102); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 103, 'RETA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 103); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 104, 'DISTRITO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 104); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 105, 'ESTRADA MUNICIPAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 105); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 106, 'ESPLANADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 106); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 107, '15ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 107); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 108, '4ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 108); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 109, '2ª TRAVESSA DA RODOVIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 109); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 110, 'ESTRADA ANTIGA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 110); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 111, 'FONTE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 111); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 112, 'ILHA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 112); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 113, 'VIELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 113); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 114, 'PARADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 114); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 115, 'PARQUE MUNICIPAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 115); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 116, 'PARQUE RESIDENCIAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 116); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 117, 'BLOCO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 117); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 118, 'AVENIDA MARGINAL ESQUERDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 118); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 119, 'BOSQUE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 119); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 120, 'LOTEAMENTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 120); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 121, 'CONJUNTO MUTIRÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 121); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 122, 'BECO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 122); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 123, 'TÚNEL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 123); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 124, '4ª PARALELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 124); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 125, 'VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 125); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 126, 'VIA DE PEDESTRE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 126); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 127, 'FORTE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 127); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 128, '10ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 128); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 129, 'RUA PARTICULAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 129); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 130, 'AVENIDA MARGINAL DIREITA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 130); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 131, '3ª PARALELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 131); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 132, 'VIA EXPRESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 132); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 133, '1ª TRAVESSA DA RODOVIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 133); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 134, '7ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 134); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 135, 'FAZENDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 135); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 136, '11ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 136); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 137, '1ª VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 137); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 138, 'CALÇADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 138); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 139, 'VIA LITORANEA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 139); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 140, 'LADEIRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 140); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 141, '8ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 141); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 142, 'CÓRREGO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 142); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 143, 'ESTACIONAMENTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 143); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 144, '9ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 144); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 145, '5ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 145); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 146, 'ESTÁDIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 146); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 147, '2ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 147); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 148, '3ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 148); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 149, '3ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 149); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 150, 'AVENIDA CONTORNO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 150); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 151, 'AVENIDA PERIMETRAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 151); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 152, 'ARTÉRIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 152); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 153, 'MORRO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 153); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 154, 'RESIDENCIAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 154); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 155, 'ESTRADA VELHA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 155); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 156, 'DESVIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 156); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 157, '2ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 157); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 158, 'VIA PEDESTRE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 158); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 159, 'PONTE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 159); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 160, 'VEREDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 160); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 161, 'VIA LOCAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 161); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 162, 'PRAÇA DE ESPORTES' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 162); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 163, 'PASSARELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 163); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 164, '2º BECO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 164); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 165, 'NÚCLEO RURAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 165); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 166, 'MARGEM' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 166); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 167, '10ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 167); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 168, 'CIRCULAR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 168); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 169, '11ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 169); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 170, 'TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 170); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 171, 'ROTATÓRIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 171); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 172, '6ª SUBIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 172); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 173, '1º ALTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 173); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 174, 'ESTRADA VICINAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 174); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 175, 'CONDOMÍNIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 175); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 176, '2ª VILA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 176); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 177, '12ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 177); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 178, '6ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 178); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 179, 'CAIS' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 179); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 180, 'CAMINHO DE SERVIDÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 180); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 181, '3ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 181); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 182, '14ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 182); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 183, 'ESTRADA DE SERVIDÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 183); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 184, 'SÍTIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 184); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 185, 'BLOCOS' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 185); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 186, 'LARGO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 186); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 187, '3ª LADEIRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 187); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 188, 'RODOVIA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 188); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 189, 'ZIGUE-ZAGUE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 189); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 190, 'FEIRA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 190); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 191, '6ª AVENIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 191); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 192, 'ACAMPAMENTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 192); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 193, 'SETOR' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 193); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 194, '1ª PARALELA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 194); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 195, 'CONTORNO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 195); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 196, 'RETIRO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 196); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 197, 'LAGO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 197); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 198, 'MÓDULO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 198); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 199, 'BOULEVARD' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 199); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 200, 'DESCIDA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 200); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 201, 'NÚCLEO HABITACIONAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 201); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 202, 'ESTAÇÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 202); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 203, 'ÁREA VERDE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 203); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 204, 'CONDOMÍNIO RESIDENCIAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 204); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 205, 'AVENIDA MARGINAL NORTE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 205); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 206, 'ÁREA ESPECIAL' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 206); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 207, '3ª TRAVESSA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 207); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 208, '1º BECO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 208); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 209, '1ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 209); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 210, 'UNIDADE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 210); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 211, 'LAGOA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 211); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 212, '5ª RUA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 212); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 213, 'ATALHO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 213); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 214, 'PASSAGEM DE PEDESTRES' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 214); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 215, 'ALTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 215); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 216, 'ESTRADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 216); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 217, 'MONTE' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 217); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 218, 'ESTRADA DE FERRO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 218); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 219, 'RÓTULA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 219); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 220, 'VIA DE PEDESTRES' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 220); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 221, 'PÁTIO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 221); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 222, 'PONTA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 222); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 223, 'ANTIGA ESTRADA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 223); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 224, 'PORTO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 224); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 225, 'VALA' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 225); 
-- INSERT INTO end_logradouro (id, ds_descricao) SELECT 226, 'SERVIDÃO' WHERE NOT EXISTS ( SELECT id FROM end_logradouro WHERE id = 226); 
SELECT setval('end_logradouro_id_seq', max(id)) FROM end_logradouro;

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

INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 1, 'Nota Fiscal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 1);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 2, 'Boleto' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 2);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 3, 'Dinheiro' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 3);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 4, 'Cheque' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 4);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 5, 'Cheque-Pré' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 5);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 6, 'Cartão de Crédito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 6);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 7, 'Cartão de Débito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 7);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 8, 'Depósito Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 8);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 9, 'Doc Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 9);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 10, 'Transferência Bancária' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 10);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 11, 'Ticket' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 11);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 12, 'Recibo' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 12);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 13, 'Extrato Geral' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 13);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 14, 'Extrato 01' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 14);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 15, 'Extrato 02' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 15);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 16, 'Extrato 03' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 16);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 17, 'Extrato 04' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 17);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 18, 'Extrato 05' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 18);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 19, 'Extrato 06' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 19);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 20, 'Extrato 07' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 20);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 21, 'Extrato 08' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 21);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 22, 'Extrato 09' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 22);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 23, 'Extrato 10' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 23);
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
SELECT setval('fin_tipo_pagamento_id_seq', max(id)) FROM fin_tipo_documento;

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


-- sis_relatorios 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 1,'Contribuintes','/Relatorios/CONTRIBUINTES.jasper',5,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 1);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 2,'Contribuintes(Paisagem)','/Relatorios/CONTRIBUINTESPAISAGEM.jasper',5,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 2);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 3,'Contribuintes por Escritório','/Relatorios/CONTRIBUINTESPORESCRITORIO.jasper',5,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 3);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 4,'Etiqueta','/Relatorios/ETCONTRIBUINTES6181.jasper',5,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 4);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 5,'Escritórios','/Relatorios/ESCRITORIOS.jasper',6,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 5);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 6,'Etiquetas','/Relatorios/ETESCRITORIO6181.jasper',6,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 6);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 7,'Extrato de Arrecadação','/Relatorios/EXTRATO_ARRECADACAO.jasper',110,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 7);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 8,'Demonstrativo de Débito','/Relatorios/DEMONSTRATIVO_DEBITO.jasper',110,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 8);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 9,'Demonstrativo de Débito Por Empresa','/Relatorios/DEMONSTRATIVO_DEBITO_EMPRESA.jasper',110,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 9);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 10,'Relação de Sócios','/Relatorios/SOCIOS.jasper',171,'','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 10);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 11,'Registro de Associados','"/Relatorios/REGISTRO_DE_ASSOCIADOS.jasper',171,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 11);  
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 12,'Sócios por Empresa','"/Relatorios/SOCIOS_EMPRESA.jasper"',171,'p.empresa, p.cnpj','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 12);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 13,'Sócios por Empresa com Assinatura','/Relatorios/SOCIOS_EMPRESA_ASSINATURA.jasper',171,'p.empresa, p.cnpj','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 13);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 14,'Etiquetas Sócios','"/Relatorios/ETIQUETA_SOCIO.jasper"',171,'','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 14);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 15,'Etiqueta Sócio Individual','/Relatorios/ETIQUETA_TERMICA_SOCIAL_RETRATO.jasper',171,'','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 15);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 16,'Sócios por Empresa Paisagem','/Relatorios/SOCIOS_EMPRESA_PAISAGEM.jasper',171,'p.empresa,p.cnpj','so.inativacao is null' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 16);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 17,'Contribuintes/Email','/Relatorios/CONTRIBUINTES_EMAIL.jasper',5,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 17);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 18,'Escritórios/Email','/Relatorios/ESCRITORIOS_EMAIL.jasper',6,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 18);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 19,'Relatório de Homologação','/Relatorios/HOMOLOGACAO.jasper',177,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 19);
SELECT setval('sis_relatorios_id_seq', max(id)) FROM sis_relatorios;

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

