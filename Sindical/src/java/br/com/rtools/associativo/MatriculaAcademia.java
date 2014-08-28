package br.com.rtools.associativo;

import br.com.rtools.academia.AcademiaServicoValor;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.text.SimpleDateFormat;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "matr_academia")
@NamedQuery(name = "MatriculaAcademia.pesquisaID", query = "select ma from MatriculaAcademia ma where ma.id=:pid")
public class MatriculaAcademia implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inativo")
    private Date dtInativo;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private ServicoPessoa servicoPessoa;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    @JoinColumn(name = "id_servico_valor", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private AcademiaServicoValor academiaServicoValor;
    @JoinColumn(name = "id_evt", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Evt evt;
    @Column(name = "nr_parcelas")
    private int numeroParcelas;
    @Column(name = "is_taxa", columnDefinition = "boolean default false")
    private boolean taxa;
    @Column(name = "is_taxa_cartao", columnDefinition = "boolean default false")
    private boolean taxaCartao;
    @Column(name = "ds_motivo_inativacao", length = 250)
    private String motivoInativacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade")
    private Date dtValidade;

    public MatriculaAcademia() {
        this.id = -1;
        this.setInativo("");
        this.servicoPessoa = new ServicoPessoa();
        this.usuario = new Usuario();
        this.academiaServicoValor = new AcademiaServicoValor();
        this.evt = new Evt();
        this.numeroParcelas = 0;
        this.taxa = false;
        this.taxaCartao = false;
        this.motivoInativacao = "";
        this.setValidade("");
    }

    public MatriculaAcademia(int id, String inativo, ServicoPessoa servicoPessoa, Usuario usuario, AcademiaServicoValor academiaServicoValor, Evt evt, int numeroParcelas, boolean taxa, boolean taxaCartao, String motivoInativacao, String validade) {
        this.id = id;
        this.setInativo(inativo);
        this.servicoPessoa = servicoPessoa;
        this.usuario = usuario;
        this.academiaServicoValor = academiaServicoValor;
        this.evt = evt;
        this.numeroParcelas = numeroParcelas;
        this.taxa = taxa;
        this.taxaCartao = taxaCartao;
        this.motivoInativacao = motivoInativacao;
        this.dtValidade = DataHoje.converte(validade);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtInativo() {
        return dtInativo;
    }

    public void setDtInativo(Date dtInativo) {
        this.dtInativo = dtInativo;
    }

    public void selecionaDataInativacao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtInativo = DataHoje.converte(format.format(event.getObject()));
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public String getInativo() {
        if (dtInativo != null) {
            return DataHoje.converteData(dtInativo);
        } else {
            return "";
        }
    }

    public void setInativo(String inativo) {
        if (!(inativo.isEmpty())) {
            this.dtInativo = DataHoje.converte(inativo);
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public AcademiaServicoValor getAcademiaServicoValor() {
        return academiaServicoValor;
    }

    public void setAcademiaServicoValor(AcademiaServicoValor academiaServicoValor) {
        this.academiaServicoValor = academiaServicoValor;
    }

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public boolean isTaxa() {
        return taxa;
    }

    public void setTaxa(boolean taxa) {
        this.taxa = taxa;
    }

    public boolean isTaxaCartao() {
        return taxaCartao;
    }

    public void setTaxaCartao(boolean taxaCartao) {
        this.taxaCartao = taxaCartao;
    }

    public String getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(String motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }

    public Date getDtValidade() {
        return dtValidade;
    }

    public void setDtValidade(Date dtValidade) {
        this.dtValidade = dtValidade;
    }

    public String getValidade() {
        return DataHoje.converteData(dtValidade);
    }

    public void setValidade(String validade) {
        this.dtValidade = DataHoje.converte(validade);
    }
}
