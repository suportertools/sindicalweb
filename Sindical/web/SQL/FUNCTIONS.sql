-- Table: sis_contador_acessos

-- DROP TABLE sis_contador_acessos;



CREATE OR REPLACE FUNCTION func_valor_folha(id_mov integer)
  RETURNS double precision AS
$BODY$

declare valor     float:=(select nr_valor        from fin_movimento where id=id_mov);
declare tipo      int  :=(select id_tipo_servico from fin_movimento where id=id_mov);
BEGIN
   if (tipo <> 4 and valor =0) then
       valor :=
          (
---          select f.nr_valor,f.nr_num_funcionarios,d.nr_valor_por_empregado,d.nr_percentual from fin_movimento as m 
          select (f.nr_num_funcionarios*d.nr_valor_por_empregado)+(d.nr_percentual*f.nr_valor/100) from fin_movimento as m 
          inner join pes_juridica                 as j  on j.id_pessoa=m.id_pessoa
          inner join arr_faturamento_folha_empresa as f  on f.id_juridica=j.id and f.ds_referencia=m.ds_referencia and f.id_tipo_servico=m.id_tipo_servico
          inner join fin_mensagem_cobranca  as mc on mc.id_movimento=m.id
          inner join arr_mensagem_convencao as mg on mg.id=mc.id_mensagem_convencao
          inner join arr_desconto_empregado as d on d.id_servicos=m.id_servicos and d.id_convencao=mg.id_convencao and d.id_grupo_cidade=mg.id_grupo_cidade
          and
          (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(d.ds_ref_inicial,4,4)||substring(d.ds_ref_inicial,1,2)) and
          (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(d.ds_ref_final,4,4)||substring(d.ds_ref_final,1,2))
          where m.id=id_mov
       );
       if (valor is null) then
          valor:=0;
       end if;
    end if;
    RETURN round(cast(valor*100 as decimal) / 100, 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor_folha(integer)
  OWNER TO postgres;



-- by

CREATE OR REPLACE FUNCTION func_correcao(idmov integer)
  RETURNS double precision AS
$BODY$

declare indice      int:=
                 (
                 select cr.id_indice from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
     (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
       where m.id=idMov
       );
declare vlIndice      float := 0;
declare valor         float := func_valor_folha(idMov);
declare valorBase     float := valor;

declare idTipoServico int   := (select id_tipo_servico from fin_movimento where id=idMov );
declare idservico     int   := (select id_servicos from fin_movimento where id=idMov );
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare idpessoa      int   :=(select id_pessoa from fin_movimento where id=idMov );


declare vencto      date  := 
     (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );



DECLARE lista CURSOR FOR 
(
SELECT nr_valor FROM fin_indice_mensal 
where 
id_indice=indice and 
(
text(nr_ano)||right('0'||text(nr_mes),2)
>=----'201201'
(text(extract('year' from  vencto))||right('0'||text(extract('month' from  vencto)),2))
)
order by nr_ano,nr_mes
);
begin  

if (idTipoServico =4 ) then --- se for acordo pegar vencto e valor do movimento.
   valor  := (select nr_valor      from fin_movimento where id=idMov);
   vencto := (select dt_vencimento from fin_movimento where id=idMov);
end if;


open lista;
   if (CURRENT_DATE>vencto) then   --- se data vencida
  -- Para ir para o primeiro registo:
 FETCH FIRST FROM lista into vlIndice;
 loop
          if (vlIndice is null) then vlIndice:=0; end if;
          valor := valor + ((valor * vlIndice)/100);
  FETCH NEXT FROM lista into vlIndice;
  EXIT WHEN NOT FOUND;
 end loop;
   end if;------ se data vencida
   close lista;
   RETURN round(cast( (valor-valorBase)*100 as decimal) / 100, 2);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_correcao(integer)
  OWNER TO postgres;







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


CREATE OR REPLACE FUNCTION func_juros(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare qDias         int  :=0;
declare idMov         int  :=id_movimento;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare valor         float:=func_valor_folha(idMov);
declare juros         float:=0;
declare jJurosDiario  float:=0;
--declare vencto        date:=select dt_vencimento from fin_movimento where id=idMov;


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
        qMeses        := (select func_intervalo_meses(CURRENT_DATE,vencto));
        qDias         := (select CURRENT_DATE-vencto);

	jPrimeiroMes := (select cr.nr_juros_pri_mes from fin_correcao  as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);
			
	jSegundoMes := (select cr.nr_juros_apartir_2mes from  fin_correcao as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		       );

	jJurosDiario:=  (select cr.nr_juros_diarios from fin_correcao  as cr 
			 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);


	juros := juros + (jJurosDiario*qDias);
	juros := juros + ((jPrimeiroMes * valor)/100);
	juros := juros + (qMeses*((jSegundoMes  * valor)/100));
    end if;
    RETURN round(cast(juros as decimal), 2);
    --RETURN qDias ;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros(integer)
  OWNER TO postgres;
/*

CREATE OR REPLACE FUNCTION func_desconto(id_movimento integer, desconto double precision, total double precision)
  RETURNS double precision AS
$BODY$

declare wdesconto  float := desconto;
declare wtotal     float := total;
declare wdesc      float := 0;
declare wvalor     float := (select func_multa(id)+func_juros(id)+func_correcao(id) from fin_movimento where id=id_movimento);
declare wservico   int   := (select id_servicos from fin_movimento where id=id_movimento);

begin  
   if wservico <> 1 and wdesconto > 0 and wtotal > 0 then
      wdesc := (wdesconto/wtotal)*100;
      wdesc := (wvalor*wdesc)/100;
   end if;
   return wdesc;
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_desconto(integer, double precision, double precision)
  OWNER TO postgres;


*/


-- Function: func_juros_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_juros_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_juros_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare idTipoServico int        :=idtiposervico;
declare ref           varchar(7) :=ref;

declare qDias         int  :=0;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare valor         float:=func_valor_folha_sm(idpessoa,idservico,idtiposervico,ref);
declare juros         float:=0;
declare jJurosDiario  float:=0;
declare tipo          int  :=0;
declare vencto        date;
BEGIN
    vencto        := 
    (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

   
   if (CURRENT_DATE>vencto) then
        qMeses        := (select func_intervalo_meses(CURRENT_DATE,vencto));
        qDias         := (select CURRENT_DATE-vencto);

	jPrimeiroMes := (select cr.nr_juros_pri_mes from fin_correcao  as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);
			
	jSegundoMes := (select cr.nr_juros_apartir_2mes from  fin_correcao as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		       );

	jJurosDiario:=  (select cr.nr_juros_diarios from fin_correcao  as cr 
			 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);


	juros := juros + (jJurosDiario*qDias);
	juros := juros + ((jPrimeiroMes * valor)/100);
	juros := juros + (qMeses*((jSegundoMes  * valor)/100));
    end if;
    RETURN round(cast( juros as decimal ), 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros_sm(integer, integer, integer, character varying)
  OWNER TO postgres;

-- Function: func_multa(integer)

-- DROP FUNCTION func_multa(integer);

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


-- Function: func_correcao(integer)

-- DROP FUNCTION func_correcao(integer);

CREATE OR REPLACE FUNCTION func_correcao_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare idTipoServico int        :=idtiposervico;
declare ref           varchar(7) :=ref;

declare indice      int:=
	                (
	                select cr.id_indice from  fin_correcao                  as cr where cr.id_servicos=idServico and 
			(substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
  			(substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		   	 
		   	 );
declare vlIndice    float := 0;
declare valor       float := func_valor_folha_sm(idpessoa,idservico,idtiposervico,ref);
declare valorBase   float := valor;
declare vencto      date  := 
     (
        select dt_vencimento from arr_mensagem_convencao where 't'||id_tipo_servico||'s'||id_servicos||'r'||ds_referencia||'c'||id_convencao||'g'||id_grupo_cidade=
        (select 't'||idTipoServico||'s'||idServico||'r'||ref||'c'||id_convencao||'g'||id_grupo_cidade from arr_contribuintes_vw where id_pessoa=idPessoa)
    );

DECLARE lista CURSOR FOR 
(
SELECT nr_valor FROM fin_indice_mensal 
where 
id_indice=indice and 
(
text(nr_ano)||substring('0'||text(nr_mes),length('0'||text(nr_mes))-1,length('0'||text(nr_mes)))
)
>=
(
text(extract('year' from  vencto))||substring('0'||text(extract('month' from  vencto)),length('0'||text(extract('month' from  vencto)))-1,length('0'||text(extract('month' from  vencto))))
)
order by nr_ano,nr_mes
);
begin  
   open lista;
   if (CURRENT_DATE>vencto) then   --- se data vencida
	-- Para ir para o primeiro registo:
	FETCH FIRST FROM lista into vlIndice;
	loop
	        if (vlIndice is null) then vlIndice=0; end if;
 	        valor := valor + ((valor * vlIndice)/100);
		FETCH NEXT FROM lista into vlIndice;
		EXIT WHEN NOT FOUND;
	end loop;
   end if;------ se data vencida
   close lista;
    RETURN round(cast( (valor-valorBase)*100 as decimal) / 100, 2);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_correcao_sm(integer, integer, integer, character varying)
  OWNER TO postgres;






-- Function: func_valor_folha_sm(integer, integer, integer, character varying)

-- DROP FUNCTION func_valor_folha_sm(integer, integer, integer, character varying);

CREATE OR REPLACE FUNCTION func_valor_folha_sm(idpessoa integer, idservico integer, idtiposervico integer, ref character varying)
  RETURNS double precision AS
$BODY$

declare idPessoa      int        :=idpessoa;
declare idServico     int        :=idservico;
declare tipo int        :=idtiposervico;
declare ref           varchar(7) :=ref;
declare valor         float:=0;
BEGIN
   if (tipo <> 4 and valor =0) then
       valor :=
          (
           select (f.nr_num_funcionarios*d.nr_valor_por_empregado)+(d.nr_percentual*f.nr_valor/100) from 
           arr_contribuintes_vw                 as j
           inner join arr_faturamento_folha_empresa as f  on f.id_juridica=j.id_juridica and f.ds_referencia=ref and f.id_tipo_servico=tipo
           inner join arr_desconto_empregado as d on d.id_servicos=idServico and d.id_convencao=j.id_convencao and d.id_grupo_cidade=j.id_grupo_cidade
           and
           (substring(ref,4,4)||substring(ref,1,2)) >= (substring(d.ds_ref_inicial,4,4)||substring(d.ds_ref_inicial,1,2)) and
           (substring(ref,4,4)||substring(ref,1,2)) <= (substring(d.ds_ref_final,4,4)||substring(d.ds_ref_final,1,2))
           where  j.id_pessoa=idPessoa
          );
       if (valor is null) then
          valor:=0;
       end if;
    end if;
    RETURN round(cast( valor*100 as decimal) / 100, 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor_folha_sm(integer, integer, integer, character varying)
  OWNER TO postgres;

-- Function: func_nullInteger(n int)

-- DROP FUNCTION func_nullInteger(n int);

CREATE OR REPLACE FUNCTION func_nullInteger(n int)
  RETURNS int AS
$BODY$
DECLARE nun int := 0;
BEGIN
      if (n is not null) then nun=n; end if;
      RETURN nun;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_nullInteger(int)
  OWNER TO postgres;



--------------------------------------------------------------------------------

-- Function: func_multa_ass(integer)

CREATE OR REPLACE FUNCTION func_multa_ass(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare idMov         int   :=id_movimento;
declare qMeses        int   :=0;
declare qDias         int   :=0;
declare mPrimeiroMes  float :=0;
declare mSegundoMes   float :=0;
declare multa         float :=0;
   
declare idBaixa       int        := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int        := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date       := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float      := (select nr_valor      from fin_movimento where id=idMov);
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);
BEGIN

    if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
       qDias         := (CURRENT_DATE-vencto);
       qMeses        := (func_intervalo_meses(CURRENT_DATE,vencto));
 
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

       if (mPrimeiroMes is null) then mPrimeiroMes :=0; end if;
       if (mSegundoMes  is null) then mSegundoMes  :=0; end if;

       multa := multa + ((mPrimeiroMes * valor)/100);
       multa := multa + (qMeses*((mSegundoMes  * valor)/100));
    end if;
   
    RETURN round(cast( multa as decimal) , 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_multa_ass(integer)
  OWNER TO postgres;



--------------------------------------------------------------------------------

-- Function: func_juros_ass(integer)

CREATE OR REPLACE FUNCTION func_juros_ass(id_movimento integer)
  RETURNS double precision AS
$BODY$

declare qDias         int  :=0;
declare idMov         int  :=id_movimento;
declare qMeses        int  :=0;
declare jPrimeiroMes  float:=0;
declare jSegundoMes   float:=0;
declare juros         float:=0;
declare jJurosDiario  float:=0;
 

declare idBaixa       int   := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int   := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date  := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float := (select nr_valor      from fin_movimento where id=idMov);
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);

 
BEGIN
  
   if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
   
        qMeses        := (select func_intervalo_meses(CURRENT_DATE,vencto));
        qDias         := (select CURRENT_DATE-vencto);

	jPrimeiroMes := (select cr.nr_juros_pri_mes from fin_correcao  as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);
			
	jSegundoMes := (select cr.nr_juros_apartir_2mes from  fin_correcao as cr 
	                 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
		       );

	jJurosDiario:=  (select cr.nr_juros_diarios from fin_correcao  as cr 
			 where cr.id_servicos=idServico and 
			 (substring(ref,4,4)||substring(ref,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
			 (substring(ref,4,4)||substring(ref,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
			);


        if (jPrimeiroMes is null) then jPrimeiroMes :=0; end if;
        if (jSegundoMes  is null) then jSegundoMes  :=0; end if;


	juros := juros + (jJurosDiario*qDias);
	juros := juros + ((jPrimeiroMes * valor)/100);
	juros := juros + (qMeses*((jSegundoMes  * valor)/100));
    end if;
    RETURN round(cast(juros as decimal), 2);
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_juros_ass(integer)
  OWNER TO postgres;

--------------------------------------------------------------------------------

-- Function: func_correcao_ass(integer)

CREATE OR REPLACE FUNCTION func_correcao_ass(idmov integer)
  RETURNS double precision AS
$BODY$

declare indice      int:=
                 (
                 select cr.id_indice from fin_movimento as m 
   left join fin_correcao                  as cr on cr.id_servicos=m.id_servicos and 
   (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) >= (substring(cr.ds_ref_inicial,4,4)||substring(cr.ds_ref_inicial,1,2)) and 
     (substring(m.ds_referencia,4,4)||substring(m.ds_referencia,1,2)) <= (substring(cr.ds_ref_final,4,4)||substring(cr.ds_ref_final,1,2))
       where m.id=idMov
       );

declare vlIndice      float := 0;
  
declare idBaixa       int   := (select id_baixa      from fin_movimento where id=idMov);
declare idservico     int   := (select id_servicos   from fin_movimento where id=idMov);
declare vencto        date  := (select dt_vencimento from fin_movimento where id=idMov);
declare valor         float := (select nr_valor      from fin_movimento where id=idMov);
declare valorBase     float := valor;
declare ref           varchar(7) :=(select ds_referencia from fin_movimento where id=idMov );
declare es            varchar(1) := (select ds_es    from fin_movimento where id=idMov);


DECLARE lista CURSOR FOR 
(
SELECT nr_valor FROM fin_indice_mensal 
where 
id_indice=indice and 
(
text(nr_ano)||right('0'||text(nr_mes),2)
>=
----'201201'
(text(extract('year' from  vencto))||right('0'||text(extract('month' from  vencto)),2))
)
order by nr_ano,nr_mes
);
begin  


open lista;
   if (CURRENT_DATE>vencto and idBaixa is null and es='E') then
          -- Para ir para o primeiro registo:
          FETCH FIRST FROM lista into vlIndice;
          loop
             if (vlIndice is null) then vlIndice:=0; end if;
             valor := valor + ((valor * vlIndice)/100);
             FETCH NEXT FROM lista into vlIndice;
             EXIT WHEN NOT FOUND;
          end loop;
   end if;------ se data vencida
   close lista;
   RETURN round(cast( (valor-valorBase)*100 as decimal) / 100, 2);
end;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_correcao_ass(integer)
  OWNER TO postgres;

--------------------------------------------------------------------------------

-- Function: func_intervalo_dias(date, date)

CREATE OR REPLACE FUNCTION func_intervalo_dias(vencimentoi date, vencimentof date)
  RETURNS integer AS
$BODY$
DECLARE dias int := 0;
DECLARE DI   varchar(20);
DECLARE DF   varchar(20);
BEGIN
    DI   := to_char(vencimentoI,'DD/MM/YYYY HH:MI:SS');
    DF   := to_char(vencimentoF,'DD/MM/YYYY HH:MI:SS');
    dias :=
----    (select DATE_PART('day',(to_timestamp('01/03/2013', 'DD/MM/YYYY HH:MI:SS') - to_timestamp('01/02/2012', 'DD/MM/YYYY HH:MI:SS'))));
        (select DATE_PART('day',(to_timestamp(DF, 'DD/MM/YYYY HH:MI:SS') - to_timestamp(DI, 'DD/MM/YYYY HH:MI:SS'))));
    if (dias < 0) then dias:=0; end if;
    RETURN dias;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_intervalo_dias(date, date)
  OWNER TO postgres;

--------------------------------------------------------------------------------


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

-- Function: func_valor_servico(integer, integer, date, integer)

-- DROP FUNCTION func_valor_servico(integer, integer, date, integer);

CREATE OR REPLACE FUNCTION func_valor_servico(pessoa integer, servico integer, vencimento date, tipo integer)
  RETURNS double precision AS
$BODY$

    declare valor double precision;
BEGIN
/*
  Tipo: 0=valor, 1=desconto até o vencimento=taxa

  Ex:

                               pessoa, servico,   vencto    , tipo
---  não é sócio e empresa sem desconto (valor sem desconto)
select func_valor_servico(11815,104,'09/10/2013',0), --- Valor (já calculado)
       func_valor_servico(11815,104,'09/10/2013',1), --- Valor até o vencimento (já calculado)
       func_valor_servico(11815,104,'09/10/2013',2)  --- Taxa até o vencimento (já calculado)

---- não sócio empresa com desconto (convênio)
select func_valor_servico(11699,104,'09/10/2013',0),
       func_valor_servico(11699,104,'09/10/2013',1),
       func_valor_servico(11699,104,'09/10/2013',2)

---- sócio
select func_valor_servico(12859,104,'09/10/2013',0),
       func_valor_servico(12859,104,'09/10/2013',1),
       func_valor_servico(12859,104,'09/10/2013',2)
  
*/
        if (tipo=0) then
		valor :=
		(
			select 
			sv.nr_valor-(cd.nr_desconto*sv.nr_valor/100)
			----cd.nr_desconto desconto,sv.nr_valor valor,sv.nr_valor-(cd.nr_desconto*sv.nr_valor/100) as valor_com_desconto 
			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa
		);
	
		if (valor is null) then
			valor = 
			(
			select 
			----func_idade(p.dt_nascimento,vencimento),de.nr_desconto desconto,sv.nr_valor valor,sv.nr_valor-(de.nr_desconto*sv.nr_valor/100) as valor_com_desconto 
			sv.nr_valor-(de.nr_desconto*sv.nr_valor/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
			----func_idade(p.dt_nascimento,vencimento),de.nr_desconto desconto,sv.nr_valor valor,sv.nr_valor-(de.nr_desconto*sv.nr_valor/100) as valor_com_desconto 
			sv.nr_valor  
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

        if (tipo=1) then
		valor :=
		(
			select 
			sv.nr_desconto_ate_vencimento-(cd.nr_desconto*sv.nr_desconto_ate_vencimento/100)
			----cd.nr_desconto desconto,sv.nr_valor valor,sv.nr_valor-(cd.nr_desconto*sv.nr_valor/100) as valor_com_desconto 
			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa
		);
	
		if (valor is null) then
			valor = 
			(
			select 
			----func_idade(p.dt_nascimento,vencimento),de.nr_desconto desconto,sv.nr_valor valor,sv.nr_valor-(de.nr_desconto*sv.nr_valor/100) as valor_com_desconto 
			sv.nr_desconto_ate_vencimento-(de.nr_desconto*sv.nr_desconto_ate_vencimento/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_desconto_ate_vencimento 
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

        if (tipo=2) then
		valor :=
		(
			select 
			sv.nr_taxa-(cd.nr_desconto*sv.nr_taxa/100)
 			from soc_socios_vw as so 
			inner join soc_categoria_desconto cd on cd.id_categoria=so.id_categoria and cd.id_servico=servico
			inner join pes_fisica as f on f.id_pessoa=so.codsocio
			inner join fin_servico_valor as sv on sv.id_servico=cd.id_servico and func_idade(f.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where so.inativacao is null and so.codsocio=pessoa
		);
	
		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_taxa-(de.nr_desconto*sv.nr_taxa/100) 
			from pes_pessoa_vw as p
			inner join fin_desconto_servico_empresa as de on de.id_juridica=p.e_id and de.id_servico=servico
			inner join fin_servico_valor as sv on sv.id_servico=de.id_servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

		if (valor is null) then
			valor = 
			(
			select 
 			sv.nr_taxa  
			from pes_pessoa_vw as p
 			inner join fin_servico_valor as sv on sv.id_servico=servico and func_idade(p.dt_nascimento,vencimento) BETWEEN sv.nr_idade_ini and sv.nr_idade_fim
			where p.codigo=pessoa
			);	
		end if;

	end if;

	if (valor is null) then
		valor=0;
	end if;

        RETURN valor;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor_servico(integer, integer, date, integer)
  OWNER TO postgres;



-- Function: func_valor(integer)

-- DROP FUNCTION func_valor(integer);

CREATE OR REPLACE FUNCTION func_valor(mov integer)
  RETURNS double precision AS
$BODY$

    declare valor double precision;
    declare vencto date;
    
BEGIN
    vencto = (select dt_vencimento from fin_movimento where id=mov);
    valor  = (select nr_valor from fin_movimento where id=mov);
    if (vencto >= current_date) then
       valor  = (select nr_valor-nr_desconto_ate_vencimento from fin_movimento where id=mov);
    end if;
 
    RETURN valor;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION func_valor(integer)
  OWNER TO postgres;


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
