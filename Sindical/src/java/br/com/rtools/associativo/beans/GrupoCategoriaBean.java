package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class GrupoCategoriaBean {

    private GrupoCategoria grupoCategoria;
    private List<GrupoCategoria> listGrupoCategoria;

    @PostConstruct
    public void init() {
        grupoCategoria = new GrupoCategoria();
        listGrupoCategoria = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void clear() {
        GenericaSessao.remove("grupoCategoriaBean");
    }

    public void save() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (grupoCategoria.getGrupoCategoria().equals("") || grupoCategoria.getGrupoCategoria() == null) {
            GenericaMensagem.warn("Validação", "Digite um nome para o grupo!");
            return;
        }
        if (grupoCategoria.getNrProximaMatricula() < 0) {
            GenericaMensagem.warn("Validação", "Número da matricula inválida!");
            return;
        }
        grupoCategoria.setGrupoCategoria(grupoCategoria.getGrupoCategoria().toUpperCase());
        di.openTransaction();
        if (grupoCategoria.getId() == -1) {
            if (!listGrupoCategoria.isEmpty()) {
                for (GrupoCategoria listaGrupoCategoria1 : listGrupoCategoria) {
                    if (listaGrupoCategoria1.getGrupoCategoria().equals(grupoCategoria.getGrupoCategoria())) {
                        GenericaMensagem.warn("Validação", "Grupo categoria já existe!");
                        PF.update("form_grupo_categoria:i_msg");
                        return;
                    }
                }
            }
            if (di.save(grupoCategoria)) {
                novoLog.save(
                        "ID: " + grupoCategoria.getId()
                        + " - Descrição: " + grupoCategoria.getGrupoCategoria()
                        + " - Próxima Matrícula: " + grupoCategoria.getNrProximaMatricula()
                );
                di.commit();
                GenericaMensagem.info("Sucesso", "Grupo salvo com sucesso!");
                grupoCategoria = new GrupoCategoria();
                listGrupoCategoria.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao salvar grupo!");
                di.rollback();
            }
        } else {
            GrupoCategoria gc = (GrupoCategoria) di.find(grupoCategoria);
            String beforeUpdate
                    = "ID: " + gc.getId()
                    + " - Descrição: " + gc.getGrupoCategoria()
                    + " - Próxima Matrícula: " + gc.getNrProximaMatricula();
            if (di.update(grupoCategoria)) {
                novoLog.update(beforeUpdate,
                        "ID: " + grupoCategoria.getId()
                        + " - Descrição: " + grupoCategoria.getGrupoCategoria()
                        + " - Próxima Matrícula: " + grupoCategoria.getNrProximaMatricula()
                );
                GenericaMensagem.info("Sucesso", "Grupo atualizado com sucesso.");
                di.commit();
                grupoCategoria = new GrupoCategoria();
                listGrupoCategoria.clear();
            } else {
                GenericaMensagem.warn("Erro", "Ao atualizar grupo!");
                di.rollback();
            }
        }
        PF.update("form_grupo_categoria:i_msg");
    }

    public void delete() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (grupoCategoria.getId() == -1) {
            GenericaMensagem.warn("Validação", "Selecione um grupo para ser excluído!");
            return;
        }
        di.openTransaction();
        if (di.delete(grupoCategoria)) {
            novoLog.delete(
                    "ID: " + grupoCategoria.getId()
                    + " - Descrição: " + grupoCategoria.getGrupoCategoria()
                    + " - Próxima Matrícula: " + grupoCategoria.getNrProximaMatricula()
            );
            GenericaMensagem.info("Sucesso", "Grupo deletado com sucesso!");
            di.commit();
            grupoCategoria = new GrupoCategoria();
            listGrupoCategoria.clear();
            PF.update("i_msg");
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Erro ao deletar grupo!");
        }
    }

    public String editar(GrupoCategoria gc) {
        grupoCategoria = gc;
        return null;
    }

    public GrupoCategoria getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(GrupoCategoria grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public List<GrupoCategoria> getListGrupoCategoria() {
        if (listGrupoCategoria.isEmpty()) {
            DaoInterface di = new Dao();
            listGrupoCategoria = (List<GrupoCategoria>) di.list(new GrupoCategoria(), true);
        }
        return listGrupoCategoria;
    }

    public void setListGrupoCategoria(List<GrupoCategoria> listGrupoCategoria) {
        this.listGrupoCategoria = listGrupoCategoria;
    }
}
