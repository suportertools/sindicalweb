package br.com.rtools.associativo;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "CONV_MOTIVO_SUSPENCAO")
@NamedQueries({
    @NamedQuery(name = "ConviteMotivoSuspencao.pesquisaID", query = "SELECT CONMS FROM ConviteMotivoSuspencao AS CONMS WHERE CONMS.id = :pid"),
    @NamedQuery(name = "ConviteMotivoSuspencao.findAll", query = "SELECT CONMS FROM ConviteMotivoSuspencao AS CONMS ORDER BY CONMS.descricao ASC"),
    @NamedQuery(name = "ConviteMotivoSuspencao.findName", query = "SELECT CONMS FROM ConviteMotivoSuspencao AS CONMS WHERE UPPER(CONMS.descricao) LIKE :pdescricao ORDER BY CONMS.descricao ASC ")
})
public class ConviteMotivoSuspencao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 255, nullable = false)
    private String descricao;

    public ConviteMotivoSuspencao() {
        this.id = -1;
        this.descricao = "";
    }

    public ConviteMotivoSuspencao(int id, String descricao) {
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
