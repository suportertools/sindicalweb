ALTER TABLE SEG_USUARIO ADD CHECK (ds_login <> '');
ALTER TABLE PES_TIPO_ENDERECO ADD CHECK (ds_descricao <> '');
ALTER TABLE PES_TIPO_DOCUMENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_TIPO_DOCUMENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE PES_PROFISSAO ADD CHECK (ds_profissao <> '');
ALTER TABLE HOM_STATUS ADD CHECK (ds_descricao <> '');
ALTER TABLE HOM_DEMISSAO ADD CHECK (ds_descricao <> '');
ALTER TABLE PES_PORTE ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_TIPO_SERVICO ADD CHECK (ds_descricao <> '');
ALTER TABLE EVE_BANDA ADD CHECK (ds_descricao <> '');
ALTER TABLE SOC_MIDIA ADD CHECK (ds_descricao <> '');
ALTER TABLE HOM_STATUS ADD CHECK (ds_descricao <> '');
ALTER TABLE ESC_COMPONENTE_CURRICULAR ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_INDICE ADD CHECK (ds_descricao <> '');
ALTER TABLE LOG_GENERO ADD CHECK (ds_descricao <> '');
ALTER TABLE PES_TIPO_CENTRO_COMERCIAL ADD CHECK (ds_descricao <> '');
ALTER TABLE SOC_CONVENIO_GRUPO ADD CHECK (ds_descricao <> '');
ALTER TABLE CONV_MOTIVO_SUSPENCAO ADD CHECK (ds_descricao <> '');
ALTER TABLE ARR_GRUPO_CIDADE ADD CHECK (ds_descricao <> '');
ALTER TABLE AGE_GRUPO_AGENDA ADD CHECK (ds_descricao <> '');
ALTER TABLE SIS_SEMANA ADD CHECK (ds_descricao <> '');
ALTER TABLE SIS_PERIORO ADD CHECK (ds_descricao <> '');
ALTER TABLE SIS_MES ADD CHECK (ds_descricao <> '');
ALTER TABLE SEG_NIVEL ADD CHECK (ds_descricao <> '');
ALTER TABLE SEG_MODULO ADD CHECK (ds_descricao <> '');
ALTER TABLE SEG_EVENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE SEG_DEPARTAMENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_CONDICAO_PAGAMENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE AGE_TIPO_TELEFONE ADD CHECK (ds_descricao <> '');
ALTER TABLE ARR_CONVENCAO ADD CHECK (ds_descricao <> '');
ALTER TABLE ARR_MOTIVO_INATIVACAO ADD CHECK (ds_descricao <> '');
ALTER TABLE ARR_REPIS_STATUS ADD CHECK (ds_descricao <> '');
ALTER TABLE EVE_STATUS ADD CHECK (ds_descricao <> '');
ALTER TABLE SOC_MOTIVO_INATIVACAO ADD CHECK (ds_descricao <> '');
ALTER TABLE ATE_OPERACAO ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_COBRANCA_TIPO ADD CHECK (ds_descricao <> '');
ALTER TABLE FIN_TIPO_PAGAMENTO ADD CHECK (ds_descricao <> '');
ALTER TABLE EST_PRODUTO ADD CHECK (ds_descricao <> '');
ALTER TABLE EST_TIPO ADD CHECK (ds_descricao <> '');
ALTER TABLE EST_GRUPO ADD CHECK (ds_descricao <> '');
ALTER TABLE EST_SUBGRUPO ADD CHECK (ds_descricao <> '');
ALTER TABLE EST_UNIDADE ADD CHECK (ds_descricao <> '');
ALTER TABLE SIS_COR ADD CHECK (ds_descricao <> '');