package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_indice_mensal")
@NamedQuery(name = "IndiceMensal.pesquisaID", query = "select i from IndiceMensal i where i.id=:pid")
public class IndiceMensal implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_ano", length = 4, nullable = false)
    private int ano;
    @Column(name = "nr_mes", length = 2, nullable = false)
    private int mes;
    @Column(name = "nr_valor", nullable = false)
    private float valor;
    @JoinColumn(name = "id_indice", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Indice indice;

    public IndiceMensal() {
        this.id = -1;
        this.ano = -1;
        this.mes = -1;
        this.valor = 0;
    }

    public IndiceMensal(int id, int ano, int mes, float valor) {
        this.id = id;
        this.ano = ano;
        this.mes = mes;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAno() {
        return ano;
    }

    public void setAno(int ano) {
        this.ano = ano;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }
}
