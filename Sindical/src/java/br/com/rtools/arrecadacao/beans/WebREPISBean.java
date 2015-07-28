package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CertidaoDisponivel;
import br.com.rtools.arrecadacao.CertidaoMensagem;
import br.com.rtools.arrecadacao.CertidaoTipo;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.RepisMovimento;
import br.com.rtools.arrecadacao.RepisStatus;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.endereco.dao.CidadeDao;
import br.com.rtools.homologacao.db.HomologacaoDB;
import br.com.rtools.homologacao.db.HomologacaoDBToplink;
import br.com.rtools.impressao.ParametroCertificado;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
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
public class WebREPISBean implements Serializable {

    private Pessoa pessoa = new Pessoa();
    private Endereco endereco = new Endereco();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Pessoa pessoaContribuinte = new Pessoa();
    private Pessoa pessoaContabilidade = new Pessoa();
    private Pessoa pessoaSolicitante = new Pessoa();
    private Pessoa escritorio = new Pessoa();
    private List<SelectItem> listComboPessoa = new ArrayList();
    private List<SelectItem> listComboRepisStatus = new ArrayList();
    private List<SelectItem> listComboCertidaoDisponivel = new ArrayList();
    private List<SelectItem> listaTipoCertidao = new ArrayList();
    private List<SelectItem> listaCidade = new ArrayList();
    private List<RepisMovimento> listRepisMovimento = new ArrayList();
    private List<RepisMovimento> listRepisMovimentoPatronal = new ArrayList();
    private List<RepisMovimento> listRepisMovimentoPatronalSelecionado = new ArrayList();
    private int idPessoa = 0;
    private int idRepisStatus = 0;
    private int indexCertidaoDisponivel = 0;
    private int indexCertidaoTipo = 0;
    private int indexCidade = 0;
    private boolean renderContabil = false;
    private boolean renderEmpresa = false;
    private boolean showProtocolo = false;
    private RepisMovimento repisMovimento = new RepisMovimento();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String tipoPesquisa = "status";
    private String descricao = "";
    private List listArquivosEnviados = new ArrayList();
    private List<SelectItem> listaStatus = new ArrayList();
    private int indexStatus = 1;
    private String valueLenght = "15";
    private String contato = "";

    public WebREPISBean() {
        UsuarioDB db = new UsuarioDBToplink();
        getPessoa();
        pessoaContribuinte = db.ValidaUsuarioContribuinteWeb(pessoa.getId());
        pessoaContabilidade = db.ValidaUsuarioContabilidadeWeb(pessoa.getId());
        if (pessoaContribuinte != null && pessoaContabilidade != null) {
            renderEmpresa = false;
            renderContabil = true;
        } else if (pessoaContribuinte != null) {
            renderEmpresa = true;
            renderContabil = false;
        } else if (pessoaContabilidade != null) {
            renderEmpresa = false;
            renderContabil = true;
        } else {
            renderEmpresa = false;
            renderContabil = false;
        }
    }

    public PessoaEndereco enderecoPessoa(int id_pessoa) {
        PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
        PessoaEndereco endereco_pessoa = dbe.pesquisaEndPorPessoaTipo(id_pessoa, 5);
        return endereco_pessoa;
    }

    public String retornaCor(RepisMovimento rm) {
        if (DataHoje.igualdadeData(rm.getPessoa().getCriacao(), rm.getDataEmissaoString()) && rm.getRepisStatus().getId() == 1) {
            return "tblColorx";
        }
        return "";
    }

    public void alterValueLenght(String value) {
        listRepisMovimentoPatronal.clear();
        listRepisMovimentoPatronalSelecionado.clear();

        valueLenght = value;

        pesquisar();
    }

    public String refresh() {
        return "webLiberacaoREPIS";
    }

    public void liberarListaSolicitacao() {
        DaoInterface di = new Dao();
        di.openTransaction();

        for (RepisMovimento listRepis : listRepisMovimentoPatronalSelecionado) {
            RepisMovimento rm = (RepisMovimento) di.find(new RepisMovimento(), listRepis.getId());

            RepisStatus rs = (RepisStatus) di.find(new RepisStatus(), Integer.parseInt(listComboRepisStatus.get(idRepisStatus).getDescription()));
            rm.setRepisStatus(rs);
            if (rs.getId() == 2 || rs.getId() == 3 || rs.getId() == 4) {
                rm.setDataResposta(DataHoje.dataHoje());
            } else {
                rm.setDataResposta(null);
            }

            if (!di.update(rm)) {
                di.rollback();
                GenericaMensagem.error("Erro", "Não foi possível atualizar STATUS, tente novamente!");
                return;
            }
        }
        GenericaMensagem.info("Sucesso", "Registros Atualizados");
        di.commit();

        listRepisMovimentoPatronal.clear();
        listRepisMovimentoPatronalSelecionado.clear();
    }

