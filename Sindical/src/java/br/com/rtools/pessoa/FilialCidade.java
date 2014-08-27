package br.com.rtools.pessoa;

import br.com.rtools.endereco.Cidade;
import javax.persistence.*;

@Entity
@Table(name = "pes_filial_cidade")
@NamedQuery(name = "FilialCidade.pesquisaID", query = "select fd from FilialCidade fd where fd.id=:pid")
public class FilialCidade implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Cidade cidade;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Filial filial;

    public FilialCidade(int id, Cidade cidade, Filial filial) {
        this.id = id;
        this.cidade = cidade;
        this.filial = filial;
    }

    public FilialCidade() {
        this.id = -1;
        this.cidade = new Cidade();
        this.filial = new Filial();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
