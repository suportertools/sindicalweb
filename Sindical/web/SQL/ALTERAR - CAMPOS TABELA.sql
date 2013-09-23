ALTER TABLE fin_movimento
  ADD CONSTRAINT fk_fin_movimento_id_tipo_documento FOREIGN KEY (id_tipo_documento)
     REFERENCES fin_tipo_documento (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

-- hom_cancelar_horario 
-- Criate: 2013-08-05
-- Last edition: 2013-08-05 - by: Bruno Vieira

ALTER TABLE hom_cancelar_horario DROP COLUMN ds_hora;

-- sis_contador_acessos
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Claudemir S. Custódio

ALTER TABLE seg_rotina DROP COLUMN nr_acesso;

-- matr_contrato
-- Criate: 2013-09-18
-- Last edition: 2013-09-18 - by: Rogério M. Sarmento

ALTER TABLE matr_contrato DROP COLUMN ds_observacao;

-- fin_mocimentp 
-- Criate: 2013-09-23
-- Last edition: 2013-09-23 - by: Rogério M. Sarmento

ALTER TABLE fin_movimento DROP COLUMN id_evt;



