-- SEGUNDA VERSÃO COMERCIO

ALTER TABLE fin_servicos ADD COLUMN is_produto BOOLEAN DEFAULT FALSE;

ALTER TABLE est_produto ADD COLUMN nr_valor DOUBLE PRECISION DEFAULT 0;


ALTER TABLE soc_convenio_servico ADD COLUMN is_encaminhamento boolean default false;
ALTER TABLE fin_guia ADD COLUMN is_encaminhamento boolean default false;
ALTER TABLE fin_guia ADD COLUMN id_convenio_sub_grupo integer;

UPDATE soc_convenio_servico SET is_encaminhamento = FALSE;
UPDATE fin_guia SET is_encaminhamento = FALSE;


create  VIEW soc_convenio_vw AS 
select p.id id_pessoa, j.id as id_juridica,g.id as id_grupo, s.id as id_subgrupo,g.ds_descricao as ds_grupo,s.ds_descricao as ds_subgrupo, p.ds_nome, 
e.logradouro,e.endereco,pe.ds_numero numero,pe.ds_complemento as complemento,e.bairro,e.cidade,e.uf,e.cep
from soc_convenio as c 
inner join pes_juridica as j on j.id=c.id_juridica
inner join pes_pessoa as p on p.id=j.id_pessoa
inner join pes_pessoa_endereco as pe on pe.id_pessoa=p.id and pe.id_tipo_endereco=2
inner join endereco_vw as e on e.id=pe.id_endereco 
inner join soc_convenio_sub_grupo as s on s.id=c.id_convenio_sub_grupo
inner join soc_convenio_grupo as g on g.id=s.id_grupo_convenio


INSERT INTO est_tipo (id, ds_descricao) SELECT 3,'BRINDES' WHERE NOT EXISTS ( SELECT id FROM est_tipo WHERE id = 3);
SELECT setval('est_tipo_id_seq', max(id)) FROM est_tipo;



-- TERCEIRA VERSÃO NO COMERCIO


ALTER TABLE seg_registro ADD COLUMN is_cobranca_carteirinha BOOLEAN DEFAULT FALSE;
UPDATE seg_registro SET is_cobranca_carteirinha  = false;

ALTER TABLE seg_registro ADD COLUMN is_validade_barras BOOLEAN DEFAULT FALSE;;
UPDATE seg_registro SET is_validade_barras = false;

-- Table: soc_modelo_carteirinha

-- DROP TABLE soc_modelo_carteirinha;

