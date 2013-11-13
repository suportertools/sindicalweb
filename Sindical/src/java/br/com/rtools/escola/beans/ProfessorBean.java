package br.com.rtools.escola.beans;

import br.com.rtools.escola.Professor;
import br.com.rtools.escola.db.ProfessorDB;
import br.com.rtools.escola.db.ProfessorDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean
@SessionScoped
public class ProfessorBean implements java.io.Serializable {

    private Professor professor = new Professor();
    private String mensagem = "";
    private List<Professor> listaProfessores = new ArrayList();

    public void salvar() {
        ProfessorDB professorDB = new ProfessorDBToplink();
        if (professor.getProfessor().getId() == -1) {
            mensagem = "Pesquise uma pessoa para ser Professor!";
            return;
        }
        if (professorDB.existeProfessor(professor)) {
            mensagem = "Professor já cadastrado!";
            return;
        }

        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (professor.getId() == -1) {
            if (!salvarAcumuladoDB.inserirObjeto(professor)) {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao salvar Professor!";
                return;
            } else {
                mensagem = "Cadastro salvo com sucesso!";
            }
        } else {
            if (!salvarAcumuladoDB.alterarObjeto(professor)) {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao atualizar Professor!";
                return;
            } else {
                mensagem = "Cadastro atualizado com sucesso!";
            }
        }
        salvarAcumuladoDB.comitarTransacao();
        professor = new Professor();
        listaProfessores.clear();
    }

    public void editar(Professor p) {
        professor = p;
    }

    public void excluir() {
        if (professor.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            professor = (Professor) salvarAcumuladoDB.pesquisaCodigo(professor.getId(), "Professor");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(professor)) {
                salvarAcumuladoDB.comitarTransacao();
                listaProfessores.clear();
                professor = new Professor();
                mensagem = "Cadastro excluído com sucesso!";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao excluir Cadastro!";
            }
        }
    }

    public void excluir(Professor p) {
        if (p.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            professor = (Professor) salvarAcumuladoDB.pesquisaCodigo(p.getId(), "Professor");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(professor)) {
                salvarAcumuladoDB.comitarTransacao();
                listaProfessores.clear();
                professor = new Professor();
                mensagem = "Cadastro excluído com sucesso!";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                mensagem = "Erro ao excluir Cadastro!";
            }
        }
    }

    public void novo() {
        professor = new Professor();
        mensagem = "";
    }

    public Professor getProfessor() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            professor.setProfessor((Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public List<Professor> getListaProfessores() {
        if (listaProfessores.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            listaProfessores = (List<Professor>) salvarAcumuladoDB.listaObjeto("Professor", true);
        }
        return listaProfessores;
    }

    public void setListaProfessores(List<Professor> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }
}
