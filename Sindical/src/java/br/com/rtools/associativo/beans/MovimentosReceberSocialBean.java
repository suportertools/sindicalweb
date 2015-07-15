package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.MovimentosReceberSocialDB;
import br.com.rtools.associativo.db.MovimentosReceberSocialDBToplink;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.Boleto;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Guia;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.financeiro.beans.ConfiguracaoFinanceiroBean;
import br.com.rtools.financeiro.dao.ServicoPessoaDao;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDB;
import br.com.rtools.financeiro.db.ServicoContaCobrancaDBToplink;
import br.com.rtools.impressao.ParametroEncaminhamento;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.movimento.ImprimirBoleto;
import br.com.rtools.movimento.ImprimirRecibo;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.beans.FisicaBean;
import br.com.rtools.pessoa.beans.JuridicaBean;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.AutenticaUsuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.Diretorio;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.db.FunctionsDB;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@ManagedBean
@SessionScoped
public class MovimentosReceberSocialBean implements Serializable {

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
    private String desconto = "0,00";
    private boolean chkSeleciona = false;
    private boolean addMais = false;
    private Pessoa pessoa = new Pessoa();
    private List<Pessoa> listaPessoa = new ArrayList();

    private String matricula = "";
    private String categoria = "";
    private String grupo = "";
    private String status = "";

    private String descPesquisaBoleto = "";
    private List<SelectItem> listaContas = new ArrayList();
    private int indexConta = 0;
    private final ConfiguracaoSocialBean csb = new ConfiguracaoSocialBean();

    private boolean pessoaJuridicaNaLista = false;
    private final ConfiguracaoFinanceiroBean cfb = new ConfiguracaoFinanceiroBean();
    private String motivoInativacao = "";

    private ControleAcessoBean cab = new ControleAcessoBean();
    private String referenciaPesquisa = "";

    private Socios socios;
    private DataObject linhaSelecionada = new DataObject();
    private String novoDesconto = "0,00";

    private boolean booAcrescimo = true;

    private List<DataObject> listaBoletosAbertos = new ArrayList();
    private List<DataObject> listaBoletosAbertosSelecionados = new ArrayList();

    private List<Movimento> listaMovimentosAnexo = new ArrayList();
    private List<Movimento> listaMovimentosAnexoSelecionados = new ArrayList();

    private String vencimentoNovoBoleto = "";

    private Movimento movimentoRemover = null;

    private DataObject objectVencimento = new DataObject(new Boleto(), "");
    private DataObject objectMensagem = new DataObject(new Boleto(), "");
    private boolean chkBoletosAtrasados = false;

    private String criterioReferencia = "";
    private String criterioLoteBaixa = "";

    private List<Movimento> listaMovimentoDoBoletoSelecionado = new ArrayList();

    @PostConstruct
    public void init() {
        Object cc = GenericaSessao.getObject("pessoaPesquisa");
        csb.init();
        cfb.init();

        cab = (ControleAcessoBean) GenericaSessao.getObject("controleAcessoBean");

        socios = new Socios();
    }

    @PreDestroy
    public void destroy() {
        //GenericaSessao.remove("movimentosReceberSocialBean");
        GenericaSessao.remove("usuarioAutenticado");
    }

    public void clickCriteriosDeBusca() {
        //criterioReferencia = ((referenciaPesquisa.isEmpty()) ? criterioReferencia : referenciaPesquisa);
    }

    public void limparCriteriosDeBusca() {
        criterioReferencia = "";
        criterioLoteBaixa = "";

        listaMovimento.clear();
    }

    public TransferenciaCaixa transferenciaCaixa(int id_fechamento_caixa_saida) {
        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        List<TransferenciaCaixa> l = db.transferenciaCaixa(id_fechamento_caixa_saida);
        return (l.isEmpty()) ? new TransferenciaCaixa() : l.get(0);
    }

    public void alterarVencimento() {
        String vencimentox = objectVencimento.getArgumento1().toString();
        if (DataHoje.menorData(vencimentox, DataHoje.data())) {
            GenericaMensagem.warn("Atençao", "Data de vencimento nao pode ser MENOR que data atual!");
            return;
        }

        Boleto boletox = (Boleto) objectVencimento.getArgumento0();
        Dao dao = new Dao();

        dao.openTransaction();

        boletox.setVencimento(vencimentox);

        if (!dao.update(boletox)) {
            GenericaMensagem.error("Error", "Nao foi possivel alterar vencimento do Boleto! Tente Novamente.");
            return;
        }

        dao.commit();

        GenericaMensagem.info("Sucesso", "Vencimento Alterado para " + vencimentox);

        objectVencimento = new DataObject(new Boleto(), "");
        loadBoletosAbertos();
    }

    public void selecionaVencimentoBoleto(Integer id_boleto) {
        Boleto boletox = (Boleto) new Dao().find(new Boleto(), id_boleto);

        if (boletox != null) {
            // BOLETO COM VENCIMENTO ANTERIOR , NOVO VENCIMENTO
            objectVencimento = new DataObject(boletox, "");
        }
    }

    public void alterarMensagem() {
        String mensagem = objectMensagem.getArgumento1().toString();
        Boleto boletox = (Boleto) objectMensagem.getArgumento0();
        Dao dao = new Dao();

        dao.openTransaction();

        boletox.setMensagem(mensagem);

        if (!dao.update(boletox)) {
            GenericaMensagem.error("Error", "Nao foi possivel alterar mensagem do Boleto! Tente Novamente.");
            return;
        }

        dao.commit();

        GenericaMensagem.info("Sucesso", "Mensagem Alterada para " + mensagem);

        objectMensagem = new DataObject(new Boleto(), "");
        loadBoletosAbertos();
    }

    public void selecionaMensagemBoleto(Integer id_boleto) {
        Boleto boletox = (Boleto) new Dao().find(new Boleto(), id_boleto);

        if (boletox != null) {
            // BOLETO COM MENSAGEM ANTERIOR , NOVA MENSAGEM
            objectMensagem = new DataObject(boletox, "");
        }
    }

    public void removerMovimento() {
        Dao dao = new Dao();

        dao.openTransaction();
        if (movimentoRemover == null) {
            for (Movimento m : listaMovimentoDoBoletoSelecionado) {
                m.setNrCtrBoleto("");
                m.setDocumento("");

                if (!dao.update(m)) {
                    GenericaMensagem.error("Erro", "Não foi possível atualizar Movimento, tente novamente!");
                    dao.rollback();
                    return;
                }
            }
        } else {
            movimentoRemover.setNrCtrBoleto("");
            movimentoRemover.setDocumento("");

            if (!dao.update(movimentoRemover)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Movimento, tente novamente!");
                dao.rollback();
                return;
            }
        }
        dao.commit();

        loadBoletosAbertos();
        loadMovimentosAnexo();
        movimentoRemover = null;
        listaMovimentoDoBoletoSelecionado.clear();
    }

