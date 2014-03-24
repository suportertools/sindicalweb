package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "EVE_EVENTO_BAILE")
@NamedQuery(name = "EventoBaile.pesquisaID", query = "select s from EventoBaile s where s.id=:pid")
public class EventoBaile implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private AEvento evento;
    @Column(name = "NR_MESAS", nullable = true)
    private int quantidadeMesas;
    @Column(name = "NR_CONVITES", nullable = true)
    private int quantidadeConvites;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA", nullable = false)
    private Date data;
    @Column(name = "TM_INICIO", nullable = false)
    private String horaInicio;
    @Column(name = "TM_FIM", nullable = true)
    private String horaFim;

    public EventoBaile() {
        this.id = -1;
        this.evento = new AEvento();
        this.quantidadeMesas = 0;
        this.data = null;
        this.horaInicio = "";
        this.horaFim = "";
        this.quantidadeConvites = 0;
    }

    public EventoBaile(int id, AEvento evento, int quantidadeMesas, Date data, String horaInicio, String horaFim, int quantidadeConvites) {
        this.id = id;
        this.evento = evento;
        this.quantidadeMesas = quantidadeMesas;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.quantidadeConvites = quantidadeConvites;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AEvento getEvento() {
        return evento;
    }

    public void setEvento(AEvento evento) {
        this.evento = evento;
    }

    public int getQuantidadeMesas() {
        return quantidadeMesas;
    }

    public void setQuantidadeMesas(int quantidadeMesas) {
        this.quantidadeMesas = quantidadeMesas;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
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

    public String getDataString() {
        if (getData() != null) {
            return DataHoje.converteData(getData());
        } else {
            return "";
        }
    }

    public void setDataString(String data) {
        if (!(data.isEmpty())) {
            this.setData(DataHoje.converte(data));
        }
    }

    public int getQuantidadeConvites() {
        return quantidadeConvites;
    }

    public void setQuantidadeConvites(int quantidadeConvites) {
        this.quantidadeConvites = quantidadeConvites;
    }
}
