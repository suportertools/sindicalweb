-- SEG_REGISTRO --
 
-- update: 2013-07-31
-- edited by: Bruno Vieira da Silva

ALTER TABLE seg_registro ADD COLUMN  ds_obs_ficha_social character varying(8000);
ALTER TABLE seg_registro ADD COLUMN dt_atualiza_homologacao date;

UPDATE seg_registro SET dt_atualiza_homologacao = '1900-01-01' WHERE id = 1 AND dt_atualiza_homologacao is null;

-- update: 2013-11-05
-- edited by: Bruno Vieira da Silva

ALTER TABLE seg_registro ADD COLUMN is_boleto_web boolean;
-- UPDATE seg_registro SET is_boleto_web = false WHERE id = 1 AND is_boleto_web is null;
ALTER TABLE seg_registro ADD COLUMN is_repis_web boolean;
-- UPDATE seg_registro SET is_repis_web = false WHERE id = 1 AND is_repis_web is null;
ALTER TABLE seg_registro ADD COLUMN is_agendamento_web boolean;
-- UPDATE seg_registro SET is_agendamento_web = false WHERE id = 1 AND is_agendamento_web is null;
ALTER TABLE seg_registro ADD COLUMN nr_limite_envios_notificacao integer;
-- UPDATE seg_registro SET nr_limite_envios_notificacao = 150;
ALTER TABLE seg_registro ADD COLUMN nr_intervalo_envios_notificacao integer;
-- UPDATE seg_registro SET nr_intervalo_envios_notificacao = 60;
ALTER TABLE seg_registro ADD COLUMN dt_limite_agendamento_retroativo date;
-- UPDATE seg_registro SET dt_limite_agendamento_retroativo = '1900-01-01' WHERE id = 1 AND dt_limite_agendamento_retroativo is null;

-- *****************************************************************************

ALTER TABLE fin_movimento
  ADD CONSTRAINT fk_fin_movimento_id_tipo_documento FOREIGN KEY (id_tipo_documento)
      REFERENCES fin_tipo_documento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;


-- Table: sis_contador_acessos

-- DROP TABLE sis_contador_acessos;

