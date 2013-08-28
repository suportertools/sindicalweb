package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="PES_PESSOA")
@NamedQuery(name="Pessoa.pesquisaID", query="select pes from Pessoa pes where pes.id=:pid")
public class Pessoa implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_NOME", length=150,nullable=false)
    private String nome;
    @JoinColumn(name="ID_TIPO_DOCUMENTO", referencedColumnName="ID", nullable=false)
    @OneToOne(fetch=FetchType.EAGER)
    private TipoDocumento tipoDocumento;
    @Column(name="DS_OBS", length=500,nullable=true)
    private String obs;
    @Column(name="DS_SITE", length=50,nullable=true)
    private String site;
    @Column(name="DS_TELEFONE1", length=20,nullable=true)
    private String telefone1;
    @Column(name="DS_TELEFONE2", length=20)
    private String telefone2;
    @Column(name="DS_TELEFONE3", length=20)
    private String telefone3;
    @Column(name="DS_EMAIL1", length=50,nullable=true)
    private String email1;
    @Column(name="DS_EMAIL2", length=50)
    private String email2;
    @Column(name="DS_EMAIL3", length=50)
    private String email3;
    @Column(name="DS_DOCUMENTO", length=30,nullable=false)
    private String documento;
    @Column(name="DS_LOGIN", length=50,nullable=true)
    private String login;
    @Column(name="DS_SENHA", length=50,nullable=true)
    private String senha;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_CRIACAO")
    private Date dtCriacao;    


    public Pessoa(){
         this.id = -1;
         this.nome = "";
         this.tipoDocumento = new TipoDocumento();
         this.obs = "";
         this.site = "";
         setCriacao(DataHoje.data());
         this.telefone1 = "";
         this.telefone2 = "";
         this.telefone3 = "";
         this.email1 = "";
         this.email2 = "";
         this.email3 = "";
         this.documento = "";
         this.login = "";
         this.senha = "";
    }

    public Pessoa(int id,String nome, TipoDocumento tipoDocumento,String obs, String site, String criacao, 
            String telefone1, String telefone2, String telefone3, String email1, String email2, String email3, String documento,String login, String senha)
    {
         this.id = id;
         this.nome = nome;
         this.tipoDocumento = tipoDocumento;
         this.obs = obs;
         this.site = site;
         setCriacao(criacao);
         this.telefone1 = telefone1;
         this.telefone2 = telefone2;
         this.telefone3 = telefone3;
         this.email1 = email1;
         this.email2 = email2;
         this.email3 = email3;
         this.documento = documento;
         this.login = login;
         this.senha = senha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getTelefone1() {
        return telefone1;
    }

    public void setTelefone1(String telefone1) {
        this.telefone1 = telefone1;
    }

    public String getTelefone2() {
        return telefone2;
    }

    public void setTelefone2(String telefone2) {
        this.telefone2 = telefone2;
    }

    public String getTelefone3() {
        return telefone3;
    }

    public void setTelefone3(String telefone3) {
        this.telefone3 = telefone3;
    }

    public String getEmail1() {
        return email1;
    }

    public void setEmail1(String email1) {
        this.email1 = email1;
    }

    public String getEmail2() {
        return email2;
    }

    public void setEmail2(String email2) {
        this.email2 = email2;
    }

    public String getEmail3() {
        return email3;
    }

    public void setEmail3(String email3) {
        this.email3 = email3;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public String getCriacao() {
        return DataHoje.converteData(dtCriacao);
    }

    public void setCriacao(String criacao) {
        this.dtCriacao = DataHoje.converte(criacao);
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }


}