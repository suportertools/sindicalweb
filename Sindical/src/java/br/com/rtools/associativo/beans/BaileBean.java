package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.*;
import br.com.rtools.associativo.db.*;
import br.com.rtools.endereco.Endereco;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.db.ServicosDB;
import br.com.rtools.financeiro.db.ServicosDBToplink;
import br.com.rtools.seguranca.controleUsuario.ControleUsuarioBean;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.SalvaArquivos;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
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
public class BaileBean implements Serializable{
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
    private List<Integer> listaQuantidade = new ArrayList();
    private List<EventoBanda> listaEventoBanda = new ArrayList();
    private List<EventoBaile> listaEventoBaile = new ArrayList();
    private List<EventoServicoValor> listaEventoServicoValor = new ArrayList();
    private List<SelectItem> listaComboBanda = new ArrayList();
    private List<SelectItem> listaComboServicos = new ArrayList();
    private List<SelectItem> listaComboDescricaoEvento = new ArrayList();
    private String msgConfirma = "";
    private String comoPesquisa = "I";
    private String descPesquisa = "";
    private String mesaTop = "";
    private String mesaLeft = "";
    private boolean limpar = false;
    DataHoje dataHoje = new DataHoje();
    private List<EventoBaileMapa> listaMesas = new ArrayList();
    private List<EventoBaileMapa> listaMesaSelecionada = new ArrayList();
    
