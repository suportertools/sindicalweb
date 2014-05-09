
-- Table: age_contato

-- DROP TABLE age_contato;

CREATE TABLE age_contato
(
  id serial NOT NULL,
  ds_email2 character varying(255),
  ds_departamento character varying(100),
  ds_contato character varying(100),
  dt_nascimento date,
  is_notifica_aniversario boolean DEFAULT false,
  ds_email1 character varying(255),
  id_agenda integer,
  CONSTRAINT age_contato_pkey PRIMARY KEY (id),
  CONSTRAINT fk_age_contato_id_agenda FOREIGN KEY (id_agenda)
      REFERENCES age_agenda (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_age_contato_0 UNIQUE (id_agenda, ds_contato)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE age_contato
  OWNER TO postgres;


select * from seg_log;
select * from seg_log_complemento;
-- drop table seg_log;
-- drop table seg_log_complemento;

-- Table: seg_log

-- DROP TABLE seg_log;

CREATE TABLE seg_log
(
  id serial NOT NULL,
  ds_conteudo_original character varying(1024),
  ds_conteudo_alterado character varying(1024),
  dt_data date,
  ds_hora character varying(50) NOT NULL,
  id_usuario integer NOT NULL,
  id_rotina integer NOT NULL,
  id_evento integer,
  CONSTRAINT seg_log_pkey PRIMARY KEY (id),
  CONSTRAINT fk_seg_log_id_evento FOREIGN KEY (id_evento)
      REFERENCES seg_evento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_seg_log_id_rotina FOREIGN KEY (id_rotina)
      REFERENCES seg_rotina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_seg_log_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE seg_log
  OWNER TO postgres;


