package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Administradora;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class AdministradoraBean implements Serializable {

    private Administradora administradora;
    private List<Administradora> listAdministradoras;
    private String message;

    @PostConstruct
    public void init() {
        administradora = new Administradora();
        listAdministradoras = new ArrayList();
        message = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("administradoraBean");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void save() {
        if (administradora.getPessoa().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar pessoa!");
        }
        for (int i = 0; i < listAdministradoras.size(); i++) {
            if (listAdministradoras.get(i).getPessoa().getId() == administradora.getPessoa().getId()) {
                GenericaMensagem.warn("Validação", "Pessoa já cadastrada!");
                return;
            }
        }
        DaoInterface di = new Dao();
        if (administradora.getId() == -1) {
            di.openTransaction();
            if (di.save(administradora)) {
                di.commit();
                NovoLog novoLog = new NovoLog();
                novoLog.save("ID: " + administradora.getId() + " - Pessoa: (" + administradora.getPessoa().getId() + ") " + administradora.getPessoa().getNome());
                GenericaMensagem.info("Sucesso", "Registro inserido");
                listAdministradoras.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao adicionar registro!");
            }
        }
        administradora = new Administradora();
    }

    public void clear() {
        administradora = new Administradora();
        listAdministradoras.clear();
    }

    public void delete(Administradora cac) {
        if (cac.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(cac)) {
                di.commit();
                GenericaMensagem.info("Sucesso", "Registro excluído");
                NovoLog novoLog = new NovoLog();
                novoLog.delete("ID: " + cac.getId() + " - Pessoa: (" + cac.getPessoa().getId() + ") " + cac.getPessoa().getNome());
                listAdministradoras.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao excluir registro!");
            }
        }
    }

    public Administradora getAdministradora() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            administradora.setPessoa(((Juridica) GenericaSessao.getObject("juridicaPesquisa", true)).getPessoa());
        }
        return administradora;
    }

    public void setAdministradora(Administradora administradora) {
        this.administradora = administradora;
    }

    public List<Administradora> getListAdministradoras() {
        if (listAdministradoras.isEmpty()) {
            DaoInterface di = new Dao();
            listAdministradoras = (List<Administradora>) di.list(new Administradora(), true);
        }
        return listAdministradoras;
    }

    public void setListAdministradoras(List<Administradora> listAdministradoras) {
        this.listAdministradoras = listAdministradoras;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
