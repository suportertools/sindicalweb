package br.com.rtools.filters;

import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

public class AuthorizationListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        FacesContext facesContext = event.getFacesContext();
        String currentPage = facesContext.getViewRoot().getViewId();

        boolean isLoginPage = 
                (
                currentPage.lastIndexOf("index.xhtml") > -1 || 
                currentPage.lastIndexOf("indexLogin.xhtml") > -1 || 
                currentPage.lastIndexOf("indexLoginExpirou.xhtml") > -1 ||
                currentPage.lastIndexOf("indexAcessoWeb.xhtml") > -1
                );
        HttpSession session = (HttpSession) facesContext.getExternalContext().getSession(true);
        Object currentUser = session.getAttribute("userName");
        if (!isLoginPage && currentUser == null) {
            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
            nh.handleNavigation(facesContext, null, "indexLoginExpirou");
        }
    }

    @Override
    public void beforePhase(PhaseEvent event) {
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

}
