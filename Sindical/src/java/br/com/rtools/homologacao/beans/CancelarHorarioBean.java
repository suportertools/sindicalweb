package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.db.CancelarHorarioDB;
import br.com.rtools.homologacao.db.CancelarHorarioDBToplink;
import br.com.rtools.homologacao.db.HorariosDB;
import br.com.rtools.homologacao.db.HorariosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class CancelarHorarioBean implements Serializable {

    private CancelarHorario cancelarHorario = new CancelarHorario();
    private Horarios horarios = new Horarios();
    private MacFilial macFilial = new MacFilial();
    private Filial filial = new Filial();
    private String msgConfirma = "";
    private List<CancelarHorario> listaHorariosCancelados = new ArrayList();
    private List<SelectItem> listaFiliais = new ArrayList<>();
    private List<SelectItem> listSemana = new ArrayList<>();
    private List<SelectItem> listHorarios = new ArrayList<>();
    private int idFilial = 0;
    private Integer idSemana = 0;
    private int nrQuantidadeDisponivel = 0;
    private int nrQuantidadeCancelado = 0;
    private int nrQuantidadeCancelar = 0;
    private Date data = DataHoje.dataHoje();
    private Date dataInicial = DataHoje.dataHoje();
    private Date dataFinal = DataHoje.dataHoje();
    private int idHorariosDisponiveis = 0;
    private int idHorario = 0;
    private boolean desabilitaBotoes = false;
    private boolean desabilitaFilial = false;
    private String tipoCancelamento = "Dia";
    private Boolean habilitaSemana = false;
    private Boolean habilitaHorarios = false;

    public void cancelarHorario(boolean todos) {

        if (!todos) {
            if (nrQuantidadeCancelar == 0) {
                msgConfirma = "Digite uma quantidade!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                return;
            }
        }

        Dao dao = new Dao();
        CancelarHorarioDB db = new CancelarHorarioDBToplink();
        CancelarHorario ch;
        boolean erro = false;
        Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        dao.openTransaction();
        for (int i = 0; i < getListaHorariosDisponiveis().size(); i++) {
            cancelarHorario.setFilial((Filial) dao.find(new Filial(), Integer.parseInt(getListaFiliais().get(idFilial).getDescription())));
            cancelarHorario.setUsuario(u);
            if (todos) {
                cancelarHorario.setHorarios((Horarios) dao.find(new Horarios(), Integer.parseInt(getListaHorariosDisponiveis().get(i).getDescription())));
                nrQuantidadeDisponivel = cancelarHorario.getHorarios().getQuantidade();
            } else {
                cancelarHorario.setHorarios((Horarios) dao.find(new Horarios(), Integer.parseInt(getListaHorariosDisponiveis().get(idHorariosDisponiveis).getDescription())));
            }
            ch = db.pesquisaCancelamentoHorario(data, cancelarHorario.getHorarios().getId(), cancelarHorario.getFilial().getId());
            if (ch.getId() == -1) {
                cancelarHorario.setDtData(data);
                if (todos) {
                    if (nrQuantidadeDisponivel > 0) {
                        cancelarHorario.setQuantidade(nrQuantidadeDisponivel);
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                } else {
                    cancelarHorario.setQuantidade(nrQuantidadeCancelar);
                }
                if (dao.save(cancelarHorario)) {
                    cancelarHorario = new CancelarHorario();
                    nrQuantidadeDisponivel = 0;
                    erro = false;

                } else {
                    erro = true;
                    break;
                }
            } else {
                cancelarHorario = ch;
                if (todos) {
                    if (nrQuantidadeDisponivel > 0) {
                        cancelarHorario.setQuantidade(nrQuantidadeDisponivel);
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                } else {
                    cancelarHorario.setQuantidade(ch.getQuantidade() + nrQuantidadeCancelar);
                }
                if (dao.update(cancelarHorario)) {
                    cancelarHorario = new CancelarHorario();
                    nrQuantidadeDisponivel = 0;
                    erro = false;
                } else {
                    erro = true;
                    break;
                }
            }
            if (!todos) {
                break;
            }
        }

        if (erro) {
            dao.rollback();
            msgConfirma = "Erro ao cancelar horário(s)!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return;
        } else {
            dao.commit();
            msgConfirma = "Horário cancelado com sucesso.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
            getListaHorariosDisponiveis().clear();
        }
        nrQuantidadeDisponivel = 0;
        listaHorariosCancelados.clear();
        cancelarHorario = new CancelarHorario();
        calculaQuantidadeDisponivel();
    }

    public void cancelarHorarioPeriodo() {
        Date date = DataHoje.dataHoje();
        int intDataHoje = DataHoje.converteDataParaInteger(DataHoje.converteData(date));
        int intDataInicial = DataHoje.converteDataParaInteger(DataHoje.converteData(getDataInicial()));
        int intDataFinal = DataHoje.converteDataParaInteger(DataHoje.converteData(dataFinal));
        String strDataInicial = DataHoje.converteData(getDataInicial());

        if (intDataInicial < intDataHoje) {
            msgConfirma = "A data inicial tem que ser maior ou igual a data de hoje!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return;
        }

        if (intDataFinal < intDataHoje) {
            msgConfirma = "A data final tem que ser maior ou igual a data de hoje!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return;
        }

        if (intDataFinal < intDataInicial) {
            msgConfirma = "A data final tem que ser maior ou igual que a data inicial!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return;
        }

        Dao dao = new Dao();
        Filial f = (Filial) dao.find(new Filial(), Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()));
        HorariosDB horariosDB = new HorariosDBToplink();
        DataHoje dataHoje = new DataHoje();
        List listDatas = new ArrayList();
        int i = 0;
        int y = 0;
        while (i == y) {
            if (i > 0) {
                strDataInicial = dataHoje.incrementarDias(1, strDataInicial);
            }
            intDataInicial = DataHoje.converteDataParaInteger(strDataInicial);
            if (intDataInicial > intDataFinal) {
                y = i + 1;
            } else {
                listDatas.add(i, strDataInicial);
            }
            y++;
            i++;
            if (i == 100) {
                break;
            }
        }
        boolean erro = false;
        List<Horarios> horarioses = new ArrayList<>();
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        CancelarHorario ch;
        Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        dao.openTransaction();
        for (int z = 0; z < listDatas.size(); z++) {
            horarioses.clear();
            cancelarHorario = new CancelarHorario();
            strDataInicial = listDatas.get(z).toString();
            if (!habilitaSemana && !habilitaHorarios) {
                horarioses = horariosDB.pesquisaTodosPorFilial(f.getId(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
            } else if (habilitaSemana && !habilitaHorarios) {
                if (listSemana.isEmpty()) {
                    return;
                }
                if (DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)) == Integer.parseInt(listSemana.get(idSemana).getDescription())) {
                    horarioses = horariosDB.pesquisaTodosPorFilial(f.getId(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
                }
            } else if (!habilitaSemana && habilitaHorarios) {
                if (listHorarios.isEmpty()) {
                    return;
                }
                horarioses = horariosDB.pesquisaPorHorarioFilial(f.getId(), listHorarios.get(idHorario).getDescription());
            } else if (habilitaSemana && habilitaHorarios) {
                if (DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)) == Integer.parseInt(listSemana.get(idSemana).getDescription())) {
                    if (listHorarios.isEmpty() || listSemana.isEmpty()) {
                        return;
                    }
                    horarioses = horariosDB.pesquisaPorHorarioFilial(f.getId(), listHorarios.get(idHorario).getDescription(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
                }
            }
            erro = false;
            for (int x = 0; x < horarioses.size(); x++) {
                ch = cancelarHorarioDB.pesquisaCancelamentoHorario(DataHoje.converte(strDataInicial), horarioses.get(x).getId(), f.getId());
                cancelarHorario.setUsuario(u);
                if (ch.getId() == -1) {
                    cancelarHorario.setFilial(f);
                    cancelarHorario.setHorarios(horarioses.get(x));
                    if (horarioses.get(x).getQuantidade() > 0) {
                        cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                    cancelarHorario.setDtData(DataHoje.converte(strDataInicial));
                    if (dao.save(cancelarHorario)) {
                        cancelarHorario = new CancelarHorario();
                        erro = false;
                    } else {
                        erro = true;
                        break;
                    }
                } else {
                    cancelarHorario = ch;
                    if (horarioses.get(x).getQuantidade() > 0) {
                        cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                    if (dao.update(cancelarHorario)) {
                        cancelarHorario = new CancelarHorario();
                        erro = false;
                    } else {
                        erro = true;
                        break;
                    }
                }
            }
        }

        if (erro) {
            msgConfirma = "Erro ao cancelar horário(s) do período!";
            GenericaMensagem.warn("Erro", msgConfirma);
            dao.rollback();
            return;
        }
        dao.commit();
        listaHorariosCancelados.clear();
        cancelarHorario = new CancelarHorario();
        msgConfirma = "Horários cancelados com sucesso";
        GenericaMensagem.info("Sucesso", msgConfirma);
    }

    public void excluir(CancelarHorario ch) {
        Dao dao = new Dao();
        ch = (CancelarHorario) dao.find(new CancelarHorario(), ch.getId());
        if (ch != null) {
            if (ch.getId() != -1) {
                dao.openTransaction();
                if (dao.delete(ch)) {
                    dao.commit();
                    msgConfirma = "Registro excluído com sucesso.";
                    GenericaMensagem.info("Sucesso", msgConfirma);
                } else {
                    dao.rollback();
                    msgConfirma = "Erro ao excluir horário cancelado!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return;
                }
            }
        }
        calculaQuantidadeDisponivel();
        getListaHorariosCancelados().clear();
    }

    public List<SelectItem> getListaFiliais() {
        if (listaFiliais.isEmpty()) {
            getFilial();
            Dao dao = new Dao();
            List<Filial> select = new ArrayList<>();
            if (filial.getId() != -1) {
                select.add((Filial) dao.find(new Filial(), filial.getId()));
            } else {
                select = (List<Filial>) dao.list(new Filial(), true);
            }
            for (int i = 0; i < select.size(); i++) {
                listaFiliais.add(
                        new SelectItem(
                                i,
                                select.get(i).getFilial().getPessoa().getDocumento() + " / " + select.get(i).getFilial().getPessoa().getNome(),
                                Integer.toString(select.get(i).getId())));
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }

    public List<SelectItem> getListaHorariosDisponiveis() {
        getListaHorariosCancelados();
        List<SelectItem> result = new ArrayList<>();
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        List<Horarios> select = cancelarHorarioDB.listaTodosHorariosDisponiveisPorFilial(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, false);
        if (select.isEmpty()) {
            desabilitaBotoes = true;
            idHorariosDisponiveis = 0;
        } else {
            desabilitaBotoes = false;
            for (int i = 0; i < select.size(); i++) {
                result.add(
                        new SelectItem(
                                i,
                                select.get(i).getHora(),
                                Integer.toString(select.get(i).getId())));
            }
        }
        return result;
    }

    public CancelarHorario getCancelarHorario() {
        return cancelarHorario;
    }

    public void setCancelarHorario(CancelarHorario cancelarHorario) {
        this.cancelarHorario = cancelarHorario;
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<CancelarHorario> getListaHorariosCancelados() {
        listaHorariosCancelados.clear();
        switch (getTipoCancelamento()) {
            case "Dia": {
                CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
                listaHorariosCancelados = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, null);
                break;
            }
            case "Período": {
                CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
                listaHorariosCancelados = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal);
                break;
            }
        }
        return listaHorariosCancelados;
    }

    public void setListaHorariosCancelados(List<CancelarHorario> listaHorariosCancelados) {
        this.listaHorariosCancelados = listaHorariosCancelados;
    }

    public int getIdHorariosDisponiveis() {
        return idHorariosDisponiveis;
    }

    public void setIdHorariosDisponiveis(int idHorariosDisponiveisI) {
        this.idHorariosDisponiveis = idHorariosDisponiveisI;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void dataListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.data = DataHoje.converte(format.format(event.getObject()));
        calculaQuantidadeDisponivel();
    }

    public void dataFinalListener(SelectEvent event) {
        SimpleDateFormat format = new SimpleDateFormat("d/M/yyyy");
        this.dataFinal = DataHoje.converte(format.format(event.getObject()));
    }

    public void calculaQuantidadeDisponivel() {
        nrQuantidadeCancelar = 0;
        nrQuantidadeCancelado = 0;
        nrQuantidadeDisponivel = 0;
        if (getTipoCancelamento().equals("Dia")) {
            CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
            int idHorariox = -1;
            if (!getListaHorariosDisponiveis().isEmpty()) {
                idHorariox = Integer.parseInt(getListaHorariosDisponiveis().get(idHorariosDisponiveis).getDescription());
                Dao dao = new Dao();
                horarios = (Horarios) dao.find(new Horarios(), idHorariox);
            } else {
                horarios = new Horarios();
                idHorariosDisponiveis = 0;
            }
            if (horarios.getId() != -1) {
                CancelarHorario cancelarHorarioA = cancelarHorarioDB.pesquisaCancelamentoHorario(data, idHorariox, Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
                if (cancelarHorarioA != null) {
                    if (horarios.getQuantidade() > 0) {
                        if (cancelarHorarioA.getQuantidade() > horarios.getQuantidade()) {
                            nrQuantidadeDisponivel = 0;
                        } else {
                            nrQuantidadeDisponivel = horarios.getQuantidade() - cancelarHorarioA.getQuantidade();
                        }
                    }
                    nrQuantidadeCancelado = cancelarHorarioA.getQuantidade();
                } else {
                    nrQuantidadeDisponivel = horarios.getQuantidade();
                }
            }
        }
    }

    public int calculaQuantidadeDisponivel(int quantidadeDisponivel, int quantidadeCancelada) {
        int quantidadeRestante = 0;
        if (quantidadeDisponivel > 0) {
            if (quantidadeCancelada > quantidadeDisponivel) {
                quantidadeRestante = 0;
            } else {
                quantidadeRestante = quantidadeDisponivel - quantidadeCancelada;
            }
        }
        return quantidadeRestante;
    }

    public void validaQuantidadeDisponivel() {
        if (nrQuantidadeDisponivel > 0) {
            if (nrQuantidadeCancelar > nrQuantidadeDisponivel) {
                nrQuantidadeCancelar = nrQuantidadeDisponivel;
            }
        }
    }

    public int getNrQuantidadeDisponivel() {
        return nrQuantidadeDisponivel;
    }

    public void setNrQuantidadeDisponivel(int nrQuantidadeDisponivel) {
        this.nrQuantidadeDisponivel = nrQuantidadeDisponivel;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public int getNrQuantidadeCancelado() {
        return nrQuantidadeCancelado;
    }

    public void setNrQuantidadeCancelado(int nrQuantidadeCancelado) {
        this.nrQuantidadeCancelado = nrQuantidadeCancelado;
    }

    public int getNrQuantidadeCancelar() {
        return nrQuantidadeCancelar;
    }

    public void setNrQuantidadeCancelar(int nrQuantidadeCancelar) {
        this.nrQuantidadeCancelar = nrQuantidadeCancelar;
    }

    public boolean isDesabilitaBotoes() {
        return desabilitaBotoes;
    }

    public void setDesabilitaBotoes(boolean desabilitaBotoes) {
        this.desabilitaBotoes = desabilitaBotoes;
    }

    public MacFilial getMacFilial() {
        return macFilial;
    }

    public void setMacFilial(MacFilial macFilial) {
        this.macFilial = macFilial;
    }

    public Filial getFilial() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial") != null) {
            desabilitaFilial = true;
            filial = ((MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial")).getFilial();
        }
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public boolean isDesabilitaFilial() {
        return desabilitaFilial;
    }

    public void setDesabilitaFilial(boolean desabilitaFilial) {
        this.desabilitaFilial = desabilitaFilial;
    }

    public Date getDataFinal() {
        return dataFinal;
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public void cancelamentoPor(TabChangeEvent event) {
        switch (event.getTab().getTitle()) {
            case "Dia":
                setTipoCancelamento("Dia");
                break;
            case "Período":
                setTipoCancelamento("Período");
                break;
            default:
                setTipoCancelamento("");
                break;
        }
        data = DataHoje.dataHoje();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        calculaQuantidadeDisponivel();
    }

    public void excluirCancelamentos() {
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        List<CancelarHorario> list = new ArrayList();
        switch (getTipoCancelamento()) {
            case "Dia":
                list = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, null);
                break;
            case "Período":
                if (!habilitaSemana && !habilitaHorarios) {
                    list = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal);
                } else if (habilitaSemana && !habilitaHorarios) {
                    if (listSemana.isEmpty()) {
                        return;
                    }
                    list = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, Integer.parseInt(listSemana.get(idSemana).getDescription()));
                } else if (!habilitaSemana && habilitaHorarios) {
                    list = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, listHorarios.get(idHorario).getDescription());
                } else if (habilitaSemana && habilitaHorarios) {
                    list = cancelarHorarioDB.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, Integer.parseInt(listSemana.get(idSemana).getDescription()), listHorarios.get(idHorario).getDescription());
                }
                break;
            default:
                return;
        }
        if (!list.isEmpty()) {
            boolean erro = false;
            Dao dao = new Dao();
            try {
                dao.openTransaction();
                for (int i = 0; i < list.size(); i++) {
                    CancelarHorario ch = (CancelarHorario) dao.find(new CancelarHorario(), list.get(i).getId());
                    if (ch != null) {
                        if (!dao.delete(ch)) {
                            erro = true;
                            break;
                        }
                    }
                }
                if (erro) {
                    dao.rollback();
                    msgConfirma = "Erro ao excluir horários!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                } else {
                    dao.commit();
                    calculaQuantidadeDisponivel();
                    getListaHorariosCancelados().clear();
                    msgConfirma = "Horarios excluídos com sucesso.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                }
            } catch (Exception e) {
                dao.rollback();
                msgConfirma = "Erro ao excluir horários!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            }
        } else {
            msgConfirma = "Não existem horários a serem excluídos para data / período!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
        }
    }

    public String getTipoCancelamento() {
        return tipoCancelamento;
    }

    public void setTipoCancelamento(String tipoCancelamento) {
        this.tipoCancelamento = tipoCancelamento;
    }

    public Date getDataInicial() {
        return dataInicial;
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Boolean getHabilitaHorarios() {
        return habilitaHorarios;
    }

    public void setHabilitaHorarios(Boolean habilitaHorarios) {
        if (!habilitaHorarios) {
            listHorarios.clear();
            idHorario = 0;
        }
        this.habilitaHorarios = habilitaHorarios;
    }

    public Boolean getHabilitaSemana() {
        return habilitaSemana;
    }

    public void setHabilitaSemana(Boolean habilitaSemana) {
        if (!habilitaSemana) {
            idSemana = 0;
            listSemana.clear();
        }
        this.habilitaSemana = habilitaSemana;
    }

    public List<SelectItem> getListSemana() {
        if (listSemana.isEmpty()) {
            Dao dao = new Dao();
            List<Semana> list = dao.list(new Semana());
            for (int i = 0; i < list.size(); i++) {
                listSemana.add(new SelectItem(i, list.get(i).getDescricao(), "" + list.get(i).getId()));
            }
        }
        return listSemana;
    }

    public void setListSemana(List<SelectItem> listSemana) {
        this.listSemana = listSemana;
    }

    public List<SelectItem> getListHorarios() {
        if (listHorarios.isEmpty()) {
            HorariosDB horariosDB = new HorariosDBToplink();
            List list;
            if (habilitaSemana) {
                list = horariosDB.listaHorariosAgrupadosPorFilialSemana(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), Integer.parseInt(getListSemana().get(idSemana).getDescription()));
            } else {
                list = horariosDB.listaHorariosAgrupadosPorFilialSemana(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), null);
            }
            for (int i = 0; i < list.size(); i++) {
                listHorarios.add(new SelectItem(i, list.get(i).toString(), "" + list.get(i).toString()));
            }
        }
        return listHorarios;
    }

    public void setListHorarios(List<SelectItem> listHorarios) {
        this.listHorarios = listHorarios;
    }

    public Integer getIdSemana() {
        return idSemana;
    }

    public void setIdSemana(Integer idSemana) {
        this.idSemana = idSemana;
    }

    public int getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(int idHorario) {
        this.idHorario = idHorario;
    }
}
