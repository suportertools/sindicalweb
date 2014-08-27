package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "pes_tipo_centro_comercial")
@NamedQueries({
    @NamedQuery(name = "TipoCentroComercial.pesquisaID",  query = "SELECT TCC FROM TipoCentroComercial AS TCC WHERE TCC.id = :pid"),
    @NamedQuery(name = "TipoCentroComercial.findAll",     query = "SELECT TCC FROM TipoCentroComercial AS TCC ORDER BY TCC.descricao ASC "),
    @NamedQuery(name = "TipoCentroComercial.findName",    query = "SELECT TCC FROM TipoCentroComercial AS TCC WHERE UPPER(TCC.descricao) LIKE :pdescricao ORDER BY TCC.descricao ASC ")
})
public class TipoCentroComercial implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 100, unique = true)
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