    public void pesquisar() {
        WebREPISDB db = new WebREPISDBToplink();
        listRepisMovimentoPatronal.clear();
        listRepisMovimentoPatronalSelecionado.clear();
        Patronal patro = db.pesquisaPatronalPorPessoa(pessoa.getId());

        switch (tipoPesquisa) {
            case "tipo":
                listRepisMovimentoPatronal = db.pesquisarListaLiberacao("tipo", getListaTipoCertidao().get(indexCertidaoTipo).getDescription(), patro.getId(), valueLenght);
                break;
            case "status":
                listRepisMovimentoPatronal = db.pesquisarListaLiberacao("status", getListaStatus().get(indexStatus).getDescription(), patro.getId(), valueLenght);
                break;
            case "cidade":
                listRepisMovimentoPatronal = db.pesquisarListaLiberacao("cidade", getListaCidade().get(indexCidade).getDescription(), patro.getId(), valueLenght);
                break;
            default:
                listRepisMovimentoPatronal = db.pesquisarListaLiberacao(tipoPesquisa, descricao, patro.getId(), valueLenght);
                break;
        }
    }

    public String pesquisarPorSolicitante() {
        WebREPISDB db = new WebREPISDBToplink();
        listRepisMovimento.clear();
        Patronal patro = db.pesquisaPatronalPorPessoa(pessoa.getId());
        if (renderEmpresa) {
            listRepisMovimento = db.pesquisarListaSolicitacao(tipoPesquisa, descricao, pessoa.getId(), -1);
        } else {
            listRepisMovimento = db.pesquisarListaSolicitacao(tipoPesquisa, descricao, -1, pessoa.getId());
        }

        return null;
    }

    public void limpar() {
        repisMovimento = new RepisMovimento();
        showProtocolo = false;
        pessoaSolicitante = new Pessoa();
        //idPessoa = 0;
        listRepisMovimento.clear();
    }

    public String limparRepisLiberacao() {
        repisMovimento = new RepisMovimento();
        listRepisMovimentoPatronal.clear();
        return "webLiberacaoREPIS";
    }

    public RepisMovimento getRepisMovimento() {
        return repisMovimento;
    }

    public void setRepisMovimento(RepisMovimento repisMovimento) {
        this.repisMovimento = repisMovimento;
    }

    public List listPessoaRepisAno() {
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        List<RepisMovimento> result = new ArrayList();
        if (renderEmpresa) {
            result = wsrepisdb.pesquisarListaSolicitacao("", "", pessoa.getId(), -1);
        } else if (renderContabil) {
            //Pessoa pes = (Pessoa) new Dao().find(new Pessoa(), Integer.parseInt(listComboPessoa.get(idPessoa).getDescription()));
            result = wsrepisdb.pesquisarListaSolicitacao("", "", -1, pessoa.getId());
        }
        return result;
    }

    public boolean showAndamentoProtocolo(int idPessoa, int idPatronal) {
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        CertidaoDisponivel cd = (CertidaoDisponivel) new Dao().find(new CertidaoDisponivel(), Integer.valueOf(listComboCertidaoDisponivel.get(indexCertidaoDisponivel).getDescription()));
        if (wsrepisdb.validaPessoaRepisAnoTipoPatronal(idPessoa, getAnoConvencao(), cd.getCertidaoTipo().getId(), idPatronal).size() > 0) {
            return true;
        }
        return false;
    }

