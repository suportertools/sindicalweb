package br.com.rtools.financeiro.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.db.CategoriaDescontoDB;
import br.com.rtools.associativo.db.CategoriaDescontoDBToplink;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.utilitarios.AnaliseString;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;
//import org.primefaces.component.tabview.Tab;
//import org.primefaces.event.TabChangeEvent;

public class ServicosJSFBean implements java.io.Serializable {

    private Servicos servicos = new Servicos();
    private Plano5 plano5 = new Plano5();
    private String porPesquisa = "descricao";
    private String comoPesquisa = "P";
    private String descPesquisa = "";
    private String mensagem;
    private ServicoValor servicoValor = new ServicoValor();
    private String valorf = "0";
    private String taxa = "0";
    private String desconto = "0";
    private String indice = "servico";
    private List<ServicoValor> listaServicoValor = new ArrayList();
    private List<Servicos> listaServicos = new ArrayList();
    private List<CategoriaDesconto> listaCategoriaDesconto = new ArrayList();
    private float descontoCategoria = 0;
    private CategoriaDesconto categoriaDesconto = new CategoriaDesconto();
    private String textoBtnServico = "Adicionar";
//    private String tabViewTitle = "0";

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public Servicos getServicos() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaServicos") != null) {
            servicos = (Servicos) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaServicos");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaServicos");
        }
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaPlano") != null) {
            servicos.setPlano5((Plano5) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaPlano"));
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaPlano");
        }
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public void acaoInicial() {
        comoPesquisa = "I";
        listaServicos.clear();
    }

    public void acaoParcial() {
        comoPesquisa = "P";
        listaServicos.clear();
    }

    public void salvar() {
        if (servicos.getDescricao().equals("")) {
            mensagem = "Informe o nome do serviço a ser cadastrado!";
            return;
        }

        if (servicos.getPlano5().getId() == -1) {
            mensagem = "Pesquise o plano de contas antes de salvar!";
            return;
        }
        ServicosDB db = new ServicosDBToplink();
        SalvarAcumuladoDB salvar = new SalvarAcumuladoDBToplink();
        try {
            salvar.abrirTransacao();
            if (servicos.getId() == -1) {
                if (db.idServicos(servicos) == null) {
                    servicos.setDepartamento((Departamento) salvar.pesquisaCodigo(14, "Departamento"));
                    servicos.setFilial((Filial) salvar.pesquisaCodigo(1, "Filial"));
                    salvar.inserirObjeto(servicos);
                    mensagem = "Serviço salvo com Sucesso!";
                } else {
                    mensagem = "Este serviço já existe no Sistema.";
                }
            } else {
                if (salvar.alterarObjeto(servicos)) {
                    mensagem = "Serviço atualizado com sucesso!";
                } else {
                    mensagem = "Erro na atualização do serviço!";
                }
            }
            for (CategoriaDesconto categoria : listaCategoriaDesconto) {
                if (categoria.getId() == -1) {
                    salvar.inserirObjeto(categoria);
                } else {
                    salvar.alterarObjeto(categoria);
                }

            }
            salvar.comitarTransacao();
        } catch (Exception e) {
            salvar.desfazerTransacao();
            mensagem = "Erro no cadastro de serviço!";
        }
    }

    public void novo() {
        servicos = new Servicos();
        listaCategoriaDesconto.clear();
        listaServicoValor.clear();
        listaServicos.clear();
        valorf = "0";
        desconto = "0";
        taxa = "0";
//        tabViewTitle = "0";
        GenericaSessao.remove("contaCobrancaPesquisa");
    }

    public String novox() {
        return "servicos";
    }

    public String editar(Servicos s) {
        servicos = s;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaServicos", servicos);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        servicoValor = new ServicoValor();
        valorf = "0";
        desconto = "0";
        taxa = "0";
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return "servicos";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void excluir() {
        if (servicos.getId() != -1) {
            SalvarAcumuladoDB salvar = new SalvarAcumuladoDBToplink();
            servicos = (Servicos) salvar.pesquisaCodigo(servicos.getId(), "Servicos");
            if (!listaServicoValor.isEmpty()) {
                mensagem = "Existem valores cadastrados neste serviço!";
                salvar.desfazerTransacao();
                return;
            }

            try {
                for (CategoriaDesconto categoria : listaCategoriaDesconto) {
                    if (categoria.getId() != -1) {
                        salvar.deletarObjeto(salvar.pesquisaCodigo(categoria.getId(), "CategoriaDesconto"));
                    }
                }
                salvar.abrirTransacao();
                if (salvar.deletarObjeto(servicos)) {
                    salvar.comitarTransacao();
                    mensagem = "Cadastro excluido com sucesso!";
                } else {
                    salvar.desfazerTransacao();
                    mensagem = "Erro cadastro não pode ser excluído!";
                }
            } catch (Exception e) {
                mensagem = "Erro cadastro não pode ser excluído!";
            }
        }
        novo();
    }

    public String pesquisaContaCobranca() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "servicos");
        return "pesquisaContaCobranca";
    }

    public String pesquisarServicos() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("urlRetorno", "servicos");
        descPesquisa = "";
        return "pesquisaServicos";
    }

    public List<Servicos> getListaServicos() {
        if (listaServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            setListaServicos((List<Servicos>) db.pesquisaServicos(descPesquisa, porPesquisa, comoPesquisa));
        }
        return listaServicos;
    }

    public List<ServicoValor> getListaServicoValor() {
        ServicoValorDB servicoValorDB = new ServicoValorDBToplink();
        listaServicoValor.clear();
        listaServicoValor = servicoValorDB.pesquisaServicoValor(servicos.getId());
        if (listaServicoValor == null) {
            listaServicoValor = new ArrayList();
        }
        return listaServicoValor;
    }

    public boolean getDesabilitaValor() {
        return servicos.getId() == -1;
    }

    public void salvarValor() {
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        servicoValor.setValor(Moeda.substituiVirgulaFloat(valorf));
        servicoValor.setTaxa(Moeda.substituiVirgulaFloat(taxa));
        servicoValor.setDescontoAteVenc(Moeda.substituiVirgulaFloat(desconto));
        boolean existeValor = true;
//        if (servicoValor.getValor() != 0) {
//            existeValor = true;
//        } else if (servicoValor.getTaxa() != 0){
//            existeValor = false;            
//        }
        if (existeValor) {
            if (servicoValor.getDescontoAteVenc() > servicoValor.getValor()) {
                servicoValor.setDescontoAteVenc(servicoValor.getValor());
            }
            if (servicoValor.getId() == -1) {
                acumuladoDB.abrirTransacao();
                servicoValor.setServicos(servicos);
                if (acumuladoDB.inserirObjeto(servicoValor)) {
                    acumuladoDB.comitarTransacao();
                    listaServicoValor.clear();
                    GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                } else {
                    GenericaMensagem.warn("Validação", "Este valor para o serviço já existe no sistema.");
                    acumuladoDB.desfazerTransacao();
                }
            } else {
                acumuladoDB.abrirTransacao();
                if (acumuladoDB.alterarObjeto(servicoValor)) {
                    acumuladoDB.comitarTransacao();
                    GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                } else {
                    acumuladoDB.desfazerTransacao();
                }
            }
        } else {
            GenericaMensagem.warn("Validação", "Informar o valor / taxa!");
        }
        setIndice("valor");
    }

    public String novoServicoValor() {
        servicoValor = new ServicoValor();
        valorf = "0";
        desconto = "0";
        taxa = "0";
        setIndice("valor");
        textoBtnServico = "Adicionar";
        listaServicoValor.clear();
        return null;
    }

    public void editarServicoValor(ServicoValor sv) {
        servicoValor = sv;
        valorf = Moeda.converteR$Float(servicoValor.getValor());
        desconto = Moeda.converteR$Float(servicoValor.getDescontoAteVenc());
        taxa = Moeda.converteR$Float(servicoValor.getTaxa());
        textoBtnServico = "Atualizar";
        setIndice("valor");
    }

    public void removerServicoValor() {
        removerServicoValor(null);
    }

    public void removerServicoValor(ServicoValor sv) {
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        if (sv != null) {
            if (sv.getId() != -1) {
                servicoValor = sv;
            }
        }
        textoBtnServico = "Adicionar";
        if (servicoValor.getId() != -1) {
            acumuladoDB.abrirTransacao();
            servicoValor = (ServicoValor) acumuladoDB.pesquisaCodigo(servicoValor.getId(), "ServicoValor");
            if (acumuladoDB.deletarObjeto(servicoValor)) {
                acumuladoDB.comitarTransacao();
                listaServicoValor.clear();
                mensagem = "Registro excluido com sucesso.";
            } else {
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
                acumuladoDB.desfazerTransacao();
            }
        }
        servicoValor = new ServicoValor();
        setIndice("valor");
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
            if(AnaliseString.isInteger(valorf)){
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
            if(AnaliseString.isInteger(desconto)){
                this.desconto = Moeda.substituiVirgula(desconto);
            } else if (AnaliseString.isFloat(desconto)) {
                this.desconto = Moeda.substituiVirgula(desconto);
            } else {
                this.desconto = Moeda.substituiVirgula("0");
            }
        } else {
            this.desconto = Moeda.substituiVirgula("0");
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
            if(AnaliseString.isInteger(taxa)){
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

    public void setListaServicoValor(List listaServicoValor) {
        this.listaServicoValor = listaServicoValor;
    }

    public float getDescontoCategoria() {
        return descontoCategoria;
    }

    public void setDescontoCategoria(float descontoCategoria) {
        this.descontoCategoria = descontoCategoria;
    }

    public List<CategoriaDesconto> getListaCategoriaDesconto() {
        CategoriaDescontoDB categoriaDescontoDB = new CategoriaDescontoDBToplink();
        List<Categoria> listaCategoria = categoriaDescontoDB.pesquisaCategoriasSemServico(servicos.getId());
        listaCategoriaDesconto = categoriaDescontoDB.pesquisaTodosPorServico(servicos.getId());
        int i = 0;
        if (listaCategoriaDesconto == null) {
            listaCategoriaDesconto = new ArrayList<CategoriaDesconto>();
        }

        if ((listaCategoria != null) && (this.servicos.getId() != -1)) {
            while (i < listaCategoria.size()) {
                listaCategoriaDesconto.add(new CategoriaDesconto(-1, this.servicos, listaCategoria.get(i), 0));
                i++;
            }
        }
        return listaCategoriaDesconto;
    }

    public void setListaCategoriaDesconto(List listaCategoriaDesconto) {
        this.listaCategoriaDesconto = listaCategoriaDesconto;
    }

    public CategoriaDesconto getCategoriaDesconto() {
        return categoriaDesconto;
    }

    public void setCategoriaDesconto(CategoriaDesconto categoriaDesconto) {
        this.categoriaDesconto = categoriaDesconto;
    }

    public void setListaServicos(List<Servicos> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getTextoBtnServico() {
        return textoBtnServico;
    }

    public void setTextoBtnServico(String textoBtnServico) {
        this.textoBtnServico = textoBtnServico;
    }
    
//    public void onChange(TabChangeEvent event) {
//        Tab activeTab = event.getTab();
//        this.tabViewTitle = activeTab.getTitle();
//    }    
//
//    public String getTabViewTitle() {
//        return tabViewTitle;
//    }
//
//    public void setTabViewTitle(String tabViewTitle) {
//        this.tabViewTitle = tabViewTitle;
//    }
    
}
