package br.com.rtools.arrecadacao.beans;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDB;
import br.com.rtools.arrecadacao.db.ConvencaoPeriodoDBTopLink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ConvencaoPeriodoBean {

    private ConvencaoPeriodo convencaoPeriodo;
    private String message;
    private int idConvencao;
    private int idGrupoCidade;
    private List<ConvencaoPeriodo> listConvencaoPeriodos;
    private List<SelectItem> listConvencao;
    private List<SelectItem> listGrupoCidade;

    @PostConstruct
    public void init() {
        convencaoPeriodo = new ConvencaoPeriodo();
        message = "";
        idConvencao = 0;
        idGrupoCidade = 0;
        listConvencaoPeriodos = new ArrayList<ConvencaoPeriodo>();
        listConvencao = new ArrayList();
        listGrupoCidade = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("convencaoPeriodoBean");
    }

    public void clear() {
        GenericaSessao.remove("convencaoPeriodoBean");
    }

    public void edit(ConvencaoPeriodo cp) {
        convencaoPeriodo = cp;
        for (int i = 0; i < listConvencao.size(); i++) {
            if (Integer.parseInt(listConvencao.get(i).getDescription()) == convencaoPeriodo.getConvencao().getId()) {
                idConvencao = i;
                break;
            }
        }
        for (int i = 0; i < getListGrupoCidade().size(); i++) {
            if (Integer.parseInt(getListGrupoCidade().get(i).getDescription()) == convencaoPeriodo.getGrupoCidade().getId()) {
                idGrupoCidade = i;
                break;
            }
        }
    }

    public void save() {
        ConvencaoPeriodoDB convencaoPeriodoDB = new ConvencaoPeriodoDBTopLink();
        if (getConvencaoPeriodo().getReferenciaInicial().equals("__/____") || getConvencaoPeriodo().getReferenciaInicial().equals("")) {
            setMessage("Informar a referência inicial!");
            GenericaMensagem.warn("Sistema", "Informar a referência inicial!");
            return;
        }
        if (getConvencaoPeriodo().getReferenciaFinal().equals("__/____") || getConvencaoPeriodo().getReferenciaFinal().equals("")) {
            setMessage("Informar a referência final!");
            GenericaMensagem.warn("Sistema", "Informar a referência final!");
            return;
        }
        getConvencaoPeriodo().getGrupoCidade().setId(Integer.parseInt(listGrupoCidade.get(idGrupoCidade).getDescription()));
        getConvencaoPeriodo().getConvencao().setId(Integer.parseInt(listConvencao.get(idConvencao).getDescription()));
        if (convencaoPeriodoDB.convencaoPeriodoExiste(getConvencaoPeriodo())) {
            setMessage("Convenção período já existe!");
            GenericaMensagem.warn("Sistema", "Convenção período já existe!");
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (getConvencaoPeriodo().getId() == -1) {
            di.openTransaction();
            if (di.save(getConvencaoPeriodo())) {
                di.commit();
                novoLog.save(
                        "ID: " + getConvencaoPeriodo().getId()
                        + " - Convencao: (" + getConvencaoPeriodo().getConvencao().getId() + ") " + getConvencaoPeriodo().getConvencao().getDescricao()
                        + " - Grupo Cidade: (" + getConvencaoPeriodo().getGrupoCidade().getId() + ") " + getConvencaoPeriodo().getGrupoCidade().getDescricao()
                        + " - Ref: " + getConvencaoPeriodo().getReferenciaInicial() + " - " + getConvencaoPeriodo().getReferenciaFinal()
                );
                convencaoPeriodo = new ConvencaoPeriodo();
                listConvencaoPeriodos.clear();
                GenericaMensagem.info("Sucesso", "Registro inserido");
                setMessage("Registro inserido com sucesso");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao inserir esse registro!");
                setMessage("Erro ao inserir esse registro!");
            }
        } else {
            ConvencaoPeriodo cp = (ConvencaoPeriodo) di.find(convencaoPeriodo);
            String beforeUpdate
                    = "ID: " + cp.getId()
                    + " - Convencao: (" + cp.getConvencao().getId() + ") " + cp.getConvencao().getDescricao()
                    + " - Grupo Cidade: (" + cp.getGrupoCidade().getId() + ") " + cp.getGrupoCidade().getDescricao()
                    + " - Ref: " + cp.getReferenciaInicial() + " - " + cp.getReferenciaFinal();
            di.openTransaction();
            if (di.update(getConvencaoPeriodo())) {
                di.commit();
                novoLog.update(beforeUpdate,
                        "ID: " + convencaoPeriodo.getId()
                        + " - Convencao: (" + convencaoPeriodo.getConvencao().getId() + ") " + convencaoPeriodo.getConvencao().getDescricao()
                        + " - Grupo Cidade: (" + convencaoPeriodo.getGrupoCidade().getId() + ") " + convencaoPeriodo.getGrupoCidade().getDescricao()
                        + " - Ref: " + convencaoPeriodo.getReferenciaInicial() + " - " + convencaoPeriodo.getReferenciaFinal()
                );                
                GenericaMensagem.info("Sucesso", "Registro atualizado");
                setMessage("Sucesso registro atualizado");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao atualizar esse registro!");
                setMessage("Erro ao atualizar este registro!");
            }
        }
    }

    public void delete() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (getConvencaoPeriodo().getId() != -1) {
            di.openTransaction();
            if (di.delete(convencaoPeriodo)) {
                novoLog.delete(
                        "ID: " + convencaoPeriodo.getId()
                        + " - Convencao: (" + convencaoPeriodo.getConvencao().getId() + ") " + convencaoPeriodo.getConvencao().getDescricao()
                        + " - Grupo Cidade: (" + convencaoPeriodo.getGrupoCidade().getId() + ") " + convencaoPeriodo.getGrupoCidade().getDescricao()
                        + " - Ref: " + convencaoPeriodo.getReferenciaInicial() + " - " + convencaoPeriodo.getReferenciaFinal()
                );                
                di.commit();
                convencaoPeriodo = new ConvencaoPeriodo();
                listConvencaoPeriodos.clear();
                GenericaMensagem.info("Sucesso", "Registro excluído");
                setMessage("Registro excluído com sucesso");
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro ao excluir esse registro!");
                setMessage("Erro ao excluir esse registro!");
            }
        }
    }

    public List<SelectItem> getListConvencao() {
        if (listConvencao.isEmpty()) {
            DaoInterface di = new Dao();
            List<Convencao> list = di.list("Convencao", true);
            for (int i = 0; i < list.size(); i++) {
                listConvencao.add(new SelectItem(i,
                        list.get(i).getDescricao().toUpperCase(),
                        Integer.toString(list.get(i).getId())));
            }

        }
        return listConvencao;
    }

    public void setListConvencao(List<SelectItem> listConvencao) {
        this.listConvencao = listConvencao;
    }

    public List<SelectItem> getListGrupoCidade() {
        listGrupoCidade.clear();
        ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
        List<ConvencaoCidade> list = db.listaGrupoCidadePorConvencao(Integer.parseInt(listConvencao.get(idConvencao).getDescription()));
        for (int i = 0; i < list.size(); i++) {
            listGrupoCidade.add(new SelectItem(i,
                    list.get(i).getGrupoCidade().getDescricao(),
                    Integer.toString(list.get(i).getGrupoCidade().getId())));
        }
        return listGrupoCidade;
    }

    public void setListGrupoCidade(List<SelectItem> listGrupoCidade) {
        this.listGrupoCidade = listGrupoCidade;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
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

    public List<ConvencaoPeriodo> getListConvencaoPeriodos() {
        if (listConvencaoPeriodos.isEmpty()) {
            ConvencaoPeriodoDB db = new ConvencaoPeriodoDBTopLink();
            setListConvencaoPeriodos((List<ConvencaoPeriodo>) db.listaConvencaoPeriodo());
        }
        return listConvencaoPeriodos;
    }

    public void setListConvencaoPeriodos(List<ConvencaoPeriodo> listConvencaoPeriodos) {
        this.listConvencaoPeriodos = listConvencaoPeriodos;
    }

    public ConvencaoPeriodo getConvencaoPeriodo() {
        return convencaoPeriodo;
    }

    public void setConvencaoPeriodo(ConvencaoPeriodo convencaoPeriodo) {
        this.convencaoPeriodo = convencaoPeriodo;
    }
}
