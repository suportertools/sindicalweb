package br.com.rtools.associativo;

import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "conv_suspencao")
@NamedQuery(name = "ConviteSuspencao.pesquisaID", query = "SELECT CONS FROM ConviteSuspencao CONS WHERE CONS.id = :pid")
public class ConviteSuspencao implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_sis_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SisPessoa sisPessoa;
    @JoinColumn(name = "id_motivo_suspencao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private ConviteMotivoSuspencao conviteMotivoSuspencao;
    @Column(name = "ds_obs", length = 300, nullable = true)
    private String observacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicio")
    private Date dtInicio;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_fim")
    private Date dtFim;

    public ConviteSuspencao() {
        this.id = -1;
        this.sisPessoa = new SisPessoa();
        this.conviteMotivoSuspencao = new ConviteMotivoSuspencao();
        this.observacao = "";
        this.dtInicio = DataHoje.dataHoje();
        this.dtFim = DataHoje.dataHoje();
    }

    public ConviteSuspencao(int id, SisPessoa sisPessoa, ConviteMotivoSuspencao conviteMotivoSuspencao, String observacao, String inicio, String fim) {
        this.id = id;
        this.sisPessoa = sisPessoa;
        this.conviteMotivoSuspencao = conviteMotivoSuspencao;
        this.observacao = observacao;
        this.dtInicio = DataHoje.converte(inicio);
        this.dtFim = DataHoje.converte(fim);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SisPessoa getSisPessoa() {
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
    }

    public ConviteMotivoSuspencao getConviteMotivoSuspencao() {
        return conviteMotivoSuspencao;
    }

    public void setConviteMotivoSuspencao(ConviteMotivoSuspencao conviteMotivoSuspencao) {
        this.conviteMotivoSuspencao = conviteMotivoSuspencao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public String getInicio() {
        return DataHoje.converteData(dtInicio);
    }

    public void setDtInicio(String inicio) {
        this.dtInicio = DataHoje.converte(inicio);
    }

    public String getFim() {
        return DataHoje.converteData(dtFim);
    }

    public void setFim(String fim) {
        this.dtFim = DataHoje.converte(fim);
    }

    public void dataInicioListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtInicio = DataHoje.converte(format.format(event.getObject()));
    }

    public void dataFimListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtInicio = DataHoje.converte(format.format(event.getObject()));
    }

}
