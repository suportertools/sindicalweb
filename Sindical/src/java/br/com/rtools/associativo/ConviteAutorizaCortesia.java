package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "conv_autoriza_cortesia")
@NamedQueries({
    @NamedQuery(name = "ConviteAutorizaCortesia.pesquisaID", query = "SELECT CACOR FROM ConviteAutorizaCortesia CACOR WHERE CACOR.id = :pid"),
    @NamedQuery(name = "ConviteAutorizaCortesia.findAll", query = "SELECT CACOR FROM ConviteAutorizaCortesia AS CACOR ORDER BY CACOR.pessoa.nome ASC ")
})
public class ConviteAutorizaCortesia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Pessoa pessoa;

    public ConviteAutorizaCortesia() {
        this.id = -1;
        this.pessoa = new Pessoa();
    }

    public ConviteAutorizaCortesia(int id, Pessoa pessoa) {
        this.id = id;
        this.pessoa = pessoa;
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

    @Override
    public String toString() {
        return "ConviteAutorizaCortesia{" + "id=" + id + ", pessoa=" + pessoa + '}';
    }

}
