package br.com.rtools.sistema;

import javax.persistence.*;

@Entity
@Table(name = "SIS_PERIODO")
@NamedQuery(name = "Periodo.pesquisaID", query = "SELECT P FROM Periodo AS P WHERE P.id = :pid")
public class Periodo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_DESCRICAO", length = 20, unique = true)
    private String descricao;
    @Column(name = "NR_DIAS")
    private int dias;

    public Periodo() {
        this.id = -1;
        this.descricao = "";
        this.dias = 0;
    }

    public Periodo(int id, String descricao, int dias) {
        this.id = id;
        this.descricao = descricao;
        this.dias = dias;
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

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
}
