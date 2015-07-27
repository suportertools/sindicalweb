package br.com.rtools.pessoa.beans;

import br.com.rtools.arrecadacao.db.GrupoCidadesDB;
import br.com.rtools.arrecadacao.db.GrupoCidadesDBToplink;
import br.com.rtools.endereco.Cidade;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.FilialCidade;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.db.FilialCidadeDB;
import br.com.rtools.pessoa.db.FilialCidadeDBToplink;
import br.com.rtools.pessoa.db.FilialDB;
import br.com.rtools.pessoa.db.FilialDao;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
import org.primefaces.event.CellEditEvent;

@ManagedBean
@SessionScoped
public class FilialBean {

    private Filial filial;
    private List<Filial> listaFilial;
    private int idFilial;
    private List<DataObject> listaCidade;
    private boolean adicionarLista;
    private List<SelectItem> result;

    @PostConstruct
    public void init() {
        filial = new Filial();
        listaFilial = new ArrayList();
        idFilial = 0;
        listaCidade = new ArrayList();
        adicionarLista = false;
        result = new ArrayList();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("filialBean");
        GenericaSessao.remove("juridicaPesquisa");
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        Dao dao = new Dao();
        Filial fx = (Filial) dao.find(new Filial(), listaFilial.get(event.getRowIndex()).getId());
        fx.setQuantidadeAgendamentosPorEmpresa((Integer) newValue);

        dao.openTransaction();
        if (!dao.update(fx)) {
            dao.rollback();
            GenericaMensagem.error("Erro", "Não foi possível atualizar filial");
            return;
        }
        dao.commit();
        GenericaMensagem.info("Sucesso", "Filial Atualizada!");
    }

    public void updateFilial(Filial filialx) {

        Dao dao = new Dao();

        dao.openTransaction();
        if (!dao.update(filialx)) {
            dao.rollback();
            GenericaMensagem.error("Erro", "Não foi possível atualizar filial");
            return;
        }

        dao.commit();

    }

    public void removerFilial() {
        filial = new Filial();
    }

    public void clear() {
        GenericaSessao.remove("filialBean");
    }

