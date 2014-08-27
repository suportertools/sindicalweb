package br.com.rtools.escola;

import br.com.rtools.pessoa.Filial;
import javax.persistence.*;

@Entity
@Table(name = "esc_matr_turma")
@NamedQuery(name = "MatriculaTurma.pesquisaID", query = "select m from MatriculaTurma m where m.id=:pid")
public class MatriculaTurma implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_matr_escola", referencedColumnName = "id")
    @ManyToOne
    private MatriculaEscola matriculaEscola;
    @JoinColumn(name = "id_turma", referencedColumnName = "id")
    @ManyToOne
    private Turma turma;

    public MatriculaTurma() {
        id = -1;
        matriculaEscola = new MatriculaEscola();
        turma = new Turma();
    }

    public MatriculaTurma(int id, MatriculaEscola matriculaEscola, Turma turma, Filial filial) {
        this.id = id;
        this.matriculaEscola = matriculaEscola;
        this.turma = turma;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MatriculaEscola getMatriculaEscola() {
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }
}
