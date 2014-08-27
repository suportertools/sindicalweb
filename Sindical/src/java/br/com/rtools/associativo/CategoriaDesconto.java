package br.com.rtools.associativo;

import br.com.rtools.financeiro.Servicos;
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
@Table(name = "soc_categoria_desconto")
@NamedQuery(name = "CategoriaDesconto.pesquisaID", query = "select c from CategoriaDesconto c where c.id=:pid")
public class CategoriaDesconto implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servico", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Categoria categoria;
    @Column(name = "nr_desconto", nullable = false)
    private float desconto;

    public CategoriaDesconto() {
        this.id = -1;
        this.servicos = new Servicos();
        this.categoria = new Categoria();
        this.desconto = 0;
    }

    public CategoriaDesconto(int id, Servicos servicos, Categoria categoria, float desconto) {
        this.id = id;
        this.servicos = servicos;
        this.categoria = categoria;
        this.desconto = desconto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }
}
