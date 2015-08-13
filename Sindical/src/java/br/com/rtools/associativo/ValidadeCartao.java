package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "soc_validade_cartao",
        uniqueConstraints = @UniqueConstraint(columnNames = {"id_categoria", "id_parentesco"})
)
@NamedQueries({
    @NamedQuery(name = "ValidadeCartao.findAll", query = "SELECT VC FROM ValidadeCartao AS VC ORDER BY VC.categoria.categoria ASC, VC.parentesco.parentesco ASC")
})
public class ValidadeCartao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Categoria categoria;
    @JoinColumn(name = "id_parentesco", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Parentesco parentesco;
    @Column(name = "nr_validade_meses", length = 10, nullable = true)
    private Integer nrValidadeMeses;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade_fixa", nullable = true)
    private Date dtValidadeFixa;

    public ValidadeCartao() {
        this.id = null;
        this.categoria = new Categoria();
        this.parentesco = new Parentesco();
        this.nrValidadeMeses = 0;
        this.dtValidadeFixa = null;
    }

    public ValidadeCartao(Integer id, Categoria categoria, Parentesco parentesco, Integer nrValidadeMeses, String validadeFixa) {
        this.id = id;
        this.categoria = categoria;
        this.parentesco = parentesco;
        this.nrValidadeMeses = nrValidadeMeses;
        this.dtValidadeFixa = DataHoje.converte(validadeFixa);
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Parentesco getParentesco() {
        return parentesco;
    }

    public void setParentesco(Parentesco parentesco) {
        this.parentesco = parentesco;
    }

    public Integer getNrValidadeMeses() {
        if (nrValidadeMeses < 0) {
            nrValidadeMeses = 0;
        }
        if (nrValidadeMeses > 60) {
            nrValidadeMeses = 60;
        }
        return nrValidadeMeses;
    }

    public void setNrValidadeMeses(Integer nrValidadeMeses) {
        this.nrValidadeMeses = nrValidadeMeses;
    }

    public String getNrValidadeMesesString() {
        return Integer.toString(nrValidadeMeses);
    }

    public void setNrValidadeMesesString(String nrValidadeMeseStrings) {
        this.nrValidadeMeses = Integer.parseInt(nrValidadeMeseStrings);
        if (this.nrValidadeMeses < 0) {
            this.nrValidadeMeses = 0;
        }
        if (this.nrValidadeMeses > 60) {
            this.nrValidadeMeses = 60;
        }
    }

    public Date getDtValidadeFixa() {
        return dtValidadeFixa;
    }

    public void setDtValidadeFixa(Date dtValidadeFixa) {
        this.dtValidadeFixa = dtValidadeFixa;
    }

    public String getValidadeFixaString() {
        return DataHoje.converteData(dtValidadeFixa);
    }

    public void setValidadeFixaString(String validadeFixaString) {
        this.dtValidadeFixa = DataHoje.converte(validadeFixaString);
    }

}
