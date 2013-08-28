
package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_INDICE")
@NamedQuery(name="Indice.pesquisaID", query="select i from Indice i where i.id=:pid")
public class Indice implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=20,nullable = false, unique = true)
    private String descricao;
    
    public Indice() {
        this.id = -1;
        this.descricao = "";
    }    
    
    public Indice(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}