ALTER TABLE seg_registro ADD COLUMN sis_is_email_marketing BOOLEAN DEFAULT FALSE;

-- Table: sis_email

-- DROP TABLE sis_email;

CREATE TABLE sis_email
(
  id serial NOT NULL,
  ds_mensagem character varying(255),
  is_confirma_recebimento boolean DEFAULT false,
  dt_data date,
  is_rascunho boolean DEFAULT false,
  ds_assunto character varying(255),
  ds_hora character varying(5),
  id_usuario integer,
  id_sis_prioridade integer,
  id_rotina integer,
  CONSTRAINT sis_email_pkey PRIMARY KEY (id),
  CONSTRAINT fk_sis_email_id_rotina FOREIGN KEY (id_rotina)
      REFERENCES seg_rotina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_email_id_sis_prioridade FOREIGN KEY (id_sis_prioridade)
      REFERENCES sis_email_prioridade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_email_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_email
  OWNER TO postgres;


-- Table: sis_email_arquivo

-- DROP TABLE sis_email_arquivo;

CREATE TABLE sis_email_arquivo
(
  id serial NOT NULL,
  id_email integer,
  id_arquivo integer,
  CONSTRAINT sis_email_arquivo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_sis_email_arquivo_id_arquivo FOREIGN KEY (id_arquivo)
      REFERENCES sis_arquivo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_email_arquivo_id_email FOREIGN KEY (id_email)
      REFERENCES sis_email (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_email_arquivo
  OWNER TO postgres;

-- Table: sis_email_pessoa

-- DROP TABLE sis_email_pessoa;

CREATE TABLE sis_email_pessoa
(
  id serial NOT NULL,
  ds_co character varying(255),
  dt_recebimento date,
  ds_cc character varying(255),
  ds_destinatario character varying(255),
  id_email integer,
  id_pessoa integer,
  CONSTRAINT sis_email_pessoa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_sis_email_pessoa_id_email FOREIGN KEY (id_email)
      REFERENCES sis_email (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_email_pessoa_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_email_pessoa
  OWNER TO postgres;


-- Table: sis_email_prioridade

-- DROP TABLE sis_email_prioridade;

CREATE TABLE sis_email_prioridade
(
  id serial NOT NULL,
  ds_descricao character varying(15),
  CONSTRAINT sis_email_prioridade_pkey PRIMARY KEY (id),
  CONSTRAINT sis_email_prioridade_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_email_prioridade
  OWNER TO postgres;


-- Table: sis_email_protocolo

-- DROP TABLE sis_email_protocolo;

CREATE TABLE sis_email_protocolo
(
  id serial NOT NULL,
  ds_descricao character varying(50) NOT NULL,
  CONSTRAINT sis_email_protocolo_pkey PRIMARY KEY (id),
  CONSTRAINT sis_email_protocolo_pdescricao_unique UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_email_protocolo
  OWNER TO postgres;
