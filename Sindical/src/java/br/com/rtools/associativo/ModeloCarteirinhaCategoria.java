package br.com.rtools.associativo;

import br.com.rtools.seguranca.Rotina;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "SOC_MODELO_CARTEIRINHA_CATEGORIA")
@NamedQuery(name = "ModeloCarteirinhaCategoria.pesquisaID", query = "select mc from ModeloCarteirinhaCategoria mc where mc.id = :pid")
public class ModeloCarteirinhaCategoria  implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID")
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID")
    @ManyToOne
    private Categoria categoria;
    @JoinColumn(name = "ID_MODELO_CARTEIRINHA", referencedColumnName = "ID")
    @ManyToOne
    private ModeloCarteirinha modeloCarteirinha;

    public ModeloCarteirinhaCategoria() {
        this.id = -1;
        this.rotina = null;
        this.categoria = null;
        this.modeloCarteirinha = null;
    }
    
    public ModeloCarteirinhaCategoria(int id, Rotina rotina, Categoria categoria, ModeloCarteirinha modeloCarteirinha) {
        this.id = id;
        this.rotina = rotina;
        this.categoria = categoria;
        this.modeloCarteirinha = modeloCarteirinha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public ModeloCarteirinha getModeloCarteirinha() {
        return modeloCarteirinha;
    }

    public void setModeloCarteirinha(ModeloCarteirinha modeloCarteirinha) {
        this.modeloCarteirinha = modeloCarteirinha;
    }
    
    
}
