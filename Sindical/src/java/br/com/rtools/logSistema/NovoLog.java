package br.com.rtools.logSistema;

import br.com.rtools.seguranca.Evento;
import br.com.rtools.seguranca.Log;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.beans.SimplesBean;
import br.com.rtools.seguranca.db.RotinaDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class NovoLog extends salvaLogs {

    private boolean transaction;
    private boolean cadastroSimples;
    private final List<Log> listLogs;
    private String tabela;
    private Integer codigo;

    public NovoLog() {
        transaction = false;
        cadastroSimples = false;
        listLogs = new ArrayList<>();
        tabela = null;
        codigo = null;
    }

// LOG ARQUIVOS    
    public String novo(String acao, String obs) {
        Usuario user = (Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario");
        String iusuario = ": ";
        if (user != null) {
            iusuario = iusuario + user.getPessoa().getId() + " " + user.getPessoa().getNome();
        }
        String ihorario = "[horario " + DataHoje.horaMinuto() + "]";
        String iacao = " >> " + acao;
        String iobs = "# " + obs;
        salvarLinha(ihorario + " " + iusuario + " " + iacao + " " + iobs);
        return null;
    }

    // LOG NO BANCO DE DADOS
    /**
     * <p>
     * <strong>startList</strong></p>
     * <p>
     * <strong>Example:</strong>startList();</p>
     * <p>
     * Abre uma transação e cria uma lista do processo atual para geração de
     * log, essa semana só será finalizada se houver o commit do processo.
     * Evitando assim gerar logs sem necessidade. Finish: commit() or
     * rollback()</p>
     *
     * @author Bruno
     *
     */
    public void startList() {
        transaction = true;
    }

    /**
     * <p>
     * <strong>saveList</strong></p>
     * <p>
     * Grava todos os logs da transação atual. Finish: saveList()</p>
     *
     * @author Bruno
     *
     */
    public void saveList() {
        for (Log l : listLogs) {
            execute(l);
        }
        listLogs.clear();
        transaction = false;
    }

    /**
     * <p>
     * <strong>cancelList</strong></p>
     * <p>
     * Apaga todos os logs da transação atual. Finish: cancelList()</p>
     *
     * @author Bruno
     *
     */
    public void cancelList() {
        listLogs.clear();
        transaction = false;
    }

    /**
     * <p>
     * <strong>Live</strong></p>
     * <p>
     * <strong>Example:</strong>live("User" + user.getLogin());</p>
     *
     * @author Bruno
     * @param infoLive Texto de informações livres para o log, não é gerado
     * nenhum Evento (seg_evento -> default null) para esta execução.
     *
     */
    public void live(String infoLive) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", null, null, null));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", null, null, null);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Save - String</strong></p>
     * <strong>Example:</strong><ul><li>save("User" + user.getLogin());</li><li>
     * Utilizar se for um novo registro. </li></ul>
     *
     * @author Bruno
     * @param infoLive Texto de informações livres para o log.
     *
     */
    public void save(String infoLive) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", getEvento(1), tabela, codigo));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", getEvento(1), tabela, codigo);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Save - Object</strong></p>
     * <p>
     * <strong>Example:</strong>save((User) user, true); Utilizar se for um novo
     * registro. </p>
     *
     * @author Bruno
     * @param object - Texto de informações livres para o log.
     * @param isObject - default = true
     *
     */
    public void save(Object object, boolean isObject) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), "", getEvento(1), tabela, codigo));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), "", getEvento(1), tabela, codigo);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Update - String</strong></p>
     * <p>
     * <strong>Example:</strong>update("Joao", "João"); Utilizar se for alterar
     * um registro existente. </p>
     *
     * @author Bruno
     * @param beforeUpdate - Informações antes da modificação.
     * @param afterUpdate - Informações depois da modificação.
     *
     */
    public void update(String beforeUpdate, String afterUpdate) {
        validaTabela();
        if (!beforeUpdate.equals(afterUpdate)) {
            if (transaction) {
                listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate, afterUpdate, getEvento(3), tabela, codigo));
            } else {
                Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate, afterUpdate, getEvento(3), tabela, codigo);
                execute(log);
            }
        }
    }

    /**
     * <p>
     * <strong>Update - Object</strong></p>
     * <p>
     * <strong>Example:</strong>update((User) userBefore, (User) userAfter,
     * true); Utilizar se for alterar um registro existente. </p>
     *
     * @author Bruno
     * @param beforeUpdate - Informações antes da modificação.
     * @param afterUpdate - Informações depois da modificação.
     * @param isObject - default = true
     *
     */
    public void update(Object beforeUpdate, Object afterUpdate, boolean isObject) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate.toString(), afterUpdate.toString(), getEvento(3), tabela, codigo));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate.toString(), afterUpdate.toString(), getEvento(3), tabela, codigo);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Delete - String</strong></p>
     * <p>
     * <strong>Example:</strong>save("User" + user.getLogin()); Utilizar se for
     * remover um registro. </p>
     *
     * @author Bruno
     * @param infoLive - Texto de informações livres para o log.
     *
     */
    public void delete(String infoLive) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, null, getEvento(2), tabela, codigo));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, null, getEvento(2), tabela, codigo);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Delete - Object</strong></p>
     * <p>
     * <strong>Example:</strong>delete((User) user, true); Utilizar se for
     * remover um registro. </p>
     *
     * @author Bruno
     * @param object - Texto de informações livres para o log.
     * @param isObject - default = true
     *
     */
    public void delete(Object object, boolean isObject) {
        validaTabela();
        if (transaction) {
            listLogs.add(new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), null, getEvento(2), tabela, codigo));
        } else {
            Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), null, getEvento(2), tabela, codigo);
            execute(log);
        }
    }

    /**
     * <p>
     * <strong>Execute</strong></p>
     * <p>
     * <strong>Example:</strong>Executa o método.</p>
     *
     * @param log
     *
     */
    public void execute(Log log) {
        tabela = null;
        codigo = null;
        Dao dao = new Dao();
        dao.save(log, true);
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            return (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return null;
    }

    public Rotina getRotina() {
        if (cadastroSimples) {
            String classe = ((SimplesBean) GenericaSessao.getObject("simplesBean")).getSessoes()[0];
            RotinaDao rotinaDao = new RotinaDao();
            Rotina rotina = rotinaDao.pesquisaRotinaPorClasse(classe);
            if (rotina == null) {
                return null;
            }
            if (rotina.getId() != -1) {
                return rotina;
            }
        } else {
            HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
            String nomePagina = this.converteURL(paginaRequerida.getRequestURI());
            RotinaDao rotinaDao = new RotinaDao();
            Rotina rotina = rotinaDao.pesquisaRotinaPorPagina(nomePagina);
            try {
                if (rotina != null) {
                    return rotina;
                }
            } catch (Exception e) {

            }
        }
        return null;
    }

    /**
     * <p>
     * <strong>Evento</strong></p>
     * <p>
     * <strong>Lista de eventos:</strong>
     * <ul>
     * <li>(1) Inclusão;</li>
     * <li>(2) Exclusão;</li>
     * <li>(3) Alteração;</li>
     * <li>(4) Consulta;</li>
     * </ul>
     *
     * @author Bruno
     * @param idEvento
     *
     * @return Evento
     */
    public Evento getEvento(Integer idEvento) {
        try {
            Dao dao = new Dao();
            return (Evento) dao.find(new Evento(), idEvento);
        } catch (Exception e) {
        }
        return null;
    }

    public boolean isCadastroSimples() {
        return cadastroSimples;
    }

    public void setCadastroSimples(boolean cadastroSimples) {
        this.cadastroSimples = cadastroSimples;
    }

    /**
     * Tabela principal do log gerado
     *
     * @return
     */
    public String getTabela() {
        return tabela;
    }

    /**
     *
     * @param tabela (Tabela principal do log gerado)
     */
    public void setTabela(String tabela) {
        this.tabela = tabela;
    }

    /**
     * Código da tabela principal do log gerado
     *
     * @return
     */
    public Integer getCodigo() {
        return codigo;
    }

    /**
     *
     * @param codigo Código da tabela principal do log gerado
     */
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public void validaTabela() {
        if (tabela != null && !tabela.isEmpty()) {
            if (codigo == null) {
                tabela = null;
            }
        }
    }
}
