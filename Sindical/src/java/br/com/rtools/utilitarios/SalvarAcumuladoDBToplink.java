package br.com.rtools.utilitarios;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.principal.DB;
import br.com.rtools.seguranca.Usuario;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Query;

public class SalvarAcumuladoDBToplink extends DB implements SalvarAcumuladoDB {

    public static Exception EXCEPCION = null;

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
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            return false;
        }
    }

    @Override
    public boolean alterarObjeto(Object objeto) {
        Class classe = objeto.getClass();
        int id;
        NovoLog log = new NovoLog();
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            id = (Integer) metodo.invoke(objeto, (Object[]) null);
            if (id == -1) {
                //log.novo("Alterar " + objeto.getClass().getSimpleName(), "Objeto esta passando -1");
                return false;
            }
        } catch (IllegalAccessException e) {
            //log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            //log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        } catch (NoSuchMethodException e) {
            //log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        } catch (SecurityException e) {
            //log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            //log.novo("Alterar Objeto", "Exception - Message: " + e.getMessage());
            return false;
        }
        try {
            getEntityManager().merge(objeto);
            if (!getEntityManager().getTransaction().isActive()) {
                return false;
            }
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
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
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            return false;
        }
    }

    /**
     * @deprecated @param id
     * @param tipo
     * @return
     */
    @Override
    public Object pesquisaCodigo(int id, String tipo) {
        if (id == -1) {
            return null;
        }
        Object result = null;
        try {
            Query qry = getEntityManager().createNamedQuery(tipo + ".pesquisaID");
            qry.setParameter("pid", id);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            e.getMessage();
        }
        return result;
    }

    /**
     * @deprecated @param id
     * @param tipo
     * @return
     */
    @Override
    public List pesquisaCodigo(int id[], String tipo) {
        List<Object> list = new ArrayList<Object>();
        Object object;
        for (int i = 0; i < id.length; i++) {
            object = pesquisaCodigo(id[i], tipo);
            if (object != null) {
                list.add(object);
            }
        }
        return list;
    }

    @Override
    public Object pesquisaObjeto(int id, String className) {
        return pesquisaObjeto(id, className, "");
    }

    @Override
    public Object find(Object object) {
        return find(object, null);
    }

    @Override
    public Object find(Object object, Object objectId) {
        if (object == null) {
            return null;
        }
        if (objectId == null) {
            int id;
            try {
                Class classe = object.getClass();
                Method metodo = classe.getMethod("getId", new Class[]{});
                id = (Integer) metodo.invoke(object, (Object[]) null);
                if (id == -1) {
                    return null;
                }
            } catch (IllegalAccessException e) {
                return null;
            } catch (IllegalArgumentException e) {
                return null;
            } catch (NoSuchMethodException e) {
                return null;
            } catch (SecurityException e) {
                return null;
            } catch (InvocationTargetException e) {
                return null;
            }
            object = getEntityManager().find(object.getClass(), id);
        } else {
            try {
                object = getEntityManager().find(object.getClass(), objectId);
            } catch (Exception e) {
                EXCEPCION = e;
                if (GenericaSessao.exists("habilitaLog")) {
                    if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                        GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                        PF.update("form_log:i_messages");
                    }
                }
                return null;
            }
        }
        return object;
    }

    @Override
    public Object find(String object, int objectId) {
        return pesquisaObjeto(objectId, object);
    }

    @Override
    public Object find(String object, Integer objectId) {
        return pesquisaObjeto(objectId, object);
    }

