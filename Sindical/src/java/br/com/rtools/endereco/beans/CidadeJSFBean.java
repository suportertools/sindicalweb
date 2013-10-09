package br.com.rtools.endereco.beans;

import br.com.rtools.arrecadacao.GrupoCidades;
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
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class CidadeJSFBean {

    private Cidade cidade = new Cidade();
    private String msgConfirma;
    private String comoPesquisa = "";
    private int idIndex = -1;
    private List<Cidade> listaCidade = new ArrayList();

    public CidadeJSFBean() {
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
        NovoLog log = new NovoLog();

        sv.abrirTransacao();
        if (cidade.getId() == -1) {
            if (sv.inserirObjeto(cidade)) {
                msgConfirma = "Cidade salva com Sucesso!";
                listaCidade.clear();
                log.novo("Novo registro", "Cidade inserida " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Erro ao salvar Cidade!";
                sv.desfazerTransacao();
                return null;
            }
        } else {
            Cidade c = new Cidade();
            c = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            String antes = "De: " + c.getCidade() + " / " + c.getUf();

            if (sv.alterarObjeto(cidade)) {
                msgConfirma = "Registro atualizado!";
                listaCidade.clear();
                log.novo("Atualizado", antes + " - para: " + cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Erro ao atualizar!";
                sv.desfazerTransacao();
                return null;
            }
        }
        sv.comitarTransacao();
        return null;
    }

    public String novo() {
        cidade = new Cidade();
        return "cidade";
    }

    public String excluir() {
        NovoLog log = new NovoLog();
        cidade = (Cidade) listaCidade.get(idIndex);

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (cidade.getId() != -1) {
            cidade = (Cidade) sv.pesquisaCodigo(cidade.getId(), "Cidade");
            if (sv.deletarObjeto(cidade)) {
                msgConfirma = "Cidade Excluida com Sucesso!";
                listaCidade.clear();
                log.novo("Excluido", cidade.getId() + " - " + cidade.getCidade() + " / " + cidade.getUf());
            } else {
                msgConfirma = "Cidade não pode ser Excluida!";
            }
            sv.desfazerTransacao();
            return null;
        }
        cidade = new Cidade();
        sv.comitarTransacao();
        return null;
    }

    public String editar() {
        String result = "cidade";
        cidade = (Cidade) listaCidade.get(idIndex);
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
                    //for (int i = 0;i < lgc.size();i++){
                    listaCidade.addAll(lgc);
                    //}
                }
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}
