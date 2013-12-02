package br.com.rtools.academia;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ACA_GRADE")
@NamedQuery(name = "AcademiaGrade.pesquisaID", query = "SELECT AG FROM AcademiaGrade AS AG WHERE AG.id = :pid")
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
        this.horaInicio = "";
        this.horaFim = "";
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
