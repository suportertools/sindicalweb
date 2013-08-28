package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Indice;
import br.com.rtools.financeiro.IndiceMensal;
import br.com.rtools.financeiro.db.IndiceMensalDB;
import br.com.rtools.financeiro.db.IndiceMensalDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.model.SelectItem;

public class IndiceMensalJSFBean {

    private IndiceMensal indiceMensal = new IndiceMensal();
    private List<IndiceMensal> listaIndiceMensal = new ArrayList<IndiceMensal>();
    private int idIndex = -1;
    private String msgConfirma = "";
    private int numMes = 0;
    private int ano = 0;
    private String valor = "";
    private int idIndice = 0;
    private boolean limpar = false;
    
    public String novo(){
        msgConfirma = "";
        indiceMensal = new IndiceMensal();
        limpar = false;
        return "indiceMensal";
    }

   public List<SelectItem> getListaIndices(){
       List<SelectItem> result = new Vector<SelectItem>();
       int i = 0;
       IndiceMensalDB db = new IndiceMensalDBToplink();
       List select = null;
       select = db.pesquisaTodosIndices();
       while (i < select.size()){
          result.add(new SelectItem(  new Integer(i),
                                      ((Indice) select.get(i)).getDescricao(),
                                      Integer.toString( ((Indice) select.get(i)).getId() )
                                  )
                    );
          i++;
       }
       return result;
   }

    public String salvar(){
        
        msgConfirma = "";
        
        if(valor.equals("")){
            msgConfirma = "Digite um valor válido!";
        }
        
        IndiceMensalDB db = new IndiceMensalDBToplink();
        Indice indice = new Indice();
        indice = db.pesquisaCodigoIndice( Integer.valueOf(getListaIndices().get(idIndice).getDescription()));
        indiceMensal.setAno(Integer.valueOf( getListaAnos().get(ano).getDescription()) );
        indiceMensal.setMes(numMes);
        indiceMensal.setValor( Moeda.substituiVirgulaFloat( valor ));
        indiceMensal.setIndice(indice);
        if (db.pesquisaIndMensalExistente(indice.getId(), indiceMensal.getAno(), indiceMensal.getMes()).isEmpty()){
            if (db.insert(indiceMensal)){
                msgConfirma = "Indice Mensal salvo com Sucesso!";
            }else{
                msgConfirma = "Erro ao salvar Indice Mensal";
            }
        }else{
            msgConfirma = "Indice Mensal ja existe no Sistema!";
        }
        indiceMensal = new IndiceMensal();
        listaIndiceMensal.clear();
        return null;
    }

    public String btnExcluir(){
        indiceMensal =  (IndiceMensal) listaIndiceMensal.get(idIndex);
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto(sv.pesquisaCodigo(indiceMensal.getId(), "IndiceMensal") )){
            msgConfirma = "Erro ao excluir Indice Mensal!";
            sv.desfazerTransacao();
            return null;
        }else{
            msgConfirma = "Registro excluido com Sucesso!";
            indiceMensal = new IndiceMensal();
            listaIndiceMensal.clear();
            setLimpar(true);
            sv.comitarTransacao();
        }
        
        return null;
    }

    public List<SelectItem> getListaAnos(){
        List<SelectItem> result = new Vector<SelectItem>();
        int an = 0;
        an = Integer.valueOf(DataHoje.data().substring(6, 10));
        for (int o = 0; o < 6; o++){
            result.add(new SelectItem(  new Integer(o),
                                        String.valueOf(an),
                                        String.valueOf(an)));
            an = an - 1;
        }
        return result;
    }

    public IndiceMensal getIndiceMensal() {
        return indiceMensal;
    }

    public void setIndiceMensal(IndiceMensal indiceMensal) {
        this.indiceMensal = indiceMensal;
    }

    public List<IndiceMensal> getListaIndiceMensal() {
        IndiceMensalDB db = new IndiceMensalDBToplink();
        listaIndiceMensal = db.pesquisaIndiceMensalPorIDIndice(Integer.valueOf( getListaIndices().get(idIndice).getDescription()));
        return listaIndiceMensal;
    }
    
    
    
    public void limpar(){
        if(limpar == true){
            novo();
        }          
        listaIndiceMensal.clear();
        indiceMensal = new IndiceMensal();
        valor = "";
        ano = 0;
        numMes = 0;      
    }

    public void setListaIndiceMensal(List<IndiceMensal> listaIndiceMensal) {
        this.listaIndiceMensal = listaIndiceMensal;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }
    
    public int getNumMes() {
        return numMes;
    }

    public void setNumMes(int numMes) {
        this.numMes = numMes;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public int getIdIndice() {
        return idIndice;
    }

    public void setIdIndice(int idIndice) {
        this.idIndice = idIndice;
    }

    /**
     * @return the limpar
     */
    public boolean isLimpar() {
        return limpar;
    }

    /**
     * @param limpar the limpar to set
     */
    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }



}
