package br.com.rtools.financeiro;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "fin_movimento_boleto")
public class MovimentoBoleto implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id")
    @ManyToOne
    private Movimento movimento;    
    @JoinColumn(name = "id_boleto", referencedColumnName = "id")
    @ManyToOne
    private Boleto boleto;

    public MovimentoBoleto() {
        this.id = -1;
        this.movimento = new Movimento();
        this.boleto = new Boleto();
    }
    
    public MovimentoBoleto(int id, Movimento movimento, Boleto boleto) {
        this.id = id;
        this.movimento = movimento;
        this.boleto = boleto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Movimento getMovimento() {
        return movimento;
    }

    public void setMovimento(Movimento movimento) {
        this.movimento = movimento;
    }

    public Boleto getBoleto() {
        return boleto;
    }

    public void setBoleto(Boleto boleto) {
        this.boleto = boleto;
    }
    
    
}
