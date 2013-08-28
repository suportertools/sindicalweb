package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConvenioServico;
import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.ConvenioServicoDB;
import br.com.rtools.associativo.db.ConvenioServicoDBToplink;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.DataObject;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class SubGrupoConvenioJSFBean {
    private SubGrupoConvenio subGrupoConvenio = new SubGrupoConvenio();
    private GrupoConvenio grupoConvenio = new GrupoConvenio();
    private List<SubGrupoConvenio> listaSubGrupoConvenio = new ArrayList<SubGrupoConvenio>();
    private String msgConfirma;
    private String comoPesquisa = "I";
    private String descPesquisa = "";
    private List listaDisponiveis = new ArrayList();
    private List listaAdicionados = new ArrayList();
    private boolean disabled = true;
    private int idIndexSub = -1;
    
    public void adicionar(){
        SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        this.subGrupoConvenio.setGrupoConvenio(grupoConvenio);
        if (subGrupoConvenio.getId() == -1){
            if (db.pesquisaSubGrupoConvenioPorDescricao(getSubGrupoConvenio().getDescricao()).isEmpty()){
                if (!getSubGrupoConvenio().getDescricao().isEmpty()){
                    if (db.insert(subGrupoConvenio)){
                        setMsgConfirma("SubGrupo do convênio cadastrado com sucesso!");
                    }else{
                        setMsgConfirma("Erro ao cadastrar subgrupo do convênio!");
                    }
                }else{
                    setMsgConfirma("SubGrupo inválido!");
                }
            }else{
                setMsgConfirma("SubGrupo já existe!");
            }
        }else{
            if (db.pesquisaSubGrupoConvenioPorDescricao(getSubGrupoConvenio().getDescricao()).isEmpty()){
                if (!getSubGrupoConvenio().getDescricao().isEmpty()){
                    if (db.update(subGrupoConvenio)){
                        setMsgConfirma("SubGrupo do convênio Atualizado com sucesso!");
                    }else{
                        setMsgConfirma("Erro ao atualizar subgrupo do convênio!");
                    }
                }else{
                    setMsgConfirma("SubGrupo inválido!");
                }
            }else{
                setMsgConfirma("SubGrupo já existe!");
            }
        }
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
    }   

    public String novo(){
        subGrupoConvenio = new SubGrupoConvenio();
        setMsgConfirma("");
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
        return "subGrupoConvenio";
    }

    public String novoGrupo(){
        this.setGrupoConvenio(new GrupoConvenio());
        subGrupoConvenio = new SubGrupoConvenio();
        setMsgConfirma("");
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
        return "subGrupoConvenio";
    }

    public void excluir(){
        subGrupoConvenio = (SubGrupoConvenio)listaSubGrupoConvenio.get(idIndexSub);
        SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        if (getSubGrupoConvenio().getId() != -1){
            setSubGrupoConvenio(db.pesquisaCodigo(getSubGrupoConvenio().getId()));
            if (db.delete(getSubGrupoConvenio())){
                setMsgConfirma("SubGrupo do convênio excluido com sucesso!");
            }else{
                setMsgConfirma("SubGrupo convênio não pode ser Excluido!");
            }
        }
        setSubGrupoConvenio(new SubGrupoConvenio());        
    }

    public boolean desabilitaSubGrupo(){
        if (getGrupoConvenio().getId() == -1){
            return false;
        }else{
            return true;
        }
    }

    public List getListaServicosDisponiveis(){
        DataObject dt = null;
        SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        if (listaDisponiveis.isEmpty()){
            if (subGrupoConvenio.getId() != -1){
                List result = db.pesquisaSubGrupoConvênioComServico(subGrupoConvenio.getId());
                for (int i = 0; i < result.size();i++){
                    dt = new DataObject(new Boolean(false), (Servicos)result.get(i));
                    listaDisponiveis.add(dt);
                }
                disabled = false;
            }else{
                List result = dbs.pesquisaTodos();
                for (int i = 0; i < result.size();i++){
                    dt = new DataObject(new Boolean(false), (Servicos)result.get(i));
                    listaDisponiveis.add(dt);
                }
                disabled = true;
            }
        }
        return listaDisponiveis;
    }

    public List getListaServicosAdicionados(){
        DataObject dt = null;
        SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        if (listaAdicionados.isEmpty()){
            if (subGrupoConvenio.getId() != -1){
                List result = db.pesquisaSubGrupoConvênioSemServico(subGrupoConvenio.getId());
                for (int i = 0; i < result.size();i++){
                    dt = new DataObject(new Boolean(false), (Servicos)result.get(i));
                    listaAdicionados.add(dt);
                }
            }
        }
        return listaAdicionados;
    }

    public String adicionarServicos(){
        //SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        ConvenioServicoDB db = new ConvenioServicoDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        ConvenioServico convenioServico = new ConvenioServico();
        if (grupoConvenio.getId() != -1){
            for (int i = 0; i < listaDisponiveis.size(); i++){
                if ( ((Boolean)((DataObject)listaDisponiveis.get(i)).getArgumento0()) == true ){
                    convenioServico.setServicos( dbs.pesquisaCodigo(((Servicos)((DataObject)listaDisponiveis.get(i)).getArgumento1()).getId()) );
                    convenioServico.setSubGrupoConvenio(subGrupoConvenio);
                    db.insert(convenioServico);
                    convenioServico = new ConvenioServico();
                }
            }
        }
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
        return "subGrupoConvenio";
    }

    public String removerServicos(){
        //SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
        ConvenioServicoDB db = new ConvenioServicoDBToplink();
        ServicosDB dbs = new ServicosDBToplink();
        ConvenioServico convenioServico = new ConvenioServico();
        if (grupoConvenio.getId() != -1){
            for (int i = 0; i < listaAdicionados.size(); i++){
                if ( ((Boolean)((DataObject)listaAdicionados.get(i)).getArgumento0()) == true ){
                    convenioServico = db.pesquisaConvenioServicoPorServESubGrupo(((Servicos)((DataObject)listaAdicionados.get(i)).getArgumento1()).getId(),
                                                                           subGrupoConvenio.getId());
                    db.delete(convenioServico);
                    convenioServico = new ConvenioServico();
                }
            }
        }
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
        return "subGrupoConvenio";
    }

    public String editar(){
        subGrupoConvenio = (SubGrupoConvenio)listaSubGrupoConvenio.get(idIndexSub);
        listaDisponiveis = new ArrayList();
        listaAdicionados = new ArrayList();
        return "subGrupoConvenio";
    }

    public void refreshForm(){

    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
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

    public SubGrupoConvenio getSubGrupoConvenio() { 
        return subGrupoConvenio;
    }

    public void setSubGrupoConvenio(SubGrupoConvenio subGrupoConvenio) {
        this.subGrupoConvenio = subGrupoConvenio;
    }

    public List<SubGrupoConvenio> getListaSubGrupoConvenio() {
        SubGrupoConvenioDB subGrupoConvenioDB = new SubGrupoConvenioDBToplink();
        listaSubGrupoConvenio = subGrupoConvenioDB.pesquisaSubGrupoConvênioPorGrupo(this.grupoConvenio.getId());
        if (listaSubGrupoConvenio ==  null){
            listaSubGrupoConvenio = new ArrayList();
        }
        return listaSubGrupoConvenio;
    }

    public GrupoConvenio getGrupoConvenio() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa") != null){
            grupoConvenio = (GrupoConvenio)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        }
        return grupoConvenio;
    }

    public void setGrupoConvenio(GrupoConvenio grupoConvenio) {
        this.grupoConvenio = grupoConvenio;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getIdIndexSub() {
        return idIndexSub;
    }

    public void setIdIndexSub(int idIndexSub) {
        this.idIndexSub = idIndexSub;
    }

}