-- Function: func_multa_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_multa_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_multa_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare idTipoServico int        :=idtiposervico;
declare ref           varchar(7) :=ref;

declare qDias         int  :=0;
declare qMeses        int  :=0;
declare qFuncionarios int  :=0;
declare mFuncionario  float:=0;
declare mPrimeiroMes  float:=0;
declare mSegundoMes   float:=0;
declare valor         float:=func_valor_folha_sm(idpessoa,idservico,idtiposervico,ref);
declare multa         float:=0;
declare tipo          int:=0;
declare vencto        date;
BEGIN
    vencto        := 
    (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

   if (CURRENT_DATE>vencto) then
       qDias         := (select CURRENT_DATE-vencto);
       qMeses        := (select func_intervalo_meses(CURRENT_DATE,vencto));

       qFuncionarios := (select f.nr_num_funcionarios  from pes_juridica                 as j 
			left join arr_faturamento_folha_empresa as f  on f.id_juridica=j.id and f.ds_referencia=ref and f.id_tipo_servico=idTipoServico
			 where j.id_pessoa=idPessoa
			);
			
        if (qFuncionarios is null) then qFuncionarios := 0;
        end if;


	mFuncionario := (select cr.nr_multa_por_funcionario from fin_correcao                  as cr 
	                 where cr.id_servicos=idServico and 
			(substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			(substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			 );

	mPrimeiroMes := (select cr.nr_multa_primeiro_mes from  fin_correcao                  as cr where cr.id_servicos=idServico and 
			(substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			(substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);


	mSegundoMes := (select cr.nr_multa_apartir_2mes from  fin_correcao  as cr where cr.id_servicos=idServico and 
			(substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			(substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		       );

	multa := multa + (qFuncionarios*mFuncionario);
	multa := multa + ((mPrimeiroMes * valor)/100);
	multa := multa + (qMeses*((mSegundoMes  * valor)/100));
    end if;
    RETURN round(cast( multa as decimal), 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_multa_sm(integer, integer, integer, character varying)
  OWNER TO postgres;

