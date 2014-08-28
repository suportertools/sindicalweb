package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_cheques")
@NamedQuery(name = "Cheques.pesquisaID", query = "select c from Cheques c where c.id=:pid")
public class Cheques implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @Column(name = "nr_cheque", length = 100, nullable = false)
    private String numero;
    @Column(name = "dt_impressao", length = 10, nullable = true)
    private String dataImpressao;
    @Column(name = "dt_cancelamento", length = 10, nullable = true)
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
