package br.com.rtools.seguranca;

import br.com.rtools.financeiro.Servicos;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "seg_registro")
@NamedQuery(name = "Registro.pesquisaID", query = "select regi from Registro regi where regi.id=:pid")
public class Registro implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @JoinColumn(name = "id_filial", referencedColumnName = "id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER)
    private Juridica filial;
    @Column(name = "ds_tipo_empresa", length = 1, nullable = false)
    private String tipoEmpresa;
    @Column(name = "ds_email", length = 50)
    private String email;
    @Column(name = "ds_senha", length = 20)
    private String senha;
    @Column(name = "ds_smtp", length = 50)
    private String smtp;
    @Column(name = "bloquear_homologacao")
    private boolean bloquearHomologacao;
    @Column(name = "carteirinha_dependente")
    private boolean carteirinhaDependente;
    @Column(name = "meses_inadimplentes_impressao_boletos")
    private int mesesInadimplentes;
    @Column(name = "ds_tipo_entidade", length = 1)
    private String tipoEntidade;
    @Column(name = "baixa_vencimento")
    private boolean baixaVencimento;
    @Column(name = "is_bloqueia_atrasados_web")
    private boolean bloqueiaAtrasadosWeb;
    @Column(name = "ds_mensagem_bloqueio_boleto_web", length = 8000)
    private String mensagemBloqueioBoletoWeb;
    @Column(name = "ds_url_path", length = 50)
    private String urlPath;
    @Column(name = "ds_obs_ficha_social", length = 8000)
    private String fichaSocial;
    @Column(name = "meses_inadimplentes_agenda")
    private int mesesInadimplentesAgenda;
    @Column(name = "dias_bloqueia_atrasados_web")
    private int diasBloqueiaAtrasadosWeb;
    @Column(name = "is_email_autenticado")
    private boolean emailAutenticado;
    @Column(name = "is_senha_homologacao")
    private boolean senhaHomologacao;
    @Column(name = "ds_documento_homologacao", length = 8000)
    private String documentoHomologacao;
    @Column(name = "ds_forma_pagamento_homologacao", length = 8000)
    private String formaPagamentoHomologacao;
    @Column(name = "is_agendar_sem_horario_web")
    private boolean agendarSemHorarioWeb;
    @Column(name = "is_enviar_email_anexo")
    private boolean enviarEmailAnexo;
    @Temporal(TemporalType.DATE)
    @Column(name = "dt_atualiza_homologacao")
    private Date dataAtualizaHomologacao;
    @Column(name = "is_boleto_web")
    private boolean boletoWeb;
    @Column(name = "is_repis_web")
    private boolean repisWeb;
    @Column(name = "is_agendamento_web")
    private boolean agendamentoWeb;
    @Column(name = "nr_limite_envios_notificacao")
    private int limiteEnvios;
    @Column(name = "nr_intervalo_envios_notificacao")
    private int intervaloEnvios;
    @Column(name = "fin_dia_vencimento_cobranca")
    private int finDiaVencimentoCobranca;
    @Column(name = "dt_limite_agendamento_retroativo")
    @Temporal(TemporalType.DATE)
    private Date agendamentoRetroativo;
    @JoinColumn(name = "id_servico_cartao", referencedColumnName = "id", nullable = true)
    @ManyToOne(fetch = FetchType.EAGER)
    private Servicos servicos;
    @Column(name = "convite_dias_convidado")
    private int conviteDiasConvidado;
    @Column(name = "convite_qtde_convidado")
    private int conviteQuantidadeConvidado;
    @Column(name = "convite_dias_socio")
    private int conviteDiasSocio;
    @Column(name = "convite_qtde_socio")
    private int conviteQuantidadeSocio;
    @Column(name = "sis_email_resposta", length = 50)
    private String sisEmailResposta;
    @Column(name = "sis_email_porta")
    private int sisEmailPorta;
    @JoinColumn(name = "id_email_protocolo", referencedColumnName = "id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SisEmailProtocolo sisEmailProtocolo;
    @Column(name = "is_cobranca_carteirinha")
    private boolean cobrancaCarteirinha;
    @Column(name = "is_validade_barras")
    private boolean validadeBarras;
    @Column(name = "is_foto_cartao")
    private boolean fotoCartao;
    @Column(name = "hom_nr_limite_meses", columnDefinition = "integer default 3")
    private int homolocaoLimiteMeses;
    @Column(name = "sis_is_email_marketing", columnDefinition = "boolean default false")
    private boolean sisEmailMarketing;
    @Column(name = "hom_dt_habilita_correcao")
    @Temporal(TemporalType.DATE)
    private Date homolocaoHabilitaCorrecao;
    @Column(name = "sis_email_marketing_resposta", length = 50)
    private String sisEmailMarketingResposta;
    @Column(name = "is_acesso_web_documento", columnDefinition = "boolean default false")
    private boolean acessoWebDocumento;
    @Column(name = "rais_mensagem_email", length = 8000)
    private String raisMensagemEmail;
    @Column(name = "is_biometria", columnDefinition = "boolean default false")
    private boolean biometria;

    public Registro() {
        this.id = -1;
        this.filial = new Juridica();
        this.tipoEmpresa = "E";
        this.email = "";
        this.senha = "";
        this.smtp = "";
        this.bloquearHomologacao = false;
        this.carteirinhaDependente = false;
        this.mesesInadimplentes = 0;
        this.tipoEntidade = "S";
        this.baixaVencimento = true;
        this.bloqueiaAtrasadosWeb = true;
        this.mensagemBloqueioBoletoWeb = "";
        this.urlPath = "";
        this.fichaSocial = "";
        this.mesesInadimplentesAgenda = 0;
        this.diasBloqueiaAtrasadosWeb = 0;
        this.emailAutenticado = true;
        this.sisEmailResposta = "";
        this.senhaHomologacao = false;
        this.documentoHomologacao = "";
        this.formaPagamentoHomologacao = "";
        this.agendarSemHorarioWeb = false;
        this.enviarEmailAnexo = true;
        this.dataAtualizaHomologacao = new Date();
        this.boletoWeb = false;
        this.repisWeb = false;
        this.agendamentoWeb = false;
        this.limiteEnvios = 0;
        this.intervaloEnvios = 0;
        this.finDiaVencimentoCobranca = 10;
        this.agendamentoRetroativo = new Date();
//        this.financeiroDiaVencimento = 0;
        this.servicos = new Servicos();
        this.conviteDiasConvidado = 365;
        this.conviteQuantidadeConvidado = 1;
        this.conviteDiasSocio = 30;
        this.conviteQuantidadeSocio = 10;
        this.sisEmailPorta = 25;
        this.sisEmailProtocolo = new SisEmailProtocolo();
        this.cobrancaCarteirinha = false;
        this.validadeBarras = false;
        this.fotoCartao = false;
        this.homolocaoLimiteMeses = 3;
        this.sisEmailMarketing = false;
        this.homolocaoHabilitaCorrecao = new Date();
        this.sisEmailMarketingResposta = "";
        this.acessoWebDocumento = false;
        this.raisMensagemEmail = "";
        this.biometria = false;
    }

    public Registro(int id,
            Juridica filial,
            String tipoEmpresa,
            String email,
            String senha,
            String smtp,
            boolean bloquearHomologacao,
            boolean carteirinhaDependente,
            int mesesInadimplentes,
            String tipoEntidade,
            boolean baixaVencimento,
            boolean bloqueiaAtrasadosWeb,
            String mensagemBloqueioBoletoWeb,
            String urlPath,
            String fichaSocial,
            int mesesInadimplentesAgenda,
            int diasBloqueiaAtrasadosWeb,
            boolean emailAutenticado,
            boolean senhaHomologação,
            String documentoHomologacao,
            String formaPagamentoHomologacao,
            boolean agendarSemHorarioWeb,
            boolean enviarEmailAnexo,
            String dataAtualizaHomologacao,
            boolean boletoWeb,
            boolean repisWeb,
            boolean agendamentoWeb,
            int limiteEnvios,
            int intervaloEnvios,
            int finDiaVencimentoCobranca,
            String agendamentoRetroativo,
            Servicos servicos,
            int conviteDiasConvidado,
            int conviteQuantidadeConvidado,
            int conviteDiasSocio,
            int conviteQuantidadeSocio,
            String sisEmailResposta,
            int sisEmailPorta,
            SisEmailProtocolo sisEmailProtocolo,
            boolean cobrancaCarteirinha,
            boolean validadeBarras,
            boolean fotoCartao,
            int homolocaoLimiteMeses,
            Date homolocaoHabilitaCorrecao,
            boolean sisEmailMarketing,
            String sisEmailMarketingResposta,
            boolean acessoWebDocumento,
            String raisMensagemEmail,
            boolean cadastroCnpj,
            boolean biometria) {

        this.id = id;
        this.filial = filial;
        this.tipoEmpresa = tipoEmpresa;
        this.email = email;
        this.senha = senha;
        this.smtp = smtp;
        this.bloquearHomologacao = bloquearHomologacao;
        this.carteirinhaDependente = carteirinhaDependente;
        this.mesesInadimplentes = mesesInadimplentes;
        this.tipoEntidade = tipoEntidade;
        this.baixaVencimento = baixaVencimento;
        this.bloqueiaAtrasadosWeb = bloqueiaAtrasadosWeb;
        this.mensagemBloqueioBoletoWeb = mensagemBloqueioBoletoWeb;
        this.urlPath = urlPath;
        this.fichaSocial = fichaSocial;
        this.mesesInadimplentesAgenda = mesesInadimplentesAgenda;
        this.diasBloqueiaAtrasadosWeb = diasBloqueiaAtrasadosWeb;
        this.emailAutenticado = emailAutenticado;
        this.senhaHomologacao = senhaHomologação;
        this.documentoHomologacao = documentoHomologacao;
        this.formaPagamentoHomologacao = formaPagamentoHomologacao;
        this.agendarSemHorarioWeb = agendarSemHorarioWeb;
        this.enviarEmailAnexo = enviarEmailAnexo;
        setDataAtualizaHomologacaoStr(dataAtualizaHomologacao);
        this.boletoWeb = boletoWeb;
        this.repisWeb = repisWeb;
        this.agendamentoWeb = agendamentoWeb;
        this.limiteEnvios = limiteEnvios;
        this.intervaloEnvios = intervaloEnvios;
        this.finDiaVencimentoCobranca = finDiaVencimentoCobranca;
        this.agendamentoRetroativo = DataHoje.converte(agendamentoRetroativo);
        this.servicos = new Servicos();
        this.conviteDiasConvidado = conviteDiasConvidado;
        this.conviteQuantidadeConvidado = conviteQuantidadeConvidado;
        this.conviteDiasSocio = conviteDiasSocio;
        this.conviteQuantidadeSocio = conviteQuantidadeSocio;
        this.sisEmailResposta = sisEmailResposta;
        this.sisEmailPorta = sisEmailPorta;
        this.sisEmailProtocolo = sisEmailProtocolo;
        this.cobrancaCarteirinha = cobrancaCarteirinha;
        this.validadeBarras = validadeBarras;
        this.fotoCartao = fotoCartao;
        this.homolocaoLimiteMeses = homolocaoLimiteMeses;
        this.sisEmailMarketing = sisEmailMarketing;
        this.homolocaoHabilitaCorrecao = homolocaoHabilitaCorrecao;
        this.sisEmailMarketingResposta = sisEmailMarketingResposta;
        this.acessoWebDocumento = acessoWebDocumento;
        this.raisMensagemEmail = raisMensagemEmail;
        this.biometria = biometria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Juridica getFilial() {
        return filial;
    }

    public void setFilial(Juridica filial) {
        this.filial = filial;
    }

    public String getTipoEmpresa() {
        return tipoEmpresa;
    }

    public void setTipoEmpresa(String tipoEmpresa) {
        this.tipoEmpresa = tipoEmpresa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSmtp() {
        return smtp;
    }

    public void setSmtp(String smtp) {
        this.smtp = smtp;
    }

    public boolean isBloquearHomologacao() {
        return bloquearHomologacao;
    }

    public void setBloquearHomologacao(boolean bloquearHomologacao) {
        this.bloquearHomologacao = bloquearHomologacao;
    }

    public boolean isCarteirinhaDependente() {
        return carteirinhaDependente;
    }

    public void setCarteirinhaDependente(boolean carteirinhaDependente) {
        this.carteirinhaDependente = carteirinhaDependente;
    }

    public int getMesesInadimplentes() {
        return mesesInadimplentes;
    }

    public void setMesesInadimplentes(int mesesInadimplentes) {
        this.mesesInadimplentes = mesesInadimplentes;
    }

    public String getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(String tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

    public boolean isBaixaVencimento() {
        return baixaVencimento;
    }

    public void setBaixaVencimento(boolean baixaVencimento) {
        this.baixaVencimento = baixaVencimento;
    }

    public String getMensagemBloqueioBoletoWeb() {
        return mensagemBloqueioBoletoWeb;
    }

    public void setMensagemBloqueioBoletoWeb(String mensagemBloqueioBoletoWeb) {
        this.mensagemBloqueioBoletoWeb = mensagemBloqueioBoletoWeb;
    }

    public boolean isBloqueiaAtrasadosWeb() {
        return bloqueiaAtrasadosWeb;
    }

    public void setBloqueiaAtrasadosWeb(boolean bloqueiaAtrasadosWeb) {
        this.bloqueiaAtrasadosWeb = bloqueiaAtrasadosWeb;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public int getMesesInadimplentesAgenda() {
        return mesesInadimplentesAgenda;
    }

    public void setMesesInadimplentesAgenda(int mesesInadimplentesAgenda) {
        this.mesesInadimplentesAgenda = mesesInadimplentesAgenda;
    }

    public int getDiasBloqueiaAtrasadosWeb() {
        return diasBloqueiaAtrasadosWeb;
    }

    public void setDiasBloqueiaAtrasadosWeb(int diasBloqueiaAtrasadosWeb) {
        this.diasBloqueiaAtrasadosWeb = diasBloqueiaAtrasadosWeb;
    }

    public boolean isEmailAutenticado() {
        return emailAutenticado;
    }

    public void setEmailAutenticado(boolean emailAutenticado) {
        this.emailAutenticado = emailAutenticado;
    }

    public boolean isSenhaHomologacao() {
        return senhaHomologacao;
    }

    public void setSenhaHomologacao(boolean senhaHomologacao) {
        this.senhaHomologacao = senhaHomologacao;
    }

    public String getDocumentoHomologacao() {
        return documentoHomologacao;
    }

    public void setDocumentoHomologacao(String documentoHomologacao) {
        this.documentoHomologacao = documentoHomologacao;
    }

    public String getFormaPagamentoHomologacao() {
        return formaPagamentoHomologacao;
    }

    public void setFormaPagamentoHomologacao(String formaPagamentoHomologacao) {
        this.formaPagamentoHomologacao = formaPagamentoHomologacao;
    }

    public boolean isAgendarSemHorarioWeb() {
        return agendarSemHorarioWeb;
    }

    public void setAgendarSemHorarioWeb(boolean agendarSemHorarioWeb) {
        this.agendarSemHorarioWeb = agendarSemHorarioWeb;
    }

    public boolean isEnviarEmailAnexo() {
        return enviarEmailAnexo;
    }

    public void setEnviarEmailAnexo(boolean enviarEmailAnexo) {
        this.enviarEmailAnexo = enviarEmailAnexo;
    }

    public String getFichaSocial() {
        return fichaSocial;
    }

    public void setFichaSocial(String fichaSocial) {
        this.fichaSocial = fichaSocial;
    }

    public Date getDataAtualizaHomologacao() {
        return dataAtualizaHomologacao;
    }

    public void setDataAtualizaHomologacao(Date dataAtualizaHomologacao) {
        this.dataAtualizaHomologacao = dataAtualizaHomologacao;
    }

    public String getDataAtualizaHomologacaoStr() {
        if (dataAtualizaHomologacao != null) {
            return DataHoje.converteData(dataAtualizaHomologacao);
        } else {
            return "";
        }
    }

    public void setDataAtualizaHomologacaoStr(String data) {
        if (!(data.isEmpty())) {
            this.dataAtualizaHomologacao = DataHoje.converte(data);
        }
    }

    public boolean isBoletoWeb() {
        return boletoWeb;
    }

    public void setBoletoWeb(boolean boletoWeb) {
        this.boletoWeb = boletoWeb;
    }

    public boolean isRepisWeb() {
        return repisWeb;
    }

    public void setRepisWeb(boolean repisWeb) {
        this.repisWeb = repisWeb;
    }

    public boolean isAgendamentoWeb() {
        return agendamentoWeb;
    }

    public void setAgendamentoWeb(boolean agendamentoWeb) {
        this.agendamentoWeb = agendamentoWeb;
    }

    public int getLimiteEnvios() {
        return limiteEnvios;
    }

    public void setLimiteEnvios(int limiteEnvios) {
        this.limiteEnvios = limiteEnvios;
    }

    public int getIntervaloEnvios() {
        return intervaloEnvios;
    }

    public void setIntervaloEnvios(int intervaloEnvios) {
        this.intervaloEnvios = intervaloEnvios;
    }

    public Date getAgendamentoRetroativo() {
        return agendamentoRetroativo;
    }

    public void setAgendamentoRetroativo(Date agendamentoRetroativo) {
        this.agendamentoRetroativo = agendamentoRetroativo;
    }

    public String getAgendamentoRetroativoString() {
        return DataHoje.converteData(agendamentoRetroativo);
    }

    public void setAgendamentoRetroativoString(String agendamentoRetroativoString) {
        this.agendamentoRetroativo = DataHoje.converte(agendamentoRetroativoString);
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public int getConviteDiasConvidado() {
        return conviteDiasConvidado;
    }

    public void setConviteDiasConvidado(int conviteDiasConvidado) {
        this.conviteDiasConvidado = conviteDiasConvidado;
    }

    public int getConviteQuantidadeConvidado() {
        return conviteQuantidadeConvidado;
    }

    public void setConviteQuantidadeConvidado(int conviteQuantidadeConvidado) {
        this.conviteQuantidadeConvidado = conviteQuantidadeConvidado;
    }

    public int getConviteDiasSocio() {
        return conviteDiasSocio;
    }

    public void setConviteDiasSocio(int conviteDiasSocio) {
        this.conviteDiasSocio = conviteDiasSocio;
    }

    public int getConviteQuantidadeSocio() {
        return conviteQuantidadeSocio;
    }

    public void setConviteQuantidadeSocio(int conviteQuantidadeSocio) {
        this.conviteQuantidadeSocio = conviteQuantidadeSocio;
    }

    public int getFinDiaVencimentoCobranca() {
        return finDiaVencimentoCobranca;
    }

    public void setFinDiaVencimentoCobranca(int finDiaVencimentoCobranca) {
        this.finDiaVencimentoCobranca = finDiaVencimentoCobranca;
    }

    public String getSisEmailResposta() {
        return sisEmailResposta;
    }

    public void setSisEmailResposta(String sisEmailResposta) {
        this.sisEmailResposta = sisEmailResposta;
    }

    public Registro getRegistroEmpresarial() {
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        Registro r = (Registro) sadb.find(new Registro(), 1);
        return r;
    }

    public int getSisEmailPorta() {
        return sisEmailPorta;
    }

    public void setSisEmailPorta(int sisEmailPorta) {
        this.sisEmailPorta = sisEmailPorta;
    }

    public SisEmailProtocolo getSisEmailProtocolo() {
        return sisEmailProtocolo;
    }

    public void setSisEmailProtocolo(SisEmailProtocolo sisEmailProtocolo) {
        this.sisEmailProtocolo = sisEmailProtocolo;
    }

    public boolean isCobrancaCarteirinha() {
        return cobrancaCarteirinha;
    }

    public void setCobrancaCarteirinha(boolean cobrancaCarteirinha) {
        this.cobrancaCarteirinha = cobrancaCarteirinha;
    }

    public boolean isValidadeBarras() {
        return validadeBarras;
    }

    public void setValidadeBarras(boolean validadeBarras) {
        this.validadeBarras = validadeBarras;
    }

    public boolean isFotoCartao() {
        return fotoCartao;
    }

    public void setFotoCartao(boolean fotoCartao) {
        this.fotoCartao = fotoCartao;
    }

    public int getHomolocaoLimiteMeses() {
        return homolocaoLimiteMeses;
    }

    public void setHomolocaoLimiteMeses(int homolocaoLimiteMeses) {
        this.homolocaoLimiteMeses = homolocaoLimiteMeses;
    }

    public boolean isSisEmailMarketing() {
        return sisEmailMarketing;
    }

    public void setSisEmailMarketing(boolean sisEmailMarketing) {
        this.sisEmailMarketing = sisEmailMarketing;
    }

    public Date getHomolocaoHabilitaCorrecao() {
        return homolocaoHabilitaCorrecao;
    }

    public void setHomolocaoHabilitaCorrecao(Date homolocaoHabilitaCorrecao) {
        this.homolocaoHabilitaCorrecao = homolocaoHabilitaCorrecao;
    }

    public String getSisEmailMarketingResposta() {
        return sisEmailMarketingResposta;
    }

    public void setSisEmailMarketingResposta(String sisEmailMarketingResposta) {
        this.sisEmailMarketingResposta = sisEmailMarketingResposta;
    }

    public boolean isAcessoWebDocumento() {
        return acessoWebDocumento;
    }

    public void setAcessoWebDocumento(boolean acessoWebDocumento) {
        this.acessoWebDocumento = acessoWebDocumento;
    }

    public String getRaisMensagemEmail() {
        return raisMensagemEmail;
    }

    public void setRaisMensagemEmail(String raisMensagemEmail) {
        this.raisMensagemEmail = raisMensagemEmail;
    }

    public boolean isBiometria() {
        return biometria;
    }

    public void setBiometria(boolean biometria) {
        this.biometria = biometria;
    }
}
