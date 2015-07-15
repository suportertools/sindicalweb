package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.*;
import br.com.rtools.associativo.dao.BaileDao;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.event.DragDropEvent;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

@ManagedBean
@SessionScoped
public class BaileBean implements Serializable {

    private EventoBaile eventoBaile = new EventoBaile();
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
    private int nrMesa = 0;
    private int idNrMesa = 0;
    private int idNrConvite = 0;
    private List<Integer> listaQuantidade = new ArrayList();
    private List<EventoBanda> listaEventoBanda = new ArrayList();
    private List<EventoBaile> listaEventoBaile = new ArrayList();
    private List<EventoServicoValor> listaEventoServicoValor = new ArrayList();
    private List<SelectItem> listaComboBanda = new ArrayList();
    private List<SelectItem> listaComboServicos = new ArrayList();
    private List<SelectItem> listaComboDescricaoEvento = new ArrayList();
    private List<SelectItem> listaMesasDisponiveis = new ArrayList();
    private List<SelectItem> listaConvitesDisponiveis = new ArrayList();
    private String msgConfirma = "";
    private String comoPesquisa = "I";
    private String descPesquisa = "";
    private String mesaTop = "";
    private String mesaLeft = "";
    private boolean limpar = false;
    DataHoje dataHoje = new DataHoje();
    private List<EventoBaileMapa> listaMesas = new ArrayList();
    private List<EventoBaileConvite> listaConvites = new ArrayList();
    private EventoBaileMapa ebmSelecionado = new EventoBaileMapa();
    private int idCategoria = 0;
    private List<SelectItem> listaCategoria = new ArrayList();

    private boolean visibleMapa = false;
    private Servicos servicos = new Servicos();

    @PostConstruct
    public void init() {
        loadListaServicos();
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("baileBean");
    }

    public void updateTela() {
        loadListaServicos();
        updateServicoCategoria();
    }

    public void updateServicoCategoria() {
        servicos = (Servicos) new Dao().find(new Servicos(), Integer.valueOf(listaComboServicos.get(idServicos).getDescription()));
        if (servicos.getId() == 13 || servicos.getId() == 15) {
            eventoServicoValor.setValor(0);
            eventoServicoValor.setIdadeInicial(0);
            eventoServicoValor.setIdadeFinal(150);
            eventoServicoValor.setSexo("A");
            eventoServico.setSocio(false);
        }
        loadCategoria();
    }

    public void loadListaServicos() {
        listaComboServicos.clear();
        List<Servicos> select = new ArrayList();
        if (eventoServico.isMesa()) {
            select.add((Servicos) new Dao().find(new Servicos(), 12));
            select.add((Servicos) new Dao().find(new Servicos(), 13));
        } else {
            select.add((Servicos) new Dao().find(new Servicos(), 14));
            select.add((Servicos) new Dao().find(new Servicos(), 15));
        }

        if (!select.isEmpty()) {
            for (int i = 0; i < select.size(); i++) {
                listaComboServicos.add(
                        new SelectItem(
                                i,
                                select.get(i).getDescricao(),
                                Integer.toString((select.get(i)).getId())
                        )
                );
            }
            servicos = select.get(0);
        }
        loadCategoria();
    }

    public void loadCategoria() {
        listaCategoria.clear();
        List<Categoria> result;
        if (eventoBaile.getId() == -1) {
            result = new Dao().list(new Categoria());
        } else {
            result = new BaileDao().listaCategoriaPorEventoServico(
                    Integer.valueOf(listaComboServicos.get(idServicos).getDescription()),
                    eventoServicoValor.getSexo(),
                    eventoServicoValor.getIdadeInicial(),
                    eventoServicoValor.getIdadeFinal(),
                    eventoBaile.getEvento().getId()
            );
        }

        for (int i = 0; i < result.size(); i++) {
            listaCategoria.add(new SelectItem(i, result.get(i).getCategoria(), "" + result.get(i).getId()));
        }
    }

