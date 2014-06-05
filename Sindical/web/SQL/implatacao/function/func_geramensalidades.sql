
-- Function: func_geramensalidades(integer, character varying)

DROP FUNCTION func_geramensalidades(integer, character varying);

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

	insert into soc_lote_boleto (dt_processamento) (select current_date);
	wlote := (select max(id) from soc_lote_boleto);
 
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
