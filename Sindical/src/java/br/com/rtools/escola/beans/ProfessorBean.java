package br.com.rtools.escola.beans;

import br.com.rtools.escola.Professor;
import br.com.rtools.escola.dao.ProfessorDao;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
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
        listProfessores = new ArrayList<>();
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
        ProfessorDao pd = new ProfessorDao();
        if (professor.getProfessor().getId() == -1) {
            message = "Pesquise uma pessoa para ser Professor!";
            return;
        }
        if (pd.existProfessor(professor)) {
            message = "Professor já cadastrado!";
            return;
        }
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        dao.openTransaction();
        if (professor.getId() == -1) {
            if (!dao.save(professor)) {
                dao.rollback();
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
            Professor p = (Professor) dao.find(professor);
            String beforeUpdate
                    = "ID " + p.getId()
                    + " - Pessoa: (" + p.getProfessor().getId() + ") " + p.getProfessor().getNome()
                    + " - Comissão: " + p.getNrComissao();
            if (!dao.update(professor)) {
                dao.rollback();
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
        dao.commit();
        professor = new Professor();
        listProfessores.clear();
    }

    public void edit(Professor p) {
        Dao dao = new Dao();
        professor = (Professor) dao.rebind(p);
    }

    public void delete() {
        if (professor.getId() != -1) {
            Dao dao = new Dao();
            NovoLog novoLog = new NovoLog();
            dao.openTransaction();
            if (dao.delete(professor)) {
                novoLog.delete(
                        "ID " + professor.getId()
                        + " - Pessoa: (" + professor.getProfessor().getId() + ") " + professor.getProfessor().getNome()
                        + " - Comissão: " + professor.getNrComissao()
                );
                dao.commit();
                listProfessores.clear();
                professor = new Professor();
                message = "Cadastro excluído com sucesso!";
            } else {
                dao.rollback();
                message = "Erro ao excluir Cadastro!";
            }
        }
    }

    public void delete(Professor p) {
        if (p.getId() != -1) {
            Dao dao = new Dao();
            dao.openTransaction();
            NovoLog novoLog = new NovoLog();
            if (dao.delete(p)) {
                novoLog.delete(
                        "ID " + p.getId()
                        + " - Pessoa: (" + p.getProfessor().getId() + ") " + p.getProfessor().getNome()
                        + " - Comissão: " + p.getNrComissao()
                );
                dao.commit();
                listProfessores.clear();
                professor = new Professor();
                message = "Cadastro excluído com sucesso!";
            } else {
                dao.rollback();
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
            Dao dao = new Dao();
            listProfessores = (List<Professor>) dao.list(new Professor(), true);
        }
        return listProfessores;
    }

    public void setListProfessores(List<Professor> listProfessores) {
        this.listProfessores = listProfessores;
    }
}
