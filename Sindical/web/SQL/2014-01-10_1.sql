-- Table: eve_evento_baile_mapa

-- DROP TABLE eve_evento_baile_mapa;

CREATE TABLE eve_evento_baile_mapa
(
  id serial NOT NULL,
  nr_mesa integer,
  ds_posicao character varying(255),
  id_evento_baile integer,
  CONSTRAINT eve_evento_baile_mapa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_eve_evento_baile_mapa_id_evento_baile FOREIGN KEY (id_evento_baile)
      REFERENCES eve_evento_baile (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE eve_evento_baile_mapa
  OWNER TO postgres;

DROP TABLE eve_mesa;

-- Table: eve_mesa

-- DROP TABLE eve_mesa;

CREATE TABLE eve_mesa
(
  id serial NOT NULL,
  id_evento_baile_mapa integer,
  id_status integer,
  id_venda integer,
  CONSTRAINT eve_mesa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_eve_mesa_id_evento_baile_mapa FOREIGN KEY (id_evento_baile_mapa)
      REFERENCES eve_evento_baile_mapa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_eve_mesa_id_status FOREIGN KEY (id_status)
      REFERENCES eve_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_eve_mesa_id_venda FOREIGN KEY (id_venda)
      REFERENCES eve_venda (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE eve_mesa
  OWNER TO postgres;








ALTER TABLE eve_evento_servico
   ADD COLUMN ds_descricao character varying(500);


-- Table: fin_caixa

-- DROP TABLE fin_caixa;

CREATE TABLE fin_caixa
(
  id serial NOT NULL,
  nr_caixa integer,
  ds_descricao character varying(100),
  id_filial integer,
  CONSTRAINT fin_caixa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_caixa_id_filial FOREIGN KEY (id_filial)
      REFERENCES pes_filial (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_caixa
  OWNER TO postgres;



ALTER TABLE fin_conta_saldo
  DROP COLUMN id_tipo_servico;
ALTER TABLE fin_conta_saldo
  DROP COLUMN id_servicos;

ALTER TABLE fin_conta_saldo
   ADD COLUMN id_caixa integer;

ALTER TABLE fin_conta_saldo
   ADD COLUMN nr_valor_informado double precision;

ALTER TABLE fin_conta_saldo
  ADD CONSTRAINT fk_fin_conta_saldo_id_caixa FOREIGN KEY (id_caixa) REFERENCES fin_caixa (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_fin_conta_saldo_id_caixa
  ON fin_conta_saldo(id_caixa);

ALTER TABLE seg_mac_filial
   ADD COLUMN id_caixa integer;

ALTER TABLE seg_mac_filial
  ADD CONSTRAINT fk_seg_mac_filial_id_caixa FOREIGN KEY (id_caixa)
      REFERENCES fin_caixa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE fin_baixa
   ADD COLUMN id_caixa integer;

ALTER TABLE fin_baixa
  ADD CONSTRAINT fk_fin_baixa_id_caixa FOREIGN KEY (id_caixa) REFERENCES fin_caixa (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_fin_baixa_id_caixa
  ON fin_baixa(id_caixa);


ALTER TABLE fin_baixa
  DROP COLUMN dt_fechamento_caixa;

ALTER TABLE fin_baixa
   ADD COLUMN id_fechamento_caixa integer;

ALTER TABLE fin_baixa
  ADD CONSTRAINT fk_fin_baixa_id_fechamento_caixa FOREIGN KEY (id_fechamento_caixa) REFERENCES fin_fechamento_caixa (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_fin_baixa_id_fechamento_caixa
  ON fin_baixa(id_fechamento_caixa);


ALTER TABLE fin_status
   ADD COLUMN ds_historico character varying(500);

INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 203, 'PESQUISA VENDAS CARAVANA', '"/Sindical/pesquisaVendasCaravana.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 203);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 204, 'CAIXA', '"/Sindical/caixa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 204);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 205, 'MENU LOCADORA', '"/Sindical/menuLocadora.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 205);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 206, 'MENU ATENDIMENTO', '"/Sindical/menuAtendimento.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 206);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 207, 'PISO SALARIAL', '"/Sindical/pisoSalarial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 207);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 208, 'BLOQUEIO DE SERVIÇOS', '"/Sindical/bloqueioServicos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 208);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 209, 'AGRUPA TURMA', '"/Sindical/agrupaTurma.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM esc_agrupa_turma WHERE id = 209);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 210, 'PESQUISA AGRUPA TURMA', '"/Sindical/pesquisaAgrupaTurma.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM esc_agrupa_turma WHERE id = 210);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

INSERT INTO fin_cobranca_tipo (id, ds_descricao) VALUES (6, 'ETIQUETAS PARA EMPRESAS');
INSERT INTO fin_cobranca_tipo (id, ds_descricao) VALUES (7, 'ETIQUETAS PARA ESCRITÓRIOS');



