package br.com.rtools.utilitarios;

import java.util.List;
import javax.faces.context.FacesContext;

public class GenericaSessao {

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
        for (int i = 0; i < list.size(); i++) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(list.get(i).toString());
        }
    }

    public static boolean exists(String sessionName) {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName) != null) {
            return true;
        } else {
            return false;
        }
    }
}
