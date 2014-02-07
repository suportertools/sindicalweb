package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class MovimentoBancarioBean implements Serializable{
    private int idConta = 0;
    private int idServicos = 0;
    private List<SelectItem> listaConta = new ArrayList<SelectItem>();
    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
    private String valor = "";
    private String tipo = "";
    private List<DataObject> listaMovimento = new ArrayList<DataObject>();
    
    public void salvar(){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        
        
        Lote lote = null;
        Movimento movimento = null;
        Plano5 plano = (Plano5)sv.pesquisaCodigo( Integer.valueOf(listaConta.get(idConta).getDescription()), "Plano5");
        
        if (tipo.equals("debito")){
            lote = novoLote(sv, "P", plano, null, Moeda.converteUS$(valor));
            movimento = novoMovimento(sv, lote, null, "S");
        }else{
            lote = novoLote(sv, "R", plano, null, Moeda.converteUS$(valor));
            movimento = novoMovimento(sv, lote, null, "E");
        }
        
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(lote)){
            GenericaMensagem.warn("Erro", "Erro ao salvar lote");
            sv.desfazerTransacao();
            return;
        }
        
        if (!sv.inserirObjeto(movimento)){
            GenericaMensagem.warn("Erro", "Erro ao salvar movimento");
            sv.desfazerTransacao();
            return;
        }
        
        
        GenericaMensagem.info("Sucesso", "Movimento salvo com Sucesso!");
        //sv.comitarTransacao();
    }
    
    public Lote novoLote(SalvarAcumuladoDB sv, String pag_rec, Plano5 plano, ChequeRec cheque, float valor){
        return new Lote(
                    -1,
                    (Rotina) sv.pesquisaCodigo(224, "Rotina"), // ROTINA
                    pag_rec, // PAG REC
                    DataHoje.data(), // LANCAMENTO
                    (Pessoa)sv.pesquisaCodigo(0, "Pessoa"), // PESSOA
                    plano, // PLANO 5
                    false,// VENCER CONTABIL
                    cheque.getCheque(), // DOCUMENTO
                    valor, // VALOR
                    (Filial) sv.pesquisaCodigo(1, "Filial"), // FILIAL
                    null, // DEPARTAMENTO
                    null, // EVT
                    "Deposito bancário para a conta ??", // HISTORICO
                    (FTipoDocumento) sv.pesquisaCodigo(4, "FTipoDocumento"), // 4 - CHEQUE / 5 - CHEQUE PRE
                    (CondicaoPagamento) sv.pesquisaCodigo(1, "CondicaoPagamento"), // 1 - A VISTA / 2 - PRAZO
                    (FStatus) sv.pesquisaCodigo(1, "FStatus"), // 1 - EFETIVO // 8 - DEPOSITADO
                    null, // PESSOA SEM CADASTRO
                    false, // DESCONTO FOLHA
                    null // MATRICULA SOCIO
            );
    }
    
    public Movimento novoMovimento(SalvarAcumuladoDB sv, Lote lote, Baixa baixa, String e_s){
        return new Movimento(
                    -1,
                    lote,
                    lote.getPlano5(), // PLANO 5
                    lote.getPessoa(), // PESSOA
                    (Servicos)sv.pesquisaCodigo(50, "Servicos"), // SERVICO
                    baixa, // BAIXA
                    (TipoServico)sv.pesquisaCodigo(1, "TipoServico"), // TIPO SERVICO
                    null, // ACORDO
                    lote.getValor(), // VALOR
                    "", // REFERENCIA
                    DataHoje.data(), // VENCIMENTO
                    1, // QND
                    true, // ATIVO
                    e_s, // E_S
                    false, // OBRIGACAO 
                    null, // TITULAR
                    null, // BENEFICIARIO
                    "", // DOCUMENTO
                    "", // NR_CTR_BOLETO
                    DataHoje.data(), // VENCTO ORIGINAL
                    0, // DESCONTO ATE VENCIMENTO
                    0, // CORRECAO
                    0, // JUROS
                    0, // MULTA
                    0, // DESCONTO
                    0, // TAXA
                    lote.getValor(), // VALOR BAIXA
                    lote.getFtipoDocumento(), // 4 - CHEQUE / 5 - CHEQUE PRE
                    0 // REPASSE AUTOMATICO
            );
    }    
    
    public void atualizarStatus(DataObject linha){
        int index = Integer.valueOf(linha.getArgumento5().toString());
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        ChequeRec cheque = (ChequeRec)linha.getArgumento4();
        
        sv.abrirTransacao();
        if (index == 0){
            // LIQUIDADO
            cheque.setStatus((FStatus)sv.pesquisaCodigo(9, "FStatus"));
            
            if (!sv.alterarObjeto(cheque)){
                sv.desfazerTransacao();
                return;
            }
            
            sv.comitarTransacao();
        }else if (index == 1 || index == 2){
            if (index == 1){
                // DEVOLVIDO
                cheque.setStatus((FStatus)sv.pesquisaCodigo(10, "FStatus"));
            }else{
                // SUSTADO
                cheque.setStatus((FStatus)sv.pesquisaCodigo(11, "FStatus"));
            }
            
            if (!sv.alterarObjeto(cheque)){
                sv.desfazerTransacao();
                return;
            }
            
            Plano5 plano = (Plano5)sv.pesquisaCodigo( Integer.valueOf(listaConta.get(idConta).getDescription()), "Plano5");
            float valor = Moeda.converteUS$(linha.getArgumento3().toString());
            Baixa baixa = (Baixa)sv.pesquisaCodigo((Integer) ((Vector)linha.getArgumento0()).get(1), "Baixa");
            
            Lote lote = novoLote(sv, "P", plano, cheque, valor);
            Movimento movimento = novoMovimento(sv, lote, baixa, "S");
            
            if (!sv.inserirObjeto(lote)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar lote saida!");
                sv.desfazerTransacao();
            }
            
            if (!sv.inserirObjeto(movimento)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar movimento saida!");
                sv.desfazerTransacao();
                return;
            }
            
            //listaCheques.clear();
            //listaSelecionado.clear();
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Cheques depositados com Sucesso!");
        }
    }    
    
    public List<SelectItem> getListaConta() {
        if (listaConta.isEmpty()) {
            Plano5DB db = new Plano5DBToplink();
            
            List<Plano5> result = db.pesquisaCaixaBanco();
            for (int i = 0; i < result.size(); i++) {
                listaConta.add(new SelectItem(
                        new Integer(i), 
                        result.get(i).getContaBanco().getBanco().getBanco() + " - " + result.get(i).getContaBanco().getAgencia() + " - " + result.get(i).getContaBanco().getConta(), 
                        Integer.toString((result.get(i).getId())))
                );
            }
        }
        return listaConta;
    }

    public List<SelectItem> getListaServicos() {
        if (listaServicos.isEmpty()){
            ServicosDB db = new ServicosDBToplink();
            List select = db.pesquisaTodos(4);
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaServicos.add(new SelectItem(
                            new Integer(i),
                            (String) ((Servicos) select.get(i)).getDescricao(),
                            Integer.toString(((Servicos) select.get(i)).getId())
                        )
                    );
                }
            }
        }
        return listaServicos;
    }
    
    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }
    
    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<DataObject> getListaMovimento() {
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }
}
