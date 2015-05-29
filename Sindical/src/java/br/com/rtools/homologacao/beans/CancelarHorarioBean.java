package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.dao.CancelarHorarioDao;
import br.com.rtools.homologacao.dao.HorariosDao;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.sistema.Semana;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Tabbed;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

@ManagedBean
@SessionScoped
public class CancelarHorarioBean implements Serializable {

    private CancelarHorario cancelarHorario;
    private Horarios horarios;
    private MacFilial macFilial;
    private Filial filial;
    private List<CancelarHorario> listaHorariosCancelados;
    private List<SelectItem> listaFiliais;
    private List<SelectItem> listSemana;
    private List<SelectItem> listHorarios;
    private Integer idFilial;
    private Integer idSemana;
    private Integer nrQuantidadeDisponivel;
    private Integer nrQuantidadeCancelado;
    private Integer nrQuantidadeCancelar;
    private Integer nrQuantidadeDisponivelB;
    private Integer nrQuantidadeCanceladoB;
    private Integer nrQuantidadeCancelarB;
    private Date data;
    private Date dataInicial = DataHoje.dataHoje();
    private Date dataFinal;
    private Integer idHorariosDisponiveis;
    private Integer idHorario;
    private boolean desabilitaBotoes;
    private boolean desabilitaFilial;
    private String tipoCancelamento;
    private Boolean habilitaSemana;
    private Boolean habilitaHorarios;

    @PostConstruct
    public void init() {
        cancelarHorario = new CancelarHorario();
        horarios = new Horarios();
        macFilial = new MacFilial();
        filial = new Filial();
        listaHorariosCancelados = new ArrayList();
        listaFiliais = new ArrayList<>();
        listSemana = new ArrayList<>();
        listHorarios = new ArrayList<>();
        idFilial = 0;
        idSemana = 0;
        nrQuantidadeDisponivel = 0;
        nrQuantidadeCancelado = 0;
        nrQuantidadeCancelar = 0;
        nrQuantidadeDisponivelB = 0;
        nrQuantidadeCanceladoB = 0;
        nrQuantidadeCancelarB = 0;
        data = DataHoje.dataHoje();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        idHorariosDisponiveis = 0;
        idHorario = 0;
        desabilitaBotoes = false;
        desabilitaFilial = false;
        tipoCancelamento = "Dia";
        habilitaSemana = false;
        habilitaHorarios = false;
        new Tabbed().setTitle("1");
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("cancelarHorarioBean");
        GenericaSessao.remove("tabbedBean");
    }

    public void clear(Integer tCase) {
        if (tCase == 1) {
            listHorarios.clear();
            getListHorarios();
            calculaQuantidadeDisponivel();
        } else if (tCase == 2) {
            calculaQuantidadeDisponivel();
        } else {

        }
    }

