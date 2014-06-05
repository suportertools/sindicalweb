ALTER TABLE soc_convenio_servico ADD COLUMN is_encaminhamento boolean default false;
ALTER TABLE fin_guia ADD COLUMN is_encaminhamento boolean default false;
ALTER TABLE fin_guia ADD COLUMN id_convenio_sub_grupo integer;

UPDATE soc_convenio_servico SET is_encaminhamento = FALSE;
UPDATE fin_guia SET is_encaminhamento = FALSE;


create  VIEW soc_convenio_vw AS 
select p.id id_pessoa, j.id as id_juridica,g.id as id_grupo, s.id as id_subgrupo,g.ds_descricao as ds_grupo,s.ds_descricao as ds_subgrupo, p.ds_nome, 
e.logradouro,e.endereco,pe.ds_numero numero,pe.ds_complemento as complemento,e.bairro,e.cidade,e.uf,e.cep
from soc_convenio as c 
inner join pes_juridica as j on j.id=c.id_juridica
inner join pes_pessoa as p on p.id=j.id_pessoa
inner join pes_pessoa_endereco as pe on pe.id_pessoa=p.id and pe.id_tipo_endereco=2
inner join endereco_vw as e on e.id=pe.id_endereco 
inner join soc_convenio_sub_grupo as s on s.id=c.id_convenio_sub_grupo
inner join soc_convenio_grupo as g on g.id=s.id_grupo_convenio