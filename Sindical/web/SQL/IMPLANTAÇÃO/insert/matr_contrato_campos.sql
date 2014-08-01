-- matr_contrato_campos;

-- Módulo Escola (8)

-- DELETE FROM matr_contrato_campos;

INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 1,  'Nome do aluno',         '$aluno',               8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 1);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 2,  'CPF do Aluno',          '$cpfAluno',            8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 2);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 3,  'RG do aluno',           '$rgAluno',             8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 3);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 4,  'Nome do responsável',   '$responsavel',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 4);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 5,  'CPF do responsável',    '$cpfResponsavel',      8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 5);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 6,  'RG do responsável',     '$rgResponsavel',       8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 6);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 7,  'Curso',                 '$curso',               8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 7);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 8,  'Dia da semana',         '$diaSemana',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 8);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 9,  'Data inicial',          '$dataInicial',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 9);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 10, 'Data final',            '$dataFinal',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 10);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 11, 'Data por extenso',      '$dataExtenso',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 11);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 12, 'Valor da parcela',      '$valorParcela',        8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 12);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 13, 'Número de Parcelas',    '$parcelas',            8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 13);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 14, 'Dia Vencimento',        '$diaVencimento',       8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 14);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 15, 'Valor até o vencimento','$valorAteVencimento',  8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 15);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 16, 'Hora inicial',          '$horaInicial',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 16);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 17, 'Hora final',            '$horaFinal',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 17);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 18, 'Valor total',           '$valorTotal',          8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 18);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 19, 'Taxa',                  '$taxa',                8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 19);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 20, 'Matrícula',             '$matricula',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 20);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 21, 'Ano',                   '$ano',                 8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 21);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 22, 'Endereço aluno',        '$enderecoAluno',       8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 22);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 23, 'Bairro aluno',          '$bairroAluno',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 23);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 24, 'Cidade aluno',          '$cidadeAluno',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 24);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 25, 'Estado aluno',          '$estadoAluno',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 25);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 26, 'CEP aluno',             '$cepAluno',            8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 26);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 27, 'E-mail aluno',          '$emailAluno',          8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 27);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 28, 'Nascimento aluno',      '$alunoDataNascimento', 8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 28);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 29, 'Endereço responsável',  '$enderecoResponsavel', 8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 29);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 30, 'Bairro responsavel',    '$bairroResponsavel',   8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 30);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 31, 'Cidade responsavel',    '$cidadeResponsavel',   8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 31);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 32, 'Estado responsavel',    '$estadoResponsavel',   8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 32);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 33, 'CEP responsavel',       '$cepResponsavel',      8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 33);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 34, 'Data inicial extenso',  '$dataInicialExtenso',  8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 34);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 35, 'Data final extenso',    '$dataFinalExtenso',    8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 35);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 36, 'Meses',                 '$meses',               8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 36);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 37, 'Meses extenso',         '$mesesExtenso',        8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 37);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 38, 'Descrição',             '$descricao',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 38);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 39, 'Nascimento aluno',      '$nascimentoAluno',     8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 39);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 40, 'Lista valores',         '$listaValores',        8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 40);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 41, 'Lista valores com data','$listaValoresComData', 8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 41);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 42, 'E-mail responsável',    '$emailResponsavel',    8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 42);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 43, 'Telefones Responsável', '$telefonesResponsavel',8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 43);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 44, 'Telefones Aluno',       '$telefonesAluno',      8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 44);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 45, 'Estado Cívil Responsável',      '$estadoCivilResponsavel',      8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 45);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 46, 'Estado Cívil Aluno',            '$estadoCivilAluno',            8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 46);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 47, 'Local trabalho aluno',          '$localTrabalhoAluno',          8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 47);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 48, 'Local trabalho responsável',    '$localTrabalhoResponsavel',    8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 48);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 49, 'Data do vencimento da parcela', '$vencimentoParcela',           8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 49);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 50, 'Desconto',                      '$desconto',                    8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 50);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 51, 'Desconto por extenso',          '$descontoExtenso',             8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 51);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 52, 'Mes/Ano Inicial Extenso',       '$mesAnoInicalExtenso',         8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 52);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 53, 'Mes/Ano Final   Extenso',       '$mesAnoFinalExtenso',          8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 53);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 54, 'Sala',                          '$sala',                        8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 54);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 55, 'Data matrícula',                '$dataMatricula',               8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 55);
INSERT INTO matr_contrato_campos (id, ds_campo, ds_variavel, id_modulo) SELECT 56, 'Valor total com desconto',      '$valorTotalComDesconto',       8 WHERE NOT EXISTS ( SELECT id FROM matr_contrato_campos WHERE id = 56);
SELECT setval('matr_contrato_campos_id_seq', max(id)) FROM matr_contrato_campos;
