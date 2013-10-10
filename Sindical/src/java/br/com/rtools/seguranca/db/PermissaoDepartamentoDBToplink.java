package br.com.rtools.seguranca.db;

import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Permissao;
import br.com.rtools.seguranca.PermissaoDepartamento;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class PermissaoDepartamentoDBToplink extends DB implements PermissaoDepartamentoDB {

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select pd from PermissaoDepartamento pd ");
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaPermissaoDptoIdEvento(int id) {
        try {
            Query qry = getEntityManager().createQuery("select pd from PermissaoDepartamento pd "
                    + " where pd.id = " + id);
            return (qry.getResultList());
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List pesquisaPermissaDisponivel(String ids) {
        String textQuery = "";
        try {
            if (ids.length() == 0) {
                textQuery = "select p "
                        + "  from Permissao p "
                        + " order by p.modulo.descricao";
            } else {
                textQuery = "select p "
                        + "  from Permissao p"
                        + " where p.id not in (select pd.permissao.id "
                        + "                      from PermissaoDepartamento pd"
                        + "                     where pd.id in (" + ids + "))"
                        + " order by p.modulo.descricao,"
                        + "          p.rotina.rotina";
            }

            Query qry = getEntityManager().createQuery(textQuery);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaPermissaoAdc(int idDepto, int idNivel) {
        try {
            Query qry = getEntityManager().createQuery("select pd "
                    + "  from PermissaoDepartamento pd "
                    + " where pd.departamento.id = :idDepto "
                    + "   and pd.nivel.id = :idNivel "
                    + " order by pd.permissao.modulo.descricao,"
                    + "          pd.permissao.rotina.rotina");
            qry.setParameter("idDepto", idDepto);
            qry.setParameter("idNivel", idNivel);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaPermissaDepto(String ids) {
        String textQuery = "";
        try {
            textQuery = "select pd "
                    + " from PermissaoDepartamento pd "
                    + "where pd.permissao.id in (" + ids + ")";
            Query qry = getEntityManager().createQuery(textQuery);
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List<Permissao> listaPermissaoDepartamentoDisponivel(int idDepartamento, int idNivel, String descricaoPesquisa) {
        String queryFiltro = "";
        if (!descricaoPesquisa.equals("")) {
            queryFiltro = " AND UPPER(P.rotina.rotina) LIKE '%" + descricaoPesquisa.toUpperCase() + "%'";
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT P FROM Permissao AS P WHERE P.id NOT IN ( SELECT PD.permissao.id FROM PermissaoDepartamento AS PD WHERE PD.departamento.id = " + idDepartamento + " AND PD.nivel.id = " + idNivel + ") " + queryFiltro + " ORDER BY P.modulo.descricao ASC, P.rotina.rotina ASC ");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();

    }

    @Override
    public List<PermissaoDepartamento> listaPermissaoDepartamentoAdicionada(int idDepartamento, int idNivel, String descricaoPesquisa) {
        String queryFiltro = "";
        if (!descricaoPesquisa.equals("")) {
            queryFiltro = " AND UPPER(PD.permissao.rotina.rotina) LIKE '%" + descricaoPesquisa.toUpperCase() + "%'";
        }
        try {
            Query query = getEntityManager().createQuery(" SELECT PD FROM PermissaoDepartamento AS PD WHERE PD.departamento.id = :idDepartamento AND PD.nivel.id = :idNivel " + queryFiltro + " ORDER BY PD.permissao.modulo.descricao ASC, PD.permissao.rotina.rotina ASC ");
            query.setParameter("idDepartamento", idDepartamento);
            query.setParameter("idNivel", idNivel);
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            return new ArrayList();
        }
        return new ArrayList();
    }
}
