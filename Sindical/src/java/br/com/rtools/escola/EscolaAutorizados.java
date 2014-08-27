package br.com.rtools.escola;

import br.com.rtools.pessoa.Pessoa;
import java.io.Serializable;
import javax.persistence.*;

/**
 * <p>
 * <b>Escola Autorizados</b></p>
 * <p>
 * Pessoas autorizadas a buscar o aluno na escola.</p>
 * <br /><br />
 * <p>
 * Exemplo: Marina é funcionária de Paulo. Paulo tem um filho chamado Pedro que
 * estuda na Escola Feliz Mundo. Marina foi autorizada por Paulo à buscar o
 * Aluno Pedro.</p>
 * <br /><br />
 * <p style="color: red;"><b>IMPORTANTE:</b>Os responsáveis também deverão ter o
 * cadastro.</p>
 *
 * @author rtools
 */
@Entity
@Table(name = "esc_autorizados")
@NamedQueries({
    @NamedQuery(name = "EscolaAutorizados.pesquisaID", query = "SELECT EA FROM EscolaAutorizados AS EA WHERE EA.id = :pid"),
    @NamedQuery(name = "EscolaAutorizados.findAll", query = "SELECT EA FROM EscolaAutorizados AS EA ORDER BY EA.matriculaEscola.id ASC, EA.pessoa.nome ASC ")
})
public class EscolaAutorizados implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_matricula_escola", referencedColumnName = "id")
    @ManyToOne
    private MatriculaEscola matriculaEscola;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id")
    @ManyToOne
    private Pessoa pessoa;

    public EscolaAutorizados() {
        this.id = -1;
        this.matriculaEscola = new MatriculaEscola();
        this.pessoa = new Pessoa();
    }

    public EscolaAutorizados(int id, MatriculaEscola matriculaEscola, Pessoa pessoa) {
        this.id = id;
        this.matriculaEscola = matriculaEscola;
        this.pessoa = pessoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MatriculaEscola getMatriculaEscola() {
        return matriculaEscola;
    }

    public void setMatriculaEscola(MatriculaEscola matriculaEscola) {
        this.matriculaEscola = matriculaEscola;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    @Override
    public String toString() {
        return "EscolaAutorizados{" + "id=" + id + ", matriculaEscola=" + matriculaEscola + ", pessoa=" + pessoa + '}';
    }
}
