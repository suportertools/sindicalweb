-- Index: id_cidade

-- DROP INDEX id_cidade;

CREATE INDEX id_cidade
  ON end_cidade
  USING btree
  (id);

