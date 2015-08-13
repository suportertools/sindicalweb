package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.Parentesco;
import br.com.rtools.associativo.ValidadeCartao;
import br.com.rtools.associativo.dao.ValidadeCartaoDao;
import br.com.rtools.associativo.db.ParentescoDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
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
public class ValidadeCartaoBean implements Serializable {

    private ValidadeCartao validadeCartao;
    private List<SelectItem> listCategoria;
    private List<SelectItem> listParentesco;
    private List<ValidadeCartao> listValidadeCartao;
    private Integer idCategoria;
    private Integer idParentesco;
    private String validade;
    private String filter;

    @PostConstruct
    public void init() {
        validadeCartao = new ValidadeCartao();
        listCategoria = new ArrayList<>();
        listParentesco = new ArrayList<>();
        listValidadeCartao = new ArrayList<>();
        idCategoria = null;
        idParentesco = null;
        validade = "";
        filter = "categoria";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("validadeCartaoBean");
    }

    public void edit(ValidadeCartao vc) {
        validadeCartao = (ValidadeCartao) new Dao().rebind(vc);
    }

    /**
     * 1 - Categoria; 2 = Parentesco
     *
     * @param tcase
     * @param id
     */
    public void editAll(Integer tcase, Integer id) {

    }

    public void save() {

    }

    /**
     *
     * @param tcase 0 = Adiciona somente um; 1 Adiciona todas categorias + 1
     * parentesco = ; 2 = Adiciona todos os parentescos e uma categoria;
     */
    public void save(Integer tcase) {
        Boolean cont = false;
        Dao dao = new Dao();
        Categoria c;
        Parentesco p;
        String qtdeMeses = validadeCartao.getNrValidadeMesesString();
        String validadeFixa = validadeCartao.getValidadeFixaString();
        if (listParentesco.isEmpty() && validadeCartao.getId() == null) {
            GenericaMensagem.warn("Validação", "Não existem parentescos disponiveis para esta categoria!");
            return;
        }
        if (validadeCartao.getNrValidadeMeses() <= 0 && validadeCartao.getDtValidadeFixa() == null) {
            GenericaMensagem.warn("Validação", "Informar número de meses!");
            return;
        }
        if (tcase == 0) {
            ValidadeCartao vc = new ValidadeCartao();
            vc.setValidadeFixaString(validadeFixa);
            vc.setNrValidadeMesesString(qtdeMeses);
            if (validadeCartao.getNrValidadeMeses() > 0 && validadeCartao.getDtValidadeFixa() != null) {
                validadeCartao.setNrValidadeMeses(0);
            }
            if (validadeCartao.getId() == null) {
                if (idCategoria == null) {
                    GenericaMensagem.warn("Validação", "Informar categoria!");
                    return;
                }
                if (idParentesco == null) {
                    GenericaMensagem.warn("Validação", "Informar parentesco!");
                    return;
                }
                c = (Categoria) dao.find(new Categoria(), idCategoria);
                p = (Parentesco) dao.find(new Parentesco(), idParentesco);
                vc.setCategoria(c);
                vc.setParentesco(p);
                if (!dao.save(vc, true)) {
                    GenericaMensagem.warn("Erro", "Ao adicionar registro!");
                    return;
                }
            } else {
                if (!dao.update(validadeCartao, true)) {
                    GenericaMensagem.warn("Erro", "Ao adicionar registro!");
                    return;
                }
            }
            GenericaMensagem.info("Sucesso", "Registro " + (validadeCartao.getId() == null ? "inserido" : "atualizado") + " com sucesso");
            validadeCartao = new ValidadeCartao();
            listParentesco.clear();
            idParentesco = null;
            listValidadeCartao.clear();
            return;
        } else if (tcase == 1) {
            if (validadeCartao.getNrValidadeMeses() > 0 && validadeCartao.getDtValidadeFixa() != null) {
                validadeCartao.setNrValidadeMeses(0);
            }
            p = (Parentesco) dao.find(new Parentesco(), idParentesco);
            for (int i = 0; i < listCategoria.size(); i++) {
                ValidadeCartao vc = new ValidadeCartaoDao().findByCategoriaParentesco((Integer) listCategoria.get(i).getValue(), p.getId());
                if (vc == null) {
                    vc = new ValidadeCartao();
                    vc.setCategoria((Categoria) dao.find(new Categoria(), (Integer) listCategoria.get(i).getValue()));
                    vc.setParentesco(p);
                    vc.setNrValidadeMeses(validadeCartao.getNrValidadeMeses());
                    vc.setDtValidadeFixa(validadeCartao.getDtValidadeFixa());
                    if (!dao.save(vc, true)) {
                        return;
                    }
                }
            }
            validadeCartao = new ValidadeCartao();
            idParentesco = null;
            idCategoria = null;
            listCategoria.clear();
            listParentesco.clear();
            listValidadeCartao.clear();
        } else if (tcase == 2) {
            c = (Categoria) dao.find(new Categoria(), idCategoria);
            if (validadeCartao.getNrValidadeMeses() > 0 && validadeCartao.getDtValidadeFixa() != null) {
                validadeCartao.setNrValidadeMeses(0);
            }
            if (!listParentesco.isEmpty()) {
                for (int i = 0; i < listParentesco.size(); i++) {
                    ValidadeCartao vc = new ValidadeCartaoDao().findByCategoriaParentesco(idCategoria, (Integer) listParentesco.get(i).getValue());
                    if (vc == null) {
                        vc = new ValidadeCartao();
                        vc.setCategoria(c);
                        vc.setParentesco((Parentesco) dao.find(new Parentesco(), (Integer) listParentesco.get(i).getValue()));
                        vc.setNrValidadeMeses(validadeCartao.getNrValidadeMeses());
                        vc.setDtValidadeFixa(validadeCartao.getDtValidadeFixa());
                        if (!dao.save(vc, true)) {
                            return;
                        }
                    }
                }
                cont = true;
            }
            if (cont == false) {
                for (int i = 0; i < listValidadeCartao.size(); i++) {
                    listValidadeCartao.get(i).setNrValidadeMeses(validadeCartao.getNrValidadeMeses());
                    listValidadeCartao.get(i).setDtValidadeFixa(validadeCartao.getDtValidadeFixa());
                    if (!dao.update(listValidadeCartao.get(i), true)) {
                        return;
                    }
                }
            }
            if (!listParentesco.isEmpty()) {
                GenericaMensagem.info("Sucesso", "Registro inseridos com sucesso");
            }
            validadeCartao = new ValidadeCartao();
            idParentesco = null;
            listParentesco.clear();
            listValidadeCartao.clear();
        }
    }

