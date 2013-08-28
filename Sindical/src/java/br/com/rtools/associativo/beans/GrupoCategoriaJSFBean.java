package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoCategoria;
import br.com.rtools.associativo.db.GrupoCategoriaDB;
import br.com.rtools.associativo.db.GrupoCategoriaDBToplink;
import java.util.ArrayList;
import java.util.List;

public class GrupoCategoriaJSFBean {
    private GrupoCategoria grupoCategoria = new GrupoCategoria();
    private String msgConfirma = "";
    private int idIndex = -1;
    private List <GrupoCategoria> listaGrupoCategoria = new ArrayList();

    public String salvar(){
        GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
        if (grupoCategoria.getGrupoCategoria().equals("") || grupoCategoria.getGrupoCategoria() == null){
            msgConfirma = "Digite um nome para o grupo!";
            return null;
        }
        if(grupoCategoria.getNrProximaMatricula() < 0 ){
            msgConfirma = "Número da matricula inválida!";
            return null;
        }
        if (grupoCategoria.getId() == -1){
            if (db.insert(grupoCategoria)){
                msgConfirma = "Grupo salvo com sucesso!";
                grupoCategoria = new GrupoCategoria();
            }else{
                msgConfirma = "Erro ao salvar grupo!";
            }
        }else{
            if (db.update(grupoCategoria)){
                msgConfirma = "Grupo atualizado com sucesso.";
                grupoCategoria = new GrupoCategoria();
            }else
                msgConfirma = "Erro ao atualizar grupo!";
        }
        return null;
    }

    public String excluir(){
        GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
        if (grupoCategoria.getId() == -1){
            msgConfirma = "Selecione um grupo para ser excluído!";
            return null;
        }
        if (db.delete(db.pesquisaCodigo(grupoCategoria.getId()))){
            msgConfirma = "Grupo deletado com sucesso!";
            grupoCategoria = new GrupoCategoria();
        }else{
            msgConfirma = "Erro ao deletar grupo!";
        }
        return null;
    }

    public String editar(){
        grupoCategoria = (GrupoCategoria) listaGrupoCategoria.get(idIndex);
        return "grupoCategoria";
    }

    public String novo(){
        grupoCategoria = new GrupoCategoria();
        listaGrupoCategoria.clear();
        msgConfirma = "";
        return "grupoCategoria";
    }

    public GrupoCategoria getGrupoCategoria() {
        return grupoCategoria;
    }

    public void setGrupoCategoria(GrupoCategoria grupoCategoria) {
        this.grupoCategoria = grupoCategoria;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public List <GrupoCategoria> getListaGrupoCategoria() {
        GrupoCategoriaDB db = new GrupoCategoriaDBToplink();
        listaGrupoCategoria = db.pesquisaTodos();
        return listaGrupoCategoria;
    }

    public void setListaGrupoCategoria(List <GrupoCategoria> listaGrupoCategoria) {
        this.listaGrupoCategoria = listaGrupoCategoria;
    }

}
