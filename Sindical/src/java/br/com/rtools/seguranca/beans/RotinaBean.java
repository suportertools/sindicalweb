package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Dao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class RotinaBean implements Serializable {

    private Rotina rotina;
    private String message;
    private String descricaoPesquisa;
    private List<Rotina> listRotina;

    @PostConstruct
    public void init() {
        rotina = new Rotina();
        message = "";
        descricaoPesquisa = "";
        listRotina = new ArrayList<Rotina>();
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void save() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (rotina.getId() == -1) {
            if (rotina.getRotina().equals("")) {
                message = "Digite uma Rotina!";
            } else {
                RotinaDB rotinaDB = new RotinaDBToplink();
                if (!rotinaDB.existeRotina(rotina)) {
                    di.openTransaction();
                    if (di.save(rotina)) {
                        di.commit();
                        novoLog.save("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                        message = "Registro salvo com sucesso";
                        listRotina.clear();
                        descricaoPesquisa = "";
                    } else {
                        di.rollback();
                        message = "Erro ao inserir registro!";
                    }
                } else {
                    message = "Esta Rotina já existe no Sistema.";
                }
            }
        } else {
            Rotina r = (Rotina) di.find(rotina);
            String beforeUpdate = "ID: " + r.getId() + " - Rotina: " + r.getRotina() + " - Página: " + r.getRotina() + " - Ativa: " + r.isAtivo();
            di.openTransaction();
            if (di.update(rotina)) {
                novoLog.update(beforeUpdate, "ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                di.commit();
                listRotina.clear();
                descricaoPesquisa = "";
                message = "Registro atualizado com sucesso";
            } else {
                di.rollback();
                message = "Erro ao atualizar registro!";
            }
        }
    }

    public void clear() {
        GenericaSessao.remove("rotinaBean");
    }

    public void delete() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (rotina.getId() != -1) {
            di.openTransaction();
            if (di.delete(rotina)) {
                novoLog.delete("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                di.commit();
                descricaoPesquisa = "";
                listRotina.clear();
                message = "Registro excluido com sucesso";
            } else {
                di.rollback();
                message = "Esta registro não pode ser excluido!";
            }
        }
        rotina = new Rotina();
    }

    public String edit(Rotina r) {
        DaoInterface di = new Dao();
        rotina = new Rotina();
        rotina = (Rotina) di.rebind(r);
        GenericaSessao.put("rotinaPesquisa", rotina);
        GenericaSessao.put("linkClicado", true);
        if (GenericaSessao.exists("urlRetorno")) {
            if (!GenericaSessao.getString("urlRetorno").equals("menuPrincipal")) {
                return (String) GenericaSessao.getString("urlRetorno");
            }
        } else {
            return "rotina";
        }
        return null;
    }

    public List<Rotina> getListRotina() {
        if (listRotina.isEmpty()) {
            RotinaDB db = new RotinaDBToplink();
            if (descricaoPesquisa.equals("")) {
                listRotina = db.pesquisaTodosOrdenado();
            } else {
                listRotina = db.pesquisaRotinaPorDescricao(descricaoPesquisa);
            }
        }
        return listRotina;
    }

    public void setListRotina(List<Rotina> listRotina) {
        this.listRotina = listRotina;
    }

    public String rotinaAtiva(boolean ativo) {
        if (ativo) {
            return "Ativo";
        }
        return "Inativo";
    }

    public Rotina getRotina() {
        return rotina;
    }

    public void setRotina(Rotina rotina) {
        this.rotina = rotina;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }
}
