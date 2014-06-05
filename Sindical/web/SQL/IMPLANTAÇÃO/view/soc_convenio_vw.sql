-- View: soc_convenio_vw

-- DROP VIEW soc_convenio_vw;

CREATE OR REPLACE VIEW soc_convenio_vw AS 
 SELECT p.id AS id_pessoa, j.id AS id_juridica, g.id AS id_grupo, 
    s.id AS id_subgrupo, g.ds_descricao AS ds_grupo, 
    s.ds_descricao AS ds_subgrupo, p.ds_nome, e.logradouro, e.endereco, 
    pe.ds_numero AS numero, pe.ds_complemento AS complemento, e.bairro, 
    e.cidade, e.uf, e.cep
   FROM soc_convenio c
   JOIN pes_juridica j ON j.id = c.id_juridica
   JOIN pes_pessoa p ON p.id = j.id_pessoa
   JOIN pes_pessoa_endereco pe ON pe.id_pessoa = p.id AND pe.id_tipo_endereco = 2
   JOIN endereco_vw e ON e.id = pe.id_endereco
   JOIN soc_convenio_sub_grupo s ON s.id = c.id_convenio_sub_grupo
   JOIN soc_convenio_grupo g ON g.id = s.id_grupo_convenio;

ALTER TABLE soc_convenio_vw
  OWNER TO postgres;

