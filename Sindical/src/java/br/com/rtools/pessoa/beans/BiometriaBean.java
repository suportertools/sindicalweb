package br.com.rtools.pessoa.beans;

import br.com.rtools.pessoa.Biometria;
import br.com.rtools.pessoa.BiometriaAtualizaCatraca;
import br.com.rtools.pessoa.BiometriaCaptura;
import br.com.rtools.pessoa.BiometriaServidor;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.dao.BiometriaDao;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Registro;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class BiometriaBean implements Serializable {

    private Biometria biometria = null;
    private Boolean disabled = false;

    public boolean isOpenBiometria() {
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
        if (p == null) {
            return false;
        }
        if (biometria == null) {
            if (GenericaSessao.exists("acessoFilial")) {
                BiometriaDao biometriaDao = new BiometriaDao();
                Dao dao = new Dao();
                biometria = biometriaDao.pesquisaBiometriaPorPessoa(p.getId());
                if (biometria != null) {
                    biometria = (Biometria) dao.rebind(biometria);
                    if (biometria.isAtivo() && biometria.getBiometria() != null && !biometria.getBiometria().isEmpty()) {
                        return true;
                    }
                }
            }
        } else {
            if (biometria.getBiometria() != null && !biometria.getBiometria().isEmpty() && biometria.isAtivo()) {
                return true;
            }
        }
        return false;
    }

    public void gravar(Pessoa p) {
        BiometriaAtualizaCatraca bac = (BiometriaAtualizaCatraca) new Dao().find(new BiometriaAtualizaCatraca(), 1);
        Dao dao = new Dao();
        if (bac == null) {
            bac = new BiometriaAtualizaCatraca();
            dao.save(bac, true);
        }
        BiometriaDao biometriaDao = new BiometriaDao();
        if (GenericaSessao.exists("acessoFilial")) {
            disabled = true;
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
            biometria = null;
            for (int i = 0; i < 10; i++) {
                if(isStatus()) {
                    biometria = biometriaDao.pesquisaBiometriaPorPessoa(p.getId());
                    if (biometria != null) {
                        biometria = (Biometria) dao.rebind(biometria);
                        if (!biometria.isAtivo() && biometria.getBiometria() != null && !biometria.getBiometria().isEmpty()) {
                            biometria.setBiometria("");
                            dao.update(biometria, true);
                            bac.setAparelho1(true);
                            bac.setAparelho2(true);
                            bac.setAparelho3(true);
                            bac.setAparelho4(true);
                            dao.update(bac, true);
                        }
                        if (biometria.getBiometria() != null && !biometria.getBiometria().isEmpty()) {
                            PF.closeDialog("dlg_waiting_biometria");
                            disabled = false;
                            break;
                        }
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException ex) {
                    }
                    i--;                    
                } else {
                    PF.closeDialog("dlg_waiting_biometria");
                    disabled = false;
                    PF.openDialog("dlg_waiting_biometria_info");
                    break;
                }
            }
        }
    }

    public void delete(Pessoa p) {
        BiometriaAtualizaCatraca bac = (BiometriaAtualizaCatraca) new Dao().find(new BiometriaAtualizaCatraca(), 1);
        Dao dao = new Dao();
        if (bac == null) {
            bac = new BiometriaAtualizaCatraca();
            dao.save(bac, true);
        }
        BiometriaDao biometriaDao = new BiometriaDao();
        if (GenericaSessao.exists("acessoFilial")) {
            Biometria b = biometriaDao.pesquisaBiometriaPorPessoa(p.getId());
            if (b != null) {
                b.setAtivo(false);
                b.setBiometria("");
                if (dao.update(b, true)) {
                    List list = biometriaDao.listaBiometriaDepartamentoPorPessoa(p.getId());
                    for (Object list1 : list) {
                        dao.delete(list1, true);
                    }
                    bac.setAparelho1(true);
                    bac.setAparelho2(true);
                    bac.setAparelho3(true);
                    bac.setAparelho4(true);
                    dao.update(bac, true);
                    biometria = null;
                    GenericaMensagem.info("Sucesso", "Registro excluído com sucesso");
                } else {
                    GenericaMensagem.warn("Erro", "Ao excluir registro!");
                }
            }
        }
    }

    public Biometria getBiometria() {
        return biometria;
    }

    public void setBiometria(Biometria biometria) {
        this.biometria = biometria;
    }

    public Boolean getDisabled() {
        return disabled;
    }

    public void setDisabled(Boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isDisabledSave() {
        if (biometria != null) {
            if (biometria.isAtivo()) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

}