//    @Override
//    public Object find(Object object, long objectId) {
//        return find(objectId, object);
//    }
    @Override
    public Object pesquisaObjeto(int id, String className, String field) {
        if (id == -1) {
            return null;
        }
        String stringCampo = "id";
        if (!field.isEmpty()) {
            stringCampo = field;
        }
        Object result = null;
        try {
            Query qry = getEntityManager().createQuery("SELECT OB FROM " + className + " OB WHERE OB." + stringCampo + " = " + id);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getSingleResult();
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            e.getMessage();
        }
        return result;
    }

    @Override
    public List pesquisaObjeto(int id[], String className) {
        return pesquisaObjeto(id, className, "");
    }

    @Override
    public List pesquisaObjeto(int id[], String className, String field) {
        String stringCampo = "id";
        if (!field.isEmpty()) {
            stringCampo = field;
        }
        List list = new ArrayList<Object>();
        String queryPesquisaString = "";
        // Object object;
        for (int i = 0; i < id.length; i++) {
            if (i == 0) {
                queryPesquisaString = Integer.toString(id[i]);
            } else {
                queryPesquisaString += ", " + Integer.toString(id[i]);
            }
            String queryString = "SELECT OB FROM " + className + " OB WHERE OB." + stringCampo + " IN (" + queryPesquisaString + ")";
            Query query = getEntityManager().createQuery(queryString);
            list = query.getResultList();
            if (list.isEmpty()) {
                return list;
            }
        }
        return list;
    }

    @Override
    public List listaObjeto(String className) {
        List result = new ArrayList();
        String queryString = "SELECT OB FROM " + className + " AS OB";
        try {
            Query qry = getEntityManager().createQuery(queryString);
            if (!qry.getResultList().isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List listaObjeto(String className, boolean order) {
        try {
            Query query = getEntityManager().createNamedQuery(className + ".findAll");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
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
                query.setParameter("pdescricao", "" + descricao.toUpperCase() + "%");
            } else if (tipoPesquisa.equals("p")) {
                query.setParameter("pdescricao", "%" + descricao.toUpperCase() + "%");
            } else if (tipoPesquisa.equals("all")) {
                query.setParameter("pdescricao", "%" + descricao.toUpperCase() + "%");
                query.setParameter("pdescricao", "" + descricao.toUpperCase() + "%");
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
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
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
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
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
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", "Exception - Message: Erro ao executar: " + textQuery);
                    PF.update("form_log:i_messages");
                }
                return false;
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            return false;
        }
    }

    @Override
    public boolean executeQueryObject(String textQuery) {
        try {
            Object xvalor = getEntityManager().createNativeQuery(textQuery).getSingleResult();
            if (xvalor != null) {
                return true;
            } else {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", "Exception - Message: Erro ao executar: " + textQuery);
                    PF.update("form_log:i_messages");
                }
                return false;
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            return false;
        }
    }

    @Override
    public boolean executeQueryVetor(String textQuery) {
        try {
            List<List> valor = getEntityManager().createNativeQuery(textQuery).getResultList();
            if ((Integer) valor.get(0).get(0) > 0) {
                return true;
            } else {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", "Exception - Message: Erro ao executar: " + textQuery);
                    PF.update("form_log:i_messages");
                }
                return false;
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
            return false;
        }
    }

    @Override
    public List nativeQuery(String textQuery) {
        return nativeQuery(textQuery, false);
    }

    @Override
    public List nativeQuery(String textQuery, boolean singleResult) {
        try {
            Query query = getEntityManager().createNativeQuery(textQuery);
            if (singleResult) {
                return (List) query.getSingleResult();
            } else {
                return query.getResultList();
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
        }
        return new ArrayList();
    }

    @Override
    public List objectQuery(String textQuery) {
        return objectQuery(textQuery, false);
    }

    @Override
    public List objectQuery(String textQuery, boolean singleResult) {
        try {
            Query query = getEntityManager().createQuery(textQuery);
            if (singleResult) {
                return (List) query.getSingleResult();
            } else {
                return query.getResultList();
            }
        } catch (Exception e) {
            EXCEPCION = e;
            if (GenericaSessao.exists("habilitaLog")) {
                if (Usuario.getUsuario().getId() == 1 && GenericaSessao.getBoolean("habilitaLog")) {
                    GenericaMensagem.fatal("LOG", EXCEPCION.getMessage());
                    PF.update("form_log:i_messages");
                }
            }
        }
        return new ArrayList();
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

    @Override
    public void fecharTransacao() {
        getEntityManager().close();
    }

//    @Override
//    public List<Object> selectTranslateNative(String table, String classe, String field, String and, String value_search){
//        //value_search = value_search.replaceAll(" ","_");  
//        value_search = Normalizer.normalize(value_search, Normalizer.Form.NFD);  
//        value_search = value_search.toLowerCase().replaceAll("[^\\p{ASCII}]", "");  
//        
//        //value_search = value_search.toLowerCase().replaceAll("[^\\p{ASCII}]", "");
//        String text_qry = "SELECT id FROM "+table+" WHERE LOWER(TRANSLATE("+field+")) LIKE '"+value_search+"'";
//        
//        if (!and.isEmpty())
//            text_qry = "SELECT id FROM "+table+" WHERE LOWER(TRANSLATE("+field+")) LIKE '"+value_search+"' " + and;
//        
//        Query qry = getEntityManager().createNativeQuery(text_qry);
//        
//        List<Vector> result_list = qry.getResultList();
//        List<Object> return_list = new ArrayList<Object>();
//
//        for (Vector list : result_list){
//            return_list.add((Object) new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer)list.get(0), classe));
//        }
//        return return_list;
//    }
}
