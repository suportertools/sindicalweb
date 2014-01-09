package br.com.rtools.escola;

import javax.persistence.*;

/**
 * <p>
 * <strong>ComponenteCurricular</strong></p>
 * <p>
 * <strong>Definição:</strong> Chamamos de componente Curricular a matéria ou
 * disciplina acadêmica que compõe a grade curricular de um determinado curso de
 * um determinado nível de ensino.</p>
 * <h4>Exemplos:</h4>
 * <ul>
 * <li>Matemática, espanhol, inglês, geografia, física... são componentes
 * currículares válidos;</li>
 * <li>Paulo é professor de matemática e inglês;</li>
 * <li>Julio quer ter aulas individuais de espanhol com a professora Maria;</li>
 * </ul>
 *
 * @author rtools
 */
@Entity
@Table(name = "ESC_COMPONENTE_CURRICULAR")
@NamedQueries({
    @NamedQuery(name = "ComponenteCurricular.pesquisaID", query = "SELECT CC FROM ComponenteCurricular AS CC WHERE CC.id = :pid"),
    @NamedQuery(name = "ComponenteCurricular.findAll", query = "SELECT CC FROM ComponenteCurricular AS CC ORDER BY CC.descricao ASC "),
    @NamedQuery(name = "ComponenteCurricular.findName", query = "SELECT CC FROM ComponenteCurricular AS CC WHERE UPPER(CC.descricao) LIKE :pdescricao ORDER BY CC.descricao ASC ")
})
public class ComponenteCurricular implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = true, unique = true)
    private String descricao;

    public ComponenteCurricular() {
        this.id = -1;
        this.descricao = "";
    }

    public ComponenteCurricular(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
