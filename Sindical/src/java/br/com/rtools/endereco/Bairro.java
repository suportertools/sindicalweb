package br.com.rtools.endereco;

import javax.persistence.*;

@Entity
@Table(name="END_BAIRRO")
@NamedQuery(name="Bairro.pesquisaID", query="select bai from Bairro bai where bai.id=:pid")
public class Bairro implements java.io.Serializable {    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=70, nullable = false, unique = true)
    private String descricao;
    
    public Bairro(){  
        this.id = -1;
        this.descricao = "";
    }
        
    public Bairro(int id, String descricao){
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

    