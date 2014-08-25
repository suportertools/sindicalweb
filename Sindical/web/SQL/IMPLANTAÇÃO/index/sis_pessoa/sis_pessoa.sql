
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