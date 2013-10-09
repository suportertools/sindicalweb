package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "PES_CNAE")
@NamedQuery(name = "Cnae.pesquisaID", query = "select cnae from Cnae cnae where cnae.id=:pid")
public class Cnae implements java.io.Serializable {

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