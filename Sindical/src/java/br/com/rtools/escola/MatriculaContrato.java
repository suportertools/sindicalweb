package br.com.rtools.escola;

import br.com.rtools.seguranca.Modulo;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "MATR_CONTRATO")
@NamedQuery(name = "MatriculaContrato.pesquisaID", query = "select mc from MatriculaContrato mc where mc.id=:pid")
public class MatriculaContrato implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "DS_TITULO", length = 100)
    private String titulo;
    @Column(name = "DS_DESCRICAO", length = 15000)
    private String descricao;
    @Column(name = "DS_OBSERVACAO", length = 5000)
    private String observacao;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_CADASTRO")
    private Date dataCadastro;
    @Temporal(TemporalType.DATE)
    @Column(name = "DT_ATUALIZADO")
    private Date dataAtualizado;
    @JoinColumn(name = "ID_MODULO", referencedColumnName = "ID")
    @ManyToOne
    private Modulo modulo;

    public MatriculaContrato() {
        this.id = -1;
        this.titulo = "";
        this.descricao = "";
        this.observacao = "";
        this.dataCadastro = new Date();
        this.dataAtualizado = new Date();
        this.modulo = new Modulo();
    }

    public MatriculaContrato(int id, String titulo, String descricao, String observacao, String dataCadastro, String dataAtualizado, Modulo modulo) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.observacao = observacao;
        setDataCadastro(dataCadastro);
        setDataAtualizado(dataAtualizado);
        this.modulo = modulo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getDataCadastro() {
        return DataHoje.converteData(dataCadastro);
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = DataHoje.converte(dataCadastro);
    }

    public String getDataAtualizado() {
        return DataHoje.converteData(dataAtualizado);
    }

    public void setDataAtualizado(String dataAtualizado) {
        this.dataAtualizado = DataHoje.converte(dataAtualizado);
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }
}