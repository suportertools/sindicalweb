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
            if (id == -1) {
                NovoLog log = new NovoLog();
                log.novo("Alterar " + objeto.getClass().getSimpleName(), "Objeto esta passando -1");
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
            if (!qry.getResultList().isEmpty()) {
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public List pesquisaCodigo(int id[], String tipo) {
        List<Object> list = new ArrayList<Object>(); 
        Object object; 
        for (int i = 0; i < id.length; i++) {
            object = pesquisaCodigo(id[i], tipo);
            if(object != null) {
                list.add(object);
            }   
        }
        return list;
    }

    @Override
    public Object pesquisaObjeto(int id, String tabela) {
        Object result = null;
        try {
            Query qry = getEntityManager().createQuery("SELECT OB FROM " + tabela + " OB WHERE OB.id = " + id);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return result;
    }
    
    @Override
    public List pesquisaObjeto(int id[], String tabela) {
        List<Object> list = new ArrayList<Object>(); 
        Object object; 
        for (int i = 0; i < id.length; i++) {
            object = pesquisaObjeto(id[i], null);
            if(object != null) {
                list.add(object);
            }   
        }
        return list;
    }    

    @Override
    public List listaObjeto(String tabela) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT OB FROM " + tabela + " AS OB");
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }
    
    @Override
    public List listaObjeto(String tabela, boolean order) {
        try {
            Query query = getEntityManager().createNamedQuery(tabela + ".findAll");
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
    public List pesquisaObjetoPorDescricao(String tabela, String descricao) {
        return pesquisaObjetoPorDescricao(tabela, descricao, "");
    }    
    
    @Override
    public List pesquisaObjetoPorDescricao(String tabela, String descricao, String tipoPesquisa) {
        try {
            Query query = getEntityManager().createNamedQuery(tabela + ".findName");
            if (tipoPesquisa.equals("i")) {
                query.setParameter("pdescricao", ""+descricao.toUpperCase()+"%");                
            } else if (tipoPesquisa.equals("p")) {
                query.setParameter("pdescricao", "%"+descricao.toUpperCase()+"%");                
            } else if (tipoPesquisa.equals("all")) {
                query.setParameter("pdescricao", "%"+descricao.toUpperCase()+"%");                
                query.setParameter("pdescricao", ""+descricao.toUpperCase()+"%");
            } else {
                query.setParameter("pdescricao", descricao.toUpperCase());
            }
            if (descricao.length() <= 1) {
                query.setMaxResults(1000);
            }
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
    public List listaObjetoGenericoOrdem(String tabela, String descricao) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT ob FROM " + tabela + " ob WHERE UPPER(ob.descricao) LIKE '%" + descricao.toUpperCase() + "%' ORDER BY ob.descricao ASC");
            qry.setMaxResults(100);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }
    
    public List listaObjetoGenericoOrdemDesc(String tabela, String descricao) {
        List result = new ArrayList();
        try {
            Query qry = getEntityManager().createQuery("SELECT ob FROM " + tabela + " ob WHERE UPPER(ob.descricao) LIKE '%" + descricao.toUpperCase() + "%' ORDER BY ob.descricao DESC");
            qry.setMaxResults(100);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
        }
        return result;
    }

    @Override
    public boolean executeQuery(String textQuery) {
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
