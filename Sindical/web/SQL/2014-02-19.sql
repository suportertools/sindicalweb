ALTER TABLE seg_registro ADD COLUMN fin_dia_vencimento_cobranca integer;
-- UPDATE seg_registro SET fin_dia_vencimento_cobranca = 10;

ALTER TABLE seg_registro ADD COLUMN sis_email_resposta character varying(50);
-- UPDATE seg_registro SET sis_email_resposta = '';

ALTER TABLE eve_desc_evento DROP COLUMN id_servico_movimento;
