-- Function: func_esc_turmas_vagas_disponiveis(integer)

-- DROP FUNCTION func_esc_turmas_vagas_disponiveis(integer);

CREATE OR REPLACE FUNCTION func_esc_turmas_vagas_disponiveis(turma integer)
  RETURNS integer AS
$BODY$

    declare cod_agrupamento int;
    declare agrupamento int;
    declare vagas_disponiveis int;
BEGIN
     vagas_disponiveis=0;
     vagas_disponiveis=(select nr_quantidade from esc_turma where id = turma);
     agrupamento = (select count(*) from esc_agrupa_turma where id_turma=turma);
------ Se não for agrupamento
     if (cod_agrupamento=0) then
       vagas_disponiveis=(vagas_disponiveis-(select count(*) from matr_turma where id_turma=turma));
      end if;

------ Se for agrupamento
     if (cod_agrupamento>0) then
             ---- codigo do agrupamento
            cod_agrupamento = (select id_turma_integral from esc_agrupa_turma where id_turma=3);
            cod_agrupamento = (select id_turma_integral from esc_agrupa_turma where id_turma=3);
            ---- se for integral
            if (cod_agrupamento=turma) then
           vagas_disponiveis=(vagas_disponiveis-	    
	      (
              select count(*) from esc_matr_turma as m
              inner join esc_agrupa_turma as a on a.id_turma_integral=m.id_turma
              where a.id_turma_integral=turma
 	      )
 	    );
            end if;
 

            ---- se NÃO for integral

           vagas_disponiveis=(vagas_disponiveis-	    
            (
              select count(*) from esc_matr_turma as m
              inner join esc_agrupa_turma as a on a.id_turma=m.id_turma
              where (a.id_turma=turma and a.id_turma_integral<>turma) or (a.id_turma=cod_agrupamento and a.id_turma_integral=cod_agrupamento)
            )
            );

     end if;
  
     RETURN vagas_disponiveis;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_esc_turmas_vagas_disponiveis(integer)
  OWNER TO postgres;

