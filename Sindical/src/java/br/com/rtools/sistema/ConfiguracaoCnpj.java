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
    private Integer id;
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
    private Integer dias;
    @Column(name = "is_local", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean local;
    @Column(name = "is_web", columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean web;

    public ConfiguracaoCnpj() {
        this.id = -1;
        this.dataCadastro = DataHoje.dataHoje();
        this.dataAtualizacao = null;
        this.email = "";
        this.senha = "";
        this.dias = 0;
        this.local = false;
        this.web = false;
    }

    public ConfiguracaoCnpj(Integer id, Date dataCadastro, Date dataAtualizacao, String email, String senha, Integer dias, Boolean local, Boolean web) {
        this.id = id;
        this.dataCadastro = dataCadastro;
        this.dataAtualizacao = dataAtualizacao;
        this.email = email;
        this.senha = senha;
        this.dias = dias;
        this.local = local;
        this.web = web;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public Integer getDiasString() {
        return dias;
    }

    public void setDiasString(String dias) {
        this.dias = Integer.parseInt(dias);
    }

    public Boolean getLocal() {
        return local;
    }

    public void setLocal(Boolean local) {
        this.local = local;
    }

    public Boolean getWeb() {
        return web;
    }

    public void setWeb(Boolean web) {
        this.web = web;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ConfiguracaoCnpj other = (ConfiguracaoCnpj) obj;
        return true;
    }

    @Override
    public String toString() {
        return "ConfiguracaoCnpj{" + "id=" + id + ", dataCadastro=" + dataCadastro + ", dataAtualizacao=" + dataAtualizacao + ", email=" + email + ", senha=" + senha + ", dias=" + dias + ", local=" + local + ", web=" + web + '}';
    }

}
