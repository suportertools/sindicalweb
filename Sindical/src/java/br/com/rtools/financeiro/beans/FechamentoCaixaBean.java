package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ContaSaldo;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


@ManagedBean
@SessionScoped
public class FechamentoCaixaBean implements Serializable{
    private int idCaixa = 0;
    private List<SelectItem> listaCaixa = new ArrayList<SelectItem>();
    private int idCaixaDestino = 0;
    private List<SelectItem> listaCaixaDestino = new ArrayList<SelectItem>();
    private FechamentoCaixa fechamento = new FechamentoCaixa();
    private String valor = "";
    private String valorTransferencia = "";
    private String saldoAnterior = "";
    private ContaSaldo contaSaldo = new ContaSaldo();
    private List<DataObject> listaFechamento = new ArrayList();
    
    
    public  FechamentoCaixaBean(){
        
    }
    
    public void transferir(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
        Caixa caixa_destino = (Caixa)sv.pesquisaCodigo(Integer.valueOf(listaCaixaDestino.get(idCaixaDestino).getDescription()),"Caixa");
        
        FinanceiroDB db = new FinanceiroDBToplink();
        List<Vector> result = db.listaMovimentoCaixa(caixa.getId());
        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaEntrada(caixa.getId());
        
        if (result.isEmpty() && lEntrada.isEmpty()){
            GenericaMensagem.warn("Erro", "Não existe movimentos para este Caixa!");
            return;
            
        }
        
        sv.abrirTransacao();
        
        TransferenciaCaixa tc = new TransferenciaCaixa(
                -1, 
                caixa, 
                Moeda.converteUS$(valorTransferencia), 
                caixa_destino, 
                DataHoje.dataHoje(), 
                (FStatus)sv.pesquisaCodigo(13,"FStatus"), 
                null, 
                null
        );
        
        
        if (!sv.inserirObjeto(tc)){
            GenericaMensagem.warn("Erro", "Não foi possivel completar transferência!");
            sv.desfazerTransacao();
        }
        
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Dinheiro transferido com Sucesso!");
        valorTransferencia = "0,00";
    }
    
    public void imprimir(){
        
    }
    
    public void excluir(Caixa c){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        
    }
    
