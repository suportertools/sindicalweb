
-- Function: func_nullstring(character varying)

-- DROP FUNCTION func_nullstring(character varying);

CREATE OR REPLACE FUNCTION func_nullstring(n character varying)
  RETURNS character varying AS
$BODY$
DECLARE nun varchar(8000) := '';
BEGIN
      if (n is not null) then nun=n; end if;
      RETURN nun;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_nullstring(character varying)
  OWNER TO postgres;
