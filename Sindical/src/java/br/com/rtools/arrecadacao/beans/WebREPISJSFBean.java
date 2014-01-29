package br.com.rtools.arrecadacao.beans;

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
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Download;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;

public class WebREPISJSFBean {

    private Pessoa pessoa = new Pessoa();
    private Endereco endereco = new Endereco();
    private PessoaEndereco pessoaEndereco = new PessoaEndereco();
    private Pessoa pessoaContribuinte = new Pessoa();
    private Pessoa pessoaContabilidade = new Pessoa();
    private Pessoa pessoaSolicitante = new Pessoa();
    private Pessoa escritorio = new Pessoa();
    private List<SelectItem> listaComboPessoa = new Vector<SelectItem>();
    private List<SelectItem> listaComboRepisStatus = new Vector<SelectItem>();
    private List<RepisMovimento> listaRepisMovimento = new ArrayList<RepisMovimento>();
    private List<RepisMovimento> listaRepisMovimentoPatronal = new ArrayList<RepisMovimento>();
    private int idPessoa = 0;
    private int idRepisStatus = 0;
    private boolean renderContabil = false;
    private boolean renderEmpresa = false;
    private boolean showProtocolo = false;
    private boolean showPessoa = true;
    private String msg = "";
    private RepisMovimento repisMovimento = new RepisMovimento();
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private String tipoPesquisa = "";
    private String descricao = "";
    private List listaArquivosEnviados = new ArrayList();

