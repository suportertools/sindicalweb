-- Table: ate_operacao

-- DROP TABLE ate_operacao;

CREATE TABLE ate_operacao
(
  id serial NOT NULL,
  ds_descricao character varying(50) NOT NULL,
  CONSTRAINT ate_operacao_pkey PRIMARY KEY (id),
  CONSTRAINT ate_operacao_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ate_operacao
  OWNER TO postgres;

INSERT INTO ate_operacao (id, ds_descricao) SELECT 1, 'Calculos' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 1);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 2, 'Juridico' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 2);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 3, 'Colônia de Férias' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 3);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 4, 'Filiações' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 4);
INSERT INTO ate_operacao (id, ds_descricao) SELECT 5, 'Outros' WHERE NOT EXISTS ( SELECT id FROM ate_operacao WHERE id = 5);
SELECT setval('ate_operacao_id_seq', max(id)) FROM ate_operacao;


-- Table: sis_pessoa

-- DROP TABLE sis_pessoa;

CREATE TABLE sis_pessoa
(
  id serial NOT NULL,
  ds_documento character varying(30) NOT NULL,
  ds_rg character varying(12) NOT NULL,
  ds_nome character varying(200) NOT NULL,
  ds_telefone character varying(20),
  id_tipo_documento integer,
  id_endereco integer,
  ds_sexo character varying(1),
  ds_complemento character varying(150),
  ds_numero character varying(30),
  ds_celular character varying(20),
  dt_nascimento date,
  ds_email1 character varying(50),
  ds_email2 character varying(50),
  ds_obs character varying(300),
  dt_criacao date,
  CONSTRAINT ate_pessoa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_sis_pessoa_id_endereco FOREIGN KEY (id_endereco)
      REFERENCES end_endereco (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_sis_pessoa_id_tipo_documento FOREIGN KEY (id_tipo_documento)
      REFERENCES pes_tipo_documento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_pessoa
  OWNER TO postgres;



-- Table: ate_movimento

-- DROP TABLE ate_movimento;

CREATE TABLE ate_movimento
(
  id serial NOT NULL,
  ds_historico character varying(500),
  ds_hora character varying(5),
  dt_emissao date,
  id_filial integer NOT NULL,
  id_sis_pessoa integer NOT NULL,
  id_operacao integer NOT NULL,
  CONSTRAINT ate_movimento_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ate_movimento_id_sis_pessoa FOREIGN KEY (id_sis_pessoa)
      REFERENCES sis_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ate_movimento_id_filial FOREIGN KEY (id_filial)
      REFERENCES pes_filial (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_ate_movimento_id_operacao FOREIGN KEY (id_operacao)
      REFERENCES ate_operacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE ate_movimento
  OWNER TO postgres;




-- sis_semana
-- Criate: 2013-07-24
-- Last edition: 2013-07-24 - by: Bruno Vieira

INSERT INTO sis_semana (id, ds_descricao) SELECT 1, 'Domingo'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 1 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 2, 'Segunda'   WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 2 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 3, 'Terça'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 3 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 4, 'Quarta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 4 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 5, 'Quinta'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 5 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 6, 'Sexta'     WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 6 );
INSERT INTO sis_semana (id, ds_descricao) SELECT 7, 'Sábado'    WHERE NOT EXISTS ( SELECT id FROM sis_semana WHERE id = 7 );
SELECT setval('sis_semana_id_seq', max(id)) FROM sis_semana;


INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 20,'Extrato com Valor Líquido','/Relatorios/EXTRATO_ARRECADACAO_LIQUIDO.jasper',110,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 20);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 21,'Contribuintes por Escritório Endereço','/Relatorios/CONTRIBUINTESPORESCRITORIO.jasper',5,'ccon.ds_uf,ccon.ds_cidade,bcon.ds_descricao,decon.ds_descricao,pecon.ds_numero,conpes.ds_nome,conpes.id,p.ds_nome','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 21);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 22,'Convites Clube','/Relatorios/RELATORIO_CONVITES_CLUBE.jasper', 226,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 22);
INSERT INTO sis_relatorios (id, ds_nome, ds_jasper, id_rotina, ds_qry_ordem, ds_qry) SELECT 23,'Oposição','/Relatorios/RELATORIO_OPOSICAO.jasper', 163,'','' WHERE NOT EXISTS ( SELECT id FROM sis_relatorios WHERE id = 23);
SELECT setval('sis_relatorios_id_seq', max(id)) FROM sis_relatorios;

-- Table: sis_cor

-- DROP TABLE sis_cor;

CREATE TABLE sis_cor
(
  id serial NOT NULL,
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT sis_cor_pkey PRIMARY KEY (id),
  CONSTRAINT sis_cor_ds_descricao_key UNIQUE (ds_descricao),
  CONSTRAINT sis_cor_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_cor
  OWNER TO postgres;




-- Table: est_grupo

-- DROP TABLE est_grupo;

CREATE TABLE est_grupo
(
  id serial NOT NULL,
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT est_grupo_pkey PRIMARY KEY (id),
  CONSTRAINT est_grupo_ds_descricao_key UNIQUE (ds_descricao),
  CONSTRAINT est_grupo_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_grupo
  OWNER TO postgres;


-- Table: est_subgrupo

-- DROP TABLE est_subgrupo;

CREATE TABLE est_subgrupo
(
  id serial NOT NULL,
  id_grupo integer NOT NULL,  
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT est_subgrupo_pkey PRIMARY KEY (id),
  CONSTRAINT est_subgrupo_ds_descricao_check CHECK (ds_descricao::text <> ''::text),
  CONSTRAINT fk_est_subgrupo_id_grupo FOREIGN KEY (id_grupo)
      REFERENCES est_grupo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_est_grupo_0 UNIQUE (ds_descricao, id_grupo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_subgrupo
  OWNER TO postgres;

-- Table: est_tipo

-- DROP TABLE est_tipo;

CREATE TABLE est_tipo
(
  id serial NOT NULL,
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT est_tipo_pkey PRIMARY KEY (id),
  CONSTRAINT est_tipo_ds_descricao_key UNIQUE (ds_descricao),
  CONSTRAINT est_tipo_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_tipo
  OWNER TO postgres;

INSERT INTO est_tipo (id, ds_descricao) SELECT 1,'CONSUMO' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 1);
INSERT INTO est_tipo (id, ds_descricao) SELECT 2,'VENDAS' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 2);
SELECT setval('est_tipo_id_seq', max(id)) FROM est_tipo;


-- Table: est_unidade

-- DROP TABLE est_unidade;

CREATE TABLE est_unidade
(
  id serial NOT NULL,
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT est_unidade_pkey PRIMARY KEY (id),
  CONSTRAINT est_unidade_ds_descricao_key UNIQUE (ds_descricao),
  CONSTRAINT est_unidade_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_unidade
  OWNER TO postgres;



-- Table: est_produto

-- DROP TABLE est_produto;

CREATE TABLE est_produto
(
  id serial NOT NULL,
  ds_barras character varying(25),
  ds_obs character varying(5000),
  ds_modelo character varying(255),
  ds_descricao character varying(100) NOT NULL,
  nr_qtde_embalagem integer DEFAULT 0,
  ds_fabricante character varying(100),
  dt_cadastro date,
  ds_medida character varying(25),
  ds_sabor character varying(100),
  ds_marca character varying(100),
  id_subgrupo integer,
  id_grupo integer NOT NULL,
  id_cor integer NOT NULL,
  id_unidade integer NOT NULL,
  CONSTRAINT est_produto_pkey PRIMARY KEY (id),
  CONSTRAINT fk_est_produto_id_cor FOREIGN KEY (id_cor)
      REFERENCES sis_cor (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_produto_id_grupo FOREIGN KEY (id_grupo)
      REFERENCES est_grupo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_produto_id_subgrupo FOREIGN KEY (id_subgrupo)
      REFERENCES est_subgrupo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_produto_id_unidade FOREIGN KEY (id_unidade)
      REFERENCES est_unidade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT est_produto_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_produto
  OWNER TO postgres;



-- Table: est_estoque

-- DROP TABLE est_estoque;

CREATE TABLE est_estoque
(
  id serial NOT NULL,
  nr_custo_medio double precision DEFAULT 0,
  ativo boolean,
  nr_estoque_minimo integer DEFAULT 1,
  nr_estoque integer DEFAULT 0,
  nr_estoque_maximo integer DEFAULT 1,
  id_tipo integer,
  id_filial integer,
  id_produto integer,
  CONSTRAINT est_estoque_pkey PRIMARY KEY (id),
  CONSTRAINT fk_est_estoque_id_filial FOREIGN KEY (id_filial)
      REFERENCES pes_filial (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_estoque_id_produto FOREIGN KEY (id_produto)
      REFERENCES est_produto (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_estoque_id_tipo FOREIGN KEY (id_tipo)
      REFERENCES est_tipo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT unq_est_estoque_0 UNIQUE (id_produto, id_filial, id_tipo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_estoque
  OWNER TO postgres;



-- Table: est_pedido

-- DROP TABLE est_pedido;

CREATE TABLE est_pedido
(
  id serial NOT NULL,
  nr_desconto_unitario double precision DEFAULT 0,
  nr_valor_unitario double precision DEFAULT 0,
  nr_quantidade integer DEFAULT 0,
  id_tipo integer DEFAULT 1,
  id_produto integer,
  id_lote integer,
  CONSTRAINT est_pedido_pkey PRIMARY KEY (id),
  CONSTRAINT fk_est_pedido_id_lote FOREIGN KEY (id_lote)
      REFERENCES fin_lote (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_pedido_id_produto FOREIGN KEY (id_produto)
      REFERENCES est_produto (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_est_pedido_id_tipo FOREIGN KEY (id_tipo)
      REFERENCES est_tipo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE est_pedido
  OWNER TO postgres;


-- Table: conv_autoriza_cortesia

-- DROP TABLE conv_autoriza_cortesia;

CREATE TABLE conv_autoriza_cortesia
(
  id serial NOT NULL,
  id_pessoa integer NOT NULL,
  CONSTRAINT conv_autoriza_cortesia_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_autoriza_cortesia_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_autoriza_cortesia
  OWNER TO postgres;


-- Table: conv_motivo_suspencao

-- DROP TABLE conv_motivo_suspencao;

CREATE TABLE conv_motivo_suspencao
(
  id serial NOT NULL,
  ds_descricao character varying(255) NOT NULL,
  CONSTRAINT conv_motivo_suspencao_pkey PRIMARY KEY (id),
  CONSTRAINT conv_motivo_suspencao_ds_descricao_check CHECK (ds_descricao::text <> ''::text)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_motivo_suspencao
  OWNER TO postgres;


-- Table: conv_servico

-- DROP TABLE conv_servico;

CREATE TABLE conv_servico
(
  id serial NOT NULL,
  is_quinta boolean DEFAULT false,
  is_sexta boolean DEFAULT false,
  is_segunda boolean DEFAULT false,
  is_sabado boolean DEFAULT false,
  is_quarta boolean DEFAULT false,
  is_domingo boolean DEFAULT false,
  is_terca boolean DEFAULT false,
  id_servico integer,
  is_feriado boolean,
  CONSTRAINT conv_servico_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_servico_id_servico FOREIGN KEY (id_servico)
      REFERENCES fin_servicos (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_servico
  OWNER TO postgres;




-- Table: conv_movimento

-- DROP TABLE conv_movimento;

CREATE TABLE conv_movimento
(
  id serial NOT NULL,
  is_cortesia boolean DEFAULT false,
  is_ativo boolean DEFAULT true,
  ds_obs character varying(300),
  dt_validade date,
  dt_emissao date,
  id_usuario integer NOT NULL,
  id_evt integer,
  id_departamemtno integer,
  id_pessoa integer NOT NULL,
  id_sis_pessoa integer NOT NULL,
  id_servicos integer,
  id_usuario_inativacao integer,
  id_autoriza_cortesia integer,
  CONSTRAINT conv_movimento_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_movimento_id_autoriza_cortesia FOREIGN KEY (id_autoriza_cortesia)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_departamemtno FOREIGN KEY (id_departamemtno)
      REFERENCES seg_departamento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_evt FOREIGN KEY (id_evt)
      REFERENCES fin_evt (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_servicos FOREIGN KEY (id_servicos)
      REFERENCES conv_servico (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_sis_pessoa FOREIGN KEY (id_sis_pessoa)
      REFERENCES sis_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_movimento_id_usuario_inativacao FOREIGN KEY (id_usuario_inativacao)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_movimento
  OWNER TO postgres;


-- Table: conv_exporta

-- DROP TABLE conv_exporta;

CREATE TABLE conv_exporta
(
  id serial NOT NULL,
  dt_exportacao date,
  id_conv_movimento integer NOT NULL,
  CONSTRAINT conv_exporta_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_exporta_id_conv_movimento FOREIGN KEY (id_conv_movimento)
      REFERENCES conv_movimento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_exporta
  OWNER TO postgres;



-- Table: conv_suspencao

-- DROP TABLE conv_suspencao;

CREATE TABLE conv_suspencao
(
  id serial NOT NULL,
  dt_inicio date,
  dt_fim date,
  ds_obs character varying(300),
  id_sis_pessoa integer NOT NULL,
  id_motivo_suspencao integer NOT NULL,
  CONSTRAINT conv_suspencao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_suspencao_id_motivo_suspencao FOREIGN KEY (id_motivo_suspencao)
      REFERENCES conv_motivo_suspencao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_conv_suspencao_id_sis_pessoa FOREIGN KEY (id_sis_pessoa)
      REFERENCES sis_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_suspencao
  OWNER TO postgres;
