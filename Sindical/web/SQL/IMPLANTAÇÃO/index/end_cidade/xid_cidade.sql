-- Index: xid_cidade

--DROP INDEX xid_cidade;

CREATE UNIQUE INDEX xid_cidade
  ON end_cidade
  USING btree
  (id);

