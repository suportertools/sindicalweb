
-- Index: xds_boleto_id_conta_cobranca_fin_boleto

-- DROP INDEX xds_boleto_id_conta_cobranca_fin_boleto;

CREATE INDEX xds_boleto_id_conta_cobranca_fin_boleto
  ON fin_boleto
  USING btree
  (ds_boleto COLLATE pg_catalog."default", id_conta_cobranca);
