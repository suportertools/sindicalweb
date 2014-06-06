-- Index: id_faturamento_folha_empresa

-- DROP INDEX id_faturamento_folha_empresa;

CREATE INDEX id_faturamento_folha_empresa
  ON arr_faturamento_folha_empresa
  USING btree
  (id);

