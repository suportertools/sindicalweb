package br.com.rtools.atendimento;

import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "ate_movimento")
@NamedQuery(name = "AteMovimento.pesquisaID", query = "select amov from AteMovimento amov where amov.id=:pid")
public class AteMovimento implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_sis_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private SisPessoa pessoa;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Filial filial;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dataEmissao;
    @Column(name = "ds_hora", length = 5)
    private String horaEmissao;
    @JoinColumn(name = "id_operacao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private AteOperacao operacao;
    @Column(name = "ds_historico", length = 500)
    private String historico;
    @JoinColumn(name = "id_status", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private AteStatus status;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id")
    @ManyToOne
    private Juridica juridica;
    @JoinColumn(name = "id_atendente", referencedColumnName = "id")
    @ManyToOne
    private Usuario atendente;
    @JoinColumn(name = "id_reserva", referencedColumnName = "id")
    @ManyToOne
    private Usuario reserva;
    
    public AteMovimento() {
        this.id = -1;
        this.pessoa = new SisPessoa();
        this.filial = new Filial();
        setDataEmissaoString(DataHoje.data());
        this.dataEmissao = new Date();
        this.horaEmissao = "";
        this.operacao = new AteOperacao();
        this.historico = "";
        this.status = new AteStatus();
        this.juridica = new Juridica();
        this.atendente = new Usuario();
        this.reserva = null;
    }

    public AteMovimento(int id, SisPessoa pessoa, Filial filial, String dataEmissao, String horaEmissao, AteOperacao operacao, String historico, AteStatus status, Juridica juridica, Usuario atendente, Usuario reserva) {
        this.id = id;
        this.pessoa = pessoa;
        this.filial = filial;
        setDataEmissaoString(dataEmissao);
        this.horaEmissao = horaEmissao;
        this.operacao = operacao;
        this.historico = historico;
        this.status = status;
        this.juridica = juridica;
        this.atendente = atendente;
        this.reserva = reserva;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SisPessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(SisPessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Date getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(Date dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getHoraEmissao() {
        return horaEmissao;
    }

    public void setHoraEmissao(String horaEmissao) {
        this.horaEmissao = horaEmissao;
    }

    public AteOperacao getOperacao() {
        return operacao;
    }

    public void setOperacao(AteOperacao operacao) {
        this.operacao = operacao;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public String getDataEmissaoString() {
        return DataHoje.converteData(this.dataEmissao);
    }

    public void setDataEmissaoString(String criacao) {
        this.dataEmissao = DataHoje.converte(criacao);
    }

    public AteStatus getStatus() {
        return status;
    }

    public void setStatus(AteStatus status) {
        this.status = status;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Usuario getAtendente() {
        return atendente;
    }

    public void setAtendente(Usuario atendente) {
        this.atendente = atendente;
    }

    public Usuario getReserva() {
        return reserva;
    }

    public void setReserva(Usuario reserva) {
        this.reserva = reserva;
    }
}
