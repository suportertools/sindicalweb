package br.com.rtools.academia;

import br.com.rtools.sistema.Semana;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "aca_semana")
@NamedQueries({
    @NamedQuery(name = "AcademiaSemana.pesquisaID", query = "SELECT ASE FROM AcademiaSemana AS ASE WHERE ASE.id = :pid"),
    @NamedQuery(name = "AcademiaSemana.findAll", query = "SELECT ASE FROM AcademiaSemana AS ASE ORDER BY ASE.semana.descricao ASC ")
})
public class AcademiaSemana implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_grade", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private AcademiaGrade academiaGrade;
    @JoinColumn(name = "id_semana", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Semana semana;
    @JoinColumn(name = "id_servico_valor", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private AcademiaServicoValor academiaServicoValor;
    
    public AcademiaSemana() {
        this.id = -1;
        this.academiaGrade = new AcademiaGrade();
        this.semana = new Semana();
        this.academiaServicoValor = new AcademiaServicoValor();
    }

    public AcademiaSemana(int id, AcademiaGrade academiaGrade, Semana semana, AcademiaServicoValor academiaServicoValor) {
        this.id = id;
        this.academiaGrade = academiaGrade;
        this.semana = semana;
        this.academiaServicoValor = academiaServicoValor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AcademiaGrade getAcademiaGrade() {
        return academiaGrade;
    }

    public void setAcademiaGrade(AcademiaGrade academiaGrade) {
        this.academiaGrade = academiaGrade;
    }

    public Semana getSemana() {
        return semana;
    }

    public void setSemana(Semana semana) {
        this.semana = semana;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + this.id;
        hash = 13 * hash + (this.academiaGrade != null ? this.academiaGrade.hashCode() : 0);
        hash = 13 * hash + (this.semana != null ? this.semana.hashCode() : 0);
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
        final AcademiaSemana other = (AcademiaSemana) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.academiaGrade != other.academiaGrade && (this.academiaGrade == null || !this.academiaGrade.equals(other.academiaGrade))) {
            return false;
        }
        if (this.semana != other.semana && (this.semana == null || !this.semana.equals(other.semana))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AcademiaSemana{" + "id=" + id + ", academiaGrade=" + academiaGrade + ", semana=" + semana + '}';
    }

    public AcademiaServicoValor getAcademiaServicoValor() {
        return academiaServicoValor;
    }

    public void setAcademiaServicoValor(AcademiaServicoValor academiaServicoValor) {
        this.academiaServicoValor = academiaServicoValor;
    }
}
