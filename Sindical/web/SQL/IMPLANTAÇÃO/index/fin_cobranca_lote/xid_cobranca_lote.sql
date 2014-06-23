
-- Index: xid_cobranca_lote

-- DROP INDEX xid_cobranca_lote;

CREATE INDEX xid_cobranca_lote
  ON fin_cobranca_lote
  USING btree
  (id);
