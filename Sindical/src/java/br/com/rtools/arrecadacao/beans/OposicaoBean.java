package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.Oposicao;
import br.com.rtools.arrecadacao.OposicaoPessoa;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDB;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDBTopLink;
import br.com.rtools.arrecadacao.db.OposicaoDB;
import br.com.rtools.arrecadacao.db.OposicaoDBToplink;
import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class OposicaoBean implements Serializable {

    private Oposicao oposicao;
    private OposicaoPessoa oposicaoPessoa;
    private ConvencaoPeriodo convencaoPeriodo;
    private List<ConvencaoPeriodo> listaConvencaoPeriodos;
    private String message;
    private String mensagemEmpresa;
    private String sexo;
    private String valorPesquisa;
    private boolean desabilitaPessoa;
    private List<Oposicao> listaOposicaos;
    private String porPesquisa;
    private String descricaoPesquisa;
    private String comoPesquisa;
    private Socios socios;
    private boolean removeFiltro;

    @PostConstruct
    public void init() {
        oposicao = new Oposicao();
        oposicaoPessoa = new OposicaoPessoa();
        convencaoPeriodo = new ConvencaoPeriodo();
        listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
        message = "";
        mensagemEmpresa = "";
        sexo = "M";
        valorPesquisa = "";
        desabilitaPessoa = false;
        listaOposicaos = new ArrayList<Oposicao>();
        porPesquisa = "todos";
        descricaoPesquisa = "";
        comoPesquisa = "";
        socios = new Socios();
        removeFiltro = false;
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("oposicaoBean");
        GenericaSessao.remove("oposicaoPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("removeFiltro");
    }

    public void clear() {
        oposicao = new Oposicao();
        oposicaoPessoa = new OposicaoPessoa();
        convencaoPeriodo = new ConvencaoPeriodo();
        listaConvencaoPeriodos.clear();
        listaOposicaos.clear();
        sexo = "";
        porPesquisa = "todos";
        message = "";
        setMensagemEmpresa("");
        setDesabilitaPessoa(false);
        valorPesquisa = "";
        setSexo("M");
    }

    public void save() {
        if (oposicao.getJuridica().getId() == -1) {
            message = "Informar o Pessoa Jurídica!";
            return;
        }
        if (oposicao.getOposicaoPessoa().getNome().equals("")) {
            message = "Informar o nome da Pessoa!";
            return;
        }
        if (oposicao.getConvencaoPeriodo().getId() == -1) {
            message = "Informar o período de convenção!";
            return;
        }
        if (oposicao.getEmissao().isEmpty()) {
            message = "Campo emissão não pode estar vazio!";
            return;
        }
        if (socios.getId() != -1) {
            if (socios.getServicoPessoa().isAtivo()) {
                message = "CPF cadastrado como sócio, inative para salvar oposição!";
                return;
            }
        }
        DaoInterface di = new Dao();
        String beforeUpdate = "";
        Oposicao o = (Oposicao) di.find(oposicao);
        if (oposicao.getId() != -1) {
            beforeUpdate = ""
                    + "ID: " + o.getId()
                    + " - Pessoa (Oposição Pessoa): (" + o.getOposicaoPessoa().getId() + ") " + o.getOposicaoPessoa().getNome()
                    + " - Jurídica: (" + o.getJuridica().getPessoa().getId() + ") " + o.getJuridica().getPessoa().getNome()
                    + " - Convençao Período: (" + o.getConvencaoPeriodo().getId() + ") "
                    + " [" + o.getConvencaoPeriodo().getConvencao().getDescricao()
                    + " - " + o.getConvencaoPeriodo().getGrupoCidade().getDescricao()
                    + " - Ref: " + o.getConvencaoPeriodo().getReferenciaInicial()
                    + " - " + o.getConvencaoPeriodo().getReferenciaFinal()
                    + " ]";
        }
        if (!saveOposicaoPessoa()) {
            message = "Falha ao salvar oposição pessoa!";
            return;
        }

        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (oposicao.getId() == -1) {
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            if (oposicaoDB.validaOposicao(oposicao)) {
                message = "Oposição já cadastrada para essa pessoa para a convenção vigente!";
                return;
            }
            if (di.save(oposicao)) {
                novoLog.save(""
                        + "ID: " + oposicao.getId()
                        + " - Pessoa (Oposição Pessoa): (" + oposicao.getOposicaoPessoa().getId() + ") " + oposicao.getOposicaoPessoa().getNome()
                        + " - Jurídica: (" + oposicao.getJuridica().getPessoa().getId() + ") " + oposicao.getJuridica().getPessoa().getNome()
                        + " - Convençao Período: (" + oposicao.getConvencaoPeriodo().getId() + ") "
                        + " [" + oposicao.getConvencaoPeriodo().getConvencao().getDescricao()
                        + " - " + oposicao.getConvencaoPeriodo().getGrupoCidade().getDescricao()
                        + " - Ref: " + oposicao.getConvencaoPeriodo().getReferenciaInicial()
                        + " - " + oposicao.getConvencaoPeriodo().getReferenciaFinal()
                        + " ]"
                );
                di.commit();
                clear();
                setMessage("Registro salvo com sucesso.");
            } else {
                di.rollback();
                clear();
                message = "Falha ao salvar este registro!";
            }
        } else {
            if (di.update(oposicao)) {
                novoLog.update(beforeUpdate, ""
                        + "ID: " + oposicao.getId()
                        + " - Pessoa (Oposição Pessoa): (" + oposicao.getOposicaoPessoa().getId() + ") " + oposicao.getOposicaoPessoa().getNome()
                        + " - Jurídica: (" + oposicao.getJuridica().getPessoa().getId() + ") " + oposicao.getJuridica().getPessoa().getNome()
                        + " - Convençao Período: (" + oposicao.getConvencaoPeriodo().getId() + ") "
                        + " [" + oposicao.getConvencaoPeriodo().getConvencao().getDescricao()
                        + " - " + oposicao.getConvencaoPeriodo().getGrupoCidade().getDescricao()
                        + " - Ref: " + oposicao.getConvencaoPeriodo().getReferenciaInicial()
                        + " - " + oposicao.getConvencaoPeriodo().getReferenciaFinal()
                        + " ]"
                );
                di.commit();
                clear();
                setMessage("Registro atualizado com sucesso.");
            } else {
                di.rollback();
                clear();
                message = "Falha ao excluir este registro!";
            }
        }
    }

    public String edit(Oposicao o) {
        DaoInterface di = new Dao();
        setDesabilitaPessoa(true);
        oposicao = (Oposicao) di.rebind(o);
        sexo = oposicao.getOposicaoPessoa().getSexo();
        GenericaSessao.put("oposicaoPesquisa", oposicao);
        GenericaSessao.put("linkClicado", true);
        String urlRetorno = "oposicao";
        if (GenericaSessao.exists("urlRetorno")) {
            GenericaSessao.put("oposicaoPesquisaPor", porPesquisa);
            urlRetorno = GenericaSessao.getString("urlRetorno");
        }
        return urlRetorno;
    }

    public boolean saveOposicaoPessoa() {
        if (oposicao.getOposicaoPessoa().getCpf().equals("___.___.___-__") || oposicao.getOposicaoPessoa().getCpf().equals("")) {
            message = "Informar o CPF!";
            return false;
        }
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(oposicao.getOposicaoPessoa().getCpf()))) {
            message = "CPF inválido!";
            return false;
        }
        oposicao.getOposicaoPessoa().setSexo(sexo);
        DaoInterface di = new Dao();
        di.openTransaction();
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        OposicaoPessoa op = oposicaoDB.pesquisaOposicaoPessoa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
        if (op.getId() == -1) {
            if (oposicao.getOposicaoPessoa().getId() == -1) {
                if (di.save(oposicao.getOposicaoPessoa())) {
                    listaConvencaoPeriodos.clear();
                    di.commit();
                    return true;
                } else {
                    di.rollback();
                    return false;
                }
            }
        } else {
            if (op.getCpf().equals(oposicao.getOposicaoPessoa().getCpf()) && op.getRg().equals(oposicao.getOposicaoPessoa().getRg()) && op.getNome().equals(oposicao.getOposicaoPessoa().getNome()) && op.getSexo().equals(oposicao.getOposicaoPessoa().getSexo())) {
                return true;
            } else {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    oposicao.getOposicaoPessoa().setId(op.getId());
                    if (di.update(oposicao.getOposicaoPessoa())) {
                        di.commit();
                        return true;
                    } else {
                        di.rollback();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void delete() {
        delete(0);
    }

    public void delete(int id) {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (id > 0) {
            oposicao = (Oposicao) di.find(new Oposicao(), id);
        } else {
            oposicao = (Oposicao) di.find(oposicao);
        }
        if (oposicao.getId() != -1) {
            di.openTransaction();
            if (di.delete(oposicao)) {
                novoLog.delete(""
                        + "ID: " + oposicao.getId()
                        + " - Pessoa (Oposição Pessoa): (" + oposicao.getOposicaoPessoa().getId() + ") " + oposicao.getOposicaoPessoa().getNome()
                        + " - Jurídica: (" + oposicao.getJuridica().getPessoa().getId() + ") " + oposicao.getJuridica().getPessoa().getNome()
                        + " - Convençao Período: (" + oposicao.getConvencaoPeriodo().getId() + ") "
                        + " - [" + oposicao.getConvencaoPeriodo().getConvencao().getDescricao()
                        + " - " + oposicao.getConvencaoPeriodo().getGrupoCidade().getDescricao()
                        + " - Ref: " + oposicao.getConvencaoPeriodo().getReferenciaInicial()
                        + " - " + oposicao.getConvencaoPeriodo().getReferenciaFinal()
                        + " ]"
                );
                di.commit();
                clear();
                setMessage("Registro salvo com sucesso.");
            } else {
                di.rollback();
                clear();
                message = "Falha na exclusão do registro!";
            }
        }
    }

    public void consultaPessoa() {
        boolean isVerificaDocumento = false;
        if (oposicao.getOposicaoPessoa().getCpf().equals("___.___.___-__") || oposicao.getOposicaoPessoa().getCpf().equals("")) {
            isVerificaDocumento = true;
        }
        if (!oposicao.getOposicaoPessoa().getRg().equals("")) {
            isVerificaDocumento = false;
        }
        if (isVerificaDocumento == true) {
            return;
        }
        if (oposicao.getOposicaoPessoa().getId() != -1) {
            return;
        }
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(oposicao.getOposicaoPessoa().getCpf()))) {
            //RequestContext.getCurrentInstance().execute("PF('input_cpf').focus();");
            GenericaMensagem.warn("Validação", "CPF inválido!");
            return;
        }
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        PessoaEmpresa pessoaEmpresa = oposicaoDB.pesquisaPessoaFisicaEmpresa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
        if (pessoaEmpresa.getId() != -1) {
            if (oposicao.getJuridica().getId() == -1) {
                oposicao.setJuridica(pessoaEmpresa.getJuridica());
            }
            setDesabilitaPessoa(true);
            oposicaoPessoa = oposicaoDB.pesquisaOposicaoPessoa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
            if (oposicaoPessoa.getId() != -1) {
                oposicao.setOposicaoPessoa(oposicaoPessoa);
            } else {
                oposicao.getOposicaoPessoa().setNome(pessoaEmpresa.getFisica().getPessoa().getNome());
                oposicao.getOposicaoPessoa().setRg(pessoaEmpresa.getFisica().getRg());
                oposicao.getOposicaoPessoa().setCpf(pessoaEmpresa.getFisica().getPessoa().getDocumento());
                oposicao.getOposicaoPessoa().setSexo(pessoaEmpresa.getFisica().getSexo());
                sexo = pessoaEmpresa.getFisica().getSexo();
            }
        } else {
            oposicaoPessoa = oposicaoDB.pesquisaOposicaoPessoa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
            if (oposicaoPessoa.getId() != -1) {
                oposicao.setOposicaoPessoa(oposicaoPessoa);
            } else {
                if (oposicao.getJuridica().getId() != -1 && oposicao.getJuridica().getDtFechamento() == null) {
                    setDesabilitaPessoa(true);
                }
                oposicao.getOposicaoPessoa().setSexo(sexo);
            }
        }
        listaConvencaoPeriodos.clear();
        convencaoPeriodoConvencaoGrupoCidade();
        SociosDB db = new SociosDBToplink();
        socios = db.pesquisaSocioPorId(pessoaEmpresa.getFisica().getPessoa().getId());

        if (socios.getId() != -1) {
            if (socios.getServicoPessoa().isAtivo()) {
                GenericaMensagem.warn("Validação", "CPF cadastrado como sócio, inative para salvar oposição!");
            }
        }
    }

    public void convencaoPeriodoConvencaoGrupoCidade() {
        if (oposicao.getJuridica().getId() != -1) {
            if (oposicao.getConvencaoPeriodo().getId() == -1) {
                OposicaoDB oposicaoDB = new OposicaoDBToplink();
                List list = oposicaoDB.pesquisaPessoaConvencaoGrupoCidade(oposicao.getJuridica().getId());
                ConvencaoPeriodoDB di = new ConvencaoPeriodoDBTopLink();
                if (!list.isEmpty()) {
                    convencaoPeriodo = di.convencaoPeriodoConvencaoGrupoCidade((Integer) list.get(0), (Integer) list.get(1));
                    if (convencaoPeriodo.getId() == -1) {
                        convencaoPeriodo = new ConvencaoPeriodo();
                    } else {
                        oposicao.setConvencaoPeriodo(convencaoPeriodo);
                    }
                }
            } else {
                convencaoPeriodo = oposicao.getConvencaoPeriodo();
            }
        }
    }

    public Oposicao getOposicao() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            oposicao.setJuridica((Juridica) GenericaSessao.getObject("juridicaPesquisa", true));
            mensagemEmpresa = "";
            if (oposicao.getJuridica().getDtFechamento() != null) {
                mensagemEmpresa = "Empresa está inátiva!";
                return null;
            }
            listaConvencaoPeriodos.clear();
            convencaoPeriodoConvencaoGrupoCidade();
        }
        if (GenericaSessao.exists("oposicaoPesquisa")) {
            setDesabilitaPessoa(true);
            oposicao = (Oposicao) GenericaSessao.getObject("oposicaoPesquisa", true);
            listaConvencaoPeriodos.clear();
            convencaoPeriodoConvencaoGrupoCidade();
        }
        return oposicao;
    }

    public void setOposicao(Oposicao oposicao) {
        this.oposicao = oposicao;
    }

    public OposicaoPessoa getOposicaoPessoa() {
        return oposicaoPessoa;
    }

    public void setOposicaoPessoa(OposicaoPessoa oposicaoPessoa) {
        this.oposicaoPessoa = oposicaoPessoa;
    }

    public ConvencaoPeriodo getConvencaoPeriodo() {
        return convencaoPeriodo;
    }

    public void setConvencaoPeriodo(ConvencaoPeriodo convencaoPeriodo) {
        this.convencaoPeriodo = convencaoPeriodo;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMensagemEmpresa(String mensagemEmpresa) {
        this.mensagemEmpresa = mensagemEmpresa;
    }

    public String getMsgEmpresa() {
        return mensagemEmpresa;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getValorPesquisa() {
        return valorPesquisa;
    }

    public void setValorPesquisa(String valorPesquisa) {
        this.valorPesquisa = valorPesquisa;
    }

    public boolean isDesabilitaPessoa() {
        return desabilitaPessoa;
    }

    public void setDesabilitaPessoa(boolean desabilitaPessoa) {
        this.desabilitaPessoa = desabilitaPessoa;
    }

    public List<Oposicao> getListaOposicaos() {
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        if (listaOposicaos.isEmpty()) {
            if (removeFiltro) {
                if (porPesquisa.equals("todos")) {
                    return new ArrayList();
                }
            }
            listaOposicaos = oposicaoDB.pesquisaOposicao(descricaoPesquisa, porPesquisa, comoPesquisa);
        }
        return listaOposicaos;
    }

    public void setListaOposicaos(List<Oposicao> listaOposicaos) {
        this.listaOposicaos = listaOposicaos;
    }

    public void acaoPesquisaInicial() {
        listaOposicaos.clear();
        setComoPesquisa("Inicial");
    }

    public void acaoPesquisaParcial() {
        listaOposicaos.clear();
        setComoPesquisa("Parcial");
    }

    public String getPorPesquisa() {
        if (porPesquisa.equals("todos")) {
            descricaoPesquisa = "";
        }
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

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public String getMascara() {
        return Mask.getMascaraPesquisa(porPesquisa, true);
    }

    public boolean isRemoveFiltro() {
        if (GenericaSessao.exists("removeFiltro")) {
            removeFiltro = GenericaSessao.getBoolean("removeFiltro", true);
        }
        return removeFiltro;
    }

    public void setRemoveFiltro(boolean removeFiltro) {
        this.removeFiltro = removeFiltro;
    }
}
