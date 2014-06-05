
-- Function: func_idbaixa_cheque_rec(integer)

DROP FUNCTION func_idbaixa_cheque_rec(integer);

CREATE OR REPLACE FUNCTION func_idbaixa_cheque_rec(idchequerec integer)
  RETURNS integer AS
$BODY$
    declare idBaixa int;
BEGIN
/* Ex: select func_idBaixa_cheque_rec(id) from fin_cheque_rec */
    idBaixa = (select min(id_baixa) from fin_forma_pagamento where id_cheque_rec = idChequeRec);
    if (idBaixa is null) then
       idBaixa=0;
    end if;
    RETURN idBaixa;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_idbaixa_cheque_rec(integer)
  OWNER TO postgres;
