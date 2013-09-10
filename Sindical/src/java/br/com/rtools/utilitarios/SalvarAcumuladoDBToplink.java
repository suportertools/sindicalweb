package br.com.rtools.utilitarios;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.principal.DB;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SalvarAcumuladoDBToplink extends DB implements SalvarAcumuladoDB {

    @Override
    public void abrirTransacao() {
        getEntityManager().getTransaction().begin();
    }

    @Override
    public void comitarTransacao() {
        getEntityManager().getTransaction().commit();
    }

    @Override
    public void desfazerTransacao() {
        getEntityManager().getTransaction().rollback();
    }

    @Override
    public boolean inserirObjeto(Object objeto) {
        try {
            getEntityManager().persist(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            NovoLog log = new NovoLog();
            log.novo("Novo Objeto", "Exception - Message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean alterarObjeto(Object objeto) {
        Class classe = objeto.getClass();
        int id;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            id = (Integer) metodo.invoke(objeto, (Object[]) null);
            if(id == -1){
                NovoLog log = new NovoLog();
                log.novo("Alterar "+objeto.getClass().getSimpleName(), "Objeto esta passando -1");
                return false;
            }
            getEntityManager().merge(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            NovoLog log = new NovoLog();
            log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deletarObjeto(Object objeto) {
        try {
            getEntityManager().remove(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            NovoLog log = new NovoLog();
            log.novo("Deletar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public Object pesquisaCodigo(int id, String tipo) {
        Object result = null;
        try {
            Query qry = getEntityManager().createNamedQuery(tipo + ".pesquisaID");
            qry.setParameter("pid", id);
            if(!qry.getResultList().isEmpty()){
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public Object pesquisaObjeto(int id, String tabela) {
        Object result = null;
        try {
            Query qry = getEntityManager().createQuery("SELECT OB FROM " + tabela + " OB WHERE OB.id = " + id);
            if(!qry.getResultList().isEmpty()){
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }

    @Override
    public List listaObjeto(String tabela) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT OB FROM " + tabela + " AS OB");
            if(!qry.getResultList().isEmpty()){
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaObjetoGenericoOrdem(String tabela) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("select ob from " + tabela + " ob order by ob.descricao");
            qry.setMaxResults(100);
            if(!qry.getResultList().isEmpty()){
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public List listaObjetoGenericoOrdemDesc(String tabela, String descricao) {
        List result = new ArrayList();
        if(tabela.isEmpty()){
            return result;            
        }
        try {
            Query qry = getEntityManager().createQuery("SELECT ob FROM " + tabela + " ob WHERE UPPER(ob.descricao) LIKE '%" + descricao.toUpperCase() + "%' ORDER BY ob.descricao");
            qry.setMaxResults(100);
            if(!qry.getResultList().isEmpty()){
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public boolean inserirQuery(String textQuery) {
        try {
            int valor = getEntityManager().createNativeQuery(textQuery).executeUpdate();
            if (valor > 0) {
                return true;
            } else {
                NovoLog log = new NovoLog();
                log.novo("Novo Objeto", "Exception - Message: Erro ao executar: " + textQuery);
                return false;
            }
        } catch (Exception e) {
            NovoLog log = new NovoLog();
            log.novo("Novo Objeto", "Exception - Message: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean descricaoExiste(String descricao, String campo, String objeto) {
        try {
            Query qry = getEntityManager().createQuery("select ob from " + objeto + " ob where upper(ob." + campo + ") = :descricao");
            qry.setParameter("descricao", descricao.toUpperCase());
            if (!qry.getResultList().isEmpty()) {
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
