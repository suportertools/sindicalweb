package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FIN_FILTRO_LANCAMENTO")
@NamedQuery(name = "FiltroLancamento.pesquisaID", query = "select fl from FiltroLancamento fl where fl.id = :pid")
public class FiltroLancamento implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_LOTE", referencedColumnName = "ID")
    @ManyToOne
    private Lote lote;
    @JoinColumn(name = "ID_OPERACAO", referencedColumnName = "ID")
    @ManyToOne
    private Operacao operacao;
    @JoinColumn(name = "ID_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCusto centroCusto;
    @JoinColumn(name = "ID_TIPO_CENTRO_CUSTO", referencedColumnName = "ID")
    @ManyToOne
    private CentroCustoContabilSub tipoCentroCusto;
    @JoinColumn(name = "ID_CONTA_OPERACAO", referencedColumnName = "ID")
    @ManyToOne
    private ContaOperacao contaOperacao;
    @JoinColumn(name = "ID_USUARIO", referencedColumnName = "ID")
    @ManyToOne
    private Usuario usuario;

    public FiltroLancamento() {
        this.id = -1;
        this.lote = new Lote();
        this.operacao = new Operacao();
        this.centroCusto = new CentroCusto();
        this.tipoCentroCusto = new CentroCustoContabilSub();
        this.contaOperacao = new ContaOperacao();
        this.usuario = new Usuario();
    }

    public FiltroLancamento(int id, Lote lote, Operacao operacao, CentroCusto centroCusto, CentroCustoContabilSub tipoCentroCusto, ContaOperacao contaOperacao, Usuario usuario) {
        this.id = id;
        this.lote = lote;
        this.operacao = operacao;
        this.centroCusto = centroCusto;
        this.tipoCentroCusto = tipoCentroCusto;
        this.contaOperacao = contaOperacao;
        this.usuario = usuario;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Operacao getOperacao() {
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public CentroCusto getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(CentroCusto centroCusto) {
        this.centroCusto = centroCusto;
    }

    public CentroCustoContabilSub getTipoCentroCusto() {
        return tipoCentroCusto;
    }

    public void setTipoCentroCusto(CentroCustoContabilSub tipoCentroCusto) {
        this.tipoCentroCusto = tipoCentroCusto;
    }

    public ContaOperacao getContaOperacao() {
        return contaOperacao;
    }

    public void setContaOperacao(ContaOperacao contaOperacao) {
        this.contaOperacao = contaOperacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
