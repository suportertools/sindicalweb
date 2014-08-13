package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean
public class SociosCardBean implements Serializable {

    private Socios socios = new Socios();

    public void setIdPessoa(int idPessoa) {
        if (socios.getId() == -1) {
            if (idPessoa == -1) {
                return;
            }
            SociosDB sociosDB = new SociosDBToplink();
            socios = sociosDB.pesquisaSocioPorPessoa(idPessoa);
        }
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

}
