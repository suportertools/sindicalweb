package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.ServicoCategoria;
import br.com.rtools.associativo.db.*;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicoRotinaDB;
import br.com.rtools.financeiro.db.ServicoRotinaDBToplink;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CategoriaBean {

    private Categoria categoria = new Categoria();
    private String msgConfirma = "";
    private int idGrupoCategoria = 0;
    private int idIndex = -1;
    private List<Categoria> listaCategoria = new ArrayList();
    private boolean limpar = false;
    private List lista = new ArrayList();
    private List<SelectItem> listaServicos = new ArrayList();

    public String salvar() {
        if (categoria.getCategoria().equals("")) {
            msgConfirma = "Digite uma categoria!";
            return null;
        }
        ServicoCategoriaDB dbSeC = new ServicoCategoriaDBToplink();
        ServicoCategoria servicoCategoria = new ServicoCategoria();
        ServicosDB dbS = new ServicosDBToplink();
        CategoriaDB db = new CategoriaDBToplink();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        if (categoria.getId() == -1) {
            categoria.setGrupoCategoria((GrupoCategoria) sadb.pesquisaObjeto(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()), "GrupoCategoria"));
            if (db.insert(categoria)) {
                msgConfirma = "Categoria salva com Sucesso!";
                limpar = false;
            } else {
                msgConfirma = "Erro ao Salvar!";
            }
            for (int i = 0; i < lista.size(); i++) {
                if (Integer.parseInt(listaServicos.get(Integer.parseInt((String) ((DataObject) lista.get(i)).getArgumento1())).getDescription()) != -1) {
                    servicoCategoria.setCategoria(categoria);
                    servicoCategoria.setParentesco((Parentesco) ((DataObject) lista.get(i)).getArgumento0());
                    servicoCategoria.setServicos(dbS.pesquisaCodigo(Integer.parseInt(
                            listaServicos.get(Integer.parseInt((String) ((DataObject) lista.get(i)).getArgumento1())).getDescription())));
                    dbSeC.insert(servicoCategoria);
                    servicoCategoria = new ServicoCategoria();
                }
            }
            lista = new ArrayList();
        } else {
            categoria.setGrupoCategoria((GrupoCategoria) sadb.pesquisaCodigo(Integer.parseInt(getListaGrupoCategoria().get(idGrupoCategoria).getDescription()), "GrupoCategoria"));
            if (db.update(categoria)) {
                msgConfirma = "Categoria atualizada com Sucesso!";
            } else {
                msgConfirma = "Erro ao atualizar!";
            }
            for (int i = 0; i < lista.size(); i++) {
                servicoCategoria = (ServicoCategoria) ((DataObject) lista.get(i)).getArgumento2();
                if (Integer.parseInt(listaServicos.get(Integer.parseInt((String) ((DataObject) lista.get(i)).getArgumento1())).getDescription()) == -1) {
                    if (servicoCategoria.getId() != -1) {
                        servicoCategoria = dbSeC.pesquisaCodigo(servicoCategoria.getId());
                        dbSeC.delete(servicoCategoria);
                    }
                } else {
                    servicoCategoria.setServicos(dbS.pesquisaCodigo(Integer.parseInt(
                            listaServicos.get(Integer.parseInt((String) ((DataObject) lista.get(i)).getArgumento1())).getDescription())));
                    servicoCategoria.setCategoria(categoria);
                    servicoCategoria.setParentesco((Parentesco) ((DataObject) lista.get(i)).getArgumento0());
                    if (servicoCategoria.getId() == -1) {
                        dbSeC.insert(servicoCategoria);
                    } else {
                        dbSeC.update(servicoCategoria);
                    }
                }
            }
            lista = new ArrayList();
        }
        return null;
    }

    public String excluir() {
        CategoriaDB db = new CategoriaDBToplink();
        if (categoria.getId() != -1) {
            categoria = db.pesquisaCodigo(categoria.getId());
        }
        if (db.delete(categoria)) {
            limpar = true;
            msgConfirma = "Categoria excluída com Sucesso!";
        } else {
            msgConfirma = "Erro ao excluir categoria!";
            categoria = new Categoria();
        }
        return null;
    }

    public String novo() {
        categoria = new Categoria();
        idGrupoCategoria = 0;
        lista.clear();
        listaCategoria.clear();
        return "categoria";
    }

    public String limpar() {
        if (limpar == true) {
            novo();
        }
        return "categoria";
    }

    public String editar() {
        categoria = (Categoria) listaCategoria.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("pesquisaCategoria", categoria);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        lista.clear();
        
        for (int i = 0; i < getListaGrupoCategoria().size(); i++) {
            if ( Integer.valueOf(getListaGrupoCategoria().get(i).getDescription()) == categoria.getGrupoCategoria().getId() ){
                idGrupoCategoria = i;
            }
        }   
        
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno") != null) {
            return "categoria";
        } else {
            return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
        }
    }

    public void atualizaServicos(int index) {
        if (((Parentesco) ((DataObject) lista.get(index)).getArgumento0()).getId() == 1) {
            for (int i = 0; i < lista.size(); i++) {
                if (Integer.parseInt(String.valueOf(((DataObject) lista.get(i)).getArgumento1())) == 0) {
                    ((DataObject) lista.get(i)).setArgumento1(Integer.parseInt(String.valueOf(((DataObject) lista.get(index)).getArgumento1())));
                }
            }
        }
    }

    public List<Categoria> getListaCategoria() {
        if (listaCategoria.isEmpty()) {
            CategoriaDB db = new CategoriaDBToplink();
            listaCategoria = db.pesquisaTodos();
        }
        return listaCategoria;
    }

    public List<SelectItem> getListaGrupoCategoria() {
        List<SelectItem> listaG = new Vector<SelectItem>();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        List select = (List<GrupoCategoria>) sadb.listaObjeto("GrupoCategoria", true);
        for (int i =0; i < select.size(); i++) {
            listaG.add(new SelectItem(i, (String) ((GrupoCategoria) select.get(i)).getGrupoCategoria(), ""+((GrupoCategoria) select.get(i)).getId()));
        }
        return listaG;
    }

    public List<SelectItem> getListaServicos() {
        //List<SelectItem> listaSe = new Vector<SelectItem>();
        if (listaServicos.isEmpty()) {
            int i = 0;
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List select = db.pesquisaTodosServicosComRotinas(130);
            listaServicos.add(new SelectItem(new Integer(i),
                    " -- NENHUM -- ",
                    "-1"));
            while (i < select.size()) {
                listaServicos.add(new SelectItem(new Integer(i + 1),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return listaServicos;
    }

    public List getListaParentescos() {
        if (lista.isEmpty()) {
            DataObject dtObj = null;
            ParentescoDB db = new ParentescoDBToplink();
            ServicoCategoriaDB dbSeC = new ServicoCategoriaDBToplink();
            List<ServicoCategoria> listaSerCat = dbSeC.pesquisaServCatPorId(categoria.getId());
            List<Parentesco> listaPar = db.pesquisaTodos();
            if (listaSerCat.isEmpty()) {
                for (int i = 0; i < listaPar.size(); i++) {
                    dtObj = new DataObject(listaPar.get(i), 0, new ServicoCategoria(), null, null, null);
                    lista.add(dtObj);
                }
            } else {
                int index = 0;
                boolean temServico = false;
                for (int i = 0; i < listaPar.size(); i++) {
                    for (int x = 0; x < listaSerCat.size(); x++) {
                        if (listaPar.get(i).getId() == listaSerCat.get(x).getParentesco().getId()) {
                            for (int w = 0; w < listaServicos.size(); w++) {
                                if (listaSerCat.get(x).getServicos().getId() == (Integer.parseInt(listaServicos.get(w).getDescription()))) {
                                    index = w;
                                    temServico = true;
                                    break;
                                }
                            }
                            dtObj = new DataObject(listaPar.get(i), index, listaSerCat.get(x), null, null, null);
                            break;
                        }
                    }
                    if (!temServico) {
                        dtObj = new DataObject(listaPar.get(i), index, new ServicoCategoria(), null, null, null);
                    }
                    temServico = false;
                    index = 0;
                    lista.add(dtObj);
                }
            }
        }
        return lista;
    }

    public Categoria getCategoria() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaCategoria") != null) {
            categoria = (Categoria) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("pesquisaCategoria");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("pesquisaCategoria");
        }
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdGrupoCategoria() {
        return idGrupoCategoria;
    }

    public void setIdGrupoCategoria(int idGrupoCategoria) {
        this.idGrupoCategoria = idGrupoCategoria;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public void setListaCategoria(List<Categoria> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }
}
