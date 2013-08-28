package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name="PES_CONSELHO")
@NamedQuery(name="Conselho.pesquisaID", query="select consel from Conselho consel where consel.id=:pid")
public class Conselho implements java.io.Serializable {    
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @Column(name="DS_CONSELHO", length=50,nullable=false)
    private String conselho;
    @Column(name="DS_TIPO_CONSELHO", length=50,nullable=false)
    private String tipoConselho;

    public Conselho() {
        this.id = -1;
        this.conselho = "";
        this.tipoConselho = "";
    }
    
    public Conselho(int id, String conselho, String tipoConselho) {
        this.id = id;
        this.conselho = conselho;
        this.tipoConselho = tipoConselho;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getConselho() {
        return conselho;
    }

    public void setConselho(String conselho) {
        this.conselho = conselho;
    }

    public String getTipoConselho() {
        return tipoConselho;
    }

    public void setTipoConselho(String tipoConselho) {
        this.tipoConselho = tipoConselho;
    }
}
            
    