package br.com.rtools.sistema;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "SIS_EMAIL_ARQUIVO")
public class EmailArquivo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EMAIL", referencedColumnName = "ID")
    @ManyToOne
    private Email email;
    @JoinColumn(name = "ID_ARQUIVO", referencedColumnName = "ID")
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