    public void loadBoletosAbertos() {
        listaBoletosAbertos.clear();
        listaBoletosAbertosSelecionados.clear();

        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();

        List<Vector> result = db.listaBoletosAbertosAgrupado(pessoa.getId(), chkBoletosAtrasados);

        for (List linha : result) {

            listaBoletosAbertos.add(
                    new DataObject(
                            linha, // ARGUMENTO 0 || 0 - fin_boleto.id, 1 - fin_boleto.nr_ctr_boleto, sum(fin.movimento.nr_valor)
                            new Dao().find(new Boleto(), linha.get(0)), // ARGUMENTO 1 || Boleto 
                            db.listaMovimentosPorNrCtrBoleto(linha.get(1).toString()) // ARGUMENTO 2 || List<Movimento>
                    )
            );

        }
    }

    public void loadMovimentosAnexo() {
        listaMovimentosAnexo.clear();
        listaMovimentosAnexoSelecionados.clear();

        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();

        listaMovimentosAnexo = db.listaMovimentosAbertosAnexarAgrupado(pessoa.getId());
    }

    public void clickRemoverMovimentos(Movimento movimento) {
        if (movimento != null) {
            movimentoRemover = (Movimento) new Dao().rebind(movimento);
        } else {
            for (int i = 0; i < listaMovimentoDoBoletoSelecionado.size(); i++) {
                listaMovimentoDoBoletoSelecionado.set(i, (Movimento) new Dao().rebind(listaMovimentoDoBoletoSelecionado.get(i)));
            }
        }
    }

    public void clickAnexarMovimentos() {
        chkBoletosAtrasados = false;

        loadBoletosAbertos();
        loadMovimentosAnexo();

        vencimentoNovoBoleto = "";
    }

    public void anexarMovimentos() {
        if (listaBoletosAbertosSelecionados.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Selecione um Boleto para Anexar Movimentos");
            return;
        }

        if (listaBoletosAbertosSelecionados.size() > 1) {
            GenericaMensagem.warn("Atenção", "Apenas 1 Boleto pode ser selecionado para Anexar!");
            return;
        }

        if (listaMovimentosAnexoSelecionados.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Selecione ao menos 1 Movimento para Anexar!");
            return;
        }

        Dao dao = new Dao();

        dao.openTransaction();
        for (Movimento mov : listaMovimentosAnexoSelecionados) {
            //Movimento mov = (Movimento) dao.find(selecionados.getArgumento0());

            mov.setNrCtrBoleto(((Boleto) listaBoletosAbertosSelecionados.get(0).getArgumento1()).getNrCtrBoleto());
            mov.setDocumento(((Boleto) listaBoletosAbertosSelecionados.get(0).getArgumento1()).getBoletoComposto());

            if (!dao.update(mov)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Movimento, tente novamente!");
                return;
            }
        }

        dao.commit();

        GenericaMensagem.info("Sucesso", "Movimentos Anexados ao Boleto " + ((Boleto) listaBoletosAbertosSelecionados.get(0).getArgumento1()).getBoletoComposto());

        Object ob = dao.liveSingle("select func_boleto_vencimento_original()", true);

        if (ob == null || (!(Boolean) ((Vector) ob).get(0))) {
            GenericaMensagem.error("Erro", "Não foi possível atualizar vencimento original!");
        }

        loadBoletosAbertos();
        loadMovimentosAnexo();
    }

    public void criarBoletos() {
        if (listaMovimentosAnexoSelecionados.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Selecione ao menos 1 Movimento para Criar um novo Boleto!");
            return;
        }

        if (vencimentoNovoBoleto.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um VENCIMENTO para este novo Boleto!");
            return;
        }

        if (DataHoje.menorData(vencimentoNovoBoleto, DataHoje.data())) {
            GenericaMensagem.warn("Atenção", "VENCIMENTO não pode ser menor que Data de Hoje!");
            return;
        }

        FunctionsDao f = new FunctionsDao();

        if (f.gerarBoletoSocial(listaMovimentosAnexoSelecionados, vencimentoNovoBoleto)) {
            GenericaMensagem.info("Sucesso", "Boleto Criado para o vencimento " + vencimentoNovoBoleto);
            loadBoletosAbertos();
            loadMovimentosAnexo();
        } else {
            GenericaMensagem.error("Erro", "Não foi possível gerar Boleto!");
        }

    }

    public void imprimirBoletos(int id_boleto) {
        Boleto boletox = (Boleto) new Dao().find(new Boleto(), id_boleto);

        ImprimirBoleto ib = new ImprimirBoleto();
        ib.imprimirBoletoSocial(boletox, false);
        ib.visualizar(null);
    }

    public void cliqueCalculoAcrescimo(DataObject linha) {
        if (linha != null) {
            linhaSelecionada = linha;
            if (cab.verificaPermissao("calcularJurosSocial", 3)) {
                GenericaSessao.put("AutenticaUsuario", new AutenticaUsuario("calcularJurosSocial", 3, "formMovimentosReceber", "movimentosReceberSocialBean", "calculoAcrescimo"));
                return;
            }
            calculoAcrescimo();
        } else {
            if (cab.verificaPermissao("calcularJurosSocial", 3)) {
                GenericaSessao.put("AutenticaUsuario", new AutenticaUsuario("calcularJurosSocial", 3, "formMovimentosReceber", "movimentosReceberSocialBean", "calculoTodosAcrescimo"));
                return;
            }
            calculoTodosAcrescimo();
        }

        PF.update("formMovimentosReceber");
    }

