package br.com.rtools.seguranca;

import br.com.rtools.pessoa.Filial;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "seg_filial_rotina",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_filial", "id_rotina"})
)
public class FilialRotina implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Rotina rotina;

    public FilialRotina() {
        this.id = null;
        this.filial = null;
        this.rotina = null;
    }

    public FilialRotina(Integer id, Filial filial, Rotina rotina) {
        this.id = id;
        this.filial = filial;
        this.rotina = rotina;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    @Override
    public int hashCode() {
        int hash = 5;
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
        final FilialRotina other = (FilialRotina) obj;
        return true;
    }

    @Override
    public String toString() {
        return "FilialRotina{" + "id=" + id + ", filial=" + filial + ", rotina=" + rotina + '}';
    }

}
