-- Index: xid_abairro

-- DROP INDEX xid_abairro;

CREATE UNIQUE INDEX xid_abairro
  ON end_bairro
  USING btree
  (id);

