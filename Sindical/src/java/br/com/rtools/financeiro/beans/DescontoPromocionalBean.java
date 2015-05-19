package br.com.rtools.financeiro.beans;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.financeiro.DescontoPromocional;
import br.com.rtools.financeiro.GrupoFinanceiro;
import br.com.rtools.financeiro.ServicoValor;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.dao.DescontoPromocionalDao;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.financeiro.db.ServicoValorDB;
import br.com.rtools.financeiro.db.ServicoValorDBToplink;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class DescontoPromocionalBean implements Serializable {

    private List<SelectItem> listGrupoFinanceiro = new ArrayList();
    private Integer idGrupoFinanceiro = 0;
    private List<SelectItem> listSubGrupoFinanceiro = new ArrayList();
    private Integer idSubGrupoFinanceiro = 0;
    private List<SelectItem> listServicos = new ArrayList();
    private Integer idServicos = 0;
    private List<SelectItem> listCategoria = new ArrayList();
    private Integer idCategoria = 0;
    private DescontoPromocional descontoPromocional = new DescontoPromocional();

    private List<DataObject> listDataObject = new ArrayList();
    private String filtroPor = "naoVencidos";

    @PostConstruct
    public void init() {
        loadListGrupoFinanceiro();
        loadCategoria();
        loadListDataObject();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("descontoPromocionalBean");
    }
//    
//    public void atualizaValor(){
//        if (!listServicos.isEmpty()){
//            ServicoValorDB db = new ServicoValorDBToplink();
//            //Servicos s = (Servicos) new Dao().find(new Servicos(),  );
//            ServicoValor sv = db.pesquisaServicoValorPorIdade(Integer.valueOf(listServicos.get(idServicos).getDescription()), 0);
//            valor = sv.getValorString();
//        }else
//            valor = "0,00";
//    }

    public void save() {
        if (listServicos.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Lista de Serviço não pode ser vazia!");
            return;
        }

        if (descontoPromocional.getReferenciaInicial().isEmpty()) {
            GenericaMensagem.warn("Atenção", "A referência Inicial não pode ser vazia!");
            return;
        }

        if (descontoPromocional.getReferenciaFinal().isEmpty()) {
            GenericaMensagem.warn("Atenção", "A referência Final não pode ser vazia!");
            return;
        }

        DescontoPromocionalDao dpd = new DescontoPromocionalDao();
        Dao dao = new Dao();

        dao.openTransaction();

        if (descontoPromocional.getId() == -1) {
            descontoPromocional.setServico((Servicos) dao.find(new Servicos(), Integer.valueOf(listServicos.get(idServicos).getDescription())));
            if (listCategoria.get(idCategoria).getDescription().equals("0")) {
                int exist = 0;
                for (int i = 2; i < listCategoria.size(); i++) {
                    DescontoPromocional dp = new DescontoPromocional(
                            -1,
                            descontoPromocional.getDesconto(),
                            descontoPromocional.getReferenciaInicial(),
                            descontoPromocional.getReferenciaFinal(),
                            descontoPromocional.getServico(),
                            (Categoria) dao.find(new Categoria(), Integer.valueOf(listCategoria.get(i).getDescription()))
                    );

                    if (!dpd.listaDescontoPromocional(dp.getServico().getId(), dp.getCategoria().getId(), "", "").isEmpty()) {
                        exist++;
                        GenericaMensagem.warn("Atençao", "Este Desconto " + dp.getServico().getDescricao() + ", " + dp.getCategoria().getCategoria() + " já existe!");
                        continue;
                    }

                    if (!dao.save(dp)) {
                        GenericaMensagem.error("Atençao", "Nao foi possivel salvar Desconto, tente novamente!");
                        dao.rollback();
                        return;
                    }
                }

                if (exist == (listCategoria.size() - 2)) {
                    dao.rollback();
                } else {
                    GenericaMensagem.info("Sucesso", "Desconto Salvo!");
                    dao.commit();
                }
            } else if (listCategoria.get(idCategoria).getDescription().equals("-1")) {
                descontoPromocional.setCategoria(null);

                if (!dpd.listaDescontoPromocional(descontoPromocional.getServico().getId(), null, "", "").isEmpty()) {
                    dao.rollback();
                    GenericaMensagem.warn("Atençao", "Este Desconto já existe!");
                    return;
                }

                if (!dao.save(descontoPromocional)) {
                    GenericaMensagem.error("Atençao", "Nao foi possivel salvar Desconto, tente novamente!");
                    dao.rollback();
                    return;
                }
                dao.commit();
                GenericaMensagem.info("Sucesso", "Desconto Salvo!");
            } else {
                descontoPromocional.setCategoria((Categoria) dao.find(new Categoria(), Integer.valueOf(listCategoria.get(idCategoria).getDescription())));

                if (!dpd.listaDescontoPromocional(descontoPromocional.getServico().getId(), descontoPromocional.getCategoria().getId(), "", "").isEmpty()) {
                    dao.rollback();
                    GenericaMensagem.warn("Atençao", "Este Desconto já existe!");
                    return;
                }

                if (!dao.save(descontoPromocional)) {
                    GenericaMensagem.error("Atençao", "Nao foi possivel salvar Desconto, tente novamente!");
                    dao.rollback();
                    return;
                }
                dao.commit();
                GenericaMensagem.info("Sucesso", "Desconto Salvo!");
            }
        } else {
            if (!dao.update(descontoPromocional)) {
                GenericaMensagem.error("Atençao", "Não foi possível atualizar Desconto, tente novamente!");
                dao.rollback();
                return;
            }
            
            dao.commit();
            GenericaMensagem.info("Sucesso", "Desconto Atualizado!");
        }
        descontoPromocional = new DescontoPromocional();

        loadListDataObject();
    }

    public void delete() {
        Dao dao = new Dao();

        dao.openTransaction();
        if (!dao.delete(dao.find(descontoPromocional))) {
            GenericaMensagem.error("Erro", "Não foi possível excluir Desconto Promocional, tente novamente!");
            return;
        }

        GenericaMensagem.info("Sucesso", "Desconto Promocional excluído!");
        dao.commit();

        clear();
        init();
    }

    public void edit(DataObject linha) {
        descontoPromocional = (DescontoPromocional) linha.getArgumento0();

        for (int i = 0; i < listGrupoFinanceiro.size(); i++) {
            if (Integer.valueOf(listGrupoFinanceiro.get(i).getDescription()) == descontoPromocional.getServico().getSubGrupoFinanceiro().getGrupoFinanceiro().getId()) {
                idGrupoFinanceiro = i;
            }
        }

        loadListSubGrupoFinanceiro();

        for (int i = 0; i < listSubGrupoFinanceiro.size(); i++) {
            if (Integer.valueOf(listSubGrupoFinanceiro.get(i).getDescription()) == descontoPromocional.getServico().getSubGrupoFinanceiro().getId()) {
                idSubGrupoFinanceiro = i;
            }
        }

        loadServicos();

        for (int i = 0; i < listServicos.size(); i++) {
            if (Integer.valueOf(listServicos.get(i).getDescription()) == descontoPromocional.getServico().getId()) {
                idServicos = i;
            }
        }

//        atualizaValor();
        if (descontoPromocional.getCategoria() != null){
            for (int i = 0; i < listCategoria.size(); i++) {
                if (Integer.valueOf(listCategoria.get(i).getDescription()) == descontoPromocional.getCategoria().getId()) {
                    idCategoria = i;
                }
            }
        }else{
            idCategoria = 1;
        }

    }

    public void clear() {
        GenericaSessao.remove("descontoPromocionalBean");
    }

    public void loadListGrupoFinanceiro() {
        listGrupoFinanceiro.clear();
        idGrupoFinanceiro = 0;

        List<GrupoFinanceiro> list = new Dao().list(new GrupoFinanceiro(), true);
        for (int i = 0; i < list.size(); i++) {
            listGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
        }

        loadListSubGrupoFinanceiro();
    }

    public void loadListSubGrupoFinanceiro() {
        listSubGrupoFinanceiro.clear();
        idSubGrupoFinanceiro = 0;
        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
        List<SubGrupoFinanceiro> list = financeiroDB.listaSubGrupo(Integer.parseInt(listGrupoFinanceiro.get(idGrupoFinanceiro).getDescription()));
        for (int i = 0; i < list.size(); i++) {
            listSubGrupoFinanceiro.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
        }

        loadServicos();
    }

    public void loadServicos() {
        listServicos.clear();
        idServicos = 0;

        DescontoPromocionalDao dao = new DescontoPromocionalDao();
        List<Servicos> result = dao.listaServicosDisponiveis(Integer.parseInt(listSubGrupoFinanceiro.get(idSubGrupoFinanceiro).getDescription()));
        for (int i = 0; i < result.size(); i++) {
            listServicos.add(new SelectItem(i, result.get(i).getDescricao(), "" + result.get(i).getId()));
        }

//        atualizaValor();
    }

    public void loadCategoria() {
        listCategoria.clear();
        idCategoria = 0;

        List<Categoria> result = new DescontoPromocionalDao().listaCategoria();

        listCategoria.add(new SelectItem(0, "TODAS", "0"));
        listCategoria.add(new SelectItem(1, "NÃO SÓCIO", "-1"));

        int contador = 2;
        for (int i = 0; i < result.size(); i++) {
            listCategoria.add(new SelectItem(contador, result.get(i).getCategoria(), "" + result.get(i).getId()));
            contador++;
        }
    }

    public void loadListDataObject() {
        listDataObject.clear();

        DescontoPromocionalDao dao = new DescontoPromocionalDao();

        List<Vector> result = dao.listaDescontoPromocional(filtroPor);

        for (Vector vect : result) {
            DescontoPromocional dp = (DescontoPromocional) new Dao().find(new DescontoPromocional(), vect.get(0));
            ServicoValorDB db = new ServicoValorDBToplink();
            ServicoValor sv = db.pesquisaServicoValorPorIdade(dp.getServico().getId(), 0);

            String valorx = sv.getValorString();
            listDataObject.add(new DataObject(
                    dp,
                    vect,
                    Moeda.valorDoPercentual(vect.get(3).toString(), Moeda.converteR$Float(dp.getDesconto())) // VALOR FINAL
            )
            );
        }
    }

    public String converteMoeda(float valor) {
        return Moeda.converteR$Float(valor);
    }

    public List<SelectItem> getListGrupoFinanceiro() {
        return listGrupoFinanceiro;
    }

    public void setListGrupoFinanceiro(List<SelectItem> listGrupoFinanceiro) {
        this.listGrupoFinanceiro = listGrupoFinanceiro;
    }

    public Integer getIdGrupoFinanceiro() {
        return idGrupoFinanceiro;
    }

    public void setIdGrupoFinanceiro(Integer idGrupoFinanceiro) {
        this.idGrupoFinanceiro = idGrupoFinanceiro;
    }

    public List<SelectItem> getListSubGrupoFinanceiro() {
        return listSubGrupoFinanceiro;
    }

    public void setListSubGrupoFinanceiro(List<SelectItem> listSubGrupoFinanceiro) {
        this.listSubGrupoFinanceiro = listSubGrupoFinanceiro;
    }

    public Integer getIdSubGrupoFinanceiro() {
        return idSubGrupoFinanceiro;
    }

    public void setIdSubGrupoFinanceiro(Integer idSubGrupoFinanceiro) {
        this.idSubGrupoFinanceiro = idSubGrupoFinanceiro;
    }

    public List<SelectItem> getListServicos() {
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

//    public String getValor() {
//        return Moeda.converteR$(valor);
//    }
//
//    public void setValor(String valor) {
//        this.valor = Moeda.converteR$(valor);
//    }
    public List<SelectItem> getListCategoria() {
        return listCategoria;
    }

    public void setListCategoria(List<SelectItem> listCategoria) {
        this.listCategoria = listCategoria;
    }

    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public DescontoPromocional getDescontoPromocional() {
        return descontoPromocional;
    }

    public void setDescontoPromocional(DescontoPromocional descontoPromocional) {
        this.descontoPromocional = descontoPromocional;
    }

    public List<DataObject> getListDataObject() {
        return listDataObject;
    }

    public void setListDataObject(List<DataObject> listDataObject) {
        this.listDataObject = listDataObject;
    }

    public String getFiltroPor() {
        return filtroPor;
    }

    public void setFiltroPor(String filtroPor) {
        this.filtroPor = filtroPor;
    }
}
