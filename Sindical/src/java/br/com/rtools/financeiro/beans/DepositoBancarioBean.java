package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Rotina;
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
public class DepositoBancarioBean implements Serializable{
    private int idConta = 0;
    private List<SelectItem> listaConta = new ArrayList<SelectItem>();
    private List<DataObject> listaCheques = new ArrayList<DataObject>();
    private List<DataObject> listaSelecionado = new ArrayList<DataObject>();
    
    public void depositar(){
        if (listaSelecionado.isEmpty()){
            GenericaMensagem.warn("Erro", "Nenhum cheque foi selecionado!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        //Baixa baixa = (Baixa) sv.pesquisaCodigo((Integer) ((Vector)listaSelecionado.get(i).getArgumento0()).get(1), "Baixa");
        
        // MOVIMENTO SAIDA -----------------------------------------------------
        // ---------------------------------------------------------------------
        // ---------------------------------------------------------------------
        Baixa baixa_saida = null;
        Lote lote_saida = null;
        Movimento movimento_saida = null;
        for (int i = 0; i < listaSelecionado.size(); i++){
            if (baixa_saida == null){
                baixa_saida = novaBaixa();
                if (!sv.inserirObjeto(baixa_saida)){
                    GenericaMensagem.warn("Erro", "Não foi possivel salvar Baixa Saida!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            
            ChequeRec cheque = (ChequeRec) listaSelecionado.get(i).getArgumento4();
            
            if (!mensagemStatus(cheque.getStatus().getId())){
                return;
            }
            
            float valor = Moeda.converteUS$(listaSelecionado.get(i).getArgumento3().toString());
            
            Plano5 plano = (Plano5) sv.pesquisaCodigo(1, "Plano5");
            if (lote_saida == null){
                lote_saida = novoLote(sv, "P", plano, cheque, valor);
                if (!sv.inserirObjeto(lote_saida)){
                    GenericaMensagem.warn("Erro", "Não foi possivel salvar Lote Saida!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            
            
            if (movimento_saida == null){
                movimento_saida = novoMovimento(sv, lote_saida, baixa_saida, "S");
                if (!sv.inserirObjeto(movimento_saida)){
                    GenericaMensagem.warn("Erro", "Não foi possivel salvar Movimento Saida!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            
            if (!sv.inserirObjeto(novaFormaPagamento(sv, baixa_saida, valor, plano, cheque) )){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Forma de Pagamento Saida!");
                sv.desfazerTransacao();
                return;
            }
        }        
        
        // MOVIMENTO ENTRADA ---------------------------------------------------
        // ---------------------------------------------------------------------
        // ---------------------------------------------------------------------
        Baixa baixa_entrada = null;
        Lote lote_entrada = null;
        Movimento movimento_entrada = null;
        for (int i = 0; i < listaSelecionado.size(); i++){
            if (baixa_entrada == null){
                baixa_entrada = novaBaixa();
                if (!sv.inserirObjeto(baixa_entrada)){
                    GenericaMensagem.warn("Erro", "Não foi possivel salvar Baixa Entrada!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            
            ChequeRec cheque = (ChequeRec) listaSelecionado.get(i).getArgumento4();
            
            if (!mensagemStatus(cheque.getStatus().getId())){
                return;
            }
            
            float valor = Moeda.converteUS$(listaSelecionado.get(i).getArgumento3().toString());
            
            Plano5 plano = (Plano5)sv.pesquisaCodigo( Integer.valueOf(listaConta.get(idConta).getDescription()), "Plano5");
            
            
            if (lote_entrada == null){
                lote_entrada = novoLote(sv, "R", plano, cheque, valor);
                if (!sv.inserirObjeto(lote_entrada)){
                    GenericaMensagem.warn("Erro", "Não foi possivel salvar Lote Entrada!");
                    sv.desfazerTransacao();
                    return;
                }
            }
            
            movimento_entrada = novoMovimento(sv, lote_entrada, baixa_entrada, "E");
            if (!sv.inserirObjeto(movimento_entrada)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Movimento Entrada!");
                sv.desfazerTransacao();
                return;
            }
            
            cheque.setStatus((FStatus)sv.pesquisaCodigo(8, "FStatus"));
            if (!sv.alterarObjeto(cheque)){
                GenericaMensagem.warn("Erro", "Não foi possivel atualizar cheque!");
                sv.desfazerTransacao();
                return;
            }
            
            if (!sv.inserirObjeto(novaFormaPagamento(sv, baixa_entrada, valor, plano, cheque) )){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Forma de Pagamento Saida!");
                sv.desfazerTransacao();
                return;
            }
        }
        
        listaCheques.clear();
        listaSelecionado.clear();
        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Cheques depositados com Sucesso!");
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
                    0     // DESCONTO
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
                    0, // REPASSE AUTOMATICO
                    null // MATRICULA SÓCIO
            );
    }
    
    public Baixa novaBaixa(){
        return new Baixa(
                -1, 
                (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"), 
                DataHoje.data(), 
                "", 
                0, 
                "", 
                null, 
                null,
                null,
                0
        );
    }      
    
    public FormaPagamento novaFormaPagamento(SalvarAcumuladoDB sv, Baixa baixa, float valor, Plano5 plano, ChequeRec cheque){
        return new FormaPagamento(
                    -1, 
                    baixa, 
                    cheque, 
                    null, 
                    100, 
                    valor, 
                    (Filial) sv.pesquisaCodigo(1, "Filial"), 
                    plano, 
                    null, 
                    null, 
                    null, 
                    0, 
                    DataHoje.dataHoje(), 
                    0
            );
    }  
    
    public List<SelectItem> getListaConta() {
        if (listaConta.isEmpty()) {
            //SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            Plano5DB db = new Plano5DBToplink();
            
            List<Plano5> result = db.pesquisaCaixaBanco();
            //List<ContaBanco> select = sv.listaObjeto("ContaBanco");
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
    
    public boolean mensagemStatus(int id_status){
        if (id_status == 8){
            GenericaMensagem.warn("Erro", "Cheque DEPOSITADO não pode ser selecionado!");
            return false;
        }else if (id_status == 9){
            GenericaMensagem.warn("Erro", "Cheque LIQUIDADO não pode ser selecionado!");
            return false;
        }else if (id_status == 10){
            GenericaMensagem.warn("Erro", "Cheque DEVOLVIDO não pode ser selecionado!");
            return false;
        }else if (id_status == 11){
            GenericaMensagem.warn("Erro", "Cheque SUSTADO não pode ser selecionado!");
            return false;
        }
        return true;
    }

    public void setListaConta(List<SelectItem> listaConta) {
        this.listaConta = listaConta;
    }
    
    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public List<DataObject> getListaCheques() {
        if (listaCheques.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            
            List<Vector> result = db.listaDeCheques(7);
            
            for (int i = 0; i < result.size(); i++){
                ChequeRec cheque = (ChequeRec)sv.pesquisaCodigo((Integer) result.get(i).get(0), "ChequeRec");
                listaCheques.add(
                    new DataObject(
                        result.get(i), // QUERY CHEQUES
                        DataHoje.converteData( (Date) result.get(i).get(6)), // DATA EMISSAO
                        DataHoje.converteData( (Date) result.get(i).get(7)), // DATA VENCIMENTO
                        Moeda.converteR$Float(Float.parseFloat(Double.toString((Double)result.get(i).get(8)))), // VALOR
                        cheque, // CHEQUE
                        null
                    )
                );
            }
        }
        return listaCheques;
    }


    public List<DataObject> getListaSelecionado() {
        if (listaCheques.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            
            List<Vector> result = db.listaDeCheques(7);

            for (int i = 0; i < result.size(); i++){
                ChequeRec cheque = (ChequeRec)sv.pesquisaCodigo((Integer) result.get(i).get(0), "ChequeRec");
                if (cheque.getStatus().getId() == 7){
                    listaSelecionado.add(
                        new DataObject(
                            result.get(i), // QUERY CHEQUES
                            DataHoje.converteData( (Date) result.get(i).get(6)), // DATA EMISSAO
                            DataHoje.converteData( (Date) result.get(i).get(7)), // DATA VENCIMENTO
                            Moeda.converteR$Float(Float.parseFloat(Double.toString((Double)result.get(i).get(8)))), // VALOR
                            cheque, // CHEQUE
                            null
                        )
                    );
                }
            }
        }
        return listaSelecionado;
    }

    public void setListaSelecionado(List<DataObject> listaSelecionado) {
        this.listaSelecionado = listaSelecionado;
    }

}
