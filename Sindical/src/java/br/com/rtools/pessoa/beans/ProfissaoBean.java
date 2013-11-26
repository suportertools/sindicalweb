package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.db.ProfissaoDB;
import br.com.rtools.pessoa.db.ProfissaoDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ProfissaoBean extends PesquisarProfissaoBean implements Serializable {
private Profissao prof = new Profissao();
    private String msgConfirma;
    private String s_cbo = "";
    private String s_profissao = "";
    private int idIndexProfissao = -1;
    
    public String novaProfissao(){
        prof = new Profissao();
        super.profissao = new Profissao();
        return "profissao";
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
    
    public void editarProfissao(Profissao pr){
        //prof = (Profissao) super.getListaProfissao().get(super.getIdIndexProf());
        prof = pr;//(Profissao) super.getListaProfissao().get(super.getIdIndexProf());
        super.getListaProfissao().clear();
    }

    public String salvarProfissao(){

        if (prof.getProfissao().equals("")){
            msgConfirma = "Digite a profissão!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        ProfissaoDB db = new ProfissaoDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (prof.getId() == -1){
            if(sv.descricaoExiste(prof.getProfissao(), "profissao", "Profissao")){
                msgConfirma = "Profissão já cadastrada!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                return null;
            }
            if(!prof.getCbo().equals("")){
                if(sv.descricaoExiste(prof.getCbo(), "cbo", "Profissao")){
                    msgConfirma = "CBO já existe!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                    return null;
                }
            }
            if (sv.inserirObjeto(prof)){
                msgConfirma = "Profissão salva com sucesso";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
            }else{
                msgConfirma = "Erro ao salvar profissão!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
                return null;
            }
        }else{
            if (sv.alterarObjeto(prof)){
                msgConfirma = "Profissão atualizada com sucesso";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
            }else{
                msgConfirma = "Erro ao atualizar profissão!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
                return null;
            }
        }
        super.getListaProfissao().clear();
        prof = new Profissao();
        sv.comitarTransacao();
        return null;
    }
    
    public String excluirProfissao(){
        
        if (prof.getId() == -1){
            msgConfirma = "Selecione uma profissão para ser excluída!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        if (sv.deletarObjeto(prof = (Profissao) sv.pesquisaCodigo(prof.getId(), "Profissao"))){
            msgConfirma = "Profissão deletada com sucesso!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
            sv.comitarTransacao();
        }else{
            msgConfirma = "Erro ao deletar profissão!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            sv.desfazerTransacao();
        }
        super.getListaProfissao().clear();
        prof = new Profissao();
        return null;
    }
    
    public int getIdIndexProfissao() {
        return idIndexProfissao;
    }

    public void setIdIndexProfissao(int idIndexProfissao) {
        this.idIndexProfissao = idIndexProfissao;
    }

    public String getS_cbo() {
        s_cbo = super.profissao.getCbo();
        return s_cbo;
    }

    public void setS_cbo(String s_cbo) {
        this.s_cbo = s_cbo;
    }

    public String getS_profissao() {
        s_profissao = super.profissao.getProfissao();
        return s_profissao;
    }

    public void setS_profissao(String s_profissao) {
        this.s_profissao = s_profissao;
    }

    public Profissao getProf() {
        return prof;
    }

    public void setProf(Profissao prof) {
        this.prof = prof;
    }
}