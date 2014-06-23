

ALTER TABLE pes_pessoa_complemento ADD COLUMN id_responsavel integer;


ALTER TABLE pes_pessoa_complemento
  ADD CONSTRAINT fk_pes_pessoa_complemento_id_responsavel FOREIGN KEY (id_responsavel)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

	  
ALTER TABLE fin_servicos ADD COLUMN is_valor_fixo BOOLEAN DEFAULT FALSE;
-- UPDATE fin_servicos SET is_valor_fixo = FALSE;


ALTER TABLE fin_servico_pessoa ADD COLUMN nr_valor_fixo double precision;
UPDATE fin_servico_pessoa SET nr_valor_fixo = 0;


INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 253, 'COBRANÇA MENSAL', '"/Sindical/cobrancaMensal.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 253);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;