package br.com.rtools.homologacao;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "conf_homologacao")
public class ConfiguracaoHomologacao implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // PARÂMETROS
    @Column(name = "dt_habilita_correcao")
    @Temporal(TemporalType.DATE)
    private Date homolocaoHabilitaCorrecao;
    @Column(name = "nr_tempo_refresh_agendamento")
    private int tempoRefreshAgendamento;
    @Column(name = "nr_tempo_refresh_recepcao")
    private int tempoRefreshRecepcao;
    @Column(name = "nr_tempo_refresh_homologacao")
    private int tempoRefreshHomologacao;
    @Column(name = "nr_tempo_refresh_atendimento")
    private int tempoRefreshAtendimento;
    // LIMITE DE MESES PARA AGENDAMENTO - > FUTURO
    @Column(name = "nr_limite_meses", columnDefinition = "integer default 3")
    private int limiteMeses;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_limite_agendamento_retroativo")
    private Date limiteAgendamentoRetroativo;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atualiza_homologacao")
    private Date dataAtualizaHomologacao;

    // CAMPOS OBRIGATÓRIOS
    @Column(name = "is_valida_cpf", columnDefinition = "boolean default false")
    private boolean validaCpf;
    @Column(name = "is_valida_nome", columnDefinition = "boolean default false")
    private boolean validaNome;
    @Column(name = "is_valida_endereco", columnDefinition = "boolean default false")
    private boolean validaEndereco;
    @Column(name = "is_valida_carteira", columnDefinition = "boolean default false")
    private boolean validaCarteira;
    @Column(name = "is_valida_serie", columnDefinition = "boolean default false")
    private boolean validaSerie;
    @Column(name = "is_valida_funcao", columnDefinition = "boolean default false")
    private boolean validaFuncao;
    @Column(name = "is_valida_admissao", columnDefinition = "boolean default false")
    private boolean validaAdmissao;
    @Column(name = "is_valida_demissao", columnDefinition = "boolean default false")
    private boolean validaDemissao;
    @Column(name = "is_valida_contato", columnDefinition = "boolean default false")
    private boolean validaContato;
    @Column(name = "is_valida_email", columnDefinition = "boolean default false")
    private boolean validaEmail;
    @Column(name = "is_valida_telefone", columnDefinition = "boolean default false")
    private boolean validaTelefone;

    public ConfiguracaoHomologacao() {
        this.id = -1;
        this.homolocaoHabilitaCorrecao = null;
        this.tempoRefreshAgendamento = 5;
        this.tempoRefreshRecepcao = 5;
        this.tempoRefreshHomologacao = 5;
        this.tempoRefreshAtendimento = 5;
        this.limiteMeses = 3;
        this.limiteAgendamentoRetroativo = null;
        this.dataAtualizaHomologacao = null;
        this.validaCpf = false;
        this.validaNome = false;
        this.validaEndereco = false;
        this.validaCarteira = false;
        this.validaSerie = false;
        this.validaFuncao = false;
        this.validaAdmissao = false;
        this.validaDemissao = false;
        this.validaContato = false;
        this.validaEmail = false;
        this.validaTelefone = false;
    }

    public ConfiguracaoHomologacao(int id, Date homolocaoHabilitaCorrecao, int tempoRefreshAgendamento, int tempoRefreshRecepcao, int tempoRefreshHomologacao, int tempoRefreshAtendimento, int limiteMeses, Date limiteAgendamentoRetroativo, Date dataAtualizaHomologacao, boolean validaCpf, boolean validaNome, boolean validaEndereco, boolean validaCarteira, boolean validaSerie, boolean validaFuncao, boolean validaAdmissao, boolean validaDemissao, boolean validaContato, boolean validaEmail, boolean validaTelefone) {
        this.id = id;
        this.homolocaoHabilitaCorrecao = homolocaoHabilitaCorrecao;
        this.tempoRefreshAgendamento = tempoRefreshAgendamento;
        this.tempoRefreshRecepcao = tempoRefreshRecepcao;
        this.tempoRefreshHomologacao = tempoRefreshHomologacao;
        this.tempoRefreshAtendimento = tempoRefreshAtendimento;
        this.limiteMeses = limiteMeses;
        this.limiteAgendamentoRetroativo = limiteAgendamentoRetroativo;
        this.dataAtualizaHomologacao = dataAtualizaHomologacao;
        this.validaCpf = validaCpf;
        this.validaNome = validaNome;
        this.validaEndereco = validaEndereco;
        this.validaCarteira = validaCarteira;
        this.validaSerie = validaSerie;
        this.validaFuncao = validaFuncao;
        this.validaAdmissao = validaAdmissao;
        this.validaDemissao = validaDemissao;
        this.validaContato = validaContato;
        this.validaEmail = validaEmail;
        this.validaTelefone = validaTelefone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getHomolocaoHabilitaCorrecao() {
        return homolocaoHabilitaCorrecao;
    }

    public void setHomolocaoHabilitaCorrecao(Date homolocaoHabilitaCorrecao) {
        this.homolocaoHabilitaCorrecao = homolocaoHabilitaCorrecao;
    }

    public int getTempoRefreshAgendamento() {
        return tempoRefreshAgendamento;
    }

    public void setTempoRefreshAgendamento(int tempoRefreshAgendamento) {
        this.tempoRefreshAgendamento = tempoRefreshAgendamento;
    }

    public int getTempoRefreshRecepcao() {
        return tempoRefreshRecepcao;
    }

    public void setTempoRefreshRecepcao(int tempoRefreshRecepcao) {
        this.tempoRefreshRecepcao = tempoRefreshRecepcao;
    }

    public int getTempoRefreshHomologacao() {
        return tempoRefreshHomologacao;
    }

    public void setTempoRefreshHomologacao(int tempoRefreshHomologacao) {
        this.tempoRefreshHomologacao = tempoRefreshHomologacao;
    }

    public int getTempoRefreshAtendimento() {
        return tempoRefreshAtendimento;
    }

    public void setTempoRefreshAtendimento(int tempoRefreshAtendimento) {
        this.tempoRefreshAtendimento = tempoRefreshAtendimento;
    }

    public int getLimiteMeses() {
        return limiteMeses;
    }

    public void setLimiteMeses(int limiteMeses) {
        this.limiteMeses = limiteMeses;
    }

    public Date getLimiteAgendamentoRetroativo() {
        return limiteAgendamentoRetroativo;
    }

    public void setLimiteAgendamentoRetroativo(Date limiteAgendamentoRetroativo) {
        this.limiteAgendamentoRetroativo = limiteAgendamentoRetroativo;
    }

    public Date getDataAtualizaHomologacao() {
        return dataAtualizaHomologacao;
    }

    public void setDataAtualizaHomologacao(Date dataAtualizaHomologacao) {
        this.dataAtualizaHomologacao = dataAtualizaHomologacao;
    }

    public boolean isValidaCpf() {
        return validaCpf;
    }

    public void setValidaCpf(boolean validaCpf) {
        this.validaCpf = validaCpf;
    }

    public boolean isValidaNome() {
        return validaNome;
    }

    public void setValidaNome(boolean validaNome) {
        this.validaNome = validaNome;
    }

    public boolean isValidaEndereco() {
        return validaEndereco;
    }

    public void setValidaEndereco(boolean validaEndereco) {
        this.validaEndereco = validaEndereco;
    }

    public boolean isValidaCarteira() {
        return validaCarteira;
    }

    public void setValidaCarteira(boolean validaCarteira) {
        this.validaCarteira = validaCarteira;
    }

    public boolean isValidaSerie() {
        return validaSerie;
    }

    public void setValidaSerie(boolean validaSerie) {
        this.validaSerie = validaSerie;
    }

    public boolean isValidaFuncao() {
        return validaFuncao;
    }

    public void setValidaFuncao(boolean validaFuncao) {
        this.validaFuncao = validaFuncao;
    }

    public boolean isValidaAdmissao() {
        return validaAdmissao;
    }

    public void setValidaAdmissao(boolean validaAdmissao) {
        this.validaAdmissao = validaAdmissao;
    }

    public boolean isValidaDemissao() {
        return validaDemissao;
    }

    public void setValidaDemissao(boolean validaDemissao) {
        this.validaDemissao = validaDemissao;
    }

    public boolean isValidaContato() {
        return validaContato;
    }

    public void setValidaContato(boolean validaContato) {
        this.validaContato = validaContato;
    }

    public boolean isValidaEmail() {
        return validaEmail;
    }

    public void setValidaEmail(boolean validaEmail) {
        this.validaEmail = validaEmail;
    }

    public boolean isValidaTelefone() {
        return validaTelefone;
    }

    public void setValidaTelefone(boolean validaTelefone) {
        this.validaTelefone = validaTelefone;
    }

}
