package br.com.rtools.arrecadacao;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.io.Serializable;

@Entity
@Table(name = "arr_oposicao_pessoa")
@NamedQuery(name = "OposicaoPessoa.pesquisaID", query = "select op from OposicaoPessoa op where op.id=:pid")
public class OposicaoPessoa implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_cadastro")
    private Date dataCadastro;
    @Column(name = "ds_nome", length = 200, nullable = false)
    private String nome;
    @Column(name = "ds_sexo", length = 1)
    private String sexo;
    @Column(name = "ds_cpf", length = 15)
    private String cpf;
    @Column(name = "ds_rg", length = 12)
    private String rg;

    public OposicaoPessoa() {
        this.id = -1;
        setDataCadastroString(DataHoje.data());
        this.nome = "";
        this.cpf = "";
        this.rg = "";
    }

    public OposicaoPessoa(int id, String dataCadastro, String nome, String cpf, String rg, String observacao) {
        this.id = id;
        setDataCadastroString(dataCadastro);
        this.nome = nome;
        this.cpf = cpf;
        this.rg = rg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getDataCadastroString() {
        return DataHoje.converteData(this.dataCadastro);
    }

    public void setDataCadastroString(String dataCadastro) {
        this.dataCadastro = DataHoje.converte(dataCadastro);
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
