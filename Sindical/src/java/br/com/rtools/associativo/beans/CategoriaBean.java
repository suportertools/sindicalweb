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
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.model.SelectItem;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CategoriaBean implements Serializable {

    private Categoria categoria;
    private int idGrupoCategoria;
    private List<Categoria> listCategoria;
    private List<SelectItem> listGrupoCategoria;
    private boolean limpar;
    private List list;
    private List<SelectItem> listServicos;

    @PostConstruct
    public void init() {
        categoria = new Categoria();
        idGrupoCategoria = 0;
        listCategoria = new ArrayList();
        listGrupoCategoria = new ArrayList<SelectItem>();
        limpar = false;
        list = new ArrayList();
        listServicos = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("categoriaBean");
        GenericaSessao.remove("pesquisaCategoria");
    }

    public void save() {
        if (categoria.getCategoria().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite uma categoria!");
            return;
        }
        ServicoCategoriaDB dbSeC = new ServicoCategoriaDBToplink();
        ServicoCategoria servicoCategoria = new ServicoCategoria();
        CategoriaDB db = new CategoriaDBToplink();
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        if (categoria.getId() == -1) {
            categoria.setGrupoCategoria((GrupoCategoria) dao.find(new GrupoCategoria(), Integer.parseInt(getListGrupoCategoria().get(idGrupoCategoria).getDescription())));
            if (db.insert(categoria)) {
                novoLog.save(
                        "ID: " + categoria.getId()
                        + " - Categoria: " + categoria.getCategoria()
                        + " - Grupo Categoria: (" + categoria.getGrupoCategoria().getId() + ") " + categoria.getGrupoCategoria().getGrupoCategoria()
                        + " - Carência Balcão: " + categoria.getNrCarenciaBalcao()
                        + " - Desc. Folha: " + categoria.getNrCarenciaDescFolha()
                        + " - Empresa Obrigatória: " + categoria.isEmpresaObrigatoria()
                        + " - Dias: [Dom][" + categoria.isUsaClubeDomingo() + "]"
                        + "[Seg][" + categoria.isUsaClubeSegunda() + "]"
                        + "[Ter][" + categoria.isUsaClubeTerca() + "]"
                        + "[Qua][" + categoria.isUsaClubeQuarta() + "]"
                        + "[Qui][" + categoria.isUsaClubeQuinta() + "]"
                        + "[Sex][" + categoria.isUsaClubeSexta() + "]"
                        + "[Sab][" + categoria.isUsaClubeSabado() + "]"
                );
                GenericaMensagem.info("Sucesso", "Registro inserido");
                limpar = false;
            } else {
                GenericaMensagem.warn("Erro", "Ao inserir registro");
            }
            for (int i = 0; i < list.size(); i++) {
                if (Integer.parseInt(listServicos.get(Integer.parseInt((String) ((DataObject) list.get(i)).getArgumento1())).getDescription()) != -1) {
                    servicoCategoria.setCategoria(categoria);
                    servicoCategoria.setParentesco((Parentesco) ((DataObject) list.get(i)).getArgumento0());
                    servicoCategoria.setServicos((Servicos) new Dao().find(new Servicos(), Integer.parseInt(
                            listServicos.get(Integer.parseInt((String) ((DataObject) list.get(i)).getArgumento1())).getDescription())));
                    dbSeC.insert(servicoCategoria);
                    servicoCategoria = new ServicoCategoria();
                }
            }
            list = new ArrayList();
        } else {
            categoria.setGrupoCategoria((GrupoCategoria) dao.find(new GrupoCategoria(), Integer.parseInt(getListGrupoCategoria().get(idGrupoCategoria).getDescription())));
            Categoria c = (Categoria) dao.find(new Categoria(), categoria.getId());
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Categoria: " + c.getCategoria()
                    + " - Grupo Categoria: (" + c.getGrupoCategoria().getId() + ") " + c.getGrupoCategoria().getGrupoCategoria()
                    + " - Carência Balcão: " + c.getNrCarenciaBalcao()
                    + " - Desc. Folha: " + c.getNrCarenciaDescFolha()
                    + " - Empresa Obrigatória: " + c.isEmpresaObrigatoria()
                    + " - Dias: [Dom][" + c.isUsaClubeDomingo() + "]"
                    + "[Seg][" + c.isUsaClubeSegunda() + "]"
                    + "[Ter][" + c.isUsaClubeTerca() + "]"
                    + "[Qua][" + c.isUsaClubeQuarta() + "]"
                    + "[Qui][" + c.isUsaClubeQuinta() + "]"
                    + "[Sex][" + c.isUsaClubeSexta() + "]"
                    + "[Sab][" + c.isUsaClubeSabado() + "]";
            if (db.update(categoria)) {
                novoLog.update(beforeUpdate,
                        "ID: " + categoria.getId()
                        + " - Categoria: " + categoria.getCategoria()
                        + " - Grupo Categoria: (" + categoria.getGrupoCategoria().getId() + ") " + categoria.getGrupoCategoria().getGrupoCategoria()
                        + " - Carência Balcão: " + categoria.getNrCarenciaBalcao()
                        + " - Desc. Folha: " + categoria.getNrCarenciaDescFolha()
                        + " - Empresa Obrigatória: " + categoria.isEmpresaObrigatoria()
                        + " - Dias: [Dom][" + categoria.isUsaClubeDomingo() + "]"
                        + "[Seg][" + categoria.isUsaClubeSegunda() + "]"
                        + "[Ter][" + categoria.isUsaClubeTerca() + "]"
                        + "[Qua][" + categoria.isUsaClubeQuarta() + "]"
                        + "[Qui][" + categoria.isUsaClubeQuinta() + "]"
                        + "[Sex][" + categoria.isUsaClubeSexta() + "]"
                        + "[Sab][" + categoria.isUsaClubeSabado() + "]"
                );
                GenericaMensagem.info("Sucesso", "Registro atualizado");
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar registro");
            }
            for (int i = 0; i < list.size(); i++) {
                servicoCategoria = (ServicoCategoria) ((DataObject) list.get(i)).getArgumento2();
                if (Integer.parseInt(listServicos.get(Integer.parseInt((String) ((DataObject) list.get(i)).getArgumento1())).getDescription()) == -1) {
                    if (servicoCategoria.getId() != -1) {
                        servicoCategoria = dbSeC.pesquisaCodigo(servicoCategoria.getId());
                        dbSeC.delete(servicoCategoria);
                    }
                } else {
                    servicoCategoria.setServicos((Servicos) new Dao().find(new Servicos(), Integer.parseInt(
                            listServicos.get(Integer.parseInt((String) ((DataObject) list.get(i)).getArgumento1())).getDescription())));
                    servicoCategoria.setCategoria(categoria);
                    servicoCategoria.setParentesco((Parentesco) ((DataObject) list.get(i)).getArgumento0());
                    if (servicoCategoria.getId() == -1) {
                        dbSeC.insert(servicoCategoria);
                    } else {
                        dbSeC.update(servicoCategoria);
                    }
                }
            }
            list = new ArrayList();
        }
    }

    public void delete() {
        NovoLog novoLog = new NovoLog();
        Dao dao = new Dao();
        dao.openTransaction();
        if (dao.delete(categoria)) {
            dao.commit();
            novoLog.delete(
                    "ID: " + categoria.getId()
                    + " - Categoria: " + categoria.getCategoria()
                    + " - Grupo Categoria: (" + categoria.getGrupoCategoria().getId() + ") " + categoria.getGrupoCategoria().getGrupoCategoria()
                    + " - Carência Balcão: " + categoria.getNrCarenciaBalcao()
                    + " - Desc. Folha: " + categoria.getNrCarenciaDescFolha()
                    + " - Empresa Obrigatória: " + categoria.isEmpresaObrigatoria()
                    + " - Dias: [Dom][" + categoria.isUsaClubeDomingo() + "]"
                    + "[Seg][" + categoria.isUsaClubeSegunda() + "]"
                    + "[Ter][" + categoria.isUsaClubeTerca() + "]"
                    + "[Qua][" + categoria.isUsaClubeQuarta() + "]"
                    + "[Qui][" + categoria.isUsaClubeQuinta() + "]"
                    + "[Sex][" + categoria.isUsaClubeSexta() + "]"
                    + "[Sab][" + categoria.isUsaClubeSabado() + "]"
            );
            limpar = true;
            GenericaMensagem.info("Sucesso", "Registro excluído");
        } else {
            dao.rollback();
            GenericaMensagem.warn("Erro", "Ao excluir registro");
        }
        categoria = new Categoria();
    }

    public void clear() {
        clear(1);
    }

    public void clear(int tcase) {
        if (tcase == 1) {
            GenericaSessao.remove("categoriaBean");
        }
    }

    public String edit(Categoria c) {
        categoria = c;
        GenericaSessao.put("pesquisaCategoria", categoria);
        GenericaSessao.put("linkClicado", true);
        list.clear();
        for (int i = 0; i < getListGrupoCategoria().size(); i++) {
            if (Integer.valueOf(getListGrupoCategoria().get(i).getDescription()) == categoria.getGrupoCategoria().getId()) {
                idGrupoCategoria = i;
            }
        }
        return (String) GenericaSessao.getString("urlRetorno");
    }

    public void updateServicos(int index) {
        if (((Parentesco) ((DataObject) list.get(index)).getArgumento0()).getId() == 1) {
            for (int i = 0; i < list.size(); i++) {
                if (Integer.parseInt(String.valueOf(((DataObject) list.get(i)).getArgumento1())) == 0) {
                    ((DataObject) list.get(i)).setArgumento1(Integer.parseInt(String.valueOf(((DataObject) list.get(index)).getArgumento1())));
                }
            }
        }
    }

    public List<Categoria> getListCategoria() {
        if (listCategoria.isEmpty()) {
            Dao dao = new Dao();
            listCategoria = dao.list(new Categoria());
        }
        return listCategoria;
    }

    public List<SelectItem> getListGrupoCategoria() {
        if (listGrupoCategoria.isEmpty()) {
            Dao dao = new Dao();
            List select = (List<GrupoCategoria>) dao.list(new GrupoCategoria(), true);
            for (int i = 0; i < select.size(); i++) {
                listGrupoCategoria.add(new SelectItem(i, (String) ((GrupoCategoria) select.get(i)).getGrupoCategoria(), "" + ((GrupoCategoria) select.get(i)).getId()));
            }
        }
        return listGrupoCategoria;
    }

    public void setListGrupoCategoria(List<SelectItem> listGrupoCategoria) {
        this.listGrupoCategoria = listGrupoCategoria;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            int i = 0;
            ServicoRotinaDB db = new ServicoRotinaDBToplink();
            List select = db.pesquisaTodosServicosComRotinas(130);
            listServicos.add(new SelectItem(new Integer(i),
                    " -- NENHUM -- ",
                    "-1"));
            while (i < select.size()) {
                listServicos.add(new SelectItem(new Integer(i + 1),
                        (String) ((Servicos) select.get(i)).getDescricao(),
                        Integer.toString(((Servicos) select.get(i)).getId())));
                i++;
            }
        }
        return listServicos;
    }

    public List getListParentescos() {
        if (list.isEmpty()) {
            DataObject dtObj = null;
            ParentescoDB db = new ParentescoDao();
            ServicoCategoriaDB dbSeC = new ServicoCategoriaDBToplink();
            List<ServicoCategoria> listaSerCat = dbSeC.pesquisaServCatPorId(categoria.getId());
            List<Parentesco> listaPar = db.pesquisaTodos();
            if (listaSerCat.isEmpty()) {
                for (int i = 0; i < listaPar.size(); i++) {
                    dtObj = new DataObject(listaPar.get(i), 0, new ServicoCategoria(), null, null, null);
                    list.add(dtObj);
                }
            } else {
                int index = 0;
                boolean temServico = false;
                for (int i = 0; i < listaPar.size(); i++) {
                    for (int x = 0; x < listaSerCat.size(); x++) {
                        if (listaPar.get(i).getId() == listaSerCat.get(x).getParentesco().getId()) {
                            for (int w = 0; w < listServicos.size(); w++) {
                                if (listaSerCat.get(x).getServicos().getId() == (Integer.parseInt(listServicos.get(w).getDescription()))) {
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
                    list.add(dtObj);
                }
            }
        }
        return list;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getIdGrupoCategoria() {
        return idGrupoCategoria;
    }

    public void setIdGrupoCategoria(int idGrupoCategoria) {
        this.idGrupoCategoria = idGrupoCategoria;
    }

    public void setListCategoria(List<Categoria> listCategoria) {
        this.listCategoria = listCategoria;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }

    public void semanaToda() {
        categoria.setUsaClubeDomingo(true);
        categoria.setUsaClubeSegunda(true);
        categoria.setUsaClubeTerca(true);
        categoria.setUsaClubeQuarta(true);
        categoria.setUsaClubeQuinta(true);
        categoria.setUsaClubeSexta(true);
        categoria.setUsaClubeSabado(true);
    }
}
