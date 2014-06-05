
-- Function: func_valor_folha(integer)

-- DROP FUNCTION func_valor_folha(integer);

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
