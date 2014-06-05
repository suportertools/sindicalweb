-- Function: concatenar(character varying, character varying)

-- DROP FUNCTION concatenar(character varying, character varying);

CREATE OR REPLACE FUNCTION concatenar(character varying, character varying)
  RETURNS character varying AS
$BODY$

SELECT $1 || $2;

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION concatenar(character varying, character varying)
  OWNER TO postgres;

