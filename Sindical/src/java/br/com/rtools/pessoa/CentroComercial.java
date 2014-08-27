package br.com.rtools.pessoa;

import javax.persistence.*;

@Entity
@Table(name = "pes_centro_comercial")
@NamedQueries({
    @NamedQuery(name = "CentroComercial.pesquisaID", query = "SELECT CC FROM CentroComercial AS CC WHERE CC.id = :pid"),
    @NamedQuery(name = "CentroComercial.findAll", query = "SELECT CC FROM CentroComercial AS CC ORDER BY CC.tipoCentroComercial.descricao ASC, CC.juridica.pessoa.nome ASC "),
    @NamedQuery(name = "CentroComercial.findName", query = "SELECT CC FROM CentroComercial AS CC WHERE UPPER(CC.tipoCentroComercial.descricao) LIKE :pdescricao ORDER BY CC.tipoCentroComercial.descricao ASC, CC.juridica.pessoa.nome ASC ")
})
public class CentroComercial implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_tipo_centro_comercial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private TipoCentroComercial tipoCentroComercial;
    @JoinColumn(name = "id_juridica", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
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
