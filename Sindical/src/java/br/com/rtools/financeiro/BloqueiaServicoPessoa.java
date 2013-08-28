package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name="FIN_BLOQUEIA_SERVICO_PESSOA")
@NamedQuery(name="BloqueiaServicoPessoa.pesquisaID", query="select b from BloqueiaServicoPessoa b where b.id=:pid")
public class BloqueiaServicoPessoa implements java.io.Serializable {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;

    @JoinColumn(name="ID_PESSOA", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Pessoa pessoa;
    
    @JoinColumn(name="ID_SERVICOS", referencedColumnName="ID", nullable=false)
    @ManyToOne
    private Servicos servicos;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_INICIO")
    private Date dtInicio;

    @Temporal(TemporalType.DATE)
    @Column(name="DT_FIM")
    private Date dtFim;
    
    @Column(name="IS_IMPRESSAO")
    private boolean impressao;
    
    @Column(name="IS_GERACAO")
    private boolean geracao;

    public BloqueiaServicoPessoa() {
        this.id = -1;
        this.pessoa = new Pessoa();
        this.servicos = new Servicos();
        this.setInicio("");
        this.setFim("");
        this.impressao = true;
        this.geracao = true;
    }
    
    public BloqueiaServicoPessoa(int id, Pessoa pessoa, Servicos servicos, String inicio, String fim, boolean impressao, boolean geracao) {
        this.id = id;
        this.pessoa = pessoa;
        this.servicos = servicos;
        this.setInicio(inicio);
        this.setFim(fim);
        this.impressao = impressao;
        this.geracao = geracao;
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

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public Date getDtInicio() {
        return dtInicio;
    }

    public void setDtInicio(Date dtInicio) {
        this.dtInicio = dtInicio;
    }

    public Date getDtFim() {
        return dtFim;
    }

    public void setDtFim(Date dtFim) {
        this.dtFim = dtFim;
    }

    public boolean isImpressao() {
        return impressao;
    }

    public void setImpressao(boolean impressao) {
        this.impressao = impressao;
    }

    public boolean isGeracao() {
        return geracao;
    }

    public void setGeracao(boolean geracao) {
        this.geracao = geracao;
    }

    public String getInicio() {
        return DataHoje.converteData(dtInicio);
    }

    public void setInicio(String inicio) {
        this.dtInicio = DataHoje.converte(inicio);
    }    
    
    public String getFim() {
        return DataHoje.converteData(dtFim);
    }

    public void setFim(String fim) {
        this.dtFim = DataHoje.converte(fim);
    }
}