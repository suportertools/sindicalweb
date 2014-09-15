
-- POR: ROGÉRIO MORAES SARMENTO
-- 13/09/2014

-- Function: func_valor_servico(integer, integer, date, integer)

-- DROP FUNCTION func_valor_servico(integer, integer, date, integer);

CREATE OR REPLACE FUNCTION func_valor_servico(pessoa integer, servico integer, vencimento date, tipo integer)
  RETURNS double precision AS
$BODY$

    declare valor double precision;
BEGIN


/*
----RITMO - (GM)- 10311
-- Funcionario titular
select * from pes_pessoa where ds_nome='ROGERIO MORAES SARMENTO' -- 28502
select func_valor_servico(28502,10311,'13/09/2014',0) --- Valor (já calculado)
-- Funcionario Dependente
select * from pes_pessoa where ds_nome='LUCILENE MARGARETE VIEIRA MORAES SARMENTO' -- 123273
select func_valor_servico(123273,10311,'09/10/2013',0) --- Valor (já calculado)


select * from fin_servicos where ds_descricao='RITMO - (GM)'



  Tipo: 0=valor 
	1=desconto até o vencimento 
	2=taxa

  Ex:

                               pessoa, servico,   vencto    , tipo
---  não é sócio e empresa sem desconto (valor sem desconto)
select func_valor_servico(11815,104,'09/10/2013',0), --- Valor (já calculado)
       func_valor_servico(11815,104,'09/10/2013',1), --- Valor até o vencimento (já calculado)
       func_valor_servico(11815,104,'09/10/2013',2)  --- Taxa até o vencimento (já calculado)

---- não sócio empresa com desconto (convênio)
select func_valor_servico(11699,104,'09/10/2013',0),
       func_valor_servico(11699,104,'09/10/2013',1),
       func_valor_servico(11699,104,'09/10/2013',2)

---- sócio
select func_valor_servico(12859,104,'09/10/2013',0),
       func_valor_servico(12859,104,'09/10/2013',1),
       func_valor_servico(12859,104,'09/10/2013',2)


select id,nr_desconto,id_parentesco,id_categoria_desconto from soc_categoria_desconto_dependente  
select * from soc_socios_vw

				CASE WHEN a=1 THEN 'one'
					WHEN a=2 THEN 'two'
					ELSE 'other'
				END
*/

        if (tipo=0) then
		valor :=
		(
			select
 
				CASE WHEN cdd.id > 0 THEN (sv.nr_valor-(cdd.nr_desconto*sv.nr_valor/100))
 					ELSE (sv.nr_valor-(cd.nr_desconto*sv.nr_valor/100))
				END as valor
 			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			left join soc_categoria_desconto_dependente as cdd on cdd.id_categoria_desconto=cd.id and so.id_parentesco=cdd.id_parentesco
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa
		);
	
		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_valor-(de.nr_desconto*sv.nr_valor/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_valor  
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

        if (tipo=1) then
		valor :=
		(
 
			select
 
				CASE WHEN cdd.id > 0 THEN (sv.nr_desconto_ate_vencimento-(cdd.nr_desconto*sv.nr_desconto_ate_vencimento/100))
 					ELSE (sv.nr_desconto_ate_vencimento-(cd.nr_desconto*sv.nr_desconto_ate_vencimento/100))
				END as valor

			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			left join soc_categoria_desconto_dependente as cdd on cdd.id_categoria_desconto=cd.id and so.id_parentesco=cdd.id_parentesco
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa
		);
	
		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_desconto_ate_vencimento-(de.nr_desconto*sv.nr_desconto_ate_vencimento/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_desconto_ate_vencimento 
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

        if (tipo=2) then
		valor :=
		(
			select 
 
				CASE WHEN cdd.id > 0 THEN (sv.nr_taxa-(cdd.nr_desconto*sv.nr_taxa/100))
 					ELSE (sv.nr_taxa-(cd.nr_desconto*sv.nr_taxa/100))
				END as valor
 
 			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			left join soc_categoria_desconto_dependente as cdd on cdd.id_categoria_desconto=cd.id and so.id_parentesco=cdd.id_parentesco
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa

 
		);
	
		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_taxa-(de.nr_desconto*sv.nr_taxa/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_taxa  
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

	if (valor is null) then
		valor=0;
	end if;

        RETURN valor;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor_servico(integer, integer, date, integer)
  OWNER TO postgres;
