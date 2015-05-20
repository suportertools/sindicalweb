package br.com.rtools.relatorios.db;

import br.com.rtools.principal.DB;
import br.com.rtools.relatorios.Relatorios;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.persistence.Query;
import oracle.toplink.essentials.exceptions.EJBQLException;

public class RelatorioContribuintesDBToplink extends DB implements RelatorioContribuintesDB {

    @Override
    public List pesquisaContabilidades() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select distinct jur.contabilidade "
                    + "  from Juridica jur"
                    + " where jur.contabilidade is not null"
                    + " order by jur.contabilidade.pessoa.nome");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisarCnaeConvencaoPorConvencao(String ids) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select distinct cc from CnaeConvencao cc "
                    + " where cc.convencao.id in (" + ids + ") "
                    + " order by cc.cnae.cnae");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisarGrupoPorConvencao(String ids) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select gc from GrupoCidade gc "
                    + " where gc.id in (select c.grupoCidade.id from ConvencaoCidade c where c.convencao.id in  (" + ids + "))"
                    + " order by gc.descricao");
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaRelatorioContribuintes(Relatorios relatorios, String emails, String condicao, String escritorio, String tipoPCidade, String cidade, String ordem, String cnaes,
            int idTipoEndereco, String idEndereco, String cTipo, String inCentroComercial, String dsNumero, String idGrupos, String bairros, String convencoes,
            String dataCadastroInicial, String dataCadastroFinal) {
        List result = new ArrayList();
        String textQuery = "";
        try {
            textQuery = "select j.id                     as idJuridica," +//0
                    "       p.ds_nome                as nome," +//1
                    "       p.ds_documento           as documento," +//2
                    "       l.ds_descricao           as logradouro," +//3
                    "       de.ds_descricao          as descricaoEndereco," +//4
                    "       c.ds_cidade              as cidade," +//5
                    "       c.ds_uf                  as uf," +//6
                    "       pe.ds_numero             as numero," +//7
                    "       pe.ds_complemento        as complemento," +//8
                    "       e.ds_cep                 as cep," +//9
                    "       conpes.ds_nome           as nomeContabilidade," +//10
                    "       b.ds_descricao           as bairro," +//11
                    "       p.ds_telefone1           as telefone," +//12
                    "       p.ds_email1              as email," +//13
                    "       t.ds_descricao           as tdocumento," +//14
                    "       cn.id                    as idCnae," +//15
                    "       cn.ds_numero             as numero," +//16
                    "       cn.ds_cnae               as descricao," +//17
                    "       con.id                   as idContabilidade," +//18
                    "       lcon.ds_descricao        as conLogradouro," +//19
                    "       decon.ds_descricao as conDescricaoEndereco," +//20
                    "       bcon.ds_descricao        as conBairro," +//21
                    "       ccon.ds_cidade           as conCidade," +//22
                    "       ccon.ds_uf               as conUf," +//23
                    "       pecon.ds_numero          as conNumero," +//24
                    "       pecon.ds_complemento     as conComplemento," +//25
                    "       econ.ds_cep              as conCep," +//26
                    "       conpes.ds_telefone1      as contelefone1," +//27
                    "       conpes.ds_email1         as conemal1," +//28
                    "       p.id                     as idPessoa" +//29
                    "  from pes_juridica j"
                    + "   left join pes_pessoa_endereco    as pe on pe.id_pessoa = j.id_pessoa"
                    + "   left join end_endereco           as e on e.id = pe.id_endereco"
                    + "   inner join pes_pessoa            as p on p.id = j.id_pessoa"
                    + "   left join end_cidade             as c on e.id_cidade = c.id"
                    + "   left join end_logradouro         as l on l.id = e.id_logradouro"
                    + "   left join end_descricao_endereco as de on de.id = e.id_descricao_endereco"
                    + "   left join end_bairro             as b on b.id = e.id_bairro"
                    + "   left join pes_juridica           as con on con.id = j.id_contabilidade"
                    + "   left join pes_pessoa             as conpes on conpes.id = con.id_pessoa"
                    + "   left join pes_tipo_documento     as t on t.id = p.id_tipo_documento"
                    + "   left join pes_cnae               as cn on cn.id = j.id_cnae"
                    + "   left join pes_pessoa_endereco    as pecon on pecon.id_pessoa = con.id_pessoa"
                    + "   left join end_endereco           as econ  on econ.id = pecon.id_endereco"
                    + "   left join end_cidade             as ccon  on econ.id_cidade = ccon.id"
                    + "   left join end_logradouro         as lcon  on lcon.id = econ.id_logradouro"
                    + "   left join end_descricao_endereco as decon on decon.id = econ.id_descricao_endereco"
                    + "   left join end_bairro             as bcon  on bcon.id = econ.id_bairro"
                    + "   LEFT JOIN pes_centro_comercial   AS CECOM ON CECOM.id_juridica = j.id ";

            // CONVENCAO GRUPO --------------------------------------------
            String cg_where = "", cg_and = "";
            if (idGrupos.length() != 0) {
                //textQuery += " and c.id in (select gc.id_cidade from arr_grupo_cidades gc where gc.id_grupo_cidade in ("+idGrupos+") group by gc.id_cidade)";
                //textQuery += " and c.id in (select gc.id_cidade from arr_grupo_cidades gc where gc.id_grupo_cidade in ("+idGrupos+") group by gc.id_cidade)";
                cg_where += " where c.id_grupo_cidade in (" + idGrupos + ")";
                cg_and += " and c.id_grupo_cidade in (" + idGrupos + ")";
            }

            // CONDICAO -----------------------------------------------------
            switch (condicao) {
                case "contribuintes":
                    textQuery += " where (j.id in (select c.id_juridica from arr_contribuintes_vw c " + cg_where + ") ) ";
                    break;
                case "ativos":
                    textQuery += "  where j.id in (select c.id_juridica from arr_contribuintes_vw c where c.dt_inativacao is null " + cg_and + ") ";
                    break;
                case "inativos":
                    textQuery += "  where j.id in (select c.id_juridica from arr_contribuintes_vw c where c.dt_inativacao is not null " + cg_and + ")";
                    break;
                case "naoContribuinte":
                    textQuery += "  where (j.id not in (select c.id_juridica from arr_contribuintes_vw c " + cg_where + ")) ";
                    break;
            }

            // ESCRITORIO ---------------------------------------------------
            switch (escritorio) {
                case "todos":
                    break;
                case "semEscritorio":
                    textQuery += " and j.id_contabilidade is null ";
                    break;
                case "comEscritorio":
                    textQuery += " and j.id_contabilidade is not null ";
                    break;
                default:
                    textQuery += " and j.id_contabilidade = " + Integer.parseInt(escritorio);
                    break;
            }

            // ENVIO DE LOGIN SENHA
            switch (emails) {
                case "e":
                    textQuery += " and j.id_pessoa in (select ee.id_pessoa from pes_envio_emails ee where ee.ds_operacao = 'Login e Senha') ";
                    break;
                case "n":
                    textQuery += " and j.id_pessoa not in (select ee.id_pessoa from pes_envio_emails ee where ee.ds_operacao = 'Login e Senha') ";
                    break;
            }

            // CIDADE -------------------------------------------------------
            switch (tipoPCidade) {
                case "todas":
                    textQuery += " and (pe.id_tipo_endereco = " + idTipoEndereco + " or pe.id_tipo_endereco is null)"
                            + " and (pecon.id_tipo_endereco = 5 or pecon.id_tipo_endereco is null)";
                    break;
                case "especificas":
                case "local":
                    textQuery += " and (pe.id_tipo_endereco = " + idTipoEndereco + " or pe.id_tipo_endereco is null)"
                            + " and (pecon.id_tipo_endereco = 5 or pecon.id_tipo_endereco is null)"
                            + " and e.id_cidade = " + Integer.parseInt(cidade);
                    break;
                case "outras":
                    textQuery += " and (pe.id_tipo_endereco = " + idTipoEndereco + " or pe.id_tipo_endereco is null)"
                            + " and (pecon.id_tipo_endereco = 5 or pecon.id_tipo_endereco is null)"
                            + " and e.id_cidade <> " + Integer.parseInt(cidade);
                    break;
            }

            // CENTRO COMERCIAL --------------------------------------------
            if (idEndereco.length() != 0) {
                if (cTipo.equals("com")) {
                    textQuery += " and pe.id in (select pe2.id from pes_pessoa_endereco pe2 where pe2.id_tipo_endereco = 5 and pe2.id_endereco in (" + idEndereco + ") and pe2.ds_numero in (" + dsNumero + "))";
                } else {
                    textQuery += " and pe.id not in (select pe2.id from pes_pessoa_endereco pe2 where pe2.id_tipo_endereco = 5 and pe2.id_endereco in (" + idEndereco + ") and pe2.ds_numero in (" + dsNumero + "))";
                }
            }

//            if (!inCentroComercial.isEmpty()) {
//                textQuery += " AND CECOM.id IN (" + inCentroComercial + ") ";
//            }

            if (bairros.length() != 0) {
                textQuery += " and b.id in (" + bairros + ")";
            }

            // CONVENÇÃO
            if (convencoes.length() != 0) {
                textQuery += " AND j.id IN (select c.id_juridica from arr_contribuintes_vw c where id_convencao in (" + convencoes + ")) ";
            }
            // CNAES
            if (cnaes.length() != 0) {
                textQuery += " and j.id_cnae in ( " + cnaes + " ) ";
            }

            // DATA
            if (!dataCadastroInicial.isEmpty()) {
                textQuery += " and p.dt_criacao between '" + dataCadastroInicial + "' and '" + dataCadastroFinal + "' ";
            }
            // ORDEM ------------------------------------------------------------------------
            if (relatorios.getQryOrdem() == null || relatorios.getQryOrdem().isEmpty()) {
                switch (ordem) {
                    case "razao":
                        textQuery += " order by p.ds_nome ";
                        break;
                    case "documento":
                        textQuery += " order by p.ds_documento ";
                        break;
                    case "endereco":
                        textQuery += " order by c.ds_uf, c.ds_cidade, l.ds_descricao, de.ds_descricao, pe.ds_numero";
                        break;
                    case "cep":
                        textQuery += " order by e.ds_cep, c.ds_uf, c.ds_cidade, l.ds_descricao, de.ds_descricao, pe.ds_numero";
                        break;
                    case "escritorio":
                        textQuery += " order by conpes.ds_nome,p.ds_nome ";
                        break;
                }
            } else {
                textQuery += " ORDER BY " + relatorios.getQryOrdem();
            }
            Query qry = getEntityManager().createNativeQuery(textQuery);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    @Override
    public List listaCentros(String ids) {
        List result = new ArrayList();
        String textQuery = "select pe2.id_endereco, pe2.ds_numero"
                + "  from pes_pessoa_endereco pe2"
                + " inner join pes_pessoa p2 on p2.id = pe2.id_pessoa"
                + " inner join pes_juridica j2 on j2.id_pessoa = p2.id"
                + " inner join pes_centro_comercial cc2 on cc2.id_juridica = j2.id"
                + " where pe2.id_tipo_endereco = 5"
                + "   and j2.id in (" + ids + ")";
        try {
            Query qry = getEntityManager().createNativeQuery(textQuery);
            result = qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaRelatorioContribuintesPorJuridica(String condicao, String escritorio, String tipoPCidade, String cidade, String ordem, String cnaes, int idTipoEndereco, String idsJuridica) {
        List result = new ArrayList();
        String textQuery = "";
        try {
            Query qry = getEntityManager().createNativeQuery("select id_juridica from arr_contribuintes_vw");

            // VIEWER CONTRIBUINTES ---------------------------------------------------------------------
            Vector listCont = (Vector) qry.getResultList();
            String idsListCont = "";
            for (int i = 0; i < listCont.size(); i++) {
                if (idsListCont.length() > 0 && i != listCont.size()) {
                    idsListCont = idsListCont + ",";
                }
                idsListCont = idsListCont + (Integer) ((Vector) listCont.get(i)).get(0);
            }

            textQuery = "select j "
                    + "   from Juridica j,"
                    + "        PessoaEndereco pe ";

            // CONDICAO -----------------------------------------------------
            if (condicao.equals("contribuintes")) {
                textQuery = textQuery + "  where (j.id in (" + idsListCont + ") "
                        + "     or j.id in (select ci.juridica.id "
                        + "                   from ContribuintesInativos ci "
                        + "                  group by ci.juridica.id)) "
                        + "    and j.id in (" + idsJuridica + ")";
            } else if (condicao.equals("ativos")) {
                textQuery = textQuery + "  where j.id in (" + idsListCont + ") "
                        + "    and j.id in (" + idsJuridica + ")";
            } else if (condicao.equals("inativos")) {
                textQuery = textQuery + "  where j.id not in (" + idsListCont + ") "
                        + "    and j.id in (select ci.juridica.id from ContribuintesInativos ci group by ci.juridica.id) "
                        + "    and j.id in (" + idsJuridica + ")";
            } else if (condicao.equals("naoContribuinte")) {
                textQuery += "  where j.id not in (" + idsListCont + ") "
                        + "     and j.id in (" + idsJuridica + ")";
            }
            // ESCRITORIO ---------------------------------------------------
            if (escritorio.equals("todos")) {
            } else if (escritorio.equals("semEscritorio")) {
                textQuery = textQuery + " and j.contabilidade is null ";
            } else if (escritorio.equals("comEscritorio")) {
                textQuery = textQuery + " and j.contabilidade is not null ";
            } else {
                textQuery = textQuery + " and j.contabilidade.id = " + Integer.parseInt(escritorio);
            }
            // CIDADE -------------------------------------------------------
            if (tipoPCidade.equals("todas")) {
                textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id "
                        + " and pe.tipoEndereco.id = " + idTipoEndereco;
            } else if (tipoPCidade.equals("especificas")) {
                textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id "
                        + " and pe.tipoEndereco.id = " + idTipoEndereco
                        + " and pe.endereco.cidade.id = " + Integer.parseInt(cidade);
            } else if (tipoPCidade.equals("local")) {
                textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id "
                        + " and pe.tipoEndereco.id = " + idTipoEndereco
                        + " and pe.endereco.cidade.id = " + Integer.parseInt(cidade);
            } else if (tipoPCidade.equals("outras")) {
                textQuery = textQuery + " and pe.pessoa.id = j.pessoa.id "
                        + " and pe.tipoEndereco.id = " + idTipoEndereco
                        + " and pe.endereco.cidade.id <> " + Integer.parseInt(cidade);
            }
            // CNAES

            if (cnaes.length() != 0) {
                textQuery = textQuery + " and j.cnae.id in ( " + cnaes + " ) ";
            }

            // ORDEM ------------------------------------------------------------------------
            if (ordem.equals("razao")) {
                textQuery = textQuery + " order by j.pessoa.nome";
            } else if (ordem.equals("documento")) {
                textQuery = textQuery + " order by j.pessoa.documento";
            } else if (ordem.equals("endereco")) {
                textQuery = textQuery + " order by pe.endereco.cidade.uf,"
                        + " pe.endereco.cidade.cidade, pe.endereco.logradouro.descricao,"
                        + " pe.endereco.descricaoEndereco.descricao,"
                        + " pe.numero";
            } else if (ordem.equals("cep")) {
                textQuery = textQuery + " order by pe.endereco.cep,"
                        + " pe.endereco.cidade.uf,"
                        + " pe.endereco.cidade.cidade, pe.endereco.logradouro.descricao,"
                        + " pe.endereco.descricaoEndereco.descricao,"
                        + " pe.numero";
            } else if (ordem.equals("escritorio")) {
                textQuery = textQuery + " order by j.contabilidade.pessoa.nome,"
                        + "          j.pessoa.nome";
            }

            qry = getEntityManager().createQuery(textQuery);
            result = qry.getResultList();
        } catch (EJBQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}
