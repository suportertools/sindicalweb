package br.com.rtools.escola;

import br.com.rtools.utilitarios.BaseEntity;
import java.io.Serializable;
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
@Table(name = "esc_componente_curricular")
@NamedQueries({
    @NamedQuery(name = "ComponenteCurricular.pesquisaID", query = "SELECT CC FROM ComponenteCurricular AS CC WHERE CC.id = :pid"),
    @NamedQuery(name = "ComponenteCurricular.findAll", query = "SELECT CC FROM ComponenteCurricular AS CC ORDER BY CC.descricao ASC "),
    @NamedQuery(name = "ComponenteCurricular.findName", query = "SELECT CC FROM ComponenteCurricular AS CC WHERE UPPER(CC.descricao) LIKE :pdescricao ORDER BY CC.descricao ASC ")
})
public class ComponenteCurricular implements BaseEntity, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = true, unique = true)
    private String descricao;

    public ComponenteCurricular() {
        this.id = -1;
        this.descricao = "";
    }

    public ComponenteCurricular(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    @Override
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

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + this.id;
        hash = 67 * hash + (this.descricao != null ? this.descricao.hashCode() : 0);
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
        final ComponenteCurricular other = (ComponenteCurricular) obj;
        if (this.id != other.id) {
            return false;
        }
        if ((this.descricao == null) ? (other.descricao != null) : !this.descricao.equals(other.descricao)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ComponenteCurricular{" + "id=" + id + ", descricao=" + descricao + '}';
    }

}
