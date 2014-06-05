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


