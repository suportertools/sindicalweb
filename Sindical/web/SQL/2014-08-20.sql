
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 261, 'INDEX ACESSO WEB', '"/Sindical/indexAcessoWeb.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 261);
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