    public void delete() {
        if (validadeCartao.getId() != null) {
            delete(validadeCartao);
        }
    }

    /**
     * @param vc
     */
    public void delete(ValidadeCartao vc) {
        if (!new Dao().delete(vc, true)) {
            GenericaMensagem.warn("Erro", "Ao excluir registro!");
            return;
        }
        validadeCartao = new ValidadeCartao();
        idParentesco = null;
        listParentesco.clear();
        listValidadeCartao.clear();
        GenericaMensagem.info("Sucesso", "Registro excluido");
    }

    public List<SelectItem> getListCategoria() {
        if (listCategoria.isEmpty()) {
            List<Categoria> list = new Dao().list(new Categoria(), true);
            for (int i = 0; i < list.size(); i++) {
                if (i == 0) {
                    idCategoria = list.get(i).getId();
                }
                listCategoria.add(new SelectItem(list.get(i).getId(), list.get(i).getCategoria()));
            }
        }
        return listCategoria;
    }

    public void setListCategoria(List<SelectItem> listCategoria) {
        this.listCategoria = listCategoria;
    }

    public List<SelectItem> getListParentesco() {
        if (listParentesco.isEmpty()) {
            if (idCategoria != null) {
                List<Parentesco> list = new ParentescoDao().findNotInByTabela("soc_validade_cartao", "id_categoria", Integer.toString(idCategoria));
                for (int i = 0; i < list.size(); i++) {
                    listParentesco.add(new SelectItem(list.get(i).getId(), list.get(i).getParentesco()));
                }
            }
        }
        return listParentesco;
    }

    public void setListParentesco(List<SelectItem> listParentesco) {
        this.listParentesco = listParentesco;
    }

    public List<ValidadeCartao> getListValidadeCartao() {
        if (listValidadeCartao.isEmpty()) {
            switch (filter) {
                case "todos":
                    listValidadeCartao = new Dao().list(new ValidadeCartao(), true);
                    break;
                case "categoria":
                    if (idCategoria != null) {
                        listValidadeCartao = new ValidadeCartaoDao().findAllByCategoria(idCategoria);
                    }
                    break;
                case "parentesco":
                    if (idParentesco != null) {
                        listValidadeCartao = new ValidadeCartaoDao().findAllByParentesco(idParentesco);
                    }
                    break;
            }
        }
        return listValidadeCartao;
    }

    public void setListValidadeCartao(List<ValidadeCartao> listValidadeCartao) {
        this.listValidadeCartao = listValidadeCartao;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public Integer getIdParentesco() {
        return idParentesco;
    }

    public void setIdParentesco(Integer idParentesco) {
        this.idParentesco = idParentesco;
    }

    public void clear() {
        GenericaSessao.remove("validadeCartaoBean");
    }

    public void clear(Integer tcase) {
        switch (tcase) {
            case 0:
                listValidadeCartao.clear();
                break;
            case 1:
                idParentesco = null;
                listParentesco.clear();
                filter = "categoria";
                listValidadeCartao.clear();
                break;
        }
    }

    public ValidadeCartao getValidadeCartao() {
        return validadeCartao;
    }

    public void setValidadeCartao(ValidadeCartao validadeCartao) {
        this.validadeCartao = validadeCartao;
    }

}
