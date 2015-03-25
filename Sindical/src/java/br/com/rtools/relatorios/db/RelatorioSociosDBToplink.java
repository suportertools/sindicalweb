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
            boolean booAniversario, String meses_aniversario, String dia_inicial, String dia_final, boolean booData, String dt_cadastro, String dt_cadastro_fim, String dt_recadastro,
            String dt_recadastro_fim, String dt_demissao, String dt_demissao_fim, String dt_admissao_socio, String dt_admissao_socio_fim, String dt_admissao_empresa, String dt_admissao_empresa_fim, boolean booVotante, String tipo_votante,
            boolean booEmail, String tipo_email, boolean booTelefone, String tipo_telefone, boolean booEstadoCivil, String tipo_estado_civil, boolean booEmpresas, String tipo_empresa, int id_juridica, String data_aposentadoria, String data_aposentadoria_fim, String ordem, String tipoCarencia, Integer carenciaDias) {
        String textQry = ""
                + "SELECT "
                + "           ''                  AS sindLogo,                  " // 0
                + "           sind.jurSite        AS sindSite,                  " // 1
                + "           sind.jurNome        AS sinnome,                   " // 2
                + "           sind.jurEndereco    AS sinendereco,               " // 3
                + "           sind.jurLogradouro  AS sinlogradouro,             " // 4
                + "           sind.jurNumero      AS sinnumero,                 " // 5
                + "           sind.jurComplemento AS sincomplemento,            " // 6
                + "           sind.jurBairro      AS sinbairro,                 " // 7
                + "           substring(sind.jurCep,1,5)||'-'||substring(sind.jurCep,6,3)  AS sincep,   "
                + "           sind.jurCidade      AS sincidade,                 " // 9
                + "           sind.jurUf          AS sinuF,                     " // 10
                + "           sind.jurDocumento   AS sindocumento,              " // 11
                + "           p.codigo,                                         " // 12
                + "           p.cadastro,                                       " // 13
                + "           p.nome,                                           " // 14
                + "           p.cpf,                                            " // 15
                + "           p.telefone,                                       " // 16
                + "           p.ds_uf_emissao_rg,                               " // 17
                + "           p.estado_civil,                                   " // 18
                + "           p.ctps,                                           " // 19
                + "           p.pai,                                            " // 20
                + "           p.sexo,                                           " // 21
                + "           p.mae,                                            " // 22
                + "           p.nacionalidade,                                  " // 23
                + "           p.nit,                                            " // 24
                + "           p.ds_orgao_emissao_rg,                            " // 25
                + "           p.ds_pis,                                         " // 26
                + "           p.ds_serie,                                       " // 27
                + "           p.dt_aposentadoria,                               " // 28
                + "           p.ds_naturalidade,                                " // 29
                + "           p.recadastro,                                     " // 30
                + "           p.dt_nascimento,                                  " // 31
                + "           p.dt_foto,                                        " // 32
                + "           p.ds_rg,                                          " // 33
                + "           foto,                                             " // 34
                + "           p.logradouro,                                     " // 35
                + "           p.endereco,                                       " // 36
                + "           p.numero,                                         " // 37
                + "           p.complemento,                                    " // 38
                + "           p.bairro,                                         " // 39
                + "           p.cidade,                                         " // 40
                + "           p.uf,                                             " // 41
                + "           p.cep,                                            " // 42
                + "           p.setor,                                          " // 43
                + "           p.admissao,                                       " // 44
                + "           p.profissao,                                      " // 45
                + "           p.fantasia,                                       " // 46
                + "           p.empresa,                                        " // 47
                + "           p.cnpj,                                           " // 48
                + "           p.e_telefone,                                     " // 49
                + "           p.e_logradouro,                                   " // 50
                + "           p.e_endereco,                                     " // 51
                + "           p.e_numero,                                       " // 52
                + "           p.e_complemento,                                  " // 53
                + "           p.e_bairro,                                       " // 54
                + "           p.e_cidade,                                       " // 55
                + "           p.e_uf,                                           " // 56
                + "           p.e_cep,                                          " // 57
                + "           titular,                                          " // 58
                + "           so.codsocio,                                      " // 59
                + "           pt.ds_nome as titular,                            " // 60
                + "           so.parentesco,                                    " // 61
                + "           so.matricula,                                     " // 62
                + "           so.categoria,                                     " // 63
                + "           so.grupo_categoria,                               " // 64
                + "           so.filiacao,                                      " // 65
                + "           so.inativacao,                                    " // 66
                + "           so.votante,                                       " // 67
                + "           so.grau,                                          " // 68
                + "           so.nr_desconto,                                   " // 69
                + "           so.desconto_folha,                                " // 70
                + "           so.tipo_cobranca,                                 " // 71
                + "           so.cod_tipo_cobranca,                             " // 72
                + "           p.telefone2,                                      " // 73
                + "           p.telefone3,                                      " // 74           
                + "           p.email,                                          " // 75
                + "           PC.ds_nome,                                       " // 76
                + "           PJC.ds_contato,                                   " // 77
                + "           PC.ds_telefone1                                   " // 78
                + "      FROM pes_pessoa_vw      AS p                           "
                + "INNER JOIN soc_socios_vw      AS so   ON so.codsocio     = p.codigo              "
                + "INNER JOIN pes_juridica_vw    AS sind ON sind.id_pessoa  = 1                     "
                + "INNER JOIN pes_pessoa         AS pt   ON pt.id           = so.titular            "
                + " LEFT JOIN pes_juridica       AS J    ON J.id            = P.e_id                "
                + " LEFT JOIN pes_juridica       AS PJC  ON PJC.id          = J.id_contabilidade    "
                + " LEFT JOIN pes_pessoa         AS PC   ON PC.id           = PJC.id_pessoa         ";

        String filtro = "";

        if (relatorio.getQry() == null || relatorio.getQry().isEmpty()) {
            filtro = " WHERE ";
        } else {
            filtro = " WHERE " + relatorio.getQry() + " AND ";
        }
        // MATRICULA --------------------
        if (booMatricula) {
            filtro += " so.matricula >= " + matricula_inicial + " AND so.matricula <= " + matricula_final;
        } else {
            filtro += " so.matricula >= 0 AND so.matricula <= 9999999";
        }

        //filtro += relatorio.getQry(); 
        // IDADE ------------
        if (booIdade) {
            filtro += " AND extract(year FROM age(p.dt_nascimento)) >= " + idade_inicial + " AND extract(year FROM age(p.dt_nascimento)) <= " + idade_final;
        }

        // GRUPO CATEGORIA ----------------
        if (booGrupo) {
            if (!ids_gc.isEmpty()) {
                filtro += " AND so.id_grupo_categoria IN(" + ids_gc + ")";
            }

            if (!ids_gc.isEmpty()) {
                filtro += " AND so.id_categoria IN(" + ids_c + ")";
            }
        }

        // SEXO --------------------
        if (booSexo) {
            filtro += " AND p.sexo = '" + tipo_sexo + "'";
        }

        // PARENTESCO ------------------
        if (booGrau) {
            if (!ids_parentesco.isEmpty()) {
                filtro += " AND so.id_parentesco IN(" + ids_parentesco + ")";
            }
        }

        if (booFoto) {
            if (tipo_foto.equals("com")) {
                filtro += " AND p.dt_foto IS NOT NULL";
            } else if (tipo_foto.equals("sem")) {
                filtro += " AND p.dt_foto IS NULL";
            }
        }

        if (booCarteirinha) {
            if (tipo_carteirinha.equals("com")) {
                filtro += " AND so.codsocio IN(SELECT sc.id_pessoa FROM soc_carteirinha AS sc GROUP BY sc.id_pessoa)";
            } else if (tipo_carteirinha.equals("sem")) {
                filtro += " AND so.codsocio NOT IN(SELECT sc.id_pessoa FROM soc_carteirinha AS sc GROUP BY sc.id_pessoa)";
            }
        }

        if (booTipoPagamento) {
            filtro += " AND so.cod_tipo_cobranca IN(" + ids_pagamento + ")";
        }

        if (booCidadeSocio) {
            filtro += " AND p.id_cidade IN(" + ids_cidade_socio + ")";
        }

        if (booCidadeEmpresa) {
            filtro += " AND p.e_id_cidade IN(" + ids_cidade_empresa + ")";
        }

        if (booAniversario) {
            filtro += " AND substring(text(p.dt_nascimento),6,2) IN('" + meses_aniversario + "')";

            filtro += " AND substring(text(p.dt_nascimento),9,2) >= '" + dia_inicial + "'"
                    + " AND substring(text(p.dt_nascimento),9,2) <= '" + dia_final + "'";
        }

        if (booData) {
            // DATA DE CADASTRO ---------------
            if (!dt_cadastro.isEmpty() && !dt_cadastro_fim.isEmpty()) {
                filtro += " AND p.cadastro >= '" + dt_cadastro + "' AND p.cadastro <= '" + dt_cadastro_fim + "'";
            } else if (!dt_cadastro.isEmpty()) {
                filtro += " AND p.cadastro = '" + dt_cadastro + "'";
            }

            // DATA DE RECADASTRO -------------
            if (!dt_recadastro.isEmpty() && !dt_recadastro_fim.isEmpty()) {
                filtro += " AND p.recadastro >= '" + dt_recadastro + "' AND p.recadastro <= '" + dt_recadastro_fim + "'";
            } else if (!dt_recadastro.isEmpty()) {
                filtro += " AND p.recadastro = '" + dt_recadastro + "'";
            }

            // DATA DE DEMISSAO ----------------
            if (!dt_demissao.isEmpty() && !dt_demissao_fim.isEmpty()) {
                filtro += " AND p.demissao >= '" + dt_demissao + "' AND p.demissao <= '" + dt_demissao_fim + "'";
            } //            else if (!dt_demissao.isEmpty())
            //                filtro += " and p.demissao = '"+dt_demissao+"'";
            else {
                filtro += " AND (p.principal = true OR p.principal IS NULL) ";
                //filtro += " and p.demissao is null ";
            }

            // DATA DE FILIACAO DO SOCIO ----------------
            if (!dt_admissao_socio.isEmpty() && !dt_admissao_socio_fim.isEmpty()) {
                filtro += " AND so.filiacao >= '" + dt_admissao_socio + "' AND so.filiacao <= '" + dt_admissao_socio_fim + "'";
            } else if (!dt_admissao_socio.isEmpty()) {
                filtro += " AND so.filiacao = '" + dt_admissao_socio + "'";
            }

            // DATA DE ADMISSAO EMPRESA
            if (!dt_admissao_empresa.isEmpty() && !dt_admissao_empresa_fim.isEmpty()) {
                filtro += " AND p.admissao >= '" + dt_admissao_empresa + "' AND p.admissao <= '" + dt_admissao_empresa_fim + "'";
            } else if (!dt_admissao_empresa.isEmpty()) {
                filtro += " AND p.admissao = '" + dt_admissao_empresa + "'";
            }

            // DATA APOSENTADORIA
            if (!data_aposentadoria.isEmpty() && !data_aposentadoria_fim.isEmpty()) {
                filtro += " AND p.dt_aposentadoria >= '" + data_aposentadoria + "' AND p.dt_aposentadoria <= '" + data_aposentadoria_fim + "'";
            } else if (!data_aposentadoria.isEmpty()) {
                filtro += " AND p.dt_aposentadoria = '" + data_aposentadoria + "'";
            }

        } else {
            filtro += " AND (p.principal = true OR p.principal IS NULL) ";
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
            } else if (tipo_empresa.equals("com")) {
                filtro += " AND p.empresa <> '' ";
            } else if (tipo_empresa.equals("sem")) {
                filtro += " AND (p.empresa = '' OR p.empresa IS NULL) ";
            }
        }

        if (carenciaDias != null && carenciaDias >= 0) {
            switch (tipoCarencia) {
                case "todos":
                    filtro += " AND func_inadimplente_todos(so.codsocio, " + carenciaDias + ") = false ";
                    break;
                case "eleicao":
                    filtro += " AND func_inadimplente_eleicao(so.codsocio, " + carenciaDias + ") = false ";
                    break;
                case "clube":
                    filtro += " AND func_inadimplente_clube(so.codsocio, " + carenciaDias + ") = false ";
                    break;
            }
        }

        String tordem = "";
        if (ordem != null) {
            if (ordem.equals("nome")) {
                tordem = " p.nome ";
            } else if (ordem.equals("matricula")) {
                tordem = " so.matricula ";
            } else if (ordem.equals("cep")) {
                tordem = " p.cep ";
            } else if (ordem.equals("endereco")) {
                tordem = " p.logradouro, p.endereco, p.numero, p.bairro ";
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
            String queryString = textQry + filtro;
            Query qry = getEntityManager().createNativeQuery(queryString);
//            String novaQuery = textQry + filtro;
            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Vector> listaSociosInativos(boolean comDependentes, boolean chkInativacao, boolean chkFiliacao, String dt_inativacao_i, String dt_inativacao_f, String dt_filiacao_i, String dt_filiacao_f, int categoria, int grupoCategoria, String ordernarPor) {

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
