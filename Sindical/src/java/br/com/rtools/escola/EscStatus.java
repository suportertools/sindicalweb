package br.com.rtools.escola;

import javax.persistence.*;

@Entity
@Table(name="ESC_STATUS")
@NamedQuery(name="EscStatus.pesquisaID", query="select es from EscStatus es where es.id=:pid")
public class EscStatus implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", nullable=true)
    private String descricao;

    public EscStatus(){
         id = -1;
         descricao = "";
    }

    public EscStatus(int id, String descricao){
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