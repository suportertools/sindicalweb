-- NÃO EXECUTAR ANTES DE FALAR COMIGO - BRUNO

arr_patronal_cnae

DROP TABLE arr_patronal_cnae; 

ALTER TABLE arr_patronal DROP COLUMN id_convencao;

ALTER TABLE arr_patronal DROP COLUMN id_grupo_cidade;

ALTER TABLE arr_repis_movimento ADD COLUMN id_patronal integer;

ALTER TABLE arr_repis_movimento
  ADD CONSTRAINT fk_arr_repis_movimento_id_patronal FOREIGN KEY (id_patronal) REFERENCES arr_patronal (id)
   ON UPDATE NO ACTION ON DELETE NO ACTION;;
