package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_lote_remessa")
@NamedQuery(name = "LoteRemessa.pesquisaID", query = "select l from LoteRemessa l where l.id=:pid")
public class LoteRemessa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "nr_tipo_movimento", nullable = false)
    private int tipoMovimento;
    @Column(name = "ds_moeda", length = 2, nullable = false)
    private String moeda;
    @Column(name = "nr_contribuicao", nullable = false)
    private int tipoContribuicao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_remessa", nullable = false)
    private Date dtRemessa;

    public LoteRemessa(int id, int tipoMovimento, String moeda, int tipoContribuicao, Date dtRemessa) {
        this.id = id;
        this.tipoMovimento = tipoMovimento;
        this.moeda = moeda;
        this.tipoContribuicao = tipoContribuicao;
        this.dtRemessa = dtRemessa;
    }

    public LoteRemessa() {
        setId(-1);
        setTipoMovimento(0);
        setMoeda("");
        setTipoContribuicao(0);
        setRemessa(DataHoje.data());
    }

    public LoteRemessa(int id, int tipoMovimento, String moeda, int tipoContribuicao, String remessa) {
        setId(id);
        setTipoMovimento(tipoMovimento);
        setMoeda(moeda);
        setTipoContribuicao(tipoContribuicao);
        setRemessa(remessa);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(int tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public String getMoeda() {
        return moeda;
    }

    public void setMoeda(String moeda) {
        if (moeda.length() <= 2) {
            this.moeda = moeda;
        }
    }

    public int getTipoContribuicao() {
        return tipoContribuicao;
    }

    public void setTipoContribuicao(int tipoContribuicao) {
        this.tipoContribuicao = tipoContribuicao;
    }

    public Date getDtRemessa() {
        return dtRemessa;
    }

    public void setDtRemessa(Date dtRemessa) {
        this.dtRemessa = dtRemessa;
    }

    public String getRemessa() {
        return DataHoje.converteData(dtRemessa);
    }

    public void setRemessa(String remessa) {
        this.dtRemessa = DataHoje.converte(remessa);
    }
}
