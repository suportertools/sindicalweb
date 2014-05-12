package br.com.rtools.financeiro.beans;

import br.com.rtools.arrecadacao.beans.BaixaBoletoJSFBean;
import br.com.rtools.associativo.beans.EmissaoGuiasBean;
import br.com.rtools.associativo.beans.MovimentosReceberSocialJSFBean;
import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Cartao;
import br.com.rtools.financeiro.ChequePag;
import br.com.rtools.financeiro.ChequeRec;
import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.financeiro.db.ContaBancoDBToplink;
import br.com.rtools.financeiro.db.ContaRotinaDB;
import br.com.rtools.financeiro.db.ContaRotinaDBToplink;
import br.com.rtools.financeiro.db.FTipoDocumentoDB;
import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDB;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.impressao.ParametroRecibo;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.db.PermissaoUsuarioDB;
import br.com.rtools.seguranca.db.PermissaoUsuarioDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
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
public class BaixaGeralBean {

    private String quitacao = DataHoje.data();
    private String vencimento = DataHoje.data();
    private String valor = "0";
    private String numero = "";
    private String numeroChequePag = "";
    private String total = "0.00";
    private List<DataObject> lista = new ArrayList();
    private List<Movimento> listaMovimentos = new ArrayList();
    private List<SelectItem> listaCartao = new ArrayList();
    private List<SelectItem> listaBanco = new ArrayList();
    private List<SelectItem> listaBancoSaida = new ArrayList();
    //private List<SelectItem> listaContaPagamento = new ArrayList();
    private int idConta = 0;
    private int idTipoDoc = 0;
    private int idCartao = 0;
    private int idBanco = 0;
    private int idBancoSaida = 0;
    //private int idContaPagamento = 0;
    private Rotina rotina = null;
    private Modulo modulo = new Modulo();
    private boolean desHabilitaConta = false;
    private boolean desHabilitaQuitacao = false;
    private boolean desHabilitaNumero = false;
    private boolean desHabilitadoVencimento = false;
    private boolean retorna = false;
    private String mensagem = "";
    private Plano5 plano5 = new Plano5();
    private int index = 0;
    private String tipo = "";
    private String banco = "";
    private String taxa = "0";
    private String es = "";
    private ChequeRec chequeRec = new ChequeRec();
    private List<FormaPagamento> lfp = new ArrayList();

