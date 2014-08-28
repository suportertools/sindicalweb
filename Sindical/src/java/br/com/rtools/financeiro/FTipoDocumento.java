package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_tipo_documento")
@NamedQuery(name = "FTipoDocumento.pesquisaID", query = "select t from FTipoDocumento t where t.id=:pid")
public class FTipoDocumento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, nullable = false)
    private String descricao;

    public FTipoDocumento() {
        this.id = -1;
        this.descricao = "";
    }

    public FTipoDocumento(int id, String descricao) {
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
