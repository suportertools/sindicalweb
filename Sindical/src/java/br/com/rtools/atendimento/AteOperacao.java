package br.com.rtools.atendimento;

import javax.persistence.*;

@Entity
@Table(name = "ate_operacao")
@NamedQueries({
    @NamedQuery(name = "AteOperacao.pesquisaID",  query = "SELECT AOP FROM AteOperacao AS AOP WHERE AOP.id = :pid"),
    @NamedQuery(name = "AteOperacao.findAll",     query = "SELECT AOP FROM AteOperacao AS AOP ORDER BY AOP.descricao ASC "),
    @NamedQuery(name = "AteOperacao.findName",    query = "SELECT AOP FROM AteOperacao AS AOP WHERE UPPER(AOP.descricao) LIKE :pdescricao ORDER BY AOP.descricao ASC ")
})
public class AteOperacao implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;

    public AteOperacao() {
        this.id = -1;
        this.descricao = "";
    }

    public AteOperacao(int id, String descricao) {
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
