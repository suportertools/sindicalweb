package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "pes_juridica_receita")
@NamedQuery(name = "JuridicaReceita.pesquisaID", query = "select jr from JuridicaReceita jr where jr.id = :pid")
public class JuridicaReceita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_pesquisa")
    private Date dtPesquisa;
    @Column(name = "ds_documento", length = 30)
    private String documento;
    @Column(name = "ds_nome", length = 300)
    private String nome;
    @Column(name = "ds_fantasia", length = 300)
    private String fantasia;
    @Column(name = "ds_cep", length = 15)
    private String cep;
    @Column(name = "ds_complemento", length = 100)
    private String complemento;
    @Column(name = "ds_descricao_end", length = 300)
    private String descricaoEndereco;
    @Column(name = "ds_bairro", length = 300)
    private String bairro;
    @Column(name = "ds_numero", length = 35)
    private String numero;
    @Column(name = "ds_cnae", length = 400)
    private String cnae;
    @Column(name = "ds_status", length = 30)
    private String status;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_abertura")
    private Date dtAbertura;
    @Column(name = "ds_cnae_segundario", length = 1000)
    private String cnaeSegundario;
    @Column(name = "ds_cidade", length = 100)
    private String cidade;
    @Column(name = "ds_uf", length = 2)
    private String uf;
    @Column(name = "ds_email", length = 1000)
    private String email;
    @Column(name = "ds_telefone", length = 500)
    private String telefone;
        
    public JuridicaReceita() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.setDataPesquisa(DataHoje.data());
        this.documento = "";
        this.nome = "";
        this.fantasia = "";
        this.cep = "";
        this.descricaoEndereco = "";
        this.bairro = "";
        this.complemento = "";
        this.numero = "";
        this.cnae = "";
        this.status = "";
        this.dtAbertura = null;
        this.cnaeSegundario = "";
        this.cidade = "";
        this.uf = "";
    }

    public JuridicaReceita(int id, Pessoa pessoa, Date dtPesquisa, String documento, String nome, String fantasia, String cep, String descricaoEndereco, String bairro, String complemento, String numero, String cnae, String status, Date dtAbertura, String cnaeSegundario, String cidade, String uf) {
        this.id = id;
        this.pessoa = pessoa;
        this.dtPesquisa = dtPesquisa;
        this.documento = documento;
        this.nome = nome;
        this.fantasia = fantasia;
        this.cep = cep;
        this.descricaoEndereco = descricaoEndereco;
        this.bairro = bairro;
        this.complemento = complemento;
        this.numero = numero;
        this.cnae = cnae;
        this.status = status;
        this.dtAbertura = dtAbertura;
        this.cnaeSegundario = cnaeSegundario;
        this.cidade = cidade;
        this.uf = uf;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDtPesquisa() {
        return dtPesquisa;
    }

    public void setDtPesquisa(Date dtPesquisa) {
        this.dtPesquisa = dtPesquisa;
    }

    public String getDataPesquisa() {
        return DataHoje.converteData(dtPesquisa);
    }

    public void setDataPesquisa(String dataPesquisa) {
        this.dtPesquisa = DataHoje.converte(dataPesquisa);
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getFantasia() {
        return fantasia;
    }

    public void setFantasia(String fantasia) {
        this.fantasia = fantasia;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCnae() {
        return cnae;
    }

    public void setCnae(String cnae) {
        this.cnae = cnae;
    }

    public String getDescricaoEndereco() {
        return descricaoEndereco;
    }

    public void setDescricaoEndereco(String descricaoEndereco) {
        this.descricaoEndereco = descricaoEndereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDtAbertura() {
        return dtAbertura;
    }

    public void setDtAbertura(Date dtAbertura) {
        this.dtAbertura = dtAbertura;
    }

    public String getCnaeSegundario() {
        return cnaeSegundario;
    }

    public void setCnaeSegundario(String cnaeSegundario) {
        this.cnaeSegundario = cnaeSegundario;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
