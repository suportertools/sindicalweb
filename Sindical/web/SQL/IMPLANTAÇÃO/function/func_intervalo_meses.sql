
-- Function: func_intervalo_meses(date, date)

-- DROP FUNCTION func_intervalo_meses(date, date);

CREATE OR REPLACE FUNCTION func_intervalo_meses(hoje date, vencimento date)
  RETURNS integer AS
$BODY$
DECLARE H  date := hoje;
DECLARE V  date := vencimento;
DECLARE C  date := vencimento;
DECLARE QM int  := 0;
DECLARE M  int  :=EXTRACT(MONTH FROM C);

BEGIN
       if (H > V) then
          while (H >= V) loop
             V = V + 31;
             QM = QM + 1;   
          end loop;
       end if;
/*
       if (V < H) then -- se estiver vencido contar um mês
          QM := QM+1;
       end if;
       while H >= C LOOP --- enquanto hoje(15+40=25/12) > C (15 + 31  = 16/12)
          if (EXTRACT(MONTH FROM C)<>M) then
                QM := QM+1;
                M  :=EXTRACT(MONTH FROM C);
           end if;
           C  := C+1; 
       END LOOP;
   end if;
*/   
   RETURN QM;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_intervalo_meses(date, date)
  OWNER TO postgres;
