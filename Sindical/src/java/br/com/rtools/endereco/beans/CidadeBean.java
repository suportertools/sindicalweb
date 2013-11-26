package br.com.rtools.endereco.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.db.CidadeDB;
import br.com.rtools.endereco.db.CidadeDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CidadeBean implements Serializable {

    private Cidade cidade = new Cidade();
    private String msgConfirma;
    private String comoPesquisa = "";
    private List<Cidade> listaCidade = new ArrayList();
    private String descricaoCidadePesquisa = "";
    private String descricaoUFPesquisa = "";

    public CidadeBean() {
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = db.pesquisaEndPorPessoaTipo(1, 3);
        cidade.setUf(pe.getEndereco().getCidade().getUf());
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        if (cidade.getCidade().isEmpty()) {
            msgConfirma = "Digite uma Cidade por favor!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();

        sv.abrirTransacao();
        if (cidade.getId() == -1) {
            if (sv.inserirObjeto(cidade)) {
                msgConfirma = "Cidade salva com Sucesso!";
                listaCidade.add(cidade);
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Salvo", msgConfirma));
                cidade = new Cidade();
                log.novo("Novo registro", "Cidade inserida " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Erro ao salvar Cidade!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
                return null;
            }
        } else {
            Cidade c = new Cidade();
            c = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            String antes = "De: " + c.getCidade() + " / " + c.getUf();

            if (sv.alterarObjeto(cidade)) {
                msgConfirma = "Registro atualizado!";
                //listaCidade.clear();
                cidade = new Cidade();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Atualizado", msgConfirma));
                log.novo("Atualizado", antes + " - para: " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Erro ao atualizar!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.comitarTransacao();
        return null;
    }

    public String novo() {
        cidade = new Cidade();
        return null;
    }

    public String excluir(Cidade ci) {
        NovoLog log = new NovoLog();
        cidade = ci;

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (cidade.getId() != -1) {
            sv.abrirTransacao();
            cidade = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            if (sv.deletarObjeto(cidade)) {
                msgConfirma = "Cidade Excluida com Sucesso!";
                listaCidade.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                log.novo("Excluido", cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Cidade não pode ser Excluida!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                sv.desfazerTransacao();
                return null;
            }
        }
        cidade = new Cidade();
        sv.comitarTransacao();
        return null;
    }

    public String editarPagina(Cidade ci) {
        cidade = ci;
        return null;
    }

    public String editar(Cidade ci) {
        String result = "cidade";
        cidade = ci;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cidadePesquisa", cidade);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null
                && !((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno")).equals("menuPrincipal")) {
            result = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
        return result;
    }

    public void setListaCidade(List<Cidade> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public List<Cidade> getListaCidade() {
        if (listaCidade.isEmpty()) {
            CidadeDB db = new CidadeDBToplink();
            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
            if (cidade.getCidade().equals("")) {
                List lgc = dbCids.pesquisaCidadesBase();
                if (!lgc.isEmpty()) {
                    listaCidade.addAll(lgc);
                }
                PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
                PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 3);
                cidade.setUf(pe.getEndereco().getCidade().getUf());
            } else {
                listaCidade = db.pesquisaCidade(cidade.getUf(), cidade.getCidade(), getComoPesquisa());
            }

        }
        return listaCidade;
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        listaCidade.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listaCidade.clear();
        comoPesquisa = "P";
    }

    public String getDescricaoCidadePesquisa() {
        return descricaoCidadePesquisa;
    }

    public void setDescricaoCidadePesquisa(String descricaoCidadePesquisa) {
        this.descricaoCidadePesquisa = descricaoCidadePesquisa;
    }

    public String getDescricaoUFPesquisa() {
        return descricaoUFPesquisa;
    }

    public void setDescricaoUFPesquisa(String descricaoUFPesquisa) {
        this.descricaoUFPesquisa = descricaoUFPesquisa;
    }
}
