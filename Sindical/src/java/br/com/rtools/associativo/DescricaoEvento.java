package br.com.rtools.associativo;

import br.com.rtools.financeiro.Servicos;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="EVE_DESC_EVENTO")
@NamedQuery(name="DescricaoEvento.pesquisaID", query="select de from DescricaoEvento de where de.id=:pid")
public class DescricaoEvento implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_DESCRICAO", length=100, nullable=true)
    private String descricao;
    @JoinColumn(name="ID_GRUPO_EVENTO", referencedColumnName="ID", nullable=false)
    @OneToOne
    private GrupoEvento grupoEvento;
    @JoinColumn(name="ID_SERVICO_MOVIMENTO", referencedColumnName="ID")
    @ManyToOne
    private Servicos servicoMovimento;

    public DescricaoEvento(int id, String descricao, GrupoEvento grupoEvento, Servicos servicoMovimento) {
        this.id = id;
        this.descricao = descricao;
        this.grupoEvento = grupoEvento;
        this.servicoMovimento = servicoMovimento;
    }

    public DescricaoEvento() {
        this.id = -1;
        this.descricao = "";
        this.grupoEvento = new GrupoEvento();
        this.servicoMovimento = new Servicos();
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

    public GrupoEvento getGrupoEvento() {
        return grupoEvento;
    }

    public void setGrupoEvento(GrupoEvento grupoEvento) {
        this.grupoEvento = grupoEvento;
    }

    public Servicos getServicoMovimento() {
        return servicoMovimento;
    }

    public void setServicoMovimento(Servicos servicoMovimento) {
        this.servicoMovimento = servicoMovimento;
    }
}
