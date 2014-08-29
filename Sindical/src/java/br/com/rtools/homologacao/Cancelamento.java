package br.com.rtools.homologacao;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "hom_cancelamento")
@NamedQuery(name = "Cancelamento.pesquisaID", query = "SELECT C FROM Cancelamento AS C WHERE C.id = :pid")
public class Cancelamento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_agendamento", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Agendamento agendamento;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data")
    private Date dtData;
    @Column(name = "ds_motivo", length = 200, nullable = false)
    private String motivo;

    public Cancelamento() {
        setId(-1);
        setAgendamento(new Agendamento());
        setUsuario(new Usuario());
        setData("");
        setMotivo("");
    }

    public Cancelamento(int id, Agendamento agendamento, Usuario usuario, String data, String motivo) {
        setId(id);
        setAgendamento(agendamento);
        setUsuario(usuario);
        setData(data);
        setMotivo(motivo);
    }

    public Date getDtData() {
        return dtData;
    }

    public void setDtData(Date dtData) {
        this.dtData = dtData;
    }

    public String getData() {
        if (dtData != null) {
            return DataHoje.converteData(dtData);
        } else {
            return "";
        }
    }

    public void setData(String data) {
        if (!(data.isEmpty())) {
            this.dtData = DataHoje.converte(data);
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agendamento getAgendamento() {
        return agendamento;
    }

    public void setAgendamento(Agendamento agendamento) {
        this.agendamento = agendamento;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
