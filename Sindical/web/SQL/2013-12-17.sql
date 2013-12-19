ALTER TABLE aca_servico_valor ADD COLUMN nr_parcelas integer;

ALTER TABLE matr_academia ADD COLUMN nr_parcelas integer;
ALTER TABLE matr_academia ADD COLUMN is_taxa boolean;
ALTER TABLE matr_academia ADD COLUMN is_taxa_cartao boolean;

ALTER TABLE seg_registro ADD COLUMN id_servico_cartao integer;
ALTER TABLE seg_registro
  ADD CONSTRAINT fk_seg_registro_id_servico_cartao FOREIGN KEY (id_servico_cartao)
     REFERENCES fin_servicos (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE matr_academia
  ADD CONSTRAINT fk_matr_academia_id_evt FOREIGN KEY (id_evt)
     REFERENCES fin_evt (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;