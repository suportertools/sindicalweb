ALTER TABLE pes_tipo_endereco ADD CONSTRAINT pes_tipo_endereco_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE pes_tipo_documento ADD CONSTRAINT pes_tipo_documento_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE fin_tipo_documento ADD CONSTRAINT fin_tipo_documento_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE pes_profissao ADD CONSTRAINT pes_profissao_ds_profissao_key UNIQUE (ds_profissao);

--

ALTER TABLE hom_status ADD CONSTRAINT hom_status_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE hom_demissao ADD CONSTRAINT hom_demissao_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE pes_porte ADD CONSTRAINT pes_porte_ds_descricao_key UNIQUE (ds_descricao);

-- ALTER TABLE pes_cnae ADD CONSTRAINT pes_cnae_ds_descricao_key UNIQUE (ds_cnae);

ALTER TABLE fin_tipo_servico ADD CONSTRAINT fin_tipo_servico_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE eve_banda ADD CONSTRAINT eve_banda_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE soc_midia ADD CONSTRAINT soc_midia_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE esc_componente_curricular ADD CONSTRAINT esc_componente_curricular_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE fin_indice ADD CONSTRAINT fin_indice_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE loc_genero ADD CONSTRAINT loc_genero_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE loc_genero ADD CONSTRAINT loc_genero_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE PES_TIPO_CENTRO_COMERCIAL ADD CONSTRAINT PES_TIPO_CENTRO_COMERCIAL_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE SOC_CONVENIO_GRUPO ADD CONSTRAINT SOC_CONVENIO_GRUPO_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE CONV_MOTIVO_SUSPENCAO ADD CONSTRAINT CONV_MOTIVO_SUSPENCAO_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE ARR_GRUPO_CIDADE ADD CONSTRAINT ARR_GRUPO_CIDADE_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE AGE_GRUPO_AGENDA ADD CONSTRAINT AGE_GRUPO_AGENDA_ds_descricao_key UNIQUE (ds_descricao);

-- 

ALTER TABLE sis_semana ADD CONSTRAINT sis_semana_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE sis_periodo ADD CONSTRAINT sis_periodo_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE SEG_NIVEL ADD CONSTRAINT SEG_NIVEL_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE SEG_MODULO ADD CONSTRAINT SEG_MODULO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE SEG_EVENTO ADD CONSTRAINT SEG_EVENTO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE SEG_DEPARTAMENTO ADD CONSTRAINT SEG_DEPARTAMENTO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE FIN_CONDICAO_PAGAMENTO ADD CONSTRAINT FIN_CONDICAO_PAGAMENTO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE AGE_TIPO_TELEFONE ADD CONSTRAINT AGE_TIPO_TELEFONE_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE ARR_CONVENCAO ADD CONSTRAINT ARR_CONVENCAO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE ARR_MOTIVO_INATIVACAO ADD CONSTRAINT ARR_MOTIVO_INATIVACAO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE ARR_REPIS_STATUS ADD CONSTRAINT ARR_REPIS_STATUS_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE EVE_STATUS ADD CONSTRAINT EVE_STATUS_ds_descricao_key UNIQUE (ds_descricao);

-- 

ALTER TABLE SOC_MOTIVO_INATIVACAO ADD CONSTRAINT SOC_MOTIVO_INATIVACAO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE ATE_OPERACAO ADD CONSTRAINT ATE_OPERACAO_ds_descricao_key UNIQUE (ds_descricao);


--

ALTER TABLE FIN_COBRANCA_TIPO ADD CONSTRAINT FIN_COBRANCA_TIPO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE FIN_TIPO_PAGAMENTO ADD CONSTRAINT FIN_TIPO_PAGAMENTO_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE EST_TIPO CONSTRAINT EST_TIPO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE EST_GRUPO CONSTRAINT EST_GRUPO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE EST_SUBGRUPO CONSTRAINT EST_SUBGRUPO_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE EST_UNIDADE CONSTRAINT EST_UNIDADE_ds_descricao_key UNIQUE (ds_descricao);
ALTER TABLE SIS_COR CONSTRAINT SIS_COR_ds_descricao_key UNIQUE (ds_descricao);

