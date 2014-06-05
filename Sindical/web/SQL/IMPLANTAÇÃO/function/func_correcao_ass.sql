
-- Function: func_correcao_ass(integer)

-- DROP FUNCTION func_correcao_ass(integer);

CREATE OR REPLACE FUNCTION func_correcao_ass(idmov integer)
  RETURNS double precision AS
$BODY$

declare indice      int:=
                 (
                 select cr.id_indice from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
     (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
       where m.id=idMov
       );

declare vlIndice      float := 0;
  
declare idBaixa       int   := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int   := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date  := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float := (select nr_valor      from fin_movimento where id=idMov);
declare valorBase     float := valor;
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);


DECLARE lista CURSOR FOR 
(
SELECT nr_valor FROM fin_indice_mensal 
where 
id_indice=indice and 
(
text(nr_ano)||right('0'||text(nr_mes),2)
>=----'201201'
(text(extract('year' from  vencto))||right('0'||text(extract('month' from  vencto)),2))
)
order by nr_ano,nr_mes
);
begin  


open lista;
   if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
          -- Para ir para o primeiro registo:
          FETCH FIRST FROM lista into vlIndice;
          loop
             if (vlIndice is null) then vlIndice:=0; end if;
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
ALTER FUNCTION func_correcao_ass(integer)
  OWNER TO postgres;
