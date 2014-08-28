package br.com.rtools.financeiro;

import br.com.rtools.seguranca.Usuario;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "fin_filtro_lancamento")
@NamedQuery(name = "FiltroLancamento.pesquisaID", query = "select fl from FiltroLancamento fl where fl.id = :pid")
public class FiltroLancamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_lote", referencedColumnName = "id")
    @ManyToOne
    private Lote lote;
    @JoinColumn(name = "id_operacao", referencedColumnName = "id")
    @ManyToOne
    private Operacao operacao;
    @JoinColumn(name = "id_centro_custo", referencedColumnName = "id")
    @ManyToOne
    private CentroCusto centroCusto;
    @JoinColumn(name = "id_tipo_centro_custo", referencedColumnName = "id")
    @ManyToOne
    private CentroCustoContabilSub tipoCentroCusto;
    @JoinColumn(name = "id_conta_operacao", referencedColumnName = "id")
    @ManyToOne
    private ContaOperacao contaOperacao;
    @JoinColumn(name = "id_usuario", referencedColumnName = "id")
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