CREATE TABLE sis_contador_acessos
(
  id serial NOT NULL,
  nr_acesso integer,
  id_pessoa integer NOT NULL,
  id_rotina integer NOT NULL,
  CONSTRAINT sis_contador_acessos_pkey PRIMARY KEY (id),
  CONSTRAINT fk_sis_contador_acessos_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_contador_acessos_id_rotina FOREIGN KEY (id_rotina)
      REFERENCES seg_rotina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_contador_acessos
  OWNER TO postgres;


-- *****************************************************************************

-- sis_semana
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

CREATE TABLE sis_semana
(
  id serial NOT NULL,
  ds_descricao character varying(15),
  CONSTRAINT sis_semana_pkey PRIMARY KEY (id),
  CONSTRAINT sis_semana_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_semana
  OWNER TO postgres;

-- HOM_HORARIOS
-- update: 2013-07-26
-- edited by: Bruno Vieira da Silva

ALTER TABLE hom_horarios ADD COLUMN id_semana integer;
ALTER TABLE hom_horarios
  ADD CONSTRAINT fk_hom_horarios_id_semana FOREIGN KEY (id_semana)
     REFERENCES sis_semana (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

UPDATE hom_horarios SET id_semana = 2 WHERE id_semana is null

-- *****************************************************************************

-- HOM_CANCELAR_HORARIO 
-- update: 2013-11-06
-- edited by: Bruno Vieira da Silva

ALTER TABLE hom_cancelar_horario ADD COLUMN id_horarios integer;
ALTER TABLE hom_cancelar_horario
  ADD CONSTRAINT fk_hom_cancelar_horario_id_horarios FOREIGN KEY (id_horarios)
     REFERENCES hom_horarios (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE hom_cancelar_horario ADD COLUMN id_usuario integer;
ALTER TABLE hom_cancelar_horario
  ADD CONSTRAINT fk_hom_cancelar_horario_id_usuario FOREIGN KEY (id_usuario)
     REFERENCES seg_usuario (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;



-- *****************************************************************************

-- HOM_AGENDAMENTO
-- update: 2013-07-31
-- edited by: Bruno Vieira da Silva

ALTER TABLE hom_agendamento ADD COLUMN dt_emissao date;

UPDATE hom_agendamento SET dt_emissao = dt_data WHERE dt_emissao is null;

-- seg_usuario_acesso  -- 
-- update: 2013-08-05
-- edited by: Bruno Vieira da Silva

ALTER TABLE seg_usuario_acesso ADD COLUMN is_permite boolean;

-- sis_relatorios
-- update: 2013-08-07
-- edited by: Bruno Vieira da Silva

ALTER TABLE sis_relatorios ADD COLUMN ds_qry_ordem character varying(1000);
ALTER TABLE sis_relatorios ADD COLUMN ds_qry character varying(1000);


-- seg_rotina -- 
-- update: 2013-08-07
-- edited by: Bruno Vieira da Silva

ALTER TABLE seg_rotina ADD COLUMN is_ativo boolean;
-- UPDATE seg_rotina SET is_ativo = true WHERE is_ativo is null;

-- seg_usuario  -- 
-- update: 2013-08-14
-- edited by: Bruno Vieira da Silva

ALTER TABLE seg_usuario ADD COLUMN is_ativo boolean;
-- UPDATE seg_usuario SET is_ativo = true WHERE is_ativo is null;

-- age_telefone -- 
-- update: 2013-09-04
-- edited by: Bruno Vieira da Silva

ALTER TABLE age_telefone ADD COLUMN  ds_ddi character varying(2);
ALTER TABLE age_telefone ADD COLUMN  ds_ddd character varying(2);

-- fin_servico_valor -- 
-- update: 2013-09-09
-- edited by: Bruno Vieira da Silva

ALTER TABLE fin_servico_valor ADD COLUMN nr_taxa double precision;
ALTER TABLE fin_servico_valor ALTER COLUMN nr_taxa SET NOT NULL;

-- *****************************************************************************

-- ESC_TURMA 
-- update: 2013-10-15
-- edited by: Bruno Vieira da Silva

ALTER TABLE esc_turma ADD COLUMN id_filial integer;
ALTER TABLE esc_turma
  ADD CONSTRAINT fk_esc_turma_id_filial FOREIGN KEY (id_filial)
     REFERENCES pes_filial (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE esc_turma ADD COLUMN nr_sala character varying(2);
ALTER TABLE esc_turma ADD COLUMN nr_quantidade integer;
ALTER TABLE esc_turma ADD COLUMN ds_descricao character varying(255);

-- MATR_ESCOLA
-- update: 2013-09-17
-- edited by: Bruno Vieira da Silva
id_matricula_socios
ALTER TABLE matr_escola ADD COLUMN id_tipo_documento integer;
ALTER TABLE matr_escola
  ADD CONSTRAINT fk_matr_escola_id_tipo_documento FOREIGN KEY (id_tipo_documento)
     REFERENCES fin_tipo_documento (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE matr_escola ADD COLUMN is_ativo boolean;


ALTER TABLE matr_escola ADD COLUMN is_desconto_folha boolean;
-- UPDATE matr_escola SET is_desconto_folha = false;

-- ESC_TURMA 
-- update: 2013-09-16
-- edited by: Bruno Vieira da Silva

ALTER TABLE esc_turma ADD COLUMN id_filial integer;
ALTER TABLE esc_turma
  ADD CONSTRAINT fk_esc_turma_id_filial FOREIGN KEY (id_filial)
     REFERENCES pes_filial (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

-- PES_PESSOA_COMPLEMENTO
-- update: 2013-09-25
-- edited by: Bruno V. da Silva

ALTER TABLE pes_pessoa_complemento ADD COLUMN is_cobranca_bancaria boolean;

-- FIN_LOTE
-- update: 2013-09-25
-- edited by: Bruno V. da Silva

ALTER TABLE fin_lote ADD COLUMN is_desconto_folha boolean;
-- UPDATE fin_lote SET is_desconto_folha = false;



-- FIN_COBRANCA_LOTE
-- update: 2013-09-27
-- edited by: Claudemir S. Custódio

ALTER TABLE fin_cobranca_lote ADD COLUMN ds_hora character varying(5);

-- SIS_LINKS
-- update: 2013-09-27
-- edited by: Claudemir S. Custódio

ALTER TABLE sis_links ADD COLUMN ds_descricao character varying(200);

ALTER TABLE sis_links ALTER COLUMN id_pessoa DROP NOT NULL;

-- pes_fisica -- 
-- update: 2013-10-21
-- edited by: Bruno Vieira da Silva

ALTER TABLE pes_fisica ALTER COLUMN ds_rg TYPE character varying(20);


-- Column: id_responsavel

-- ALTER TABLE fin_servico_pessoa DROP COLUMN id_responsavel;
-- update: 2013-11-11
ALTER TABLE fin_servico_pessoa ADD COLUMN id_responsavel integer;
