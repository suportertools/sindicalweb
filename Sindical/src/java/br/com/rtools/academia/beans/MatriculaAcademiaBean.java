package br.com.rtools.academia.beans;

import br.com.rtools.associativo.MatriculaAcademia;
import br.com.rtools.associativo.beans.ServicoPessoaBean;
import br.com.rtools.associativo.db.MatriculaAcademiaDB;
import br.com.rtools.associativo.db.MatriculaAcademiaDBToplink;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class MatriculaAcademiaBean implements Serializable {

    private MatriculaAcademia matriculaAcademia;
    private ServicoPessoaBean servicoPessoaJSFBean;
    private String descPesquisa;
    private String porPesquisa;
    private String comoPesquisa;
    private String mensagem;
    private List<MatriculaAcademia> listaAcademia;

    public MatriculaAcademiaBean() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("servicoPessoaBean", new ServicoPessoaBean());
        servicoPessoaJSFBean = ((ServicoPessoaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("servicoPessoaBean"));
        servicoPessoaJSFBean.setRenderServicos(true);
        matriculaAcademia = new MatriculaAcademia();
        descPesquisa = "";
        porPesquisa = "";
        comoPesquisa = "";
        mensagem = "";
        listaAcademia.clear();
    }

    public void salvar() {
        SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
        if (servicoPessoaJSFBean.getServicoPessoa().getPessoa().getId() == -1) {
            mensagem = "Pesquise uma Pessoa!";
            return;
        }
        dbSalvar.abrirTransacao();
        if (servicoPessoaJSFBean.getServicoPessoa().getId() == -1) {
            mensagem = servicoPessoaJSFBean.salvarServicoPessoa(null, dbSalvar);
            if (mensagem.isEmpty()) {
                matriculaAcademia.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
                matriculaAcademia.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                if (dbSalvar.inserirObjeto(matriculaAcademia)) {
                    dbSalvar.comitarTransacao();
                    mensagem = "Matricula salva com Sucesso!";
                } else {
                    dbSalvar.desfazerTransacao();
                    mensagem = "Erro ao Salvar Matricula!";
                }
            } else {
                dbSalvar.desfazerTransacao();
            }
        } else {
            mensagem = servicoPessoaJSFBean.atualizarServicoPessoa(null, dbSalvar);
            if (mensagem.isEmpty()) {
                matriculaAcademia.setServicoPessoa(servicoPessoaJSFBean.getServicoPessoa());
                matriculaAcademia.setUsuario((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
                if (dbSalvar.alterarObjeto(matriculaAcademia)) {
                    dbSalvar.comitarTransacao();
                    mensagem = "Matricula atualizada com Sucesso!";
                } else {
                    dbSalvar.desfazerTransacao();
                    mensagem = "Erro ao atualizar Matricula!";
                }
            } else {
                dbSalvar.desfazerTransacao();
            }
        }
    }

    public void novo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaAcademiaBean", new MatriculaAcademiaBean());
        matriculaAcademia = new MatriculaAcademia();
        mensagem = "";
    }

    public void excluir() {
        if (servicoPessoaJSFBean.getServicoPessoa().getId() != -1) {
            SalvarAcumuladoDB dbSalvar = new SalvarAcumuladoDBToplink();
            dbSalvar.abrirTransacao();
            if (dbSalvar.deletarObjeto((MatriculaAcademia) dbSalvar.pesquisaCodigo(matriculaAcademia.getId(), "MatriculaAcademia"))) {
                if (dbSalvar.deletarObjeto((ServicoPessoa) dbSalvar.pesquisaCodigo(servicoPessoaJSFBean.getServicoPessoa().getId(), "ServicoPessoa"))) {
                    dbSalvar.comitarTransacao();
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("matriculaAcademiaBean", new MatriculaAcademiaBean());
                    ((MatriculaAcademiaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("matriculaAcademiaBean")).setMensagem("Matricula Excluida com sucesso!");
                } else {
                    mensagem = "Erro ao excluir serviço pessoa!";
                    dbSalvar.desfazerTransacao();
                }
            } else {
                mensagem = "Erro ao excluir Matricula!";
                dbSalvar.desfazerTransacao();
            }
        }
    }

    public String editar(MatriculaAcademia ma) {
        matriculaAcademia = ma;
        servicoPessoaJSFBean.setServicoPessoa(matriculaAcademia.getServicoPessoa());
        descPesquisa = "";
        porPesquisa = "nome";
        comoPesquisa = "";
        servicoPessoaJSFBean.editar(matriculaAcademia.getServicoPessoa());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        listaAcademia.clear();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") == null) {
            return "academia";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void acaoPesquisaInicial() {
        listaAcademia.clear();
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        listaAcademia.clear();
        comoPesquisa = "P";
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

    public MatriculaAcademia getMatriculaAcademia() {
        return matriculaAcademia;
    }

    public void setMatriculaAcademia(MatriculaAcademia matriculaAcademia) {
        this.matriculaAcademia = matriculaAcademia;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<MatriculaAcademia> getListaAcademia() {
        if (listaAcademia.isEmpty()) {
            MatriculaAcademiaDB db = new MatriculaAcademiaDBToplink();
            listaAcademia = db.pesquisaMatriculaAcademia(descPesquisa, porPesquisa, comoPesquisa);
        }
        return listaAcademia;
    }

    public void setListaAcademia(List<MatriculaAcademia> listaAcademia) {
        this.listaAcademia = listaAcademia;
    }
}
