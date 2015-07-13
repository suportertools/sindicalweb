package br.com.rtools.relatorios;

import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "sis_relatorio_ordem")
public class RelatorioOrdem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_relatorio", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Relatorios relatorios;
    @Column(name = "ds_descricao", length = 100, nullable = false)
    private String nome;
    @Column(name = "ds_query", length = 250)
    private String query;
    @Column(name = "is_default")
    private Boolean principal;

    public RelatorioOrdem() {
        this.id = null;
        this.relatorios = null;
        this.nome = "";
        this.query = "";
        this.principal = false;
    }

    public RelatorioOrdem(Integer id, Relatorios relatorios, String nome, String query, Boolean principal) {
        this.id = id;
        this.relatorios = relatorios;
        this.nome = nome;
        this.query = query;
        this.principal = principal;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Relatorios getRelatorios() {
        return relatorios;
    }

    public void setRelatorios(Relatorios relatorios) {
        this.relatorios = relatorios;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    public Boolean getPrincipal() {
        return principal;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelatorioOrdem other = (RelatorioOrdem) obj;
        return true;
    }

    @Override
    public String toString() {
        return "RelatorioOrdem{" + "id=" + id + ", relatorios=" + relatorios + ", nome=" + nome + ", query=" + query + ", principal=" + principal + '}';
    }

}
