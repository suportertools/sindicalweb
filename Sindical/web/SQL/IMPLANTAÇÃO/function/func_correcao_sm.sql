-- Function: func_correcao_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_correcao_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_correcao_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare idTipoServico int        :=idtiposervico;
declare ref           varchar(7) :=ref;

declare indice      int:=
	                (
	                select cr.id_indice from  fin_correcao                  as cr where cr.id_servicos=idServico and 
			(substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
  			(substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		   	 
		   	 );
declare vlIndice    float := 0;
declare valor       float := func_valor_folha_sm(idpessoa,idservico,idtiposervico,ref);
declare valorBase   float := valor;
declare vencto      date  := 
     (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

DECLARE lista CURSOR FOR 
(
SELECT nr_valor FROM fin_indice_mensal 
where 
id_indice=indice and 
(
text(nr_ano)||substring('0'||text(nr_mes),length('0'||text(nr_mes))-1,length('0'||text(nr_mes)))
)
>=
(
text(extract('year' from  vencto))||substring('0'||text(extract('month' from  vencto)),length('0'||text(extract('month' from  vencto)))-1,length('0'||text(extract('month' from  vencto))))
)
order by nr_ano,nr_mes
);
begin  
   open lista;
   if (CURRENT_DATE>vencto) then   --- se data vencida
	-- Para ir para o primeiro registo:
	FETCH FIRST FROM lista into vlIndice;
	loop
	        if (vlIndice is null) then vlIndice=0; end if;
 	        valor := valor + ((valor * vlIndice)/100);
		FETCH NEXT FROM lista into vlIndice;
		EXIT WHEN NOT FOUND;
	end loop;
   end if;------ se data vencida
   close lista;
    RETURN round(cast( (valor-valorBase)*100 as decimal) / 100, 2);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_correcao_sm(integer, integer, integer, character varying)
  OWNER TO postgres;

