package br.com.rtools.financeiro;

import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_salario_minimo",
        uniqueConstraints = @UniqueConstraint(columnNames = {"dt_vigencia", "nr_valor_mensal"})
)
@NamedQueries({
    @NamedQuery(name = "SalarioMinimo.pesquisaID", query = "SELECT SM FROM SalarioMinimo AS SM WHERE SM.id = :pid"),
    @NamedQuery(name = "SalarioMinimo.findAll", query = "SELECT SM FROM SalarioMinimo AS SM ORDER BY SM.vigencia DESC ")
})
public class SalarioMinimo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_vigencia")
    private Date vigencia;
    @Column(name = "nr_valor_mensal", columnDefinition = "double precision default 0")
    private float valorMensal;
    @Column(name = "nr_valor_diario", columnDefinition = "double precision default 0")
    private float valorDiario;
    @Column(name = "nr_valor_hora", columnDefinition = "double precision default 0")
    private float valorHora;
    @Column(name = "ds_norma")
    private String norma;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_publicacao")
    private Date publicacao;

    public SalarioMinimo() {
        this.id = -1;
        this.vigencia = new Date();
        this.valorMensal = 0;
        this.valorDiario = 0;
        this.valorHora = 0;
        this.norma = "";
        this.publicacao = new Date();
    }

    public SalarioMinimo(int id, Date vigencia, float valorMensal, float valorDiario, float valorHora, String norma, Date publicacao) {
        this.id = id;
        this.vigencia = vigencia;
        this.valorMensal = valorMensal;
        this.valorDiario = valorDiario;
        this.valorHora = valorHora;
        this.norma = norma;
        this.publicacao = publicacao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getVigencia() {
        return vigencia;
    }

    public void setVigencia(Date vigencia) {
        this.vigencia = vigencia;
    }

    public float getValorMensal() {
        return valorMensal;
    }

    public void setValorMensal(float valorMensal) {
        this.valorMensal = valorMensal;
    }

    public float getValorDiario() {
        return valorDiario;
    }

    public void setValorDiario(float valorDiario) {
        this.valorDiario = valorDiario;
    }

    public float getValorHora() {
        return valorHora;
    }

    public void setValorHora(float valorHora) {
        this.valorHora = valorHora;
    }

    public String getNorma() {
        return norma;
    }

    public void setNorma(String norma) {
        this.norma = norma;
    }

    public Date getPublicacao() {
        return publicacao;
    }

    public void setPublicacao(Date publicacao) {
        this.publicacao = publicacao;
    }

    public String getVigenciaString() {
        return DataHoje.converteData(vigencia);
    }

    public void setVigenciaString(String vigenciaString) {
        this.vigencia = DataHoje.converte(vigenciaString);
    }

    public String getPublicacaoString() {
        return DataHoje.converteData(publicacao);
    }

    public void setPublicacaoString(String publicacaoString) {
        this.publicacao = DataHoje.converte(publicacaoString);
    }

    public String getValorMensalString() {
        return Moeda.converteR$Float(valorMensal);
    }

    public void setValorMensalString(String valorMensalString) {
        this.valorMensal = Moeda.converteUS$(valorMensalString);
    }

    public String getValorDiarioString() {
        return Moeda.converteR$Float(valorDiario);
    }

    public void setValorDiarioString(String valorDiarioString) {
        this.valorDiario = Moeda.converteUS$(valorDiarioString);
    }

    public String getValorHoraString() {
        return Moeda.converteR$Float(valorHora);
    }

    public void setValorHoraString(String valorHoraString) {
        this.valorHora = Moeda.converteUS$(valorHoraString);
    }

}
