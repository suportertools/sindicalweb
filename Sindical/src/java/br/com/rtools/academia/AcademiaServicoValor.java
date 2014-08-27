package br.com.rtools.academia;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "aca_servico_valor")
@NamedQueries({
    @NamedQuery(name = "AcademiaServicoValor.pesquisaID", query = "SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.id = :pid"),
    @NamedQuery(name = "AcademiaServicoValor.findAll", query = "SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.validade IS NULL OR ASV.validade >= CURRENT_TIMESTAMP ORDER BY ASV.periodo.descricao ASC, ASV.servicos.descricao ASC")
})
public class AcademiaServicoValor implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_servico", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "id_periodo", referencedColumnName = "id", nullable = false)
    @ManyToOne
    private Periodo periodo;
    @Column(name = "ds_formula", length = 255)
    private String formula;
    @Column(name = "nr_parcelas")
    private int numeroParcelas;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_validade")
    private Date validade;

    public AcademiaServicoValor() {
        this.id = -1;
        this.servicos = new Servicos();
        this.periodo = new Periodo();
        this.formula = "";
        this.numeroParcelas = 0;
        this.validade = new Date();
    }

    public AcademiaServicoValor(int id, Servicos servicos, Periodo periodo, String formula, int numeroParcelas, Date validade) {
        this.id = id;
        this.servicos = servicos;
        this.periodo = periodo;
        this.formula = formula;
        this.numeroParcelas = numeroParcelas;
        this.validade = validade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public Periodo getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Periodo periodo) {
        this.periodo = periodo;
    }

    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public int getNumeroParcelas() {
        return numeroParcelas;
    }

    public void setNumeroParcelas(int numeroParcelas) {
        this.numeroParcelas = numeroParcelas;
    }

    public Date getValidade() {
        return validade;
    }

    public void setValidade(Date validade) {
        this.validade = validade;
    }

    public String getValidadeString() {
        return DataHoje.converteData(validade);
    }

    public void setValidadeString(String validadeString) {
        this.validade = DataHoje.converte(validadeString);
    }

}
