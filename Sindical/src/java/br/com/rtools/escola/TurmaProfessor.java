package br.com.rtools.escola;

import javax.persistence.*;

/**
 * <p>
 * <b>TurmaProfessor</b></p>
 * <p>
 * Cadastro de professores e componentes currículares para a turma. </p>
 * <p>
 * O mesmo professor poderá ministrar diversas disciplinas, adiciones os
 * componentes currículares e as turmas que este pprofessor ministrá. </p>
 *
 * @author rtools
 */
@Entity
@Table(name = "esc_turma_professor")
@NamedQueries({
    @NamedQuery(name = "TurmaProfessor.pesquisaID", query = "SELECT TP FROM TurmaProfessor AS TP WHERE TP.id = :pid"),
    @NamedQuery(name = "TurmaProfessor.findAll", query = "SELECT TP FROM TurmaProfessor AS TP ORDER BY TP.turma.cursos.descricao ASC, TP.componenteCurricular.descricao ASC, TP.professor.professor.nome ASC ")
})
public class TurmaProfessor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_turma", referencedColumnName = "id")
    @OneToOne
    private Turma turma;
    @JoinColumn(name = "id_professor", referencedColumnName = "id")
    @OneToOne
    private Professor professor;
    @JoinColumn(name = "id_componente", referencedColumnName = "id")
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
