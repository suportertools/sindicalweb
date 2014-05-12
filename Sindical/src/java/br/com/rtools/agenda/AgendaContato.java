package br.com.rtools.agenda;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "AGE_CONTATO",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_AGENDA", "DS_CONTATO"})
)
@NamedQueries({
    @NamedQuery(name = "AgendaContato.findByAgenda", query = "SELECT AC FROM AgendaContato AS AC WHERE AC.agenda.id = :p1 ORDER BY AC.departamento ASC, AC.contato ASC, AC.nascimento ASC")
})
public class AgendaContato implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @JoinColumn(name = "ID_AGENDA", referencedColumnName = "ID")
    @ManyToOne
    private Agenda agenda;
    @Column(name = "DS_CONTATO", length = 100)
    private String contato;
    @Column(name = "DS_EMAIL1", length = 255, nullable = true)
    private String email1;
    @Column(name = "DS_EMAIL2", length = 255, nullable = true)
    private String email2;
    @Column(name = "DS_DEPARTAMENTO", length = 100, nullable = true)
    private String departamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO")
    private Date nascimento;
    @Column(name = "IS_NOTIFICA_ANIVERSARIO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean notificaAniversario;

    public AgendaContato() {
        this.id = -1;
        this.agenda = new Agenda();
        this.contato = "";
        this.email1 = "";
        this.email2 = "";
        this.departamento = "";
        this.nascimento = new Date();
        this.notificaAniversario = false;
    }

    public AgendaContato(Integer id, Agenda agenda, String contato, String email1, String email2, String departamento, Date nascimento, Boolean notificaAniversario) {
        this.id = id;
        this.agenda = agenda;
        this.contato = contato;
        this.email1 = email1;
        this.email2 = email2;
        this.departamento = departamento;
        this.nascimento = nascimento;
        this.notificaAniversario = notificaAniversario;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public boolean getNotificaAniversario() {
        return notificaAniversario;
    }

    public void setNotificaAniversario(boolean notificaAniversario) {
        this.notificaAniversario = notificaAniversario;
    }

    public String getNascimentoString() {
        return DataHoje.converteData(nascimento);
    }

    public void setNascimentoString(String nascimentoString) {
        this.nascimento = DataHoje.converte(nascimentoString);
    }

    public void selecionaDataNascimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.nascimento = DataHoje.converte(format.format(event.getObject()));
    }

    @Override
    public String toString() {
        return "AgendaContato{" + "id=" + id + ", agenda=" + agenda + ", contato=" + contato + ", email1=" + email1 + ", email2=" + email2 + ", departamento=" + departamento + ", nascimento=" + nascimento + ", notificaAniversario=" + notificaAniversario + '}';
    }

}
