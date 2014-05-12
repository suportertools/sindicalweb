package br.com.rtools.sistema;

import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.DataHoje;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "SIS_CONFIGURACAO")
@NamedQuery(name = "Configuracao.pesquisaID", query = "SELECT C FROM Configuracao c WHERE C.id = :pid")
public class Configuracao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_NOME_CLIENTE", length = 300)
    private String nomeCliente;
    @Column(name = "DS_PERSISTENCE", length = 200)
    private String persistence;
    @Column(name = "DS_CAMINHO_SISTEMA", length = 200)
    private String caminhoSistema;
    @Column(name = "DS_IDENTIFICA", length = 100, unique = true)
    private String identifica;
    @JoinColumn(name = "ID_JURIDICA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Juridica juridica;
    @Column(name = "NR_ACESSO")
    private int acessos;
    @Column(name = "DT_CADASTRO")
    @Temporal(TemporalType.DATE)
    private Date dtCadastro;
    @Column(name = "IS_ATIVO")
    private boolean ativo;
    @Column(name = "DS_HOST", length = 300)
    private String host;
    @Column(name = "DS_SENHA", length = 300)
    private String senha;

    public Configuracao() {
        this.id = -1;
        this.nomeCliente = "";
        this.persistence = "";
        this.caminhoSistema = "";
        this.identifica = "";
        this.juridica = new Juridica();
        this.acessos = 0;
        this.dtCadastro = DataHoje.dataHoje();
        this.ativo = true;
        this.host = "";
        this.senha = "";
    }

    public Configuracao(int id, String nomeCliente, String persistence, String caminhoSistema, String identifica, Juridica juridica, int acessos, String cadastro, boolean ativo, String host, String senha) {
        this.id = id;
        this.nomeCliente = nomeCliente;
        this.persistence = persistence;
        this.caminhoSistema = caminhoSistema;
        this.identifica = identifica;
        this.juridica = juridica;
        this.acessos = acessos;
        this.dtCadastro = DataHoje.converte(cadastro);
        this.ativo = ativo;
        this.host = "";
        this.senha = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getPersistence() {
        return persistence;
    }

    public void setPersistence(String persistence) {
        this.persistence = persistence;
    }

    public String getCaminhoSistema() {
        return caminhoSistema;
    }

    public void setCaminhoSistema(String caminhoSistema) {
        this.caminhoSistema = caminhoSistema;
    }

    public String getIdentifica() {
        return identifica;
    }

    public void setIdentifica(String identifica) {
        this.identifica = identifica;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }

    public int getAcessos() {
        return acessos;
    }

    public void setAcessos(int acessos) {
        this.acessos = acessos;
    }

    public Date getDtCadastro() {
        return dtCadastro;
    }

    public void setDtCadastro(Date cadastro) {
        this.dtCadastro = cadastro;
    }

    public String getCadastro() {
        return DataHoje.converteData(dtCadastro);
    }

    public void setCadastro(String cadastro) {
        this.dtCadastro = DataHoje.converte(cadastro);
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Override
    public String toString() {
        return "Configuracao{" + "id=" + id + ", nomeCliente=" + nomeCliente + ", persistence=" + persistence + ", caminhoSistema=" + caminhoSistema + ", identifica=" + identifica + ", juridica=" + juridica + ", acessos=" + acessos + ", dtCadastro=" + dtCadastro + ", ativo=" + ativo + ", host=" + host + ", senha=" + senha + '}';
    }

}
