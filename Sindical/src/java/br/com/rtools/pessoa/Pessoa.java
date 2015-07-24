package br.com.rtools.pessoa;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.pessoa.db.PessoaDB;
import br.com.rtools.pessoa.db.PessoaDBToplink;
import br.com.rtools.pessoa.db.PessoaEnderecoDBToplink;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "pes_pessoa")
@NamedQuery(name = "Pessoa.pesquisaID", query = "select pes from Pessoa pes where pes.id=:pid")
public class Pessoa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_nome", length = 150, nullable = false)
    private String nome;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private TipoDocumento tipoDocumento;
    @Column(name = "ds_obs", length = 500, nullable = true)
    private String obs;
    @Column(name = "ds_site", length = 50, nullable = true)
    private String site;
    @Column(name = "ds_telefone1", length = 20, nullable = true)
    private String telefone1;
    @Column(name = "ds_telefone2", length = 20)
    private String telefone2;
    @Column(name = "ds_telefone3", length = 20)
    private String telefone3;
    @Column(name = "ds_email1", length = 50, nullable = true)
    private String email1;
    @Column(name = "ds_email2", length = 50)
    private String email2;
    @Column(name = "ds_email3", length = 50)
    private String email3;
    @Column(name = "ds_documento", length = 30, nullable = false)
    private String documento;
    @Column(name = "ds_login", length = 50, nullable = true)
    private String login;
    @Column(name = "ds_senha", length = 50, nullable = true)
    private String senha;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_criacao")
    private Date dtCriacao;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "dt_atualizacao")
    private Date dtAtualizacao;

    public Pessoa() {
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
        this.dtAtualizacao = null;
    }

    public Pessoa(int id, String nome, TipoDocumento tipoDocumento, String obs, String site, String criacao,
            String telefone1, String telefone2, String telefone3, String email1, String email2, String email3, String documento, String login, String senha) {
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
        this.dtAtualizacao = null;
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

    public Date getDtAtualizacao() {
        return dtAtualizacao;
    }

    public void setDtAtualizacao(Date dtAtualizacao) {
        this.dtAtualizacao = dtAtualizacao;
    }

    public String getAtualizacao() {
        if(dtAtualizacao != null) {
            return DataHoje.livre(dtAtualizacao, "dd/MM/yyyy") + " às " + DataHoje.livre(dtAtualizacao, "HH:mm") + " hr(s)";
        }
        return null;
    }

    public void setAtualizacao(String dtAtualizacao) {
        this.dtAtualizacao = DataHoje.converte(dtAtualizacao);
    }

    public void selecionaDataCriacao(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dtCriacao = DataHoje.converte(format.format(event.getObject()));
    }

    public PessoaEndereco getPessoaEndereco() {
        int idTipoEndereco = 1;
        if (tipoDocumento.getId() == 2) {
            idTipoEndereco = 2;
        }
        PessoaEndereco pessoaEndereco = new PessoaEnderecoDBToplink().pesquisaEndPorPessoaTipo(this.id, idTipoEndereco);
        if (pessoaEndereco == null) {
            pessoaEndereco = new PessoaEnderecoDBToplink().pesquisaEndPorPessoaTipo(this.id, 2);
        }
        return pessoaEndereco;
    }

    public PessoaComplemento getPessoaComplemento() {
        PessoaComplemento pessoaComplemento = new PessoaComplemento();
        pessoaComplemento.setPessoa(null);
        if (this.id != -1) {
            PessoaDB pessoaDB = new PessoaDBToplink();
            pessoaComplemento = pessoaDB.pesquisaPessoaComplementoPorPessoa(this.id);
            pessoaComplemento.setPessoa(null);
        }
        return pessoaComplemento;
    }

    public Juridica getJuridica() {
        Juridica juridica = new Juridica();
        juridica.setPessoa(null);
        if (this.id != -1) {
            JuridicaDB juridicaDB = new JuridicaDBToplink();
            juridica = juridicaDB.pesquisaJuridicaPorPessoa(this.id);
            if (juridica.getId() != -1) {
                juridica = (Juridica) new Dao().rebind(juridica);
                juridica.setPessoa(null);
            }

        }
        return juridica;
    }

    public Fisica getFisica() {
        Fisica fisica = new Fisica();
        fisica.setPessoa(null);
        if (this.id != -1) {
            FisicaDB fisicaDB = new FisicaDBToplink();
            fisica = fisicaDB.pesquisaFisicaPorPessoa(this.id);
            if (fisica.getId() != -1) {
                fisica = (Fisica) new Dao().rebind(fisica);
            }
            fisica.setPessoa(null);
        }
        return fisica;
    }

    public Socios getSocios() {
        Socios socios = new Socios();
        if (this.id != -1) {
            SociosDB sociosDB = new SociosDBToplink();
            socios = sociosDB.pesquisaSocioPorPessoaAtivo(this.id);
            socios.getServicoPessoa().setPessoa(null);
        }
        return socios;
    }

    public Integer getDiaVencimentoOriginal() {
        if (this.id != -1) {
            PessoaDB db = new PessoaDBToplink();
            PessoaComplemento pc = db.pesquisaPessoaComplementoPorPessoa(this.id);
            if (pc.getId() == -1) {
                Registro registro = (Registro) new Dao().find(new Registro(), 1);
                return registro.getFinDiaVencimentoCobranca();
            } else {
                return pc.getNrDiaVencimento();
            }
        }
        return null;
    }

    @Override
    public int hashCode() {
        int hash = 3;
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
        final Pessoa other = (Pessoa) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Pessoa{" + "id=" + id + ", nome=" + nome + ", tipoDocumento=" + tipoDocumento + ", obs=" + obs + ", site=" + site + ", telefone1=" + telefone1 + ", telefone2=" + telefone2 + ", telefone3=" + telefone3 + ", email1=" + email1 + ", email2=" + email2 + ", email3=" + email3 + ", documento=" + documento + ", login=" + login + ", senha=" + senha + ", dtCriacao=" + dtCriacao + ", dtAtualizacao=" + dtAtualizacao + '}';
    }

}
