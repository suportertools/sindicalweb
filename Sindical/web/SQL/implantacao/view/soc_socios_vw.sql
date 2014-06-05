
-- View: soc_socios_vw

-- DROP VIEW soc_socios_vw;

CREATE OR REPLACE VIEW soc_socios_vw AS 
 SELECT m.id_titular AS titular, p.id AS codsocio, p.ds_nome AS nome, 
    pr.ds_parentesco AS parentesco, m.nr_matricula AS matricula, 
    c.ds_categoria AS categoria, gc.ds_grupo_categoria AS grupo_categoria, 
    m.dt_emissao AS filiacao, m.dt_inativo AS inativacao, c.votante, 
    pr.ds_parentesco AS grau, se.nr_desconto, se.desconto_folha, 
    tdoc.ds_descricao AS tipo_cobranca, tdoc.id AS cod_tipo_cobranca, 
    c.id AS id_categoria, gc.id AS id_grupo_categoria, pr.id AS id_parentesco, 
    s.id AS id_socio, s.nr_via_carteirinha AS nr_via, 
    s.dt_validade_carteirinha AS validade_carteirinha
   FROM fin_servico_pessoa se
   JOIN pes_pessoa p ON p.id = se.id_pessoa
   JOIN soc_socios s ON s.id_servico_pessoa = se.id
   JOIN matr_socios m ON m.id = s.id_matricula_socios
   JOIN pes_pessoa t ON t.id = m.id_titular
   JOIN soc_parentesco pr ON pr.id = s.id_parentesco
   JOIN soc_categoria c ON c.id = m.id_categoria
   JOIN soc_grupo_categoria gc ON gc.id = c.id_grupo_categoria
   JOIN fin_tipo_documento tdoc ON tdoc.id = se.id_tipo_documento
  WHERE m.dt_inativo IS NULL AND (se.ds_ref_validade IS NULL OR se.ds_ref_validade::text = ''::text OR ((('28'::text || '/'::text) || se.ds_ref_validade::text)::timestamp without time zone) > now());

ALTER TABLE soc_socios_vw
  OWNER TO postgres;

