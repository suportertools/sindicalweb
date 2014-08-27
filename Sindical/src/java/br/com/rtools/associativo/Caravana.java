package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "car_caravana")
@NamedQuery(name = "Caravana.pesquisaID", query = "select c from Caravana c where c.id=:pid")
public class Caravana implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_aevento", referencedColumnName = "id")
    @ManyToOne
    private AEvento aEvento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_saida", nullable = false)
    private Date dtSaida;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_retorno", nullable = false)
    private Date dtRetorno;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_chegada", nullable = false)
    private Date dtChegada;
    @Column(name = "tm_saida", nullable = false)
    private String horaSaida;
    @Column(name = "tm_retorno", nullable = false)
    private String horaRetorno;
    @Column(name = "tm_chegada", nullable = false)
    private String horaChegada;
    @Column(name = "is_cafe", nullable = true)
    private boolean isCafe;
    @Column(name = "is_almoco", nullable = true)
    private boolean isAlmoco;
    @Column(name = "is_jantar", nullable = true)
    private boolean isJantar;
    @Column(name = "nr_poltronas", nullable = true)
    private int quantidadePoltronas;
    @Column(name = "nr_guia_recolhimento", nullable = true)
    private int guiaRecolhimento;
    @Column(name = "ds_observacao")
    private String observacao;

    public Caravana(int id, AEvento aEvento, String dataSaida, String dataRetorno, String dataChegada, String horaSaida, String horaRetorno,
            String horaChegada, boolean isCafe, boolean isAlmoco, boolean isJantar, int quantidadePoltronas,
            int guiaRecolhimento, String observacao) {
        this.id = id;
        this.aEvento = aEvento;
        setDataSaida(dataSaida);
        setDataRetorno(dataRetorno);
        setDataChegada(dataChegada);
        this.horaSaida = horaSaida;
        this.horaRetorno = horaRetorno;
        this.horaChegada = horaChegada;
        this.isCafe = isCafe;
        this.isAlmoco = isAlmoco;
        this.isJantar = isJantar;
        this.quantidadePoltronas = quantidadePoltronas;
        this.guiaRecolhimento = guiaRecolhimento;
        this.observacao = observacao;
    }

    public Caravana() {
        this.id = -1;
        this.aEvento = new AEvento();
        setDataSaida("");
        setDataRetorno("");
        setDataChegada("");
        this.horaSaida = "";
        this.horaRetorno = "";
        this.horaChegada = "";
        this.isCafe = false;
        this.isAlmoco = false;
        this.isJantar = false;
        this.quantidadePoltronas = 0;
        this.guiaRecolhimento = 0;
        this.observacao = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public AEvento getaEvento() {
        return aEvento;
    }

    public void setaEvento(AEvento aEvento) {
        this.aEvento = aEvento;
    }

    public Date getDtSaida() {
        return dtSaida;
    }

    public void setDtSaida(Date dtSaida) {
        this.dtSaida = dtSaida;
    }

    public Date getDtRetorno() {
        return dtRetorno;
    }

    public void setDtRetorno(Date dtRetorno) {
        this.dtRetorno = dtRetorno;
    }

    public Date getDtChegada() {
        return dtChegada;
    }

    public void setDtChegada(Date dtChegada) {
        this.dtChegada = dtChegada;
    }

    public String getHoraSaida() {
        return horaSaida;
    }

    public void setHoraSaida(String horaSaida) {
        this.horaSaida = horaSaida;
    }

    public String getHoraRetorno() {
        return horaRetorno;
    }

    public void setHoraRetorno(String horaRetorno) {
        this.horaRetorno = horaRetorno;
    }

    public String getHoraChegada() {
        return horaChegada;
    }

    public void setHoraChegada(String horaChegada) {
        this.horaChegada = horaChegada;
    }

    public boolean isIsCafe() {
        return isCafe;
    }

    public void setIsCafe(boolean isCafe) {
        this.isCafe = isCafe;
    }

    public boolean isIsAlmoco() {
        return isAlmoco;
    }

    public void setIsAlmoco(boolean isAlmoco) {
        this.isAlmoco = isAlmoco;
    }

    public boolean isIsJantar() {
        return isJantar;
    }

    public void setIsJantar(boolean isJantar) {
        this.isJantar = isJantar;
    }

    public int getQuantidadePoltronas() {
        return quantidadePoltronas;
    }

    public void setQuantidadePoltronas(int quantidadePoltronas) {
        this.quantidadePoltronas = quantidadePoltronas;
    }

    public int getGuiaRecolhimento() {
        return guiaRecolhimento;
    }

    public void setGuiaRecolhimento(int guiaRecolhimento) {
        this.guiaRecolhimento = guiaRecolhimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataSaida() {
        if (dtSaida != null) {
            return DataHoje.converteData(dtSaida);
        } else {
            return "";
        }
    }

    public void setDataSaida(String dataSaida) {
        if (!(dataSaida.isEmpty())) {
            this.dtSaida = DataHoje.converte(dataSaida);
        }
    }

    public String getDataRetorno() {
        if (dtRetorno != null) {
            return DataHoje.converteData(dtRetorno);
        } else {
            return "";
        }
    }

    public void setDataRetorno(String dataRetorno) {
        if (!(dataRetorno.isEmpty())) {
            this.dtRetorno = DataHoje.converte(dataRetorno);
        }
    }

    public String getDataChegada() {
        if (dtChegada != null) {
            return DataHoje.converteData(dtChegada);
        } else {
            return "";
        }
    }

    public void setDataChegada(String dataChegada) {
        if (!(dataChegada.isEmpty())) {
            this.dtChegada = DataHoje.converte(dataChegada);
        }
    }
}
