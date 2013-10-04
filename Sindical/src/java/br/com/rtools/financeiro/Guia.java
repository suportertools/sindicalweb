package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Pessoa;
import javax.persistence.*;

@Entity
@Table(name = "FIN_GUIA")
@NamedQuery(name = "Guia.pesquisaID", query = "select g from Guia g where g.id=:pid")
public class Guia implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_LOTE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Lote lote;
    @JoinColumn(name = "ID_CONVENIO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Pessoa pessoa;

    public Guia(int id, Lote lote, Pessoa pessoa) {
        this.id = id;
        this.lote = lote;
        this.pessoa = pessoa;
    }

    public Guia() {
        this.id = -1;
        this.lote = new Lote();
        this.pessoa = new Pessoa();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}
