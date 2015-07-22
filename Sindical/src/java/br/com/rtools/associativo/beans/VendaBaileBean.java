package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.AStatus;
import br.com.rtools.associativo.BVenda;
import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.EventoBaileConvite;
import br.com.rtools.associativo.EventoBaileMapa;
import br.com.rtools.associativo.EventoServicoValor;
import br.com.rtools.associativo.dao.VendaBaileDao;
import br.com.rtools.associativo.db.EventoBaileDB;
import br.com.rtools.associativo.db.EventoBaileDBToplink;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.FinanceiroDB;
import br.com.rtools.financeiro.db.FinanceiroDBToplink;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.PessoaComplemento;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.seguranca.controleUsuario.ControleAcessoBean;
import br.com.rtools.utilitarios.AutenticaUsuario;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import br.com.rtools.utilitarios.db.FunctionsDao;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class VendaBaileBean implements Serializable {

    private List<SelectItem> listaEventoBaile = new ArrayList();
    private List<EventoBaileMapa> listaMesasBaile = new ArrayList();
    private List<EventoBaileMapa> listaMesasBaileSelecionada = new ArrayList();
    private List<EventoBaileConvite> listaConviteBaile = new ArrayList();
    private List<EventoBaileConvite> listaConviteBaileSelecionado = new ArrayList();
    private List<SelectItem> listaTipoVenda = new ArrayList();
    //private int idMesaBaile = 0;
    private int indexEventoBaile = 0;
    private Pessoa pessoa = new Pessoa();
    private BVenda venda = new BVenda();
    private Boolean todos = false;

    private String mesaConvite = "mesa";
    private String tipoVenda = "venda";

    private Integer idServicos = 0;
    private List<SelectItem> listaServicos = new ArrayList();

    private String valor = "0,00";
    private String desconto = "0,00";
    private String valorLiquido = "0,00";
    private String total = "0,00";
    private Integer quantidade = 0;

    private List<Vector> listaVendasMesa = new ArrayList();
    private List<Vector> listaVendasConvite = new ArrayList();
    private ControleAcessoBean cab = new ControleAcessoBean();
    private String novoDesconto = "0,00";
    private String tipoPagamento = "avista";

    private Pessoa responsavel = new Pessoa();
    private EventoServicoValor eventoServicoValor = new EventoServicoValor();
    private String descricaoServico = "NENHUM SERVIÇO CORRESPONDENTE";
    private boolean chkCortesia = false;
    private EventoBaile eventoBaile = new EventoBaile();

    private List<Movimento> listaMovimento = new ArrayList();
    
    @PostConstruct
    public void init() {
        loadListaEventoBaile();
        loadListaMesa();
        loadListaServicos();
        loadListaConvite();
        loadListaTipoVenda();

        GenericaSessao.remove("fisicaPesquisa");
        
        cab = (ControleAcessoBean) GenericaSessao.getObject("controleAcessoBean");
    }

    public String pesquisaVendas() throws IOException {
        loadListaVendasMesa();
        loadListaVendasConvite();

        return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).pagina("pesquisaVendasBaile");
    }

    public void adicionarDesconto() {
        desconto = novoDesconto;
        PF.closeDialog("dlg_autentica_usuario");
        PF.closeDialog("dlg_desconto");
        novoDesconto = "0,00";
        PF.update("formVendasBaile");
        PF.update("formVendasBaileTbl");
    }

    public void autorizarDesconto() {
        GenericaSessao.put("AutenticaUsuario", new AutenticaUsuario("dlg_desconto", "descontoVendasBaile", 3));
    }

    public void loadListaVendasMesa() {
        listaVendasMesa.clear();
        VendaBaileDao dao = new VendaBaileDao();
        listaVendasMesa = dao.listaVendasMesa(eventoBaile.getEvento().getId());
    }

    public void loadListaVendasConvite() {
        listaVendasConvite.clear();
        VendaBaileDao dao = new VendaBaileDao();
        listaVendasConvite = dao.listaVendasConvite(eventoBaile.getEvento().getId());
    }

    public void calculoDesconto() {
        if (Moeda.converteUS$(valor) <= 0) {
            desconto = "0,00";
        }
        valorLiquido = Moeda.converteR$Float(Moeda.converteUS$(valor) - Moeda.converteUS$(desconto));
        total = Moeda.converteR$Float(Moeda.converteUS$(valorLiquido) * getQuantidade());
    }

    public void somaValor() {
        if (eventoServicoValor != null) {
            valor = Moeda.converteR$Float(eventoServicoValor.getValor());
        }

        calculoDesconto();
    }

    public void loadListaServicos() {
        listaServicos.clear();
        VendaBaileDao dao = new VendaBaileDao();
        if (!listaEventoBaile.isEmpty()) {
            EventoBaile eb = (EventoBaile) new Dao().find(new EventoBaile(), Integer.valueOf(listaEventoBaile.get(indexEventoBaile).getDescription()));
            List<EventoServicoValor> result = new ArrayList();
            if (pessoa.getId() == -1) {
                eventoServicoValor = new EventoServicoValor();
                descricaoServico = "NENHUM SERVIÇO CORRESPONDENTE";
            } else {
                String sx = "A";
                Integer idade = 0, id_categoria = null;

                if (pessoa.getSocios() != null && pessoa.getSocios().getId() != -1) {
                    sx = pessoa.getFisica().getSexo();
                    DataHoje dh = new DataHoje();
                    idade = dh.calcularIdade(pessoa.getFisica().getDtNascimento());
                    id_categoria = pessoa.getSocios().getMatriculaSocios().getCategoria().getId();
                }

                Integer id_servicos = 0;
                if (mesaConvite.equals("mesa")) {
                    if (chkCortesia) {
                        id_servicos = 13;
                        id_categoria = null;
                    } else {
                        id_servicos = 12;
                    }
                } else {
                    if (chkCortesia) {
                        id_servicos = 15;
                        id_categoria = null;
                    } else {
                        id_servicos = 14;
                    }
                }

                eventoServicoValor = dao.pesquisaEventoServicoValor(
                        eb.getEvento().getId(),
                        id_categoria,
                        sx,
                        idade,
                        id_servicos,
                        mesaConvite.equals("mesa")
                );

                if (eventoServicoValor != null) {
                    String dc = eventoServicoValor.getEventoServico().isSocio() ? eventoServicoValor.getEventoServico().getCategoria().getCategoria() + " - " : "";
                    String ds = eventoServicoValor.getEventoServico().getServicos().getDescricao();// + " - " + descricaoCategoria + result.get(i).getIdadeInicial() + " à " + result.get(i).getIdadeFinal() + "(" + result.get(i).getSexo() + ") R$ " + result.get(i).getValorString();
                    descricaoServico = dc + ds;
                } else {
                    eventoServicoValor = new EventoServicoValor();
                    descricaoServico = "SERVIÇO VALOR NÃO ENCONTRADO";
                }
            }
        }

        zeraValores();
        somaValor();
    }

    public void updateTipoVenda() {
        loadListaTipoVenda();
        loadListaServicos();
        loadListaMesa();
        loadListaConvite();
        zeraValores();
        somaValor();
        eventoBaile = (EventoBaile) new Dao().find(new EventoBaile(), Integer.valueOf(listaEventoBaile.get(indexEventoBaile).getDescription()));
    }

    public void zeraValores() {
        valor = "0,00";
        valorLiquido = "0,00";
        desconto = "0,00";
        total = "0,00";
    }

    public void loadListaMesa() {
        listaMesasBaile.clear();
        listaMesasBaileSelecionada.clear();
        if (!listaEventoBaile.isEmpty()) {
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            if (tipoVenda.equals("venda") && venda.getId() == -1) {
                listaMesasBaile = (List<EventoBaileMapa>) eventoBaileDB.listaBaileMapaDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()), 1, null, null);
            } else if (tipoVenda.equals("vendaReserva")){
                listaMesasBaile = (List<EventoBaileMapa>) eventoBaileDB.listaBaileMapaDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()), 2, pessoa.getId(), venda.getId());
                listaMesasBaileSelecionada.addAll(listaMesasBaile);
            }else{
                listaMesasBaile = (List<EventoBaileMapa>) eventoBaileDB.listaBaileMapaDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()), 3, pessoa.getId(), venda.getId());
                listaMesasBaileSelecionada.addAll(listaMesasBaile);
            }
        }
    }

    public void loadListaConvite() {
        listaConviteBaile.clear();
        listaConviteBaileSelecionado = (listaConviteBaileSelecionado == null ? new ArrayList() : new ArrayList());
        if (!listaEventoBaile.isEmpty()) {
            EventoBaileDB eventoBaileDB = new EventoBaileDBToplink();
            if (venda.getId() == -1) {
                listaConviteBaile = (List<EventoBaileConvite>) eventoBaileDB.listaBaileConviteDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()), 1, null, null);
            }else{
                listaConviteBaile = (List<EventoBaileConvite>) eventoBaileDB.listaBaileConviteDisponiveis(Integer.parseInt(listaEventoBaile.get(indexEventoBaile).getDescription()), 3, pessoa.getId(), venda.getId());
            }
        }
    }

    public void loadListaEventoBaile() {
        listaEventoBaile.clear();
        VendaBaileDao dao = new VendaBaileDao();
        List<EventoBaile> result = dao.listaBaile(todos);
        if (!result.isEmpty()) {
            for (int i = 0; i < result.size(); i++) {
                listaEventoBaile.add(new SelectItem(
                        i,
                        result.get(i).getEvento().getDescricaoEvento().getDescricao() + " -  "
                        + result.get(i).getDataString() + " - ("
                        + result.get(i).getHoraInicio() + " às  "
                        + result.get(i).getHoraFim() + ")   "
                        + result.get(i).getQuantidadeMesas() + " mesas  / " + result.get(i).getQuantidadeConvites() + " convites",
                        Integer.toString((result.get(i)).getId())
                )
                );
            }
            eventoBaile = (EventoBaile) new Dao().find(new EventoBaile(), Integer.valueOf(listaEventoBaile.get(indexEventoBaile).getDescription())); //result.get(0);
        }
        loadListaServicos();
    }

    public void loadListaTipoVenda() {
        listaTipoVenda.clear();
        if (mesaConvite.equals("mesa")) {
            listaTipoVenda.add(new SelectItem("venda", "Venda"));
            listaTipoVenda.add(new SelectItem("reserva", "Reserva"));
            listaTipoVenda.add(new SelectItem("vendaReserva", "Venda Reserva"));
        } else {
            listaTipoVenda.add(new SelectItem("venda", "Venda"));
        }
    }

    public void concluir() {
        if (pessoa.getId() == -1) {
            GenericaMensagem.warn("Atenção", "Pesquise uma pessoa para salvar esta venda!");
            PF.update("formVendasBaile");
            return;
        }

        if (eventoServicoValor == null) {
            GenericaMensagem.warn("Atenção", "Nenhum Serviço foi encontrado!");
            PF.update("formVendasBaile");
            return;
        }

        if (mesaConvite.equals("mesa")) {
            if (listaMesasBaileSelecionada.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Selecione pelo menos uma MESA para reservar!");
                PF.update("formVendasBaile");
                return;
            }

            for (EventoBaileMapa m : listaMesasBaileSelecionada) {
                EventoBaileMapa ebm = new VendaBaileDao().pesquisaMesaDisponivel(m.getId());
                if (ebm.getStatus().getId() == 3) {
                    GenericaMensagem.warn("Atenção", "Mesa " + ebm.getMesa() + " acabou de ser vendida, selecione outra!");
                    loadListaMesa();
                    PF.update("formVendasBaile");
                    return;
                }
            }
        } else {
            if (listaConviteBaileSelecionado.isEmpty()) {
                GenericaMensagem.warn("Atenção", "Selecione pelo menos um CONVITE para vender!");
                PF.update("formVendasBaile");
                return;
            }

            for (EventoBaileConvite c : listaConviteBaileSelecionado) {
                EventoBaileConvite ebc = new VendaBaileDao().pesquisaConviteDisponivel(c.getId());
                if (ebc.getStatus().getId() == 3) {
                    GenericaMensagem.warn("Atenção", "Convite " + ebc.getConvite() + " acabou de ser vendido, selecione outro!");
                    loadListaConvite();
                    PF.update("formVendasBaile");
                    return;
                }
            }
        }

        if (eventoServicoValor.getEventoServico().getServicos().getId() == 13 || eventoServicoValor.getEventoServico().getServicos().getId() == 15) {
            if (pessoa.getSocios() == null || pessoa.getSocios().getId() == -1) {
                if (venda.getObs().isEmpty()) {
                    GenericaMensagem.warn("Atenção", "O campo Observação para cortesia não sócio é obrigatório!");
                    PF.update("formVendasBaile");
                    return;
                } else if (venda.getObs().length() <= 4) {
                    GenericaMensagem.warn("Atenção", "Observação inválida!");
                    PF.update("formVendasBaile");
                    return;
                } else if (venda.getObs().equals("TESTE") || venda.getObs().equals("Teste") || venda.getObs().equals("teste")) {
                    GenericaMensagem.warn("Atenção", "Observação inválida!");
                    PF.update("formVendasBaile");
                    return;
                }
            }
        }

        if (tipoVenda.equals("venda") || tipoVenda.equals("vendaReserva")) {
            if (tipoPagamento.equals("debitar")) {
                if (pessoa.getSocios() == null || pessoa.getSocios().getId() == -1) {
                    GenericaMensagem.warn("Atenção", "Para fazer um débito a pessoa precisa ser sócia!");
                    PF.update("formVendasBaile");
                    return;
                }
                
                if (new FunctionsDao().inadimplente(pessoa.getId())){
                    GenericaMensagem.warn("Atenção", "Pessoas com débitos pendentes!");
                    PF.update("formVendasBaile");
                    return;
                }
            }
            PF.openDialog("dlg_venda");
        } else if (tipoVenda.equals("reserva")) {
            PF.openDialog("dlg_reserva");
        }
    }

    public String salvar() {
        Dao dao = new Dao();

        dao.openTransaction();
        EventoBaile eb = (EventoBaile) new Dao().find(new EventoBaile(), Integer.valueOf(listaEventoBaile.get(indexEventoBaile).getDescription()));

        if (venda.getId() == -1) {
            venda.setEvento(eb.getEvento());
            venda.setPessoa(pessoa);
            venda.setResponsavel(responsavel);
            venda.setUsuario(Usuario.getUsuario());
            venda.setEventoServico(eventoServicoValor.getEventoServico());
            venda.setValorUnitarioString(valorLiquido);
            venda.setDescontoUnitarioString(desconto);

            if (!dao.save(venda)) {
                GenericaMensagem.error("Erro", "Não foi possível salvar Venda!");
                dao.rollback();
                return null;
            }
        } else {
            if (!dao.update(venda)) {
                GenericaMensagem.error("Erro", "Não foi possível atualizar Venda!");
                dao.rollback();
                return null;
            }
            
            dao.commit();
            GenericaMensagem.info("Sucesso", "Venda Atualizada!");
            return null;
        }

        switch (tipoVenda) {
            case "venda":
                if (Moeda.converteUS$(valorLiquido) > 0) {
                    if (!gerarMovimento(dao)) {
                        return null;
                    }
                }
                if (mesaConvite.equals("mesa")) {
                    for (int i = 0; i < listaMesasBaileSelecionada.size(); i++) {
                        EventoBaileMapa ebm = listaMesasBaileSelecionada.get(i);

                        ebm.setStatus((AStatus) dao.find(new AStatus(), 3));
                        ebm.setbVenda(venda);

                        if (!dao.update(ebm)) {
                            GenericaMensagem.error("Erro", "Não foi possível salvar Evento Baile Mapa!");
                            dao.rollback();
                            return null;
                        }
                    }
                } else {
                    for (int i = 0; i < listaConviteBaileSelecionado.size(); i++) {
                        EventoBaileConvite ebc = listaConviteBaileSelecionado.get(i);

                        ebc.setStatus((AStatus) dao.find(new AStatus(), 3));
                        ebc.setbVenda(venda);

                        if (!dao.update(ebc)) {
                            GenericaMensagem.error("Erro", "Não foi possível salvar Evento Baile Convite!");
                            dao.rollback();
                            return null;
                        }
                    }
                }
                break;
            case "reserva":
                for (int i = 0; i < listaMesasBaileSelecionada.size(); i++) {
                    EventoBaileMapa ebm = listaMesasBaileSelecionada.get(i);

                    ebm.setStatus((AStatus) dao.find(new AStatus(), 2));
                    ebm.setbVenda(venda);

                    if (!dao.update(ebm)) {
                        GenericaMensagem.error("Erro", "Não foi possível salvar Evento Baile Mapa!");
                        dao.rollback();
                        return null;
                    }
                }
                break;
            default:
                if (Moeda.converteUS$(valorLiquido) > 0) {
                    if (!gerarMovimento(dao)) {
                        return null;
                    }
                }
                for (int i = 0; i < listaMesasBaileSelecionada.size(); i++) {
                    EventoBaileMapa ebm = listaMesasBaileSelecionada.get(i);

                    ebm.setStatus((AStatus) dao.find(new AStatus(), 3));
                    ebm.setbVenda(venda);

                    if (!dao.update(ebm)) {
                        GenericaMensagem.error("Erro", "Não foi possível salvar Evento Baile Mapa!");
                        dao.rollback();
                        return null;
                    }
                }
                break;
        }

        GenericaMensagem.info("Sucesso", "Venda Concluída!");
        dao.commit();

        if (tipoPagamento.equals("debitar") || tipoVenda.equals("reserva")) {
            novo();
            return null;
        } else {
            return telaBaixa();
        }
    }

    public String editar(Vector linha) {
        venda = (BVenda) new Dao().find(new BVenda(), (Integer) linha.get(0));
        pessoa = venda.getPessoa();
        responsavel = new FunctionsDao().titularDaPessoa(pessoa.getId());
        
        VendaBaileDao dao = new VendaBaileDao();
        // LISTA EVENTO BAILE
        List<EventoBaileMapa> lm = dao.listaEventoBaileMapaPorVenda(venda.getId());
        List<EventoBaileConvite> lc = dao.listaEventoBaileConvitePorVenda(venda.getId());
        EventoBaile eb = new EventoBaile();
        if (!lm.isEmpty()) {
            eb = lm.get(0).getEventoBaile();
            mesaConvite = "mesa";
            tipoVenda = (lm.get(0).getStatus().getId() == 2 ? "vendaReserva" : "venda");
            quantidade = lm.size();
            loadListaMesa();
        } else if (!lc.isEmpty()) {
            eb = lc.get(0).getEventoBaile();
            mesaConvite = "convite";
            tipoVenda = "vendaReserva";
            quantidade = lc.size();
            loadListaConvite();
        }
        
        chkCortesia = (venda.getEventoServico().getServicos().getId() == 13 || venda.getEventoServico().getServicos().getId() == 13);

        loadListaEventoBaile();
        for (int i = 0; i < listaEventoBaile.size(); i++) {
            if (Integer.valueOf(listaEventoBaile.get(i).getDescription()) == eb.getId()) {
                indexEventoBaile = i;
                eventoBaile = eb;
                break;
            }
        }

        GenericaSessao.put("linkClicado", true);
        return (String) GenericaSessao.getString("urlRetorno");
    }

    public void excluir() {
        VendaBaileDao vdao = new VendaBaileDao();
        // LISTA EVENTO BAILE
        List<EventoBaileMapa> lm = vdao.listaEventoBaileMapaPorVenda(venda.getId());
        List<EventoBaileConvite> lc = vdao.listaEventoBaileConvitePorVenda(venda.getId());

        Dao dao = new Dao();
        dao.openTransaction();
        List<Movimento> listm = new ArrayList();
        if (!lm.isEmpty()) {
            for (EventoBaileMapa ebm : lm) {
                ebm.setStatus((AStatus) dao.find(new AStatus(), 1));
                ebm.setbVenda(null);

                if (ebm.getMovimento() != null) {
                    listm.add(ebm.getMovimento());
                    ebm.setMovimento(null);
                }

                if (!dao.update(ebm)) {
                    GenericaMensagem.error("Erro", "Não foi possível excluir Evento Baile Mapa!");
                    dao.rollback();
                    return;
                }
            }
            if (!listm.isEmpty()){
                if (!excluirMovimento(listm, dao)) {
                    GenericaMensagem.error("Erro", "Não foi possível excluir Movimentos!");
                    dao.rollback();
                    return;
                }
            }
        } else if (!lc.isEmpty()) {
            for (EventoBaileConvite ebc : lc) {
                ebc.setStatus((AStatus) dao.find(new AStatus(), 1));
                ebc.setbVenda(null);

                if (ebc.getMovimento() != null) {
                    listm.add(ebc.getMovimento());
                    ebc.setMovimento(null);
                }

                if (!dao.update(ebc)) {
                    GenericaMensagem.error("Erro", "Não foi possível excluir Evento Baile Convite!");
                    dao.rollback();
                    return;
                }
            }

            if (!listm.isEmpty()){
                if (!excluirMovimento(listm, dao)) {
                    GenericaMensagem.error("Erro", "Não foi possível excluir Movimentos!");
                    dao.rollback();
                    return;
                }
            }
        }

        if (!dao.delete(dao.find(venda))) {
            GenericaMensagem.error("Erro", "Não foi possível excluir Venda!");
            dao.rollback();
            return;
        }

        dao.commit();
        GenericaSessao.remove("vendaBaileBean");
        GenericaMensagem.info("Sucesso", "Venda Excluida!");
    }

    public boolean excluirMovimento(List<Movimento> listm, Dao dao) {
        Lote l = listm.get(0).getLote();

        for (Movimento m : listm) {
            if (m.getBaixa() != null) {
                GenericaMensagem.error("Erro", "Existem movimentos pagos, venda não pode ser excluída!");
                dao.rollback();
                return false;
            }
            if (!dao.delete(dao.find(m))) {
                GenericaMensagem.error("Erro", "Não foi possível excluir Movimento!");
                dao.rollback();
                return false;
            }
        }

        if (!dao.delete(dao.find(l))) {
            GenericaMensagem.error("Erro", "Não foi possível excluir Lote!");
            dao.rollback();
            return false;
        }
        return true;
    }

    public String novo() {
        //GenericaSessao.remove("vendaBaileBean");
        listaMesasBaile = new ArrayList();
        listaMesasBaileSelecionada = new ArrayList();
        listaConviteBaile = new ArrayList();
        listaConviteBaileSelecionado = new ArrayList();
        listaTipoVenda = new ArrayList();
        
        pessoa = new Pessoa();
        venda = new BVenda();
        todos = false;

        mesaConvite = "mesa";
        tipoVenda = "venda";

        idServicos = 0;
        listaServicos = new ArrayList();

        valor = "0,00";
        desconto = "0,00";
        valorLiquido = "0,00";
        total = "0,00";
        quantidade = 0;

        listaVendasMesa = new ArrayList();
        listaVendasConvite = new ArrayList();
        cab = new ControleAcessoBean();
        novoDesconto = "0,00";
        tipoPagamento = "avista";

        responsavel = new Pessoa();
        eventoServicoValor = new EventoServicoValor();
        descricaoServico = "NENHUM SERVIÇO CORRESPONDENTE";
        chkCortesia = false;

        listaMovimento = new ArrayList(); 
        
        init();
        return "vendasBaile";
    }

    public String telaBaixa() {
        List lista = new ArrayList();
        Movimento movimento;
        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");

        if (macFilial == null) {
            GenericaMensagem.warn("Atenção", "Não existe filial na sessão!");
            return null;
        }

        if (!macFilial.isCaixaOperador()) {
            if (macFilial.getCaixa() == null) {
                GenericaMensagem.warn("Atenção", "Configurar Caixa nesta estação de trabalho!");
                return null;
            }
        } else {
            FinanceiroDB dbf = new FinanceiroDBToplink();
            Caixa caixax = dbf.pesquisaCaixaUsuario(((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId());

            if (caixax == null) {
                GenericaMensagem.warn("Erro", "Configurar Caixa para este Operador!");
                return null;
            }
        }

        if (!chkCortesia){
            if (!listaMovimento.isEmpty()) {
                for (int i = 0; i < listaMovimento.size(); i++) {
                    movimento = (Movimento) listaMovimento.get(i);

                    movimento.setMulta(listaMovimento.get(i).getMulta());
                    movimento.setJuros(listaMovimento.get(i).getJuros());
                    movimento.setCorrecao(listaMovimento.get(i).getCorrecao());
                    movimento.setDesconto(listaMovimento.get(i).getDesconto());

                    movimento.setValor(listaMovimento.get(i).getValor());
                    movimento.setValorBaixa(listaMovimento.get(i).getValor());

                    lista.add(movimento);
                }

                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);

                GenericaSessao.put("caixa_banco", "caixa");
                return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();

            } else {
                GenericaMensagem.warn("Atenção", "Lista de Movimentos Vazia!");
            }
        }
        return null;
    }

    public boolean gerarMovimento(Dao dao) {
        PessoaComplemento pc = responsavel.getPessoaComplemento();
        DataHoje dh = new DataHoje();
        CondicaoPagamento cp = tipoPagamento.equals("avista") ? (CondicaoPagamento) new Dao().find(new CondicaoPagamento(), 1) : (CondicaoPagamento) new Dao().find(new CondicaoPagamento(), 2);
        String vencimento = tipoPagamento.equals("avista") ? DataHoje.data() : (pc.getNrDiaVencimento() < 9) ? "0" + pc.getNrDiaVencimento() : "" + pc.getNrDiaVencimento() + "/" + dh.incrementarMeses(1, DataHoje.data()).substring(3);
        
        listaMovimento.clear();
        
        Lote l = new Lote(
                -1,
                (Rotina) new Dao().find(new Rotina(), 141),
                "R",
                DataHoje.data(),
                responsavel,
                venda.getEventoServico().getServicos().getPlano5(),
                false,
                "",
                Moeda.converteUS$(total),
                (Filial) new Dao().find(new Filial(), 1),
                venda.getEventoServico().getServicos().getDepartamento(),
                venda.getEvt(),
                "",
                (FTipoDocumento) new Dao().find(new FTipoDocumento(), 13),
                cp,
                (FStatus) new Dao().find(new FStatus(), 1),
                null,
                false,
                0
        );

        if (!dao.save(l)) {
            GenericaMensagem.error("Error", "Nao foi possivel salvar Lote!");
            return false;
        }

        EventoBaile eb = (EventoBaile) new Dao().find(new EventoBaile(), Integer.valueOf(listaEventoBaile.get(indexEventoBaile).getDescription()));

        if (mesaConvite.equals("mesa")) {
            for (EventoBaileMapa mb : listaMesasBaileSelecionada) {
                Movimento m = movimento(l, vencimento, eb);
                if (!dao.save(m)) {
                    GenericaMensagem.error("Error", "Nao foi possivel salvar Movimento!");
                    listaMovimento.clear();
                    return false;
                }
                listaMovimento.add(m);

                mb.setMovimento(m);
                if (!dao.update(mb)) {
                    GenericaMensagem.error("Error", "Nao foi possivel alterar Evento Mesa Baile!");
                    listaMovimento.clear();
                    return false;
                }

            }
        } else {
            for (EventoBaileConvite cb : listaConviteBaileSelecionado) {
                Movimento m = movimento(l, vencimento, eb);
                if (!dao.save(m)) {
                    GenericaMensagem.error("Error", "Nao foi possivel salvar Movimento!");
                    listaMovimento.clear();
                    return false;
                }
                listaMovimento.add(m);

                cb.setMovimento(m);
                if (!dao.update(cb)) {
                    GenericaMensagem.error("Error", "Nao foi possivel alterar Evento Mesa Convite!");
                    listaMovimento.clear();
                    return false;
                }
            }
        }
        return true;
    }

    public Movimento movimento(Lote l, String vencimento, EventoBaile eb) {
        return new Movimento(
                -1,
                l,
                l.getPlano5(),
                pessoa,
                venda.getEventoServico().getServicos(),
                null, // ID_BAIXA
                (TipoServico) new Dao().find(new TipoServico(), 1),
                null,
                Moeda.converteUS$(valorLiquido),
                DataHoje.converteDataParaReferencia(eb.getDataString()),
                vencimento,
                1,
                true,
                "E",
                false,
                responsavel,
                pessoa,
                "",
                "",
                vencimento,
                0,
                0,
                0,
                0,
                0,
                0,
                0,
                (FTipoDocumento) new Dao().find(new FTipoDocumento(), 13),
                0,
                null
        );
    }

    public void removerPessoa() {
        pessoa = new Pessoa();
        responsavel = new Pessoa();
        loadListaServicos();
        loadListaMesa();
        loadListaConvite();
        zeraValores();
    }

    public void pesquisaNaoSocio() {
        pessoa = (Pessoa) new Dao().find(new Pessoa(), 1);
        responsavel = pessoa;
        loadListaServicos();
    }

    public String converteMoeda(float valorx) {
        return Moeda.converteR$Float(valorx);
    }

    public String converteData(Date datax) {
        return DataHoje.converteData(datax);
    }

    public List<SelectItem> getListaEventoBaile() {
        return listaEventoBaile;
    }

    public int getIndexEventoBaile() {
        return indexEventoBaile;
    }

    public void setIndexEventoBaile(int indexEventoBaile) {
        this.indexEventoBaile = indexEventoBaile;
    }

    public BVenda getVenda() {
        return venda;
    }

    public void setVenda(BVenda venda) {
        this.venda = venda;
    }

    public Boolean getTodos() {
        return todos;
    }

    public void setTodos(Boolean todos) {
        this.todos = todos;
    }

    public String getMesaConvite() {
        return mesaConvite;
    }

    public void setMesaConvite(String mesaConvite) {
        this.mesaConvite = mesaConvite;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("fisicaPesquisa")) {
            pessoa = ((Fisica) GenericaSessao.getObject("fisicaPesquisa", true)).getPessoa();
            responsavel = new FunctionsDao().titularDaPessoa(pessoa.getId());
            loadListaServicos();
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTipoVenda() {
        return tipoVenda;
    }

    public void setTipoVenda(String tipoVenda) {
        this.tipoVenda = tipoVenda;
    }

    public List<SelectItem> getListaTipoVenda() {
        return listaTipoVenda;
    }

    public void setListaTipoVenda(List<SelectItem> listaTipoVenda) {
        this.listaTipoVenda = listaTipoVenda;
    }

    public List<EventoBaileMapa> getListaMesasBaileSelecionada() {
        return listaMesasBaileSelecionada;
    }

    public void setListaMesasBaileSelecionada(List<EventoBaileMapa> listaMesasBaileSelecionada) {
        this.listaMesasBaileSelecionada = listaMesasBaileSelecionada;
    }

    public List<EventoBaileMapa> getListaMesasBaile() {
        return listaMesasBaile;
    }

    public void setListaMesasBaile(List<EventoBaileMapa> listaMesasBaile) {
        this.listaMesasBaile = listaMesasBaile;
    }

    public Integer getIdServicos() {
        return idServicos;
    }

    public void setIdServicos(Integer idServicos) {
        this.idServicos = idServicos;
    }

    public List<SelectItem> getListaServicos() {
        return listaServicos;
    }

    public void setListaServicos(List<SelectItem> listaServicos) {
        this.listaServicos = listaServicos;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getDesconto() {
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        this.desconto = Moeda.converteR$(desconto);
    }

    public String getValorLiquido() {
        return valorLiquido;
    }

    public void setValorLiquido(String valorLiquido) {
        this.valorLiquido = valorLiquido;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public Integer getQuantidade() {
        if (mesaConvite.equals("mesa")) {
            if (listaMesasBaileSelecionada != null) {
                quantidade = listaMesasBaileSelecionada.size();
            } else {
                quantidade = 0;
            }
        } else {
            if (listaConviteBaileSelecionado != null) {
                quantidade = listaConviteBaileSelecionado.size();
            } else {
                quantidade = 0;
            }
        }
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public List<Vector> getListaVendasMesa() {
        return listaVendasMesa;
    }

    public void setListaVendasMesa(List<Vector> listaVendasMesa) {
        this.listaVendasMesa = listaVendasMesa;
    }

    public ControleAcessoBean getCab() {
        return cab;
    }

    public void setCab(ControleAcessoBean cab) {
        this.cab = cab;
    }

    public String getNovoDesconto() {
        return novoDesconto;
    }

    public void setNovoDesconto(String novoDesconto) {
        this.novoDesconto = novoDesconto;
    }

    public List<EventoBaileConvite> getListaConviteBaile() {
        return listaConviteBaile;
    }

    public void setListaConviteBaile(List<EventoBaileConvite> listaConviteBaile) {
        this.listaConviteBaile = listaConviteBaile;
    }

    public List<EventoBaileConvite> getListaConviteBaileSelecionado() {
        return listaConviteBaileSelecionado;
    }

    public void setListaConviteBaileSelecionado(List<EventoBaileConvite> listaConviteBaileSelecionado) {
        this.listaConviteBaileSelecionado = listaConviteBaileSelecionado;
    }

    public String getTipoPagamento() {
        return tipoPagamento;
    }

    public void setTipoPagamento(String tipoPagamento) {
        this.tipoPagamento = tipoPagamento;
    }

    public Pessoa getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(Pessoa responsavel) {
        this.responsavel = responsavel;
    }

    public EventoServicoValor getEventoServicoValor() {
        return eventoServicoValor;
    }

    public void setEventoServicoValor(EventoServicoValor eventoServicoValor) {
        this.eventoServicoValor = eventoServicoValor;
    }

    public String getDescricaoServico() {
        return descricaoServico;
    }

    public void setDescricaoServico(String descricaoServico) {
        this.descricaoServico = descricaoServico;
    }

    public boolean isChkCortesia() {
        return chkCortesia;
    }

    public void setChkCortesia(boolean chkCortesia) {
        this.chkCortesia = chkCortesia;
    }

    public EventoBaile getEventoBaile() {
        return eventoBaile;
    }

    public void setEventoBaile(EventoBaile eventoBaile) {
        this.eventoBaile = eventoBaile;
    }

    public List<Vector> getListaVendasConvite() {
        return listaVendasConvite;
    }

    public void setListaVendasConvite(List<Vector> listaVendasConvite) {
        this.listaVendasConvite = listaVendasConvite;
    }
}
