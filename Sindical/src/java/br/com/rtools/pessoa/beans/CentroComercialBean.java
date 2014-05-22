package br.com.rtools.pessoa.beans;

import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.CentroComercial;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoCentroComercial;
import br.com.rtools.pessoa.db.CentroComercialDB;
import br.com.rtools.pessoa.db.CentroComercialDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class CentroComercialBean {

    private CentroComercial centroComercial;
    private int idTipos;
    private List<SelectItem> listTiposCentroComercial;
    private List<CentroComercial> listCentroComercial;

    @PostConstruct
    public void init() {
        centroComercial = new CentroComercial();
        idTipos = 0;
        listTiposCentroComercial = new ArrayList<SelectItem>();
        listCentroComercial = new ArrayList();
    }

    public void destroy() {
        GenericaSessao.remove("centroComercialBean");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void save() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        CentroComercialDB db = new CentroComercialDBToplink();
        if (centroComercial.getJuridica().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquise uma empresa antes de salvar!");
            return;
        }
        int idTipoCentroComercial = Integer.parseInt(listTiposCentroComercial.get(idTipos).getDescription());
        if (!db.listaCentroComercial(idTipoCentroComercial, centroComercial.getJuridica().getId()).isEmpty()) {
            GenericaMensagem.warn("Validação", "Essa empresa já existe!");
            return;
        }
        centroComercial.setTipoCentroComercial((TipoCentroComercial) di.find(new TipoCentroComercial(), idTipoCentroComercial));
        di.openTransaction();
        if (centroComercial.getId() == -1) {
            if (!di.save(centroComercial)) {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao inserir registro");
                return;
            }
            novoLog.save(
                    "ID: " + centroComercial.getId()
                    + " - Jurídica: (" + centroComercial.getJuridica().getPessoa().getId() + ") " + centroComercial.getJuridica().getPessoa().getNome()
                    + " - Tipo Centro Comercial: (" + centroComercial.getTipoCentroComercial().getId() + ") " + centroComercial.getTipoCentroComercial().getDescricao()
            );
            GenericaMensagem.info("Sucesso", "Registro inserido");
        } else {
            CentroComercial cc = (CentroComercial) di.find(centroComercial);
            String beforeUpdate
                    = "ID: " + cc.getId()
                    + " - Jurídica: (" + cc.getJuridica().getPessoa().getId() + ") " + cc.getJuridica().getPessoa().getNome()
                    + " - Tipo Centro Comercial: (" + cc.getTipoCentroComercial().getId() + ") " + cc.getTipoCentroComercial().getDescricao();
            if (!di.update(centroComercial)) {
                GenericaMensagem.warn("Erro", "Ao atualizar registro");
                di.rollback();
                return;
            }
            novoLog.update(beforeUpdate,
                    "ID: " + centroComercial.getId()
                    + " - Jurídica: (" + centroComercial.getJuridica().getPessoa().getId() + ") " + centroComercial.getJuridica().getPessoa().getNome()
                    + " - Tipo Centro Comercial: (" + centroComercial.getTipoCentroComercial().getId() + ") " + centroComercial.getTipoCentroComercial().getDescricao()
            );
            GenericaMensagem.info("Sucesso", "Registro atualizado");
        }
        centroComercial = new CentroComercial();
        listCentroComercial.clear();
        di.commit();
    }

    public String edit(CentroComercial cc) {
        DaoInterface di = new Dao();
        centroComercial = (CentroComercial) di.rebind(cc);
        for (int i = 0; i < listTiposCentroComercial.size(); i++) {
            if (Integer.parseInt(listTiposCentroComercial.get(i).getDescription()) == centroComercial.getTipoCentroComercial().getId()) {
                idTipos = i;
            }
        }
        return null;
    }

    public void delete() {
        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();
        if (di.delete(centroComercial)) {
            novoLog.delete(
                    "ID: " + centroComercial.getId()
                    + " - Jurídica: (" + centroComercial.getJuridica().getPessoa().getId() + ") " + centroComercial.getJuridica().getPessoa().getNome()
                    + " - Tipo Centro Comercial: (" + centroComercial.getTipoCentroComercial().getId() + ") " + centroComercial.getTipoCentroComercial().getDescricao()
            );
            di.commit();
            GenericaMensagem.info("Sucesso", "Registro excluído");
            centroComercial = new CentroComercial();
            listCentroComercial.clear();
        } else {
            di.rollback();
            GenericaMensagem.warn("Erro", "Ao excluir registro!");
        }
    }

    public List<SelectItem> getListTiposCentroComercial() {
        if (listTiposCentroComercial.isEmpty()) {
            DaoInterface di = new Dao();
            List<TipoCentroComercial> list = (List<TipoCentroComercial>) di.list(new TipoCentroComercial());
            for (int i = 0; i < list.size(); i++) {
                listTiposCentroComercial.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString((list.get(i)).getId())));
            }
        }
        return listTiposCentroComercial;
    }

    public void setListTiposCentroComercial(List<SelectItem> listTiposCentroComercial) {
        this.listTiposCentroComercial = listTiposCentroComercial;
    }

    public CentroComercial getCentroComercial() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            centroComercial.setJuridica((Juridica) GenericaSessao.getObject("juridicaPesquisa", true));
        }
        return centroComercial;
    }

    public void setCentroComercial(CentroComercial centroComercial) {
        this.centroComercial = centroComercial;
    }

    public int getIdTipos() {
        return idTipos;
    }

    public void setIdTipos(int idTipos) {
        this.idTipos = idTipos;
    }

    public List<CentroComercial> getListCentroComercial() {
        if (listCentroComercial.isEmpty()) {
            DaoInterface di = new Dao();
            listCentroComercial = (List<CentroComercial>) di.list(new CentroComercial(), true);
        }
        return listCentroComercial;
    }

    public void setListCentroComercial(List<CentroComercial> listCentroComercial) {
        this.listCentroComercial = listCentroComercial;
    }
}
