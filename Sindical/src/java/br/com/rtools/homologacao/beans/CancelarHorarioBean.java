package br.com.rtools.homologacao.beans;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
import br.com.rtools.homologacao.db.CancelarHorarioDB;
import br.com.rtools.homologacao.db.CancelarHorarioDBToplink;
import br.com.rtools.homologacao.db.HorariosDB;
import br.com.rtools.homologacao.db.HorariosDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
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
    private List<SelectItem> listaFiliais = new ArrayList<SelectItem>();
    private int idFilial = 0;
    private int nrQuantidadeDisponivel = 0;
    private int nrQuantidadeCancelado = 0;
    private int nrQuantidadeCancelar = 0;
    private Date data = DataHoje.dataHoje();
    private Date dataFinal = DataHoje.dataHoje();
    private int idHorariosDisponiveis = 0;
    private boolean desabilitaBotoes = false;
    private boolean desabilitaFilial = false;
    private String tipoCancelamento = "Dia";

    public String cancelarHorario(boolean todos) {

        if (!todos) {
            if (nrQuantidadeCancelar == 0) {
                msgConfirma = "Digite uma quantidade!";
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
                return null;
            }
        }

        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        CancelarHorarioDB db = new CancelarHorarioDBToplink();
        CancelarHorario ch;
        boolean erro = false;
        acumuladoDB.abrirTransacao();
        for (int i = 0; i < getListaHorariosDisponiveis().size(); i++) {
            cancelarHorario.setFilial((Filial) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial"));
            if (todos) {
                cancelarHorario.setHorarios((Horarios) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaHorariosDisponiveis().get(i).getDescription()), "Horarios"));
                nrQuantidadeDisponivel = cancelarHorario.getHorarios().getQuantidade();
            } else {
                cancelarHorario.setHorarios((Horarios) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaHorariosDisponiveis().get(idHorariosDisponiveis).getDescription()), "Horarios"));
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
                if (acumuladoDB.inserirObjeto(cancelarHorario)) {
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
                if (acumuladoDB.alterarObjeto(cancelarHorario)) {
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
            acumuladoDB.desfazerTransacao();
            msgConfirma = "Erro ao cancelar horário(s)!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
            return null;
        } else {
            msgConfirma = "Horário cancelado com sucesso.";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
            getListaHorariosDisponiveis().clear();
            acumuladoDB.comitarTransacao();
        }
        nrQuantidadeDisponivel = 0;
        listaHorariosCancelados.clear();
        cancelarHorario = new CancelarHorario();
        calculaQuantidadeDisponivel();
        return null;
    }

    public String cancelarHorarioPeriodo() {
        Date date = DataHoje.dataHoje();
        int intDataHoje = DataHoje.converteDataParaInteger(DataHoje.converteData(date));
        int intDataInicial = DataHoje.converteDataParaInteger(DataHoje.converteData(data));
        int intDataFinal = DataHoje.converteDataParaInteger(DataHoje.converteData(dataFinal));
        String strDataInicial = DataHoje.converteData(data);
        String strDataFinal = DataHoje.converteData(dataFinal);

        if (intDataInicial < intDataHoje) {
            msgConfirma = "A data inicial tem que ser maior ou igual a data de hoje!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return null;
        }

        if (intDataFinal < intDataHoje) {
            msgConfirma = "A data final tem que ser maior ou igual a data de hoje!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return null;
        }

        if (intDataFinal < intDataInicial) {
            msgConfirma = "A data final tem que ser maior ou igual que a data inicial!";
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Sistema", msgConfirma));
            return null;
        }

        SalvarAcumuladoDB acumuladoDB = new SalvarAcumuladoDBToplink();
        Filial f = (Filial) acumuladoDB.pesquisaCodigo(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), "Filial");

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
        HorariosDB horariosDB = new HorariosDBToplink();
        List<Horarios> horarioses;
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        CancelarHorario ch;
        acumuladoDB.abrirTransacao();
        for (int z = 0; z < listDatas.size(); z++) {
            cancelarHorario = new CancelarHorario();
            strDataInicial = listDatas.get(z).toString();
            horarioses = horariosDB.pesquisaTodosPorFilial(f.getId(), DataHoje.diaDaSemana(DataHoje.converte(strDataInicial)));
            erro = false;
            for (int x = 0; x < horarioses.size(); x++) {
                ch = cancelarHorarioDB.pesquisaCancelamentoHorario(DataHoje.converte(strDataInicial), horarioses.get(x).getId(), f.getId());
                if (ch.getId() == -1) {
                    cancelarHorario.setFilial(f);
                    cancelarHorario.setHorarios(horarioses.get(x));
                    if (horarioses.get(x).getQuantidade() > 0) {
                        cancelarHorario.setQuantidade(horarioses.get(x).getQuantidade());
                    } else {
                        cancelarHorario.setQuantidade(0);
                    }
                    cancelarHorario.setDtData(DataHoje.converte(strDataInicial));
                    if (acumuladoDB.inserirObjeto(cancelarHorario)) {
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
                    if (acumuladoDB.alterarObjeto(cancelarHorario)) {
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
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));             
            acumuladoDB.desfazerTransacao();
            return null;
        }
        acumuladoDB.comitarTransacao();
        listaHorariosCancelados.clear();
        cancelarHorario = new CancelarHorario();
        msgConfirma = "Horários cancelados com sucesso";
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));  
        return null;
    }

    public void excluir(CancelarHorario ch) {
        SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
        ch = (CancelarHorario) dB.pesquisaCodigo(ch.getId(), "CancelarHorario");
        if (ch != null) {
            if (ch.getId() != -1) {
                dB.abrirTransacao();
                if (dB.deletarObjeto(ch)) {
                    dB.comitarTransacao();
                    msgConfirma = "Registro excluído com sucesso.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                } else {
                    dB.desfazerTransacao();
                    msgConfirma = "Erro ao excluir horário cancelado!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));                    
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
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();            
            List<Filial> select = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
            for (int i = 0; i < select.size(); i++) {
                if (filial.getId() != -1) {
                    if (filial.getId() == select.get(i).getFilial().getId()) {
                        listaFiliais.add(
                                new SelectItem(
                                new Integer(i),
                                select.get(i).getFilial().getPessoa().getDocumento() + " / " + select.get(i).getFilial().getPessoa().getNome(),
                                Integer.toString(select.get(i).getId())));
                    }
                } else {
                    listaFiliais.add(
                            new SelectItem(
                            new Integer(i),
                            select.get(i).getFilial().getPessoa().getDocumento() + " / " + select.get(i).getFilial().getPessoa().getNome(),
                            Integer.toString(select.get(i).getId())));
                }
            }
        }
        return listaFiliais;
    }

    public void setListaFiliais(List<SelectItem> listaFiliais) {
        this.listaFiliais = listaFiliais;
    }
    
    public List<SelectItem> getListaHorariosDisponiveis() {
        getListaHorariosCancelados();
        List<SelectItem> result = new ArrayList<SelectItem>();
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        List<Horarios> select = cancelarHorarioDB.listaTodosHorariosDisponiveisPorFilial(Integer.parseInt(listaFiliais.get(idFilial).getDescription()), data, false);
        if (select.isEmpty()) {
            desabilitaBotoes = true;
            idHorariosDisponiveis = 0;
        } else {
            desabilitaBotoes = false;
            for (int i = 0; i < select.size(); i++) {
                result.add(
                        new SelectItem(
                        new Integer(i),
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
        if (getTipoCancelamento().equals("Dia")) {
            CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
            listaHorariosCancelados = cancelarHorarioDB.listaTodosHorariosCanceladosPorFilial(Integer.parseInt(listaFiliais.get(idFilial).getDescription()), data, null);
        } else if (getTipoCancelamento().equals("Período")) {
            CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
            listaHorariosCancelados = cancelarHorarioDB.listaTodosHorariosCanceladosPorFilial(Integer.parseInt(listaFiliais.get(idFilial).getDescription()), data, dataFinal);
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

    public void calculaQuantidadeDisponivel() {
        nrQuantidadeCancelar = 0;
        nrQuantidadeCancelado = 0;
        nrQuantidadeDisponivel = 0;
        if (getTipoCancelamento().equals("Dia")) {
            CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
            int idHorario = -1;
            if (!getListaHorariosDisponiveis().isEmpty()) {
                idHorario = Integer.parseInt(getListaHorariosDisponiveis().get(idHorariosDisponiveis).getDescription());
                SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
                horarios = (Horarios) dB.pesquisaCodigo(idHorario, "Horarios");
            } else {
                horarios = new Horarios();
                idHorariosDisponiveis = 0;
            }
            if (horarios.getId() != -1) {
                CancelarHorario cancelarHorarioA = cancelarHorarioDB.pesquisaCancelamentoHorario(data, idHorario, Integer.parseInt(getListaFiliais().get(idFilial).getDescription()));
                if (cancelarHorarioA != null) {
                    if (horarios.getQuantidade() > 0) {
                        if(cancelarHorarioA.getQuantidade() > horarios.getQuantidade()) {
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
            if(quantidadeCancelada > quantidadeDisponivel) {
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
        if (event.getTab().getTitle().equals("Dia")) {
            setTipoCancelamento("Dia");
        } else if (event.getTab().getTitle().equals("Período")) {
            setTipoCancelamento("Período");            
        } else {
            setTipoCancelamento("");
        }
        data = DataHoje.dataHoje();
        dataFinal = DataHoje.dataHoje();
        calculaQuantidadeDisponivel();
    }

    public void excluirCancelamentos() {
        CancelarHorarioDB cancelarHorarioDB = new CancelarHorarioDBToplink();
        List<CancelarHorario> list;
        if (getTipoCancelamento().equals("Dia")) {
            list = cancelarHorarioDB.listaTodosHorariosCanceladosPorFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), data, null);
        } else if (getTipoCancelamento().equals("Período")) {
            list = cancelarHorarioDB.listaTodosHorariosCanceladosPorFilial(Integer.parseInt(getListaFiliais().get(idFilial).getDescription()), data, dataFinal);
        } else {
            return;
        }
        if (!list.isEmpty()) {
            boolean erro = false;
            SalvarAcumuladoDB dB = new SalvarAcumuladoDBToplink();
            try {
                dB.abrirTransacao();
                for (int i = 0; i < list.size(); i++) {
                    CancelarHorario ch = (CancelarHorario) dB.pesquisaObjeto(list.get(i).getId(), "CancelarHorario");
                    if(ch != null) {
                        if (!dB.deletarObjeto(ch)) {
                            erro = true;
                            break;
                        }
                    }
                }
                if (erro) {
                    dB.desfazerTransacao();
                    msgConfirma = "Erro ao excluir horários!";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Erro", msgConfirma));
                } else {
                    dB.comitarTransacao();
                    calculaQuantidadeDisponivel();
                    getListaHorariosCancelados().clear();
                    msgConfirma = "Horarios excluídos com sucesso.";
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", msgConfirma));
                }
            } catch (Exception e) {
                dB.desfazerTransacao();
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
}