
-- Function: atualiza_controle()

-- DROP FUNCTION atualiza_controle();

CREATE OR REPLACE FUNCTION atualiza_controle()
  RETURNS trigger AS
$BODY$begin
     if new.nr_ctr_boleto = -1 then   
        new.nr_ctr_boleto = new.id;
    end if;
     return new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION atualiza_controle()
  OWNER TO postgres;
