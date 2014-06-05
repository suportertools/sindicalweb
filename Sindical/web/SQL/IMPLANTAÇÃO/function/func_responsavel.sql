
-- Function: func_responsavel(integer, boolean)

-- DROP FUNCTION func_responsavel(integer, boolean);

CREATE OR REPLACE FUNCTION func_responsavel(pessoa integer, descontofolha boolean)
  RETURNS integer AS
$BODY$
     declare titular int;
     declare responsavel int;
     declare idade int;
BEGIN

     idade = (select func_idade(dt_nascimento,current_date) from pes_fisica where id_pessoa=pessoa);

---  Responsável default:

---  Sócio e desconto sem folha  falso: Pegar o titular
     titular =  (select s.titular from soc_socios_vw as s where s.inativacao is null and s.codsocio=pessoa);
     if (titular is not null and descontofolha=false) then
        responsavel = titular;
     end if;

---  Sócio com  desconto em  folha  verdadeiro: Pegar o a Empresa do Titular.
       if (titular is not null and descontofolha=true) then
         responsavel = 
 	 (
 	    select j.id_pessoa  from pes_pessoa_vw as p
	    inner join pes_juridica as j  on  j.id=p.e_id
	    where p.codigo=titular and demissao is null
	 );
	 if (responsavel is null) then
            responsavel=titular;
         end if;
     end if;

---- Não Sócio Desconto em Folha
     if (titular is null and idade >= 18 and descontofolha=true) then
          responsavel = 
 	 (
 	    select j.id_pessoa  from pes_pessoa_vw as p
	    inner join pes_juridica as j  on  j.id=p.e_id
	    where p.codigo=pessoa and demissao is null
	 );
	 if (responsavel is null) then
            responsavel=0;
         end if;
     end if;
---  Não Sócio Maior Sem Desconto em Folha.
     if (titular is null and idade >= 18 and descontofolha=false) then
          responsavel = pessoa;
     end if;
---  Não Sócio menor: exigir uma PF maior de idade ou uma PJ
     if (titular is null and idade < 18) then
        responsavel=0;
     end if;

   if (responsavel is null) then responsavel=0; end if;   
   RETURN responsavel;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_responsavel(integer, boolean)
  OWNER TO postgres;