CREATE TABLE soc_modelo_carteirinha
(
  id serial NOT NULL,
  ds_jasper character varying(150),
  ds_descricao character varying(150),
  id_categoria integer,
  id_rotina integer,
  CONSTRAINT soc_modelo_carteirinha_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_modelo_carteirinha_id_categoria FOREIGN KEY (id_categoria)
      REFERENCES soc_categoria (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_modelo_carteirinha_id_rotina FOREIGN KEY (id_rotina)
      REFERENCES seg_rotina (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_modelo_carteirinha
  OWNER TO postgres;

  
-- Table: soc_autoriza_impressao_cartao

-- DROP TABLE soc_autoriza_impressao_cartao;

CREATE TABLE soc_autoriza_impressao_cartao
(
  id serial NOT NULL,
  dt_emissao date,
  id_historico_carteirinha integer,
  id_usuario integer,
  id_pessoa integer,
  id_modelo_carteirinha integer,
  CONSTRAINT soc_autoriza_impressao_cartao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_autoriza_impressao_cartao_id_historico_carteirinha FOREIGN KEY (id_historico_carteirinha)
      REFERENCES soc_historico_carteirinha (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_autoriza_impressao_cartao_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_autoriza_impressao_cartao_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_autoriza_impressao_cartao_id_modelo_carteirinha FOREIGN KEY (id_modelo_carteirinha)
      REFERENCES soc_modelo_carteirinha (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_autoriza_impressao_cartao
  OWNER TO postgres;
  
  
  
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 250, 'AUTORIZAR CARTEIRINHA', '"/Sindical/autorizaCarteirinha.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 250);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;



-- Function: func_responsavel(integer, boolean)

-- DROP FUNCTION func_responsavel(integer, boolean);

CREATE OR REPLACE FUNCTION func_responsavel(pessoa integer, descontofolha boolean)
  RETURNS integer AS
$BODY$
     declare titular int;
     declare responsavel int;
     declare idade int;
BEGIN

     idade = (select func_idade(dt_nascimento,current_date) from pes_fisica where id_pessoa=pessoa);

---  Responsável default:

---  Sócio e desconto sem folha  falso: Pegar o titular
     titular =  (select s.titular from soc_socios_vw as s where s.inativacao is null and s.codsocio=pessoa);
     if (titular is not null and descontofolha=false) then
        responsavel = titular;
     end if;

---  Sócio com  desconto em  folha  verdadeiro: Pegar o a Empresa do Titular.
       if (titular is not null and descontofolha=true) then
         responsavel = 
 	 (
 	    select j.id_pessoa  from pes_pessoa_vw as p
	    inner join pes_juridica as j  on  j.id=p.e_id
	    where p.codigo=titular and demissao is null
	 );
	 if (responsavel is null) then
            responsavel=titular;
         end if;
     end if;

---- Não Sócio Desconto em Folha
     if (titular is null and idade >= 18 and descontofolha=true) then
          responsavel = 
 	 (
 	    select j.id_pessoa  from pes_pessoa_vw as p
	    inner join pes_juridica as j  on  j.id=p.e_id
	    where p.codigo=pessoa and demissao is null
	 );
	 if (responsavel is null) then
            responsavel=0;
         end if;
     end if;
---  Não Sócio Maior Sem Desconto em Folha.
     if (titular is null and idade >= 18 and descontofolha=false) then
          responsavel = pessoa;
     end if;
---  Não Sócio menor: exigir uma PF maior de idade ou uma PJ
     if (titular is null and idade < 18) then
        responsavel=0;
     end if;

   if (responsavel is null) then responsavel=0; end if;   
   RETURN responsavel;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_responsavel(integer, boolean)
  OWNER TO postgres;
  
  
  

-- APENAS FAZER EM CLIENTES COM ESSA TABELA VAZIA.
-- ALTER TABLE soc_carteirinha DROP COLUMN id_socio;

ALTER TABLE soc_carteirinha ADD COLUMN id_pessoa integer;
ALTER TABLE soc_carteirinha
  ADD CONSTRAINT fk_soc_carteirinha_id_pessoa FOREIGN KEY (id_pessoa)
      REFERENCES pes_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_soc_carteirinha_id_pessoa
  ON soc_carteirinha(id_pessoa);


ALTER TABLE soc_carteirinha ADD COLUMN id_modelo_carteirinha integer;
ALTER TABLE soc_carteirinha
  ADD CONSTRAINT fk_soc_carteirinha_id_modelo_carteirinha FOREIGN KEY (id_modelo_carteirinha) REFERENCES soc_modelo_carteirinha (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_soc_carteirinha_id_modelo_carteirinha
  ON soc_carteirinha(id_modelo_carteirinha);

ALTER TABLE soc_carteirinha ADD COLUMN nr_cartao integer;
ALTER TABLE soc_carteirinha ADD COLUMN nr_via integer;
ALTER TABLE soc_carteirinha ADD COLUMN dt_validade_carteirinha date;


ALTER TABLE soc_historico_carteirinha DROP COLUMN id_socio;
ALTER TABLE soc_historico_carteirinha ADD COLUMN id_carteirinha integer;

ALTER TABLE soc_historico_carteirinha
  ADD CONSTRAINT fk_soc_historico_carteirinha_id_carteirinha FOREIGN KEY (id_carteirinha) REFERENCES soc_carteirinha (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_soc_historico_carteirinha_id_carteirinha
  ON soc_historico_carteirinha(id_carteirinha);

ALTER TABLE soc_historico_carteirinha ADD COLUMN id_movimento integer;

ALTER TABLE soc_historico_carteirinha
  ADD CONSTRAINT fk_soc_historico_carteirinha_id_movimento FOREIGN KEY (id_movimento) REFERENCES fin_movimento (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE INDEX fki_soc_historico_carteirinha_id_movimento
  ON soc_historico_carteirinha(id_movimento);


INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 251, 'MODELO CARTEIRINHA', '"/Sindical/modeloCarteirinha.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 251);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;

  
  
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
  
  
  -- Column: id_modelo_carteirinha

-- ALTER TABLE soc_autoriza_impressao_cartao DROP COLUMN id_modelo_carteirinha;

ALTER TABLE soc_autoriza_impressao_cartao ADD COLUMN id_modelo_carteirinha integer;

  
-- Column: is_foto_cartao

-- ALTER TABLE seg_registro DROP COLUMN is_foto_cartao;

ALTER TABLE seg_registro ADD COLUMN is_foto_cartao BOOLEAN DEFAULT FALSE;
UPDATE seg_registro SET is_foto_cartao = false;

  
-- Column: is_foto

-- ALTER TABLE soc_autoriza_impressao_cartao DROP COLUMN is_foto;

ALTER TABLE soc_autoriza_impressao_cartao ADD COLUMN is_foto boolean;
UPDATE soc_autoriza_impressao_cartao SET is_foto = false;