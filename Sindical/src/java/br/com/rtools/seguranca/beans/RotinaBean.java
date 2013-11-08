package br.com.rtools.seguranca.beans;

import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class RotinaBean implements Serializable {

    private Rotina rotina = new Rotina();
    private String mensagem = "";
    private String descricaoPesquisa = "";
    private List<Rotina> listaRotina = new ArrayList<Rotina>();

    public void salvar() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (rotina.getId() == -1) {
            if (rotina.getRotina().equals("")) {
                mensagem = "Digite uma Rotina!";
            } else {
                RotinaDB rotinaDB = new RotinaDBToplink();
                if (!rotinaDB.existeRotina(rotina)) {
                    salvarAcumuladoDB.abrirTransacao();
                    if (salvarAcumuladoDB.inserirObjeto(rotina)) {
                        salvarAcumuladoDB.comitarTransacao();
                        mensagem = "Registro salvo com sucesso";
                        listaRotina.clear();
                    } else {
                        salvarAcumuladoDB.desfazerTransacao();
                        mensagem = "Erro ao inserir registro!";
                    }
                } else {
                    mensagem = "Esta Rotina já existe no Sistema.";
                }
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(rotina)) {
                listaRotina.clear();
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Registro atualizado com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar registro!";
            }
        }
    }

    public void novo() {
        rotina = new Rotina();
        descricaoPesquisa = "";
    }

    public void excluir() {
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (rotina.getId() != -1) {
            rotina = (Rotina) salvarAcumuladoDB.pesquisaCodigo(rotina.getId(), "Rotina");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(rotina)) {
                salvarAcumuladoDB.comitarTransacao();
                listaRotina.clear();
                mensagem = "Registro excluido com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Esta registro não pode ser excluido!";
            }
        }
        rotina = new Rotina();
    }

    public String editar(Rotina r) {
        rotina = r;
        GenericaSessao.put("rotinaPesquisa", rotina);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno")) {
            if (!GenericaSessao.getString("urlRetorno").equals("menuPrincipal")) {
                return (String) GenericaSessao.getString("urlRetorno");                
            }
        } else {
            return "rotina";
        }
        return null;
    }

    public List<Rotina> getListaRotina() {
        if (listaRotina.isEmpty()) {
            RotinaDB db = new RotinaDBToplink();
            if (descricaoPesquisa.equals("")) {
                listaRotina = db.pesquisaTodosOrdenado();
            } else {
                listaRotina = db.pesquisaRotinaPorDescricao(descricaoPesquisa);
            }
        }
        return listaRotina;
    }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public String rotinaAtiva(boolean ativo) {
        if (ativo) {
            return "Ativo";
        }
        return "Inativo";
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
}
