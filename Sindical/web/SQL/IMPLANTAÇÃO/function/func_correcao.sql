-- Function: func_correcao(integer)

-- DROP FUNCTION func_correcao(integer);

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
