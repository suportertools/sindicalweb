package br.com.rtools.financeiro.beans;

import br.com.rtools.academia.beans.MatriculaAcademiaBean;
import br.com.rtools.arrecadacao.beans.BaixaBoletoBean;
import br.com.rtools.associativo.beans.EmissaoGuiasBean;
import br.com.rtools.associativo.beans.MovimentosReceberSocialBean;
import br.com.rtools.associativo.beans.VendaBaileBean;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Caixa;
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
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDB;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.Plano5DB;
import br.com.rtools.financeiro.db.Plano5DBToplink;
import br.com.rtools.financeiro.lista.ListValoresBaixaGeral;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.movimento.ImprimirRecibo;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.PermissaoUsuarioDB;
import br.com.rtools.seguranca.db.PermissaoUsuarioDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

@ManagedBean
@SessionScoped
public class BaixaGeralBean implements Serializable {

    private String quitacao = DataHoje.data();
    private String vencimento = DataHoje.data();
    private String valor = "0,00";
    private String numero = "";
    private String numeroChequePag = "";
    private String total = "0,00";
    private List<ListValoresBaixaGeral> listaValores = new ArrayList();
    private List<Movimento> listaMovimentos = new ArrayList();
    private List<SelectItem> listaCartao = new ArrayList();
    private List<SelectItem> listaBanco = new ArrayList();
    private List<SelectItem> listaBancoSaida = new ArrayList();
    private List<SelectItem> listaConta = new ArrayList();
    private List<SelectItem> listaTipoPagamento = new ArrayList();
    private Integer idConta = 0;
    private int idTipoPagamento = 0;
    private int idCartao = 0;
    private int idBanco = 0;
    private int idBancoSaida = 0;
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
    private boolean visibleModal = false;
    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();

    private String valorTroco = "";
    private String valorEditavel = "";
    private TipoPagamento tipoPagamento;

    @PostConstruct
    public void init() {
        cfb.init();
        getListaMovimentos();
        getTipo();
        tipoPagamento = (TipoPagamento) new Dao().find(new TipoPagamento(), Integer.valueOf(getListaTipoPagamento().get(idTipoPagamento).getDescription()));
    }

    public void verificaValorDigitado() {
        float valor_troco = 0;
        boolean dinheiro = false;
        for (int i = 0; i < listaValores.size(); i++) {
            if (listaValores.get(i).getTipoPagamento().getId() == 3) {
                dinheiro = true;
            }
        }

        if (dinheiro) {
            for (int i = 0; i < listaValores.size(); i++) {
                if (listaValores.get(i).getTipoPagamento().getId() == 3) {
                    float valorx = Moeda.converteUS$(listaValores.get(i).getValor());
                    float valordigitado = Moeda.converteUS$(listaValores.get(i).getValorDigitado());
                    if (valorx < valordigitado) {
                        valor_troco = Moeda.subtracaoValores(valordigitado, valorx);
                        valorTroco = Moeda.converteR$Float(valor_troco);
                    }
                }
            }
        } else {
            valorTroco = "";
        }

        //TipoPagamento tp = (TipoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(, "TipoPagamento");
//        if (Integer.parseInt( getListaTipoPagamento().get(idTipoPagamento).getDescription()) != 3){
//            float valordigitado = Moeda.converteUS$(valor);
//            float calculo = Moeda.subtracaoValores(Moeda.converteUS$(total), somaValoresGrid());
//            if (valordigitado > calculo){
//                valor = Moeda.converteR$Float(calculo);
//            }
//        }else{
//            float valordigitado = Moeda.converteUS$(valor);
//            float calculo = Moeda.subtracaoValores(Moeda.converteUS$(total), somaValoresGrid());
//            if (valordigitado > calculo){
//                valorTroco = Moeda.converteR$Float(Moeda.subtracaoValores(valordigitado, calculo));
//            }
//        }
    }

