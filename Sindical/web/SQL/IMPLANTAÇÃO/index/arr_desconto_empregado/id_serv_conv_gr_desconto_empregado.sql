
-- Index: id_serv_conv_gr_desconto_empregado

-- DROP INDEX id_serv_conv_gr_desconto_empregado;

CREATE INDEX id_serv_conv_gr_desconto_empregado
  ON arr_desconto_empregado
  USING btree
  (id_servicos, id_convencao, id_grupo_cidade);
