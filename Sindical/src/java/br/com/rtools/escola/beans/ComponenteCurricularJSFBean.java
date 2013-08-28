

package br.com.rtools.escola.beans;


import br.com.rtools.escola.ComponenteCurricular;
import br.com.rtools.escola.db.ComponenteCurricularDB;
import br.com.rtools.escola.db.ComponenteCurricularDBToplink;
import java.util.List;
import javax.faces.context.FacesContext;

public class ComponenteCurricularJSFBean {
    private ComponenteCurricular componenteCurricular;
    private String comoPesquisa;
    private String descPesquisa;
    private String msgConfirma;
    
    public ComponenteCurricularJSFBean(){
        componenteCurricular = new ComponenteCurricular();
        comoPesquisa = "";
        descPesquisa = "";
    }

    public String novo(){
        componenteCurricular = new ComponenteCurricular();
        return "componenteCurricular";
    }

    public String salvar(){
        ComponenteCurricularDB componenteCurricularDB = new ComponenteCurricularDBToplink();
        if (componenteCurricular.getId()==-1){
            if (componenteCurricular.getDescricao().equals("")){
                msgConfirma = "Digite o nome do componente curricular!";
            }else{
                if (componenteCurricularDB.idComponenteCurricular(componenteCurricular) == null){
                   if (componenteCurricularDB.insert(componenteCurricular)){
                       msgConfirma = "Cadastro efetuado com sucesso!";
                   }else{
                       msgConfirma = "Erro! Cadastro não foi efetuado.";
                   }
                }else{
                   msgConfirma = "Já existe um componente curricular com esse nome.";
                }
            }
        }else{
            if (componenteCurricularDB.update(componenteCurricular)){       
                msgConfirma = "Cadastro atualizado com sucesso!";
            }else{
            }
        }
        componenteCurricular = new ComponenteCurricular();
        return null;
    }

   public String excluir(){
       ComponenteCurricularDB componenteCurricularDB = new ComponenteCurricularDBToplink();
       if (componenteCurricular.getId()!= -1){
           componenteCurricular = componenteCurricularDB.pesquisaCodigo(componenteCurricular.getId());
           if (componenteCurricularDB.delete(componenteCurricular)){
               msgConfirma = "Cadastro excluído com sucesso!";
           }else{
               msgConfirma = "Erro! Cadastro não foi excluído.";
           }
       }else{
           msgConfirma = "Não há registro para excluir.";
       }
       componenteCurricular = new ComponenteCurricular();
       return null;
   }


    public String editar(){
        //componenteCurricular = (ComponenteCurricular) getHtmlTable().getRowData();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("componenteCurricularPesquisa", componenteCurricular);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
        descPesquisa = "";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null)
            return "componenteCurricular";
        else
            return (String)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public void acaoPesquisaInicial(){
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial(){
        comoPesquisa = "P";
    }

    public List getListaComponenteCurricular(){
        //Pesquisa pesquisa = new Pesquisa();
        List result = null;
        //result = pesquisa.pesquisar("ComponenteCurricular", "descricao" , getDescPesquisa(), "descricao", getComoPesquisa());
        return result;
    }

    public void refreshForm(){

    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
}