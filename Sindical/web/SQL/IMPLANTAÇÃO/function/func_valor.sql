
-- Function: func_valor(integer)

-- DROP FUNCTION func_valor(integer);

CREATE OR REPLACE FUNCTION func_valor(mov integer)
  RETURNS double precision AS
$BODY$

    declare valor double precision;
    declare vencto date;
    
BEGIN
    vencto = (select dt_vencimento from fin_movimento where id=mov);
    valor  = (select nr_valor from fin_movimento where id=mov);
    if (vencto >= current_date) then
       valor  = (select nr_valor-nr_desconto_ate_vencimento from fin_movimento where id=mov);
    end if;
 
    RETURN valor;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor(integer)
  OWNER TO postgres;
