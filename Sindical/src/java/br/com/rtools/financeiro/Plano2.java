package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_plano2")
@NamedQuery(name = "Plano2.pesquisaID", query = "select p from Plano2 p where p.id=:pid")
public class Plano2 implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_plano", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Plano plano;
    @Column(name = "ds_numero", length = 100, nullable = false)
    private String numero;
    @Column(name = "ds_conta", length = 200, nullable = false)
    private String conta;
    @Column(name = "ds_acesso", length = 10)
    private String acesso;
    @Column(name = "ds_classificador", length = 20)
    private String classificador;

    public Plano2() {
        this.id = -1;
        this.plano = new Plano();
        this.numero = "";
        this.conta = "";
        this.acesso = "";
        this.classificador = "";
    }

    public Plano2(int id, Plano plano, String numero, String conta, String acesso, String classificador) {
        this.id = id;
        this.plano = plano;
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

    public Plano getPlano() {
        return plano;
    }

    public void setPlano(Plano plano) {
        this.plano = plano;
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
