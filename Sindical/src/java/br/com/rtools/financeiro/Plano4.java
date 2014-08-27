package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_plano4")
@NamedQuery(name = "Plano4.pesquisaID", query = "select p from Plano4 p where p.id=:pid")
public class Plano4 implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_plano3", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Plano3 plano3;
    @Column(name = "ds_numero", length = 100, nullable = false)
    private String numero;
    @Column(name = "ds_conta", length = 200, nullable = false)
    private String conta;
    @Column(name = "ds_acesso", length = 10)
    private String acesso;
    @Column(name = "ds_classificador", length = 20)
    private String classificador;

    public Plano4() {
        this.id = -1;
        this.plano3 = new Plano3();
        this.numero = "";
        this.conta = "";
        this.acesso = "";
        this.classificador = "";
    }

    public Plano4(int id, Plano3 plano3, String numero, String conta, String acesso, String classificador) {
        this.id = id;
        this.plano3 = plano3;
        this.numero = numero;
        this.conta = conta;
        this.acesso = acesso;
        this.classificador = classificador;
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

    public String getClassificador() {
        return classificador;
    }

    public void setClassificador(String classificador) {
        this.classificador = classificador;
    }
}
