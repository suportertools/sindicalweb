package br.com.rtools.escola;

import br.com.rtools.associativo.Midia;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.ServicoPessoa;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.DataHoje;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "matr_escola")
@NamedQuery(name = "MatriculaEscola.pesquisaID", query = "SELECT MAE FROM MatriculaEscola MAE WHERE MAE.id = :pid")
public class MatriculaEscola implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @ManyToOne
    private EscStatus escStatus;
    @JoinColumn(name = "id_vendedor", referencedColumnName = "id")
    @ManyToOne
    private Vendedor vendedor;
    @JoinColumn(name = "id_midia", referencedColumnName = "id")
    @ManyToOne
    private Midia midia;
    @Column(name = "ds_obs", length = 200)
    private String obs;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_status")
    private Date status;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne
    private Filial filial;
    @JoinColumn(name = "id_servico_pessoa", referencedColumnName = "id")
    @ManyToOne
    private ServicoPessoa servicoPessoa;

    public MatriculaEscola() {
        id = -1;
        escStatus = new EscStatus();
        vendedor = new Vendedor();
        midia = new Midia();
        obs = "";
        status = new Date();
        filial = new Filial();
        servicoPessoa = new ServicoPessoa();
    }

    public MatriculaEscola(int id, EscStatus escStatus, Vendedor vendedor, Midia midia, String obs, Date status, Filial filial, ServicoPessoa servicoPessoa) {
        this.id = id;
        this.escStatus = escStatus;
        this.vendedor = vendedor;
        this.midia = midia;
        this.obs = obs;
        this.status = status;
        this.filial = filial;
        this.servicoPessoa = servicoPessoa;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EscStatus getEscStatus() {
        return escStatus;
    }

    public void setEscStatus(EscStatus escStatus) {
        this.escStatus = escStatus;
    }

    public Vendedor getVendedor() {
        return vendedor;
    }

    public void setVendedor(Vendedor vendedor) {
        this.vendedor = vendedor;
    }

    public Midia getMidia() {
        return midia;
    }

    public void setMidia(Midia midia) {
        this.midia = midia;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Date getStatus() {
        return status;
    }

    public void setStatus(Date status) {
        this.status = status;
    }

    public void setStatusString(String status) {
        this.status = DataHoje.converte(status);
    }

    public String getStatusString() {
        return DataHoje.converteData(status);
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public void listenerStatus(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.status = DataHoje.converte(format.format(event.getObject()));
    }

    public ServicoPessoa getServicoPessoa() {
        return servicoPessoa;
    }

    public void setServicoPessoa(ServicoPessoa servicoPessoa) {
        this.servicoPessoa = servicoPessoa;
    }
}
