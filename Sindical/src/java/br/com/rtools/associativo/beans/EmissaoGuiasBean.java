package br.com.rtools.associativo.beans;

import br.com.rtools.associativo.GrupoConvenio;
import br.com.rtools.associativo.HistoricoEmissaoGuias;
import br.com.rtools.associativo.MatriculaSocios;
import br.com.rtools.associativo.SubGrupoConvenio;
import br.com.rtools.associativo.db.ConvenioServicoDB;
import br.com.rtools.associativo.db.ConvenioServicoDBToplink;
import br.com.rtools.associativo.db.LancamentoIndividualDB;
import br.com.rtools.associativo.db.LancamentoIndividualDBToplink;
import br.com.rtools.associativo.db.SubGrupoConvenioDB;
import br.com.rtools.associativo.db.SubGrupoConvenioDBToplink;
import br.com.rtools.estoque.Estoque;
import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Pedido;
import br.com.rtools.estoque.Produto;
import br.com.rtools.estoque.dao.ProdutoDao;
import br.com.rtools.financeiro.CondicaoPagamento;
import br.com.rtools.financeiro.FStatus;
import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.Guia;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Servicos;
import br.com.rtools.financeiro.TipoServico;
import br.com.rtools.financeiro.db.MovimentoDB;
import br.com.rtools.financeiro.db.MovimentoDBToplink;
import br.com.rtools.financeiro.lista.ListMovimentoEmissaoGuias;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DaoInterface;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.PF;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class EmissaoGuiasBean implements Serializable {

    private Filial filial;
    private Pessoa pessoa;
    private Fisica fisica;
    private Lote lote;
    private int quantidade;
    private List<Movimento> listaMovimentoAuxiliar;
    private List<HistoricoEmissaoGuias> listHistoricoEmissaoGuias;
    private String valor;
    private String desconto;
    private String total;
    private String message;
    private boolean validaPessoa;

    // MODIFICAÇÕES BRUNO
    private boolean modalPedido;
    private int quantidadePedido;
    private String valorUnitarioPedido;
    private String descontoUnitarioPedido;
    private List<Pedido> listPedidos;
    private String valorTotal;
    private List<ListMovimentoEmissaoGuias> listaMovimento;
    private Juridica juridica;
    private Pedido pedido;
    private Estoque estoque;
    private boolean enabledItensPedido;
    /**
     * <ul>
     * <li> Indices </li>
     * <li>[0] Grupo </li>
     * <li>[1] Subgrupo </li>
     * <li>[2] Serviços </li>
     * <li>[3] Jurídica </li>
     * </ul>
     */
    private List<SelectItem>[] listSelectItem;
    private Integer[] index;

    /**
     * <ul>
     * <li> Variáveis </li>
     * <li>[0] Valor </li>
     * <li>[1] Desconto </li>
     * <li>[2] Total </li>
     * <li>[3] Mensagem </li>
     * <li>[4] Mensagem Filial</li>
     * </ul>
     */
    private String[] var;

    @PostConstruct
    public void init() {
        estoque = new Estoque();
        filial = MacFilial.getAcessoFilial().getFilial();
        pessoa = new Pessoa();
        fisica = new Fisica();
        lote = new Lote();
        quantidade = 1;
        listaMovimentoAuxiliar = new ArrayList<Movimento>();
        listHistoricoEmissaoGuias = new ArrayList<HistoricoEmissaoGuias>();
        valor = "";
        desconto = "";
        total = "";
        listaMovimento = new ArrayList<ListMovimentoEmissaoGuias>();
        validaPessoa = false;
        // MODIFICAÇÕES BRUNO
        modalPedido = false;
        quantidadePedido = 1;
        valorUnitarioPedido = "";
        descontoUnitarioPedido = "";
        listPedidos = new ArrayList<Pedido>();
        valorTotal = "0";
        message = "";
        juridica = new Juridica();
        pedido = new Pedido();
        listSelectItem = new ArrayList[]{
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>(),
            new ArrayList<SelectItem>()
        };
        enabledItensPedido = false;
        index = new Integer[]{0, 0, 0, 0};
        var = new String[]{"", "", "", "", ""};
    }

    @PreDestroy
    public void destroy() {
        GenericaSessao.remove("emissaoGuiasBean");
        GenericaSessao.remove("fisicaPesquisa");
        GenericaSessao.remove("pessoaPesquisa");
        GenericaSessao.remove("juridicaPesquisa");
        GenericaSessao.remove("produtoPesquisa");
        GenericaSessao.remove("listaMovimento");
    }

    public String baixarErrado(HistoricoEmissaoGuias heg) {
        if (!listHistoricoEmissaoGuias.isEmpty()) {
            listaMovimentoAuxiliar.clear();

            for (int i = 0; i < listHistoricoEmissaoGuias.size(); i++) {
                if (heg.getMovimento().getLote().getId() == listHistoricoEmissaoGuias.get(i).getMovimento().getLote().getId()) {
                    listaMovimentoAuxiliar.add(listHistoricoEmissaoGuias.get(i).getMovimento());
                }
            }
            listHistoricoEmissaoGuias.clear();
            GenericaSessao.put("listaMovimento", listaMovimentoAuxiliar);
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).baixaGeral();
        }
        return null;
    }

    public String excluirErrado(HistoricoEmissaoGuias heg) {
        if (!listHistoricoEmissaoGuias.isEmpty()) {
            listaMovimentoAuxiliar.clear();

            DaoInterface di = new Dao();
            MovimentoDB db = new MovimentoDBToplink();
            di.openTransaction();

            for (int i = 0; i < listHistoricoEmissaoGuias.size(); i++) {
                if (heg.getMovimento().getLote().getId() == listHistoricoEmissaoGuias.get(i).getMovimento().getLote().getId()) {
                    listaMovimentoAuxiliar.add(listHistoricoEmissaoGuias.get(i).getMovimento());
                    if (!di.delete(listHistoricoEmissaoGuias.get(i))) {
                        di.rollback();
                        return null;
                    }
                }
            }

            // GUIA
            Guia guias = db.pesquisaGuias(heg.getMovimento().getLote().getId());

            if (!di.delete(guias)) {
                di.rollback();
                return null;
            }

            // MOVIMENTO
            for (int i = 0; i < listaMovimentoAuxiliar.size(); i++) {
                if (!di.delete(listaMovimentoAuxiliar.get(i))) {
                    di.rollback();
                    return null;
                }
            }

            // LOTE
            if (!di.delete(heg.getMovimento().getLote())) {
                di.rollback();
                return null;
            }
            di.commit();
            listHistoricoEmissaoGuias.clear();
        }
        return "menuPrincipal";
    }

    public void atualizarHistorico() {
        DaoInterface di = new Dao();
        HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
        Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        MovimentoDB db = new MovimentoDBToplink();
        if (!listaMovimentoAuxiliar.isEmpty()) {
            for (int i = 0; i < listaMovimentoAuxiliar.size(); i++) {
                heg = db.pesquisaHistoricoEmissaoGuiasPorMovimento(usuario.getId(), listaMovimentoAuxiliar.get(i).getId());
                heg.setBaixado(true);
                di.openTransaction();
                if (di.update(heg)) {
                    di.commit();
                } else {
                    di.rollback();
                }
            }
        }
    }

    public List<HistoricoEmissaoGuias> getListHistoricoEmissaoGuias() {
        if (listHistoricoEmissaoGuias.isEmpty()) {
            MovimentoDB db = new MovimentoDBToplink();
            Usuario usuario = (Usuario) GenericaSessao.getObject("sessaoUsuario");
            listHistoricoEmissaoGuias = db.pesquisaHistoricoEmissaoGuias(usuario.getId());
        }
        return listHistoricoEmissaoGuias;
    }

    public void setListHistoricoEmissaoGuias(List<HistoricoEmissaoGuias> listHistoricoEmissaoGuias) {
        this.listHistoricoEmissaoGuias = listHistoricoEmissaoGuias;
    }

    /**
     * <ul>
     * <li>tcase = 3 => Nova pessoa </li>
     * </ul>
     *
     * @param tcase
     */
    public void clear(int tcase) {
        switch (tcase) {
            case 1:
                listSelectItem[0] = new ArrayList<SelectItem>();
                listSelectItem[2] = new ArrayList<SelectItem>();
                listSelectItem[3] = new ArrayList<SelectItem>();
                index[0] = 0;
                index[2] = 0;
                index[3] = 0;
                getListSubGrupo();
                getListServicos();
                getListJuridica();
                listenerEnabledItensPedido();
                break;
            case 2:
                listSelectItem[0] = new ArrayList<SelectItem>();
                ;
                listSelectItem[2] = new ArrayList<SelectItem>();
                listSelectItem[3] = new ArrayList<SelectItem>();
                index[2] = 0;
                index[3] = 0;
                getListSubGrupo();
                getListServicos();
                getListJuridica();
                listenerEnabledItensPedido();
                break;
            case 3:
                pessoa = new Pessoa();
                juridica = new Juridica();
                fisica = new Fisica();
                break;
        }
    }

    public void pesquisaSemCadastro(String por) {

        FisicaDB dbf = new FisicaDBToplink();

        if (por.equals("cpf")) {
            if (!pessoa.getDocumento().isEmpty()) {
                List lista = dbf.pesquisaFisicaPorDoc(pessoa.getDocumento());
                if (!lista.isEmpty()) {
                    message = "Este CPF já esta cadastrado!";
                    GenericaMensagem.warn("Validação", message);
                    validaPessoa = false;
                    return;
                }
            }
        }

        if (por.equals("rg")) {
            if (!fisica.getRg().isEmpty()) {
                List lista = dbf.pesquisaFisicaPorNomeNascRG("", null, fisica.getRg());
                if (!lista.isEmpty()) {
                    message = "Este RG já esta cadastrado!";
                    GenericaMensagem.warn("Validação", message);
                    validaPessoa = false;
                    return;
                }
            }
        }

        if (por.equals("nome") || por.equals("nascimento")) {
            if (!pessoa.getNome().isEmpty() && !fisica.getNascimento().isEmpty()) {
                List lista = dbf.pesquisaFisicaPorNomeNascRG(pessoa.getNome(), fisica.getDtNascimento(), fisica.getRg());
                if (!lista.isEmpty()) {
                    message = "Esta pessoa já esta cadastrada em nosso sistema!";
                    GenericaMensagem.warn("Validação", message);
                    validaPessoa = false;

                    pessoa.setNome("");
                    fisica.setDtNascimento(null);
                    return;
                }
            } else {
                validaPessoa = false;
                return;
            }
        }

        validaPessoa = true;
    }

    public void clear() {
        GenericaSessao.remove("emissaoGuiasBean");
    }

    public void saveSemCadastro() {
        if (!validaPessoa) {
            GenericaMensagem.fatal("Erro", "Verifique os dados antes de salvar!");
            return;
        }
        if (pessoa.getNome().isEmpty()) {
            message = "Nome não pode estar vazio!";
            return;
        }
        DaoInterface di = new Dao();
        pessoa.setTipoDocumento((TipoDocumento) di.find(new TipoDocumento(), 1));
        fisica.setPessoa(pessoa);
        if (pessoa.getId() == -1 && fisica.getId() == -1) {
            di.openTransaction();
            if (!di.save(pessoa)) {
                message = "Falha ao salvar Pessoa!";
                di.rollback();
                return;
            }
            if (!di.save(fisica)) {
                message = "Falha ao salvar Física!";
                di.rollback();
                return;
            }
            message = "Pessoa salva com Sucesso!";
            di.commit();
        }
    }

    public void addServico() {
        if (!listaMovimento.isEmpty()) {
            if (listaMovimento.get(0).getMovimento().getId() != -1) {
                GenericaMensagem.warn("Validação", "Emissão concluída!");
                return;
            }
        }
        if (pessoa.getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquise uma pessoa para gerar Parcelas");
            return;
        }
        if (getListServicos().isEmpty()) {
            GenericaMensagem.warn("Validação", "A lista de serviços não pode estar vazia!");
            return;
        }
        String vencto_ini = DataHoje.data();
        DaoInterface di = new Dao();
        FTipoDocumento fTipoDocumento = (FTipoDocumento) di.find(new FTipoDocumento(), 2); // FTipo_documento 13 - CARTEIRA, 2 - BOLETO
        float valorx = Moeda.converteUS$(valor);
        if (valorx == 0) {
            GenericaMensagem.warn("Validação", "Informar valor do serviço!");
            return;
        }
        Servicos servicos = (Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[2]).getDescription()));
        for (int i = 0; i < listaMovimento.size(); i++) {
            if (listaMovimento.get(i).getMovimento().getServicos().getId() == servicos.getId()) {
                GenericaMensagem.warn("Validação", "Este serviço já foi adicionado!");
                return;
            }
        }
        if (servicos.isProduto()) {
            for (int i = 0; i < listaMovimento.size(); i++) {
                if (listaMovimento.get(i).getMovimento().getServicos().isProduto()) {
                    GenericaMensagem.warn("Validação", "Serviço com produto(s) já cadastrado!");
                    return;
                }
            }
        }
        float descontox = Moeda.converteUS$(desconto);
        listaMovimento.add(
                new ListMovimentoEmissaoGuias(
                        new Movimento(
                                -1,
                                new Lote(),
                                servicos.getPlano5(),
                                pessoa,
                                servicos,
                                null, // BAIXA
                                (TipoServico) di.find(new TipoServico(), 1), // TIPO SERVICO
                                null, // ACORDO
                                valorx, // VALOR
                                DataHoje.data().substring(3), // REFERENCIA
                                vencto_ini, // VENCIMENTO
                                quantidade, // QUANTIDADE
                                true, // ATIVO
                                "E", // ES
                                false, // OBRIGACAO
                                pessoa, // PESSOA TITULAR
                                pessoa, // PESSOA BENEFICIARIO
                                "", // DOCUMENTO
                                "", // NR_CTR_BOLETO
                                vencto_ini, // VENCIMENTO ORIGINAL
                                0, // DESCONTO ATE VENCIMENTO
                                0, // CORRECAO
                                0, // JUROS
                                0, // MULTA
                                0,//descontox, // DESCONTO
                                0, // TAXA
                                0,//Moeda.multiplicarValores(quantidade, Moeda.subtracaoValores(valorx, descontox)), // VALOR BAIXA
                                fTipoDocumento, // FTipo_documento 13 - CARTEIRA, 2 - BOLETO
                                0, // REPASSE AUTOMATICO
                                new MatriculaSocios()
                        ),
                        Moeda.converteR$Float(Moeda.converteFloatR$Float(valorx)), // VALOR COM MASCARA
                        Moeda.converteR$Float(Moeda.converteFloatR$Float(descontox)), // DESCONTO COM MASCARA
                        Moeda.converteR$Float(Moeda.multiplicarValores(quantidade, Moeda.subtracaoValores(valorx, descontox))) // VALOR TOTAL QUANTIDADE * (VALOR-DESCONTO)
                ));

        total = "0";
        for (int i = 0; i < listaMovimento.size(); i++) {
            String total_desconto = listaMovimento.get(i).getTotal();
            total = Moeda.converteR$Float(Moeda.somaValores(Moeda.converteUS$(total), Moeda.converteUS$(total_desconto)));
        }
        desconto = "0";

    }

    public void removeServico(ListMovimentoEmissaoGuias lmeg) {
        for (int i = 0; i < listaMovimento.size(); i++) {

            
            
            if (lmeg.getMovimento().getServicos().getId() == listaMovimento.get(i).getMovimento().getServicos().getId()) {
                if (lmeg.getMovimento().getServicos().getId() != -1) {

                }
                listaMovimento.remove(i);
                PF.update("form_eg:i_tbl_eg");
                break;
            }
        }
        
        total = "0";
        for (int i = 0; i < listaMovimento.size(); i++) {
            String total_desconto = listaMovimento.get(i).getTotal();
            total = Moeda.converteR$Float(Moeda.somaValores(Moeda.converteUS$(total), Moeda.converteUS$(total_desconto)));
        }
    }

    public String save() {
        if (pessoa.getId() == -1) {
            message = "Pesquise uma pessoa para gerar!";
            return null;
        }
        if (getListServicos().isEmpty()) {
            message = "A lista de serviços não pode estar vazia!";
            return null;
        }
        if (listaMovimento.isEmpty()) {
            message = "A lista de lançamento não pode estar vazia!";
            return null;
        }
        DaoInterface di = new Dao();
        // CODICAO DE PAGAMENTO
        CondicaoPagamento cp = null;
        if (DataHoje.converteDataParaInteger(listaMovimento.get(0).getMovimento().getVencimento()) > DataHoje.converteDataParaInteger(DataHoje.data())) {
            cp = (CondicaoPagamento) di.find(new CondicaoPagamento(), 2);
        } else {
            cp = (CondicaoPagamento) di.find(new CondicaoPagamento(), 1);
        }
        // TIPO DE DOCUMENTO  FTipo_documento 13 - CARTEIRA, 2 - BOLETO
        FTipoDocumento td = (FTipoDocumento) di.find(new FTipoDocumento(), 2);
        Servicos serv = (Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[2]).getDescription()));
        lote.setEmissao(DataHoje.data());
        lote.setAvencerContabil(false);
        lote.setPagRec("R");
        lote.setValor(Moeda.converteUS$(total));
        lote.setFilial(serv.getFilial());
        lote.setEvt(null);
        lote.setPessoa(pessoa);
        lote.setFTipoDocumento(td);
        lote.setRotina((Rotina) di.find(new Rotina(), 132));
        lote.setStatus((FStatus) di.find(new FStatus(), 1));
        lote.setPessoaSemCadastro(null);
        lote.setDepartamento(serv.getDepartamento());
        lote.setCondicaoPagamento(cp);
        lote.setPlano5(serv.getPlano5());
        lote.setDescontoFolha(false);

        di.openTransaction();
        if (!di.save(lote)) {
            message = " Erro ao salvar Lote!";
            di.rollback();
            return null;
        }

        for (int i = 0; i < listaMovimento.size(); i++) {
            listaMovimento.get(i).getMovimento().setLote(lote);
            if (!di.save(listaMovimento.get(i).getMovimento())) {
                message = " Erro ao salvar Movimento!";
                di.rollback();
                return null;
            }
            //listaaux.add((Movimento)listaMovimento.get(i).getArgumento0());
        }

        Estoque e = new Estoque();
        ProdutoDao produtoDao = new ProdutoDao();
        for (int i = 0; i < listPedidos.size(); i++) {
            listPedidos.get(i).setLote(lote);
            if (!di.save(listPedidos.get(i))) {
                message = " Erro ao salvar Pedido!";
                di.rollback();
                return null;
            }
            int qtde = 0;
            e = produtoDao.listaEstoquePorProdutoFilial(listPedidos.get(i).getProduto(), filial);
            e.setEstoque(e.getEstoque() - listPedidos.get(i).getQuantidade());
            if (!di.update(e)) {
                message = " Erro ao salvar Pedido!";
                di.rollback();
                return null;
            }
            e = new Estoque();
        }

        Guia guias = new Guia(
                -1,
                lote,
                pessoa,
                (SubGrupoConvenio) di.find(new SubGrupoConvenio(), Integer.parseInt(getListSubGrupo().get(index[1]).getDescription())),
                false
        );

        if (!di.save(guias)) {
            message = " Erro ao salvar Guias!";
            di.rollback();
            return null;
        }
        di.commit();

        Movimento movimento = new Movimento();
        //List<Movimento> listaaux = new ArrayList();
        float valorx = 0;
        Moeda.converteUS$(valor);
        float descontox = 0;
        Moeda.converteUS$(desconto);

        for (int i = 0; i < listaMovimento.size(); i++) {
            HistoricoEmissaoGuias heg = new HistoricoEmissaoGuias();
            movimento = (Movimento) di.find(new Movimento(), listaMovimento.get(i).getMovimento().getId());

            descontox = Moeda.converteUS$(listaMovimento.get(i).getDesconto());
            valorx = Moeda.converteUS$(listaMovimento.get(i).getValor());

            movimento.setMulta(listaMovimento.get(i).getMovimento().getMulta());
            movimento.setJuros(listaMovimento.get(i).getMovimento().getJuros());
            movimento.setCorrecao((listaMovimento.get(i).getMovimento()).getCorrecao());
            movimento.setDesconto(descontox);

            //movimento.setValor( Float.valueOf( listaMovimento.get(i).getArgumento9().toString() ) );
            movimento.setValor(listaMovimento.get(i).getMovimento().getValor());

            // movimento.setValorBaixa( Moeda.subtracaoValores(movimento.getValor(), movimento.getDesconto()) );
            movimento.setValorBaixa(valorx);

            listaMovimentoAuxiliar.add(movimento);
            heg.setMovimento(movimento);
            heg.setUsuario((Usuario) GenericaSessao.getObject("sessaoUsuario"));

            di.openTransaction();
            if (!di.save(heg)) {
                di.rollback();
            } else {
                di.commit();
            }
        }

        if (!listaMovimentoAuxiliar.isEmpty()) {
            GenericaSessao.put("listaMovimento", listaMovimentoAuxiliar);
            return ((ChamadaPaginaBean) GenericaSessao.getObject("chamadaPaginaBean")).baixaGeral();
        }

        message = " Lançamento efetuado com Sucesso!";
        return null;
    }

    public String getValor() {
        if (pessoa.getId() != -1) {
            if (!enabledItensPedido) {
                LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
                List<Vector> valorx = db.pesquisaServicoValor(pessoa.getId(), Integer.parseInt(getListServicos().get(index[2]).getDescription()));
                float vl = Float.valueOf(((Double) valorx.get(0).get(0)).toString());
                valor = Moeda.converteR$Float(vl);
            } else {
                float v = 0;
                if (!listPedidos.isEmpty()) {
                    for (Pedido p : listPedidos) {
                        v += Moeda.somaValores(v, Moeda.converteUS$(valorTotalPedido(p)));
                    }
                }
                valor = "" + v;
            }

        }
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Pessoa getPessoa() {
        if (GenericaSessao.exists("pessoaPesquisa")) {
            pessoa = (Pessoa) GenericaSessao.getObject("pessoaPesquisa", true);
            fisica = new Fisica();
            FisicaDB db = new FisicaDBToplink();
            fisica = db.pesquisaFisicaPorPessoa(pessoa.getId());
        }
        boolean isFisica = false;
        if (GenericaSessao.exists("fisicaPesquisa")) {
            fisica = (Fisica) GenericaSessao.getObject("fisicaPesquisa", true);
            pessoa = new Pessoa();
            pessoa = fisica.getPessoa();
            isFisica = true;
        }
        if (!isFisica) {
            if (GenericaSessao.exists("juridicaPesquisa")) {
                juridica = (Juridica) GenericaSessao.getObject("juridicaPesquisa", true);
                pessoa = new Pessoa();
                pessoa = juridica.getPessoa();
            }
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public List<SelectItem> getListGrupo() {
        if (listSelectItem[0].isEmpty()) {
            DaoInterface di = new Dao();
            List<GrupoConvenio> list = (List<GrupoConvenio>) di.list(new GrupoConvenio());
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[0].add(new SelectItem(i, (String) list.get(i).getDescricao(), Integer.toString(list.get(i).getId())
                ));
            }
            if (listSelectItem[0].isEmpty()) {
                listSelectItem[0] = new ArrayList<SelectItem>();
            }
        }
        return listSelectItem[0];
    }

    public List<SelectItem> getListSubGrupo() {
        if (listSelectItem[1].isEmpty()) {
            if (!listSelectItem[0].isEmpty()) {
                SubGrupoConvenioDB db = new SubGrupoConvenioDBToplink();
                List<SubGrupoConvenio> list = (List<SubGrupoConvenio>) db.listaSubGrupoConvenioPorGrupo(Integer.parseInt(listSelectItem[0].get(index[0]).getDescription()));
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[1].add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())
                    ));
                }
                if (listSelectItem[1].isEmpty()) {
                    listSelectItem[1] = new ArrayList<SelectItem>();
                }
            }

        }
        return listSelectItem[1];
    }

    public List<SelectItem> getListServicos() {
        if (listSelectItem[2].isEmpty()) {
            ConvenioServicoDB db = new ConvenioServicoDBToplink();
            if (!getListGrupo().isEmpty() && !getListSubGrupo().isEmpty()) {
                List<Servicos> list = (List<Servicos>) db.pesquisaServicosSubGrupoConvenio(Integer.parseInt(getListSubGrupo().get(index[1]).getDescription()));
                for (int i = 0; i < list.size(); i++) {
                    listSelectItem[2].add(new SelectItem(i, list.get(i).getDescricao(), Integer.toString(list.get(i).getId())));
                }
            }
        }
        return listSelectItem[2];
    }

    public List<SelectItem> getListJuridica() {
        if (listSelectItem[3].isEmpty()) {
            LancamentoIndividualDB db = new LancamentoIndividualDBToplink();
            if (getListSubGrupo().isEmpty()) {
                return listSelectItem[3];
            }
            List<Juridica> list = (List<Juridica>) db.listaEmpresaConveniadaPorSubGrupo(Integer.parseInt(getListSubGrupo().get(index[1]).getDescription()));
            for (int i = 0; i < list.size(); i++) {
                listSelectItem[3].add(new SelectItem(i, list.get(i).getPessoa().getNome(), Integer.toString(list.get(i).getId())));
            }
        }
        return listSelectItem[3];
    }

    public String getDesconto() {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        return Moeda.converteR$(desconto);
    }

    public void setDesconto(String desconto) {
        if (desconto.isEmpty()) {
            desconto = "0";
        }
        this.desconto = Moeda.substituiVirgula(desconto);
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public List<ListMovimentoEmissaoGuias> getListaMovimento() {
        for (int i = 0; i < listaMovimento.size(); i++) {
            listaMovimento.get(i).setValor(Moeda.converteR$(listaMovimento.get(i).getValor()));
            listaMovimento.get(i).getMovimento().setValor(Moeda.converteUS$(Moeda.converteR$(listaMovimento.get(i).getValor())));
        }
        return listaMovimento;
    }

    public void setListaMovimento(List<ListMovimentoEmissaoGuias> listaMovimento) {
        this.listaMovimento = listaMovimento;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Fisica getFisica() {
        return fisica;
    }

    public void setFisica(Fisica fisica) {
        this.fisica = fisica;
    }

    public String getTotal() {
        if (total.isEmpty()) {
            total = "0";
        }
        return Moeda.converteR$(total);
    }

    public void setTotal(String total) {
        if (total.isEmpty()) {
            total = "0";
        }
        this.total = Moeda.substituiVirgula(total);
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public Integer[] getIndex() {
        return index;
    }

    public void setIndex(Integer[] index) {
        this.index = index;
    }

    public void addItemPedido() {
        pedido.setValorUnitario(Moeda.substituiVirgulaFloat(valorUnitarioPedido));
        //pedido.setDescontoUnitario(Moeda.substituiVirgulaFloat(descontoUnitarioPedido));
        pedido.setQuantidade(quantidadePedido);
        if (pedido.getProduto().getId() == -1) {
            GenericaMensagem.warn("Validação", "Pesquisar um produto!");
            return;
        }
        if (pedido.getQuantidade() < 1) {
            GenericaMensagem.warn("Validação", "Adicionar quantidade!");
            return;
        }
        if (pedido.getValorUnitario() < 1) {
            GenericaMensagem.warn("Validação", "Informar valor do produto!");
            return;
        }
        for (int i = 0; i < listPedidos.size(); i++) {
            if (listPedidos.get(i).getProduto().getId() == pedido.getProduto().getId()) {
                GenericaMensagem.warn("Validação", "Produto já adicionado!");
                return;
            }
        }
        Dao dao = new Dao();
        if (pedido.getId() == -1) {
            pedido.setEstoqueTipo((EstoqueTipo) dao.find(new EstoqueTipo(), 3));
            listPedidos.add(pedido);
        } else {
            dao.openTransaction();
            dao.update(pedido);
            dao.commit();
            listPedidos.add(pedido);
        }
        pedido = new Pedido();
        estoque = new Estoque();
        valorUnitarioPedido = "";
        descontoUnitarioPedido = "";
        quantidadePedido = 1;
    }

    public void editarItemPedido(int index) {
        Dao dao = new Dao();
        for (int i = 0; i < listPedidos.size(); i++) {
            if (i == index) {
                if (listPedidos.get(index).getId() == -1) {
                    pedido = listPedidos.get(index);
                    listPedidos.remove(index);
                } else {
                    pedido = (Pedido) dao.rebind(listPedidos.get(index));
                    listPedidos.remove(index);
                }
                valorUnitarioPedido = pedido.getValorUnitarioString();
                descontoUnitarioPedido = pedido.getDescontoUnitarioString();
                quantidadePedido = pedido.getQuantidade();
                break;
            }
        }
        modalPedido = true;
    }

    public void removeItemPedido(int index) {
        boolean erro = false;
        Dao dao = new Dao();
        dao.openTransaction();
        for (int i = 0; i < listPedidos.size(); i++) {
            if (i == index) {
                if (listPedidos.get(i).getId() != -1) {
                    if (!dao.delete(dao.find(listPedidos.get(i)))) {
                        dao.rollback();
                        erro = true;
                        break;
                    }
                }
                listPedidos.remove(i);
                break;
            }
        }
        if (erro) {
            dao.rollback();
        } else {
            dao.commit();
        }
    }

    public void novoPedido() {
        pedido = new Pedido();
        listPedidos = new ArrayList<Pedido>();
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = 1;
    }

    public void openModalPedido() {
        modalPedido = true;
        pedido = new Pedido();
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = 1;
    }

    public void closeModalPedido() {
        modalPedido = false;
    }

    public String valorTotalPedido(Pedido p) {
        if (p != null) {
            float value = Moeda.subtracaoValores(p.getValorUnitario(), p.getDescontoUnitario());
            value = Moeda.multiplicarValores(value, p.getQuantidade());
            return Moeda.converteR$Float(value);
        } else {
            return Moeda.converteR$Float(0);
        }

    }

    public String getValorTotal() {
        float v = 0;
        for (Pedido p : listPedidos) {
            v = Moeda.somaValores(v, Moeda.converteUS$(valorTotalPedido(p)));
        }
        valorTotal = Moeda.converteR$Float(v);
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<Pedido> getListPedidos() {
        return listPedidos;
    }

    public void setListPedidos(List<Pedido> listPedidos) {
        this.listPedidos = listPedidos;
    }

    public String getDescontoUnitarioPedido() {
        return descontoUnitarioPedido;
    }

    public void setDescontoUnitarioPedido(String descontoUnitarioPedido) {
        this.descontoUnitarioPedido = descontoUnitarioPedido;
    }

    public String getValorUnitarioPedido() {
        return valorUnitarioPedido;
    }

    public void setValorUnitarioPedido(String valorUnitarioPedido) {
        this.valorUnitarioPedido = valorUnitarioPedido;
    }

    public boolean isModalPedido() {
        return modalPedido;
    }

    public void setModalPedido(boolean modalPedido) {
        this.modalPedido = modalPedido;
    }

    public int getQuantidadePedido() {
        return quantidadePedido;
    }

    public void setQuantidadePedido(int quantidadePedido) {
        this.quantidadePedido = quantidadePedido;
    }

    public Pedido getPedido() {
        if (GenericaSessao.exists("produtoPesquisa")) {
            Produto p = (Produto) GenericaSessao.getObject("produtoPesquisa", true);
            for (int i = 0; i < listPedidos.size(); i++) {
                if (listPedidos.get(i).getProduto().getId() == p.getId()) {
                    GenericaMensagem.warn("Validação", "Produto já adicionado!");
                    return new Pedido();
                }
            }
            ProdutoDao produtoDao = new ProdutoDao();
            estoque = new Estoque();
            estoque = produtoDao.listaEstoquePorProdutoFilial(p, filial);
            if (estoque == null) {
                GenericaMensagem.warn("Validação", "Produto indiponível para esta filial!");
            } else {
                if (estoque.getEstoqueTipo().getId() == 3) {
                    pedido.setProduto(estoque.getProduto());
                    if (estoque.getEstoque() == 0) {
                        GenericaMensagem.warn("Validação", "Quantidade indiponível!");
                    }
                    valorUnitarioPedido = pedido.getProduto().getValorString();
                } else {
                    GenericaMensagem.warn("Validação", "Tipo de produto estoque " + estoque.getEstoqueTipo().getDescricao() + " não é permitido!");
                }
            }
        }
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public boolean isEnabledItensPedido() {
        return enabledItensPedido;
    }

    public void setEnabledItensPedido(boolean enabledItensPedido) {
        this.enabledItensPedido = enabledItensPedido;
    }

    public void listenerEnabledItensPedido() {
        DaoInterface di = new Dao();
        Servicos servicos = (Servicos) di.find(new Servicos(), Integer.parseInt(getListServicos().get(index[2]).getDescription()));
        enabledItensPedido = servicos.isProduto();
    }

    /* FIM PRODUTOS --------------------------------------------------------- */
    public Filial getFilial() {
        return filial;
    }

    public void setFilial(Filial filial) {
        this.filial = filial;
    }

    public String[] getVar() {
        return var;
    }

    public void setVar(String[] var) {
        this.var = var;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public void setEstoque(Estoque estoque) {
        this.estoque = estoque;
    }
}
