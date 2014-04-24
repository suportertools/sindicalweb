package br.com.rtools.utilitarios;

import java.util.Collection;
import java.util.logging.Logger;
import org.primefaces.context.RequestContext;

public class PF {
    private static final Logger LOG = Logger.getLogger(PF.class.getName());

    /**
     * UPDATE Atualiza um único componente
     *
     * @param string
     */
    public static void update(String string) {
        RequestContext.getCurrentInstance().update(string);
    }

    /**
     * UPDATE Atualiza uma coleção de componentes
     *
     * @param c
     */
    public static void update(Collection c) {
        RequestContext.getCurrentInstance().update(c);
    }

    /**
     * EXECUTE Executa uma ação no componente referenciado
     *
     * @param string
     */
    public static void execute(String string) {
        try {
            RequestContext.getCurrentInstance().execute(string);            
        } catch (Exception e) {
        }
    }

    /**
     * Open DIALOG Abre o dialog
     *
     * @param string
     */
    public static void openDialog(String string) {
        RequestContext.getCurrentInstance().execute("PF('" + string + "').show()");
    }

    /**
     * Close DIALOG Fecha o dialog
     *
     * @param string
     */
    public static void closeDialog(String string) {
        RequestContext.getCurrentInstance().execute("PF('" + string + "').hide()");
    }

    public static void scrollTo(String string) {
        RequestContext.getCurrentInstance().scrollTo(string);
    }

    public static void scrollTo(String string, Object o) {
        RequestContext.getCurrentInstance().addCallbackParam(string, o);
    }

    /**
     * RESET Limpa um único componente
     *
     * @param string
     */
    public static void reset(String string) {
        RequestContext.getCurrentInstance().reset(string);
    }

    /**
     * RESET Limpa um ou mais componentes
     *
     * @param c
     */
    public static void reset(Collection c) {
        RequestContext.getCurrentInstance().reset(c);
    }
}
