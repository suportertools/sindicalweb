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
@Table(name = "soc_servico_categoria")
@NamedQuery(name = "ServicoCategoria.pesquisaID", query = "select sc from ServicoCategoria sc where sc.id=:pid")
public class ServicoCategoria implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Categoria categoria;
    @JoinColumn(name = "id_parentesco", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Parentesco parentesco;
    @JoinColumn(name = "id_servicos", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Servicos servicos;

    public ServicoCategoria() {
        this.id = -1;
        this.categoria = new Categoria();
        this.parentesco = new Parentesco();
        this.servicos = new Servicos();
    }

    public ServicoCategoria(int id, Categoria categoria, Parentesco parentesco, Servicos servicos) {
        this.id = id;
        this.categoria = categoria;
        this.parentesco = parentesco;
        this.servicos = servicos;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
