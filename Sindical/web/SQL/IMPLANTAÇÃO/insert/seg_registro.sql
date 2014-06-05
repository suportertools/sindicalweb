

INSERT INTO seg_registro (
            id, ds_mensagem_bloqueio_boleto_web, ds_url_path, ds_tipo_empresa, 
            ds_obs_ficha_social, ds_senha, meses_inadimplentes_agenda, bloquear_homologacao, 
            dias_bloqueia_atrasados_web, meses_inadimplentes_impressao_boletos, 
            is_email_autenticado, baixa_vencimento, is_senha_homologacao, 
            ds_documento_homologacao, ds_smtp, ds_forma_pagamento_homologacao, 
            ds_tipo_entidade, is_agendar_sem_horario_web, ds_email, is_enviar_email_anexo, 
            is_bloqueia_atrasados_web, dt_atualiza_homologacao, carteirinha_dependente, 
            id_filial)
    SELECT 1, null, null, '', 
            null, null, 0, null, 
            null, null, 
            false, null, false, 
            null, null, null, 
            null, false, null, false, 
            false, '1900-01-01', null, 
            1 WHERE NOT EXISTS ( SELECT id FROM seg_registro WHERE id = 1);