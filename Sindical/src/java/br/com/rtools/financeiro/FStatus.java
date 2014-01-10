package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_STATUS")
@NamedQuery(name = "FStatus.pesquisaID", query = "select s from FStatus s where s.id=:pid")
public class FStatus implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, unique = true, nullable = false)
    private String descricao;
    @Column(name = "DS_HISTORICO", length = 500)
    private String historico;

    public FStatus() {
        this.id = -1;
        this.descricao = "";
        this.historico = "";
    }

    public FStatus(int id, String descricao, String historico) {
        this.id = id;
        this.descricao = descricao;
        this.historico = historico;
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

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }
}