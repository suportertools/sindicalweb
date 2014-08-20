
-- fin_status 
-- Criate: 2013-08-02
-- Last edition: 2013-08-02 - by: Bruno Vieira

-- DELETE FROM fin_status;

INSERT INTO fin_status (id, ds_descricao) SELECT 1, 'EFETIVO'   WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 1);
INSERT INTO fin_status (id, ds_descricao) SELECT 2, 'PEDIDO'    WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 2);
INSERT INTO fin_status (id, ds_descricao) SELECT 3, 'BLOQUEADO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 3);
INSERT INTO fin_status (id, ds_descricao) SELECT 4, 'PROVISÃO'  WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 4);
INSERT INTO fin_status (id, ds_descricao) SELECT 5, 'ORÇAMENTO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 5);
INSERT INTO fin_status (id, ds_descricao) SELECT 6, 'TRANFERÊNCIA ENTRE CONTAS'    WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 6);
INSERT INTO fin_status (id, ds_descricao) SELECT 7, 'A DEPOSITAR' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 7);
INSERT INTO fin_status (id, ds_descricao) SELECT 8, 'DEPOSITADO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 8);
INSERT INTO fin_status (id, ds_descricao) SELECT 9, 'LIQUIDADO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 9);
INSERT INTO fin_status (id, ds_descricao) SELECT 10, 'DEVOLVIDO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 10);
INSERT INTO fin_status (id, ds_descricao) SELECT 11, 'SUSTADO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 11);
INSERT INTO fin_status (id, ds_descricao) SELECT 12, 'FECHAMENTO' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 12);
INSERT INTO fin_status (id, ds_descricao) SELECT 13, 'TRANSFERÊNCIA' WHERE NOT EXISTS ( SELECT id FROM fin_status WHERE id = 13);
SELECT setval('fin_status_id_seq', max(id)) FROM fin_status;

