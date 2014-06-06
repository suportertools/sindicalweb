-- Index: ds_logradouro

-- DROP INDEX ds_logradouro;

CREATE INDEX ds_logradouro
  ON end_logradouro
  USING btree
  (upper(translate(ds_descricao::text, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ*'::text, 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC'::text)) COLLATE pg_catalog."default");

