
-- Index: xid_juridica_rea

-- DROP INDEX xid_juridica_rea;

CREATE INDEX xid_juridica_rea
  ON arr_contribuintes_inativos
  USING btree
  (id_juridica);
