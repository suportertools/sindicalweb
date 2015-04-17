package br.com.rtools.financeiro.beans;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FechamentoCaixa;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.beans.ImprimirFechamentoCaixa;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CaixaFechadoBean implements Serializable{
    private List<DataObject> listaFechamento = new ArrayList();
    private List<SelectItem> listaCaixa = new ArrayList();
    private int idCaixa = 0;
    private FechamentoCaixa fechamentoCaixa = new FechamentoCaixa();
    private String valorTransferencia = "0,00";

    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();
    
    @PostConstruct
    public void init(){
        cfb.init();
    }
    
    @PreDestroy
    public void destroy(){
        
    }
    
    public void imprimir(DataObject linha){
//        FechamentoCaixa fc = null;
//        fc = (FechamentoCaixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo( (Integer) ((Vector)linha.getArgumento0()).get(1), "FechamentoCaixa"));
//        
//        Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo( ,"Caixa"));
        ImprimirFechamentoCaixa ifc = new ImprimirFechamentoCaixa();

        // id_fechamento, id_caixa
        ifc.imprimir((Integer) ((Vector)linha.getArgumento0()).get(1), Integer.valueOf(listaCaixa.get(idCaixa).getDescription()));
        
        
        
//        FinanceiroDB db = new FinanceiroDBToplink();
//        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "E");
//        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "S");
//        float transferencia_entrada = 0, transferencia_saida = 0, dinheiro_baixa = 0, cheque = 0, cheque_pre = 0, cartao_cre = 0, cartao_deb = 0, saldo_atual = 0;
//        float dinheiro_pagamento = 0;
//        Collection lista = new ArrayList();
//        List<DataObject> lista_cheque = new ArrayList();
//        
//        
////        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fc.getId(), caixa.getId());
////        for (int i = 0; i < lista_tc.size(); i++){
////            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
////        }
//        
//        List<TransferenciaCaixa> lEntrada = db.listaTransferenciaDinheiroEntrada(fc.getId(), caixa.getId());
//        List<TransferenciaCaixa> lSaida = db.listaTransferenciaDinheiroSaida(fc.getId(),caixa.getId());
//        for (int i = 0; i < lEntrada.size(); i++){
//            transferencia_entrada = Moeda.somaValores(transferencia_entrada, lEntrada.get(i).getValor());
//        } 
//        
//        for (int i = 0; i < lSaida.size(); i++){
//            transferencia_saida = Moeda.somaValores(transferencia_saida, lSaida.get(i).getValor());
//        }        
//        
//        for (int i = 0; i < lista_fp_entrada.size(); i++){
//            switch (lista_fp_entrada.get(i).getTipoPagamento().getId()){
//                case 3:
//                    dinheiro_baixa = Moeda.somaValores(dinheiro_baixa, lista_fp_entrada.get(i).getValor());
//                    break;
//                case 4:
//                    cheque = Moeda.somaValores(cheque, lista_fp_entrada.get(i).getValor());
//                    lista_cheque.add(new DataObject(lista_fp_entrada.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_entrada.get(i).getValor())));
//                    break;
//                case 5:
//                    cheque_pre = Moeda.somaValores(cheque_pre, lista_fp_entrada.get(i).getValor());
//                    lista_cheque.add(new DataObject(lista_fp_entrada.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_entrada.get(i).getValor())));
//                    break;
//                case 6:
//                    cartao_cre = Moeda.somaValores(cartao_cre, lista_fp_entrada.get(i).getValor());
//                    break;
//                case 7:
//                    cartao_deb = Moeda.somaValores(cartao_deb, lista_fp_entrada.get(i).getValor());
//                    break;
//            }
//        }
//        
//        for (int i = 0; i < lista_fp_saida.size(); i++) {
//            switch (lista_fp_saida.get(i).getTipoPagamento().getId()) {
//                case 3:
//                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//                    //dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//                    break;
//                case 4:
//                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//                    //cheque = Moeda.somaValores(cheque, lista_fp_saida.get(i).getValor());
//                    //lista_cheque.add(new DataObject(lista_fp_saida.get(i).getChequeRec(), Moeda.converteR$Float(lista_fp_saida.get(i).getValor())));
//                    break;
//                case 5:
//                    dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//                    //cheque_pre = Moeda.somaValores(cheque_pre, lista_fp_saida.get(i).getValor());
//                    break;
//            }
//        }        
//        
//        String status = "VALOR BATIDO";
//        float soma = 0;
//        if (fc.getValorFechamento() > fc.getValorInformado()){
//            soma = Moeda.subtracaoValores(fc.getValorFechamento(), fc.getValorInformado());
//            status = "EM FALTA R$ "+Moeda.converteR$Float(soma);
//        }else if (fc.getValorFechamento() < fc.getValorInformado()){
//            soma = Moeda.subtracaoValores(fc.getValorInformado(), fc.getValorFechamento());
//            status = "EM SOBRA R$ "+Moeda.converteR$Float(soma);
//        }
//        
//        List<Vector> lista_s = db.pesquisaSaldoAtualRelatorio(caixa.getId(), fc.getId());
//        
//        if (!lista_s.isEmpty()){
//            saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista_s.get(0).get(1).toString()));
//        }
//        
//        float total_dinheiro = dinheiro_baixa;
//        float valor_transferido = Moeda.somaValores(Moeda.somaValores(Moeda.somaValores(Moeda.somaValores(dinheiro_baixa, cheque), cheque_pre), cartao_cre), cartao_deb);
//        if (!lista_cheque.isEmpty()){
//            for(int i = 0; i < lista_cheque.size(); i++){
//                ChequeRec cr = (ChequeRec)lista_cheque.get(i).getArgumento0();
//                lista.add(new ParametroFechamentoCaixa(
//                        fc.getData() + " - " + fc.getHora(),
//                        caixa.getFilial().getFilial().getPessoa().getNome(),
//                        Integer.toString(caixa.getCaixa()),
//                        fc.getUsuario().getPessoa().getNome(),
//                        Moeda.converteR$Float(fc.getValorFechamento()),
//                        Moeda.converteR$Float(fc.getValorInformado()),
//                        Moeda.converteR$Float(saldo_atual),
//                        Moeda.converteR$Float(total_dinheiro),
//                        Moeda.converteR$Float(cheque),
//                        Moeda.converteR$Float(cheque_pre),
//                        Moeda.converteR$Float(cartao_cre),
//                        Moeda.converteR$Float(cartao_deb),
//                        Moeda.converteR$Float(transferencia_entrada),
//                        Moeda.converteR$Float(transferencia_saida),
//                        Moeda.converteR$Float(dinheiro_pagamento),
//                        status,
//                        cr.getAgencia() + " - " + cr.getConta() + " " + cr.getBanco(),
//                        cr.getCheque() +" - " + cr.getVencimento() + " | R$ " + lista_cheque.get(i).getArgumento1(),
//                        caixa.getDescricao(),
//                        cfb.getConfiguracaoFinanceiro().isAlterarValorFechamento(),
//                        Moeda.converteR$Float(total_dinheiro),
//                        Moeda.converteR$Float(valor_transferido),
//                        Moeda.converteR$Float( Moeda.subtracaoValores(total_dinheiro, valor_transferido) ),
//                        "",
//                        "",
//                        "",
//                        "",
//                        "",
//                        ""
//                ));
//            }
//        }else{
//            lista.add(new ParametroFechamentoCaixa(
//                    fc.getData() + " - " + fc.getHora(),
//                    caixa.getFilial().getFilial().getPessoa().getNome(),
//                    Integer.toString(caixa.getCaixa()),
//                    fc.getUsuario().getPessoa().getNome(),
//                    Moeda.converteR$Float(fc.getValorFechamento()),
//                    Moeda.converteR$Float(fc.getValorInformado()),
//                    Moeda.converteR$Float(saldo_atual),
//                    Moeda.converteR$Float(total_dinheiro),
//                    Moeda.converteR$Float(cheque),
//                    Moeda.converteR$Float(cheque_pre),
//                    Moeda.converteR$Float(cartao_cre),
//                    Moeda.converteR$Float(cartao_deb),
//                    Moeda.converteR$Float(transferencia_entrada),
//                    Moeda.converteR$Float(transferencia_saida),
//                    Moeda.converteR$Float(dinheiro_pagamento),
//                    status,
//                    null,
//                    null,
//                    caixa.getDescricao(),
//                    cfb.getConfiguracaoFinanceiro().isAlterarValorFechamento(),
//                    Moeda.converteR$Float(total_dinheiro),
//                    Moeda.converteR$Float(valor_transferido),
//                    Moeda.converteR$Float( Moeda.subtracaoValores(total_dinheiro, valor_transferido) ),
//                    "",
//                    "",
//                    "",
//                    "",
//                    "",
//                    ""
//            ));
//        }
//        try{
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
//            
//            
////            JasperViewer jrviewer = new JasperViewer(jasperPrint, false);
////            jrviewer.setTitle("Relatório Fechamento Caixa");
////            jrviewer.setVisible(true);
//        }catch(Exception e){
//            
//        }
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
        
        GenericaMensagem.info("Sucesso", "Reabertura de Caixa concluído!");
        sv.comitarTransacao();
        listaFechamento.clear();
        fechamentoCaixa = new FechamentoCaixa();
    }
    
    public void reabrir(DataObject dob){
        fechamentoCaixa = (FechamentoCaixa)(new SalvarAcumuladoDBToplink()).pesquisaCodigo((Integer) ((Vector)dob.getArgumento0()).get(1), "FechamentoCaixa");
    }
    
    public void transferir(){
        //Caixa caixa = (Caixa)(new SalvarAcumuladoDBToplink().pesquisaCodigo(Integer.valueOf(listaCaixa.get(idCaixa).getDescription()) ,"Caixa"));
        
        transferirCaixaGenerico(fechamentoCaixa.getId(), Integer.valueOf(listaCaixa.get(idCaixa).getDescription()), valorTransferencia);
                
        fechamentoCaixa = new FechamentoCaixa();
        listaFechamento.clear();
    }
    
    public void transferir(DataObject dob){
        fechamentoCaixa = (FechamentoCaixa) new Dao().find(new FechamentoCaixa(), (Integer) ((Vector)dob.getArgumento0()).get(1));
        
        Caixa caixa = (Caixa) new Dao().find(new Caixa(), Integer.valueOf(listaCaixa.get(idCaixa).getDescription()));
        valorTransferencia = somaValorTransferencia(fechamentoCaixa, caixa);
    }
    
    public void transferirCaixaGenerico(Integer id_fechamento, Integer id_caixa, String valort){
        FinanceiroDB db = new FinanceiroDBToplink();
        
        Caixa caixa = (Caixa) new Dao().find(new Caixa(), id_caixa);
        FechamentoCaixa fc = (FechamentoCaixa) new Dao().find(new FechamentoCaixa(), id_fechamento);
        
        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fc.getId(), caixa.getId());
        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "E");
        List<FormaPagamento> lista_fp_saida = db.listaTransferenciaFormaPagamento(fc.getId(), caixa.getId(), "S");
        
        //float dinheiro_transferencia = 0, outros = 0;
        //float dinheiro_pagamento = 0, outros_pagamento = 0;
