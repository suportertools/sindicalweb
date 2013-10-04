package br.com.rtools.escola;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "ESC_PROFESSOR")
@NamedQuery(name = "Professor.pesquisaID", query = "select p from Professor p where p.id=:pid")
public class Professor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PROFESSOR", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa professor;
    @Column(name = "NR_COMISSAO")
    private float nrComissao;

    public Professor() {
        this.id = -1;
        this.professor = new Pessoa();
        this.nrComissao = 0;
    }

    public Professor(int id, Pessoa professor, float nrComissao) {
        this.id = id;
        this.professor = professor;
        this.nrComissao = nrComissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getProfessor() {
        return professor;
    }

    public void setProfessor(Pessoa professor) {
        this.professor = professor;
    }

    public float getNrComissao() {
        return nrComissao;
    }

    public void setNrComissao(float nrComissao) {
        this.nrComissao = nrComissao;
    }
}
