package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Cnae;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_patronal_cnae")
@NamedQuery(name = "PatronalCnae.pesquisaID", query = "select pc from PatronalCnae pc where pc.id = :pid")
public class PatronalCnae implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_patronal", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Patronal patronal;
    @JoinColumn(name = "id_cnae", referencedColumnName = "id", nullable = false)
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
