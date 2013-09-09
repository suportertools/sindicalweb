package br.com.rtools.escola.beans;

import br.com.rtools.escola.Professor;
import br.com.rtools.escola.db.ProfessorDB;
import br.com.rtools.escola.db.ProfessorDBToplink;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Moeda;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class ProfessorJSFBean implements java.io.Serializable {
    private Professor professor = new Professor();
    private Pessoa pessoa = new Pessoa();
    private String msgConfirma = "";
    private String valorComissao = "0";
    private List<Professor> listaProfessores = new ArrayList();
    private int idIndex = -1;

    public void refreshForm(){

    }

    public String salvar(){
        ProfessorDB db = new ProfessorDBToplink();
        if (pessoa.getId() == -1){
            msgConfirma = "Pesquise uma pessoa para ser Professor!";
            return null;
        }
        professor.setNrComissao(Moeda.substituiVirgulaFloat(valorComissao));
        professor.setProfessor(pessoa);
        if (professor.getId() == -1){
            if (!db.insert(professor)){
                msgConfirma = "Erro ao salvar Professor!";
                return null;
            }else{
                msgConfirma = "Cadastro salvo com sucesso!";
            }
        }else{
            if (!db.update(professor)){
                msgConfirma = "Erro ao atualizar Professor!";
                return null;
            }else{
                msgConfirma = "Cadastro atualizado com sucesso!";
            }
        }
        listaProfessores.clear();
        return null;
    }

    public String editar(){
        professor = (Professor) listaProfessores.get(idIndex);
        pessoa = professor.getProfessor();
        valorComissao = Moeda.converteR$Float(professor.getNrComissao());
        return "professor";
    }

    public String excluir(){
        ProfessorDB db = new ProfessorDBToplink();
        if (professor.getId() != -1){
            if (db.delete(db.pesquisaCodigo(professor.getId()))){
                msgConfirma = "Cadastro excluído com sucesso!";
            }else{
                msgConfirma = "Erro ao excluir Cadastro!";
            }
        }
        listaProfessores.clear();
        return null;
    }

    public String novo(){
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
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pessoaPesquisa") != null){
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
        if (valorComissao.isEmpty())
            valorComissao = "0";
            return Moeda.converteR$(valorComissao);
    }

    public void setValorComissao(String valorComissao) {
        if (valorComissao.isEmpty())
            valorComissao = "0";
           this.valorComissao = Moeda.substituiVirgula(valorComissao);
    }

    public List<Professor> getListaProfessores() {
        if (listaProfessores.isEmpty()){
            ProfessorDB db = new ProfessorDBToplink();
            listaProfessores = db.pesquisaTodos();
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