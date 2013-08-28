package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="PES_PESSOA_EMPRESA")
@NamedQuery(name="PessoaEmpresa.pesquisaID", query="select pe from PessoaEmpresa pe where pe.id = :pid")
public class PessoaEmpresa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_FISICA", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private Fisica fisica;
    @JoinColumn(name="ID_JURIDICA", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private Juridica juridica;
    @JoinColumn(name="ID_FUNCAO", referencedColumnName="ID",  nullable=true)
    @ManyToOne (fetch=FetchType.EAGER)
    private Profissao funcao;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_ADMISSAO")
    private Date dtAdmissao;
    @Temporal(TemporalType.DATE)
    @Column(name="DT_DEMISSAO")
    private Date dtDemissao;
    @Column(name="DS_SETOR", length=30, nullable=false)
    private String setor;
    @Column(name="AVISO_TRABALHADO", nullable=true)
    private boolean avisoTrabalhado;
    
    public PessoaEmpresa() {
        this.id = -1;
        this.fisica = new Fisica();
        this.juridica = new Juridica();
        this.funcao = new Profissao();
        setAdmissao("");
        setDemissao("");
        this.setor = "";
        this.avisoTrabalhado = true;
    }
    
    public PessoaEmpresa(int id, Fisica fisica, Juridica juridica, Profissao funcao, String admissao, String demissao, String setor, boolean avisoTrabalhado) {
        this.id = id;
        this.fisica = fisica;
        this.juridica = juridica;
        this.funcao = funcao;
        setAdmissao(admissao);
        setDemissao(demissao);
        this.setor = setor;
        this.avisoTrabalhado = avisoTrabalhado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public Profissao getFuncao() {
        return funcao;
    }

    public void setFuncao(Profissao funcao) {
        this.funcao = funcao;
    }

    public Date getDtAdmissao() {
        return dtAdmissao;
    }

    public void setDtAdmissao(Date dtAdmissao) {
        this.dtAdmissao = dtAdmissao;
    }

    public String getAdmissao() {
        if (dtAdmissao != null)
            return DataHoje.converteData(dtAdmissao);
        else
            return "";
    }

    public void setAdmissao(String admissao) {
        if (!(admissao.isEmpty()))
            this.dtAdmissao = DataHoje.converte(admissao);
    }

    public Date getDtDemissao() {
        return dtDemissao;
    }

    public void setDtDemissao(Date dtDemissao) {
        this.dtDemissao = dtDemissao;
    }

    public String getDemissao() {
        if (dtDemissao != null)
            return DataHoje.converteData(dtDemissao);
        else
            return "";
    }

    public void setDemissao(String demissao) {
        if (!(demissao.isEmpty()))
            this.dtDemissao = DataHoje.converte(demissao);
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public boolean isAvisoTrabalhado() {
        return avisoTrabalhado;
    }

    public void setAvisoTrabalhado(boolean avisoTrabalhado) {
        this.avisoTrabalhado = avisoTrabalhado;
    }
}