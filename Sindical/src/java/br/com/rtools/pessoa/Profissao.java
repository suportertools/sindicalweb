package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "PES_PROFISSAO")
@NamedQuery(name = "Profissao.pesquisaID", query = "select prof from Profissao prof where prof.id=:pid")
public class Profissao implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_PROFISSAO", length = 200, nullable = false)
    private String profissao;
    @Column(name = "DS_CBO", length = 10, nullable = true)
    private String cbo;

    public Profissao() {
        this.id = -1;
        this.profissao = "";
        this.cbo = "";
    }

    public Profissao(int id, String profissao, String cbo) {
        this.id = id;
        this.profissao = profissao;
        this.profissao = cbo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfissao() {
        return profissao;
    }

    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    public String getCbo() {
        return cbo;
    }

    public void setCbo(String cbo) {
        this.cbo = cbo;
    }
}