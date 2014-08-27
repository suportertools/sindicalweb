package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_remessa_banco")
@NamedQuery(name = "RemessaBanco.pesquisaID", query = "select r from RemessaBanco r where r.id=:pid")
public class RemessaBanco implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @Column(name = "ds_hora", length = 5)
    private String hora;
    @Column(name = "nr_lote")
    private int lote;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;

    public RemessaBanco() {
        this.id = -1;
        setEmissao(DataHoje.data());
        this.hora = DataHoje.hora().substring(0, 5);
        this.lote = 0;
        this.movimento = new Movimento();
    }

    public RemessaBanco(int id, Date dtEmissao, String hora, int lote, Movimento movimento) {
        this.id = id;
        this.dtEmissao = dtEmissao;
        this.hora = hora;
        this.lote = lote;
        this.movimento = movimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public int getLote() {
        return lote;
    }

    public void setLote(int lote) {
        this.lote = lote;
    }
}
