package br.com.rtools.escola.beans;

import br.com.rtools.escola.Professor;
import br.com.rtools.escola.db.ProfessorDB;
import br.com.rtools.escola.db.ProfessorDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class ProfessorBean implements java.io.Serializable {

    private Professor professor;
    private String message;
    private List<Professor> listProfessores;

    @PostConstruct
    public void init() {
        professor = new Professor();
        message = "";
        listProfessores = new ArrayList<Professor>();
    }

    @PreDestroy
    public void destroy() {
        clear();
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("professorBean");
    }

    public void save() {
        ProfessorDB professorDB = new ProfessorDBToplink();
        if (professor.getProfessor().getId() == -1) {
            message = "Pesquise uma pessoa para ser Professor!";
            return;
        }
        if (professorDB.existeProfessor(professor)) {
            message = "Professor já cadastrado!";
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (professor.getId() == -1) {
            if (!di.save(professor)) {
                di.rollback();
                message = "Erro ao salvar Professor!";
                return;
            } else {
                message = "Cadastro salvo com sucesso!";
            }
            novoLog.save(
                    "ID " + professor.getId()
                    + " - Pessoa: (" + professor.getProfessor().getId() + ") " + professor.getProfessor().getNome()
                    + " - Comissão: " + professor.getNrComissao()
            );
        } else {
            Professor p = (Professor) di.find(professor);
            String beforeUpdate
                    = "ID " + p.getId()
                    + " - Pessoa: (" + p.getProfessor().getId() + ") " + p.getProfessor().getNome()
                    + " - Comissão: " + p.getNrComissao();
            if (!di.update(professor)) {
                di.rollback();
                message = "Erro ao atualizar Professor!";
                return;
            } else {
                message = "Cadastro atualizado com sucesso!";
            }
            novoLog.update(beforeUpdate,
                    "ID " + professor.getId()
                    + " - Pessoa: (" + professor.getProfessor().getId() + ") " + professor.getProfessor().getNome()
                    + " - Comissão: " + professor.getNrComissao()
            );
        }
        di.commit();
        professor = new Professor();
        listProfessores.clear();
    }

    public void edit(Professor p) {
        DaoInterface di = new Dao();
        professor = (Professor) di.rebind(p);
    }

    public void delete() {
        if (professor.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            if (di.delete(professor)) {
                novoLog.delete(
                        "ID " + professor.getId()
                        + " - Pessoa: (" + professor.getProfessor().getId() + ") " + professor.getProfessor().getNome()
                        + " - Comissão: " + professor.getNrComissao()
                );
                di.commit();
                listProfessores.clear();
                professor = new Professor();
                message = "Cadastro excluído com sucesso!";
            } else {
                di.rollback();
                message = "Erro ao excluir Cadastro!";
            }
        }
    }

    public void delete(Professor p) {
        if (p.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            NovoLog novoLog = new NovoLog();
            if (di.delete(p)) {
                novoLog.delete(
                        "ID " + p.getId()
                        + " - Pessoa: (" + p.getProfessor().getId() + ") " + p.getProfessor().getNome()
                        + " - Comissão: " + p.getNrComissao()
                );
                di.commit();
                listProfessores.clear();
                professor = new Professor();
                message = "Cadastro excluído com sucesso!";
            } else {
                di.rollback();
                message = "Erro ao excluir Cadastro!";
            }
        }
    }

    public Professor getProfessor() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            professor.setProfessor((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Professor> getListProfessores() {
        if (listProfessores.isEmpty()) {
            DaoInterface di = new Dao();
            listProfessores = (List<Professor>) di.list(new Professor(), true);
        }
        return listProfessores;
    }

    public void setListProfessores(List<Professor> listProfessores) {
        this.listProfessores = listProfessores;
    }
}
