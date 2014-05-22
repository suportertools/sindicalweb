package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Cnae;
import br.com.rtools.pessoa.db.CnaeDB;
import br.com.rtools.pessoa.db.CnaeDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class CnaeBean implements Serializable {

    private Cnae cnae;
    private List<Cnae> listCnae;

    @PostConstruct
    public void init() {
        cnae = new Cnae();
        listCnae = new ArrayList<Cnae>();

    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cnaeBean");
        GenericaSessao.remove("cnaePesquisado");
    }

    public void clear() {
        GenericaSessao.remove("cnaeBean");
    }

    public Cnae getCnae() {
        if (GenericaSessao.exists("cnaePesquisado")) {
            cnae = (Cnae) GenericaSessao.getObject("cnaePesquisado", true);
        }
        return cnae;
    }

    public void setCnae(Cnae cnae) {
        this.cnae = cnae;
    }

    public void save() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        CnaeDB db = new CnaeDBToplink();
        if (getCnae().getCnae().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite um Cnae!");
            return;
        }

        if (getCnae().getNumero().isEmpty()) {
            GenericaMensagem.warn("Validação", "Digite o Número do Cnae!");
            return;
        }

        if (db.idCnae(getCnae()) != null) {
            GenericaMensagem.warn("Validação", "Este Cnae já existe no Sistema!");
            return;
        }

        di.openTransaction();
        if (getCnae().getId() == -1) {
            if (di.save(cnae)) {
                novoLog.save(
                        "ID: " + cnae.getId()
                        + " - Cnae: " + cnae.getCnae()
                        + " - Número: " + cnae.getNumero()
                );
                GenericaMensagem.info("Sucesso", "Cnae salvo com sucesso");
                listCnae.clear();
                di.commit();
            } else {
                GenericaMensagem.warn("Erro", "Erro ao salvar Cnae!");
                di.rollback();
            }
        } else {
            Cnae c = (Cnae) di.find(cnae);
            String beforeUpdate
                    = "ID: " + c.getId()
                    + " - Cnae: " + c.getCnae()
                    + " - Número: " + c.getNumero();
            if (di.update(cnae)) {
                novoLog.update(beforeUpdate,
                        "ID: " + cnae.getId()
                        + " - Cnae: " + cnae.getCnae()
                        + " - Número: " + cnae.getNumero()
                );
                GenericaMensagem.info("Sucesso", "Cnae atualizada com sucesso.");
                listCnae.clear();
                di.commit();
            } else {
                GenericaMensagem.warn("Erro", "Erro ao atualizar Cnae!");
                di.rollback();
            }
        }
    }

    public void delete() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (cnae.getId() != -1) {
            di.openTransaction();
            if (di.delete(cnae)) {
                novoLog.delete(
                        "ID: " + cnae.getId()
                        + " - Cnae: " + cnae.getCnae()
                        + " - Número: " + cnae.getNumero()
                );
                GenericaMensagem.info("Sucesso", "Cadastro excluído com sucesso!");
                cnae = new Cnae();
                listCnae.clear();
                di.commit();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Erro! Cadastro não foi excluído.");
            }
        } else {
            GenericaMensagem.warn("Erro", "Não há registro para excluir.");
        }
    }

    public String edit(Cnae c) {
        cnae = c;
        GenericaSessao.put("cnaePesquisado", getCnae());
        GenericaSessao.put("linkClicado", true);
        setCnae(new Cnae());
        if (GenericaSessao.exists("urlRetorno")) {
            return null;
        } else {
            return GenericaSessao.getString("urlRetorno");
        }
    }

    public List<Cnae> getListCnae() {
        DaoInterface di = new Dao();
        listCnae = di.list(new Cnae());
        return listCnae;
    }

    public void setListCnae(List<Cnae> listCnae) {
        this.listCnae = listCnae;
    }
}
