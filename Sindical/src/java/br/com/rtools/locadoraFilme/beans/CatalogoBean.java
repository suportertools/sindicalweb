package br.com.rtools.locadoraFilme.beans;

import br.com.rtools.locadoraFilme.Catalogo;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.locadoraFilme.db.CatalogoDB;
import br.com.rtools.locadoraFilme.db.CatalogoDBToplink;
import br.com.rtools.locadoraFilme.db.TituloDB;
import br.com.rtools.locadoraFilme.db.TituloDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CatalogoBean {

    private Catalogo catalogo;
    private String comoPesquisa;
    private String descPesquisa;
    private String msgConfirma;
//    private List<GCatalogo> listaCatalogo;
    private int idCatalogo;
    private List<SelectItem> listaTituloCombo;
    private List<Titulo> listaTitulo;
    private int idTitulo;
    private List<SelectItem> listaFilialCombo;
    private List<Filial> listaFilial;
    private int idFilial;
    private int idIndex;
    private int quantidade;
    private Date data;

    public CatalogoBean() {
        catalogo = new Catalogo();
        idFilial = 0;
        idTitulo = 0;
//        listaCatalogo = new ArrayList<GCatalogo>();
        listaFilialCombo = new ArrayList<SelectItem>();
        listaFilial = new ArrayList<Filial>();
        listaTituloCombo = new ArrayList<SelectItem>();
        listaTitulo = new ArrayList<Titulo>();
        comoPesquisa = "";
        descPesquisa = "";
        msgConfirma = "";
        idIndex = -1;
    }

    public String novo() {
        catalogo = new Catalogo();
        idFilial = 0;
        idTitulo = 0;
        listaFilialCombo = new ArrayList<SelectItem>();
        listaFilial = new ArrayList<Filial>();
        listaTituloCombo = new ArrayList<SelectItem>();
        listaTitulo = new ArrayList<Titulo>();
//        listaCatalogo = new ArrayList<GCatalogo>();
        comoPesquisa = "";
        descPesquisa = "";
        msgConfirma = "";
        return "catalogoFilme";
    }

    public synchronized String salvar() {
        CatalogoDB catalogoDB = new CatalogoDBToplink();
        if (catalogo.getQuantidade() <= 0) {
            msgConfirma = "Quantidade deve ser maior que 01.";
            return null;
        }
        catalogo.setFilial(listaFilial.get(idFilial));
        catalogo.setTitulo(listaTitulo.get(idTitulo));

        if (!catalogoDB.verificaFilial(catalogo.getFilial(), catalogo.getTitulo())) {
            msgConfirma = "Já existe este catálogo para esta filial.";
            return null;
        }
        if (catalogo.getId() == -1) {
            if (!catalogoDB.insert(catalogo)) {
                msgConfirma = "Erro! Cadastro não foi efetuado.";
            } else {
                msgConfirma = "Cadastro efetuado com sucesso!";
            }
        } else {
            if (catalogoDB.update(catalogo)) {
                msgConfirma = "Cadastro atualizado com sucesso!";
            } else {
                msgConfirma = "Erro ao Atualizar registro!";
            }
        }
        catalogo = new Catalogo();
        return null;
    }

    public synchronized String excluir() {
        CatalogoDB catalogoDB = new CatalogoDBToplink();
        if (catalogo.getId() != -1) {
            catalogo = catalogoDB.pesquisaCodigo(getCatalogo().getId());
            if (catalogoDB.delete(catalogo)) {
                msgConfirma = "Cadastro excluído com sucesso!";
            } else {
                msgConfirma = "Erro! Cadastro não foi excluído";
            }
        } else {
            msgConfirma = "Não há registro para excluir";
        }
        catalogo = new Catalogo();
        return null;
    }

//
//    public synchronized void editar(){
//        GCatalogo gCatalogo = (GCatalogo) listaCatalogo.get(idTitulo);
//        catalogo = gCatalogo.getCatalogo();
//        for (int i = 0; i < listaFilial.size(); i++){
//            if(catalogo.getFilial().getId() == listaFilial.get(i).getFilial().getId()){
//                idFilial = i;
//            }
//        }
//        for (int j = 0; j < listaTitulo.size(); j++){
//            if(catalogo.getTitulo().getId() == listaTitulo.get(j).getId()){
//                idTitulo = j;
//            }
//        }
//    }
//    private void verificaQuantidadeMatriz(){
//        Catalogo catalogo = new Catalogo();
//        for (int i = 0; i < listaFilial.size(); i++){
//            if(catalogo.getFilial().getId() == listaFilial.get(i).getId()){
//                if(catalogo.getQuantidade() < listaCatalogo.get(i).getCatalogo().getQuantidade()){
//
//                }
//            }
//        }
//    }
    public void acaoPesquisaInicial() {
        setComoPesquisa("I");
    }

    public void acaoPesquisaParcial() {
        setComoPesquisa("P");
    }

    public void refreshForm() {
    }

    public Catalogo getCatalogo() {
        return catalogo;
    }

    public List<SelectItem> getListaFilialCombo() {
        int i = 0;
        //FilialDB db = new FilialDBToplink();
        if (listaFilial.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            setListaFilial((List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true));
            //setListaFilial((List<Filial>) db.pesquisaTodos());
            while (i < getListaFilial().size()) {
                listaFilialCombo.add(new SelectItem(
                        new Integer(i),
                        getListaFilial().get(i).getFilial().getPessoa().getNome()));
                i++;
            }
        }
        return listaFilialCombo;
    }

    public synchronized void pesquisaCatalogo() {
//        listaCatalogo.clear();
    }

    public List<SelectItem> getListaTituloCombo() {
        int i = 0;
        TituloDB db = new TituloDBToplink();
        if (listaTituloCombo.isEmpty()) {
            setListaTitulo((List<Titulo>) db.pesquisaTodos());
            while (i < getListaTitulo().size()) {
                listaTituloCombo.add(new SelectItem(
                        new Integer(i),
                        getListaTitulo().get(i).getDescricao()));
                i++;
            }
        }
        return listaTituloCombo;
    }

//    public List<GCatalogo> getListaCatalogo() {
//        if(listaCatalogo.isEmpty()){
//            CatalogoDB pesquisaCatalogoDB = new CatalogoDBToplink();
//            List<Catalogo> lista = pesquisaCatalogoDB.pesquisaCatalogo(getListaFilial().get(idFilial));
//            if(lista == null){
//                lista = new ArrayList();
//            }
//            for (int i = 0; i < lista.size(); i++){
//                listaCatalogo.add(new GCatalogo (lista.get(i), lista.get(i).getQuantidade()));                
//            }
//        }
//        return listaCatalogo;
//    }
    public void setCatalogo(Catalogo catalogo) {
        this.catalogo = catalogo;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public void setListaTituloCombo(List<SelectItem> listaTituloCombo) {
        this.listaTituloCombo = listaTituloCombo;
    }

    public List<Titulo> getListaTitulo() {
        return listaTitulo;
    }

    public void setListaTitulo(List<Titulo> listaTitulo) {
        this.listaTitulo = listaTitulo;
    }

    public int getIdTitulo() {
        return idTitulo;
    }

    public void setIdTitulo(int idTitulo) {
        this.idTitulo = idTitulo;
    }

    public void setListaFilialCombo(List<SelectItem> listaFilialCombo) {
        this.listaFilialCombo = listaFilialCombo;
    }

    public List<Filial> getListaFilial() {
        return listaFilial;
    }

    public void setListaFilial(List<Filial> listaFilial) {
        this.listaFilial = listaFilial;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public int getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(int idCatalogo) {
        this.idCatalogo = idCatalogo;
    }
//
//    public void setListaCatalogo(List<GCatalogo> listaCatalogo) {
//        this.listaCatalogo = listaCatalogo;
//    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;

    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }
}