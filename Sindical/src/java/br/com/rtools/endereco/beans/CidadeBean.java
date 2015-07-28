package br.com.rtools.endereco.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.endereco.dao.CidadeDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.PessoaEndereco;
import br.com.rtools.pessoa.db.PessoaEnderecoDB;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CidadeBean implements Serializable {

    private Cidade cidade;
    private String message;
    private String comoPesquisa;
    private List<Cidade> listCidade;
    private String descricaoCidadePesquisa;
    private String descricaoUFPesquisa;

    @PostConstruct
    public void init() {
        cidade = new Cidade();
        message = "";
        comoPesquisa = "";
        listCidade = new ArrayList<Cidade>();
        descricaoCidadePesquisa = "";
        descricaoUFPesquisa = "";
        PessoaEnderecoDB db = new PessoaEnderecoDBToplink();
        PessoaEndereco pe = db.pesquisaEndPorPessoaTipo(1, 3);
        cidade.setUf(pe.getEndereco().getCidade().getUf());
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cidadeBean");

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void save() {
        if (cidade.getCidade().isEmpty()) {
            message = "Digite uma Cidade por favor!";
            GenericaMensagem.warn("Erro", message);
            return;
        }

        DaoInterface di = new Dao();
        NovoLog log = new NovoLog();

        di.openTransaction();
        if (cidade.getId() == -1) {
            if (di.save(cidade)) {
                message = "Cidade salva com Sucesso!";
                GenericaMensagem.info("Salvo", message);
                listCidade.add(cidade);
                log.save("ID: " + cidade.getId() + " - Cidade: " + cidade.getCidade() + " - UF: " + cidade.getUf());
                clear();
            } else {
                message = "Erro ao salvar Cidade!";
                GenericaMensagem.warn("Erro", message);
                di.rollback();
            }
        } else {
            Cidade c = (Cidade) di.find(cidade);
            String beforeUpdate = "ID: " + c.getId() + " - Cidade: " + c.getCidade() + " - UF: " + c.getUf();
            if (di.update(cidade)) {
                message = "Registro atualizado!";
                GenericaMensagem.info("Atualizado", message);
                log.update(beforeUpdate, ""
                        + "ID: " + cidade.getId() + " - Cidade: " + cidade.getCidade() + " - UF: " + cidade.getUf()
                );
                clear();
            } else {
                message = "Erro ao atualizar!";
                GenericaMensagem.warn("Erro", message);
                di.rollback();
            }
        }
        di.commit();
    }

    public void clear() {
        GenericaSessao.remove("cidadeBean");
    }

    public void delete(Cidade ci) {
        NovoLog log = new NovoLog();
        DaoInterface di = new Dao();
        if (ci.getId() != -1) {
            di.openTransaction();
            if (di.delete(ci)) {
                message = "Cidade Excluida com Sucesso!";
                listCidade.clear();
                GenericaMensagem.info("Sucesso", message);
                log.delete("ID: " + ci.getId() + " - Cidade: " + ci.getCidade() + " - UF: " + ci.getUf());
            } else {
                message = "Cidade não pode ser Excluida!";
                GenericaMensagem.warn("Erro", message);
                di.rollback();
            }
        }
        cidade = new Cidade();
        di.commit();
    }

    public String editPagina(Cidade ci) {
        cidade = ci;
        return null;
    }

    public String edit(Cidade ci) {
        String urlRetorno = null;
        cidade = ci;
        GenericaSessao.put("cidadePesquisa", cidade);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno")) {
            if (!GenericaSessao.getString("urlRetorno").equals("menuPrincipal")) {
                urlRetorno = GenericaSessao.getString("urlRetorno");
            } else {
                PF.openDialog("dlg_save");
            }
        } else {
            PF.openDialog("dlg_save");
        }
        return urlRetorno;
    }

    public void setListCidade(List<Cidade> listCidade) {
        this.listCidade = listCidade;
    }

    public List<Cidade> getListCidade() {
        if (listCidade.isEmpty()) {
            CidadeDao db = new CidadeDao();
            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
            if (cidade.getCidade().isEmpty()) {
                List lgc = dbCids.pesquisaCidadesBase();
                if (!lgc.isEmpty()) {
                    listCidade.addAll(lgc);
                }
                PessoaEnderecoDB dbp = new PessoaEnderecoDBToplink();
                PessoaEndereco pe = dbp.pesquisaEndPorPessoaTipo(1, 3);
                cidade.setUf(pe.getEndereco().getCidade().getUf());
            } else {
                listCidade = db.pesquisaCidadePorCidade(cidade.getCidade(), getComoPesquisa());
            }

        }
        return listCidade;
    }

    public void refreshForm() {
    }

    public void acaoPesquisaInicial() {
        listCidade.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listCidade.clear();
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
