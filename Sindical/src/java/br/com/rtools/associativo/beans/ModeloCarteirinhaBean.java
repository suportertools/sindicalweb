package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.ModeloCarteirinha;
import br.com.rtools.associativo.ModeloCarteirinhaCategoria;
import br.com.rtools.associativo.db.SocioCarteirinhaDB;
import br.com.rtools.associativo.db.SocioCarteirinhaDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ModeloCarteirinhaBean { 

    private ModeloCarteirinha modelo = new ModeloCarteirinha();
    private ModeloCarteirinhaCategoria modeloCategoria = new ModeloCarteirinhaCategoria();
    private int idCategoria = 0;
    private int idRotina = 0;
    private boolean[] disabled = new boolean[]{false};
    private List<SelectItem> listaCategoria = new ArrayList<SelectItem>();
    private List<SelectItem> listaRotina = new ArrayList<SelectItem>();
    private List<ModeloCarteirinhaCategoria> listaModelo = new ArrayList<ModeloCarteirinhaCategoria>();

    public void salvar() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        if (modelo.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite um nome para o Modelo!");
            return;
        }

        if (modelo.getJasper().isEmpty()) {
            GenericaMensagem.warn("Erro", "Digite o caminho do Jasper!");
            return;
        }

        sv.abrirTransacao();

        Rotina rotina = (Rotina) sv.pesquisaCodigo(Integer.valueOf(listaRotina.get(idRotina).getDescription()), "Rotina");

        Categoria categoria = null;
        if (Integer.valueOf(listaCategoria.get(idCategoria).getDescription()) != -1) {
            categoria = (Categoria) sv.pesquisaCodigo(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()), "Categoria");
        }

        if (modelo.getId() == -1) {
            if (!sv.inserirObjeto(modelo)) {
                GenericaMensagem.warn("Erro", "Não foi possível adicionar Modelo!");
                sv.desfazerTransacao();
                return;
            }

            ModeloCarteirinhaCategoria mcc = new ModeloCarteirinhaCategoria(
                    -1,
                    rotina,
                    categoria,
                    modelo
            );

            if (!sv.inserirObjeto(mcc)) {
                GenericaMensagem.warn("Erro", "Não foi possível salvar Modelo Categoria!");
                sv.desfazerTransacao();
                return;
            }
        } else {
            if (!sv.alterarObjeto(modelo)) {
                GenericaMensagem.warn("Erro", "Não foi possível alterar Modelo!");
                sv.desfazerTransacao();
                return;
            }

            SocioCarteirinhaDB db = new SocioCarteirinhaDBToplink();

            if (db.pesquisaModeloCarteirinhaCategoria(modelo.getId(), (categoria == null) ? -1 : categoria.getId(), rotina.getId()) == null) {
                ModeloCarteirinhaCategoria mcc = new ModeloCarteirinhaCategoria(
                        -1,
                        rotina,
                        categoria,
                        modelo
                );

                if (!sv.inserirObjeto(mcc)) {
                    GenericaMensagem.warn("Erro", "Não foi possível salvar Modelo Categoria!");
                    sv.desfazerTransacao();
                    return;
                }
            }
        }

        sv.comitarTransacao();
        listaModelo.clear();
        //modelo = new ModeloCarteirinha();
        GenericaMensagem.info("Sucesso", "Modelo Salvo!");
    }

    public void novo() {
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("modeloCarteirinhaBean", new ModeloCarteirinhaBean());
    }

    public void excluir(ModeloCarteirinhaCategoria linha) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find(linha))) {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Não foi possível excluir Modelo!");
            return;
        }

        sv.comitarTransacao();
        listaModelo.clear();
        GenericaMensagem.info("Sucesso", "Modelo Excluído!");
    }

    public void editar(ModeloCarteirinhaCategoria linha) {
        modeloCategoria = linha;
        modelo = modeloCategoria.getModeloCarteirinha();
        boolean cat = false;
        if (modeloCategoria.getCategoria() != null) {
            for (int i = 0; i < listaCategoria.size(); i++) {
                if (Integer.valueOf(listaCategoria.get(i).getDescription()) == modeloCategoria.getCategoria().getId()) {
                    idCategoria = i;
                    cat = true;
                }
            }
        }

        if (!cat) {
            idCategoria = 0;
        }

        for (int i = 0; i < listaRotina.size(); i++) {
            if (Integer.valueOf(listaRotina.get(i).getDescription()) == modeloCategoria.getRotina().getId()) {
                idRotina = i;
            }
        }
    }

    public ModeloCarteirinha getModelo() {
        return modelo;
    }

    public void setModelo(ModeloCarteirinha modelo) {
        this.modelo = modelo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public int getIdRotina() {
        return idRotina;
    }

    public void setIdRotina(int idRotina) {
        this.idRotina = idRotina;
    }

    public List<SelectItem> getListaCategoria() {
        if (listaCategoria.isEmpty()) {
            List<Categoria> result = (new SalvarAcumuladoDBToplink()).listaObjeto("Categoria");

            if (!result.isEmpty()) {
                listaCategoria.add(new SelectItem(0, "Sem Categoria", "-1"));
                for (int i = 1; (i - 1) < result.size(); i++) {
                    listaCategoria.add(new SelectItem(i, result.get((i - 1)).getCategoria(), Integer.toString(result.get((i - 1)).getId())));
                }
            } else {
                listaCategoria.add(new SelectItem(0, "Nenhuma Categoria encontrada", "0"));
            }
        }
        return listaCategoria;
    }

    public void setListaCategoria(List<SelectItem> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()) {
            List<Rotina> result = new ArrayList<Rotina>();
            result.add((Rotina) new SalvarAcumuladoDBToplink().pesquisaCodigo(170, "Rotina"));
            result.add((Rotina) new SalvarAcumuladoDBToplink().pesquisaCodigo(122, "Rotina"));

            if (!result.isEmpty()) {
                for (int i = 0; i < result.size(); i++) {
                    listaRotina.add(new SelectItem(i, result.get(i).getRotina(), Integer.toString(result.get(i).getId())));
                }
            } else {
                listaRotina.add(new SelectItem(0, "Nenhuma Rotina encontrada", "0"));
            }
        }
        return listaRotina;
    }

    public void setListaRotina(List<SelectItem> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public List<ModeloCarteirinhaCategoria> getListaModelo() {
        if (listaModelo.isEmpty()) {
            listaModelo = new SalvarAcumuladoDBToplink().listaObjeto("ModeloCarteirinhaCategoria");
        }
        return listaModelo;
    }

    public void setListaModelo(List<ModeloCarteirinhaCategoria> listaModelo) {
        this.listaModelo = listaModelo;
    }

    public boolean[] getDisabled() {
        if (!getListaRotina().get(idRotina).getDescription().isEmpty()) {
            if (Integer.parseInt(getListaRotina().get(idRotina).getDescription()) == 122) {
                idCategoria = 0;
                disabled[0] = true;
            } else {
                disabled[0] = false;

            }
        }
        return disabled;
    }

    public void setDisabled(boolean[] disabled) {
        this.disabled = disabled;
    }

}
