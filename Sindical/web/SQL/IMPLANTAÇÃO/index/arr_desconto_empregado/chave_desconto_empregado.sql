
-- Index: chave_desconto_empregado

-- DROP INDEX chave_desconto_empregado;

CREATE INDEX chave_desconto_empregado
  ON arr_desconto_empregado
  USING btree
  (id_convencao, id_grupo_cidade, id_servicos, "substring"(ds_ref_inicial::text, 4, 4) COLLATE pg_catalog."default", "substring"(ds_ref_inicial::text, 1, 2) COLLATE pg_catalog."default", "substring"(ds_ref_final::text, 4, 4) COLLATE pg_catalog."default", "substring"(ds_ref_final::text, 1, 2) COLLATE pg_catalog."default");
