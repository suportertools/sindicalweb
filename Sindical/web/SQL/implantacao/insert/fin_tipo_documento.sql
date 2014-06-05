-- fin_tipo_documento 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 1,  'Nota Fiscal'              WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 1);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 2,  'Boleto'                   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 2);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 10, 'Transferência Bancária'   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 10);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 12, 'Recibo'                   WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 12);
INSERT INTO fin_tipo_documento (id, ds_descricao) SELECT 13, 'Extrato Geral'            WHERE NOT EXISTS ( SELECT id FROM fin_tipo_documento WHERE id = 13);
SELECT setval('fin_tipo_documento_id_seq', max(id)) FROM fin_tipo_documento;
