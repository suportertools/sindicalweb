package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "pes_mala_direta",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_pessoa", "id_grupo"})
)
@NamedQueries({
    @NamedQuery(name = "MalaDireta.findAll", query = "SELECT MD FROM MalaDireta AS MD ORDER BY MD.pessoa.nome ASC ")
})
public class MalaDireta implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @JoinColumn(name = "id_grupo", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private MalaDiretaGrupo malaDiretaGrupo;

    public MalaDireta() {
        this.id = null;
        this.pessoa = null;
        this.malaDiretaGrupo = null;
    }

    public MalaDireta(Integer id, Pessoa pessoa, MalaDiretaGrupo malaDiretaGrupo) {
        this.id = id;
        this.pessoa = pessoa;
        this.malaDiretaGrupo = malaDiretaGrupo;
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

    public MalaDiretaGrupo getMalaDiretaGrupo() {
        return malaDiretaGrupo;
    }

    public void setMalaDiretaGrupo(MalaDiretaGrupo malaDiretaGrupo) {
        this.malaDiretaGrupo = malaDiretaGrupo;
    }

}
