package br.com.rtools.utilitarios;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class GenericaSessao implements Serializable {

    public static void put(String sessionName, String sessionValue) {
        if (GenericaSessao.exists(sessionName)) {
            GenericaSessao.remove(sessionName);
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(sessionName, sessionValue);
    }

    public void putString(String sessionName, String sessionValue) {
        if (GenericaSessao.exists(sessionName)) {
            GenericaSessao.remove(sessionName);
        }
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(sessionName, sessionValue);
    }

    public static void put(String sessionName, Object sessionValue) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(sessionName, sessionValue);
    }

    public static String getString(String sessionName) {
        return getString(sessionName, false);
    }

    public static String getString(String sessionName, boolean remove) {
        String string = "";
        if (exists(sessionName)) {
            string = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            if (remove) {
                remove(sessionName);
            }
        }
        return string;
    }

    public static String[] getStringVector(String sessionName) {
        return getStringVector(sessionName, false);
    }

    public static String[] getStringVector(String sessionName, boolean remove) {
        String[] string = new String[]{};
        if (exists(sessionName)) {
            string = (String[]) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            if (remove) {
                remove(sessionName);
            }
        }
        return string;
    }

    public static boolean getBoolean(String sessionName) {
        return getBoolean(sessionName, false);
    }

    public static boolean getBoolean(String sessionName, boolean remove) {
        boolean is = (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
        if (exists(sessionName)) {
            if (remove) {
                remove(sessionName);
            }
        }
        return is;
    }

    public static Object getObject(String sessionName) {
        return getObject(sessionName, false);
    }

    public static Object getObject(String sessionName, boolean remove) {
        Object object = null;
        if (exists(sessionName)) {
            object = (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            if (remove) {
                remove(sessionName);
            }
        }
        return object;
    }

    public static List getList(String sessionName) {
        return getList(sessionName, false);
    }

    public static List getList(String sessionName, boolean remove) {
        List list = new ArrayList();
        if (exists(sessionName)) {
            list = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            if (remove) {
                remove(sessionName);
            }
        }
        return list;
    }

    public static Integer getInteger(String sessionName) {
        return getInteger(sessionName, false);
    }

    public static Integer getInteger(String sessionName, boolean remove) {
        Integer integer = -1;
        if (exists(sessionName)) {
            integer = (Integer) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            if (remove) {
                remove(sessionName);
            }
        }
        return integer;
    }

    public static void remove(String sessionName) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(sessionName);
    }

    public static void remove(List list) {
        for (Object list1 : list) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(list1.toString());
        }
    }

    public static boolean exists(String sessionName) {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName) != null;
    }
}