    public void calculoTodosAcrescimo() {
        booAcrescimo = (booAcrescimo) ? false : true;
        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        for (DataObject linha : listaMovimento) {
            float[] valor = db.pesquisaValorAcrescimo(((Movimento) linha.getArgumento1()).getId());
            if (!booAcrescimo) {
                linha.setArgumento29(false);
                linha.setArgumento9(Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(linha.getArgumento9().toString()), valor[0])));
            } else {
                linha.setArgumento29(true);
                linha.setArgumento9(Moeda.converteR$Float(valor[1]));
            }
        }
        calculoDesconto();
    }

    public void calculoAcrescimo() {

        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        float[] valor = db.pesquisaValorAcrescimo(((Movimento) linhaSelecionada.getArgumento1()).getId());
        if ((Boolean) linhaSelecionada.getArgumento29()) {
            linhaSelecionada.setArgumento29(false);
            linhaSelecionada.setArgumento9(Moeda.converteR$Float(Moeda.subtracaoValores(Moeda.converteUS$(linhaSelecionada.getArgumento9().toString()), valor[0])));
        } else {
            linhaSelecionada.setArgumento29(true);
            linhaSelecionada.setArgumento9(Moeda.converteR$Float(valor[1]));
        }

        calculoDesconto();
    }

    public String cadastroPessoa(DataObject linha, Pessoa pessoax) {
        if (pessoax == null) {
            Movimento mov = (Movimento) linha.getArgumento1();
            pessoax = mov.getBeneficiario();
        }

        FisicaDB dbf = new FisicaDBToplink();
        Fisica f = dbf.pesquisaFisicaPorPessoa(pessoax.getId());

        if (f != null) {
            String retorno = ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pessoaFisica();

            FisicaBean fb = new FisicaBean();
            fb.editarFisica(f, true);
            GenericaSessao.put("fisicaBean", fb);
            return retorno;
        }

        JuridicaDB dbj = new JuridicaDBToplink();
        Juridica j = dbj.pesquisaJuridicaPorPessoa(pessoax.getId());

        if (j != null) {
            String retorno = (new ChamadaPaginaBean()).pessoaJuridica();
            JuridicaBean jb = new JuridicaBean();
            jb.editar(j, true);
            GenericaSessao.put("juridicaBean", jb);
            return retorno;
        }
        return null;
    }

    public void autorizarDesconto() {
        GenericaSessao.put("AutenticaUsuario", new AutenticaUsuario("dlg_desconto", "autorizaDescontos", 3));
    }

    public void adicionarDesconto() {
        desconto = novoDesconto;
        PF.closeDialog("dlg_autentica_usuario");
        PF.closeDialog("dlg_desconto");
        calculoDesconto();
        novoDesconto = "0,00";
        PF.update("formMovimentosReceber");
    }

    public void permissaoEcalculoDesconto() {
        // if TRUE não tem permissão
        if (cab.verificarPermissao("descontoTotalMensalidades", 1) || cab.verificarPermissao("descontoTotalMensalidades", 3)) {
            if (Moeda.converteUS$(desconto) > 5) {
                GenericaMensagem.warn("Atenção", "Usuário sem permissão para desconto maior que R$ 5,00");
                desconto = "0,00";
            }
        }

        calculoDesconto();
    }

    public void inativarMovimentos() {
        if (motivoInativacao.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Digite um motivo para exclusão!");
            return;
        } else if (motivoInativacao.length() < 6) {
            GenericaMensagem.warn("Atenção", "Motivo de exclusão inválido!");
            return;
        }

        List<Movimento> listam = new ArrayList();

        if (baixado()) {
            GenericaMensagem.warn("Atenção", "Boletos BAIXADOS não podem ser excluídos!");
            return;
        }

        if (fechadosCaixa()) {
            GenericaMensagem.warn("Atenção", "Boletos COM CAIXA FECHADO não podem ser estornados!");
            return;
        }

        if (acordados()) {
            GenericaMensagem.warn("Atenção", "Boletos do tipo ACORDO não podem ser excluídos!");
            return;
        }

        for (DataObject dh : listaMovimento) {
            if ((Boolean) dh.getArgumento0()) {
                int id_movimento = ((Movimento) dh.getArgumento1()).getId();
                Movimento mov = (Movimento) new Dao().find(new Movimento(), id_movimento);
                listam.add(mov);
            }
        }

        if (listam.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhum boletos foi selecionado!");
            return;
        }

        Dao dao = new Dao();
        dao.openTransaction();

        if (!GerarMovimento.inativarArrayMovimento(listam, motivoInativacao, dao).isEmpty()) {
            GenericaMensagem.error("Atenção", "Ocorreu um erro em uma das exclusões, verifique o log!");
            dao.rollback();
            return;
        } else {
            GenericaMensagem.info("Sucesso", "Boletos foram excluídos!");
        }

        listaMovimento.clear();
        dao.commit();
    }

    public String caixaOuBanco() {
        ControleAcessoBean cabx = new ControleAcessoBean();

        if (!cabx.getBotaoBaixaBanco()) {
            PF.openDialog("dlg_caixa_banco");
            return null;
        }

        return telaBaixa("caixa");
    }

    public void pessoaJuridicaNaListaxx() {
        JuridicaDB db = new JuridicaDBToplink();
        for (Pessoa p : listaPessoa) {
            Juridica j = db.pesquisaJuridicaPorPessoa(p.getId());

            if (j != null) {
                pessoaJuridicaNaLista = true;
                return;
            }
        }
        pessoaJuridicaNaLista = false;
    }

    public Guia pesquisaGuia(int id_lote) {
        MovimentoDB db = new MovimentoDBToplink();
        Guia gu = db.pesquisaGuias(id_lote);
        return gu;
    }

    public List<SelectItem> getListaContas() {
        if (listaContas.isEmpty()) {
            ServicoContaCobrancaDB servDB = new ServicoContaCobrancaDBToplink();
            List<ContaCobranca> result = servDB.listaContaCobrancaAtivoAssociativo();
            if (result.isEmpty()) {
                listaContas.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
                return listaContas;
            }
            int contador = 0;
            for (int i = 0; i < result.size(); i++) {
                // LAYOUT 2 = SINDICAL
                if (result.get(i).getLayout().getId() != 2) {
                    listaContas.add(
                            new SelectItem(
                                    contador,
                                    result.get(i).getApelido() + " - " + result.get(i).getCodCedente(), // CODCEDENTE NO CASO DE OUTRAS
                                    Integer.toString(result.get(i).getId())
                            )
                    );
                    contador++;
                }
            }
        }
        return listaContas;
    }

    public void pesquisaBoleto() {
        if (descPesquisaBoleto.isEmpty()) {
            if (pessoa.getId() != -1) {
                porPesquisa = "todos";
                listaMovimento.clear();
                getListaMovimento();
                socios = new Socios();
            }
            return;
        }

        try {
            //int numerox = Integer.valueOf(descPesquisaBoleto);
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            ContaCobranca contaCobranca = (ContaCobranca) new Dao().find(new ContaCobranca(), Integer.parseInt(((SelectItem) listaContas.get(indexConta)).getDescription()));
            Pessoa p = db.pesquisaPessoaPorBoleto(descPesquisaBoleto, contaCobranca.getId());
            listaPessoa.clear();
            pessoa = new Pessoa();
            socios = new Socios();

            if (p != null) {
                pessoa = p;
                listaPessoa.add(p);
                pessoaJuridicaNaListaxx();
            }
            porPesquisa = "todos";
            listaMovimento.clear();
            getListaMovimento();
        } catch (Exception e) {
            descPesquisaBoleto = "";
            GenericaMensagem.fatal("Atenção", "Digite um número de Boleto válido!");
        }
    }

    public void salvarRecibo(byte[] arquivo, Baixa baixa) {
        if (baixa.getCaixa() == null) {
            return;
        }

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/" + "Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));
        Diretorio.criar("Arquivos/recibo/" + baixa.getCaixa().getCaixa() + "/" + DataHoje.converteData(baixa.getDtBaixa()).replace("/", "-"));

        String path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + ".pdf";
        File file_arquivo = new File(path_arquivo);

        if (file_arquivo.exists()) {
            path_arquivo = caminho + "/" + String.valueOf(baixa.getUsuario().getId()) + "_" + String.valueOf(baixa.getId()) + "_(2).pdf";
        }

        try {
            File fl = new File(path_arquivo);
            try (FileOutputStream out = new FileOutputStream(fl)) {
                out.write(arquivo);
                out.flush();
            }
        } catch (IOException e) {
            System.out.println(e);
        }

    }

    public String recibo(Movimento mov) {
        ImprimirRecibo ir = new ImprimirRecibo();
        ir.recibo(mov.getId());
        return null;
    }

    public void encaminhamento(int id_lote) {
        Juridica sindicato = (Juridica) (new Dao()).find(new Juridica(), 1);
        PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 2);
        MovimentoDB db = new MovimentoDBToplink();

        Collection vetor = new ArrayList();

        Guia guia = pesquisaGuia(id_lote);

        if (guia.getId() == -1) {
            return;
        }

        SociosDB dbs = new SociosDBToplink();

        List<Movimento> list_movimentos = db.pesquisaGuia(guia);

        Socios socios_enc = dbs.pesquisaSocioPorPessoaAtivo(list_movimentos.get(0).getBeneficiario().getId());

        String str_usuario, str_nome, str_validade;

        PessoaEndereco pe_empresa = dbp.pesquisaEndPorPessoaTipo(guia.getPessoa().getId(), 5);
        String complemento = (pe_empresa.getComplemento().isEmpty()) ? "" : " ( " + pe_empresa.getComplemento() + " ) ";
        String endereco = pe_empresa.getEndereco().getLogradouro().getDescricao() + " "
                + pe_empresa.getEndereco().getDescricaoEndereco().getDescricao() + ", " + pe_empresa.getNumero() + " - " + complemento
                + pe_empresa.getEndereco().getBairro().getDescricao() + ", "
                + pe_empresa.getEndereco().getCidade().getCidade() + "  -  "
                + pe_empresa.getEndereco().getCidade().getUf();

        str_usuario = list_movimentos.get(0).getBaixa().getUsuario().getPessoa().getNome();

        str_nome = list_movimentos.get(0).getBeneficiario().getNome();

        List<JasperPrint> list_jasper = new ArrayList();
        Map<String, String> hash = new HashMap();
        try {
            String path = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads");
            String nameFile = "encaminhamento_" + DataHoje.livre(DataHoje.dataHoje(), "yyyyMMdd-HHmmss") + ".pdf";
            File fl_original = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ENCAMINHAMENTO.jasper"));
            File fl_menor = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/ENCAMINHAMENTO_MENOR.jasper"));
            File fl_jasper = null;

            if (fl_menor.exists()) {
                fl_jasper = fl_menor;
            } else {
                fl_jasper = fl_original;
            }

            JasperReport jasper = (JasperReport) JRLoader.loadObject(fl_jasper);

            for (Movimento movs : list_movimentos) {
                DataHoje dh = new DataHoje();
                if (movs.getServicos().isValidadeGuiasVigente()) {
                    str_validade = dh.ultimoDiaDoMes(guia.getLote().getEmissao());
                } else {
                    str_validade = dh.incrementarDias(movs.getServicos().getValidade(), guia.getLote().getEmissao());
                }

                if (hash.containsKey(str_validade)) {
                    hash.put(str_validade, hash.get(str_validade) + ", " + movs.getServicos().getDescricao());
                } else {
                    hash.put(str_validade, movs.getServicos().getDescricao());
                }
            }

            for (Map.Entry<String, String> entry : hash.entrySet()) {

                vetor.add(new ParametroEncaminhamento(
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
                        String.valueOf(guia.getId()), // GUIA
                        String.valueOf(guia.getPessoa().getId()), // CODIGO
                        guia.getSubGrupoConvenio().getGrupoConvenio().getDescricao(), // GRUPO
                        guia.getSubGrupoConvenio().getDescricao(), // SUB GRUPO
                        pe_empresa.getPessoa().getNome(), // EMPRESA CONVENIADA
                        endereco, // EMPRESA ENDERECO
                        pe_empresa.getPessoa().getTelefone1(), // EMPRESA TELEFONE
                        entry.getValue(),//str_servicos, // SERVICOS
                        guia.getLote().getEmissao(), // EMISSAO
                        entry.getKey(),//str_validade, // VALIDADE
                        str_usuario, // USUARIO
                        str_nome,
                        socios_enc.getParentesco().getParentesco(),
                        (socios_enc.getMatriculaSocios().getId() == -1) ? "" : String.valueOf(socios_enc.getMatriculaSocios().getId()),
                        socios_enc.getMatriculaSocios().getCategoria().getCategoria(),
                        guia.getSubGrupoConvenio().getObservacao()
                ));

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                JasperPrint print = JasperFillManager.fillReport(jasper, null, dtSource);
                list_jasper.add(print);

                vetor.clear();
            }

            JRPdfExporter exporter = new JRPdfExporter();
            
            exporter.setExporterInput(SimpleExporterInput.getInstance(list_jasper));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path + "/" + nameFile));

            SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

            configuration.setCreatingBatchModeBookmarks(true);

            exporter.setConfiguration(configuration);
            exporter.exportReport();

            File flx = new File(path);

            if (flx.exists()) {
                Download download = new Download(
                        nameFile,
                        path,
                        "application/pdf",
                        FacesContext.getCurrentInstance()
                );

                download.baixar();
                download.remover();
            }