    public void excluirMesa(EventoBaileMapa ebm) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find("EventoBaileMapa", ebm.getId()))) {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro!", "Não foi possível excluir esta mesa!");
        } else {
            GenericaMensagem.info("Sucesso!", "Mesa excluída com sucesso!");
            sv.comitarTransacao();
        }
        listaMesas.clear();
    }

    public void excluirConvite(EventoBaileConvite ebc) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!sv.deletarObjeto(sv.find("EventoBaileConvite", ebc.getId()))) {
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro!", "Não foi possível excluir este Convite!");
        } else {
            GenericaMensagem.info("Sucesso!", "Convite excluída com sucesso!");
            sv.comitarTransacao();
        }
        listaConvites.clear();
    }

    public void salvarMesa(boolean all) {
        if (eventoBaile.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            sv.abrirTransacao();
            if (all) {
                boolean err = false;
                for (SelectItem listaMesasDisponivei : listaMesasDisponiveis) {
                    if (((EventoBaileMapa) eventoBaileDB.pesquisaMesaBaile(eventoBaile.getId(), Integer.parseInt(listaMesasDisponivei.getDescription()))).getId() == -1) {
                        EventoBaileMapa ebm = new EventoBaileMapa(
                                -1,
                                eventoBaile,
                                Integer.parseInt(listaMesasDisponivei.getDescription()),
                                "",
                                null,
                                (AStatus) sv.pesquisaCodigo(1, "AStatus"),
                                null
                        );
                        if (!sv.inserirObjeto(ebm)) {
                            err = true;
                            break;
                        }
                    }
                }
                if (err) {
                    sv.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Não foi possível adicionar Mesas!");
                } else {
                    sv.comitarTransacao();
                    idNrMesa = 0;
                    nrMesa = 0;
                    listaMesasDisponiveis.clear();
                    listaMesas.clear();

                    GenericaMensagem.info("Sucesso", "Mesas Adicionadas!");
                }
            }
        }
    }

    public void salvarConvite(boolean all) {
        if (eventoBaile.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            sv.abrirTransacao();
            if (all) {
                boolean err = false;
                for (SelectItem conviteDisponivel : listaConvitesDisponiveis) {
                    if (((EventoBaileConvite) eventoBaileDB.pesquisaConviteBaile(eventoBaile.getId(), Integer.parseInt(conviteDisponivel.getDescription()))).getId() == -1) {
                        EventoBaileConvite ebc = new EventoBaileConvite(
                                -1,
                                eventoBaile,
                                Integer.parseInt(conviteDisponivel.getDescription()),
                                null,
                                (AStatus) sv.pesquisaCodigo(1, "AStatus"),
                                null
                        );
                        if (!sv.inserirObjeto(ebc)) {
                            err = true;
                            break;
                        }
                    }
                }
                if (err) {
                    sv.desfazerTransacao();
                    GenericaMensagem.error("Erro", "Não foi possível adicionar Convite!");
                } else {
                    sv.comitarTransacao();
                    idNrConvite = 0;
                    listaConvitesDisponiveis.clear();
                    listaConvites.clear();

                    GenericaMensagem.info("Sucesso", "Convites Adicionadas!");
                }
            }
        }
    }

    public List<EventoBaileMapa> getListaMesas() {
        if (listaMesas.isEmpty()) {
            if (eventoBaile.getId() != -1) {
                EventoBaileDB db = new EventoBaileDBToplink();
                listaMesas = db.listaBaileMapa(eventoBaile.getId());
            }
        }
        return listaMesas;
    }

    public void setListaMesas(List<EventoBaileMapa> listaMesas) {
        this.listaMesas = listaMesas;
    }

    public List<EventoBaileConvite> getListaConvites() {
        if (listaConvites.isEmpty()) {
            if (eventoBaile.getId() != -1) {
                EventoBaileDB db = new EventoBaileDBToplink();
                listaConvites = db.listaBaileConvite(eventoBaile.getId());
            }
        }
        return listaConvites;
    }

    public void setListaConvites(List<EventoBaileConvite> listaConvites) {
        this.listaConvites = listaConvites;
    }

    public void removerEndereco() {
        if (endereco.getId() == -1) {
            endereco = new AEndereco();
        } else {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            if (!sv.deletarObjeto(sv.find("AEndereco", endereco.getId()))) {
                GenericaMensagem.warn("Erro", "Não foi possível excluir este endereço!");
                sv.desfazerTransacao();
                return;
            }
            endereco = new AEndereco();
            sv.comitarTransacao();
        }
    }

    public EventoBaile getEventoBaile() {
        if (GenericaSessao.exists("enderecoPesquisa")) {
            endereco.setEndereco((Endereco) GenericaSessao.getObject("enderecoPesquisa", true));
        }
        return eventoBaile;
    }

    public void setEventoBaile(EventoBaile eventoBaile) {
        this.eventoBaile = eventoBaile;
    }

    public String novo() {
        GenericaSessao.remove("baileBean");
        return "baile";
    }

    public void atualizaListaEventoBanda() {
        getListaEventoBanda();
    }

    public void limpar() {
        if (isLimpar() == true) {
            novo();
        }
    }

    public boolean validaSalvar() {
        if (eventoBaile.getDataString().equals("")) {
            msgConfirma = "Informar data do evento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }

        if (eventoBaile.getHoraInicio().equals("")) {
            msgConfirma = "Necessário preencher a hora inicial do evento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }

        if (eventoBaile.getHoraFim().equals("")) {
            msgConfirma = "Necessário preencher a hora final do evento!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }
        if (eventoBaile.getQuantidadeMesas() <= 0) {
            msgConfirma = "Necessário informar a quantidade de mesas!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }
        if (endereco.getEndereco().getId() == -1) {
            msgConfirma = "Pesquise um endereço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }
        if (endereco.getNumero().equals("")) {
            msgConfirma = "Informar o número do endereço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return false;
        }
        return true;
    }

    public String salvar() {
        if (!validaSalvar()) {
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        evento.setDescricaoEvento(((DescricaoEvento) sv.find("DescricaoEvento", Integer.parseInt(listaComboDescricaoEvento.get(idDescricaoEvento).getDescription()))));
        sv.abrirTransacao();

        if (evento.getId() == -1) {
            if (!DataHoje.maiorData(eventoBaile.getDataString(), DataHoje.converteData(DataHoje.dataHoje()))) {
                msgConfirma = "Data do evento deve ser superior a data de hoje!";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            if (!sv.inserirObjeto(evento)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao inserir Evento!";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            Evt evt = new Evt();
            if (!sv.inserirObjeto(evt)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao inserir EVT!";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            eventoBaile.setEvt(evt);
            eventoBaile.setEvento(evento);
            endereco.setEvento(evento);

            if (!sv.inserirObjeto(eventoBaile)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao inserir Evento Baile!";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            if (!sv.inserirObjeto(endereco)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao inserir o endereço!";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            listaMesas.clear();
            msgConfirma = "Registro inserido com sucesso";
            GenericaMensagem.info("Sucesso", msgConfirma);
            sv.comitarTransacao();
        } else {
            if (!sv.alterarObjeto(evento)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao atualizar Evento";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            if (eventoBaile.getEvt() == null) {
                Evt evt = new Evt();
                if (!sv.inserirObjeto(evt)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao inserir EVT!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }
                eventoBaile.setEvt(evt);
            }

            if (!sv.alterarObjeto(eventoBaile)) {
                sv.desfazerTransacao();
                msgConfirma = "Falha ao atualizar Evento Baile";
                GenericaMensagem.warn("Erro", msgConfirma);
                return null;
            }

            if (endereco.getId() != -1) {
                if (!sv.alterarObjeto(endereco)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao atualizar o Endereço!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }
            } else {
                endereco.setEvento(evento);
                if (!sv.inserirObjeto(endereco)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao atualizar o Endereço!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }
            }

            listaMesas.clear();
            msgConfirma = "Registro atualizado com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            sv.comitarTransacao();
        }
        return null;
    }

    public String editar(EventoBaile eve) {
        eventoBaile = eve;
        GenericaSessao.put("eventoBandaPesquisa", eventoBaile);
        descPesquisa = "";
        EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
        evento = eventoBaile.getEvento();
        endereco = eventoBaileDB.pesquisaEnderecoEvento(eventoBaile.getEvento().getId());
        loadCategoria();
        listaEventoServicoValor.clear();
        for (int i = 0; i < listaComboDescricaoEvento.size(); i++) {
            if (Integer.valueOf(listaComboDescricaoEvento.get(i).getDescription()) == eve.getEvento().getDescricaoEvento().getId()) {
                idDescricaoEvento = i;
            }
        }

        listaMesas.clear();
        listaConvites.clear();

        GenericaSessao.put("linkClicado", true);
        return (String) GenericaSessao.getString("urlRetorno");
    }

    public String excluir() {
        if (evento.getId() == -1) {
            msgConfirma = "Pesquise um Baile para ser excluído!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();

        // EXCLUIR EVENTO BANDA
        for (EventoBanda listaEventoBanda1 : listaEventoBanda) {
            if (!excluirBanda(listaEventoBanda1.getId(), sv)) {
                msgConfirma = "Banda do Evento não podem ser excluídas!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }

        // EXCLUIR EVENTO SERVIÇO VALOR
        for (EventoServicoValor listaEventoServicoValor1 : listaEventoServicoValor) {
            if (!excluirEventoServico(listaEventoServicoValor1.getId(), sv)) {
                msgConfirma = "Serviços de Valores não podem ser excluídos!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }

        // EXCLUIR EVENTO BAILE MAPA
        for (EventoBaileMapa mesas : listaMesas) {
            if (!sv.deletarObjeto(sv.pesquisaCodigo(mesas.getId(), "EventoBaileMapa"))) {
                msgConfirma = "Evento Baile Mapa não podem ser excluídos!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }

        // EXCLUIR EVENTO BAILE CONVITE
        for (EventoBaileConvite convites : listaConvites) {
            if (!sv.deletarObjeto(sv.pesquisaCodigo(convites.getId(), "EventoBaileConvite"))) {
                msgConfirma = "Evento Baile Convite não podem ser excluídos!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }

        endereco = (AEndereco) sv.find(endereco);
        if (!sv.deletarObjeto(endereco)) {
            msgConfirma = "Endereço Baile não pode ser excluído!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }
        eventoBaile = (EventoBaile) sv.find(eventoBaile);
        if (!sv.deletarObjeto(eventoBaile)) {
            msgConfirma = "Evento Baile não pode ser excluído! ";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }

        evento = (AEvento) sv.find(evento);
        if (!sv.deletarObjeto(evento)) {
            msgConfirma = "Evento não pode ser excluído! ";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }

        msgConfirma = "Evento excluído com sucesso!";
        GenericaMensagem.info("Sucesso", msgConfirma);
        sv.comitarTransacao();
        GenericaSessao.remove("baileBean");
        return null;
    }

    public void adicionarServico() {
        if (evento.getId() == -1) {
            msgConfirma = "Salve este Baile antes de Adicionar Serviços!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return;
        }

        if (eventoServico.isSocio() && listaCategoria.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Lista de Categoria Vazia!");
            return;
        }

        if (Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == 0 || Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == -1) {
            msgConfirma = "Escolha um serviço válido!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return;
        }

        if (eventoServicoValor.getValor() < 0) {
            msgConfirma = "Informar o valor do serviço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return;
        }

        if (eventoServicoValor.getIdadeFinal() == 0) {
            msgConfirma = "Informar a idade final!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return;
        }

        if (eventoServicoValor.getIdadeFinal() < eventoServicoValor.getIdadeInicial()) {
            msgConfirma = "Idade final deve ser maior ou igual a idade inicial!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return;
        }

        BaileDao dao = new BaileDao();

        if (dao.pesquisaEventoServico(Integer.valueOf(listaComboServicos.get(idServicos).getDescription()),
                (eventoServico.isSocio()) ? Integer.valueOf(listaCategoria.get(idCategoria).getDescription()) : null, evento.getId(),
                eventoServicoValor.getIdadeInicial(),
                eventoServicoValor.getIdadeFinal(),
                eventoServicoValor.getSexo()
        ) != null) {
            GenericaMensagem.warn("Atenção", "Evento Serviço já adicionado!");
            return;
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        eventoServico.setServicos(((Servicos) sv.find("Servicos", Integer.parseInt(listaComboServicos.get(idServicos).getDescription()))));

        if (eventoServico.isSocio()) {
            eventoServico.setCategoria((Categoria) sv.pesquisaCodigo(Integer.valueOf(listaCategoria.get(idCategoria).getDescription()), "Categoria"));
        } else {
            eventoServico.setCategoria(null);
        }

        eventoServicoValor.setId(-1);
        eventoServico.setId(-1);

        if (eventoServicoValor.getId() == -1) {
            sv.abrirTransacao();
            eventoServico.setaEvento(evento);

            if (!sv.inserirObjeto(eventoServico)) {
                msgConfirma = "Serviço não pode ser adicionado!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }

            if (eventoServico.isMesa()) {
                eventoServicoValor.setSexo("A");
            }

            eventoServicoValor.setEventoServico(eventoServico);
            if (!sv.inserirObjeto(eventoServicoValor)) {
                msgConfirma = "Serviço Valor não pode ser adicionado!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }

            msgConfirma = "Serviço Adicionado!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            listaEventoServicoValor.clear();

            sv.comitarTransacao();

            loadCategoria();

        }
    }

    public String removerEventoServico(EventoServicoValor esv) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (excluirEventoServico(esv.getId(), sv)) {
            listaEventoServicoValor.clear();
            eventoServicoValor = new EventoServicoValor();
            msgConfirma = "Serviço removida com sucesso";
            sv.comitarTransacao();

            loadCategoria();
        } else {
            msgConfirma = "Serviço não pode ser removido!";
            sv.desfazerTransacao();
        }
        return null;
    }

    public boolean excluirEventoServico(int id, SalvarAcumuladoDB sv) {
        eventoServicoValor = (EventoServicoValor) sv.find("EventoServicoValor", id);
        if (eventoServicoValor.getId() != -1) {
            if (sv.deletarObjeto(eventoServicoValor)) {
                eventoServico = (EventoServico) sv.find("EventoServico", eventoServicoValor.getEventoServico().getId());
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

    public void adicionarBanda() {
        if (listaComboBanda.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Nenhuma Banda para ser Adicionada!");
            return;
        }

        if (evento.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Salve este Baile antes de Adicionar uma Banda!");
            return;
        }

        Dao dao = new Dao();
        Banda banda = (Banda) dao.find(new Banda(), Integer.parseInt(listaComboBanda.get(idBanda).getDescription()));
        for (EventoBanda eb : listaEventoBanda) {
            if (eb.getBanda().getId() == banda.getId()) {
                GenericaMensagem.warn("Atenção", "Banda já cadastrada para esse Evento");
                return;
            }
        }

        EventoBanda eb = new EventoBanda(-1, banda, evento);

        dao.openTransaction();
        if (!dao.save(eb)) {
            dao.rollback();
            GenericaMensagem.error("Erro", "Banda não pode ser adicionada!");
        }

        dao.commit();
        GenericaMensagem.info("Sucesso", "Banda adicionada!");
        listaEventoBanda.clear();
    }

    public void removerBanda(EventoBanda linha) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (!excluirBanda(linha.getId(), sv)) {
            GenericaMensagem.error("Error", "Não foi possível remover Banda!");
            sv.desfazerTransacao();
            return;
        }
        sv.comitarTransacao();

        GenericaMensagem.info("Sucesso", "Banda Excluída!");
        listaEventoBanda.clear();
    }

    public boolean excluirBanda(int id, SalvarAcumuladoDB sv) {
        EventoBanda eb = (EventoBanda) sv.find("EventoBanda", id);
        if (eb.getId() != -1) {
            return sv.deletarObjeto(eb);
        }
        return false;
    }

    public String converteMoeda(float v) {
        return Moeda.converteR$Float(v);
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
            List<Banda> list = sv.listaObjeto("Banda");
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    listaComboBanda.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                }
            }
        }
        return listaComboBanda;
    }

    public void setListaComboBanda(List<SelectItem> listaComboBanda) {
        this.listaComboBanda = listaComboBanda;
    }

    public List<SelectItem> getListaComboServicos() {
        return listaComboServicos;
    }

    public void setListaComboServicos(List<SelectItem> listaComboServicos) {
        this.listaComboServicos = listaComboServicos;
    }

    public List<SelectItem> getListaComboDescricaoEvento() {
        if (listaComboDescricaoEvento.isEmpty()) {
            DescricaoEventoDB db = new DescricaoEventoDBToplink();
            List<DescricaoEvento> list = db.pesquisaDescricaoPorGrupo(1);
            if (!list.isEmpty()) {
                for (int i = 0; i < list.size(); i++) {
                    listaComboDescricaoEvento.add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                }
            }
        }
        return listaComboDescricaoEvento;
    }

    public void setListaComboDescricaoEvento(List<SelectItem> listaComboDescricaoEvento) {
        this.listaComboDescricaoEvento = listaComboDescricaoEvento;
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
        this.listaEventoBanda = listaEventoBanda;
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
            listaEventoServicoValor = new BaileDao().listaServicoValorPorEvento(evento.getId());
        }
        return listaEventoServicoValor;
    }

    public void setListaEventoServicoValor(List<EventoServicoValor> listaEventoServicoValor) {
        this.listaEventoServicoValor = listaEventoServicoValor;
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

    public String getMesaTop() {
        return mesaTop;
    }

    public void setMesaTop(String mesaTop) {
        this.mesaTop = mesaTop;
    }

    public String getMesaLeft() {
        return mesaLeft;
    }

    public void setMesaLeft(String mesaLeft) {
        this.mesaLeft = mesaLeft;
    }

    public List<Integer> getListaQuantidade() {
        if (listaQuantidade.isEmpty()) {
            for (int i = 1; i < 426; i++) {
                listaQuantidade.add(i);
            }
        }
        return listaQuantidade;
    }

    public void setListaQuantidade(List<Integer> listaQuantidade) {
        this.listaQuantidade = listaQuantidade;
    }

    public int getNrMesa() {
        return nrMesa;
    }

    public void setNrMesa(int nrMesa) {
        this.nrMesa = nrMesa;
    }

    public EventoBaileMapa getEbmSelecionado() {
        return ebmSelecionado;
    }

    public void setEbmSelecionado(EventoBaileMapa ebmSelecionado) {
        this.ebmSelecionado = ebmSelecionado;
    }

    public List<SelectItem> getListaMesasDisponiveis() {
        listaMesasDisponiveis.clear();
        int qm = 1;
        if (eventoBaile.getQuantidadeMesas() > 0) {
            qm = eventoBaile.getQuantidadeMesas();
        }
        int j = 1;
        for (int i = 0; i < qm; i++) {
            listaMesasDisponiveis.add(new SelectItem(i, "Mesa " + j, "" + j));
            j++;
        }
        return listaMesasDisponiveis;
    }

    public void setListaMesasDisponiveis(List<SelectItem> listaMesasDisponiveis) {
        this.listaMesasDisponiveis = listaMesasDisponiveis;
    }

    public List<SelectItem> getListaConvitesDisponiveis() {
        listaConvitesDisponiveis.clear();
        int qm = 1;
        if (eventoBaile.getQuantidadeConvites() > 0) {
            qm = eventoBaile.getQuantidadeConvites();
        }
        int j = 1;
        for (int i = 0; i < qm; i++) {
            listaConvitesDisponiveis.add(new SelectItem(i, "Convite " + j, "" + j));
            j++;
        }
        return listaConvitesDisponiveis;
    }

    public void setListaConvitesDisponiveis(List<SelectItem> listaConvitesDisponiveis) {
        this.listaConvitesDisponiveis = listaConvitesDisponiveis;
    }

    public int getIdNrMesa() {
        return idNrMesa;
    }

    public void setIdNrMesa(int idNrMesa) {
        this.idNrMesa = idNrMesa;
    }

    public int getIdNrConvite() {
        return idNrConvite;
    }

    public void setIdNrConvite(int idNrConvite) {
        this.idNrConvite = idNrConvite;
    }

    public List<SelectItem> getListaCategoria() {
        return listaCategoria;
    }

    public void setListaCategoria(List<SelectItem> listaCategoria) {
        this.listaCategoria = listaCategoria;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public boolean isVisibleMapa() {
        return visibleMapa;
    }

    public void setVisibleMapa(boolean visibleMapa) {
        this.visibleMapa = visibleMapa;
    }

    public Servicos getServicos() {
        return servicos;
    }

    public void setServicos(Servicos servicos) {
        this.servicos = servicos;
    }

}
