package br.com.rtools.relatorios;

import br.com.rtools.seguranca.Rotina;
import javax.persistence.*;

@Entity
@Table(name = "SIS_RELATORIOS")
@NamedQuery(name = "Relatorios.pesquisaID", query = "select rel from Relatorios rel where rel.id=:pid")
public class Relatorios implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_ROTINA", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Rotina rotina;
    @Column(name = "DS_NOME", length = 100, nullable = false)
    private String nome;
    @Column(name = "DS_JASPER", length = 50, nullable = false)
    private String jasper;
    @Column(name = "DS_QRY", length = 1000)
    private String qry;
    @Column(name = "DS_QRY_ORDEM", length = 1000)
    private String qryOrdem;

    public Relatorios() {
        this.id = -1;
        this.rotina = new Rotina();
        this.nome = "";
        this.jasper = "";
        this.qry = "";
        this.qryOrdem = "";
    }

    public Relatorios(int id, Rotina rotina, String nome, String jasper, String qry, String qryOrdem) {
        this.id = -1;
        this.rotina = rotina;
        this.nome = nome;
        this.jasper = jasper;
        this.qry = qry;
        this.qryOrdem = qryOrdem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getJasper() {
        return jasper;
    }

    public void setJasper(String jasper) {
        this.jasper = jasper;
    }

    public String getQry() {
        return qry;
    }

    public void setQry(String qry) {
        this.qry = qry;
    }

    public String getQryOrdem() {
        return qryOrdem;
    }

    public void setQryOrdem(String qryOrdem) {
        this.qryOrdem = qryOrdem;
    }
}
