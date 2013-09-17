package br.com.rtools.escola;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "ESC_VENDEDOR")
@NamedQuery(name = "Vendedor.pesquisaID", query = "SELECT V FROM Vendedor V WHERE V.id = :pid")
public class Vendedor implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa pessoa;

    public Vendedor() {
        this.id = -1;
        this.pessoa = new Pessoa();
    }

    public Vendedor(int id, Pessoa pessoa) {
        this.id = id;
        this.pessoa = pessoa;
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
}
