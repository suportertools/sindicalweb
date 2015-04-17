package br.com.rtools.financeiro;

import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaSemCadastro;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.DataHoje;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "fin_lote")
@NamedQuery(name = "Lote.pesquisaID", query = "select l from Lote l where l.id=:pid")
public class Lote implements java.io.Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_rotina", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Rotina rotina;
    @Column(name = "ds_pag_rec", length = 1, nullable = true)
    private String pagRec;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_lancamento")
    private Date dtLancamento;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_emissao")
    private Date dtEmissao;
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Pessoa pessoa;
    @JoinColumn(name = "id_plano_5", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Plano5 plano5;
    @Column(name = "is_avencer_contabil", columnDefinition = "boolean default false")
    private boolean avencerContabil;
    @Column(name = "ds_documento", length = 100)
    private String documento;
    @Column(name = "nr_valor", length = 10, nullable = false)
    private float valor;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @OneToOne
    private Filial filial;
    @JoinColumn(name = "id_departamento", referencedColumnName = "id")
    @OneToOne
    private Departamento departamento;
    @JoinColumn(name = "id_evt", referencedColumnName = "id")
    @OneToOne
    private Evt evt;
    @Column(name = "ds_historico", length = 2000)
    private String historico;
    @JoinColumn(name = "id_tipo_documento", referencedColumnName = "id")
    @OneToOne
    private FTipoDocumento ftipoDocumento;
    @JoinColumn(name = "id_condicao_pagamento", referencedColumnName = "id")
    @OneToOne
    private CondicaoPagamento condicaoPagamento;
    @JoinColumn(name = "id_status", referencedColumnName = "id")
    @OneToOne
    private FStatus status;
    @JoinColumn(name = "id_pessoa_sem_cadastro", referencedColumnName = "id")
    @OneToOne
    private PessoaSemCadastro pessoaSemCadastro;
    @Column(name = "is_desconto_folha", columnDefinition = "boolean default false")
    private boolean descontoFolha;
    @Column(name = "nr_desconto", length = 10)
    private float desconto;

    public Lote() {
        this.id = -1;
        this.rotina = new Rotina();
        this.pagRec = "P";
        setLancamento(DataHoje.data());
        this.dtEmissao = null;
        this.pessoa = new Pessoa();
        this.plano5 = new Plano5();
        this.avencerContabil = false;
        this.documento = "";
        this.valor = 0;
        this.filial = new Filial();
        this.departamento = new Departamento();
        this.evt = new Evt();
        this.historico = "";
        this.ftipoDocumento = new FTipoDocumento();
        this.condicaoPagamento = new CondicaoPagamento();
        this.status = new FStatus();
        this.pessoaSemCadastro = new PessoaSemCadastro();
        this.descontoFolha = false;
        this.desconto = 0;
    }

    public Lote(int id, Rotina rotina, String pagRec, String dtLancamento, Pessoa pessoa, Plano5 plano5, boolean avencerContabil, String documento, float valor, Filial filial, Departamento departamento, Evt evt, String historico, FTipoDocumento ftipoDocumento, CondicaoPagamento condicaoPagamento, FStatus status, PessoaSemCadastro pessoaSemCadastro, boolean descontoFolha, float desconto) {
        this.id = id;
        this.rotina = rotina;
        this.pagRec = pagRec;
        this.setLancamento(dtLancamento);
        this.setEmissao(DataHoje.data());
        this.pessoa = pessoa;
        this.plano5 = plano5;
        this.avencerContabil = avencerContabil;
        this.documento = documento;
        this.valor = valor;
        this.filial = filial;
        this.departamento = departamento;
        this.evt = evt;
        this.historico = historico;
        this.ftipoDocumento = ftipoDocumento;
        this.condicaoPagamento = condicaoPagamento;
        this.status = status;
        this.pessoaSemCadastro = pessoaSemCadastro;
        this.descontoFolha = descontoFolha;
        this.desconto = desconto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getPagRec() {
        return pagRec;
    }

    public void setPagRec(String pagRec) {
        this.pagRec = pagRec;
    }

    public Date getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(Date dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public String getLancamento() {
        return DataHoje.converteData(dtLancamento);
    }

    public void setLancamento(String dataLancamento) {
        this.dtLancamento = DataHoje.converte(dataLancamento);
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

    public void setEmissao(String emissao) {
        this.dtEmissao = DataHoje.converte(emissao);
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public Plano5 getPlano5() {
        return plano5;
    }

    public void setPlano5(Plano5 plano5) {
        this.plano5 = plano5;
    }

    public boolean isAvencerContabil() {
        return avencerContabil;
    }

    public void setAvencerContabil(boolean avencerContabil) {
        this.avencerContabil = avencerContabil;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public float getValor() {
        return valor;
    }

    public void setValor(float valor) {
        this.valor = valor;
    }

    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Evt getEvt() {
        return evt;
    }

    public void setEvt(Evt evt) {
        this.evt = evt;
    }

    public String getHistorico() {
        return historico;
    }

    public void setHistorico(String historico) {
        this.historico = historico;
    }

    public FTipoDocumento getFtipoDocumento() {
        return ftipoDocumento;
    }

    public void setFTipoDocumento(FTipoDocumento ftipoDocumento) {
        this.ftipoDocumento = ftipoDocumento;
    }

    public CondicaoPagamento getCondicaoPagamento() {
        return condicaoPagamento;
    }

    public void setCondicaoPagamento(CondicaoPagamento condicaoPagamento) {
        this.condicaoPagamento = condicaoPagamento;
    }

    public FStatus getStatus() {
        return status;
    }

    public void setStatus(FStatus status) {
        this.status = status;
    }

    public PessoaSemCadastro getPessoaSemCadastro() {
        return pessoaSemCadastro;
    }

    public void setPessoaSemCadastro(PessoaSemCadastro pessoaSemCadastro) {
        this.pessoaSemCadastro = pessoaSemCadastro;
    }

    public boolean isDescontoFolha() {
        return descontoFolha;
    }

    public void setDescontoFolha(boolean descontoFolha) {
        this.descontoFolha = descontoFolha;
    }

    public float getDesconto() {
        return desconto;
    }

    public void setDesconto(float desconto) {
        this.desconto = desconto;
    }
}
