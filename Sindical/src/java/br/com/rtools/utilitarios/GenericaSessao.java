package br.com.rtools.utilitarios;

import javax.faces.context.FacesContext;

public class GenericaSessao {

    public static void put(String sessionName, String sessionValue) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(sessionName, sessionValue);
    }

    public static void put(String sessionName, Object sessionValue) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(sessionName, sessionValue);
    }

    public static String getString(String sessionName) {
        String string = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName) != null) {
            string = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(sessionName);
        }
        return string;
    }

    public static String getString(String sessionName, boolean remove) {
        String string = GenericaSessao.getString(sessionName);
        if (remove) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(sessionName);
        }
        return string;
    }

    public static Object getObject(String sessionName) {
        Object object = new Object();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName) != null) {
            object = (Object) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName);
        }
        return object;
    }

    public static Object getObject(String sessionName, boolean remove) {
        Object object = GenericaSessao.getObject(sessionName);
        if (remove) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(sessionName);
        }
        return object;
    }

    public static void remove(String sessionName) {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove(sessionName);
    }

    public static boolean exists(String sessionName) {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get(sessionName) != null) {
            return true;
        } else {
            return false;
        }
    }
}
