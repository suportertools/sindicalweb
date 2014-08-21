
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 261, 'INDEX ACESSO WEB', '"/Sindical/indexAcessoWeb.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 261);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 262, 'WEB AGENDAMENTO CONTÁBIL', '"/Sindical/webAgendamentoContabilidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 262);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 263, 'WEB AGENDAMENTO CONTRIBUINTE', '"/Sindical/webAgendamentoContribuinte.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 263);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 264, 'WEB IMPRESSÃO DE BOLETOS', '"/Sindical/webContabilidade.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 264);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 265, 'WEB IMPRESSÃO DE BOLETOS', '"/Sindical/webContribuinte.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 265);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 266, 'WEB LIBERAÇÃO REPIS', '"/Sindical/webLiberacaoREPIS.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 266);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 267, 'WEB SOLICITAÇÃO REPIS', '"/Sindical/webSolicitaREPIS.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 267);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

ALTER TABLE seg_registro ALTER COLUMN id_servico_cartao DROP NOT NULL;

-- Table: soc_categoria_desconto_dependente

-- DROP TABLE soc_categoria_desconto_dependente;

CREATE TABLE soc_categoria_desconto_dependente
(
  id serial NOT NULL,
  nr_desconto double precision,
  id_parentesco integer,
  id_categoria_desconto integer,
  CONSTRAINT soc_categoria_desconto_dependente_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_categoria_desconto_dependente_id_categoria_desconto FOREIGN KEY (id_categoria_desconto)
      REFERENCES soc_categoria_desconto (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_categoria_desconto_dependente_id_parentesco FOREIGN KEY (id_parentesco)
      REFERENCES soc_parentesco (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_categoria_desconto_dependente
  OWNER TO postgres;
