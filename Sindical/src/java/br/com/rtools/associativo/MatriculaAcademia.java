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
@Table(name = "MATR_ACADEMIA")
@NamedQuery(name = "MatriculaAcademia.pesquisaID", query = "select ma from MatriculaAcademia ma where ma.id=:pid")
public class MatriculaAcademia implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_INATIVO")
    private Date dtInativo;
    @JoinColumn(name = "ID_SERVICO_PESSOA", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private ServicoPessoa servicoPessoa;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Usuario usuario;
    @JoinColumn(name = "ID_SERVICO_VALOR", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private AcademiaServicoValor academiaServicoValor;
    @JoinColumn(name = "ID_EVT", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Evt evt;
    @Column(name = "NR_PARCELAS")
    private int numeroParcelas;
    @Column(name = "IS_TAXA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean taxa;    
    @Column(name = "IS_TAXA_CARTAO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean taxaCartao;    

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
    }

    public MatriculaAcademia(int id, String inativo, ServicoPessoa servicoPessoa, Usuario usuario, AcademiaServicoValor academiaServicoValor, Evt evt, int numeroParcelas, boolean taxa, boolean taxaCartao) {
        this.id = id;
        this.setInativo(inativo);
        this.servicoPessoa = servicoPessoa;
        this.usuario = usuario;
        this.academiaServicoValor = academiaServicoValor;
        this.evt = evt;
        this.numeroParcelas = numeroParcelas;
        this.taxa = taxa;
        this.taxaCartao = taxaCartao;
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
}
