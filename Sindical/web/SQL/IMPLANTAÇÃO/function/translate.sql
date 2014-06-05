
-- Function: translate(character varying)

DROP FUNCTION translate(character varying);

CREATE OR REPLACE FUNCTION translate(character varying)
  RETURNS character varying AS
$BODY$

SELECT * from TRANSLATE($1, 
'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ','aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC'); 


$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION translate(character varying)
  OWNER TO postgres;
