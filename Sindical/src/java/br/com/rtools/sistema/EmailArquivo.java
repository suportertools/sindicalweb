package br.com.rtools.sistema;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_email_arquivo")
public class EmailArquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_email", referencedColumnName = "id")
    @ManyToOne
    private Email email;
    @JoinColumn(name = "id_arquivo", referencedColumnName = "id")
    @ManyToOne
    private Arquivo arquivo;

    public EmailArquivo() {
        this.id = -1;
        this.email = new Email();
        this.arquivo = new Arquivo();
    }

    public EmailArquivo(int id, Email email, Arquivo arquivo) {
        this.id = id;
        this.email = email;
        this.arquivo = arquivo;
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

    public Arquivo getArquivo() {
        return arquivo;
    }

    public void setArquivo(Arquivo arquivo) {
        this.arquivo = arquivo;
    }
}
