package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteAutorizaCortesia;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
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
public class ConviteAutorizaCortesiaBean implements Serializable {

    private ConviteAutorizaCortesia conviteAutorizaCortesia;
    private List<ConviteAutorizaCortesia> listPessoasAutorizadas;
    private String message;

    @PostConstruct
    public void init() {
        conviteAutorizaCortesia = new ConviteAutorizaCortesia();
        listPessoasAutorizadas = new ArrayList();
        message = "";
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("conviteAutorizaCortesiaBean");
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void updateStatus(ConviteAutorizaCortesia cac) {
        Dao dao = new Dao();

        dao.openTransaction();
        if (cac.isAtivo()) {
            cac.setAtivo(false);
        } else {
            cac.setAtivo(true);
        }
        dao.commit();

        message = "Autorização atualizada com Sucesso!";
    }

    public void save() {
        if (conviteAutorizaCortesia.getPessoa().getId() == -1) {
            message = "Pesquisar pessoa!";
        }
        for (int i = 0; i < listPessoasAutorizadas.size(); i++) {
            if (listPessoasAutorizadas.get(i).getPessoa().getId() == conviteAutorizaCortesia.getPessoa().getId()) {
                message = "Pessoa já cadastrada!";
                return;
            }
        }
        DaoInterface di = new Dao();
        if (conviteAutorizaCortesia.getId() == -1) {
            di.openTransaction();
            if (di.save(conviteAutorizaCortesia)) {
                di.commit();
                NovoLog novoLog = new NovoLog();
                novoLog.save("ID: " + conviteAutorizaCortesia.getId() + " - Pessoa: (" + conviteAutorizaCortesia.getPessoa().getId() + ") " + conviteAutorizaCortesia.getPessoa().getNome());
                message = "Registro inserido com sucesso";
                listPessoasAutorizadas.clear();
            } else {
                di.rollback();
                message = "Erro ao adicionar registro!";
            }
        }
        conviteAutorizaCortesia = new ConviteAutorizaCortesia();
    }

    public void clear() {
        conviteAutorizaCortesia = new ConviteAutorizaCortesia();
        listPessoasAutorizadas.clear();
    }

    public void delete(ConviteAutorizaCortesia cac) {
        if (cac.getId() != -1) {
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(cac)) {
                di.commit();
                message = "Registro excluído com sucesso.";
                NovoLog novoLog = new NovoLog();
                novoLog.delete("ID: " + cac.getId() + " - Pessoa: (" + cac.getPessoa().getId() + ") " + cac.getPessoa().getNome());
                listPessoasAutorizadas.clear();
            } else {
                di.rollback();
                di.openTransaction();
                cac.setAtivo(false);
                if (!di.update(cac)) {
                    di.rollback();
                    message = "Erro ao excluir registro!";
                    return;
                }
                message = "Registro Inativado!";
                di.commit();
            }
        }
    }

    public ConviteAutorizaCortesia getConviteAutorizaCortesia() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            conviteAutorizaCortesia.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return conviteAutorizaCortesia;
    }

    public void setConviteAutorizaCortesia(ConviteAutorizaCortesia conviteAutorizaCortesia) {
        this.conviteAutorizaCortesia = conviteAutorizaCortesia;
    }

    public List<ConviteAutorizaCortesia> getListPessoasAutorizadas() {
        if (listPessoasAutorizadas.isEmpty()) {
            DaoInterface di = new Dao();
            listPessoasAutorizadas = (List<ConviteAutorizaCortesia>) di.list(new ConviteAutorizaCortesia(), true);
        }
        return listPessoasAutorizadas;
    }

    public void setListPessoasAutorizadas(List<ConviteAutorizaCortesia> listPessoasAutorizadas) {
        this.listPessoasAutorizadas = listPessoasAutorizadas;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
