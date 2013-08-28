package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CONDICAO_PAGAMENTO")
@NamedQuery(name="CondicaoPagamento.pesquisaID", query="select cp from CondicaoPagamento cp where cp.id=:pid")
public class CondicaoPagamento implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @Column(name="DS_DESCRICAO", length=20)
    private String descricao;

    public CondicaoPagamento() {
        this.id = -1;
        this.descricao = "";
    }

    public CondicaoPagamento(int id, String descricao) {
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