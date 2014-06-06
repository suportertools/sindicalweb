-- Index: ds_cidade

-- DROP INDEX ds_cidade;

CREATE INDEX ds_cidade
  ON end_cidade
  USING btree
  (upper(translate(ds_cidade::text, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ*'::text, 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC'::text)) COLLATE pg_catalog."default");

