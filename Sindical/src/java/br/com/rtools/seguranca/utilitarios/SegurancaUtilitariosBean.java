package br.com.rtools.seguranca.utilitarios;

import br.com.rtools.seguranca.MacFilial;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SegurancaUtilitariosBean implements Serializable {

    public boolean getExisteMacFilial() {
        return MacFilial.getAcessoFilial().getId() != -1;
    }

}
