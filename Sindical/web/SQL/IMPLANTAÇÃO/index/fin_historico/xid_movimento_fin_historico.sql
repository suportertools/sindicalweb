
-- Index: xid_movimento_fin_historico

-- DROP INDEX xid_movimento_fin_historico;

CREATE INDEX xid_movimento_fin_historico
  ON fin_historico
  USING btree
  (id_movimento);
