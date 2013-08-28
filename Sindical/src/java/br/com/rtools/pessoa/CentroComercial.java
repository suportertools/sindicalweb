package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name="PES_CENTRO_COMERCIAL")
@NamedQuery(name="CentroComercial.pesquisaID", query="select c from CentroComercial c where c.id=:pid")
public class CentroComercial implements java.io.Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name="ID_TIPO_CENTRO_COMERCIAL", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private TipoCentroComercial tipoCentroComercial;
    @JoinColumn(name="ID_JURIDICA", referencedColumnName="ID")
    @ManyToOne (fetch=FetchType.EAGER)
    private Juridica juridica;

    public CentroComercial() {
        this.id = -1;
        this.tipoCentroComercial = new TipoCentroComercial();
        this.juridica = new Juridica();
    }
    
    public CentroComercial(int id, TipoCentroComercial tipoCentroComercial, Juridica juridica) {
        this.id = id;
        this.tipoCentroComercial = tipoCentroComercial;
        this.juridica = juridica;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TipoCentroComercial getTipoCentroComercial() {
        return tipoCentroComercial;
    }

    public void setTipoCentroComercial(TipoCentroComercial tipoCentroComercial) {
        this.tipoCentroComercial = tipoCentroComercial;
    }

    public Juridica getJuridica() {
        return juridica;
    }

    public void setJuridica(Juridica juridica) {
        this.juridica = juridica;
    }    
}