
-- Index: ds_descricao_endereco

-- DROP INDEX ds_descricao_endereco;

CREATE INDEX ds_descricao_endereco
  ON end_descricao_endereco
  USING btree
  (upper(translate(ds_descricao::text, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ*'::text, 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC'::text)) COLLATE pg_catalog."default");
