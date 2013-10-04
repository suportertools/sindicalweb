package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_PLANO")
@NamedQuery(name = "Plano.pesquisaID", query = "select p from Plano p where p.id=:pid")
public class Plano implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_NUMERO", length = 10, nullable = false)
    private String numero;
    @Column(name = "DS_CONTA", length = 200, nullable = false)
    private String conta;
    @Column(name = "DS_ACESSO", length = 5)
    private String acesso;

    public Plano() {
        this.id = -1;
        this.numero = "";
        this.conta = "";
        this.acesso = "";
    }

    public Plano(int id, String numero, String conta, String acesso) {
        this.id = id;
        this.numero = numero;
        this.conta = conta;
        this.acesso = acesso;
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

    public String getConta() {
        return conta;
    }

    public void setConta(String conta) {
        this.conta = conta;
    }

    public String getAcesso() {
        return acesso;
    }

    public void setAcesso(String acesso) {
        this.acesso = acesso;
    }
}