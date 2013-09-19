package br.com.rtools.escola;

import br.com.rtools.associativo.Midia;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="MATR_ESCOLA")
@NamedQuery(name="MatriculaEscola.pesquisaID", query="SELECT MAE FROM MatriculaEscola MAE WHERE MAE.id = :pid")
public class MatriculaEscola implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DATA")
    private Date dataMatricula;
  
    @JoinColumn(name="ID_STATUS", referencedColumnName="ID")
    @ManyToOne
    private EscStatus escStatus;

    @JoinColumn(name="ID_RESPONSAVEL", referencedColumnName="ID")
    @ManyToOne
    private Pessoa responsavel;

    @JoinColumn(name="ID_ALUNO", referencedColumnName="ID")
    @ManyToOne
    private Pessoa aluno;

    @JoinColumn(name="ID_VENDEDOR", referencedColumnName="ID")
    @ManyToOne
    private Vendedor vendedor;

    @JoinColumn(name="ID_MIDIA", referencedColumnName="ID")
    @ManyToOne
    private Midia midia;

    @JoinColumn(name="ID_EVT", referencedColumnName="ID")
    @ManyToOne
    private Evt evt;

    @Column(name="NR_PARCELAS")
    private int numeroParcelas;

    @Column(name="NR_DESCONTO")
    private float desconto;

    @Column(name="DS_OBS", length=200)
    private String obs;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_STATUS")
    private Date status;
    
    @JoinColumn(name="ID_FINANCEIRO", referencedColumnName="ID" )
    @ManyToOne
    private EFinanceiro esEFinanceiro;

    @Column(name="NR_VALOR_TOTAL")
    private float valorTotal;

    @Column(name="NR_DIA_VENCIMENTO")
    private int diaVencimento;

    @Column(name="NR_DESCONTO_ATE_VENCIMENTO")
    private float descontoAteVencimento;
    
    @JoinColumn(name="ID_TIPO_DOCUMENTO", referencedColumnName="ID" )
    @ManyToOne
    private FTipoDocumento tipoDocumento;
    
    @JoinColumn(name="ID_FILIAL", referencedColumnName="ID" )
    @ManyToOne
    private Filial filial;

    @Column(name="IS_DESCONTO_FOLHA")
    private boolean descontoFolha;
    
    @Column(name="IS_HABILITADO")
    private boolean habilitado;
    
    public MatriculaEscola() {
        id = -1;
        dataMatricula = new Date();
        escStatus = new EscStatus();
        responsavel = new Pessoa();
        aluno = new Pessoa();
        vendedor = new Vendedor();
        midia = new Midia();
        evt = null;
        numeroParcelas = 1;
        desconto = 0;
        obs = "";
        status = new Date();
        esEFinanceiro = new EFinanceiro();
        valorTotal = 0;
        diaVencimento = 0;
        descontoAteVencimento = 0;
        tipoDocumento = new FTipoDocumento();
        filial = new Filial();
        descontoFolha = false;
        habilitado = true;
    }

    public MatriculaEscola(int id, Date dataMatricula, EscStatus escStatus, Pessoa responsavel, Pessoa aluno, Vendedor vendedor, Midia midia, Evt evt, int numeroParcelas, float desconto, String obs, Date status, EFinanceiro esEFinanceiro, float valorTotal, int diaVencimento, float descontoAteVencimento, FTipoDocumento tipoDocumento, Filial filial, boolean descontoFolha, boolean habilitado) {
        this.id = id;
        this.dataMatricula = dataMatricula;
        this.escStatus = escStatus;
        this.responsavel = responsavel;
        this.aluno = aluno;
        this.vendedor = vendedor;
        this.midia = midia;
        this.evt = evt;
        this.numeroParcelas = numeroParcelas;
        this.desconto = desconto;
        this.obs = obs;
        this.status = status;
        this.esEFinanceiro = esEFinanceiro;
        this.valorTotal = valorTotal;
        this.diaVencimento = diaVencimento;
        this.descontoAteVencimento = descontoAteVencimento;
        this.tipoDocumento = tipoDocumento; 
        this.filial = filial; 
        this.descontoFolha = descontoFolha; 
        this.habilitado = habilitado; 
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(Date dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public EscStatus getEscStatus() {
        return escStatus;
    }

    public void setEscStatus(EscStatus escStatus) {
        this.escStatus = escStatus;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public Pessoa getAluno() {
        return aluno;
    }

    public void setAluno(Pessoa aluno) {
        this.aluno = aluno;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Midia getMidia() {
        return midia;
    }

    public void setMidia(Midia midia) {
        this.midia = midia;
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

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getStatus() {
        return status;
    }

    public void setStatus(Date status) {
        this.status = status;
    }

    public EFinanceiro getEsEFinanceiro() {
        return esEFinanceiro;
    }

    public void setEsEFinanceiro(EFinanceiro esEFinanceiro) {
        this.esEFinanceiro = esEFinanceiro;
    }

    public float getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(float valorTotal) {
        this.valorTotal = valorTotal;
    }

    public int getDiaVencimento() {
        return diaVencimento;
    }

    public void setDiaVencimento(int diaVencimento) {
        this.diaVencimento = diaVencimento;
    }

    public float getDescontoAteVencimento() {
        return descontoAteVencimento;
    }

    public void setDescontoAteVencimento(float descontoAteVencimento) {
        this.descontoAteVencimento = descontoAteVencimento;
    }

    public void setDataMatriculaString (String dataMatricula){
        this.dataMatricula = DataHoje.converte(dataMatricula);
    }

    public String getDataMatriculaString (){
        return DataHoje.converteData(dataMatricula);
    }

    public void setStatusString (String status){
        this.status = DataHoje.converte(status);
    }

    public String getStatusString(){
        return DataHoje.converteData(status);
    }

    public String getValorTotalString() {
        return Moeda.converteR$Float(valorTotal);
    }

    public void setValorTotalString(String valorTotal) {
        this.valorTotal = Moeda.substituiVirgulaFloat(valorTotal);
    }
    
    public String getDescontoString() {
        return Moeda.converteR$Float(desconto);
    }

    public void setDescontoString(String desconto) {
        this.desconto = Moeda.substituiVirgulaFloat(desconto);
    }

    public String getDescontoAteVencimentoString() {
        return Moeda.converteR$Float(descontoAteVencimento);
    }

    public void setDescontoAteVencimentoString(String descontoAteVencimento) {
        this.descontoAteVencimento = Moeda.substituiVirgulaFloat(descontoAteVencimento);
   }

    public FTipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(FTipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public boolean isDescontoFolha() {
        return descontoFolha;
    }

    public void setDescontoFolha(boolean descontoFolha) {
        this.descontoFolha = descontoFolha;
    }
    
    public boolean isHabilitado() {
        return habilitado;
    }

    public void setHabilitado(boolean habilitado) {
        this.habilitado = habilitado;
    }
}