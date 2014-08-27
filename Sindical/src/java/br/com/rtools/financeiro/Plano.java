package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_plano")
@NamedQuery(name = "Plano.pesquisaID", query = "select p from Plano p where p.id=:pid")
public class Plano implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_numero", length = 10, nullable = false)
    private String numero;
    @Column(name = "ds_conta", length = 200, nullable = false)
    private String conta;
    @Column(name = "ds_acesso", length = 10)
    private String acesso;
    @Column(name = "ds_classificador", length = 20)
    private String classificador;

    public Plano() {
        this.id = -1;
        this.numero = "";
        this.conta = "";
        this.acesso = "";
        this.classificador = "";
    }

    public Plano(int id, String numero, String conta, String acesso, String classificador) {
        this.id = id;
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
