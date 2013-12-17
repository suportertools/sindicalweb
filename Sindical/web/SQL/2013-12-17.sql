ALTER TABLE aca_servico_valor ADD COLUMN nr_parcelas integer;

ALTER TABLE matr_academia ADD COLUMN nr_parcelas integer;
ALTER TABLE matr_academia ADD COLUMN is_taxa boolean;

ALTER TABLE seg_registro ADD COLUMN id_servico_cartao integer;
ALTER TABLE seg_registro
  ADD CONSTRAINT fk_seg_registro_id_servico_cartao FOREIGN KEY (id_servico_cartao)
     REFERENCES fin_servicos (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;