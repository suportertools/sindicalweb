package br.com.rtools.endereco.db;

import br.com.rtools.principal.DB;
import br.com.rtools.endereco.Cidade;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CidadeDBToplink extends DB implements CidadeDB {

    @Override
    public Cidade pesquisaCodigo(int id) {
        Cidade result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Cidade.pesquisaID");
            qry.setParameter("pid", id);
            result = (Cidade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select cid from Cidade cid ");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    @Override
    public List pesquisaCidadeObj(String des_uf) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery(
                    "select cid "
                    + "  from Cidade cid "
                    + " where cid.uf = :des_uf");
            qry.setParameter("des_uf", des_uf);
            result = (List) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List<String> pesquisaCidade(String des_cidade, String des_uf) {
        List<String> result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select cid.cidade "
                    + "  from Cidade cid "
                    + " where cid.cidade like :des_cidade "
                    + "   and cid.uf = :des_uf order by cid.cidade");
            qry.setParameter("des_cidade", des_cidade);
            qry.setParameter("des_uf", des_uf);
            result = (List<String>) qry.getResultList();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public Cidade idCidade(Cidade des_cidade) {
        Cidade result = null;
        String descricao = des_cidade.getCidade().toLowerCase().toUpperCase();
        try {
            Query qry = getEntityManager().createQuery("select cid from Cidade cid where UPPER(cid.cidade) = :d_cidade and cid.uf = :d_uf");
            qry.setParameter("d_cidade", descricao);
            qry.setParameter("d_uf", des_cidade.getUf());
            result = (Cidade) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    @Override
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
}
