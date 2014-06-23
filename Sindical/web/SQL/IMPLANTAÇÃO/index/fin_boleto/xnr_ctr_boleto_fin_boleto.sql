-- Index: xnr_ctr_boleto_fin_boleto

-- DROP INDEX xnr_ctr_boleto_fin_boleto;

CREATE UNIQUE INDEX xnr_ctr_boleto_fin_boleto
  ON fin_boleto
  USING btree
  (nr_ctr_boleto COLLATE pg_catalog."default");

