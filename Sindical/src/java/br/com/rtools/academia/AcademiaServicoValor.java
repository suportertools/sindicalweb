package br.com.rtools.academia;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.sistema.Periodo;
import java.io.Serializable;
import javax.persistence.*;

@Entity
@Table(name = "ACA_SERVICO_VALOR")
@NamedQueries({
    @NamedQuery(name = "AcademiaServicoValor.pesquisaID", query = "SELECT ASV FROM AcademiaServicoValor AS ASV WHERE ASV.id = :pid"),
    @NamedQuery(name = "AcademiaServicoValor.findAll", query = "SELECT ASV FROM AcademiaServicoValor AS ASV ORDER BY ASV.periodo.descricao ASC, ASV.servicos.descricao ASC, ASV.academiaGrade.horaInicio ASC")
})
public class AcademiaServicoValor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SERVICO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "ID_PERIODO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Periodo periodo;
    @JoinColumn(name = "ID_GRADE", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private AcademiaGrade academiaGrade;
    @Column(name = "DS_FORMULA", length = 255)
    private String formula;
    @Column(name = "NR_PARCELAS")
    private int numeroParcelas;

    public AcademiaServicoValor() {
        this.id = -1;
        this.servicos = new Servicos();
        this.periodo = new Periodo();
        this.academiaGrade = new AcademiaGrade();
        this.formula = "";
        this.numeroParcelas = 0;
    }

    public AcademiaServicoValor(int id, Servicos servicos, Periodo periodo, AcademiaGrade academiaGrade, String formula, int numeroParcelas) {
        this.id = id;
        this.servicos = servicos;
        this.periodo = periodo;
        this.academiaGrade = academiaGrade;
        this.formula = formula;
        this.numeroParcelas = numeroParcelas;
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

    public AcademiaGrade getAcademiaGrade() {
        return academiaGrade;
    }

    public void setAcademiaGrade(AcademiaGrade academiaGrade) {
        this.academiaGrade = academiaGrade;
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

}
