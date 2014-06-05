
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
