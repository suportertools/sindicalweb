package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_banco")
@NamedQuery(name = "Banco.pesquisaID", query = "select o from Banco o where o.id=:pid")
public class Banco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_num_banco", length = 20, nullable = false)
    private String numero;
    @Column(name = "ds_banco", length = 100, nullable = false)
    private String banco;
    @Column(name = "ds_logo", length = 100, nullable = true)
    private String logo;

    public Banco() {
        this.id = -1;
        this.numero = "";
        this.banco = "";
        this.logo = "";
    }

    public Banco(int id, String numero, String banco, String logo) {
        this.id = id;
        this.numero = numero;
        this.banco = banco;
        this.logo = logo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBanco() {
        return banco;
    }

    public void setBanco(String banco) {
        this.banco = banco;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
