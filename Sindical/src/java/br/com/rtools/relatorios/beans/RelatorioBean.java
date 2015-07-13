package br.com.rtools.relatorios.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.relatorios.RelatorioOrdem;
import br.com.rtools.relatorios.Relatorios;
import br.com.rtools.relatorios.db.RelatorioGenericoDB;
import br.com.rtools.relatorios.dao.RelatorioOrdemDao;
import br.com.rtools.relatorios.db.RelatorioGenericoDBToplink;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.GenericaMensagem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class RelatorioBean implements Serializable {

    private Relatorios relatorio = new Relatorios();
    private RelatorioOrdem relatorioOrdem = new RelatorioOrdem();
    private List<SelectItem> listaRotina = new ArrayList<>();
    private List<Relatorios> listaRelatorio = new ArrayList();
    private List<RelatorioOrdem> listRelatorioOrdem = new ArrayList<>();
    private String msgConfirma = "";
    private int index = 0;

    public void addRelatorioOrdem() {
        if (relatorioOrdem.getNome().isEmpty() || relatorioOrdem.getQuery().isEmpty()) {
            GenericaMensagem.warn("Sucesso", "Informar descrição e query!");
            return;

        }
        Dao dao = new Dao();
        Boolean sucess = false;
        String message = "";
        if (relatorioOrdem.getId() == null) {
            relatorioOrdem.setRelatorios(relatorio);
            if (dao.save(relatorioOrdem, true)) {
                sucess = true;
                message = "Registro inserido";
            } else {
                message = "Ao inserir registro!";
            }
        } else {
            if (dao.update(relatorioOrdem, true)) {
                sucess = true;
                message = "Registro atualizado";
            } else {
                message = "Ao atualizar registro!";
            }
        }
        if (!defaultRelatorioOrdem(relatorioOrdem)) {
            sucess = false;
            message = "Ao definir default!";
        }
        if (sucess) {
            GenericaMensagem.info("Sucesso", message);
            relatorioOrdem = new RelatorioOrdem();
            listRelatorioOrdem.clear();
        } else {
            GenericaMensagem.warn("Erro", message);

        }
    }

    public Boolean defaultRelatorioOrdem(RelatorioOrdem ro) {
        if (new RelatorioOrdemDao().defineDefault(ro)) {
            return true;
        }
        return false;
    }

    public void defaultOrdem(RelatorioOrdem ro) {
        ro.setPrincipal(true);
        if(defaultRelatorioOrdem(ro)) {
            listRelatorioOrdem.clear();
            getListRelatorioOrdem();
        }
    }

    public void deleteRelatorioOrdem(RelatorioOrdem ro) {
        Dao dao = new Dao();
        if (dao.delete(ro, true)) {
            GenericaMensagem.info("Sucesso", "Registro excluído");
            relatorioOrdem = new RelatorioOrdem();
            listRelatorioOrdem.clear();
        } else {
            GenericaMensagem.warn("Erro", "Ao excluir registro!");
        }
    }

    public void editRelatorioOrdem(RelatorioOrdem ro) {
        relatorioOrdem = ro;
    }

    public String salvar() {
        if (relatorio.getNome().isEmpty()) {
            msgConfirma = "Digite uma descrição!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        if (relatorio.getJasper().isEmpty()) {
            msgConfirma = "Digite um caminho para o Jasper!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        }

        Dao dao = new Dao();
        NovoLog log = new NovoLog();

        relatorio.setRotina((Rotina) dao.find(new Rotina(), Integer.parseInt(listaRotina.get(index).getDescription())));

        dao.openTransaction();
        if (relatorio.getId() == -1) {
            if (dao.save(relatorio)) {
                msgConfirma = "Relatório salvo com Sucesso!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
                log.save("Relatório inserido " + relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
            } else {
                msgConfirma = "Erro ao salvar Relatório!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                dao.rollback();
                return null;
            }
        } else {
            Relatorios rel = (Relatorios) dao.find(new Relatorios(), relatorio.getId());
            String antes = "De: " + rel.getNome() + " / " + relatorio.getNome() + " -  " + rel.getJasper() + " / " + relatorio.getJasper();

            if (dao.update(relatorio)) {
                msgConfirma = "Registro atualizado!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
                log.update(antes, relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
            } else {
                msgConfirma = "Erro ao atualizar!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                dao.rollback();
                return null;
            }
        }
        listaRelatorio.clear();
        dao.commit();
        return null;
    }

    public String novo() {
        relatorio = new Relatorios();
        listRelatorioOrdem.clear();
        relatorioOrdem = new RelatorioOrdem();
        index = 0;
        msgConfirma = "";
        return "relatorio";
    }

    public String excluir() {
        if (relatorio.getId() == -1) {
            msgConfirma = "Pesquise um relatório para exclusão";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return "relatorio";
        }

        Dao dao = new Dao();
        NovoLog log = new NovoLog();
        dao.openTransaction();

        if (dao.delete(relatorio)) {
            msgConfirma = "Relatório excluido com Sucesso!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso!", msgConfirma));
            log.delete(relatorio.getId() + " - " + relatorio.getNome() + " / " + relatorio.getJasper());
        } else {
            msgConfirma = "Relatório não pode ser excluido";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            dao.rollback();
            return null;
        }

        dao.commit();
        relatorio = new Relatorios();
        index = 0;
        return null;
    }

    public String editar(Relatorios rela) {
        this.relatorio = rela;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        if (!getListaRotina().isEmpty()) {
            for (int o = 0; o < listaRotina.size(); o++) {
                if (Integer.parseInt(listaRotina.get(o).getDescription()) == relatorio.getRotina().getId()) {
                    index = o;
                }
            }
        }
        return "relatorio";
    }

    public Relatorios getRelatorio() {
        return relatorio;
    }

    public void setRelatorio(Relatorios relatorio) {
        this.relatorio = relatorio;
    }

    public List<SelectItem> getListaRotina() {
        if (listaRotina.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            List<Rotina> result = db.pesquisaRotina();
            for (int i = 0; i < result.size(); i++) {
                listaRotina.add(new SelectItem(i,
                        result.get(i).getRotina(),
                        String.valueOf(result.get(i).getId()))
                );
            }
        }
        return listaRotina;
    }

    public void setListaRotina(List<SelectItem> listaRotina) {
        this.listaRotina = listaRotina;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public List<Relatorios> getListaRelatorio() {
        if (listaRelatorio.isEmpty()) {
            RelatorioGenericoDB db = new RelatorioGenericoDBToplink();
            listaRelatorio = db.pesquisaTodosRelatorios();
        }
        return listaRelatorio;
    }

    public void setListaRelatorio(List<Relatorios> listaRelatorio) {
        this.listaRelatorio = listaRelatorio;
    }

    public RelatorioOrdem getRelatorioOrdem() {
        return relatorioOrdem;
    }

    public void setRelatorioOrdem(RelatorioOrdem relatorioOrdem) {
        this.relatorioOrdem = relatorioOrdem;
    }

    public List<RelatorioOrdem> getListRelatorioOrdem() {
        if (listRelatorioOrdem.isEmpty()) {
            RelatorioOrdemDao relatorioOrdemDao = new RelatorioOrdemDao();
            listRelatorioOrdem = relatorioOrdemDao.findAllByRelatorio(relatorio.getId());
            for(int i = 0; i < listRelatorioOrdem.size(); i++) {
                listRelatorioOrdem.set(i, (RelatorioOrdem) new Dao().rebind(listRelatorioOrdem.get(i)));
            }
        }
        return listRelatorioOrdem;
    }

    public void setListRelatorioOrdem(List<RelatorioOrdem> listRelatorioOrdem) {
        this.listRelatorioOrdem = listRelatorioOrdem;
    }
}
