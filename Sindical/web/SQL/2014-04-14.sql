ALTER TABLE pes_juridica ADD COLUMN is_cobranca_escritorio boolean;
-- UPDATE pes_juridica SET is_cobranca_escritorio = false WHERE id_contabilidade is null;
-- UPDATE pes_juridica SET is_cobranca_escritorio = true WHERE id_contabilidade > 0;

-- fin_previsao_pagamento

INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 13, 'Débito Automático' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 13);

