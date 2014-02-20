package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GrupoCategoriaBean {

    private GrupoCategoria grupoCategoria = new GrupoCategoria();
    private List<GrupoCategoria> listaGrupoCategoria = new ArrayList();

    public void salvar() {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (grupoCategoria.getGrupoCategoria().equals("") || grupoCategoria.getGrupoCategoria() == null) {
            GenericaMensagem.warn("Validação", "Digite um nome para o grupo!");
            return;
        }
        if (grupoCategoria.getNrProximaMatricula() < 0) {
            GenericaMensagem.warn("Validação", "Número da matricula inválida!");
            return;
        }
        grupoCategoria.setGrupoCategoria(grupoCategoria.getGrupoCategoria().toUpperCase());
        if(!listaGrupoCategoria.isEmpty()) {
            for (GrupoCategoria listaGrupoCategoria1 : listaGrupoCategoria) {
                if (listaGrupoCategoria1.getGrupoCategoria().equals(grupoCategoria.getGrupoCategoria())) {
                    GenericaMensagem.warn("Validação", "Grupo categoria já existe!");
                    PF.update("form_grupo_categoria:i_msg");
                    return;
                }
            }
        }
        dB.abrirTransacao();
        if (grupoCategoria.getId() == -1) {
            if (dB.inserirObjeto(grupoCategoria)) {
                dB.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Grupo salvo com sucesso!");
                grupoCategoria = new GrupoCategoria();
                listaGrupoCategoria.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao salvar grupo!");
                dB.desfazerTransacao();
            }
        } else {
            if (dB.alterarObjeto(grupoCategoria)) {
                GenericaMensagem.info("Sucesso", "Grupo atualizado com sucesso.");
                dB.comitarTransacao();
                grupoCategoria = new GrupoCategoria();
                listaGrupoCategoria.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar grupo!");
                dB.desfazerTransacao();
            }
        }
        PF.update("form_grupo_categoria:i_msg");
    }

    public void excluir() {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        if (grupoCategoria.getId() == -1) {
            GenericaMensagem.warn("Validação", "Selecione um grupo para ser excluído!");
            return;
        }
        dB.abrirTransacao();
        if (dB.deletarObjeto((GrupoCategoria) dB.pesquisaCodigo(grupoCategoria.getId(), "GrupoCategoria"))) {
            GenericaMensagem.info("Sucesso", "Grupo deletado com sucesso!");
            dB.comitarTransacao();
            grupoCategoria = new GrupoCategoria();
            listaGrupoCategoria.clear();
            PF.update("i_msg");
        } else {
            dB.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Erro ao deletar grupo!");
        }
    }

    public String editar(GrupoCategoria gc) {
        grupoCategoria = gc;
        return null;
    }

    public void novo() {
        grupoCategoria = new GrupoCategoria();
        listaGrupoCategoria.clear();
    }

    public GrupoCategoria getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(GrupoCategoria grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public List<GrupoCategoria> getListaGrupoCategoria() {
        if (listaGrupoCategoria.isEmpty()) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            listaGrupoCategoria = (List<GrupoCategoria>) sadb.listaObjeto("GrupoCategoria", true);
        }
        return listaGrupoCategoria;
    }

    public void setListaGrupoCategoria(List<GrupoCategoria> listaGrupoCategoria) {
        this.listaGrupoCategoria = listaGrupoCategoria;
    }
}
