package br.com.rtools.associativo;

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
@Table(name = "SOC_CATEGORIA_DESCONTO_DEPENDENTE")
@NamedQuery(name = "CategoriaDescontoDependente.pesquisaID", query = "select c from CategoriaDescontoDependente c where c.id=:pid")
public class CategoriaDescontoDependente implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_categoria_desconto", referencedColumnName = "id")
    @ManyToOne
    private CategoriaDesconto categoriaDesconto;
    @JoinColumn(name = "id_parentesco", referencedColumnName = "id")
    @ManyToOne
    private Parentesco parentesco;
    @Column(name = "nr_desconto")
    private float desconto;

    public CategoriaDescontoDependente() {
        this.id = -1;
        this.categoriaDesconto = new CategoriaDesconto();
        this.parentesco = new Parentesco();
        this.desconto = 0;
    }
    
    public CategoriaDescontoDependente(int id, CategoriaDesconto categoriaDesconto, Parentesco parentesco, float desconto) {
        this.id = id;
        this.categoriaDesconto = categoriaDesconto;
        this.parentesco = parentesco;
        this.desconto = desconto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CategoriaDesconto getCategoriaDesconto() {
        return categoriaDesconto;
    }

    public void setCategoriaDesconto(CategoriaDesconto categoriaDesconto) {
        this.categoriaDesconto = categoriaDesconto;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }
}