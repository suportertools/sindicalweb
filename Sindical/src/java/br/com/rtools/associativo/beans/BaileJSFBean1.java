package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.seguranca.db.UsuarioDB;
import br.com.rtools.seguranca.db.UsuarioDBToplink;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

public class BaileJSFBean1 {
    private EventoBaile eventoBaile = new EventoBaile();
    private EventoBanda eventoBanda = new EventoBanda();
    private EventoServico eventoServico = new EventoServico();
    private EventoServicoValor eventoServicoValor = new EventoServicoValor();
    private AEvento evento = new AEvento();
    private AEndereco endereco = new AEndereco();
    private int idIndex = -1;
    private int idIndexBanda = -1;
    private int idIndexServico = -1;
    private int idBanda = 0;
    private int idDescricaoEvento = 0;
    private int idServicos = 0;
    private List<EventoBanda> listaEventoBanda = new ArrayList();
    private List<EventoBaile> listaEventoBaile = new ArrayList();
    private List<EventoServicoValor> listaEventoServicoValor = new ArrayList();
    private List<SelectItem> listaComboBanda = new ArrayList();
    private List<SelectItem> listaComboServicos = new ArrayList();
    private List<SelectItem> listaComboDescricaoEvento = new ArrayList();
    private String msgConfirma = "";
    private String comoPesquisa = "I";
    private String descPesquisa = "";
    private boolean limpar = false;
    DataHoje dataHoje = new DataHoje();

