package br.com.rtools.associativo;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.persistence.*;
import org.primefaces.event.SelectEvent;

@Entity
@Table(name = "soc_suspencao")
@NamedQueries({
    @NamedQuery(name = "Suspencao.pesquisaID", query = "SELECT S FROM Suspencao AS S WHERE S.id = :pid"),
    @NamedQuery(name = "Suspencao.findAll", query = "SELECT S FROM Suspencao AS S ORDER BY S.dtFinal DESC, S.pessoa.nome ASC ")
})
public class Suspencao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inicial")
    private Date dtInicial;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_final")
    private Date dtFinal;
    @Column(name = "ds_motivo", length = 300, nullable = true)
    private String motivo;

    public Suspencao() {
        this.id = -1;
        this.pessoa = new Pessoa();
        setDataInicial("");
        setDataFinal("");
        this.motivo = "";
    }

    public Suspencao(int id, String dataInicial, String dataFinal, Pessoa pessoa, String motivo) {
        this.id = id;
        this.pessoa = pessoa;
        setDataInicial(dataInicial);
        setDataFinal(dataFinal);
        this.motivo = motivo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getDtInicial() {
        return dtInicial;
    }

    public void setDtInicial(Date dtInicial) {
        this.dtInicial = dtInicial;
    }

    public Date getDtFinal() {
        return dtFinal;
    }

    public void setDtFinal(Date dtFinal) {
        this.dtFinal = dtFinal;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getDataInicial() {
        if (dtInicial != null) {
            return DataHoje.converteData(dtInicial);
        } else {
            return "";
        }
    }

    public void setDataInicial(String dataInicial) {
        if (!(dataInicial.isEmpty())) {
            this.dtInicial = DataHoje.converte(dataInicial);
        }
    }

    public String getDataFinal() {
        if (dtFinal != null) {
            return DataHoje.converteData(dtFinal);
        } else {
            return "";
        }
    }

    public void setDataFinal(String dataFinal) {
        if (!(dataFinal.isEmpty())) {
            this.dtFinal = DataHoje.converte(dataFinal);
        }
    }
    
    public void selecionaDataInicio(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy"); 
        this.dtInicial = DataHoje.converte(format.format(event.getObject()));
    }
    
    public void selecionaDataTermino(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy"); 
        this.dtFinal = DataHoje.converte(format.format(event.getObject()));
    }    
}
