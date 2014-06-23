package br.com.rtools.arrecadacao;

import br.com.rtools.financeiro.TipoRemuneracao;
import br.com.rtools.pessoa.ClassificacaoEconomica;
import br.com.rtools.pessoa.Escolaridade;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Nacionalidade;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Profissao;
import br.com.rtools.pessoa.Raca;
import br.com.rtools.pessoa.TipoDeficiencia;
import br.com.rtools.sistema.SisPessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @ManyToOne(fetch = FetchType.EAGER)
    private Nacionalidade nacionalidade;
    @JoinColumn(name = "ID_RACA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Raca raca;
    @JoinColumn(name = "ID_EMPRESA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica empresa;
    @JoinColumn(name = "ID_ESCOLARIDADE", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Escolaridade escolaridade;
    @JoinColumn(name = "ID_SIS_PESSOA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private SisPessoa sisPessoa;
    @JoinColumn(name = "ID_CLASSIFICACAO_ECONOMICA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private ClassificacaoEconomica classificacaoEconomica;
    @JoinColumn(name = "ID_PROFISSAO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Profissao profissao;
    @JoinColumn(name = "ID_TIPO_REMUNERACAO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoRemuneracao tipoRemuneracao;
    @JoinColumn(name = "ID_RESPONSAVEL_CADASTRO", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa responsavelCadastro;
    @JoinColumn(name = "ID_TIPO_DEFICIENCIA", referencedColumnName = "ID")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoDeficiencia tipoDeficiencia;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ADMISSAO")
    private Date admissao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_AFASTAMENTO")
    private Date afastamento;
    @Column(name = "DS_MOTIVO_AFASTAMENTO", length = 500)
    private String motivoAfastamento;
    @Column(name = "IS_ALVARA", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private boolean alvara;
    @Column(name = "DS_OBSERVACAO", length = 500)
    private String observacao;
    @Column(name = "NR_CARGA_HORARIA", columnDefinition = "INTEGER DEFAULT 0")
    private int cargaHoraria;
    @Column(name = "NR_SALARIO", columnDefinition = "DOUBLE PRECISION DEFAULT 0")
    private float salario;
    @Column(name = "DS_FUNCAO", length = 255)
    private String funcao;
    @Column(name = "DS_CARTEIRA")
    private String carteira;
    @Column(name = "DS_SERIE")
    private String serie;
    @Column(name = "NR_CTPS", columnDefinition = "INTEGER DEFAULT 0")
    private int ctps;

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
        this.admissao = new Date();
        this.afastamento = new Date();
        this.motivoAfastamento = "";
        this.alvara = false;
        this.observacao = "";
        this.cargaHoraria = 0;
        this.salario = 0;
        this.funcao = "";
        this.carteira = "";
        this.serie = "";
        this.ctps = 0;
    }

    public Rais(int id, Date emissao, Nacionalidade nacionalidade, Raca raca, Juridica empresa, Escolaridade escolaridade, SisPessoa sisPessoa, ClassificacaoEconomica classificacaoEconomica, Profissao profissao, TipoRemuneracao tipoRemuneracao, Pessoa responsavelCadastro, TipoDeficiencia tipoDeficiencia, Date admissao, Date afastamento, String motivoAfastamento, boolean alvara, String observacao, int cargaHoraria, float salario, String funcao, String carteira, String serie, int ctps) {
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
        this.admissao = admissao;
        this.afastamento = afastamento;
        this.motivoAfastamento = motivoAfastamento;
        this.alvara = alvara;
        this.observacao = observacao;
        this.cargaHoraria = cargaHoraria;
        this.salario = salario;
        this.funcao = funcao;
        this.carteira = carteira;
        this.serie = serie;
        this.ctps = ctps;
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

    public void setEmissao(String emissaoString) {
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

    public Date getAdmissao() {
        return admissao;
    }

    public void setAdmissao(Date admissao) {
        this.admissao = admissao;
    }

    public Date getAfastamento() {
        return afastamento;
    }

    public void setAfastamento(Date afastamento) {
        this.afastamento = afastamento;
    }

    public String getAdmissaoString() {
        return DataHoje.converteData(admissao);
    }

    public void setAdmissaoString(String admissaoString) {
        this.admissao = DataHoje.converte(admissaoString);
    }

    public String getAfastamentoString() {
        return DataHoje.converteData(afastamento);
    }

    public void setAfastamentoString(String afastamentoString) {
        this.afastamento = DataHoje.converte(afastamentoString);
    }

    public String getMotivoAfastamento() {
        return motivoAfastamento;
    }

    public void setMotivoAfastamento(String motivoAfastamento) {
        this.motivoAfastamento = motivoAfastamento;
    }

    public boolean isAlvara() {
        return alvara;
    }

    public void setAlvara(boolean alvara) {
        this.alvara = alvara;
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

    public float getSalario() {
        return salario;
    }

    public void setSalario(float salario) {
        this.salario = salario;
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
}
