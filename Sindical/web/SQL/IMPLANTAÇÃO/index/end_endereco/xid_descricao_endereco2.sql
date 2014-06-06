
-- Index: xid_descricao_endereco2

-- DROP INDEX xid_descricao_endereco2;

CREATE INDEX xid_descricao_endereco2
  ON end_endereco
  USING btree
  (id_descricao_endereco);
