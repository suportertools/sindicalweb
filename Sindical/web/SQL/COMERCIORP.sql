-- Table: age_grupo_agenda

-- DROP TABLE age_grupo_agenda;

CREATE TABLE age_grupo_agenda
(
  id serial NOT NULL,
  ds_descricao character varying(100) NOT NULL,
  CONSTRAINT age_grupo_agenda_pkey PRIMARY KEY (id),
  CONSTRAINT age_grupo_agenda_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE age_grupo_agenda
  OWNER TO postgres;


-- Table: age_telefone

-- DROP TABLE age_telefone;

CREATE TABLE age_telefone
(
  id serial NOT NULL,
  ds_contato character varying(50),
  ds_telefone character varying(20),
  id_agenda integer NOT NULL,
  id_tipo_telefone integer NOT NULL,
  ds_ddi character varying(2),
  ds_ddd character varying(2),
  CONSTRAINT age_telefone_pkey PRIMARY KEY (id),
  CONSTRAINT fk_age_telefone_id_agenda FOREIGN KEY (id_agenda)
      REFERENCES age_agenda (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_age_telefone_id_tipo_telefone FOREIGN KEY (id_tipo_telefone)
      REFERENCES age_tipo_telefone (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE age_telefone
  OWNER TO postgres;


-- Table: age_tipo_telefone

-- DROP TABLE age_tipo_telefone;

CREATE TABLE age_tipo_telefone
(
  id serial NOT NULL,
  ds_descricao character varying(50),
  CONSTRAINT age_tipo_telefone_pkey PRIMARY KEY (id),
  CONSTRAINT age_tipo_telefone_ds_descricao_key UNIQUE (ds_descricao)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE age_tipo_telefone
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
  id_ate_pessoa integer NOT NULL,
  id_operacao integer NOT NULL,
  CONSTRAINT ate_movimento_pkey PRIMARY KEY (id),
  CONSTRAINT fk_ate_movimento_id_ate_pessoa FOREIGN KEY (id_ate_pessoa)
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
  CONSTRAINT sis_pessoa_pkey PRIMARY KEY (id),
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


-- Table: conv_motivo_suspencao

-- DROP TABLE conv_motivo_suspencao;

CREATE TABLE conv_motivo_suspencao
(
  id serial NOT NULL,
  ds_descricao character varying(255) NOT NULL,
  CONSTRAINT conv_motivo_suspencao_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE conv_motivo_suspencao
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
  id_sis_pessoa integer NOT NULL,
  id_evt integer NOT NULL,
  id_usuario_inativacao integer NOT NULL,
  id_pessoa integer NOT NULL,
  id_departamemtno integer NOT NULL,
  id_autoriza_cortesia integer NOT NULL,
  CONSTRAINT conv_movimento_pkey PRIMARY KEY (id),
  CONSTRAINT fk_conv_movimento_id_autoriza_cortesia FOREIGN KEY (id_autoriza_cortesia)
      REFERENCES conv_autoriza_cortesia (id) MATCH SIMPLE
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
  feriado boolean,
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



-- Table: pes_spc

-- DROP TABLE pes_spc;

CREATE TABLE pes_spc
(
  id serial NOT NULL,
  dt_saida date,
  ds_obs character varying(200),
  dt_entrada date,
  id_pessoa integer NOT NULL,
  CONSTRAINT pes_spc_pkey PRIMARY KEY (id),
  CONSTRAINT fk_pes_spc_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE pes_spc
  OWNER TO postgres;


-- Table: sis_configuracao

-- DROP TABLE sis_configuracao;

CREATE TABLE sis_configuracao
(
  id serial NOT NULL,
  ds_persistence character varying(200) NOT NULL,
  ds_caminho_sistema character varying(200) NOT NULL,
  ds_nome_cliente character varying(300) NOT NULL,
  ds_identifica character varying(100) NOT NULL,
  CONSTRAINT sis_configuracao_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE sis_configuracao
  OWNER TO postgres;


-- Table: soc_geracao

-- DROP TABLE soc_geracao;

CREATE TABLE soc_geracao
(
  id serial NOT NULL,
  ds_vencimento character varying(7),
  dt_lancamento date,
  id_servico_pessoa integer,
  CONSTRAINT soc_geracao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_geracao_id_servico_pessoa FOREIGN KEY (id_servico_pessoa)
      REFERENCES fin_servico_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_geracao
  OWNER TO postgres;

-- Table: soc_historico_carteirinha

-- DROP TABLE soc_historico_carteirinha;

CREATE TABLE soc_historico_carteirinha
(
  id serial NOT NULL,
  ds_hora character varying(255),
  ds_descricao character varying(255),
  dt_emissao date,
  id_socio integer,
  CONSTRAINT soc_historico_carteirinha_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_historico_carteirinha_id_socio FOREIGN KEY (id_socio)
      REFERENCES soc_socios (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_historico_carteirinha
  OWNER TO postgres;



-- Sequence: age_telefone_id_seq

-- DROP SEQUENCE age_telefone_id_seq;

CREATE SEQUENCE age_telefone_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE age_telefone_id_seq
  OWNER TO postgres;


-- Sequence: age_tipo_telefone_id_seq

-- DROP SEQUENCE age_tipo_telefone_id_seq;

CREATE SEQUENCE age_tipo_telefone_id_seq
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 0
  CACHE 1;
ALTER TABLE age_tipo_telefone_id_seq
  OWNER TO postgres;

-- Function: func_valor_folha_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_valor_folha_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_valor_folha_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare tipo int        :=idtiposervico;
declare ref           varchar(7) :=ref;
declare valor         float:=0;
BEGIN
   if (tipo <> 4 and valor =0) then
       valor :=
          (
           select (f.nr_num_funcionarios*d.nr_valor_por_empregado)+(d.nr_percentual*f.nr_valor/100) from 
           arr_contribuintes_vw                 as j
           inner join arr_faturamento_folha_empresa as f  on f.id_juridica=j.id_juridica and f.ds_referencia=ref and f.id_tipo_servico=tipo
           inner join arr_desconto_empregado as d on d.id_servicos=idServico and d.id_convencao=j.id_convencao and d.id_grupo_cidade=j.id_grupo_cidade
           and
           (substring(ref,4,4)||substring(ref,1,2)) >= (substring(d.ds_ref_inicial,4,4)||substring(d.ds_ref_inicial,1,2)) and
           (substring(ref,4,4)||substring(ref,1,2)) <= (substring(d.ds_ref_final,4,4)||substring(d.ds_ref_final,1,2))
           where  j.id_pessoa=idPessoa
          );
       if (valor is null) then
          valor:=0;
       end if;
    end if;
    RETURN round(cast( valor*100 as decimal) / 100, 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor_folha_sm(integer, integer, integer, character varying)
  OWNER TO postgres;



-- Function: func_valor(integer)

-- DROP FUNCTION func_valor(integer);

CREATE OR REPLACE FUNCTION func_valor(mov integer)
  RETURNS double precision AS
$BODY$

    declare valor double precision;
    declare vencto date;
    
BEGIN
    vencto = (select dt_vencimento from fin_movimento where id=mov);
    valor  = (select nr_valor from fin_movimento where id=mov);
    if (vencto >= current_date) then
       valor  = (select nr_valor-nr_desconto_ate_vencimento from fin_movimento where id=mov);
    end if;
 
    RETURN valor;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor(integer)
  OWNER TO postgres;
