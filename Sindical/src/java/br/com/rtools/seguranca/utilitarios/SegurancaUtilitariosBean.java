package br.com.rtools.seguranca.utilitarios;

import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class SegurancaUtilitariosBean implements Serializable {

    private Registro registro;

    @PostConstruct
    public void init() {
        registro = new Registro();
    }

    public boolean getExisteMacFilial() {
        return MacFilial.getAcessoFilial().getId() != -1;
    }
    
    public boolean getExisteMacFilialComCaixa() {
        MacFilial macFilial = MacFilial.getAcessoFilial();
        if(macFilial.getId() != -1 && macFilial.getCaixa() != null) {
            return true;
        }
        return false;            
    }

    public Usuario getSessaoUsuario() {
        return (Usuario) GenericaSessao.getObject("sessaoUsuario");
    }

    public Registro getRegistro() {
        if (registro.getId() == -1) {
            registro = (Registro) new Dao().find(new Registro());
            if (registro == null) {
                registro = new Registro();
            }
        }
        return registro;
    }

}
