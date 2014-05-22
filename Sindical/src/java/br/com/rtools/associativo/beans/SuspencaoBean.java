package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Suspencao;
import br.com.rtools.associativo.db.SuspencaoDB;
import br.com.rtools.associativo.db.SuspencaoDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean
@SessionScoped
public class SuspencaoBean {

    private Suspencao suspencao;
    private String message;
    private List<Suspencao> listSuspencao;

    @PostConstruct
    public void init() {
        suspencao = new Suspencao();
        message = "";
        listSuspencao = new ArrayList<Suspencao>();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("suspencaoBean");
        GenericaSessao.remove("pessoaPesquisa");
    }

    public void clear() {
        GenericaSessao.remove("suspencaoBean");
    }

    public void save() {
        if (suspencao.getPessoa().getId() == -1) {
            message = "Pesquise um sócio para Suspender!";
            return;
        }
        if (suspencao.getDataInicial().length() < 7 || suspencao.getDataFinal().length() < 7) {
            message = "Data inválida!";
            return;
        }
        if (DataHoje.converteDataParaInteger(suspencao.getDataInicial())
                > DataHoje.converteDataParaInteger(suspencao.getDataFinal())) {
            message = "Data inicial não pode ser maior que data final!";
            return;
        }
        if (suspencao.getMotivo().equals("") || suspencao.getMotivo() == null) {
            message = "Digite um motivo de Suspensão!";
            return;
        }
        SuspencaoDB suspencaoDB = new SuspencaoDBToplink();
        if (suspencaoDB.existeSuspensaoSocio(suspencao)) {
            message = "Sócio já encontra-se suspenso!";
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (suspencao.getId() == -1) {
            if (di.save(suspencao, true)) {
                novoLog.save(
                        "ID: " + suspencao.getId()
                        + " - Pessoa: (" + suspencao.getPessoa().getId()+ ") " + suspencao.getPessoa().getNome()
                        + " - Período: " + suspencao.getDataInicial() + " até " + suspencao.getDataFinal()
                        + " - Motivo: " + suspencao.getMotivo()
                );
                message = "Suspensão salva com sucesso.";
            } else {
                message = "Erro ao salvar suspensão!";
            }
        } else {
            Suspencao s = (Suspencao) di.find(suspencao);
            String beforeUpdate = 
                    "ID: " + s.getId()
                    + " - Pessoa: (" + s.getPessoa().getId()+ ") " + s.getPessoa().getNome()
                    + " - Período: " + s.getDataInicial() + " até " + s.getDataFinal()
                    + " - Motivo: " + s.getMotivo()
            ;
            if (di.update(suspencao, true)) {
                novoLog.update(beforeUpdate,
                        "ID: " + suspencao.getId()
                        + " - Pessoa: (" + suspencao.getPessoa().getId()+ ") " + suspencao.getPessoa().getNome()
                        + " - Período: " + suspencao.getDataInicial() + " até " + suspencao.getDataFinal()
                        + " - Motivo: " + suspencao.getMotivo()
                );
                message = "Suspensão atualizada com sucesso.";
            } else {
                message = "Erro ao atualizar suspensão!";
            }
        }
    }

    public void delete() {
        if (suspencao.getId() == -1) {
            message = "Selecione uma suspensão para ser excluída!";
            return;
        }
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        if (di.delete(suspencao, true)) {
            novoLog.delete(
                    "ID: " + suspencao.getId()
                    + " - Pessoa: (" + suspencao.getPessoa().getId()+ ") " + suspencao.getPessoa().getNome()
                    + " - Período: " + suspencao.getDataInicial() + " até " + suspencao.getDataFinal()
                    + " - Motivo: " + suspencao.getMotivo()
            );            
            suspencao = new Suspencao();
            message = "Suspensão deletada com sucesso!";
            listSuspencao.clear();
        } else {
            message = "Erro ao deletar suspensão!";
        }
    }

    public void novo() {
        suspencao = new Suspencao();
        message = "";
    }

    public String edit(Suspencao s) {
        DaoInterface di = new Dao();
        suspencao = (Suspencao) di.rebind(s);
        GenericaSessao.put("pessoaPesquisa", suspencao.getPessoa());
        GenericaSessao.put("linkClicado", true);
        return "suspencao";
    }

    public List<Suspencao> getListSuspencao() {
        if (listSuspencao.isEmpty()) {
            DaoInterface di = new Dao();
            listSuspencao = (List<Suspencao>) di.list(new Suspencao(), true);
        }
        return listSuspencao;
    }

    public void setListSuspencao(List<Suspencao> listSuspencao) {
        this.listSuspencao = listSuspencao;
    }

    public Suspencao getSuspencao() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            suspencao.setPessoa((Pessoa) GenericaSessao.getObject("pessoaPesquisa", true));
        }
        return suspencao;
    }

    public void setSuspencao(Suspencao suspencao) {
        this.suspencao = suspencao;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
