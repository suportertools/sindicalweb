package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import br.com.rtools.associativo.GrupoEvento;
import br.com.rtools.associativo.db.DescricaoEventoDB;
import br.com.rtools.associativo.db.DescricaoEventoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class DescricaoEventoBean implements Serializable {

    private DescricaoEvento descricaoEvento = new DescricaoEvento();
    private int idGrupoEvento = 0;
    private List<DescricaoEvento> listaDescricaoEvento = new ArrayList<DescricaoEvento>();
    private List<SelectItem> listaGrupoEvento = new ArrayList<SelectItem>();

    public void salvar() {
        if (descricaoEvento.getDescricao().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite um nome para o Evento!");
            PF.update("formDescricaoEvento:i_msg");
            return;
        }
        if (getListaGrupoEvento().isEmpty()) {
            GenericaMensagem.warn("Validaçao", "Lista de Grupo Evento vazia!");
            PF.update("formDescricaoEvento:i_msg");
            return;
        }
        DescricaoEventoDB dedb = new DescricaoEventoDBToplink();
        SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
        GrupoEvento grupoEvento = (GrupoEvento) sadb.find(new GrupoEvento(), Integer.parseInt(getListaGrupoEvento().get(idGrupoEvento).getDescription()));
        descricaoEvento.setGrupoEvento(grupoEvento);
        if (dedb.existeDescricaoEvento(descricaoEvento)) {
            GenericaMensagem.warn("Validaçao", "Descrição já cadastrada para o grupo selecionado!");
            PF.update("formDescricaoEvento:i_msg");
            return;
        }
        if (descricaoEvento.getId() == -1) {
            sadb.abrirTransacao();
            if (sadb.inserirObjeto(descricaoEvento)) {
                sadb.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Registro adicionado");
                listaDescricaoEvento.clear();
                NovoLog novoLog = new NovoLog();
                novoLog.save(descricaoEvento.toString());
                descricaoEvento = new DescricaoEvento();
                PF.update("formDescricaoEvento");
            } else {
                sadb.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
                PF.update("formDescricaoEvento:i_msg");
            }
        }
    }

    public void excluir(DescricaoEvento de) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        DescricaoEventoDB db = new DescricaoEventoDBToplink();
        List<AEvento> ae = db.listaEventoPorDescricao(de.getId());
        sv.abrirTransacao();
        for (AEvento ae1 : ae) {
            if (!sv.deletarObjeto((AEvento) sv.find(ae1))) {
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Ao excluir evento!");
                PF.update("formDescricaoEvento:i_msg");
                return;
            }
        }
        if (!sv.deletarObjeto((DescricaoEvento) sv.find(de))) {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro", "Ao excluir registro");
            PF.update("formDescricaoEvento:i_msg");
        } else {
            NovoLog novoLog = new NovoLog();
            novoLog.delete(de.toString());
            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Registro excluído!");
            listaDescricaoEvento.clear();
            PF.update("formDescricaoEvento");
        }
    }

    public List<DescricaoEvento> getListaDescricaoEvento() {
        if (listaDescricaoEvento.isEmpty()) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            listaDescricaoEvento = (List<DescricaoEvento>) sadb.listaObjeto("DescricaoEvento", true);
        }
        return listaDescricaoEvento;
    }

    public void setListaDescricaoEvento(List<DescricaoEvento> listaDescricaoEvento) {
        this.listaDescricaoEvento = listaDescricaoEvento;
    }

    public List<SelectItem> getListaGrupoEvento() {
        if (listaGrupoEvento.isEmpty()) {
            SalvarAcumuladoDB sadb = new SalvarAcumuladoDBToplink();
            List<GrupoEvento> list = (List<GrupoEvento>) sadb.listaObjeto("GrupoEvento", true);
            for (int i = 0; i < list.size(); i++) {
                listaGrupoEvento.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listaGrupoEvento;
    }

    public DescricaoEvento getDescricaoEvento() {
        return descricaoEvento;
    }

    public void setDescricaoEvento(DescricaoEvento descricaoEvento) {
        this.descricaoEvento = descricaoEvento;
    }

    public int getIdGrupoEvento() {
        return idGrupoEvento;
    }

    public void setIdGrupoEvento(int idGrupoEvento) {
        this.idGrupoEvento = idGrupoEvento;
    }

    public void setListaGrupoEvento(List<SelectItem> listaGrupoEvento) {
        this.listaGrupoEvento = listaGrupoEvento;
    }
}
