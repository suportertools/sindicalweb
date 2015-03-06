package br.com.rtools.associativo.beans;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class AcordoSocialBean implements Serializable{
    private Acordo acordo = new Acordo();
    private Pessoa pessoa = new Pessoa();
    private List<DataObject> listaVizualizado = new ArrayList();
    private String total = "0,00";
    private int parcela = 1;
    private int idVencimento = 0;
    private List<SelectItem> listaVencimento = new ArrayList();
    private int frequencia = 30;
    private String valorEntrada = "0,00";
    private String vencimento = DataHoje.data();
    private Historico historico = new Historico();
    private List<DataObject> listaOperado = new ArrayList();
    private List<Movimento> listaMovs = new ArrayList();
    
    @PostConstruct
    public void init(){
        loadPessoa();
        loadListaVisualizado();
    }
    
    public void loadPessoa(){
        if (GenericaSessao.getObject("listaMovimento") != null) {
            listaMovs = GenericaSessao.getList("listaMovimento");
            pessoa = listaMovs.get(0).getPessoa();
            GenericaSessao.remove("listaMovimento");
        }
    }
    
    public void loadListaVisualizado(){
        if (listaVizualizado.isEmpty() && !listaMovs.isEmpty() && pessoa.getId() != -1) {
            historico.setHistorico("Acordo correspondente a: ");
//            List<DataObject> aux = new ArrayList();
//            aux.add(null);
//            aux.add(null);
//            aux.add(null);
//            aux.add(null);
            float soma = 0;

            String h_sind = "",
                    h_assis = "",
                    h_conf = "",
                    h_neg = "";
            
            Map<Integer, DataObject> hash = new LinkedHashMap();
            String s_historico = "";
            for (Movimento listaMov : listaMovs) {
                soma = Moeda.somaValores(soma, listaMov.getValorBaixa());
                //aux.set(1, new DataObject(listaMov.getServicos(), soma, null, null, null, null));
                
                if (hash.get(listaMov.getServicos().getId()) == null)
                    s_historico = listaMov.getServicos().getDescricao() + ", " + listaMov.getReferencia();
                else
                    s_historico += hash.get(listaMov.getServicos().getId()).getArgumento2() + ", " + listaMov.getReferencia();
                
                hash.put(listaMov.getServicos().getId(), new DataObject(listaMov.getServicos(), Moeda.converteR$Float(soma), s_historico ));
            }
            
            for (Map.Entry<Integer, DataObject> entry : hash.entrySet()) {
                listaVizualizado.add(new DataObject(entry.getValue().getArgumento0(), entry.getValue().getArgumento1()));
            }
            
            historico.setHistorico(historico.getHistorico() + h_sind + " " + h_assis + " " + h_conf + " " + h_neg);
            
            //float soma_total = Moeda.somaValores(Moeda.somaValores(soma_assis, soma_conf), soma_neg);
            total = Moeda.converteR$Float(soma);
//            
//            for (DataObject aux1 : aux) {
//                if (aux1 != null) {
//                    listaVizualizado.add(new DataObject(aux1.getArgumento0(), Moeda.converteR$Float((Float) aux1.getArgumento1()), aux1.getArgumento2(), (String) aux1.getArgumento3(), null, null));
//                }
//            }
        }
    }
    
    public synchronized void adicionarParcela() {
        
    }
    
    public synchronized String subirData() {
        return null;
    }
    
    public synchronized String descerData() {
        return null;
    }
    
    public void limparEntrada() {
        valorEntrada = "0,00";
    }
    
    public Acordo getAcordo() {
        return acordo;
    }

    public void setAcordo(Acordo acordo) {
        this.acordo = acordo;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<DataObject> getListaVizualizado() {
        return listaVizualizado;
    }

    public void setListaVizualizado(List<DataObject> listaVizualizado) {
        this.listaVizualizado = listaVizualizado;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getParcela() {
        return parcela;
    }

    public void setParcela(int parcela) {
        this.parcela = parcela;
    }

    public int getIdVencimento() {
        return idVencimento;
    }

    public void setIdVencimento(int idVencimento) {
        this.idVencimento = idVencimento;
    }

    public List<SelectItem> getListaVencimento() {
        if (listaVencimento.isEmpty()){
            int i = 0;
            DataHoje data = new DataHoje();
            listaVencimento.add(new SelectItem(
                    i,
                    vencimento)
            );
            i++;
            while (i < 31) {
                listaVencimento.add(new SelectItem(
                        i,
                        data.incrementarDias(i, vencimento))
                );
                i++;
            }
        }
        return listaVencimento;
    }

    public void setListaVencimento(List<SelectItem> listaVencimento) {
        this.listaVencimento = listaVencimento;
    }

    public int getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(int frequencia) {
        this.frequencia = frequencia;
    }

    public String getValorEntrada() {
        return valorEntrada;
    }

    public void setValorEntrada(String valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public Historico getHistorico() {
        return historico;
    }

    public void setHistorico(Historico historico) {
        this.historico = historico;
    }

    public List<DataObject> getListaOperado() {
        return listaOperado;
    }

    public void setListaOperado(List<DataObject> listaOperado) {
        this.listaOperado = listaOperado;
    }
    
}
