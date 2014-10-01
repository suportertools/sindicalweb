package br.com.rtools.atendimento;

import javax.persistence.*;

@Entity
@Table(name = "ate_status")
@NamedQuery(name = "AteStatus.pesquisaID", query = "select ats from AteStatus ats where ats.id = :pid")
public class AteStatus implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "ds_descricao", length = 50, nullable = false, unique = true)
    private String descricao;
    
    public AteStatus() {
        this.id = -1;
        this.descricao = "";
    }

    public AteStatus(int id, String descricao) {
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
