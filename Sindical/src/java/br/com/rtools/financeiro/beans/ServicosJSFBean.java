package br.com.rtools.financeiro.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.db.CategoriaDescontoDB;
import br.com.rtools.associativo.db.CategoriaDescontoDBToplink;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.*;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDBToplink;
import br.com.rtools.seguranca.Departamento;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.context.FacesContext;

public class ServicosJSFBean implements java.io.Serializable {

    private Servicos servicos = new Servicos();
    private Plano5 plano5 = new Plano5();
    private String porPesquisa = "descricao";
    private String comoPesquisa = "P";
    private String descPesquisa = "";
    private String msgConfirma;
    private int idIndex = -1;
    private ServicoValor servicoValor = new ServicoValor();
    private String valor = "0";
    private String taxaToString = "0";
    private String desconto = "0";
    private String indice = "servico";
    private List<ServicoValor> listaServicoValor = new ArrayList();
    private List<Servicos> listaServicos = new ArrayList();
    private List<CategoriaDesconto> listaCategoriaDesconto = new ArrayList();
    private float descontoCategoria = 0;
    private CategoriaDesconto categoriaDesconto = new CategoriaDesconto();
    private String textoBtnServico = "Adicionar";

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

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public void refreshForm() {
    }

    public void acaoInicial() {
        comoPesquisa = "I";
        listaServicos.clear();
    }

    public void acaoParcial() {
        comoPesquisa = "P";
        listaServicos.clear();
    }

