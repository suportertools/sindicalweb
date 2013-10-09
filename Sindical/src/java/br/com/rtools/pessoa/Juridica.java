package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "PES_JURIDICA")
@NamedQuery(name = "Juridica.pesquisaID", query = "select jur from Juridica jur where jur.id=:pid")
public class Juridica implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = false)
    @OneToOne //(fetch=FetchType.EAGER)//(optional=false)   (cascade=CascadeType.ALL)
    private Pessoa pessoa;
    @Column(name = "DS_FANTASIA", length = 200, nullable = false)
    private String fantasia;
    @JoinColumn(name = "ID_CNAE", referencedColumnName = "ID", nullable = true)
    @OneToOne(fetch = FetchType.EAGER)
    private Cnae cnae;
    @JoinColumn(name = "ID_CONTABILIDADE", referencedColumnName = "ID", nullable = true)
    @OneToOne
    private Juridica contabilidade;
    @Column(name = "DS_INSCRICAO_ESTADUAL", length = 30, nullable = true)
    private String inscricaoEstadual;
    @Column(name = "DS_INSCRICAO_MUNICIPAL", length = 30, nullable = true)
    private String inscricaoMunicipal;
    @Column(name = "DS_CONTATO", length = 50, nullable = true)
    private String contato;
    @Column(name = "DS_RESPONSAVEL", length = 50, nullable = true)
    private String responsavel;
    @JoinColumn(name = "ID_PORTE", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Porte porte;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ABERTURA")
    private Date dtAbertura;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_FECHAMENTO")
    private Date dtFechamento;
    @Column(name = "IS_EMAIL_ESCRITORIO")
    private boolean emailEscritorio;

    public Juridica() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.fantasia = "";
        this.cnae = new Cnae();
        this.contabilidade = null;
        this.inscricaoEstadual = "";
        this.inscricaoMunicipal = "";
        this.contato = "";
        this.responsavel = "";
        this.porte = new Porte();
        setAbertura("");
        setFechamento("");
        this.emailEscritorio = false;
    }

    public Juridica(int id, Pessoa pessoa, String fantasia, Cnae cnae, Juridica contabilidade, String inscricaoEstadual, String inscricaoMunicipal, String contato, String responsavel, Porte porte, String abertura, String fechamento, boolean emailEscritorio) {
        this.id = id;
        this.pessoa = pessoa;
        this.fantasia = fantasia;
        this.cnae = cnae;
        this.contabilidade = contabilidade;
        this.inscricaoEstadual = inscricaoEstadual;
        this.inscricaoMunicipal = inscricaoMunicipal;
        this.contato = contato;
        this.responsavel = responsavel;
        this.porte = porte;
        setAbertura(abertura);
        setFechamento(fechamento);
        this.emailEscritorio = emailEscritorio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public Cnae getCnae() {
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public String getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(String inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    public String getInscricaoMunicipal() {
        return inscricaoMunicipal;
    }

    public void setInscricaoMunicipal(String inscricaoMunicipal) {
        this.inscricaoMunicipal = inscricaoMunicipal;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Date getDtAbertura() {
        return dtAbertura;
    }

    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }

    public Date getDtFechamento() {
        return dtFechamento;
    }

    public void setDtFechamento(Date dtFechamento) {
        this.dtFechamento = dtFechamento;
    }

    public String getAbertura() {
        if (dtAbertura != null) {
            return DataHoje.converteData(dtAbertura);
        } else {
            return "";
        }
    }

    public void setAbertura(String abertura) {
        if (!(abertura.isEmpty())) {
            this.dtAbertura = DataHoje.converte(abertura);
        }
    }

    public String getFechamento() {
        if (dtAbertura != null) {
            return DataHoje.converteData(dtFechamento);
        } else {
            return "";
        }
    }

    public void setFechamento(String fechamento) {
        if (!(fechamento.isEmpty())) {
            this.dtFechamento = DataHoje.converte(fechamento);
        }
    }

    public Porte getPorte() {
        return porte;
    }

    public void setPorte(Porte porte) {
        this.porte = porte;
    }

    public Juridica getContabilidade() {
        return contabilidade;
    }

    public void setContabilidade(Juridica contabilidade) {
        this.contabilidade = contabilidade;
    }

    public boolean isEmailEscritorio() {
        return emailEscritorio;
    }

    public void setEmailEscritorio(boolean emailEscritorio) {
        this.emailEscritorio = emailEscritorio;
    }
}