package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.ConviteServico;
import br.com.rtools.associativo.db.ConviteDB;
import br.com.rtools.associativo.db.ConviteDBToplink;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.logSistema.NovoLog;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class ConviteServicoBean implements Serializable {

    private Servicos servicos;
    private ConviteServico conviteServico;
    private List<ConviteServico> listConviteServicos;
    private String message;
    private String descricaoPesquisa;
    private List<SelectItem> listServicos;
    private int idServicos;

    @PostConstruct
    public void init() {
        servicos = new Servicos();
        conviteServico = new ConviteServico();
        listConviteServicos = new ArrayList();
        message = "";
        descricaoPesquisa = "";
        listServicos = new ArrayList();
        idServicos = 0;
    }

    @PreDestroy
    public void destroy() {
        clear();
    }

    public void save() {
        if (listServicos.isEmpty()) {
            GenericaMensagem.warn("Validação", "Cadastrar serviço!");
            return;
        }
        
        ConviteDB db = new ConviteDBToplink();
        if (!db.listaConviteServico(Integer.parseInt(listServicos.get(idServicos).getDescription())).isEmpty()){
            message = "Esse serviço já foi adicionado!";
            return;
        }
        
        NovoLog novoLog = new NovoLog();
        DaoInterface di = new Dao();
        
        conviteServico.setServicos((Servicos) di.find(new Servicos(), Integer.parseInt(listServicos.get(idServicos).getDescription())));
        if (conviteServico.getId() == -1) {
            di.openTransaction();
            if (di.save(conviteServico)) {
                di.commit();
                novoLog.save(""
                        + "ID: " + conviteServico.getId()
                        + " - Serviço: (" + conviteServico.getId() + ") " + conviteServico.getServicos().getDescricao()
                        + " - Dias: "
                        + " [Dom][" + conviteServico.isDomingo() + "]"
                        + " [Seg][" + conviteServico.isSegunda() + "]"
                        + " [Ter][" + conviteServico.isTerca() + "]"
                        + " [Qua][" + conviteServico.isQuarta() + "]"
                        + " [Qui][" + conviteServico.isQuinta() + "]"
                        + " [Sex][" + conviteServico.isSexta() + "]"
                        + " [Sab][" + conviteServico.isSabado() + "]"
                        + " [Feriados][" + conviteServico.isFeriado() + "]"
                );
                message = "Registro inserido com sucesso";
                listConviteServicos.clear();
            } else {
                di.rollback();
                message = "Erro ao adicionar registro!";
            }
        } else {
            ConviteServico cs = (ConviteServico) di.find(conviteServico);
            String beforeUpdate = ""
                    + "ID: " + cs.getId()
                    + " - Serviço: (" + cs.getId() + ") " + cs.getServicos().getDescricao()
                    + " - Dias: "
                    + " [Dom][" + cs.isDomingo() + "]"
                    + " [Seg][" + cs.isSegunda() + "]"
                    + " [Ter][" + cs.isTerca() + "]"
                    + " [Qua][" + cs.isQuarta() + "]"
                    + " [Qui][" + cs.isQuinta() + "]"
                    + " [Sex][" + cs.isSexta() + "]"
                    + " [Sab][" + cs.isSabado() + "]"
                    + " [Feriados][" + cs.isFeriado() + "]";
            di.openTransaction();
            if (di.update(conviteServico)) {
                di.commit();
                novoLog.update(beforeUpdate, ""
                        + " ID: " + conviteServico.getId()
                        + " - Serviço: (" + conviteServico.getId() + ") " + conviteServico.getServicos().getDescricao()
                        + " - Dias: "
                        + " [Dom][" + conviteServico.isDomingo() + "]"
                        + " [Seg][" + conviteServico.isSegunda() + "]"
                        + " [Ter][" + conviteServico.isTerca() + "]"
                        + " [Qua][" + conviteServico.isQuarta() + "]"
                        + " [Qui][" + conviteServico.isQuinta() + "]"
                        + " [Sex][" + conviteServico.isSexta() + "]"
                        + " [Sab][" + conviteServico.isSabado() + "]"
                        + " [Feriados][" + conviteServico.isFeriado() + "]"
                );
                message = "Registro atualizado com sucesso";
                listConviteServicos.clear();
            } else {
                di.rollback();
                message = "Erro ao atualizar registro!";
            }
        }
        conviteServico = new ConviteServico();
    }

    public void clear() {
        GenericaSessao.remove("conviteServicoBean");
    }

    public void edit(ConviteServico cs) {
        conviteServico = new ConviteServico();
        DaoInterface di = new Dao();
        conviteServico = (ConviteServico) di.rebind(cs);
        for (int i = 0; i < listServicos.size(); i++) {
            if (Integer.parseInt(listServicos.get(i).getDescription()) == cs.getServicos().getId()) {
                idServicos = i;
                break;
            }
        }
    }

    public void updateDiaSemana(ConviteServico cs) {
        if (cs.getId() != -1) {
            NovoLog novoLog = new NovoLog();
            DaoInterface di = new Dao();
            di.openTransaction();
            ConviteServico csb = (ConviteServico) di.find(cs);
            String beforeUpdate = ""
                    + "ID: " + csb.getId()
                    + " - Serviço: (" + csb.getId() + ") " + csb.getServicos().getDescricao()
                    + " - Dias: "
                    + " [Dom][" + csb.isDomingo() + "]"
                    + " [Seg][" + csb.isSegunda() + "]"
                    + " [Ter][" + csb.isTerca() + "]"
                    + " [Qua][" + csb.isQuarta() + "]"
                    + " [Qui][" + csb.isQuinta() + "]"
                    + " [Sex][" + csb.isSexta() + "]"
                    + " [Sab][" + csb.isSabado() + "]"
                    + " [Feriados][" + csb.isFeriado() + "]";
            if (di.update(cs)) {
                if (cs.getId() == conviteServico.getId()) {
                    conviteServico = cs;
                }
                di.commit();
                novoLog.update(beforeUpdate, ""
                        + "ID: " + cs.getId()
                        + " - Serviço: (" + cs.getId() + ") " + csb.getServicos().getDescricao()
                        + " - Dias: "
                        + " [Dom][" + cs.isDomingo() + "]"
                        + " [Seg][" + cs.isSegunda() + "]"
                        + " [Ter][" + cs.isTerca() + "]"
                        + " [Qua][" + cs.isQuarta() + "]"
                        + " [Qui][" + cs.isQuinta() + "]"
                        + " [Sex][" + cs.isSexta() + "]"
                        + " [Sab][" + cs.isSabado() + "]"
                        + " [Feriados][" + cs.isFeriado() + "]"
                );
                GenericaMensagem.info("Sucesso", "Registro atualizado.");
                listConviteServicos.clear();
            } else {
                di.rollback();
                GenericaMensagem.warn("Erro", "Ao atualizar registro!");
            }

        }
    }

    public void delete() {
        delete(conviteServico);
        conviteServico = new ConviteServico();
    }

    public void delete(ConviteServico cs) {
        if (cs.getId() != -1) {
            DaoInterface di = new Dao();
            NovoLog novoLog = new NovoLog();
            di.openTransaction();
            if (di.delete(cs)) {
                novoLog.delete(""
                        + "ID: " + cs.getId()
                        + " - Serviço: (" + cs.getId() + ") " + cs.getServicos().getDescricao()
                        + " - Dias: "
                        + " [Dom][" + cs.isDomingo() + "]"
                        + " [Seg][" + cs.isSegunda() + "]"
                        + " [Ter][" + cs.isTerca() + "]"
                        + " [Qua][" + cs.isQuarta() + "]"
                        + " [Qui][" + cs.isQuinta() + "]"
                        + " [Sex][" + cs.isSexta() + "]"
                        + " [Sab][" + cs.isSabado() + "]"
                        + " [Feriados][" + cs.isFeriado() + "]"
                );
                di.commit();
                message = "Registro excluído com sucesso";
                listConviteServicos.clear();
            } else {
                di.rollback();
                message = "Erro ao excluir registro!";
            }
        }
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

    public ConviteServico getConviteServico() {
        return conviteServico;
    }

    public void setConviteServico(ConviteServico conviteServico) {
        this.conviteServico = conviteServico;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescricaoPesquisa() {
        return descricaoPesquisa;
    }

    public void setDescricaoPesquisa(String descricaoPesquisa) {
        this.descricaoPesquisa = descricaoPesquisa;
    }

    public List<ConviteServico> getListConviteServicos() {
        if (listConviteServicos.isEmpty()) {
            DaoInterface di = new Dao();
            listConviteServicos = (List<ConviteServico>) di.list(new ConviteServico(), true);
        }
        return listConviteServicos;
    }

    public void setListConviteServicos(List<ConviteServico> listConviteServicos) {
        this.listConviteServicos = listConviteServicos;
    }

    public List<SelectItem> getListServicos() {
        if (listServicos.isEmpty()) {
            ServicosDB db = new ServicosDBToplink();
            List<Servicos> lista = (List<Servicos>) db.pesquisaTodos(215);
            int i = 0;
            for (Servicos s : lista) {
                listServicos.add(new SelectItem(i, s.getDescricao(), Integer.toString((s.getId()))));
                i++;
            }
        }
        return listServicos;
    }

    public void setListServicos(List<SelectItem> listServicos) {
        this.listServicos = listServicos;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

}
