package br.com.rtools.pessoa;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "pes_mala_direta_grupo")
@NamedQueries({
    @NamedQuery(name = "MalaDiretaGrupo.findAll", query = "SELECT MDG FROM MalaDiretaGrupo AS MDG ORDER BY MDG.descricao ASC "),
    @NamedQuery(name = "MalaDiretaGrupo.findName", query = "SELECT MDG FROM MalaDiretaGrupo AS MDG WHERE UPPER(MDG.descricao) LIKE :pdescricao ORDER BY MDG.descricao ASC ")
})
public class MalaDiretaGrupo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;
    @Column(name = "is_ativo", nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean ativo;

    public MalaDiretaGrupo() {
        this.id = null;
        this.descricao = "";
        this.ativo = false;
    }

    public MalaDiretaGrupo(Integer id, String descricao, Boolean ativo) {
        this.id = id;
        this.descricao = descricao;
        this.ativo = ativo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }
}
