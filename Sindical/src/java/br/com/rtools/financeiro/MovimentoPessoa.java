package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "fin_movimento_pessoa")
@NamedQuery(name = "MovimentoPessoa.pesquisaID", query = "select c from MovimentoPessoa c where c.id=:pid")
public class MovimentoPessoa implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Movimento movimento;
    @Column(name = "ds_nome_pessoa", length = 100, nullable = false)
    private String nome;

    public MovimentoPessoa() {
        this.id = -1;
        this.movimento = new Movimento();
        this.nome = "";
    }

    public MovimentoPessoa(int id, Movimento movimento, String nome) {
        this.id = id;
        this.movimento = movimento;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
