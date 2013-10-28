ALTER TABLE soc_convenio_servico ADD COLUMN is_encaminhamento boolean;
ALTER TABLE fin_guia ADD COLUMN is_encaminhamento boolean;
ALTER TABLE fin_guia ADD COLUMN id_convenio_sub_grupo integer;

UPDATE soc_convenio_servico SET is_encaminhamento = FALSE;
UPDATE fin_guia SET is_encaminhamento = FALSE;
