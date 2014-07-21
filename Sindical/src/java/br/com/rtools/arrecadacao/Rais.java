package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.TipoRemuneracao;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.pessoa.Escolaridade;
import br.com.rtools.pessoa.IndicadorAlvara;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.Raca;
import br.com.rtools.pessoa.TipoDeficiencia;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ARR_RAIS")
public class Rais implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_EMISSAO")
    private Date emissao;
    @JoinColumn(name = "ID_NACIONALIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Nacionalidade nacionalidade;
    @JoinColumn(name = "ID_RACA", referencedColumnName = "ID")
    @ManyToOne
    private Raca raca;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne
    private Juridica empresa;
    @JoinColumn(name = "ID_ESCOLARIDADE", referencedColumnName = "ID")
    @ManyToOne
    private Escolaridade escolaridade;
    @JoinColumn(name = "ID_SIS_PESSOA", referencedColumnName = "ID")
    @ManyToOne
    private SisPessoa sisPessoa;
    @JoinColumn(name = "ID_CLASSIFICACAO_ECONOMICA", referencedColumnName = "ID")
    @ManyToOne
    private ClassificacaoEconomica classificacaoEconomica;
    @JoinColumn(name = "ID_PROFISSAO", referencedColumnName = "ID")
    @ManyToOne
    private Profissao profissao;
    @JoinColumn(name = "ID_TIPO_REMUNERACAO", referencedColumnName = "ID")
    @ManyToOne
    private TipoRemuneracao tipoRemuneracao;
    @JoinColumn(name = "ID_RESPONSAVEL_CADASTRO", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa responsavelCadastro;
    @JoinColumn(name = "ID_TIPO_DEFICIENCIA", referencedColumnName = "ID")
    @ManyToOne
    private TipoDeficiencia tipoDeficiencia;
    @JoinColumn(name = "ID_INDICADOR_ALVARA", referencedColumnName = "ID")
    @ManyToOne
    private IndicadorAlvara indicadorAlvara;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ADMISSAO")
    private Date admissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DEMISSAO")
    private Date demissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_AFASTAMENTO")
    private Date afastamento;
    @Column(name = "DS_MOTIVO_AFASTAMENTO", length = 500)
    private String motivoAfastamento;
    @Column(name = "DS_OBSERVACAO", length = 500)
    private String observacao;
    @Column(name = "NR_CARGA_HORARIA", columnDefinition = "INTEGER DEFAULT 0")
    private int cargaHoraria;
    @Column(name = "NR_SALARIO", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private float salario;
    @Column(name = "DS_FUNCAO", length = 255)
    private String funcao;
    @Column(name = "DS_CARTEIRA", length = 9)
    private String carteira;
    @Column(name = "DS_SERIE", length = 15)
    private String serie;
    @Column(name = "NR_CTPS", columnDefinition = "INTEGER DEFAULT 0")
    private int ctps;
    @Column(name = "NR_ANO_CHEGADA", columnDefinition = "INTEGER DEFAULT 0")
    private int anoChegada;
    @Column(name = "IS_ALVARA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean alvara;
    @Column(name = "IS_EMPREGADO_FILIADO", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean empregadoFiliado;
    @Column(name = "DS_PIS", length = 14)
    private String pis;

    public Rais() {
        this.id = -1;
        this.emissao = new Date();
        this.nacionalidade = new Nacionalidade();
        this.raca = new Raca();
        this.empresa = new Juridica();
        this.escolaridade = new Escolaridade();
        this.sisPessoa = new SisPessoa();
        this.classificacaoEconomica = new ClassificacaoEconomica();
        this.profissao = new Profissao();
        this.tipoRemuneracao = new TipoRemuneracao();
        this.responsavelCadastro = new Pessoa();
        this.tipoDeficiencia = new TipoDeficiencia();
        this.indicadorAlvara = new IndicadorAlvara();
        this.admissao = new Date();
        this.demissao = new Date();
        this.afastamento = new Date();
        this.motivoAfastamento = "";
        this.observacao = "";
        this.cargaHoraria = 0;
        this.salario = 0;
        this.funcao = "";
        this.carteira = "";
        this.serie = "";
        this.ctps = 0;
        this.anoChegada = 0;
        this.alvara = false;
        this.empregadoFiliado = false;
        this.pis = "";
    }

    public Rais(int id, Date emissao, Nacionalidade nacionalidade, Raca raca, Juridica empresa, Escolaridade escolaridade, SisPessoa sisPessoa, ClassificacaoEconomica classificacaoEconomica, Profissao profissao, TipoRemuneracao tipoRemuneracao, Pessoa responsavelCadastro, TipoDeficiencia tipoDeficiencia, IndicadorAlvara indicadorAlvara, Date admissao, Date demissao, Date afastamento, String motivoAfastamento, String observacao, int cargaHoraria, float salario, String funcao, String carteira, String serie, int ctps, int anoChegada, boolean alvara, boolean empregadoFiliado, String pis) {
        this.id = id;
        this.emissao = emissao;
        this.nacionalidade = nacionalidade;
        this.raca = raca;
        this.empresa = empresa;
        this.escolaridade = escolaridade;
        this.sisPessoa = sisPessoa;
        this.classificacaoEconomica = classificacaoEconomica;
        this.profissao = profissao;
        this.tipoRemuneracao = tipoRemuneracao;
        this.responsavelCadastro = responsavelCadastro;
        this.tipoDeficiencia = tipoDeficiencia;
        this.indicadorAlvara = indicadorAlvara;
        this.admissao = admissao;
        this.afastamento = afastamento;
        this.motivoAfastamento = motivoAfastamento;
        this.observacao = observacao;
        this.cargaHoraria = cargaHoraria;
        this.salario = salario;
        this.funcao = funcao;
        this.carteira = carteira;
        this.serie = serie;
        this.ctps = ctps;
        this.anoChegada = anoChegada;
        this.alvara = alvara;
        this.empregadoFiliado = empregadoFiliado;
        this.pis = pis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getEmissao() {
        return emissao;
    }

    public void setEmissao(Date emissao) {
        this.emissao = emissao;
    }

    public String getEmissaoString() {
        return DataHoje.converteData(emissao);
    }

    public void setEmissaoString(String emissaoString) {
        this.emissao = DataHoje.converte(emissaoString);
    }

    public Nacionalidade getNacionalidade() {
        return nacionalidade;
    }

    public void setNacionalidade(Nacionalidade nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

    public Raca getRaca() {
        return raca;
    }

    public void setRaca(Raca raca) {
        this.raca = raca;
    }

    public Juridica getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Juridica empresa) {
        this.empresa = empresa;
    }

    public Escolaridade getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(Escolaridade escolaridade) {
        this.escolaridade = escolaridade;
    }

    public SisPessoa getSisPessoa() {
        return sisPessoa;
    }

    public void setSisPessoa(SisPessoa sisPessoa) {
        this.sisPessoa = sisPessoa;
    }

    public ClassificacaoEconomica getClassificacaoEconomica() {
        return classificacaoEconomica;
    }

    public void setClassificacaoEconomica(ClassificacaoEconomica classificacaoEconomica) {
        this.classificacaoEconomica = classificacaoEconomica;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }

    public TipoRemuneracao getTipoRemuneracao() {
        return tipoRemuneracao;
    }

    public void setTipoRemuneracao(TipoRemuneracao tipoRemuneracao) {
        this.tipoRemuneracao = tipoRemuneracao;
    }

    public Pessoa getResponsavelCadastro() {
        return responsavelCadastro;
    }

    public void setResponsavelCadastro(Pessoa responsavelCadastro) {
        this.responsavelCadastro = responsavelCadastro;
    }

    public TipoDeficiencia getTipoDeficiencia() {
        return tipoDeficiencia;
    }

    public void setTipoDeficiencia(TipoDeficiencia tipoDeficiencia) {
        this.tipoDeficiencia = tipoDeficiencia;
    }

    public IndicadorAlvara getIndicadorAlvara() {
        return indicadorAlvara;
    }

    public void setIndicadorAlvara(IndicadorAlvara indicadorAlvara) {
        this.indicadorAlvara = indicadorAlvara;
    }

    public Date getAdmissao() {
        return admissao;
    }

    public void setAdmissao(Date admissao) {
        this.admissao = admissao;
    }

    public String getAdmissaoString() {
        return DataHoje.converteData(admissao);
    }

    public void setAdmissaoString(String admissaoString) {
        this.admissao = DataHoje.converte(admissaoString);
    }

    public Date getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Date afastamento) {
        this.afastamento = afastamento;
    }

    public String getMotivoAfastamento() {
        return motivoAfastamento;
    }

    public void setMotivoAfastamento(String motivoAfastamento) {
        this.motivoAfastamento = motivoAfastamento;
    }

    public String getAfastamentoString() {
        return DataHoje.converteData(afastamento);
    }

    public void setAfastamentoString(String afastamentoString) {
        this.afastamento = DataHoje.converte(afastamentoString);
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getCargaHorariaString() {
        return Integer.toString(cargaHoraria);
    }

    public void setCargaHorariaString(String cargaHorariaString) {
        if(!cargaHorariaString.isEmpty()) {
            this.cargaHoraria = Integer.parseInt(cargaHorariaString);            
        }
    }

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
    }

    public String getSalarioString() {
        return Moeda.converteR$Float(salario);
    }

    public void setSalarioString(String salarioString) {
        if(!salarioString.isEmpty()) {
            this.salario = Moeda.converteUS$(salarioString);
            if(this.salario < 0) {
                this.salario = 0;
            }
        }
    }

    public String getFuncao() {
        return funcao;
    }

    public void setFuncao(String funcao) {
        this.funcao = funcao;
    }

    public String getCarteira() {
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public int getCtps() {
        return ctps;
    }

    public void setCtps(int ctps) {
        this.ctps = ctps;
    }

    public int getAnoChegada() {
        return anoChegada;
    }

    public void setAnoChegada(int anoChegada) {
        this.anoChegada = anoChegada;
    }

    public boolean isAlvara() {
        return alvara;
    }

    public void setAlvara(boolean alvara) {
        this.alvara = alvara;
    }

    public boolean isEmpregadoFiliado() {
        return empregadoFiliado;
    }

    public void setEmpregadoFiliado(boolean empregadoFiliado) {
        this.empregadoFiliado = empregadoFiliado;
    }

    public Date getDemissao() {
        return demissao;
    }

    public void setDemissao(Date demissao) {
        this.demissao = demissao;
    }

    public String getDemissaoString() {
        return DataHoje.converteData(demissao);
    }

    public void setDemissaoString(String demissaoString) {
        this.demissao = DataHoje.converte(demissaoString);
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

}
