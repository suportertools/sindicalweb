package br.com.rtools.suporte;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PRO_INTERRUPCAO")
@NamedQuery(name = "Interrupcao.pesquisaID", query = "select os from Interrupcao os where os.id=:pid")
public class Interrupcao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_OS", referencedColumnName = "ID")
    @ManyToOne
    private OrdemServico ordemServico;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA")
    private Date date;
    @Column(name = "TM_HORARIO", length = 5)
    private String horario;
    @Column(name = "DS_MOTIVO", length = 500)
    private String motivo;

    public Interrupcao(int id, OrdemServico ordemServico, Date date, String horario, String motivo) {
        this.id = id;
        this.ordemServico = ordemServico;
        this.date = date;
        this.horario = horario;
        this.motivo = motivo;
    }

    public Interrupcao() {
        this.id = -1;
        this.ordemServico = new OrdemServico();
        this.date = new Date();
        this.horario = DataHoje.horaMinuto();
        this.motivo = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public OrdemServico getOrdemServico() {
        return ordemServico;
    }

    public void setOrdemServico(OrdemServico ordemServico) {
        this.ordemServico = ordemServico;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDataString() {
        return DataHoje.converteData(date);
    }

    public void setDataString(String date) {
        this.date = DataHoje.converte(date);
    }
}
