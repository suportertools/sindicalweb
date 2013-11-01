package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Convenio;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.ConvenioDB;
import br.com.rtools.associativo.db.ConvenioDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Juridica;
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
public class ConvenioBean implements Serializable {

    private Convenio convenio = new Convenio();
    private String mensagem;
    private List<Convenio> listaConvenio = new ArrayList();
    private boolean orderJuridica = false;
    private boolean orderGrupo = false;
    private boolean orderSubGrupo = false;

    public void salvar() {
        if (convenio.getJuridica().getPessoa().getId() == -1) {
            mensagem = "Pesquisar uma empresa!";
            return;
        }
        if (convenio.getSubGrupoConvenio().getId() == -1) {
            mensagem = "Pesquisar um subgrupo!";
            return;
        }
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        if (convenio.getId() == -1) {
            ConvenioDB db = new ConvenioDBToplink();
            if (db.existeSubGrupoEmpresa(convenio)) {
                mensagem = "Convênio já existe!";
                return;
            }
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.inserirObjeto(convenio)) {
                salvarAcumuladoDB.comitarTransacao();
                listaConvenio.clear();
                mensagem = "Registro inserido com sucesso";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                NovoLog novoLog = new NovoLog();
                novoLog.novo("Inserir - ID:", convenio.getId() + " - Empresa:" + convenio.getJuridica().getPessoa().getNome() + " - Empresa:" + convenio.getSubGrupoConvenio().getDescricao());
                listaConvenio.clear();
                mensagem = "Erro ao adicionar este registro!";
            }
        } else {
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.alterarObjeto(convenio)) {
                salvarAcumuladoDB.comitarTransacao();
                listaConvenio.clear();
                mensagem = "Registro atualizado com sucesso";
                listaConvenio.clear();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar este registro!";
            }
        }
    }

    public void novo() {
        listaConvenio.clear();
        convenio = new Convenio();
        mensagem = "";
    }

    public void remover(Convenio c) {
        if (c.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            convenio = (Convenio) salvarAcumuladoDB.pesquisaCodigo(c.getId(), "Convenio");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(convenio)) {
                salvarAcumuladoDB.comitarTransacao();
                NovoLog novoLog = new NovoLog();
                novoLog.novo("Remover - ID:", convenio.getId() + " - Empresa:" + convenio.getJuridica().getPessoa().getNome() + " - Empresa:" + convenio.getSubGrupoConvenio().getDescricao());
                listaConvenio.clear();
                mensagem = "Registro removido com sucesso";
                convenio = new Convenio();
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Falha ao remover este registro!";
            }
        } else {
            mensagem = "Pesquisar registro a ser excluído!";
        }
    }

    public void editar(Convenio c) {
        convenio = c;
    }

    public Convenio getConvenio() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            convenio.setJuridica((Juridica) GenericaSessao.getObject("juridicaPesquisa", true));
        }
        if (GenericaSessao.exists("subGrupoConvenioPesquisa")) {
            convenio.setSubGrupoConvenio((SubGrupoConvenio) GenericaSessao.getObject("subGrupoConvenioPesquisa", true));
        }
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<Convenio> getListaConvenio() {
        if (listaConvenio.isEmpty()) {
            ConvenioDB db = new ConvenioDBToplink();
            if (convenio.getJuridica().getId() != -1) {
                listaConvenio = db.listaTodosPorPessoa(orderJuridica, orderGrupo, orderSubGrupo, convenio);
            } else {
                listaConvenio = db.listaTodos(orderJuridica, orderGrupo, orderSubGrupo);
            }
        }
        return listaConvenio;
    }

    public void setListaConvenio(List<Convenio> listaConvenio) {
        this.listaConvenio = listaConvenio;
    }

    public boolean isOrderJuridica() {
        return orderJuridica;
    }

    public void setOrderJuridica(boolean orderJuridica) {
        this.orderJuridica = orderJuridica;
    }

    public boolean isOrderGrupo() {
        return orderGrupo;
    }

    public void setOrderGrupo(boolean orderGrupo) {
        this.orderGrupo = orderGrupo;
    }

    public boolean isOrderSubGrupo() {
        return orderSubGrupo;
    }

    public void setOrderSubGrupo(boolean orderSubGrupo) {
        this.orderSubGrupo = orderSubGrupo;
    }
}
