
-- Index: xid_descricao_endereco

-- DROP INDEX xid_descricao_endereco;

CREATE INDEX xid_descricao_endereco
  ON end_descricao_endereco
  USING btree
  (id);
