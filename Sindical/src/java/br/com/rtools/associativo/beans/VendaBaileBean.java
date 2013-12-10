package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.EventoBaileMapa;
import br.com.rtools.associativo.db.EventoBaileDB;
import br.com.rtools.associativo.db.EventoBaileDBToplink;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;


@ManagedBean
@SessionScoped
public class VendaBaileBean implements Serializable{
    private final List<SelectItem> listaEventoBaile = new ArrayList();
    private List<EventoBaile> listae = new ArrayList();
    private int indexEventoBaile = 0;
    private Fisica fisica = new Fisica();
    private List<EventoBaileMapa> listaMesas = new ArrayList();
    private List<EventoBaileMapa> listaMesaSelecionada = new ArrayList();
    private List<Integer> listaQuantidade = new ArrayList();

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
    
        public List<EventoBaileMapa> getListaMesas() {
        if (listaMesas.isEmpty()){
            for (int i = 1; i <= listae.get(indexEventoBaile).getQuantidadeMesas(); i++){
                
                EventoBaileDB db = new EventoBaileDBToplink();
                EventoBaileMapa result = db.pesquisaMesaBaile(listae.get(indexEventoBaile).getId(), i);
                if (result.getId() == -1)
                    listaMesas.add(new EventoBaileMapa(-1, new EventoBaile(), i, "i_"+i, ""));
                else{
                    listaMesas.add(result);
                    listaMesaSelecionada.add(result);
                }
                
            }
        }
        return listaMesas;
    }

    public void setListaMesas(List<EventoBaileMapa> listaMesas) {
        this.listaMesas = listaMesas;
    }    
    
    public List<EventoBaileMapa> getListaMesaSelecionada() {
        if (listaMesaSelecionada.isEmpty()){
            
            EventoBaileDB db = new EventoBaileDBToplink();
            List<EventoBaileMapa> lista = db.listaBaileMapa(listae.get(indexEventoBaile).getId());

            for (int i = 0; i < lista.size(); i++){
                listaMesaSelecionada.add(new EventoBaileMapa(lista.get(i).getId(), listae.get(indexEventoBaile), lista.get(i).getMesa(), lista.get(i).getComponenteId(), lista.get(i).getPosicao()));
            }
            
        }
        return listaMesaSelecionada;
    }

    public void setListaMesaSelecionada(List<EventoBaileMapa> listaMesaSelecionada) {
        this.listaMesaSelecionada = listaMesaSelecionada;
    }
    
    public void removerFisica(){
        fisica = new Fisica();
    }
    
    public List<SelectItem> getListaEventoBaile(){
        if(listaEventoBaile.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            listae = sv.listaObjeto("EventoBaile");
            if (!listae.isEmpty()){
                for (int i = 0; i < listae.size(); i++){
                    listaEventoBaile.add(new SelectItem(
                           new Integer(i),
                           listae.get(i).getEvento().getDescricaoEvento().getDescricao() + " -  " +
                           listae.get(i).getDataString() + " - (" +
                           listae.get(i).getHoraInicio() + " às  " +
                           listae.get(i).getHoraFim() + ")   " +
                           listae.get(i).getQuantidadeMesas() + " mesas  ",
                           Integer.toString(( listae.get(i)).getId())  ));
                }
            }
        }
        return listaEventoBaile;
    }

    public int getIndexEventoBaile() {
        return indexEventoBaile;
    }

    public void setIndexEventoBaile(int indexEventoBaile) {
        this.indexEventoBaile = indexEventoBaile;
    }

