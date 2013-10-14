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
    private Pessoa pessoa = new Pessoa();
    private String msgConfirma = "";
    private String valorComissao = "0";
    private List<Professor> listaProfessores = new ArrayList();
    private int idIndex = -1;

    public String salvar() {
        ProfessorDB professorDB = new ProfessorDBToplink();
        if (pessoa.getId() == -1) {
            msgConfirma = "Pesquise uma pessoa para ser Professor!";
            return null;
        }
        professor.setProfessor(pessoa);
        if (professorDB.existeProfessor(professor)) {
            msgConfirma = "Professor já cadastrado!";
            return null;
        }
        professor.setNrComissao(Moeda.substituiVirgulaFloat(valorComissao));
        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
        salvarAcumuladoDB.abrirTransacao();
        if (professor.getId() == -1) {
            if (!salvarAcumuladoDB.inserirObjeto(professor)) {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao salvar Professor!";
                return null;
            } else {
                msgConfirma = "Cadastro salvo com sucesso!";
            }
        } else {
            if (!salvarAcumuladoDB.alterarObjeto(professor)) {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao atualizar Professor!";
                return null;
            } else {
                msgConfirma = "Cadastro atualizado com sucesso!";
            }
        }
        salvarAcumuladoDB.comitarTransacao();
        listaProfessores.clear();
        return null;
    }

    public String editar(Professor p) {
        professor = p;
        pessoa = professor.getProfessor();
        valorComissao = Moeda.converteR$Float(professor.getNrComissao());
        return "professor";
    }

    public String excluir() {
        if (professor.getId() != -1) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            professor = (Professor) salvarAcumuladoDB.pesquisaCodigo(professor.getId(), "Professor");
            salvarAcumuladoDB.abrirTransacao();
            if (salvarAcumuladoDB.deletarObjeto(professor)) {
                salvarAcumuladoDB.comitarTransacao();
                listaProfessores.clear();
                professor = new Professor();
                msgConfirma = "Cadastro excluído com sucesso!";
            } else {
                salvarAcumuladoDB.desfazerTransacao();
                msgConfirma = "Erro ao excluir Cadastro!";
            }
        }
        return null;
    }

    public String novo() {
        pessoa = new Pessoa();
        professor = new Professor();
        msgConfirma = "";
        valorComissao = "0";
        return "professor";
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null) {
            pessoa = (Pessoa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getValorComissao() {
        if (valorComissao.isEmpty()) {
            valorComissao = "0";
        }
        return Moeda.converteR$(valorComissao);
    }

    public void setValorComissao(String valorComissao) {
        if (valorComissao.isEmpty()) {
            valorComissao = "0";
        }
        this.valorComissao = Moeda.substituiVirgula(valorComissao);
    }

    public List<Professor> getListaProfessores() {
        if (listaProfessores.isEmpty()) {
            ProfessorDB professorDB = new ProfessorDBToplink();
            listaProfessores = professorDB.pesquisaTodos();
        }
        return listaProfessores;
    }

    public void setListaProfessores(List<Professor> listaProfessores) {
        this.listaProfessores = listaProfessores;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}