//        
//        float transferencia_entrada = 0, transferencia_saida = 0, dinheiro_baixa = 0, cheque = 0, cheque_pre = 0, cartao_cre = 0, cartao_deb = 0, saldo_atual = 0;
//        float deposito_bancario = 0, doc_bancario = 0, transferencia_bancaria = 0, ticket = 0, debito = 0, boleto = 0;
        
        float valor_caixa = 0, outros = 0;
        float valor_saida = 0;
        
//        for (int i = 0; i < lista_tc.size(); i++){
//            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
//        }
        
        // TOTAL DINHEIRO
//        for (int i = 0; i < lista_fp_entrada.size(); i++){
//            if (lista_fp_entrada.get(i).getTipoPagamento().getId() == 3){
//                dinheiro_baixa = Moeda.somaValores(dinheiro_baixa, lista_fp_entrada.get(i).getValor());
//            }
//        }
        
        // TOTAL SAIDA
        for (int i = 0; i < lista_fp_saida.size(); i++){
            valor_saida = Moeda.somaValores(valor_saida, lista_fp_saida.get(i).getValor());
//            if (lista_fp_saida.get(i).getTipoPagamento().getId() == 3){
//                dinheiro_pagamento = Moeda.somaValores(dinheiro_pagamento, lista_fp_saida.get(i).getValor());
//            }else{
//                outros_pagamento = Moeda.somaValores(outros_pagamento, lista_fp_saida.get(i).getValor());
//            }
        }

        // VALOR DO CAIXA
        for (int i = 0; i < lista_fp_entrada.size(); i++){
            if (lista_fp_entrada.get(i).getPlano5() != null && lista_fp_entrada.get(i).getPlano5().getId() == 1){
                valor_caixa = Moeda.somaValores(valor_caixa,  lista_fp_entrada.get(i).getValor());
            }else{
                outros = Moeda.somaValores(outros,  lista_fp_entrada.get(i).getValor());
            }
        }
        List<Vector> lista = db.pesquisaSaldoAtualRelatorio(caixa.getId(), fc.getId());
        float valor_saldo_atual = 0;
        if (!lista.isEmpty()){
            valor_saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista.get(0).get(1).toString()));
        }
        valor_caixa = Moeda.somaValores(valor_caixa, valor_saldo_atual);
        valor_caixa = Moeda.subtracaoValores(valor_caixa, valor_saida);
        
        
        //float total_dinheiro = Moeda.somaValores(Moeda.somaValores(dinheiro_transferencia, dinheiro_baixa), valor_saldo_atual);
        
        //float soma = Moeda.somaValores(total_dinheiro, outros);
