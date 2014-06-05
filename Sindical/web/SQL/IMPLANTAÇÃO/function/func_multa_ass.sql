
-- Function: func_multa_ass(integer)

-- DROP FUNCTION func_multa_ass(integer);

CREATE OR REPLACE FUNCTION func_multa_ass(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare idMov         int   :=id_movimento;
declare qMeses        int   :=0;
declare qDias         int   :=0;
declare mPrimeiroMes  float :=0;
declare mSegundoMes   float :=0;
declare multa         float :=0;
   
declare idBaixa       int        := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int        := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date       := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float      := (select nr_valor      from fin_movimento where id=idMov);
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);
BEGIN

    if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
       qDias         := (CURRENT_DATE-vencto);
       qMeses        := (func_intervalo_meses(CURRENT_DATE,vencto));
 
       mPrimeiroMes := (select cr.nr_multa_primeiro_mes from fin_movimento as m 
       left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
       (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
       (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
       where m.id=idMov);

       mSegundoMes := (select cr.nr_multa_apartir_2mes from fin_movimento as m 
       left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
       (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
       (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
        where m.id=idMov);

       if (mPrimeiroMes is null) then mPrimeiroMes :=0; end if;
       if (mSegundoMes  is null) then mSegundoMes  :=0; end if;

       multa := multa + ((mPrimeiroMes * valor)/100);
       multa := multa + (qMeses*((mSegundoMes  * valor)/100));
    end if;
   
    RETURN round(cast( multa as decimal) , 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_multa_ass(integer)
  OWNER TO postgres;
