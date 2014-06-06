-- Index: id_logradouro

-- DROP INDEX id_logradouro;

CREATE INDEX id_logradouro
  ON end_logradouro
  USING btree
  (id);