    public String salvar() {

        if (servicos.getDescricao().equals("")) {
            msgConfirma = "Informe o nome do serviço a ser cadastrado!";
            return null;
        }

        if (servicos.getPlano5().getId() == -1) {
            msgConfirma = "Pesquise o plano de contas antes de salvar!";
            return null;
        }


        ServicosDB db = new ServicosDBToplink();
        SalvarAcumuladoDB salvar = new SalvarAcumuladoDBToplink();
        //servicos.setPlano5(plano5);

        try {
            salvar.abrirTransacao();
            if (servicos.getId() == -1) {
                if (db.idServicos(servicos) == null) {
                    FilialDB filialDB = new FilialDBToplink();
                    servicos.setDepartamento((Departamento) salvar.pesquisaCodigo(14, "Departamento"));
                    servicos.setFilial(filialDB.pesquisaCodigo(1));
                    salvar.inserirObjeto(servicos);
                    msgConfirma = "Serviço salvo com Sucesso!";
                } else {
                    msgConfirma = "Este serviço já existe no Sistema.";
                }
            } else {
                if (salvar.alterarObjeto(servicos)) {
                    msgConfirma = "Serviço atualizado com sucesso!";
                } else {
                    msgConfirma = "Erro na atualização do serviço!";
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
            msgConfirma = "Erro no cadastro de serviço!";
        }
        return null;
    }

    public String novo() {
        servicos = new Servicos();
        listaCategoriaDesconto = new ArrayList<CategoriaDesconto>();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaCobrancaPesquisa");
        return "servicos";
    }

    public String novox() {
        return "servicos";
    }

    public String editar() {
        servicos = (Servicos) listaServicos.get(getIdIndex());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaServicos", servicos);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return "servicos";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public String excluir() {
        SalvarAcumuladoDB salvar = new SalvarAcumuladoDBToplink();
        if (servicos.getId() != -1) {
            salvar.abrirTransacao();
            servicos = (Servicos) salvar.pesquisaCodigo(servicos.getId(), "Servicos");

            if (!listaServicoValor.isEmpty()) {
                msgConfirma = "Existem valores cadastrados neste serviço!";
                salvar.desfazerTransacao();
                return null;
            }

            try {
                for (CategoriaDesconto categoria : listaCategoriaDesconto) {
                    if (categoria.getId() != -1) {
                        salvar.deletarObjeto(salvar.pesquisaCodigo(categoria.getId(), "CategoriaDesconto"));
                    }
                }
                if (salvar.deletarObjeto(servicos)) {
                    salvar.comitarTransacao();
                    msgConfirma = "Cadastro excluido com sucesso!";
                } else {
                    salvar.desfazerTransacao();
                    msgConfirma = "Erro cadastro não pode ser excluído!";
                }
            } catch (Exception e) {
                salvar.desfazerTransacao();
                msgConfirma = "Erro cadastro não pode ser excluído!";
            }
        }
        listaCategoriaDesconto.clear();
        listaServicoValor.clear();
        listaServicos.clear();
        servicos = new Servicos();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("contaCobrancaPesquisa");
        return null;
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
        if (servicos.getId() == -1) {
            return true;
        } else {
            return false;
        }
    }

    public String salvarValor() {
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        servicoValor.setValor(Moeda.substituiVirgulaFloat(valor));
        servicoValor.setDescontoAteVenc(Moeda.substituiVirgulaFloat(desconto));
        servicoValor.setTaxa(Moeda.substituiVirgulaFloat(taxaToString));
        if (servicoValor.getValor() != 0) {
            if (servicoValor.getId() == -1) {
                acumuladoDB.abrirTransacao();
                servicoValor.setServicos(servicos);
                if (acumuladoDB.inserirObjeto(servicoValor)) {
                    acumuladoDB.comitarTransacao();
                    listaServicoValor.clear();
                    msgConfirma = "Valor do serviço com sucesso!";
                } else {
                    msgConfirma = "Este valor para o serviço já existe no sistema.";
                    acumuladoDB.desfazerTransacao();
                }
            } else {
                acumuladoDB.abrirTransacao();
                if (acumuladoDB.alterarObjeto(servicoValor)) {
                    acumuladoDB.comitarTransacao();
                    msgConfirma = "Valor do serviço atualizado com Sucesso!";
                } else {
                    acumuladoDB.desfazerTransacao();
                }
            }
        }
        setIndice("valor");
        return null;
    }

    public String novoValor() {
        servicoValor = new ServicoValor();
        valor = "0";
        desconto = "0";
        taxaToString = "0";
        setIndice("valor");
        textoBtnServico = "Adicionar";
        listaServicoValor.clear();
        return null;
    }

    public String editarValor(int index) {
        if (index == -1) {
            return null;
        }
        servicoValor = (ServicoValor) listaServicoValor.get(index);
        setValor(Moeda.converteR$Float(servicoValor.getValor()));
        setDesconto(Moeda.converteR$Float(servicoValor.getDescontoAteVenc()));
        setTaxaToString(Moeda.converteR$Float(servicoValor.getTaxa()));
        textoBtnServico = "Atualizar";
        setIndice("valor");
        return null;
    }

    public String excluirValor() {
        excluirValor(-1);
        return null;
    }

    public String excluirValor(int index) {
        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        if (index != -1) {
            servicoValor.setId(listaServicoValor.get(index).getId());
        }
        textoBtnServico = "Adicionar";
        if (servicoValor.getId() != -1) {
            acumuladoDB.abrirTransacao();
            servicoValor = (ServicoValor) acumuladoDB.pesquisaCodigo(servicoValor.getId(), "ServicoValor");
            if (acumuladoDB.deletarObjeto(servicoValor)) {
                acumuladoDB.comitarTransacao();
                listaServicoValor.clear();
                msgConfirma = "Registro excluido com sucesso.";
            } else {
                msgConfirma = "Falha ao excluir registro!";
                acumuladoDB.desfazerTransacao();
            }
        }
        servicoValor = new ServicoValor();
        setIndice("valor");
        return null;
    }

    public ServicoValor getServicoValor() {
        return servicoValor;
    }

    public void setServicoValor(ServicoValor servicoValor) {
        this.servicoValor = servicoValor;
    }

    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDesconto() {
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        this.desconto = desconto;
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

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public String getTaxaToString() {
        return Moeda.converteR$(taxaToString);
    }

    public void setTaxaToString(String taxaToString) {
        this.taxaToString = taxaToString;
    }

    public String getTextoBtnServico() {
        return textoBtnServico;
    }

    public void setTextoBtnServico(String textoBtnServico) {
        this.textoBtnServico = textoBtnServico;
    }
}