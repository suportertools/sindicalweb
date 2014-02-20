package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "PES_TIPO_DOCUMENTO")
@NamedQueries({
    @NamedQuery(name = "TipoDocumento.pesquisaID", query = "SELECT TDOC FROM TipoDocumento AS TDOC WHERE TDOC.id = :pid"),
    @NamedQuery(name = "TipoDocumento.findAll", query = "SELECT TDOC FROM TipoDocumento AS TDOC ORDER BY TDOC.descricao ASC "),
    @NamedQuery(name = "TipoDocumento.findName", query = "SELECT TDOC FROM TipoDocumento AS TDOC WHERE UPPER(TDOC.descricao) LIKE :pdescricao ORDER BY TDOC.descricao ASC ")
})
public class TipoDocumento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = false, unique = true)
    private String descricao;

    public TipoDocumento() {
        this.id = -1;
        this.descricao = "";
    }

    public TipoDocumento(int id, String descricao) {
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

    @Override
    public String toString() {
        return "TipoDocumento{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
