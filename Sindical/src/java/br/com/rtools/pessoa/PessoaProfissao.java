package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "pes_pessoa_profissao")
@NamedQuery(name = "PessoaProfissao.pesquisaID", query = "select pprof from PessoaProfissao pprof where pprof.id=:pid")
public class PessoaProfissao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_fisica", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Fisica fisica;
    @JoinColumn(name = "id_profissao", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Profissao profissao;

    public PessoaProfissao() {
        this.id = -1;
        this.fisica = new Fisica();
        this.profissao = new Profissao();
    }

    public PessoaProfissao(int id, Fisica fisica, Profissao profissao) {
        this.id = id;
        this.fisica = fisica;
        this.profissao = profissao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public Profissao getProfissao() {
        return profissao;
    }

    public void setProfissao(Profissao profissao) {
        this.profissao = profissao;
    }
}
