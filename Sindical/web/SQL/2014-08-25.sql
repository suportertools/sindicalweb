
CREATE INDEX rais_emissao 
  ON arr_rais 
  USING btree 
  (dt_emissao); 

CREATE INDEX rais_empresa 
  ON arr_rais 
  USING btree 
  (id_empresa); 

CREATE INDEX rais_pessoa 
  ON arr_rais 
  USING btree 
  (id_sis_pessoa); 

CREATE INDEX rais_profissao 
  ON arr_rais 
  USING btree 
  (id_profissao); 

CREATE INDEX sis_pessoa_cpf 
  ON sis_pessoa 
  USING btree 
  (ds_documento COLLATE pg_catalog."default"); 

CREATE INDEX sis_pessoa_nome 
  ON sis_pessoa 
  USING btree 
  (ds_nome COLLATE pg_catalog."default"); 
CREATE INDEX sis_pessoa_nome_nacimento 
  ON sis_pessoa 
  USING btree 
  (ds_nome COLLATE pg_catalog."default", dt_nascimento); 