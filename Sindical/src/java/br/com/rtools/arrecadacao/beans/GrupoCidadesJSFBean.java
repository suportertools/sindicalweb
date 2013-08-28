package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.GrupoCidades;
import br.com.rtools.arrecadacao.MensagemConvencao;
import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDB;
import br.com.rtools.arrecadacao.db.MensagemConvencaoDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class GrupoCidadesJSFBean {
    private GrupoCidades grupoCidades = new GrupoCidades();
    private String comoPesquisa = "T";
    private String descPesquisa = "";
    private String msgConfirma;
    private String msgGrupoCidade = "";
    private List<GrupoCidades> listaCidade = new ArrayList();
    private Cidade cidade = new Cidade();
    private int idIndex = -1;


    public List<GrupoCidades> getListaCidade() {
        GrupoCidadesDB dbGC = new GrupoCidadesDBToplink();
        if (listaCidade.isEmpty()){
            if (getGrupoCidades().getGrupoCidade().getId() != -1){
                listaCidade = dbGC.pesquisaPorGrupo(getGrupoCidades().getGrupoCidade().getId());
            }
        }else{  
            if (( (GrupoCidades) listaCidade.get(0)).getGrupoCidade().getId() != getGrupoCidades().getGrupoCidade().getId()){
                listaCidade = dbGC.pesquisaPorGrupo(getGrupoCidades().getGrupoCidade().getId());
            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<GrupoCidades> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public Cidade getCidade() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa") != null){
            cidade = (Cidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cidadePesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        }
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
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

    public GrupoCidadesJSFBean(){
//        htmlTable = new HtmlDataTable();
    }

    public GrupoCidades getGrupoCidades() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa") != null){
            grupoCidades.setGrupoCidade((GrupoCidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        }
        return grupoCidades;
    }

    public void setGrupoCidades(GrupoCidades grupoCidades) {
        this.grupoCidades = grupoCidades;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgGrupoCidade() {
        return msgGrupoCidade;
    }

    public void setMsgGrupoCidade(String msgGrupoCidade) {
        this.msgGrupoCidade = msgGrupoCidade;
    }

    public String salvar(){
        int i = 0;
        while (i < listaCidade.size()){
            grupoCidades = (GrupoCidades) listaCidade.get(i);
            this.salvarGrupoCidade(grupoCidades);
            i++;
        }
        grupoCidades = new GrupoCidades();
        listaCidade.clear();
        cidade = new Cidade();
        msgConfirma = "Adicionados com sucesso!";
        return null;
    }
    
    public boolean salvarGrupoCidade(GrupoCidades grupoCidades){
        GrupoCidadesDB db = new GrupoCidadesDBToplink();
        NovoLog log = new NovoLog();
        if(grupoCidades.getId() == -1){
            if(db.insert(grupoCidades)){
                log.novo("Novo registro", "Grupo Cidades inserido "+grupoCidades.getId()+" - Cidade: "+grupoCidades.getCidade().getCidade()+" - Grupo Cidade: "+grupoCidades.getGrupoCidade().getDescricao());
                return true;
            }else
                return false;
        }else{
            db.getEntityManager().getTransaction().begin();
            GrupoCidades gc = new GrupoCidades();
            gc = db.pesquisaCodigo(grupoCidades.getId());
            String antes = "De -  Grupo Cidade:: "+gc.getGrupoCidade().getDescricao()+" - Grupo Cidade: "+gc.getCidade().getCidade();
            if(db.update(grupoCidades)){
                db.getEntityManager().getTransaction().commit();
                log.novo("Atualizado", antes +" - para: "+grupoCidades.getId()+" - Cidade: "+grupoCidades.getCidade().getCidade()+" - Grupo Cidade: "+grupoCidades.getGrupoCidade().getDescricao());
                return true;
            }else{
                db.getEntityManager().getTransaction().rollback();
                return false;
            }
        }
    }

    public boolean salvarMensagemConvencao(MensagemConvencao mensagemConvencao){
        MensagemConvencaoDB db = new MensagemConvencaoDBToplink();
        NovoLog log = new NovoLog();
        if(mensagemConvencao.getId() == -1){
            if(db.insert(mensagemConvencao)){
                log.novo("Novo registro", "Mensagem convencao inserido "+mensagemConvencao.getId()+" - Referencia: "+mensagemConvencao.getReferencia()+ " - vencimento: "+mensagemConvencao.getVencimento()+" - Mensagem Convencao inseridas "+mensagemConvencao.getId()+" - Mensagem compensacao: "+mensagemConvencao.getMensagemCompensacao()+" - Mensagem contribuinte: "+mensagemConvencao.getMensagemContribuinte()+" - Convencao: "+mensagemConvencao.getConvencao()+" - Convencao: "+mensagemConvencao.getConvencao().getDescricao()+" - Grupo Cidade: "+mensagemConvencao.getGrupoCidade().getDescricao()+" - Tipo Servico: "+mensagemConvencao.getTipoServico().getDescricao()+" - Servico: "+mensagemConvencao.getServicos().getDescricao());
                return true;
            }else
                return false;
        }
        else{
            db.getEntityManager().getTransaction().begin();
            MensagemConvencao mc = new MensagemConvencao();
            mc = db.pesquisaCodigo(mensagemConvencao.getId());
            String antes = "De - Referencia: "+mc.getReferencia()+ " - vencimento: "+mc.getVencimento()+" - Mensagem Convencao inseridas "+mc.getId()+" - Mensagem compensacao: "+mensagemConvencao.getMensagemCompensacao()+" - Mensagem contribuinte: "+mensagemConvencao.getMensagemContribuinte()+" - Convencao: "+mensagemConvencao.getConvencao()+" - Convencao: "+mensagemConvencao.getConvencao().getDescricao()+" - Grupo Cidade: "+mensagemConvencao.getGrupoCidade().getDescricao()+" - Tipo Servico: "+mensagemConvencao.getTipoServico().getDescricao()+" - Servico: "+mc.getServicos().getDescricao();
            if(db.update(mensagemConvencao)){
                db.getEntityManager().getTransaction().commit();
                log.novo("Atualizado", antes +" - para: "+mensagemConvencao.getId()+" - Referencia: "+mc.getReferencia()+ " - vencimento: "+mc.getVencimento()+" - Mensagem Convencao inseridas "+mc.getId()+" - Mensagem compensacao: "+mensagemConvencao.getMensagemCompensacao()+" - Mensagem contribuinte: "+mensagemConvencao.getMensagemContribuinte()+" - Convencao: "+mensagemConvencao.getConvencao()+" - Convencao: "+mensagemConvencao.getConvencao().getDescricao()+" - Grupo Cidade: "+mensagemConvencao.getGrupoCidade().getDescricao()+" - Tipo Servico: "+mensagemConvencao.getTipoServico().getDescricao()+" - Servico: "+mc.getServicos().getDescricao());
                return true;
            }else{
                db.getEntityManager().getTransaction().rollback();
                return false;
            }
        }
    }

   public String novo(){
       grupoCidades = new GrupoCidades();
       msgConfirma = "";
       return "grupoCidades";
   }

   public String excluir(){
//        GrupoCidadesDB db = new GrupoCidadesDBToplink();
//        NovoLog log = new NovoLog();
//        if(grupoCidades.getId()!=-1){
//            db.getEntityManager().getTransaction().begin();
//            grupoCidades = db.pesquisaCodigo(grupoCidades.getId());
//            if (db.delete(grupoCidades)){
//                db.getEntityManager().getTransaction().commit();
//                msgConfirma = "Grupo Excluida com sucesso!";
//                log.novo("Excluido", grupoCidades.getId()+" - Grupo Cidade: "+grupoCidades.getCidade().getCidade());
//            }else{
//                db.getEntityManager().getTransaction().rollback();
//            msgConfirma = "Grupo não pode ser excluido!";}
//        }
        //msgGrupoCidade = "";
        //grupoCidades = new GrupoCidades();
        cidade = new Cidade();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        return "grupoCidades";
    }

    public List getListaGrupoCidades() {
//        Pesquisa pesquisa = new Pesquisa();
        List result = null;
//        result = pesquisa.pesquisar("GrupoCidades", "descricao", descPesquisa, "descricao", comoPesquisa);
        return result;
   }

    public void refreshForm(){

    }

    public String editar(){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("grupoCidadesPesquisa", grupoCidades);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
        return "grupoCidades";
    }

    public String inserirCidade(){
        //GrupoCidadesDB db = new GrupoCidadesDBToplink();
        if (grupoCidades.getGrupoCidade().getId() == -1){
            msgGrupoCidade = "Pesquise um grupo Cidades";
            return "grupoCidades";
        }
        
        if (cidade.getId() == -1){
            msgGrupoCidade = "Pesquise uma Cidade";
            return "grupoCidades";
        }
        
        for(int i = 0; i < listaCidade.size();i++){
            if (listaCidade.get(i).getCidade().getId() == cidade.getId()){
                msgGrupoCidade = "Cidade já pertencente a um grupo!";
                return "grupoCidades";
            }
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        GrupoCidades gc = new GrupoCidades();
        gc.setGrupoCidade(grupoCidades.getGrupoCidade());
        gc.setCidade(cidade);
        if (!sv.inserirObjeto(gc)){
            msgConfirma = "Erro ao salvar grupo Cidades";
            return "grupoCidades";
        }
        
        listaCidade.clear();
        cidade = new Cidade();
        msgGrupoCidade = "";
        sv.comitarTransacao();
        return "grupoCidades";
    }

    public String removerCidade(){
        grupoCidades =  (GrupoCidades) listaCidade.get(idIndex);
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto(sv.pesquisaCodigo(grupoCidades.getId(), "GrupoCidades") )){
            msgConfirma = "Erro ao excluir Cidade do Grupo!";
            sv.desfazerTransacao();
            return null;
        }else{
            msgConfirma = "Registro excluido com Sucesso!";
            listaCidade.clear();
            sv.comitarTransacao();
        }
        return null;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}


