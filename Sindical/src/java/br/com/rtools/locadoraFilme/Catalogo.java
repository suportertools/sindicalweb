package br.com.rtools.locadoraFilme;

import br.com.rtools.pessoa.Filial;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "loc_titulo_filial")
@NamedQuery(name = "Catalogo.pesquisaID", query = "Select c from Catalogo c where c.id = :pid")
public class Catalogo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne
    private Filial filial;
    @JoinColumn(name = "id_titulo", referencedColumnName = "id")
    @ManyToOne
    private Titulo titulo;
    @Column(name = "nr_qtde")
    private int quantidade;

    public Catalogo(int id, Filial filial, Titulo titulo, int quantidade) {
        this.id = id;
        this.filial = filial;
        this.titulo = titulo;
        this.quantidade = quantidade;
    }

    public Catalogo() {
        id = -1;
        filial = new Filial();
        titulo = new Titulo();
        quantidade = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Titulo getTitulo() {
        return titulo;
    }

    public void setTitulo(Titulo titulo) {
        this.titulo = titulo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }
}
