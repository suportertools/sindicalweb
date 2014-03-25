ALTER TABLE seg_usuario ADD COLUMN ds_email character varying(100);
-- UPDATE seg_usuario SET ds_email = '';

ALTER TABLE eve_evento_baile ADD COLUMN nr_convites integer;
-- UPDATE eve_evento_baile SET nr_convites = 0;

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


ALTER TABLE seg_registro ADD COLUMN id_email_protocolo integer;
ALTER TABLE seg_registro
  ADD CONSTRAINT fk_seg_registro_id_email_protocolo FOREIGN KEY (id_email_protocolo)
     REFERENCES sis_email_protocolo (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE seg_registro ADD COLUMN sis_email_porta integer;
-- UPDATE seg_registro SET sis_email_porta = 25;

