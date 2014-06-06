
-- Index: xid_endereco

-- DROP INDEX xid_endereco;

CREATE UNIQUE INDEX xid_endereco
  ON end_endereco
  USING btree
  (id);
