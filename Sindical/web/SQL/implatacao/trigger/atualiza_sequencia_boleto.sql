
-- Function: atualiza_sequencia_boleto()

DROP FUNCTION atualiza_sequencia_boleto();

CREATE OR REPLACE FUNCTION atualiza_sequencia_boleto()
  RETURNS trigger AS
$BODY$begin
     if (select max(nr_boleto)+1 from fin_boleto where id_conta_cobranca=new.id_conta_cobranca and is_ativo=true) is null then
           new.nr_boleto = 1;
     else
           new.nr_boleto = (select max(nr_boleto)+1 from fin_boleto where id_conta_cobranca=new.id_conta_cobranca and is_ativo=true);
      end if;
     return new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION atualiza_sequencia_boleto()
  OWNER TO postgres;