    public WebREPISJSFBean() {
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

    public String refresh(){
        return "webLiberacaoREPIS";
    }
    
    public String pesquisar(){
        WebREPISDB db = new WebREPISDBToplink();
        
        //listaRepisMovimento = db.listaRepisMovimento("nome", descricao.toUpperCase());
        listaRepisMovimentoPatronal.clear();
        getListaRepisMovimentoPatronal();
        
        List<RepisMovimento>  lista = new ArrayList<RepisMovimento>();
        for(int i = 0; i < listaRepisMovimentoPatronal.size(); i++){
            if (tipoPesquisa.equals("nome")){
                if (listaRepisMovimentoPatronal.get(i).getPessoa().getNome().contains(descricao.toUpperCase()))
                    lista.add(listaRepisMovimentoPatronal.get(i));
            }else if (tipoPesquisa.equals("cnpj")){
                if (listaRepisMovimentoPatronal.get(i).getPessoa().getDocumento().contains(descricao.toUpperCase()))
                    lista.add(listaRepisMovimentoPatronal.get(i));
            }else if (tipoPesquisa.equals("protocolo")){
                if (Integer.toString(listaRepisMovimentoPatronal.get(i).getId()).equals(descricao.toUpperCase()))
                    lista.add(listaRepisMovimentoPatronal.get(i));
            }else if (tipoPesquisa.equals("status")){
                if (listaRepisMovimentoPatronal.get(i).getRepisStatus().getDescricao().toUpperCase().equals(descricao.toUpperCase()))
                    lista.add(listaRepisMovimentoPatronal.get(i));
            }else if (tipoPesquisa.equals("socilitante")){
                if (listaRepisMovimentoPatronal.get(i).getContato().toUpperCase().contains(descricao.toUpperCase()))
                    lista.add(listaRepisMovimentoPatronal.get(i));
            }
        }
        
        listaRepisMovimentoPatronal.clear();
        
        listaRepisMovimentoPatronal.addAll(lista);
        return "webLiberacaoREPIS";
    }
    
    public void limpar() {
        msg = "";
        repisMovimento = new RepisMovimento();
        showProtocolo = false;
        pessoaSolicitante = new Pessoa();
        idPessoa = 0;
        listaRepisMovimento.clear();
    }

    public String limparRepisLiberacao() {
        repisMovimento = new RepisMovimento();
        setShowPessoa(true);
        listaRepisMovimentoPatronal.clear();
        return "webLiberacaoREPIS";
    }

    public RepisMovimento getRepisMovimento() {
        return repisMovimento;
    }

    public void setRepisMovimento(RepisMovimento repisMovimento) {
        this.repisMovimento = repisMovimento;
    }

    public List listaPessoaRepisAno() {
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

    public String btnSolicitarREPIS() {
        msg = "";
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

//        if (listaArquivosEnviados.isEmpty()){
//            msg = " PROCURAR SÍNDICATO! ";
//            return null;
//        }

        if (!listaComboPessoa.isEmpty()) {
            if (Integer.parseInt(listaComboPessoa.get(idPessoa).getDescription()) > 0) {
                setPessoaSolicitante((Pessoa) sv.pesquisaObjeto(Integer.parseInt(listaComboPessoa.get(idPessoa).getDescription()), "Pessoa"));
            }
        } else {
            setPessoaSolicitante(getPessoa());
        }
        HomologacaoDB db = new HomologacaoDBToplink();
        setShowProtocolo(false);
        if (!db.pesquisaPessoaDebito(pessoaSolicitante.getId(), DataHoje.data()).isEmpty()) {
            msg = " PROCURAR SÍNDICATO! ";
            return null;
        } else {
            if (repisMovimento.getContato().isEmpty()) {
                msg = " Informar o nome do solicitante! ";
                return null;
            }
            repisMovimento.setAno(getAno());
            repisMovimento.setRepisStatus((RepisStatus) sv.pesquisaObjeto(1, "RepisStatus"));
            repisMovimento.setPessoa(getPessoaSolicitante());
            repisMovimento.setDataResposta(null);
            repisMovimento.setDataEmissao(DataHoje.dataHoje());
            sv.abrirTransacao();
            if (!showAndamentoProtocolo(pessoaSolicitante.getId())) {
                if (sv.inserirObjeto(repisMovimento)) {
                    sv.comitarTransacao();
                    msg = "Solicitação encaminhada com sucesso.";
                    return null;
                } else {
                    sv.desfazerTransacao();
                    msg = "Não foi possível concluir sua solicitação. Consulte o sindícato.";
                    return null;
                }
            } else {
                msg = " Repis já solicitado! ";
                sv.desfazerTransacao();
                return null;
            }
        }
    }

    public String btnSalvarStatus() {
        msg = "";
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (repisMovimento.getId() != -1) {
            repisMovimento.setRepisStatus((RepisStatus) sv.pesquisaObjeto(Integer.parseInt(listaComboRepisStatus.get(idRepisStatus).getDescription()), "RepisStatus"));
            repisMovimento.setDataResposta(DataHoje.dataHoje());
            sv.abrirTransacao();
            if (sv.alterarObjeto(repisMovimento)) {
                sv.comitarTransacao();
                msg = "Status atualizado com sucesso.";
                setShowPessoa(true);
                listaRepisMovimento.clear();
                repisMovimento = new RepisMovimento();
                return null;
            } else {
                sv.desfazerTransacao();
                msg = "Falha na atualização do Status!";
                return null;
            }
        }
        return null;
    }

    public String editar(int id) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        repisMovimento = (RepisMovimento) dB.pesquisaCodigo(listaRepisMovimentoPatronal.get(id).getId(), "RepisMovimento");
        if (repisMovimento.getId() != -1) {
            setShowPessoa(false);
            for (int i = 0; i < getListaComboRepisStatus().size(); i++) {
                if (Integer.parseInt(listaComboRepisStatus.get(i).getDescription()) == repisMovimento.getRepisStatus().getId()) {
                    setIdRepisStatus(i);
                }
            }
            WebREPISDB dbw = new WebREPISDBToplink();
            escritorio = dbw.pesquisaEscritorioDaEmpresa(repisMovimento.getPessoa().getId()).getPessoa();
        }
        return "webLiberacaoREPIS";
    }

    public String imprimirCertificado(int id) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        setRepisMovimento((RepisMovimento) dB.pesquisaCodigo(id, "RepisMovimento"));
        JuridicaDB dbj = new JuridicaDBToplink();
        Juridica jur = dbj.pesquisaJuridicaPorPessoa(repisMovimento.getPessoa().getId());
        WebREPISDB dbw = new WebREPISDBToplink();
        List<Vector> listax = dbj.listaJuridicaContribuinte(jur.getId());

        if (listax.isEmpty()) {
            msg = "Empresa não contribuinte";
            return null;
        }

        int id_convencao = (Integer) listax.get(0).get(5), id_grupo = (Integer) listax.get(0).get(6);

        Patronal patronal = dbw.pesquisaPatronalPorConvGrupo(id_convencao, id_grupo);
        if (patronal.getId() == -1) {
            msg = "Patronal não encontrada, contate seu sindicato!";
            return null;
        }
        byte[] arquivo = new byte[0];

        if (repisMovimento.getId() != -1) {
            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            try {
                JasperReport jasper = (JasperReport) JRLoader.loadObject(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Relatorios/REPIS.jasper"));
                Collection vetor = new ArrayList<ParametroCertificado>();

                //PisoSalarialLote lote = dbw.pesquisaPisoSalarial(repisMovimento.getAno(), patronal.getId(), dbj.pesquisaJuridicaPorPessoa(patronal.getPessoa().getId()).getPorte().getId());
                PisoSalarialLote lote = dbw.pesquisaPisoSalarial(repisMovimento.getAno(), patronal.getId(), jur.getPorte().getId());

                if (lote.getId() == -1) {
                    msg = "Lote Salarial não encontrado, contate seu sindicato!";
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
                            repisMovimento.getPessoa().getNome(),
                            repisMovimento.getPessoa().getDocumento(),
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
                            String.valueOf(repisMovimento.getId()),
                            "0000000000".substring(0, 10 - String.valueOf(repisMovimento.getId()).length()) + String.valueOf(repisMovimento.getId())));
                }

                JRBeanCollectionDataSource dtSource = new JRBeanCollectionDataSource(vetor);
                JasperPrint print = JasperFillManager.fillReport(
                        jasper,
                        null,
                        dtSource);
                arquivo = JasperExportManager.exportReportToPdf(print);

                String nomeDownload = "repis_" + repisMovimento.getId() + "_" + repisMovimento.getPessoa().getId() + ".pdf";
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String getEnderecoString() {
        PessoaEnderecoDB enderecoDB = new PessoaEnderecoDBToplink();
        PessoaEndereco ende = null;
        List listaEnd = enderecoDB.pesquisaEndPorPessoa(repisMovimento.getPessoa().getId());
        String strCompl = "";
        String enderecoString = "";
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

    public void btnAndamentoProtocolo() {
        setShowProtocolo(true);
        getListaRepisMovimento();
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuarioAcessoWeb");
            //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sessaoUsuarioAcessoWeb");
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public List<RepisMovimento> getListaRepisMovimento() {
        if (listaRepisMovimento.isEmpty()) {
            if (!listaPessoaRepisAno().isEmpty()) {
                listaRepisMovimento = listaPessoaRepisAno();
            }
        }
        return listaRepisMovimento;
    }

    public void setListaRepisMovimento(List<RepisMovimento> listaRepisMovimento) {
        this.listaRepisMovimento = listaRepisMovimento;
    }

    public List<SelectItem> getListaComboPessoa() {
        JuridicaDB dbJur = new JuridicaDBToplink();
        getPessoa();
        List<Juridica> select = null;
        select = dbJur.listaContabilidadePertencente(dbJur.pesquisaJuridicaPorPessoa(pessoa.getId()).getId());
        if (select != null) {
            int i = 0;
            while (i < select.size()) {
                listaComboPessoa.add(new SelectItem(new Integer(i),
                        (String) (select.get(i)).getPessoa().getNome(),
                        Integer.toString((select.get(i)).getPessoa().getId())));
                i++;
            }
        }
        return listaComboPessoa;
    }

    public List<SelectItem> getListaComboRepisStatus() {
        if (listaComboRepisStatus.isEmpty()) {
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            List<RepisStatus> select = null;
            select = dB.listaObjeto("RepisStatus");
            if (select != null) {
                int i = 0;
                while (i < select.size()) {
                    listaComboRepisStatus.add(new SelectItem(new Integer(i), select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
                    i++;
                }
            }

        }
        return listaComboRepisStatus;
    }

    public void setListaComboPessoa(List<SelectItem> listaComboPessoa) {
        this.listaComboPessoa = listaComboPessoa;
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
        listaRepisMovimento.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaRepisMovimento.clear();
    }

    public List<RepisMovimento> getListaRepisMovimentoPatronal() {
        WebREPISDB wsrepisdb = new WebREPISDBToplink();
        if (listaRepisMovimentoPatronal.isEmpty()) {
            // Patronal patro = new Patronal();
            //getPessoa();
            Patronal patro = wsrepisdb.pesquisaPatronalPorPessoa(pessoa.getId());
            listaRepisMovimentoPatronal = wsrepisdb.listaProtocolosPorPatronalCnae(patro.getId());
        }
        return listaRepisMovimentoPatronal;
    }

    public void setListaRepisMovimentoPatronal(List<RepisMovimento> listaRepisMovimentoPatronal) {
        this.listaRepisMovimentoPatronal = listaRepisMovimentoPatronal;
    }

    public void setListaComboRepisStatus(List<SelectItem> listaComboRepisStatus) {
        this.listaComboRepisStatus = listaComboRepisStatus;
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

    public List getListaArquivosEnviados() {

        String caminho = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis/" + pessoa.getId() + "/");
        File file = new File(caminho);
        file.mkdir();

        File file2 = new File(((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Arquivos/downloads/repis/"));
        File listFile[] = file2.listFiles();
        for (int i = 0; i < listFile.length; i++) {
            if (listFile[i].isFile()) {
                listFile[i].renameTo(new File(file.getPath() + "/" + listFile[i].getName()));
                listaArquivosEnviados.clear();
            }
        }

        File list[] = file.listFiles();
        if (listaArquivosEnviados.size() != list.length) {
            for (int i = 0; i < list.length; i++) {
                listaArquivosEnviados.add(list[i].getName());
            }
        }

        return listaArquivosEnviados;
    }

    public void setListaArquivosEnviados(List listaArquivosEnviados) {
        this.listaArquivosEnviados = listaArquivosEnviados;
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
}