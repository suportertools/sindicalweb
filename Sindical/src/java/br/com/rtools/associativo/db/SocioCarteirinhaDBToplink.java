package br.com.rtools.associativo.db;

import br.com.rtools.associativo.SocioCarteirinha;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SocioCarteirinhaDBToplink extends DB implements SocioCarteirinhaDB {

    @Override
    public SocioCarteirinha pesquisaCodigo(int id) {
        SocioCarteirinha result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("SocioCarteirinha.pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = (SocioCarteirinha) qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("SELECT sc FROM SocioCarteirinha sc");
            if (!qry.getResultList().isEmpty()) {
                return (qry.getResultList());
            }
        } catch (Exception e) {
        }
        return null;
    }

    @Override
    public List pesquisaSocioSemCarteirinha() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("   SELECT s FROM Socios s"
                    + "    WHERE s.id NOT IN ( SELECT sc.socios.id FROM SocioCarteirinha sc)"
                    + " ORDER BY s.matriculaSocios.id");
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaSocioSemCarteirinhaDependente() {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("   SELECT s                                                             "
                    + "     FROM Socios s                                                      "
                    + "    WHERE s.id NOT IN ( SELECT sc.socios.id FROM SocioCarteirinha sc )  "
                    + "      AND s.parentesco.id = 1                                           "
                    + " ORDER BY s.matriculaSocios.id                                          ");
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaSocioCarteirinhaSocio(int idSocio) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT sc "
                    + "  FROM SocioCarteirinha sc"
                    + " WHERE sc.socios.id = :idSocio");
            qry.setParameter("idSocio", idSocio);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaFiltro(String indexFiltro, String descEmpresa, String indexOrdem, boolean fantasia) {
        List lista = new ArrayList();
        String textqry = "     SELECT p.codigo AS codigo,                                        " + // 0 CÓDIGO
                "            p.nome AS nome,                                            " + // 1 NOME
                "            p.cnpj AS cnpj,                                            " + // 2 CNPJ
                "            p.empresa as empresa,                                      " + // 3 EMPRESA
                "            to_char(c.dt_emissao, 'DD/MM/YYYY') AS data,               " + // 4 DATA EMISSÃO
                "            p.e_cidade AS cidade,                                      " + // 5 CIDADE
                "            to_char(s.validade_carteirinha, 'DD/MM/YYYY') AS validade, " + // 6 VALIDADE
                "            p.e_uf AS uf,                                              " + // 7 ESTADO
                "            to_char(p.admissao, 'DD/MM/YYYY') AS admissao,             " + // 8 ADMISSÃO
                "            p.fantasia AS fantasia,                                    " + // 9 FANTASIA
                "            s.matricula AS matricula,                                  " + // 10 MATRICULA
                "            s.nr_via AS via,                                           " + // 11 VIA
                "            s.id_socio AS codsocio,                                    " + // 12 CÓDIGO SÓCIO
                "            to_char(s.filiacao, 'DD/MM/YYYY') as filiacao,             " + // 13 FILIAÇÃO
                "            p.profissao AS cargo,                                      " + // 14 PROFISSÃO
                "            p.cpf,                                                     " + // 15 CPF
                "            p.ds_rg AS rg                                              " + // 16 RG                
                "       FROM pes_pessoa_vw AS p                                         "
                + " INNER JOIN soc_socios_vw AS s on s.codsocio = p.codigo and s.parentesco = 'TITULAR' "
                + "  LEFT JOIN soc_carteirinha AS c on c.id_socio = s.id_socio";
        String por = "empresa";
        if (fantasia) {
            por = "fantasia";
        }

        descEmpresa = descEmpresa.toUpperCase();
        if (indexFiltro.equals("0")) {
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is null";
            textqry += " where s.id_socio not in (select id_socio from soc_carteirinha) and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("1")) {
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is null and upper(p."+por+") like ('%"+descEmpresa+"%')";
            textqry += " where ( s.id_socio not in (select id_socio from soc_carteirinha) ) and upper(p." + por + ") like ('%" + descEmpresa + "%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("2")) {
            textqry += " where ( s.id_socio not in (select id_socio from soc_carteirinha) ) and p.cnpj like ('%" + descEmpresa + "%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("3")) {
            textqry += " where upper(p.nome) like ('%" + descEmpresa + "%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("4")) {
            int cod;
            try {
                cod = Integer.valueOf(descEmpresa);
            } catch (Exception e) {
                cod = 0;
            }
            textqry += " where p.codigo = " + cod + " and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("5")) {
            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and upper(p." + por + ") like ('%" + descEmpresa + "%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("6")) {
            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and p.cnpj like ('" + descEmpresa + "%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
        } else if (indexFiltro.equals("7")) {
            //textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and c.dt_emissao>=(current_date-30) and to_char(p.demissao, 'DD/MM/YYYY') is null";
            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and to_char(dt_emissao,'yyyymmyy')>=to_char(current_date-30,'yyyymmyy') and to_char(p.demissao, 'DD/MM/YYYY') is null ";
        } else if (indexFiltro.equals("8")) {
            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and to_char(p.demissao, 'DD/MM/YYYY') is null";
        }
//        if (indexFiltro.equals("0")){
////            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is null";
//            textqry += " where s.id_socio not in (select id_socio from soc_carteirinha) and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("1")){
////            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is null and upper(p."+por+") like ('%"+descEmpresa+"%')";
//            textqry += " where ( s.id_socio not in (select id_socio from soc_carteirinha) ) and upper(p."+por+") like ('%"+descEmpresa+"%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("2")){
//            textqry += " where ( s.id_socio not in (select id_socio from soc_carteirinha) ) and p.cnpj like ('%"+descEmpresa+"%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("3")){
//            textqry += " where upper(p.nome) like ('%"+descEmpresa+"%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("4")){
//            int cod = 0;
//            try{ cod = Integer.valueOf(descEmpresa); }catch(Exception e){cod = 0;}
//            textqry += " where p.codigo = "+cod+" and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("5")){
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and upper(p."+por+") like ('%"+descEmpresa+"%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("6")){
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and p.cnpj like ('"+descEmpresa+"%') and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("7")){
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and c.dt_emissao>=(current_date-30) and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }else if (indexFiltro.equals("8")){
//            textqry += " where to_char(c.dt_emissao, 'DD/MM/YYYY') is not null and to_char(p.demissao, 'DD/MM/YYYY') is null";
//        }


        textqry += " group by p.codigo, "
                + " p.nome, "
                + " p.cnpj, "
                + " p.empresa, "
                + " to_char(c.dt_emissao, 'DD/MM/YYYY'), "
                + " p.e_cidade, "
                + " to_char(s.validade_carteirinha, 'DD/MM/YYYY'), "
                + " p.e_uf, to_char(p.admissao, 'DD/MM/YYYY'), "
                + " p.fantasia, "
                + " s.matricula, "
                + " s.nr_via, "
                + " s.id_socio, "
                + " s.filiacao, "
                + " p.profissao, "
                + " p.cpf, "
                + " p.ds_rg ";

        if (indexOrdem.equals("0")) {
            textqry += " order by p.nome ";
        } else if (indexOrdem.equals("1")) {
            textqry += " order by p.empresa, p.cnpj, p.nome ";
        } else if (indexOrdem.equals("2")) {
            textqry += " order by p.cnpj, p.nome ";
        } else if (indexOrdem.equals("3")) {
            textqry += " order by data desc, p.nome ";
        } else if (indexOrdem.equals("4")) {
            textqry += " order by data desc, p.empresa, p.cnpj, p.nome";
        }


        try {
            Query qry = getEntityManager().createNativeQuery(textqry);
            if (!qry.getResultList().isEmpty()) {
                lista = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return lista;
    }

    @Override
    public List listaFiltroCartao(int id_socio) {
        List lista = new ArrayList();
        String textqry = "     SELECT p.codigo as codigo,                                        " + // 0 CÓDIGO
                "            p.nome as nome,                                            " + // 1 NOME
                "            p.cnpj as cnpj,                                            " + // 2 CNPJ
                "            p.empresa as empresa,                                      " + // 3 EMPRESA
                "            to_char(c.dt_emissao, 'DD/MM/YYYY') as data,               " + // 4 DATA EMISSÃO
                "            p.e_cidade as cidade,                                      " + // 5 CIDADE
                "            to_char(s.validade_carteirinha, 'DD/MM/YYYY') as validade, " + // 6 VALIDADE
                "            p.e_uf as uf,                                              " + // 7 ESTADO
                "            to_char(p.admissao, 'DD/MM/YYYY') as admissao,             " + // 8 ADMISSÃO
                "            p.fantasia as fantasia,                                    " + // 9 FANTASIA
                "            s.matricula as matricula,                                  " + // 10 MATRICULA
                "            s.nr_via as via,                                           " + // 11 VIA
                "            s.id_socio as codsocio,                                    " + // 12 CÓDIGO SÓCIO
                "            to_char(s.filiacao, 'DD/MM/YYYY') as filiacao              " + // 13 FILIAÇÃO
                "       FROM pes_pessoa_vw AS p                                         "
                + " INNER JOIN soc_socios_vw AS s on s.codsocio = p.codigo                "
                + "  LEFT JOIN soc_carteirinha AS c on c.id_socio = s.id_socio            "
                + "      WHERE s.id_socio = " + id_socio + " ";
        try {
            Query qry = getEntityManager().createNativeQuery(textqry);
            if (!qry.getResultList().isEmpty()) {
                lista = qry.getResultList();
            }
        } catch (Exception e) {
        }

        return lista;
    }

    @Override
    public List filtroCartao(int id_socio) {
        List lista = new ArrayList();
        String textqry = "     SELECT p.codigo AS codigo,                                        " + // 0 CÓDIGO
                "            p.nome AS nome,                                            " + // 1 NOME
                "            p.cnpj AS cnpj,                                            " + // 2 CNPJ
                "            p.empresa AS empresa,                                      " + // 3 EMPRESA
                "            to_char(c.dt_emissao, 'DD/MM/YYYY') AS data,               " + // 4 DATA EMISSÃO
                "            p.e_cidade AS cidade,                                      " + // 5 CIDADE
                "            to_char(s.validade_carteirinha, 'DD/MM/YYYY') AS validade, " + // 6 VALIDADE
                "            p.e_uf AS uf,                                              " + // 7 ESTADO
                "            to_char(p.admissao, 'DD/MM/YYYY') as admissao,             " + // 8 ADMISSÃO
                "            p.fantasia AS fantasia,                                    " + // 9 FANTASIA
                "            s.matricula AS matricula,                                  " + // 10 MATRICULA
                "            s.nr_via AS via,                                           " + // 11 VIA
                "            s.id_socio AS codsocio,                                    " + // 12 CÓDIGO SÓCIO
                "            to_char(s.filiacao, 'DD/MM/YYYY') as filiacao              " + // 13 FILIAÇÃO
                "       FROM pes_pessoa_vw AS p                                         "
                + " INNER JOIN soc_socios_vw AS s on s.codsocio = p.codigo                "
                + " INNER JOIN soc_carteirinha as c on c.id_socio = s.id_socio            "
                + "      WHERE s.id_socio = " + id_socio + "                                  "
                + "        AND c.id in (                                                  "
                + "                      SELECT MAX(id) from soc_carteirinha              "
                + "                       WHERE id_socio = " + id_socio + "                   "
                + "        )";
        try {
            Query qry = getEntityManager().createNativeQuery(textqry);
            if (!qry.getResultList().isEmpty()) {
                lista = qry.getResultList();
            }
        } catch (Exception e) {
        }

        return lista;
    }

    @Override
    public List listaPesquisaEtiqueta(int id_pessoa) {
        List lista = new ArrayList();
        String textqry = "SELECT nome, logradouro, endereco, numero, bairro, cidade, uf, cep, complemento FROM pes_pessoa_vw WHERE codigo = " + id_pessoa;
        try {
            Query qry = getEntityManager().createNativeQuery(textqry);
            if (!qry.getResultList().isEmpty()) {
                lista = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return lista;
    }

    @Override
    public boolean verificaSocioCarteirinhaExiste(int id_socio) {
        try {
            Query qry = getEntityManager().createNativeQuery(" SELECT * FROM soc_carteirinha WHERE id_socio = " + id_socio + " AND dt_emissao = current_date ");
            if (!qry.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }
}
