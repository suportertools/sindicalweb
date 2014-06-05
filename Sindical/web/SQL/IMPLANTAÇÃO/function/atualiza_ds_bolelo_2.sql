-- Function: atualiza_ds_boleto2()

-- DROP FUNCTION atualiza_ds_boleto2();

CREATE OR REPLACE FUNCTION atualiza_ds_boleto2()
  RETURNS boolean AS
$BODY$
BEGIN
    update fin_boleto 
    set ds_boleto=substring(ds_boleto_inicial,1,length(text(ds_boleto_inicial))-length(text(nr_boleto)))||text(nr_boleto)
    from fin_conta_cobranca
    where fin_conta_cobranca.id=id_conta_cobranca and ds_boleto is null and nr_boleto is not null;
    RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION atualiza_ds_boleto2()
  OWNER TO postgres;
