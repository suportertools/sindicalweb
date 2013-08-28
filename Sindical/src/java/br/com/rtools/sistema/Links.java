package br.com.rtools.sistema;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="SIS_LINKS")
@NamedQuery(name="Links.pesquisaID", query="select l from Links l where l.id = :pid")
public class Links implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_PESSOA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Pessoa pessoa;
    @Column(name="DS_NOME_ARQUIVO")
    private String nomeArquivo;   
    @Column(name="DS_CAMINHO")
    private String caminho;   
    @Temporal(TemporalType.DATE)
    @Column(name="DT_EMISSAO")
    private Date dtEmissao;

    public Links() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.nomeArquivo = "";
        this.caminho = "";
        this.setEmissao(DataHoje.data());
    }
    
    public Links(int id, Pessoa pessoa, String nomeArquivo, String caminho, String emissao) {
        this.id = id;
        this.pessoa = pessoa;
        this.nomeArquivo = nomeArquivo;
        this.caminho = nomeArquivo;
        this.setEmissao(emissao);
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
        if (dtEmissao != null)
            return DataHoje.converteData(dtEmissao);
        else
            return "";
    }

    public void setEmissao(String emissao) {
        if (!(emissao.isEmpty()))
            this.dtEmissao = DataHoje.converte(emissao);
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }
    
    
    
}
