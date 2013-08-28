package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.CentroComercial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.db.CentroComercialDB;
import br.com.rtools.pessoa.db.CentroComercialDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class CentroComercialJSFBean {
    private CentroComercial centroComercial= new CentroComercial();
    private String msgConfirma = "";
    private int idTipos = 0;
    private int idIndex = -1;
    private List<SelectItem> tipos = new Vector<SelectItem>();
    private List<CentroComercial> listaCentros = new ArrayList();

    public String salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CentroComercialDB db = new CentroComercialDBToplink();
        if (centroComercial.getJuridica().getId() == -1){
            msgConfirma = "Pesquise uma empresa antes de salvar!";
            return null;
        }
                
        if (!db.listaCentros(Integer.parseInt(tipos.get(idTipos).getDescription()), centroComercial.getJuridica().getId()).isEmpty()){
            msgConfirma = "Essa empresa já existe!";
            return null;
        }

        centroComercial.setTipoCentroComercial((TipoCentroComercial) sv.pesquisaCodigo(Integer.parseInt(tipos.get(idTipos).getDescription()), "TipoCentroComercial"));
        sv.abrirTransacao();
        if(centroComercial.getId() == -1){
            if (!sv.inserirObjeto(centroComercial)){
                msgConfirma = "Erro ao salvar Centro comercial!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Centro salvo com Sucesso!";
        }else{
            if (!sv.alterarObjeto(centroComercial)){
                msgConfirma = "Erro ao atualizar Centro comercial!";
                sv.desfazerTransacao();
                return null;
            }
            msgConfirma = "Centro atualizado com Sucesso!";
        }
        centroComercial = new CentroComercial();
        listaCentros = new ArrayList();
        sv.comitarTransacao();
        return null;
    }
    
    public String editar(){
        centroComercial = (CentroComercial)listaCentros.get(idIndex);
        for (int i = 0; i < tipos.size(); i++){
            if (Integer.parseInt(tipos.get(i).getDescription()) == centroComercial.getTipoCentroComercial().getId()){
                idTipos = i;
            }
        }
        return null;
    }
    
    public String excluir(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.pesquisaCodigo(centroComercial.getId(), "CentroComercial"))){
            msgConfirma = "Erro ao excluir cadastro!";
            sv.desfazerTransacao();
            return null;
        }else{
            msgConfirma = "Centro excluido com Sucesso!";
            centroComercial = new CentroComercial();
            listaCentros = new ArrayList();
            sv.comitarTransacao();
        }

        return null;
    }

    public List<SelectItem> getListaTipos(){
        if (tipos.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List result = sv.listaObjeto("TipoCentroComercial");
            for (int i = 0;i <  result.size(); i++){
                tipos.add(new SelectItem(
                           new Integer(i),
                           (String) ((TipoCentroComercial) result.get(i)).getDescricao(),
                           Integer.toString(((TipoCentroComercial) result.get(i)).getId())  ));
            }
        }
        return tipos;
    }
    
    public void refreshForm(){
        
    }

    public CentroComercial getCentroComercial() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null){
            centroComercial.setJuridica((Juridica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            msgConfirma = "";
        }
        return centroComercial;
    }

    public void setCentroComercial(CentroComercial centroComercial) {
        this.centroComercial = centroComercial;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdTipos() {
        return idTipos;
    }

    public void setIdTipos(int idTipos) {
        this.idTipos = idTipos;
    }

    public List<CentroComercial> getListaCentros() {
        if (listaCentros.isEmpty()){
            CentroComercialDB db = new CentroComercialDBToplink();
            listaCentros = db.pesquisaTodosOrdernado();
        }
        return listaCentros;
    }

    public void setListaCentros(List<CentroComercial> listaCentros) {
        this.listaCentros = listaCentros;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
