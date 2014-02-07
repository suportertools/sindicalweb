ALTER TABLE fin_conta_saldo DROP COLUMN nr_valor_informado;

ALTER TABLE fin_fechamento_caixa DROP COLUMN id_filial;
ALTER TABLE fin_fechamento_caixa DROP COLUMN id_caixa;

ALTER TABLE fin_fechamento_caixa ADD COLUMN nr_valor_fechamento double precision;

ALTER TABLE fin_cartao_rec ADD COLUMN id_status integer;
ALTER TABLE fin_cartao_rec ADD COLUMN dt_liquidacao date;
ALTER TABLE fin_cartao_rec ADD CONSTRAINT fk_cartao_rec_id_status FOREIGN KEY (id_status) REFERENCES fin_status (id) ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE fin_cheque_rec ADD COLUMN dt_liquidacao date;


INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (7, 'A DEPOSITAR', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (8, 'DEPOSITADO', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (9, 'LIQUIDADO', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (10, 'DEVOLVIDO', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (11, 'SUSTADO', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (12, 'FECHAMENTO', '');
INSERT INTO fin_status (id, ds_descricao, ds_historico) VALUES (13, 'TRANSFERÊNCIA', '');
SELECT setval('fin_status_id_seq', max(id)) FROM fin_status;

-- Table: fin_transferencia_caixa

-- DROP TABLE fin_transferencia_caixa;

CREATE TABLE fin_transferencia_caixa
(
  id serial NOT NULL,
  dt_lancamento date,
  nr_valor double precision,
  id_status integer,
  id_caixa_entrada integer,
  id_caixa_saida integer,
  CONSTRAINT fin_transferencia_caixa_pkey PRIMARY KEY (id),
  CONSTRAINT fk_fin_transferencia_caixa_id_caixa_entrada FOREIGN KEY (id_caixa_entrada)
      REFERENCES fin_caixa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_transferencia_caixa_id_caixa_saida FOREIGN KEY (id_caixa_saida)
      REFERENCES fin_caixa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_fin_transferencia_caixa_id_status FOREIGN KEY (id_status)
      REFERENCES fin_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE fin_transferencia_caixa
  OWNER TO postgres;
  
  
  
-- Table: esc_agrupa_turma

-- DROP TABLE esc_agrupa_turma;

CREATE TABLE esc_agrupa_turma
(
  id serial NOT NULL,
  id_turma integer,
  id_turma_integral integer,
  CONSTRAINT esc_agrupa_turma_pkey PRIMARY KEY (id),
  CONSTRAINT fk_esc_agrupa_turma_id_turma FOREIGN KEY (id_turma)
      REFERENCES esc_turma (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_esc_agrupa_turma_id_turma_integral FOREIGN KEY (id_turma_integral)
      REFERENCES esc_turma (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE esc_agrupa_turma
  OWNER TO postgres;




INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 211, 'CARTÃO BANCÁRIO', '"/Sindical/cartao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 211);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 212, 'FECHAMENTO DE CAIXA', '"/Sindical/fechamentoCaixa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 212);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 213, 'CAIXA FECHADO', '"/Sindical/caixaFechado.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 213);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 214, 'CONVITE SERVICO', '"/Sindical/conviteServico.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 214);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 215, 'CONVITE CLUBE', '"/Sindical/conviteClube.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 215);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 216, 'CONVITE MOTIVO SUSPENÇÃO', '"/Sindical/conviteMotivoSuspencao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 216);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 217, 'CONVITE AUTORIZA CORTESIA', '"/Sindical/conviteAutorizaCortesia.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 217);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 218, 'CONVITE SUSPENÇÃO', '"/Sindical/conviteSuspecao.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 218);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 219, 'TRANSFERÊNCIA ENTRE CAIXAS', '"/Sindical/transferenciaEntreCaixa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 219);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 220, 'LIBERAÇÃO REPIS', '"/Sindical/webLiberacaoREPIS.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 220);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 221, 'PESQUISA PESSOA', '"/Sindical/pesquisaSisPessoa.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 221);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 222, 'CONVENÇÃO PERIODO', '"/Sindical/convencaoPeriodo.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 222);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 223, 'SOLICITAR REPIS', '"/Sindical/webSolicitaREPIS.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 223);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 224, 'DEPÓSITO BANCÁRIO', '"/Sindical/depositoBancario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 224);
INSERT INTO seg_rotina (id, ds_rotina, ds_nome_pagina, ds_classe, is_ativo) SELECT 225, 'MOVIMENTO BANCÁRIO', '"/Sindical/movimentoBancario.jsf"', '', true WHERE NOT EXISTS ( SELECT id FROM seg_rotina WHERE id = 225);
SELECT setval('seg_rotina_id_seq', max(id)) FROM seg_rotina;


INSERT INTO fin_servicos 
       (id, is_altera_valor, is_adm, ds_descricao, is_tabela, ds_codigo, is_eleicao, is_debito_clube, is_agrupa_boleto, nr_validade_guia_dias, ds_situacao, id_plano5, id_filial, id_departamento)
VALUES (50, false, false, 'DEPÓSITO', false, '', false, false, false, null, null, null, 1, null);


-- Table: soc_historico_emissao_guias

-- DROP TABLE soc_historico_emissao_guias;

CREATE TABLE soc_historico_emissao_guias
(
  id serial NOT NULL,
  is_baixado boolean,
  dt_emissao date,
  id_usuario integer,
  id_movimento integer,
  CONSTRAINT soc_historico_emissao_guias_pkey PRIMARY KEY (id),
  CONSTRAINT fk_soc_historico_emissao_guias_id_movimento FOREIGN KEY (id_movimento)
      REFERENCES fin_movimento (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_soc_historico_emissao_guias_id_usuario FOREIGN KEY (id_usuario)
      REFERENCES seg_usuario (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE soc_historico_emissao_guias
  OWNER TO postgres;
  

-- Column: id_matricula_socios

-- ALTER TABLE fin_lote DROP COLUMN id_matricula_socios;

ALTER TABLE fin_lote ADD COLUMN id_matricula_socios integer;


  
ALTER TABLE fin_transferencia_caixa
  ADD COLUMN id_fechamento_entrada integer;
ALTER TABLE fin_transferencia_caixa
  ADD COLUMN id_fechamento_saida integer;
ALTER TABLE fin_transferencia_caixa
  DROP CONSTRAINT fk_fin_transferencia_caixa_id_caixa_entrada;
ALTER TABLE fin_transferencia_caixa
  DROP CONSTRAINT fk_fin_transferencia_caixa_id_caixa_saida;
ALTER TABLE fin_transferencia_caixa
  DROP CONSTRAINT fk_fin_transferencia_caixa_id_status;
ALTER TABLE fin_transferencia_caixa
  ADD CONSTRAINT fk_fin_transferencia_caixa_id_caixa_entrada FOREIGN KEY (id_caixa_entrada)
      REFERENCES fin_caixa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_transferencia_caixa
  ADD CONSTRAINT fk_fin_transferencia_caixa_id_caixa_saida FOREIGN KEY (id_caixa_saida)
      REFERENCES fin_caixa (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_transferencia_caixa
  ADD CONSTRAINT fk_fin_transferencia_caixa_id_status FOREIGN KEY (id_status)
      REFERENCES fin_status (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_transferencia_caixa
  ADD CONSTRAINT fk_fin_transferencia_caixa_id_fechamento_entrada FOREIGN KEY (id_fechamento_entrada) REFERENCES fin_fechamento_caixa (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE fin_transferencia_caixa
  ADD CONSTRAINT fk_fin_transferencia_caixa_id_fechamento_saida FOREIGN KEY (id_fechamento_saida) REFERENCES fin_fechamento_caixa (id) ON UPDATE NO ACTION ON DELETE NO ACTION;
  

  
CREATE OR REPLACE FUNCTION func_idBaixa_cheque_rec(idChequeRec int)
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
ALTER FUNCTION func_idBaixa_cheque_rec(int)
  OWNER TO postgres;
  