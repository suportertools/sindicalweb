package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.Socios;
import br.com.rtools.associativo.dao.SociosDao;
import br.com.rtools.associativo.db.SociosDB;
import br.com.rtools.associativo.db.SociosDBToplink;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ViewScoped
@ManagedBean
public class SociosCardBean implements Serializable {

    private Socios socios = new Socios();
    private Socios sociosTitular = new Socios();

    public void load(int idPessoa) {
        if (idPessoa != -1) {
            SociosDao sociosDao = new SociosDao();
            SociosDB sociosDB = new SociosDBToplink();
            socios = sociosDB.pesquisaSocioPorPessoa(idPessoa);
            if (socios == null) {
                socios = new Socios();
            } else {
                if (socios.getParentesco().getId() != 1) {
                    sociosTitular = sociosDao.pesquisaTitularPorDependente(socios.getMatriculaSocios().getTitular().getId());
                    if (sociosTitular == null) {
                        sociosTitular = new Socios();
                    }
                } else {
                    sociosTitular = new Socios();
                }
            }
        }
    }

    public Socios getSocios() {
        return socios;
    }

    public void setSocios(Socios socios) {
        this.socios = socios;
    }

    public Socios getSociosTitular() {
        return sociosTitular;
    }

    public void setSociosTitular(Socios sociosTitular) {
        this.sociosTitular = sociosTitular;
    }

}
