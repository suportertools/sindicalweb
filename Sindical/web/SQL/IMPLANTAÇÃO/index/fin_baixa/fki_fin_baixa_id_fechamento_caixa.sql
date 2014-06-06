
-- Index: fki_fin_baixa_id_fechamento_caixa

-- DROP INDEX fki_fin_baixa_id_fechamento_caixa;

CREATE INDEX fki_fin_baixa_id_fechamento_caixa
  ON fin_baixa
  USING btree
  (id_fechamento_caixa);
