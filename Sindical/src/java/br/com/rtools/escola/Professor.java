package br.com.rtools.escola;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

/**
 * <p>
 * <b>Professor</b></p>
 * <p>
 * Cadastro único de professores. </p>
 * <p>
 * O valor percentual da comissão será usado para gerar um bônus ao professor
 * conforme o valor final do serviço após ser gravado na tabela movimento
 * financeiro.</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "esc_professor")
@NamedQueries({
    @NamedQuery(name = "Professor.pesquisaID", query = "SELECT P FROM Professor AS P WHERE P.id = :pid"),
    @NamedQuery(name = "Professor.findAll", query = "SELECT P FROM Professor AS P ORDER BY P.professor.nome ASC"),
    @NamedQuery(name = "Professor.findName", query = "SELECT P FROM Professor AS P WHERE UPPER(P.professor.nome) LIKE :pdescricao ORDER BY P.professor.nome ASC ")
})
public class Professor implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_professor", referencedColumnName = "id", unique = true)
    @ManyToOne
    private Pessoa professor;
    @Column(name = "nr_comissao")
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

    @Override
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + this.id;
        hash = 59 * hash + (this.professor != null ? this.professor.hashCode() : 0);
        hash = 59 * hash + Float.floatToIntBits(this.nrComissao);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Professor other = (Professor) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.professor != other.professor && (this.professor == null || !this.professor.equals(other.professor))) {
            return false;
        }
        if (Float.floatToIntBits(this.nrComissao) != Float.floatToIntBits(other.nrComissao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Professor{" + "id=" + id + ", professor=" + professor + ", nrComissao=" + nrComissao + '}';
    }

}
