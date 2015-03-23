package br.com.rtools.utilitarios;

import com.sun.faces.component.visit.FullVisitContext;
import java.util.Collection;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
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

    public static UIComponent findComponent(final String id) {

        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        final UIComponent[] found = new UIComponent[1];

        root.visitTree(new FullVisitContext(context), new VisitCallback() {
            @Override
            public VisitResult visit(VisitContext context, UIComponent component) {
                if (component.getId().equals(id)) {
                    found[0] = component;
                    return VisitResult.COMPLETE;
                }
                return VisitResult.ACCEPT;
            }
        });

        return found[0];

    }
}
