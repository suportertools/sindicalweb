package br.com.rtools.suporte;

import br.com.rtools.seguranca.Modulo;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "PRO_OS")
public class OrdemServico implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PROTOCOLO", referencedColumnName = "ID")
    @ManyToOne
    private Protocolo protocolo;
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private Modulo modulo;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID")
    @ManyToOne
    private Rotina rotina;
    @JoinColumn(name = "ID_RESPONSAVEL", referencedColumnName = "ID")
    @ManyToOne
    private Usuario responsavel;
    @Column(name = "DS_HISTORICO", length = 2000)
    private String historico;
    @Column(name = "DS_HISTORICO_INTERNO", length = 2000)
    private String historicoInterno;
    @JoinColumn(name = "ID_PRIORIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Prioridade prioridade;
    @JoinColumn(name = "ID_STATUS", referencedColumnName = "ID")
    @ManyToOne
    private ProStatus proStatus;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PREVISAO")
    private Date dataPrevisao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CONCLUSAO")
    private Date dataConclusao;

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
}
