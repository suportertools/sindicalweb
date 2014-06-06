
-- Index: xid_cidade_endereco

-- DROP INDEX xid_cidade_endereco;

CREATE INDEX xid_cidade_endereco
  ON end_endereco
  USING btree
  (id_cidade);
