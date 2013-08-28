package br.com.rtools.atendimento;

import javax.persistence.*;

@Entity
@Table(name="ATE_OPERACAO")
@NamedQuery(name="AteOperacao.pesquisaID", query="select aop from AteOperacao aop where aop.id=:pid")
public class AteOperacao implements java.io.Serializable {    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=50, nullable = false, unique = true)
    private String descricao;
    
    public AteOperacao(){  
        this.id = -1;
        this.descricao = "";
    }
        
    public AteOperacao(int id, String descricao){
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

    