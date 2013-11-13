package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CnaeBean implements Serializable {
    private Cnae cnae = new Cnae();
    private String msgConfirma;
    private int idIndex = -1;
    private List<Cnae> listaCnae = new ArrayList();
    
    public String novo(){
        cnae = new Cnae();
        listaCnae.clear();
        return "cnae";
    }

    public Cnae getCnae() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado")!= null){
            cnae = (Cnae)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
        }
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CnaeDB db = new CnaeDBToplink();
        if (getCnae().getCnae().isEmpty()){
            setMsgConfirma("Digite um Cnae!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        if (getCnae().getNumero().isEmpty()){
            setMsgConfirma("Digite o Número do Cnae!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        if(db.idCnae(getCnae()) != null){
            setMsgConfirma("Este Cnae já existe no Sistema!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        sv.abrirTransacao();
        if (getCnae().getId() == -1){
            if (sv.inserirObjeto(cnae)){
                setMsgConfirma("Cnae salvo com sucesso");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                listaCnae.clear();
                sv.comitarTransacao();
            }else{
                setMsgConfirma("Erro ao salvar Cnae!");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
            }
        }else{
            if (sv.alterarObjeto(cnae)){
                setMsgConfirma("Cnae atualizada com sucesso.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                listaCnae.clear();
                sv.comitarTransacao();
            }else{
                setMsgConfirma("Erro ao atualizar Cnae!");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String excluir(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (cnae.getId()!= -1){
            sv.abrirTransacao();
            if ( sv.deletarObjeto(sv.pesquisaCodigo(cnae.getId(), "Cnae")) ){
                setMsgConfirma("Cadastro excluído com sucesso!");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                cnae = new Cnae();
                listaCnae.clear();
                sv.comitarTransacao();
            }else{
                setMsgConfirma("Erro! Cadastro não foi excluído.");
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            }
        }else{
            setMsgConfirma("Não há registro para excluir.");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
        }
        return null;
    }

    public String editar(){
        setCnae((Cnae) getListaCnae().get(getIdIndex()));
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cnaePesquisado", getCnae());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
        setCnae(new Cnae());
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null){
            return "cnae";
        }else{
            return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public List<Cnae> getListaCnae() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        listaCnae = sv.listaObjeto("Cnae");
        return listaCnae;
    }

    public void setListaCnae(List<Cnae> listaCnae) {
        this.listaCnae = listaCnae;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}