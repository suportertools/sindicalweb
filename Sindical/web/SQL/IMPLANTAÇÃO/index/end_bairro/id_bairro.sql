-- Index: id_bairro

-- DROP INDEX id_bairro;

CREATE INDEX id_bairro
  ON end_bairro
  USING btree
  (id);

