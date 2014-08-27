package br.com.rtools.academia;

import br.com.rtools.utilitarios.BaseEntity;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import javax.persistence.*;

/**
 * <b>AcademiaGrade</b>
 * <p>
 * Define a grade de horários, que serão usados para gerar o valor do serviço
 * dentro do período.</p>
 * <p>
 * Formato de hora: 24 horas</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "aca_grade",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ds_hora_inicio", "ds_hora_fim"})
)
@NamedQueries({
    @NamedQuery(name = "AcademiaGrade.pesquisaID", query = "SELECT AG FROM AcademiaGrade AS AG WHERE AG.id = :pid"),
    @NamedQuery(name = "AcademiaGrade.findAll", query = "SELECT AG FROM AcademiaGrade AS AG ORDER BY AG.horaInicio ASC, AG.horaFim ASC")
})
public class AcademiaGrade implements BaseEntity, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_hora_inicio", length = 5)
    private String horaInicio;
    @Column(name = "ds_hora_fim", length = 5)
    private String horaFim;

    public AcademiaGrade() {
        this.id = -1;
        this.horaInicio = DataHoje.livre(DataHoje.dataHoje(), "HH:mm");
        this.horaFim = DataHoje.livre(DataHoje.dataHoje(), "HH:mm");
    }

    public AcademiaGrade(int id, String horaInicio, String horaFim) {
        this.id = id;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
    }

    @Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHoraInicio() {
        return horaInicio;
    }

    public void setHoraInicio(String horaInicio) {
        this.horaInicio = horaInicio;
    }

    public String getHoraFim() {
        return horaFim;
    }

    public void setHoraFim(String horaFim) {
        this.horaFim = horaFim;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (this.horaInicio != null ? this.horaInicio.hashCode() : 0);
        hash = 97 * hash + (this.horaFim != null ? this.horaFim.hashCode() : 0);
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
        final AcademiaGrade other = (AcademiaGrade) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.horaInicio == null) ? (other.horaInicio != null) : !this.horaInicio.equals(other.horaInicio)) {
            return false;
        }
        if ((this.horaFim == null) ? (other.horaFim != null) : !this.horaFim.equals(other.horaFim)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AcademiaGrade{" + "id=" + id + ", horaInicio=" + horaInicio + ", horaFim=" + horaFim + '}';
    }

}
