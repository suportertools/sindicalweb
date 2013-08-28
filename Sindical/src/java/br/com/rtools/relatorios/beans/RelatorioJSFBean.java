package br.com.rtools.relatorios.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class RelatorioJSFBean {
    private Relatorios relatorio = new Relatorios();
    private List<SelectItem> listaRotina = new ArrayList<SelectItem>();
    private List<Relatorios> listaRelatorio = new ArrayList();
    private String msgConfirma = "";
    private int index = 0;
    
    public String salvar(){
        if (relatorio.getNome().isEmpty()){
            msgConfirma = "Digite uma descrição!";
            return null;
        }
        
        if (relatorio.getJasper().isEmpty()){
            msgConfirma = "Digite um caminho para o Jasper!";
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        
        relatorio.setRotina( (Rotina)sv.pesquisaCodigo( Integer.parseInt(listaRotina.get(index).getDescription()), "Rotina" ) );
        
        sv.abrirTransacao();
        if (relatorio.getId() == -1){
           if (sv.inserirObjeto(relatorio)){
               msgConfirma = "Relatório salvo com Sucesso!";
               
               log.novo("Novo registro", "Relatório inserido "+relatorio.getId()+" - "+relatorio.getNome()+" / "+relatorio.getJasper());
           }else{
               msgConfirma = "Erro ao salvar Relatório!";
               sv.desfazerTransacao();
               return null;
           }
        }else{
            Relatorios rel = new Relatorios();
            rel = (Relatorios)sv.pesquisaCodigo(relatorio.getId(), "Relatorios");
            String antes = "De: "+rel.getNome()+" / "+relatorio.getNome() +" -  "+rel.getJasper()+" / "+relatorio.getJasper();
            
            if (sv.alterarObjeto(relatorio)){
                msgConfirma = "Registro atualizado!";
                
                log.novo("Atualizado", antes +" - para: "+relatorio.getId()+" - "+relatorio.getNome()+" / "+relatorio.getJasper());
            }else{
                msgConfirma = "Erro ao atualizar!";
                sv.desfazerTransacao();
                return null;
            }
        }
        listaRelatorio.clear();
        sv.comitarTransacao();
        return null;
    }
    
    public String novo(){
        relatorio = new Relatorios();
        index = 0;
        msgConfirma = "";
        return "relatorio";
    }
    
    public String excluir(){
        if (relatorio.getId() == -1){
            msgConfirma = "Pesquise um relatório para exclusão";
            return "relatorio";
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        
        sv.abrirTransacao();
        
        relatorio = (Relatorios)sv.pesquisaCodigo(relatorio.getId(), "Relatorios");
        if (sv.deletarObjeto(relatorio)){
            msgConfirma = "Relatório excluido com Sucesso!";
            log.novo("Excluido", relatorio.getId()+" - "+relatorio.getNome()+" / "+relatorio.getJasper());
        }else{
            msgConfirma = "Relatório não pode ser excluido";
            sv.desfazerTransacao();
            return null;
        }
    
        sv.comitarTransacao();
        relatorio = new Relatorios();
        index = 0;
        return null;
    }
    
    public String editar(Relatorios rela){
        this.relatorio = rela;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado",true);
               if(!getListaRotina().isEmpty()){
           for(int o = 0; o < listaRotina.size(); o++){
               if(Integer.parseInt( listaRotina.get(o).getDescription() ) == relatorio.getRotina().getId()){
                   index = o;
               }
           }
       }
        return "relatorio";
    }
    
    public Relatorios getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorios relatorio) {
        this.relatorio = relatorio;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()){
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Rotina> result = db.pesquisaRotina();
            for (int i = 0; i < result.size(); i++){
                listaRotina.add(new SelectItem(new Integer(i), 
                                               result.get(i).getRotina(), 
                                               String.valueOf(result.get(i).getId()))
                );
            }
        }
        return listaRotina;
    }

    public void setListaRotina(List<SelectItem> listaRotina) {
        this.listaRotina = listaRotina;
    }

    /**
     * @return the msgConfirma
     */
    public String getMsgConfirma() {
        return msgConfirma;
    }

    /**
     * @param msgConfirma the msgConfirma to set
     */
    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    public List<Relatorios> getListaRelatorio(){
        if (listaRelatorio.isEmpty()){
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            listaRelatorio = db.pesquisaTodosRelatorios();
        }
        return listaRelatorio;
    }

    public void setListaRelatorio(List<Relatorios> listaRelatorio) {
        this.listaRelatorio = listaRelatorio;
    }
}