-- Index: ds_bairro

-- DROP INDEX ds_bairro;

CREATE INDEX ds_bairro
  ON end_bairro
  USING btree
  (upper(translate(ds_descricao::text, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ*'::text, 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC'::text)) COLLATE pg_catalog."default");

