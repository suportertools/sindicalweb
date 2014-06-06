-- Index: arr_acordo_comissao_id_conta_cobranca

-- DROP INDEX arr_acordo_comissao_id_conta_cobranca;

CREATE INDEX arr_acordo_comissao_id_conta_cobranca
  ON arr_acordo_comissao
  USING btree
  (id_conta_cobranca, nr_num_documento COLLATE pg_catalog."default");

