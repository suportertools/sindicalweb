package br.com.rtools.academia;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
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
@Table(name = "ACA_GRADE")
@NamedQueries({
    @NamedQuery(name = "AcademiaGrade.pesquisaID", query = "SELECT AG FROM AcademiaGrade AS AG WHERE AG.id = :pid"),
    @NamedQuery(name = "AcademiaGrade.findAll", query = "SELECT AG FROM AcademiaGrade AS AG ORDER BY AG.horaInicio ASC, AG.horaFim ASC")
})
public class AcademiaGrade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_HORA_INICIO", length = 5)
    private String horaInicio;
    @Column(name = "DS_HORA_FIM", length = 5)
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
}
