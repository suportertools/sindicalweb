
-- Function: func_nullinteger(integer)

-- DROP FUNCTION func_nullinteger(integer);

CREATE OR REPLACE FUNCTION func_nullinteger(n integer)
  RETURNS integer AS
$BODY$
DECLARE nun int := 0;
BEGIN
      if (n is not null) then nun=n; end if;
      RETURN nun;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_nullinteger(integer)
  OWNER TO postgres;
