package br.com.rtools.associativo;

import br.com.rtools.endereco.Cidade;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
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
@Table(name = "matr_socios")
@NamedQuery(name = "MatriculaSocios.pesquisaID", query = "select ms from MatriculaSocios ms where ms.id=:pid")
public class MatriculaSocios implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_inativo")
    private Date dtInativo;
    @JoinColumn(name = "id_categoria", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Categoria categoria;
    @Column(name = "nr_matricula", length = 10, nullable = true)
    private int nrMatricula;
    @JoinColumn(name = "id_cidade", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Cidade cidade;
    @Column(name = "ds_observacao", length = 100, nullable = true)
    private String observacao;
    @Column(name = "ds_observacao_aviso", length = 100, nullable = true)
    private String observacaoAviso;
    @Column(name = "bloqueia_obs_aviso", nullable = true)
    private boolean bloqueiaObsAviso;
    @JoinColumn(name = "id_motivo_inativacao", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SMotivoInativacao motivoInativacao;
    @JoinColumn(name = "id_titular", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Pessoa titular;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @Column(name = "ds_motivo", length = 500)
    private String motivo;
    @JoinColumn(name = "id_filial", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Filial filial;

    public MatriculaSocios(int id, String inativo, Categoria categoria, int nrMatricula, Cidade cidade, String observacao, String observacaoAviso, boolean bloqueiaObsAviso, SMotivoInativacao motivoInativacao, Pessoa titular, String emissao, String motivo, Filial filial) {
        this.id = id;
        this.setInativo(inativo);
        this.categoria = categoria;
        this.nrMatricula = nrMatricula;
        this.cidade = cidade;
        this.observacao = observacao;
        this.observacaoAviso = observacaoAviso;
        this.bloqueiaObsAviso = bloqueiaObsAviso;
        this.motivoInativacao = motivoInativacao;
        this.titular = titular;
        this.setEmissao(emissao);
        this.motivo = motivo;
        this.filial = filial;

    }

    public MatriculaSocios() {
        this.id = -1;
        this.setInativo("");
        this.categoria = new Categoria();
        this.nrMatricula = 0;
        this.cidade = new Cidade();
        this.observacao = "";
        this.observacaoAviso = "";
        this.bloqueiaObsAviso = false;
        this.motivoInativacao = new SMotivoInativacao();
        this.titular = new Pessoa();
        this.setEmissao("");
        this.motivo = "";
        this.filial = null;
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

    public String getInativo() {
        if (dtInativo != null) {
            return DataHoje.converteData(dtInativo);
        } else {
            return "";
        }
    }

    public void setInativo(String inativo) {
        if (!(inativo.isEmpty())) {
            this.dtInativo = DataHoje.converte(inativo);
        }
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getNrMatricula() {
        return nrMatricula;
    }

    public void setNrMatricula(int nrMatricula) {
        this.nrMatricula = nrMatricula;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getObservacaoAviso() {
        return observacaoAviso;
    }

    public void setObservacaoAviso(String observacaoAviso) {
        this.observacaoAviso = observacaoAviso;
    }

    public boolean isBloqueiaObsAviso() {
        return bloqueiaObsAviso;
    }

    public void setBloqueiaObsAviso(boolean bloqueiaObsAviso) {
        this.bloqueiaObsAviso = bloqueiaObsAviso;
    }

    public SMotivoInativacao getMotivoInativacao() {
        return motivoInativacao;
    }

    public void setMotivoInativacao(SMotivoInativacao motivoInativacao) {
        this.motivoInativacao = motivoInativacao;
    }

    public Pessoa getTitular() {
        return titular;
    }

    public void setTitular(Pessoa titular) {
        this.titular = titular;
    }

    public Date getDtEmissao() {
        return dtEmissao;
    }

    public void setDtEmissao(Date dtEmissao) {
        this.dtEmissao = dtEmissao;
    }

    public String getEmissao() {
        return DataHoje.converteData(dtEmissao);
    }

    public void setEmissao(String dtEmissao) {
        this.dtEmissao = DataHoje.converte(dtEmissao);
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }
}
