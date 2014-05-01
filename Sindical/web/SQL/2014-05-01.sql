INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 231, 'LANÇAMENTO FINANCEIRO', '"/Sindical/lancamentoFinanceiro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 231);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;


DROP TABLE fin_centro_custo;

CREATE TABLE fin_centro_custo_contabil
(
  id serial NOT NULL,
  nr_codigo integer,
  ds_descricao character varying(255),
  CONSTRAINT fin_centro_custo_contabil_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_centro_custo_contabil
  OWNER TO postgres;

  
CREATE TABLE fin_centro_custo
(
  id serial NOT NULL,
  ds_descricao character varying(255),
  id_filial integer,
  id_centro_custo_contabil integer,
  CONSTRAINT fin_centro_custo_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_centro_custo_id_centro_custo_contabil FOREIGN KEY (id_centro_custo_contabil)
      REFERENCES fin_centro_custo_contabil (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_centro_custo_id_filial FOREIGN KEY (id_filial)
      REFERENCES pes_filial (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_centro_custo
  OWNER TO postgres;
  
  
CREATE TABLE fin_centro_custo_contabil_sub
(
  id serial NOT NULL,
  ds_descricao character varying(255),
  ds_es character varying(255),
  nr_codigo integer,
  id_centro_custo_contabil integer,
  CONSTRAINT fin_centro_custo_contabil_sub_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_centro_custo_contabil_sub_id_centro_custo_contabil FOREIGN KEY (id_centro_custo_contabil)
      REFERENCES fin_centro_custo_contabil (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_centro_custo_contabil_sub
  OWNER TO postgres;
  

CREATE TABLE fin_conta_operacao
(
  id serial NOT NULL,
  ds_es character varying(255),
  id_plano5 integer,
  id_centro_custo_contabil_sub integer,
  id_operacao integer,
  CONSTRAINT fin_conta_operacao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_conta_operacao_id_centro_custo_contabil_sub FOREIGN KEY (id_centro_custo_contabil_sub)
      REFERENCES fin_centro_custo_contabil_sub (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_conta_operacao_id_operacao FOREIGN KEY (id_operacao)
      REFERENCES fin_operacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_conta_operacao_id_plano5 FOREIGN KEY (id_plano5)
      REFERENCES fin_plano5 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_conta_operacao
  OWNER TO postgres;
  
ALTER TABLE fin_conta_operacao ADD COLUMN is_conta_fixa boolean;
-- UPDATE fin_conta_operacao SET is_conta_fixa = false WHERE is_conta_fixa is null;



CREATE TABLE fin_conta_tipo
(
  id serial NOT NULL,
  ds_descricao character varying(50),
  CONSTRAINT fin_conta_tipo_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_conta_tipo
  OWNER TO postgres;

INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 1, 'IMPOSTO' WHERE NOT EXISTS (SELECT id FROM fin_conta_tipo WHERE id = 1);
INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 2, 'ESTOQUE' WHERE NOT EXISTS (SELECT id FROM fin_conta_tipo WHERE id = 2);
INSERT INTO fin_conta_tipo (id, ds_descricao) SELECT 3, 'PATRIMONIO' WHERE NOT EXISTS (SELECT id FROM fin_conta_tipo WHERE id = 3);
SELECT setval('fin_conta_tipo_id_seq', max(id)) FROM fin_conta_tipo;



CREATE TABLE fin_conta_tipo_plano5
(
  id serial NOT NULL,
  id_plano5 integer,
  id_conta_tipo integer,
  CONSTRAINT fin_conta_tipo_plano5_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_conta_tipo_plano5_id_conta_status FOREIGN KEY (id_conta_tipo)
      REFERENCES fin_conta_tipo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_conta_tipo_plano5_id_plano5 FOREIGN KEY (id_plano5)
      REFERENCES fin_plano5 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_conta_tipo_plano5
  OWNER TO postgres;

  
  
CREATE TABLE fin_filtro_lancamento
(
  id serial NOT NULL,
  id_tipo_centro_custo integer,
  id_centro_custo integer,
  id_usuario integer,
  id_conta_operacao integer,
  id_lote integer,
  id_operacao integer,
  CONSTRAINT fin_filtro_lancamento_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_filtro_lancamento_id_centro_custo FOREIGN KEY (id_centro_custo)
      REFERENCES fin_centro_custo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_filtro_lancamento_id_conta_operacao FOREIGN KEY (id_conta_operacao)
      REFERENCES fin_conta_operacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_filtro_lancamento_id_lote FOREIGN KEY (id_lote)
      REFERENCES fin_lote (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_filtro_lancamento_id_operacao FOREIGN KEY (id_operacao)
      REFERENCES fin_operacao (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_filtro_lancamento_id_tipo_centro_custo FOREIGN KEY (id_tipo_centro_custo)
      REFERENCES fin_centro_custo_contabil_sub (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_filtro_lancamento_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_filtro_lancamento
  OWNER TO postgres;

  
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 4, 81, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 4);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 5, 85, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 5);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 6, 93, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 6);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 7, 95, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 7);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 8, 98, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 8);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 9, 113, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 9);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 10, 114, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 10);
INSERT INTO fin_conta_tipo_plano5 (id, id_plano5, id_conta_tipo) SELECT 11, 118, 2 WHERE NOT EXISTS ( SELECT id FROM fin_conta_tipo_plano5 WHERE id = 11);
SELECT setval('fin_conta_tipo_plano5_id_seq', max(id)) FROM fin_conta_tipo_plano5;

  
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 24, 'Cupom Fiscal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 24);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 25, 'Guia' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 25);
SELECT setval('fin_tipo_documento_id_seq', max(id)) FROM fin_tipo_documento;
  
ALTER TABLE fin_cheque_pag ADD COLUMN dt_cancelamento date;  
  
ALTER TABLE fin_plano ALTER COLUMN ds_acesso TYPE varchar(10);
ALTER TABLE fin_plano2 ALTER COLUMN ds_acesso TYPE varchar(10);
ALTER TABLE fin_plano3 ALTER COLUMN ds_acesso TYPE varchar(10);
ALTER TABLE fin_plano4 ALTER COLUMN ds_acesso TYPE varchar(10);
ALTER TABLE fin_plano5 ALTER COLUMN ds_acesso TYPE varchar(10);

ALTER TABLE fin_plano ADD ds_classificador varchar(20);
ALTER TABLE fin_plano2 ADD ds_classificador varchar(20);
ALTER TABLE fin_plano3 ADD ds_classificador varchar(20);
ALTER TABLE fin_plano4 ADD ds_classificador varchar(20);
ALTER TABLE fin_plano5 ADD ds_classificador varchar(20);  
  
ALTER TABLE fin_lote ADD COLUMN nr_desconto double precision;  
  
UPDATE fin_lote SET nr_desconto = 0;
  
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 242, 'RELATÓRIO CHEQUES RECEBIDOS', '"/Sindical/relatorioChequesRecebidos.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 242);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 243, 'GRUPO FINANCEIRO', '"/Sindical/grupoFinanceiro.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 243);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 244, 'GERAR BOLETO', '"/Sindical/gerarBoleto.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 244);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;
  
ALTER TABLE fin_servicos ADD COLUMN id_subgrupo integer;  
ALTER TABLE fin_servicos ADD CONSTRAINT fk_fin_servicos_id_subgrupo FOREIGN KEY (id_subgrupo) REFERENCES fin_subgrupo (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
  
  
ALTER TABLE fin_movimento ALTER COLUMN nr_ctr_boleto TYPE character varying(30);
ALTER TABLE fin_boleto ALTER COLUMN nr_ctr_boleto TYPE character varying(30);
  
  
-- Table: soc_lote_geracao

-- DROP TABLE soc_lote_geracao;

CREATE TABLE soc_lote_geracao
(
  id serial NOT NULL,
  ds_vencimento character varying(7),
  id_servico_pessoa integer,
  dt_lancamento date,
  CONSTRAINT soc_lote_geracao_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_lote_geracao_id_servico_pessoa FOREIGN KEY (id_servico_pessoa)
      REFERENCES fin_servico_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_socios_id_servico_pessoa FOREIGN KEY (id_servico_pessoa)
      REFERENCES fin_servico_pessoa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_lote_geracao
  OWNER TO postgres;

SELECT setval('public.soc_lote_geracao_id_seq', 600000000, true);


CREATE TABLE soc_lote_boleto
(
id serial NOT NULL,
dt_processamento date,
CONSTRAINT soc_geracao_lote_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE soc_lote_boleto
OWNER TO postgres;
COMMENT ON TABLE soc_lote_boleto
IS 'Lote para agrupar os registros em um boleto específico.';  
  
  
CREATE TABLE soc_cobranca
(
id serial NOT NULL,
ds_descricao character varying(50),
ds_informativo character varying(2000),
ds_local_pagamento character varying(2000),
CONSTRAINT soc_cobranca_pkey PRIMARY KEY (id)
)
WITH (
OIDS=FALSE
);
ALTER TABLE soc_cobranca
OWNER TO postgres;
  
  
  
CREATE OR REPLACE VIEW pes_juridica_vw AS 
SELECT p.id AS id_pessoa, p.dt_criacao AS cadastro, p.ds_site AS jursite, 
p.ds_nome AS jurnome, p.ds_documento AS jurdocumento, 
p.ds_telefone1 AS jurtelefone, l.ds_descricao AS jurlogradouro, 
de.ds_descricao AS jurendereco, pend.ds_numero AS jurnumero, 
pend.ds_complemento AS jurcomplemento, b.ds_descricao AS jurbairro, 
c.ds_cidade AS jurcidade, c.ds_uf AS juruf, ende.ds_cep AS jurcep, 
pesc.ds_nome AS escnome, pesc.ds_telefone1 AS esctelefone, 
pesc.ds_email1 AS escemail, l_es.ds_descricao AS esclogradouro, 
de_es.ds_descricao AS escendereco, pend_es.ds_numero AS escnumero, 
pend_es.ds_complemento AS esccomplemento, b_es.ds_descricao AS escbairro, 
c_es.ds_cidade AS esccidade, c_es.ds_uf AS escuf, ende_es.ds_cep AS esccep, 
esc.id AS escid, c.id AS jur_idcidade, pj.id AS jurid, 
p.ds_email1 AS juremail1, p.ds_email2 AS juremail2, 
p.ds_email3 AS juremail3
FROM pes_pessoa p
JOIN pes_juridica pj ON pj.id_pessoa = p.id
LEFT JOIN pes_juridica esc ON esc.id = pj.id_contabilidade
LEFT JOIN pes_pessoa pesc ON pesc.id = esc.id_pessoa
LEFT JOIN pes_pessoa_endereco pend ON pend.id_pessoa = p.id AND pend.id_tipo_endereco = 2
LEFT JOIN end_endereco ende ON ende.id = pend.id_endereco
LEFT JOIN end_logradouro l ON l.id = ende.id_logradouro
LEFT JOIN end_descricao_endereco de ON de.id = ende.id_descricao_endereco
LEFT JOIN end_bairro b ON b.id = ende.id_bairro
LEFT JOIN end_cidade c ON c.id = ende.id_cidade
LEFT JOIN pes_pessoa_endereco pend_es ON pend_es.id_pessoa = esc.id_pessoa AND pend_es.id_tipo_endereco = 2
LEFT JOIN end_endereco ende_es ON ende_es.id = pend_es.id_endereco
LEFT JOIN end_logradouro l_es ON l_es.id = ende_es.id_logradouro
LEFT JOIN end_descricao_endereco de_es ON de_es.id = ende_es.id_descricao_endereco
LEFT JOIN end_bairro b_es ON b_es.id = ende_es.id_bairro
LEFT JOIN end_cidade c_es ON c_es.id = ende_es.id_cidade;

ALTER TABLE pes_juridica_vw
OWNER TO postgres;
  
  
ALTER TABLE fin_servico_pessoa ADD id_tipo_cobranca int; --- vincular com soc_cobranca

UPDATE fin_servico_pessoa SET id_tipo_cobranca = 1;

INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 245, 'IMPRESSÃO DE BOLETO SOCIAL', '"/Sindical/impressaoBoletoSocial.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 245);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;
  
  
  
CREATE OR REPLACE FUNCTION func_geramensalidades(pessoa integer, mesano character varying)
RETURNS integer AS
$BODY$
declare wlote int :=0;
declare wlote_geracao int :=0;
BEGIN
/*

select func_geramensalidades(null, mesano)
select func_geramensalidades(null, '05/2015')

*/


wlote_geracao = (select max(id) from soc_lote_geracao);
----------------------------------------------------------------------------------------------------------------------------------------

insert into soc_lote_geracao (ds_vencimento , id_servico_pessoa, dt_lancamento)
(
select
mesano,
sp.id,
current_date
from 
fin_servico_pessoa as sp
inner join fin_servicos se on se.id=sp.id_servico
where 
sp.is_ativo=true 
---- se está dentro da vigoração >= mes/ano vecto (mesano = mes/ano parâmetro da tela de chamada)
and (substring(mesano,4,4)||substring(mesano,1,2))>=(substring(sp.ds_ref_vigoracao,4,4)||substring(sp.ds_ref_vigoracao,1,2)) 
---- se está dentro da validade 
and 
(
sp.ds_ref_validade='' or (substring(sp.ds_ref_validade,4,4)||substring(sp.ds_ref_validade,1,2)) >= (substring(mesano,4,4)||substring(mesano,1,2))
)
------------------------ Verifica se existe no movimento --------------------------------------------------------------------------------------------------------------------------------------
and sp.id_pessoa||'S'||sp.id_servico not in (select id_beneficiario||'S'||id_servicos from fin_movimento where CAST(substring(mesano,1,2) AS int)=extract(month from dt_vencimento) and CAST(substring(mesano,4,4) AS int)=extract(year from dt_vencimento))
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
------------------------ Verifica se existe no soc_lote_geracao
and sp.id||'REF'||mesano not in (select id_servico_pessoa||'REF'||ds_vencimento FROM soc_lote_geracao)
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
and (sp.id_pessoa=pessoa or pessoa is null)
order by sp.id
);

----------------------------------------------------------------------------------------------------------------------------------------

insert into fin_lote
(
id,
dt_emissao,
is_avencer_contabil,
ds_pag_rec,
ds_documento,
nr_valor,
dt_lancamento,
ds_historico,
id_filial,
id_evt,
id_pessoa, 
id_tipo_documento,
id_rotina, 
id_status,
id_pessoa_sem_cadastro,
id_departamento,
id_condicao_pagamento,
id_plano_5,
is_desconto_folha,
id_matricula_socios
)

(
select
g.id, 
current_date as dt_emissao, 
false as is_avencer_contabil,
'R' as ds_pag_rec, 
null as ds_documento,------------------------------???
func_valor_servico(sp.id_pessoa,sp.id_servico, to_date(substring('0'||(pc.nr_dia_vencimento),length('0'||(pc.nr_dia_vencimento))-1,2)||'/'||mesano,'dd/mm/yyyy'), 0) as valor,
current_date as dt_lancamento,
null as ds_historico, 
se.id_filial, 
null as id_evt,
sp.id_pessoa, 
sp.id_tipo_documento, 
118 as id_rotina, 
1 as id_status,
null as id_pessoa_sem_cadastro,
se.id_departamento,
2 as id_condicao_pagamento,
se.id_plano5,
sp.desconto_folha,
ms.id as id_matricula_socios 
----is_banco true/false ver se gera boleto ou não
---select id_servico_pessoa from soc_socios group by id_servico_pessoa having count(*) > 1
from 
fin_servico_pessoa as sp
inner join pes_pessoa_complemento as pc on pc.id_pessoa=sp.id_cobranca
inner join fin_servicos se on se.id=sp.id_servico
inner join soc_lote_geracao as g on g.id_servico_pessoa=sp.id and g.ds_vencimento=mesano
left join soc_socios as s on s.id_servico_pessoa=sp.id
left join matr_socios as ms on ms.id=s.id_matricula_socios and ms.dt_inativo is null
where 
sp.is_ativo=true 
---- se está dentro da vigoração >= mes/ano vecto (mesano = mes/ano parâmetro da tela de chamada)
and (substring(mesano,4,4)||substring(mesano,1,2))>=(substring(sp.ds_ref_vigoracao,4,4)||substring(sp.ds_ref_vigoracao,1,2)) 
---- se está dentro da validade 
and 
(
sp.ds_ref_validade='' or (substring(sp.ds_ref_validade,4,4)||substring(sp.ds_ref_validade,1,2)) >= (substring(mesano,4,4)||substring(mesano,1,2))
)
------------------------ Verifica se existe no movimento --------------------------------------------------------------------------------------------------------------------------------------
and sp.id_pessoa||'S'||sp.id_servico not in (select id_beneficiario||'S'||id_servicos from fin_movimento where CAST(substring(mesano,1,2) AS int)=extract(month from dt_vencimento) and CAST(substring(mesano,4,4) AS int)=extract(year from dt_vencimento))
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
and (sp.id_pessoa=pessoa or pessoa is null)
order by sp.id

);

--------------------------------------------------------------------------------------------------------------------------------------------------

insert into fin_movimento
(
nr_juros,
nr_multa,
nr_acordado,
nr_desconto,
dt_vencimento,
nr_taxa,
is_ativo,
nr_valor_baixa,
is_obrigacao,
nr_repasse_automatico,
nr_ctr_boleto,
ds_referencia,
nr_quantidade,
nr_correcao,
ds_es,
nr_desconto_ate_vencimento,
ds_documento,
nr_valor,
dt_vencimento_original,
id_plano5,
id_servicos,
id_pessoa, 
id_baixa,
id_lote,
id_acordo,
id_tipo_servico,
id_beneficiario,
id_tipo_documento
)

(
select 
---------------------------------------------------------------
0 as nr_juros,
0 as nr_multa,
null as nr_acordado,
0 as nr_desconto,
to_date(substring('0'||(pc.nr_dia_vencimento),length('0'||(pc.nr_dia_vencimento))-1,2)||'/'||mesano,'dd/mm/yyyy') as dt_vencimento, 
0 as nr_taxa,
true as is_ativo,
0 as nr_valor_baixa,
false as is_obrigacao,
0 as nr_repasse_automatico,
'' as nr_ctr_boleto, ------------------------------------------------------------------------------------------ ????
--------------referencia------------------------------
substring(
'0'||extract(month from to_date('01/'||mesano,'dd/mm/yyyy')-1),
length('0'||extract(month from to_date('01/'||mesano,'dd/mm/yyyy')-1))-1,
length('0'||extract(month from to_date('01/'||mesano,'dd/mm/yyyy')-1)) 
)||'/'||extract(year from to_date('01/'||mesano,'dd/mm/yyyy')-1)
as ds_referencia, 
--------------------------------------------
1 as nr_quantidade,
0 as nr_correcao,
'E' as ds_es,
func_valor_servico(sp.id_pessoa,sp.id_servico,
to_date(substring('0'||(pc.nr_dia_vencimento),length('0'||(pc.nr_dia_vencimento))-1,2)||'/'||mesano,'dd/mm/yyyy'), 1) as nr_desconto_ate_vencimento, 
'' as ds_documento,---------------------------------------- ????
func_valor_servico(sp.id_pessoa,sp.id_servico, 
to_date(substring('0'||(pc.nr_dia_vencimento),length('0'||(pc.nr_dia_vencimento))-1,2)||'/'||mesano,'dd/mm/yyyy'), 0) as nr_valor, 
to_date(substring('0'||(pc.nr_dia_vencimento),length('0'||(pc.nr_dia_vencimento))-1,2)||'/'||mesano,'dd/mm/yyyy') as dt_vencimento_original, 
se.id_plano5,
sp.id_servico,
sp.id_cobranca as id_pessoa,
null as id_baixa,
g.id as id_lote, 
null as id_acordo,
1 as id_tipo_servico,
sp.id_pessoa as id_beneficiario,
2 as id_tipo_documento -------------------------------------------------- 
-------------------------------------------------------------------------
from 
fin_servico_pessoa as sp
inner join fin_servicos se on se.id=sp.id_servico
inner join pes_pessoa_complemento as pc on pc.id_pessoa=sp.id_cobranca
inner join soc_lote_geracao as g on g.id_servico_pessoa=sp.id and g.ds_vencimento=mesano
where 
sp.is_ativo=true 
---- se está dentro da vigoração >= mes/ano vecto (mesano = mes/ano parâmetro da tela de chamada)
and (substring(mesano,4,4)||substring(mesano,1,2))>=(substring(sp.ds_ref_vigoracao,4,4)||substring(sp.ds_ref_vigoracao,1,2)) 
---- se está dentro da validade 
and 
(
sp.ds_ref_validade='' or (substring(sp.ds_ref_validade,4,4)||substring(sp.ds_ref_validade,1,2)) >= (substring(mesano,4,4)||substring(mesano,1,2))
)
------------------------ Verifica se existe no movimento --------------------------------------------------------------------------------------------------------------------------------------
and sp.id_pessoa||'S'||sp.id_servico not in (select id_beneficiario||'S'||id_servicos from fin_movimento where CAST(substring(mesano,1,2) AS int)=extract(month from dt_vencimento) and CAST(substring(mesano,4,4) AS int)=extract(year from dt_vencimento))
-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
and (sp.id_pessoa=pessoa or pessoa is null)
order by sp.id
);


/*

select 
responsavel(8)
fator de vencimento(4)
conta_cobraça(3)
lote de geracao(5)
id_tipo_cobranca (soc_cobranca) (2)
*/

-------- Cria o lote de Geração dos boletos
/*
Agrupa todos os registros neste lote com este boleto
*/
if ((select max(id) from soc_lote_geracao) <> wlote_geracao) then
	insert into soc_lote_boleto (dt_processamento) (select current_date);
	wlote := (select max(id) from soc_lote_boleto);
end if;

-------->>>> Grava nr_ctr_boleto em fin_movimento
update fin_movimento 
set nr_ctr_boleto=
----Pessoa
right('00000000'||id_pessoa,8)||
----Fator de Vencimento
right('0000'||(select CAST(right('00'||nr_dia_vencimento,2)||'/'||mesano as date)-CAST('07/10/1997' as date) from pes_pessoa_complemento where id_pessoa=fin_movimento.id_pessoa),4)||
----Conta Cobrança
right('000'||(select id_conta_cobranca from fin_servico_conta_cobranca where id_servicos=fin_movimento.id_servicos and id_tipo_servico=fin_movimento.id_tipo_servico),3)||
----Lote de Geração
right('00000'||text(wlote),5)||
----id do soc_cobranca (extrato)
'01' 
where id_baixa is null and
extract(month from dt_vencimento)=cast(left(mesano,2)as int) and extract(year from dt_vencimento)=cast(right(mesano,4)as int) and
(nr_ctr_boleto='' or nr_ctr_boleto is null) and
id_servicos not in (select id_servicos from fin_servico_rotina where id_rotina=4);

-------->>>> Insere Boleto
insert into fin_boleto (nr_ctr_boleto,id_conta_cobranca,is_ativo) 
(
select nr_ctr_boleto,cast(substring(nr_ctr_boleto,13,3) as int),true from fin_movimento 
where length(nr_ctr_boleto)=22 and nr_ctr_boleto not in (select nr_ctr_boleto from fin_boleto where length(nr_ctr_boleto) = 22)
group by nr_ctr_boleto,cast(substring(nr_ctr_boleto,13,3) as int)
);
-----------------------------------------------------------------------------------------------------
update fin_movimento set ds_documento=fin_boleto.ds_boleto from fin_boleto where fin_movimento.nr_ctr_boleto=fin_boleto.nr_ctr_boleto and (ds_documento is null or ds_documento='') and length(fin_movimento.nr_ctr_boleto)=22;
-----------------------------------------------------------------------------------------------------

-- APAGA OS LOTE BOLETO GERADOS QUE NÃO TEM MOVIMENTO
delete from soc_lote_boleto where id not in (select cast(substring(nr_ctr_boleto,16,5) as int) from fin_movimento where length(nr_ctr_boleto) = 22);

RETURN 1;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;
ALTER FUNCTION func_geramensalidades(integer, character varying)
OWNER TO postgres;  
  
  
  
-- View: soc_boletos_vw

-- DROP VIEW soc_boletos_vw;

CREATE OR REPLACE VIEW soc_boletos_vw AS 
 SELECT l.id AS id_fin_lote, m.id AS id_fin_movimeto, m.nr_ctr_boleto, 
    sl.id AS id_lote_boleto, sl.dt_processamento AS processamento, 
    '' AS logo_banco, '' AS logo, '' AS logo_informativo, '' AS logo_verso, 
    pr.id AS codigo, pr.ds_nome AS responsavel, 
    '1997-10-07'::date + "substring"(m.nr_ctr_boleto::text, 9, 4)::integer AS vencimento, 
    mtr.nr_matricula, gc.ds_grupo_categoria AS grupo_categoria, 
    ct.ds_categoria AS categoria, se.ds_descricao AS servico, m.id_beneficiario, 
    pb.ds_nome AS nome_beneficiario, m.nr_valor AS valor, 
    0 AS mensalidades_corrigidas, 
    'NÃO RECEBER APÓS O VENCIMENTO.' AS mensagem_boleto, 
    bco.nr_num_banco AS banco, cb.ds_agencia AS agencia, 
    c.ds_cod_cedente AS cedente, b.ds_boleto AS boleto, f.juremail1 AS email, 
    f.jurnome AS nome_filial, f.jursite AS silte_filial, 
    f.jurdocumento AS cnpj_filial, f.jurtelefone AS tel_filial, 
    (((((f.jurlogradouro::text || ' '::text) || f.jurendereco::text) || ', '::text) || f.jurnumero::text) || ' '::text) || f.jurcomplemento::text AS endereco_filial, 
    f.jurbairro AS bairro_filial, f.jurcidade AS cidade_filial, 
    f.juruf AS uf_filial, 
    ("substring"(f.jurcep::text, 1, 5) || '-'::text) || "right"(f.jurcep::text, 3) AS cep_filial, 
    er.logradouro AS logradouro_responsavel, 
    rtrim((((er.endereco::text || ', '::text) || per.ds_numero::text) || ' '::text) || per.ds_complemento::text || er.bairro::text) AS endereco_responsavel, 
    ("left"(er.cep::text, 5) || '-'::text) || "right"(er.cep::text, 3) AS cep_responsavel, 
    er.uf AS uf_responsavel, er.cidade AS cidade_responsavel,
    co.ds_informativo as informativo,
    co.ds_local_pagamento as local_pagamento
   FROM fin_lote l
   JOIN fin_movimento m ON m.id_lote = l.id
   JOIN soc_lote_boleto sl ON sl.id = "substring"(m.nr_ctr_boleto::text, 16, 5)::integer AND length(m.nr_ctr_boleto::text) = 22
   JOIN pes_pessoa pr ON pr.id = m.id_pessoa
   JOIN pes_pessoa pb ON pb.id = m.id_beneficiario
   JOIN pes_juridica_vw f ON f.id_pessoa = 1
   JOIN fin_servicos se ON se.id = m.id_servicos
   JOIN fin_boleto b ON b.nr_ctr_boleto::text = m.nr_ctr_boleto::text
   JOIN fin_conta_cobranca c ON c.id = b.id_conta_cobranca
   JOIN fin_conta_banco cb ON cb.id = c.id_conta_banco
   JOIN fin_banco bco ON bco.id = cb.id_banco
   JOIN pes_pessoa_endereco per ON per.id_pessoa = pr.id AND per.id_tipo_endereco = 3
   JOIN endereco_vw er ON er.id = per.id_endereco
   JOIN soc_cobranca co ON co.id = 1
   LEFT JOIN matr_socios mtr ON mtr.id = l.id_matricula_socios
   LEFT JOIN soc_categoria ct ON ct.id = mtr.id_categoria
   LEFT JOIN soc_grupo_categoria gc ON gc.id = ct.id_grupo_categoria;

ALTER TABLE soc_boletos_vw
  OWNER TO postgres;
  
  
  
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 246, 'AGENDA GRUPO USUÁRIO', '"/Sindical/agendaGrupoUsuario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 246);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 247, 'PESQUISA CONVÊNIO MÉDICO', '"/Sindical/pesquisaConvenioMedico.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 247);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  
  





