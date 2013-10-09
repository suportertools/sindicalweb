package br.com.rtools.associativo;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.Evt;
import java.sql.Time;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EVE_ENDERECO")
@NamedQuery(name = "AEndereco.pesquisaID", query = "select s from AEndereco s where s.id=:pid")
public class AEndereco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_EVENTO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private AEvento evento;
    @JoinColumn(name = "ID_ENDERECO", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private Endereco endereco;
    @Column(name = "DS_NOMERO", nullable = true, length = 9)
    private String numero;
    @Column(name = "DS_COMPLEMENTO", nullable = true, length = 100)
    private String complemento;

    public AEndereco() {
        this.id = -1;
        this.evento = new AEvento();
        this.endereco = new Endereco();
        this.numero = "";
        this.complemento = "";
    }

    public AEndereco(int id, AEvento evento, Endereco endereco, String numero, String complemento) {
        this.id = id;
        this.evento = evento;
        this.endereco = endereco;
        this.numero = numero;
        this.complemento = complemento;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}