package br.com.rtools.associativo;

import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.utilitarios.DataHoje;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
@Table(name = "matr_convenio_medico")
@NamedQuery(name = "MatriculaConvenioMedico.pesquisaID", query = "select mc from MatriculaConvenioMedico mc where mc.id=:pid")
public class MatriculaConvenioMedico implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inativo")
    private Date dtInativo;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private ServicoPessoa servicoPessoa;
    @Column(name = "ds_codigo", length = 50, nullable = true)
    private String codigo;

    public MatriculaConvenioMedico() {
        this.id = -1;
        this.dtInativo = null;
        this.servicoPessoa = new ServicoPessoa();
        this.codigo = "";
    }

    public MatriculaConvenioMedico(int id, String inativo, ServicoPessoa servicoPessoa, String codigo) {
        this.id = id;
        this.setInativo(inativo);
        this.servicoPessoa = servicoPessoa;
        this.codigo = codigo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDtInativo() {
        return dtInativo;
    }

    public void setDtInativo(Date dtInativo) {
        this.dtInativo = dtInativo;
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getInativo() {
        if (dtInativo != null) {
            return DataHoje.converteData(dtInativo);
        } else {
            return "";
        }
    }

    public void setInativo(String inativo) {
        if (inativo != null && !inativo.isEmpty()) {
            this.dtInativo = DataHoje.converte(inativo);
        }
    }
}
