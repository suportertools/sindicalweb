package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_TIPO_PAGAMENTO")
@NamedQuery(name="TipoPagamento.pesquisaID", query="select tp from TipoPagamento tp where tp.id=:pid")
public class TipoPagamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="DS_DESCRICAO", length=20)
    private String descricao;
    
    public TipoPagamento() {
        this.id = -1;
        this.descricao = "";
    }
    
    public TipoPagamento(int id, String descricao) {
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