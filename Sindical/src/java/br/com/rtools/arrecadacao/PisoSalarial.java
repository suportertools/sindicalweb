package br.com.rtools.arrecadacao;

import br.com.rtools.pessoa.Porte;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ARR_PISO_SALARIAL")
@NamedQuery(name = "PisoSalarial.pesquisaID", query = "select ps from PisoSalarial ps where ps.id = :pid")
public class PisoSalarial implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PISO_SALARIAL_LOTE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private PisoSalarialLote pisoLote;
    @Column(name = "DS_DESCRICAO", length = 300, nullable = true)
    private String descricao;
    @Column(name = "NR_VALOR", nullable = false)
    private float valor;

    public PisoSalarial() {
        this.id = -1;
        this.pisoLote = new PisoSalarialLote();
        this.descricao = "";
        this.valor = 0;
    }

    public PisoSalarial(int id, PisoSalarialLote pisoLote, Porte porte, String descricao, float valor, int ano, String mensagem) {
        this.id = id;
        this.pisoLote = pisoLote;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public PisoSalarialLote getPisoSalarialLote() {
        return pisoLote;
    }

    public void setPisoSalarialLote(PisoSalarialLote pisoLote) {
        this.pisoLote = pisoLote;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }
}
