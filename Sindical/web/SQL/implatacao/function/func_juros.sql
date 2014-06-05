
-- Function: func_juros(integer)

DROP FUNCTION func_juros(integer);

CREATE OR REPLACE FUNCTION func_juros(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare qDias         int  :=0;
declare idMov         int  :=id_movimento;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare valor         float:=func_valor_folha(idMov);
declare juros         float:=0;
declare jJurosDiario  float:=0;
--declare vencto        date:=select dt_vencimento from fin_movimento where id=idMov;


declare idTipoServico int   := (select id_tipo_servico from fin_movimento where id=idMov );
declare idservico     int   := (select id_servicos from fin_movimento where id=idMov );
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare idpessoa      int   :=(select id_pessoa from fin_movimento where id=idMov );



declare vencto      date  := 
     (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );
BEGIN



     if (idTipoServico =4 ) then --- se for acordo pegar vencto e valor do movimento.
	valor  := (select nr_valor      from fin_movimento where id=idMov);
	vencto := (select dt_vencimento from fin_movimento where id=idMov);
     end if;

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
    RETURN round(cast(juros as decimal), 2);
    --RETURN qDias ;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros(integer)
  OWNER TO postgres;
