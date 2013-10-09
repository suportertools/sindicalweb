package br.com.rtools.locadoraFilme;

import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LOC_TITULO")
@NamedQuery(name = "Titulo.pesquisaID", query = "Select t from Titulo t where t.id = :pid")
public class Titulo implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_DATA")
    private Date data;
    @Column(name = "DS_DESCRICAO", length = 50, nullable = true)
    private String descricao;
    @Column(name = "DS_AUTOR", length = 100, nullable = true)
    private String autor;
    @JoinColumn(name = "ID_GENERO", referencedColumnName = "ID")
    @ManyToOne
    private Genero genero;
    @Column(name = "DS_BARRAS", length = 100, nullable = true)
    private String barras;
    @Column(name = "NR_DURACAO_MINUTOS", length = 5, nullable = true)
    private String duracao;
    @Column(name = "ANO_LANCAMENTO")
    private int anoLancamento;
    @Column(name = "DS_LEGENDA", length = 10, nullable = true)
    private String legenda;
    @Column(name = "DS_FORMATO", length = 20, nullable = true)
    private String formato;
    @Column(name = "IS_IMPRIME_ETIQUETA")
    private boolean imprimeEtiqueta;

    public Titulo(int id, Date data, String descricao, String autor, Genero genero, String barras, String duracao, int anoLancamento, String legenda, String formato, boolean imprimeEtiqueta) {
        this.id = id;
        this.data = data;
        this.descricao = descricao;
        this.autor = autor;
        this.genero = genero;
        this.barras = barras;
        this.duracao = duracao;
        this.anoLancamento = anoLancamento;
        this.legenda = legenda;
        this.formato = formato;
        this.imprimeEtiqueta = imprimeEtiqueta;
    }

    public Titulo() {
        id = -1;
        data = new Date();
        descricao = "";
        autor = "";
        genero = new Genero();
        barras = "";
        duracao = "";
        anoLancamento = 0;
        legenda = "";
        formato = "";
        imprimeEtiqueta = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public Genero getGenero() {
        return genero;
    }

    public void setGenero(Genero genero) {
        this.genero = genero;
    }

    public String getBarras() {
        return barras;
    }

    public void setBarras(String barras) {
        this.barras = barras;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public int getAnoLancamento() {
        return anoLancamento;
    }

    public void setAnoLancamento(int anoLancamento) {
        this.anoLancamento = anoLancamento;
    }

    public String getLegenda() {
        return legenda;
    }

    public void setLegenda(String legenda) {
        this.legenda = legenda;
    }

    public String getFormato() {
        return formato;
    }

    public void setFormato(String formato) {
        this.formato = formato;
    }

    public boolean isImprimeEtiqueta() {
        return imprimeEtiqueta;
    }

    public void setImprimeEtiqueta(boolean imprimeEtiqueta) {
        this.imprimeEtiqueta = imprimeEtiqueta;
    }

    public void setDataString(String data) {
        this.data = DataHoje.converte(data);
    }

    public String getDataString() {
        return DataHoje.converteData(data);
    }

    public void setAnoLancamentoString(String anoLancamento) {
        try {
            this.anoLancamento = Integer.parseInt(anoLancamento);
        } catch (Exception e) {
            this.anoLancamento = 0;
        }
    }

    public String getAnoLancamentoString() {
        if (anoLancamento == 0) {
            return "";
        } else {
            return Integer.toString(anoLancamento);
        }
    }
}
