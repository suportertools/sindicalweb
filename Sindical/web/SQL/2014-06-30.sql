-- Table: pes_nacionalidade

-- DROP TABLE pes_nacionalidade;

CREATE TABLE pes_nacionalidade
(
  id serial NOT NULL,
  ds_descricao character varying(50) NOT NULL,
  CONSTRAINT pes_nacionalidade_pkey PRIMARY KEY (id),
  CONSTRAINT pes_nacionalidade_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_nacionalidade
  OWNER TO postgres;


-- Table: pes_indicador_alvara

-- DROP TABLE pes_indicador_alvara;

CREATE TABLE pes_indicador_alvara
(
  id serial NOT NULL,
  ds_descricao character varying(150) NOT NULL,
  CONSTRAINT pes_indicador_alvara_pkey PRIMARY KEY (id),
  CONSTRAINT pes_indicador_alvara_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_indicador_alvara
  OWNER TO postgres;


-- Table: pes_raca

-- DROP TABLE pes_raca;

CREATE TABLE pes_raca
(
  id serial NOT NULL,
  ds_descricao character varying(50) NOT NULL,
  CONSTRAINT pes_raca_pkey PRIMARY KEY (id),
  CONSTRAINT pes_raca_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_raca
  OWNER TO postgres;


-- Table: pes_tipo_deficiencia

-- DROP TABLE pes_tipo_deficiencia;

CREATE TABLE pes_tipo_deficiencia
(
  id serial NOT NULL,
  ds_descricao character varying(50) NOT NULL,
  CONSTRAINT pes_tipo_deficiencia_pkey PRIMARY KEY (id),
  CONSTRAINT pes_tipo_deficiencia_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_tipo_deficiencia
  OWNER TO postgres;


-- Table: pes_escolaridade

-- DROP TABLE pes_escolaridade;

CREATE TABLE pes_escolaridade
(
  id serial NOT NULL,
  ds_descricao character varying(255) NOT NULL,
  CONSTRAINT pes_escolaridade_pkey PRIMARY KEY (id),
  CONSTRAINT pes_escolaridade_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_escolaridade
  OWNER TO postgres;


-- Table: fin_tipo_remuneracao

-- DROP TABLE fin_tipo_remuneracao;

CREATE TABLE fin_tipo_remuneracao
(
  id serial NOT NULL,
  ds_descricao character varying(20),
  CONSTRAINT fin_tipo_remuneracao_pkey PRIMARY KEY (id),
  CONSTRAINT fin_tipo_remuneracao_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_tipo_remuneracao
  OWNER TO postgres;

-- Table: fin_salario_minimo

-- DROP TABLE fin_salario_minimo;

CREATE TABLE fin_salario_minimo
(
  id serial NOT NULL,
  nr_valor_diario double precision DEFAULT 0,
  nr_valor_hora double precision DEFAULT 0,
  nr_valor_mensal double precision DEFAULT 0,
  ds_norma character varying(255),
  dt_vigencia date,
  dt_publicacao date,
  CONSTRAINT fin_salario_minimo_pkey PRIMARY KEY (id),
  CONSTRAINT unq_fin_salario_minimo_0 UNIQUE (dt_vigencia, nr_valor_mensal)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_salario_minimo
  OWNER TO postgres;

-- Table: pes_classificacao_economica

-- DROP TABLE pes_classificacao_economica;

CREATE TABLE pes_classificacao_economica
(
  id serial NOT NULL,
  nr_salario_minimo_inicial integer DEFAULT 0,
  nr_salario_minimo_final integer DEFAULT 0,
  ds_descricao character varying(255) NOT NULL,
  dt_atualizado date,
  CONSTRAINT pes_classificacao_economica_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_classificacao_economica
  OWNER TO postgres;

-- Table: arr_rais

-- DROP TABLE arr_rais;

CREATE TABLE arr_rais
(
  id serial NOT NULL,
  nr_ano_chegada integer DEFAULT 0,
  is_alvara boolean DEFAULT false,
  dt_admissao date,
  is_empregado_filiado boolean DEFAULT false,
  dt_afastamento date,
  ds_pis character varying(13),
  ds_observacao character varying(500),
  ds_serie character varying(15),
  nr_ctps integer DEFAULT 0,
  ds_funcao character varying(255),
  dt_emissao date,
  ds_sexo character varying(1),
  dt_demissao date,
  ds_carteira character varying(9),
  ds_motivo_afastamento character varying(500),
  nr_salario double precision DEFAULT 0,
  nr_carga_horaria integer DEFAULT 0,
  id_raca integer,
  id_indicador_alvara integer,
  id_sis_pessoa integer,
  id_tipo_deficiencia integer,
  id_empresa integer,
  id_escolaridade integer,
  id_responsavel_cadastro integer,
  id_nacionalidade integer,
  id_profissao integer,
  id_tipo_remuneracao integer,
  id_classificacao_economica integer,
  CONSTRAINT arr_rais_pkey PRIMARY KEY (id),
  CONSTRAINT fk_arr_rais_id_classificacao_economica FOREIGN KEY (id_classificacao_economica)
      REFERENCES pes_classificacao_economica (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_empresa FOREIGN KEY (id_empresa)
      REFERENCES pes_juridica (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_escolaridade FOREIGN KEY (id_escolaridade)
      REFERENCES pes_escolaridade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_indicador_alvara FOREIGN KEY (id_indicador_alvara)
      REFERENCES pes_indicador_alvara (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_nacionalidade FOREIGN KEY (id_nacionalidade)
      REFERENCES pes_nacionalidade (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_profissao FOREIGN KEY (id_profissao)
      REFERENCES pes_profissao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_raca FOREIGN KEY (id_raca)
      REFERENCES pes_raca (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_responsavel_cadastro FOREIGN KEY (id_responsavel_cadastro)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_sis_pessoa FOREIGN KEY (id_sis_pessoa)
      REFERENCES sis_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_tipo_deficiencia FOREIGN KEY (id_tipo_deficiencia)
      REFERENCES pes_tipo_deficiencia (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_arr_rais_id_tipo_remuneracao FOREIGN KEY (id_tipo_remuneracao)
      REFERENCES fin_tipo_remuneracao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE arr_rais
  OWNER TO postgres;




INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 1, 'Analfabeto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 1);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 2, 'Sabe ler/escrever, mas não cursou escola' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 2);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 3, 'Até a 4ª série do ensino fundamental incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 3);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 4, 'Até a 4ª série do ensino fundamental completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 4);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 5, 'De 5ª à 8ª série do ensino fundamental/antigo/ginásio incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 5);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 6, 'De 5ª à 8ª série do ensino fundamental/antigo/ginásio completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 6);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 7, 'Ensino médio/colegial/1ª a 3ª série incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 7);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 8, 'Ensino médio/colegial/1ª a 3ª série completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 8);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 9, 'Superior incompleto' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 9);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 10, 'Superior completo' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 10);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 11, 'Pós-graduação / Mestrado / Doutorado' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 11);
INSERT INTO pes_escolaridade (id, ds_descricao) SELECT 12, '(NR)' WHERE NOT EXISTS ( SELECT id FROM pes_escolaridade WHERE id = 12);
SELECT setval('pes_escolaridade_id_seq', max(id)) FROM pes_escolaridade;



INSERT INTO pes_indicador_alvara (id, ds_descricao) SELECT 1, 'O funcionário tem alvará judicial para trabalhar' WHERE NOT EXISTS ( SELECT id FROM pes_indicador_alvara WHERE id = 1);
INSERT INTO pes_indicador_alvara (id, ds_descricao) SELECT 2, 'O funcionário não tem alvará judicial para trabalhar' WHERE NOT EXISTS ( SELECT id FROM pes_indicador_alvara WHERE id = 2);
SELECT setval('pes_indicador_alvara_id_seq', max(id)) FROM pes_indicador_alvara;


INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 10, 'Brasileiro' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 10);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 20, 'Naturalizado Brasileiro' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 20);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 21, 'Argentino' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 21);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 22, 'Boliviano' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 22);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 23, 'Chileno' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 23);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 24, 'Paraguaio' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 24);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 25, 'Uruguaio' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 25);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 30, 'Alemão' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 30);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 31, 'Belga' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 31);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 32, 'Britânico' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 32);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 34, 'Canadense' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 34);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 35, 'Espanhol' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 35);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 36, 'Norte-americano (EUA)' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 36);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 37, 'Francês' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 37);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 38, 'Suíço' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 38);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 39, 'Italiano' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 39);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 41, 'Japonês' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 41);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 42, 'Chinês' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 42);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 43, 'Coreano' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 43);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 45, 'Português' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 45);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 48, 'Outros latino-americanos' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 48);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 49, 'Outros asiáticos' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 49);
INSERT INTO pes_nacionalidade (id, ds_descricao) SELECT 50, 'Outros' WHERE NOT EXISTS ( SELECT id FROM pes_nacionalidade WHERE id = 50);
SELECT setval('pes_nacionalidade_id_seq', max(id)) FROM pes_nacionalidade;


INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 1, 'A1', 20, 0 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 1);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 2, 'A2', 20, 0 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 2);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 3, 'B1', 10, 20 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 3);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 4, 'B2', 10, 20 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 4);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 5, 'C1', 4, 10 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 5);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 6, 'C2', 4, 10 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 6);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 7, 'D', 2, 4 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 7);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 8, 'E', 0, 2 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 8);
INSERT INTO pes_classificacao_economica (id, ds_descricao, nr_salario_minimo_inicial, nr_salario_minimo_final) SELECT 9, 'Recusa', 0, 0 WHERE NOT EXISTS ( SELECT id FROM pes_classificacao_economica WHERE id = 9);
SELECT setval('pes_classificacao_economica_id_seq', max(id)) FROM pes_classificacao_economica;



INSERT INTO pes_raca (id, ds_descricao) SELECT 1, 'Amarela' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 1);
INSERT INTO pes_raca (id, ds_descricao) SELECT 2, 'Branca' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 2);
INSERT INTO pes_raca (id, ds_descricao) SELECT 3, 'Índigena' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 3);
INSERT INTO pes_raca (id, ds_descricao) SELECT 4, 'Parda' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 4);
INSERT INTO pes_raca (id, ds_descricao) SELECT 5, 'Preta' WHERE NOT EXISTS ( SELECT id FROM pes_raca WHERE id = 5);
SELECT setval('pes_raca_id_seq', max(id)) FROM pes_raca;


INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 0, 'Nenhuma' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 0);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 1, 'Física' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 1);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 2, 'Auditiva' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 2);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 3, 'Visual' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 3);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 4, 'Intelectual (mental)' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 4);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 5, 'Mútipla' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 5);
INSERT INTO pes_tipo_deficiencia (id, ds_descricao) SELECT 6, 'Reabilitado' WHERE NOT EXISTS ( SELECT id FROM pes_tipo_deficiencia WHERE id = 6);
SELECT setval('pes_tipo_deficiencia_id_seq', max(id)) FROM pes_tipo_deficiencia;


INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 1, 'Mensalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 1);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 2, 'Quinzenalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 2);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 3, 'Semanalista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 3);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 4, 'Diarista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 4);
INSERT INTO fin_tipo_remuneracao (id, ds_descricao) SELECT 5, 'Horista'  WHERE NOT EXISTS ( SELECT id FROM fin_tipo_remuneracao WHERE id = 5);
SELECT setval('fin_tipo_remuneracao_id_seq', max(id)) FROM fin_tipo_remuneracao;



