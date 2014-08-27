package br.com.rtools.sistema;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "sis_links")
@NamedQuery(name = "Links.pesquisaID", query = "select l from Links l where l.id = :pid")
public class Links implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne
    private Pessoa pessoa;
    @Column(name = "ds_nome_arquivo")
    private String nomeArquivo;
    @Column(name = "ds_caminho")
    private String caminho;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @Column(name = "ds_descricao")
    private String descricao;

    public Links() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.nomeArquivo = "";
        this.caminho = "";
        this.setEmissao(DataHoje.data());
        this.descricao = "";
    }

    public Links(int id, Pessoa pessoa, String nomeArquivo, String caminho, String emissao, String descricao) {
        this.id = id;
        this.pessoa = pessoa;
        this.nomeArquivo = nomeArquivo;
        this.caminho = nomeArquivo;
        this.setEmissao(emissao);
        this.descricao = descricao;
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

    public String getNomeArquivo() {
        return nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        if (dtEmissao != null) {
            return DataHoje.converteData(dtEmissao);
        } else {
            return "";
        }
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty())) {
            this.dtEmissao = DataHoje.converte(emissao);
        }
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
