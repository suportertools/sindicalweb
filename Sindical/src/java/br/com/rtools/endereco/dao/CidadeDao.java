package br.com.rtools.endereco.dao;

import br.com.rtools.principal.DB;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.utilitarios.AnaliseString;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CidadeDao extends DB {

    public List pesquisaCidadeObj(String des_uf) {
        try {
            Query qry = getEntityManager().createQuery(
                    "      SELECT C                 "
                    + "      FROM Cidade AS C       "
                    + "     WHERE UPPER(C.uf) = :uf        "
                    + "  ORDER BY C.cidade ASC      ");
            qry.setParameter("uf", des_uf.toUpperCase());
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public List<String> pesquisaCidade(String des_cidade, String des_uf) {
        List<String> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select cid.cidade "
                    + "  from Cidade cid "
                    + " where upper(cid.cidade) like :des_cidade "
                    + "   and upper(cid.uf) = :des_uf order by cid.cidade");
            qry.setParameter("des_cidade", des_cidade.toUpperCase());
            qry.setParameter("des_uf", des_uf.toUpperCase());
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaCidade(String uf, String cidade, String como) {
        List result = new ArrayList();
        String query = "";
        cidade = cidade.toLowerCase().toUpperCase();
        if (como.equals("P")) {
            if (!(cidade.equals(""))) {
                cidade = "%" + cidade + "%";
            }
            query = "select c from Cidade c where upper(c.cidade) like :desc and c.uf = :uf";
        } else if (como.equals("I")) {
            if (!(cidade.equals(""))) {
                cidade = cidade + "%";
            }
            query = "select c from Cidade c where upper(c.cidade) like :desc and c.uf = :uf";
        }
        try {
            Query qry = getEntityManager().createQuery(query);
            qry.setParameter("desc", cidade);
            qry.setParameter("uf", uf);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaCidadePorCidade(String cidade, String como) {
        String queryString = "";
        cidade = cidade.toLowerCase().toUpperCase();
        if (como.equals("P")) {
            if (!(cidade.equals(""))) {
                cidade = "%" + cidade + "%";
            }
        } else if (como.equals("I")) {
            if (!(cidade.equals(""))) {
                cidade = cidade + "%";
            }
        }
        queryString = "SELECT C.id FROM end_cidade AS C WHERE UPPER(TRANSLATE(C.ds_cidade)) LIKE '" + AnaliseString.removerAcentos(cidade) + "' ORDER BY C.ds_cidade ASC";
        try {
            Query qry = getEntityManager().createNativeQuery(queryString);
            List listNative = qry.getResultList();
            if (!listNative.isEmpty()) {
                String inIds = "";
                for (int i = 0; i < listNative.size(); i++) {
                    if (i == 0) {
                        inIds = "" + ((List) listNative.get(i)).get(0).toString();
                    } else {
                        inIds += "," + ((List) listNative.get(i)).get(0).toString();
                    }
                }
                String queryOO = "SELECT C FROM Cidade AS C WHERE C.id IN(" + inIds + ")";
                Query query = getEntityManager().createQuery(queryOO);
                List list = query.getResultList();
                if (!list.isEmpty()) {
                    return list;
                }
            }
        } catch (Exception e) {
        }
        return new ArrayList();
    }

    public Cidade pesquisaCidadePorEstadoCidade(String uf, String cidade) {
        cidade = cidade.toLowerCase().toUpperCase();
        try {
            Query query = getEntityManager().createNativeQuery("SELECT C.* FROM end_cidade AS C WHERE UPPER(TRANSLATE(C.ds_cidade)) = '" + AnaliseString.removerAcentos(cidade) + "'  AND UPPER(C.ds_uf) = '" + uf.toUpperCase() + "'", Cidade.class);
            List list = query.getResultList();
            if (!list.isEmpty() || list.size() == 1) {
                return (Cidade) query.getSingleResult();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public List<Cidade> listaCidadeParaREPIS() {
        try {
            Query qry = getEntityManager().createNativeQuery(
                    " SELECT c.id id, c.ds_cidade ds_cidade, c.ds_uf ds_uf "
                    + "   FROM arr_repis_movimento rm "
                    + "  INNER JOIN pes_pessoa p ON p.id = rm.id_pessoa "
                    + "  INNER JOIN pes_pessoa_endereco pe ON pe.id_pessoa = p.id AND pe.id_tipo_endereco = 5 "
                    + "  INNER JOIN end_endereco e ON e.id = pe.id_endereco "
                    + "  INNER JOIN end_cidade c ON c.id = e.id_cidade "
                    + "  GROUP BY c.id, c.ds_cidade, c.ds_uf "
                    + "  ORDER BY c.ds_cidade ", Cidade.class);

            return qry.getResultList();
        } catch (Exception e) {
            e.getMessage();
        }
        return new ArrayList();
    }
}
