
-- Index: id_serv_conv_gr_reff_desconto_empregado

-- DROP INDEX id_serv_conv_gr_reff_desconto_empregado;

CREATE INDEX id_serv_conv_gr_reff_desconto_empregado
  ON arr_desconto_empregado
  USING btree
  (id_servicos, id_convencao, id_grupo_cidade, "substring"(ds_ref_final::text, 4, 4) COLLATE pg_catalog."default", "substring"(ds_ref_final::text, 1, 2) COLLATE pg_catalog."default");
