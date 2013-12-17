-- SEG_REGISTRO --
 
-- update: 2013-07-31
-- edited by: Bruno Vieira da Silva

ALTER TABLE matr_academia ADD COLUMN id_servico_valor integer;
ALTER TABLE matr_academia
  ADD CONSTRAINT fk_matr_academia_id_servico_valor FOREIGN KEY (id_servico_valor)
     REFERENCES aca_servico_valor (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE matr_academia ADD COLUMN id_evt integer;
ALTER TABLE matr_academia
  ADD CONSTRAINT fk_matr_academia_id_evt FOREIGN KEY (id_evt)
     REFERENCES aca_servico_valor (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;