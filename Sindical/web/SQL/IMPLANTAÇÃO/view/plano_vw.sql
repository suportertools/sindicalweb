-- View: plano_vw

-- DROP VIEW plano_vw;

CREATE OR REPLACE VIEW plano_vw AS 
 SELECT p1.id AS id_p1, p1.ds_conta AS conta1, p2.id AS id_p2, 
    p2.ds_conta AS conta2, p3.id AS id_p3, p3.ds_conta AS conta3, 
    p4.id AS id_p4, p4.ds_conta AS conta4, p5.id AS id_p5, 
    p5.ds_conta AS conta5, p5.ds_classificador AS classificador
   FROM fin_plano p1
   JOIN fin_plano2 p2 ON p2.id_plano = p1.id
   JOIN fin_plano3 p3 ON p3.id_plano2 = p2.id
   JOIN fin_plano4 p4 ON p4.id_plano3 = p3.id
   JOIN fin_plano5 p5 ON p5.id_plano4 = p4.id
  ORDER BY p5.ds_classificador, p5.id;

ALTER TABLE plano_vw
  OWNER TO postgres;


