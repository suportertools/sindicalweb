package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.impressao.ParametroRecibo;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
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

public class MovimentosReceberSocialJSFBean {

    private String porPesquisa = "abertos";
    private List<DataObject> listaMovimento = new ArrayList();
    private String titular = "";
    private String beneficiario = "";
    private String data = "";
    private String boleto = "";
    private String diasAtraso = "";
    private String multa = "", juros = "", correcao = "";
    private String caixa = "";
    private String documento = "";
    private String referencia = "";
    private String tipo = "";
    private String id_baixa = "";
    private String msgConfirma = "";
    private String desconto = "0";
    private boolean chkSeleciona = false;
    private boolean addMais = false;
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();
    
    private String matricula = "";
    private String categoria = "";
    private String grupo = "";
    private String status = "";
    
    
    public String recibo(int id_movimento){
        
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
       
        movimento = db.pesquisaCodigo(id_movimento);
            try {
                Collection vetor = new ArrayList();
                Juridica sindicato = (Juridica) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Juridica");
                PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
                //MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();
                
                PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
                String formas[] = new String[10];
                
                
                // PESQUISA FORMA DE PAGAMENTO
                List<FormaPagamento> fp = db.pesquisaFormaPagamento(movimento.getBaixa().getId());
                
                for (int i = 0; i < fp.size(); i++){
                    // 4 - CHEQUE    
                    if (fp.get(i).getTipoPagamento().getId() == 4){
                        formas[i] = fp.get(i).getTipoPagamento().getDescricao()+": R$ "+Moeda.converteR$Float(fp.get(i).getValor()) + " (B: "+fp.get(i).getChequeRec().getBanco()+" Ag: "+fp.get(i).getChequeRec().getAgencia()+ " C: "+ fp.get(i).getChequeRec().getConta()+" CH: "+fp.get(i).getChequeRec().getCheque();
                    // 5 - CHEQUE PRÉ
                    }else if (fp.get(i).getTipoPagamento().getId() == 5){
                        formas[i] = fp.get(i).getTipoPagamento().getDescricao()+": R$ "+Moeda.converteR$Float(fp.get(i).getValor()) + " (B: "+fp.get(i).getChequeRec().getBanco()+" Ag: "+fp.get(i).getChequeRec().getAgencia()+ " C: "+ fp.get(i).getChequeRec().getConta()+" CH: "+fp.get(i).getChequeRec().getCheque()+" P: "+fp.get(i).getChequeRec().getVencimento()+")";
                    // QUALQUER OUTRO    
                    }else{
                        formas[i] = fp.get(i).getTipoPagamento().getDescricao()+": R$ "+Moeda.converteR$Float(fp.get(i).getValor());
                    }
                }
                
                
                List<Movimento> lista = db.listaMovimentoBaixaOrder(movimento.getBaixa().getId());
                for (int i = 0; i < lista.size(); i++){
                        vetor.add(new ParametroRecibo(
                                ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                                sindicato.getPessoa().getNome(), 
                                pe.getEndereco().getDescricaoEndereco().getDescricao(), 
                                pe.getEndereco().getLogradouro().getDescricao(), 
                                pe.getNumero(), 
                                pe.getComplemento(), 
                                pe.getEndereco().getBairro().getDescricao(), 
                                pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5), 
                                pe.getEndereco().getCidade().getCidade(), 
                                pe.getEndereco().getCidade().getUf(), 
                                sindicato.getPessoa().getTelefone1(), 
                                sindicato.getPessoa().getEmail1(), 
                                sindicato.getPessoa().getSite(), 
                                sindicato.getPessoa().getDocumento(), 
                                lista.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
                                String.valueOf(lista.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
                                String.valueOf(lista.get(i).getBaixa().getId()), // ID_BAIXA
                                lista.get(i).getBeneficiario().getNome(), // BENEFICIÁRIO
                                lista.get(i).getServicos().getDescricao(), // SERVICO
                                lista.get(i).getVencimento(), // VENCIMENTO
                                new BigDecimal(lista.get(i).getValorBaixa()), // VALOR BAIXA
                                lista.get(i).getBaixa().getUsuario().getLogin(), 
                                lista.get(i).getBaixa().getBaixa(), 
                                DataHoje.horaMinuto(),
                                formas[0], 
                                formas[1], 
                                formas[2],
                                formas[3],
                                formas[4],
                                formas[5], 
                                formas[6], 
                                formas[7], 
                                formas[8], 
                                formas[9])
                        );
                    
                }

                JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                
                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
                
                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
                res.setContentType("application/pdf");
                res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
                res.getOutputStream().write(arquivo);
                res.getCharacterEncoding();
                
                FacesContext.getCurrentInstance().responseComplete();
            } catch (JRException ex) {
                Logger.getLogger(MovimentosReceberSocialJSFBean.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(MovimentosReceberSocialJSFBean.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        return null;
    }
    
    public String removerPesquisa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        pessoa = new Pessoa();
        return "movimentosReceberSocial";
    }

    public String removerPessoaLista(int index) {
        listaPessoa.remove(index);
        listaMovimento.clear();
        return "movimentosReceberSocial";
    }


    public boolean baixado() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) > 0.0) {
                return true;
            }
        }
        return false;
    }

    public boolean semValor() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()) <= 0.0) {
                return true;
            }
        }
        return false;
    }
    
    public boolean acordados() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && String.valueOf(listaMovimento.get(i).getArgumento3()).equals("Acordo")) {
                return true;
            }
        }
        return false;
    }

    public String estornarBaixa() { 
        if (listaMovimento.isEmpty()) {
            msgConfirma = "Não existem boletos para serem estornados!";
            return null;
        }

        if (!baixado()) {
            msgConfirma = "Existem boletos que não foram pagos para estornar!";
            return null;
        }
        
        MovimentoDB db = new MovimentoDBToplink();
        int qnt = 0;
        Movimento mov = null;
        
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                qnt++;
                mov = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
            }
        }
        
        if (qnt > 1){
            msgConfirma = "Mais de um movimento foi selecionado!";
            return null;
        }

        boolean est = true;
        
        if (!mov.isAtivo()) {
            msgConfirma = "Boleto ID: " + mov.getId() + " esta inativo, não é possivel concluir estorno!";
            return null;
        }
        
        if (!GerarMovimento.estornarMovimento(mov)) {
            est = false;
        }
        
        if (!est) {
            msgConfirma = "Ocorreu erros ao estornar boletos, verifique o log!";
        } else {
            msgConfirma = "Boletos estornados com sucesso!";
        }
        listaMovimento.clear();
        chkSeleciona = true;
        return null;
    }

    public String telaBaixa() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
            MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");

        if (macFilial == null) {
            msgConfirma = "Não existe filial na sessão!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        
        if (macFilial.getCaixa() == null) {
            msgConfirma = "Configurar Caixa nesta estação de trabalho!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        
        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Baixados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString() ));
                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    //movimento.setValor( Float.valueOf( listaMovimento.get(i).getArgumento9().toString() ) );
                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));

                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            msgConfirma = "Lista vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        return null;
    }

    public String telaMovimento(int id_movimento) {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
       
        
//                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
        movimento = db.pesquisaCodigo(id_movimento);

//                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
//                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
//                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
//
//                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));
//
//                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
        lista.add(movimento);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
        return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).alterarMovimento();
    }
    
    public String telaAcordo() {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (semValor()) {
            msgConfirma = "Boletos sem valor não podem ser Acordados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        
        if (acordados()) {
            msgConfirma = "Boletos do tipo Acordo não podem ser Reacordados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));

                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).acordo();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
                GenericaMensagem.warn("Erro", msgConfirma);
            }
        } else {
            msgConfirma = "Lista vazia!";
            GenericaMensagem.warn("Erro", msgConfirma);
        }
        return null;
    }

    public void calculoDesconto() {
        float descPorcento = 0;
        float desc = 0;
        float acre = 0;
        float calc = Moeda.substituiVirgulaFloat(getValorPraDesconto());
        if (Float.valueOf(desconto) > calc) {
            desconto = String.valueOf(calc);
        }

        descPorcento = Moeda.multiplicarValores(Moeda.divisaoValores(Float.valueOf(desconto), calc), 100);
        
        
        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                acre = Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString());
                
                float valor_calc = Moeda.somaValores(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()), acre);
                desc = Moeda.divisaoValores(Moeda.multiplicarValores(valor_calc, descPorcento), 100);
                
                listaMovimento.get(i).setArgumento8( Moeda.converteR$(String.valueOf(desc)) );
                listaMovimento.get(i).setArgumento9( Moeda.converteR$(String.valueOf(Moeda.subtracaoValores(valor_calc, desc) )));
            }else{
                listaMovimento.get(i).setArgumento8( "0,00" );
                listaMovimento.get(i).setArgumento9( Moeda.converteR$(listaMovimento.get(i).getArgumento6().toString()) );
            }
        }
    }

    public void atualizarStatus() {
        listaMovimento.clear();
    }

    public String getTotal() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getAcrescimo() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento7().toString()));
                }
            }

            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public String getValorPraDesconto(){
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento24().toString()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }
    
    public String getTotalCalculado() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    soma = Moeda.somaValores(soma, Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0";
        }
    }

    public void complementoPessoa(DataObject linha) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        //titular = (String) linha.getArgumento15(); // 13 - TITULAR
        tipo = (String) linha.getArgumento3(); // 1 - TIPO SERVIÇO
        referencia = (String) linha.getArgumento4(); // 2 - REFERENCIA
        id_baixa = linha.getArgumento26().toString(); // 23 - ID_BAIXA
        
        beneficiario = (String) linha.getArgumento14(); // 12 - BENEFICIARIO
        data = linha.getArgumento16().toString(); // 16 - CRIACAO
        boleto = (String) linha.getArgumento17(); // 17 - BOLETO
        diasAtraso = linha.getArgumento18().toString(); // 18 - DIAS EM ATRASO
        multa = "R$ " + Moeda.converteR$(linha.getArgumento19().toString()); // 19 - MULTA
        juros = "R$ " + Moeda.converteR$(linha.getArgumento20().toString()); // 20 - JUROS
        correcao = "R$ " + Moeda.converteR$(linha.getArgumento21().toString()); // 21 - CORRECAO
        caixa = (linha.getArgumento22() == null) ? "Nenhum" : linha.getArgumento22().toString(); // 22 - CAIXA
        documento = (linha.getArgumento23() == null) ? "Sem Documento" : linha.getArgumento23().toString(); // 24 - DOCUMENTO

        int id_lote = Integer.valueOf(linha.getArgumento27().toString());
        
        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        List<Vector> lista = db.dadosSocio(id_lote);
        
        if (!lista.isEmpty()){
            titular = lista.get(0).get(0).toString(); // TITULAR
            matricula = lista.get(0).get(1).toString(); // MATRICULA
            categoria = lista.get(0).get(2).toString(); // CATEGORIA
            grupo = lista.get(0).get(3).toString(); // GRUPO
            status = lista.get(0).get(4).toString(); // CASE
        }else{
            titular = "";
            matricula = "";
            categoria = "";
            grupo = "";
            status = "";
        }
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public void marcarTodos() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            listaMovimento.get(i).setArgumento0(chkSeleciona);
        }
    }
   
    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty() && !listaPessoa.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            String ids = "";
            for (int i = 0; i < listaPessoa.size(); i++) {
                if (ids.length() > 0 && i != listaPessoa.size()) {
                    ids = ids + ",";
                }
                ids = ids + String.valueOf(listaPessoa.get(i).getId());
            }
            List<Vector> lista = db.pesquisaListaMovimentos(ids, porPesquisa);
            //float soma = 0;
            boolean chk = false, disabled = false;
            String dataBaixa = "";
            
            
            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get(8) != null){
                    dataBaixa = DataHoje.converteData( (Date)lista.get(i).get(8) );
                }else{
                    dataBaixa = "";
                }
                //soma = Moeda.somaValores(Moeda.converteR$(lista.get(i).get(5).toString()), Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                // DATA DE HOJE MENOR OU IGUAL A DATA DE VENCIMENTO
                if ( DataHoje.converteDataParaInteger(DataHoje.converteData((Date)lista.get(i).get(3))) <= 
                     DataHoje.converteDataParaInteger(DataHoje.data()) &&
                     dataBaixa.isEmpty()){
                    chk = true;
                }else{
                    chk = false;
                }
                
                // DATA DE HOJE MENOR QUE DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date)lista.get(i).get(3))) < 
                    DataHoje.converteDataParaInteger(DataHoje.data()) && dataBaixa.isEmpty()){
                   disabled = true; 
                }else{
                   disabled = false;
                }
                
                listaMovimento.add(new DataObject(
                            chk, // ARG 0
                            lista.get(i).get(14), // ARG 1 ID_MOVIMENTO
                            lista.get(i).get(0), // ARG 2 SERVICO
                            lista.get(i).get(1), // ARG 3 TIPO_SERVICO
                            lista.get(i).get(2), // ARG 4 REFERENCIA
                            DataHoje.converteData((Date)lista.get(i).get(3)), // ARG 5 VENCIMENTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(4))), // ARG 6 VALOR
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(5))), // ARG 7 ACRESCIMO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(6))), // ARG 8 DESCONTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(7))), // ARG 9 VALOR CALCULADO
                            dataBaixa, // ARG 10 DATA BAIXA
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(9))), // ARG 11 VALOR_BAIXA
                            lista.get(i).get(10), // ARG 12 ES
                            lista.get(i).get(11), // ARG 13 RESPONSAVEL
                            lista.get(i).get(12), // ARG 14 BENEFICIARIO
                            lista.get(i).get(13), // ARG 15 TITULAR
                            DataHoje.converteData((Date)lista.get(i).get(16)), // ARG 16 CRIACAO
                            lista.get(i).get(17), // ARG 17 BOLETO
                            lista.get(i).get(18), // ARG 18 DIAS DE ATRASO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(19))), // ARG 29 MULTA
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(20))), // ARG 20 JUROS
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(21))), // ARG 21 CORRECAO
                            getConverteNullString(lista.get(i).get(22)), // ARG 22 CAIXA
                            lista.get(i).get(24), // ARG 23 DOCUMENTO
                            Moeda.converteR$(getConverteNullString(lista.get(i).get(7))),  // ARG 24 VALOR CALCULADO ORIGINAL
                            disabled, 
                            lista.get(i).get(18), // ARG 26 ID_BAIXA
                            lista.get(i).get(15) // ARG 27 ID_LOTE
                            )
                        );
            }
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<DataObject> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getBoleto() {
        return boleto;
    }

    public void setBoleto(String boleto) {
        this.boleto = boleto;
    }

    public String getDiasAtraso() {
        return diasAtraso;
    }

    public void setDiasAtraso(String diasAtraso) {
        this.diasAtraso = diasAtraso;
    }

    public String getMulta() {
        return multa;
    }

    public void setMulta(String multa) {
        this.multa = multa;
    }

    public String getJuros() {
        return juros;
    }

    public void setJuros(String juros) {
        this.juros = juros;
    }

    public String getCorrecao() {
        return correcao;
    }

    public void setCorrecao(String correcao) {
        this.correcao = correcao;
    }

    public String getCaixa() {
        return caixa;
    }

    public void setCaixa(String caixa) {
        this.caixa = caixa;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        this.desconto = Moeda.substituiVirgula(desconto);
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public boolean isChkSeleciona() {
        return chkSeleciona;
    }

    public void setChkSeleciona(boolean chkSeleciona) {
        this.chkSeleciona = chkSeleciona;
    }

    public void adicionarPesquisa() {
        addMais = true;
        //return "movimentosReceberSocial";
    }
    
    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            if (!addMais){
                pessoa = new Pessoa();
                pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
                
                listaPessoa.clear();
                
                listaPessoa.add(pessoa);
                listaMovimento.clear();
            }else{
                listaPessoa.add((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
                listaMovimento.clear();
                addMais = false;
            }
            calculoDesconto();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<Pessoa> getListaPessoa() {
        return listaPessoa;
    }

    public void setListaPessoa(List<Pessoa> listaPessoa) {
        this.listaPessoa = listaPessoa;
    }
    
    public String getConverteNullString(Object object) {
        if (object == null) {
            return "";
        } else {
            return String.valueOf(object);
        }
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getId_baixa() {
        return id_baixa;
    }

    public void setId_baixa(String id_baixa) {
        this.id_baixa = id_baixa;
    }
}
