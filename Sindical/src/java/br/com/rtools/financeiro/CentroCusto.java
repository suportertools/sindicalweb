package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_CENTRO_CUSTO")
@NamedQuery(name = "CentroCusto.pesquisaID", query = "select cc from CentroCusto cc where cc.id=:pid")
public class CentroCusto implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PLANO5", referencedColumnName = "ID")
    @OneToOne
    private Plano5 plano5;
    @Column(name = "NR_CENTRO_CUSTO", length = 10)
    private int centroCusto;

    public CentroCusto() {
        this.id = -1;
        this.centroCusto = 0;
        this.plano5 = new Plano5();
    }

    public CentroCusto(int id, int centroCusto, Plano5 plano5) {
        this.id = id;
        this.centroCusto = centroCusto;
        this.plano5 = plano5;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public int getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(int centroCusto) {
        this.centroCusto = centroCusto;
    }
}
