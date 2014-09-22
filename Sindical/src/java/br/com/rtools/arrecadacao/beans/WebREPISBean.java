package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.CertidaoTipo;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PisoSalarial;
import br.com.rtools.arrecadacao.PisoSalarialLote;
import br.com.rtools.arrecadacao.RepisMovimento;
import br.com.rtools.arrecadacao.RepisStatus;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.endereco.Endereco;
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
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvaArquivos;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

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
    private List<SelectItem> listComboCertidaoTipo = new ArrayList();
    private List<RepisMovimento> listRepisMovimento = new ArrayList();
    private List<RepisMovimento> listRepisMovimentoPatronal = new ArrayList();
    private int idPessoa = 0;
    private int idRepisStatus = 0;
    private int indexCertidaoTipo = 0;
    private boolean renderContabil = false;
    private boolean renderEmpresa = false;
    private boolean showProtocolo = false;
    private boolean showPessoa = true;
    private String message = "";
    private RepisMovimento repisMovimento = new RepisMovimento();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String tipoPesquisa = "";
    private String descricao = "";
    private List listArquivosEnviados = new ArrayList();

    public WebREPISBean() {
        UsuarioDB db = new UsuarioDBToplink();
        getPessoa();
        pessoaContribuinte = db.ValidaUsuarioContribuinteWeb(pessoa.getId());
        pessoaContabilidade = db.ValidaUsuarioContabilidadeWeb(pessoa.getId());
        if (pessoaContribuinte != null && pessoaContabilidade != null) {
            //renderEmpresa = true;
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

    public String refresh() {
        return "webLiberacaoREPIS";
    }

    public String pesquisar() {
        WebREPISDB db = new WebREPISDBToplink();

        //listaRepisMovimento = db.listaRepisMovimento("nome", descricao.toUpperCase());
        listRepisMovimentoPatronal.clear();
        getListRepisMovimentoPatronal();

        List<RepisMovimento> lista = new ArrayList<RepisMovimento>();
        for (int i = 0; i < listRepisMovimentoPatronal.size(); i++) {
            if (tipoPesquisa.equals("nome")) {
                if (listRepisMovimentoPatronal.get(i).getPessoa().getNome().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimentoPatronal.get(i));
                }
            } else if (tipoPesquisa.equals("cnpj")) {
                if (listRepisMovimentoPatronal.get(i).getPessoa().getDocumento().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimentoPatronal.get(i));
                }
            } else if (tipoPesquisa.equals("protocolo")) {
                if (Integer.toString(listRepisMovimentoPatronal.get(i).getId()).equals(descricao.toUpperCase())) {
                    lista.add(listRepisMovimentoPatronal.get(i));
                }
            } else if (tipoPesquisa.equals("status")) {
                if (listRepisMovimentoPatronal.get(i).getRepisStatus().getDescricao().toUpperCase().equals(descricao.toUpperCase())) {
                    lista.add(listRepisMovimentoPatronal.get(i));
                }
            } else if (tipoPesquisa.equals("socilitante")) {
                if (listRepisMovimentoPatronal.get(i).getContato().toUpperCase().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimentoPatronal.get(i));
                }
            }
        }

        listRepisMovimentoPatronal.clear();

        listRepisMovimentoPatronal.addAll(lista);
        return "webLiberacaoREPIS";
    }

    public String pesquisarPorSolicitante() {
        WebREPISDB db = new WebREPISDBToplink();

        //listaRepisMovimento = db.listaRepisMovimento("nome", descricao.toUpperCase());
        listRepisMovimento.clear();
        getListRepisMovimento();

        List<RepisMovimento> lista = new ArrayList<RepisMovimento>();
        for (int i = 0; i < listRepisMovimento.size(); i++) {
            if (tipoPesquisa.equals("nome")) {
                if (listRepisMovimento.get(i).getPessoa().getNome().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimento.get(i));
                }
            } else if (tipoPesquisa.equals("cnpj")) {
                if (listRepisMovimento.get(i).getPessoa().getDocumento().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimento.get(i));
                }
            } else if (tipoPesquisa.equals("protocolo")) {
                if (Integer.toString(listRepisMovimento.get(i).getId()).equals(descricao.toUpperCase())) {
                    lista.add(listRepisMovimento.get(i));
                }
            } else if (tipoPesquisa.equals("status")) {
                if (listRepisMovimento.get(i).getRepisStatus().getDescricao().toUpperCase().equals(descricao.toUpperCase())) {
                    lista.add(listRepisMovimento.get(i));
                }
            } else if (tipoPesquisa.equals("socilitante")) {
                if (listRepisMovimento.get(i).getContato().toUpperCase().contains(descricao.toUpperCase())) {
                    lista.add(listRepisMovimento.get(i));
                }
            }
        }

        listRepisMovimento.clear();

        listRepisMovimento.addAll(lista);
        return null;
    }

    public void limpar() {
        message = "";
        repisMovimento = new RepisMovimento();
        showProtocolo = false;
        pessoaSolicitante = new Pessoa();
        idPessoa = 0;
        listRepisMovimento.clear();
    }

    public String limparRepisLiberacao() {
        repisMovimento = new RepisMovimento();
        setShowPessoa(true);
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
        //getPessoa();
        List result = new ArrayList();
        if (renderEmpresa) {
            result = wsrepisdb.validaPessoaRepisAno(pessoa.getId(), getAno());
        } else if (renderContabil) {
            result = wsrepisdb.listaProtocolosPorContabilidade(pessoa.getId(), getAno());
        }
        return result;
    }

    public boolean showAndamentoProtocolo(int idPessoa) {
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        if (wsrepisdb.validaPessoaRepisAno(idPessoa, getAno()).size() > 0) {
            return true;
        }
        return false;
    }

    public void solicitarREPIS() {
        message = "";
        DaoInterface di = new Dao();

//        if (listaArquivosEnviados.isEmpty()){
//            message = " PROCURAR SÍNDICATO! ";
//            return null;
//        }
        if (!listComboPessoa.isEmpty()) {
            if (Integer.parseInt(listComboPessoa.get(idPessoa).getDescription()) > 0) {
                setPessoaSolicitante((Pessoa) di.find(new Pessoa(), Integer.parseInt(listComboPessoa.get(idPessoa).getDescription())));
            }
        } else {
            setPessoaSolicitante(getPessoa());
        }
        WebREPISDB dbr = new WebREPISDBToplink();
        if (!dbr.listaAcordoAberto(pessoaSolicitante.getId()).isEmpty()) {
            message = "Não foi possível concluir sua solicitação. Consulte o sindícato.";
            return;
        }

        HomologacaoDB db = new HomologacaoDBToplink();
        setShowProtocolo(false);

        if (!db.pesquisaPessoaDebito(pessoaSolicitante.getId(), DataHoje.data()).isEmpty()) {
            message = " PROCURAR SÍNDICATO! ";
        } else {
            if (repisMovimento.getContato().isEmpty()) {
                message = " Informar o nome do solicitante! ";
                return;
            }
            Patronal patronal = dbr.pesquisaPatronalPorSolicitante(getPessoaSolicitante().getId());
            if (patronal == null) {
                message = "Nenhuma patronal encontrado!";
                return;
            }
            JuridicaDB dbj = new JuridicaDBToplink();    
            Juridica juridicax = dbj.pesquisaJuridicaPorPessoa(pessoaSolicitante.getId());
            PisoSalarialLote lote = dbr.pesquisaPisoSalarial(getAno(), patronal.getId(), juridicax.getPorte().getId());
            
            if (lote.getId() == -1){
                message = "Patronal sem Lote, contate seu Sindicato!";
                return;
            }
            
            if (DataHoje.menorData(lote.getValidade(), DataHoje.data())){
                message = "Solicitação para esta patronal vencida!";
                return;
            }
            
            PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
            PessoaEndereco pend = dbe.pesquisaEndPorPessoaTipo(pessoaSolicitante.getId(), 5);
            // AQUI ---
            pend.getEndereco().getCidade().getId();
            
            
            repisMovimento.setAno(getAno());
            repisMovimento.setRepisStatus((RepisStatus) di.find(new RepisStatus(), 1));
            repisMovimento.setPessoa(getPessoaSolicitante());
            repisMovimento.setDataResposta(null);
            repisMovimento.setDataEmissao(DataHoje.dataHoje());
            repisMovimento.setPatronal(patronal);
            repisMovimento.setCertidaoTipo( (CertidaoTipo) di.find("CertidaoTipo", Integer.valueOf(listComboCertidaoTipo.get(indexCertidaoTipo).getDescription())) );
            di.openTransaction();
            if (!showAndamentoProtocolo(pessoaSolicitante.getId())) {
                if (di.save(repisMovimento)) {
                    di.commit();
                    message = "Solicitação encaminhada com sucesso.";
                } else {
                    di.rollback();
                    message = "Não foi possível concluir sua solicitação. Consulte o sindícato.";
                }
            } else {
                message = " Repis já solicitado! ";
                di.rollback();
            }
        }
    }

    public void updateStatus() {
        message = "";
        DaoInterface di = new Dao();
        if (repisMovimento.getId() != -1) {
            repisMovimento.setRepisStatus((RepisStatus) di.find(new RepisStatus(), Integer.parseInt(listComboRepisStatus.get(idRepisStatus).getDescription())));
            repisMovimento.setDataResposta(DataHoje.dataHoje());
            di.openTransaction();
            if (di.update(repisMovimento)) {
                di.commit();
                message = "Status atualizado com sucesso.";
                setShowPessoa(true);
                listRepisMovimento.clear();
                repisMovimento = new RepisMovimento();
            } else {
                di.rollback();
                message = "Falha na atualização do Status!";
            }
        }
    }

    public void edit(RepisMovimento rm) {
        repisMovimento = rm;
        if (repisMovimento.getId() != -1) {
            setShowPessoa(false);
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
//        PF.update("form_libera_repis");
//        PF.openDialog("dlg_repis");
    }

    public String printCertificado(RepisMovimento rm) {
        JuridicaDB dbj = new JuridicaDBToplink();
        Juridica jur = dbj.pesquisaJuridicaPorPessoa(rm.getPessoa().getId());
        WebREPISDB dbw = new WebREPISDBToplink();
        List<List> listax = dbj.listaJuridicaContribuinte(jur.getId());
        if (listax.isEmpty()) {
            message = "Empresa não contribuinte";
            return null;
        }
        int id_convencao = (Integer) listax.get(0).get(5), id_grupo = (Integer) listax.get(0).get(6);
        //Patronal patronal = dbw.pesquisaPatronalPorConvGrupo(id_convencao, id_grupo);
        Patronal patronal = rm.getPatronal();
        if (patronal.getId() == -1) {
            message = "Patronal não encontrada, contate seu sindicato!";
            return null;
        }
        byte[] arquivo;
        arquivo = new byte[0];

        if (rm.getId() != -1) {
            try {
                JasperReport jasper = (JasperReport) JRLoader.loadObject(new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/REPIS.jasper")));
                Collection vetor = new ArrayList<ParametroCertificado>();
                PisoSalarialLote lote = dbw.pesquisaPisoSalarial(rm.getAno(), patronal.getId(), jur.getPorte().getId());
                if (lote.getId() == -1) {
                    message = "Piso / Lote Salarial não encontrado, contate seu sindicato!";
                    return null;
                }
                List<PisoSalarial> lista = dbw.listaPisoSalarialLote(lote.getId());
                Juridica sindicato = dbj.pesquisaJuridicaPorPessoa(1);
                PessoaEnderecoDB dbe = new PessoaEnderecoDBToplink();
                PessoaEndereco sindicato_endereco = dbe.pesquisaEndPorPessoaTipo(1, 5);
                for (int i = 0; i < lista.size(); i++) {
                    BigDecimal valor = new BigDecimal(lista.get(i).getValor());
                    if (valor.toString().equals("0")) {
                        valor = null;
                    }
                    String logoPatronal = "";
                    String logoCaminho = (String) ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoPatronal/" + patronal.getId());
                    if (new File(logoCaminho + ".jpg").exists()) {
                        logoCaminho = logoCaminho + ".jpg";
                    } else if (new File(logoCaminho + ".JPG").exists()) {
                        logoCaminho = logoCaminho + ".JPG";
                    } else if (new File(logoCaminho + ".png").exists()) {
                        logoCaminho = logoCaminho + ".png";
                    } else if (new File(logoCaminho + ".PNG").exists()) {
                        logoCaminho = logoPatronal + patronal.getId() + ".PNG";
                    } else if (new File(logoCaminho + ".gif").exists()) {
                        logoCaminho = logoCaminho + ".gif";
                    } else {
                        logoCaminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png");
                    }
                    vetor.add(
                            new ParametroCertificado(
                                    patronal.getPessoa().getNome(),
                                    logoCaminho,
                                    patronal.getBaseTerritorial(),
                                    sindicato.getPessoa().getNome(),
                                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"),
                                    rm.getPessoa().getNome(),
                                    rm.getPessoa().getDocumento(),
                                    jur.getPorte().getDescricao(),
                                    lista.get(i).getDescricao(),
                                    valor,
                                    lista.get(i).getPisoSalarialLote().getMensagem(),
                                    lista.get(i).getPisoSalarialLote().getDtValidade(),
                                    sindicato_endereco.getEndereco().getCidade().getCidade() + " - " + sindicato_endereco.getEndereco().getCidade().getUf(),
                                    lista.get(i).getPisoSalarialLote().getAno(),
                                    //((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoSelo.png"),
                                    //((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/"+ controleUsuarioJSFBean.getCliente()+"/Imagens/LogoFundo.png"),
                                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoSelo.png"),
                                    ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Imagens/LogoFundo.png"),
                                    String.valueOf(rm.getId()),
                                    "0000000000".substring(0, 10 - String.valueOf(rm.getId()).length()) + String.valueOf(rm.getId()),
                                    DataHoje.dataExtenso(rm.getDataEmissaoString(), 3))
                    );
                }

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "repis_" + rm.getId() + "_" + rm.getPessoa().getId() + ".pdf";
                String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis");
                SalvaArquivos sa = new SalvaArquivos(arquivo,
                        nomeDownload,
                        false);
                sa.salvaNaPasta(pathPasta);

                Download download = new Download(nomeDownload,
                        pathPasta,
                        "application/pdf",
                        FacesContext.getCurrentInstance());
                download.baixar();
                download.remover();
            } catch (JRException e) {
                e.getMessage();
            }
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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getAno() {
        return Integer.parseInt(DataHoje.livre(DataHoje.dataHoje(), "yyyy"));
    }

    public boolean isShowProtocolo() {
        return showProtocolo;
    }

    public void setShowProtocolo(boolean showProtocolo) {
        this.showProtocolo = showProtocolo;
    }

    public List<RepisMovimento> getListRepisMovimento() {
        if (listRepisMovimento.isEmpty()) {
            if (!listPessoaRepisAno().isEmpty()) {
                listRepisMovimento = listPessoaRepisAno();
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
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        if (listRepisMovimentoPatronal.isEmpty()) {
            Patronal patro = wsrepisdb.pesquisaPatronalPorPessoa(pessoa.getId());
//            listaRepisMovimentoPatronal = wsrepisdb.listaProtocolosPorPatronalCnae(patro.getId());
            listRepisMovimentoPatronal = wsrepisdb.listaProtocolosPorPatronal(patro.getId());
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

    public boolean isShowPessoa() {
        return showPessoa;
    }

    public void setShowPessoa(boolean showPessoa) {
        this.showPessoa = showPessoa;
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

    public List<SelectItem> getListComboCertidaoTipo() {
        if (listComboCertidaoTipo.isEmpty()){
            WebREPISDB db = new WebREPISDBToplink();
            
            List<CertidaoTipo> result = db.listaCertidaoTipo();
            
            for (int i = 0; i < result.size(); i++){
                listComboCertidaoTipo.add(
                        new SelectItem(
                            i, result.get(i).getDescricao(), String.valueOf(result.get(i).getId())
                        )
                );
            }
        }
        return listComboCertidaoTipo;
    }

    public void setListComboCertidaoTipo(List<SelectItem> listComboCertidaoTipo) {
        this.listComboCertidaoTipo = listComboCertidaoTipo;
    }

    public int getIndexCertidaoTipo() {
        return indexCertidaoTipo;
    }

    public void setIndexCertidaoTipo(int indexCertidaoTipo) {
        this.indexCertidaoTipo = indexCertidaoTipo;
    }
}
