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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class CidadeBean implements Serializable {

    private Cidade cidade = new Cidade();
    private String msgConfirma;
    private String comoPesquisa = "";
    private String descricaoCidadePesquisa = "";
    private String descricaoUFPesquisa = "";
    private List<Cidade> listaCidade = new ArrayList();

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
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        CidadeDB cidadeDB = new CidadeDBToplink();
        NovoLog log = new NovoLog();
        if (cidade.getId() == -1) {
            List list = cidadeDB.pesquisaCidade(cidade.getCidade(), cidade.getUf());
            if (!list.isEmpty()) {
                msgConfirma = "Registro já existe!";
                return null;
            }
            sv.abrirTransacao();
            if (sv.inserirObjeto(cidade)) {
                sv.comitarTransacao();
                msgConfirma = "Cidade salva com Sucesso!";
                listaCidade.clear();
                log.novo("Novo registro", "Cidade inserida " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                sv.desfazerTransacao();
                msgConfirma = "Erro ao salvar Cidade!";
            }
        } else {
            Cidade c = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            String antes = "De: " + c.getCidade() + " / " + c.getUf();
            sv.abrirTransacao();
            if (sv.alterarObjeto(cidade)) {
                sv.comitarTransacao();
                msgConfirma = "Registro atualizado!";
                listaCidade.clear();
                log.novo("Atualizado", antes + " - para: " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Erro ao atualizar!";
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String novo() {
        cidade = new Cidade();
        return null;
    }

    public String excluir(Cidade c) {
        if (c.getId() != -1) {
            cidade = c;
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            cidade = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            sv.abrirTransacao();
            if (sv.deletarObjeto(cidade)) {
                sv.comitarTransacao();
                msgConfirma = "Cidade Excluida com Sucesso!";
                cidade = new Cidade();
                listaCidade.clear();
                NovoLog log = new NovoLog();
                log.novo("Excluido", cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                sv.desfazerTransacao();
                msgConfirma = "Cidade não pode ser Excluida!";
            }
        }
        return null;
    }

    public String editar(Cidade c) {
        String result = null;
        cidade = c;
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
            if (descricaoCidadePesquisa.equals("")) {
                List lgc = dbCids.pesquisaCidadesBase();
                if (!lgc.isEmpty()) {
                    listaCidade.addAll(lgc);
                }
            } else {
                listaCidade = db.pesquisaCidade(descricaoUFPesquisa, descricaoCidadePesquisa, getComoPesquisa());
            }
        }
        return listaCidade;
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
