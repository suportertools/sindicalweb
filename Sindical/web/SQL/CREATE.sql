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



-- Table: pes_juridica_receita

-- DROP TABLE pes_juridica_receita;

CREATE TABLE pes_juridica_receita
(
  id serial NOT NULL,
  ds_complemento character varying(35),
  ds_descricao_end character varying(300),
  ds_documento character varying(30),
  ds_bairro character varying(300),
  ds_fantasia character varying(300),
  ds_numero character varying(35),
  dt_pesquisa date,
  ds_cnae character varying(400),
  ds_cep character varying(15),
  ds_status character varying(30),
  ds_nome character varying(300),
  id_pessoa integer,
  CONSTRAINT pes_juridica_receita_pkey PRIMARY KEY (id),
  CONSTRAINT fk_pes_juridica_receita_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_juridica_receita
  OWNER TO postgres;

--------------------------------------------------------------------------------

-- Table: fin_polling_email

-- DROP TABLE fin_polling_email;

CREATE TABLE fin_polling_email
(
  id serial NOT NULL,
  is_ativo boolean,
  dt_envio date,
  ds_hora character varying(5),
  dt_emissao date,
  id_cobranca_envio integer,
  id_link integer,
  CONSTRAINT fin_polling_email_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_polling_email_id_cobranca_envio FOREIGN KEY (id_cobranca_envio)
      REFERENCES fin_cobranca_envio (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_polling_email_id_link FOREIGN KEY (id_link)
      REFERENCES sis_links (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_polling_email
  OWNER TO postgres;


-- CLAUDEMIR

-- Table: soc_historico_carteirinha

-- DROP TABLE soc_historico_carteirinha;

CREATE TABLE soc_historico_carteirinha
(
  id serial NOT NULL,
  ds_hora character varying(255),
  ds_descricao character varying(255),
  dt_emissao date,
  id_socio integer,
  CONSTRAINT soc_historico_carteirinha_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_historico_carteirinha_id_socio FOREIGN KEY (id_socio)
      REFERENCES soc_socios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_historico_carteirinha
  OWNER TO postgres;