    public void salvar(){
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        
        if (usuario == null){
            GenericaMensagem.warn("Erro", "Faça o login novamente!");
            return;
        }
        
        FinanceiroDB db = new FinanceiroDBToplink();
        
        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
        
        List<Vector> result = db.listaMovimentoCaixa(caixa.getId());
        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaEntrada(caixa.getId());
        List<TransferenciaCaixa> lSaida = db.listaTransferenciaSaida(caixa.getId());
        
        if (result.isEmpty() && lEntrada.isEmpty() && lSaida.isEmpty() ){
            GenericaMensagem.warn("Erro", "Não existe movimentos para este Caixa!");
            return;
            
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        
        // CALCULO PARA SOMAR OS VALORES DA QUERY
        fechamento.setValorInformado(Moeda.converteUS$(valor));
        fechamento.setUsuario(usuario);
        if (!sv.inserirObjeto(fechamento)){
            GenericaMensagem.warn("Erro", "Não foi possivel concluir este fechamento!");
            sv.desfazerTransacao();
            fechamento = new FechamentoCaixa();
            return;
        }
        
        if (!result.isEmpty()){
            float valorx = 0;
            for(int i = 0; i < result.size(); i++){
                Baixa ba = ((Baixa)sv.pesquisaCodigo((Integer) result.get(i).get(8), "Baixa"));
                ba.setFechamentoCaixa(fechamento);
                
                valorx = Moeda.somaValores(valorx, Float.parseFloat(Double.toString((Double) result.get(i).get(6))));
                if (!sv.alterarObjeto(ba)){
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a Baixa!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(valorx);
            sv.alterarObjeto(fechamento);
        }        
        
        if (!lEntrada.isEmpty()){
            float valorx = 0;
            for(int i = 0; i < lEntrada.size(); i++){
                TransferenciaCaixa tc = ((TransferenciaCaixa)sv.pesquisaCodigo(lEntrada.get(i).getId(), "TransferenciaCaixa"));
                tc.setFechamentoEntrada(fechamento);
                
                valorx = Moeda.somaValores(valorx, tc.getValor());
                if (!sv.alterarObjeto(tc)){
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a entrada de Transferência entre Caixas!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(Moeda.somaValores(fechamento.getValorFechamento(), valorx));
            sv.alterarObjeto(fechamento);
        }
        
        if (!lSaida.isEmpty()){
            float valorx = fechamento.getValorFechamento();
            for(int i = 0; i < lSaida.size(); i++){
                TransferenciaCaixa tc = ((TransferenciaCaixa)sv.pesquisaCodigo(lSaida.get(i).getId(), "TransferenciaCaixa"));
                tc.setFechamentoSaida(fechamento);
                
                valorx = Moeda.subtracaoValores(valorx, tc.getValor());
                if (!sv.alterarObjeto(tc)){
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a saída de Transferência entre Caixas!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(Moeda.somaValores(valorx, contaSaldo.getSaldo()));
            sv.alterarObjeto(fechamento);
        }
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Fechamento de Caixa concluído!");
        fechamento = new FechamentoCaixa();
        listaFechamento.clear();
        valor = "0,00";
    }
    
    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            List<Caixa> list = (new FinanceiroDBToplink()).listaCaixa();
            for (int i = 0; i < list.size(); i++) {
                listaCaixa.add(new SelectItem(new Integer(i),
                                list.get(i).getCaixa() + " - " +list.get(i).getDescricao(),
                                Integer.toString(list.get(i).getId())));
            }
            
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null && 
                ((MacFilial)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial")).getCaixa()  != null){
                
                
                Caixa caixa = ((MacFilial)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial")).getCaixa();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == caixa.getId()){
                        idCaixa = i;
                    }
                }
            }
        }
        return listaCaixa;
    }

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }

    public FechamentoCaixa getFechamento() {
        return fechamento;
    }

    public void setFechamento(FechamentoCaixa fechamento) {
        this.fechamento = fechamento;
    }
    
    public String getValorTransferencia() {
        return Moeda.converteR$(valorTransferencia);
    }

    public void setValorTransferencia(String valorTransferencia) {
        this.valorTransferencia = Moeda.substituiVirgula(valorTransferencia);
    }
    
    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getSaldoAnterior() {
        if (contaSaldo.getId() == -1 && !listaCaixa.isEmpty()){
            Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
            FinanceiroDB db = new FinanceiroDBToplink();
            contaSaldo = db.pesquisaSaldoInicial(caixa.getId());
            saldoAnterior = Moeda.converteR$Float(contaSaldo.getSaldo());
        }else{
            saldoAnterior = Moeda.converteR$Float(contaSaldo.getSaldo());
        }
        return saldoAnterior;
    }

    public void setSaldoAnterior(String saldoAnterior) {
        this.saldoAnterior = saldoAnterior;
    }

    public int getIdCaixaDestino() {
        return idCaixaDestino;
    }

    public void setIdCaixaDestino(int idCaixaDestino) {
        this.idCaixaDestino = idCaixaDestino;
    }

    public List<SelectItem> getListaCaixaDestino() {
        if (listaCaixaDestino.isEmpty() && !listaCaixa.isEmpty()) {
            List<Caixa> list = (new SalvarAcumuladoDBToplink()).listaObjeto("Caixa");
            Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
            for (int i = 0; i < list.size(); i++) {
                listaCaixaDestino.add(new SelectItem(new Integer(i),
                                list.get(i).getCaixa() + " - " +list.get(i).getDescricao(),
                                Integer.toString(list.get(i).getId())));
                if (list.get(i).getId() == caixa.getId()){
                    idCaixaDestino = i;
                }
            }
        }
        return listaCaixaDestino;
    }

    public void setListaCaixaDestino(List<SelectItem> listaCaixaDestino) {
        this.listaCaixaDestino = listaCaixaDestino;
    }

    public List<DataObject> getListaFechamento() {
        if (listaFechamento.isEmpty() && !listaCaixa.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
            List<Vector> lista = db.listaFechamentoCaixa(caixa.getId());
            //List<Vector> lista = db.listaFechamentoCaixaTransferencia(caixa.getId());
            int status = 0;
            for (int i = 0; i < lista.size(); i++){
                if (Moeda.converteUS$(lista.get(i).get(2).toString()) > Moeda.converteUS$(lista.get(i).get(3).toString())){
                    status = 1;
                }else if (Moeda.converteUS$(lista.get(i).get(2).toString()) < Moeda.converteUS$(lista.get(i).get(3).toString())){
                    status = 2;
                }
                listaFechamento.add(new DataObject(lista.get(i), 
                                                   DataHoje.converteData((Date)lista.get(i).get(4)), // DATA
                                                   lista.get(i).get(5).toString(), // HORA
                                                   Moeda.converteR$(lista.get(i).get(2).toString()),  // VALOR FECHAMENTO
                                                   Moeda.converteR$(lista.get(i).get(3).toString()),  // VALOR INFORMADO
                                                   status  // STATUS
                )); 
            }
            
//            lista = db.listaFechamentoCaixaTransferencia(caixa.getId());
//            status = 0;
//            for (int i = 0; i < lista.size(); i++){
//                if (Moeda.converteUS$(lista.get(i).get(2).toString()) > Moeda.converteUS$(lista.get(i).get(3).toString())){
//                    status = 1;
//                }else if (Moeda.converteUS$(lista.get(i).get(2).toString()) < Moeda.converteUS$(lista.get(i).get(3).toString())){
//                    status = 2;
//                }
//                listaFechamento.add(new DataObject(lista.get(i), 
//                                                   DataHoje.converteData((Date)lista.get(i).get(4)), // DATA
//                                                   lista.get(i).get(5).toString(), // HORA
//                                                   Moeda.converteR$(lista.get(i).get(2).toString()),  // VALOR FECHAMENTO
//                                                   Moeda.converteR$(lista.get(i).get(3).toString()),  // VALOR INFORMADO
//                                                   status  // STATUS
//                )); 
//            }
        }
        return listaFechamento;
    }

    public void setListaFechamento(List<DataObject> listaFechamento) {
        this.listaFechamento = listaFechamento;
    }
}