//        float soma_pagamento = Moeda.somaValores(dinheiro_pagamento, outros_pagamento);
//        float valor_minimo = Moeda.subtracaoValores(outros, soma_pagamento);

        if (valor_caixa != Moeda.converteUS$(valort)){
            if (Moeda.converteUS$(valort) > valor_caixa){
                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÁXIMO R$ " + Moeda.converteR$Float(valor_caixa));
                return;
            }

            if (Moeda.converteUS$(valort) < outros){
                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÍNIMO R$ " + Moeda.converteR$Float(outros));
                return;
            }
            
            float saldo_atual = 0;
            if (Moeda.converteUS$(valort) >= outros){
                saldo_atual = Moeda.subtracaoValores(valor_caixa, Moeda.converteUS$(valort));
            }
            fc.setSaldoAtual(saldo_atual);
        }

//        if (Moeda.converteUS$(valort) != soma){
//            if (Moeda.converteUS$(valort) > fc.getValorFechamento()){
//                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÁXIMO R$ " + Moeda.converteR$Float(fc.getValorFechamento()));
//                return;
//            }
//
//            if (Moeda.converteUS$(valort) < outros){
//                GenericaMensagem.warn("Erro", "Valor da Transferência deve ser no MÍNIMO R$ " + Moeda.converteR$Float(outros));
//                return;
//            }else if (Moeda.converteUS$(valort) >= outros){
//                saldo_atual = Moeda.subtracaoValores(fc.getValorFechamento(),Moeda.converteUS$(valort));
//            }
//            fc.setSaldoAtual(saldo_atual);
//        
//        }
        
        Dao dao = new Dao();
        dao.openTransaction();
        
        if (!dao.update(fc)){
            GenericaMensagem.warn("Erro", "Não foi possivel alterar Fechamento Caixa!");
            //sv.desfazerTransacao();
            dao.rollback();
            return;
        }
        
        // AQUI pesquisaCaixaUm COLOCAR id_filial
        TransferenciaCaixa tc = new TransferenciaCaixa(
                -1,
                caixa,
                Moeda.converteUS$(valort),
                //Moeda.subtracaoValores(total_transferencia, caixa.getFundoFixo()),
                (new FinanceiroDBToplink()).pesquisaCaixaUm(),
                DataHoje.dataHoje(),
                (FStatus) new Dao().find(new FStatus(), 12),
                null,
                fc,
                (Usuario) GenericaSessao.getObject("sessaoUsuario")
        );
        
        if (!dao.save(tc)){
            GenericaMensagem.warn("Erro", "Não foi possivel salvar esta Transferência, verifique se existe CAIXA 01 cadastrado!");
            //sv.desfazerTransacao();
            dao.rollback();
            return;
        }
        
        //sv.comitarTransacao();
        dao.commit();
        GenericaMensagem.info("Sucesso", "Transferência entre Caixas concluído!");
    }
    
    public String somaValorTransferencia(FechamentoCaixa fc, Caixa c){
        FinanceiroDB db = new FinanceiroDBToplink();
        List<FormaPagamento> lista_fp_entrada = db.listaTransferenciaFormaPagamento(fc.getId(), c.getId(), "E");
        List<TransferenciaCaixa> lista_tc = db.listaTransferenciaDinheiro(fc.getId(), c.getId());
        
        
        float dinheiro_transferencia = 0;
        float total_transferencia = 0;
        
        for (int i = 0; i < lista_tc.size(); i++){
            dinheiro_transferencia = Moeda.somaValores(dinheiro_transferencia, lista_tc.get(i).getValor());
        }
        
        for (int i = 0; i < lista_fp_entrada.size(); i++){
            if (lista_fp_entrada.get(i).getTipoPagamento().getId() == 3 || 
                lista_fp_entrada.get(i).getTipoPagamento().getId() == 4 || 
                lista_fp_entrada.get(i).getTipoPagamento().getId() == 5 ||
                lista_fp_entrada.get(i).getTipoPagamento().getId() == 11 ){
                total_transferencia = Moeda.somaValores(total_transferencia, lista_fp_entrada.get(i).getValor());
            }
        }
        
        total_transferencia = Moeda.somaValores(total_transferencia, dinheiro_transferencia);
        
        List<Vector> lista = db.pesquisaSaldoAtualRelatorio(c.getId(), fc.getId());
        float valor_saldo_atual;
        
        if (!lista.isEmpty()){
            valor_saldo_atual = Moeda.converteUS$(Moeda.converteR$(lista.get(0).get(1).toString()));
            total_transferencia = Moeda.somaValores(valor_saldo_atual, total_transferencia);
        }
        
        if (total_transferencia <= c.getFundoFixo())
            total_transferencia = 0;
        else
            total_transferencia = Moeda.subtracaoValores(total_transferencia, c.getFundoFixo());
        
        return Moeda.converteR$Float(total_transferencia);
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
