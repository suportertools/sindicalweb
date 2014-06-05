-- Function: func_juros_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_juros_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_juros_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare idTipoServico int        :=idtiposervico;
declare ref           varchar(7) :=ref;

declare qDias         int  :=0;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare valor         float:=func_valor_folha_sm(idpessoa,idservico,idtiposervico,ref);
declare juros         float:=0;
declare jJurosDiario  float:=0;
declare tipo          int  :=0;
declare vencto        date;
BEGIN
    vencto        := 
    (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

   
   if (CURRENT_DATE>vencto) then
        qMeses        := (select func_intervalo_meses(CURRENT_DATE,vencto));
        qDias         := (select CURRENT_DATE-vencto);

	jPrimeiroMes := (select cr.nr_juros_pri_mes from fin_correcao  as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);
			
	jSegundoMes := (select cr.nr_juros_apartir_2mes from  fin_correcao as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		       );

	jJurosDiario:=  (select cr.nr_juros_diarios from fin_correcao  as cr 
			 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);


	juros := juros + (jJurosDiario*qDias);
	juros := juros + ((jPrimeiroMes * valor)/100);
	juros := juros + (qMeses*((jSegundoMes  * valor)/100));
    end if;
    RETURN round(cast( juros as decimal ), 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros_sm(integer, integer, integer, character varying)
  OWNER TO postgres;

