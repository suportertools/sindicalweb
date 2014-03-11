ALTER TABLE pes_tipo_endereco ADD CONSTRAINT pes_tipo_endereco_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE pes_tipo_documento ADD CONSTRAINT pes_tipo_documento_ds_descricao_key UNIQUE (ds_descricao);

ALTER TABLE fin_tipo_documento ADD CONSTRAINT fin_tipo_documento_ds_descricao_key UNIQUE (ds_descricao);