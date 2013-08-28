
package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CORRECAO")
@NamedQuery(name="Correcao.pesquisaID", query="select i from Correcao i where i.id=:pid")
public class Correcao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="NR_MULTA_PRIMEIRO_MES", nullable=false)
    private float multaPriMes;
    @Column(name="NR_MULTA_APARTIR_2MES", nullable=false)
    private float multaApartir2Mes;
    @Column(name="NR_JUROS_PRI_MES", nullable=false)
    private float jurosPriMes;
    @Column(name="NR_JUROS_APARTIR_2MES", nullable=false)
    private float jurosApartir2Mes;
    @Column(name="NR_MULTA_POR_FUNCIONARIO", nullable=false)
    private float multaPorFuncionario;
    @Column(name="NR_JUROS_DIARIOS", nullable=false)
    private float jurosDiarios; 
    @JoinColumn(name="ID_INDICE", referencedColumnName="ID", nullable=false)
    @OneToOne
    private Indice indice;
    @JoinColumn(name="ID_SERVICOS", referencedColumnName="ID", nullable=false)
    @OneToOne
    private Servicos servicos;
    @Column(name="DS_REF_INICIAL", nullable=false)
    private String referenciaInicial;
    @Column(name="DS_REF_FINAL", nullable=false)
    private String referenciaFinal;

    public Correcao() {
        this.id = -1;
        this.multaPriMes = 0;
        this.multaApartir2Mes = 0;
        this.jurosPriMes = 0;
        this.jurosApartir2Mes = 0;
        this.multaPorFuncionario = 0;
        this.indice = new Indice();
        this.servicos = new Servicos();
        this.jurosDiarios = 0;
        this.referenciaInicial = "";
        this.referenciaFinal = "";
    }
    
    public Correcao(int id, float multaPriMes, float multaApartir2Mes, float jurosPriMes, float jurosApartir2Mes, float multaPorFuncionario, float jurosDiarios, Indice indice, Servicos servicos, String referenciaInicial, String referenciaFinal) {
        this.id = id;
        this.multaPriMes = multaPriMes;
        this.multaApartir2Mes = multaApartir2Mes;
        this.jurosPriMes = jurosPriMes;
        this.jurosApartir2Mes = jurosApartir2Mes;
        this.multaPorFuncionario = multaPorFuncionario;
        this.jurosDiarios = jurosDiarios;
        this.indice = indice;
        this.servicos = servicos;
        this.referenciaInicial = referenciaInicial;
        this.referenciaFinal = referenciaFinal;
    }    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getMultaPriMes() {
        return multaPriMes;
    }

    public void setMultaPriMes(float multaPriMes) {
        this.multaPriMes = multaPriMes;
    }

    public float getMultaApartir2Mes() {
        return multaApartir2Mes;
    }

    public void setMultaApartir2Mes(float multaApartir2Mes) {
        this.multaApartir2Mes = multaApartir2Mes;
    }

    public float getJurosPriMes() {
        return jurosPriMes;
    }

    public void setJurosPriMes(float jurosPriMes) {
        this.jurosPriMes = jurosPriMes;
    }

    public float getJurosApartir2Mes() {
        return jurosApartir2Mes;
    }

    public void setJurosApartir2Mes(float jurosApartir2Mes) {
        this.jurosApartir2Mes = jurosApartir2Mes;
    }

    public float getMultaPorFuncionario() {
        return multaPorFuncionario;
    }

    public void setMultaPorFuncionario(float multaPorFuncionario) {
        this.multaPorFuncionario = multaPorFuncionario;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public float getJurosDiarios() {
        return jurosDiarios;
    }

    public void setJurosDiarios(float jurosDiarios) {
        this.jurosDiarios = jurosDiarios;
    }

    public Indice getIndice() {
        return indice;
    }

    public void setIndice(Indice indice) {
        this.indice = indice;
    }

    public String getReferenciaInicial() {
        return referenciaInicial;
    }

    public void setReferenciaInicial(String referenciaInicial) {
        this.referenciaInicial = referenciaInicial;
    }

    public String getReferenciaFinal() {
        return referenciaFinal;
    }

    public void setReferenciaFinal(String referenciaFinal) {
        this.referenciaFinal = referenciaFinal;
    }
}