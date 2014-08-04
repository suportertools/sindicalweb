ALTER TABLE sis_email ALTER COLUMN ds_mensagem TYPE character varying(5000);

ALTER TABLE seg_registro ADD COLUMN SIS_EMAIL_MARKETING_RESPOSTA character varying(50);
