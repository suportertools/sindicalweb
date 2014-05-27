ALTER TABLE est_estoque ADD CONSTRAINT unq_est_estoque_0 UNIQUE (id_produto, id_filial, id_tipo);
ALTER TABLE fin_caixa ADD CONSTRAINT fin_caixa_0 UNIQUE (id_filial, nr_caixa, ds_descricao);