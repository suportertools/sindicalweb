
package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name="FIN_CHEQUES")
@NamedQuery(name="Cheques.pesquisaID", query="select c from Cheques c where c.id=:pid")
public class Cheques implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_MOVIMENTO", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Movimento movimento;
    @Column(name="NR_CHEQUE", length=100,nullable=false)
    private String numero;
    @Column(name="DT_IMPRESSAO", length=10,nullable=true)
    private String dataImpressao;
    @Column(name="DT_CANCELAMENTO", length=10,nullable=true)
    private String dataCancelamento;

    public Cheques() {
        this.id = -1;
        this.movimento = new Movimento();
        this.numero = "";
        this.dataImpressao = "";
        this.dataCancelamento = "";
    }    
    
    public Cheques(int id, Movimento movimento, String numero, String dataImpressao, String dataCancelamento) {
        this.id = id;
        this.movimento = movimento;
        this.numero = numero;
        this.dataImpressao = dataImpressao;
        this.dataCancelamento = dataCancelamento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDataImpressao() {
        return dataImpressao;
    }

    public void setDataImpressao(String dataImpressao) {
        this.dataImpressao = dataImpressao;
    }

    public String getDataCancelamento() {
        return dataCancelamento;
    }

    public void setDataCancelamento(String dataCancelamento) {
        this.dataCancelamento = dataCancelamento;
    }

}