    public EventoBaile getEventoBaile() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa") != null) {
            endereco.setEndereco((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("enderecoPesquisa"));
            endereco.setEndereco((Endereco) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("enderecoPesquisa"));
        }
        return eventoBaile;
    }

    public void setEventoBaile(EventoBaile eventoBaile) {
        this.eventoBaile = eventoBaile;
    }

    public String novo() {
        evento = new AEvento();
        endereco = new AEndereco();
        eventoBaile = new EventoBaile();
        eventoBanda = new EventoBanda();
        eventoServico = new EventoServico();
        listaEventoBanda.clear();
        listaEventoServicoValor.clear();
        idIndex = -1;
        idIndexBanda = -1;
        idIndexServico = -1;
        idBanda = 0;
        idServicos = 0;
        idDescricaoEvento = 0;
        limpar = false;
        msgConfirma = "";
        return "baile";
    }

    public void atualizaListaEventoBanda() {
        eventoBanda = new EventoBanda();
        getListaEventoBanda();
    }

    public void limpar() {
        if (isLimpar() == true) {
            novo();
        }
    }

    public String salvar() {
        if (eventoBaile.getDataString().equals("")) {
            msgConfirma = "Informar data do evento!";
            return null;
        }
        if (eventoBaile.getHoraInicio().equals("")) {
            msgConfirma = "Necessário preencher a hora inicial do evento!";
            return null;
        }
        if (eventoBaile.getHoraFim().equals("")) {
            msgConfirma = "Necessário preencher a hora final do evento!";
            return null;
        }
        if (eventoBaile.getQuantidadeMesas() <= 0) {
            msgConfirma = "Necessário informar a quantidade de mesas!";
            return null;
        }
        if (endereco.getEndereco().getId() == -1) {
            msgConfirma = "Pesquise um endereço!";
            return null;
        }
        if (endereco.getNumero().equals("")) {
            msgConfirma = "Informar o número do endereço!";
            return null;
        }
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        evento.setDescricaoEvento(((DescricaoEvento) sv.pesquisaCodigo(Integer.parseInt(listaComboDescricaoEvento.get(idDescricaoEvento).getDescription()), "DescricaoEvento")));
        if (evento.getId() == -1) {
            if (!dataHoje.maiorData(eventoBaile.getDataString(), DataHoje.converteData(dataHoje.dataHoje()))) {
                msgConfirma = "Data do evento deve superir a data de hoje!";
                return null;
            }
            sv.abrirTransacao();
            if (sv.inserirObjeto(evento)) {
                eventoBaile.setEvento(evento);
                endereco.setEvento(evento);
                if (sv.inserirObjeto(eventoBaile)) {
                    if (sv.inserirObjeto(endereco)) {
                        msgConfirma = "Registro inserido com sucesso";
                        sv.comitarTransacao();
                        return null;
                    } else {
                        msgConfirma = "Falha ao inserir o endereço!";
                    }
                } else {
                    msgConfirma = "Falha ao inserir Evento Baile!";
                }
            }
            msgConfirma = "Falha ao inserir Evento!";
            sv.desfazerTransacao();
        } else {
            sv.abrirTransacao();
            if (sv.alterarObjeto(evento)) {
                if (sv.alterarObjeto(eventoBaile)) {
                    if (sv.alterarObjeto(eventoBaile)) {
                        if (sv.alterarObjeto(endereco)) {
                            msgConfirma = "Registro atualizado com sucesso!";
                            sv.comitarTransacao();
                            return null;
                        } else {
                            msgConfirma = "Falha ao atualizar o Endereço!";
                        }
                    } else {
                        msgConfirma = "Falha ao atualizar o Evento Serviço!";
                    }
                } else {
                    msgConfirma = "Falha ao atualizar Evento Baile";
                }
            }
            msgConfirma = "Falha ao atualizar Evento";
            sv.desfazerTransacao();
        }
        return null;
    }

    public String editar() {
        eventoBaile = (EventoBaile) listaEventoBaile.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("eventoBandaPesquisa", eventoBaile);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisa = "";
        EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
        evento = eventoBaile.getEvento();
        endereco = eventoBaileDB.pesquisaEnderecoEvento(eventoBaile.getEvento().getId());
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public String excluir() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        for (int i = 0; i < listaEventoBanda.size(); i++) {
            if (!excluirBanda(listaEventoBanda.get(i).getId(), sv)) {
                msgConfirma = "Banda do Evento não podem ser excluídas!";
                sv.desfazerTransacao();
                return null;
            }
        }
        for (int i = 0; i < listaEventoServicoValor.size(); i++) {
            if (!excluirEventoServico(listaEventoServicoValor.get(i).getId(), sv)) {
                msgConfirma = "Serviços de Valores não podem ser excluídos!";
                sv.desfazerTransacao();
                return null;
            }
        }
        endereco = (AEndereco) sv.pesquisaCodigo(endereco.getId(), "AEndereco");
        if (!sv.deletarObjeto(endereco)) {
            msgConfirma = "Endereço Baile não pode ser excluído!";
            sv.desfazerTransacao();
            return null;
        }
        eventoBaile = (EventoBaile) sv.pesquisaCodigo(eventoBaile.getId(), "EventoBaile");
        if (!sv.deletarObjeto(eventoBaile)) {
            msgConfirma = "Evento Baile não pode ser excluído! ";
            sv.desfazerTransacao();
            return null;
        }
        evento = (AEvento) sv.pesquisaCodigo(eventoBaile.getEvento().getId(), "AEvento");
        if (!sv.deletarObjeto(evento)) {
            msgConfirma = "Evento não pode ser excluído! ";
            sv.desfazerTransacao();
            return null;
        }
        msgConfirma = "Evento excluído com sucesso.";
        sv.comitarTransacao();
        limpar = true;
        return null;
    }

    public String adicionarServico() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        eventoServico.setServicos(((Servicos) sv.pesquisaCodigo(Integer.parseInt(listaComboServicos.get(idServicos).getDescription()), "Servicos")));
        if (Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == 0
                || Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == -1) {
            msgConfirma = "Escolha um serviço válido!";
            return null;
        }
        if (eventoServicoValor.getValor() == 0) {
            msgConfirma = "Informar o valor do serviço!";
            return null;
        }
        if (eventoServicoValor.getIdadeInicial() == 0) {
            msgConfirma = "Informar a idade inicial!";
            return null;
        }
        if (eventoServicoValor.getIdadeFinal() == 0) {
            msgConfirma = "Informar a idade final!";
            return null;
        }
        if (eventoServicoValor.getIdadeFinal() < eventoServicoValor.getIdadeInicial()) {
            msgConfirma = "Idade final deve ser maior ou igual a idade inicial!";
            return null;
        }
        eventoServicoValor.setId(-1);
        if (eventoServicoValor.getId() == -1) {
            sv.abrirTransacao();
            eventoServico.setaEvento(evento);
            if (sv.inserirObjeto(eventoServico)) {
                eventoServicoValor.setEventoServico(eventoServico);
                if (sv.inserirObjeto(eventoServicoValor)) {
                    msgConfirma = "Serviço adicionado com sucesso";
                    listaEventoServicoValor.clear();
                    eventoServicoValor = new EventoServicoValor();
                    eventoServico = new EventoServico();
                    sv.comitarTransacao();
                } else {
                    msgConfirma = "Serviço Valor não pode ser adicionado!";
                    sv.desfazerTransacao();
                }
            } else {
                msgConfirma = "Serviço não pode ser adicionado!";
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String removerEventoServico() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (excluirEventoServico(listaEventoServicoValor.get(idIndexServico).getId(), sv)) {
            listaEventoServicoValor.clear();
            eventoServicoValor = new EventoServicoValor();
            eventoServico = new EventoServico();
            msgConfirma = "Serviço removida com sucesso";
            sv.comitarTransacao();
        } else {
            msgConfirma = "Serviço não pode ser removido!";
            sv.desfazerTransacao();
        }
        return null;
    }

    public boolean excluirEventoServico(int id, SalvarAcumuladoDB sv) {
        eventoServicoValor = (EventoServicoValor) sv.pesquisaCodigo(id, "EventoServicoValor");
        if (eventoServicoValor.getId() != -1) {
            if (sv.deletarObjeto(eventoServicoValor)) {
                eventoServico = (EventoServico) sv.pesquisaCodigo(eventoServicoValor.getEventoServico().getId(), "EventoServico");
                if (sv.deletarObjeto(eventoServico)) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    public String adicionarBanda() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        EventoBandaDB db = new EventoBandaDBToplink();
        Banda banda = new Banda();
        banda = (Banda) sv.pesquisaCodigo(Integer.parseInt(listaComboBanda.get(idBanda).getDescription()), "Banda");
        for (int i = 0; i < listaEventoBanda.size(); i++) {
            if (listaEventoBanda.get(i).getBanda().getId() == banda.getId()) {
                msgConfirma = "Banda já cadastrada para esse Evento";
                return null;
            }
        }
        eventoBanda.setId(-1);
        eventoBanda.setBanda(banda);
        if (eventoBanda.getId() == -1) {
            eventoBanda.setEvento(evento);
            sv.abrirTransacao();
            if (sv.inserirObjeto(eventoBanda)) {
                msgConfirma = "Banda adicionada com sucesso!";
                listaEventoBanda.clear();
                sv.comitarTransacao();
            } else {
                msgConfirma = "Banda não pode ser adicionada!";
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String removerBanda() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (excluirBanda(listaEventoBanda.get(idIndexBanda).getId(), sv)) {
            msgConfirma = "Banda removida com sucesso!";
            listaEventoBanda.clear();
            sv.comitarTransacao();
        } else {
            msgConfirma = "Serviço não pode ser removido!";
            sv.desfazerTransacao();
        }
        return null;
    }

    public boolean excluirBanda(int id, SalvarAcumuladoDB sv) {
        eventoBanda = (EventoBanda) sv.pesquisaCodigo(id, "EventoBanda");
        if (eventoBanda.getId() != -1) {
            if (sv.deletarObjeto(eventoBanda)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public EventoServico getEventoServico() {
        return eventoServico;
    }

    public void setEventoServico(EventoServico eventoServico) {
        this.eventoServico = eventoServico;
    }

    public EventoServicoValor getEventoServicoValor() {
        return eventoServicoValor;
    }

    public void setEventoServicoValor(EventoServicoValor eventoServicoValor) {
        this.eventoServicoValor = eventoServicoValor;
    }

    public AEvento getEvento() {
        return evento;
    }

    public void setEvento(AEvento evento) {
        this.evento = evento;
    }

    public AEndereco getEndereco() {
        return endereco;
    }

    public void setEndereco(AEndereco endereco) {
        this.endereco = endereco;
    }

    public int getIdIndex() {
        return idIndex;
    }

    public void setIdIndex(int idIndex) {
        this.idIndex = idIndex;
    }

    public int getIdBanda() {
        return idBanda;
    }

    public void setIdBanda(int idBanda) {
        this.idBanda = idBanda;
    }

    public int getIdDescricaoEvento() {
        return idDescricaoEvento;
    }

    public void setIdDescricaoEvento(int idDescricaoEvento) {
        this.idDescricaoEvento = idDescricaoEvento;
    }

    public int getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(int idServicos) {
        this.idServicos = idServicos;
    }

    public List<EventoBanda> getListaEventoBanda() {
        if (listaEventoBanda.isEmpty()) {
            EventoBandaDB db = new EventoBandaDBToplink();
            listaEventoBanda = db.pesquisaBandasDoEvento(eventoBaile.getEvento().getId());
        }
        return listaEventoBanda;
    }

    public void setListaBanda(List<EventoBanda> listaEventoBanda) {
        this.setListaEventoBanda(listaEventoBanda);
    }

    public List<SelectItem> getListaComboBanda() {
        if (listaComboBanda.isEmpty()) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            List<Banda> select = new ArrayList<Banda>();
            select = sv.listaObjeto("Banda");
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaComboBanda.add(new SelectItem(new Integer(i), select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
                }
            }
        }
        return listaComboBanda;
    }

    public void setListaComboBanda(List<SelectItem> listaComboBanda) {
        this.setListaComboBanda(listaComboBanda);
    }

    public List<SelectItem> getListaComboServicos() {
        int i = 0, j = 0;
        listaComboServicos.clear();
        ServicosDB db = new ServicosDBToplink();
        List<Servicos> select = db.pesquisaTodos(139);
        if (!select.isEmpty()) {
            while (i < select.size()) {
                if ((!eventoServico.isMesa() && (select.get(i).getId() == 12) || (select.get(i).getId() == 13))
                        || (eventoServico.isMesa()) && ((select.get(i).getId() == 14) || (select.get(i).getId() == 15))) {
                    listaComboServicos.add(new SelectItem(
                            new Integer(j),
                            (String) (select.get(i)).getDescricao(),
                            Integer.toString((select.get(i)).getId())));
                    j++;
                }
                i++;
            }
        }
        return listaComboServicos;
    }

    public void setListaComboServicos(List<SelectItem> listaComboServicos) {
        this.setListaComboServicos(listaComboServicos);
    }

    public List<SelectItem> getListaComboDescricaoEvento() {
        if (listaComboDescricaoEvento.isEmpty()) {
            List<DescricaoEvento> select = new ArrayList();
            DescricaoEventoDB db = new DescricaoEventoDBToplink();
            select = db.pesquisaDescricaoPorGrupo(1);
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaComboDescricaoEvento.add(new SelectItem(new Integer(i), select.get(i).getDescricao(), Integer.toString(select.get(i).getId())));
                }
            }
        }
        return listaComboDescricaoEvento;
    }

    public void setListaComboDescricaoEvento(List<SelectItem> listaComboDescricaoEvento) {
        this.setListaComboDescricaoEvento(listaComboDescricaoEvento);
    }

    public String getMsgConfirma() {
        return msgConfirma;
    }

    public void setMsgConfirma(String msgConfirma) {
        this.msgConfirma = msgConfirma;
    }

    public String getComoPesquisa() {
        return comoPesquisa;
    }

    public void setComoPesquisa(String comoPesquisa) {
        this.comoPesquisa = comoPesquisa;
    }

    public String getDescPesquisa() {
        return descPesquisa;
    }

    public void setDescPesquisa(String descPesquisa) {
        this.descPesquisa = descPesquisa;
    }

    public boolean isLimpar() {
        return limpar;
    }

    public void setLimpar(boolean limpar) {
        this.limpar = limpar;
    }

    public void setListaEventoBanda(List<EventoBanda> listaEventoBanda) {
        this.setListaEventoBanda(listaEventoBanda);
    }

    public List<EventoBaile> getListaEventoBaile() {
        EventoBaileDB db = new EventoBaileDBToplink();
        listaEventoBaile = db.pesquisaEventoDescricao(descPesquisa, comoPesquisa);
        return listaEventoBaile;
    }

    public void acaoPesquisaInicial() {
        comoPesquisa = "I";
    }

    public void acaoPesquisaParcial() {
        comoPesquisa = "P";
    }

    public void setListaEventoBaile(List<EventoBaile> listaEventoBaile) {
        this.listaEventoBaile = listaEventoBaile;
    }

    public List<EventoServicoValor> getListaEventoServicoValor() {
        if (listaEventoServicoValor.isEmpty()) {
            EventoServicoValorDB eventoServicoValorDB = new EventoServicoValorDBToplink();
            listaEventoServicoValor = eventoServicoValorDB.pesquisaServicoValorPorEvento(evento.getId());
        }
        return listaEventoServicoValor;
    }

    public void setListaEventoServicoValor(List<EventoServicoValor> listaEventoServicoValor) {
        this.listaEventoServicoValor = listaEventoServicoValor;
    }

    public EventoBanda getEventoBanda() {
        return eventoBanda;
    }

    public void setEventoBanda(EventoBanda eventoBanda) {
        this.eventoBanda = eventoBanda;
    }

    public int getIdIndexBanda() {
        return idIndexBanda;
    }

    public void setIdIndexBanda(int idIndexBanda) {
        this.idIndexBanda = idIndexBanda;
    }

    public int getIdIndexServico() {
        return idIndexServico;
    }

    public void setIdIndexServico(int idIndexServico) {
        this.idIndexServico = idIndexServico;
    }
}
