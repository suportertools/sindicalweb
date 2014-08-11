package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "FIN_SERVICO_PESSOA")
@NamedQuery(name = "ServicoPessoa.pesquisaID", query = "select sp from ServicoPessoa sp where sp.id=:pid")
public class ServicoPessoa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date dtEmissao;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Column(name = "DESCONTO_FOLHA", nullable = true)
    private boolean descontoFolha;
    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Servicos servicos;
    @Column(name = "NR_DESCONTO", nullable = true)
    private float nrDesconto;
    @Column(name = "DS_REF_VIGORACAO", length = 7, nullable = true)
    private String referenciaVigoracao;
    @Column(name = "DS_REF_VALIDADE", length = 7, nullable = true)
    private String referenciaValidade;
    @Column(name = "NR_DIA_VENCIMENTO", length = 10, nullable = true)
    private int nrDiaVencimento;
    @JoinColumn(name = "ID_TIPO_DOCUMENTO", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private FTipoDocumento tipoDocumento;
    @JoinColumn(name = "ID_COBRANCA", referencedColumnName = "ID", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa cobranca;
    @Column(name = "IS_ATIVO", nullable = true, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private boolean ativo;
    @Column(name = "IS_BANCO", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean banco;
    @Column(name = "NR_VALOR_FIXO", length = 10, nullable = true)
    private float nrValorFixo;

    public ServicoPessoa() {
        this.id = -1;
        this.setEmissao(DataHoje.data());
        this.pessoa = new Pessoa();
        this.descontoFolha = false;
        this.servicos = new Servicos();
        this.nrDesconto = 0;
        this.referenciaVigoracao = DataHoje.data().substring(3, 10);
        this.referenciaValidade = null;
        this.nrDiaVencimento = 10;
        this.tipoDocumento = new FTipoDocumento();
        this.cobranca = new Pessoa();
        this.ativo = true;
        this.banco = false;
        this.nrValorFixo = 0;
    }

    public ServicoPessoa(int id, String emissao, Pessoa pessoa, boolean descontoFolha, Servicos servicos, float nr_desconto, String referenciaVigoracao,
            String referenciaValidade, int nrDiaVencimento, FTipoDocumento tipoDocumento, Pessoa cobranca, boolean ativo, boolean banco, float nrValorFixo) {
        this.id = id;
        this.setEmissao(emissao);
        this.pessoa = pessoa;
        this.descontoFolha = descontoFolha;
        this.servicos = servicos;
        this.nrDesconto = nr_desconto;
        this.referenciaVigoracao = referenciaVigoracao;
        this.referenciaValidade = referenciaValidade;
        this.nrDiaVencimento = nrDiaVencimento;
        this.tipoDocumento = tipoDocumento;
        this.cobranca = cobranca;
        this.ativo = ativo;
        this.banco = banco;
        this.nrValorFixo = nrValorFixo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public boolean isDescontoFolha() {
        return descontoFolha;
    }

    public void setDescontoFolha(boolean descontoFolha) {
        this.descontoFolha = descontoFolha;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public float getNrDesconto() {
        return nrDesconto;
    }

    public void setNrDesconto(float nrDesconto) {
        this.nrDesconto = nrDesconto;
    }

    public String getNrDescontoString() {
        return Moeda.converteR$Float(nrDesconto);
    }

    public void setNrDescontoString(String nrDescontoString) {
        this.nrDesconto = Moeda.converteUS$(nrDescontoString);
    }

    public String getReferenciaVigoracao() {
        return referenciaVigoracao;
    }

    public void setReferenciaVigoracao(String referenciaVigoracao) {
        this.referenciaVigoracao = referenciaVigoracao;
    }

    public String getReferenciaValidade() {
        return referenciaValidade;
    }

    public void setReferenciaValidade(String referenciaValidade) {
        this.referenciaValidade = referenciaValidade;
    }

    public int getNrDiaVencimento() {
        return nrDiaVencimento;
    }

    public void setNrDiaVencimento(int nrDiaVencimento) {
        this.nrDiaVencimento = nrDiaVencimento;
    }

    public FTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(FTipoDocumento FtipoDocumento) {
        this.tipoDocumento = FtipoDocumento;
    }

    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
    }

    public Pessoa getCobranca() {
        return cobranca;
    }

    public void setCobranca(Pessoa cobranca) {
        this.cobranca = cobranca;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public boolean isBanco() {
        return banco;
    }

    public void setBanco(boolean banco) {
        this.banco = banco;
    }

    public float getNrValorFixo() {
        return nrValorFixo;
    }

    public void setNrValorFixo(float nrValorFixo) {
        this.nrValorFixo = nrValorFixo;
    }
}
