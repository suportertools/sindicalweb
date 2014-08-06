ALTER TABLE sis_email_pessoa ADD COLUMN DS_HORA_SAIDA character varying(5);


-- Para atualizar a lista usar este script, caso os horários de saída estejam vazios
select 'update sis_email_pessoa set ds_hora_saida = @'|| ds_hora || '@ where id_email = '|| id || ';' from sis_email