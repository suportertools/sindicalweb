package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroFechamentoCaixa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

@ManagedBean
@SessionScoped
public class CaixaFechadoBean implements Serializable{
    private List<DataObject> listaFechamento = new ArrayList<DataObject>();
    private List<SelectItem> listaCaixa = new ArrayList<SelectItem>();
    private int idCaixa = 0;
    private FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
    private String valorTransferencia = "0,00";

    public void imprimir(DataObject linha){
        FechamentoCaixa fc = null;
        fc = (FechamentoCaixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo( (Integer) ((Vector)linha.getArgumento0()).get(1), "FechamentoCaixa"));
        
        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
        FinanceiroDB db = new FinanceiroDBToplink();
        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "E");
        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "S");
        float transferencia_entrada = 0, transferencia_saida = 0, dinheiro_baixa = 0, cheque = 0, cheque_pre = 0, cartao_cre = 0, cartao_deb = 0, saldo_atual = 0;
        float dinheiro_pagamento = 0;
        Collection lista = new ArrayList();
        List<DataObject> lista_cheque = new ArrayList();
        
        
//        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fc.getId(), caixa.getId());
//        for (int i = 0; i < lista_tc.size(); i++){
//            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
//        }
        
        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaDinheiroEntrada(fc.getId(), caixa.getId());
        List<TransferenciaCaixa> lSaida = db.listaTransferenciaDinheiroSaida(fc.getId(),caixa.getId());
        for (int i = 0; i < lEntrada.size(); i++){
            transferencia_entrada = Moeda.somaValores(transferencia_entrada, lEntrada.get(i).getValor());
        } 
        
        for (int i = 0; i < lSaida.size(); i++){
            transferencia_saida = Moeda.somaValores(transferencia_saida, lSaida.get(i).getValor());
        }        
        
        for (int i = 0; i < lista_fp_entrada.size(); i++){
            switch (lista_fp_entrada.get(i).getTipoPagamento().getId()){
                case 3:
                    dinheiro_baixa = Moeda.somaValores(dinheiro_baixa, lista_fp_entrada.get(i).getValor());
                    break;
                case 4:
                    cheque = Moeda.somaValores(cheque, lista_fp_entrada.get(i).getValor());
                    lista_cheque.add(new DataObject(lista_fp_entrada.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_entrada.get(i).getValor())));
                    break;
                case 5:
                    cheque_pre = Moeda.somaValores(cheque_pre, lista_fp_entrada.get(i).getValor());
                    lista_cheque.add(new DataObject(lista_fp_entrada.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_entrada.get(i).getValor())));
                    break;
                case 6:
                    cartao_cre = Moeda.somaValores(cartao_cre, lista_fp_entrada.get(i).getValor());
                    break;
                case 7:
                    cartao_deb = Moeda.somaValores(cartao_deb, lista_fp_entrada.get(i).getValor());
                    break;
            }
        }
        
        for (int i = 0; i < lista_fp_saida.size(); i++) {
            switch (lista_fp_saida.get(i).getTipoPagamento().getId()) {
                case 3:
                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
                    //dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
                    break;
                case 4:
                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
                    //cheque = Moeda.somaValores(cheque, lista_fp_saida.get(i).getValor());
                    //lista_cheque.add(new DataObject(lista_fp_saida.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_saida.get(i).getValor())));
                    break;
                case 5:
                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
                    //cheque_pre = Moeda.somaValores(cheque_pre, lista_fp_saida.get(i).getValor());
                    break;
            }
        }        
        
        String status = "VALOR BATIDO";
        float soma = 0;
        if (fc.getValorFechamento() > fc.getValorInformado()){
            soma = Moeda.subtracaoValores(fc.getValorFechamento(), fc.getValorInformado());
            status = "EM FALTA R$ "+Moeda.converteR$Float(soma);
        }else if (fc.getValorFechamento() < fc.getValorInformado()){
            soma = Moeda.subtracaoValores(fc.getValorInformado(), fc.getValorFechamento());
            status = "EM SOBRA R$ "+Moeda.converteR$Float(soma);
        }
        
        List<Vector> lista_s = db.pesquisaSaldoAtualRelatorio(caixa.getId(), fc.getId());
        
        if (!lista_s.isEmpty()){
            saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista_s.get(0).get(1).toString()));
        }
        
        float total_dinheiro = dinheiro_baixa;
        if (!lista_cheque.isEmpty()){
            for(int i = 0; i < lista_cheque.size(); i++){
                ChequeRec cr = (ChequeRec)lista_cheque.get(i).getArgumento0();
                lista.add(new ParametroFechamentoCaixa(
                        fc.getData() + " - " + fc.getHora(),
                        caixa.getFilial().getFilial().getPessoa().getNome(),
                        Integer.toString(caixa.getCaixa()),
                        fc.getUsuario().getPessoa().getNome(),
                        Moeda.converteR$Float(fc.getValorFechamento()),
                        Moeda.converteR$Float(fc.getValorInformado()),
                        Moeda.converteR$Float(saldo_atual),
                        Moeda.converteR$Float(total_dinheiro),
                        Moeda.converteR$Float(cheque),
                        Moeda.converteR$Float(cheque_pre),
                        Moeda.converteR$Float(cartao_cre),
                        Moeda.converteR$Float(cartao_deb),
                        Moeda.converteR$Float(transferencia_entrada),
                        Moeda.converteR$Float(transferencia_saida),
                        Moeda.converteR$Float(dinheiro_pagamento),
                        status,
                        cr.getAgencia() + " - " + cr.getConta() + " " + cr.getBanco(),
                        cr.getCheque() +" - " + cr.getVencimento() + " | R$ " + lista_cheque.get(i).getArgumento1()
                ));
            }
        }else{
            lista.add(new ParametroFechamentoCaixa(
                    fc.getData() + " - " + fc.getHora(),
                    caixa.getFilial().getFilial().getPessoa().getNome(),
                    Integer.toString(caixa.getCaixa()),
                    fc.getUsuario().getPessoa().getNome(),
                    Moeda.converteR$Float(fc.getValorFechamento()),
                    Moeda.converteR$Float(fc.getValorInformado()),
                    Moeda.converteR$Float(saldo_atual),
                    Moeda.converteR$Float(total_dinheiro),
                    Moeda.converteR$Float(cheque),
                    Moeda.converteR$Float(cheque_pre),
                    Moeda.converteR$Float(cartao_cre),
                    Moeda.converteR$Float(cartao_deb),
                    Moeda.converteR$Float(transferencia_entrada),
                    Moeda.converteR$Float(transferencia_saida),
                    Moeda.converteR$Float(dinheiro_pagamento),
                    status,
                    null,
                    null
            ));
        }
        try{
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/FECHAMENTO_CAIXA.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
            
            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
//            
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Relatório Fechamento Caixa.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();
            
            
            JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
            jrviewer.setTitle("Relatório Fechamento Caixa");
            jrviewer.setVisible(true);
        }catch(Exception e){
            
        }
    }    
    
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
        FinanceiroDB db = new FinanceiroDBToplink();
        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fechamentoCaixa.getId(), caixa.getId());
        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fechamentoCaixa.getId(), caixa.getId(), "E");
        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fechamentoCaixa.getId(), caixa.getId(), "S");
        
        float dinheiro_transferencia = 0, dinheiro_baixa = 0, outros = 0, saldo_atual = 0;
        float dinheiro_pagamento = 0, outros_pagamento = 0;
        
        for (int i = 0; i < lista_tc.size(); i++){
            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
        }
        
        for (int i = 0; i < lista_fp_entrada.size(); i++){
            if (lista_fp_entrada.get(i).getTipoPagamento().getId() == 3){
                dinheiro_baixa = Moeda.somaValores(dinheiro_baixa, lista_fp_entrada.get(i).getValor());
            }else{
                outros = Moeda.somaValores(outros, lista_fp_entrada.get(i).getValor());
            }
        }
        
        for (int i = 0; i < lista_fp_saida.size(); i++){
            if (lista_fp_saida.get(i).getTipoPagamento().getId() == 3){
                dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
            }else{
                outros_pagamento = Moeda.somaValores(outros_pagamento, lista_fp_saida.get(i).getValor());
            }
        }

        List<Vector> lista = db.pesquisaSaldoAtual(caixa.getId());
        float valor_saldo_atual = 0;
        
        if (!lista.isEmpty()){
            valor_saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista.get(0).get(1).toString()));
        }
        
        float total_dinheiro = Moeda.somaValores(Moeda.somaValores(dinheiro_transferencia, dinheiro_baixa), valor_saldo_atual);
        
        float soma = Moeda.somaValores(total_dinheiro, outros);
        float soma_pagamento = Moeda.somaValores(dinheiro_pagamento, outros_pagamento);
        //float valor_minimo = Moeda.subtracaoValores(outros, soma_pagamento);
        //if (fechamentoCaixa.getValorFechamento() != soma){
            
            //if (Moeda.converteUS$(valorTransferencia) > soma){
            if (Moeda.converteUS$(valorTransferencia) > fechamentoCaixa.getValorFechamento()){
                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÁXIMO R$ " + Moeda.converteR$Float(fechamentoCaixa.getValorFechamento()));
                return;

            }


            if (Moeda.converteUS$(valorTransferencia) < outros){
                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÍNIMO R$ " + Moeda.converteR$Float(outros));
                return;
            }else if (Moeda.converteUS$(valorTransferencia) >= outros){
                //saldo_atual = Moeda.subtracaoValores(total_dinheiro, Moeda.subtracaoValores(Moeda.converteUS$(valorTransferencia), outros));
                saldo_atual = Moeda.subtracaoValores(fechamentoCaixa.getValorFechamento(),Moeda.converteUS$(valorTransferencia));
            }
            fechamentoCaixa.setSaldoAtual(saldo_atual);
        
        //}
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.alterarObjeto(fechamentoCaixa)){
            GenericaMensagem.warn("Erro", "Não foi possivel alterar Fechamento Caixa!");
            sv.desfazerTransacao();
            return;
        }
        
        TransferenciaCaixa tc = new TransferenciaCaixa(
                -1,
                caixa,
                Moeda.converteUS$(valorTransferencia),
                (new FinanceiroDBToplink()).pesquisaCaixaUm(),
                DataHoje.dataHoje(),
                (FStatus) new SalvarAcumuladoDBToplink().pesquisaCodigo(12, "FStatus"),
                fechamentoCaixa,
                null
        );
        
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
            for (int i = 0; i < lista.size(); i++){
                String valor_d = "0,00";
                int status = 0;
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
