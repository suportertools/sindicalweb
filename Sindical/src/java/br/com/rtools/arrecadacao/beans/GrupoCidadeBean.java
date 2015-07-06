package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.GrupoCidade;
import br.com.rtools.arrecadacao.db.GrupoCidadeDB;
import br.com.rtools.arrecadacao.db.GrupoCidadeDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GrupoCidadeBean {

    private GrupoCidade grupoCidade = new GrupoCidade();
    private String comoPesquisa = "T";
    private String descPesquisa = "";
    private String msgConfirma;
    private String linkVoltar;

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public GrupoCidadeBean() {
    }

    public GrupoCidade getGrupoCidade() {
        return grupoCidade;
    }

    public void setGrupoCidade(GrupoCidade grupoCidade) {
        this.grupoCidade = grupoCidade;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String salvar() {
        GrupoCidadeDB db = new GrupoCidadeDBToplink();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        NovoLog log = new NovoLog();
        if (grupoCidade.getId() == -1) {
            if (grupoCidade.getDescricao().equals("")) {
                msgConfirma = "Digite uma Grupo por favor!";
            } else {
                if (db.idGrupoCidade(grupoCidade) == null) {
                    sadb.abrirTransacao();
                    if (sadb.inserirObjeto(grupoCidade)) {
                        sadb.comitarTransacao();
                        msgConfirma = "Grupo salvo com Sucesso!";
                        log.save("Grupo Cidade inserido " + grupoCidade.getId() + " - " + grupoCidade.getDescricao());
                    } else {
                        sadb.desfazerTransacao();
                    }
                } else {
                    msgConfirma = "Este Grupo já existe no Sistema.";
                }
            }
        } else {
            GrupoCidade gc = (GrupoCidade) sadb.find(grupoCidade);
            String antes = "De: " + gc.getDescricao();
            sadb.abrirTransacao();
            if (sadb.alterarObjeto(grupoCidade)) {
                sadb.comitarTransacao();
                msgConfirma = "Grupo atualizado com Sucesso!";
                log.update(antes, grupoCidade.getId() + " - " + grupoCidade.getDescricao());
            } else {
                sadb.desfazerTransacao();
            }

        }
        return null;
    }

    public String novo() {
        grupoCidade = new GrupoCidade();
        msgConfirma = "";
        return "grupoCidade";
    }

    public String excluir() {
        if (grupoCidade.getId() != -1) {
            NovoLog log = new NovoLog();
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            grupoCidade = (GrupoCidade) sadb.find(grupoCidade);
            sadb.abrirTransacao();
            if (sadb.deletarObjeto(grupoCidade)) {
                sadb.comitarTransacao();
                msgConfirma = "Grupo Excluida com Sucesso!";
                log.delete(grupoCidade.getId() + " - " + grupoCidade.getDescricao());
            } else {
                sadb.desfazerTransacao();
                msgConfirma = "Grupo não pode ser Excluido!";
            }

        }
        grupoCidade = new GrupoCidade();
        return null;
    }

    public List getListaGrupoCidade() {
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        List list = sadb.listaObjeto("GrupoCidade");
        return list;
    }

    public String editar() {
        GenericaSessao.put("grupoCidadePesquisa", grupoCidade);
        GenericaSessao.put("linkClicado", true);
        linkVoltar = (String) GenericaSessao.getString("urlRetorno");
        if (linkVoltar == null) {
            return "grupoCidade";
        } else {
            return linkVoltar;
        }
    }

    public String linkVoltarPesquisaGrupoCidade() {
        linkVoltar = (String) GenericaSessao.getString("urlRetorno");
        if (linkVoltar == null) {
            return "grupoCidade";
        } else {
            GenericaSessao.remove("urlRetorno");
        }
        return linkVoltar;
    }
}
