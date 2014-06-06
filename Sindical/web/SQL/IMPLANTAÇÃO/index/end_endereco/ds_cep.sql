
-- Index: ds_cep

-- DROP INDEX ds_cep;

CREATE INDEX ds_cep
  ON end_endereco
  USING btree
  (ds_cep COLLATE pg_catalog."default");
