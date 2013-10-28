//package br.com.rtools.financeiro.beans;
//
//import br.com.rtools.financeiro.DescontoServicoEmpresa;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.db.DescontoServicoEmpresaDB;
//import br.com.rtools.financeiro.db.DescontoServicoEmpresaDBTopLink;
//import br.com.rtools.financeiro.db.ServicosDB;
//import br.com.rtools.financeiro.db.ServicosDBToplink;
//import br.com.rtools.pessoa.Juridica;
//import br.com.rtools.utilitarios.Moeda;
//import br.com.rtools.utilitarios.SalvarAcumuladoDB;
//import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.application.FacesMessage;
//import javax.faces.bean.ManagedBean;
//import javax.faces.bean.SessionScoped;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//
//@ManagedBean(name = "descontoServicoEmpresaBean", eager = true)
//@SessionScoped
//public class DescontoServicoEmpresaJSFBean {
//
//    // private static final long serialVersionUID = 1L;
//    private DescontoServicoEmpresa descontoServicoEmpresa = new DescontoServicoEmpresa();
//    private int idServicos = 0;
//    private List<SelectItem> listaServicos = new ArrayList<SelectItem>();
//    private List<DescontoServicoEmpresa> listaDescontoServicoEmpresa = new ArrayList<DescontoServicoEmpresa>();
//    private List<DescontoServicoEmpresa> listaDSEPorEmpresa = new ArrayList<DescontoServicoEmpresa>();
//    private float desconto = 0;
//    private String descricaoPesquisaNome = "";
//    private String descricaoPesquisaCNPJ = "";
//    private String comoPesquisa = "";
//    private String porPesquisa = "";
//    private String mensagem = "";
//    private boolean desabilitaPesquisaNome = false;
//    private boolean desabilitaPesquisaCNPJ = false;
//
//    public String novo() {
//        limpar();
//        return "descontoServicoEmpresa";
//    }
//
//    public void limpar() {
//        desconto = 0;
//        descontoServicoEmpresa = new DescontoServicoEmpresa();
//        idServicos = 0;
//        listaServicos.clear();
//        listaDescontoServicoEmpresa.clear();
//        listaDSEPorEmpresa.clear();
//    }
//
//    public void salvar() {
//        if (descontoServicoEmpresa.getJuridica().getId() == -1) {
//            mensagem = "Pesquisar pessoa jurídica!";
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", mensagem));
//            return;
//        }
//        if (listaServicos.isEmpty()) {
//            mensagem = "Cadastrar serviços!";
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", mensagem));
//            return;
//        }
//        if (desconto <= 0) {
//            mensagem = "Informar o valor do desconto!";
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", mensagem));
//            return;
//        }
//        // descontoServicoEmpresa.setDesconto(Moeda.converteUS$(desconto));
//        descontoServicoEmpresa.setDesconto(desconto);
//        SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//        int idServicoAntes = -1;
//        if (descontoServicoEmpresa.getId() != -1) {
//            idServicoAntes = descontoServicoEmpresa.getServicos().getId();
//        }
//        descontoServicoEmpresa.setServicos((Servicos) salvarAcumuladoDB.pesquisaCodigo(Integer.parseInt(listaServicos.get(idServicos).getDescription()), "Servicos"));
//        DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
//        Juridica juridica = descontoServicoEmpresa.getJuridica();
//        if (descontoServicoEmpresa.getId() == -1) {
//            if (descontoServicoEmpresaDB.existeDescontoServicoEmpresa(descontoServicoEmpresa)) {
//                mensagem = "Desconto já cadastrado para essa empresa!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", mensagem));
//                return;
//            }
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.inserirObjeto(descontoServicoEmpresa)) {
//                salvarAcumuladoDB.comitarTransacao();
//                mensagem = "Registro cadastrado";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
//                limpar();
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                descontoServicoEmpresa.setId(-1);
//                mensagem = "Erro ao atualizar este registro!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", mensagem));
//            }
//        } else {
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.alterarObjeto(descontoServicoEmpresa)) {
//                salvarAcumuladoDB.comitarTransacao();
//                mensagem = "Registro atualizado";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
//                limpar();
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                mensagem = "Erro ao atualizar este registro!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", mensagem));
//            }
//        }
//        descontoServicoEmpresa.setJuridica(juridica);
//    }
//
//    public void atualizarDesconto(DescontoServicoEmpresa dse) {
//        if (dse.getId() != -1) {
//            dse.setDesconto(Float.parseFloat(Moeda.substituiVirgula(dse.getDescontoString())));
//            if (dse.getDescontoString().equals("") || dse.getDescontoString().equals("0,00") || dse.getDescontoString().equals("0") || dse.getDescontoString().equals("0.00")) {
//                mensagem = "Informar o valor do desconto!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Validação", mensagem));
//                return;
//            }
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.alterarObjeto(dse)) {
//                salvarAcumuladoDB.comitarTransacao();
//                mensagem = "Desconto atualizado com sucesso";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                mensagem = "Erro ao atualizar este desconto!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", mensagem));
//            }
//        }
//    }
//
//    public String editar(DescontoServicoEmpresa dse) {
//        descontoServicoEmpresa = dse;
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("descontoServicoEmpresaPesquisa", dse);
//        for (int i = 0; i < listaServicos.size(); i++) {
//            if (Integer.parseInt(listaServicos.get(i).getDescription()) == dse.getServicos().getId()) {
//                idServicos = i;
//            }
//        }
//        desconto = descontoServicoEmpresa.getDesconto();
//        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
//        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
//    }
//
//    public void editarDSE(DescontoServicoEmpresa dse) {
//        descontoServicoEmpresa = dse;
//        for (int i = 0; i < listaServicos.size(); i++) {
//            if (Integer.parseInt(listaServicos.get(i).getDescription()) == dse.getServicos().getId()) {
//                idServicos = i;
//            }
//        }
//        desconto = descontoServicoEmpresa.getDesconto();
//    }
//
//    public void excluir() {
//        if (descontoServicoEmpresa.getId() != -1) {
//            Juridica juridica = descontoServicoEmpresa.getJuridica();
//            boolean isMantemJuridica = true;
//            if (listaDSEPorEmpresa.isEmpty()) {
//                isMantemJuridica = false;
//            }
//            listaDSEPorEmpresa.size();
//            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
//            descontoServicoEmpresa = (DescontoServicoEmpresa) salvarAcumuladoDB.pesquisaCodigo(descontoServicoEmpresa.getId(), "DescontoServicoEmpresa");
//            salvarAcumuladoDB.abrirTransacao();
//            if (salvarAcumuladoDB.deletarObjeto(descontoServicoEmpresa)) {
//                salvarAcumuladoDB.comitarTransacao();
//                mensagem = "Registro excluído";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", mensagem));
//                limpar();
//
//            } else {
//                salvarAcumuladoDB.desfazerTransacao();
//                mensagem = "Erro ao excluir registro!";
//                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", mensagem));
//            }
//            descontoServicoEmpresa.setJuridica(juridica);
//        } else {
//            mensagem = "Pesquisar registro a ser excluído!";
//            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", mensagem));
//        }
//    }
//
//    public List<DescontoServicoEmpresa> getListaDescontoServicoEmpresa() {
//        if (listaDescontoServicoEmpresa.isEmpty()) {
//            DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
//            if (desabilitaPesquisaCNPJ && !descricaoPesquisaNome.equals("")) {
//                listaDescontoServicoEmpresa = descontoServicoEmpresaDB.pesquisaDescontoServicoEmpresas("nome", descricaoPesquisaNome, comoPesquisa);
//            } else if (desabilitaPesquisaNome && !descricaoPesquisaCNPJ.equals("")) {
//                listaDescontoServicoEmpresa = descontoServicoEmpresaDB.pesquisaDescontoServicoEmpresas("cnpj", descricaoPesquisaCNPJ, comoPesquisa);
//            } else {
//                listaDescontoServicoEmpresa = descontoServicoEmpresaDB.listaTodos();
//            }
//        }
//        return listaDescontoServicoEmpresa;
//    }
//
//    public List<DescontoServicoEmpresa> getListaDSEPorEmpresa() {
//        if (listaDSEPorEmpresa.isEmpty()) {
//            if (descontoServicoEmpresa.getJuridica().getId() != -1) {
//                DescontoServicoEmpresaDB descontoServicoEmpresaDB = new DescontoServicoEmpresaDBTopLink();
//                listaDSEPorEmpresa = descontoServicoEmpresaDB.listaTodosPorEmpresa(descontoServicoEmpresa.getJuridica().getId());
//            }
//        }
//        return listaDSEPorEmpresa;
//    }
//
//    public DescontoServicoEmpresa getDescontoServicoEmpresa() {
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
//            Juridica juridica = (Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa");
//            if (descontoServicoEmpresa.getId() == -1) {
//                descontoServicoEmpresa.setJuridica(juridica);
//            } else {
//                if (descontoServicoEmpresa.getJuridica().getId() != juridica.getId()) {
//                    listaDSEPorEmpresa.clear();
//                    descontoServicoEmpresa.setId(-1);
//                    descontoServicoEmpresa.setJuridica(juridica);
//                }
//            }
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
//        }
//        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("descontoServicoEmpresaPesquisa") != null) {
//            listaDSEPorEmpresa.clear();
//            descontoServicoEmpresa = ((DescontoServicoEmpresa) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("descontoServicoEmpresaPesquisa"));
//            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("descontoServicoEmpresaPesquisa");
//        }
//        return descontoServicoEmpresa;
//    }
//
//    public void setDescontoServicoEmpresa(DescontoServicoEmpresa descontoServicoEmpresa) {
//        this.descontoServicoEmpresa = descontoServicoEmpresa;
//    }
//
//    public int getIdServicos() {
//        return idServicos;
//    }
//
//    public void setIdServicos(int idServicos) {
//        this.idServicos = idServicos;
//    }
//
//    public List<SelectItem> getListaServicos() {
//        if (listaServicos.isEmpty()) {
//            ServicosDB servicosDB = new ServicosDBToplink();
//            List<Servicos> list = (List<Servicos>) servicosDB.pesquisaTodos();
//            if (!list.isEmpty()) {
//                for (int i = 0; i < list.size(); i++) {
//                    listaServicos.add(new SelectItem(new Integer(i), list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
//                }
//            }
//        }
//        return listaServicos;
//    }
//
//    public void setListaServicos(List<SelectItem> listaServicos) {
//        this.listaServicos = listaServicos;
//    }
//
//    public float getDesconto() {
//        return desconto;
//    }
//
//    public void setDesconto(Float desconto) {
//        this.desconto = desconto;
//    }
//
//    public String getDescricaoPesquisaNome() {
//        return descricaoPesquisaNome;
//    }
//
//    public void setDescricaoPesquisaNome(String descricaoPesquisaNome) {
//        this.descricaoPesquisaNome = descricaoPesquisaNome;
//    }
//
//    public String getDescricaoPesquisaCNPJ() {
//        return descricaoPesquisaCNPJ;
//    }
//
//    public void setDescricaoPesquisaCNPJ(String descricaoPesquisaCNPJ) {
//        this.descricaoPesquisaCNPJ = descricaoPesquisaCNPJ;
//    }
//
//    public boolean isDesabilitaPesquisaNome() {
//        return desabilitaPesquisaNome;
//    }
//
//    public void setDesabilitaPesquisaNome(boolean desabilitaPesquisaNome) {
//        this.desabilitaPesquisaNome = desabilitaPesquisaNome;
//    }
//
//    public boolean isDesabilitaPesquisaCNPJ() {
//        return desabilitaPesquisaCNPJ;
//    }
//
//    public void setDesabilitaPesquisaCNPJ(boolean desabilitaPesquisaCNPJ) {
//        this.desabilitaPesquisaCNPJ = desabilitaPesquisaCNPJ;
//    }
//
//    public void tipoPesquisa() {
//        if (!descricaoPesquisaNome.equals("")) {
//            desabilitaPesquisaCNPJ = true;
//            descricaoPesquisaCNPJ = "";
//        } else if (!descricaoPesquisaCNPJ.equals("")) {
//            desabilitaPesquisaNome = true;
//            descricaoPesquisaNome = "";
//        } else {
//            desabilitaPesquisaNome = false;
//            desabilitaPesquisaCNPJ = false;
//            descricaoPesquisaNome = "";
//            descricaoPesquisaCNPJ = "";
//        }
//
//    }
//
//    public void acaoPesquisaInicial() {
//        comoPesquisa = "I";
//        listaDescontoServicoEmpresa.clear();
//    }
//
//    public void acaoPesquisaParcial() {
//        comoPesquisa = "P";
//        listaDescontoServicoEmpresa.clear();
//    }
//
//    public String getComoPesquisa() {
//        return comoPesquisa;
//    }
//
//    public void setComoPesquisa(String comoPesquisa) {
//        this.comoPesquisa = comoPesquisa;
//    }
//
//    public String getPorPesquisa() {
//        return porPesquisa;
//    }
//
//    public void setPorPesquisa(String porPesquisa) {
//        this.porPesquisa = porPesquisa;
//    }
//
//    public String getMensagem() {
//        return mensagem;
//    }
//
//    public void setMensagem(String mensagem) {
//        this.mensagem = mensagem;
//    }
//}
