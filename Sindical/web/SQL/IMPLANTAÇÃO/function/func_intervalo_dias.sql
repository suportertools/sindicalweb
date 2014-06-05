-- Function: func_intervalo_dias(date, date)

-- DROP FUNCTION func_intervalo_dias(date, date);

CREATE OR REPLACE FUNCTION func_intervalo_dias(vencimentoi date, vencimentof date)
  RETURNS integer AS
$BODY$
DECLARE dias int := 0;
DECLARE DI   varchar(20);
DECLARE DF   varchar(20);
BEGIN
    DI   := to_char(vencimentoI,'DD/MM/YYYY HH:MI:SS');
    DF   := to_char(vencimentoF,'DD/MM/YYYY HH:MI:SS');
    dias :=
----    (select DATE_PART('day',(to_timestamp('01/03/2013', 'DD/MM/YYYY HH:MI:SS') - to_timestamp('01/02/2012', 'DD/MM/YYYY HH:MI:SS'))));
        (select DATE_PART('day',(to_timestamp(DF, 'DD/MM/YYYY HH:MI:SS') - to_timestamp(DI, 'DD/MM/YYYY HH:MI:SS'))));
    if (dias < 0) then dias:=0; end if;
    RETURN dias;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_intervalo_dias(date, date)
  OWNER TO postgres;