//            byte[] arquivo = JasperExportManager.exportReportToPdf(print);
//
//            HttpServletResponse res = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//            res.setContentType("application/pdf");
//            res.setHeader("Content-disposition", "inline; filename=\"" + "Impressão de Encaminhamento" + ".pdf\"");
//            res.getOutputStream().write(arquivo);
//            res.getCharacterEncoding();
//
//            FacesContext.getCurrentInstance().responseComplete();
        } catch (JRException e) {
            e.getMessage();
        }
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
            if (((Boolean) listaMovimento.get(i).getArgumento0()) && listaMovimento.get(i).getArgumento26() != null) {
                return true;
            }
        }
        return false;
    }

    public boolean fechadosCaixa() {
        MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
        for (int i = 0; i < listaMovimento.size(); i++) {
            if (((Boolean) listaMovimento.get(i).getArgumento0())
                    && (((Movimento) listaMovimento.get(i).getArgumento1()).getBaixa() != null) && (((Movimento) listaMovimento.get(i).getArgumento1()).getBaixa().getFechamentoCaixa() != null)) {
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

    public String refazerMovimentos() {
        if (listaMovimento.isEmpty()) {
            msgConfirma = "Não existem Movimentos para serem refeitos!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        MovimentoDB db = new MovimentoDBToplink();
        int qnt = 0;

        List<Movimento> lm = new ArrayList();

        for (DataObject listaMovimento1 : listaMovimento) {
            if ((Boolean) listaMovimento1.getArgumento0()) {
                qnt++;
                lm.add((Movimento) listaMovimento1.getArgumento1());
            }
        }

        if (qnt == 0) {
            msgConfirma = "Nenhum Movimentos selecionado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (baixado()) {
            msgConfirma = "Existem Movimentos pagos, não podem ser refeitos!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        ServicoPessoaDao spd = new ServicoPessoaDao();
        for (Movimento m : lm) {
            ServicoPessoa sp = spd.pesquisaServicoPessoa(m.getBeneficiario().getId(), m.getServicos().getId(), true);

            if (sp == null) {
                msgConfirma = "O SERVIÇO " + m.getServicos().getDescricao() + " para a PESSOA " + m.getBeneficiario().getNome() + " não pode ser refeito!";
                GenericaMensagem.warn("Atenção", msgConfirma);
                return null;
            }
        }
//      PERMISSÃO DE ACESSO
//        ControleAcessoBean cab = new ControleAcessoBean();
//        Usuario user = (Usuario) GenericaSessao.getObject("sessaoUsuario");
//        if (mov.getBaixa().getUsuario().getId() != user.getId()) {
//            if (cab.getBotaoEstornarMensalidadesOutrosUsuarios()) {
//                GenericaMensagem.error("Atenção", "Você não tem permissão para estornar esse movimento!");
//                return null;
//            }
//        }
        if (!GerarMovimento.refazerMovimentos(lm)) {
            msgConfirma = "Não foi possível refazer movimentos";
            GenericaMensagem.error("Erro", msgConfirma);
            return null;
        }

        msgConfirma = "Boletos atualizados!";
        GenericaMensagem.info("Sucesso", msgConfirma);

        listaMovimento.clear();
        return null;
    }

    public String estornarBaixa() {
        if (listaMovimento.isEmpty()) {
            msgConfirma = "Não existem boletos para serem estornados!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        MovimentoDB db = new MovimentoDBToplink();
        int qnt = 0;
        Movimento mov = null;

        for (int i = 0; i < listaMovimento.size(); i++) {
            if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                qnt++;
                mov = (Movimento) listaMovimento.get(i).getArgumento1();
            }
        }

        if (qnt == 0) {
            msgConfirma = "Nenhum Movimento selecionado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (qnt > 1) {
            msgConfirma = "Mais de um movimento foi selecionado!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (!baixado()) {
            msgConfirma = "Existem boletos que não foram pagos para estornar!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (fechadosCaixa()) {
            GenericaMensagem.warn("Atenção", "Boletos COM CAIXA FECHADO não podem ser estornados!");
            return null;
        }

        Usuario user = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        if (mov.getBaixa().getUsuario().getId() != user.getId()) {
            if (cab.getBotaoEstornarMensalidadesOutrosUsuarios()) {
                GenericaMensagem.error("Atenção", "Você não tem permissão para estornar esse movimento!");
                return null;
            }
        }

        boolean est = true;

        if (!mov.isAtivo()) {
            msgConfirma = "Boleto ID: " + mov.getId() + " esta inativo, não é possivel concluir estorno!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (mov.getLote().getRotina() != null && mov.getLote().getRotina().getId() == 132) {
            mov.setAtivo(false);
        }

        if (!GerarMovimento.estornarMovimento(mov)) {
            est = false;
        }

        if (!est) {
            msgConfirma = "Ocorreu erros ao estornar boletos, verifique o log!";
            GenericaMensagem.warn("Erro", msgConfirma);
        } else {
            msgConfirma = "Boletos estornados com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
        }
        listaMovimento.clear();
        chkSeleciona = true;
        return null;
    }

    public String telaBaixa(String caixa_banco) {
        List lista = new ArrayList();
        MovimentoDB db = new MovimentoDBToplink();
        Movimento movimento = new Movimento();
        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");

        if (macFilial == null) {
            msgConfirma = "Não existe filial na sessão!";
            GenericaMensagem.warn("Erro", msgConfirma);
            PF.closeDialog("dlg_caixa_banco");
            PF.update("formMovimentosReceber");
            return null;
        }

        //if (!cfb.getConfiguracaoFinanceiro().isCaixaOperador()) {
        if (!macFilial.isCaixaOperador()) {
            if (macFilial.getCaixa() == null) {
                msgConfirma = "Configurar Caixa nesta estação de trabalho!";
                GenericaMensagem.warn("Erro", msgConfirma);
                PF.closeDialog("dlg_caixa_banco");
                PF.update("formMovimentosReceber");
                return null;
            }
        } else {
            FinanceiroDB dbf = new FinanceiroDBToplink();
            Caixa caixax = dbf.pesquisaCaixaUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId());

            if (caixax == null) {
                msgConfirma = "Configurar Caixa para este Operador!";
                GenericaMensagem.warn("Erro", msgConfirma);
                PF.closeDialog("dlg_caixa_banco");
                PF.update("formMovimentosReceber");
                return null;
            }
        }

        if (baixado()) {
            msgConfirma = "Existem boletos baixados na lista!";
            GenericaMensagem.warn("Erro", msgConfirma);
            PF.closeDialog("dlg_caixa_banco");
            PF.update("formMovimentosReceber");
            return null;
        }

        // ROGÉRIO PEDIU PARA BAIXAR BOLETOS COM VALOR ZERADO -- chamado 540
//        if (semValor()) {
//            msgConfirma = "Boletos sem valor não podem ser Baixados!";
//            GenericaMensagem.warn("Erro", msgConfirma);
//            PF.closeDialog("dlg_caixa_banco");
//            PF.update("formMovimentosReceber");            
//            return null;
//        }
        if (!listaMovimento.isEmpty()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0()) {
                    movimento = (Movimento) listaMovimento.get(i).getArgumento1();

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$(listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$(listaMovimento.get(i).getArgumento21().toString()));
                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));

                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);

                GenericaSessao.put("caixa_banco", caixa_banco);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
            } else {
                msgConfirma = "Nenhum boleto foi selecionado";
                GenericaMensagem.warn("Atenção", msgConfirma);
                PF.closeDialog("dlg_caixa_banco");
                PF.update("formMovimentosReceber");
            }
        } else {
            msgConfirma = "Lista vazia!";
            GenericaMensagem.warn("Atenção", msgConfirma);
            PF.closeDialog("dlg_caixa_banco");
            PF.update("formMovimentosReceber");
        }
        return null;
    }

    public String telaMovimento(Movimento mov) {
        List lista = new ArrayList();
        //MovimentoDB db = new MovimentoDBToplink();
        //Movimento movimento = new Movimento();

//                    movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
        //movimento = db.pesquisaCodigo(id_movimento);
//                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
//                    movimento.setJuros(Moeda.converteUS$( listaMovimento.get(i).getArgumento20().toString()));
//                    movimento.setCorrecao(Moeda.converteUS$( listaMovimento.get(i).getArgumento21().toString()));
//
//                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));
//
//                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
        // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
//                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
        lista.add(mov);
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
                    movimento = (Movimento) listaMovimento.get(i).getArgumento1();

                    movimento.setMulta(Moeda.converteUS$(listaMovimento.get(i).getArgumento19().toString()));
                    movimento.setJuros(Moeda.converteUS$(listaMovimento.get(i).getArgumento20().toString()));
                    movimento.setCorrecao(Moeda.converteUS$(listaMovimento.get(i).getArgumento21().toString()));

                    movimento.setDesconto(Moeda.converteUS$(listaMovimento.get(i).getArgumento8().toString()));

                    movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));

                    // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
                    movimento.setValorBaixa(Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                    lista.add(movimento);
                }
            }
            if (!lista.isEmpty()) {
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).acordoSocial();
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
        float calc = Moeda.substituiVirgulaFloat(getValorPraDesconto()); // VALOR PARA DESCONTO TEM QUE SER A SOMA DE TODOS OS VALORES CHECADOS (MENOS) IF SEM ACRESCIMO
        float calculo_total_aberto = 0;

        if (Moeda.converteUS$(desconto) > calc) {
            desconto = String.valueOf(calc);
        }

        descPorcento = Moeda.multiplicarValores(Moeda.divisaoValores(Moeda.converteUS$(desconto), calc), 100);
        List<DataObject> linha = new ArrayList();

        for (int i = 0; i < listaMovimento.size(); i++) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            float[] valorx = db.pesquisaValorAcrescimo(((Movimento) listaMovimento.get(i).getArgumento1()).getId());

            if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                float calculo = 0;
                if ((Boolean) listaMovimento.get(i).getArgumento29()) {
                    float valox = valorx[1];
                    desc = Moeda.divisaoValores(Moeda.multiplicarValores(valox, descPorcento), 100);
                    listaMovimento.get(i).setArgumento8(Moeda.converteR$(String.valueOf(desc)));
                    calculo = Moeda.converteFloatR$Float(Moeda.subtracaoValores(valox, desc));
                    listaMovimento.get(i).setArgumento9(Moeda.converteR$(String.valueOf(calculo)));

                    linha.add(listaMovimento.get(i));
                } else {
                    float valox = Moeda.subtracaoValores(valorx[1], valorx[0]);
                    desc = Moeda.divisaoValores(Moeda.multiplicarValores(valox, descPorcento), 100);
                    calculo = Moeda.converteFloatR$Float(Moeda.subtracaoValores(valox, desc));
                    listaMovimento.get(i).setArgumento9(Moeda.converteR$(String.valueOf(calculo)));
                }
                calculo_total_aberto = Moeda.somaValores(calculo_total_aberto, calculo);
            } else {
                listaMovimento.get(i).setArgumento8("0,00");
                if ((Boolean) listaMovimento.get(i).getArgumento29()) {
                    listaMovimento.get(i).setArgumento9(Moeda.converteR$Float(valorx[1]));
                } else {
                    listaMovimento.get(i).setArgumento9(Moeda.converteR$Float(Moeda.subtracaoValores(valorx[1], valorx[0])));
                }
            }
        }

        // CORRIGE OS VALORES QUE NÃO CORRESPONDE OS CENTAVOS APÓS DESCONTO
        // ex. VALOR 27,00 DESCONTO 20,00 VALOR CALCULADO 6,99
        // ADICIONA 0,01 CENTAVO NO ULTIMO MOVIMENTO SELECIONADO
        float calcx = Moeda.subtracaoValores(calc, Moeda.converteUS$(desconto));
        if (calcx != Moeda.converteFloatR$Float(calculo_total_aberto)) {
            if (calculo_total_aberto > calcx) {
                int quantidade = Integer.valueOf(Moeda.limparVirgula(Moeda.converteR$Float(Moeda.subtracaoValores(calculo_total_aberto, calcx))));
                for (int i = 0; i < quantidade; i++) {
                    // SOMA O DESCONTO
                    float vld = Moeda.converteUS$(linha.get(i).getArgumento8().toString());
                    vld = Moeda.somaValores(vld, Float.parseFloat("0.01"));
                    linha.get(i).setArgumento8(Moeda.converteR$Float(vld));

                    // SUBTRAI DO VALOR CALCULADO
                    float vlc = Moeda.converteUS$(linha.get(i).getArgumento9().toString());
                    vlc = Moeda.subtracaoValores(vlc, Float.parseFloat("0.01"));
                    linha.get(i).setArgumento9(Moeda.converteR$Float(vlc));
                }
            } else {
                int quantidade = Integer.valueOf(Moeda.limparVirgula(Moeda.converteR$Float(Moeda.subtracaoValores(calcx, calculo_total_aberto))));
                for (int i = 0; i < quantidade; i++) {
                    // SUBTRAI DO DESCONTO
                    float vld = Moeda.converteUS$(linha.get(i).getArgumento8().toString());
                    vld = Moeda.subtracaoValores(vld, Float.parseFloat("0.01"));
                    linha.get(i).setArgumento8(Moeda.converteR$Float(vld));

                    // SOMA O VALOR CALCULADO
                    float vlc = Moeda.converteUS$(linha.get(i).getArgumento9().toString());
                    vlc = Moeda.somaValores(vlc, Float.parseFloat("0.01"));
                    linha.get(i).setArgumento9(Moeda.converteR$Float(vlc));
                }
            }
        }
        // ------------------------------------------------------------------
    }

    public void atualizarStatus() {
        listaMovimento.clear();
    }

    public void listenerPesquisa() {
        listaMovimento.clear();
    }

    public String converteData(Date data) {
        return DataHoje.converteData(data);
    }

    public String converteValor(String valor) {
        return Moeda.converteR$(valor);
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
            return "0,00";
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
            return "0,00";
        }
    }

    public String getValorPraDesconto() {
        if (!listaMovimento.isEmpty()) {
            float soma = 0;
            for (int i = 0; i < listaMovimento.size(); i++) {
                if ((Boolean) listaMovimento.get(i).getArgumento0() && Moeda.converteUS$(listaMovimento.get(i).getArgumento11().toString()) == 0.0) {
                    MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
                    float[] valorx = db.pesquisaValorAcrescimo(((Movimento) listaMovimento.get(i).getArgumento1()).getId());

                    if ((Boolean) listaMovimento.get(i).getArgumento29()) {
                        soma = Moeda.somaValores(soma, valorx[1]);
                    } else {
                        soma = Moeda.somaValores(soma, Moeda.subtracaoValores(valorx[1], valorx[0]));
                    }
                }
            }
            return Moeda.converteR$Float(soma);
        } else {
            return "0,00";
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
            return "0,00";
        }
    }

    public void complementoPessoa(DataObject linha) {
        // COMENTARIO PARA ORDEM QUE VEM DA QUERY
        //titular = (String) linha.getArgumento15(); // 13 - TITULAR
        tipo = (String) linha.getArgumento3(); // 1 - TIPO SERVIÇO
        referencia = (String) linha.getArgumento4(); // 2 - REFERENCIA
        id_baixa = (linha.getArgumento26() == null) ? "" : linha.getArgumento26().toString(); // 23 - ID_BAIXA

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

        if (!lista.isEmpty()) {
            titular = lista.get(0).get(0).toString(); // TITULAR
            matricula = lista.get(0).get(1).toString(); // MATRICULA
            categoria = lista.get(0).get(2).toString(); // CATEGORIA
            grupo = lista.get(0).get(3).toString(); // GRUPO
            status = lista.get(0).get(4).toString(); // CASE
        } else {
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
        for (DataObject listaMovimento1 : listaMovimento) {
            listaMovimento1.setArgumento0(chkSeleciona);
        }

        calculoDesconto();
    }

    public List<DataObject> getListaMovimento() {
        if (listaMovimento.isEmpty() && !listaPessoa.isEmpty()) {
            MovimentosReceberSocialDB db = new MovimentosReceberSocialDBToplink();
            String id_pessoa = "", id_responsavel = "";

            FisicaDB dbf = new FisicaDBToplink();
            JuridicaDB dbj = new JuridicaDBToplink();
            FunctionsDB dbfunc = new FunctionsDao();

            List<Pessoa> listaPessoaQry = new ArrayList();
            for (Pessoa pe : listaPessoa) {
                // PESSOA FISICA -----
                Fisica fi = dbf.pesquisaFisicaPorPessoa(pe.getId());
                if (fi != null) {
                    // PESQUISA RESPONSAVEL DA PESSOA
                    Pessoa t = dbfunc.titularDaPessoa(pe.getId());
                    if (t != null) {
                        listaPessoaQry.add(t);
                    }
                    continue;
                }

                // PESSOA JURIDICA ---
                Juridica ju = dbj.pesquisaJuridicaPorPessoa(pe.getId());
                if (ju != null) {
                    listaPessoaQry.add(ju.getPessoa());
                }
            }

            for (int i = 0; i < listaPessoa.size(); i++) {
                if (id_pessoa.length() > 0 && i != listaPessoa.size()) {
                    id_pessoa = id_pessoa + ",";
                }
                id_pessoa = id_pessoa + String.valueOf(listaPessoa.get(i).getId());
            }

            for (int i = 0; i < listaPessoaQry.size(); i++) {
                if (id_responsavel.length() > 0 && i != listaPessoaQry.size()) {
                    id_responsavel = id_responsavel + ",";
                }
                id_responsavel = id_responsavel + String.valueOf(listaPessoaQry.get(i).getId());
            }

            List<Vector> lista = null;

            if (dbf.pesquisaFisicaPorPessoa(pessoa.getId()) != null) {
                lista = db.pesquisaListaMovimentos(id_pessoa, id_responsavel, porPesquisa, criterioReferencia, "fisica", criterioLoteBaixa);
            } else {
                lista = db.pesquisaListaMovimentos(id_pessoa, id_responsavel, porPesquisa, criterioReferencia, "juridica", criterioLoteBaixa);
            }

            boolean chk = false, disabled = false;
            String dataBaixa = "";

            for (int i = 0; i < lista.size(); i++) {
                if (lista.get(i).get(8) != null) {
                    dataBaixa = DataHoje.converteData((Date) lista.get(i).get(8));
                } else {
                    dataBaixa = "";
                }
                //soma = Moeda.somaValores(Moeda.converteR$(lista.get(i).get(5).toString()), Moeda.converteUS$(listaMovimento.get(i).getArgumento9().toString()));
                // DATA DE HOJE MENOR OU IGUAL A DATA DE VENCIMENTO
                if ((DataHoje.converteDataParaInteger(DataHoje.converteData((Date) lista.get(i).get(3)))
                        <= DataHoje.converteDataParaInteger(DataHoje.data())
                        || DataHoje.converteDataParaReferencia(DataHoje.converteData((Date) lista.get(i).get(3))).equals(DataHoje.converteDataParaReferencia(DataHoje.data())))
                        && dataBaixa.isEmpty()) {
                    chk = true;
                } else {
                    chk = false;
                }

                // DATA DE HOJE MENOR QUE DATA DE VENCIMENTO
                if (DataHoje.converteDataParaInteger(DataHoje.converteData((Date) lista.get(i).get(3)))
                        < DataHoje.converteDataParaInteger(DataHoje.data())
                        && dataBaixa.isEmpty()) {

                    if (csb.getConfiguracaoSocial() == null) {
                        csb.init();
                    }
                    if (csb.getConfiguracaoSocial().isRecebeAtrasado()) {
                        disabled = true;
                    } else {
                        disabled = false;
                    }

                } else {
                    disabled = false;
                }

                listaMovimento.add(new DataObject(
                        chk, // ARG 0
                        (Movimento) new Dao().find(new Movimento(), lista.get(i).get(14)), // ARG 1 Movimento
                        lista.get(i).get(0), // ARG 2 SERVICO
                        lista.get(i).get(1), // ARG 3 TIPO_SERVICO
                        lista.get(i).get(2), // ARG 4 REFERENCIA
                        DataHoje.converteData((Date) lista.get(i).get(3)), // ARG 5 VENCIMENTO
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
                        DataHoje.converteData((Date) lista.get(i).get(16)), // ARG 16 CRIACAO
                        lista.get(i).get(17), // ARG 17 BOLETO
                        lista.get(i).get(18), // ARG 18 DIAS DE ATRASO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(19))), // ARG 29 MULTA
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(20))), // ARG 20 JUROS
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(21))), // ARG 21 CORRECAO
                        getConverteNullString(lista.get(i).get(22)), // ARG 22 CAIXA 
                        lista.get(i).get(24), // ARG 23 DOCUMENTO
                        Moeda.converteR$(getConverteNullString(lista.get(i).get(7))), // ARG 24 VALOR CALCULADO ORIGINAL
                        disabled,
                        //lista.get(i).get(18), // ARG 26 ID_BAIXA
                        lista.get(i).get(23), // ARG 26 ID_BAIXA
                        lista.get(i).get(15), // ARG 27 ID_LOTE
                        (!descPesquisaBoleto.isEmpty() && descPesquisaBoleto.equals(lista.get(i).get(17))) ? "tblListaBoleto" : "", // BOLETO PESQUISADO -- ARG 28
                        true, // ARG 29 JUROS
                        lista.get(i).get(25) // ARG 30 NOME TITULAR
                )
                );
            }

            calculoDesconto();
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
            desconto = "0,00";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0,00";
        }
        this.desconto = Moeda.converteR$(desconto);
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
        if (GenericaSessao.exists("pessoaPesquisa")) {
            if (!addMais) {
                pessoa = new Pessoa();
                pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa");

                SociosDB dbs = new SociosDBToplink();
                socios = dbs.pesquisaSocioPorPessoaAtivo(pessoa.getId());

                listaPessoa.clear();

                listaPessoa.add(pessoa);
                listaMovimento.clear();
            } else {
                listaPessoa.add((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
                listaMovimento.clear();
                addMais = false;
            }
            calculoDesconto();
            pessoaJuridicaNaListaxx();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
            booAcrescimo = true;
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

    public String getDescPesquisaBoleto() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            descPesquisaBoleto = "";
        }
        return descPesquisaBoleto;
    }

    public void setDescPesquisaBoleto(String descPesquisaBoleto) {
        this.descPesquisaBoleto = descPesquisaBoleto;
    }

    public void setListaContas(List<SelectItem> listaContas) {
        this.listaContas = listaContas;
    }

    public int getIndexConta() {
        return indexConta;
    }

    public void setIndexConta(int indexConta) {
        this.indexConta = indexConta;
    }

    public boolean isPessoaJuridicaNaLista() {
        return pessoaJuridicaNaLista;
    }

    public void setPessoaJuridicaNaLista(boolean pessoaJuridicaNaLista) {
        this.pessoaJuridicaNaLista = pessoaJuridicaNaLista;
    }

    public String getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(String motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }

    public String getReferenciaPesquisa() {
        return referenciaPesquisa;
    }

    public void setReferenciaPesquisa(String referenciaPesquisa) {
        this.referenciaPesquisa = referenciaPesquisa;
    }

    public ControleAcessoBean getCab() {
        return cab;
    }

    public void setCab(ControleAcessoBean cab) {
        this.cab = cab;
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public DataObject getLinhaSelecionada() {
        return linhaSelecionada;
    }

    public void setLinhaSelecionada(DataObject linhaSelecionada) {
        this.linhaSelecionada = linhaSelecionada;
    }

    public String getNovoDesconto() {
        if (novoDesconto.isEmpty()) {
            novoDesconto = "0,00";
        }
        return Moeda.converteR$(novoDesconto);
    }

    public void setNovoDesconto(String novoDesconto) {
        if (novoDesconto.isEmpty()) {
            novoDesconto = "0,00";
        }
        this.novoDesconto = Moeda.converteR$(novoDesconto);
    }

    public boolean isBooAcrescimo() {
        return booAcrescimo;
    }

    public void setBooAcrescimo(boolean booAcrescimo) {
        this.booAcrescimo = booAcrescimo;
    }

    public List<DataObject> getListaBoletosAbertos() {
        return listaBoletosAbertos;
    }

    public void setListaBoletosAbertos(List<DataObject> listaBoletosAbertos) {
        this.listaBoletosAbertos = listaBoletosAbertos;
    }

    public List<DataObject> getListaBoletosAbertosSelecionados() {
        return listaBoletosAbertosSelecionados;
    }

    public void setListaBoletosAbertosSelecionados(List<DataObject> listaBoletosAbertosSelecionados) {
        this.listaBoletosAbertosSelecionados = listaBoletosAbertosSelecionados;
    }

//    public List<DataObject> getListaMovimentosAnexo() {
//        return listaMovimentosAnexo;
//    }
//
//    public void setListaMovimentosAnexo(List<DataObject> listaMovimentosAnexo) {
//        this.listaMovimentosAnexo = listaMovimentosAnexo;
//    }
//
//    public List<DataObject> getListaMovimentosAnexoSelecionados() {
//        return listaMovimentosAnexoSelecionados;
//    }
//
//    public void setListaMovimentosAnexoSelecionados(List<DataObject> listaMovimentosAnexoSelecionados) {
//        this.listaMovimentosAnexoSelecionados = listaMovimentosAnexoSelecionados;
//    }
    public List<Movimento> getListaMovimentosAnexo() {
        return listaMovimentosAnexo;
    }

    public void setListaMovimentosAnexo(List<Movimento> listaMovimentosAnexo) {
        this.listaMovimentosAnexo = listaMovimentosAnexo;
    }

    public List<Movimento> getListaMovimentosAnexoSelecionados() {
        return listaMovimentosAnexoSelecionados;
    }

    public void setListaMovimentosAnexoSelecionados(List<Movimento> listaMovimentosAnexoSelecionados) {
        this.listaMovimentosAnexoSelecionados = listaMovimentosAnexoSelecionados;
    }

    public String getVencimentoNovoBoleto() {
        return vencimentoNovoBoleto;
    }

    public void setVencimentoNovoBoleto(String vencimentoNovoBoleto) {
        this.vencimentoNovoBoleto = vencimentoNovoBoleto;
    }

    public Movimento getMovimentoRemover() {
        return movimentoRemover;
    }

    public void setMovimentoRemover(Movimento movimentoRemover) {
        this.movimentoRemover = movimentoRemover;
    }

    public DataObject getObjectVencimento() {
        return objectVencimento;
    }

    public void setObjectVencimento(DataObject objectVencimento) {
        this.objectVencimento = objectVencimento;
    }

    public boolean isChkBoletosAtrasados() {
        return chkBoletosAtrasados;
    }

    public void setChkBoletosAtrasados(boolean chkBoletosAtrasados) {
        this.chkBoletosAtrasados = chkBoletosAtrasados;
    }

    public String getCriterioReferencia() {
        return criterioReferencia;
    }

    public void setCriterioReferencia(String criterioReferencia) {
        this.criterioReferencia = criterioReferencia;
    }

    public String getCriterioLoteBaixa() {
        return criterioLoteBaixa;
    }

    public void setCriterioLoteBaixa(String criterioLoteBaixa) {
        this.criterioLoteBaixa = criterioLoteBaixa;
    }

    public List<Movimento> getListaMovimentoDoBoletoSelecionado() {
        return listaMovimentoDoBoletoSelecionado;
    }

    public void setListaMovimentoDoBoletoSelecionado(List<Movimento> listaMovimentoDoBoletoSelecionado) {
        this.listaMovimentoDoBoletoSelecionado = listaMovimentoDoBoletoSelecionado;
    }

    public DataObject getObjectMensagem() {
        return objectMensagem;
    }

    public void setObjectMensagem(DataObject objectMensagem) {
        this.objectMensagem = objectMensagem;
    }

}
