package br.com.rtools.associativo;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "eve_evento_baile")
@NamedQuery(name = "EventoBaile.pesquisaID", query = "select s from EventoBaile s where s.id=:pid")
public class EventoBaile implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_evento", referencedColumnName = "id", nullable = true)
    @OneToOne
    private AEvento evento;
    @Column(name = "nr_mesas", nullable = true)
    private int quantidadeMesas;
    @Column(name = "nr_convites", nullable = true)
    private int quantidadeConvites;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data", nullable = false)
    private Date data;
    @Column(name = "tm_inicio", nullable = false)
    private String horaInicio;
    @Column(name = "tm_fim", nullable = true)
    private String horaFim;
    @JoinColumn(name = "id_evt", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Evt evt;

    public EventoBaile() {
        this.id = -1;
        this.evento = new AEvento();
        this.quantidadeMesas = 0;
        this.data = null;
        this.horaInicio = "";
        this.horaFim = "";
        this.quantidadeConvites = 0;
        this.evt = null;
    }

    public EventoBaile(int id, AEvento evento, int quantidadeMesas, Date data, String horaInicio, String horaFim, int quantidadeConvites, Evt evt) {
        this.id = id;
        this.evento = evento;
        this.quantidadeMesas = quantidadeMesas;
        this.data = data;
        this.horaInicio = horaInicio;
        this.horaFim = horaFim;
        this.quantidadeConvites = quantidadeConvites;
        this.evt = evt;
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

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }
}
