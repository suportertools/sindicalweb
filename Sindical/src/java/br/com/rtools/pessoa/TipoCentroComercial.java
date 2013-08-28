package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name="PES_TIPO_CENTRO_COMERCIAL")
@NamedQuery(name="TipoCentroComercial.pesquisaID", query="select t from TipoCentroComercial t where t.id=:pid")
public class TipoCentroComercial  implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=100, unique = true)
    private String descricao;

    public TipoCentroComercial(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public TipoCentroComercial() {
        this.id = -1;
        this.descricao = "";
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