    public void solicitarREPIS() {
        DaoInterface di = new Dao();

        if (!listComboPessoa.isEmpty()) {
            if (Integer.parseInt(listComboPessoa.get(idPessoa).getDescription()) > 0) {
                setPessoaSolicitante((Pessoa) di.find(new Pessoa(), Integer.parseInt(listComboPessoa.get(idPessoa).getDescription())));
            }
        } else {
            setPessoaSolicitante(getPessoa());
        }
        WebREPISDB dbr = new WebREPISDBToplink();
        if (!dbr.listaAcordoAberto(pessoaSolicitante.getId()).isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não foi possível concluir sua solicitação. Consulte o sindícato!");
            return;
        }

        HomologacaoDB db = new HomologacaoDBToplink();
        setShowProtocolo(false);

        if (!db.pesquisaPessoaDebito(pessoaSolicitante.getId(), DataHoje.data()).isEmpty()) {
            GenericaMensagem.warn("Atenção", "Não foi possível concluir sua solicitação. Consulte o sindícato!");
        } else {
            if (contato.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Informe o nome do solicitante!");
                return;
            }

            Patronal patronal = dbr.pesquisaPatronalPorSolicitante(getPessoaSolicitante().getId());
            if (patronal == null) {
                GenericaMensagem.warn("Atenção", "Nenhuma patronal encontrada!");
                return;
            }
            JuridicaDB dbj = new JuridicaDBToplink();
            Juridica juridicax = dbj.pesquisaJuridicaPorPessoa(pessoaSolicitante.getId());
            PisoSalarialLote lote = dbr.pesquisaPisoSalarial(getAnoConvencao(), patronal.getId(), juridicax.getPorte().getId());

            if (lote.getId() == -1) {
                GenericaMensagem.warn("Atenção", "Patronal sem Lote, contate seu Sindicato!");
                return;
            }

            if (DataHoje.menorData(lote.getValidade(), DataHoje.data())) {
                GenericaMensagem.warn("Atenção", "Solicitação para esta patronal vencida!");
                return;
            }

            if (listComboCertidaoDisponivel.size() == 1 && listComboCertidaoDisponivel.get(indexCertidaoDisponivel).getDescription().equals("0")) {
                GenericaMensagem.warn("Atenção", "Nenhuma Certidão disponível!");
                return;
            }

            CertidaoDisponivel cd = (CertidaoDisponivel) di.find(new CertidaoDisponivel(), Integer.valueOf(listComboCertidaoDisponivel.get(indexCertidaoDisponivel).getDescription()));

            List<ConvencaoPeriodo> result = dbr.listaConvencaoPeriodo(cd.getCidade().getId(), cd.getConvencao().getId());

            Integer anoConvencao = null;

            if (cd.isPeriodoConvencao()) {
                if (result.isEmpty()) {
                    GenericaMensagem.warn("Atenção", "Contribuinte fora do Período de Convenção!");
                    return;
                }
                anoConvencao = Integer.valueOf(result.get(0).getReferenciaFinal().substring(3));
            } else {
                anoConvencao = getAnoAtual();
            }

            //repisMovimento.setAno(getAnoConvencao());
            repisMovimento.setAno(anoConvencao);
            repisMovimento.setContato(contato);
            repisMovimento.setRepisStatus((RepisStatus) di.find(new RepisStatus(), 1));
            repisMovimento.setPessoa(getPessoaSolicitante());
            repisMovimento.setDataResposta(null);
            repisMovimento.setDataEmissao(DataHoje.dataHoje());
            repisMovimento.setPatronal(patronal);
            repisMovimento.setCertidaoTipo(cd.getCertidaoTipo());

            di.openTransaction();
            if (!showAndamentoProtocolo(pessoaSolicitante.getId(), repisMovimento.getPatronal().getId())) {
                if (di.save(repisMovimento)) {
                    di.commit();
                    GenericaMensagem.info("Sucesso", "Solicitação encaminhada com sucesso!");
                    limpar();
                } else {
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível concluir sua solicitação. Consulte o sindicato!");
                }
            } else {
                GenericaMensagem.warn("Atenção", "Certidão já solicitada!");
                di.rollback();
                limpar();
            }
        }
    }

    public void updateStatus() {
        DaoInterface di = new Dao();
        if (repisMovimento.getId() != -1) {
            RepisStatus rs = (RepisStatus) di.find(new RepisStatus(), Integer.parseInt(listComboRepisStatus.get(idRepisStatus).getDescription()));
            repisMovimento.setRepisStatus(rs);

            if (rs.getId() == 2 || rs.getId() == 3 || rs.getId() == 4) {
                repisMovimento.setDataResposta(DataHoje.dataHoje());
            } else {
                repisMovimento.setDataResposta(null);
            }

            di.openTransaction();
            if (di.update(repisMovimento)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Status atualizado com sucesso!");

                listRepisMovimento.clear();
                repisMovimento = new RepisMovimento();
            } else {
                di.rollback();
                GenericaMensagem.error("Erro", "Falha na atualização do Status!");
            }
        }
    }

