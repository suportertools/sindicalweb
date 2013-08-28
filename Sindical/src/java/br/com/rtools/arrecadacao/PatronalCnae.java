package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Cnae;
import javax.persistence.*;

@Entity
@Table(name="ARR_PATRONAL_CNAE")
@NamedQuery(name="PatronalCnae.pesquisaID", query="select pc from PatronalCnae pc where pc.id = :pid")
public class PatronalCnae implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_PATRONAL", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Patronal patronal;    
    @JoinColumn(name="ID_CNAE", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Cnae cnae;    

    public PatronalCnae() {
        this.id = -1;
        this.patronal = new Patronal();
        this.cnae = new Cnae();
    }

    public PatronalCnae(int id, Patronal patronal, Cnae cnae) {
        this.id = id;
        this.patronal = patronal;
        this.cnae = cnae;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patronal getPatronal() {
        return patronal;
    }

    public void setPatronal(Patronal patronal) {
        this.patronal = patronal;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }
    

}