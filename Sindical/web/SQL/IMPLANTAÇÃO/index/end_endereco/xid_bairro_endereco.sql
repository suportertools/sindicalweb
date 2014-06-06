
-- Index: xid_bairro_endereco

-- DROP INDEX xid_bairro_endereco;

CREATE INDEX xid_bairro_endereco
  ON end_endereco
  USING btree
  (id_bairro);
