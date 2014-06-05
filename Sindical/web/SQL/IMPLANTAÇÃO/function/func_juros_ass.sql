
-- Function: func_juros_ass(integer)

-- DROP FUNCTION func_juros_ass(integer);

CREATE OR REPLACE FUNCTION func_juros_ass(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare qDias         int  :=0;
declare idMov         int  :=id_movimento;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare juros         float:=0;
declare jJurosDiario  float:=0;
 

declare idBaixa       int   := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int   := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date  := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float := (select nr_valor      from fin_movimento where id=idMov);
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);

 
BEGIN
  
   if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
   
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


        if (jPrimeiroMes is null) then jPrimeiroMes :=0; end if;
        if (jSegundoMes  is null) then jSegundoMes  :=0; end if;


	juros := juros + (jJurosDiario*qDias);
	juros := juros + ((jPrimeiroMes * valor)/100);
	juros := juros + (qMeses*((jSegundoMes  * valor)/100));
    end if;
    RETURN round(cast(juros as decimal), 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros_ass(integer)
  OWNER TO postgres;
