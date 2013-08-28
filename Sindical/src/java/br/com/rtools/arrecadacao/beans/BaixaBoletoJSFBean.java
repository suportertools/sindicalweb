package br.com.rtools.arrecadacao.beans;

import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoContaCobranca;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.controleUsuario.chamadaPaginaJSFBean;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class BaixaBoletoJSFBean {
    private int idServicos = 0;
    private int index = 0;
    private String numBoleto = "";
    private String caixaBanco = "banco";
    private String msgConfirma = "";
    private List<DataObject> listBoletos = new ArrayList();
    private boolean carregarGrid = false;
    private boolean marcarTodos = false;
    private Pessoa pessoa = new Pessoa();
    private boolean novoNumero = false;
    private boolean habPessoa = false;
    private boolean disBtnBaixar = true;

    public void refreshForm(){

    }

    public String refreshFormCaixa(){
        return "baixaBoleto";
    }

    public List<SelectItem> getListaServicoCobranca(){
       List<SelectItem> servicoCobranca = new ArrayList<SelectItem>();
       int i = 0;
       ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
       List<ServicoContaCobranca> select = servDB.pesquisaTodosFiltrado();
       if (select == null){
           select = new ArrayList<ServicoContaCobranca>();
       }
       while (i < select.size()){
           if (select.get(i).getServicos().getId() == 1){
               servicoCobranca.add(
                       new SelectItem(
                       new Integer(i) ,
                       select.get(i).getServicos().getDescricao() +" - " +
                       select.get(i).getContaCobranca().getSicasSindical(),//SICAS NO CASO DE SINDICAL
                       Integer.toString(select.get(i).getId())
               ));
           }else{
               servicoCobranca.add(
                       new SelectItem(
                       new Integer(i) ,
                       select.get(i).getServicos().getDescricao() +" - " +
                       select.get(i).getContaCobranca().getCodCedente(),//CODCEDENTE NO CASO DE OUTRAS
                       Integer.toString(select.get(i).getId())
               ));
           }
          i++;
       }
       return servicoCobranca;
   }


   public String pesquisarBoleto(){
        carregarGrid = true;
       listBoletos.clear();
       return "baixaBoleto";
   }

    public synchronized String baixarBoletos(){
        if (!listBoletos.isEmpty()){
            //SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            Movimento mov = null;
            List<Movimento> lista = new ArrayList();
            
            // NECESSÁRIO BLOQUEAR PORQ NA BAIXA POR RETORNO ELE FAZ VINCULO COM O DOCUMENTO DA PESSOA NA PESQUISA DO BOLETO
            if (pessoa.getDocumento().isEmpty() || pessoa.getDocumento().equals("0")){
                msgConfirma = "Pessoa não possui documento!";
                return null;
            }
            
            if (caixaBanco.equals("caixa")){
                for (int i = 0; i < listBoletos.size();i++){
                    if ( (Boolean)listBoletos.get(i).getArgumento8() == true ){
                        mov = (Movimento) listBoletos.get(i).getArgumento1();
                        mov.setValorBaixa(Float.parseFloat(Moeda.substituiVirgula( ((String)listBoletos.get(i).getArgumento7()))) );
                           
                        mov.setMulta(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(i).getArgumento3()))) );
                        mov.setJuros(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(i).getArgumento4()))) );
                        mov.setCorrecao(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(i).getArgumento5()))) );
                        mov.setDesconto(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(i).getArgumento6()))) );
                        
                        if (mov.getValorBaixa() <= 0){
                            msgConfirma = "Valor não pode ser zerado";
                            return null;
                        }
                        
//                        if (GerarMovimento.baixarMovimento(mov, null)){
//                             msgConfirma = "Erro ao atualizar Boleto!";
//                             return null;
//                        }
                        lista.add(mov);
                    }
                }
            }else{
                mov = (Movimento) listBoletos.get(index).getArgumento1();
                mov.setValorBaixa(Float.parseFloat(Moeda.substituiVirgula( ((String)listBoletos.get(index).getArgumento7()))));
               
                mov.setMulta(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(index).getArgumento3()))) );
                mov.setJuros(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(index).getArgumento4()))) );
                mov.setCorrecao(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(index).getArgumento5()))) );
                mov.setDesconto(Float.parseFloat(Moeda.substituiVirgula( ((String) listBoletos.get(index).getArgumento6()))) );

                if (mov.getValorBaixa() <= 0){
                    msgConfirma = "Valor não pode ser zerado";
                    return null;
                }
