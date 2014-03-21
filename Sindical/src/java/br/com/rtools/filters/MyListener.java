package br.com.rtools.filters;

import javax.faces.event.*;

public class MyListener implements PhaseListener {

    @Override
    public void afterPhase(PhaseEvent event) {
        String viewId = event.getFacesContext().getViewRoot().getViewId();
        // do thing
    }

    @Override
    public void beforePhase(PhaseEvent event) {
        // do thing
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }
}
