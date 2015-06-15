package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ContaSaldo;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroCaixaAnalitico;
import br.com.rtools.impressao.ResumoFechamentoCaixa;
import br.com.rtools.impressao.beans.ImprimirFechamentoCaixa;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

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

    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();
    
    public FechamentoCaixaBean() {
        cfb.init();
        getListaCaixa();
        
    }
    
    public void transferirParaCentral(){
        if (fechamento.getId() == -1){
            return;
        }
        
        CaixaFechadoBean cf = new CaixaFechadoBean();
        
        cf.transferirCaixaGenerico(fechamento.getId(), Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), valorTransferencia);
//        
//        FinanceiroDB db = new FinanceiroDBToplink();
//        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
//        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fechamento.getId(), caixa.getId());
//        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fechamento.getId(), caixa.getId(), "E");
//        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fechamento.getId(), caixa.getId(), "S");
//        
//        float dinheiro_transferencia = 0, dinheiro_baixa = 0, outros = 0, saldo_atual = 0;
//        float dinheiro_pagamento = 0, outros_pagamento = 0;
//        
//        for (int i = 0; i < lista_tc.size(); i++){
//            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
//        }
//        
//        for (int i = 0; i < lista_fp_entrada.size(); i++){
//            if (lista_fp_entrada.get(i).getTipoPagamento().getId() == 3){
//                dinheiro_baixa = Moeda.somaValores(dinheiro_baixa, lista_fp_entrada.get(i).getValor());
//            }else{
//                outros = Moeda.somaValores(outros, lista_fp_entrada.get(i).getValor());
//            }
//        }
//        
//        for (int i = 0; i < lista_fp_saida.size(); i++){
//            if (lista_fp_saida.get(i).getTipoPagamento().getId() == 3){
//                dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//            }else{
//                outros_pagamento = Moeda.somaValores(outros_pagamento, lista_fp_saida.get(i).getValor());
//            }
//        }
//
//        List<Vector> lista = db.pesquisaSaldoAtual(caixa.getId());
//        float valor_saldo_atual = 0;
//        
//        if (!lista.isEmpty()){
//            valor_saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista.get(0).get(1).toString()));
//        }
//        
//        float total_dinheiro = Moeda.somaValores(Moeda.somaValores(dinheiro_transferencia, dinheiro_baixa), valor_saldo_atual);
//        
//        float soma = Moeda.somaValores(total_dinheiro, outros);
//        float soma_pagamento = Moeda.somaValores(dinheiro_pagamento, outros_pagamento);
//        float valor_minimo = Moeda.subtracaoValores(outros, soma_pagamento);
//        
//        if (Moeda.converteUS$(valorTransferencia) != soma){
//            //if (Moeda.converteUS$(valorTransferencia) > soma){
//            if (Moeda.converteUS$(valorTransferencia) > fechamento.getValorFechamento()){
//                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÁXIMO R$ " + Moeda.converteR$Float(fechamento.getValorFechamento()));
//                return;
//            }
//
//            if (Moeda.converteUS$(valorTransferencia) < outros){
//                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÍNIMO R$ " + Moeda.converteR$Float(outros));
//                return;
//            }else if (Moeda.converteUS$(valorTransferencia) >= outros){
//                //saldo_atual = Moeda.subtracaoValores(total_dinheiro, Moeda.subtracaoValores(Moeda.converteUS$(valorTransferencia), outros));
//                saldo_atual = Moeda.subtracaoValores(fechamento.getValorFechamento(),Moeda.converteUS$(valorTransferencia));
//            }
//            fechamento.setSaldoAtual(saldo_atual);
//        }
//        
//        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
//        
//        sv.abrirTransacao();
//        
//        if (!sv.alterarObjeto(fechamento)){
//            GenericaMensagem.warn("Erro", "Não foi possivel alterar Fechamento Caixa!");
//            sv.desfazerTransacao();
//            return;
//        }
//        
//        // AQUI pesquisaCaixaUm COLOCAR id_filial
//        TransferenciaCaixa tc = new TransferenciaCaixa(
//                -1,
//                caixa,
//                Moeda.converteUS$(valorTransferencia),
//                (new FinanceiroDBToplink()).pesquisaCaixaUm(),
//                DataHoje.dataHoje(),
//                (FStatus) new SalvarAcumuladoDBToplink().pesquisaCodigo(12, "FStatus"),
//                null,
//                fechamento,
//                (Usuario) GenericaSessao.getObject("sessaoUsuario")
//        );
//        
//        if (!sv.inserirObjeto(tc)){
//            GenericaMensagem.warn("Erro", "Não foi possivel salvar esta Transferência, verifique se existe CAIXA 01 cadastrado!");
//            sv.desfazerTransacao();
//            return;
//        }
//        
//        sv.comitarTransacao();
//        GenericaMensagem.info("Sucesso", "Transferência entre Caixas concluído!");
        fechamento = new FechamentoCaixa();
        listaFechamento.clear();
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
            Jasper.PATH = "downloads";
            Jasper.PART_NAME = "";
            Jasper.printReports("/Relatorios/CAIXA_ANALITICO.jasper", "analitico", lista);
//            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/CAIXA_ANALITICO.jasper"));
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
//
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
//            
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Relatório Caixa Analítico.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
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
            Jasper.PATH = "downloads";
            Jasper.PART_NAME = "";
            Jasper.printReports("/Relatorios/RESUMO_FECHAMENTO_CAIXA.jasper", "fechamento_caixa", lista);
            
