-- View: arr_contribuintes_vw

-- DROP VIEW arr_contribuintes_vw;

CREATE OR REPLACE VIEW arr_contribuintes_vw AS 
 SELECT p.id AS id_pessoa, j.id AS id_juridica, j.id_contabilidade, p.ds_nome, 
    p.ds_documento, ccid.id_convencao, ccid.id_grupo_cidade, co.ds_descricao, 
    gc.ds_descricao AS ds_grupo_cidade, i.dt_inativacao, 
    mt.ds_descricao AS motivo, mt.id AS id_motivo
   FROM pes_pessoa p
   JOIN pes_juridica j ON j.id_pessoa = p.id
   JOIN pes_pessoa_endereco pe ON j.id_pessoa = pe.id_pessoa AND pe.id_tipo_endereco = 5
   JOIN end_endereco e ON pe.id_endereco = e.id
   JOIN arr_cnae_convencao cc ON cc.id_cnae = j.id_cnae
   JOIN arr_grupo_cidades gcs ON gcs.id_cidade = e.id_cidade
   JOIN arr_convencao_cidade ccid ON ccid.id_grupo_cidade = gcs.id_grupo_cidade AND ccid.id_convencao = cc.id_convencao
   JOIN arr_convencao co ON co.id = ccid.id_convencao
   JOIN arr_grupo_cidade gc ON gc.id = ccid.id_grupo_cidade
   LEFT JOIN arr_contribuintes_inativos i ON i.id_juridica = j.id AND i.dt_ativacao IS NULL
   LEFT JOIN arr_motivo_inativacao mt ON mt.id = i.id_motivo_inativacao;

ALTER TABLE arr_contribuintes_vw
  OWNER TO postgres;


-- View: endereco_vw

-- DROP VIEW endereco_vw;

CREATE OR REPLACE VIEW endereco_vw AS 
 SELECT e.id, l.ds_descricao AS logradouro, de.ds_descricao AS endereco, 
    b.ds_descricao AS bairro, c.ds_cidade AS cidade, c.ds_uf AS uf, 
    e.ds_cep AS cep
   FROM end_endereco e
   JOIN end_logradouro l ON l.id = e.id_logradouro
   JOIN end_descricao_endereco de ON de.id = e.id_descricao_endereco
   JOIN end_bairro b ON b.id = e.id_bairro
   JOIN end_cidade c ON c.id = e.id_cidade;

ALTER TABLE endereco_vw
  OWNER TO postgres;



-- View: pes_fisica_vw

-- DROP VIEW pes_fisica_vw;

CREATE OR REPLACE VIEW pes_fisica_vw AS 
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
    cj.ds_cidade AS e_cidade, cj.ds_uf AS e_uf, endj.ds_cep AS e_cep
   FROM pes_pessoa p
   JOIN pes_fisica pf ON p.id = pf.id_pessoa
   LEFT JOIN pes_pessoa_empresa pe ON pe.id_fisica = pf.id AND pe.dt_demissao IS NULL
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

ALTER TABLE pes_fisica_vw
  OWNER TO postgres;



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
    c_es.ds_cidade AS esccidade, c_es.ds_uf AS escuf, ende_es.ds_cep AS esccep
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


-- View: soc_socios_vw

-- DROP VIEW soc_socios_vw;

CREATE OR REPLACE VIEW soc_socios_vw AS 
 SELECT m.id_titular AS titular, p.id AS codsocio, p.ds_nome AS nome, 
    pr.ds_parentesco AS parentesco, m.nr_matricula AS matricula, 
    c.ds_categoria AS categoria, gc.ds_grupo_categoria AS grupo_categoria
   FROM fin_servico_pessoa se
   JOIN pes_pessoa p ON p.id = se.id_pessoa
   JOIN soc_socios s ON s.id_servico_pessoa = se.id
   JOIN matr_socios m ON m.id = s.id_matricula_socios
   JOIN pes_pessoa t ON t.id = m.id_titular
   JOIN soc_parentesco pr ON pr.id = s.id_parentesco
   JOIN soc_categoria c ON c.id = m.id_categoria
   JOIN soc_grupo_categoria gc ON gc.id = c.id_grupo_categoria
  WHERE m.dt_inativo IS NULL AND (se.ds_ref_validade IS NULL OR se.ds_ref_validade::text = ''::text OR ((('28'::text || '/'::text) || se.ds_ref_validade::text)::timestamp without time zone) > now())
  ORDER BY p.ds_nome;

ALTER TABLE soc_socios_vw
  OWNER TO postgres;


