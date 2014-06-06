-- Index: chave_faturamento_folha_empresa

-- DROP INDEX chave_faturamento_folha_empresa;

CREATE INDEX chave_faturamento_folha_empresa
  ON arr_faturamento_folha_empresa
  USING btree
  (id_juridica, id_tipo_servico, ds_referencia COLLATE pg_catalog."default");

