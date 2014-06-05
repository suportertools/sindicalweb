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

