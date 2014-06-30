package br.com.rtools.sistema;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.utilitarios.DataHoje;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "SIS_PESSOA")
@NamedQuery(name = "SisPessoa.pesquisaID", query = "SELECT SISP FROM SisPessoa SISP WHERE SISP.id = :pid")
public class SisPessoa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_NOME", length = 200, nullable = false)
    private String nome;
    @Column(name = "DS_DOCUMENTO", length = 20, nullable = false)
    private String documento;
    @Column(name = "DS_RG", length = 12, nullable = false)
    private String rg;
    @Column(name = "DS_TELEFONE", length = 20, nullable = true)
    private String telefone;
    @Column(name = "DS_CELULAR", length = 20, nullable = true)
    private String celular;
    @Column(name = "DS_EMAIL1", length = 20, nullable = true)
    private String email1;
    @Column(name = "DS_EMAIL2", length = 20, nullable = true)
    private String email2;
    @Column(name = "DS_OBS", length = 300, nullable = true)
    private String observacao;
    @Column(name = "DS_COMPLEMENTO", length = 150, nullable = true)
    private String complemento;
    @Column(name = "DS_NUMERO", length = 30, nullable = true)
    private String numero;
    @Column(name = "DS_SEXO", length = 1, nullable = true)
    private String sexo;
    @JoinColumn(name = "ID_TIPO_DOCUMENTO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private TipoDocumento tipoDocumento;
    @JoinColumn(name = "ID_ENDERECO", referencedColumnName = "ID", nullable = true)
    @ManyToOne
    private Endereco endereco;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CRIACAO")
    private Date dtCriacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_NASCIMENTO")
    private Date dtNascimento;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_IMPORTACAO")
    private Date dtImportacao;

    public SisPessoa() {
        this.id = -1;
        this.nome = "";
        this.documento = "";
        this.rg = "";
        this.telefone = "";
        this.celular = "";
        this.email1 = "";
        this.email2 = "";
        this.observacao = "";
        this.complemento = "";
        this.numero = "";
        this.sexo = "";
        this.tipoDocumento = new TipoDocumento();
        this.endereco = new Endereco();
        this.dtCriacao = DataHoje.dataHoje();
        this.dtNascimento = DataHoje.dataHoje();
        this.dtImportacao = new Date();
    }

    public SisPessoa(int id, String nome, String documento, String rg, String telefone, String celular, String email1, String email2, String observacao, String complemento, String numero, String sexo, TipoDocumento tipoDocumento, Endereco endereco, String criacao, String nascimento) {
        this.id = id;
        this.nome = nome;
        this.documento = documento;
        this.rg = rg;
        this.telefone = telefone;
        this.celular = celular;
        this.email1 = email1;
        this.email2 = email2;
        this.observacao = observacao;
        this.complemento = complemento;
        this.numero = numero;
        this.sexo = sexo;
        this.tipoDocumento = tipoDocumento;
        this.endereco = endereco;
        this.dtCriacao = DataHoje.converte(criacao);
        this.dtNascimento = DataHoje.converte(nascimento);
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

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
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

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
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

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Date getDtCriacao() {
        return dtCriacao;
    }

    public void setDtCriacao(Date dtCriacao) {
        this.dtCriacao = dtCriacao;
    }

    public Date getDtNascimento() {
        return dtNascimento;
    }

    public void setDtNascimento(Date dtNascimento) {
        this.dtNascimento = dtNascimento;
    }

    public String getCriacao() {
        return DataHoje.converteData(dtCriacao);
    }

    public void setCriacao(String criacao) {
        this.dtCriacao = DataHoje.converte(criacao);
    }

    public String getNascimento() {
        return DataHoje.converteData(dtNascimento);
    }

    public void setNascimento(String nascimento) {
        this.dtNascimento = DataHoje.converte(nascimento);
    }

    @Override
    public String toString() {
        return "SisPessoa{" + "id=" + id + ", nome=" + nome + ", documento=" + documento + ", rg=" + rg + ", telefone=" + telefone + ", celular=" + celular + ", email1=" + email1 + ", email2=" + email2 + ", observacao=" + observacao + ", complemento=" + complemento + ", numero=" + numero + ", sexo=" + sexo + ", tipoDocumento=" + tipoDocumento + ", endereco=" + endereco + ", dtCriacao=" + dtCriacao + ", dtNascimento=" + dtNascimento + '}';
    }

    public void selecionaDataNascimento(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtNascimento = DataHoje.converte(format.format(event.getObject()));
    }

    public Date getDtImportacao() {
        return dtImportacao;
    }

    public void setDtImportacao(Date dtImportacao) {
        this.dtImportacao = dtImportacao;
    }

}
