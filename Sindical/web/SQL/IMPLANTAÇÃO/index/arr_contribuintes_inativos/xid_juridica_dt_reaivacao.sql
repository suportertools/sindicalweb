
-- Index: xid_juridica_dt_reaivacao

-- DROP INDEX xid_juridica_dt_reaivacao;

CREATE INDEX xid_juridica_dt_reaivacao
  ON arr_contribuintes_inativos
  USING btree
  (id_juridica, dt_ativacao);
