
-- Index: xds_cep

-- DROP INDEX xds_cep;

CREATE INDEX xds_cep
  ON end_endereco
  USING btree
  (ds_cep COLLATE pg_catalog."default");
