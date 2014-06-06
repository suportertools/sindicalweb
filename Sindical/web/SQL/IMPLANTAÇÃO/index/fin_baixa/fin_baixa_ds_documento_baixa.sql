
-- Index: fin_baixa_ds_documento_baixa

-- DROP INDEX fin_baixa_ds_documento_baixa;

CREATE INDEX fin_baixa_ds_documento_baixa
  ON fin_baixa
  USING btree
  (ds_documento_baixa COLLATE pg_catalog."default");
