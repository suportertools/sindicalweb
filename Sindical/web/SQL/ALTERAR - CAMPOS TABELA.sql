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



