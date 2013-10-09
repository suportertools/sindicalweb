package br.com.rtools.pessoa;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "PES_JURIDICA_RECEITA")
@NamedQuery(name = "JuridicaReceita.pesquisaID", query = "select jr from JuridicaReceita jr where jr.id = :pid")
public class JuridicaReceita implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_PESQUISA")
    private Date dtPesquisa;
    @Column(name = "DS_DOCUMENTO", length = 30)
    private String documento;
    @Column(name = "DS_NOME", length = 300)
    private String nome;
    @Column(name = "DS_FANTASIA", length = 300)
    private String fantasia;
    @Column(name = "DS_CEP", length = 15)
    private String cep;
    @Column(name = "DS_COMPLEMENTO", length = 35)
    private String complemento;
    @Column(name = "DS_DESCRICAO_END", length = 300)
    private String descricaoEndereco;
    @Column(name = "DS_BAIRRO", length = 300)
    private String bairro;
    @Column(name = "DS_NUMERO", length = 35)
    private String numero;
    @Column(name = "DS_CNAE", length = 400)
    private String cnae;
    @Column(name = "DS_STATUS", length = 30)
    private String status;

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
    }

    public JuridicaReceita(int id, Pessoa pessoa, Date dtPesquisa, String documento, String nome, String fantasia, String cep, String descricaoEndereco, String bairro, String complemento, String numero, String cnae, String status) {
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
}
