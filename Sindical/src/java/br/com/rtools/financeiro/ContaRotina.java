
package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Rotina;
import javax.persistence.*;

@Entity
@Table(name="FIN_CONTA_ROTINA")
@NamedQuery(name="ContaRotina.pesquisaID", query="select o from ContaRotina o where o.id=:pid")
public class ContaRotina implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_ROTINA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name="ID_PLANO4", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Plano4 plano4;
    @Column(name="DS_PAG_REC", length=1,nullable=true)
    private String pagRec;
    @Column(name="NR_PARTIDA", length=1,nullable=false)
    private int partida;   // 1 Partida 0 Contra Partida


    public ContaRotina() {
        this.id = -1;
        this.rotina = new Rotina();
        this.plano4 = new Plano4();
        this.pagRec = "";
        this.partida = 0;
    }

    public ContaRotina(int id, Rotina rotina, Plano4 plano4, String pagRec, int partida) {
        this.id = id;
        this.rotina = rotina;
        this.plano4 = plano4;
        this.pagRec = pagRec;
        this.partida = partida;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Plano4 getPlano4() {
        return plano4;
    }

    public void setPlano4(Plano4 plano4) {
        this.plano4 = plano4;
    }

    public String getPagRec() {
        return pagRec;
    }

    public void setPagRec(String pagRec) {
        this.pagRec = pagRec;
    }

    public int getPartida() {
        return partida;
    }

    public void setPartida(int partida) {
        this.partida = partida;
    }
}