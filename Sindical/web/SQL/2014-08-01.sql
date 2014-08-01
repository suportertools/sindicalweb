ALTER TABLE pes_pessoa_empresa ADD COLUMN ds_codigo character varying(20);

//** versão **//

UPDATE seg_rotina SET ds_nome_pagina = '"/Sindical/academiaServicoValor.jsf"' WHERE id = 200;

ALTER TABLE aca_servico_valor DROP COLUMN id_grade;

ALTER TABLE aca_semana ADD COLUMN id_servico_valor integer;

ALTER TABLE aca_semana
  ADD CONSTRAINT fk_aca_semana_id_servico_valor FOREIGN KEY (id_servico_valor)
      REFERENCES aca_servico_valor (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
