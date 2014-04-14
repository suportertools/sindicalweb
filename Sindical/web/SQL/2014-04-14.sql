ALTER TABLE pes_juridica ADD COLUMN is_cobranca_escritorio boolean;
-- UPDATE pes_juridica SET is_cobranca_escritorio = false WHERE id_contabilidade is null;
-- UPDATE pes_juridica SET is_cobranca_escritorio = true WHERE id_contabilidade > 0;
