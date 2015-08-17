package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class RelatorioSociosDBToplink extends DB implements RelatorioSociosDB {

    @Override
    public List listaEmpresaDoSocio() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select pe.juridica "
                    + "  from PessoaEmpresa pe"
                    + " where pe.fisica.id in "
                    + "(select f.id "
                    + "   from Fisica f, Socios s"
                    + "  where s.servicoPessoa.pessoa.id = f.pessoa.id "
                    + "  group by f.id) "
                    + " group by pe.juridica, pe.juridica.pessoa.nome order by pe.juridica.pessoa.nome");
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    public List listaCidadeDoSocio() {
        List result = new ArrayList();
        String textQuery = "";
        try {
            textQuery = "select c from Cidade c"
                    + " where c.id in (select e.cidade.id"
                    + "                  from Endereco e"
                    + "                 where e.id in (select pe.endereco.id"
                    + "                                  from PessoaEndereco pe"
                    + "                                 where pe.pessoa.id in (select s.servicoPessoa.pessoa.id"
                    + "                                                          from Socios s"
                    + "                                                         group by s.servicoPessoa.pessoa.id"
                    + "                                                       )"
                    + "                                   and pe.tipoEndereco.id = 1"
                    + "                                 group by pe.endereco.id"
                    + "                                )"
                    + "                 group by e.cidade.id"
                    + "                )";
            Query qry = getEntityManager().createQuery(textQuery);
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    @Override
    public List listaCidadeDaEmpresa() {
        List result = new ArrayList();
        String textQuery = "";
        try {
            textQuery = "select c from Cidade c"
                    + " where c.id in (select e.cidade.id"
                    + "                  from Endereco e"
                    + "                 where e.id in (select pe.endereco.id"
                    + "                                  from PessoaEndereco pe"
                    + "                                 where pe.pessoa.id in (select pem.juridica.pessoa.id "
                    + "  from PessoaEmpresa pem"
                    + " where pem.fisica.id in "
                    + "(select f.id "
                    + "   from Fisica f, Socios s"
                    + "  where s.servicoPessoa.pessoa.id = f.pessoa.id "
                    + "  group by f.id) "
                    + " group by pem.juridica.pessoa.id "
                    + ")"
                    + "                                   and pe.tipoEndereco.id = 5"
                    + "                                 group by pe.endereco.id"
                    + "                                )"
                    + "                 group by e.cidade.id"
                    + "                )";
            Query qry = getEntityManager().createQuery(textQuery);
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    @Override
    public List listaSPSocios() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select sp.servicos "
                    + "  from ServicoPessoa sp"
                    + " where sp.id in (select s.servicoPessoa.id "
                    + "                   from Socios s)"
                    + " group by sp.servicos");
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    public List listaSPAcademia() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select sp.servicos "
                    + "  from ServicoPessoa sp"
                    + " where sp.id in (select m.servicoPessoa.id "
                    + "                   from MatriculaAcademia m)"
                    + " group by sp.servicos");
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    @Override
    public List listaSPEscola() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select s "
                    + "  from Servicos s"
                    + " where s.id in (select mi.curso.id from MatriculaIndividual mi group by mi.curso.id)"
                    + "    or s.id in (select t.cursos.id from Turma t group by t.cursos.id)"
                    + " group by s");
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    @Override
    public List listaSPConvenioMedico() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select sp.servicos "
                    + "  from ServicoPessoa sp"
                    + " where sp.id in (select m.servicoPessoa.id "
                    + "                   from MatriculaConvenioMedico m)"
                    + " group by sp.servicos");
            result = qry.getResultList();
        } catch (EJBQLException e) {
        }
        return result;
    }

    @Override
    public List pesquisaSocios(Relatorios relatorio, boolean booMatricula, int matricula_inicial, int matricula_final, boolean booIdade, int idade_inicial, int idade_final, boolean booGrupo, String ids_gc, String ids_c,
            boolean booSexo, String tipo_sexo, boolean booGrau, String ids_parentesco, boolean booFoto, String tipo_foto, boolean booCarteirinha, String tipo_carteirinha,
            boolean booTipoPagamento, String ids_pagamento, boolean booCidadeSocio, String ids_cidade_socio, boolean booCidadeEmpresa, String ids_cidade_empresa,
            boolean booAniversario, String meses_aniversario, String dia_inicial, String dia_final, boolean ordemAniversario, boolean booData, String dt_cadastro, String dt_cadastro_fim, String dt_recadastro,
            String dt_recadastro_fim, String dt_demissao, String dt_demissao_fim, String dt_admissao_socio, String dt_admissao_socio_fim, String dt_admissao_empresa, String dt_admissao_empresa_fim, boolean booVotante, String tipo_votante,
            boolean booEmail, String tipo_email, boolean booTelefone, String tipo_telefone, boolean booEstadoCivil, String tipo_estado_civil, boolean booEmpresas, String tipo_empresa, int id_juridica, Integer minQtdeFuncionario, Integer maxQtdeFuncionario,
            String data_aposentadoria, String data_aposentadoria_fim, String ordem, String tipoCarencia, Integer carenciaDias, String situacao, boolean booBiometria, String tipoBiometria, boolean booDescontoFolha, String tipoDescontoFolha,
            String data_atualizacao, String data_atualizacao_fim) {

        String p_demissao = "";
        if (booData && !dt_demissao.isEmpty() && !dt_demissao_fim.isEmpty()) {
            p_demissao = " , pempresa.admissao_empresa_demissionada, \n" + // 79
                    "   pempresa.demissao_empresa_demissionada, \n" + // 80
                    "   pempresa.cnpj_empresa_demissionada, \n" + // 81
                    "   pempresa.empresa_demissionada \n "; // 82
        } else {
            p_demissao = " , null AS admissao_empresa_demissionada, \n" + // 79
                    "   null AS demissao_empresa_demissionada, \n" + // 80
                    "   null AS cnpj_empresa_demissionada, \n" + // 81
                    "   null AS empresa_demissionada \n "; // 82
        }

        String textQry = ""
                + "SELECT "
                + "           ''                  AS sindLogo,                  \n " // 0
                + "           sind.jurSite        AS sindSite,                  \n " // 1
                + "           sind.jurNome        AS sinnome,                   \n " // 2
                + "           sind.jurEndereco    AS sinendereco,               \n " // 3
                + "           sind.jurLogradouro  AS sinlogradouro,             \n " // 4
                + "           sind.jurNumero      AS sinnumero,                 \n " // 5
                + "           sind.jurComplemento AS sincomplemento,            \n " // 6
                + "           sind.jurBairro      AS sinbairro,                 \n " // 7
                + "           substring(sind.jurCep,1,5)||'-'||substring(sind.jurCep,6,3)  AS sincep,   \n "
                + "           sind.jurCidade      AS sincidade,                 \n " // 9
                + "           sind.jurUf          AS sinuF,                     \n " // 10
                + "           sind.jurDocumento   AS sindocumento,              \n " // 11
                + "           p.codigo,                                         \n " // 12
                + "           p.cadastro,                                       \n " // 13
                + "           p.nome,                                           \n " // 14
                + "           p.cpf,                                            \n " // 15
                + "           p.telefone,                                       \n " // 16
                + "           p.ds_uf_emissao_rg,                               \n " // 17
                + "           p.estado_civil,                                   \n " // 18
                + "           p.ctps,                                           \n " // 19
                + "           p.pai,                                            \n " // 20
                + "           p.sexo,                                           \n " // 21
                + "           p.mae,                                            \n " // 22
                + "           p.nacionalidade,                                  \n " // 23
                + "           p.nit,                                            \n " // 24
                + "           p.ds_orgao_emissao_rg,                            \n " // 25
                + "           p.ds_pis,                                         \n " // 26
                + "           p.ds_serie,                                       \n " // 27
                + "           p.dt_aposentadoria,                               \n " // 28
                + "           p.ds_naturalidade,                                \n " // 29
                + "           p.recadastro,                                     \n " // 30
                + "           p.dt_nascimento,                                  \n " // 31
                + "           p.dt_foto,                                        \n " // 32
                + "           p.ds_rg,                                          \n " // 33
                + "           foto,                                             \n " // 34
                + "           p.logradouro,                                     \n " // 35
                + "           p.endereco,                                       \n " // 36
                + "           p.numero,                                         \n " // 37
                + "           p.complemento,                                    \n " // 38
                + "           p.bairro,                                         \n " // 39
                + "           p.cidade,                                         \n " // 40
                + "           p.uf,                                             \n " // 41
                + "           p.cep,                                            \n " // 42
                + "           p.setor,                                          \n " // 43
                + "           p.admissao,                                       \n " // 44
                + "           p.profissao,                                      \n " // 45
                + "           p.fantasia,                                       \n " // 46
                + "           p.empresa,                                        \n " // 47
                + "           p.cnpj,                                           \n " // 48
                + "           p.e_telefone,                                     \n " // 49
                + "           p.e_logradouro,                                   \n " // 50
                + "           p.e_endereco,                                     \n " // 51
                + "           p.e_numero,                                       \n " // 52
                + "           p.e_complemento,                                  \n " // 53
                + "           p.e_bairro,                                       \n " // 54
                + "           p.e_cidade,                                       \n " // 55
                + "           p.e_uf,                                           \n " // 56
                + "           p.e_cep,                                          \n " // 57
                + "           titular,                                          \n " // 58
                + "           so.codsocio,                                      \n " // 59
                + "           pt.ds_nome as titular,                            \n " // 60
                + "           so.parentesco,                                    \n " // 61
                + "           so.matricula,                                     \n " // 62
                + "           so.categoria,                                     \n " // 63
                + "           so.grupo_categoria,                               \n " // 64
                + "           so.filiacao,                                      \n " // 65
                + "           so.inativacao,                                    \n " // 66
                + "           so.votante,                                       \n " // 67
                + "           so.grau,                                          \n " // 68
                + "           so.nr_desconto,                                   \n " // 69
                + "           so.desconto_folha,                                \n " // 70
                + "           so.tipo_cobranca,                                 \n " // 71
                + "           so.cod_tipo_cobranca,                             \n " // 72
                + "           p.telefone2,                                      \n " // 73
                + "           p.telefone3,                                      \n " // 74           
                + "           p.email,                                          \n " // 75
                + "           PC.ds_nome,                                       \n " // 76
                + "           PJC.ds_contato,                                   \n " // 77
                + "           PC.ds_telefone1                                   \n " // 78
                + "           " + p_demissao + ",                                   \n "
                + "           func_idade(p.dt_nascimento,CURRENT_DATE) AS idade \n " // 83
                + "      FROM pes_pessoa_vw      AS p                           \n "
                + "INNER JOIN soc_socios_vw      AS so   ON so.codsocio     = p.codigo              \n "
                + "INNER JOIN pes_juridica_vw    AS sind ON sind.id_pessoa  = 1                     \n "
                + "INNER JOIN pes_pessoa         AS pt   ON pt.id           = so.titular            \n "
                + " LEFT JOIN pes_juridica       AS J    ON J.id            = P.e_id                \n "
                + " LEFT JOIN pes_juridica       AS PJC  ON PJC.id          = J.id_contabilidade    \n "
                + " LEFT JOIN pes_pessoa         AS PC   ON PC.id           = PJC.id_pessoa         \n ";

        String filtro = "";
        String innerjoin = "";

        if (relatorio.getQry() == null || relatorio.getQry().isEmpty()) {
            filtro = " WHERE ";
        } else {
            filtro = " WHERE " + relatorio.getQry() + " AND ";
        }
        // MATRICULA --------------------
        if (booMatricula) {
            filtro += " so.matricula >= " + matricula_inicial + " AND so.matricula <= " + matricula_final + " \n ";
        } else {
            filtro += " so.matricula >= 0 AND so.matricula <= 9999999" + " \n ";
        }

        //filtro += relatorio.getQry(); 
        // IDADE ------------
        if (booIdade) {
            filtro += " AND extract(year FROM age(p.dt_nascimento)) >= " + idade_inicial + " AND extract(year FROM age(p.dt_nascimento)) <= " + idade_final + " \n ";
        }

        // GRUPO CATEGORIA ----------------
        if (booGrupo) {
            if (!ids_gc.isEmpty()) {
                filtro += " AND so.id_grupo_categoria IN(" + ids_gc + ")" + " \n ";
            }

            if (!ids_gc.isEmpty()) {
                filtro += " AND so.id_categoria IN(" + ids_c + ")" + " \n ";
            }
        }

        // SEXO --------------------
        if (booSexo) {
            filtro += " AND p.sexo = '" + tipo_sexo + "'" + " \n ";
        }

        // PARENTESCO ------------------
        if (booGrau) {
            if (!ids_parentesco.isEmpty()) {
                filtro += " AND so.id_parentesco IN(" + ids_parentesco + ")" + " \n ";
            }
        }

        if (booFoto) {
            if (tipo_foto.equals("com")) {
                filtro += " AND p.dt_foto IS NOT NULL" + " \n ";
            } else if (tipo_foto.equals("sem")) {
                filtro += " AND p.dt_foto IS NULL" + " \n ";
            }
        }

        if (booCarteirinha) {
            if (tipo_carteirinha.equals("com")) {
                filtro += " AND so.codsocio IN(SELECT sc.id_pessoa FROM soc_carteirinha AS sc GROUP BY sc.id_pessoa)" + " \n ";
            } else if (tipo_carteirinha.equals("sem")) {
                filtro += " AND so.codsocio NOT IN(SELECT sc.id_pessoa FROM soc_carteirinha AS sc GROUP BY sc.id_pessoa)" + " \n ";
            }
        }

        if (booTipoPagamento) {
            filtro += " AND so.cod_tipo_cobranca IN(" + ids_pagamento + ")" + " \n ";
        }

        if (booCidadeSocio) {
            filtro += " AND p.id_cidade IN(" + ids_cidade_socio + ")" + " \n ";
        }

        if (booCidadeEmpresa) {
            filtro += " AND p.e_id_cidade IN(" + ids_cidade_empresa + ")" + " \n ";
        }

        if (booAniversario) {
            filtro += " AND p.codigo IN (SELECT id_pessoa FROM pes_fisica WHERE ";
            filtro += "     extract(month from dt_nascimento) IN (" + meses_aniversario + ")" + " \n ";

            filtro += " AND extract(day from dt_nascimento) >= " + Integer.valueOf(dia_inicial) + " \n "
                    + " AND extract(day from dt_nascimento) <= " + Integer.valueOf(dia_final) + " \n ";

            filtro += " ) ";
        }

        if (booData) {
            // DATA DE CADASTRO ---------------
            if (!dt_cadastro.isEmpty() && !dt_cadastro_fim.isEmpty()) {
                filtro += " AND p.cadastro >= '" + dt_cadastro + "' AND p.cadastro <= '" + dt_cadastro_fim + "'" + " \n ";
            } else if (!dt_cadastro.isEmpty()) {
                filtro += " AND p.cadastro = '" + dt_cadastro + "'" + " \n ";
            }

            // DATA DE RECADASTRO -------------
            if (!dt_recadastro.isEmpty() && !dt_recadastro_fim.isEmpty()) {
                filtro += " AND p.recadastro >= '" + dt_recadastro + "' AND p.recadastro <= '" + dt_recadastro_fim + "'" + " \n ";
            } else if (!dt_recadastro.isEmpty()) {
                filtro += " AND p.recadastro = '" + dt_recadastro + "'" + " \n ";
            }

            // DATA DE DEMISSAO ----------------
            if (!dt_demissao.isEmpty() && !dt_demissao_fim.isEmpty()) {
                innerjoin += " INNER JOIN (SELECT id_fisica, pe.dt_admissao AS admissao_empresa_demissionada, dt_demissao AS demissao_empresa_demissionada, p.ds_documento as cnpj_empresa_demissionada, p.ds_nome as empresa_demissionada \n"
                        + "               FROM pes_pessoa_empresa pe \n "
                        + "              INNER JOIN pes_juridica j ON j.id = pe.id_juridica \n "
                        + "              INNER JOIN pes_pessoa p ON p.id = j.id_pessoa \n "
                        + "              WHERE pe.dt_demissao >= '" + dt_demissao + "' \n "
                        + "                AND pe.dt_demissao <= '" + dt_demissao_fim + "' \n "
                        + " ) AS pempresa ON pempresa.id_fisica = p.id_fisica \n ";
            }

            // DATA DE FILIACAO DO SOCIO ----------------
            if (!dt_admissao_socio.isEmpty() && !dt_admissao_socio_fim.isEmpty()) {
                filtro += " AND so.filiacao >= '" + dt_admissao_socio + "' AND so.filiacao <= '" + dt_admissao_socio_fim + "'" + " \n ";
            } else if (!dt_admissao_socio.isEmpty()) {
                filtro += " AND so.filiacao = '" + dt_admissao_socio + "'" + " \n ";
            }

            // DATA DE ADMISSAO EMPRESA
            if (!dt_admissao_empresa.isEmpty() && !dt_admissao_empresa_fim.isEmpty()) {
                filtro += " AND p.admissao >= '" + dt_admissao_empresa + "' AND p.admissao <= '" + dt_admissao_empresa_fim + "'" + " \n ";
            } else if (!dt_admissao_empresa.isEmpty()) {
                filtro += " AND p.admissao = '" + dt_admissao_empresa + "'" + " \n ";
            }

            // DATA APOSENTADORIA
            if (!data_aposentadoria.isEmpty() && !data_aposentadoria_fim.isEmpty()) {
                filtro += " AND p.dt_aposentadoria >= '" + data_aposentadoria + "' AND p.dt_aposentadoria <= '" + data_aposentadoria_fim + "'" + " \n ";
            } else if (!data_aposentadoria.isEmpty()) {
                filtro += " AND p.dt_aposentadoria = '" + data_aposentadoria + "'" + " \n ";
            }

            // DATA ATUALIZAÇÃO
            if (!data_atualizacao.isEmpty() && !data_atualizacao_fim.isEmpty()) {
                filtro += " AND p.dt_atualizacao >= '" + data_atualizacao + "' AND p.dt_atualizacao <= '" + data_atualizacao_fim + "'" + " \n ";
            } else if (!data_atualizacao.isEmpty()) {
                filtro += " AND p.dt_atualizacao = '" + data_atualizacao + "'" + " \n ";
            }

        } else {
            filtro += " AND (p.principal = true OR p.principal IS NULL) " + " \n ";
            //filtro += " and p.demissao is null ";
        }

        if (booVotante) {
            if (tipo_votante.equals("votante")) {
                filtro += " AND so.votante = true ";
            } else if (tipo_votante.equals("naoVotante")) {
                filtro += " AND so.votante = false ";
            }
        }

        if (booEmail) {
            if (tipo_email.equals("com")) {
                filtro += " AND p.email <> '' ";
            } else if (tipo_email.equals("sem")) {
                filtro += " AND p.email = '' ";
            }
        }

        if (booTelefone) {
            if (tipo_telefone.equals("com")) {
                filtro += " AND p.telefone <> '' ";
            } else if (tipo_telefone.equals("sem")) {
                filtro += " AND p.telefone = '' ";
            }
        }

        if (booEstadoCivil) {
            filtro += " AND p.estado_civil '" + tipo_estado_civil + "'";
        }

        if (booEmpresas) {
            if (id_juridica != -1) {
                filtro += " AND p.e_id = " + id_juridica;
            } else if (tipo_empresa != null && tipo_empresa.equals("com")) {
                filtro += " AND p.empresa <> '' ";
            } else if (tipo_empresa != null && tipo_empresa.equals("sem")) {
                filtro += " AND p.empresa IS NULL ";
            }
            if (minQtdeFuncionario != null && maxQtdeFuncionario != null && (minQtdeFuncionario > 0 || maxQtdeFuncionario > 0)) {
                filtro += " AND p.e_id IN (SELECT pempre.e_id "
                        + "         FROM soc_socios_vw socp  "
                        + " INNER JOIN pes_pessoa_vw pempre ON pempre.codigo = socp.codsocio    "
                        + " WHERE pempre.e_id > 0  "
                        + " GROUP BY pempre.e_id  ";
                if (minQtdeFuncionario.equals(maxQtdeFuncionario)) {
                    filtro += " HAVING COUNT(*) = " + minQtdeFuncionario + ") ";
                } else if (minQtdeFuncionario > 0 && maxQtdeFuncionario == 0) {
                    filtro += " HAVING COUNT(*) <= " + minQtdeFuncionario + ") ";
                } else if (minQtdeFuncionario == 0 && maxQtdeFuncionario > 0) {
                    filtro += " HAVING COUNT(*) >= " + maxQtdeFuncionario + ") ";
                } else if (minQtdeFuncionario > 0 && maxQtdeFuncionario > 0) {
                    filtro += " HAVING COUNT(*) >= " + minQtdeFuncionario + " AND COUNT(*) <= " + maxQtdeFuncionario + ") ";
                }
            }
        }

        if (carenciaDias != null && carenciaDias >= 0) {
            Boolean s = false;
            if (situacao != null) {
                s = !situacao.equals("adimplente");
            }
            switch (tipoCarencia) {
                case "todos":
                    filtro += " AND func_inadimplente_todos(so.codsocio, " + carenciaDias + ") = " + s;
                    break;
                case "eleicao":
                    filtro += " AND func_inadimplente_eleicao(so.codsocio, " + carenciaDias + ") = " + s;
                    break;
                case "clube":
                    filtro += " AND func_inadimplente_clube(so.codsocio, " + carenciaDias + ") = " + s;
                    break;
            }
        }

        if (booBiometria) {
            if (tipoBiometria.equals("com")) {
                filtro += " AND p.codigo IN (SELECT id_pessoa FROM pes_biometria WHERE is_ativo = TRUE) ";
            } else {
                filtro += " AND p.codigo NOT IN (SELECT id_pessoa FROM pes_biometria WHERE is_ativo = TRUE) ";
            }
        }

        if (booDescontoFolha) {
            if (tipoDescontoFolha.equals("com")) {
                filtro += " AND so.desconto_folha = true ";
            } else {
                filtro += " AND so.desconto_folha = false ";
            }
        }

        String tordem = "";
        
        if (ordemAniversario){
            tordem += " p.dt_nascimento ";
        }
        
        if (ordem != null) {
            if (ordem.equals("nome")) {
                tordem += tordem.isEmpty() ? " p.nome " : ", p.nome ";
            } else if (ordem.equals("matricula")) {
                tordem += tordem.isEmpty() ? " so.matricula " : ", so.matricula ";
            } else if (ordem.equals("cep")) {
                tordem += tordem.isEmpty() ? " p.cep " : ", p.cep ";
            } else if (ordem.equals("endereco")) {
                tordem += tordem.isEmpty() ? " p.logradouro, p.endereco, p.numero, p.bairro " : ", p.logradouro, p.endereco, p.numero, p.bairro ";
            }
        }

        // ORDEM DA QRY
        if (relatorio.getQryOrdem() == null || relatorio.getQryOrdem().isEmpty()) {
            filtro += " ORDER BY " + tordem;
        } else {
            filtro += " ORDER BY " + relatorio.getQryOrdem();
            if (!tordem.isEmpty()) {
                filtro += ", " + tordem;
            }
        }
        try {
            String queryString = textQry + innerjoin + filtro;
            Query qry = getEntityManager().createNativeQuery(queryString);
//            String novaQuery = textQry + filtro;
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Vector> listaSociosInativos(boolean comDependentes, boolean chkInativacao, boolean chkFiliacao, String dt_inativacao_i, String dt_inativacao_f, String dt_filiacao_i, String dt_filiacao_f, int categoria, int grupoCategoria, String ordernarPor
    ) {

        String select = "", innerjoin = "", textQry = "", and = "", orderby = "", ordem = "";

        if (comDependentes) {
            select = "SELECT p.ds_nome as titular, s.titular as codtitular, s.codsocio, s.nome, s.parentesco, s.matricula, s.categoria, s.filiacao, s.inativacao, s.motivo_inativacao, s.id_categoria, s.id_grupo_categoria ";
            innerjoin = " INNER JOIN pes_pessoa as p on p.id = s.titular ";
            orderby = " p.ds_nome, s.titular, s.categoria, s.matricula, s.id_parentesco, s.nome, s.parentesco ";

            if (ordernarPor.equals("matricula")) {
                ordem = "  s.matricula, ";
            } else if (ordernarPor.equals("categoria")) {
                ordem = "  s.categoria, ";
            } else if (ordernarPor.equals("inativacao")) {
                ordem = "  s.inativacao DESC, ";
            } else if (ordernarPor.equals("filiacao")) {
                ordem = "  s.filiacao DESC, ";
            }

        } else {
            select = "SELECT s.nome as titular, s.codsocio as codtitular, s.codsocio, s.nome, s.parentesco, s.matricula, s.categoria, s.filiacao, s.inativacao, s.motivo_inativacao, s.id_categoria, s.id_grupo_categoria ";
            and = " WHERE s.parentesco = 'TITULAR' ";
            orderby = " s.inativacao DESC ";

            if (ordernarPor.equals("filiacao")) {
                ordem = " s.filiacao DESC, ";
            } else if (ordernarPor.equals("nome")) {
                ordem = " s.nome, ";
            } else if (ordernarPor.equals("matricula")) {
                ordem = "  s.matricula, ";
            } else if (ordernarPor.equals("categoria")) {
                ordem = "  s.categoria, ";
            }
        }

        orderby = " ORDER BY " + ordem + orderby;

        if (chkInativacao) {
            if (!dt_inativacao_i.isEmpty() && dt_inativacao_f.isEmpty()) {
                and += " AND s.inativacao >= '" + dt_inativacao_i + "'";
            } else if (dt_inativacao_i.isEmpty() && !dt_inativacao_f.isEmpty()) {
                and += " AND s.inativacao <= '" + dt_inativacao_f + "'";
            } else if (!dt_inativacao_i.isEmpty() && !dt_inativacao_f.isEmpty()) {
                and += " AND s.inativacao >= '" + dt_inativacao_i + "' AND s.inativacao <= '" + dt_inativacao_f + "'";
            }
        }

        if (chkFiliacao) {
            if (!dt_filiacao_i.isEmpty() && dt_filiacao_f.isEmpty()) {
                and += " AND s.filiacao >= '" + dt_filiacao_i + "'";
            } else if (dt_filiacao_i.isEmpty() && !dt_filiacao_f.isEmpty()) {
                and += " AND s.filiacao <= '" + dt_filiacao_f + "'";
            } else if (!dt_filiacao_i.isEmpty() && !dt_filiacao_f.isEmpty()) {
                and += " AND s.filiacao >= '" + dt_filiacao_i + "' AND s.filiacao <= '" + dt_filiacao_f + "'";
            }
        }

        if (categoria != -1) {
            and += " AND s.id_categoria = " + categoria;
        }

        if (grupoCategoria != -1) {
            and += " AND s.id_grupo_categoria = " + grupoCategoria;
        }

        textQry = select
                + "  FROM soc_socios_inativos_vw s"
                + innerjoin
                + and
                + orderby;

        try {
            Query qry = getEntityManager().createNativeQuery(textQry);
            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
