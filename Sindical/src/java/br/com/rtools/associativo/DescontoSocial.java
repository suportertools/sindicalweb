package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "soc_desconto")
@NamedQuery(name = "DescontoSocial.pesquisaID", query = "select ds from DescontoSocial ds where ds.id=:pid")
public class DescontoSocial implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_desconto", nullable = true)
    private float nrDesconto;
    @Column(name = "ds_descricao", length = 100)
    private String descricao;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Categoria categoria;    

    public DescontoSocial() {
        this.id = -1;
        this.nrDesconto = 0;
        this.descricao = "";
        this.categoria = null;
    }
    
    public DescontoSocial(int id, float nrDesconto, String descricao, Categoria categoria) {
        this.id = id;
        this.nrDesconto = nrDesconto;
        this.descricao = descricao;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getNrDesconto() {
        return nrDesconto;
    }

    public void setNrDesconto(float nrDesconto) {
        this.nrDesconto = nrDesconto;
    }

    public String getNrDescontoString() {
        return String.valueOf(nrDesconto);
    }
    
    public void setNrDescontoString(String nrDescontoString) {
        try{
            this.nrDesconto = Float.valueOf(nrDescontoString.replace(",", "."));
        }catch(Exception e){
            this.nrDesconto = 0;
        }
    }
    
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }
}
