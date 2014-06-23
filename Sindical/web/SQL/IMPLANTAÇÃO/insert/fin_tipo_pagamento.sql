-- fin_tipo_pagamento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM fin_tipo_pagamento;

INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 1, 'Nota Fiscal' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 1);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 2, 'Boleto' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 2);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 3, 'Dinheiro' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 3);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 4, 'Cheque' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 4);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 5, 'Cheque-Pré' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 5);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 6, 'Cartão de Crédito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 6);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 7, 'Cartão de Débito' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 7);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 8, 'Depósito Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 8);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 9, 'Doc Bancário' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 9);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 10, 'Trans. Bancária' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 10);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 11, 'Ticket' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 11);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 12, 'Recibo' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 12);
INSERT INTO fin_tipo_pagamento (id, ds_descricao) SELECT 13, 'Débito Automático' WHERE NOT EXISTS ( SELECT id FROM fin_tipo_pagamento WHERE id = 13);
SELECT setval('fin_tipo_pagamento_id_seq', max(id)) FROM fin_tipo_pagamento;

