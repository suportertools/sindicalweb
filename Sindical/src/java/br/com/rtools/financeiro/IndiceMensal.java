package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_INDICE_MENSAL")
@NamedQuery(name="IndiceMensal.pesquisaID", query="select i from IndiceMensal i where i.id=:pid")
public class IndiceMensal implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="NR_ANO", length=4,nullable=false)
    private int ano;
    @Column(name="NR_MES", length=2,nullable=false)
    private int mes;
    @Column(name="NR_VALOR", nullable=false)
    private float valor;
    @JoinColumn(name="ID_INDICE", referencedColumnName="ID", nullable=false)
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