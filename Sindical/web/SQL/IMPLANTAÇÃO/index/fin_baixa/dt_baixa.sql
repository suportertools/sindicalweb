
-- Index: dt_baixa

-- DROP INDEX dt_baixa;

CREATE INDEX dt_baixa
  ON fin_baixa
  USING btree
  (dt_baixa);
