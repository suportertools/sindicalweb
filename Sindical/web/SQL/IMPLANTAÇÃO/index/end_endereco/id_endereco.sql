
-- Index: id_endereco

-- DROP INDEX id_endereco;

CREATE INDEX id_endereco
  ON end_endereco
  USING btree
  (id);
