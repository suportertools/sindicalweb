package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_PLANO4")
@NamedQuery(name = "Plano4.pesquisaID", query = "select p from Plano4 p where p.id=:pid")
public class Plano4 implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PLANO3", referencedColumnName = "ID", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Plano3 plano3;
    @Column(name = "DS_NUMERO", length = 100, nullable = false)
    private String numero;
    @Column(name = "DS_CONTA", length = 200, nullable = false)
    private String conta;
    @Column(name = "DS_ACESSO", length = 5)
    private String acesso;

    public Plano4() {
        this.id = -1;
        this.plano3 = new Plano3();
        this.numero = "";
        this.conta = "";
        this.acesso = "";
    }

    public Plano4(int id, Plano3 plano3, String numero, String conta, String acesso) {
        this.id = id;
        this.plano3 = plano3;
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

    public Plano3 getPlano3() {
        return plano3;
    }

    public void setPlano3(Plano3 plano3) {
        this.plano3 = plano3;
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