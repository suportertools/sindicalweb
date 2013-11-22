package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Suspencao;
import br.com.rtools.associativo.db.SuspencaoDB;
import br.com.rtools.associativo.db.SuspencaoDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class SuspencaoBean {

    private Suspencao suspencao = new Suspencao();
    private String mensagem = "";
    private List<Suspencao> listaSuspencao = new ArrayList();

    public void salvar() {
        if (suspencao.getPessoa().getId() == -1) {
            mensagem = "Pesquise um sócio para Suspender!";
            return;
        }
        if (suspencao.getDataInicial().length() < 7 || suspencao.getDataFinal().length() < 7) {
            mensagem = "Data inválida!";
            return;
        }
        if (DataHoje.converteDataParaInteger(suspencao.getDataInicial())
                > DataHoje.converteDataParaInteger(suspencao.getDataFinal())) {
            mensagem = "Data inicial não pode ser maior que data final!";
            return;
        }
        if (suspencao.getMotivo().equals("") || suspencao.getMotivo() == null) {
            mensagem = "Digite um motivo de Suspensão!";
            return;
        }
        SuspencaoDB suspencaoDB = new SuspencaoDBToplink();
        if (suspencaoDB.existeSuspensaoSocio(suspencao)) {
            mensagem = "Sócio já encontra-se suspenso!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (suspencao.getId() == -1) {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(suspencao)) {
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Suspensão salva com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao salvar suspensão!";
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(suspencao)) {
                salvarAcumuladoDB.comitarTransacao();
                mensagem = "Suspensão atualizada com sucesso.";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar suspensão!";
            }
        }
    }

    public void excluir() {
        if (suspencao.getId() == -1) {
            mensagem = "Selecione uma suspensão para ser excluída!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        suspencao = (Suspencao) salvarAcumuladoDB.pesquisaCodigo(suspencao.getId(), "Suspencao");
        salvarAcumuladoDB.abrirTransacao();
        if (salvarAcumuladoDB.deletarObjeto(suspencao)) {
            salvarAcumuladoDB.comitarTransacao();
            suspencao = new Suspencao();
            mensagem = "Suspensão deletada com sucesso!";
            listaSuspencao.clear();
        } else {
            salvarAcumuladoDB.desfazerTransacao();
            mensagem = "Erro ao deletar suspensão!";
        }
    }

    public void novo() {
        suspencao = new Suspencao();
        mensagem = "";
    }

    public String editar(Suspencao s) {
        suspencao = s;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pessoaPesquisa", suspencao.getPessoa());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return "suspencao";
    }

    public List<Suspencao> getListaSuspencao() {
        if (listaSuspencao.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaSuspencao = (List<Suspencao>) salvarAcumuladoDB.listaObjeto("Suspencao", true);
        }
        return listaSuspencao;
    }

    public void setListaSuspencao(List<Suspencao> listaSuspencao) {
        this.listaSuspencao = listaSuspencao;
    }

    public Suspencao getSuspencao() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            suspencao.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return suspencao;
    }

    public void setSuspencao(Suspencao suspencao) {
        this.suspencao = suspencao;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
