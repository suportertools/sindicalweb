package br.com.rtools.financeiro;

import javax.persistence.*;

@Entity
@Table(name = "FIN_SERVICO_CONTA_COBRANCA")
@NamedQuery(name = "ServicoContaCobranca.pesquisaID", query = "select s from ServicoContaCobranca s where s.id=:pid")
public class ServicoContaCobranca implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @JoinColumn(name = "ID_SERVICOS", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Servicos servicos;
    @JoinColumn(name = "ID_CONTA_COBRANCA", referencedColumnName = "ID", nullable = false)
    @OneToOne
    private ContaCobranca contaCobranca;
    @JoinColumn(name = "ID_TIPO_SERVICO", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private TipoServico tipoServico;

    public ServicoContaCobranca() {
        this.id = -1;
        this.servicos = new Servicos();
        this.contaCobranca = new ContaCobranca();
        this.tipoServico = new TipoServico();
    }

    public ServicoContaCobranca(int id, Servicos servicos, ContaCobranca contaCobranca, TipoServico tipoServico) {
        this.id = id;
        this.servicos = servicos;
        this.contaCobranca = contaCobranca;
        this.tipoServico = tipoServico;
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

    public ContaCobranca getContaCobranca() {
        return contaCobranca;
    }

    public void setContaCobranca(ContaCobranca contaCobranca) {
        this.contaCobranca = contaCobranca;
    }

    public TipoServico getTipoServico() {
        return tipoServico;
    }

    public void setTipoServico(TipoServico tipoServico) {
        this.tipoServico = tipoServico;
    }
}
