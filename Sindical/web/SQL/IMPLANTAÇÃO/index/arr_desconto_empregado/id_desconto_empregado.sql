-- Index: id_desconto_empregado

-- DROP INDEX id_desconto_empregado;

CREATE INDEX id_desconto_empregado
  ON arr_desconto_empregado
  USING btree
  (id);
