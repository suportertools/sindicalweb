package br.com.rtools.sistema;

import br.com.rtools.seguranca.Usuario;
import br.com.rtools.utilitarios.DataHoje;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "sis_etiquetas")
public class SisEtiquetas implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "id_solicitante", referencedColumnName = "id")
    @ManyToOne
    private Usuario solicitante;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_solicitacao", nullable = true)
    private Date dtSolicitacao;
    @Column(name = "ds_titulo", length = 100)
    private String titulo;
    @Column(name = "ds_detalhes", length = 500)
    private String detalhes;
    @Column(name = "ds_obs", length = 50)
    private String observacao;
    @Column(name = "ds_sql", length = 2000)
    private String sql;

    public SisEtiquetas() {
        this.id = null;
        this.solicitante = null;
        this.dtSolicitacao = DataHoje.dataHoje();
        this.titulo = "";
        this.detalhes = "";
        this.observacao = "";
        this.sql = "SELECT \n"
                + "'' AS nome,\n"
                + "'' AS logradouro,\n"
                + "'' AS endereco,\n"
                + "'' AS numero,\n"
                + "'' AS bairro,\n"
                + "'' AS cidade,\n"
                + "'' AS uf,\n"
                + "'' AS cep,\n"
                + "'' AS complemento,\n"
                + "'' AS observacao;";
    }

    public SisEtiquetas(Integer id, Usuario solicitante, Date dtSolicitacao, String titulo, String detalhes, String observacao, String sql) {
        this.id = id;
        this.solicitante = solicitante;
        this.dtSolicitacao = dtSolicitacao;
        this.titulo = titulo;
        this.detalhes = detalhes;
        this.observacao = observacao;
        this.sql = sql;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(Usuario solicitante) {
        this.solicitante = solicitante;
    }

    public Date getDtSolicitacao() {
        return dtSolicitacao;
    }

    public void setDtSolicitacao(Date dtSolicitacao) {
        this.dtSolicitacao = dtSolicitacao;
    }

    public String getSolicitacaoString() {
        return DataHoje.converteData(dtSolicitacao);
    }

    public void setSolicitacaoString(String solicitacaoString) {
        this.dtSolicitacao = DataHoje.converte(solicitacaoString);
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDetalhes() {
        return detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

}
