package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "PES_TIPO_ENDERECO")
@NamedQueries({ 
    @NamedQuery(name = "TipoEndereco.pesquisaID", query = "SELECT TEND FROM TipoEndereco AS TEND WHERE TEND.id = :pid"),
    @NamedQuery(name = "TipoEndereco.findAll",    query = "SELECT TEND FROM TipoEndereco AS TEND ORDER BY TEND.descricao ASC "),
    @NamedQuery(name = "TipoEndereco.findName",   query = "SELECT TEND FROM TipoEndereco AS TEND WHERE UPPER(TEND.descricao) LIKE :pdescricao ORDER BY TEND.descricao ASC ")
})
public class TipoEndereco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false, unique = true)
    private String descricao;

    public TipoEndereco() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoEndereco(int id, String descricao) {
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