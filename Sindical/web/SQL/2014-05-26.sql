ALTER TABLE fin_servico_valor ADD CONSTRAINT fin_servico_valor_idade_i UNIQUE(id_servico, nr_idade_ini);
ALTER TABLE fin_servico_valor ADD CONSTRAINT fin_servico_valor_idade_f UNIQUE(id_servico, nr_idade_fim);

ALTER TABLE seg_registro ADD COLUMN hom_nr_limite_meses INTEGER DEFAULT 3;