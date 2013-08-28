package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="FIN_REMESSA_BANCO")
@NamedQuery(name="RemessaBanco.pesquisaID", query="select r from RemessaBanco r where r.id=:pid")
public class RemessaBanco implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    private Date dtEmissao;
    @Column(name="DS_HORA", length=5)
    private String hora;
    @Column(name="NR_LOTE")
    private int lote;
    @JoinColumn(name="ID_MOVIMENTO", referencedColumnName="ID", nullable=false)
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