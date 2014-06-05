
-- View: pes_juridica_vw

-- DROP VIEW pes_juridica_vw;

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
    esc.id AS escid, c.id AS jur_idcidade, pj.id AS jurid, 
    p.ds_email1 AS juremail1, p.ds_email2 AS juremail2, 
    p.ds_email3 AS juremail3
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

