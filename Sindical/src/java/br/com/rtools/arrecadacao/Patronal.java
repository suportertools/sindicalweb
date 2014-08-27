package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "arr_patronal",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_pessoa", "ds_base_territorial"})
)
@NamedQuery(name = "Patronal.pesquisaID", query = "select p from Patronal p where p.id = :pid")
public class Patronal implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "ds_base_territorial", length = 2000, nullable = true)
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
