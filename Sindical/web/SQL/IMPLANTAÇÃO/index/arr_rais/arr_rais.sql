
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

