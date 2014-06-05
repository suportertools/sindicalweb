-- View: soc_boletos_vw

-- DROP VIEW soc_boletos_vw;

CREATE OR REPLACE VIEW soc_boletos_vw AS 
 SELECT l.id AS id_fin_lote, m.id AS id_fin_movimeto, m.nr_ctr_boleto, 
    sl.id AS id_lote_boleto, sl.dt_processamento AS processamento, 
    '' AS logo_banco, '' AS logo, '' AS logo_informativo, '' AS logo_verso, 
    pr.id AS codigo, pr.ds_nome AS responsavel, 
    '1997-10-07'::date + "substring"(m.nr_ctr_boleto::text, 9, 4)::integer AS vencimento, 
    mtr.matricula, mtr.grupo_categoria, mtr.categoria, 
    se.ds_descricao AS servico, m.id_beneficiario, 
    pb.ds_nome AS nome_beneficiario, m.nr_valor AS valor, 
    0 AS mensalidades_corrigidas, 
    'NÃO RECEBER APÓS O VENCIMENTO.' AS mensagem_boleto, 
    bco.nr_num_banco AS banco, cb.ds_agencia AS agencia, 
    c.ds_cod_cedente AS cedente, b.ds_boleto AS boleto, f.juremail1 AS email, 
    f.jurnome AS nome_filial, f.jursite AS silte_filial, 
    f.jurdocumento AS cnpj_filial, f.jurtelefone AS tel_filial, 
    (((((f.jurlogradouro::text || ' '::text) || f.jurendereco::text) || ', '::text) || f.jurnumero::text) || ' '::text) || f.jurcomplemento::text AS endereco_filial, 
    f.jurbairro AS bairro_filial, f.jurcidade AS cidade_filial, 
    f.juruf AS uf_filial, 
    ("substring"(f.jurcep::text, 1, 5) || '-'::text) || "right"(f.jurcep::text, 3) AS cep_filial, 
    er.logradouro AS logradouro_responsavel, 
    rtrim(((((er.endereco::text || ', '::text) || per.ds_numero::text) || ' '::text) || per.ds_complemento::text) || er.bairro::text) AS endereco_responsavel, 
    ("left"(er.cep::text, 5) || '-'::text) || "right"(er.cep::text, 3) AS cep_responsavel, 
    er.uf AS uf_responsavel, er.cidade AS cidade_responsavel, 
    co.ds_informativo AS informativo, co.ds_local_pagamento AS local_pagamento
   FROM fin_lote l
   JOIN fin_movimento m ON m.id_lote = l.id
   JOIN soc_lote_boleto sl ON sl.id = "substring"(m.nr_ctr_boleto::text, 16, 5)::integer AND length(m.nr_ctr_boleto::text) = 22
   JOIN pes_pessoa pr ON pr.id = m.id_pessoa
   JOIN pes_pessoa pb ON pb.id = m.id_beneficiario
   JOIN pes_juridica_vw f ON f.id_pessoa = 1
   JOIN fin_servicos se ON se.id = m.id_servicos
   JOIN fin_boleto b ON b.nr_ctr_boleto::text = m.nr_ctr_boleto::text
   JOIN fin_conta_cobranca c ON c.id = b.id_conta_cobranca
   JOIN fin_conta_banco cb ON cb.id = c.id_conta_banco
   JOIN fin_banco bco ON bco.id = cb.id_banco
   JOIN pes_pessoa_endereco per ON per.id_pessoa = pr.id AND per.id_tipo_endereco = 3
   JOIN endereco_vw er ON er.id = per.id_endereco
   JOIN soc_cobranca co ON co.id = 1
   LEFT JOIN soc_socios_vw mtr ON mtr.codsocio = pr.id;

ALTER TABLE soc_boletos_vw
  OWNER TO postgres;


