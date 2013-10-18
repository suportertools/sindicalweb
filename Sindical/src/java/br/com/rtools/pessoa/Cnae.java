package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "PES_CNAE")
@NamedQueries({
    @NamedQuery(name = "Cnae.pesquisaID",   query = "SELECT CN FROM Cnae AS CN WHERE CN.id = :pid"),
    @NamedQuery(name = "Cnae.findAll",      query = "SELECT CN FROM Cnae AS CN ORDER BY CN.cnae ASC, CN.numero ASC"),
    @NamedQuery(name = "Cnae.findName",     query = "SELECT CN FROM Cnae AS CN WHERE UPPER(CN.cnae) LIKE :pdescricao ORDER BY CN.cnae ASC, CN.numero ASC ")
})
public class Cnae implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_CNAE", length = 1000, nullable = false)
    private String cnae;
    @Column(name = "DS_NUMERO", length = 50, nullable = false)
    private String numero;

    public Cnae() {
        this.id = -1;
        this.cnae = "";
        this.numero = "";
    }

    public Cnae(int id, String cnae, String numero) {
        this.id = id;
        this.cnae = cnae;
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }
}