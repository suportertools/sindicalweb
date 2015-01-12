package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pes_administradora")
@NamedQuery(name = "Administradora.findAll", query = "SELECT A FROM Administradora AS A ORDER BY A.pessoa.nome ASC")
public class Administradora implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false, unique = true)
    @ManyToOne
    private Pessoa pessoa;

    public Administradora() {
        this.id = -1;
        this.pessoa = new Pessoa();
    }

    public Administradora(Integer id, Pessoa pessoa) {
        this.id = id;
        this.pessoa = pessoa;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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
        final Administradora other = (Administradora) obj;
        return true;
    }

    @Override
    public String toString() {
        return "Administradora{" + "id=" + id + ", pessoa=" + pessoa + '}';
    }

}
