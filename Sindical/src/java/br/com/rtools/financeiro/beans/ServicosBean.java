package br.com.rtools.financeiro.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.CategoriaDescontoDependente;
import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.db.CategoriaDescontoDB;
import br.com.rtools.associativo.db.CategoriaDescontoDBToplink;
import br.com.rtools.associativo.db.ParentescoDB;
import br.com.rtools.associativo.db.ParentescoDao;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.financeiro.lista.ListServicosCategoriaDesconto;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Administradora;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.sistema.Periodo;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ServicosBean implements Serializable {

    private Servicos servicos;
    private ServicoValor servicoValor;
    //private ServicoValor servicoValorDetalhe;
    private CategoriaDesconto categoriaDesconto;
    private List<ServicoValor> listServicoValor;
    private List<Servicos> listServicos;
    private List<CategoriaDesconto> listCategoriaDesconto;
    private List<ListServicosCategoriaDesconto> listServicosCategoriaDesconto;
    private List<SelectItem> listSubGrupo;
    private List<SelectItem> listGrupo;
    private List<SelectItem> listPeriodo;
    private List<SelectItem> listAdministradora;
    private String porPesquisa;
    private String comoPesquisa;
    private String descPesquisa;
    private String message;
    private String valorf;
    //private String valorCategoriaDesconto;
    private String taxa;
    private String desconto;
    private String indice;
    private Integer activeIndex;
    private String textoBtnServico;
    private int idGrupo;
    private int idSubGrupo;
    private int idPeriodo;
    private Integer idAdministradora;
    private float descontoCategoria;
    private List<SelectItem> listaParentesco;
    private int id_categoria;
    private Integer indexParentesco;
    private CategoriaDescontoDependente descontoDepentende;
    private String valorDependente;
    private List<CategoriaDescontoDependente> listaDescontoDependente;
    private String situacao;

    @PostConstruct
    public void init() {
        servicos = new Servicos();
        porPesquisa = "descricao";
        comoPesquisa = "P";
        descPesquisa = "";
        message = "";
        servicoValor = new ServicoValor();
        //servicoValorDetalhe = new ServicoValor();
        valorf = "0";
        taxa = "0";
        desconto = "0";
        activeIndex = 0;
        indice = "servico";
        listServicoValor = new ArrayList<>();
        listServicos = new ArrayList<>();
        listCategoriaDesconto = new ArrayList();
        listServicosCategoriaDesconto = new ArrayList<>();
        descontoCategoria = 0;
        categoriaDesconto = new CategoriaDesconto();
        textoBtnServico = "Adicionar";
        listGrupo = new ArrayList<>();
        listSubGrupo = new ArrayList<>();
        listPeriodo = new ArrayList<>();
        listAdministradora = new ArrayList<>();
        idGrupo = 0;
        idSubGrupo = 0;
        idPeriodo = 0;
        listaParentesco = new ArrayList<>();
        descontoDepentende = new CategoriaDescontoDependente();
        indexParentesco = 0;
        id_categoria = 0;
        valorDependente = "0,00";
        listaDescontoDependente = new ArrayList<>();
        //    private String tabViewTitle = "0";
        situacao = "A";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("servicosBean");
        GenericaSessao.remove("pesquisaPlano");
        GenericaSessao.remove("pesquisaServicos");
        GenericaSessao.remove("contaCobrancaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("servicosBean");
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public Servicos getServicos() {
        if (GenericaSessao.exists("pesquisaServicos")) {
            servicos = (Servicos) GenericaSessao.getObject("pesquisaServicos", true);
            listServicosCategoriaDesconto.clear();
        }
        if (GenericaSessao.exists("pesquisaPlano")) {
            servicos.setPlano5((Plano5) GenericaSessao.getObject("pesquisaPlano", true));
        }
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void acaoInicial() {
        comoPesquisa = "I";
        listServicos.clear();
    }

    public void acaoParcial() {
        comoPesquisa = "P";
        listServicos.clear();
    }

    public void save() {
        if (servicos.getDescricao().isEmpty()) {
            message = "Informe o nome do serviço a ser cadastrado!";
            return;
        }

        if (servicos.getPlano5().getId() == -1) {
            message = "Pesquise o plano de contas antes de salvar!";
            return;
        }
        ServicosDB db = new ServicosDBToplink();
        Dao di = new Dao();
        NovoLog novoLog = new NovoLog();
        try {
            di.openTransaction();
            if (!listSubGrupo.isEmpty() && Integer.valueOf(listSubGrupo.get(idSubGrupo).getDescription()) != 0) {
                servicos.setSubGrupoFinanceiro((SubGrupoFinanceiro) di.find(new SubGrupoFinanceiro(), Integer.valueOf(listSubGrupo.get(idSubGrupo).getDescription())));
            } else {
                servicos.setSubGrupoFinanceiro(null);
            }

            if (!listPeriodo.isEmpty() && Integer.valueOf(listPeriodo.get(idPeriodo).getDescription()) != 0) {
                servicos.setPeriodo((Periodo) di.find(new Periodo(), Integer.valueOf(listPeriodo.get(idPeriodo).getDescription())));
            } else {
                servicos.setPeriodo(null);
            }
            if (!listAdministradora.isEmpty() && idAdministradora != null) {
                servicos.setAdministradora((Administradora) di.find(new Administradora(), Integer.parseInt(listAdministradora.get(idAdministradora).getDescription())));
            } else {
                servicos.setAdministradora(null);
            }

            if (servicos.getId() == -1) {
                if (db.idServicos(servicos) == null) {
                    servicos.setDepartamento((Departamento) di.find(new Departamento(), 14));
                    servicos.setFilial((Filial) di.find(new Filial(), 1));
                    novoLog.save(
                            "ID: " + servicos.getId()
                            + " - Plano5: (" + servicos.getPlano5().getId() + ") " + servicos.getPlano5().getConta()
                            + " - Descrição: " + servicos.getDescricao()
                            + " - Validade: " + servicos.getValidade()
                            + " - Adm: [" + servicos.isAdm() + "]"
                            + " - Agrupa Boleto: [" + servicos.isAgrupaBoleto() + "]"
                            + " - Altera Valor: [" + servicos.isAlterarValor() + "]"
                            + " - Débito: [" + servicos.isDebito() + "]"
                            + " - Eleição: [" + servicos.isEleicao() + "]"
                            + " - Produto: [" + servicos.isProduto() + "]"
                            + " - Tabela: [" + servicos.isTabela() + "]"
                    );
                    di.save(servicos);
                    message = "Serviço salvo com Sucesso!";
                } else {
                    message = "Este serviço já existe no Sistema.";
                }
            } else {
                Servicos s = (Servicos) di.find(servicos);
                String beforeUpdate
                        = "ID: " + s.getId()
                        + " - Plano5: (" + s.getPlano5().getId() + ") " + s.getPlano5().getConta()
                        + " - Descrição: " + s.getDescricao()
                        + " - Validade: " + s.getValidade()
                        + " - Adm: [" + s.isAdm() + "]"
                        + " - Agrupa Boleto: [" + s.isAgrupaBoleto() + "]"
                        + " - Altera Valor: [" + s.isAlterarValor() + "]"
                        + " - Débito: [" + s.isDebito() + "]"
                        + " - Eleição: [" + s.isEleicao() + "]"
                        + " - Produto: [" + s.isProduto() + "]"
                        + " - Tabela: [" + s.isTabela() + "]";
                if (di.update(servicos)) {
                    novoLog.update(beforeUpdate,
                            "ID: " + servicos.getId()
                            + " - Plano5: (" + servicos.getPlano5().getId() + ") " + servicos.getPlano5().getConta()
                            + " - Descrição: " + servicos.getDescricao()
                            + " - Validade: " + servicos.getValidade()
                            + " - Adm: [" + servicos.isAdm() + "]"
                            + " - Agrupa Boleto: [" + servicos.isAgrupaBoleto() + "]"
                            + " - Altera Valor: [" + servicos.isAlterarValor() + "]"
                            + " - Débito: [" + servicos.isDebito() + "]"
                            + " - Eleição: [" + servicos.isEleicao() + "]"
                            + " - Produto: [" + servicos.isProduto() + "]"
                            + " - Tabela: [" + servicos.isTabela() + "]"
                    );
                    message = "Serviço atualizado com sucesso!";
                } else {
                    message = "Erro na atualização do serviço!";
                }
            }
            for (CategoriaDesconto categoria : listCategoriaDesconto) {
                if (categoria.getId() == -1) {
                    if (di.save(categoria)) {
                        novoLog.save(
                                "Categoria - Desconto - ID: " + categoria.getId()
                                + " - Descrição: " + categoria.getCategoria().getCategoria()
                                + " - Serviços: (" + categoria.getServicoValor().getServicos().getId() + ") " + categoria.getServicoValor().getServicos().getDescricao()
                                + " - Desconto: " + categoria.getDesconto()
                        );
                    }
                } else {
                    CategoriaDesconto cd = (CategoriaDesconto) di.find(categoria);
                    String beforeUpdate
                            = "Categoria - Desconto - ID: " + cd.getId()
                            + " - Descrição: " + cd.getCategoria().getCategoria()
                            + " - Serviços: (" + cd.getServicoValor().getServicos().getId() + ") " + cd.getServicoValor().getServicos().getDescricao()
                            + " - Desconto: " + cd.getDesconto();
                    if (di.update(categoria)) {
                        novoLog.update(beforeUpdate,
                                "Categoria - Desconto - ID: " + categoria.getId()
                                + " - Descrição: " + categoria.getCategoria().getCategoria()
                                + " - Serviços: (" + categoria.getServicoValor().getServicos().getId() + ") " + categoria.getServicoValor().getServicos().getDescricao()
                                + " - Desconto: " + categoria.getDesconto()
                        );
                    }
                }
            }
            di.commit();
        } catch (Exception e) {
            di.rollback();
            message = "Erro no cadastro de serviço!";
        }
    }

    public String novo() {
//        servicos = new Servicos();
//        listCategoriaDesconto.clear();
//        listServicoValor.clear();
//        listServicos.clear();
//        valorf = "0";
//        desconto = "0";
//        taxa = "0";
//        activeIndex = 0; 
////        tabViewTitle = "0";

        //GenericaSessao.newSessionBean("servicosBean", new ServicosBean());
        GenericaSessao.put("servicosBean", new ServicosBean());

        GenericaSessao.remove("contaCobrancaPesquisa");

        return null;
    }

    public String novox() {
        return "servicos";
    }

    public String edit(Servicos s) {
        idAdministradora = null;
        servicos = s;
        GenericaSessao.put("pesquisaServicos", servicos);
        GenericaSessao.put("linkClicado", true);
        servicoValor = new ServicoValor();
        valorf = "0";
        desconto = "0";
        taxa = "0";

        //List<SubGrupoFinanceiro> listax = (new SalvarAcumuladoDBToplink().listaObjeto("SubGrupoFinanceiro"));
        if (servicos.getSubGrupoFinanceiro() != null && !getListSubGrupo().isEmpty()) {
            listSubGrupo.clear();
            for (int i = 0; i < listGrupo.size(); i++) {
                //for(SubGrupoFinanceiro sgf : listax){
                if (Integer.valueOf(listGrupo.get(i).getDescription()) == servicos.getSubGrupoFinanceiro().getGrupoFinanceiro().getId()) {
                    idGrupo = i;
                }
                //}
            }

            getListSubGrupo();
            for (int i = 0; i < listSubGrupo.size(); i++) {
                if (Integer.valueOf(listSubGrupo.get(i).getDescription()) == servicos.getSubGrupoFinanceiro().getId()) {
                    idSubGrupo = i;
                }
            }
        } else {
            listSubGrupo.clear();
            listGrupo.clear();
            idGrupo = 0;
            idSubGrupo = 0;
        }

        if (servicos.getAdministradora() != null) {
            for (int i = 0; i < listAdministradora.size(); i++) {
                if (Integer.parseInt(listAdministradora.get(i).getDescription()) == servicos.getAdministradora().getId()) {
                    idAdministradora = i;
                }
            }
        }

        if (servicos.getPeriodo() != null) {
            getListPeriodo();
            for (int i = 0; i < listPeriodo.size(); i++) {
                if (Integer.valueOf(listPeriodo.get(i).getDescription()) == servicos.getPeriodo().getId()) {
                    idPeriodo = i;
                }
            }
        } else {
            idPeriodo = 0;
        }
        if (GenericaSessao.exists("urlRetorno")) {
            return "servicos";
        } else {
            return GenericaSessao.getString("urlRetorno");
        }
    }

    public void delete() {
        if (servicos.getId() != -1) {
            Dao di = new Dao();
            NovoLog novoLog = new NovoLog();
            if (!listServicoValor.isEmpty()) {
                message = "Existem valores cadastrados neste serviço!";
                return;
            }
            try {
                for (CategoriaDesconto cd : listCategoriaDesconto) {
                    if (cd.getId() != -1) {
                        if (di.delete(cd, true)) {
                            novoLog.delete(
                                    "Categoria - Desconto - ID: " + cd.getId()
                                    + " - Descrição: " + cd.getCategoria().getCategoria()
                                    + " - Serviços: (" + cd.getServicoValor().getServicos().getId() + ") " + cd.getServicoValor().getServicos().getDescricao()
                                    + " - Desconto: " + cd.getDesconto()
                            );
                        }
                    }
                }
                di.openTransaction();
                if (di.delete(servicos)) {
                    novoLog.delete(
                            "ID: " + servicos.getId()
                            + " - Plano5: (" + servicos.getPlano5().getId() + ") " + servicos.getPlano5().getConta()
                            + " - Descrição: " + servicos.getDescricao()
                            + " - Validade: " + servicos.getValidade()
                            + " - Adm: [" + servicos.isAdm() + "]"
                            + " - Agrupa Boleto: [" + servicos.isAgrupaBoleto() + "]"
                            + " - Altera Valor: [" + servicos.isAlterarValor() + "]"
                            + " - Débito: [" + servicos.isDebito() + "]"
                            + " - Eleição: [" + servicos.isEleicao() + "]"
                            + " - Produto: [" + servicos.isProduto() + "]"
                            + " - Tabela: [" + servicos.isTabela() + "]"
                    );
                    di.commit();
                    message = "Cadastro excluido com sucesso!";
                } else {
                    di.rollback();
                    message = "Erro cadastro não pode ser excluído!";
                }
            } catch (Exception e) {
                message = "Erro cadastro não pode ser excluído!";
            }
        }
        novo();
    }

    public String pesquisaContaCobranca() {
        GenericaSessao.put("urlRetorno", "servicos");
        return "pesquisaContaCobranca";
    }

    public String pesquisarServicos() {
        GenericaSessao.put("urlRetorno", "servicos");
        descPesquisa = "";
        return "pesquisaServicos";
    }

    public List<Servicos> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            setListServicos((List<Servicos>) db.pesquisaServicos(descPesquisa, porPesquisa, comoPesquisa, situacao));
        }
        return listServicos;
    }

    public List<ServicoValor> getListServicoValor() {
        //servicoValorDetalhe = new ServicoValor();
        ServicoValorDB servicoValorDB = new ServicoValorDBToplink();
        listServicoValor.clear();
        listServicoValor = servicoValorDB.pesquisaServicoValor(servicos.getId());
        if (listServicoValor == null) {
            listServicoValor = new ArrayList<>();
        } else {
            if (!listServicoValor.isEmpty()) {
            //    servicoValorDetalhe = listServicoValor.get(0);
                //valorCategoriaDesconto = Moeda.converteR$Float(servicoValorDetalhe.getValor());
            }
        }
        return listServicoValor;
    }

    public void setListServicoValor(List<ServicoValor> listServicoValor) {
        this.listServicoValor = listServicoValor;
    }

    public boolean getDesabilitaValor() {
        return servicos.getId() == -1;
    }

    public void saveServicoValor() {
        Dao di = new Dao();
        NovoLog novoLog = new NovoLog();
        servicoValor.setValor(Moeda.substituiVirgulaFloat(valorf));
        servicoValor.setTaxa(Moeda.substituiVirgulaFloat(taxa));
        servicoValor.setDescontoAteVenc(Moeda.substituiVirgulaFloat(desconto));
        boolean existeValor = true;
        if (existeValor) {
            di.openTransaction();
            if (servicoValor.getId() == -1) {
                servicoValor.setServicos(servicos);
                if (di.save(servicoValor)) {
                    novoLog.save(
                            "Serviço Valor - "
                            + "ID: " + servicoValor.getId()
                            + " - Serviços: (" + servicoValor.getServicos().getId() + ") " + servicoValor.getServicos().getDescricao()
                            + " - Valor: (" + servicoValor.getValor()
                            + " - Desconto: " + servicoValor.getDescontoAteVenc()
                            + " - Taxa: " + servicoValor.getTaxa()
                            + " - Idade: " + servicoValor.getIdadeIni() + " - " + servicoValor.getIdadeFim()
                    );
                    
                    List<Categoria> listc = new Dao().list(new Categoria());
                    for (Categoria c : listc){
                        CategoriaDesconto cd = new CategoriaDesconto(-1, c, 0, servicoValor);
                        di.save(cd);
                    }
                    di.commit();
                    GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                    listServicoValor.clear();
                    servicoValor = new ServicoValor();
                    valorf = "0";
                    desconto = "0";
                    taxa = "0";
                } else {
                    di.rollback();
                    GenericaMensagem.warn("Validação", "Este valor para o serviço já existe no sistema.");
                }
            } else {
                ServicoValor sv = (ServicoValor) di.find(servicoValor);
                String beforeUpdate
                        = "Serviço Valor - "
                        + "ID: " + sv.getId()
                        + " - Serviços: (" + sv.getServicos().getId() + ")" + sv.getServicos().getDescricao()
                        + " - Valor: (" + sv.getValor()
                        + " - Desconto: " + sv.getDescontoAteVenc()
                        + " - Taxa: " + sv.getTaxa()
                        + " - Idade: " + sv.getIdadeIni() + " - " + sv.getIdadeFim();
                if (di.update(servicoValor)) {
                    novoLog.update(beforeUpdate,
                            "Serviço Valor - "
                            + "ID: " + servicoValor.getId()
                            + " - Serviços: (" + servicoValor.getServicos().getId() + ") " + servicoValor.getServicos().getDescricao()
                            + " - Valor: (" + servicoValor.getValor()
                            + " - Desconto: " + servicoValor.getDescontoAteVenc()
                            + " - Taxa: " + servicoValor.getTaxa()
                            + " - Idade: " + servicoValor.getIdadeIni() + " - " + servicoValor.getIdadeFim()
                    );
                    List<Categoria> listc = new Dao().list(new Categoria());
                    CategoriaDescontoDB db = new CategoriaDescontoDBToplink();
                    
                    for (Categoria c : listc){
                        if (db.listaCategoriaDescontoCategoriaServicoValor(c.getId(), servicoValor.getId()).isEmpty()){
                            CategoriaDesconto cd = new CategoriaDesconto(-1, c, 0, servicoValor);
                            di.save(cd);
                        }
                    }
                    di.commit();
                    GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                    listServicoValor.clear();
                    servicoValor = new ServicoValor();
                    valorf = "0";
                    desconto = "0";
                    taxa = "0";
                    textoBtnServico = "Adicionar";
                } else {
                    GenericaMensagem.warn("Erro", "Ao atualizar registro!");
                    di.rollback();
                }
            }
            listServicosCategoriaDesconto.clear();
        } else {
            GenericaMensagem.warn("Validação", "Informar o valor / taxa!");
        }
        setActiveIndex(1);
    }

    public String clearServicoValor() {
        servicoValor = new ServicoValor();
        valorf = "0";
        desconto = "0";
        taxa = "0";
        setActiveIndex(1);
        textoBtnServico = "Adicionar";
        listServicoValor.clear();
        return null;
    }

    public void editServicoValor(ServicoValor sv) {
        servicoValor = sv;
        valorf = Moeda.converteR$Float(servicoValor.getValor());
        desconto = Moeda.converteR$Float(servicoValor.getDescontoAteVenc());
        taxa = Moeda.converteR$Float(servicoValor.getTaxa());
        textoBtnServico = "Atualizar";
        setActiveIndex(1);
    }

    public void removeServicoValor() {
        removeServicoValor(null);
    }

    public void removeServicoValor(ServicoValor sv) {
        Dao di = new Dao();
        if (sv != null) {
            if (sv.getId() != -1) {
                servicoValor = sv;
            }
        }
        
        NovoLog novoLog = new NovoLog();
        textoBtnServico = "Adicionar";
        if (servicoValor.getId() != -1) {
            CategoriaDescontoDB db = new CategoriaDescontoDBToplink();
            List<CategoriaDesconto> listc = db.listaCategoriaDescontoServicoValor(servicoValor.getId());
            
            for (CategoriaDesconto cd : listc){
                if (!di.delete(di.find(cd))){
                    di.rollback();
                    GenericaMensagem.error("Erro", "Não foi possível excluir registro!");
                    return;
                }
            }
            
            if (di.delete(servicoValor, false)) {
                novoLog.delete(
                        "Serviço Valor - "
                        + "ID: " + servicoValor.getId()
                        + " - Serviços: (" + servicoValor.getServicos().getId() + ") " + servicoValor.getServicos().getDescricao()
                        + " - Valor: (" + servicoValor.getValor()
                        + " - Desconto: " + servicoValor.getDescontoAteVenc()
                        + " - Taxa: " + servicoValor.getTaxa()
                        + " - Idade: " + servicoValor.getIdadeIni() + " - " + servicoValor.getIdadeFim()
                );
                di.commit();
                servicoValor = new ServicoValor();
                valorf = "0";
                desconto = "0";
                taxa = "0";
                listServicoValor.clear();
                GenericaMensagem.info("Sucesso", "Registro excluido com sucesso");
                textoBtnServico = "Adicionar";
            } else {
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
        setActiveIndex(1);
    }

    public ServicoValor getServicoValor() {
        return servicoValor;
    }

    public void setServicoValor(ServicoValor servicoValor) {
        this.servicoValor = servicoValor;
    }

    public String getValorf() {
        if (valorf.isEmpty()) {
            valorf = "0";
        }
        return Moeda.converteR$(valorf);
    }

    public void setValorf(String valorf) {
        if (!valorf.isEmpty()) {
            if (AnaliseString.isInteger(valorf)) {
                this.valorf = Moeda.substituiVirgula(valorf);
            } else if (AnaliseString.isFloat(valorf)) {
                this.valorf = Moeda.substituiVirgula(valorf);
            } else {
                this.valorf = Moeda.substituiVirgula("0");
            }
        } else {
            this.valorf = Moeda.substituiVirgula("0");
        }
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (!desconto.isEmpty()) {
            if (AnaliseString.isInteger(desconto)) {
                this.desconto = Moeda.substituiVirgula(desconto);
            } else if (AnaliseString.isFloat(desconto)) {
                this.desconto = Moeda.substituiVirgula(desconto);
            } else {
                this.desconto = Moeda.substituiVirgula("0");
            }
        } else {
            this.desconto = Moeda.substituiVirgula("0");
        }
        if (Float.parseFloat(this.desconto) > 100) {
            this.desconto = "100,00";
        }
    }

    public String getTaxa() {
        if (taxa.isEmpty()) {
            taxa = "0";
        }
        return Moeda.converteR$(taxa);
    }

    public void setTaxa(String taxa) {
        if (!taxa.isEmpty()) {
            if (AnaliseString.isInteger(taxa)) {
                this.taxa = Moeda.substituiVirgula(taxa);
            } else if (AnaliseString.isFloat(taxa)) {
                this.taxa = Moeda.substituiVirgula(taxa);
            } else {
                this.taxa = Moeda.substituiVirgula("0");
            }
        } else {
            this.taxa = Moeda.substituiVirgula("0");
        }
    }

    public String getIndice() {
        return indice;
    }

    public void setIndice(String indice) {
        this.indice = indice;
    }

    public float getDescontoCategoria() {
        return descontoCategoria;
    }

    public void setDescontoCategoria(float descontoCategoria) {
        this.descontoCategoria = descontoCategoria;
    }

    public List<CategoriaDesconto> getListCategoriaDesconto() {
        CategoriaDescontoDB categoriaDescontoDB = new CategoriaDescontoDBToplink();
        List<Categoria> listaCategoria = categoriaDescontoDB.pesquisaCategoriasSemServico(servicos.getId());
        listCategoriaDesconto = categoriaDescontoDB.pesquisaTodosPorServico(servicos.getId());
        if (listCategoriaDesconto == null) {
            listCategoriaDesconto = new ArrayList();
        }
        if ((listaCategoria != null) && (this.servicos.getId() != -1)) {
            for (Categoria c : listaCategoria) {
                listCategoriaDesconto.add(new CategoriaDesconto(-1, c, 0, servicoValor)); // AQUI VERIFICAR
            }
        }
        return listCategoriaDesconto;
    }

    public void setListCategoriaDesconto(List listaCategoriaDesconto) {
        this.listCategoriaDesconto = listaCategoriaDesconto;
    }

    public void updateDescontoCategoriaPercentual(ListServicosCategoriaDesconto lscd) {
        float v = 0;
        for (int i = 0; i < listServicosCategoriaDesconto.size(); i++) {
            if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getId() == lscd.getCategoriaDesconto().getId()) {
                if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getDesconto() > 100) {
                    listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(100);
                }
                v = listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getServicoValor().getValor() - (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getDesconto() / 100) * listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getServicoValor().getValor();
                listServicosCategoriaDesconto.get(i).setValorDesconto(Moeda.converteFloatR$Float(v));
            }
        }
//        for (int i = 0; i < listServicosCategoriaDesconto.size(); i++) {
//            if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getCategoria().getId() == lscd.getCategoriaDesconto().getCategoria().getId()) {
//                if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getDesconto() > 100) {
//                    listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(100);
//                }
//                v = servicoValorDetalhe.getValor() - (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getDesconto() / 100) * servicoValorDetalhe.getValor();
//                listServicosCategoriaDesconto.get(i).setValorDesconto(Moeda.converteFloatR$Float(v));
//            }
//        }
    }

    public void updateDescontoCategoriaValor(ListServicosCategoriaDesconto lscd) {
        //float v = 0;
        float v1 = 0;
        float v2 = 0;
//        200 + 200 * p/100 = 300 
//        200 * p/100 = 100 
//        p/100 = 100/200 
//        p/100 = 1/2 
//        p= 50 

        for (int i = 0; i < listServicosCategoriaDesconto.size(); i++) {
            if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getId() == lscd.getCategoriaDesconto().getId()) {
                if (listServicosCategoriaDesconto.get(i).getValorDesconto() <= listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getServicoValor().getValor()) {
                    v1 = Moeda.subtracaoValores(listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getServicoValor().getValor(), listServicosCategoriaDesconto.get(i).getValorDesconto());
                    v2 = Moeda.multiplicarValores(Moeda.divisaoValores(v1, listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getServicoValor().getValor()), 100);
                    listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(Moeda.converteFloatR$Float(v2));
                } else {
                    updateDescontoCategoriaPercentual(lscd);
                }
                //listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(v);
            }
        }
        
        // METODO ANTERIOR QUE PEGAVA O VALOR TOTAL DO SERVIÇO
//        for (int i = 0; i < listServicosCategoriaDesconto.size(); i++) {
//            if (listServicosCategoriaDesconto.get(i).getCategoriaDesconto().getCategoria().getId() == lscd.getCategoriaDesconto().getCategoria().getId()) {
//                if (listServicosCategoriaDesconto.get(i).getValorDesconto() <= servicoValorDetalhe.getValor()) {
//                    v1 = Moeda.subtracaoValores(servicoValorDetalhe.getValor(), listServicosCategoriaDesconto.get(i).getValorDesconto());
//                    v2 = Moeda.multiplicarValores(Moeda.divisaoValores(v1, servicoValorDetalhe.getValor()), 100);
//                    listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(Moeda.converteFloatR$Float(v2));
//                } else {
//                    updateDescontoCategoriaPercentual(lscd);
//                }
//                //listServicosCategoriaDesconto.get(i).getCategoriaDesconto().setDesconto(v);
//            }
//        }
    }

    public CategoriaDesconto getCategoriaDesconto() {
        return categoriaDesconto;
    }

    public void setCategoriaDesconto(CategoriaDesconto categoriaDesconto) {
        this.categoriaDesconto = categoriaDesconto;
    }

    public void setListServicos(List<Servicos> listaServicos) {
        this.listServicos = listaServicos;
    }

    public String getTextoBtnServico() {
        return textoBtnServico;
    }

    public void setTextoBtnServico(String textoBtnServico) {
        this.textoBtnServico = textoBtnServico;
    }

    public List<SelectItem> getListGrupo() {
        if (listGrupo.isEmpty()) {
            Dao di = new Dao();
            List<GrupoFinanceiro> result = di.list(new GrupoFinanceiro());

            listGrupo.add(new SelectItem(0, "Nenhum Grupo Financeiro Adicionado", "0"));
            if (!result.isEmpty()) {
//                if (servicos.getSubGrupoFinanceiro() == null)
//                    listGrupo.add(new SelectItem(10000, "Nenhum Grupo Financeiro Adicionado", "0"));
                for (int i = 0; i < result.size(); i++) {
                    listGrupo.add(new SelectItem(i + 1,
                            result.get(i).getDescricao(),
                            Integer.toString(result.get(i).getId()))
                    );
                }
            }
        }
        return listGrupo;
    }

    public void setListGrupo(List<SelectItem> listGrupo) {
        this.listGrupo = listGrupo;
    }

    public int getIdGrupo() {
        return idGrupo;
    }

    public void setIdGrupo(int idGrupo) {
        this.idGrupo = idGrupo;
    }

    public List<SelectItem> getListSubGrupo() {
        if (listSubGrupo.isEmpty()) {
            FinanceiroDB db = new FinanceiroDBToplink();

            List<SubGrupoFinanceiro> result = db.listaSubGrupo(Integer.valueOf(getListGrupo().get(idGrupo).getDescription()));
            listSubGrupo.add(new SelectItem(0, "Nenhum Sub Grupo Financeiro Encontrado", "0"));
            if (!result.isEmpty()) {
//                if (servicos.getSubGrupoFinanceiro() == null)
//                    listSubGrupo.add(new SelectItem(0, "Nenhum Sub Grupo Financeiro Encontrado", "0"));
                for (int i = 0; i < result.size(); i++) {
                    listSubGrupo.add(new SelectItem(i + 1,
                            result.get(i).getDescricao(),
                            Integer.toString(result.get(i).getId()))
                    );
                }
            }

        }
        return listSubGrupo;
    }

    public void setListSubGrupo(List<SelectItem> listSubGrupo) {
        this.listSubGrupo = listSubGrupo;
    }

    public int getIdSubGrupo() {
        return idSubGrupo;
    }

    public void setIdSubGrupo(int idSubGrupo) {
        this.idSubGrupo = idSubGrupo;
    }

//    public ServicoValor getServicoValorDetalhe() {
//        return servicoValorDetalhe;
//    }
//
//    public void setServicoValorDetalhe(ServicoValor servicoValorDetalhe) {
//        this.servicoValorDetalhe = servicoValorDetalhe;
//    }

    public String valorCategoriaDesconto(CategoriaDesconto cd) {
        // Quanto é 50% de 1000?
        // É 0,5 multiplicado por 1000 => 0,5  x 1000 = 500
        // Y = 1000 + 0,2  x 1000
        float v = 0;
        if (cd.getServicoValor().getId() != -1) {
            //v = servicoValorDetalhe.getValor() + (cd.getDesconto() / 100) * servicoValorDetalhe.getValor();
            v = cd.getServicoValor().getValor() + (cd.getDesconto() / 100) * cd.getServicoValor().getValor();
            return Moeda.converteR$Float(v);
        }
        return "0,00";
    }

    public List<ListServicosCategoriaDesconto> getListServicosCategoriaDesconto() {
        if (listServicosCategoriaDesconto.isEmpty()) {
            List<CategoriaDesconto> cds = getListCategoriaDesconto();
            ListServicosCategoriaDesconto lscd = new ListServicosCategoriaDesconto();
            float v = 0;
            for (CategoriaDesconto cd : cds) {
                lscd.setCategoriaDesconto(cd);
                //v = servicoValorDetalhe.getValor() - (cd.getDesconto() / 100) * servicoValorDetalhe.getValor();
                v = cd.getServicoValor().getValor() - (cd.getDesconto() / 100) * cd.getServicoValor().getValor();
                lscd.setValorDesconto(v);
                listServicosCategoriaDesconto.add(lscd);
                lscd = new ListServicosCategoriaDesconto();
                v = 0;
            }
        }
        return listServicosCategoriaDesconto;
    }

    public void setListServicosCategoriaDesconto(List<ListServicosCategoriaDesconto> listServicosCategoriaDesconto) {
        this.listServicosCategoriaDesconto = listServicosCategoriaDesconto;
    }

    public Integer getActiveIndex() {
        return activeIndex;
    }

    public void setActiveIndex(Integer activeIndex) {
        this.activeIndex = activeIndex;
    }

    public int getIdPeriodo() {
        return idPeriodo;
    }

    public void setIdPeriodo(int idPeriodo) {
        this.idPeriodo = idPeriodo;
    }

    public List<SelectItem> getListPeriodo() {
        if (listPeriodo.isEmpty()) {
            FinanceiroDB db = new FinanceiroDBToplink();

            List<Periodo> result = (new SalvarAcumuladoDBToplink()).listaObjeto("Periodo");
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

            listPeriodo.add(new SelectItem(0, "Selecionar Periodo", "0"));
            if (!result.isEmpty()) {
                for (int i = 1; i < result.size(); i++) {
                    listPeriodo.add(
                            new SelectItem(i,
                                    result.get(i).getDescricao(),
                                    Integer.toString(result.get(i).getId())
                            )
                    );
                }
            }
        }
        return listPeriodo;
    }

    public void setListPeriodo(List<SelectItem> listPeriodo) {
        this.listPeriodo = listPeriodo;
    }

    public void openDescontoDependente(CategoriaDesconto cd) {
        if (cd.getId() == -1) {
            GenericaMensagem.error("Atenção", "Salve o Serviço antes de Adicionar um Desconto");
        }

        this.id_categoria = cd.getCategoria().getId();
        indexParentesco = 0;
        listaParentesco.clear();
        listaDescontoDependente.clear();
        valorDependente = cd.getServicoValor().getValorString();//servicoValorDetalhe.getValorString();
        categoriaDesconto = cd;
        updateDescontoCategoriaDependenteValor();
        
        //oncomplete="PF('dlg_desconto_dependente').show()" update=":form_servicos:i_desconto_dependente"
        PF.update("form_servicos:i_desconto_dependente");
        PF.openDialog("dlg_desconto_dependente");
    }

    public List<SelectItem> getListaParentesco() {
        if (listaParentesco.isEmpty()) {
            ParentescoDB db = new ParentescoDao();
            //List<Parentesco> select = db.pesquisaTodosSemTitularCategoria(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()));
            List<Parentesco> select = db.pesquisaTodosComTitularCategoriaSemDesconto(id_categoria, categoriaDesconto.getId());
            //List<Parentesco> select = db.pesquisaTodosSemTitular();
            listaParentesco.add(new SelectItem(0, "Selecione um parentesco", "0"));
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaParentesco.add(new SelectItem(i + 1, select.get(i).getParentesco(), "" + select.get(i).getId()));
                }
            }
            return listaParentesco;
        }
        return listaParentesco;
    }

    public void updateDescontoCategoriaDependentePercentual() {
        float v = 0;

        if (descontoDepentende.getDesconto() > 100) {
            descontoDepentende.setDesconto(100);
        }
        //v = servicoValorDetalhe.getValor() - (descontoDepentende.getDesconto() / 100) * servicoValorDetalhe.getValor();
        v = categoriaDesconto.getServicoValor().getValor() - (descontoDepentende.getDesconto() / 100) * categoriaDesconto.getServicoValor().getValor();
        //descontoDepentende.setDesconto(Moeda.converteFloatR$Float(v));
        valorDependente = Moeda.converteR$Float(v);
    }

    public String linhaDescontoCategoriaDependentePercentual(float desconto) {
        float v = 0;
        //v = servicoValorDetalhe.getValor() - (desconto / 100) * servicoValorDetalhe.getValor();
        v = categoriaDesconto.getServicoValor().getValor() - (desconto / 100) * categoriaDesconto.getServicoValor().getValor();
        return Moeda.converteR$Float(v);
    }

    public void updateDescontoCategoriaDependenteValor() {
        float v = 0;
        float v1 = 0;
        float v2 = 0;
        if (Moeda.converteUS$(valorDependente) <= categoriaDesconto.getServicoValor().getValor()) {
            v1 = Moeda.subtracaoValores(categoriaDesconto.getServicoValor().getValor(), Moeda.converteUS$(valorDependente));
            v2 = Moeda.multiplicarValores(Moeda.divisaoValores(v1, categoriaDesconto.getServicoValor().getValor()), 100);
            descontoDepentende.setDesconto(Moeda.converteFloatR$Float(v2));
        } else {
            updateDescontoCategoriaDependentePercentual();
        }
    }

    public void adicionarDescontoDependente() {
        Dao di = new Dao();

        if (Integer.valueOf(listaParentesco.get(indexParentesco).getDescription()) == 0) {
            GenericaMensagem.error("Erro", "Selecione um Parentesco para adicionar Desconto!");
            return;
        }
        Parentesco par = (Parentesco) di.find(new Parentesco(), Integer.valueOf(listaParentesco.get(indexParentesco).getDescription()));
        if (!listaDescontoDependente.isEmpty()) {
            CategoriaDescontoDB db = new CategoriaDescontoDBToplink();
            if (db.pesquisaDescontoDependentePorCategoria(par.getId(), categoriaDesconto.getId()) != null) {
                GenericaMensagem.warn("Atenção", "Esse parentesco já existe para esta categoria!");
                return;
            }
        }
        di.openTransaction();

        descontoDepentende.setParentesco(par);
        descontoDepentende.setCategoriaDesconto(categoriaDesconto);

        if (!di.save(descontoDepentende)) {
            GenericaMensagem.error("Erro", "Não foi possivel salvar Desconto!");
            di.rollback();
            return;
        }

        descontoDepentende = new CategoriaDescontoDependente();
        valorDependente = categoriaDesconto.getServicoValor().getValorString();
        listaDescontoDependente.clear();
        indexParentesco = 0;
        listaParentesco.clear();
        di.commit();
    }

    public void adicionarDescontoDependenteTodosParentesco() {
        Dao di = new Dao();

        if (listaParentesco.size() == 1 && Integer.valueOf(listaParentesco.get(0).getDescription()) == 0) {
            GenericaMensagem.error("Erro", "Não existe lista de Parentesco para ser adicionada!");
            return;
        }

        di.openTransaction();
        float descontox = descontoDepentende.getDesconto();
        //float valorx = descontoDepentende.getDesconto();
        for (SelectItem si : listaParentesco) {
            Parentesco par = (Parentesco) di.find(new Parentesco(), Integer.valueOf(si.getDescription()));
            if (par != null) {
                CategoriaDescontoDB db = new CategoriaDescontoDBToplink();

                if (db.pesquisaDescontoDependentePorCategoria(par.getId(), categoriaDesconto.getId()) == null) {
                    descontoDepentende.setParentesco(par);
                    descontoDepentende.setCategoriaDesconto(categoriaDesconto);

                    if (!di.save(descontoDepentende)) {
                        GenericaMensagem.error("Erro", "Não foi possivel salvar Desconto!");
                        di.rollback();
                        return;
                    }
                }
                descontoDepentende = new CategoriaDescontoDependente();
                descontoDepentende.setDesconto(descontox);
            }
        }

        valorDependente = categoriaDesconto.getServicoValor().getValorString();//servicoValorDetalhe.getValorString();
        listaDescontoDependente.clear();
        indexParentesco = 0;
        listaParentesco.clear();
        di.commit();
    }

    public void deletarDescontoDependente(CategoriaDescontoDependente cdd) {
        Dao di = new Dao();

        di.openTransaction();

        if (!di.delete(di.find(cdd, cdd.getId()))) {
            GenericaMensagem.error("Erro", "Não foi possivel deletar Desconto!");
            di.rollback();
            return;
        }

        listaDescontoDependente.clear();
        indexParentesco = 0;
        listaParentesco.clear();
        di.commit();
    }

    public void deletarDescontoDependenteTodos() {
        Dao di = new Dao();
        di.openTransaction();

        for (CategoriaDescontoDependente cdd : listaDescontoDependente) {
            if (!di.delete(di.find(cdd, cdd.getId()))) {
                GenericaMensagem.error("Erro", "Não foi possivel deletar Desconto!");
                di.rollback();
                return;
            }
        }
        listaDescontoDependente.clear();
        indexParentesco = 0;
        listaParentesco.clear();
        di.commit();
    }

    public void setListaParentesco(List<SelectItem> listaParentesco) {
        this.listaParentesco = listaParentesco;
    }

    public Integer getIndexParentesco() {
        return indexParentesco;
    }

    public void setIndexParentesco(Integer indexParentesco) {
        this.indexParentesco = indexParentesco;
    }

    public CategoriaDescontoDependente getDescontoDepentende() {
        return descontoDepentende;
    }

    public void setDescontoDepentende(CategoriaDescontoDependente descontoDepentende) {
        this.descontoDepentende = descontoDepentende;
    }

    public String getValorDependente() {
        return Moeda.converteR$(valorDependente);
    }

    public void setValorDependente(String valorDependente) {
        this.valorDependente = Moeda.converteR$(valorDependente);
    }

    public List<CategoriaDescontoDependente> getListaDescontoDependente() {
        if (listaDescontoDependente.isEmpty() && categoriaDesconto.getId() != -1) {
            CategoriaDescontoDB db = new CategoriaDescontoDBToplink();
            listaDescontoDependente = db.listaDescontoDependentePorCategoria(categoriaDesconto.getId());
        }
        return listaDescontoDependente;
    }

    public void setListaDescontoDependente(List<CategoriaDescontoDependente> listaDescontoDependente) {
        this.listaDescontoDependente = listaDescontoDependente;
    }

    public List<SelectItem> getListAdministradora() {
        if (listAdministradora.isEmpty()) {
            Dao dao = new Dao();
            List<Administradora> list = dao.list(new Administradora(), true);
            for (int i = 0; i < list.size(); i++) {
                listAdministradora.add(new SelectItem(i, list.get(i).getPessoa().getNome(), "" + list.get(i).getId()));
            }
        }
        return listAdministradora;
    }

    public void setListAdministradora(List<SelectItem> listAdministradora) {
        this.listAdministradora = listAdministradora;
    }

    public Integer getIdAdministradora() {
        return idAdministradora;
    }

    public void setIdAdministradora(Integer idAdministradora) {
        this.idAdministradora = idAdministradora;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}
