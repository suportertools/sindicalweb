package br.com.rtools.escola;

import javax.persistence.*;

@Entity
@Table(name = "ESC_TURMA_PROFESSOR")
@NamedQuery(name = "TurmaProfessor.pesquisaID", query = "select tp from TurmaProfessor tp where tp.id=:pid")
public class TurmaProfessor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_TURMA", referencedColumnName = "ID")
    @OneToOne
    private Turma turma;
    @JoinColumn(name = "ID_PROFESSOR", referencedColumnName = "ID")
    @OneToOne
    private Professor professor;
    @JoinColumn(name = "ID_COMPONENTE", referencedColumnName = "ID")
    @OneToOne
    private ComponenteCurricular componenteCurricular;

    public TurmaProfessor() {
        this.id = -1;
        this.turma = new Turma();
        this.professor = new Professor();
        this.componenteCurricular = new ComponenteCurricular();
    }

    public TurmaProfessor(int id, Turma turma, Professor professor, ComponenteCurricular componenteCurricular) {
        this.id = id;
        this.turma = turma;
        this.professor = professor;
        this.componenteCurricular = componenteCurricular;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = professor;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }
}