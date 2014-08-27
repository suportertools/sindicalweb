package br.com.rtools.sistema;

import br.com.rtools.pessoa.Pessoa;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "sis_email_pessoa")
@NamedQuery(name = "EmailPessoa.findByEmail", query = "SELECT EP FROM EmailPessoa AS EP WHERE EP.email.id = :p1 ")
public class EmailPessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_email", referencedColumnName = "id")
    @ManyToOne
    private Email email;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "ds_destinatario", nullable = true)
    private String destinatario;
    @Column(name = "ds_cc", nullable = true)
    private String cc;
    @Column(name = "ds_co", nullable = true)
    private String bcc;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_recebimento", nullable = true)
    private Date recebimento;
    @Column(name = "ds_hora_saida", length = 5)
    private String horaSaida;

    public EmailPessoa() {
        this.id = -1;
        this.email = new Email();
        this.pessoa = new Pessoa();
        this.destinatario = "";
        this.cc = "";
        this.bcc = "";
        this.recebimento = null;
        this.horaSaida = "";
    }

    public EmailPessoa(int id, Email email, Pessoa pessoa, String destinatario, String cc, String bcc, Date recebimento, String horaSaida) {
        this.id = id;
        this.email = email;
        this.pessoa = pessoa;
        this.destinatario = destinatario;
        this.cc = cc;
        this.bcc = bcc;
        this.recebimento = recebimento;
        this.horaSaida = horaSaida;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getDestinatario() {
        return destinatario;
    }

    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public Date getRecebimento() {
        return recebimento;
    }

    public void setRecebimento(Date recebimento) {
        this.recebimento = recebimento;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }
}
