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
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;

public class OposicaoJSFBean {

    private Oposicao oposicao = new Oposicao();
    private OposicaoPessoa oposicaoPessoa = new OposicaoPessoa();
    private ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
    private List<ConvencaoPeriodo> listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
    private String msg = "";
    private String msgCPF = "";
    private String msgEmpresa = "";
    private int idIndex = 0;
    private String sexo = "M";
    private String valorPesquisa = "";
    private boolean desabilitaPessoa = false;
    private List<Oposicao> listaOposicaos = new ArrayList<Oposicao>();
    private String porPesquisa = "todos";
    private String descricaoPesquisa = "";
    private String comoPesquisa = "";
    private Socios socios = new Socios();

    public String novo() {
        oposicao = new Oposicao();
        oposicaoPessoa = new OposicaoPessoa();
        convencaoPeriodo = new ConvencaoPeriodo();
        listaConvencaoPeriodos.clear();
        listaOposicaos.clear();
        idIndex = -1;
        sexo = "";
        porPesquisa = "todos";
        setMsg("");
        setMsgEmpresa("");
        setMsgCPF("");
        setDesabilitaPessoa(false);
        valorPesquisa = "";
        setSexo("M");
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("oposicaoBean", new Oposicao());
        return "oposicao";
    }

    public String salvar() {
        setMsg("");
        if (oposicao.getJuridica().getId() == -1) {
            msg = "Informar o Pessoa Jurídica!";
            return null;
        }
        if (oposicao.getOposicaoPessoa().getNome().equals("")) {
            msg = "Informar o nome da Pessoa!";
            return null;
        }
        if (oposicao.getConvencaoPeriodo().getId() == -1) {
            msg = "Informar o período de convenção!";
            return null;
        }
        if (socios.getId() != -1) {
            if (socios.getServicoPessoa().isAtivo()) {
                msg = "CPF cadastrado como sócio, inative para salvar oposição!";
                return null;
            }
        }
        if (!salvarOposicaoPessoa()) {
            msg = "Falha ao salvar oposição pessoa!";
            return null;
        }

        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        if (oposicao.getId() == -1) {
            OposicaoDB oposicaoDB = new OposicaoDBToplink();
            if (oposicaoDB.validaOposicao(oposicao)) {
                msg = "Oposição já cadastrada para essa pessoa para a convenção vigente!";
                return null;
            }
            if (dB.inserirObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                msg = "Registro salvo com sucesso.";
                return null;
            } else {
                dB.desfazerTransacao();
                novo();
                msg = "Falha ao salvar este registro!";
                return null;
            }
        } else {
            if (dB.alterarObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                msg = "Registro atualizado com sucesso.";
                return null;
            } else {
                dB.desfazerTransacao();
                novo();
                msg = "Falha ao excluir este registro!";
                return null;
            }
        }
    }

    public String editar(int id) {
        setDesabilitaPessoa(true);
        oposicao = (Oposicao) getListaOposicaos().get(id);
        sexo = oposicao.getOposicaoPessoa().getSexo();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("oposicaoPesquisa", oposicao);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("oposicaoPesquisa");
        return "oposicao";
    }

    public boolean salvarOposicaoPessoa() {
        if (oposicao.getOposicaoPessoa().getCpf().equals("___.___.___-__") || oposicao.getOposicaoPessoa().getCpf().equals("")) {
            msg = "Informar o CPF!";
            return false;
        }
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(oposicao.getOposicaoPessoa().getCpf()))) {
            msg = "CPF inválido!";
            return false;
        }
        oposicao.getOposicaoPessoa().setSexo(sexo);
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        dB.abrirTransacao();
        OposicaoPessoa op = new OposicaoPessoa();
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        op = oposicaoDB.pesquisaOposicaoPessoa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
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

    public String excluir(int id) {
        setMsg("");
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (id > 0) {
            oposicao = (Oposicao) dB.pesquisaCodigo(id, "Oposicao");
        } else {
            oposicao = (Oposicao) dB.pesquisaCodigo(oposicao.getId(), "Oposicao");
        }
        dB.abrirTransacao();
        if (oposicao.getId() != -1) {
            if (dB.deletarObjeto(oposicao)) {
                dB.comitarTransacao();
                novo();
                msg = "Registro excluído com sucesso.";
                return null;
            } else {
                dB.desfazerTransacao();
                novo();
                msg = "Falha na exclusão do registro!";
                return null;
            }
        }
        return "oposicao";
    }

    public String consultaPessoa() {
        msgCPF = "";
        boolean isVerificaDocumento = false;
        if (oposicao.getOposicaoPessoa().getCpf().equals("___.___.___-__") || oposicao.getOposicaoPessoa().getCpf().equals("")) {
            isVerificaDocumento = true;
            msgCPF = "";
        }
        if (!oposicao.getOposicaoPessoa().getRg().equals("")) {
            isVerificaDocumento = false;
        }
        if (isVerificaDocumento == true) {
            msgCPF = "";
            return null;
        }
        if (oposicao.getOposicaoPessoa().getId() != -1) {
            msgCPF = "";
            return null;
        }
        if (!ValidaDocumentos.isValidoCPF(AnaliseString.extrairNumeros(oposicao.getOposicaoPessoa().getCpf()))) {
            msgCPF = "CPF inválido!";
            return null;
        }
        OposicaoDB oposicaoDB = new OposicaoDBToplink();
        PessoaEmpresa pessoaEmpresa = new PessoaEmpresa();
        pessoaEmpresa = oposicaoDB.pesquisaPessoaFisicaEmpresa(oposicao.getOposicaoPessoa().getCpf(), oposicao.getOposicaoPessoa().getRg());
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
                msgCPF = "CPF cadastrado como sócio, inative para salvar oposição!";
            }
        }
        return "oposicao";
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
            msgEmpresa = "";
            if (oposicao.getJuridica().getDtFechamento() != null) {
                msgEmpresa = "Empresa está inátiva!";
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

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgCPF() {
        return msgCPF;
    }

    public void setMsgCPF(String msgCPF) {
        this.msgCPF = msgCPF;
    }

    public void setMsgEmpresa(String msgEmpresa) {
        this.msgEmpresa = msgEmpresa;
    }

    public String getMsgEmpresa() {
        return msgEmpresa;
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
        setComoPesquisa("Inicial");
        listaOposicaos.clear();
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("Parcial");
        listaOposicaos.clear();
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

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
}
