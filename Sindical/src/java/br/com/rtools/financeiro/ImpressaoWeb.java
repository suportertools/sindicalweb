package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_impressao_web")
@NamedQuery(name = "ImpressaoWeb.pesquisaID", query = "select i from ImpressaoWeb i where i.id=:pid")
public class ImpressaoWeb implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_movimento", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Movimento movimento;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_data", nullable = false)
    private Date data;
    @Column(name = "ds_hora", nullable = false)
    private String hora;

    public ImpressaoWeb() {
        this.id = -1;
        this.movimento = null;
        this.pessoa = null;
        setData(DataHoje.dataHoje());
        setHora(DataHoje.hora());
    }

    public ImpressaoWeb(int id, Movimento movimento, Pessoa pessoa, Date data, String hora) {
        this.id = id;
        this.movimento = movimento;
        this.pessoa = pessoa;
        this.data = data;
        this.hora = hora;
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

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
