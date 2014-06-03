package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_PATRONAL",
        uniqueConstraints = @UniqueConstraint(columnNames = {"ID_PESSOA", "DS_BASE_TERRITORIAL"})
)
@NamedQuery(name = "Patronal.pesquisaID", query = "select p from Patronal p where p.id = :pid")
public class Patronal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "DS_BASE_TERRITORIAL", length = 2000, nullable = true)
    private String baseTerritorial;

    public Patronal() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.baseTerritorial = "";
    }

    public Patronal(int id, Pessoa pessoa, String baseTerritorial) {
        this.id = id;
        this.pessoa = pessoa;
        this.baseTerritorial = baseTerritorial;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getBaseTerritorial() {
        return baseTerritorial;
    }

    public void setBaseTerritorial(String baseTerritorial) {
        this.baseTerritorial = baseTerritorial;
    }
}