    public void atualizaTipo() {
        //TipoPagamento tipoPagamento = (TipoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoPagamento().get(idTipoPagamento)).getDescription()), "TipoPagamento");
        tipoPagamento = (TipoPagamento) new Dao().find(new TipoPagamento(), Integer.valueOf(getListaTipoPagamento().get(idTipoPagamento).getDescription()));
        if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
            listaCartao.clear();
            idCartao = 0;
        }
    }

    public void alteraNumeroChequeConta() {
        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        Plano5DB dbx = new Plano5DBToplink();
        if (listaBancoSaida.size() == 1 && listaBancoSaida.get(0).getDescription().isEmpty()) {
            GenericaMensagem.error("Erro", "Nenhum Banco para Saida Encontrado");
            return;
        }

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

    public String retorno() {
        if (retorna) {
            String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            GenericaSessao.put("linkClicado", true);
            if (url.equals("baixaBoleto")) {
                ((BaixaBoletoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).loadListaBoleto();
                //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaBoleto();
                return "baixaBoleto";
            } else if (url.equals("movimentosReceberSocial")) {
                ((MovimentosReceberSocialBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosReceberSocialBean")).getListaMovimento().clear();
                //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).movimentosReceberSocialBaixa();
                return "movimentosReceberSocial";
            } else if (url.equals("lancamentoFinanceiro")) {
                return "lancamentoFinanceiro";
                //return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).lancamentoFinanceiro();
            } else if (url.equals("emissaoGuias")) {
                return "emissaoGuias";
            } else if (url.equals("matriculaAcademia")) {
                return "matriculaAcademia";
            } else if (url.equals("menuPrincipal")) {
                return "menuPrincipal";
            } else if (url.equals("acessoNegado")) {
                return "menuPrincipal";
            } else if (url.equals("geracaoDebitosCartao")) {
                GenericaSessao.put("lista_movimentos_baixados", listaMovimentos);
                return "geracaoDebitosCartao";
            }  else if (url.equals("matriculaEscola")) {
                return "matriculaEscola";
            } else if (url.equals("vendasBaile")) {
                ((VendaBaileBean) GenericaSessao.getObject("vendaBaileBean")).novo();
                return "vendasBaile";
            }  else if (url.equals("conviteMovimento")) {
                return "conviteMovimento";
            }  else {
                return null;
            }
        } else {
            return null;
        }
    }

    private float somaValoresGrid() {
        float soma = 0;
        for (ListValoresBaixaGeral listaValore : listaValores) {
            soma = Moeda.somaValores(soma,
                    Float.parseFloat(Moeda.substituiVirgula(String.valueOf(listaValore.getValor())))
            );
        }
        return soma;
    }

    public void inserir() {

        if (Moeda.converteUS$(valor) < 0) {
            GenericaMensagem.error("Atenção", "Valor negativo não é permitido!");
            return;
        }

        if (Moeda.converteUS$(total) == somaValoresGrid() && Moeda.converteUS$(total) != 0) {
            GenericaMensagem.error("Atenção", "Os valores já conferem!");
            return;
        }

        float valorDigitado = 0;
        valorEditavel = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(total), somaValoresGrid()));

        if (Moeda.converteUS$(valor) > Moeda.converteUS$(valorEditavel)) {
            valorDigitado = Moeda.converteUS$(valor);
            valor = valorEditavel;
        } else {
            valorDigitado = Moeda.converteUS$(valor);
        }

        //FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        //TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoPagamento().get(idTipoPagamento)).getDescription()));
        // CHEQUE
        if (tipoPagamento.getId() == 4 || tipoPagamento.getId() == 5) {
            if (!getEs().isEmpty() && getEs().equals("S")) {
                Plano5DB db = new Plano5DBToplink();
                if (listaBancoSaida.size() == 1 && listaBancoSaida.get(0).getDescription().isEmpty()) {
                    GenericaMensagem.error("Erro", "Nenhum Banco saida Encontrado!");
                    return;
                }
                Plano5 pl = db.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()));

                for (int i = 0; i < listaValores.size(); i++) {
                    if (listaValores.get(i).getChequePag() != null) {
                        if (listaValores.get(i).getChequePag().getPlano5().getId() == pl.getId()) {
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

                //lista.add(new DataObject(vencimento, valor, numeroChequePag, tipoPagamento, ch_p, pl));
                listaValores.add(new ListValoresBaixaGeral(vencimento, valor, numeroChequePag, tipoPagamento, ch_p, null, pl, null, Moeda.converteR$Float(valorDigitado)));
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
                //lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, ch, null));
                listaValores.add(new ListValoresBaixaGeral(vencimento, valor, numero, tipoPagamento, null, ch, null, null, Moeda.converteR$Float(valorDigitado)));
            }
            numero = "";
            numeroChequePag = "";
            chequeRec = new ChequeRec();
        } else if (tipoPagamento.getId() == 6 || tipoPagamento.getId() == 7) {
            // CARTAO
            if (listaCartao.size() == 1 && !listaCartao.get(0).getDescription().isEmpty()) //lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaCartao.get(idCartao).getDescription()), "Cartao")));
            {
                listaValores.add(new ListValoresBaixaGeral(vencimento, valor, numero, tipoPagamento, null, null, null, (Cartao) new Dao().find(new Cartao(), Integer.valueOf(listaCartao.get(idCartao).getDescription())), Moeda.converteR$Float(valorDigitado)));
            }
        } else if (tipoPagamento.getId() == 2 || tipoPagamento.getId() == 8 || tipoPagamento.getId() == 9 || tipoPagamento.getId() == 10 || tipoPagamento.getId() == 13) {
            Plano5DB db = new Plano5DBToplink();
            if (listaBanco.size() == 1 && listaBanco.get(0).getDescription().isEmpty()) {
                GenericaMensagem.error("Erro", "Nenhum Banco Encontrado!");
                return;
            }
            Plano5 pl = db.pesquisaPlano5IDContaBanco(Integer.valueOf(listaBanco.get(idBanco).getDescription()));
            //lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, pl));
            listaValores.add(new ListValoresBaixaGeral(vencimento, valor, numero, tipoPagamento, null, null, pl, null, Moeda.converteR$Float(valorDigitado)));

            numero = "";
        } else {
            //lista.add(new DataObject(vencimento, valor, numero, tipoPagamento, null, null));
            listaValores.add(new ListValoresBaixaGeral(vencimento, valor, numero, tipoPagamento, null, null, null, null, Moeda.converteR$Float(valorDigitado)));
        }
        desHabilitaConta = true;
        desHabilitaQuitacao = true;

        verificaValorDigitado();
        valor = Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(total), somaValoresGrid()));
    }

    public String remover(int index) {
        //lista.remove(index);
        listaValores.remove(index);
        float soma = somaValoresGrid();
        float valorF = Float.parseFloat(Moeda.substituiVirgula(valor));
        float totalF = Float.parseFloat(Moeda.substituiVirgula(total));
        if ((Moeda.somaValores(soma, valorF) < totalF) || (soma == 0)) {
            valorF = Moeda.subtracaoValores(totalF, soma);
        } else {
            valorF = 0;
        }
        setValor(Moeda.converteR$Float(valorF));

        verificaValorDigitado();
        return null;
    }

    public List<SelectItem> getListaConta() {
        if (listaConta.isEmpty()) {
            ContaRotinaDB db = new ContaRotinaDBToplink();
            List select;
            if (verificaBaixaBoleto()) {
                select = db.pesquisaContasPorRotina(1);
            } else {
                select = db.pesquisaContasPorRotina();
            }

            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaConta.add(new SelectItem(
                            i,
                            (String) ((Plano5) select.get(i)).getConta(),
                            Integer.toString(((Plano5) select.get(i)).getId())));
                }
            } else {
                listaConta.add(new SelectItem(0, "Nenhuma Conta Encontrada", ""));
            }
        }
        return listaConta;
    }

    public List<SelectItem> getListaTipoPagamento() {
        if (listaTipoPagamento.isEmpty()) {
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<TipoPagamento> select = new ArrayList();
            if (!getEs().isEmpty() && getEs().equals("S")) {
                select = db.pesquisaCodigoTipoPagamentoIDS("3,4,5,8,9,10");
                idTipoPagamento = 0;
            } else {
                if (tipo.equals("caixa")) {
                    select = db.pesquisaCodigoTipoPagamentoIDS("2,3,4,5,6,7,8,9,10,11,13");
                    idTipoPagamento = 1;
                } else {
                    select = db.pesquisaCodigoTipoPagamentoIDS("2,8,9,10,11,13");
                    idTipoPagamento = 0;
                }

            }
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaTipoPagamento.add(new SelectItem(
                            i,
                            select.get(i).getDescricao(),
                            Integer.toString(select.get(i).getId())
                    ));
                }
            } else {
                listaTipoPagamento.add(new SelectItem(0, "Nenhum tipo de pagamento Encontrado", ""));
            }
        }
        return listaTipoPagamento;
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
//        if (lista.isEmpty()) {
//            return mensagem = "Lista esta vazia!";
//        }
        if (listaValores.isEmpty()) {
            return mensagem = "Lista esta vazia!";
        }
        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        Caixa caixa = null;
        Usuario usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");

        Filial filial;
        Departamento departamento = new Departamento();

        try {
            filial = macFilial.getFilial();
            departamento = macFilial.getDepartamento();
        } catch (Exception e) {
            return mensagem = "Não é foi possível encontrar a filial no sistema!";
        }

        if (!macFilial.isCaixaOperador()) {
            if (tipo.equals("caixa")) {
                if (macFilial.getCaixa() == null) {
                    return mensagem = "Não é possivel salvar baixa sem um caixa definido para esta estação!";
                }

                caixa = macFilial.getCaixa();
            }
        } else {
            FinanceiroDB db = new FinanceiroDBToplink();
            caixa = db.pesquisaCaixaUsuario(usuario.getId());

            if (tipo.equals("caixa")) {
                if (caixa == null) {
                    return mensagem = "Não é possivel salvar baixa sem um caixa/operador definido!";
                }
            }
        }

        if (Moeda.converteUS$(valor) > 0) {
            return mensagem = "Complete as parcelas para que o Valor seja zerado!";
        } else if (Moeda.converteUS$(valor) < 0) {
            return mensagem = "Erro com o campo valor!";

        }

        Plano5DB plano5DB = new Plano5DBToplink();

        if (verificaBaixaBoleto()) {
            if (getListaConta().size() == 1 && getListaConta().get(0).getDescription().isEmpty()) {
                return mensagem = "Lista de Planos Vazia, verificar Conta Rotina!";
            }
            DaoInterface di = new Dao();
            plano5 = (Plano5) di.find(new Plano5(), Integer.parseInt(((SelectItem) getListaConta().get(getIdConta())).getDescription()));
        } else {
            // PEGAR PLANO 5 VINCULADO AO FIN_BOLETO > FIN_CONTA_COBRANCA
            // CASO NÃO TENHA FIN_BOLETO return mensagem = "Não existe conta banco para baixar este boleto!";
            MovimentoDB db = new MovimentoDBToplink();
            Boleto bol = db.pesquisaBoletos(listaMovimentos.get(0).getNrCtrBoleto());
            if (bol == null) {
                return mensagem = "Não existe conta banco para baixar este boleto!";
            }
            plano5 = plano5DB.pesquisaPlano5IDContaBanco(bol.getContaCobranca().getContaBanco().getId());
        }

        if (DataHoje.converte(quitacao) == null) {
            quitacao = DataHoje.data();
        }

        for (int i = 0; i < listaValores.size(); i++) {
            float valor = Moeda.converteUS$(String.valueOf(listaValores.get(i).getValor()));
            // CHEQUE
            if (listaValores.get(i).getTipoPagamento().getId() == 4 || listaValores.get(i).getTipoPagamento().getId() == 5) {
                if (!getEs().isEmpty() && getEs().equals("S")) {
                    lfp.add(new FormaPagamento(-1, null, null, listaValores.get(i).getChequePag(), 0, valor, filial, listaValores.get(i).getPlano5(), null, null, listaValores.get(i).getTipoPagamento(), 0, null, 0));
                } else {
                    lfp.add(new FormaPagamento(-1, null, listaValores.get(i).getChequeRec(), null, 0, valor, filial, plano5, null, null, listaValores.get(i).getTipoPagamento(), 0, null, 0));
                }
            } else if (listaValores.get(i).getTipoPagamento().getId() == 6 || listaValores.get(i).getTipoPagamento().getId() == 7) {
                // CARTAO    
                Cartao cartao = listaValores.get(i).getCartao();
                DataHoje dh = new DataHoje();
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, cartao.getPlano5(), null, null, listaValores.get(i).getTipoPagamento(), 0, dh.converte(dh.incrementarDias(cartao.getDias(), quitacao)), Moeda.divisaoValores(Moeda.multiplicarValores(valor, cartao.getTaxa()), 100)));
            } else if (listaValores.get(i).getTipoPagamento().getId() == 8 || listaValores.get(i).getTipoPagamento().getId() == 9 || listaValores.get(i).getTipoPagamento().getId() == 10) {
                // DOC BANCARIO    
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, listaValores.get(i).getPlano5(), null, null, listaValores.get(i).getTipoPagamento(), 0, DataHoje.dataHoje(), 0));
            } else {
                // DINHEIRO E OUTROS
                lfp.add(new FormaPagamento(-1, null, null, null, 0, valor, filial, plano5, null, null, listaValores.get(i).getTipoPagamento(), 0, null, 0));
            }
        }

        for (int i = 0; i < listaMovimentos.size(); i++) {
            listaMovimentos.get(i).setTaxa(Moeda.converteUS$(taxa));
        }
        float vl = (!valorTroco.isEmpty()) ? Moeda.converteUS$(valorTroco) : 0;
        if (!GerarMovimento.baixarMovimentoManual(listaMovimentos, usuario, lfp, Moeda.substituiVirgulaFloat(total), quitacao, caixa, vl)) {
            mensagem = "Erro ao atualizar boleto!";
            return null;
        } else {
            //lista.clear();
            listaValores.clear();
            total = "0.0";
            String url = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            GenericaSessao.put("linkClicado", true);
            if (url.equals("baixaBoleto")) {
//                ((BaixaBoletoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).getListaBoletos().clear();
//                ((BaixaBoletoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).setCarregarGrid(true);
                ((BaixaBoletoBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("baixaBoletoBean")).loadListaBoleto();
            } else if (url.equals("movimentosReceberSocial")) {
                ((MovimentosReceberSocialBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("movimentosReceberSocialBean")).getListaMovimento().clear();
            } else if (url.equals("emissaoGuias") || url.equals("menuPrincipal")) {
                ((EmissaoGuiasBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("emissaoGuiasBean")).atualizarHistorico();
            } else if (url.equals("lancamentoFinanceiro")) {
                ((LancamentoFinanceiroBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lancamentoFinanceiroBean")).getListaParcela().clear();
                ((LancamentoFinanceiroBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("lancamentoFinanceiroBean")).getListaParcelaSelecionada().clear();
            } else if (url.equals("matriculaAcademia")) {
                ((MatriculaAcademiaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaAcademiaBean")).getListaMovimentos().clear();
                ((MatriculaAcademiaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaAcademiaBean")).setDesabilitaCamposMovimento(true);
                ((MatriculaAcademiaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaAcademiaBean")).setDesabilitaDiaVencimento(true);

            }
            retorna = true;
            mensagem = "Baixa realizada com sucesso!";
            GenericaSessao.put("baixa_sucesso", true);
            visibleModal = true;
        }
        return null;
    }

//    public String imprimirRecibo() {
//        if (!listaMovimentos.isEmpty()) {
//            ImprimirRecibo ir = new ImprimirRecibo();
//            ir.recibo(listaMovimentos.get(0).getId());
//        }
//        return retorno();
//    }
//    

    public void imprimirRecibo() {
        if (!listaMovimentos.isEmpty()) {
            ImprimirRecibo ir = new ImprimirRecibo();
            ir.recibo(listaMovimentos.get(0).getId());
        }
    }

    public String getQuitacao() {
        return quitacao;
    }

    public void setQuitacao(String quitacao) {
        this.quitacao = quitacao;
    }

    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.converteR$(valor);
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

    public Integer getIdConta() {
        return idConta;
    }

    public void setIdConta(Integer idConta) {
        this.idConta = idConta;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

//    public List<DataObject> getLista() {
//        return lista;
//    }
//
//    public void setLista(List<DataObject> lista) {
//        this.lista = lista;
//    }
    public int getIdTipoPagamento() {
        return idTipoPagamento;
    }

    public void setIdTipoPagamento(int idTipoPagamento) {
        this.idTipoPagamento = idTipoPagamento;
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
        //if ((!lista.isEmpty()) || (!verificaBaixaBoleto())) {
        if ((!listaValores.isEmpty()) || (!verificaBaixaBoleto())) {
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
        //FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        //TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoPagamento().get(idTipoPagamento)).getDescription()));
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
        //FTipoDocumentoDB tipoDocDB = new FTipoDocumentoDBToplink();
        //TipoPagamento tipoPagamento = tipoDocDB.pesquisaCodigoTipoPagamento(Integer.parseInt(((SelectItem) getListaTipoPagamento().get(idTipoPagamento)).getDescription()));
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

                if (total.equals("0,00")) {
                    for (int i = 0; i < listaMovimentos.size(); i++) {
                        valorTotal = Moeda.somaValores(valorTotal, listaMovimentos.get(i).getValorBaixa());
                    }
                    total = Moeda.converteR$Float(valorTotal);
                    valor = total;
                }
                if (!verificaBaixaBoleto()) {
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

    /**
     *
     * @param tipo (Tipos: caixa)
     */
    public static void listenerTipoCaixaSession(String tipo) {
        GenericaSessao.put("caixa_banco", tipo);
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
            if (bol != null) {
                banco = bol.getContaCobranca().getContaBanco().getConta() + " / " + bol.getContaCobranca().getContaBanco().getBanco().getBanco();
            } else {
                banco = "BANCO";
            }
        } else {
            banco = "BANCO";
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
            //TipoPagamento tipoPagamento = (TipoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.parseInt(((SelectItem) getListaTipoPagamento().get(idTipoPagamento)).getDescription()), "TipoPagamento");
            int conta = 0;
            if (!result.isEmpty()) {
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
            } else {
                listaCartao.add(new SelectItem(0, "Nenhum Cartão Encontrado", ""));
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

            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    listaBanco.add(new SelectItem(i, result.get(i).getAgencia() + " " + result.get(i).getConta() + " - " + result.get(i).getBanco().getBanco(), Integer.toString(result.get(i).getId())));
                }
            } else {
                listaBanco.add(new SelectItem(0, "Nenhum Banco Encontrado", ""));
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

    public String getNumeroChequePag() {
        return numeroChequePag;
    }

    public void setNumeroChequePag(String numeroChequePag) {
        this.numeroChequePag = numeroChequePag;
    }

    public List<SelectItem> getListaBancoSaida() {
        if (listaBancoSaida.isEmpty()) {
            List<ContaBanco> result = (new ContaBancoDBToplink()).pesquisaTodos();

            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    listaBancoSaida.add(new SelectItem(i, result.get(i).getAgencia() + " " + result.get(i).getConta() + " - " + result.get(i).getBanco().getBanco(), Integer.toString(result.get(i).getId())));
                }
            } else {
                listaBancoSaida.add(new SelectItem(0, "Nenhum Banco Encontrado", ""));
            }
        }

        if (!getEs().isEmpty() && getEs().equals("S")) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            ContaBanco cb = (ContaBanco) sv.pesquisaCodigo(Integer.valueOf(listaBancoSaida.get(idBancoSaida).getDescription()), "ContaBanco");

            numeroChequePag = String.valueOf(cb.getUCheque() + 1);
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

    public void setListaConta(List<SelectItem> listaConta) {
        this.listaConta = listaConta;
    }

    public void setListaTipoPagamento(List<SelectItem> listaTipoPagamento) {
        this.listaTipoPagamento = listaTipoPagamento;
    }

    public boolean isVisibleModal() {
        return visibleModal;
    }

    public void setVisibleModal(boolean visibleModal) {
        this.visibleModal = visibleModal;
    }

    public String getValorTroco() {
        return valorTroco;
    }

    public void setValorTroco(String valorTroco) {
        this.valorTroco = valorTroco;
    }

    public List<ListValoresBaixaGeral> getListaValores() {
        return listaValores;
    }

    public void setListaValores(List<ListValoresBaixaGeral> listaValores) {
        this.listaValores = listaValores;
    }

    public String getValorEditavel() {
        return valorEditavel;
    }

    public void setValorEditavel(String valorEditavel) {
        this.valorEditavel = valorEditavel;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }
}

//
//    public void salvarRecibo(byte[] arquivo, Baixa baixa) {
//        if (baixa.getCaixa() == null) {
//            return;
//        }
//
//        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
//        Diretorio.criar("Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
//
//        String path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + ".pdf";
//        File file_arquivo = new File(path_arquivo);
//
//        if (file_arquivo.exists()) {
//            path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + "_(2).pdf";
//        }
//
//        try {
//            File fl = new File(path_arquivo);
//            FileOutputStream out = new FileOutputStream(fl);
//            out.write(arquivo);
//            out.flush();
//            out.close();
//        } catch (IOException e) {
//            System.out.println(e);
//        }
//
//    }
//
//    public String imprimirRecibo() {
//        if (!listaMovimentos.isEmpty()) {
//            try {
//                Collection vetor = new ArrayList();
//                Juridica sindicato = (Juridica) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "Juridica");
//                PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
//                MovimentosReceberSocialDB dbs = new MovimentosReceberSocialDBToplink();
//                MovimentoDB db = new MovimentoDBToplink();
//                
//                PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
//                String formas[] = new String[10];
//
//                
//                for (int i = 0; i < lfp.size(); i++) {
//                    // 4 - CHEQUE    
//                    if (lfp.get(i).getTipoPagamento().getId() == 4) {
//                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque();
//                        // 5 - CHEQUE PRÉ
//                    } else if (lfp.get(i).getTipoPagamento().getId() == 5) {
//                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor()) + " (B: " + lfp.get(i).getChequeRec().getBanco() + " Ag: " + lfp.get(i).getChequeRec().getAgencia() + " C: " + lfp.get(i).getChequeRec().getConta() + " CH: " + lfp.get(i).getChequeRec().getCheque() + " P: " + lfp.get(i).getChequeRec().getVencimento() + ")";
//                        // QUALQUER OUTRO    
//                    } else {
//                        formas[i] = lfp.get(i).getTipoPagamento().getDescricao() + ": R$ " + Moeda.converteR$Float(lfp.get(i).getValor());
//                    }
//                }
//
//                Guia gu = db.pesquisaGuias(listaMovimentos.get(0).getLote().getId());
//                String conveniada = "";
//                if (gu.getId() != -1 && gu.getSubGrupoConvenio() != null) {
//                    LancamentoIndividualDB dbl = new LancamentoIndividualDBToplink();
//                    List<Juridica> list = (List<Juridica>) dbl.listaEmpresaConveniadaPorSubGrupo(gu.getSubGrupoConvenio().getId());
//                    if (!list.isEmpty()) {
//                        conveniada = list.get(0).getFantasia();
//                    }
//                }
//                
//                for (int i = 0; i < listaMovimentos.size(); i++) {
//                    //String valor = (getConverteNullString(listaMovimento.get(i).getArgumento11().toString()) == "") ? "0" : listaMovimento.get(i).getArgumento11().toString();
//                    if (listaMovimentos.get(i).getBaixa() == null) {
//                        mensagem = "Baixa não foi concluída, não existe recibo";
//                        return null;
//                    }
//                    vetor.add(new ParametroRecibo(
//                            ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
//                            sindicato.getPessoa().getNome(),
//                            pe.getEndereco().getDescricaoEndereco().getDescricao(),
//                            pe.getEndereco().getLogradouro().getDescricao(),
//                            pe.getNumero(),
//                            pe.getComplemento(),
//                            pe.getEndereco().getBairro().getDescricao(),
//                            pe.getEndereco().getCep().substring(0, 5) + "-" + pe.getEndereco().getCep().substring(5),
//                            pe.getEndereco().getCidade().getCidade(),
//                            pe.getEndereco().getCidade().getUf(),
//                            sindicato.getPessoa().getTelefone1(),
//                            sindicato.getPessoa().getEmail1(),
//                            sindicato.getPessoa().getSite(),
//                            sindicato.getPessoa().getDocumento(),
//                            listaMovimentos.get(i).getLote().getPessoa().getNome(), // RESPONSÁVEL
//                            String.valueOf(listaMovimentos.get(i).getLote().getPessoa().getId()), // ID_RESPONSAVEL
//                            String.valueOf(listaMovimentos.get(i).getBaixa().getId()), // ID_BAIXA
//                            listaMovimentos.get(i).getBeneficiario().getNome(), // BENEFICIÁRIO
//                            listaMovimentos.get(i).getServicos() != null ? listaMovimentos.get(i).getServicos().getDescricao() : "", // SERVICO
//                            listaMovimentos.get(i).getVencimento(), // VENCIMENTO
//                            new BigDecimal(listaMovimentos.get(i).getValorBaixa()), // VALOR BAIXA
//                            listaMovimentos.get(i).getBaixa().getUsuario().getLogin(),
//                            listaMovimentos.get(i).getBaixa().getBaixa(),
//                            DataHoje.horaMinuto(),
//                            formas[0],
//                            formas[1],
//                            formas[2],
//                            formas[3],
//                            formas[4],
//                            formas[5],
//                            formas[6],
//                            formas[7],
//                            formas[8],
//                            formas[9],
//                            ( conveniada.isEmpty() ) ? "" : "Empresa Conveniada: " + conveniada
//                    )
//                    );
//
//                }
//
//                File fl = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/RECIBO.jasper"));
//                JasperReport jasper = (JasperReport) JRLoader.loadObject(fl);
//
//                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
//                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
//
//                byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//
//                salvarRecibo(arquivo, listaMovimentos.get(0).getBaixa());
//
//                HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//                res.setContentType("application/pdf");
//                res.setHeader("Content-disposition", "inline; filename=\"" + "boleto_x" + ".pdf\"");
//                res.getOutputStream().write(arquivo);
//                res.getCharacterEncoding();
//
//                FacesContext.getCurrentInstance().responseComplete();
//            } catch (JRException | IOException ex) {
//                Logger.getLogger(MovimentosReceberSocialBean.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//        return retorno();
//    }
