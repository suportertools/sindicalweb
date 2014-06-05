-- Function: func_multa(integer)

-- DROP FUNCTION func_multa(integer);

CREATE OR REPLACE FUNCTION func_multa(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare idMov         int   :=id_movimento;
declare qMeses        int   :=0;
declare qDias         int   :=0;
declare qFuncionarios int   :=0;
declare mFuncionario  float :=0;
declare mPrimeiroMes  float :=0;
declare mSegundoMes   float :=0;
declare valor         float := func_valor_folha(idMov);
declare multa         float :=0;
  
declare idTipoServico int   := (select id_tipo_servico from fin_movimento where id=idMov );
declare idservico     int   := (select id_servicos from fin_movimento where id=idMov );
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare idpessoa      int   :=(select id_pessoa from fin_movimento where id=idMov );


declare vencto      date  := 
     (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

BEGIN
     if (idTipoServico =4 ) then --- se for acordo pegar vencto e valor do movimento.
 valor  := (select nr_valor      from fin_movimento where id=idMov);
 vencto := (select dt_vencimento from fin_movimento where id=idMov);
     end if;

     if (CURRENT_DATE>vencto) then
       qDias         := (CURRENT_DATE-vencto);
       qMeses        := (func_intervalo_meses(CURRENT_DATE,vencto));
     end if;
 
 qFuncionarios := (select f.nr_num_funcionarios from fin_movimento as m 
   inner join pes_juridica                 as j  on j.id_pessoa=m.id_pessoa
   left join arr_faturamento_folha_empresa as f  on f.id_juridica=j.id and f.ds_referencia=m.ds_referencia and f.id_tipo_servico=m.id_tipo_servico
   where m.id=idMov);

 if (qFuncionarios is null) then qFuncionarios :=0; end if;
 
 mFuncionario := (select cr.nr_multa_por_funcionario as mFuncionario from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
   where m.id=idMov);


 mPrimeiroMes := (select cr.nr_multa_primeiro_mes from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
   where m.id=idMov);

 mSegundoMes := (select cr.nr_multa_apartir_2mes from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
   where m.id=idMov);

    multa := multa + (qFuncionarios*mFuncionario);
    multa := multa + ((mPrimeiroMes * valor)/100);
    multa := multa + (qMeses*((mSegundoMes  * valor)/100));

    if (CURRENT_DATE<=vencto) then
       multa:=0;
    end if;
    
    RETURN round(cast( multa as decimal) , 2);
---   return qFuncionarios;
---   return mFuncionario;
---   return mPrimeiroMes;
---   return valor;
---   return qMeses;
---   return mSegundoMes;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_multa(integer)
  OWNER TO postgres;

