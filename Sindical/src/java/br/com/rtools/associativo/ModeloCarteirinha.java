package br.com.rtools.associativo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "SOC_MODELO_CARTEIRINHA")
@NamedQuery(name = "ModeloCarteirinha.pesquisaID", query = "select mc from ModeloCarteirinha mc where mc.id = :pid")
public class ModeloCarteirinha  implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 150)
    private String descricao;
    @Column(name = "DS_JASPER", length = 150)
    private String jasper;

    public ModeloCarteirinha() {
        this.id = -1;
        this.descricao = "";
        this.jasper = "";
    }
    
    public ModeloCarteirinha(int id, String descricao, String jasper) {
        this.id = id;
        this.descricao = descricao;
        this.jasper = jasper;
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

    public String getJasper() {
        return jasper;
    }

    public void setJasper(String jasper) {
        this.jasper = jasper;
    }
}
