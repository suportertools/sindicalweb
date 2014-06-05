-- Function: func_qtde_empresas(integer)

-- DROP FUNCTION func_qtde_empresas(integer);

CREATE OR REPLACE FUNCTION func_qtde_empresas(id_esc integer)
  RETURNS integer AS
$BODY$
DECLARE quantidade integer := 0;
BEGIN
     quantidade := 
	(
	select count(*) from arr_contribuintes_vw as c
  	 inner join pes_juridica as j on j.id=c.id_juridica
	 where j.id_contabilidade=id_esc
	 );
      RETURN quantidade;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_qtde_empresas(integer)
  OWNER TO postgres;

