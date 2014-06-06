
-- Index: fki_fin_baixa_id_caixa

-- DROP INDEX fki_fin_baixa_id_caixa;

CREATE INDEX fki_fin_baixa_id_caixa
  ON fin_baixa
  USING btree
  (id_caixa);
