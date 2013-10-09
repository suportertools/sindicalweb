package br.com.rtools.seguranca.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.Patronal;
import br.com.rtools.arrecadacao.PatronalCnae;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDB;
import br.com.rtools.arrecadacao.db.CnaeConvencaoDBToplink;
import br.com.rtools.arrecadacao.db.WebREPISDB;
import br.com.rtools.arrecadacao.db.WebREPISDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class RegistroPatronalJSFBean {

    private Patronal patronal = new Patronal();
    private String msg = "";
    private String msgConfirma = "";
    private String msgErro = "";
    private List<Patronal> listaPatronal = new ArrayList();
    private int idIndex = -1;
    private String descPesquisa = "";
    private String porPesquisa = "nome";
    private String comoPesquisa = "";
    private List<PatronalCnae> listaPatronalCnae = new ArrayList();
    private Cnae cnae = new Cnae();

    public String novo() {
        patronal = new Patronal();
        msg = "";
        listaPatronalCnae.clear();
        return "registroPatronal";
    }

    public void limpar() {
    }

    public String salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        setMsgErro("");
        if (getPatronal().getPessoa().getId() == -1) {
            setMsg("Pesquisar pessoa!");
            return null;
        }
        if (getPatronal().getConvencao().getId() == -1) {
            setMsg("Pesquisar convenção!");
            return null;
        }
        if (getPatronal().getGrupoCidade().getId() == -1) {
            setMsg("Pesquisar grupo cidade!");
            return null;
        }
        if (getPatronal().getId() == -1) {
            if (validaPatronal(getPatronal().getPessoa().getId(),
                    getPatronal().getConvencao().getId(),
                    getPatronal().getGrupoCidade().getId()).size() > 0) {
                setMsg("Pessoa já cadastrada para essa convenção / grupo cidade!");
                return null;
            }
        }
        NovoLog log = new NovoLog();
        sv.abrirTransacao();
        if (getPatronal().getId() == -1) {
            if (!sv.inserirObjeto(patronal)) {
                sv.desfazerTransacao();
                setMsg("Erro ao salvar patronal!");
                return null;
            }
            log.novo("Novo registro", "Mensagem patronal inserido " + getPatronal().getId() + " - Pessoa: " + getPatronal().getPessoa().getId() + " - " + getPatronal().getPessoa().getNome() + " - Convenção: " + getPatronal().getConvencao().getId() + " - " + getPatronal().getConvencao().getDescricao() + " - Grupo Cidade: " + getPatronal().getGrupoCidade().getId() + " - " + getPatronal().getGrupoCidade().getDescricao());
            setMsg("Patronal salvo com Sucesso!");
        } else {
            if (!sv.alterarObjeto(patronal)) {
                sv.desfazerTransacao();
                setMsg("Erro ao atualizar patronal!");
                return null;
            }

            Patronal patro = new Patronal();
            patro = (Patronal) sv.pesquisaCodigo(getPatronal().getId(), "Patronal");
            String antes = "De - Referencia: " + patro.getId() + " Pessoa: " + patro.getPessoa().getId() + " - " + patro.getPessoa().getNome() + " - Convenção: " + patro.getConvencao().getId() + " - " + patro.getConvencao().getDescricao() + " - Grupo Cidade: " + patro.getGrupoCidade().getId() + " - " + patro.getGrupoCidade().getDescricao();
            log.novo("Atualizado", antes + " - para: " + getPatronal().getPessoa().getId() + " - " + getPatronal().getPessoa().getNome() + " - Convenção: " + getPatronal().getConvencao().getId() + " - " + getPatronal().getConvencao().getDescricao() + " - Grupo Cidade: " + getPatronal().getGrupoCidade().getId() + " - " + getPatronal().getGrupoCidade().getDescricao());
            setMsg("Patronal atualizado com Sucesso!");
        }



        for (int i = 0; i < listaPatronalCnae.size(); i++) {
            if (listaPatronalCnae.get(i).getId() == -1) {
                if (!sv.inserirObjeto(listaPatronalCnae.get(i))) {
                    msg = "Erro ao salvar Cnae!";
                    sv.desfazerTransacao();
                    return null;
                }
            }
        }

        sv.comitarTransacao();
        return null;
    }

    public List validaPatronal(int idPessoa, int idConvencao, int idGCidade) {
        FilialDB db = new FilialDBToplink();
        List result = null;
        result = db.pesquisaPessoaConvencaoGCidade(idPessoa, idConvencao, idGCidade);
        return result;
    }

    public String editar() {
        patronal = (Patronal) getListaPatronal().get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("patronalPesquisa", getPatronal());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public String excluirCnae(int index) {
        if (listaPatronalCnae.get(index).getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            sv.abrirTransacao();

            if (!sv.deletarObjeto(sv.pesquisaCodigo(listaPatronalCnae.get(index).getId(), "PatronalCnae"))) {
                msg = "Erro ao excluir Cnae!";
                sv.desfazerTransacao();
                return null;
            }

            sv.comitarTransacao();
            listaPatronalCnae.remove(index);
            msg = "Excluído com sucesso!";
        } else {
            listaPatronalCnae.remove(index);
        }
        return null;
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        if (patronal.getId() > 0) {
            sv.abrirTransacao();
            setPatronal((Patronal) sv.pesquisaCodigo(getPatronal().getId(), "Patronal"));
            if (sv.deletarObjeto(getPatronal())) {
                sv.comitarTransacao();
                setMsg("Patronal Excluído com sucesso!");
                log.novo("Excluido", getPatronal().getId() + " - Pessoa: " + getPatronal().getPessoa().getId() + " - " + getPatronal().getPessoa().getNome());
            } else {
                sv.desfazerTransacao();
                setMsg("Patronal não pode ser excluido!");
            }
        }
        setPatronal(new Patronal());
        return "registroPatronal";
    }

    public Patronal getPatronal() {
        if ((Patronal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("patronalPesquisa") != null) {
            patronal = (Patronal) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("patronalPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("patronalPesquisa");
        }
        if ((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            patronal.setPessoa(((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa")).getPessoa());
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        if ((Convencao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("convencaoPesquisa") != null) {
            patronal.setConvencao((Convencao) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("convencaoPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convencaoPesquisa");
        }
        if ((GrupoCidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa") != null) {
            patronal.setGrupoCidade((GrupoCidade) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("simplesPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        }
        return patronal;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
        listaPatronal.clear();
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
        listaPatronal.clear();
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getMsgErro() {
        return msgErro;
    }

    public void setMsgErro(String msgErro) {
        this.msgErro = msgErro;
    }

    public List<Patronal> getListaPatronal() {
        if (listaPatronal.isEmpty()) {
            FilialDB db = new FilialDBToplink();
            listaPatronal = db.pesquisaPessoaPatronal(getDescPesquisa(), getPorPesquisa(), getComoPesquisa());
        }
        return listaPatronal;
    }

    public void setListaPatronal(List<Patronal> listaPatronal) {
        this.setListaPatronal(listaPatronal);
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
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

    public String addCnae() {
        if (patronal.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            if (patronal.getGrupoCidade().getId() == -1) {
                msg = "Pesquise um Grupo Cidade!";
                return null;
            }

            if (cnae.getId() == -1) {
                msg = "Pesquise um Cnae!";
                return null;
            }

            WebREPISDB db = new WebREPISDBToplink();

            if (!db.pesquisaCnaePermitido(cnae.getId(), patronal.getGrupoCidade().getId())) {
                msg = "Cnae já existente em outro Grupo Cidade!";
                return null;
            }

            sv.abrirTransacao();
            PatronalCnae pc = new PatronalCnae(-1, patronal, cnae);

            if (!sv.inserirObjeto(pc)) {
                msg = "Erro ao inserir patronal cnae!";
                sv.desfazerTransacao();
                return null;
            } else {
                msg = "Inserido com sucesso!";
                listaPatronalCnae.add(pc);
            }
            sv.comitarTransacao();
            cnae = new Cnae();
        }
        return null;
    }

    public List<PatronalCnae> getListaPatronalCnae() {
        if (listaPatronalCnae.isEmpty() && patronal.getConvencao().getId() != -1) {
            CnaeConvencaoDB db = new CnaeConvencaoDBToplink();
            List<PatronalCnae> listap = db.listaCnaePorPatronal(patronal.getId());
            if (!listap.isEmpty()) {
                listaPatronalCnae.addAll(listap);
            } else {
                List<Cnae> listac = db.listaCnaePorConvencao(patronal.getConvencao().getId());
                if (!listac.isEmpty()) {
                    for (int i = 0; i < listac.size(); i++) {
                        listaPatronalCnae.add(new PatronalCnae(-1, patronal, listac.get(i)));
                    }
                }
            }
        }
        return listaPatronalCnae;
    }

    public void setListaPatronalCnae(List<PatronalCnae> listaPatronalCnae) {
        this.listaPatronalCnae = listaPatronalCnae;
    }

    public Cnae getCnae() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado") != null) {
            cnae = (Cnae) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("cnaePesquisado");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
        }
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }
}
