package br.com.rtools.seguranca.controleUsuario;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.sistema.ContadorAcessos;
import br.com.rtools.sistema.db.AtalhoDB;
import br.com.rtools.sistema.db.AtalhoDBToplink;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class chamadaPaginaJSFBean implements java.io.Serializable {

    private HttpServletRequest paginaRequerida = null;
    private boolean carregaPg = true;
    private boolean linkClicado = false;
    private String urlAtual = "";
    private String paginaDestino;
    private String oqFazer;
    private DataObject dtObject = new DataObject(null, null, null, null, null, null);
    private DataObject dtObjectLabel = new DataObject(null, null, null, null, null, null);
    private int nivelLink = 0;
    private int fimURL;
    private int iniURL;
    private int tipoPagina = 0;
    private final int FINANCEIRO = 1;
    private final int SOCIAL = 2;
    private final int ARRECADACAO = 3;
    private final int HOMOLOGACAO = 4;
    private final int JURIDICO = 5;
    private final int CLUBE = 6;
    private final int ACADEMIA = 7;
    private final int ESCOLA = 8;
    private final int CADASTRO_AUXILIAR = 9;
    private final int SEGURANCA = 10;
    private final int LOCADORA = 11;
    private final int ATENDIMENTO = 12;
    private boolean render1 = false;
    private boolean render2 = false;
    private boolean render3 = false;
    private boolean render4 = false;
    private boolean render5 = false;
    private boolean render6 = false;
    private boolean render7 = false;
    private boolean render8 = false;
    private boolean render9 = false;
    private boolean renderPesquisa = true;
    private List<Rotina> listaRotina = new ArrayList();

    public void atualizaAcessos(String url) {
        RotinaDB db = new RotinaDBToplink();
        AtalhoDB dba = new AtalhoDBToplink();
        Rotina rotina = db.pesquisaAcesso(url);
        Usuario usuario = new Usuario();
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario") != null) {
            usuario = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        }
        if (rotina.getId() != -1) {
            sv.abrirTransacao();
            ContadorAcessos cont = dba.pesquisaContadorAcessos(usuario.getId(), rotina.getId());
            if (cont == null) {
                cont = new ContadorAcessos();
                cont.setRotina(rotina);
                cont.setUsuario(usuario);
                cont.setAcessos(cont.getAcessos() + 1);
                if (sv.inserirObjeto(cont)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            } else {
                cont.setAcessos(cont.getAcessos() + 1);
                if (sv.alterarObjeto(cont)) {
                    sv.comitarTransacao();
                } else {
                    sv.desfazerTransacao();
                }
            }

        }
    }

    // CHAMADA DE PAGINAS GENERICAS-------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    public synchronized Object chamadaGenerica(String pagina) {
        pagina = converteURL(pagina);
        Object object = null;
        try {
            object = this.getClass().getMethod(pagina).invoke(this);
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return object;
    }

    public String metodoGenerico(int tipo, String pagina) {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlAtual = converteURL(paginaRequerida.getRequestURI());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", urlAtual);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        carregaPg = true;
        tipoPagina = tipo;
        atualizaAcessos('"' + "/Sindical/" + pagina + ".jsf" + '"');
        return pagina;
    }

    public synchronized String pessoaJuridica() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("juridicaBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaContabil");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
        //}
        return metodoGenerico(2, "pessoaJuridica");
    }

    public synchronized String pessoaJuridicaComParametros() {
        return metodoGenerico(2, "pessoaJuridica");
    }

    public synchronized String pessoaFisica() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("fisicaBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoNum");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoComp");
        //}
        return metodoGenerico(2, "pessoaFisica");
    }

    public synchronized String pessoaFisicaComParametros() {
        return metodoGenerico(2, "pessoaFisica");
    }

    public synchronized String usuario() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("usuarioBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("usuarioBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        //}
        return metodoGenerico(2, "usuario");
    }

    public synchronized String permissao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("permissaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("permissaoBean");
        //}
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        return metodoGenerico(2, "permissao");
    }

    public synchronized String rotina() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("rotinaBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("rotinaBean");
        //}
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        return metodoGenerico(2, "rotina");
    }

    public synchronized String endereco() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("enderecoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bairroPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("logradouroPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descricaoEnderecoPesquisa");
        //}
        if (urlAtual.equals("pesquisaEndereco")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", true);
        }
        return metodoGenerico(2, "endereco");
    }

    public synchronized String cidade() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("cidadeBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadeBean");
        //}
        return metodoGenerico(2, "cidade");
    }

    public synchronized String cnae() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("cnaeBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaeBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaePesquisado");
        //}
        return metodoGenerico(2, "cnae");
    }

    public synchronized String profissao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("profissaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("profissaoBean");
        //}
        return metodoGenerico(2, "profissao");
    }

    public synchronized String cnaeConvencao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("cnaeConvencaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cnaeConvencaoBean");
        //}
        return metodoGenerico(2, "cnaeConvencao");
    }

    public synchronized String grupoCidades() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("grupoCidadesBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoCidadesBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoCidadesPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoCidadePesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        //}
        return metodoGenerico(2, "grupoCidades");
    }

    public synchronized String mensagem() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("mensagemConvencaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("mensagemConvencaoBean");
        //}
        return metodoGenerico(2, "mensagem");
    }

    public synchronized String descontoEmpregado() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("descontoEmpregadoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descontoEmpregadoBean");
        //}
        return metodoGenerico(2, "descontoEmpregado");
    }

    public synchronized String extratoTela() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("extratoTelaBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("extratoTelaBean");
        //}
        if (!urlAtual.equals("pessoaJuridica") && !urlAtual.equals("agendamento")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        }
        return metodoGenerico(2, "extratoTela");
    }

    public synchronized String plano() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("planoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("planoBean");
        //}
        return metodoGenerico(2, "plano");
    }

    public synchronized String servicos() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("servicosBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicosBean");
        //}
        return metodoGenerico(2, "servicos");
    }

    public synchronized String contaBanco() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaBancoBean");
        return metodoGenerico(2, "contaBanco");
    }

    public synchronized String contaCobranca() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaCobrancaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaCobrancaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaBancoPesquisa");
        return metodoGenerico(2, "contaCobranca");
    }

    public synchronized String servicoContaCobranca() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("servicoContaCobrancaBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicoContaCobrancaBean");
        //}
        return metodoGenerico(2, "servicoContaCobranca");
    }

    public synchronized String webContribuinte() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webContribuintesBean");
        return metodoGenerico(2, "webContribuinte");
    }

    public String webContabilidade() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webContabilidadeBean");
        return metodoGenerico(2, "webContabilidade");
    }

    public synchronized String webConfiguracoes() {
        return metodoGenerico(2, "webConfiguracoes");
    }

    public synchronized String movimentosReceber() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("movimentosReceberBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("movimentosReceberBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        //}
        return metodoGenerico(2, "movimentosReceber");
    }

    public synchronized String acordo() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("acordoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("acordoBean");
        //}
        return metodoGenerico(2, "acordo");
    }

    public synchronized String baixaGeral() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("baixaGeralBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baixaGeralBean");
        //}
        if (urlAtual.equals("baixaBoleto")) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", true);
        }
        return metodoGenerico(2, "baixaGeral");
    }

    public synchronized String processamentoIndividual() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("processamentoIndividualBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("processamentoIndividualBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        //}
        return metodoGenerico(2, "processamentoIndividual");
    }

    public synchronized String contribuicao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("contribuicaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contribuicaoBean");
        //}
        return metodoGenerico(2, "contribuicao");
    }

    public synchronized String impressaoBoletos() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("impressaoBoletosBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("impressaoBoletosBean");
        //}
        return metodoGenerico(2, "impressaoBoletos");
    }

    public synchronized String arquivoBanco() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("arquivoBancoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("arquivoBancoBean");
        //}
        return metodoGenerico(2, "arquivoBanco");
    }

    public synchronized String indiceMensal() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("indiceMensalBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("indiceMensalBean");
        //}
        return metodoGenerico(2, "indiceMensal");
    }

    public synchronized String correcao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("correcaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("correcaoBean");
        //}
        return metodoGenerico(2, "correcao");
    }

    public synchronized String permissaoDepartamento() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("permissaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("permissaoBean");
        //}
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        return metodoGenerico(2, "permissaoDepartamento");
    }

    public synchronized String retornoBanco() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("arquivoBancoBean");
        return metodoGenerico(2, "retornoBanco");
    }

    public synchronized String retornoPadrao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("retornoPadraoBean");
        return metodoGenerico(2, "retornoPadrao");
    }

    public synchronized String registroEmpresarial() {
        return metodoGenerico(2, "registroEmpresarial");
    }

    public synchronized String enviarEmail() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaBean");
        return metodoGenerico(2, "enviarEmail");
    }

    public synchronized String agendamento() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("agendamentoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("agendamentoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        //}
        return metodoGenerico(2, "agendamento");
    }

    public synchronized String feriados() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("feriadosBean");
        return metodoGenerico(2, "feriados");
    }

    public synchronized String horarios() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("horariosBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("horariosBean");
        //}
        return metodoGenerico(2, "horarios");
    }

    public synchronized String homologacao() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("homologacaoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("homologacaoBean");
        //}
        return metodoGenerico(2, "homologacao");
    }

    public synchronized String webAgendamentoContribuinte() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("webAgendamentoContribuinteBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webAgendamentoContribuinteBean");
        //}
        return metodoGenerico(2, "webAgendamentoContribuinte");
    }

    public synchronized String webAgendamentoContabilidade() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("webAgendamentoContabilidadeBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webAgendamentoContabilidadeBean");
        //}
        return metodoGenerico(2, "webAgendamentoContabilidade");
    }

    public synchronized String subGrupoConvenio() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("subGrupoConvenioBean");
        return metodoGenerico(2, "subGrupoConvenio");
    }

    public synchronized String convenio() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convenioBean");
        return metodoGenerico(2, "convenio");
    }

    public synchronized String parentesco() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("parentescoBean");
        return metodoGenerico(2, "parentesco");
    }

    public synchronized String suspencao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("suspencaoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return metodoGenerico(2, "suspencao");
    }

    public synchronized String grupoCategoria() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("grupoCategoriaBean");
        return metodoGenerico(2, "grupoCategoria");
    }

    public synchronized String categoria() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("categoriaBean");
        return metodoGenerico(2, "categoria");
    }

    public synchronized String socios() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("sociosBean");
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        //FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        return metodoGenerico(2, "socios");
    }

    public synchronized String contribuicaoSocio() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contribuicaoSocioBean");
        return metodoGenerico(2, "contribuicaoSocio");
    }

    public synchronized String contribuicaoPorSocio() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contribuicaoPorSocioBean");
        return metodoGenerico(2, "contribuicaoPorSocio");
    }

    public synchronized String convenioMedico() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convenioMedicoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicoPessoaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        return metodoGenerico(2, "convenioMedico");
    }

    public synchronized String academia() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("academiaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicoPessoaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        return metodoGenerico(2, "academia");
    }

    public synchronized String servicoRotina() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("servicoRotinaBean");
        return metodoGenerico(2, "servicoRotina");
    }

    public synchronized String lancamentoIndividual() {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        return metodoGenerico(2, "lancamentoIndividual");
    }

    public synchronized String emissaoGuias() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("emissaoGuiasBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return metodoGenerico(2, "emissaoGuias");
    }

    public synchronized String filial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("filialBean");
        return metodoGenerico(2, "filial");
    }

    public synchronized String emissaoCarteirinha() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("socioCarteirinhaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("socioPesquisa");
        return metodoGenerico(2, "emissaoCarteirinha");
    }

    public synchronized String baixaBoleto() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baixaBoletoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baixaBoleto:caixaBanco");
        return metodoGenerico(2, "baixaBoleto");
    }

    public synchronized String caravana() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("caravanaBean");
        return metodoGenerico(2, "caravana");
    }

    public synchronized String vendasCaravana() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendasCaravanaBean");
        return metodoGenerico(2, "vendasCaravana");
    }

    public synchronized String descricaoEvento() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descricaoEventoBean");
        return metodoGenerico(2, "descricaoEvento");
    }

    public synchronized String baile() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baileBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        return metodoGenerico(2, "baile");
    }

    public synchronized String vendasBaile() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendaBaileBean");
        return metodoGenerico(2, "vendasBaile");
    }

    public synchronized String vendedor() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("vendedorBean");
        return metodoGenerico(2, "vendedor");
    }

    public synchronized String eFinanceiro() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eFinanceiroBean");
        return metodoGenerico(2, "eFinanceiro");
    }

    public synchronized String professor() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("professorBean");
        return metodoGenerico(2, "professor");
    }

    public synchronized String turma() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("turmaBean");
        return metodoGenerico(2, "turma");
    }

    public synchronized String contrato() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contratoBean");
        return metodoGenerico(2, "contrato");
    }

    public synchronized String matriculaEscola() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("matriculaEscolaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaFisicaTipo");
        return metodoGenerico(2, "matriculaEscola");
    }

    public synchronized String agendaTelefone() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoAgendaTelefone", "agendaTelefone");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("agendaTelefoneBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("agendaTelefonePesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        return metodoGenerico(2, "agendaTelefone");
    }

    public synchronized String cancelarHorario() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cancelarHorarioBean");
        return metodoGenerico(2, "cancelarHorario");
    }

    public synchronized String titulo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("tituloBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return metodoGenerico(2, "titulo");
    }

    public synchronized String macFilial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("macFilialBean");
        return metodoGenerico(2, "macFilial");
    }

    public synchronized String agendaTarefa() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("agendaTarefaBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        return metodoGenerico(2, "agendaTarefa");
    }

    public synchronized String interrupcao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("interrupcaoBean");
        return metodoGenerico(2, "interrupcao");
    }

    public synchronized String centroComercial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("centroComercialBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        return metodoGenerico(2, "centroComercial");
    }

    public synchronized String catalogoFilme() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("catalogoBean");
        return metodoGenerico(2, "catalogoFilme");
    }

    public synchronized String convencao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convencaoBean");
        return metodoGenerico(2, "convencao");
    }

    public synchronized String enviarArquivosContabilidade() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enviarArquivosBean");
        return metodoGenerico(2, "enviarArquivosContabilidade");
    }

    public synchronized String enviarArquivosContribuinte() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enviarArquivosBean");
        return metodoGenerico(2, "enviarArquivosContribuinte");
    }

    public synchronized String oposicao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("oposicaoBean");
        return metodoGenerico(2, "oposicao");
    }

    public synchronized String webSolicitaREPIS() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webREPISBean");
        return metodoGenerico(2, "webSolicitaREPIS");
    }

    public synchronized String webLiberacaoREPIS() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("webREPISBean");
        return metodoGenerico(2, "webLiberacaoREPIS");
    }

    public synchronized String registroPatronal() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("registroPatronalBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convencaoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesPesquisa");
        return metodoGenerico(2, "registroPatronal");
    }

    public synchronized String pisoSalarial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pisoSalarialBean");
        return metodoGenerico(2, "pisoSalarial");
    }

    public synchronized String atendimento() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("atendimentoBean");
        return metodoGenerico(2, "atendimento");
    }

    public synchronized String convencaoPeriodo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("convencaoPeriodoBean");
        return metodoGenerico(2, "convencaoPeriodo");
    }

    public synchronized String relatorio() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioBean");
        return metodoGenerico(2, "relatorio");
    }

    public synchronized String notificacao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("notificacaoBean");
        return metodoGenerico(2, "notificacao");
    }

    public synchronized String recepcao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("recepcaoBean");
        return metodoGenerico(2, "recepcao");
    }

    public synchronized String matriculaContrato() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("matriculaContratoBean");
        return metodoGenerico(2, "matriculaContrato");
    }

    public synchronized String bloqueioServicos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bloqueioServicosBean");
        return metodoGenerico(2, "bloqueioServicos");
    }

    public synchronized String configuracao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("configuracaoBean");
        return metodoGenerico(2, "configuracao");
    }

    public synchronized String matriculaContratoCampos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("matriculaContratoBean");
        return metodoGenerico(2, "matriculaContratoCampos");
    }

    // CADASTROS SIMPLES ----------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------
    // ----------------------------------------------------------------------------------------------------------------
    public synchronized String simples() {
        return metodoGenerico(2, "simples");
    }

    public synchronized String logradouro() {
        String[] lista = new String[]{"Logradouro", "Logradouro"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String descricaoEndereco() {
        String[] lista = new String[]{"DescricaoEndereco", "Descrição do Endereço"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String bairro() {
        String[] lista = new String[]{"Bairro", "Bairro"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String tipoEndereco() {
        String[] lista = new String[]{"TipoEndereco", "Tipo de Endereço"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String tipoDocumento() {
        String[] lista = new String[]{"TipoDocumento", "Tipo de Documento"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String grupoAgenda() {
        String[] lista = new String[]{"GrupoAgenda", "Grupo Agenda"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String evento() {
        String[] lista = new String[]{"Evento", "Evento"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("eventoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String modulo() {
        String[] lista = new String[]{"Modulo", "Modulo"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("moduloBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String departamento() {
        String[] lista = new String[]{"Departamento", "Departamento"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("departamentoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SEGURANCA);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String grupoCidade() {
        String[] lista = new String[]{"GrupoCidade", "Grupo Cidade"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String genero() {
        String[] lista = new String[]{"Genero", "Gênero"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pessoaPesquisa");
        return metodoGenerico(2, "simples");
    }

    public synchronized String midia() {
        String[] lista = new String[]{"Midia", "Mídia"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String tipoCentroComercial() {
        String[] lista = new String[]{"TipoCentroComercial", "Tipo Centro Comercial"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String grupoConvenio() {
        String[] lista = new String[]{"GrupoConvenio", "Grupo Convênio"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String componenteCurricular() {
        String[] lista = new String[]{"ComponenteCurricular", "Componente Curricular"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String indice() {
        String[] lista = new String[]{"Indice", "Indice"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String grupoEvento() {
        String[] lista = new String[]{"GrupoEvento", "Grupo Evento"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String bandas() {
        String[] lista = new String[]{"Banda", "Cadastro Bandas"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String nivel() {
        String[] lista = new String[]{"Nivel", "Cadastro Níveis"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String motivoInativacao() {
        String[] lista = new String[]{"MotivoInativacao", "Motivo de Inativação"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String tipoServico() {
        String[] lista = new String[]{"TipoServico", "Tipo Serviço"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    public synchronized String atendimentoOperacao() {
        String[] lista = new String[]{"AteOperacao", "Atendimento Operação"};
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("cadastroSimples", lista);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("simplesBean");
        return metodoGenerico(2, "simples");
    }

    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    // CHAMADA DE PESQUISAS --------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    public synchronized String pesquisaPessoaFisica() {
        return metodoGenerico(1, "pesquisaPessoaFisica");
    }

    public synchronized String pesquisaPessoaJuridica() {
        return metodoGenerico(1, "pesquisaPessoaJuridica");
    }

    public synchronized String pesquisaRelatorios() {
        return metodoGenerico(1, "pesquisaRelatorios");
    }

    public synchronized String pesquisaPessoa() {
        return metodoGenerico(1, "pesquisaPessoa");
    }

    public synchronized String pesquisaUsuario() {
        return metodoGenerico(1, "pesquisaUsuario");
    }

    public synchronized String pesquisaCnae() {
        return metodoGenerico(1, "pesquisaCnae");
    }

    public synchronized String pesquisaContabilidade() {
        return metodoGenerico(1, "pesquisaContabilidade");
    }

    public synchronized String pesquisaPermissao() {
        return metodoGenerico(1, "pesquisaPermissao");
    }

    public synchronized String pesquisaRotina() {
        return metodoGenerico(1, "pesquisaRotina");
    }

    public synchronized String pesquisaConfiguracao() {
        return metodoGenerico(1, "pesquisaConfiguracao");
    }

    public synchronized String pesquisaEndereco() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("enderecoBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cidadePesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("bairroPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("logradouroPesquisa");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descricaoEnderecoPesquisa");
        //}
        return metodoGenerico(1, "pesquisaEndereco");
    }

    public synchronized String pesquisaCidade() {
        return metodoGenerico(1, "pesquisaCidade");
    }

    public synchronized String pesquisaProfissao() {
        return metodoGenerico(1, "pesquisaProfissao");
    }

    public synchronized String pesquisaPlano() {
        return metodoGenerico(1, "pesquisaPlano");
    }

    public synchronized String pesquisaServicos() {
        return metodoGenerico(1, "pesquisaServicos");
    }

    public synchronized String pesquisaContaBanco() {
        return metodoGenerico(1, "pesquisaContaBanco");
    }

    public synchronized String pesquisaContaCobranca() {
        return metodoGenerico(1, "pesquisaContaCobranca");
    }

    public synchronized String pesquisaIndice() {
        return metodoGenerico(1, "pesquisaIndice");
    }

    public synchronized String pesquisaSubGrupoConvenio() {
        return metodoGenerico(1, "pesquisaSubGrupoConvenio");
    }

    public synchronized String pesquisaParentesco() {
        return metodoGenerico(1, "pesquisaParentesco");
    }

    public synchronized String pesquisaSuspencao() {
        return metodoGenerico(1, "pesquisaSuspencao");
    }

    public synchronized String pesquisaCategoria() {
        return metodoGenerico(1, "pesquisaCategoria");
    }

    public synchronized String pesquisaSocios() {
        return metodoGenerico(1, "pesquisaSocios");
    }

    public synchronized String pesquisaAcademia() {
        return metodoGenerico(1, "pesquisaAcademia");
    }

    public synchronized String pesquisaConvenioMedico() {
        return metodoGenerico(1, "pesquisaConvenioMedico");
    }

    public synchronized String pesquisaJuridicaConvenio() {
        return metodoGenerico(1, "pesquisaJuridicaConvenio");
    }

    public synchronized String pesquisaGuiasEmitidas() {
        return metodoGenerico(1, "pesquisaGuiasEmitidas");
    }

    public synchronized String pesquisaBanda() {
        return metodoGenerico(1, "pesquisaBanda");
    }

    public synchronized String pesquisaCaravana() {
        return metodoGenerico(1, "pesquisaCaravana");
    }

    public synchronized String pesquisaBaile() {
        return metodoGenerico(1, "pesquisaBaile");
    }

    public synchronized String pesquisaVendasCaravana() {
        return metodoGenerico(1, "pesquisaVendasCaravana");
    }

    public synchronized String pesquisaVendedor() {
        return metodoGenerico(1, "pesquisaVendedor");
    }

    public synchronized String pesquisaTurma() {
        return metodoGenerico(1, "pesquisaTurma");
    }

    public synchronized String pesquisaMatriculaEscola() {
        return metodoGenerico(1, "pesquisaMatriculaEscola");
    }

    public synchronized String pesquisaGenero() {
        return metodoGenerico(1, "pesquisaGenero");
    }

    public synchronized String pesquisaTitulo() {
        return metodoGenerico(1, "pesquisaTitulo");

    }

    public synchronized String pesquisaConvencao() {
        return metodoGenerico(1, "pesquisaConvencao");
    }

    public synchronized String pesquisaOposicao() {
        return metodoGenerico(1, "pesquisaOposicao");
    }

    public synchronized String pesquisaLog() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaLogBean");
        return metodoGenerico(1, "pesquisaLog");
    }

    public synchronized String pesquisaPatronal() {
        return metodoGenerico(1, "pesquisaPatronal");
    }

    public synchronized String pesquisaPisoSalarial() {
        return metodoGenerico(1, "pesquisaPisoSalarial");
    }

    public synchronized String pesquisaMatriculaContrato() {
        return metodoGenerico(1, "pesquisaMatriculaContrato");
    }

    public synchronized String pesquisaAgendaTelefone() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("tipoAgendaTelefone", "pesquisaAgendaTelefone");
        return metodoGenerico(1, "pesquisaAgendaTelefone");
    }

    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    // CHAMADA DE MENUS ------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    public synchronized String menuPrincipal() {
        return metodoGenerico(0, "menuPrincipal");
    }

    public synchronized String menuSocial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", SOCIAL);
        return metodoGenerico(0, "menuSocial");
    }

    public synchronized String menuArrecadacao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", ARRECADACAO);
        return metodoGenerico(0, "menuArrecadacao");
    }

    public synchronized String menuFinanceiro() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", FINANCEIRO);
        return metodoGenerico(0, "menuFinanceiro");
    }

    public synchronized String menuHomologacao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", HOMOLOGACAO);
        return metodoGenerico(0, "menuHomologacao");
    }

    public synchronized String menuAcademia() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", ACADEMIA);
        return metodoGenerico(0, "menuAcademia");
    }

    public synchronized String menuEscola() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", ESCOLA);
        return metodoGenerico(0, "menuEscola");
    }

    public synchronized String menuClube() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", CLUBE);
        return metodoGenerico(0, "menuClube");
    }

    public synchronized String menuLocadora() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", LOCADORA);
        return metodoGenerico(0, "menuLocadora");
    }

    public synchronized String menuAtendimento() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idModulo", ATENDIMENTO);
        return metodoGenerico(0, "menuAtendimento");
    }

    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    // CHAMADA DE RELATORIOS --------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    public synchronized String relatorioContribuintes() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("relatorioContribuintesBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioContribuintesBean");
        //}
        return metodoGenerico(3, "relatorioContribuintes");
    }

    public synchronized String relatorioContabilidades() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("relatorioContabilidadesBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioContabilidadesBean");
        //}
        return metodoGenerico(3, "relatorioContabilidades");
    }

    public synchronized String relatorioMovimentos() {
        //if(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().containsKey("relatorioMovimentosBean")){
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioMovimentosBean");
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
        //}
        return metodoGenerico(3, "relatorioMovimentos");
    }

    public synchronized String fechamentoComissaoAcordo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fechamentoComissaoAcordoBean");
        return metodoGenerico(3, "fechamentoComissaoAcordo");
    }

    public synchronized String relatorioSocios() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioSociosBean");
        return metodoGenerico(3, "relatorioSocios");
    }

    public synchronized String cartaoSocial() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("cartaoSocialBean");
        return metodoGenerico(2, "cartaoSocial");
    }

    public synchronized String relatorioHomologacao() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("relatorioHomologacaoBean");
        return metodoGenerico(3, "relatorioHomologacao");
    }
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------
    //------------------------------------------------------------------------------------------------------------

    public String converteURLNome(String strURLNome) {
        String nomePg = "";
        /*if (strURLNome.substring(0, 4).equals("menu")){
         tipoPagina = 0;
         }else if (strURLNome.substring(0, 4).equals("pesq")){
         tipoPagina = 1;
         }else if (strURLNome.substring(0, 4).equals("rela")){
         tipoPagina = 3;
         }else{
         tipoPagina = 2;
         }*/
        // CASOS DE MENUS ----------------------------------------
        //--------------------------------------------------------
        if (tipoPagina == 0) {
            if (strURLNome.equals("menuPrincipal")) {
                nomePg = "Menu Principal";
            } else if (strURLNome.equals("menuSocial")) {
                nomePg = "Menu Social";
            } else if (strURLNome.equals("menuArrecadacao")) {
                nomePg = "Menu Arrecadação";
            } else if (strURLNome.equals("menuFinanceiro")) {
                nomePg = "Menu Financeiro";
            } else if (strURLNome.equals("menuHomologacao")) {
                nomePg = "Menu Homologação";
            } else if (strURLNome.equals("menuAcademia")) {
                nomePg = "Menu Academia";
            } else if (strURLNome.equals("menuEscola")) {
                nomePg = "Menu Escola";
            } else if (strURLNome.equals("menuClube")) {
                nomePg = "Menu Clube";
            } else if (strURLNome.equals("menuLocadora")) {
                nomePg = "Menu Locadora";
            } else if (strURLNome.equals("menuAtendimento")) {
                nomePg = "Menu Atendimento";
            }
            return nomePg;
        }

        // CASOS DE PESQUISAS -------------------------------------
        //---------------------------------------------------------
        if (tipoPagina == 1) {
            if (strURLNome.equals("pesquisaUsuario")) {
                nomePg = "Pesquisa de Usuário";
            } else if (strURLNome.equals("pesquisaPessoaFisica")) {
                nomePg = "Pesquisa de Pessoas Físicas";
            } else if (strURLNome.equals("pesquisaPessoaJuridica")) {
                nomePg = "Pesquisa de Pessoas Jurídica";
            } else if (strURLNome.equals("pesquisaRelatorios")) {
                nomePg = "Pesquisa de Relatórios";
            } else if (strURLNome.equals("pesquisaPessoa")) {
                nomePg = "Pesquisa de Pessoas";
            } else if (strURLNome.equals("pesquisaCnae")) {
                nomePg = "Pesquisa Cnae";
            } else if (strURLNome.equals("pesquisaContabilidade")) {
                nomePg = "Pesquisa Contabilidade";
            } else if (strURLNome.equals("pesquisaPermissao")) {
                nomePg = "Pesquisa Permissões";
            } else if (strURLNome.equals("pesquisaDepartamento")) {
                nomePg = "Pesquisa Departamentos";
            } else if (strURLNome.equals("pesquisaRotina")) {
                nomePg = "Pesquisa Rotinas";
            } else if (strURLNome.equals("pesquisaEvento")) {
                nomePg = "Pesquisa Eventos";
            } else if (strURLNome.equals("pesquisaModulo")) {
                nomePg = "Pesquisa Módulos";
            } else if (strURLNome.equals("pesquisaEndereco")) {
                nomePg = "Pesquisa Endereços";
            } else if (strURLNome.equals("pesquisaCidade")) {
                nomePg = "Pesquisa Cidades";
            } else if (strURLNome.equals("pesquisaBairro")) {
                nomePg = "Pesquisa Bairros";
            } else if (strURLNome.equals("pesquisaLogradouro")) {
                nomePg = "Pesquisa Logradouros";
            } else if (strURLNome.equals("pesquisaDescricaoEndereco")) {
                nomePg = "Pesquisa Descrição Endereço";
            } else if (strURLNome.equals("pesquisaTipoEndereco")) {
                nomePg = "Pesquisa Tipo Endereço";
            } else if (strURLNome.equals("pesquisaTipoDocumento")) {
                nomePg = "Pesquisa Tipo Documento";
            } else if (strURLNome.equals("pesquisaProfissao")) {
                nomePg = "Pesquisa Profissões";
            } else if (strURLNome.equals("pesquisaConvencao")) {
                nomePg = "Pesquisa Convenções";
            } else if (strURLNome.equals("pesquisaMotivoInativacao")) {
                nomePg = "Pesquisa Motivo de Inativações";
            } else if (strURLNome.equals("pesquisaPlano")) {
                nomePg = "Pesquisa Plano";
            } else if (strURLNome.equals("pesquisaServicos")) {
                nomePg = "Pesquisa Contribuições";
            } else if (strURLNome.equals("pesquisaContaBanco")) {
                nomePg = "Pesquisa Conta Banco";
            } else if (strURLNome.equals("pesquisaContaCobranca")) {
                nomePg = "Pesquisa Conta Cobrança";
            } else if (strURLNome.equals("pesquisaIndice")) {
                nomePg = "Pesquisa Índices";
            } else if (strURLNome.equals("pesquisaGrupoConvenio")) {
                nomePg = "Pesquisa Grupo de Convênio";
            } else if (strURLNome.equals("pesquisaSubGrupoConvenio")) {
                nomePg = "Pesquisa SubGrupo de Convênio";
            } else if (strURLNome.equals("pesquisaParentesco")) {
                nomePg = "Pesquisa Parentesco";
            } else if (strURLNome.equals("pesquisaSuspencao")) {
                nomePg = "Pesquisa Suspensão";
            } else if (strURLNome.equals("pesquisaCategoria")) {
                nomePg = "Pesquisa Categoria";
            } else if (strURLNome.equals("pesquisaSocios")) {
                nomePg = "Pesquisa Sócios";
            } else if (strURLNome.equals("pesquisaAcademia")) {
                nomePg = "Pesquisa Matricula Academia";
            } else if (strURLNome.equals("pesquisaConvenioMedico")) {
                nomePg = "Pesquisa Convênio Médico";
            } else if (strURLNome.equals("pesquisaJuridicaConvenio")) {
                nomePg = "Pesquisa Empresas Conveniadas";
            } else if (strURLNome.equals("pesquisaGuiasEmitidas")) {
                nomePg = "Pesquisa Guias Emitidas";
            } else if (strURLNome.equals("pesquisaBanda")) {
                nomePg = "Pesquisa Banda";
            } else if (strURLNome.equals("pesquisaCaravana")) {
                nomePg = "Pesquisa Caravana";
            } else if (strURLNome.equals("pesquisaBaile")) {
                nomePg = "Pesquisa Baile";
            } else if (strURLNome.equals("pesquisaVendasCaravana")) {
                nomePg = "Pesquisa Vendas Caravana";
            } else if (strURLNome.equals("pesquisaVendedor")) {
                nomePg = "Pesquisa Vendedores";
            } else if (strURLNome.equals("pesquisaTurma")) {
                nomePg = "Pesquisa Turmas";
            } else if (strURLNome.equals("pesquisaMatriculaEscola")) {
                nomePg = "Pesquisa Matriculas";
            } else if (strURLNome.equals("pesquisaGenero")) {
                nomePg = "Pesquisa Gênero";
            } else if (strURLNome.equals("pesquisaTitulo")) {
                nomePg = "Pesquisa Titulo";
            } else if (strURLNome.equals("pesquisaOposicao")) {
                nomePg = "Pesquisa Oposição";
            } else if (strURLNome.equals("pesquisaLog")) {
                nomePg = "Pesquisa Logs / Registro de atividades";
            } else if (strURLNome.equals("pesquisaPatronal")) {
                nomePg = "Pesquisa Registro Patronal";
            } else if (strURLNome.equals("pesquisaPisoSalarial")) {
                nomePg = "Pesquisa Piso Salarial";
            } else if (strURLNome.equals("pesquisaMatriculaContrato")) {
                nomePg = "Pesquisa Contrato";
            } else if (strURLNome.equals("pesquisaAgendaTelefone")) {
                nomePg = "Pesquisa Agenda Telefone";
            } else if (strURLNome.equals("pesquisaConfiguracao")) {
                nomePg = "Pesquisa Configuração";
            }
            return nomePg;
        }

        // CASOS DE CADASTRO -------------------------------------
        //--------------------------------------------------------
        if (tipoPagina == 2) {
            if (strURLNome.equals("usuario")) {
                nomePg = "Usuário";
            } else if (strURLNome.equals("pessoaFisica")) {
                nomePg = "Pessoa Física";
            } else if (strURLNome.equals("permissao")) {
                nomePg = "Permissão";
            } else if (strURLNome.equals("pessoaJuridica")) {
                nomePg = "Pessoa Jurídica";
            } else if (strURLNome.equals("departamento")) {
                nomePg = "Departamento";
            } else if (strURLNome.equals("nivel")) {
                nomePg = "Nível";
            } else if (strURLNome.equals("rotina")) {
                nomePg = "Rotina";
            } else if (strURLNome.equals("evento")) {
                nomePg = "Evento";
            } else if (strURLNome.equals("modulo")) {
                nomePg = "Módulo";
            } else if (strURLNome.equals("endereco")) {
                nomePg = "Endereço";
            } else if (strURLNome.equals("bairro")) {
                nomePg = "Bairro";
            } else if (strURLNome.equals("cidade")) {
                nomePg = "Cidade";
            } else if (strURLNome.equals("logradouro")) {
                nomePg = "Logradouro";
            } else if (strURLNome.equals("descricaoEndereco")) {
                nomePg = "Descrição Endereço";
            } else if (strURLNome.equals("tipoEndereco")) {
                nomePg = "Tipo Endereço";
            } else if (strURLNome.equals("tipoDocumento")) {
                nomePg = "Tipo Documento";
            } else if (strURLNome.equals("cnae")) {
                nomePg = "Cnae";
            } else if (strURLNome.equals("profissao")) {
                nomePg = "Profissão";
            } else if (strURLNome.equals("convencao")) {
                nomePg = "Convenção";
            } else if (strURLNome.equals("cnaeConvencao")) {
                nomePg = "Definir Cnaes Convenção";
            } else if (strURLNome.equals("grupoCidades")) {
                nomePg = "Cidades do Grupo";
            } else if (strURLNome.equals("mensagem")) {
                nomePg = "Mensagens";
            } else if (strURLNome.equals("motivoInativacao")) {
                nomePg = "Motivo Inativação";
            } else if (strURLNome.equals("descontoEmpregado")) {
                nomePg = "Desconto Empregado";
            } else if (strURLNome.equals("extratoTela")) {
                nomePg = "Extrato na Tela";
            } else if (strURLNome.equals("plano")) {
                nomePg = "Plano de Contas";
            } else if (strURLNome.equals("servicos")) {
                nomePg = "Contribuições";
            } else if (strURLNome.equals("contaBanco")) {
                nomePg = "Conta Banco";
            } else if (strURLNome.equals("contaCobranca")) {
                nomePg = "Conta Cobrança";
            } else if (strURLNome.equals("servicoContaCobranca")) {
                nomePg = "Serviço Conta Cobrança";
            } else if (strURLNome.equals("webContribuinte")) {
                nomePg = "Acesso Contribuintes";
            } else if (strURLNome.equals("webContabilidade")) {
                nomePg = "Acesso Contabilidade";
            } else if (strURLNome.equals("webConfiguracoes")) {
                nomePg = "Configurações de Usuário";
            } else if (strURLNome.equals("movimentosReceber")) {
                nomePg = "Movimentos à Receber";
            } else if (strURLNome.equals("acordo")) {
                nomePg = "Acordo";
            } else if (strURLNome.equals("baixaGeral")) {
                nomePg = "Baixa Geral";
            } else if (strURLNome.equals("processamentoIndividual")) {
                nomePg = "Impressão Individual de Boletos";
            } else if (strURLNome.equals("contribuicao")) {
                nomePg = "Contribuição";
            } else if (strURLNome.equals("impressaoBoletos")) {
                nomePg = "Impressão de Boletos";
            } else if (strURLNome.equals("arquivoBanco")) {
                nomePg = "Remessa e Retorno";
            } else if (strURLNome.equals("indiceMensal")) {
                nomePg = "Índice Mensal";
            } else if (strURLNome.equals("correcao")) {
                nomePg = "Correção";
            } else if (strURLNome.equals("indice")) {
                nomePg = "Indice";
            } else if (strURLNome.equals("permissaoDepartamento")) {
                nomePg = "Permissão por Departamento";
            } else if (strURLNome.equals("retornoBanco")) {
                nomePg = "Retorno do Banco";
            } else if (strURLNome.equals("registroEmpresarial")) {
                nomePg = "Registro Empresarial";
            } else if (strURLNome.equals("enviarEmail")) {
                nomePg = "Enviar E-mails";
            } else if (strURLNome.equals("agendamento")) {
                nomePg = "Agendamento de Homologação";
            } else if (strURLNome.equals("homologacao")) {
                nomePg = "Homologação";
            } else if (strURLNome.equals("webAgendamentoContribuinte")) {
                nomePg = "Agendamento Web Contribuinte";
            } else if (strURLNome.equals("webAgendamentoContabilidade")) {
                nomePg = "Agendamento Web Contabilidade";
            } else if (strURLNome.equals("feriados")) {
                nomePg = "Cadastro de Feriados";
            } else if (strURLNome.equals("horarios")) {
                nomePg = "Cadastro de Horários";
            } else if (strURLNome.equals("subGrupoConvenio")) {
                nomePg = "Sub-Grupo Convênio";
            } else if (strURLNome.equals("convenio")) {
                nomePg = "Convênio";
            } else if (strURLNome.equals("parentesco")) {
                nomePg = "Cadastro de Parentesco";
            } else if (strURLNome.equals("suspencao")) {
                nomePg = "Cadastro de Suspensão";
            } else if (strURLNome.equals("grupoCategoria")) {
                nomePg = "Categoria do Grupo";
            } else if (strURLNome.equals("categoria")) {
                nomePg = "Categoria";
            } else if (strURLNome.equals("socios")) {
                nomePg = "Sócios";
            } else if (strURLNome.equals("contribuicaoSocio")) {
                nomePg = "Contribuição dos Sócios";
            } else if (strURLNome.equals("contribuicaoPorSocio")) {
                nomePg = "Contribuição individual de Sócios";
            } else if (strURLNome.equals("convenioMedico")) {
                nomePg = "Convênio Médico";
            } else if (strURLNome.equals("academia")) {
                nomePg = "Academia";
            } else if (strURLNome.equals("servicoRotina")) {
                nomePg = "Serviço Rotina";
            } else if (strURLNome.equals("lancamentoIndividual")) {
                nomePg = "Lançamento Individual";
            } else if (strURLNome.equals("emissaoGuias")) {
                nomePg = "Emissão de Guias";
            } else if (strURLNome.equals("filial")) {
                nomePg = "Filiais";
            } else if (strURLNome.equals("emissaoCarteirinha")) {
                nomePg = "Emissão de Carteirinha";
            } else if (strURLNome.equals("baixaBoleto")) {
                nomePg = "Baixar Boleto";
            } else if (strURLNome.equals("caravana")) {
                nomePg = "Cadastro de Caravanas";
            } else if (strURLNome.equals("vendasCaravana")) {
                nomePg = "Vendas de Caravanas";
            } else if (strURLNome.equals("descricaoEvento")) {
                nomePg = "Descrição Eventos";
            } else if (strURLNome.equals("baile")) {
                nomePg = "Baile";
            } else if (strURLNome.equals("bandas")) {
                nomePg = "Banda";
            } else if (strURLNome.equals("vendasBaile")) {
                nomePg = "Venda baile";
            } else if (strURLNome.equals("contrato")) {
                nomePg = "Contrato";
            } else if (strURLNome.equals("vendedor")) {
                nomePg = "Vendedores";
            } else if (strURLNome.equals("eFinanceiro")) {
                nomePg = "Financeiro";
            } else if (strURLNome.equals("professor")) {
                nomePg = "Professores";
            } else if (strURLNome.equals("turma")) {
                nomePg = "Turma";
            } else if (strURLNome.equals("matriculaEscola")) {
                nomePg = "Matricula Escola";
            } else if (strURLNome.equals("cancelarHorario")) {
                nomePg = "Cancelamento de horários";
            } else if (strURLNome.equals("genero")) {
                nomePg = "Gênero";
            } else if (strURLNome.equals("titulo")) {
                nomePg = "Titulo";
            } else if (strURLNome.equals("macFilial")) {
                nomePg = "Registro Filial";
            } else if (strURLNome.equals("agendaTarefa")) {
                nomePg = "Agenda de tarefas";
            } else if (strURLNome.equals("agendaInterrupcao")) {
                nomePg = "Interrupções";
            } else if (strURLNome.equals("tipoCentroComercial")) {
                nomePg = "Tipo Centro Comercial";
            } else if (strURLNome.equals("centroComercial")) {
                nomePg = "Centro Comercial";
            } else if (strURLNome.equals("simples")) {
                nomePg = "Cadastro Simples";
            } else if (strURLNome.equals("catalogoFilme")) {
                nomePg = "Catálogo Filme";
            } else if (strURLNome.equals("enviarArquivosContribuinte")) {
                nomePg = "Enviar Arquivos Contribuinte";
            } else if (strURLNome.equals("enviarArquivosContabilidade")) {
                nomePg = "Enviar Arquivos Contabilidade";
            } else if (strURLNome.equals("oposicao")) {
                nomePg = "Oposição";
            } else if (strURLNome.equals("webSolicitaREPIS")) {
                nomePg = "Solicitação do REPIS";
            } else if (strURLNome.equals("webLiberacaoREPIS")) {
                nomePg = "Liberação de REPIS";
            } else if (strURLNome.equals("registroPatronal")) {
                nomePg = "Registro Patronal";
            } else if (strURLNome.equals("pisoSalarial")) {
                nomePg = "Piso Salarial";
            } else if (strURLNome.equals("retornoPadrao")) {
                nomePg = "Retorno Padrao";
            } else if (strURLNome.equals("atendimentOperacao")) {
                nomePg = "Atendimento > Operação";
            } else if (strURLNome.equals("atendimento")) {
                nomePg = "Atendimento";
            } else if (strURLNome.equals("convencaoPeriodo")) {
                nomePg = "Convenção Período";
            } else if (strURLNome.equals("relatorio")) {
                nomePg = "Relatório";
            } else if (strURLNome.equals("cartaoSocial")) {
                nomePg = "Cartão Social";
            } else if (strURLNome.equals("notificacao")) {
                nomePg = "Gerar notificação";
            } else if (strURLNome.equals("matriculaContrato")) {
                nomePg = "Contrato";
            } else if (strURLNome.equals("recepcao")) {
                nomePg = "Recepção";
            } else if (strURLNome.equals("bloqueioServicos")) {
                nomePg = "Bloqueio de Serviços";
            } else if (strURLNome.equals("agendaTelefone")) {
                nomePg = "Agenda Telefone";
            } else if (strURLNome.equals("grupoAgenda")) {
                nomePg = "Grupo Agenda";
            } else if (strURLNome.equals("configuracao")) {
                nomePg = "Configuração";
            } else if (strURLNome.equals("matriculaContratoCampos")) {
                nomePg = "Matricula Contrato Campos";
            }
            return nomePg;
        }
        // CASOS DE RELATORIOS ----------------------------------------
        //--------------------------------------------------------
        if (tipoPagina == 3) {
            if (strURLNome.equals("relatorioContribuintes")) {
                nomePg = "Relatório de Contribuintes";
            } else if (strURLNome.equals("relatorioContabilidades")) {
                nomePg = "Relatório de Empresas Contábeis";
            } else if (strURLNome.equals("relatorioMovimentos")) {
                nomePg = "Relatório de Movimentos";
            } else if (strURLNome.equals("fechamentoComissaoAcordo")) {
                nomePg = "Fechamento de comissão Acordo";
            } else if (strURLNome.equals("relatorioSocios")) {
                nomePg = "Relatório sócios";
            } else if (strURLNome.equals("relatorioHomologacao")) {
                nomePg = "Relatório de Homologações";
            }
            return nomePg;
        }
        return "";
    }

    public String converteURL(String urlDest) {
        return urlDest.substring(urlDest.lastIndexOf("/") + 1, urlDest.lastIndexOf("."));
    }

    public DataObject getDtObjectLabel() {
        return dtObjectLabel;
    }

    public void setDtObjectLabel(DataObject dtObjectLabel) {
        this.dtObjectLabel = dtObjectLabel;
    }

    public void refreshForm() {
    }

    public String getControleLinks() {
        try {
            paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String urlDestino = paginaRequerida.getRequestURI();
            String linkAtual = converteURL(urlDestino);
            String linkTeste = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
            if (linkTeste == null) {
                linkTeste = "";
            }
            if (linkAtual.equals("acessoNegado") || FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso") == null) {
                carregaPg = false;
                return null;
            }
            if (linkAtual.equals("sessaoExpirou") || FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso") == null) {
                carregaPg = false;
                return null;
            }
            if (linkAtual.equals("menuPrincipal") || linkAtual.equals("menuPrincipalAcessoWeb") || linkAtual.equals("menuPrincipalSuporteWeb")) {
                carregaPg = true;
                nivelLink = 0;
            }
            if (carregaPg) {
                linkClicado = false;
                if (nivelLink == 0) {
                    if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso")).equals("local")) {
                        dtObject.setArgumento0("menuPrincipal");
                        dtObjectLabel.setArgumento0("Menu Principal");
                        limpaNivel0();
                    } else if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso")).equals("web")) {
                        dtObject.setArgumento0("menuPrincipalAcessoWeb");
                        dtObjectLabel.setArgumento0("Menu Principal");
                        limpaNivel0();
                    } else if (((String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("indicaAcesso")).equals("suporteWeb")) {
                        dtObject.setArgumento0("menuPrincipalSuporteWeb");
                        dtObjectLabel.setArgumento0("Menu Principal Suporte Web");
                        limpaNivel0();
                    }
                    nivelLink = 1;
                    carregaPg = false;
                    return null;
                }

                if (nivelLink == 1) {
                    dtObject.setArgumento1(linkAtual);
                    dtObjectLabel.setArgumento1(converteURLNome(linkAtual));
                    render1 = true;
                    carregaPg = false;
                    nivelLink = 2;
                    return null;
                }
                if (nivelLink == 2) {
                    dtObject.setArgumento2(linkAtual);
                    dtObjectLabel.setArgumento2(converteURLNome(linkAtual));
                    render2 = true;
                    carregaPg = false;
                    nivelLink = 3;
                    return null;
                }
                if (nivelLink == 3) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento2(null);
                        dtObject.setArgumento2(linkAtual);
                        dtObjectLabel.setArgumento2(null);
                        dtObjectLabel.setArgumento2(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento3(linkAtual);
                        dtObjectLabel.setArgumento3(converteURLNome(linkAtual));
                        render3 = true;
                        nivelLink = 4;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 4) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento3(null);
                        dtObject.setArgumento3(linkAtual);
                        dtObjectLabel.setArgumento3(null);
                        dtObjectLabel.setArgumento3(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento4(linkAtual);
                        dtObjectLabel.setArgumento4(converteURLNome(linkAtual));
                        render4 = true;
                        nivelLink = 5;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 5) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento4(null);
                        dtObject.setArgumento4(linkAtual);
                        dtObjectLabel.setArgumento4(null);
                        dtObjectLabel.setArgumento4(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento5(linkAtual);
                        dtObjectLabel.setArgumento5(converteURLNome(linkAtual));
                        render5 = true;
                        nivelLink = 6;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 6) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento5(null);
                        dtObject.setArgumento5(linkAtual);
                        dtObjectLabel.setArgumento5(null);
                        dtObjectLabel.setArgumento5(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento6(linkAtual);
                        dtObjectLabel.setArgumento6(converteURLNome(linkAtual));
                        setRender6(true);
                        nivelLink = 7;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 7) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento6(null);
                        dtObject.setArgumento6(linkAtual);
                        dtObjectLabel.setArgumento6(null);
                        dtObjectLabel.setArgumento6(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento7(linkAtual);
                        dtObjectLabel.setArgumento7(converteURLNome(linkAtual));
                        setRender7(true);
                        nivelLink = 8;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 8) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento7(null);
                        dtObject.setArgumento7(linkAtual);
                        dtObjectLabel.setArgumento7(null);
                        dtObjectLabel.setArgumento7(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento8(linkAtual);
                        dtObjectLabel.setArgumento8(converteURLNome(linkAtual));
                        setRender8(true);
                        nivelLink = 9;
                    }
                    carregaPg = false;
                    return null;
                }
                if (nivelLink == 9) {
                    if ((Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoCadastro")) {
                        dtObject.setArgumento8(null);
                        dtObject.setArgumento8(linkAtual);
                        dtObjectLabel.setArgumento8(null);
                        dtObjectLabel.setArgumento8(converteURLNome(linkAtual));
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("acessoCadastro", false);
                    } else {
                        dtObject.setArgumento9(linkAtual);
                        dtObjectLabel.setArgumento9(converteURLNome(linkAtual));
                        setRender9(true);
                        nivelLink = 10;
                    }
                    carregaPg = false;
                    return null;
                }
            } else if (linkTeste.equals(linkAtual)) {
                if (((String) dtObject.getArgumento0()).equals(linkTeste)) {
                    limpaNivel0();
                    nivelLink = 1;
                    return null;
                }
                if (((String) dtObject.getArgumento1()).equals(linkTeste)) {
                    limpaNivel1();
                    nivelLink = 2;
                    return null;
                }
                if (((String) dtObject.getArgumento2()).equals(linkTeste)) {
                    limpaNivel2();
                    nivelLink = 3;
                    return null;
                }
                if (((String) dtObject.getArgumento3()).equals(linkTeste)) {
                    limpaNivel3();
                    nivelLink = 4;
                    return null;
                }
                if (((String) dtObject.getArgumento4()).equals(linkTeste)) {
                    limpaNivel4();
                    nivelLink = 5;
                    return null;
                }
                if (((String) dtObject.getArgumento5()).equals(linkTeste)) {
                    limpaNivel5();
                    nivelLink = 6;
                    return null;
                }
                if (((String) dtObject.getArgumento6()).equals(linkTeste)) {
                    limpaNivel6();
                    nivelLink = 7;
                    return null;
                }
                if (((String) dtObject.getArgumento7()).equals(linkTeste)) {
                    limpaNivel7();
                    nivelLink = 8;
                    return null;
                }
                if (((String) dtObject.getArgumento8()).equals(linkTeste)) {
                    limpaNivel8();
                    nivelLink = 9;
                    return null;
                }
                if (((String) dtObject.getArgumento9()).equals(linkTeste)) {
                    limpaNivel9();
                    nivelLink = 10;
                    return null;
                }
                return null;
            }
            return null;
        } catch (Exception e) {
            NovoLog novoLog = new NovoLog();
            novoLog.novo("controleLinks", e.getMessage());
            return null;
        }

    }

//NIVEL LINK 0 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink0() {
        String irPara = (String) dtObject.getArgumento0();
        limpaNivel0();
        nivelLink = 1;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel0() {
        dtObject.setArgumento1(null);
        dtObjectLabel.setArgumento1(null);
        dtObject.setArgumento2(null);
        dtObjectLabel.setArgumento2(null);
        dtObject.setArgumento3(null);
        dtObjectLabel.setArgumento3(null);
        dtObject.setArgumento4(null);
        dtObjectLabel.setArgumento4(null);
        dtObject.setArgumento5(null);
        dtObjectLabel.setArgumento5(null);
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        render1 = false;
        render2 = false;
        render3 = false;
        render4 = false;
        render5 = false;
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 1 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink1() {
        String irPara = (String) dtObject.getArgumento1();
        limpaNivel1();
        nivelLink = 2;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel1() {
        dtObject.setArgumento2(null);
        dtObjectLabel.setArgumento2(null);
        dtObject.setArgumento3(null);
        dtObjectLabel.setArgumento3(null);
        dtObject.setArgumento4(null);
        dtObjectLabel.setArgumento4(null);
        dtObject.setArgumento5(null);
        dtObjectLabel.setArgumento5(null);
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        render2 = false;
        render3 = false;
        render4 = false;
        render5 = false;
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 2 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink2() {
        String irPara = (String) dtObject.getArgumento2();
        limpaNivel2();
        nivelLink = 3;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel2() {
        dtObject.setArgumento3(null);
        dtObjectLabel.setArgumento3(null);
        dtObject.setArgumento4(null);
        dtObjectLabel.setArgumento4(null);
        dtObject.setArgumento5(null);
        dtObjectLabel.setArgumento5(null);
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        render3 = false;
        render4 = false;
        render5 = false;
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 3 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink3() {
        String irPara = (String) dtObject.getArgumento3();
        limpaNivel3();
        nivelLink = 4;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel3() {
        dtObject.setArgumento4(null);
        dtObjectLabel.setArgumento4(null);
        dtObject.setArgumento5(null);
        dtObjectLabel.setArgumento5(null);
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        render4 = false;
        render5 = false;
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 4 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink4() {
        String irPara = (String) dtObject.getArgumento4();
        limpaNivel4();
        nivelLink = 5;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel4() {
        dtObject.setArgumento5(null);
        dtObjectLabel.setArgumento5(null);
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        render5 = false;
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 5 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink5() {
        String irPara = (String) dtObject.getArgumento5();
        limpaNivel5();
        nivelLink = 6;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel5() {
        dtObject.setArgumento6(null);
        dtObjectLabel.setArgumento6(null);
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        setRender6(false);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 6 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink6() {
        String irPara = (String) dtObject.getArgumento6();
        limpaNivel6();
        nivelLink = 7;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel6() {
        dtObject.setArgumento7(null);
        dtObjectLabel.setArgumento7(null);
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        setRender7(false);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 7 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink7() {
        String irPara = (String) dtObject.getArgumento7();
        limpaNivel7();
        nivelLink = 8;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel7() {
        dtObject.setArgumento8(null);
        dtObjectLabel.setArgumento8(null);
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        setRender8(false);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 8 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink8() {
        String irPara = (String) dtObject.getArgumento8();
        limpaNivel8();
        nivelLink = 9;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel8() {
        dtObject.setArgumento9(null);
        dtObjectLabel.setArgumento9(null);
        setRender9(false);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

//NIVEL LINK 9 -----------------------------------------------------------------
//------------------------------------------------------------------------------
    public String clickNivelLink9() {
        String irPara = (String) dtObject.getArgumento9();
        limpaNivel9();
        nivelLink = 10;
        linkClicado = true;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        return irPara;
    }

    public void limpaNivel9() {
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------

    public List<Rotina> getListaRotina() {
        RotinaDB db = new RotinaDBToplink();
        if (listaRotina.isEmpty()) {
            listaRotina = db.pesquisaTodosOrdenado();
        }
        return listaRotina;
    }

    public void setListaRotina(List<Rotina> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public String entrarPagina() {
        if (oqFazer.equals("usuario")) {
            return usuario();
        }
        if (oqFazer.equals("fisica")) {
            return pessoaFisica();
        }
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlAtual = converteURL(paginaRequerida.getRequestURI());
        oqFazer = "";
        return urlAtual;
    }

    public boolean isRender1() {
        return render1;
    }

    public void setRender1(boolean render1) {
        this.render1 = render1;
    }

    public boolean isRender2() {
        return render2;
    }

    public void setRender2(boolean render2) {
        this.render2 = render2;
    }

    public boolean isRender3() {
        return render3;
    }

    public void setRender3(boolean render3) {
        this.render3 = render3;
    }

    public boolean isRender4() {
        return render4;
    }

    public void setRender4(boolean render4) {
        this.render4 = render4;
    }

    public boolean isRender5() {
        return render5;
    }

    public void setRender5(boolean render5) {
        this.render5 = render5;
    }

    public boolean isRender6() {
        return render6;
    }

    public void setRender6(boolean render6) {
        this.render6 = render6;
    }

    public boolean isRender7() {
        return render7;
    }

    public void setRender7(boolean render7) {
        this.render7 = render7;
    }

    public boolean isRender8() {
        return render8;
    }

    public void setRender8(boolean render8) {
        this.render8 = render8;
    }

    public boolean isRender9() {
        return render9;
    }

    public void setRender9(boolean render9) {
        this.render9 = render9;
    }

    public String getOqFazer() {
        return oqFazer;
    }

    public void setOqFazer(String oqFazer) {
        this.oqFazer = oqFazer;
    }

    public boolean isRenderPesquisa() {
        paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        urlAtual = converteURL(paginaRequerida.getRequestURI());
        if (urlAtual.equals("menuPrincipalAcessoWeb")) {
            renderPesquisa = false;
        }
        return renderPesquisa;
    }

    public void setRenderPesquisa(boolean renderPesquisa) {
        this.renderPesquisa = renderPesquisa;
    }
}
