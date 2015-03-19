package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Biometria;
import br.com.rtools.pessoa.BiometriaCaptura;
import br.com.rtools.pessoa.BiometriaServidor;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.dao.BiometriaDao;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BiometriaBean {

    public boolean isBiometria() {
        if (GenericaSessao.exists("acessoFilial")) {
            Registro registro = (Registro) new Dao().find(new Registro(), 1);
            return registro.isBiometria();
        }
        return false;
    }

    public boolean isStatus() {
        if (GenericaSessao.exists("acessoFilial")) {
            BiometriaDao biometriaDao = new BiometriaDao();
            List list = biometriaDao.pesquisaStatusPorComputador(MacFilial.getAcessoFilial().getId());
            if (!list.isEmpty()) {
                BiometriaServidor biometriaServidor = (BiometriaServidor) new Dao().rebind((BiometriaServidor) list.get(0));
                return biometriaServidor.isAtivo();
            }
        }
        return false;
    }

    public boolean complete(Pessoa p) {
        if (GenericaSessao.exists("acessoFilial")) {
            BiometriaDao biometriaDao = new BiometriaDao();
            Biometria biometria = biometriaDao.pesquisaBiometriaPorPessoa(p.getId());
            if (biometria != null) {
                return true;
            }
        }
        return false;
    }

    public void gravar(Pessoa p) {
        BiometriaDao biometriaDao = new BiometriaDao();
        Dao dao = new Dao();
        if (GenericaSessao.exists("acessoFilial")) {
            List list = biometriaDao.pesquisaBiometriaCapturaPorPessoa(p.getId());
            if (!list.isEmpty()) {
                for (Object list1 : list) {
                    dao.delete(list1, true);
                }
            }
            BiometriaCaptura biometriaCaptura = new BiometriaCaptura();
            biometriaCaptura.setMacFilial((MacFilial) GenericaSessao.getObject("acessoFilial"));
            biometriaCaptura.setPessoa(p);
            dao.save(biometriaCaptura, true);
        }
    }

}
