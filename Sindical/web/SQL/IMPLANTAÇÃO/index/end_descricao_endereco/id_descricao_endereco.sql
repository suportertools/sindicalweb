
-- Index: id_descricao_endereco

-- DROP INDEX id_descricao_endereco;

CREATE INDEX id_descricao_endereco
  ON end_descricao_endereco
  USING btree
  (id);
