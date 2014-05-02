package br.com.rtools.logSistema;

import br.com.rtools.seguranca.Evento;
import br.com.rtools.seguranca.Log;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.db.RotinaDB;
import br.com.rtools.seguranca.db.RotinaDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.Date;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

public class NovoLog extends salvaLogs {

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
    public void live(String infoLive) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", null);
        execute(log);
    }

    public void save(String infoLive) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, "", getEvento(1));
        execute(log);
    }

    public void save(Object object, boolean isObject) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), "", getEvento(1));
        execute(log);
    }

    public void update(String beforeUpdate, String afterUpdate) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate, afterUpdate, getEvento(3));
        execute(log);
    }

    public void update(Object beforeUpdate, Object afterUpdate, boolean isObject) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), beforeUpdate.toString(), afterUpdate.toString(), getEvento(3));
        execute(log);
    }

    public void delete(String infoLive) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), infoLive, null, getEvento(2));
        execute(log);
    }

    public void delete(Object object, boolean isObject) {
        Log log = new Log(-1, new Date(), DataHoje.livre(new Date(), "HH:mm"), getUsuario(), getRotina(), object.toString(), null, getEvento(2));
        execute(log);
    }

    public void execute(Log log) {
        DaoInterface di = new Dao();
        di.save(log, true);
    }

    public Usuario getUsuario() {
        if (GenericaSessao.exists("sessaoUsuario")) {
            return (Usuario) GenericaSessao.getObject("sessaoUsuario");
        }
        return null;
    }

    public Rotina getRotina() {
        HttpServletRequest paginaRequerida = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String nomePagina = this.converteURL(paginaRequerida.getRequestURI());
        RotinaDB rotinaDB = new RotinaDBToplink();
        Rotina rotina = rotinaDB.pesquisaRotinaPorPagina(nomePagina);
        try {
            if (rotina.getId() != -1) {
                return rotina;
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * <p>
     * <strong>Evento</strong></p>
     * <p>
     * <strong>Info:</strong> (1) Inclusão; (2) Exclusão; (3) Alteração; (4)
     * Consulta; Consulta;</p>
     *
     * @author Bruno
     * @param idEvento
     *
     * @return Evento
     */
    public Evento getEvento(Integer idEvento) {
        try {
            DaoInterface di = new Dao();
            return (Evento) di.find(new Evento(), idEvento);
        } catch (Exception e) {
        }
        return null;
    }
}
