package br.com.rtools.utilitarios;

import java.io.IOException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@RequestScoped
public class Polling {
    
    private boolean stop = false;

    public void existeUsuarioSessao() throws IOException {
        if (!GenericaSessao.exists("sessaoUsuario")) {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/Sindical/indexLogin.jsf");
        }
    }
    
    public void habilita() {
        stop = GenericaSessao.exists("sessaoUsuario");
    }

    public boolean isStop() {
        return stop;
    }

    public void setStop(boolean stop) {
        this.stop = stop;
    }

}
