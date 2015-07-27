package br.com.rtools.seguranca.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.dao.RotinaDao;
import br.com.rtools.utilitarios.GenericaSessao;
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
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        if (rotina.getId() == -1) {
            if (rotina.getRotina().equals("")) {
                message = "Digite uma Rotina!";
            } else {
                RotinaDao rotinaDao = new RotinaDao();
                if (!rotinaDao.existeRotina(rotina)) {
                    dao.openTransaction();
                    if (dao.save(rotina)) {
                        dao.commit();
                        novoLog.save("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                        message = "Registro salvo com sucesso";
                        listRotina.clear();
                        descricaoPesquisa = "";
                    } else {
                        dao.rollback();
                        message = "Erro ao inserir registro!";
                    }
                } else {
                    message = "Esta Rotina já existe no Sistema.";
                }
            }
        } else {
            Rotina r = (Rotina) dao.find(rotina);
            String beforeUpdate = "ID: " + r.getId() + " - Rotina: " + r.getRotina() + " - Página: " + r.getRotina() + " - Ativa: " + r.isAtivo();
            dao.openTransaction();
            if (dao.update(rotina)) {
                novoLog.update(beforeUpdate, "ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                dao.commit();
                listRotina.clear();
                descricaoPesquisa = "";
                message = "Registro atualizado com sucesso";
            } else {
                dao.rollback();
                message = "Erro ao atualizar registro!";
            }
        }
    }

    public void clear() {
        GenericaSessao.remove("rotinaBean");
    }

    public void delete() {
        Dao dao = new Dao();
        NovoLog novoLog = new NovoLog();
        if (rotina.getId() != -1) {
            dao.openTransaction();
            if (dao.delete(rotina)) {
                novoLog.delete("ID: " + rotina.getId() + " - Rotina: " + rotina.getRotina() + " - Página: " + rotina.getRotina() + " - Ativa: " + rotina.isAtivo());
                dao.commit();
                descricaoPesquisa = "";
                listRotina.clear();
                message = "Registro excluido com sucesso";
            } else {
                dao.rollback();
                message = "Esta registro não pode ser excluido!";
            }
        }
        rotina = new Rotina();
    }

    public String edit(Rotina r) {
        Dao dao = new Dao();
        rotina = new Rotina();
        rotina = (Rotina) dao.rebind(r);
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
            if (descricaoPesquisa.equals("")) {
                listRotina = new Dao().list(new Rotina(), true);
            } else {
                listRotina = new RotinaDao().pesquisaRotinaPorDescricao(descricaoPesquisa);
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
