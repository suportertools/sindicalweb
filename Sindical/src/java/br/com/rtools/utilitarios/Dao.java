package br.com.rtools.utilitarios;

import br.com.rtools.principal.DB;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Query;

public class Dao extends DB {

    public void openTransaction() {
        getEntityManager().getTransaction().begin();
    }

    public void commit() {
        getEntityManager().getTransaction().commit();
    }

    public void rollback() {
        getEntityManager().getTransaction().rollback();
    }

    public void flush() {
        getEntityManager().flush();
    }

    public boolean activeSession() {
        return getEntityManager().getTransaction().isActive();
    }

    public boolean save(final Object object) {
        if (!activeSession()) {
            return false;
        }
        try {
            getEntityManager().persist(object);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public boolean save(final Object object, boolean transactionComplete) {
        if (activeSession()) {
            return false;
        }
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().persist(object);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public boolean update(final Object objeto) {
        if (!activeSession()) {
            return false;
        }
        Class classe = objeto.getClass();
        int id;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            id = (Integer) metodo.invoke(objeto, (Object[]) null);
            if (id == -1) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Objeto esta passando -1");
                return false;
            }
        } catch (IllegalAccessException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (NoSuchMethodException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (SecurityException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
        try {
            getEntityManager().merge(objeto);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public boolean update(final Object objeto, boolean transactionComplete) {
        if (activeSession()) {
            return false;
        }
        Class classe = objeto.getClass();
        int id;
        try {
            Method metodo = classe.getMethod("getId", new Class[]{});
            id = (Integer) metodo.invoke(objeto, (Object[]) null);
            if (id == -1) {
                Logger.getLogger(this.getClass().getName()).log(Level.WARNING, "Objeto esta passando -1");
                return false;
            }
        } catch (IllegalAccessException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (IllegalArgumentException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (NoSuchMethodException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (SecurityException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        } catch (InvocationTargetException e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().merge(objeto);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public boolean delete(final Object object) {
        if (!activeSession()) {
            return false;
        }
        try {
            getEntityManager().remove(object);
            getEntityManager().flush();
            return true;
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public boolean delete(final Object object, boolean transactionComplete) {
        if (activeSession()) {
            return false;
        }
        try {
            getEntityManager().getTransaction().begin();
            getEntityManager().remove(object);
            getEntityManager().flush();
            getEntityManager().getTransaction().commit();
            return true;
        } catch (Exception e) {
            getEntityManager().getTransaction().rollback();
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return false;
        }
    }

    public Object rebind(Object object) {
        try {
            openTransaction();
            object = find(object);
            getEntityManager().merge(object);
            getEntityManager().refresh(object);
            getEntityManager().flush();
            commit();
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
        }
        return object;
    }

    public void refresh(Object object) {
        try {
            openTransaction();
            object = find(object);
            getEntityManager().merge(object);
            getEntityManager().refresh(object);
            if (!getEntityManager().getTransaction().isActive()) {
                return;
            }
            getEntityManager().flush();
            commit();
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
        }
    }

    public Object find(final Object object) {
        return find(object, null);
    }

    public Object find(Object object, final Object objectId) {
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
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            } catch (IllegalArgumentException e) {
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            } catch (NoSuchMethodException e) {
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            } catch (SecurityException e) {
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            } catch (InvocationTargetException e) {
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            }
            object = getEntityManager().find(object.getClass(), id);
        } else {
            try {
                object = getEntityManager().find(object.getClass(), objectId);
            } catch (Exception e) {
                Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
                return null;
            }
        }
        return object;
    }

    public List list(Object className) {
        String name = className.getClass().getSimpleName();
        return list(name);
    }

    public List list(String className) {
        List result = new ArrayList();
        String queryString = "SELECT OB FROM " + className + " AS OB";
        try {
            Query qry = getEntityManager().createQuery(queryString);
            List list = qry.getResultList();
            if (!list.isEmpty()) {
                result = qry.getResultList();
            }
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
        }
        return result;
    }

    public List list(Object className, boolean order) {
        return list(className.getClass().getSimpleName(), order);
    }

    public List list(String className, boolean order) {
        try {
            Query query = getEntityManager().createNamedQuery(className + ".findAll");
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return new ArrayList();
        }
        return new ArrayList();
    }

    public List listQuery(String className, String find, String[] params) {
        try {
            Query query = getEntityManager().createNamedQuery(className + "." + find);
            int y = 1;
            for (String param : params) {
                query.setParameter("p" + y, param);
                y++;
            }
            List list = query.getResultList();
            if (!list.isEmpty()) {
                return list;
            }
        } catch (Exception e) {
            Logger.getLogger(Dao.class.getName()).log(Level.WARNING, e.getMessage());
            return new ArrayList();
        }
        return new ArrayList();
    }
}
