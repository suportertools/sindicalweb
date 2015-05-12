package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
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
    private int nrMesa = 0;
    private int idNrMesa = 0;
    private List<Integer> listaQuantidade = new ArrayList();
    private List<EventoBanda> listaEventoBanda = new ArrayList();
    private List<EventoBaile> listaEventoBaile = new ArrayList();
    private List<EventoServicoValor> listaEventoServicoValor = new ArrayList();
    private List<SelectItem> listaComboBanda = new ArrayList();
    private List<SelectItem> listaComboServicos = new ArrayList();
    private List<SelectItem> listaComboDescricaoEvento = new ArrayList();
    private List<SelectItem> listaMesasDisponiveis = new ArrayList();
    private String msgConfirma = "";
    private String comoPesquisa = "I";
    private String descPesquisa = "";
    private String mesaTop = "";
    private String mesaLeft = "";
    private boolean limpar = false;
    DataHoje dataHoje = new DataHoje();
    private List<EventoBaileMapa> listaMesas = new ArrayList();
    private EventoBaileMapa ebmSelecionado = new EventoBaileMapa();
    private int idCategoria = 0;
    private List<SelectItem> listaCategoria = new ArrayList();

    @PostConstruct
    public void init(){
        loadCategoria();
    }
    
    @PreDestroy
    public void destroy(){
        GenericaSessao.remove("baileBean");
    }
    
    public void loadCategoria(){
        List<Categoria> result = new Dao().list(new Categoria());
        
        listaCategoria.clear();
        for (int i = 0; i < result.size(); i++){
            listaCategoria.add(new SelectItem(i, result.get(i).getCategoria(), ""+result.get(i).getId()));
        }
    }
    
    public void uploadMapa(FileUploadEvent event) {
        UploadedFile uploadedFile = event.getFile();
        try {
            InputStream in = new BufferedInputStream(uploadedFile.getInputstream());
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Mapas");
            File arquivo = new File(pathPasta + "/" + "mapa_baile.jpg");
            FileOutputStream fout = new FileOutputStream(arquivo);
            while (in.available() != 0) {
                fout.write(in.read());
            }
            fout.close();
        } catch (IOException e) {

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

    public void salvarMesa(boolean all) {
        if (eventoBaile.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            EventoBaileMapa ebm = new EventoBaileMapa();
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            sv.abrirTransacao();
            if (all) {
                boolean err = false;
                for (int i = 0; i < listaMesasDisponiveis.size(); i++) {
                    if(((EventoBaileMapa) eventoBaileDB.pesquisaMesaBaile(eventoBaile.getId(), Integer.parseInt(listaMesasDisponiveis.get(i).getDescription()))).getId() != -1) {
                        ebm.setMesa(Integer.parseInt(listaMesasDisponiveis.get(i).getDescription()));
                        ebm.setEventoBaile(eventoBaile);
                        if (!sv.inserirObjeto(ebm)) {
                            err = true;
                            break;
                        }
                        ebm = new EventoBaileMapa();                        
                    }
                }
                if (err) {
                    sv.desfazerTransacao();
                } else {
                    sv.comitarTransacao();
                    idNrMesa = 0;
                    nrMesa = 0;
                    listaMesasDisponiveis.clear();
                    listaMesas.clear();
                }
            } else {
                ebm.setMesa(Integer.parseInt(listaMesasDisponiveis.get(idNrMesa).getDescription()));
                ebm.setEventoBaile(eventoBaile);
                if (sv.inserirObjeto(ebm)) {
                    sv.comitarTransacao();
                    idNrMesa = 0;
                    nrMesa = 0;
                    listaMesasDisponiveis.clear();
                    listaMesas.clear();
                } else {
                    sv.desfazerTransacao();
                }
            }
            sv.fecharTransacao();
        }
    }

    // excluir
    public void onDrop(DragDropEvent event) {

        HttpServletRequest http = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = http.getCookies();

        for (int i = 0; i < cookies.length; i++) {
            if (cookies[i].getName().equals("mesa_left")) {
                mesaLeft = cookies[i].getValue();
            }

            if (cookies[i].getName().equals("mesa_top")) {
                mesaTop = cookies[i].getValue();
            }
        }

        EventoBaileMapa ebm = (EventoBaileMapa) event.getData();

        if (eventoBaile.getId() != -1) {
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();

            EventoBaileDB db = new EventoBaileDBToplink();
            EventoBaileMapa result = db.pesquisaMesaBaile(eventoBaile.getId(), ebm.getMesa());

            if (result.getId() != -1) {
                result.setPosicao(" position: relative; left: " + mesaLeft + "px; top: " + mesaTop + "px");
                if (!sv.alterarObjeto(result)) {
                    sv.desfazerTransacao();
                    return;
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(result.getMesa() + " atualizada", "Posição: " + event.getDropId()));
            } else {
                if (!sv.inserirObjeto(new EventoBaileMapa(-1, eventoBaile, ebm.getMesa(), " position: relative; left: " + mesaLeft + "px; top: " + mesaTop + "px"))) {
                    sv.desfazerTransacao();
                    return;
                } else {

                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ebm.getMesa() + " adicionada", "Posição: " + event.getDropId()));
                }
            }
            sv.comitarTransacao();
            listaMesas.clear();
        }
    }

    public void adicionarMesa(EventoBaileMapa ebm) {

    }

    public List<EventoBaileMapa> getListaMesas() {
        if (listaMesas.isEmpty()) {
            if(eventoBaile.getId() != -1) {
                EventoBaileDB db = new EventoBaileDBToplink();
                listaMesas = db.listaBaileMapa(eventoBaile.getId());
//                for (int i = 0; i < 425; i++) {
//                    EventoBaileMapa result = db.pesquisaMesaBaile(eventoBaile.getId(), listaMesas.get(i).getMesa());
//                    if (result.getId() == -1) {
//                        listaMesas.add(new EventoBaileMapa(-1, eventoBaile, 0, "i_" + i));
//                    } else {
//                        listaMesas.add(result);
//                    }
//                }
            }
        }
        return listaMesas;
    }

    public void setListaMesas(List<EventoBaileMapa> listaMesas) {
        this.listaMesas = listaMesas;
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
        eventoBanda = new EventoBanda();
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
        GenericaSessao.put("linkClicado", true);
        descPesquisa = "";
        EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
        evento = eventoBaile.getEvento();
        endereco = eventoBaileDB.pesquisaEnderecoEvento(eventoBaile.getEvento().getId());
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

        for (int i = 0; i < listaEventoBanda.size(); i++) {
            if (!excluirBanda(listaEventoBanda.get(i).getId(), sv)) {
                msgConfirma = "Banda do Evento não podem ser excluídas!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
                return null;
            }
        }
        for (int i = 0; i < listaEventoServicoValor.size(); i++) {
            if (!excluirEventoServico(listaEventoServicoValor.get(i).getId(), sv)) {
                msgConfirma = "Serviços de Valores não podem ser excluídos!";
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
        evento = (AEvento) sv.find(eventoBaile.getEvento());
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

    public String adicionarServico() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        eventoServico.setServicos(((Servicos) sv.find("Servicos", Integer.parseInt(listaComboServicos.get(idServicos).getDescription()))));
        if (Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == 0
                || Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == -1) {
            msgConfirma = "Escolha um serviço válido!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (eventoServicoValor.getValor() < 0) {
            msgConfirma = "Informar o valor do serviço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (eventoServicoValor.getIdadeFinal() == 0) {
            msgConfirma = "Informar a idade final!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (eventoServicoValor.getIdadeFinal() < eventoServicoValor.getIdadeInicial()) {
            msgConfirma = "Idade final deve ser maior ou igual a idade inicial!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }

        if (evento.getId() == -1) {
            msgConfirma = "Salve este Baile antes de Adicionar Serviços!";
            GenericaMensagem.warn("Erro", msgConfirma);
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
                    GenericaMensagem.info("Sucesso", msgConfirma);
                    listaEventoServicoValor.clear();
                    eventoServicoValor = new EventoServicoValor();
                    eventoServico = new EventoServico();
                    sv.comitarTransacao();
                } else {
                    msgConfirma = "Serviço Valor não pode ser adicionado!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    sv.desfazerTransacao();
                }
            } else {
                msgConfirma = "Serviço não pode ser adicionado!";
                GenericaMensagem.warn("Erro", msgConfirma);
                sv.desfazerTransacao();
            }
        }
        return null;
    }

    public String removerEventoServico(EventoServicoValor esv) {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        sv.abrirTransacao();
        if (excluirEventoServico(esv.getId(), sv)) {
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

    public String adicionarBanda() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        EventoBandaDB db = new EventoBandaDBToplink();
        Banda banda = (Banda) sv.find("Banda", Integer.parseInt(listaComboBanda.get(idBanda).getDescription()));
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
        eventoBanda = (EventoBanda) sv.find("EventoBanda", id);
        if (eventoBanda.getId() != -1) {
            return sv.deletarObjeto(eventoBanda);
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
        int i = 0, j = 0;
        listaComboServicos.clear();
        int idServicos;
        if (!eventoServico.isMesa()) {
            idServicos = 230;
        } else {
            idServicos = 229;
        }
        ServicosDB db = new ServicosDBToplink();
        List<Servicos> select = db.pesquisaTodos(idServicos);
        if (!select.isEmpty()) {
            while (i < select.size()) {
                //if ((!eventoServico.isMesa() && (select.get(i).getId() == 12) || (select.get(i).getId() == 13)) || (eventoServico.isMesa()) && ((select.get(i).getId() == 14) || (select.get(i).getId() == 15))) {
                listaComboServicos.add(new SelectItem(i, (String) (select.get(i)).getDescricao(), Integer.toString((select.get(i)).getId())));
                //j++;
                //}
                i++;
            }
        }
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
//
//    public EventoBaileMapa getEventoBaileMapa() {
//        return eventoBaileMapa;
//    }
//
//    public void setEventoBaileMapa(EventoBaileMapa eventoBaileMapa) {
//        this.eventoBaileMapa = eventoBaileMapa;
//    }

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
        if(eventoBaile.getQuantidadeMesas() > 0) {
            qm = eventoBaile.getQuantidadeMesas();
        }
        //if (listaMesasDisponiveis.isEmpty()) {
            int j = 1;
            for (int i = 0; i < qm; i++) {
//                    boolean existe = false;
//                    for (int x = 0; x < list.size(); x++) {
//                        if(list.get(x).getMesa() == j) {
//                            existe = true;
//                        }
//                    }
//                    if(!existe) {
//                    }
                listaMesasDisponiveis.add(new SelectItem(i, "Mesa " + j, ""+j));                            
                j++;                    
//            if(eventoBaile.getId() != -1) {
//                EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
//                List<EventoBaileMapa> list = (List<EventoBaileMapa>) eventoBaileDB.listaBaileMapa(eventoBaile.getId());                
//                }
            }
        //}
        return listaMesasDisponiveis;
    }

    public void setListaMesasDisponiveis(List<SelectItem> listaMesasDisponiveis) {
        this.listaMesasDisponiveis = listaMesasDisponiveis;
    }

    public int getIdNrMesa() {
        return idNrMesa;
    }

    public void setIdNrMesa(int idNrMesa) {
        this.idNrMesa = idNrMesa;
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

}
