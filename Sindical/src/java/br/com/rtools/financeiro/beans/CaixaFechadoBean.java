package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
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
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CaixaFechadoBean implements Serializable{
    private List<DataObject> listaFechamento = new ArrayList<DataObject>();
    private List<SelectItem> listaCaixa = new ArrayList<SelectItem>();
    private int idCaixa = 0;
    private FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
    private String valorTransferencia = "0,00";

    
    public void reabrir(){
        FinanceiroDB db = new FinanceiroDBToplink();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        List<Baixa> lista_baixa = db.listaBaixa(fechamentoCaixa.getId());
        
        sv.abrirTransacao();
        for (int i = 0; i < lista_baixa.size(); i++){
            Baixa ba = ((Baixa)sv.pesquisaCodigo(lista_baixa.get(i).getId(), "Baixa"));
            
            ba.setFechamentoCaixa(null);
            if (!sv.alterarObjeto(ba)){
                GenericaMensagem.warn("Erro", "Não foi possivel alterar a Baixa!");
                sv.desfazerTransacao();
                return;
            }
        }
        
        List<TransferenciaCaixa> lista_transferencia = db.listaTransferencia(fechamentoCaixa.getId());
        for (int i = 0; i < lista_transferencia.size(); i++){
            TransferenciaCaixa tc = ((TransferenciaCaixa)sv.pesquisaCodigo(lista_transferencia.get(i).getId(), "TransferenciaCaixa"));
            
            if (tc.getFechamentoEntrada() != null && tc.getFechamentoEntrada().getId() == fechamentoCaixa.getId())
                tc.setFechamentoEntrada(null);
            else if (tc.getFechamentoSaida() != null && tc.getFechamentoSaida().getId() == fechamentoCaixa.getId())
                tc.setFechamentoSaida(null);
            
            if (!sv.alterarObjeto(tc)){
                GenericaMensagem.warn("Erro", "Não foi possivel alterar a Transferência Caixa!");
                sv.desfazerTransacao();
                return;
            }
        }
        
        if (!sv.deletarObjeto(sv.pesquisaCodigo(fechamentoCaixa.getId(), "FechamentoCaixa"))){
            GenericaMensagem.warn("Erro", "Não foi possivel excluir a Fechamento Caixa!");
            sv.desfazerTransacao();
            return;
        }
        
        GenericaMensagem.info("Sucesso", "Reabrimento de Caixa concluído!");
        sv.comitarTransacao();
        listaFechamento.clear();
        fechamentoCaixa = new FechamentoCaixa();
    }
    
    public void reabrir(DataObject dob){
        fechamentoCaixa = (FechamentoCaixa)(new SalvarAcumuladoDBToplink()).pesquisaCodigo((Integer) ((Vector)dob.getArgumento0()).get(1), "FechamentoCaixa");
    }
    
    public void transferir(){
        TransferenciaCaixa tc = new TransferenciaCaixa(
                -1,
                (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa")),
                fechamentoCaixa.getValorInformado(),
                (new FinanceiroDBToplink()).pesquisaCaixaUm(),
                DataHoje.dataHoje(),
                (FStatus) new SalvarAcumuladoDBToplink().pesquisaCodigo(12, "FStatus"),
                fechamentoCaixa,
                null
        );
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(tc)){
            GenericaMensagem.warn("Erro", "Não foi possivel salvar esta Transferência!");
            sv.desfazerTransacao();
            return;
        }
        
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Transferência entre Caixas concluído!");
        fechamentoCaixa = new FechamentoCaixa();
        listaFechamento.clear();
    }
    
    public void transferir(DataObject dob){
        fechamentoCaixa = (FechamentoCaixa)(new SalvarAcumuladoDBToplink()).pesquisaCodigo((Integer) ((Vector)dob.getArgumento0()).get(1), "FechamentoCaixa");
    }
    
    public List<DataObject> getListaFechamento() {
        if (listaFechamento.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
            
            List<Vector> lista = db.listaFechamentoCaixaTransferencia(caixa.getId());
            int status = 0;
            String valor_d = "0,00";
            for (int i = 0; i < lista.size(); i++){
                float vl_fechado = Moeda.converteUS$(lista.get(i).get(2).toString()), 
                      vl_informado = Moeda.converteUS$(lista.get(i).get(3).toString());
                if (vl_fechado > vl_informado){
                    // EM FALTA
                    status = 1;
                    valor_d = Moeda.converteR$Float(Moeda.subtracaoValores(vl_fechado, vl_informado));
                }else if (vl_fechado < vl_informado){
                    // EM SOBRA
                    valor_d = Moeda.converteR$Float(Moeda.subtracaoValores(vl_informado, vl_fechado));
                    status = 2;
                }
                
                // TESTAR SE O VALOR PARA TRANSFERENCIA ESTA NA TELA -- AQUI
                listaFechamento.add(new DataObject(lista.get(i), 
                                                   DataHoje.converteData((Date)lista.get(i).get(4)), // DATA
                                                   lista.get(i).get(5).toString(), // HORA
                                                   Moeda.converteR$(lista.get(i).get(2).toString()),  // VALOR FECHAMENTO
                                                   Moeda.converteR$(lista.get(i).get(3).toString()),  // VALOR INFORMADO
                                                   status,  // STATUS
                                                   valor_d,
                                                   null,
                                                   null,
                                                   null
                )); 
            }
        }
        return listaFechamento;
    }

    public void setListaFechamento(List<DataObject> listaFechamento) {
        this.listaFechamento = listaFechamento;
    }

    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            List<Caixa> list = (new FinanceiroDBToplink()).listaCaixa();
            for (int i = 0; i < list.size(); i++) {
                listaCaixa.add(new SelectItem(new Integer(i),
                                list.get(i).getCaixa() + " - " +list.get(i).getDescricao(),
                                Integer.toString(list.get(i).getId()))
                );
            }
        }
        return listaCaixa;
    }

    public void setListaCaixa(List<SelectItem> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }

    public int getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(int idCaixa) {
        this.idCaixa = idCaixa;
    }

    public FechamentoCaixa getFechamentoCaixa() {
        return fechamentoCaixa;
    }

    public void setFechamentoCaixa(FechamentoCaixa fechamentoCaixa) {
        this.fechamentoCaixa = fechamentoCaixa;
    }
    
    public String getValorTransferencia() {
        return Moeda.converteR$(valorTransferencia);
    }

    public void setValorTransferencia(String valorTransferencia) {
        this.valorTransferencia = Moeda.substituiVirgula(valorTransferencia);
    }
        
}