    public Fisica getFisica() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null){
            fisica = (Fisica)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
        }
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }
    

}
//
//import br.com.rtools.associativo.AStatus;
//import br.com.rtools.associativo.BVenda;
//import br.com.rtools.associativo.EventoBaile;
//import br.com.rtools.associativo.EventoServicoValor;
//import br.com.rtools.associativo.Mesa;
//import br.com.rtools.associativo.Socios;
//import br.com.rtools.associativo.db.AStatusDB;
//import br.com.rtools.associativo.db.AStatusDBToplink;
//import br.com.rtools.associativo.db.BVendaDB;
//import br.com.rtools.associativo.db.BVendaDBToplink;
//import br.com.rtools.associativo.db.EventoBaileDB;
//import br.com.rtools.associativo.db.EventoBaileDBToplink;
//import br.com.rtools.associativo.db.EventoServicoValorDB;
//import br.com.rtools.associativo.db.EventoServicoValorDBToplink;
//import br.com.rtools.associativo.db.MesaDB;
//import br.com.rtools.associativo.db.MesaDBToplink;
//import br.com.rtools.associativo.db.SociosDB;
//import br.com.rtools.associativo.db.SociosDBToplink;
//import br.com.rtools.classeOperacao.OperacaoMovimento;
//import br.com.rtools.financeiro.Evt;
//import br.com.rtools.financeiro.Lote;
//import br.com.rtools.financeiro.Movimento;
//import br.com.rtools.financeiro.Servicos;
//import br.com.rtools.financeiro.db.EvtDB;
//import br.com.rtools.financeiro.db.EvtDBToplink;
//import br.com.rtools.financeiro.db.FinanceiroDB;
//import br.com.rtools.financeiro.db.FinanceiroDBToplink;
//import br.com.rtools.financeiro.db.LoteDB;
//import br.com.rtools.financeiro.db.LoteDBToplink;
//import br.com.rtools.financeiro.db.MovimentoDB;
//import br.com.rtools.financeiro.db.MovimentoDBToplink;
//import br.com.rtools.financeiro.db.TipoServicoDB;
//import br.com.rtools.financeiro.db.TipoServicoDBToplink;
//import br.com.rtools.lista.GVendaBaile;
//import br.com.rtools.lista.MapaBaile;
//import br.com.rtools.pessoa.Fisica;
//import br.com.rtools.pessoa.Pessoa;
//import br.com.rtools.pessoa.db.FilialDB;
//import br.com.rtools.pessoa.db.FilialDBToplink;
//import br.com.rtools.seguranca.Usuario;
//import br.com.rtools.seguranca.controleUsuario.chamadaPaginaJSFBean;
//import br.com.rtools.seguranca.db.RotinaDB;
//import br.com.rtools.seguranca.db.RotinaDBToplink;
//import br.com.rtools.utilitarios.DataHoje;
//import java.util.ArrayList;
//import java.util.List;
//import javax.faces.context.FacesContext;
//import javax.faces.model.SelectItem;
//import org.richfaces.component.html.HtmlDataGrid;
//import org.richfaces.component.html.HtmlDataTable;
//
//public class VendaBaileJSFBean {
//
//    private String msgConfirma;
//    private HtmlDataTable htmlTable;
//    private HtmlDataGrid htmlGridMapa;
//    private BVenda bVenda;
//    private List<SelectItem> listaEventoBaile;
//    private List<SelectItem> listaEventoServicoValor;
//    private List<EventoServicoValor> listaEventoServicoValorObjeto;
//    private int idEventoServicoValor;
//    private int idEventoServicoValorConvite;
//    private EventoBaile eventoBaile;
//    private int idEventoBaile;
//    private boolean todos;
//    private Pessoa pessoa;
//    private Pessoa titular;
//    private List<GVendaBaile> listaVendaBaile;
//    private List<AStatus> listaStatus;
//    private List<MapaBaile> mapaBaile;
//
//    public VendaBaileJSFBean(){
//        msgConfirma = "";
//        bVenda = new BVenda();
//        listaEventoBaile = new  ArrayList<SelectItem>();
//        listaEventoServicoValor = new  ArrayList<SelectItem>();
//        listaEventoServicoValorObjeto = new  ArrayList<EventoServicoValor>();
//        eventoBaile = new EventoBaile();
//        idEventoBaile = 0;
//        idEventoServicoValor = 0;
//        todos = false;
//        pessoa = new Pessoa();
//        titular = new Pessoa();
//        listaVendaBaile = new ArrayList<GVendaBaile>();
//        AStatusDB aStatusDB = new AStatusDBToplink();
//        listaStatus = aStatusDB.pesquisaTodos();
//        listaStatus.add(new AStatus());
//        mapaBaile = new ArrayList<MapaBaile>();
//        idEventoServicoValorConvite = 0;
//    }
//
//
//    public synchronized void venderMesa(){
//        if(!getListaEventoServicoValorMesa().isEmpty()){
//            this.atualizarMapa(2);
//        }
//    }
//
//    public synchronized void reservarMesa(){
//        this.atualizarMapa(1);
//    }
//
//    public synchronized void liberarMesa(){
//         for(MapaBaile mapa : mapaBaile){
//            if(mapa.getMesa().getStatus().getId() == -1){
//               for(int i = 0; i < listaVendaBaile.size(); i++){
//                   if(listaVendaBaile.get(i).getMesa().getNrMesa() == mapa.getMesa().getNrMesa()){
//                       listaVendaBaile.remove(i);
//                       removerMesa(mapa.getMesa());
//                       mapa.getMesa().setStatus(listaStatus.get(0));
//                       return;
//                   }
//               }
//               removerMovimento(mapa.getMesa(), mapa.getMesa().getbVenda());
//               mapa.getMesa().setStatus(listaStatus.get(0));
//            }
//        }
//    }
//
//    private void atualizarMapa(int indiceStatus){
//        Usuario usuario = ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
//        EventoServicoValor eventoServicoValor = new EventoServicoValor();
//        EventoServicoValorDB eventoServicoValorDB = new EventoServicoValorDBToplink();
//        for(MapaBaile mapa : mapaBaile){
//            if((mapa.getMesa().getStatus().getId() == -1) && (mapa.getStatusAnterior().getId() != (indiceStatus +1))){
//               eventoServicoValor = eventoServicoValorDB.pesquisaCodigo(Integer.parseInt(this.getListaEventoServicoValorMesa().get(idEventoServicoValor).getDescription()));
//               mapa.setEventoServicoValor(eventoServicoValor);
//               mapa.getMesa().setStatus(listaStatus.get(indiceStatus));
//
//               if(indiceStatus == 2 ){
//                   if(mapa.getMesa().getbVenda() == null){
//                       mapa.getMesa().setbVenda(new BVenda(
//                           -1,
//                           eventoBaile.getEvento(),
//                           null,
//                           pessoa,
//                           titular(),
//                           usuario,
//                           ""
//                       ));
//                   }
//
//                   listaVendaBaile.add(
//                           new GVendaBaile(
//                           eventoServicoValor.getEventoServico().getServicos(),
//                           mapa.getMesa(),
//                           calcularValor(eventoServicoValor.getEventoServico().getServicos()),
//                           false,
//                           mapa.getMesa().getbVenda(),
//                           null,
//                           false
//                           )
//                   );
//               }else if(indiceStatus == 1 ){
//                   if(mapa.getMesa().getbVenda() == null){
//                       mapa.getMesa().setbVenda(new BVenda(
//                           -1,
//                           eventoBaile.getEvento(),
//                           null,
//                           pessoa,
//                           titular(),
//                           usuario,
//                           ""
//                       ));
//                   }
//                   for(int i = 0; i < listaVendaBaile.size(); i++){
//                       if(listaVendaBaile.get(i).getMesa().getNrMesa() == mapa.getMesa().getNrMesa()){
//                           listaVendaBaile.remove(i);
//                       }
//                   }
//                   removerMovimento(mapa.getMesa(), mapa.getMesa().getbVenda());
//               }
//
//               if(!gravarMesa(mapa.getMesa(), mapa.getMesa().getbVenda(), mapa.getEventoServicoValor(), indiceStatus)){
//                   mapa.getMesa().setStatus(mapa.getStatusAnterior());
//               }
//            }
//        }
//    }
//
//    private boolean removerMovimento(Mesa mesa, BVenda aVenda){
//        MesaDB mesaDB = new MesaDBToplink();
//        LoteDB loteDB = new LoteDBToplink();
//        EvtDB evtDB = new EvtDBToplink();
//        boolean result = true;
//        Evt evt = null;
//        int idEvt = -1;
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        BVendaDB aVendaDB = new BVendaDBToplink();
//        Movimento movimento = movimento = mesaDB.pesquisaMovimentoPorVenda(aVenda.getId());
//        if(movimento != null){
//            movimento = financeiroDB.pesquisaCodigo(movimento);
//            Lote lote = movimento.getLote();
//            lote = loteDB.pesquisaCodigo(lote.getId());
//            financeiroDB.abrirTransacao();
//            try{
//                financeiroDB.delete(movimento);
//                financeiroDB.comitarTransacao();
//            }catch(Exception e){
//                financeiroDB.desfazerTransacao();
//            }
//            loteDB.delete(lote);
//            aVenda = aVendaDB.pesquisaCodigo(aVenda.getId());
//            idEvt = aVenda.getEvt().getId();
//            aVenda.setEvt(null);
//            aVendaDB.update(aVenda);
//            evt = evtDB.pesquisaCodigo(idEvt);
//            evtDB.delete(evt);
//            if(mesa != null){
//                mesaDB.update(mesa);
//                mesa.getbVenda().setEvt(null);
//            }
//            evt = new Evt();
//        }else{
//            result = false;
//        }
//        aVenda = null;//new BVenda();
//        return result;
//    }
//
//    private void removerMesa(Mesa mesa){
//        int numero = mesa.getNrMesa();
//        BVendaDB bVendaDB = new BVendaDBToplink();
//        BVenda aVenda = new BVenda();
//        MesaDB mesaDB = new MesaDBToplink();
//        removerMovimento(mesa, mesa.getbVenda());
//        mesa = mesaDB.pesquisaCodigo(mesa.getId());
//        mesaDB.delete(mesa);
//        aVenda = bVendaDB.pesquisaCodigo(mesa.getbVenda().getId());
//        bVendaDB.delete(aVenda);
//        mesa = new Mesa(-1, null, listaStatus.get(0) , numero);
//    }
//
//    private void removerConvite(BVenda aVenda){
//        BVendaDB bVendaDB = new BVendaDBToplink();
//        removerMovimento(null, aVenda);
//        aVenda = bVendaDB.pesquisaCodigo(aVenda.getId());
//        bVendaDB.delete(aVenda);
//        aVenda = new BVenda();
//    }
//
//    private Pessoa titular(){
//        Pessoa responsavel = null;
//        if(titular.getId() == -1){
//            responsavel = pessoa;
//        }else{
//            responsavel = titular;
//        }
//        return responsavel;
//    }
//
//
//    private boolean gravarMesa(Mesa mesa, BVenda aVenda, EventoServicoValor eventoServicoValor, int indice){
//        boolean result = true;
//        BVendaDB bVendaDB = new BVendaDBToplink();
//        FilialDB filialDB = new FilialDBToplink();
//        TipoServicoDB tipoServicoDB = new TipoServicoDBToplink();
//        RotinaDB rotinaDB = new RotinaDBToplink();
//        FinanceiroDB financeiroDB = new FinanceiroDBToplink();
//        MesaDB mesaDB = new MesaDBToplink();
//        EvtDB evtDB = new EvtDBToplink();
//        Evt evt = new Evt();
//        Movimento movimento = null;
//        Lote lote = null;
//        String vencimento =  (new DataHoje()).incrementarMeses(1, DataHoje.data());
//
//        try{
//            if(mesa.getId() == -1){
//                if(indice == 2){
//                    if(aVenda.getEvt() != null){
//                        evt = evtDB.pesquisaCodigo(aVenda.getEvt().getId());
//                        if(evt == null){
//                            evt = new Evt();
//                        }
//                    }
//                    evtDB.insert(evt);
//                    aVenda.setEvt(evt);
//                    lote = new Lote(-1, rotinaDB.pesquisaCodigo(141), "P", vencimento);
//                    financeiroDB.insert(lote);
//                    movimento = new Movimento(
//                            -1,
//                            lote,
//                            eventoServicoValor.getEventoServico().getServicos(),
//                            calcularValor(eventoServicoValor.getEventoServico().getServicos()),
//                            1,
//                            eventoServicoValor.getEventoServico().getServicos().getPlano5Debito(),
//                            filialDB.pesquisaCodigo(1),
//                            aVenda.getPessoa(),
//                            vencimento,
//                            vencimento.substring(3),
//                            tipoServicoDB.pesquisaCodigo(1),
//                            1,
//                            "D",
//                            null,
//                            evt,
//                            pessoa,
//                            mesa.getNrMesa()
//                            );
//                    financeiroDB.insert(movimento);
//                }
//                bVendaDB.insert(aVenda);
//                mesa.setbVenda(aVenda);
//                mesa.setStatus(null);
//                mesaDB.insert(mesa);
//                mesa.setStatus(new AStatus());
//            }else{
//                bVendaDB.update(aVenda);
//                mesaDB.update(mesa);
//            }
//        }catch (Exception e){
//            result = false;
//        }
//        return result;
//    }
//
//    public float calcularValor(Servicos servico){
//        float result = 0;
//        SociosDB socioDB = new SociosDBToplink();
//        if(titular.getId() == -1){
//            socioDB.descontoSocioEve(pessoa.getId(), servico.getId());
//        }else{
//            socioDB.descontoSocioEve(titular.getId(), servico.getId());
//        }
//        return result;
//    }
//
//    public void refreshForm(){
//
//    }
//
//    public void alterarBaile(){
//        mapaBaile.clear();
//        listaVendaBaile.clear();
//    }
//
//
//    public void excluirMovimento(){
//
//    }
//
//    public synchronized void removerConvite(){
//        int i = htmlTable.getRowIndex();
//        GVendaBaile gVendaBaile = listaVendaBaile.get(i);
//        listaVendaBaile.remove(i);
//        this.removerConvite(gVendaBaile.getbVenda());
//    }
//
//
//    public synchronized  void venderConvite(){
//        Usuario usuario = ((Usuario) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("sessaoUsuario"));
//        if(!listaEventoServicoValor.isEmpty()){
//            EventoServicoValorDB eventoServicoValorDB = new EventoServicoValorDBToplink();
//            EventoServicoValor eventoServicoValor = eventoServicoValorDB.pesquisaCodigo(Integer.parseInt(this.getListaEventoServicoValorConvite().get(idEventoServicoValorConvite).getDescription()));
//            for(int i = 0; i < listaVendaBaile.size(); i++){
//                if(listaVendaBaile.get(i).getServicos().getId() == eventoServicoValor.getId()){
//                    return;
//                }
//            }
//            listaVendaBaile.add(
//                    new GVendaBaile(
//                        eventoServicoValor.getEventoServico().getServicos(),
//                        new Mesa() ,
//                        calcularValor(eventoServicoValor.getEventoServico().getServicos()),
//                        false,
//                        new BVenda(
//                            -1,
//                            eventoBaile.getEvento(),
//                            null,
//                            pessoa,
//                            titular(),
//                            usuario,
//                            ""
//                        ),
//                        null,
//                        true
//                    )
//            );
//        }
//    }
//
//    public List<MapaBaile>  getMapa(){
//        if(mapaBaile.isEmpty()){
//            MesaDB mesaDB = new MesaDBToplink();
//            atualizarEventoBaile();
//            List<Mesa> lista = mesaDB.pesquisaTodosPorEvento(eventoBaile.getEvento().getId());
//            if(lista == null){
//                lista = new ArrayList();
//            }
//            Mesa mesa = null;
//            for(int i = 0; i < getTotalMesas(); i++){
//                mesa = new Mesa(-1, null, listaStatus.get(0) , i + 1);
//                if(!lista.isEmpty()){
//                    for(int j = 0; j < lista.size() ;j++){
//                        if(lista.get(j).getNrMesa() == (i+1)){
//                            mesa = lista.get(j);
//                        }
//                    }
//                }
//                mapaBaile.add(new MapaBaile(mesa, null));
//            }
//        }
//        return mapaBaile;
//    }
//
//    public synchronized void selecionar(){
//        MapaBaile mapa = (MapaBaile) getHtmlGridMapa().getRowData();
//        mapa.setStatusAnterior(mapa.getMesa().getStatus());
//        mapa.getMesa().setStatus(listaStatus.get(3));
//    }
//
//    public int getTotalMesas(){
//        carregarEventoBaile();
//        return eventoBaile.getQuantidadeMesas();
//    }
//
//    public float getProporcaoAltura(){
//        return ((getTotalMesas() / 25) * 26) + 215;
//    }
//
//    private void carregarEventoBaile(){
//        if((getListaEventoBaile().size() > 0) &&
//          (eventoBaile.getId() == -1)){
//            atualizarEventoBaile();
//        }
//    }
//
//    public void atualizarEventoBaile(){
//        if(!listaEventoBaile.isEmpty()){
//            EventoBaileDB db = new EventoBaileDBToplink();
//            int id = Integer.parseInt(this.listaEventoBaile.get(idEventoBaile).getDescription());
//            eventoBaile = db.pesquisaCodigo(id);
//        }
//    }
//
//    public List<SelectItem> getListaEventoBaile(){
//        if(listaEventoBaile.isEmpty()){
//            int i = 0, j = 0;
//            EventoBaileDB db = new EventoBaileDBToplink();
//            List<EventoBaile> select = db.pesquisaTodos();
//            if (!select.isEmpty()){
//                while (i < select.size()){
//                    listaEventoBaile.add(new SelectItem(
//                           new Integer(j),
//                           select.get(i).getEvento().getDescricaoEvento().getDescricao() + " -  " +
//                           select.get(i).getDataString() + " - (" +
//                           select.get(i).getHoraInicio() + " as  " +
//                           select.get(i).getHoraFim() + ")   " +
//                           select.get(i).getQuantidadeMesas() + " mesas  ",
//                           Integer.toString(( select.get(i)).getId())  ));
//                    j++;
//                   i++;
//                }
//            }
//        }
//        return listaEventoBaile;
//    }
//
//    public List<SelectItem> getListaEventoServicoValor(boolean isMesa){
//        List lista = new ArrayList();
//        int i = 0;
//        EventoServicoValorDB db = new EventoServicoValorDBToplink();
//        carregarEventoBaile();
//        if(listaEventoServicoValorObjeto.isEmpty()){
//            List<EventoServicoValor> select = db.pesquisaServicoValorPorEvento(eventoBaile.getEvento().getId());
//            listaEventoServicoValorObjeto = select;
//        }
//        if (!listaEventoServicoValorObjeto.isEmpty()){
//            try{
//                while (i < listaEventoServicoValorObjeto.size()){
//                    listaEventoServicoValor.add(new SelectItem(
//                           new Integer(i),
//                           listaEventoServicoValorObjeto.get(i).getEventoServico().getServicos().getDescricao() + " - "+
//                           listaEventoServicoValorObjeto.get(i).getValor() + " - " +
//                           sexoExtenso(listaEventoServicoValorObjeto.get(i)) + " ( " +
//                           listaEventoServicoValorObjeto.get(i).getIdadeInicial() + " - " +
//                           listaEventoServicoValorObjeto.get(i).getIdadeFinal() + " )",
//                           Integer.toString(( listaEventoServicoValorObjeto.get(i)).getId())  ));
//                    if(isMesa){
//                        if((listaEventoServicoValorObjeto.get(i).getEventoServico().getServicos().getId() == 12) || (listaEventoServicoValorObjeto.get(i).getEventoServico().getServicos().getId() == 13)){
//                            lista.add(listaEventoServicoValor.get(i));
//                        }
//                    }else{
//                        if((listaEventoServicoValorObjeto.get(i).getEventoServico().getServicos().getId() == 14) || (listaEventoServicoValorObjeto.get(i).getEventoServico().getServicos().getId() == 15)){
//                            lista.add(listaEventoServicoValor.get(i));
//                        }
//                    }
//                   i++;
//                }
//            }catch(Exception e){
//                String ee = e.getMessage();
//            }
//        }
//
//        return lista;
//    }
//
//    public List<SelectItem> getListaEventoServicoValorMesa(){
//        return this.getListaEventoServicoValor(true);
//    }
//
//    public List<SelectItem> getListaEventoServicoValorConvite(){
//        return this.getListaEventoServicoValor(false);
//    }
//
//    public boolean getDesabilitarMapa(){
//        if(getPessoa().getId() == -1){
//            return true;
//        }else{
//            return false;
//        }
//    }
//
//    private String sexoExtenso(EventoServicoValor eventoServicoValor){
//        if(!eventoServicoValor.getEventoServico().isMesa()){
//            if(eventoServicoValor.getSexo().equals("M")){
//                return "Masculino";
//            }else if(eventoServicoValor.getSexo().equals("F")){
//                return "Feminino";
//            }else if(eventoServicoValor.getSexo().equals("A")){
//                return "Ambos";
//            }
//        }
//        return "";
//    }
//
//    public void atualizarListaDeBaile(){
//        listaEventoBaile.clear();
//    }
//
//    public Pessoa getPessoaPesquisa(){
//        Fisica fisica = new Fisica();
//        try{
//            fisica = (Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa");
//            if (fisica == null){
//                return new Pessoa();
//            }else{
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
//                return fisica.getPessoa();
//            }
//        }catch(Exception e){
//            return fisica.getPessoa();
//        }
//    }
//
//    public Pessoa getPessoa() {
//        Pessoa p = getPessoaPesquisa();
//        if(p.getId() != -1){
//            pessoa = p;
//            SociosDB socioDB = new SociosDBToplink();
//            Socios socios = socioDB.pesquisaSocioDoDependente(pessoa.getId());
//            if(socios != null){
////                titular = socios.getMatriculaSocios().getResponsavel().getTitular();
//            }
//        }
//        return pessoa;
//    }
//
//    public String baixarMovimentos(){
//        OperacaoMovimento opMovimentos = new OperacaoMovimento(new ArrayList<Movimento>());
//        if (!listaVendaBaile.isEmpty()){
//            List<Movimento> movto = new ArrayList<Movimento>();
//            for(GVendaBaile gVendaBaile : listaVendaBaile){
//                if(gVendaBaile.getMarcar()){
//                    movto.add(gVendaBaile.getMovimento());
//                }
//            }
//            if(!movto.isEmpty()){
//                opMovimentos.setLista(movto);
//                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("arrayBaixa", opMovimentos);
//                return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
//            }
//        }
//        return ((chamadaPaginaJSFBean)FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).vendasBaile();
//    }
//
//    public Pessoa getTitular() {
//        return titular;
//    }
//
//    public String getMsgConfirma() {
//        return msgConfirma;
//    }
//
//    public void setMsgConfirma(String msgConfirma) {
//        this.msgConfirma = msgConfirma;
//    }
//
//    public HtmlDataTable getHtmlTable() {
//        return htmlTable;
//    }
//
//    public void setHtmlTable(HtmlDataTable htmlTable) {
//        this.htmlTable = htmlTable;
//    }
//
//    public BVenda getbVenda() {
//        return bVenda;
//    }
//
//    public void setbVenda(BVenda bVenda) {
//        this.bVenda = bVenda;
//    }
//
//    public int getIdEventoBaile() {
//        return idEventoBaile;
//    }
//
//    public void setIdEventoBaile(int idEventoBaile) {
//        this.idEventoBaile = idEventoBaile;
//    }
//
//    public void setListaEventoBaile(List<SelectItem> listaEventoBaile) {
//        this.listaEventoBaile = listaEventoBaile;
//    }
//
//    public EventoBaile getEventoBaile() {
//        return eventoBaile;
//    }
//
//    public void setEventoBaile(EventoBaile eventoBaile) {
//        this.eventoBaile = eventoBaile;
//    }
//
//    public boolean isTodos() {
//        return todos;
//    }
//
//    public void setTodos(boolean todos) {
//        this.todos = todos;
//    }
//
//    public void setPessoa(Pessoa pessoa) {
//        this.pessoa = pessoa;
//    }
//
//    public void setListaEventoServicoValor(List<SelectItem> listaEventoServico) {
//        this.listaEventoServicoValor = listaEventoServico;
//    }
//
//    public int getIdEventoServicoValor() {
//        return idEventoServicoValor;
//    }
//
//    public void setIdEventoServicoValor(int idEventoServicoValor) {
//        this.idEventoServicoValor = idEventoServicoValor;
//    }
//
//    public List<GVendaBaile> getListaVendaBaile() {
//        if(listaVendaBaile.isEmpty()){
//            this.atualizarEventoBaile();
//            EvtDB evtDB = new EvtDBToplink();
//            List<Movimento> lista = evtDB.pesquisaMovimentoEvt(eventoBaile.getEvento().getId(), pessoa.getId());
//            if(lista != null){
//               if(!lista.isEmpty()) {
//                   MesaDB mesaDB = new MesaDBToplink();
//                   BVendaDB bVendaDB = new BVendaDBToplink();
//                   Mesa mesa = null;
//                   BVenda aVenda = null;
//                   boolean remover = false;
//                   for(Movimento movimento : lista){
//                       aVenda = bVendaDB.pesquisaVendaPorEvt(movimento.getEvt().getId());
//                       mesa = mesaDB.pesquisaVendaPorEvt(aVenda.getId());
//                       if(mesa == null){
//                           remover = true;
//                       }else{
//                           remover = false;
//                       }
//                       listaVendaBaile.add( new GVendaBaile(movimento.getServicos(), mesa, movimento.getValor(), false, aVenda, movimento, remover) );
//                   }
//               }
//            }
//        }
//
//        return listaVendaBaile;
//    }
//
//    public void setListaVendaBaile(List<GVendaBaile> listaVendaBaile) {
//        this.listaVendaBaile = listaVendaBaile;
//    }
//
//    public List<MapaBaile> getMapaBaile() {
//        return mapaBaile;
//    }
//
//    public void setMapaBaile(List<MapaBaile> mapaBaile) {
//        this.mapaBaile = mapaBaile;
//    }
//
//    public HtmlDataGrid getHtmlGridMapa() {
//        return htmlGridMapa;
//    }
//
//    public void setHtmlGridMapa(HtmlDataGrid htmlGridMapa) {
//        this.htmlGridMapa = htmlGridMapa;
//    }
//
//    public int getIdEventoServicoValorConvite() {
//        return idEventoServicoValorConvite;
//    }
//
//    public void setIdEventoServicoValorConvite(int idEventoServicoValorConvite) {
//        this.idEventoServicoValorConvite = idEventoServicoValorConvite;
//    }
//
//    public void setTitular(Pessoa titular) {
//        this.titular = titular;
//    }
//
//
//}