//            File file_jasper = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RESUMO_FECHAMENTO_CAIXA.jasper"));
//            JasperReport jasperReport = (JasperReport) JRLoader.loadObject(file_jasper);
//
//            JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(lista);
//            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, dtSource);
//            byte[] arquivo = JasperExportManager.exportReportToPdf(jasperPrint);
//            
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"Resumo Fechamento Caixa.pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//            FacesContext.getCurrentInstance().responseComplete();

        } catch (Exception e) {
            e.getMessage();
        }        
    }

    public void imprimir(DataObject linha) {
        ImprimirFechamentoCaixa ifc = new ImprimirFechamentoCaixa();
        Integer id_fechamento;
        
        if (linha != null) {
            id_fechamento = (Integer) ((Vector) linha.getArgumento0()).get(1);
        } else {
            id_fechamento = fechamento.getId();
        }
        
        
        ifc.imprimir(id_fechamento, Integer.valueOf(listaCaixa.get(idCaixa).getDescription()));
        
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
        
        
        result_entrada = db.listaMovimentoCaixa(caixa.getId(), "E", null);
        result_saida = db.listaMovimentoCaixa(caixa.getId(), "S", null);
        lEntrada = db.listaTransferenciaEntrada(caixa.getId(), null);
        lSaida = db.listaTransferenciaSaida(caixa.getId(), null);
        
        if (result_entrada.isEmpty() && result_saida.isEmpty() && lEntrada.isEmpty() && lSaida.isEmpty()){
            GenericaMensagem.warn("Erro", "Não existe movimentos para este Caixa!");
            return;
        }
        
        // true NÃO TEM PERMISSÃO
        if (permissao){
            List<Vector> result_entrada_user = db.listaMovimentoCaixa(caixa.getId(), "E", usuario.getId());
            List<Vector> result_saida_user = db.listaMovimentoCaixa(caixa.getId(), "S", usuario.getId());
            List<TransferenciaCaixa> lEntrada_user = db.listaTransferenciaEntrada(caixa.getId(), usuario.getId());
            List<TransferenciaCaixa> lSaida_user = db.listaTransferenciaSaida(caixa.getId(), usuario.getId());
            
            if (result_entrada_user.isEmpty() && result_saida_user.isEmpty() && lEntrada_user.isEmpty() && lSaida_user.isEmpty()) {
                GenericaMensagem.warn("Erro", "Usuário não efetuou recebimento neste Caixa!");
                return;
            }
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

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
        
        // CALCULO PARA SOMAR OS VALORES DA QUERY
        if (cfb.getConfiguracaoFinanceiro().isAlterarValorFechamento())
            fechamento.setValorInformado(Moeda.converteUS$(valor));
        else
            fechamento.setValorInformado(fechamento.getValorFechamento());
        
        sv.alterarObjeto(fechamento);

        sv.comitarTransacao();
        GenericaMensagem.info("Sucesso", "Fechamento de Caixa concluído!");
        
        // TRANSFERE CAIXA AUTOMATICO
        CaixaFechadoBean cf = new CaixaFechadoBean();
        if (cfb.getConfiguracaoFinanceiro().isTransferenciaAutomaticaCaixa()){
            if (cfb.getConfiguracaoFinanceiro().isModalTransferencia()){
                
                //valorTransferencia = Moeda.converteR$Float(Moeda.subtracaoValores(fechamento.getValorFechamento(), caixa.getFundoFixo()));
                valorTransferencia = cf.somaValorTransferencia(fechamento, caixa);
                PF.openDialog("i_dlg_transferir");
                PF.update(":i_panel_transferencia");
            }else{
                //valorTransferencia = Moeda.converteR$Float(Moeda.subtracaoValores(fechamento.getValorFechamento(), caixa.getFundoFixo()));
                valorTransferencia = cf.somaValorTransferencia(fechamento, caixa);
                transferirParaCentral();
            }
        }
        
        //fechamento = new FechamentoCaixa();
        listaFechamento.clear();
        valor = "0,00";
    }

    public List<SelectItem> getListaCaixa() {
        if (listaCaixa.isEmpty()) {
            ControleAcessoBean cab = new ControleAcessoBean();
            boolean permissao = cab.getBotaoFecharCaixaOutroUsuario();
            Caixa cx = null;
            Usuario usuario = ((Usuario) GenericaSessao.getObject("sessaoUsuario"));
            
            // TRUE é igual NÃO ter permissão
            if (usuario.getId() != 1 && permissao){
                if (!cfb.getConfiguracaoFinanceiro().isCaixaOperador()){
                    MacFilial mac = MacFilial.getAcessoFilial();
                    if (mac.getId() == -1 || mac.getCaixa() == null || mac.getCaixa().getId() == -1){
                        listaCaixa.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
                        return listaCaixa;
                    }

                    cx = mac.getCaixa();
                }else{
                    FinanceiroDB dbf = new FinanceiroDBToplink();
                    cx = dbf.pesquisaCaixaUsuario( ((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId() );    

                    if (cx == null){
                        listaCaixa.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
                        return listaCaixa;
                    }
                }
            
                // TRUE é igual NÃO ter permissão
                if (permissao){
                    listaCaixa.add(
                        new SelectItem(
                                0,
                                cx.getCaixa() + " - " + cx.getDescricao(),
                                Integer.toString(cx.getId())
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
                        if (list.get(i).getId() == cx.getId()) {
                            idCaixa = i;
                        }
                    }
                }
            }else{
                List<Caixa> list = (new FinanceiroDBToplink()).listaCaixa();
                if (!list.isEmpty()){
                    for (int i = 0; i < list.size(); i++) {
                        listaCaixa.add(
                                new SelectItem(i,
                                list.get(i).getCaixa() + " - " + list.get(i).getDescricao(),
                                Integer.toString(list.get(i).getId())));
                    }
                }else{
                    listaCaixa.add(new SelectItem(0, "Nenhum Caixa Encontrado", "0"));
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

    public ConfiguracaoFinanceiroBean getCfb() {
        return cfb;
    }
}
