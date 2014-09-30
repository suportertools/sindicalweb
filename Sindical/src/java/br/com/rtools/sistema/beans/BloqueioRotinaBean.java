package br.com.rtools.sistema.beans;

import br.com.rtools.sistema.dao.BloqueioRotinaDao;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;

@ManagedBean
public class BloqueioRotinaBean implements Serializable {

    public void liberaRotinasBloqueadas() {
        BloqueioRotinaDao bloqueioRotinaDao = new BloqueioRotinaDao();
        bloqueioRotinaDao.liberaRotinasBloqueadas();
    }
}
