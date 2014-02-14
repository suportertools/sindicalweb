SELECT * 
FROM soc_socios_vw AS s
INNER JOIN pes_pessoa AS p ON p.id = s.codsocio
INNER JOIN pes_fisica AS f ON f.id_pessoa = p.id
WHERE (p.ds_documento = '387.371.318-77' AND UPPER(p.ds_nome) = 'ANDRESSA APARECIDA TELLES' AND dt_nascimento = '22/06/1992' AND UPPER(f.ds_rg) = '482275017' AND inativacao IS NULL AND p.ds_nome <> '' AND p.ds_documento <> '' AND f.ds_rg <> '')
OR (UPPER(p.ds_nome) = 'ANDRESSA APARECIDA TELLES' AND dt_nascimento = '22/06/1992' AND UPPER(f.ds_rg) = '482275017' AND inativacao IS NULL AND p.ds_nome <> '' AND f.ds_rg <> '')
OR (UPPER(p.ds_nome) = 'ANDRESSA APARECIDA TELLES' AND dt_nascimento = '22/06/1992' AND inativacao IS NULL AND p.ds_nome <> '')
OR (UPPER(p.ds_nome) = 'ANDRESSA APARECIDA TELLES' AND UPPER(f.ds_rg) = '482275017' AND inativacao IS NULL AND p.ds_nome <> '')
OR (UPPER(f.ds_rg) = '482275017' AND inativacao IS NULL AND p.ds_nome <> '' AND p.ds_nome <> '' AND f.ds_rg <> '')
OR (p.ds_documento = '387.371.318-77' AND inativacao IS NULL AND p.ds_documento <> '');