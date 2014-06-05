
-- fin_condicao_pagamento 
-- Criate: 2013-08-07
-- Last edition: 2013-08-07 - by: Bruno Vieira

INSERT INTO fin_condicao_pagamento (id, ds_descricao) SELECT 1, 'A vista' WHERE NOT EXISTS ( SELECT id FROM fin_condicao_pagamento WHERE id = 1);
INSERT INTO fin_condicao_pagamento (id, ds_descricao) SELECT 2, 'A prazo' WHERE NOT EXISTS ( SELECT id FROM fin_condicao_pagamento WHERE id = 2);
SELECT setval('fin_condicao_pagamento_id_seq', max(id)) FROM fin_condicao_pagamento;

