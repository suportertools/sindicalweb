package br.com.rtools.suporte;

import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pro_os")
public class OrdemServico implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_protocolo", referencedColumnName = "id")
    @ManyToOne
    private Protocolo protocolo;
    @JoinColumn(name = "id_modulo", referencedColumnName = "id")
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id")
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "id_responsavel", referencedColumnName = "id")
    @ManyToOne
    private Usuario responsavel;
    @Column(name = "ds_historico", length = 2000)
    private String historico;
    @Column(name = "ds_historico_interno", length = 2000)
    private String historicoInterno;
    @JoinColumn(name = "id_prioridade", referencedColumnName = "id")
    @ManyToOne
    private Prioridade prioridade;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private ProStatus proStatus;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_previsao")
    private Date dataPrevisao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_conclusao")
    private Date dataConclusao;

    public OrdemServico() {
        this.id = -1;
        this.protocolo = new Protocolo();
        this.modulo = new Modulo();
        this.rotina = new Rotina();
        this.responsavel = new Usuario();
        this.historico = "";
        this.historicoInterno = "";
        this.prioridade = new Prioridade();
        this.proStatus = new ProStatus();
        this.dataPrevisao = null;
        this.dataConclusao = null;
    }

    public OrdemServico(int id, Protocolo protocolo, Modulo modulo, Rotina rotina, Usuario responsavel, String historico, String historicoInterno, Prioridade prioridade, ProStatus proStatus, Date dataPrevisao, Date dataConclusao) {
        this.id = id;
        this.protocolo = protocolo;
        this.modulo = modulo;
        this.rotina = rotina;
        this.responsavel = responsavel;
        this.historico = historico;
        this.historicoInterno = historicoInterno;
        this.prioridade = prioridade;
        this.proStatus = proStatus;
        this.dataPrevisao = dataPrevisao;
        this.dataConclusao = dataConclusao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Protocolo getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(Protocolo protocolo) {
        this.protocolo = protocolo;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getHistoricoInterno() {
        return historicoInterno;
    }

    public void setHistoricoInterno(String historicoInterno) {
        this.historicoInterno = historicoInterno;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(Prioridade prioridade) {
        this.prioridade = prioridade;
    }

    public Date getDataPrevisao() {
        return dataPrevisao;
    }

    public void setDataPrevisao(Date dataPrevisao) {
        this.dataPrevisao = dataPrevisao;
    }

    public String getDataPrevisaoString() {
        return DataHoje.converteData(dataPrevisao);
    }

    public void setDataPrevisaoString(String dataPrevisao) {
        this.dataPrevisao = DataHoje.converte(dataPrevisao);
    }

    public Date getDataConclusao() {
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public String getDataConclusaoString() {
        return DataHoje.converteData(dataConclusao);
    }

    public void setDataConclusaoString(String dataConclusao) {
        this.dataConclusao = DataHoje.converte(dataConclusao);
    }

    public Usuario getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Usuario responsavel) {
        this.responsavel = responsavel;
    }

    public ProStatus getProStatus() {
        return proStatus;
    }

    public void setProStatus(ProStatus proStatus) {
        this.proStatus = proStatus;
    }

    @Override
    public String toString() {
        return "OrdemServico{" + "id=" + id + ", protocolo=" + protocolo + ", modulo=" + modulo + ", rotina=" + rotina + ", responsavel=" + responsavel + ", historico=" + historico + ", historicoInterno=" + historicoInterno + ", prioridade=" + prioridade + ", proStatus=" + proStatus + ", dataPrevisao=" + dataPrevisao + ", dataConclusao=" + dataConclusao + '}';
    }

}