//                if (GerarMovimento.baixarMovimento(mov, null)){
//                     msgConfirma = "Erro ao atualizar Boleto!";
//                     return null;
//                }
                lista.add(mov);
            }
            
            if (!lista.isEmpty())
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
            else{
                msgConfirma = "Nenhum boleto foi selecionado!";
                return null;
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("caixa_banco", caixaBanco);
        }else{
            msgConfirma = "Lista vazia";
            return null;
        }
        return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
    }

   public List getListaBoletos(){
       DataObject dt = null;
       List<Movimento> listaQuery = new ArrayList<Movimento>();
       MovimentoDB db = new MovimentoDBToplink();
       String pesquisado = "";
       if (carregarGrid){
           listaQuery = db.movimentosAberto(getPessoa().getId());
           for(int i = 0; i < listaQuery.size();i++){
               if (listaQuery.get(i).getDocumento().equals(numBoleto))
                   pesquisado = "destaqueBoleto";
               else
                   pesquisado = "";
               
                    dt = new DataObject(pesquisado,
                                        listaQuery.get(i),
                                        Moeda.converteR$( Float.toString(listaQuery.get(i).getValor()) ), // valor
                                        Moeda.converteR$( Float.toString(listaQuery.get(i).getMulta()) ), // multa
                                        Moeda.converteR$( Float.toString(listaQuery.get(i).getJuros()) ), // juros
                                        Moeda.converteR$( Float.toString(listaQuery.get(i).getCorrecao()) ), // correcao
                                        Moeda.converteR$( Float.toString(listaQuery.get(i).getDesconto()) ), // desconto
                                        Moeda.converteR$( somarValorRecebido(Float.toString(listaQuery.get(i).getValor()),
                                                                             Float.toString(listaQuery.get(i).getMulta()),
                                                                             Float.toString(listaQuery.get(i).getJuros()),
                                                                             Float.toString(listaQuery.get(i).getCorrecao()),
                                                                             Float.toString(listaQuery.get(i).getDesconto()))), // valor pago
                                        false,
                                        null);
                listBoletos.add(dt);
                converteDescontoFora(i,(String)((DataObject)listBoletos.get(i)).getArgumento7() );
           }
            carregarGrid = false;
       }
       return listBoletos;
   }

   public void atualizaValoresGrid(int index){
       converteDesconto((String) listBoletos.get(index).getArgumento7());
       listBoletos.get(index).setArgumento7( somarValorRecebido( Moeda.converteR$( (String) listBoletos.get(index).getArgumento2()),
                                                                 Moeda.converteR$( (String) listBoletos.get(index).getArgumento3()),
                                                                 Moeda.converteR$( (String) listBoletos.get(index).getArgumento4()),
                                                                 Moeda.converteR$( (String) listBoletos.get(index).getArgumento5()),
                                                                 Moeda.converteR$( (String) listBoletos.get(index).getArgumento6())
                                                                 )
                                                              );

       converteValor(index);
       converteMulta(index);
       converteJuros(index);
       converteCorrecao(index);
       converteValorPago(index);
   }

   public String somarValorRecebido(String valor, String multa, String juros, String correcao, String desconto){
       float v = Float.parseFloat(Moeda.substituiVirgula(valor));
       float m = Float.parseFloat(Moeda.substituiVirgula(multa));
       float j = Float.parseFloat(Moeda.substituiVirgula(juros));
       float c = Float.parseFloat(Moeda.substituiVirgula(correcao));
       float d = Float.parseFloat(Moeda.substituiVirgula(desconto));
       float soma = Moeda.somaValores(
                        Moeda.somaValores(j,c),
                        Moeda.somaValores(v,m)
                    );
       if (d > soma){
           d = 0;
       }

       float subtracao = Moeda.subtracaoValores(soma,d);

       return Moeda.converteR$(String.valueOf(subtracao));
   }

   public String limpaPessoa(){
       FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
       pessoa = new Pessoa();
       habPessoa = false;
       listBoletos.clear();
       return "baixaBoleto";
   }

   public void converteValor(int index){
        listBoletos.get(index).setArgumento2( Moeda.converteR$( (String) listBoletos.get(index).getArgumento2()) );
   }
   
   public void converteMulta(int index){
       listBoletos.get(index).setArgumento3( Moeda.converteR$( (String) listBoletos.get(index).getArgumento3()));
   }
   
   public void converteJuros(int index){
       listBoletos.get(index).setArgumento4(Moeda.converteR$( (String) listBoletos.get(index).getArgumento4()));
   }
   
   public void converteCorrecao(int index){
       listBoletos.get(index).setArgumento5(Moeda.converteR$( (String) listBoletos.get(index).getArgumento5()));
   }

   public void converteDesconto(String soma){
       if (Float.parseFloat(Moeda.substituiVirgula(Moeda.converteR$( (String) listBoletos.get(index).getArgumento6()))) > Float.parseFloat(Moeda.substituiVirgula(soma)))
           listBoletos.get(index).setArgumento6( Moeda.converteR$("0.0"));
       else
           listBoletos.get(index).setArgumento6( Moeda.converteR$( (String) listBoletos.get(index).getArgumento6()) );
   }

   public void converteDescontoFora(int index, String soma){
       if (Float.parseFloat(Moeda.substituiVirgula(Moeda.converteR$( (String) listBoletos.get(index).getArgumento6()))) > Float.parseFloat(Moeda.substituiVirgula(soma)))
           listBoletos.get(index).setArgumento6(Moeda.converteR$("0.0"));
       else
           listBoletos.get(index).setArgumento6(Moeda.converteR$( (String) listBoletos.get(index).getArgumento6()));
   }

   public void converteValorPago(int index){
       listBoletos.get(index).setArgumento7( Moeda.converteR$( (String) listBoletos.get(index).getArgumento7()) );
   }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public String getNumBoleto() {
        return numBoleto;
    }

    public void setNumBoleto(String numBoleto) {
        this.numBoleto = numBoleto;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") == null ){
            if (this.novoNumero){
                ServicoContaCobranca scc = new ServicoContaCobranca();
                ServicoContaCobrancaDB dbS = new ServicoContaCobrancaDBToplink();
                MovimentoDB db = new MovimentoDBToplink();

                scc = dbS.pesquisaCodigo(Integer.parseInt(getListaServicoCobranca().get(idServicos).getDescription()));

                //Movimento mov = db.pesquisaMovPorNumDocumento(concatBoleto(scc.getContaCobranca().getBoletoInicial(), numBoleto));
                Movimento mov = db.pesquisaMovPorNumDocumento(numBoleto);
               if (mov.getId() == -1 ){
                   pessoa = new Pessoa();
               }else{
                   pessoa = mov.getPessoa();
                   numBoleto = mov.getDocumento();
               }
               novoNumero = false;
               habPessoa = false;
            }
        }else{
            pessoa = ((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa")).getPessoa();
            habPessoa = true;
            listBoletos.clear();
            carregarGrid = true;
        }
        return pessoa;
    }

    public void marcar(){
        for (int i = 0; i < listBoletos.size(); i++){
            listBoletos.get(i).setArgumento8(marcarTodos);
        }        
    }    
    
    public String concatBoleto(String contaCobranca, String numero){
        String zero = "000000000000000000";
        String result = "";
        if (contaCobranca.length() > 11){
            result = contaCobranca.substring(0,4);
            result = result + zero;
            result = result.substring(0, contaCobranca.length()-numero.length()) + numero;
            return result;
        }else
            return numero;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public void refreshPessoa(){
        novoNumero = true;
    }

    public boolean isNovoNumero() {
        return novoNumero;
    }

    public void setNovoNumero(boolean novoNumero) {
        this.novoNumero = novoNumero;
    }

    public boolean isHabPessoa() {
        return habPessoa;
    }

    public void setHabPessoa(boolean habPessoa) {
        this.habPessoa = habPessoa;
    }

    public String getCaixaBanco() {
        if (caixaBanco.equals("caixa") && !listBoletos.isEmpty())
            disBtnBaixar = false;
        else
            disBtnBaixar = true;
        return caixaBanco;
    }

    public void setCaixaBanco(String caixaBanco) {
        this.caixaBanco = caixaBanco;
    }

    public boolean isDisBtnBaixar() {
        return disBtnBaixar;
    }

    public void setDisBtnBaixar(boolean disBtnBaixar) {
        this.disBtnBaixar = disBtnBaixar;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isMarcarTodos() {
        return marcarTodos;
    }

    public void setMarcarTodos(boolean marcarTodos) {
        this.marcarTodos = marcarTodos;
    }

    public boolean isCarregarGrid() {
        return carregarGrid;
    }

    public void setCarregarGrid(boolean carregarGrid) {
        this.carregarGrid = carregarGrid;
    }


}
