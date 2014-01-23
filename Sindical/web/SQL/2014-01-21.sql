
ALTER TABLE sis_pessoa RENAME ds_cpf  TO ds_documento;
ALTER TABLE sis_pessoa ALTER COLUMN ds_documento TYPE character varying(30);

ALTER TABLE sis_pessoa ADD COLUMN id_tipo_documento integer;
ALTER TABLE sis_pessoa
  ADD CONSTRAINT fk_sis_pessoa_id_tipo_documento FOREIGN KEY (id_tipo_documento)
     REFERENCES pes_tipo_documento (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE sis_pessoa ADD COLUMN id_endereco integer;
ALTER TABLE sis_pessoa
  ADD CONSTRAINT fk_sis_pessoa_id_endereco FOREIGN KEY (id_endereco)
     REFERENCES end_endereco (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;


ALTER TABLE sis_pessoa ADD COLUMN ds_sexo character varying(1);
ALTER TABLE sis_pessoa ADD COLUMN ds_complemento character varying(150);
ALTER TABLE sis_pessoa ADD COLUMN ds_numero character varying(30);
ALTER TABLE sis_pessoa ADD COLUMN ds_celular character varying(20);

ALTER TABLE sis_pessoa ADD COLUMN dt_nascimento date;
ALTER TABLE sis_pessoa ADD COLUMN dt_data date;
ALTER TABLE sis_pessoa ADD COLUMN dt_criacao date;

ALTER TABLE sis_pessoa ADD COLUMN ds_email1 character varying(50);
ALTER TABLE sis_pessoa ADD COLUMN ds_email2 character varying(50);

ALTER TABLE sis_pessoa ADD COLUMN ds_obs character varying(300);


ALTER TABLE seg_registro ADD COLUMN convite_dias_convidado integer;
-- UPDATE seg_registro SET convite_dias_convidado = 365;
ALTER TABLE seg_registro ADD COLUMN convite_qtde_convidado integer;
-- UPDATE seg_registro SET convite_qtde_convidado = 1;
ALTER TABLE seg_registro ADD COLUMN convite_dias_socio integer;
-- UPDATE seg_registro SET convite_dias_socio = 30;
ALTER TABLE seg_registro ADD COLUMN convite_qtde_socio integer;
-- UPDATE seg_registro SET convite_qtde_socio = 10;