    public void atualizaTipo() {
        TipoPagamento tipoPagamento = (TipoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoDoc().get(idTipoDoc)).getDescription()), "TipoPagamento");
        if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
            listaCartao.clear();
            idCartao = 0;
        }
    }

    public void alteraNumeroChequeConta() {
        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        Plano5DB dbx = new Plano5DBToplink();
        Plano5 pl = dbx.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()));

        ChequePag ch = db.pesquisaChequeConta(numeroChequePag, pl.getId());
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        ContaBanco cb = (ContaBanco) sv.pesquisaCodigo(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()), "ContaBanco");
        if (ch != null) {
            GenericaMensagem.warn("Erro", "O cheque " + numeroChequePag + " já existe");
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
        }

        if (Integer.valueOf(numeroChequePag) == cb.getUCheque()) {
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
            return;
        }

        if (Integer.valueOf(numeroChequePag) == cb.getUCheque() + 1) {
            return;
        }

        if ((Integer.valueOf(numeroChequePag) + 1) > (cb.getUCheque() + 1)) {
            GenericaMensagem.warn("Erro", "Número " + (Integer.valueOf(numeroChequePag)) + " maior que permitido!");
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
        }
    }

    public void refreshForm() {
    }

    public String imprimirRecibo() {

        if (!listaMovimentos.isEmpty()) {
            try {
                Collection vetor = new ArrayList();
                Juridica sindicato = (Juridica) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Juridica");
                PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
                MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();

                PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
                String formas[] = new String[10];

                for (int i = 0; i < lfp.size(); i++) {
                    // 4 - CHEQUE    
                    if (lfp.get(i).getTipoPagamento().getId() == 4) {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque();
                        // 5 - CHEQUE PRÉ
                    } else if (lfp.get(i).getTipoPagamento().getId() == 5) {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque() + " P: " + lfp.get(i).getChequeRec().getVencimento() + ")";
                        // QUALQUER OUTRO    
                    } else {
                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor());
                    }
                }

                for (int i = 0; i < listaMovimentos.size(); i++) {
                    //String valor = (getConverteNullString(listaMovimento.get(i).getArgumento11().toString()) == "") ? "0" : listaMovimento.get(i).getArgumento11().toString();
                    if (listaMovimentos.get(i).getBaixa() == null) {
                        mensagem = "Baixa não foi concluída, não existe recibo";
                        return null;
                    }
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
                            listaMovimentos.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
                            String.valueOf(listaMovimentos.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
                            String.valueOf(listaMovimentos.get(i).getBaixa().getId()), // ID_BAIXA
                            listaMovimentos.get(i).getBeneficiario().getNome(), // BENEFICIÁRIO
                            listaMovimentos.get(i).getServicos() != null ? listaMovimentos.get(i).getServicos().getDescricao() : "", // SERVICO
                            listaMovimentos.get(i).getVencimento(), // VENCIMENTO
                            new BigDecimal(listaMovimentos.get(i).getValorBaixa()), // VALOR BAIXA
                            listaMovimentos.get(i).getBaixa().getUsuario().getLogin(),
                            listaMovimentos.get(i).getBaixa().getBaixa(),
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
        }
        return null;
    }

    public String retorno() {
        if (retorna) {
            String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            if (url.equals("baixaBoleto")) {
                GenericaSessao.put("linkClicado", true);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaBoleto();
            } else if (url.equals("movimentosReceberSocial")) {
                ((MovimentosReceberSocialJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosSocialBean")).getListaMovimento().clear();
                GenericaSessao.put("linkClicado", true);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).movimentosReceberSocialBaixa();
            } else if (url.equals("lancamentoFinanceiro")) {
                GenericaSessao.put("linkClicado", true);
                return "lancamentoFinanceiro";
                //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).lancamentoFinanceiro();
            } else {
                return null;
            }

        } else {
            return null;
        }
    }

    private float somaValoresGrid() {
        float soma = 0;
        for (int i = 0; i < lista.size(); i++) {
            soma = Moeda.somaValores(soma,
                    Float.parseFloat(
                            Moeda.substituiVirgula(
                                    String.valueOf(lista.get(i).getArgumento1()))));
        }
        return soma;
    }

    public void inserir() {
        float soma = somaValoresGrid();
        float valorF = Float.parseFloat(Moeda.substituiVirgula(valor));
        float totalF = Float.parseFloat(Moeda.substituiVirgula(total));
        if ((Moeda.substituiVirgulaFloat(valor) != 0) && (Moeda.somaValores(soma, valorF) <= totalF)) {
            FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
            if ((Moeda.somaValores(soma, valorF) < totalF) || (soma == 0)) {
                valorF = Moeda.subtracaoValores(totalF, Moeda.somaValores(soma, valorF));
            } else {
                valorF = 0;
            }
            TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoDoc().get(idTipoDoc)).getDescription()));

            // CHEQUE
            if (tipoPagamento.getId() == 4 || tipoPagamento.getId() == 5) {
                if (!getEs().isEmpty() && getEs().equals("S")) {
                    Plano5DB db = new Plano5DBToplink();
                    Plano5 pl = db.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()));

                    for (int i = 0; i < lista.size(); i++) {
                        if (lista.get(i).getArgumento4() != null) {
                            if (((ChequePag) lista.get(i).getArgumento4()).getPlano5().getId() == pl.getId()) {
                                GenericaMensagem.error("Erro", "Esta CONTA já foi adicionada!");
                                return;
                            }
                        }
                        listaBancoSaida.get(i).setValue(i);
                    }

                    ChequePag ch_p = new ChequePag();
                    ch_p.setCheque(numeroChequePag);
                    ch_p.setPlano5(pl);
                    ch_p.setVencimento(vencimento);

                    if (tipo.equals("caixa")) {
                        ch_p.setStatus((FStatus) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(7, "FStatus"));
                    } else {
                        ch_p.setStatus((FStatus) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(8, "FStatus"));
                    }

                    lista.add(new DataObject(vencimento, valor, numeroChequePag, tipoPagamento, ch_p, pl));

                } else {
                    ChequeRec ch = new ChequeRec();
                    ch.setAgencia(chequeRec.getAgencia());
                    ch.setBanco(chequeRec.getBanco());
                    ch.setCheque(numero);
                    ch.setConta(chequeRec.getConta());
                    ch.setEmissao(quitacao);
                    if (tipo.equals("caixa")) {
                        ch.setStatus((FStatus) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(7, "FStatus"));
                    } else {
                        ch.setStatus((FStatus) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(8, "FStatus"));
                    }

                    ch.setVencimento(vencimento);
                    lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, ch, null));
                }
            } else if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
                // CARTAO
                lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaCartao.get(idCartao).getDescription()), "Cartao")));
            } else if (tipoPagamento.getId() == 8 || tipoPagamento.getId() == 9 || tipoPagamento.getId() == 10) {
                Plano5DB db = new Plano5DBToplink();
                Plano5 pl = db.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBanco.get(idBanco).getDescription()));
                lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, pl));
            } else {
                lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, null));
            }
            setValor(Moeda.converteR$Float(valorF));
            desHabilitaConta = true;
            desHabilitaQuitacao = true;
        }
    }

    public String remover(int index) {
        lista.remove(index);
        float soma = somaValoresGrid();
        float valorF = Float.parseFloat(Moeda.substituiVirgula(valor));
        float totalF = Float.parseFloat(Moeda.substituiVirgula(total));
        if ((Moeda.somaValores(soma, valorF) < totalF) || (soma == 0)) {
            valorF = Moeda.subtracaoValores(totalF, soma);
        } else {
            valorF = 0;
        }
        setValor(Moeda.converteR$Float(valorF));

        return null;
    }

    public List<SelectItem> getListaConta() {
        List<SelectItem> conta = new ArrayList<SelectItem>();
        ContaRotinaDB db = new ContaRotinaDBToplink();
        List select;
        if (verificaBaixaBoleto()) {
            select = db.pesquisaContasPorRotina(1);
        } else {
            select = db.pesquisaContasPorRotina();
        }
        for (int i = 0; i < select.size(); i++) {
            conta.add(new SelectItem(
                    i,
                    (String) ((Plano5) select.get(i)).getConta(),
                    Integer.toString(((Plano5) select.get(i)).getId())));
        }
        return conta;
    }

    public List<SelectItem> getListaTipoDoc() {
        FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
        List<SelectItem> tipoDoc = new ArrayList<SelectItem>();
        //select = db.pesquisaTodosTipoPagamento();
        List select = new ArrayList();
        if (!getEs().isEmpty() && getEs().equals("S")) {
            select = db.pesquisaCodigoTipoPagamentoIDS("3,4,5,8,9,10");
        } else {
            select = db.pesquisaCodigoTipoPagamentoIDS("3,4,5,6,7,8,9,10,11");
        }

        for (int i = 0; i < select.size(); i++) {
            tipoDoc.add(new SelectItem(new Integer(i), ((TipoPagamento) select.get(i)).getDescricao(),
                    Integer.toString(((TipoPagamento) select.get(i)).getId())));
        }
        return tipoDoc;
    }

    private Rotina getRotina() {
        if (rotina == null) {
            HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String urlDestino = paginaRequerida.getRequestURI();
            PermissaoUsuarioDB db = new PermissaoUsuarioDBToplink();
            rotina = db.pesquisaRotinaPermissao(urlDestino);
        }
        return rotina;
    }

    public String baixar() {
        if (lista.isEmpty()) {
            return mensagem = "Lista esta vazia!";
        }
        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");

        if (macFilial == null) {
            return mensagem = "Não existe filial na sessão!";
        }

        if (macFilial.getCaixa() == null) {
            return mensagem = "Não é possivel salvar baixa sem um caixa definido!";
        }

        Filial filial;
        Departamento departamento = new Departamento();

        try {
            filial = macFilial.getFilial();
            departamento = macFilial.getDepartamento();
        } catch (Exception e) {
            return mensagem = "Não é foi possível encontrar a filial no sistema!";
        }

        if (Moeda.converteUS$(valor) > 0) {
            return mensagem = "Complete as parcelas para que o Valor seja zerado!";
        } else if (Moeda.converteUS$(valor) < 0) {
            return mensagem = "Erro com o campo valor!";

        }

        Plano5DB plano5DB = new Plano5DBToplink();
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");

        if (verificaBaixaBoleto()) {
            DaoInterface di = new Dao();
            plano5 = (Plano5) di.find(new Plano5(), Integer.parseInt(((SelectItem) getListaConta().get(getIdConta())).getDescription()));
        } else {
            // PEGAR PLANO 5 VINCULADO AO FIN_BOLETO > FIN_CONTA_COBRANCA
            // CASO NÃO TENHA FIN_BOLETO return mensagem = "Não existe conta banco para baixar este boleto!";
            MovimentoDB db = new MovimentoDBToplink();
            Boleto bol = db.pesquisaBoletos(listaMovimentos.get(0).getNrCtrBoleto());
            //plano5 = listaMovimentos.get(0).getPlano5();
            if (bol == null) {
                return mensagem = "Não existe conta banco para baixar este boleto!";
            }
            plano5 = plano5DB.pesquisaPlano5IDContaBanco(bol.getContaCobranca().getContaBanco().getId());
        }

        if (DataHoje.converte(quitacao) == null) {
            quitacao = DataHoje.data();
        }

        for (int i = 0; i < lista.size(); i++) {
            float valor = Float.parseFloat(String.valueOf(lista.get(i).getArgumento1()));
            // CHEQUE
            if (((TipoPagamento) lista.get(i).getArgumento3()).getId() == 4 || ((TipoPagamento) lista.get(i).getArgumento3()).getId() == 5) {
                if (!getEs().isEmpty() && getEs().equals("S")) {
                    lfp.add(new FormaPagamento(-1, null, null, (ChequePag) lista.get(i).getArgumento4(), 0, valor, filial, (Plano5) lista.get(i).getArgumento5(), null, null, (TipoPagamento) lista.get(i).getArgumento3(), 0, null, 0));
                } else {
                    lfp.add(new FormaPagamento(-1, null, (ChequeRec) lista.get(i).getArgumento4(), null, 0, valor, filial, plano5, null, null, (TipoPagamento) lista.get(i).getArgumento3(), 0, null, 0));
                }
            } else if (((TipoPagamento) lista.get(i).getArgumento3()).getId() == 6 || ((TipoPagamento) lista.get(i).getArgumento3()).getId() == 7) {
                // CARTAO    
                Cartao cartao = ((Cartao) lista.get(i).getArgumento5());
                DataHoje dh = new DataHoje();
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, cartao.getPlano5(), null, null, (TipoPagamento) lista.get(i).getArgumento3(), 0, dh.converte(dh.incrementarDias(cartao.getDias(), quitacao)), Moeda.divisaoValores(Moeda.multiplicarValores(valor, cartao.getTaxa()), 100)));
            } else if (((TipoPagamento) lista.get(i).getArgumento3()).getId() == 8 || ((TipoPagamento) lista.get(i).getArgumento3()).getId() == 9 || ((TipoPagamento) lista.get(i).getArgumento3()).getId() == 10) {
                // DOC BANCARIO    
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, (Plano5) lista.get(i).getArgumento5(), null, null, (TipoPagamento) lista.get(i).getArgumento3(), 0, DataHoje.dataHoje(), 0));
            } else {
                // DINHEIRO E OUTROS
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, plano5, null, null, (TipoPagamento) lista.get(i).getArgumento3(), 0, null, 0));
            }
        }

        for (int i = 0; i < listaMovimentos.size(); i++) {
            listaMovimentos.get(i).setTaxa(Moeda.converteUS$(taxa));
        }

        if (!GerarMovimento.baixarMovimentoManual(listaMovimentos, usuario, lfp, Moeda.substituiVirgulaFloat(total), quitacao, macFilial.getCaixa())) {
            mensagem = "Erro ao atualizar boleto!";
            return null;
        } else {
            lista.clear();
            total = "0.0";
            String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            if (url.equals("baixaBoleto")) {
                ((BaixaBoletoJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).getListaBoletos().clear();
                ((BaixaBoletoJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).setCarregarGrid(true);
            } else if (url.equals("movimentosReceberSocial")) {
                ((MovimentosReceberSocialJSFBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosSocialBean")).getListaMovimento().clear();
            } else if (url.equals("emissaoGuias") || url.equals("menuPrincipal")) {
                ((EmissaoGuiasBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emissaoGuiasBean")).atualizarHistorico();
            } else if (url.equals("lancamentoFinanceiro")) {
                ((LancamentoFinanceiroBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lancamentoFinanceiroBean")).getListaParcela().clear();
                ((LancamentoFinanceiroBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lancamentoFinanceiroBean")).getListaParcelaSelecionada().clear();
            }
            retorna = true;
            mensagem = "Baixa realizada com sucesso!";
        }
        return null;
    }

    public String getQuitacao() {
        return quitacao;
    }

    public void setQuitacao(String quitacao) {
        this.quitacao = quitacao;
    }

    public String getValor() {
        getListaMovimentos();
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getIdConta() {
        return idConta;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public List<DataObject> getLista() {
        return lista;
    }

    public void setLista(List<DataObject> lista) {
        this.lista = lista;
    }

    public int getIdTipoDoc() {
        return idTipoDoc;
    }

    public void setIdTipoDoc(int idTipoDoc) {
        this.idTipoDoc = idTipoDoc;
    }

    private void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Modulo getModulo() {
        if (modulo.getId() == -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            modulo = (Modulo) sv.pesquisaCodigo(3, "Modulo");
        }
        return modulo;
    }

    private void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public boolean isDesHabilitaConta() {
        if ((!lista.isEmpty()) || (!verificaBaixaBoleto())) {
            desHabilitaConta = true;
        } else {
            desHabilitaConta = false;
        }
        return desHabilitaConta;
    }

    public boolean verificaBaixaBoleto() {
        String urlRetorno = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        if ((urlRetorno.equals("baixaBoleto")) && (tipo.equals("banco"))) {
            return false;
        } else {
            return true;
        }

    }

    public void setDesHabilitaConta(boolean desHabilitaConta) {
        this.desHabilitaConta = desHabilitaConta;
    }

    public boolean isDesHabilitaQuitacao() {
        if (tipo.equals("banco")) {
            desHabilitaQuitacao = false;
        } else {
            desHabilitaQuitacao = true;
        }
        return desHabilitaQuitacao;
    }

    public void setDesHabilitaQuitacao(boolean desHabilitaQuitacao) {
        this.desHabilitaQuitacao = desHabilitaQuitacao;
    }

    public boolean isDesHabilitaNumero() {
        FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        //TipoPagamento tipo = null;
        TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoDoc().get(idTipoDoc)).getDescription()));
        if (tipoPagamento.getId() == 3 || tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7 || (!getEs().isEmpty() && getEs().equals("S"))) {
            desHabilitaNumero = true;
            numero = "";
        } else {
            desHabilitaNumero = false;
        }
        return desHabilitaNumero;
    }

    public void setDesHabilitaNumero(boolean desHabilitaNumero) {
        this.desHabilitaNumero = desHabilitaNumero;
    }

    public boolean isDesHabilitadoVencimento() {
        FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        //TipoPagamento tipo = null;
        TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoDoc().get(idTipoDoc)).getDescription()));
        if (tipoPagamento.getId() == 5) {
            desHabilitadoVencimento = false;
        } else {
            vencimento = quitacao;
            desHabilitadoVencimento = true;
        }
        return desHabilitadoVencimento;
    }

    public void setDesHabilitadoVencimento(boolean desHabilitadoVencimento) {
        this.desHabilitadoVencimento = desHabilitadoVencimento;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public List<Movimento> getListaMovimentos() {
        if (listaMovimentos.isEmpty()) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento") != null) {
                listaMovimentos = (List) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("listaMovimento");
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("listaMovimento");
                float valorTotal = 0;
                //Plano5DB plano5DB = new Plano5DBToplink();

                if (total.equals("0.00")) {
                    for (int i = 0; i < listaMovimentos.size(); i++) {
                        valorTotal = Moeda.somaValores(valorTotal, listaMovimentos.get(i).getValorBaixa());
                    }
                    total = Moeda.converteR$Float(valorTotal);
                    valor = total;
                }
                if (!verificaBaixaBoleto()) {
                    //plano5 = plano5DB.pesquisaPlano5IDContaBanco(opMovimento.getLista().get(0).getContaCobranca().getContaBanco().getId());
                    plano5 = listaMovimentos.get(0).getPlano5();
                }
            }
        }
        return listaMovimentos;
    }

    public void setListaMovimentos(List<Movimento> listaMovimentos) {
        this.listaMovimentos = listaMovimentos;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTipo() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("caixa_banco") != null) {
            tipo = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("caixa_banco");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("caixa_banco");
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getBanco() {
        if (banco.isEmpty() && getListaMovimentos().size() == 1) {
            MovimentoDB db = new MovimentoDBToplink();
            ImprimirBoleto imp = new ImprimirBoleto();
            Boleto bol = db.pesquisaBoletos(listaMovimentos.get(0).getNrCtrBoleto());

            if (bol == null) {
                listaMovimentos = imp.atualizaContaCobrancaMovimento(listaMovimentos);
            }

            bol = db.pesquisaBoletos(listaMovimentos.get(0).getNrCtrBoleto());
            banco = bol.getContaCobranca().getContaBanco().getConta() + " / " + bol.getContaCobranca().getContaBanco().getBanco().getBanco();
        }
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public ChequeRec getChequeRec() {
        return chequeRec;
    }

    public void setChequeRec(ChequeRec chequeRec) {
        this.chequeRec = chequeRec;
    }

    public String getTaxa() {
        return Moeda.converteR$(taxa);
    }

    public void setTaxa(String taxa) {
        this.taxa = Moeda.substituiVirgula(taxa);
    }

    public boolean isRetorna() {
        return retorna;
    }

    public void setRetorna(boolean retorna) {
        this.retorna = retorna;
    }

    public void alteraVencimento() {
        int quitacaoInteiro = DataHoje.converteDataParaRefInteger(quitacao);
        int vencimentoInteiro = DataHoje.converteDataParaRefInteger(vencimento);
        if (quitacaoInteiro != vencimentoInteiro) {
            vencimento = quitacao;
        }
    }

    public List<SelectItem> getListaCartao() {
        if (listaCartao.isEmpty()) {

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Cartao> result = sv.listaObjeto("Cartao");
            TipoPagamento tipoPagamento = (TipoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoDoc().get(idTipoDoc)).getDescription()), "TipoPagamento");
            int conta = 0;
            for (Cartao result1 : result) {
                String tipox = result1.getDebitoCredito().equals("D") ? "Débito" : "Crédito";
                if (tipoPagamento.getId() == 6 && result1.getDebitoCredito().equals("C")) {
                    listaCartao.add(new SelectItem(conta, result1.getDescricao() + " - " + tipox, Integer.toString(result1.getId())));
                    conta++;
                } else if (tipoPagamento.getId() == 7 && result1.getDebitoCredito().equals("D")) {
                    listaCartao.add(new SelectItem(conta, result1.getDescricao() + " - " + tipox, Integer.toString(result1.getId())));
                    conta++;
                }
            }
        }
        return listaCartao;
    }

    public void setListaCartao(List<SelectItem> listaCartao) {
        this.listaCartao = listaCartao;
    }

    public int getIdCartao() {
        return idCartao;
    }

    public void setIdCartao(int idCartao) {
        this.idCartao = idCartao;
    }

    public List<SelectItem> getListaBanco() {
        if (listaBanco.isEmpty()) {
            List<ContaBanco> result = (new ContaBancoDBToplink()).pesquisaTodos();
            for (int i = 0; i < result.size(); i++) {
                listaBanco.add(new SelectItem(i, result.get(i).getAgencia() + " " + result.get(i).getConta() + " - " + result.get(i).getBanco().getBanco(), Integer.toString(result.get(i).getId())));
            }
        }
        return listaBanco;
    }

    public void setListaBanco(List<SelectItem> listaBanco) {
        this.listaBanco = listaBanco;
    }

    public int getIdBanco() {
        return idBanco;
    }

    public void setIdBanco(int idBanco) {
        this.idBanco = idBanco;
    }

    public String getEs() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("esMovimento") != null) {
            es = String.valueOf(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("esMovimento"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("esMovimento");
        }
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

//    public List<SelectItem> getListaContaPagamento() {
//        if (listaContaPagamento.isEmpty()){
//            if (!getEs().isEmpty() && getEs().equals("S")){
//                LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
//                List<Plano5> result = db.listaComboPagamentoBaixa();
//                if (!result.isEmpty()){
//                    for (int i = 0; i < result.size(); i ++){
//                        listaContaPagamento.add(new SelectItem(
//                                i, 
//                                result.get(i).getContaBanco().getBanco().getBanco()+ " " +result.get(i).getContaBanco().getAgencia()+ " / " +result.get(i).getConta(), 
//                                String.valueOf(i))
//                        );
//                    }
//                }else{
//                    listaContaPagamento.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
//                }
//            }
//        }
//        return listaContaPagamento;
//    }
//
//    public void setListaContaPagamento(List<SelectItem> listaContaPagamento) {
//        this.listaContaPagamento = listaContaPagamento;
//    }
//
//    public int getIdContaPagamento() {
//        return idContaPagamento;
//    }
//
//    public void setIdContaPagamento(int idContaPagamento) {
//        this.idContaPagamento = idContaPagamento;
//    }
    public String getNumeroChequePag() {
        return numeroChequePag;
    }

    public void setNumeroChequePag(String numeroChequePag) {
        this.numeroChequePag = numeroChequePag;
    }

    public List<SelectItem> getListaBancoSaida() {
        if (listaBancoSaida.isEmpty()) {
            List<ContaBanco> result = (new ContaBancoDBToplink()).pesquisaTodos();
            for (int i = 0; i < result.size(); i++) {
                listaBancoSaida.add(new SelectItem(i, result.get(i).getAgencia() + " " + result.get(i).getConta() + " - " + result.get(i).getBanco().getBanco(), Integer.toString(result.get(i).getId())));
            }
        }

        if (!getEs().isEmpty() && getEs().equals("S")) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            ContaBanco cb = (ContaBanco) sv.pesquisaCodigo(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()), "ContaBanco");

//            if (!lista.isEmpty()){
//                for (DataObject dob : lista){
//                    try{
//                        Plano5DB db = new Plano5DBToplink();
//                        
//                        Plano5 pl = db.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()));
//                        if (dob.getArgumento5() != null){
//                            Plano5 plx = (Plano5)dob.getArgumento5();
//                            
//                            if (pl.getId() == plx.getId()){
//                                GenericaMensagem.error("Erro", "Esta conta já foi adicionada!");
//                                break;
//                            }
//                        }
//                        
//                        //ChequePag ch_p = ((ChequePag)dob.getArgumento5());
//                        //numeroChequePag = String.valueOf(ch_p.getUCheque() + 1);
//                        //if (){
//                            
//                        //}
//                    }catch(Exception e){
//                    }
//                }
//                numeroChequePag = String.valueOf(cb.getUCheque() + 1);
//            }else{
            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
//            }
        }
        return listaBancoSaida;
    }

    public void setListaBancoSaida(List<SelectItem> listaBancoSaida) {
        this.listaBancoSaida = listaBancoSaida;
    }

    public int getIdBancoSaida() {
        return idBancoSaida;
    }

    public void setIdBancoSaida(int idBancoSaida) {
        this.idBancoSaida = idBancoSaida;
    }
}