    public void edit(RepisMovimento rm) {
        repisMovimento = rm;
        for (int i = 0; i < getListComboRepisStatus().size(); i++) {
            if (Integer.parseInt(listComboRepisStatus.get(i).getDescription()) == repisMovimento.getRepisStatus().getId()) {
                setIdRepisStatus(i);
            }
        }
        WebREPISDB dbw = new WebREPISDBToplink();
        Juridica jur = dbw.pesquisaEscritorioDaEmpresa(repisMovimento.getPessoa().getId());
        if (jur != null) {
            escritorio = jur.getPessoa();
        }
    }

    public String imprimirCertificado(RepisMovimento rm) {
        List<RepisMovimento> listam = new ArrayList();
        listam.add(rm);
        imprimirCertificado(listam);
        return null;
    }

    public String imprimirCertificado(List<RepisMovimento> listam) {
        JuridicaDB dbj = new JuridicaDBToplink();
        WebREPISDB dbw = new WebREPISDBToplink();
        List<JasperPrint> lista_jasper = new ArrayList();

        if (listam.isEmpty()) {
            return null;
        }
        Dao di = new Dao();
        try {
            Juridica sindicato = dbj.pesquisaJuridicaPorPessoa(1);

            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
            PessoaEndereco sindicato_endereco = dbe.pesquisaEndPorPessoaTipo(1, 5);

            di.openTransaction();
            for (RepisMovimento repis : listam) {
                if (repis.getRepisStatus().getId() == 3 || repis.getRepisStatus().getId() == 4 || repis.getRepisStatus().getId() == 5) {
                    Juridica juridica = dbj.pesquisaJuridicaPorPessoa(repis.getPessoa().getId());
                    PisoSalarialLote lote = dbw.pesquisaPisoSalarial(repis.getAno(), repis.getPatronal().getId(), juridica.getPorte().getId());
                    PessoaEndereco ee = dbe.pesquisaEndPorPessoaTipo(repis.getPessoa().getId(), 5);
                    List<PisoSalarial> listapiso = dbw.listaPisoSalarialLote(lote.getId());

                    List<List> listax = dbj.listaJuridicaContribuinte(juridica.getId());
                    if (listax.isEmpty()) {
                        GenericaMensagem.warn("Atenção", "Empresa não é Contribuinte!");
                        di.rollback();
                        return null;
                    }
                    int id_convencao = (Integer) listax.get(0).get(5), id_grupo = (Integer) listax.get(0).get(6);

                    String referencia = DataHoje.DataToArray(repis.getDataEmissao())[2] + DataHoje.DataToArray(repis.getDataEmissao())[1];

                    List<ConvencaoPeriodo> result = dbw.listaConvencaoPeriodoData(ee.getEndereco().getCidade().getId(), id_convencao, referencia);

                    if (result.isEmpty()) {
                        GenericaMensagem.warn("Atenção", "Contribuinte fora do período de Convenção!");
                        di.rollback();
                        return null;
                    }

                    String ref = result.get(0).getReferenciaInicial().substring(3) + "/" + result.get(0).getReferenciaFinal().substring(3);
                    Date data_validade = DataHoje.converte(DataHoje.qtdeDiasDoMes(Integer.valueOf(result.get(0).getReferenciaFinal().substring(0, 2)), Integer.valueOf(result.get(0).getReferenciaFinal().substring(3))) + "/" + result.get(0).getReferenciaFinal());

                    Collection<ParametroCertificado> vetor = new ArrayList();
                    String logoPatronal = "",
                            imagemFundo = (String) ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/certificado_domingo_fundo.png"),
                            logoCaminho = (String) ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoPatronal/" + repis.getPatronal().getId());
                    if (new File(logoCaminho + ".jpg").exists()) {
                        logoCaminho = logoCaminho + ".jpg";
                    } else if (new File(logoCaminho + ".JPG").exists()) {
                        logoCaminho = logoCaminho + ".JPG";
                    } else if (new File(logoCaminho + ".png").exists()) {
                        logoCaminho = logoCaminho + ".png";
                    } else if (new File(logoCaminho + ".PNG").exists()) {
                        logoCaminho = logoPatronal + repis.getPatronal().getId() + ".PNG";
                    } else if (new File(logoCaminho + ".gif").exists()) {
                        logoCaminho = logoCaminho + ".gif";
                    } else {
                        logoCaminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png");
                    }

                    String cep = AnaliseString.mascaraCep(ee.getEndereco().getCep());
                    String ende = (ee.getComplemento().isEmpty())
                            ? ee.getEndereco().getLogradouro().getDescricao() + " " + ee.getEndereco().getDescricaoEndereco().getDescricao() + ", " + ee.getNumero() + " - " + ee.getEndereco().getBairro().getDescricao() + " - CEP: " + cep + " - " + ee.getEndereco().getCidade().getCidadeToString()
                            : ee.getEndereco().getLogradouro().getDescricao() + " " + ee.getEndereco().getDescricaoEndereco().getDescricao() + ", " + ee.getNumero() + " ( " + ee.getComplemento() + " ) " + ee.getEndereco().getBairro().getDescricao() + " - CEP: " + cep + " - " + ee.getEndereco().getCidade().getCidadeToString();

                    CertidaoMensagem certidaoMensagem = null;
                    File file = null;

                    switch (repis.getCertidaoTipo().getId()) {
                        case 1:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/REPIS.jasper"));
                            break;
                        case 2:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/CERTIDAO_DOMINGOS.jasper"));
                            break;
                        case 3:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/CERTIDAO_FERIADOS.jasper"));
                            certidaoMensagem = dbw.pesquisaCertidaoMensagem(ee.getEndereco().getCidade().getId(), 3);
                            break;
                        case 4:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/CERTIFICADO_DOMINGOS.jasper"));
                            break;
                        case 5:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/REPIS_AUXILIAR.jasper"));
                            break;
                        case 6:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/CERTIDAO_DOMINGOS_AUXILIAR.jasper"));
                            break;
                        case 7:
                            file = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Relatorios/CERTIDAO_FERIADOS_AUXILIAR.jasper"));
                            certidaoMensagem = dbw.pesquisaCertidaoMensagem(ee.getEndereco().getCidade().getId(), 3);
                            break;
                    }

                    JasperReport jasper = (JasperReport) JRLoader.loadObject(file);

                    for (PisoSalarial piso : listapiso) {
                        BigDecimal valor = new BigDecimal(piso.getValor());
                        if (valor.toString().equals("0")) {
                            valor = null;
                        }

                        vetor.add(
                                new ParametroCertificado(
                                        repis.getPatronal().getPessoa().getNome(),
                                        logoCaminho,
                                        repis.getPatronal().getBaseTerritorial(),
                                        sindicato.getPessoa().getNome(),
                                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                                        repis.getPessoa().getNome(),
                                        repis.getPessoa().getDocumento(),
                                        juridica.getPorte().getDescricao(),
                                        piso.getDescricao(),
                                        valor,
                                        (certidaoMensagem != null) ? certidaoMensagem.getMensagem() : piso.getPisoSalarialLote().getMensagem(),
                                        DataHoje.dataExtenso(DataHoje.converteData(data_validade), 3),//piso.getPisoSalarialLote().getDtValidade(),
                                        sindicato_endereco.getEndereco().getCidade().getCidade() + " - " + sindicato_endereco.getEndereco().getCidade().getUf(),
                                        piso.getPisoSalarialLote().getAno(),
                                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoSelo.png"),
                                        ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoFundo.png"),
                                        String.valueOf(repis.getId()),
                                        "0000000000".substring(0, 10 - String.valueOf(repis.getId()).length()) + String.valueOf(repis.getId()),
                                        DataHoje.dataExtenso(repis.getDataEmissaoString(), 3),
                                        ende,
                                        ref,
                                        imagemFundo
                                )
                        );
                    }

                    JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                    lista_jasper.add(JasperFillManager.fillReport(jasper, null, dtSource));

                    if (repis.getDataImpressao() == null) {
                        RepisMovimento repisx = (RepisMovimento) di.find(new RepisMovimento(), repis.getId());
                        repisx.setDataImpressao(DataHoje.dataHoje());

                        di.save(repisx);
                    }
                }
            }

            if (!lista_jasper.isEmpty()) {
                di.commit();
                JRPdfExporter exporter = new JRPdfExporter();

                String nomeDownload = "certificado_" + DataHoje.livre(DataHoje.dataHoje(), "yyyyMMdd-HHmmss") + ".pdf";
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis");

                exporter.setExporterInput(SimpleExporterInput.getInstance(lista_jasper));
                exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pathPasta + "/" + nomeDownload));

                SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();

                configuration.setCreatingBatchModeBookmarks(true);

                exporter.setConfiguration(configuration);

                exporter.exportReport();

                File fl = new File(pathPasta);

                if (fl.exists()) {
                    Download download = new Download(
                            nomeDownload,
                            pathPasta,
                            "application/pdf",
                            FacesContext.getCurrentInstance()
                    );
                    download.baixar();
                    download.remover();
                }
                listRepisMovimentoPatronal.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Atenção", "O Status da Certidão não pode impresso!");
            }
        } catch (NumberFormatException | JRException e) {
            di.rollback();
            e.getMessage();
            GenericaMensagem.error("Erro", "Arquivo de Certidão não encontrado! " + e.getMessage());
        }
        return null;
    }

    public String getEnderecoString() {
        PessoaEnderecoDB enderecoDB = new PessoaEnderecoDBToplink();
        PessoaEndereco ende = null;
        List listaEnd = enderecoDB.pesquisaEndPorPessoa(repisMovimento.getPessoa().getId());
        String strCompl;
        String enderecoString;
        if (!listaEnd.isEmpty()) {
            ende = (PessoaEndereco) listaEnd.get(0);
        }

        if (ende != null) {
            if (ende.getComplemento() == null || ende.getComplemento().isEmpty()) {
                strCompl = " ";
            } else {
                strCompl = " ( " + ende.getComplemento() + " ) ";
            }
            enderecoString = ende.getEndereco().getLogradouro().getDescricao() + " "
                    + ende.getEndereco().getDescricaoEndereco().getDescricao() + ", " + ende.getNumero() + " " + ende.getEndereco().getBairro().getDescricao() + ","
                    + strCompl + ende.getEndereco().getCidade().getCidade() + " - " + ende.getEndereco().getCidade().getUf() + " - " + AnaliseString.mascaraCep(ende.getEndereco().getCep());
        } else {
            enderecoString = "NENHUM";
        }
        return enderecoString;
    }

    public void clear() {
        setShowProtocolo(true);
        getListRepisMovimento();
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("sessaoUsuarioAcessoWeb")) {
            pessoa = (Pessoa) GenericaSessao.getObject("sessaoUsuarioAcessoWeb");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Pessoa getPessoaContribuinte() {
        return pessoaContribuinte;
    }

    public void setPessoaContribuinte(Pessoa pessoaContribuinte) {
        this.pessoaContribuinte = pessoaContribuinte;
    }

    public Pessoa getPessoaContabilidade() {
        return pessoaContabilidade;
    }

    public void setPessoaContabilidade(Pessoa pessoaContabilidade) {
        this.pessoaContabilidade = pessoaContabilidade;
    }

    public int getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(int idPessoa) {
        this.idPessoa = idPessoa;
    }

    public int getAnoAtual() {
        return Integer.parseInt(DataHoje.livre(DataHoje.dataHoje(), "yyyy"));
    }

    public int getAnoConvencao() {
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        CertidaoDisponivel cd = (CertidaoDisponivel) new Dao().find(new CertidaoDisponivel(), Integer.valueOf(listComboCertidaoDisponivel.get(indexCertidaoDisponivel).getDescription()));
        List<ConvencaoPeriodo> result = wsrepisdb.listaConvencaoPeriodo(cd.getCidade().getId(), cd.getConvencao().getId());
        if (result.isEmpty()) {
            return getAnoAtual();
        } else {
            return Integer.parseInt(result.get(0).getReferenciaFinal().substring(3));
        }
    }

    public boolean isShowProtocolo() {
        return showProtocolo;
    }

    public void setShowProtocolo(boolean showProtocolo) {
        this.showProtocolo = showProtocolo;
    }

    public List<RepisMovimento> getListRepisMovimento() {
        if (listRepisMovimento.isEmpty()) {
            List list_result = listPessoaRepisAno();
            if (!list_result.isEmpty()) {
                listRepisMovimento = list_result;
            }
        }
        return listRepisMovimento;
    }

    public void setListRepisMovimento(List<RepisMovimento> listRepisMovimento) {
        this.listRepisMovimento = listRepisMovimento;
    }

    public List<SelectItem> getListComboPessoa() {
        if (listComboPessoa.isEmpty()) {
            JuridicaDB dbJur = new JuridicaDBToplink();
            getPessoa();
            List<Juridica> select = null;
            select = dbJur.listaContabilidadePertencente(dbJur.pesquisaJuridicaPorPessoa(pessoa.getId()).getId());
            if (select != null) {
                int i = 0;
                while (i < select.size()) {
                    listComboPessoa.add(new SelectItem(i,
                            (String) (select.get(i)).getPessoa().getNome(),
                            Integer.toString((select.get(i)).getPessoa().getId())));
                    i++;
                }
            }
        }
        return listComboPessoa;
    }

    public List<SelectItem> getListComboRepisStatus() {
        if (listComboRepisStatus.isEmpty()) {
            DaoInterface di = new Dao();
            List<RepisStatus> list = di.list(new RepisStatus());
            for (int i = 0; i < list.size(); i++) {
                listComboRepisStatus.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
            }
        }
        return listComboRepisStatus;
    }

    public void setListComboPessoa(List<SelectItem> listComboPessoa) {
        this.listComboPessoa = listComboPessoa;
    }

    public Pessoa getPessoaSolicitante() {
        return pessoaSolicitante;
    }

    public void setPessoaSolicitante(Pessoa pessoaSolicitante) {
        this.pessoaSolicitante = pessoaSolicitante;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listRepisMovimento.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listRepisMovimento.clear();
    }

    public List<RepisMovimento> getListRepisMovimentoPatronal() {
        if (listRepisMovimentoPatronal.isEmpty()) {
            WebREPISDB wsrepisdb = new WebREPISDBToplink();
            Patronal patro = wsrepisdb.pesquisaPatronalPorPessoa(pessoa.getId());
            if (tipoPesquisa.equals("status")) {
                listRepisMovimentoPatronal = wsrepisdb.pesquisarListaLiberacao("status", listaStatus.get(indexStatus).getDescription(), patro.getId(), valueLenght);
            } else {
                listRepisMovimentoPatronal = wsrepisdb.pesquisarListaLiberacao("", "", patro.getId(), valueLenght);
            }
        }
        return listRepisMovimentoPatronal;
    }

    public void setListRepisMovimentoPatronal(List<RepisMovimento> listRepisMovimentoPatronal) {
        this.listRepisMovimentoPatronal = listRepisMovimentoPatronal;
    }

    public void setListComboRepisStatus(List<SelectItem> listComboRepisStatus) {
        this.listComboRepisStatus = listComboRepisStatus;
    }

    public int getIdRepisStatus() {
        return idRepisStatus;
    }

    public void setIdRepisStatus(int idRepisStatus) {
        this.idRepisStatus = idRepisStatus;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public PessoaEndereco getPessoaEndereco() {
        return pessoaEndereco;
    }

    public void setPessoaEndereco(PessoaEndereco pessoaEndereco) {
        this.pessoaEndereco = pessoaEndereco;
    }

    public boolean isRenderContabil() {
        return renderContabil;
    }

    public void setRenderContabil(boolean renderContabil) {
        this.renderContabil = renderContabil;
    }

    public boolean isRenderEmpresa() {
        return renderEmpresa;
    }

    public void setRenderEmpresa(boolean renderEmpresa) {
        this.renderEmpresa = renderEmpresa;
    }

    public List getListArquivosEnviados() {

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis/" + pessoa.getId() + "/");
        File file = new File(caminho);
        file.mkdir();

        File file2 = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis/"));
        File listFile[] = file2.listFiles();
        for (int i = 0; i < listFile.length; i++) {
            if (listFile[i].isFile()) {
                listFile[i].renameTo(new File(file.getPath() + "/" + listFile[i].getName()));
                listArquivosEnviados.clear();
            }
        }

        File list[] = file.listFiles();
        if (listArquivosEnviados.size() != list.length) {
            for (int i = 0; i < list.length; i++) {
                listArquivosEnviados.add(list[i].getName());
            }
        }

        return listArquivosEnviados;
    }

    public void setListArquivosEnviados(List listArquivosEnviados) {
        this.listArquivosEnviados = listArquivosEnviados;
    }

    public String getTipoPesquisa() {
        return tipoPesquisa;
    }

    public void setTipoPesquisa(String tipoPesquisa) {
        this.tipoPesquisa = tipoPesquisa;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Pessoa getEscritorio() {
        return escritorio;
    }

    public void setEscritorio(Pessoa escritorio) {
        this.escritorio = escritorio;
    }

    public List<SelectItem> getListComboCertidaoDisponivel() {
        if (listComboCertidaoDisponivel.isEmpty()) {
            WebREPISDB db = new WebREPISDBToplink();
            JuridicaDB dbj = new JuridicaDBToplink();

            Juridica juridica = null;
            if (pessoaContribuinte != null) {
                juridica = dbj.pesquisaJuridicaPorPessoa(pessoaContribuinte.getId());
            } else {
                juridica = dbj.pesquisaJuridicaPorPessoa(Integer.valueOf(listComboPessoa.get(idPessoa).getDescription()));
            }

            List<List> listax = dbj.listaJuridicaContribuinte(juridica.getId());

            if (listax.isEmpty()) {
                listComboCertidaoDisponivel.add(new SelectItem(0, "Nenhuma Certidão Disponível", "0"));
                return listComboCertidaoDisponivel;
            }

            int id_convencao = (Integer) listax.get(0).get(5), id_grupo = (Integer) listax.get(0).get(6);
            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
            PessoaEndereco pend = dbe.pesquisaEndPorPessoaTipo(juridica.getPessoa().getId(), 5);

            if (pend == null) {
                listComboCertidaoDisponivel.add(new SelectItem(0, "Nenhuma Certidão Disponível", "0"));
                return listComboCertidaoDisponivel;
            }

            List<CertidaoDisponivel> result = db.listaCertidaoDisponivel(pend.getEndereco().getCidade().getId(), id_convencao);

            if (result.isEmpty()) {
                listComboCertidaoDisponivel.add(new SelectItem(0, "Nenhuma Certidão Disponível", "0"));
                return listComboCertidaoDisponivel;
            }

            for (int i = 0; i < result.size(); i++) {
                listComboCertidaoDisponivel.add(new SelectItem(
                        i, result.get(i).getCertidaoTipo().getDescricao(), String.valueOf(result.get(i).getId())
                )
                );
            }
        }
        return listComboCertidaoDisponivel;
    }

    public void setListComboCertidaoDisponivel(List<SelectItem> listComboCertidaoDisponivel) {
        this.listComboCertidaoDisponivel = listComboCertidaoDisponivel;
    }

    public int getIndexCertidaoDisponivel() {
        return indexCertidaoDisponivel;
    }

    public void setIndexCertidaoDisponivel(int indexCertidaoDisponivel) {
        this.indexCertidaoDisponivel = indexCertidaoDisponivel;
    }

    public List<RepisMovimento> getListRepisMovimentoPatronalSelecionado() {
        return listRepisMovimentoPatronalSelecionado;
    }

    public void setListRepisMovimentoPatronalSelecionado(List<RepisMovimento> listRepisMovimentoPatronalSelecionado) {
        this.listRepisMovimentoPatronalSelecionado = listRepisMovimentoPatronalSelecionado;
    }

    public List<SelectItem> getListaTipoCertidao() {
        if (listaTipoCertidao.isEmpty()) {
            Dao di = new Dao();
            List<CertidaoTipo> result = di.list("CertidaoTipo");
            for (int i = 0; i < result.size(); i++) {
                listaTipoCertidao.add(new SelectItem(
                        i, result.get(i).getDescricao(), Integer.toString(result.get(i).getId()))
                );
            }
        }
        return listaTipoCertidao;
    }

    public void setListaTipoCertidao(List<SelectItem> listaTipoCertidao) {
        this.listaTipoCertidao = listaTipoCertidao;
    }

    public int getIndexCertidaoTipo() {
        return indexCertidaoTipo;
    }

    public void setIndexCertidaoTipo(int indexCertidaoTipo) {
        this.indexCertidaoTipo = indexCertidaoTipo;
    }

    public List<SelectItem> getListaStatus() {
        if (listaStatus.isEmpty()) {
            Dao di = new Dao();

            List<RepisStatus> result = di.list(new RepisStatus());
            listaStatus.add(new SelectItem(
                    0, "Todos", "0")
            );

            for (int i = 0; i < result.size(); i++) {
                listaStatus.add(new SelectItem(
                        i + 1, result.get(i).getDescricao(), Integer.toString(result.get(i).getId()))
                );
            }
        }
        return listaStatus;
    }

    public void setListaStatus(List<SelectItem> listaStatus) {
        this.listaStatus = listaStatus;
    }

    public int getIndexStatus() {
        return indexStatus;
    }

    public void setIndexStatus(int indexStatus) {
        this.indexStatus = indexStatus;
    }

    public String getValueLenght() {
        return valueLenght;
    }

    public void setValueLenght(String valueLenght) {
        this.valueLenght = valueLenght;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public List<SelectItem> getListaCidade() {
        if (listaCidade.isEmpty()) {
            CidadeDao db = new CidadeDao();

            List<Cidade> result = db.listaCidadeParaREPIS();

            if (result.isEmpty()) {
                listaCidade.add(new SelectItem(0, "Nenhuma Cidade encontrada", "0"));
                return listaCidade;
            }

            for (int i = 0; i < result.size(); i++) {
                listaCidade.add(
                        new SelectItem(i, result.get(i).getCidadeToString(), Integer.toString(result.get(i).getId()))
                );
            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<SelectItem> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public int getIndexCidade() {
        return indexCidade;
    }

    public void setIndexCidade(int indexCidade) {
        this.indexCidade = indexCidade;
    }
}
