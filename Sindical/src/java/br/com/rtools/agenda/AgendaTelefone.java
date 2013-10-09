package br.com.rtools.agenda;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AGE_TELEFONE")
@NamedQuery(name = "AgendaTelefone.pesquisaID", query = "SELECT at FROM AgendaTelefone at WHERE at.id=:pid")
public class AgendaTelefone implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_AGENDA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Agenda agenda;
    @JoinColumn(name = "ID_TIPO_TELEFONE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private TipoTelefone tipoTelefone;
    @Column(name = "DS_DDI", length = 5)
    private String ddi;
    @Column(name = "DS_DDD", length = 2)
    private String ddd;
    @Column(name = "DS_TELEFONE", length = 20)
    private String telefone;
    @Column(name = "DS_CONTATO", length = 50)
    private String contato;

    public AgendaTelefone() {
        this.id = -1;
        this.agenda = new Agenda();
        this.tipoTelefone = new TipoTelefone();
        this.ddi = "55";
        this.ddd = "";
        this.telefone = "";
        this.contato = "";
    }

    public AgendaTelefone(int id, Agenda agenda, TipoTelefone tipoTelefone, String ddi, String ddd, String telefone, String contato) {
        this.id = id;
        this.agenda = agenda;
        this.tipoTelefone = tipoTelefone;
        this.ddi = ddi;
        this.ddd = ddd;
        this.telefone = telefone;
        this.contato = contato;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public TipoTelefone getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(TipoTelefone tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getDdi() {
        return ddi;
    }

    public void setDdi(String ddi) {
        this.ddi = ddi;
    }

    public String getDdd() {
        return ddd;
    }

    public void setDdd(String ddd) {
        this.ddd = ddd;
    }
}
