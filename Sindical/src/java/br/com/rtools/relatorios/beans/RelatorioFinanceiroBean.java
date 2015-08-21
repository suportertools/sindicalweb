package br.com.rtools.relatorios.beans;

import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.TipoPagamento;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.impressao.ParametroRelatorioFinanceiro;
import br.com.rtools.relatorios.RelatorioParametros;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.dao.RelatorioDao;
import br.com.rtools.relatorios.dao.RelatorioFinanceiroDao;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Jasper;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;

@ManagedBean
@SessionScoped
public class RelatorioFinanceiroBean implements Serializable {

    private Integer idRelatorio = 0;
    private List<SelectItem> listaRelatorio = new ArrayList();
    /**
     * Lista de Filtros (indices)
     * <p>
     * 0 CONTA CONTÁBIL
     * <br />1 GRUPO
     * <br />2 SUB GRUPO
     * <br />3 SERVIÇOS
     * <br />4 DATAS
     * <br />5 CAIXA / BANCO
     * <br />6 CAIXA
     * <br />7 OPERADOR
     * <br />8 TIPO QUITAÇÃO
     * <br />9 DEPARTAMENTO
     */
    private List<Filtros> listaFiltros = new ArrayList();
    private Integer contaContabil = null;

    private Integer idGrupo = 0;
    private List<SelectItem> listaGrupo = new ArrayList();

    private Integer idSubGrupo = 0;
    private List<SelectItem> listaSubGrupo = new ArrayList();

    private Integer idServicos = 0;
    private List<SelectItem> listaServicos = new ArrayList();

    private Integer idCaixa = 0;
    private List<SelectItem> listaCaixa = new ArrayList();

    private Integer idOperador = 0;
    private List<SelectItem> listaOperador = new ArrayList();

    private Integer idTipoQuitacao = 0;
    private List<SelectItem> listaTipoQuitacao = new ArrayList();
    
    private Integer idCaixaBanco = 0;
    private List<SelectItem> listaCaixaBanco = new ArrayList();

    private String dataEmissao = "";
    private String dataEmissaoFinal = "";
    private String dataVencimento = "";
    private String dataVencimentoFinal = "";
    private String dataQuitacao = "";
    private String dataQuitacaoFinal = "";
    private String dataImportacao = "";
    private String dataImportacaoFinal = "";
    private String dataCredito = "";
    private String dataCreditoFinal = "";
    private String dataFechamentoCaixa = "";
    private String dataFechamentoCaixaFinal = "";

    private List<ListaPlanos> listaPlanos = new ArrayList();
    private Integer idPlano5 = 0;
    private List<SelectItem> listaPlano5 = new ArrayList();
    
    private boolean chkTodos = false;
    private boolean chkExcel = false;
    
    private String tipoDepartamento = "outros";

    @PostConstruct
    public void init() {
        loadListaRelatorio();
        loadListaFiltro();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("relatorioFinanceiroBean");
    }

