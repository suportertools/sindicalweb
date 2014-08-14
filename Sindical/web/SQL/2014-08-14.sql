
ALTER TABLE soc_motivo_inativacao ALTER COLUMN ds_descricao TYPE character varying(150);

ALTER TABLE fin_servicos ADD COLUMN id_periodo integer;

ALTER TABLE fin_servicos ADD COLUMN nr_qtde_periodo integer;

ALTER TABLE fin_servicos
  ADD CONSTRAINT fk_fin_servicos_id_periodo FOREIGN KEY (id_periodo)
      REFERENCES sis_periodo (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;