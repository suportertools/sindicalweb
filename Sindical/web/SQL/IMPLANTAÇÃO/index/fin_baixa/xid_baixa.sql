-- Index: xid_baixa

-- DROP INDEX xid_baixa;

CREATE UNIQUE INDEX xid_baixa
  ON fin_baixa
  USING btree
  (id);