    public void loadListaCaixaBanco(){
        listaCaixaBanco.clear();
        idCaixaBanco = 0;
        
        RelatorioFinanceiroDao dao = new RelatorioFinanceiroDao();
        

        List<Plano5> result = dao.listaCaixaBanco();
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaCaixaBanco.add(
                        new SelectItem(
                                i,
                                result.get(i).getConta(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }        
    }
    
    public void marcarTodos(){
        for (ListaPlanos listaPlano : listaPlanos) {
            listaPlano.setSelecionado(chkTodos);
        }
        
        loadListaPlano5();
    }
    
    public void loadListaPlano5(){
        listaPlano5.clear();
        idPlano5 = 0;
        
        RelatorioFinanceiroDao dao = new RelatorioFinanceiroDao();
        
        String ids = "";
        for (ListaPlanos lp : listaPlanos){
            if (lp.selecionado){
                if (ids.isEmpty()){
                    ids = "" + lp.idPlano4;
                }else{
                    ids += ", " + lp.idPlano4;
                }
            }
        }

        List<Plano5> result = dao.listaPlano5(ids);
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaPlano5.add(
                        new SelectItem(
                                i,
                                result.get(i).getConta(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
    }
    
    public void loadListaPlanos() {
        listaPlanos.clear();

        List<Object> result = new RelatorioFinanceiroDao().listaPlanos();

        for (Object linha : result) {
            listaPlanos.add(
                    new ListaPlanos(
                            false,
                            (Integer) ((List) linha).get(0),
                            ((List) linha).get(1).toString(),
                            (Integer) ((List) linha).get(2),
                            ((List) linha).get(3).toString(),
                            (Integer) ((List) linha).get(4),
                            ((List) linha).get(5).toString(),
                            (Integer) ((List) linha).get(6),
                            ((List) linha).get(7).toString()
                    )
            );
        }
        
        loadListaPlano5();
    }

    public void loadListaTipoQuitacao() {
        listaTipoQuitacao.clear();

        RelatorioFinanceiroDao dao = new RelatorioFinanceiroDao();

        List<TipoPagamento> result = dao.listaTipoQuitacao();
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaTipoQuitacao.add(
                        new SelectItem(
                                i,
                                result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
    }

    public void loadListaOperador() {
        listaOperador.clear();

        RelatorioFinanceiroDao dao = new RelatorioFinanceiroDao();

        List<Usuario> result = dao.listaUsuario();
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaOperador.add(
                        new SelectItem(
                                i,
                                result.get(i).getPessoa().getNome(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
    }

    public void loadListaCaixa() {
        listaCaixa.clear();

        FinanceiroDB db = new FinanceiroDBToplink();

        List<Caixa> result = db.listaCaixa();
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaCaixa.add(
                        new SelectItem(
                                i,
                                (result.get(i).getCaixa() != 0) ? result.get(i).getCaixa() + " - " : ""+ result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
    }

    public void loadListaGrupo() {
        listaGrupo.clear();

        Dao di = new Dao();
        List<GrupoFinanceiro> result = di.list(new GrupoFinanceiro());

        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaGrupo.add(
                        new SelectItem(
                                i,
                                result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }

        loadListaSubGrupo();
    }

    public void loadListaSubGrupo() {
        listaSubGrupo.clear();
        idSubGrupo = 0;
        idServicos = 0;
        FinanceiroDB db = new FinanceiroDBToplink();

        // (listaFiltros.get(1).ativo) GRUPO
        List<SubGrupoFinanceiro> result = db.listaSubGrupo((listaFiltros.get(1).ativo) ? Integer.valueOf(listaGrupo.get(idGrupo).getDescription()) : null);
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaSubGrupo.add(
                        new SelectItem(
                                i,
                                result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }

        loadListaServicos();
    }

    public void loadListaServicos() {
        listaServicos.clear();
        idServicos = 0;

        // (listaFiltros.get(2).ativo) SUB GRUPO
        List<Servicos> result = new RelatorioFinanceiroDao().listaServicosSubGrupo((listaFiltros.get(2).ativo) ? Integer.valueOf(listaSubGrupo.get(idSubGrupo).getDescription()) : null);
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaServicos.add(
                        new SelectItem(
                                i,
                                result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
    }

    public void imprimir() {
        Relatorios relatorios = (Relatorios) new Dao().find(new Relatorios(), Integer.parseInt(listaRelatorio.get(idRelatorio).getDescription()));
        Integer id_contabil = null, id_grupo = null, id_sub_grupo = null, id_servicos = null, id_caixa_banco = null, id_caixa = null, id_operador = null, id_tipo_quitacao = null;
        List<String> ldescricao = new ArrayList();
        // CONTA CONTABIL
        if (listaFiltros.get(0).ativo && !listaPlano5.isEmpty()) {
            id_contabil = Integer.valueOf(listaPlano5.get(idPlano5).getDescription());
        }

        // GRUPO
        if (listaFiltros.get(1).ativo) {
            id_grupo = Integer.valueOf(listaGrupo.get(idGrupo).getDescription());
        }

        // SUB GRUPO
        if (listaFiltros.get(2).ativo) {
            id_sub_grupo = Integer.valueOf(listaSubGrupo.get(idSubGrupo).getDescription());
        }

        // SERVIÇOS 
        if (listaFiltros.get(3).ativo) {
            id_servicos = Integer.valueOf(listaServicos.get(idServicos).getDescription());
        }

        // DATAS
        String dtEmissao = "", dtVencimento = "", dtQuitacao = "", dtImportacao = "", dtCredito = "", dtFechamentoCaixa = "";
        String dtEmissaoFinal = "", dtVencimentoFinal = "", dtQuitacaoFinal = "", dtImportacaoFinal = "", dtCreditoFinal = "", dtFechamentoCaixaFinal = "";
        if (listaFiltros.get(4).ativo) {
            // EMISSÃO --------------
            if (!dataEmissao.isEmpty() && !dataEmissaoFinal.isEmpty()) {
                ldescricao.add("Data de Emissão de: " + dataEmissao + " à " + dataEmissaoFinal);
                dtEmissao = dataEmissao;
                dtEmissaoFinal = dataEmissaoFinal;
            }else if (!dataEmissao.isEmpty() && dataEmissaoFinal.isEmpty()) {
                ldescricao.add("Data de Emissão: " + dataEmissao);
                dtEmissao = dataEmissao;
            }else if (dataEmissao.isEmpty() && !dataEmissaoFinal.isEmpty()) {
                ldescricao.add("Data de Emissão até: " + dataEmissaoFinal);
                dtEmissaoFinal = dataEmissaoFinal;
            }
            
            if (!dataVencimento.isEmpty() && !dataVencimentoFinal.isEmpty()) {
                ldescricao.add("Data de Vencimento de: " + dataVencimento + " à " + dataVencimentoFinal);
                dtVencimento = dataVencimento;
                dtVencimentoFinal = dataVencimentoFinal;
            }else if (!dataVencimento.isEmpty() && dataVencimentoFinal.isEmpty()) {
                ldescricao.add("Data de Vencimento: " + dataVencimento);
                dtVencimento = dataVencimento;
            }else if (dataVencimento.isEmpty() && !dataVencimentoFinal.isEmpty()) {
                ldescricao.add("Data de Vencimento até: " + dataVencimentoFinal);
                dtVencimentoFinal = dataVencimentoFinal;
            }
            
            if (!dataQuitacao.isEmpty() && !dataQuitacaoFinal.isEmpty()) {
                ldescricao.add("Data de Quitação de: " + dataQuitacao + " à "+ dataQuitacaoFinal);
                dtQuitacao = dataQuitacao;
                dtQuitacaoFinal = dataQuitacaoFinal;
            }else if (!dataQuitacao.isEmpty() && dataQuitacaoFinal.isEmpty()) {
                ldescricao.add("Data de Quitação: " + dataQuitacao);
                dtQuitacao = dataQuitacao;
            }else if (dataQuitacao.isEmpty() && !dataQuitacaoFinal.isEmpty()) {
                ldescricao.add("Data de Quitação até: " + dataQuitacaoFinal);
                dtQuitacaoFinal = dataQuitacaoFinal;
            }
            
            if (!dataImportacao.isEmpty() && !dataImportacaoFinal.isEmpty()) {
                ldescricao.add("Data de Importação de: " + dataImportacao + " à " + dataImportacaoFinal);
                dtImportacao = dataImportacao;
                dtImportacaoFinal = dataImportacaoFinal;
            }else if (!dataImportacao.isEmpty() && dataImportacaoFinal.isEmpty()) {
                ldescricao.add("Data de Importação: " + dataImportacao);
                dtImportacao = dataImportacao;
            }else if (dataImportacao.isEmpty() && !dataImportacaoFinal.isEmpty()) {
                ldescricao.add("Data de Importação até: " + dataImportacaoFinal);
                dtImportacaoFinal = dataImportacaoFinal;
            }
            
            if (!dataCredito.isEmpty() && !dataCreditoFinal.isEmpty()) {
                ldescricao.add("Data de Crédito de: " + dataCredito + " à " +dataCreditoFinal);
                dtCredito = dataCredito;
                dtCreditoFinal = dataCreditoFinal;
            }else if (!dataCredito.isEmpty() && dataCreditoFinal.isEmpty()) {
                ldescricao.add("Data de Crédito: " + dataCredito);
                dtCredito = dataCredito;
            }else if (dataCredito.isEmpty() && !dataCreditoFinal.isEmpty()) {
                ldescricao.add("Data de Crédito até: " + dataCreditoFinal);
                dtCreditoFinal = dataCreditoFinal;
            }
            
            if (!dataFechamentoCaixa.isEmpty() && !dataFechamentoCaixaFinal.isEmpty()) {
                ldescricao.add("Data do Fechamento Caixa de: " + dataFechamentoCaixa + " à " + dataFechamentoCaixaFinal);
                dtFechamentoCaixa = dataFechamentoCaixa;
                dtFechamentoCaixaFinal = dataFechamentoCaixaFinal;
            }else if (!dataFechamentoCaixa.isEmpty() && dataFechamentoCaixaFinal.isEmpty()) {
                ldescricao.add("Data do Fechamento Caixa: " + dataFechamentoCaixa);
                dtFechamentoCaixa = dataFechamentoCaixa;
            }else if (dataFechamentoCaixa.isEmpty() && !dataFechamentoCaixaFinal.isEmpty()) {
                ldescricao.add("Data do Fechamento Caixa até: " + dataFechamentoCaixaFinal);
                dtFechamentoCaixaFinal = dataFechamentoCaixaFinal;
            }
        }

        // CAIXA BANCO
        if (listaFiltros.get(5).ativo) {
            id_caixa_banco = Integer.valueOf(listaCaixaBanco.get(idCaixaBanco).getDescription());
        }

        // CAIXA
        if (listaFiltros.get(6).ativo) {
            id_caixa = Integer.valueOf(listaCaixa.get(idCaixa).getDescription());
        }

        // OPERADOR
        if (listaFiltros.get(7).ativo) {
            id_operador = Integer.valueOf(listaOperador.get(idOperador).getDescription());
        }

        // TIPO QUITAÇÃO
        if (listaFiltros.get(8).ativo) {
            id_tipo_quitacao = Integer.valueOf(listaTipoQuitacao.get(idTipoQuitacao).getDescription());
        }
        
        // DEPARTAMENTO
        String tipo_departamento = "";
        if (listaFiltros.get(9).ativo) {
            tipo_departamento = tipoDepartamento;
        }

        Map params = new HashMap();
        // MOEDA PARA BRASIL VALORES IREPORT PTBR CONVERTE VALOR JASPER PTBR MOEDA
        params.put("REPORT_LOCALE", new Locale("pt", "BR"));

        String descricaoData = "";
        for (String linha : ldescricao) {
            if (descricaoData.isEmpty()) {
                descricaoData = linha;
            } else {
                descricaoData += ", " + linha;
            }
        }
        params.put("descricao_data", descricaoData);
        params.put("logo_sindicato", ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/LogoCliente.png"));
        
        List<Object> result = new RelatorioFinanceiroDao().listaRelatorioFinanceiro(id_contabil, id_grupo, id_sub_grupo, id_servicos, dtEmissao, dtEmissaoFinal, dtVencimento, dtVencimentoFinal, dtQuitacao, dtQuitacaoFinal, dtImportacao, dtImportacaoFinal, dtCredito, dtCreditoFinal, dtFechamentoCaixa, dtFechamentoCaixaFinal, id_caixa_banco, id_caixa, id_operador, id_tipo_quitacao, tipo_departamento, relatorios);
        
        if (result.isEmpty()) {
            GenericaMensagem.error("Atenção", "Nenhum resultado encontrado para a pesquisa!");
            return;
        }

        List<RelatorioParametros> listaRL = new RelatorioDao().listaRelatorioParametro(relatorios.getId());

        List<HashMap> list_hash = new ArrayList();
        HashMap<String, Object> map = new LinkedHashMap();
        
        String[] param_query = new String[listaRL.size()];
        for (int i = 0; i < listaRL.size(); i++){
            param_query[i] = listaRL.get(i).getApelido();
        }
        
        for (Object linha : result) {
            List list = (List) linha;
            map = new LinkedHashMap();
            for (int i = 0; i < param_query.length; i++){
                map.put(param_query[i], list.get(i));
            }
        
            list_hash.add(map);
        }

        Jasper.EXPORT_TO_EXCEL = chkExcel;
        Jasper.printReports(relatorios.getJasper(), relatorios.getNome(), list_hash, params);
        //Jasper.printReports(relatorios.getJasper(), relatorios.getNome(), list_param, params);
    }

    public void acao(Filtros linha) {
        linha.setAtivo(!linha.ativo);

        switch (linha.chave) {
            case "contaContabil":
                loadListaPlanos();
                break;
            case "grupo":
                loadListaGrupo();
                break;
            case "subGrupo":
                loadListaSubGrupo();
                break;
            case "servicos":
                loadListaServicos();
                break;
            case "datas":
                break;
            case "caixaBanco":
                loadListaCaixaBanco();
                break;
            case "caixa":
                loadListaCaixa();
                break;
            case "operador":
                loadListaOperador();
                break;
            case "tipoQuitacao":
                loadListaTipoQuitacao();
                break;
            case "departamento":
                break;
        }
    }

    public void loadListaFiltro() {
        listaFiltros.clear();

        listaFiltros.add(new Filtros("contaContabil", "Conta Contábil", false));
        listaFiltros.add(new Filtros("grupo", "Grupo", false));
        listaFiltros.add(new Filtros("subGrupo", "Sub Grupo", false));
        listaFiltros.add(new Filtros("servicos", "Serviços", false));
        listaFiltros.add(new Filtros("datas", "Datas", false));
        listaFiltros.add(new Filtros("caixaBanco", "Caixa / Banco", false));
        listaFiltros.add(new Filtros("caixa", "Caixa", false));
        listaFiltros.add(new Filtros("operador", "Operador", false));
        listaFiltros.add(new Filtros("tipoQuitacao", "Tipo de Quitação", false));
        listaFiltros.add(new Filtros("departamento", "Departamento", true));

    }

    public void loadListaRelatorio() {
        RelatorioDao db = new RelatorioDao();
        List<Relatorios> list = db.pesquisaTipoRelatorio(324);
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                idRelatorio = i;
            }
            if (list.get(i).getPrincipal()) {
                idRelatorio = i;
            }
            listaRelatorio.add(
                    new SelectItem(
                            i,
                            list.get(i).getNome(),
                            Integer.toString(list.get(i).getId())
                    )
            );
        }
    }

    public Integer getIdRelatorio() {
        return idRelatorio;
    }

    public void setIdRelatorio(Integer idRelatorio) {
        this.idRelatorio = idRelatorio;
    }

    public List<SelectItem> getListaRelatorio() {
        return listaRelatorio;
    }

    public void setListaRelatorio(List<SelectItem> listaRelatorio) {
        this.listaRelatorio = listaRelatorio;
    }

    /**
     * Lista de Filtros (indices)
     * <p>
     * 0 CONTA CONTÁBIL
     * <br />1 GRUPO
     * <br />2 SUB GRUPO
     * <br />3 SERVIÇOS
     * <br />4 DATAS
     * <br />5 CAIXA / BANCO
     * <br />6 CAIXA
     * <br />7 OPERADOR
     * <br />8 TIPO QUITAÇÃO
     * <br />9 DEPARTAMENTO
     *
     * @return Lista de Filtros
     */
    public List<Filtros> getListaFiltros() {
        return listaFiltros;
    }

    public void setListaFiltros(List<Filtros> listaFiltros) {
        this.listaFiltros = listaFiltros;
    }

    public Integer getContaContabil() {
        return contaContabil;
    }

    public void setContaContabil(Integer contaContabil) {
        this.contaContabil = contaContabil;
    }

    public Integer getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(Integer idGrupo) {
        this.idGrupo = idGrupo;
    }

    public Integer getIdSubGrupo() {
        return idSubGrupo;
    }

    public void setIdSubGrupo(Integer idSubGrupo) {
        this.idSubGrupo = idSubGrupo;
    }

    public List<SelectItem> getListaGrupo() {
        return listaGrupo;
    }

    public void setListaGrupo(List<SelectItem> listaGrupo) {
        this.listaGrupo = listaGrupo;
    }

    public List<SelectItem> getListaSubGrupo() {
        return listaSubGrupo;
    }

    public void setListaSubGrupo(List<SelectItem> listaSubGrupo) {
        this.listaSubGrupo = listaSubGrupo;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getDataEmissao() {
        return dataEmissao;
    }

    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    public String getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public String getDataQuitacao() {
        return dataQuitacao;
    }

    public void setDataQuitacao(String dataQuitacao) {
        this.dataQuitacao = dataQuitacao;
    }

    public String getDataImportacao() {
        return dataImportacao;
    }

    public void setDataImportacao(String dataImportacao) {
        this.dataImportacao = dataImportacao;
    }

    public String getDataCredito() {
        return dataCredito;
    }

    public void setDataCredito(String dataCredito) {
        this.dataCredito = dataCredito;
    }

    public String getDataFechamentoCaixa() {
        return dataFechamentoCaixa;
    }

    public void setDataFechamentoCaixa(String dataFechamentoCaixa) {
        this.dataFechamentoCaixa = dataFechamentoCaixa;
    }

    public Integer getIdCaixa() {
        return idCaixa;
    }

    public void setIdCaixa(Integer idCaixa) {
        this.idCaixa = idCaixa;
    }

    public List<SelectItem> getListaCaixa() {
        return listaCaixa;
    }

    public void setListaCaixa(List<SelectItem> listaCaixa) {
        this.listaCaixa = listaCaixa;
    }

    public Integer getIdOperador() {
        return idOperador;
    }

    public void setIdOperador(Integer idOperador) {
        this.idOperador = idOperador;
    }

    public List<SelectItem> getListaOperador() {
        return listaOperador;
    }

    public void setListaOperador(List<SelectItem> listaOperador) {
        this.listaOperador = listaOperador;
    }

    public Integer getIdTipoQuitacao() {
        return idTipoQuitacao;
    }

    public void setIdTipoQuitacao(Integer idTipoQuitacao) {
        this.idTipoQuitacao = idTipoQuitacao;
    }

    public List<SelectItem> getListaTipoQuitacao() {
        return listaTipoQuitacao;
    }

    public void setListaTipoQuitacao(List<SelectItem> listaTipoQuitacao) {
        this.listaTipoQuitacao = listaTipoQuitacao;
    }

    public List<ListaPlanos> getListaPlanos() {
        return listaPlanos;
    }

    public void setListaPlanos(List<ListaPlanos> listaPlanos) {
        this.listaPlanos = listaPlanos;
    }

    public Integer getIdPlano5() {
        return idPlano5;
    }

    public void setIdPlano5(Integer idPlano5) {
        this.idPlano5 = idPlano5;
    }

    public List<SelectItem> getListaPlano5() {
        return listaPlano5;
    }

    public void setListaPlano5(List<SelectItem> listaPlano5) {
        this.listaPlano5 = listaPlano5;
    }

    public boolean isChkTodos() {
        return chkTodos;
    }

    public void setChkTodos(boolean chkTodos) {
        this.chkTodos = chkTodos;
    }

    public Integer getIdCaixaBanco() {
        return idCaixaBanco;
    }

    public void setIdCaixaBanco(Integer idCaixaBanco) {
        this.idCaixaBanco = idCaixaBanco;
    }

    public List<SelectItem> getListaCaixaBanco() {
        return listaCaixaBanco;
    }

    public void setListaCaixaBanco(List<SelectItem> listaCaixaBanco) {
        this.listaCaixaBanco = listaCaixaBanco;
    }

    public boolean isChkExcel() {
        return chkExcel;
    }

    public void setChkExcel(boolean chkExcel) {
        this.chkExcel = chkExcel;
    }

    public String getDataEmissaoFinal() {
        return dataEmissaoFinal;
    }

    public void setDataEmissaoFinal(String dataEmissaoFinal) {
        this.dataEmissaoFinal = dataEmissaoFinal;
    }

    public String getDataVencimentoFinal() {
        return dataVencimentoFinal;
    }

    public void setDataVencimentoFinal(String dataVencimentoFinal) {
        this.dataVencimentoFinal = dataVencimentoFinal;
    }

    public String getDataQuitacaoFinal() {
        return dataQuitacaoFinal;
    }

    public void setDataQuitacaoFinal(String dataQuitacaoFinal) {
        this.dataQuitacaoFinal = dataQuitacaoFinal;
    }

    public String getDataImportacaoFinal() {
        return dataImportacaoFinal;
    }

    public void setDataImportacaoFinal(String dataImportacaoFinal) {
        this.dataImportacaoFinal = dataImportacaoFinal;
    }

    public String getDataCreditoFinal() {
        return dataCreditoFinal;
    }

    public void setDataCreditoFinal(String dataCreditoFinal) {
        this.dataCreditoFinal = dataCreditoFinal;
    }

    public String getDataFechamentoCaixaFinal() {
        return dataFechamentoCaixaFinal;
    }

    public void setDataFechamentoCaixaFinal(String dataFechamentoCaixaFinal) {
        this.dataFechamentoCaixaFinal = dataFechamentoCaixaFinal;
    }

    public String getTipoDepartamento() {
        return tipoDepartamento;
    }

    public void setTipoDepartamento(String tipoDepartamento) {
        this.tipoDepartamento = tipoDepartamento;
    }

    public class Filtros {

        private String chave = "";
        private String opcao = "";
        private boolean ativo = false;

        public Filtros(String chave, String opcao, boolean ativo) {
            this.chave = chave;
            this.opcao = opcao;
            this.ativo = ativo;
        }

        public String getChave() {
            return chave;
        }

        public void setChave(String chave) {
            this.chave = chave;
        }

        public String getOpcao() {
            return opcao;
        }

        public void setOpcao(String opcao) {
            this.opcao = opcao;
        }

        public boolean isAtivo() {
            return ativo;
        }

        public void setAtivo(boolean ativo) {
            this.ativo = ativo;
        }
    }

    public class ListaPlanos {

        private boolean selecionado = false;
        private Integer idPlano1 = null;
        private String conta1 = null;
        private Integer idPlano2 = null;
        private String conta2 = null;
        private Integer idPlano3 = null;
        private String conta3 = null;
        private Integer idPlano4 = null;
        private String conta4 = null;

        public ListaPlanos(boolean selecionado, Integer idPlano1, String conta1, Integer idPlano2, String conta2, Integer idPlano3, String conta3, Integer idPlano4, String conta4) {
            this.selecionado = selecionado;
            this.idPlano1 = idPlano1;
            this.conta1 = conta1;
            this.idPlano2 = idPlano2;
            this.conta2 = conta2;
            this.idPlano3 = idPlano3;
            this.conta3 = conta3;
            this.idPlano4 = idPlano4;
            this.conta4 = conta4;
        }

        public boolean isSelecionado() {
            return selecionado;
        }

        public void setSelecionado(boolean selecionado) {
            this.selecionado = selecionado;
        }

        public Integer getIdPlano1() {
            return idPlano1;
        }

        public void setIdPlano1(Integer idPlano1) {
            this.idPlano1 = idPlano1;
        }

        public String getConta1() {
            return conta1;
        }

        public void setConta1(String conta1) {
            this.conta1 = conta1;
        }

        public Integer getIdPlano2() {
            return idPlano2;
        }

        public void setIdPlano2(Integer idPlano2) {
            this.idPlano2 = idPlano2;
        }

        public String getConta2() {
            return conta2;
        }

        public void setConta2(String conta2) {
            this.conta2 = conta2;
        }

        public Integer getIdPlano3() {
            return idPlano3;
        }

        public void setIdPlano3(Integer idPlano3) {
            this.idPlano3 = idPlano3;
        }

        public String getConta3() {
            return conta3;
        }

        public void setConta3(String conta3) {
            this.conta3 = conta3;
        }

        public Integer getIdPlano4() {
            return idPlano4;
        }

        public void setIdPlano4(Integer idPlano4) {
            this.idPlano4 = idPlano4;
        }

        public String getConta4() {
            return conta4;
        }

        public void setConta4(String conta4) {
            this.conta4 = conta4;
        }

    }
}
