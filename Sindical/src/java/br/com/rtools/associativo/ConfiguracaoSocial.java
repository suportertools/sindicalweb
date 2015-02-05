package br.com.rtools.associativo;

import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conf_social")
public class ConfiguracaoSocial implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;    
    
    @Column(name = "nr_dias_inativa_demissionado")
    private int diasInativaDemissionado;
    @Column(name = "dt_inativa_demissionado")
    @Temporal(TemporalType.DATE)
    private Date dataInativacaoDemissionado;
    @JoinColumn(name = "id_grupo_categoria_inativa_demissionado", referencedColumnName = "id")
    @ManyToOne
    private GrupoCategoria grupoCategoriaInativaDemissionado;
    @Column(name = "is_inativa_demissionado", columnDefinition = "boolean default false")
    private boolean inativaDemissionado;    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDiasInativaDemissionado() {
        return diasInativaDemissionado;
    }

    public void setDiasInativaDemissionado(int diasInativaDemissionado) {
        this.diasInativaDemissionado = diasInativaDemissionado;
    }

    public Date getDataInativacaoDemissionado() {
        return dataInativacaoDemissionado;
    }

    public void setDataInativacaoDemissionado(Date dataInativacaoDemissionado) {
        this.dataInativacaoDemissionado = dataInativacaoDemissionado;
    }
    
    public String getDataInativacaoDemissionadoString() {
        return DataHoje.converteData(dataInativacaoDemissionado);
    }

    public void setDataInativacaoDemissionadoString(String dataInativacaoDemissionado) {
        this.dataInativacaoDemissionado = DataHoje.converte(dataInativacaoDemissionado);
    }

    public GrupoCategoria getGrupoCategoriaInativaDemissionado() {
        return grupoCategoriaInativaDemissionado;
    }

    public void setGrupoCategoriaInativaDemissionado(GrupoCategoria grupoCategoriaInativaDemissionado) {
        this.grupoCategoriaInativaDemissionado = grupoCategoriaInativaDemissionado;
    }

    public boolean isInativaDemissionado() {
        return inativaDemissionado;
    }

    public void setInativaDemissionado(boolean inativaDemissionado) {
        this.inativaDemissionado = inativaDemissionado;
    }
}
