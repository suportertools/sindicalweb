INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 254, 'ALTERAR VALOR FIXO', '"/Sindical/cobrancaMensal2.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 254);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;


-- Table: fin_impressao

-- DROP TABLE fin_impressao;

CREATE TABLE fin_impressao
(
  id serial NOT NULL,
  ds_hora character varying(8) NOT NULL,
  dt_vencimento date,
  dt_impressao date NOT NULL,
  id_usuario integer NOT NULL,
  id_movimento integer NOT NULL,
  CONSTRAINT fin_impressao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_impressao_id_movimento FOREIGN KEY (id_movimento)
      REFERENCES fin_movimento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_impressao_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_impressao
  OWNER TO postgres;
