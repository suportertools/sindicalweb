-- fin_tipo_documento -- 
-- update: 2013-09-25
-- edited by: Rogério M. Sarmento

-- DELETE FROM fin_tipo_documento WHERE id IN(3, 4, 5, 6, 7, 8,9, 11);
-- UPDATE fin_tipo_documento SET ds_descricao = 'Transferência' WHERE id = 10;
-- UPDATE fin_tipo_documento SET ds_descricao = 'Carteira' WHERE id = 13;
-- DELETE FROM fin_tipo_documento WHERE id > 13;

-- Obs: executado em todos clientes do servidor 102 e na base_nova , exceto no comércio;




ALTER TABLE matr_contrato DROP COLUMN ds_observacao;

ALTER TABLE fin_movimento DROP COLUMN id_evt;

ALTER TABLE esc_turma ADD COLUMN id_filial integer;
ALTER TABLE esc_turma
  ADD CONSTRAINT fk_esc_turma_id_filial FOREIGN KEY (id_filial)
     REFERENCES pes_filial (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE matr_escola ADD COLUMN id_tipo_documento integer;
ALTER TABLE matr_escola
  ADD CONSTRAINT fk_matr_escola_id_tipo_documento FOREIGN KEY (id_tipo_documento)
     REFERENCES fin_tipo_documento (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE matr_escola ADD COLUMN is_ativo boolean;

ALTER TABLE esc_turma ADD COLUMN id_filial integer;
ALTER TABLE esc_turma
  ADD CONSTRAINT fk_esc_turma_id_filial FOREIGN KEY (id_filial)
     REFERENCES pes_filial (id) MATCH SIMPLE      
	ON UPDATE NO ACTION 
	ON DELETE NO ACTION;

ALTER TABLE pes_pessoa_complemento ADD COLUMN is_cobranca_bancaria boolean;
UPDATE pes_pessoa_complemento SET is_cobranca_bancaria = false;

ALTER TABLE fin_lote ADD COLUMN is_desconto_folha boolean;
UPDATE fin_lote SET is_desconto_folha = false;



/** --------------------------------------------------- */

/** --------------------------------------------------- */

CREATE INDEX xid_cobranca_lote
   ON fin_cobranca_lote (id ASC NULLS LAST);

CREATE INDEX xid_cobranca
   ON fin_cobranca (id ASC NULLS LAST);


CREATE INDEX xid_cobranca_movimento
   ON fin_cobranca (id ASC NULLS LAST, id_movimento ASC NULLS LAST);

CREATE INDEX xid_cobranca
   ON fin_cobranca (id ASC NULLS LAST);


-- View: pes_pessoa_vw

-- DROP VIEW pes_pessoa_vw;

CREATE OR REPLACE VIEW pes_pessoa_vw AS 
 SELECT p.id AS codigo, p.dt_criacao AS cadastro, p.ds_nome AS nome, 
    p.ds_documento AS cpf, p.ds_telefone1 AS telefone, pf.ds_uf_emissao_rg, 
    pf.ds_estado_civil AS estado_civil, pf.ds_carteira AS ctps, 
    pf.ds_pai AS pai, pf.ds_sexo AS sexo, pf.ds_mae AS mae, 
    pf.ds_nacionalidade AS nacionalidade, pf.ds_nit AS nit, 
    pf.ds_orgao_emissao_rg, pf.ds_pis, pf.ds_serie, pf.dt_aposentadoria, 
    pf.ds_naturalidade, pf.dt_recadastro AS recadastro, pf.dt_nascimento, 
    pf.dt_foto, pf.ds_rg, pf.dt_foto AS foto, lf.ds_descricao AS logradouro, 
    def.ds_descricao AS endereco, pendf.ds_numero AS numero, 
    pendf.ds_complemento AS complemento, bf.ds_descricao AS bairro, 
    cf.ds_cidade AS cidade, cf.ds_uf AS uf, endf.ds_cep AS cep, 
    pe.ds_setor AS setor, pe.dt_admissao AS admissao, 
    fu.ds_profissao AS profissao, pj.ds_fantasia AS fantasia, 
    pej.ds_nome AS empresa, pej.ds_documento AS cnpj, 
    pej.ds_telefone1 AS e_telefone, lj.ds_descricao AS e_logradouro, 
    dej.ds_descricao AS e_endereco, pendj.ds_numero AS e_numero, 
    pendj.ds_complemento AS e_complemento, bj.ds_descricao AS e_bairro, 
    cj.ds_cidade AS e_cidade, cj.ds_uf AS e_uf, endj.ds_cep AS e_cep, 
    cf.id AS id_cidade, cj.id AS e_id_cidade, p.ds_email1 AS email, 
    pj.id AS e_id, pe.dt_demissao AS demissao, p.ds_telefone2 AS telefone2, 
    p.ds_telefone3 AS telefone3
   FROM pes_pessoa p
   JOIN pes_fisica pf ON p.id = pf.id_pessoa
   LEFT JOIN pes_pessoa_empresa pe ON pe.id_fisica = pf.id
   LEFT JOIN pes_profissao fu ON fu.id = pe.id_funcao
   LEFT JOIN pes_juridica pj ON pj.id = pe.id_juridica
   LEFT JOIN pes_pessoa pej ON pej.id = pj.id_pessoa
   LEFT JOIN pes_pessoa_endereco pendf ON pendf.id_pessoa = p.id AND pendf.id_tipo_endereco = 1
   LEFT JOIN end_endereco endf ON endf.id = pendf.id_endereco
   LEFT JOIN end_logradouro lf ON lf.id = endf.id_logradouro
   LEFT JOIN end_descricao_endereco def ON def.id = endf.id_descricao_endereco
   LEFT JOIN end_bairro bf ON bf.id = endf.id_bairro
   LEFT JOIN end_cidade cf ON cf.id = endf.id_cidade
   LEFT JOIN pes_pessoa_endereco pendj ON pendj.id_pessoa = pej.id AND pendj.id_tipo_endereco = 2
   LEFT JOIN end_endereco endj ON endj.id = pendj.id_endereco
   LEFT JOIN end_logradouro lj ON lj.id = endj.id_logradouro
   LEFT JOIN end_descricao_endereco dej ON dej.id = endj.id_descricao_endereco
   LEFT JOIN end_bairro bj ON bj.id = endj.id_bairro
   LEFT JOIN end_cidade cj ON cj.id = endj.id_cidade;




CREATE OR REPLACE VIEW pes_juridica_vw AS 
 SELECT p.id AS id_pessoa, p.dt_criacao AS cadastro, p.ds_site AS jursite, 
    p.ds_nome AS jurnome, p.ds_documento AS jurdocumento, 
    p.ds_telefone1 AS jurtelefone, l.ds_descricao AS jurlogradouro, 
    de.ds_descricao AS jurendereco, pend.ds_numero AS jurnumero, 
    pend.ds_complemento AS jurcomplemento, b.ds_descricao AS jurbairro, 
    c.ds_cidade AS jurcidade, c.ds_uf AS juruf, ende.ds_cep AS jurcep, 
    pesc.ds_nome AS escnome, pesc.ds_telefone1 AS esctelefone, 
    pesc.ds_email1 AS escemail, l_es.ds_descricao AS esclogradouro, 
    de_es.ds_descricao AS escendereco, pend_es.ds_numero AS escnumero, 
    pend_es.ds_complemento AS esccomplemento, b_es.ds_descricao AS escbairro, 
    c_es.ds_cidade AS esccidade, c_es.ds_uf AS escuf, ende_es.ds_cep AS esccep, 
    esc.id AS escid, c.id AS jur_idcidade, pj.id AS jurid
   FROM pes_pessoa p
   JOIN pes_juridica pj ON pj.id_pessoa = p.id
   LEFT JOIN pes_juridica esc ON esc.id = pj.id_contabilidade
   LEFT JOIN pes_pessoa pesc ON pesc.id = esc.id_pessoa
   LEFT JOIN pes_pessoa_endereco pend ON pend.id_pessoa = p.id AND pend.id_tipo_endereco = 2
   LEFT JOIN end_endereco ende ON ende.id = pend.id_endereco
   LEFT JOIN end_logradouro l ON l.id = ende.id_logradouro
   LEFT JOIN end_descricao_endereco de ON de.id = ende.id_descricao_endereco
   LEFT JOIN end_bairro b ON b.id = ende.id_bairro
   LEFT JOIN end_cidade c ON c.id = ende.id_cidade
   LEFT JOIN pes_pessoa_endereco pend_es ON pend_es.id_pessoa = esc.id_pessoa AND pend_es.id_tipo_endereco = 2
   LEFT JOIN end_endereco ende_es ON ende_es.id = pend_es.id_endereco
   LEFT JOIN end_logradouro l_es ON l_es.id = ende_es.id_logradouro
   LEFT JOIN end_descricao_endereco de_es ON de_es.id = ende_es.id_descricao_endereco
   LEFT JOIN end_bairro b_es ON b_es.id = ende_es.id_bairro
   LEFT JOIN end_cidade c_es ON c_es.id = ende_es.id_cidade;

ALTER TABLE pes_juridica_vw
  OWNER TO postgres;



ALTER TABLE pes_pessoa_vw
  OWNER TO postgres;

ALTER TABLE seg_registro
   ADD COLUMN nr_limite_envios_notificacao integer;

update seg_registro set nr_limite_envios_notificacao = 150;

ALTER TABLE seg_registro
   ADD COLUMN nr_intervalo_envios_notificacao integer;

update seg_registro set nr_intervalo_envios_notificacao = 60;

ALTER TABLE fin_cobranca_lote
   ADD COLUMN ds_hora character varying(5);


-- Function: func_multa_ass(integer)

-- DROP FUNCTION func_multa_ass(integer);

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



--------------------------------------------------------------------------------------------------------------


-- Function: func_juros_ass(integer)

-- DROP FUNCTION func_juros_ass(integer);

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


---------------------------------------------------------------------------------------------------------------------------------------------------


-- Function: func_correcao_ass(integer)

-- DROP FUNCTION func_correcao_ass(integer);

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
>=----'201201'
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

---------------------------------------------------------------------------------------------------------------------------------

-- Function: func_intervalo_dias(date, date)

-- DROP FUNCTION func_intervalo_dias(date, date);

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

