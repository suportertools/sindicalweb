
package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_PLANO3")
@NamedQuery(name="Plano3.pesquisaID", query="select p from Plano3 p where p.id=:pid")
public class Plano3 implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_PLANO2", referencedColumnName="ID", nullable=false)
    @ManyToOne(fetch=FetchType.EAGER)
    private Plano2 plano2;
    @Column(name="DS_NUMERO", length=100,nullable=false)
    private String numero;
    @Column(name="DS_CONTA", length=200,nullable=false)
    private String conta;
    @Column(name="DS_ACESSO", length=5)
    private String acesso;    

    public Plano3() {
        this.id = -1;
        this.plano2 = new Plano2();
        this.numero = "";
        this.conta = "";
        this.acesso = "";
    }
    

    public Plano3(int id, Plano2 plano2, String numero, String conta, String acesso) {
        this.id = id;
        this.plano2 = plano2;
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

    public Plano2 getPlano2() {
        return plano2;
    }

    public void setPlano2(Plano2 plano2) {
        this.plano2 = plano2;
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