    public Filial getFilial() {
        if (GenericaSessao.exists("juridicaPesquisa")) {
            filial.setFilial((Juridica) GenericaSessao.getObject("juridicaPesquisa", true));
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public void btnAddFilial() {

        FilialDB db = new FilialDao();

        if (!db.pesquisaFilialExiste(filial.getFilial().getId()).isEmpty()) {
            GenericaMensagem.warn("Erro", "Filial já existe!");
            return;
        }

        DaoInterface di = new Dao();
        NovoLog novoLog = new NovoLog();
        di.openTransaction();

        if (filial.getId() == -1) {
            filial.setMatriz((Juridica) di.find(new Juridica(), 1));
            if (di.save(filial)) {
                novoLog.save(
                        "ID: " + filial.getId()
                        + " - Filial: " + filial.getFilial().getPessoa().getNome()
                );
                GenericaMensagem.info("Sucesso", "Registro adicionado com sucesso");
                di.commit();
            } else {
                GenericaMensagem.warn("Erro", "Falha ao adicionar a filial!");
                di.rollback();
            }
            filial = new Filial();
            adicionarLista = true;
        } else {
            Filial f = (Filial) di.find(filial);
            String beforeUpdate
                    = "ID: " + f.getId()
                    + " - Filial: " + f.getFilial().getPessoa().getNome();
            if (di.update(filial)) {
                novoLog.update(beforeUpdate,
                        "ID: " + filial.getId()
                        + " - Filial: " + filial.getFilial().getPessoa().getNome()
                );
                GenericaMensagem.info("Sucesso", "Registro atualizado com sucesso");
                di.commit();
            } else {
                GenericaMensagem.warn("Erro", "Falha ao atualizar a filial!");
                di.rollback();
            }
        }
        listaFilial.clear();
        filial = new Filial();
    }

    public void saveCidadeFilial(Cidade cid, int index) {
        FilialCidadeDB db = new FilialCidadeDBToplink();
        FilialCidade filialCidade;
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        int iCidade = cid.getId();
        int iFilial = Integer.parseInt(result.get(index).getDescription());
        if (iFilial != -1) {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                filialCidade.setFilial((Filial) di.find(new Filial(), iFilial));
                GenericaMensagem.info("Sucesso", "Cidade atualizada com Sucesso!");
                if (di.update(filialCidade, true)) {
                    novoLog.update("", "Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
            } else {
                filialCidade = new FilialCidade();
                filialCidade.setCidade((Cidade) di.find(new Cidade(), iCidade));
                filialCidade.setFilial((Filial) di.find(new Filial(), iFilial));
                GenericaMensagem.info("Sucesso", "Cidade atualizada com Sucesso!");
                if (di.save(filialCidade, true)) {
                    novoLog.save("Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
            }
        } else {
            filialCidade = db.pesquisaFilialPorCidade(iCidade);
            if (filialCidade.getId() != -1) {
                if (di.delete(filialCidade, true)) {
                    novoLog.save("Cidade Filial - "
                            + "ID: " + filialCidade.getId()
                            + " - Filial: (" + filialCidade.getFilial().getId() + ") " + filialCidade.getFilial().getFilial().getPessoa().getNome()
                            + " - Cidade: (" + filialCidade.getCidade().getId() + ") " + filialCidade.getCidade().getCidade()
                    );
                }
                GenericaMensagem.info("Sucesso", "Cidade atualizada com Sucesso!");
            }
        }
        result = new ArrayList();

    }

    public String novo() {
        filial = new Filial();
        return "filial";
    }

    public void delete(Filial fi) {
        if (fi.getId() != -1) {
            NovoLog novoLog = new NovoLog();
            DaoInterface di = new Dao();
            di.openTransaction();
            if (di.delete(fi)) {
                novoLog.delete(
                        "ID: " + fi.getId()
                        + " - Filial: " + fi.getFilial().getPessoa().getNome()
                );
                GenericaMensagem.info("Sucesso", "Filial excluída com sucesso");
                listaFilial.clear();
                result.clear();
                filial = new Filial();
                getListaFilialSemMatriz().clear();
                di.commit();
            } else {
                GenericaMensagem.warn("Erro", "Não foi possível excluir essa filial. Verifique se há vínculos!");
                listaFilial.clear();
                filial = new Filial();
                di.rollback();
            }
        }
    }

    public List<Filial> getListaFilial() {
        listaFilial = new Dao().list(new Filial(), true);
        return listaFilial;
    }

    public List<Filial> getListaFilialSemMatriz() {
        if (listaFilial.isEmpty()) {
            listaFilial = new Dao().list(new Filial(), true);
        }
        return listaFilial;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<DataObject> getListaCidade() {
        if (listaCidade.isEmpty()) {
            GrupoCidadesDB dbCids = new GrupoCidadesDBToplink();
            //List<GrupoCidades> lis = dbCids.pesquisaTodos();
            List<Cidade> lis = dbCids.pesquisaCidadesBase();

            DaoInterface di = new Dao();
            List<FilialCidade> fc = (List<FilialCidade>) di.list(new FilialCidade());

            if (!lis.isEmpty()) {
                boolean tem;
                for (int i = 0; i < lis.size(); i++) {
                    tem = false;
                    for (int w = 0; w < fc.size(); w++) {
                        if (lis.get(i).getId() == fc.get(w).getCidade().getId()) {
                            for (int u = 0; u < getResult().size(); u++) {
                                if (fc.get(w).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())) {
                                    listaCidade.add(new DataObject((Cidade) di.find(new Cidade(), lis.get(i).getId()), u));
                                    tem = true;
                                }
                                if (tem) {
                                    break;
                                }
                            }
                            if (tem) {
                                break;
                            }
                        }
                        if (tem) {
                            break;
                        }
                    }
                    if (!tem) {
                        listaCidade.add(new DataObject((Cidade) di.find(new Cidade(), lis.get(i).getId()), 0));
                    }
                }
            }
        }
        return listaCidade;
    }

    public void setListaCidade(List<DataObject> listaCidade) {
        this.listaCidade = listaCidade;
    }

    public List<SelectItem> getResult() {
        if ((result.isEmpty()) || (this.adicionarLista)) {
            result.clear();
            FilialDB db = new FilialDao();
            List<Filial> fi = new Dao().list(new Filial(), true);
            result.add(new SelectItem(0,
                    " -- NENHUM -- ",
                    "-1"));
            for (int i = 0; i < fi.size(); i++) {
                result.add(new SelectItem(i + 1,
                        fi.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(fi.get(i).getId()))
                );
            }
            this.adicionarLista = false;
//            atualizarIndexFilial();
        }
        return result;
    }

//    public void atualizarIndexFilial(){
//        FilialCidadeDB db = new FilialCidadeDBToplink();
//        FilialDB dbF = new FilialDao();
//        List<FilialCidade> fc = db.pesquisaTodos();
//        //List<Filial> fili = dbF.pesquisaTodos();
//        boolean tem;
//        for(int i = 0; i < fc.size();i++){
//            tem = false;
//            for(int w = 0; w < listaCidade.size(); w++){
//                if (!tem){
//                    if(fc.get(i).getCidade().getId() == listaCidade.get(w).getCidade().getId()){
//                        for (int u = 0; u < result.size();u++){
//                            if (fc.get(i).getFilial().getId() == Integer.valueOf(result.get(u).getDescription())){
//                                listaCidade.get(w).setIndiceFilial(u);
//                                tem = true;
//                                break;
//                            }
//                        }
//                    }
//                }else break;
//            }
//        }
//    }
    public void setResult(List<SelectItem> result) {
        this.result = result;
    }

}
