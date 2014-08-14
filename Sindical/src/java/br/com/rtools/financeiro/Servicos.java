package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.sistema.Periodo;
import javax.persistence.*;

@Entity
@Table(name = "FIN_SERVICOS")
@NamedQuery(name = "Servicos.pesquisaID", query = "SELECT S FROM Servicos AS S WHERE S.id = :pid")
public class Servicos implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 100, nullable = true)
    private String descricao;
    @JoinColumn(name = "ID_FILIAL", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Filial filial;
    @JoinColumn(name = "ID_PLANO5", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Plano5 plano5;
    @JoinColumn(name = "ID_DEPARTAMENTO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Departamento departamento;
    @Column(name = "NR_VALIDADE_GUIA_DIAS", nullable = true)
    private int validade;
    @Column(name = "DS_CODIGO", length = 10, nullable = true)
    private String codigo;
    @Column(name = "DS_SITUACAO", length = 1, nullable = true)
    private String situacao;
    @Column(name = "IS_DEBITO_CLUBE", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean debito;
    @Column(name = "IS_ALTERA_VALOR", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean alterarValor;
    @Column(name = "IS_ADM", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean adm;
    @Column(name = "IS_TABELA", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean tabela;
    @Column(name = "IS_ELEICAO", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean eleicao;
    @Column(name = "IS_AGRUPA_BOLETO", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean agrupaBoleto;
    @Column(name = "IS_PRODUTO", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean produto;
    @JoinColumn(name = "ID_SUBGRUPO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private SubGrupoFinanceiro subGrupoFinanceiro;
    @Column(name = "IS_VALOR_FIXO", nullable = true, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean valorFixo;
    @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Periodo periodo;    
    @Column(name = "NR_QTDE_PERIODO")
    private int quantidadePeriodo;

    public Servicos() {
        this.id = -1;
        this.descricao = "";
        this.filial = new Filial();
        this.plano5 = new Plano5();
        this.departamento = new Departamento();
        this.validade = 0;
        this.codigo = "";
        this.situacao = "A";
        this.debito = false;
        this.alterarValor = false;
        this.adm = false;
        this.tabela = false;
        this.eleicao = false;
        this.agrupaBoleto = false;
        this.subGrupoFinanceiro = new SubGrupoFinanceiro();
        this.produto = false;
        this.valorFixo = false;
        this.periodo = new Periodo();
        this.quantidadePeriodo = 0;
    }

    public Servicos(int id,
            String descricao,
            Filial filial,
            Plano5 plano5,
            Departamento departamento,
            int validade,
            String codigo,
            String situacao,
            boolean debito,
            boolean alterarValor,
            boolean adm,
            boolean tabela,
            boolean eleicao,
            boolean agrupaBoleto,
            SubGrupoFinanceiro subGrupoFinanceiro,
            boolean produto,
            boolean valorFixo,
            Periodo periodo,
            int quantidadePeriodo) {
        this.id = id;
        this.descricao = descricao;
        this.filial = filial;
        this.plano5 = plano5;
        this.departamento = departamento;
        this.validade = validade;
        this.codigo = codigo;
        this.situacao = situacao;
        this.debito = debito;
        this.alterarValor = alterarValor;
        this.adm = adm;
        this.tabela = tabela;
        this.eleicao = eleicao;
        this.agrupaBoleto = agrupaBoleto;
        this.subGrupoFinanceiro = subGrupoFinanceiro;
        this.produto = produto;
        this.valorFixo = valorFixo;
        this.periodo = periodo;
        this.quantidadePeriodo = quantidadePeriodo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public int getValidade() {
        return validade;
    }

    public void setValidade(int validade) {
        this.validade = validade;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public boolean isDebito() {
        return debito;
    }

    public void setDebito(boolean debito) {
        this.debito = debito;
    }

    public boolean isAlterarValor() {
        return alterarValor;
    }

    public void setAlterarValor(boolean alterarValor) {
        this.alterarValor = alterarValor;
    }

    public boolean isAdm() {
        return adm;
    }

    public void setAdm(boolean adm) {
        this.adm = adm;
    }

    public boolean isTabela() {
        return tabela;
    }

    public void setTabela(boolean tabela) {
        this.tabela = tabela;
    }

    public boolean isEleicao() {
        return eleicao;
    }

    public void setEleicao(boolean eleicao) {
        this.eleicao = eleicao;
    }

    public boolean isAgrupaBoleto() {
        return agrupaBoleto;
    }

    public void setAgrupaBoleto(boolean agrupaBoleto) {
        this.agrupaBoleto = agrupaBoleto;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public SubGrupoFinanceiro getSubGrupoFinanceiro() {
        return subGrupoFinanceiro;
    }

    public void setSubGrupoFinanceiro(SubGrupoFinanceiro subGrupoFinanceiro) {
        this.subGrupoFinanceiro = subGrupoFinanceiro;
    }

    public boolean isProduto() {
        return produto;
    }

    public void setProduto(boolean produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {
        return "Servicos{" + "id=" + id + ", descricao=" + descricao + ", filial=" + filial + ", plano5=" + plano5 + ", departamento=" + departamento + ", validade=" + validade + ", codigo=" + codigo + ", situacao=" + situacao + ", debito=" + debito + ", alterarValor=" + alterarValor + ", adm=" + adm + ", tabela=" + tabela + ", eleicao=" + eleicao + ", agrupaBoleto=" + agrupaBoleto + ", produto=" + produto + ", subGrupoFinanceiro=" + subGrupoFinanceiro + '}';
    }

    public boolean isValorFixo() {
        return valorFixo;
    }

    public void setValorFixo(boolean valorFixo) {
        this.valorFixo = valorFixo;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public int getQuantidadePeriodo() {
        return quantidadePeriodo;
    }

    public void setQuantidadePeriodo(int quantidadePeriodo) {
        this.quantidadePeriodo = quantidadePeriodo;
    }
}
