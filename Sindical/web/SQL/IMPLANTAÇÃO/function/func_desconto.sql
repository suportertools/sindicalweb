-- Function: func_desconto(integer, double precision, double precision)

-- DROP FUNCTION func_desconto(integer, double precision, double precision);

CREATE OR REPLACE FUNCTION func_desconto(id_movimento integer, desconto double precision, totalacrescimo double precision)
  RETURNS double precision AS
$BODY$
----- id do movimento, total de desconto, total do acrescimo (vl.calculado 1041.76 - total do acrescimo=41.76
declare wdesconto  float := desconto;
declare wtotal     float := totalAcrescimo;
declare wdesc      float := 0;

declare wvalor       float := func_valor_folha(id_movimento)+func_multa(id_movimento)+func_juros(id_movimento)+func_correcao(id_movimento);
declare wpacrescimo  float := func_multa(id_movimento)+func_juros(id_movimento)+func_correcao(id_movimento)/func_valor_folha(id_movimento)*100;
declare wacrescimo   float := func_multa(id_movimento)+func_juros(id_movimento)+func_correcao(id_movimento);

declare wservico   int   := (select id_servicos from fin_movimento where id=id_movimento);

begin  
   if wservico <> 1 and wdesconto > 0 and wtotal > 0 then
      wdesc := (wdesconto/wtotal)*100;
      wdesc := (wacrescimo*wdesc)/100;
   end if;
   return wdesc;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_desconto(integer, double precision, double precision)
  OWNER TO postgres;

