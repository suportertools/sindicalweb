
-- Index: ref_faturamento_folha_empresa

-- DROP INDEX ref_faturamento_folha_empresa;

CREATE INDEX ref_faturamento_folha_empresa
  ON arr_faturamento_folha_empresa
  USING btree
  (id_juridica, "substring"(ds_referencia::text, 4, 4) COLLATE pg_catalog."default", "substring"(ds_referencia::text, 1, 2) COLLATE pg_catalog."default", id_tipo_servico);
