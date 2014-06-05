
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

ALTER TABLE pes_pessoa_vw
  OWNER TO postgres;

