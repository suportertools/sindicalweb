package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "FIN_FORMA_PAGAMENTO")
@NamedQuery(name = "FormaPagamento.pesquisaID", query = "select fp from FormaPagamento fp where fp.id=:pid")
public class FormaPagamento implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_BAIXA", referencedColumnName = "ID")
    @OneToOne
    private Baixa baixa;
    @JoinColumn(name = "ID_CHEQUE_REC", referencedColumnName = "ID")
    @OneToOne
    private ChequeRec chequeRec;
    @JoinColumn(name = "ID_CHEQUE_PAG", referencedColumnName = "ID")
    @OneToOne
    private ChequePag chequePag;
    @Column(name = "NR_VALORP", length = 10)
    private float valorP;
    @Column(name = "NR_VALOR", length = 10)
    private float valor;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID")
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "ID_PLANO5", referencedColumnName = "ID")
    @ManyToOne
    private Plano5 plano5;
    @JoinColumn(name = "ID_CARTAO_PAG", referencedColumnName = "ID")
    @OneToOne
    private CartaoPag cartaoPag;
    @JoinColumn(name = "ID_CARTAO_REC", referencedColumnName = "ID")
    @OneToOne
    private CartaoRec cartaoRec;
    @JoinColumn(name = "ID_TIPO_PAGAMENTO", referencedColumnName = "ID")
    @OneToOne
    private TipoPagamento tipoPagamento;
    @Column(name = "NR_VALOR_LIQUIDO", length = 10)
    private float valorLiquido;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CREDITO", nullable = false)
    private Date dtCredito;

    public FormaPagamento() {
        this.id = -1;
        this.baixa = new Baixa();
        this.chequeRec = new ChequeRec();
        this.chequePag = new ChequePag();
        this.valorP = 0;
        this.valor = 0;
        this.filial = new Filial();
        this.plano5 = new Plano5();
        this.cartaoPag = new CartaoPag();
        this.cartaoRec = new CartaoRec();
        this.tipoPagamento = new TipoPagamento();
        this.valorLiquido = 0;
        this.dtCredito = null;
    }

    public FormaPagamento(int id,
            Baixa baixa,
            ChequeRec chequeRec,
            ChequePag chequePag,
            float valorP,
            float valor,
            Filial filial,
            Plano5 plano5,
            CartaoPag cartaoPag,
            CartaoRec cartaoRec,
            TipoPagamento tipoPagamento,
            float valorLiquido,
            Date dtCredito) {
        this.id = id;
        this.baixa = baixa;
        this.chequeRec = chequeRec;
        this.chequePag = chequePag;
        this.valorP = valorP;
        this.valor = valor;
        this.filial = filial;
        this.plano5 = plano5;
        this.cartaoPag = cartaoPag;
        this.cartaoRec = cartaoRec;
        this.tipoPagamento = tipoPagamento;
        this.valorLiquido = valorLiquido;
        this.dtCredito = dtCredito;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Baixa getBaixa() {
        return baixa;
    }

    public void setBaixa(Baixa baixa) {
        this.baixa = baixa;
    }

    public ChequeRec getChequeRec() {
        return chequeRec;
    }

    public void setChequeRec(ChequeRec chequeRec) {
        this.chequeRec = chequeRec;
    }

    public ChequePag getChequePag() {
        return chequePag;
    }

    public void setChequePag(ChequePag chequePag) {
        this.chequePag = chequePag;
    }

    public float getValorP() {
        return valorP;
    }

    public void setValorP(float valorP) {
        this.valorP = valorP;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public CartaoPag getCartaoPag() {
        return cartaoPag;
    }

    public void setCartaoPag(CartaoPag cartaoPag) {
        this.cartaoPag = cartaoPag;
    }

    public CartaoRec getCartaoRec() {
        return cartaoRec;
    }

    public void setCartaoRec(CartaoRec cartaoRec) {
        this.cartaoRec = cartaoRec;
    }

    public TipoPagamento getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(TipoPagamento tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public float getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(float valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public Date getDtCredito() {
        return dtCredito;
    }

    public void setDtCredito(Date dtCredito) {
        this.dtCredito = dtCredito;
    }
}