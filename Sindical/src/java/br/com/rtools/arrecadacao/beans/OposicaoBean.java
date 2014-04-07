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
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.PessoaEmpresa;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class OposicaoBean implements Serializable {

    private Oposicao oposicao = new Oposicao();
    private OposicaoPessoa oposicaoPessoa = new OposicaoPessoa();
    private ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
    private List<ConvencaoPeriodo> listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
    private String mensagem = "";
    private String mensagemCPF = "";
    private String mensagemEmpresa = "";
    private int idIndex = 0;
    private String sexo = "M";
    private String valorPesquisa = "";
    private boolean desabilitaPessoa = false;
    private List<Oposicao> listaOposicaos = new ArrayList<Oposicao>();
    private String porPesquisa = "todos";
    private String descricaoPesquisa = "";
    private String comoPesquisa = "";
    private Socios socios = new Socios();

    public void novo() {
        oposicao = new Oposicao();
        oposicaoPessoa = new OposicaoPessoa();
        convencaoPeriodo = new ConvencaoPeriodo();
        listaConvencaoPeriodos.clear();
        listaOposicaos.clear();
        idIndex = -1;
        sexo = "";
        porPesquisa = "todos";
        setMensagem("");
        setMensagemEmpresa("");
        setMensagemCPF("");
        setDesabilitaPessoa(false);
        valorPesquisa = "";
        setSexo("M");
    }

    public void salvar() {
        if (oposicao.getJuridica().getId() == -1) {
            mensagem = "Informar o Pessoa Jurídica!";
            return;
        }
        if (oposicao.getOposicaoPessoa().getNome().equals("")) {
            mensagem = "Informar o nome da Pessoa!";
            return;
        }
        if (oposicao.getConvencaoPeriodo().getId() == -1) {
            mensagem = "Informar o período de convenção!";
            return;
        }
        if (oposicao.getEmissao().isEmpty()) {
            mensagem = "Campo emissão não pode estar vazio!";
            return;
        }
        if (socios.getId() != -1) {
            if (socios.getServicoPessoa().isAtivo()) {
                mensagem = "CPF cadastrado como sócio, inative para salvar oposição!";
                return;
            }
        }
        if (!salvarOposicaoPessoa()) {
            mensagem = "Falha ao salvar oposição pessoa!";
            return;
        }

        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        if (oposicao.getId() == -1) {
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            if (oposicaoDB.validaOposicao(oposicao)) {
                mensagem = "Oposição já cadastrada para essa pessoa para a convenção vigente!";
                return;
            }
            if (dB.inserirObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                mensagem = "Registro salvo com sucesso.";
            } else {
                dB.desfazerTransacao();
                novo();
                mensagem = "Falha ao salvar este registro!";
            }
        } else {
            if (dB.alterarObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                mensagem = "Registro atualizado com sucesso.";
            } else {
                dB.desfazerTransacao();
                novo();
                mensagem = "Falha ao excluir este registro!";
            }
        }
    }

    public String editar(Oposicao o) {
        setDesabilitaPessoa(true);
        oposicao = o;
        sexo = oposicao.getOposicaoPessoa().getSexo();
        GenericaSessao.put("oposicaoPesquisa", oposicao);
        GenericaSessao.put("linkClicado", true);
        GenericaSessao.remove("oposicaoPesquisa");
        String urlRetorno = "oposicao";
        if (GenericaSessao.exists("urlRetorno")) {
            GenericaSessao.put("oposicaoPesquisaPor", porPesquisa);
            urlRetorno = GenericaSessao.getString("urlRetorno");
        }
        return urlRetorno;
    }

    public boolean salvarOposicaoPessoa() {
        if (oposicao.getOposicaoPessoa().getCpf().equals("___.___.___-__") || oposicao.getOposicaoPessoa().getCpf().equals("")) {
            mensagem = "Informar o CPF!";
            return false;
        }
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(oposicao.getOposicaoPessoa().getCpf()))) {
            mensagem = "CPF inválido!";
            return false;
        }
        oposicao.getOposicaoPessoa().setSexo(sexo);
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        OposicaoPessoa op = oposicaoDB.pesquisaOposicaoPessoa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
        if (op.getId() == -1) {
            if (oposicao.getOposicaoPessoa().getId() == -1) {
                if (dB.inserirObjeto(oposicao.getOposicaoPessoa())) {
                    listaConvencaoPeriodos.clear();
                    dB.comitarTransacao();
                    return true;
                } else {
                    dB.desfazerTransacao();
                    return false;
                }
            }
        } else {
            if (op.getCpf().equals(oposicao.getOposicaoPessoa().getCpf()) && op.getRg().equals(oposicao.getOposicaoPessoa().getRg()) && op.getNome().equals(oposicao.getOposicaoPessoa().getNome()) && op.getSexo().equals(oposicao.getOposicaoPessoa().getSexo())) {
                return true;
            } else {
                if (oposicao.getOposicaoPessoa().getId() != -1) {
                    oposicao.getOposicaoPessoa().setId(op.getId());
                    if (dB.alterarObjeto(oposicao.getOposicaoPessoa())) {
                        dB.comitarTransacao();
                        return true;
                    } else {
                        dB.desfazerTransacao();
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void excluir() {
        excluir(0);
    }

    public void excluir(int id) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (id > 0) {
            oposicao = (Oposicao) dB.pesquisaCodigo(id, "Oposicao");
        } else {
            oposicao = (Oposicao) dB.pesquisaCodigo(oposicao.getId(), "Oposicao");
        }
        if (oposicao.getId() != -1) {
            dB.abrirTransacao();
            if (dB.deletarObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                mensagem = "Registro excluído com sucesso.";
            } else {
                dB.desfazerTransacao();
                novo();
                mensagem = "Falha na exclusão do registro!";
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
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            List list = oposicaoDB.pesquisaPessoaConvencaoGrupoCidade(oposicao.getJuridica().getId());
            ConvencaoPeriodoDB dB = new ConvencaoPeriodoDBTopLink();
            if (!list.isEmpty()) {
                convencaoPeriodo = dB.convencaoPeriodoConvencaoGrupoCidade((Integer) list.get(0), (Integer) list.get(1));
                if (convencaoPeriodo.getId() == -1) {
                    convencaoPeriodo = new ConvencaoPeriodo();
                } else {
                    oposicao.setConvencaoPeriodo(convencaoPeriodo);
                }
            }
        }
    }

    public Oposicao getOposicao() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            oposicao.setJuridica((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa"));
            mensagemEmpresa = "";
            if (oposicao.getJuridica().getDtFechamento() != null) {
                mensagemEmpresa = "Empresa está inátiva!";
                return null;
            }
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            listaConvencaoPeriodos.clear();
            convencaoPeriodoConvencaoGrupoCidade();
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("oposicaoPesquisa") != null) {
            setDesabilitaPessoa(true);
            oposicao = (Oposicao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("oposicaoPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("oposicaoPesquisa");
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

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getMsgCPF() {
        return mensagemCPF;
    }

    public void setMensagemCPF(String mensagemCPF) {
        this.mensagemCPF = mensagemCPF;
    }

    public void setMensagemEmpresa(String mensagemEmpresa) {
        this.mensagemEmpresa = mensagemEmpresa;
    }

    public String getMsgEmpresa() {
        return mensagemEmpresa;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
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
}