    public void uploadMapa(FileUploadEvent event){
        UploadedFile uploadedFile = event.getFile(); 
        
        try{
            //InputStream in = new BufferedInputStream(uploadedFile.getInputstream());
            InputStream in = new BufferedInputStream(uploadedFile.getInputstream());
            String pathPasta = ((ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext()).getRealPath("/Cliente/" + ControleUsuarioBean.getCliente() + "/Imagens/Mapas");
            //File arquivo = new File(pathPasta+"/"+event.getFile().getFileName());
            File arquivo = new File(pathPasta+"/"+"mapa_baile.jpg");
            
            FileOutputStream fout = new FileOutputStream(arquivo);
            while(in.available() != 0){
                fout.write(in.read());
            }
            fout.close();
        }catch(Exception e){
            
        }

    }

    public void excluirMesa(EventoBaileMapa ebm){
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        sv.abrirTransacao();
        
        if (!sv.deletarObjeto( sv.pesquisaCodigo(ebm.getId(), "EventoBaileMapa")) ){
            sv.desfazerTransacao();
            GenericaMensagem.warn("Erro!", "Não foi possível excluir esta mesa!");
        }else{
            GenericaMensagem.info("Sucesso!", "Mesa excluída com sucesso!");
            sv.comitarTransacao();
        }
        
        listaMesas.clear();
    }
    
    public void onDrop(DragDropEvent event) {  
        
        HttpServletRequest http = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        Cookie[] cookies = http.getCookies();
        
        for (int i = 0; i < cookies.length; i++){
            if (cookies[i].getName().equals("mesa_left")){
                mesaLeft = cookies[i].getValue();
            }
            
            if (cookies[i].getName().equals("mesa_top")){
                mesaTop = cookies[i].getValue();
            }
        }
        
        EventoBaileMapa ebm = (EventoBaileMapa) event.getData();  
  
        if (eventoBaile.getId() != -1){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            sv.abrirTransacao();
            
            EventoBaileDB db = new EventoBaileDBToplink();
            EventoBaileMapa result = db.pesquisaMesaBaile(eventoBaile.getId(), ebm.getMesa());
            
            if (result.getId() != -1){
                result.setPosicao(" position: relative; left: "+mesaLeft+"px; top: "+mesaTop+"px");
                if(!sv.alterarObjeto(result)){
                    sv.desfazerTransacao();
                    return;
                }
                FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(result.getMesa() + " atualizada", "Posição: " + event.getDropId()));
            }else{
                if (!sv.inserirObjeto(new EventoBaileMapa(-1, eventoBaile, ebm.getMesa(), event.getDropId().replace("formBaile:", ""), " position: relative; left: "+mesaLeft+"px; top: "+mesaTop+"px"))){
                    sv.desfazerTransacao();
                    return;
                }else{
                    listaMesaSelecionada.add(new EventoBaileMapa(-1, eventoBaile, ebm.getMesa(), event.getDropId().replace("formBaile:", ""), ""));  
                    FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(ebm.getMesa() + " adicionada", "Posição: " + event.getDropId()));
                }
            }
            sv.comitarTransacao();
            listaMesas.clear();
        }
    }     
    
    public List<EventoBaileMapa> getListaMesas() {
        if (listaMesas.isEmpty()){
            for (int i = 1; i <= eventoBaile.getQuantidadeMesas(); i++){
                if (eventoBaile.getId() != -1){
                    EventoBaileDB db = new EventoBaileDBToplink();
                    EventoBaileMapa result = db.pesquisaMesaBaile(eventoBaile.getId(), i);
                    if (result.getId() == -1)
                        listaMesas.add(new EventoBaileMapa(-1, new EventoBaile(), i, "i_"+i, ""));
                    else{
                        listaMesas.add(result);
                        listaMesaSelecionada.add(result);
                    }
                }else
                    listaMesas.add(new EventoBaileMapa(-1, new EventoBaile(), i, "", ""));
            }
        }
        return listaMesas;
    }

    public void setListaMesas(List<EventoBaileMapa> listaMesas) {
        this.listaMesas = listaMesas;
    }    
    
    public List<EventoBaileMapa> getListaMesaSelecionada() {
        if (listaMesaSelecionada.isEmpty()){
            if (eventoBaile.getId() != -1){
                EventoBaileDB db = new EventoBaileDBToplink();
                List<EventoBaileMapa> lista = db.listaBaileMapa(eventoBaile.getId());
                
                for (int i = 0; i < lista.size(); i++){
                    listaMesaSelecionada.add(new EventoBaileMapa(lista.get(i).getId(), eventoBaile, lista.get(i).getMesa(), lista.get(i).getComponenteId(), lista.get(i).getPosicao()));
                }
            }
        }
        return listaMesaSelecionada;
    }

    public void setListaMesaSelecionada(List<EventoBaileMapa> listaMesaSelecionada) {
        this.listaMesaSelecionada = listaMesaSelecionada;
    }
    
    public void removerEndereco(){
        if (endereco.getId() == -1){
            endereco  = new AEndereco();
        }else{
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            
            sv.abrirTransacao();
            if (!sv.deletarObjeto(sv.pesquisaCodigo(endereco.getId(), "AEndereco"))){
                GenericaMensagem.warn("Erro", "Não foi possível excluir este endereço!");
                sv.desfazerTransacao();
                return;
            }
            
            endereco = new AEndereco();
            sv.comitarTransacao();
        }
    }
    
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
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baileBean");
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

    public boolean validaSalvar(){
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
        if (!validaSalvar()){
            return null;
        }
        
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        
        evento.setDescricaoEvento(((DescricaoEvento) sv.pesquisaCodigo(Integer.parseInt(listaComboDescricaoEvento.get(idDescricaoEvento).getDescription()), "DescricaoEvento")));
        sv.abrirTransacao();
        
        if (evento.getId() == -1) {
            if (!dataHoje.maiorData(eventoBaile.getDataString(), DataHoje.converteData(dataHoje.dataHoje()))) {
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
                
            if (endereco.getId() != -1){
                if (!sv.alterarObjeto(endereco)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao atualizar o Endereço!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }
            }else{
                endereco.setEvento(evento);
                if (!sv.inserirObjeto(endereco)) {
                    sv.desfazerTransacao();
                    msgConfirma = "Falha ao atualizar o Endereço!";
                    GenericaMensagem.warn("Erro", msgConfirma);
                    return null;
                }
            }
            
            listaMesas.clear();
            listaMesaSelecionada.clear();
            msgConfirma = "Registro atualizado com sucesso!";
            GenericaMensagem.info("Sucesso", msgConfirma);
            sv.comitarTransacao();
        }
        return null;
    }

    public String editar(EventoBaile eve) {
        eventoBaile = eve; //(EventoBaile) listaEventoBaile.get(idIndex);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("eventoBandaPesquisa", eventoBaile);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("linkClicado", true);
        descPesquisa = "";
        EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
        evento = eventoBaile.getEvento();
        endereco = eventoBaileDB.pesquisaEnderecoEvento(eventoBaile.getEvento().getId());
        return (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("urlRetorno");
    }

    public String excluir() {
        if (evento.getId() == -1){
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
        endereco = (AEndereco) sv.pesquisaCodigo(endereco.getId(), "AEndereco");
        if (!sv.deletarObjeto(endereco)) {
            msgConfirma = "Endereço Baile não pode ser excluído!";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }
        eventoBaile = (EventoBaile) sv.pesquisaCodigo(eventoBaile.getId(), "EventoBaile");
        if (!sv.deletarObjeto(eventoBaile)) {
            msgConfirma = "Evento Baile não pode ser excluído! ";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }
        evento = (AEvento) sv.pesquisaCodigo(eventoBaile.getEvento().getId(), "AEvento");
        if (!sv.deletarObjeto(evento)) {
            msgConfirma = "Evento não pode ser excluído! ";
            GenericaMensagem.warn("Erro", msgConfirma);
            sv.desfazerTransacao();
            return null;
        }
        
        msgConfirma = "Evento excluído com sucesso!";
        GenericaMensagem.info("Sucesso", msgConfirma);
        sv.comitarTransacao();
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("baileBean");
        return null;
    }

    public String adicionarServico() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        eventoServico.setServicos(((Servicos) sv.pesquisaCodigo(Integer.parseInt(listaComboServicos.get(idServicos).getDescription()), "Servicos")));
        if (Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == 0
                || Integer.parseInt(listaComboServicos.get(idServicos).getDescription()) == -1) {
            msgConfirma = "Escolha um serviço válido!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (eventoServicoValor.getValor() == 0) {
            msgConfirma = "Informar o valor do serviço!";
            GenericaMensagem.warn("Erro", msgConfirma);
            return null;
        }
        if (eventoServicoValor.getIdadeInicial() == 0) {
            msgConfirma = "Informar a idade inicial!";
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
        
        if (evento.getId() == -1){
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
        if (listaQuantidade.isEmpty()){
            for (int i = 1; i < 419; i++){
                listaQuantidade.add(i);
            }
        }
        return listaQuantidade;
    }

    public void setListaQuantidade(List<Integer> listaQuantidade) {
        this.listaQuantidade = listaQuantidade;
    }

}
