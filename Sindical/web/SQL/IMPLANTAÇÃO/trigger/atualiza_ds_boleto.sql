
-- Function: atualiza_ds_boleto()

-- DROP FUNCTION atualiza_ds_boleto();

CREATE OR REPLACE FUNCTION atualiza_ds_boleto()
  RETURNS trigger AS
$BODY$
BEGIN
     update fin_boleto 
    set ds_boleto=substring(ds_boleto_inicial,1,length(text(ds_boleto_inicial))-length(text(nr_boleto)))||text(nr_boleto)
    from fin_conta_cobranca
    where fin_conta_cobranca.id=id_conta_cobranca and ds_boleto is null and nr_boleto is not null;
     RETURN new;
end;$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION atualiza_ds_boleto()
  OWNER TO postgres;
