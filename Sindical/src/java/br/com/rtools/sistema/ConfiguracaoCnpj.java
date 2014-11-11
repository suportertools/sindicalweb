package br.com.rtools.sistema;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conf_pesquisa_cnpj")
public class ConfiguracaoCnpj implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "dt_cadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @Column(name = "dt_atualizacao")
    @Temporal(TemporalType.DATE)
    private Date dataAtualizacao;
    @Column(name = "ds_email", length = 200)
    private String email;
    @Column(name = "ds_senha", length = 10)
    private String senha;
    @Column(name = "nr_dias ")
    private int dias;

    public ConfiguracaoCnpj() {
        this.id = -1;
        this.dataCadastro = DataHoje.dataHoje();
        this.dataAtualizacao = null;
        this.email = "";
        this.senha = "";
        this.dias = 0;
    }

    public ConfiguracaoCnpj(int id, Date dataCadastro, Date dataAtualizacao, String email, String senha, int dias) {
        this.id = id;
        this.dataCadastro = dataCadastro;
        this.dataAtualizacao = dataAtualizacao;
        this.email = email;
        this.senha = senha;
        this.dias = dias;
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

    public Date getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(Date dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getDataCadastroString() {
        return DataHoje.converteData(dataCadastro);
    }

    public void setDataCadastroString(String dataCadastroString) {
        this.dataCadastro = DataHoje.converte(dataCadastroString);
    }

    public String getDataAtualizacaoString() {
        return DataHoje.converteData(dataAtualizacao);
    }

    public void setDataAtualizacaoString(String dataAtualizacaoString) {
        this.dataAtualizacao = DataHoje.converte(dataAtualizacaoString);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public int getDiasString() {
        return dias;
    }

    public void setDiasString(String dias) {
        this.dias = Integer.parseInt(dias);
    }

}
