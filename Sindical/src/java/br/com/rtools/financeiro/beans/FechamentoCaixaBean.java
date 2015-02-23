package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.ContaSaldo;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroCaixaAnalitico;
import br.com.rtools.impressao.ParametroFechamentoCaixa;
import br.com.rtools.impressao.ResumoFechamentoCaixa;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
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
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

@ManagedBean
@SessionScoped
public final class FechamentoCaixaBean implements Serializable {

    private int idCaixa = 0;
    private final List<SelectItem> listaCaixa = new ArrayList();
    private int idCaixaDestino = 0;
    private List<SelectItem> listaCaixaDestino = new ArrayList();
    private FechamentoCaixa fechamento = new FechamentoCaixa();
    private String valor = "";
    private String valorTransferencia = "";
    //private String saldoAnterior = "";
    private String saldoAtual = "0,00";
    private String dataSaldo = "";
    private ContaSaldo contaSaldo = new ContaSaldo();
    private List<DataObject> listaFechamento = new ArrayList();
    private String dataResumoFechamento = DataHoje.data();

    public FechamentoCaixaBean() {
        getListaCaixa();
    }
    
    public boolean permissaoFechamentoCaixa(){
        ControleAcessoBean cab = new ControleAcessoBean();
        MacFilial mac = MacFilial.getAcessoFilial();
        
        if (mac != null && mac.getId() != -1 && mac.getCaixa() != null){
            if ( Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) != mac.getCaixa().getId() && cab.getBotaoFecharCaixaOutroUsuario()){
                return true;
            }else
                return false;
        }
        return true;
    }
    
    public void analitico(DataObject linha){
        FinanceiroDB db = new FinanceiroDBToplink();
        
        // id_fechamento_caixa
        List<Vector> result = db.listaRelatorioAnalitico((Integer) ((Vector) linha.getArgumento0()).get(1));
        Collection lista = new ArrayList();
        
        for (int i = 0; i < result.size(); i++){
            lista.add(new ParametroCaixaAnalitico(
                    result.get(i).get(0).toString(), 
                    DataHoje.converteData((Date)result.get(i).get(1)), 
                    (result.get(i).get(2) == null) ? "" : result.get(i).get(2).toString(), 
                    (result.get(i).get(3) == null) ? "" : result.get(i).get(3).toString(), 
                    (result.get(i).get(4) == null) ? "" : result.get(i).get(4).toString(), 
                    (result.get(i).get(5) == null) ? "" : result.get(i).get(5).toString(), 
                    (result.get(i).get(6) == null) ? "" : result.get(i).get(6).toString(), 
                    (result.get(i).get(7) == null) ? "" : result.get(i).get(7).toString(), 
                    (result.get(i).get(8) == null) ? "" : result.get(i).get(8).toString(), 
                    BigDecimal.valueOf(Double.valueOf(String.valueOf(Moeda.converteUS$(result.get(i).get(9).toString())))), 
                    BigDecimal.valueOf(Double.valueOf(String.valueOf(Moeda.converteUS$(result.get(i).get(10).toString())))),
                    DataHoje.converteData((Date)result.get(i).get(12))
            )
            );
        }
        
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CAIXA_ANALITICO.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
            
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"Relatório Caixa Analítico.pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException | IOException e) {
            e.getMessage();
        }        
    }
    
    public void resumoFechamentoCaixa(){
        if (dataResumoFechamento.isEmpty()){
            return;
        }
        
        FinanceiroDB db = new FinanceiroDBToplink();
        
        // data do fechamento
        List<Vector> result = db.listaResumoFechamentoCaixa(dataResumoFechamento);
        //result.addAll(db.listaResumoFechamentoCaixa("09/02/2015"));
        Collection lista = new ArrayList();
        
        for (int i = 0; i < result.size(); i++){
            lista.add(new ResumoFechamentoCaixa(
                    DataHoje.converteData((Date)result.get(i).get(0)), 
                    (result.get(i).get(1) == null) ? "" : result.get(i).get(1).toString(), 
                    (result.get(i).get(2) == null) ? "" : result.get(i).get(2).toString(), 
                    (result.get(i).get(3) == null) ? "" : result.get(i).get(3).toString(), 
                    (result.get(i).get(4) == null) ? "" : result.get(i).get(4).toString(), 
                    BigDecimal.valueOf(Double.valueOf(String.valueOf(Moeda.converteUS$(result.get(i).get(5).toString()))))
            )
            );
        }
        
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RESUMO_FECHAMENTO_CAIXA.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
            
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"Resumo Fechamento Caixa.pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();

        } catch (JRException | IOException e) {
            e.getMessage();
        }        
    }

    public void imprimir(DataObject linha) {
        FechamentoCaixa fc = null;
        if (linha != null) {
            fc = (FechamentoCaixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo((Integer) ((Vector) linha.getArgumento0()).get(1), "FechamentoCaixa"));
        } else {
            fc = fechamento;
        }

        Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
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
        List<TransferenciaCaixa> lSaida = db.listaTransferenciaDinheiroSaida(fc.getId(), caixa.getId());
        for (int i = 0; i < lEntrada.size(); i++) {
            transferencia_entrada = Moeda.somaValores(transferencia_entrada, lEntrada.get(i).getValor());
        }

        for (int i = 0; i < lSaida.size(); i++) {
            transferencia_saida = Moeda.somaValores(transferencia_saida, lSaida.get(i).getValor());
        }

        for (int i = 0; i < lista_fp_entrada.size(); i++) {
            switch (lista_fp_entrada.get(i).getTipoPagamento().getId()) {
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
        if (!lista_cheque.isEmpty()) {
            for (int i = 0; i < lista_cheque.size(); i++) {
                ChequeRec cr = (ChequeRec) lista_cheque.get(i).getArgumento0();
                lista.add(new ParametroFechamentoCaixa(
                        fc.getData() + " - " + fc.getHora(),
                        caixa.getFilial().getFilial().getPessoa().getNome(),
                        Integer.toString(caixa.getCaixa()),
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
                        cr.getCheque() + " - " + cr.getVencimento() + " | R$ " + lista_cheque.get(i).getArgumento1()
                ));
            }
        } else {
            lista.add(new ParametroFechamentoCaixa(
                    fc.getData() + " - " + fc.getHora(),
                    caixa.getFilial().getFilial().getPessoa().getNome(),
                    Integer.toString(caixa.getCaixa()),
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
                    null
            ));
        }
        try {
            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/FECHAMENTO_CAIXA.jasper"));
            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);

            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
            
            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            res.setContentType("application/pdf");
            res.setHeader("Content-disposition", "inline; filename=\"Relatório Fechamento Caixa.pdf\"");
            res.getOutputStream().write(arquivo);
            res.getCharacterEncoding();
            FacesContext.getCurrentInstance().responseComplete();

//            JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
//            jrviewer.setTitle("Relatório Fechamento Caixa");
//            jrviewer.setVisible(true);
        } catch (JRException | IOException e) {
            e.getMessage();
        }
    }

    public void transferir() {
        if (!listaCaixa.isEmpty() && Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) == 0){
            GenericaMensagem.error("Erro", "Lista de Caixa incompleta!");
            return;
        }
        
        if (!listaCaixaDestino.isEmpty() && Integer.valueOf(listaCaixaDestino.get(idCaixa).getDescription()) == 0){
            GenericaMensagem.error("Erro", "Lista de Caixa Destino incompleta!");
            return;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
        Caixa caixa_destino = (Caixa) sv.pesquisaCodigo(Integer.valueOf(listaCaixaDestino.get(idCaixaDestino).getDescription()), "Caixa");

        FinanceiroDB db = new FinanceiroDBToplink();
        List<Vector> result_entrada = db.listaMovimentoCaixa(caixa.getId(), "E", null);
        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaEntrada(caixa.getId(), null);

        if (result_entrada.isEmpty() && lEntrada.isEmpty()) {
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
                (FStatus) sv.pesquisaCodigo(13, "FStatus"),
                null,
                null,
                (Usuario) GenericaSessao.getObject("sessaoUsuario")
        );

        if (!sv.inserirObjeto(tc)) {
            GenericaMensagem.warn("Erro", "Não foi possivel completar transferência!");
            sv.desfazerTransacao();
        }

        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Dinheiro transferido com Sucesso!");
        valorTransferencia = "0,00";
    }

    public void excluir(Caixa c) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

    }

    public void salvar() {
        Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");

        if (usuario == null) {
            GenericaMensagem.warn("Erro", "Faça o login novamente!");
            return;
        }

        FinanceiroDB db = new FinanceiroDBToplink();

        Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));

        if (!db.listaFechamentoCaixaTransferencia(caixa.getId()).isEmpty()) {
            GenericaMensagem.warn("Erro", "Seu caixa ainda NÃO FOI TRANSFERIDO, caixa não pode ser fechado!");
            return;
        }

        ControleAcessoBean cab = new ControleAcessoBean();
        List<Vector> result_entrada;
        List<Vector> result_saida;
        List<TransferenciaCaixa> lEntrada;
        List<TransferenciaCaixa> lSaida;
        
        // true NÃO TEM PERMISSÃO
        boolean permissao = cab.getBotaoFecharCaixaOutroUsuario();
        if (permissao){
            result_entrada = db.listaMovimentoCaixa(caixa.getId(), "E", usuario.getId());
            result_saida = db.listaMovimentoCaixa(caixa.getId(), "S", usuario.getId());
            lEntrada = db.listaTransferenciaEntrada(caixa.getId(), usuario.getId());
            lSaida = db.listaTransferenciaSaida(caixa.getId(), usuario.getId());
        }else{
            result_entrada = db.listaMovimentoCaixa(caixa.getId(), "E", null);
            result_saida = db.listaMovimentoCaixa(caixa.getId(), "S", null);
            lEntrada = db.listaTransferenciaEntrada(caixa.getId(), null);
            lSaida = db.listaTransferenciaSaida(caixa.getId(), null);
        }
        
        if (result_entrada.isEmpty() && result_saida.isEmpty() && lEntrada.isEmpty() && lSaida.isEmpty() && permissao) {
            GenericaMensagem.warn("Erro", "Usuário não efetuou recebimento neste Caixa!");
            return;
        }else if (result_entrada.isEmpty() && result_saida.isEmpty() && lEntrada.isEmpty() && lSaida.isEmpty()){
            GenericaMensagem.warn("Erro", "Não existe movimentos para este Caixa!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        // CALCULO PARA SOMAR OS VALORES DA QUERY
        fechamento.setValorInformado(Moeda.converteUS$(valor));
        fechamento.setUsuario(usuario);
        //fechamento.setSaldoAtual(Moeda.converteUS$(saldoAtual));
        if (!sv.inserirObjeto(fechamento)) {
            GenericaMensagem.warn("Erro", "Não foi possivel concluir este fechamento!");
            sv.desfazerTransacao();
            fechamento = new FechamentoCaixa();
            return;
        }

        if (!result_entrada.isEmpty()) {
            float valorx = 0;
            for (int i = 0; i < result_entrada.size(); i++) {
                Baixa ba = ((Baixa) sv.pesquisaCodigo((Integer) result_entrada.get(i).get(8), "Baixa"));
                ba.setFechamentoCaixa(fechamento);

                valorx = Moeda.somaValores(valorx, Float.parseFloat(Double.toString((Double) result_entrada.get(i).get(6))));
                if (!sv.alterarObjeto(ba)) {
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a Baixa!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(valorx);
            sv.alterarObjeto(fechamento);
        }

        if (!result_saida.isEmpty()) {
            float valorx = 0;
            for (int i = 0; i < result_saida.size(); i++) {
                Baixa ba = ((Baixa) sv.pesquisaCodigo((Integer) result_saida.get(i).get(8), "Baixa"));
                ba.setFechamentoCaixa(fechamento);

                valorx = Moeda.somaValores(valorx, Float.parseFloat(Double.toString((Double) result_saida.get(i).get(6))));
                if (!sv.alterarObjeto(ba)) {
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a Baixa!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            //fechamento.setValorFechamento(valorx);
            fechamento.setValorFechamento(Moeda.subtracaoValores(fechamento.getValorFechamento(), valorx));
            sv.alterarObjeto(fechamento);
        }

        if (!lEntrada.isEmpty()) {
            float valorx = 0;
            for (int i = 0; i < lEntrada.size(); i++) {
                TransferenciaCaixa tc = ((TransferenciaCaixa) sv.pesquisaCodigo(lEntrada.get(i).getId(), "TransferenciaCaixa"));
                tc.setFechamentoEntrada(fechamento);

                valorx = Moeda.somaValores(valorx, tc.getValor());
                if (!sv.alterarObjeto(tc)) {
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a entrada de Transferência entre Caixas!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(Moeda.somaValores(fechamento.getValorFechamento(), valorx));
            sv.alterarObjeto(fechamento);
        }

        if (!lSaida.isEmpty()) {
            float valorx = 0;//fechamento.getValorFechamento();
            for (int i = 0; i < lSaida.size(); i++) {
                TransferenciaCaixa tc = ((TransferenciaCaixa) sv.pesquisaCodigo(lSaida.get(i).getId(), "TransferenciaCaixa"));
                tc.setFechamentoSaida(fechamento);

                valorx = Moeda.somaValores(valorx, tc.getValor());
                if (!sv.alterarObjeto(tc)) {
                    GenericaMensagem.warn("Erro", "Não foi possivel alterar a saída de Transferência entre Caixas!");
                    sv.desfazerTransacao();
                    fechamento = new FechamentoCaixa();
                    return;
                }
            }
            fechamento.setValorFechamento(Moeda.subtracaoValores(fechamento.getValorFechamento(), valorx));
            sv.alterarObjeto(fechamento);
        }

        fechamento.setValorFechamento(Moeda.somaValores(fechamento.getValorFechamento(), Moeda.converteUS$(saldoAtual)));
        sv.alterarObjeto(fechamento);

        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Fechamento de Caixa concluído!");
        //fechamento = new FechamentoCaixa();
        listaFechamento.clear();
        valor = "0,00";
    }

    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            ControleAcessoBean cab = new ControleAcessoBean();
            boolean permissao = cab.getBotaoFecharCaixaOutroUsuario();
            MacFilial mac = MacFilial.getAcessoFilial();
            
            if (mac.getId() == -1 || mac.getCaixa() == null || mac.getCaixa().getId() == -1){
                listaCaixa.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
                return listaCaixa;
            }
            
            // TRUE é igual NÃO ter permissão
            if (permissao){
                listaCaixa.add(
                    new SelectItem(
                            0,
                            mac.getCaixa().getCaixa() + " - " + mac.getCaixa().getDescricao(),
                            Integer.toString(mac.getCaixa().getId())
                    )
                
                );
            }else{
                List<Caixa> list = (new FinanceiroDBToplink()).listaCaixa();
                if (!list.isEmpty()){

                    // TRUE é igual não ter permissão

                    for (int i = 0; i < list.size(); i++) {
                        
                        listaCaixa.add(
                                new SelectItem(i,
                                list.get(i).getCaixa() + " - " + list.get(i).getDescricao(),
                                Integer.toString(list.get(i).getId())));
                    }
                }else{
                    listaCaixa.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
                }
                    
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getId() == mac.getCaixa().getId()) {
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

//    public String getSaldoAnterior() {
//        if (contaSaldo.getId() == -1 && !listaCaixa.isEmpty()){
//            Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
//            FinanceiroDB db = new FinanceiroDBToplink();
//            contaSaldo = db.pesquisaSaldoInicial(caixa.getId());
//            saldoAnterior = Moeda.converteR$Float(contaSaldo.getSaldo());
//        }else{
//            saldoAnterior = Moeda.converteR$Float(contaSaldo.getSaldo());
//        }
//        return saldoAnterior;
//    }
//
//    public void setSaldoAnterior(String saldoAnterior) {
//        this.saldoAnterior = saldoAnterior;
//    }
    public int getIdCaixaDestino() {
        return idCaixaDestino;
    }

    public void setIdCaixaDestino(int idCaixaDestino) {
        this.idCaixaDestino = idCaixaDestino;
    }

    public List<SelectItem> getListaCaixaDestino() {
        if (listaCaixaDestino.isEmpty() && (!listaCaixa.isEmpty() && Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) != 0)) {
            List<Caixa> list = (new SalvarAcumuladoDBToplink()).listaObjeto("Caixa");
            Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
            
            if (!list.isEmpty()){
                for (int i = 0; i < list.size(); i++) {
                    listaCaixaDestino.add(new SelectItem(i,
                            list.get(i).getCaixa() + " - " + list.get(i).getDescricao(),
                            Integer.toString(list.get(i).getId())));
                    if (list.get(i).getId() == caixa.getId()) {
                        idCaixaDestino = i;
                    }
                }
            }else{
                listaCaixaDestino.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
            }
        }else if (listaCaixaDestino.isEmpty()) {
            listaCaixaDestino.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
        }
        return listaCaixaDestino;
    }

    public void setListaCaixaDestino(List<SelectItem> listaCaixaDestino) {
        this.listaCaixaDestino = listaCaixaDestino;
    }

    public List<DataObject> getListaFechamento() {
        if (listaFechamento.isEmpty() && (!listaCaixa.isEmpty() && Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) != 0)) {
            FinanceiroDB db = new FinanceiroDBToplink();
            Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
            if (caixa == null)
                return listaFechamento;
            
            List<Vector> lista = db.listaFechamentoCaixa(caixa.getId());

            for (int i = 0; i < lista.size(); i++) {
                int status = 0;
                float soma = 0;
                if (Moeda.converteUS$(lista.get(i).get(2).toString()) > Moeda.converteUS$(lista.get(i).get(3).toString())) {
                    status = 1;
                    soma = Moeda.subtracaoValores(Moeda.converteUS$(lista.get(i).get(2).toString()), Moeda.converteUS$(lista.get(i).get(3).toString()));
                } else if (Moeda.converteUS$(lista.get(i).get(2).toString()) < Moeda.converteUS$(lista.get(i).get(3).toString())) {
                    status = 2;
                    soma = Moeda.subtracaoValores(Moeda.converteUS$(lista.get(i).get(3).toString()), Moeda.converteUS$(lista.get(i).get(2).toString()));
                }
                listaFechamento.add(new DataObject(lista.get(i),
                        DataHoje.converteData((Date) lista.get(i).get(4)), // DATA
                        lista.get(i).get(5).toString(), // HORA
                        Moeda.converteR$(lista.get(i).get(2).toString()), // VALOR FECHAMENTO
                        Moeda.converteR$(lista.get(i).get(3).toString()), // VALOR INFORMADO
                        status,// STATUS
                        Moeda.converteR$Float(soma),
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

    public String getSaldoAtual() {
        if (!listaCaixa.isEmpty() && Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) != 0) {
            Caixa caixa = (Caixa) (new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), "Caixa"));
            if (caixa == null)
                return saldoAtual = "0,00";
            
            FinanceiroDB db = new FinanceiroDBToplink();
            List<Vector> lista = db.pesquisaSaldoAtual(caixa.getId());

            if (!lista.isEmpty()) {
                saldoAtual = Moeda.converteR$(lista.get(0).get(1).toString());
                dataSaldo = DataHoje.converteData((Date) lista.get(0).get(2));
            } else {
                saldoAtual = "0,00";
                dataSaldo = "";
            }
        }        
        return Moeda.converteR$(saldoAtual);
    }

    public void setSaldoAtual(String saldoAtual) {
        this.saldoAtual = saldoAtual;
    }

    public String getDataSaldo() {
        return dataSaldo;
    }

    public void setDataSaldo(String dataSaldo) {
        this.dataSaldo = dataSaldo;
    }

    public String getDataResumoFechamento() {
        return dataResumoFechamento;
    }

    public void setDataResumoFechamento(String dataResumoFechamento) {
        this.dataResumoFechamento = dataResumoFechamento;
    }
}
