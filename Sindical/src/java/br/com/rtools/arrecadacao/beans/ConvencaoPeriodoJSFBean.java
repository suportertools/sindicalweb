package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDB;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDBTopLink;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class ConvencaoPeriodoJSFBean {

    private ConvencaoPeriodo convencaoPeriodo = new ConvencaoPeriodo();
    private String msg = "";
    private int idConvencao = 0;
    private int idGrupoCidade = 0;
    private List<ConvencaoPeriodo> listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
    private List<SelectItem> listaConvencao = new ArrayList();
    private List<SelectItem> listaGrupoCidade = new ArrayList();

    public String novo() {
        convencaoPeriodo = new ConvencaoPeriodo();
        listaConvencaoPeriodos.clear();
        idConvencao = 0;
        idGrupoCidade = 0;
        // setMsg("");
        return null;
    }

    public void atualizarLista() {
//        if (listaConvencaoPeriodos == null)
        listaConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
    }

    public String editar(ConvencaoPeriodo cp) {
        convencaoPeriodo = cp;
        for (int i = 0; i < listaConvencao.size(); i++) {
            if (Integer.parseInt(listaConvencao.get(i).getDescription()) == convencaoPeriodo.getConvencao().getId()) {
                idConvencao = i;
                break;
            }
        }
        for (int i = 0; i < getListaGrupoCidade().size(); i++) {
            if (Integer.parseInt(getListaGrupoCidade().get(i).getDescription()) == convencaoPeriodo.getGrupoCidade().getId()) {
                idGrupoCidade = i;
                break;
            }
        }
        return null;
    }

    public String salvar() {
        ConvencaoPeriodoDB convencaoPeriodoDB = new ConvencaoPeriodoDBTopLink();
        if (getConvencaoPeriodo().getReferenciaInicial().equals("__/____") || getConvencaoPeriodo().getReferenciaInicial().equals("")) {
            // setMsg("Informar a referência inicial!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Informar a referência inicial!"));
            return null;
        }
        if (getConvencaoPeriodo().getReferenciaFinal().equals("__/____") || getConvencaoPeriodo().getReferenciaFinal().equals("")) {
            //setMsg("Informar a referência final!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Informar a referência final!"));
            return null;
        }
        getConvencaoPeriodo().getGrupoCidade().setId(Integer.parseInt(listaGrupoCidade.get(idGrupoCidade).getDescription()));
        getConvencaoPeriodo().getConvencao().setId(Integer.parseInt(listaConvencao.get(idConvencao).getDescription()));
        if (convencaoPeriodoDB.convencaoPeriodoExiste(getConvencaoPeriodo())) {
            // setMsg("Convenção período já existe!");
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", "Convenção período já existe!"));
            return null;
        }
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        if (getConvencaoPeriodo().getId() == -1) {
            db.abrirTransacao();
            if (db.inserirObjeto(getConvencaoPeriodo())) {
                db.comitarTransacao();
                convencaoPeriodo = new ConvencaoPeriodo();
                listaConvencaoPeriodos.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro inserido"));
                return null;
            } else {
                db.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao inserir esse registro!"));
                return null;
            }
        } else {
            db.abrirTransacao();
            if (db.alterarObjeto(getConvencaoPeriodo())) {
                db.comitarTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro atualizado"));
                return null;
            } else {
                db.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao inserir esse registro!"));
                return null;
            }
        }
    }

    public String excluir() {
        SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
        setConvencaoPeriodo((ConvencaoPeriodo) db.pesquisaObjeto(getConvencaoPeriodo().getId(), "ConvencaoPeriodo"));
        if (getConvencaoPeriodo().getId() != -1) {
            db.abrirTransacao();
            if (db.deletarObjeto(getConvencaoPeriodo())) {
                db.comitarTransacao();
                convencaoPeriodo = new ConvencaoPeriodo();
                listaConvencaoPeriodos.clear();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Registro excluído"));
                return null;
            } else {
                db.desfazerTransacao();
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", "Erro ao excluir esse registro!"));
                return null;
            }
        }
        return null;
    }

    public List<SelectItem> getListaConvencao() {
        if (listaConvencao.isEmpty()) {
            SalvarAcumuladoDB db = new SalvarAcumuladoDBToplink();
            List<Convencao> list = db.listaObjetoGenericoOrdem("Convencao");
            for (int i = 0; i < list.size(); i++) {
                listaConvencao.add(new SelectItem(new Integer(i),
                        list.get(i).getDescricao().toUpperCase(),
                        Integer.toString(list.get(i).getId())));
            }

        }
        return listaConvencao;
    }

    public void setListaConvencao(List<SelectItem> listaConvencao) {
        this.listaConvencao = listaConvencao;
    }

    public List<SelectItem> getListaGrupoCidade() {
        listaGrupoCidade.clear();
//        if (listaGrupoCidade.isEmpty()) {
        ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
        List<ConvencaoCidade> list = db.listaGrupoCidadePorConvencao(Integer.parseInt(listaConvencao.get(idConvencao).getDescription()));
        for (int i = 0; i < list.size(); i++) {
            listaGrupoCidade.add(new SelectItem(new Integer(i),
                    list.get(i).getGrupoCidade().getDescricao(),
                    Integer.toString(list.get(i).getGrupoCidade().getId())));
        }

//        }
        return listaGrupoCidade;
    }

    public void setListaGrupoCidade(List<SelectItem> listaGrupoCidade) {
        this.listaGrupoCidade = listaGrupoCidade;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getIdConvencao() {
        return idConvencao;
    }

    public void setIdConvencao(int idConvencao) {
        this.idConvencao = idConvencao;
    }

    public int getIdGrupoCidade() {
        return idGrupoCidade;
    }

    public void setIdGrupoCidade(int idGrupoCidade) {
        this.idGrupoCidade = idGrupoCidade;
    }

    public List<ConvencaoPeriodo> getListaConvencaoPeriodos() {
        if (listaConvencaoPeriodos.isEmpty()) {
            ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
            setListaConvencaoPeriodos((List<ConvencaoPeriodo>) db.listaConvencaoPeriodo());
        }
        return listaConvencaoPeriodos;
    }

    public void setListaConvencaoPeriodos(List<ConvencaoPeriodo> listaConvencaoPeriodos) {
        this.listaConvencaoPeriodos = listaConvencaoPeriodos;
    }

    public ConvencaoPeriodo getConvencaoPeriodo() {
        return convencaoPeriodo;
    }

    public void setConvencaoPeriodo(ConvencaoPeriodo convencaoPeriodo) {
        this.convencaoPeriodo = convencaoPeriodo;
    }
}
