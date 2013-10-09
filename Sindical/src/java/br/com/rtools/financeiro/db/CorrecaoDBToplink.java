package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Correcao;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.principal.DB;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class CorrecaoDBToplink extends DB implements CorrecaoDB {

    public boolean insert(Correcao correcao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(correcao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean update(Correcao correcao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(correcao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public boolean delete(Correcao correcao) {
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(correcao);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            return false;
        }
    }

    public List pesquisaTodos() {
        try {
            Query qry = getEntityManager().createQuery("select c from Correcao c");
            return (qry.getResultList());
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Correcao pesquisaCodigo(int id) {
        Correcao result = null;
        try {
            Query qry = getEntityManager().createNamedQuery("Correcao.pesquisaID");
            qry.setParameter("pid", id);
            result = (Correcao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return result;
    }

    public List pesquisaRefValida(Servicos servicos, String refInicial, String refFinal) {
        String d1 = refInicial.substring(3, 7) + refInicial.substring(0, 2);
        String d2 = refFinal.substring(3, 7) + refFinal.substring(0, 2);
        try {
            Query qry = getEntityManager().createQuery("select count(c.servicos.id)"
                    + "  from Correcao c   "
                    + " where c.servicos.id = " + servicos.getId()
                    + "   and ( '" + d1 + "' between CONCAT( SUBSTRING(c.referenciaInicial,4,8) , SUBSTRING(c.referenciaInicial,0,3) )"
                    + "   and                      CONCAT( SUBSTRING(c.referenciaFinal,4,8)   , SUBSTRING(c.referenciaFinal,0,3) )"
                    + "    or   '" + d2 + "' between CONCAT( SUBSTRING(c.referenciaInicial,4,8) , SUBSTRING(c.referenciaInicial,0,3) )"
                    + "   and                      CONCAT( SUBSTRING(c.referenciaFinal,4,8)   , SUBSTRING(c.referenciaFinal,0,3) ))");

            return qry.getResultList();
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    public Correcao pesquisaCorrecao(Servicos servicos, String referencia) {
        Correcao correcao = null;
        referencia = referencia.substring(3, 7) + referencia.substring(0, 2);
        try {
            Query qry = getEntityManager().createQuery(
                    "select c"
                    + "  from Correcao c   "
                    + " where c.servicos.id = " + servicos.getId()
                    + "   and '" + referencia + "' between"
                    + "        CONCAT( SUBSTRING(c.referenciaInicial,4,8) , SUBSTRING(c.referenciaInicial,0,3) )"
                    + "        and  "
                    + "        CONCAT( SUBSTRING(c.referenciaFinal,4,8)   , SUBSTRING(c.referenciaFinal,0,3) )");
            correcao = (Correcao) qry.getSingleResult();
        } catch (Exception e) {
        }
        return correcao;
    }
}
