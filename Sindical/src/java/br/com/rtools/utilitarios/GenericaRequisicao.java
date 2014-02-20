package br.com.rtools.utilitarios;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class GenericaRequisicao {

    public static String getParametro(String parameter) {
        HttpServletRequest hsr = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String param = hsr.getParameter(parameter);
        return param;
    }

}
