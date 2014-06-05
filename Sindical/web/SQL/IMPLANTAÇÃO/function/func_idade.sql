-- Function: func_idade(date, date)

-- DROP FUNCTION func_idade(date, date);

CREATE OR REPLACE FUNCTION func_idade(inicio date, fim date)
  RETURNS integer AS
$BODY$
    declare idade int;
BEGIN
    idade = 0;
    if (inicio is not null and fim is not null and fim > inicio) then
                idade= extract(year from fim) -  extract(year from inicio);
                if (extract(month from inicio) > extract(month from fim)) then
                               idade = idade - 1;
                end if;
                if ((extract(month from inicio)) = (extract(month from fim))) then
                               if ((extract(day from inicio)) > (extract(day from fim))) then
                                               idade = idade - 1;
                               end if;
                end if;
    end if;
    RETURN idade;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_idade(date, date)
  OWNER TO postgres;

