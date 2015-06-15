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
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
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
import java.math.BigDecimal;
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
        Baixa baixa = null;
        FormaPagamento forma_pagamento = null;
        Plano5 plano = (Plano5)sv.pesquisaCodigo(Integer.valueOf(listaConta.get(idConta).getDescription()), "Plano5");
        
        if (tipo.equals("debito")){
            baixa = novaBaixa();
            lote = novoLote(sv, "P", plano, Moeda.converteUS$(valor));
            movimento = novoMovimento(sv, lote, baixa, "S");
            forma_pagamento = novaFormaPagamento(sv, baixa, Moeda.converteUS$(valor), plano, null);
        }else{
            baixa = novaBaixa();
            lote = novoLote(sv, "R", plano, Moeda.converteUS$(valor));
            movimento = novoMovimento(sv, lote, baixa, "E");
            forma_pagamento = novaFormaPagamento(sv, baixa, Moeda.converteUS$(valor), plano, null);
        }
        
        sv.abrirTransacao();
        if (!sv.inserirObjeto(baixa)){
            GenericaMensagem.warn("Erro", "Erro ao salvar Baixa");
            sv.desfazerTransacao();
            return;
        }
        
        if (!sv.inserirObjeto(lote)){
            GenericaMensagem.warn("Erro", "Erro ao salvar Lote");
            sv.desfazerTransacao();
            return;
        }
        
        if (!sv.inserirObjeto(movimento)){
            GenericaMensagem.warn("Erro", "Erro ao salvar Movimento");
            sv.desfazerTransacao();
            return;
        }
        
        if (!sv.inserirObjeto(forma_pagamento)){
            GenericaMensagem.warn("Erro", "Erro ao salvar Forma de Pagamento");
            sv.desfazerTransacao();
            return;
        }
        
        listaMovimento.clear();
        GenericaMensagem.info("Sucesso", "Movimento salvo com Sucesso!");
        sv.comitarTransacao();
    }
    
    public Lote novoLote(SalvarAcumuladoDB sv, String pag_rec, Plano5 plano, float valor){
        return new Lote(
                    -1,
                    (Rotina) sv.pesquisaCodigo(224, "Rotina"), // ROTINA
                    pag_rec, // PAG REC
                    DataHoje.data(), // LANCAMENTO
                    (Pessoa)sv.pesquisaCodigo(0, "Pessoa"), // PESSOA
                    plano, // PLANO 5
                    false,// VENCER CONTABIL
                    "", // DOCUMENTO
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
                    null // MATRICULA SOCIO
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
                    (TipoPagamento) sv.pesquisaCodigo(8, "TipoPagamento"), 
                    0, 
                    DataHoje.dataHoje(), 
                    0
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
    
    public void atualizarStatus(DataObject linha){
        ChequeRec cheque = (ChequeRec)linha.getArgumento5();
        
        if (cheque == null || cheque.getId() == -1){
            GenericaMensagem.warn("Erro", "Cheque não encontrado!");
            return;
        }
        
        int index = Integer.valueOf(linha.getArgumento4().toString());
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (index == 0){
            // LIQUIDADO
            cheque.setStatus((FStatus)sv.pesquisaCodigo(9, "FStatus"));
            
            if (!sv.alterarObjeto(cheque)){
                sv.desfazerTransacao();
                return;
            }
            
            listaMovimento.clear();
            GenericaMensagem.info("Sucesso", "Cheque LIQUIDADO concluído!");
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
            float valor = Moeda.converteUS$(linha.getArgumento2().toString());
            
            Baixa baixa_saida = novaBaixa();
            if (!sv.inserirObjeto(baixa_saida)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Baixa Saida!");
                sv.desfazerTransacao();
                return;
            }
            
            
            Lote lote_saida = novoLote(sv, "P", plano, valor);
            if (!sv.inserirObjeto(lote_saida)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Lote Saida!");
                sv.desfazerTransacao();
                return;
            }
            
            
            
            Movimento movimento_saida = novoMovimento(sv, lote_saida, baixa_saida, "S");
            if (!sv.inserirObjeto(movimento_saida)){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Movimento Saida!");
                sv.desfazerTransacao();
                return;
            }
            
            
            if (!sv.inserirObjeto(novaFormaPagamento(sv, baixa_saida, valor, plano, cheque) )){
                GenericaMensagem.warn("Erro", "Não foi possivel salvar Forma de Pagamento Saida!");
                sv.desfazerTransacao();
                return;
            }
            
            
//            Baixa baixa = (Baixa)sv.pesquisaCodigo((Integer) ((Vector)linha.getArgumento0()).get(1), "Baixa");
//            Lote lote = novoLote(sv, "P", plano, valor);
//            Movimento movimento = novoMovimento(sv, lote, baixa, "S");
//            
//            if (!sv.inserirObjeto(lote)){
//                GenericaMensagem.warn("Erro", "Não foi possivel salvar lote saida!");
//                sv.desfazerTransacao();
//            }
//            
//            if (!sv.inserirObjeto(movimento)){
//                GenericaMensagem.warn("Erro", "Não foi possivel salvar movimento saida!");
//                sv.desfazerTransacao();
//                return;
//            }
            
            //listaCheques.clear();
            //listaSelecionado.clear();
            listaMovimento.clear();
            sv.comitarTransacao();
            
            if (index == 1){
                // DEVOLVIDO
                GenericaMensagem.info("Sucesso", "Cheque DEVOLVIDO concluído!");
            }else{
                // SUSTADO
                GenericaMensagem.info("Sucesso", "Cheque SUSTADO concluído!");
                
            }
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
            List select = db.pesquisaTodos(225);
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
        if (listaMovimento.isEmpty()){
            FinanceiroDB db = new FinanceiroDBToplink();
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            
            if (listaConta.isEmpty()){
                GenericaMensagem.fatal("Erro", "Nenhuma Conta Cadastrada!");
                return new ArrayList();
            }
            
            Plano5 plano = (Plano5)sv.pesquisaCodigo(Integer.valueOf(listaConta.get(idConta).getDescription()), "Plano5");
            List<Vector> result = db.listaMovimentoBancario(plano.getId());
            for (int i = 0; i < result.size(); i++){
                ChequeRec cheque = null;
                if (result.get(i).get(10) != null)
                    cheque = (ChequeRec)sv.pesquisaCodigo((Integer) result.get(i).get(10), "ChequeRec");
                
                listaMovimento.add(
                        new DataObject(
                                result.get(i), 
                                DataHoje.converteData( (Date) result.get(i).get(2)), 
                                Moeda.converteR$Float(Float.parseFloat(Double.toString((Double)result.get(i).get(5)))),
                                Moeda.converteR$Float(((BigDecimal)result.get(i).get(7)).floatValue() ),
                                0,
                                cheque
                        )
                );
            }
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }
}
