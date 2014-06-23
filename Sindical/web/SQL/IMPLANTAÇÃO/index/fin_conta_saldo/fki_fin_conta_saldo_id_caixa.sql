-- Index: fki_fin_conta_saldo_id_caixa

-- DROP INDEX fki_fin_conta_saldo_id_caixa;

CREATE INDEX fki_fin_conta_saldo_id_caixa
  ON fin_conta_saldo
  USING btree
  (id_caixa);

