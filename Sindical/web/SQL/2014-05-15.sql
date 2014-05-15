INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 248, 'RECIBO GERADO', '"/Sindical/reciboGerado.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 248);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;


ALTER TABLE fin_servico_pessoa DROP COLUMN id_responsavel;
ALTER TABLE fin_lote DROP COLUMN id_matricula_socios;
ALTER TABLE fin_movimento ADD COLUMN id_matricula_socios integer;

ALTER TABLE fin_movimento
  ADD CONSTRAINT fk_fin_movimento_id_matricula_socios FOREIGN KEY (id_matricula_socios) REFERENCES matr_socios (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_fin_movimento_id_matricula_socios
  ON fin_movimento(id_matricula_socios);

-- Table: fin_grupo

-- DROP TABLE fin_grupo;

CREATE TABLE fin_grupo
(
  id serial NOT NULL,
  ds_descricao character varying(100),
  id_plano5 integer,
  CONSTRAINT fin_grupo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_grupo_id_plano5 FOREIGN KEY (id_plano5)
      REFERENCES fin_plano5 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_grupo
  OWNER TO postgres;
  
  
-- Table: fin_subgrupo

-- DROP TABLE fin_subgrupo;

CREATE TABLE fin_subgrupo
(
  id serial NOT NULL,
  ds_descricao character varying(100),
  id_grupo integer,
  CONSTRAINT fin_subgrupo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_subgrupo_id_grupo FOREIGN KEY (id_grupo)
      REFERENCES fin_grupo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_subgrupo
  OWNER TO postgres;

  
ALTER TABLE fin_servicos ADD CONSTRAINT fk_fin_servicos_id_subgrupo FOREIGN KEY (id_subgrupo) REFERENCES fin_subgrupo (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_centro_custo_contabil_sub DROP COLUMN ds_es;


INSERT INTO soc_motivo_inativacao (id, ds_descricao) values (1, 'INADIMPLÊNCIA');
INSERT INTO soc_motivo_inativacao (id, ds_descricao) values (2, 'SOLICITAÇÃO');
INSERT INTO soc_motivo_inativacao (id, ds_descricao) values (3, 'OPOSIÇÃO');
INSERT INTO soc_motivo_inativacao (id, ds_descricao) values (4, 'MUDANÇA DE CATEGORIA');