    public void cancelarHorario(boolean todos) {

        if (!todos) {
            if (nrQuantidadeCancelar == 0) {
                GenericaMensagem.warn("Sistema", "Digite uma quantidade!");
                return;
            }
        }

        Dao dao = new Dao();
        CancelarHorarioDao db = new CancelarHorarioDao();
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
            GenericaMensagem.warn("Erro", "Erro ao cancelar horário(s)!");
            return;
        } else {
            dao.commit();
            GenericaMensagem.info("Sucesso", "Horário cancelado com sucesso.");
            getListaHorariosDisponiveis().clear();
        }
        nrQuantidadeDisponivel = 0;
        nrQuantidadeDisponivelB = 0;
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
            GenericaMensagem.warn("Sistema", "A data inicial tem que ser maior ou igual a data de hoje!");
            return;
        }

        if (intDataFinal < intDataHoje) {
            GenericaMensagem.warn("Sistema", "A data final tem que ser maior ou igual a data de hoje!");
            return;
        }

        if (intDataFinal < intDataInicial) {
            GenericaMensagem.warn("Sistema", "A data final tem que ser maior ou igual que a data inicial!");
            return;
        }

        Dao dao = new Dao();
        Filial f = (Filial) dao.find(new Filial(), Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()));
        HorariosDao horariosDao = new HorariosDao();
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
        CancelarHorarioDao cancelarHorarioDao = new CancelarHorarioDao();
        CancelarHorario ch;
        Usuario u = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        dao.openTransaction();
        for (int z = 0; z < listDatas.size(); z++) {
            horarioses.clear();
            cancelarHorario = new CancelarHorario();
            strDataInicial = listDatas.get(z).toString();
            if (!habilitaSemana && !habilitaHorarios) {
                horarioses = horariosDao.pesquisaTodosPorFilial(f.getId(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
            } else if (habilitaSemana && !habilitaHorarios) {
                if (listSemana.isEmpty()) {
                    return;
                }
                if (DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)) == Integer.parseInt(listSemana.get(idSemana).getDescription())) {
                    horarioses = horariosDao.pesquisaTodosPorFilial(f.getId(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
                }
            } else if (!habilitaSemana && habilitaHorarios) {
                if (listHorarios.isEmpty()) {
                    return;
                }
                horarioses = horariosDao.pesquisaPorHorarioFilial(f.getId(), listHorarios.get(idHorario).getDescription());
            } else if (habilitaSemana && habilitaHorarios) {
                if (DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)) == Integer.parseInt(listSemana.get(idSemana).getDescription())) {
                    if (listHorarios.isEmpty() || listSemana.isEmpty()) {
                        return;
                    }
                    horarioses = horariosDao.pesquisaPorHorarioFilial(f.getId(), listHorarios.get(idHorario).getDescription(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
                }
            }
            erro = false;
            for (int x = 0; x < horarioses.size(); x++) {
                if (habilitaHorarios && habilitaSemana) {
                    ch = cancelarHorarioDao.pesquisaCancelamentoHorarioSemana(DataHoje.converte(strDataInicial), horarioses.get(x).getId(), f.getId(), horarioses.get(x).getSemana().getId());
                } else {
                    ch = cancelarHorarioDao.pesquisaCancelamentoHorario(DataHoje.converte(strDataInicial), horarioses.get(x).getId(), f.getId());
                }
                cancelarHorario.setUsuario(u);
                if (ch.getId() == -1) {
                    cancelarHorario.setFilial(f);
                    cancelarHorario.setHorarios(horarioses.get(x));
                    cancelarHorario.setDtData(DataHoje.converte(strDataInicial));
                    if (horarioses.get(x).getQuantidade() > 0) {
                        if (habilitaHorarios && habilitaSemana) {
                            if (nrQuantidadeCancelarB == 0) {
                                cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                            } else {
                                int resto = horarioses.get(x).getQuantidade() - nrQuantidadeCancelarB;
                                if (resto == 0) {
                                    break;
                                } else if (resto < 1) {
                                    break;
                                } else {
                                    cancelarHorario.setQuantidade(resto);
                                }
                            }
                        } else {
                            cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                        }
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                    if (dao.save(cancelarHorario)) {
                        cancelarHorario = new CancelarHorario();
                        erro = false;
                    } else {
                        erro = true;
                        break;
                    }
                } else {
                    boolean delete = false;
                    cancelarHorario = ch;
                    if (horarioses.get(x).getQuantidade() > 0) {
                        if (habilitaHorarios && habilitaSemana) {
                            if (nrQuantidadeCancelarB == 0) {
                                cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                            } else {
                                int qtdeCancelada = horarioses.get(x).getQuantidade() - ch.getQuantidade();
                                int resto = qtdeCancelada - nrQuantidadeCancelarB;
                                if (resto == 0) {
                                    delete = true;
                                } else if (horarioses.get(x).getQuantidade() == nrQuantidadeCancelarB) {
                                    delete = true;
                                } else if (resto < 1) {
                                    delete = false;
                                    resto = horarioses.get(x).getQuantidade() - nrQuantidadeCancelarB;
                                    cancelarHorario.setQuantidade(resto);
                                } else {
                                    delete = false;
                                    cancelarHorario.setQuantidade(resto);
                                }
                            }
                        } else {
                            cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                        }
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                    if (!delete) {
                        if (dao.update(cancelarHorario)) {
                            cancelarHorario = new CancelarHorario();
                            erro = false;
                        } else {
                            erro = true;
                            break;
                        }
                    } else {
                        if (dao.delete(cancelarHorario)) {
                            cancelarHorario = new CancelarHorario();
                            erro = false;
                        } else {
                            erro = true;
                            break;
                        }
                    }
                    delete = false;
                }
            }
        }

        if (erro) {
            GenericaMensagem.warn("Erro", "Erro ao cancelar horário(s) do período!");
            dao.rollback();
            return;
        }
        dao.commit();
        listaHorariosCancelados.clear();
        cancelarHorario = new CancelarHorario();
        GenericaMensagem.info("Sucesso", "Horários cancelados com sucesso");
    }

    public void excluir(CancelarHorario ch) {
        Dao dao = new Dao();
        ch = (CancelarHorario) dao.find(new CancelarHorario(), ch.getId());
        if (ch != null) {
            if (ch.getId() != -1) {
                dao.openTransaction();
                if (dao.delete(ch)) {
                    dao.commit();
                    GenericaMensagem.info("Sucesso", "Registro excluído com sucesso.");
                } else {
                    dao.rollback();
                    GenericaMensagem.warn("Erro", "Erro ao excluir horário cancelado!");
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
        HorariosDao horariosDao = new HorariosDao();
        List<Horarios> select = horariosDao.listaTodosHorariosDisponiveisPorFilial(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, false);
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

    public Integer getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(Integer idFilial) {
        this.idFilial = idFilial;
    }

    public List<CancelarHorario> getListaHorariosCancelados() {
        listaHorariosCancelados.clear();
        switch (getTipoCancelamento()) {
            case "Dia": {
                CancelarHorarioDao cancelarHorarioDao = new CancelarHorarioDao();
                listaHorariosCancelados = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, null);
                break;
            }
            case "Período": {
                CancelarHorarioDao cancelarHorarioDao = new CancelarHorarioDao();
                listaHorariosCancelados = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal);
                break;
            }
        }
        return listaHorariosCancelados;
    }

    public void setListaHorariosCancelados(List<CancelarHorario> listaHorariosCancelados) {
        this.listaHorariosCancelados = listaHorariosCancelados;
    }

    public Integer getIdHorariosDisponiveis() {
        return idHorariosDisponiveis;
    }

    public void setIdHorariosDisponiveis(Integer idHorariosDisponiveisI) {
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
        nrQuantidadeCancelarB = 0;
        nrQuantidadeCanceladoB = 0;
        nrQuantidadeDisponivelB = 0;
        CancelarHorarioDao cancelarHorarioDao = new CancelarHorarioDao();
        int idHorariox = -1;
        if (getTipoCancelamento().equals("Dia")) {
            if (!getListaHorariosDisponiveis().isEmpty()) {
                idHorariox = Integer.parseInt(getListaHorariosDisponiveis().get(idHorariosDisponiveis).getDescription());
                Dao dao = new Dao();
                horarios = (Horarios) dao.find(new Horarios(), idHorariox);
            } else {
                horarios = new Horarios();
                idHorariosDisponiveis = 0;
            }
            if (horarios.getId() != -1) {
                CancelarHorario cancelarHorarioA = cancelarHorarioDao.pesquisaCancelamentoHorario(data, idHorariox, Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
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
        } else if (getTipoCancelamento().equals("Período")) {
            if (habilitaHorarios && habilitaSemana) {
                if (!getListaHorariosDisponiveis().isEmpty()) {
                    idHorariox = Integer.parseInt(getListaHorariosDisponiveis().get(idHorario).getDescription());
                    Dao dao = new Dao();
                    horarios = (Horarios) dao.find(new Horarios(), idHorariox);
                } else {
                    horarios = new Horarios();
                    idHorariosDisponiveis = 0;
                }
                if (horarios.getId() != -1) {
                    List<?> list = new HorariosDao().pesquisaPorHorarioFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), horarios.getHora(), Integer.parseInt(getListSemana().get(idSemana).getDescription()));
                    Horarios hx = ((List<Horarios>) list).get(0);
                    if (hx != null) {
                        nrQuantidadeDisponivelB = hx.getQuantidade();
                    } else {
                        nrQuantidadeDisponivelB = 0;
                    }
                }
            }
        }
    }

    public int calculaQuantidadeDisponivel(Integer quantidadeDisponivel, Integer quantidadeCancelada) {
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

    public void validaQuantidadeDisponivelB() {
        if (nrQuantidadeDisponivelB > 0) {
            if (nrQuantidadeCancelarB > nrQuantidadeDisponivelB) {
                nrQuantidadeCancelarB = nrQuantidadeDisponivelB;
            }
        }
    }

    public Integer getNrQuantidadeDisponivel() {
        return nrQuantidadeDisponivel;
    }

    public void setNrQuantidadeDisponivel(Integer nrQuantidadeDisponivel) {
        this.nrQuantidadeDisponivel = nrQuantidadeDisponivel;
    }

    public Horarios getHorarios() {
        return horarios;
    }

    public void setHorarios(Horarios horarios) {
        this.horarios = horarios;
    }

    public Integer getNrQuantidadeCancelado() {
        return nrQuantidadeCancelado;
    }

    public void setNrQuantidadeCancelado(Integer nrQuantidadeCancelado) {
        this.nrQuantidadeCancelado = nrQuantidadeCancelado;
    }

    public Integer getNrQuantidadeCancelar() {
        return nrQuantidadeCancelar;
    }

    public void setNrQuantidadeCancelar(Integer nrQuantidadeCancelar) {
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
                ((Tabbed) GenericaSessao.getObject("tabbedBean")).setActiveIndex("1");
                setTipoCancelamento("Dia");
                break;
            case "Período":
                setTipoCancelamento("Período");
                ((Tabbed) GenericaSessao.getObject("tabbedBean")).setActiveIndex("2");
                break;
            default:
                ((Tabbed) GenericaSessao.getObject("tabbedBean")).setActiveIndex("1");
                setTipoCancelamento("");
                break;
        }
        nrQuantidadeDisponivel = 0;
        nrQuantidadeCancelar = 0;
        nrQuantidadeCancelado = 0;
        nrQuantidadeDisponivelB = 0;
        nrQuantidadeCancelarB = 0;
        nrQuantidadeCanceladoB = 0;
        data = DataHoje.dataHoje();
        dataInicial = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        calculaQuantidadeDisponivel();
    }

    public void excluirCancelamentos() {
        CancelarHorarioDao cancelarHorarioDao = new CancelarHorarioDao();
        List<CancelarHorario> list = new ArrayList();
        switch (getTipoCancelamento()) {
            case "Dia":
                list = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), data, null);
                break;
            case "Período":
                if (!habilitaSemana && !habilitaHorarios) {
                    list = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal);
                } else if (habilitaSemana && !habilitaHorarios) {
                    if (listSemana.isEmpty()) {
                        return;
                    }
                    list = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, Integer.parseInt(listSemana.get(idSemana).getDescription()));
                } else if (!habilitaSemana && habilitaHorarios) {
                    list = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, listHorarios.get(idHorario).getDescription());
                } else if (habilitaSemana && habilitaHorarios) {
                    list = cancelarHorarioDao.listaTodosHorariosCancelados(Integer.parseInt(listaFiliais.get(this.idFilial).getDescription()), dataInicial, dataFinal, Integer.parseInt(listSemana.get(idSemana).getDescription()), listHorarios.get(idHorario).getDescription());
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
                    GenericaMensagem.warn("Erro", "Erro ao excluir horários!");
                } else {
                    dao.commit();
                    calculaQuantidadeDisponivel();
                    getListaHorariosCancelados().clear();
                    GenericaMensagem.info("Sucesso", "Horarios excluídos com sucesso.");
                }
            } catch (Exception e) {
                dao.rollback();
                GenericaMensagem.warn("Erro", "Erro ao excluir horários!");
            }
        } else {
            GenericaMensagem.warn("Sistema", "Não existem horários a serem excluídos para data / período!");
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
            HorariosDao horariosDao = new HorariosDao();
            List list;
            if (habilitaSemana) {
                list = horariosDao.listaHorariosAgrupadosPorFilialSemana(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), Integer.parseInt(getListSemana().get(idSemana).getDescription()));
            } else {
                list = horariosDao.listaHorariosAgrupadosPorFilialSemana(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), null);
            }
            idHorario = 0;
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

    public Integer getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Integer idHorario) {
        this.idHorario = idHorario;
    }

    public Integer getNrQuantidadeDisponivelB() {
        return nrQuantidadeDisponivelB;
    }

    public void setNrQuantidadeDisponivelB(Integer nrQuantidadeDisponivelB) {
        this.nrQuantidadeDisponivelB = nrQuantidadeDisponivelB;
    }

    public Integer getNrQuantidadeCanceladoB() {
        return nrQuantidadeCanceladoB;
    }

    public void setNrQuantidadeCanceladoB(Integer nrQuantidadeCanceladoB) {
        this.nrQuantidadeCanceladoB = nrQuantidadeCanceladoB;
    }

    public Integer getNrQuantidadeCancelarB() {
        return nrQuantidadeCancelarB;
    }

    public void setNrQuantidadeCancelarB(Integer nrQuantidadeCancelarB) {
        this.nrQuantidadeCancelarB = nrQuantidadeCancelarB;
    }
}
