package br.com.rtools.agenda;

import br.com.rtools.endereco.Endereco;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.TipoEndereco;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "AGE_AGENDA")
@NamedQuery(name = "Agenda.pesquisaID", query = "SELECT a FROM Agenda a WHERE a.id=:pid")
public class Agenda implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_PESSOA", referencedColumnName = "ID")
    @ManyToOne
    private Pessoa pessoa;
    @JoinColumn(name = "ID_GRUPO_AGENDA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private GrupoAgenda grupoAgenda;
    @Column(name = "DS_NOME", length = 100)
    private String nome;
    @Column(name = "DS_EMAIL1", length = 500)
    private String email1;
    @Column(name = "DS_EMAIL2", length = 500)
    private String email2;
    @Column(name = "DS_OBS", length = 8000)
    private String observacao;
    @Column(name = "DS_COMPLEMENTO", length = 50)
    private String complemento;
    @Column(name = "DS_NUMERO", length = 30)
    private String numero;
    @JoinColumn(name = "ID_TIPO_ENDERECO", referencedColumnName = "ID")
    @ManyToOne
    private TipoEndereco tipoEndereco;
    @JoinColumn(name = "ID_ENDERECO", referencedColumnName = "ID")
    @ManyToOne
    private Endereco endereco;

    public Agenda() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.grupoAgenda = new GrupoAgenda();
        this.nome = "";
        this.email1 = "";
        this.email2 = "";
        this.observacao = "";
        this.complemento = "";
        this.numero = "";
        this.tipoEndereco = new TipoEndereco();
        this.endereco = new Endereco();
    }

    public Agenda(int id, Pessoa pessoa, GrupoAgenda grupoAgenda, String nome, String email1, String email2, String observacao, String complemento, String numero, TipoEndereco tipoEndereco, Endereco endereco) {
        this.id = id;
        this.pessoa = pessoa;
        this.grupoAgenda = grupoAgenda;
        this.nome = nome;
        this.email1 = email1;
        this.email2 = email2;
        this.observacao = observacao;
        this.complemento = complemento;
        this.numero = numero;
        this.tipoEndereco = tipoEndereco;
        this.endereco = endereco;
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

    public GrupoAgenda getGrupoAgenda() {
        return grupoAgenda;
    }

    public void setGrupoAgenda(GrupoAgenda grupoAgenda) {
        this.grupoAgenda = grupoAgenda;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public TipoEndereco getTipoEndereco() {
        return tipoEndereco;
    }

    public void setTipoEndereco(TipoEndereco tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
