package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.Horarios;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class HomologacoesBean implements Serializable {

    private MacFilial macFilial = new MacFilial();
    private List<SelectItem> listaStatus = new ArrayList<SelectItem>();
    private List<Horarios> listaHorariosDisponiveis = new ArrayList<Horarios>();
    private List<Horarios> listaHorarios = new ArrayList<Horarios>();
    private String mensagem = "";

    public MacFilial getMacFilial() {
        if (macFilial.getId() == -1) {
            if (GenericaSessao.exists("acessoFilial")) {
                macFilial = (MacFilial) GenericaSessao.getObject("acessoFilial");
            }
        }
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<SelectItem> getListaStatus() {
        return listaStatus;
    }

    public void setListaStatus(List<SelectItem> listaStatus) {
        this.listaStatus = listaStatus;
    }

    public List<Horarios> getListaHorarios() {
        return listaHorarios;
    }

    public void setListaHorarios(List<Horarios> listaHorarios) {
        this.listaHorarios = listaHorarios;
    }

    public List<Horarios> getListaHorariosDisponiveis() {
        return listaHorariosDisponiveis;
    }

    public void setListaHorariosDisponiveis(List<Horarios> listaHorariosDisponiveis) {
        this.listaHorariosDisponiveis = listaHorariosDisponiveis;
    }

}
