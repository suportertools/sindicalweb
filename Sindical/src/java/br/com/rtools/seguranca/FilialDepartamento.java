package br.com.rtools.seguranca;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_filial_departamento",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_filial", "id_departamento"})
)
@NamedQueries({
    @NamedQuery(name = "FilialDepartamento.findAll", query = "SELECT FDEP FROM FilialDepartamento AS FDEP ORDER BY FDEP.filial.filial.pessoa.nome ASC, FDEP.departamento.descricao ASC "),
    @NamedQuery(name = "FilialDepartamento.findFilial", query = "SELECT FDEP FROM FilialDepartamento AS FDEP WHERE FDEP.filial.id = :p1 ORDER BY FDEP.departamento.descricao ASC "),
    @NamedQuery(name = "FilialDepartamento.findDepartamento", query = " SELECT D FROM Departamento AS D WHERE D.id NOT IN (SELECT FDEP.departamento.id FROM FilialDepartamento FDEP WHERE FDEP.filial.id = :p1 ) ORDER BY D.descricao ASC "),
    @NamedQuery(name = "FilialDepartamento.findDepartamentoPorFilial", query = " SELECT FDEP.departamento FROM FilialDepartamento AS FDEP WHERE FDEP.filial.id = :p1 ORDER BY FDEP.departamento.descricao ASC ")
})
public class FilialDepartamento implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Filial filial;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Departamento departamento;

    public FilialDepartamento() {
        this.id = -1;
        this.filial = new Filial();
        this.departamento = new Departamento();
    }

    public FilialDepartamento(int id, Filial filial, Departamento departamento) {
        this.id = id;
        this.filial = filial;
        this.departamento = departamento;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
        hash = 29 * hash + (this.filial != null ? this.filial.hashCode() : 0);
        hash = 29 * hash + (this.departamento != null ? this.departamento.hashCode() : 0);
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
        final FilialDepartamento other = (FilialDepartamento) obj;
        if (this.id != other.id) {
            return false;
        }
        if (this.filial != other.filial && (this.filial == null || !this.filial.equals(other.filial))) {
            return false;
        }
        if (this.departamento != other.departamento && (this.departamento == null || !this.departamento.equals(other.departamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "FilialDepartamento{" + "id=" + id + ", filial=" + filial + ", departamento=" + departamento + '}';
    }

}
