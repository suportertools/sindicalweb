package br.com.rtools.impressao.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.beans.ConfiguracaoFinanceiroBean;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroFechamentoCaixa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class ImprimirFechamentoCaixa {
    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();
    
    @PostConstruct
    public void init(){
        cfb.init();
    }
    
    public void imprimir(Integer id_fechamento, Integer id_caixa){
        cfb.init();
        FechamentoCaixa fc = (FechamentoCaixa) (new Dao().find(new FechamentoCaixa(), id_fechamento));

        Caixa caixa = (Caixa) (new Dao().find(new Caixa(), id_caixa));
        
        FinanceiroDB db = new FinanceiroDBToplink();
        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "E");
        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "S");
        float transferencia_entrada = 0, transferencia_saida = 0, dinheiro_baixa = 0, cheque = 0, cheque_pre = 0, cartao_cre = 0, cartao_deb = 0, saldo_atual = 0;
        float deposito_bancario = 0, doc_bancario = 0, transferencia_bancaria = 0, ticket = 0, debito = 0, boleto = 0;
        float dinheiro_pagamento = 0;
        Collection lista = new ArrayList();
        List<DataObject> lista_cheque = new ArrayList();

//        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fc.getId(), caixa.getId());
//        for (int i = 0; i < lista_tc.size(); i++){
//            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
//        }
        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaDinheiroEntrada(fc.getId(), caixa.getId());
        List<TransferenciaCaixa> lSaida = db.listaTransferenciaDinheiroSaida(fc.getId(), caixa.getId());
        for (int i = 0; i < lEntrada.size(); i++) {
            transferencia_entrada = Moeda.somaValores(transferencia_entrada, lEntrada.get(i).getValor());
        }

        for (int i = 0; i < lSaida.size(); i++) {
            transferencia_saida = Moeda.somaValores(transferencia_saida, lSaida.get(i).getValor());
        }

        
        for (int i = 0; i < lista_fp_entrada.size(); i++) {
            switch (lista_fp_entrada.get(i).getTipoPagamento().getId()) {
                case 2:
                    boleto = Moeda.somaValores(boleto, lista_fp_entrada.get(i).getValor());
                    break;
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
                case 8:
                    deposito_bancario = Moeda.somaValores(deposito_bancario, lista_fp_entrada.get(i).getValor());
                    break;
                case 9:
                    doc_bancario = Moeda.somaValores(doc_bancario, lista_fp_entrada.get(i).getValor());
                    break;
                case 10:
                    transferencia_bancaria = Moeda.somaValores(transferencia_bancaria, lista_fp_entrada.get(i).getValor());
                    break;
                case 11:
                    ticket = Moeda.somaValores(ticket, lista_fp_entrada.get(i).getValor());
                    break;
                case 13:
                    debito = Moeda.somaValores(debito, lista_fp_entrada.get(i).getValor());
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
        if (fc.getValorFechamento() > fc.getValorInformado()) {
            soma = Moeda.subtracaoValores(fc.getValorFechamento(), fc.getValorInformado());
            status = "EM FALTA R$ " + Moeda.converteR$Float(soma);
        } else if (fc.getValorFechamento() < fc.getValorInformado()) {
            soma = Moeda.subtracaoValores(fc.getValorInformado(), fc.getValorFechamento());
            status = "EM SOBRA R$ " + Moeda.converteR$Float(soma);
        }

        List<Vector> lista_s = db.pesquisaSaldoAtualRelatorio(caixa.getId(), fc.getId());
        if (!lista_s.isEmpty()) {
            saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista_s.get(0).get(1).toString()));
        }

        List<Vector> lista_u = db.pesquisaUsuarioFechamento(fc.getId());
        String usuarios = "";
        if (!lista_u.isEmpty()) {
            for (int i = 0; i < lista_u.size(); i++) {
                if (usuarios.length() > 0 && i != lista_u.size()) {
                    usuarios += " / ";
                }
                usuarios += lista_u.get(i).get(0).toString();
            }
        }

        float total_dinheiro = dinheiro_baixa;
        float valor_transferido = Moeda.somaValores(Moeda.somaValores(Moeda.somaValores(Moeda.somaValores(dinheiro_baixa, cheque), cheque_pre), ticket), saldo_atual);
        if (!lista_cheque.isEmpty()) {
            for (int i = 0; i < lista_cheque.size(); i++) {
                ChequeRec cr = (ChequeRec) lista_cheque.get(i).getArgumento0();
                lista.add(new ParametroFechamentoCaixa(
                        fc.getData() + " - " + fc.getHora(),
                        caixa.getFilial().getFilial().getPessoa().getNome(),
                        caixa.getCaixa() == 0 ? "" : Integer.toString(caixa.getCaixa()),
                        usuarios,
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
                        cr.getCheque() + " - " + cr.getVencimento() + " | R$ " + lista_cheque.get(i).getArgumento1(),
                        caixa.getDescricao(),
                        cfb.getConfiguracaoFinanceiro().isAlterarValorFechamento(),
                        Moeda.converteR$Float(valor_transferido),
                        Moeda.converteR$Float(Moeda.subtracaoValores(valor_transferido, caixa.getFundoFixo())),
                        Moeda.converteR$Float(caixa.getFundoFixo()),
                        Moeda.converteR$Float(deposito_bancario),
                        Moeda.converteR$Float(doc_bancario),
                        Moeda.converteR$Float(transferencia_bancaria),
                        Moeda.converteR$Float(ticket),
                        Moeda.converteR$Float(debito),
                        Moeda.converteR$Float(boleto)
                ));
            }
        } else {
            lista.add(new ParametroFechamentoCaixa(
                    fc.getData() + " - " + fc.getHora(),
                    caixa.getFilial().getFilial().getPessoa().getNome(),
                    caixa.getCaixa() == 0 ? "" : Integer.toString(caixa.getCaixa()),
                    usuarios,
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
                    null,
                    caixa.getDescricao(),
                    cfb.getConfiguracaoFinanceiro().isAlterarValorFechamento(),
                    Moeda.converteR$Float(valor_transferido),
                    Moeda.converteR$Float(Moeda.subtracaoValores(valor_transferido, caixa.getFundoFixo())),
                    Moeda.converteR$Float(caixa.getFundoFixo()),
                    Moeda.converteR$Float(deposito_bancario),
                    Moeda.converteR$Float(doc_bancario),
                    Moeda.converteR$Float(transferencia_bancaria),
                    Moeda.converteR$Float(ticket),
                    Moeda.converteR$Float(debito),
                    Moeda.converteR$Float(boleto)
            ));
        }
        try {
            Jasper.PATH = "downloads";
            Jasper.PART_NAME = "";
            Jasper.printReports("/Relatorios/FECHAMENTO_CAIXA.jasper", "fechamento_caixa", lista);
//            
//            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/FECHAMENTO_CAIXA.jasper"));
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
//
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
//            
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Relatório Fechamento Caixa.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();

//            JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
//            jrviewer.setTitle("Relatório Fechamento Caixa");
//            jrviewer.setVisible(true);
        } catch (Exception e) {
            e.getMessage();
        }
    }
}
