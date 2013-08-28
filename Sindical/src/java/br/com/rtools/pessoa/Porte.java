package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name="PES_PORTE")
@NamedQuery(name="Porte.pesquisaID", query="select prof from Porte prof where prof.id=:pid")
public class Porte implements java.io.Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=50,nullable=false)
    private String descricao;

    public Porte() {
        this.id = -1;
        this.descricao = "";
    }

    public Porte(int id, String descricao) {
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