package br.com.rtools.financeiro.beans;

import br.com.rtools.estoque.EstoqueTipo;
import br.com.rtools.estoque.Pedido;
import br.com.rtools.estoque.Produto;
import br.com.rtools.financeiro.*;
import br.com.rtools.financeiro.db.FTipoDocumentoDB;
import br.com.rtools.financeiro.db.FTipoDocumentoDBToplink;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDB;
import br.com.rtools.financeiro.db.LancamentoFinanceiroDBToplink;
import br.com.rtools.movimento.GerarMovimento;
import br.com.rtools.pessoa.Filial;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.pessoa.Porte;
import br.com.rtools.pessoa.TipoDocumento;
import br.com.rtools.pessoa.db.FisicaDB;
import br.com.rtools.pessoa.db.FisicaDBToplink;
import br.com.rtools.pessoa.db.JuridicaDB;
import br.com.rtools.pessoa.db.JuridicaDBToplink;
import br.com.rtools.seguranca.MacFilial;
import br.com.rtools.seguranca.Rotina;
import br.com.rtools.seguranca.Usuario;
import br.com.rtools.seguranca.controleUsuario.ChamadaPaginaBean;
import br.com.rtools.utilitarios.Dao;
import br.com.rtools.utilitarios.DataHoje;
import br.com.rtools.utilitarios.DataObject;
import br.com.rtools.utilitarios.GenericaMensagem;
import br.com.rtools.utilitarios.GenericaSessao;
import br.com.rtools.utilitarios.Mask;
import br.com.rtools.utilitarios.Moeda;
import br.com.rtools.utilitarios.SalvarAcumuladoDB;
import br.com.rtools.utilitarios.SalvarAcumuladoDBToplink;
import br.com.rtools.utilitarios.ValidaDocumentos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

@ManagedBean
@SessionScoped
public class LancamentoFinanceiroBean implements Serializable {

    private List<DataObject> listaLancamento = new ArrayList();
    private List<DataObject> listaParcela = new ArrayList();
    private List<DataObject> listaParcelaSelecionada = new ArrayList();
    private DataObject linhaSelecionada = new DataObject();

    private int idFilial = 0;
    private int idTipoDocumento = 0;
    private int idFTipo = 0;
    private int idFTipoMovimento = 0;
    private int idOperacao = 0;
    private int idContaOperacao = 0;
    private int idCentroCusto = 0;
    private int idTipoCentroCusto = 0;
    private int idContaTipoPlano5 = 0;

    private List<SelectItem> listaFilial = new ArrayList();
    private List<SelectItem> listaTipoDocumento = new ArrayList();

    private List<SelectItem> listaFTipo = new ArrayList();
    private List<SelectItem> listaFTipoMovimento = new ArrayList();

    private List<SelectItem> listaOperacao = new ArrayList();
    private List<SelectItem> listaContaOperacao = new ArrayList();
    private List<SelectItem> listaCentroCusto = new ArrayList();
    private List<SelectItem> listaTipoCentroCusto = new ArrayList();

    private List<SelectItem> listaContaTipoPlano5 = new ArrayList();

    private String descricao = "";
    private String mascara = "";
    private String es = "E";
    private String condicao = "vista";
    private String total = "";
    private String valor = "";
    private String opcaoCadastro = "";
    private String vencimento = DataHoje.data();
    private String strConta = "";
    private String documentoMovimento = "";
    private String porPesquisa = "todos";
    private String maskSearch = "todos";
    private String description = "";
    private String acrescimo = "0";
    private String multa = "0";
    private String juros = "0";
    private String correcao = "0";
    private String desconto = "0";
    private String strVisualizando = "os Meus Lançamentos";

    private boolean modalVisivel = false;
    private boolean chkImposto = false;
    private boolean disabledConta = false;
    private boolean telaSalva = true;

    private Pessoa pessoa = new Pessoa();
    private Operacao operacao = new Operacao();
    private Lote lote = new Lote();
    private ContaTipoPlano5 contaTipoPlano = new ContaTipoPlano5();
    private List<Usuario> listaUsuarioLancamento = new ArrayList<Usuario>();
    private Usuario usuarioSelecionado = new Usuario();
    private DataObject linhaDO = new DataObject();
    private int indexDO = 0;
    private int indexAcrescimo = -1;
    
    private String motivoEstorno = "";
    
    public LancamentoFinanceiroBean() {
        lote.setEmissao(DataHoje.data());
    }

    /* PRODUTOS ------------------------------------------------------------- */
    private boolean modalPedido = false;
    private Pedido pedido = new Pedido();
    private int quantidadePedido = 0;
    private String valorUnitarioPedido = "";
    private String descontoUnitarioPedido = "";
    private List<Pedido> listaPedidos = new ArrayList();
    private String valorTotal = "0";

    public void addItemPedido() {
        pedido.setValorUnitario(Moeda.substituiVirgulaFloat(valorUnitarioPedido));
        pedido.setDescontoUnitario(Moeda.substituiVirgulaFloat(descontoUnitarioPedido));
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
        Dao dao = new Dao();
        if (pedido.getId() == -1) {
            pedido.setEstoqueTipo((EstoqueTipo) dao.find(new EstoqueTipo(), 1));
            listaPedidos.add(pedido);
        } else {
            dao.openTransaction();
            dao.update(pedido);
            dao.commit();
            listaPedidos.add(pedido);
        }
        pedido = new Pedido();
        valorUnitarioPedido = "";
        descontoUnitarioPedido = "";
        quantidadePedido = 0;
    }

    public void editarItemPedido(int index) {
        Dao dao = new Dao();
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (i == index) {
                if (listaPedidos.get(index).getId() == -1) {
                    pedido = listaPedidos.get(index);
                    listaPedidos.remove(index);
                } else {
                    pedido = (Pedido) dao.rebind(listaPedidos.get(index));
                    listaPedidos.remove(index);
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
        for (int i = 0; i < listaPedidos.size(); i++) {
            if (i == index) {
                if (listaPedidos.get(i).getId() != -1) {
                    if (!dao.delete(dao.find(listaPedidos.get(i)))) {
                        dao.rollback();
                        erro = true;
                        break;
                    }
                }
                listaPedidos.remove(i);
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
        listaPedidos = new ArrayList<Pedido>();
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = 0;
    }

    public void openModalPedido() {
        modalPedido = true;
        pedido = new Pedido();
        descontoUnitarioPedido = "0,00";
        valorUnitarioPedido = "0,00";
        quantidadePedido = 0;
    }

    public void closeModalPedido() {
        modalPedido = false;
    }

    public String valorTotalGrid(Pedido linha) {
        if (linha != null) {
            float value = Moeda.subtracaoValores(linha.getValorUnitario(), linha.getDescontoUnitario());
            value = Moeda.multiplicarValores(value, linha.getQuantidade());
            return Moeda.converteR$Float(value);
        } else {
            return Moeda.converteR$Float(0);
        }

    }

    public String getValorTotal() {
        float valorx = 0;
        for (Pedido pedidox : listaPedidos) {
            valorx = Moeda.somaValores(valorx, Moeda.converteUS$(valorTotalGrid(pedidox)));
        }
        valorTotal = Moeda.converteR$Float(valorx);
        return valorTotal;
    }

    public void setValorTotal(String valorTotal) {
        this.valorTotal = valorTotal;
    }

    public List<Pedido> getListaPedidos() {
        return listaPedidos;
    }

    public void setListaPedidos(List<Pedido> listaPedidos) {
        this.listaPedidos = listaPedidos;
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
        if (GenericaSessao.exists("pesquisaProduto")) {
            pedido.setProduto((Produto) GenericaSessao.getObject("pesquisaProduto", true));
        }
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }
    /* FIM PRODUTOS --------------------------------------------------------- */
    
    public void delete() {
        // PARCELAS BAIXADAS
        Movimento movimento = new Movimento();
        for (DataObject linha : listaParcela) {
            movimento = (Movimento) linha.getArgumento1();
            if (movimento.getBaixa() != null) {
                GenericaMensagem.warn("Erro", "Existem parcelas baixadas, ESTORNE elas antes de excluir este lançamento!");
                return;
            }
        }

        Dao dao = new Dao();

        dao.openTransaction();
        for (DataObject linha : listaParcela) {
            movimento = (Movimento) linha.getArgumento1();

            if (!dao.delete(dao.find(movimento))) {
                GenericaMensagem.warn("Erro", "Movimento não pode ser Excluído!");
                dao.rollback();
                return;
            }
        }

        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        FiltroLancamento fl = db.pesquisaFiltroLancamento(lote.getId());

        if (!dao.delete(dao.find(fl))) {
            GenericaMensagem.warn("Erro", "Filtro Lançamento não pode ser Excluído!");
            dao.rollback();
            return;
        }

        for (Pedido pedidox : listaPedidos) {
            if (!dao.delete(dao.find(pedidox))) {
                GenericaMensagem.warn("Erro", "PEDIDO não pode ser Excluído!");
                dao.rollback();
                return;
            }
        }

        if (!dao.delete(dao.find(lote))) {
            GenericaMensagem.warn("Erro", "Lote não pode ser Excluído!");
            dao.rollback();
            return;
        }

        GenericaMensagem.info("Sucesso", "Lançamento excluído com Sucesso!");

        limpar();
        dao.commit();
    }

    public void reverse() {
        if (listaParcelaSelecionada.isEmpty()) {
            GenericaMensagem.warn("Atenção", "Selecione ao menos uma parcela PAGA ser estornada!");
            return;
        }

        // PARCELAS PARA SEREM ESTORNADAS
        Movimento movimento = new Movimento();


        if (motivoEstorno.isEmpty() || motivoEstorno.length() <= 5){
            GenericaMensagem.error("Atenção", "Motivo de Estorno INVÁLIDO!");
            return;
        }        
        
        boolean reverse = false;
        for (DataObject linha : listaParcelaSelecionada) {
            movimento = (Movimento) linha.getArgumento1();
            if (movimento.getBaixa() == null) {
                continue;
            }
//            Baixa baixa = (Baixa)sv.pesquisaCodigo(movimento.getBaixa().getId(), "Baixa");
//            
//            movimento.setBaixa(null);
//            movimento.setValorBaixa(0);
//            
//            if (!sv.alterarObjeto(movimento)){
//                GenericaMensagem.warn("Erro", "Erro ao alterar Movimento!");
//                sv.desfazerTransacao();
//                return;
//            }
//            
//            if (!sv.deletarObjeto(baixa)){
//                GenericaMensagem.warn("Erro", "Erro ao deletar Baixa!");
//                sv.desfazerTransacao();
//                return;
//            }

            if (GerarMovimento.estornarMovimento(movimento, motivoEstorno)) {
                reverse = true;
            }
        }

        if (reverse) {
            listaParcela.clear();
            listaParcelaSelecionada.clear();
            GenericaMensagem.info("Sucesso", "Estorno concluído!");
        } else {
            GenericaMensagem.warn("Erro", "NENHUMA parcela para ser estornada!");

        }
    }

    public String telaBaixa() {
        if (listaParcelaSelecionada.isEmpty()) {
            GenericaMensagem.warn("Erro", "Selecione ao menos UMA parcela para ser baixada!");
            return null;
        }

        MacFilial macFilial = (MacFilial) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("acessoFilial");
        if (macFilial == null) {
            GenericaMensagem.warn("Erro", "Não existe FILIAL NA SESSÃO!");
            return null;
        }

        if (macFilial.getCaixa() == null) {
            GenericaMensagem.warn("Erro", "Configurar CAIXA nesta estação de trabalho!");
            return null;
        }

        Movimento movimento = new Movimento();

        List<Movimento> lista = new ArrayList();

        // PARCELAS BAIXADAS
        for (DataObject linha : listaParcelaSelecionada) {
            movimento = (Movimento) linha.getArgumento1();
            if (movimento.getBaixa() != null) {
                GenericaMensagem.warn("Erro", "Parcelas que JÁ FORAM BAIXADAS não podem ser selecionadas!");
                return null;
            }
        }

        // PARCELAS COM VALOR ZERADO
        for (DataObject linha : listaParcelaSelecionada) {
            movimento = (Movimento) linha.getArgumento1();
            if (movimento.getValor() <= 0) {
                GenericaMensagem.warn("Erro", "Adicione um VALOR as parcelas adicionadas!");
                return null;
            }
        }

        for (DataObject linha : listaParcelaSelecionada) {
            movimento = (Movimento) linha.getArgumento1();

            //movimento = db.pesquisaCodigo(Integer.parseInt(String.valueOf(listaMovimento.get(i).getArgumento1())));
            movimento.setMulta(Moeda.converteUS$( linha.getArgumento12().toString()) );
            movimento.setJuros(Moeda.converteUS$( linha.getArgumento13().toString()) );
            movimento.setCorrecao(Moeda.converteUS$( linha.getArgumento14().toString()) );
            movimento.setDesconto(Moeda.converteUS$( linha.getArgumento5().toString()) );
            
            float valor_baixa = Moeda.somaValores(movimento.getValor(), Moeda.converteUS$(linha.getArgumento4().toString()));
            valor_baixa = Moeda.subtracaoValores(valor_baixa, movimento.getDesconto());
            //movimento.setValor(Moeda.converteUS$(listaMovimento.get(i).getArgumento6().toString()));
            
            movimento.setValorBaixa( valor_baixa );

            lista.add(movimento);
        }

        if (!lista.isEmpty()) {
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("listaMovimento", lista);
            if (es.equals("S")) 
                FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("esMovimento", "S");
            
            return ((ChamadaPaginaBean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("chamadaPaginaBean")).baixaGeral();
        }
        return null;
    }

    public void editar(DataObject linha) {
        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        lote = ((FiltroLancamento) linha.getArgumento0()).getLote();
        pessoa = lote.getPessoa();
        descricao = pessoa.getDocumento();
        modalVisivel = true;
        total = Moeda.converteR$Float(lote.getValor());
        //FiltroLancamento filtroLancamento = db.pesquisaFiltroLancamento(lote.getId());
        FiltroLancamento filtroLancamento = (FiltroLancamento) linha.getArgumento0();

        // FILIAL --
        for (int i = 0; i < listaFilial.size(); i++) {
            if (Integer.valueOf(listaFilial.get(i).getDescription()) == lote.getFilial().getId()) {
                idFilial = i;
            }
        }

        if (lote.getPagRec().equals("R")) {
            es = "E";
        } else {
            es = "S";
        }
        if (lote.getCondicaoPagamento().getId() == 1) {
            condicao = "vista";
        } else {
            condicao = "prazo";
        }

        // FTIPO DOCUMENTO
        for (int i = 0; i < listaFTipo.size(); i++) {
            if (Integer.valueOf(listaFTipo.get(i).getDescription()) == lote.getFtipoDocumento().getId()) {
                idFTipo = i;
            }
        }

        // OPERACAO
        getListaOperacao().clear();
        if (!getListaOperacao().isEmpty()) {
            for (int i = 0; i < listaOperacao.size(); i++) {
                if (Integer.valueOf(listaOperacao.get(i).getDescription()) == filtroLancamento.getOperacao().getId()) {
                    idOperacao = i;
                }
            }
        }

        // CENTRO CUSTO
        getListaCentroCusto().clear();
        if (!getListaCentroCusto().isEmpty() && filtroLancamento.getCentroCusto() != null) {
            for (int i = 0; i < listaCentroCusto.size(); i++) {
                if (Integer.valueOf(listaCentroCusto.get(i).getDescription()) == filtroLancamento.getCentroCusto().getId()) {
                    idCentroCusto = i;
                }
            }
        }

        // TIPO CENTRO CUSTO
        getListaTipoCentroCusto().clear();
        if (!getListaTipoCentroCusto().isEmpty() && filtroLancamento.getTipoCentroCusto() != null) {
            for (int i = 0; i < listaTipoCentroCusto.size(); i++) {
                if (Integer.valueOf(listaTipoCentroCusto.get(i).getDescription()) == filtroLancamento.getTipoCentroCusto().getId()) {
                    idTipoCentroCusto = i;
                }
            }
        }

        // OPERACAO
        getListaContaOperacao().clear();
        if (!getListaContaOperacao().isEmpty()) {
            for (int i = 0; i < listaContaOperacao.size(); i++) {
                if (Integer.valueOf(listaContaOperacao.get(i).getDescription()) == filtroLancamento.getContaOperacao().getId()) {
                    idContaOperacao = i;
                }
            }
        }

        // TIPO DOCUMENTO -- CNPJ -- CPF -- SEM DOCUMENTO
        for (int i = 0; i < listaTipoDocumento.size(); i++) {
            if (Integer.valueOf(listaTipoDocumento.get(i).getDescription()) == pessoa.getTipoDocumento().getId()) {
                idTipoDocumento = i;
            }
        }

        listaParcela.clear();
        listaParcelaSelecionada.clear();
//        List<Movimento> selectMovimento = db.listaParcelaLote(lote.getId());
//        float acrescimo = 0, valor_quitado = 0;
//        String data_quitacao = "";
//        String caixa = "NÃO BAIXADO";
//        String loteBaixa = "NÃO BAIXADO";
//        for (Movimento mov : selectMovimento) {
//            acrescimo = Moeda.somaValores(Moeda.somaValores(mov.getMulta(), mov.getJuros()), mov.getCorrecao());
//            
//            if (mov.getBaixa() != null){
//                valor_quitado = mov.getValorBaixa();
//                data_quitacao = mov.getBaixa().getBaixa();
//                caixa = mov.getBaixa().getCaixa() != null ? mov.getBaixa().getCaixa().getCaixa()+"" : "NÃO BAIXADO";
//                loteBaixa = mov.getBaixa() != null ? mov.getBaixa().getId()+"" : "NÃO BAIXADO";
//            }lis
//            
//            listaParcela.add(new DataObject(
//                    listaParcela.size(), 
//                    mov, 
//                    DataHoje.converteData(mov.getDtVencimento()), 
//                    Moeda.converteR$Float(mov.getValor()), 
//                    Moeda.converteR$Float(acrescimo), 
//                    Moeda.converteR$Float(mov.getDesconto()),
//                    Moeda.converteR$Float(valor_quitado),
//                    data_quitacao,
//                    filtroLancamento,
//                    filtroLancamento.getUsuario().getPessoa().getNome().length() >= 30 ? filtroLancamento.getUsuario().getPessoa().getNome().substring(0, 30)+"..." : filtroLancamento.getUsuario().getPessoa().getNome(),
//                    caixa,
//                    loteBaixa
//                    
//            ));
//        }

        List<ContaTipoPlano5> select = db.listaContaTipoPlano5(lote.getPlano5().getId(), 2);

        if (!select.isEmpty()) {
            contaTipoPlano = select.get(0);
            listaPedidos = db.listaPedido(lote.getId());
        } else {
            contaTipoPlano = new ContaTipoPlano5();
            listaPedidos.clear();
        }

    }

    public void salvar() {
        if (listaParcela.isEmpty()) {
            GenericaMensagem.warn("Erro", "ADICIONE UMA PARCELA para salvar este lançamento!");
            return;
        }
        float soma = 0;
        for (DataObject dox : listaParcela) {
            Movimento movimento = (Movimento) dox.getArgumento1();

            soma = Moeda.somaValores(soma, movimento.getValor());
        }

        if (soma < Moeda.converteUS$(total)) {
            GenericaMensagem.warn("Erro", "Valor das Parcelas é MENOR que soma Total!");
            return;
        } else if (soma > Moeda.converteUS$(total)) {
            GenericaMensagem.warn("Erro", "Valor das Parcelas é MAIOR que soma Total!");
            return;
        }
        ContaOperacao co = (ContaOperacao) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaContaOperacao.get(idContaOperacao).getDescription()), "ContaOperacao");
        FTipoDocumento td = (FTipoDocumento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaFTipo.get(idFTipo).getDescription()), "FTipoDocumento");
        Filial filial = (Filial) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaFilial.get(idFilial).getDescription()), "Filial");
        CondicaoPagamento cp = null;
        if (condicao.equals("vista")) {
            cp = (CondicaoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(1, "CondicaoPagamento");
        } else {
            cp = (CondicaoPagamento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(2, "CondicaoPagamento");
        }

        lote.setValor(Moeda.converteUS$(total));
        lote.setPlano5(co.getPlano5());
        lote.setFTipoDocumento(td);
        lote.setFilial(filial);
        lote.setCondicaoPagamento(cp);
        lote.setDepartamento(null);
        lote.setRotina((Rotina) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(231, "Rotina"));
        lote.setStatus(null);
        lote.setEvt(null);
        lote.setPessoa(pessoa);
        lote.setPessoaSemCadastro(null);
        if (es.equals("E")) {
            lote.setPagRec("R");
        } else {
            lote.setPagRec("P");
        }

        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();

        sv.abrirTransacao();

        if (lote.getId() == -1) {
            if (!sv.inserirObjeto(lote)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar Lote!");
                sv.desfazerTransacao();
                return;
            }
        } else {
            if (!sv.alterarObjeto(lote)) {
                GenericaMensagem.warn("Erro", "Erro ao Atualizar Lote!");
                sv.desfazerTransacao();
                return;
            }
        }
        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        for (DataObject parcela : listaParcela) {
            Movimento movimento = (Movimento) parcela.getArgumento1();
            movimento.setLote(lote);
            if (movimento.getId() == -1) {
                if (!sv.inserirObjeto(movimento)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Lançamento!");
                    sv.desfazerTransacao();
                    return;
                }
            } else {
                movimento.setPessoa(pessoa);
                movimento.setTitular(pessoa);
                movimento.setBeneficiario(pessoa);
                
                movimento.setMulta(Moeda.converteUS$(parcela.getArgumento12().toString()));
                movimento.setJuros(Moeda.converteUS$(parcela.getArgumento13().toString()));
                movimento.setCorrecao(Moeda.converteUS$(parcela.getArgumento14().toString()));
                
                movimento.setDesconto(Moeda.converteUS$(parcela.getArgumento5().toString()));
                List<ContaTipoPlano5> select = db.listaContaTipoPlano5(movimento.getPlano5().getId(), 1);
                if (select.isEmpty()) {
                    movimento.setPlano5(lote.getPlano5());
                }

                if (!sv.alterarObjeto(movimento)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Lançamento!");
                    sv.desfazerTransacao();
                    return;
                }
            }
        }
        CentroCusto cc = null;
        CentroCustoContabilSub cs = null;
        Usuario us = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        if (!listaCentroCusto.isEmpty()) {
            cc = (CentroCusto) sv.pesquisaCodigo(Integer.valueOf(listaCentroCusto.get(idCentroCusto).getDescription()), "CentroCusto");
        }
        if (!listaTipoCentroCusto.isEmpty()) {
            cs = (CentroCustoContabilSub) sv.pesquisaCodigo(Integer.valueOf(listaTipoCentroCusto.get(idTipoCentroCusto).getDescription()), "CentroCustoContabilSub");
        }

        FiltroLancamento fl = db.pesquisaFiltroLancamento(lote.getId());
        if (fl.getId() == -1) {
            fl = new FiltroLancamento(-1, lote, operacao, cc, cs, co, us);
            if (!sv.inserirObjeto(fl)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar FILTRO LANÇAMENTO!");
                sv.desfazerTransacao();
                return;
            }
        } else {
            fl = (FiltroLancamento) sv.pesquisaCodigo(fl.getId(), "FiltroLancamento");
            fl.setLote(lote);
            fl.setOperacao(operacao);
            fl.setCentroCusto(cc);
            fl.setTipoCentroCusto(cs);
            fl.setContaOperacao(co);

            if (!sv.alterarObjeto(fl)) {
                GenericaMensagem.warn("Erro", "Erro ao Atualizar FILTRO LANÇAMENTO!");
                sv.desfazerTransacao();
                return;
            }
        }

        for (Pedido pedidox : listaPedidos) {
            if (pedidox.getId() == -1) {
                pedidox.setLote(lote);
                if (!sv.inserirObjeto(pedidox)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Pedidos");
                    sv.desfazerTransacao();
                    return;
                }
            } else {
                if (!sv.alterarObjeto(pedidox)) {
                    GenericaMensagem.warn("Erro", "Erro ao Alterar Pedidos");
                    sv.desfazerTransacao();
                    return;
                }
            }
        }

        GenericaMensagem.info("OK", "Lançamento SALVO com Sucesso!");
        sv.comitarTransacao();
        telaSalva = true;
        listaLancamento.clear();
    }

    public void openExcluirParcela(DataObject linha, int index) {
        linhaDO = linha;
        indexDO = index;
    }
    public void excluirParcela() {
        if (((Movimento) linhaDO.getArgumento1()).getId() == -1) {
            listaParcela.remove(indexDO);
            GenericaMensagem.info("Sucesso", "Item removido!");
        } else {
            if (((Movimento) linhaDO.getArgumento1()).getBaixa() != null) {
                GenericaMensagem.warn("Erro", "Não é possivel excluir parcela BAIXADA, para fazer isso ESTORNE os lançamentos!");
                return;
            }

            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            Movimento mov = (Movimento) sv.pesquisaCodigo(((Movimento) linhaDO.getArgumento1()).getId(), "Movimento");

            sv.abrirTransacao();
            if (!sv.deletarObjeto(mov)) {
                sv.desfazerTransacao();
                GenericaMensagem.warn("Erro", "Não foi possivel excluir este Movimento!");
            } else {
                sv.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Item removido!");
                listaParcela.remove(indexDO);
            }
        }
    }

    public void adicionarParcela() {
//        if (Moeda.converteUS$(total) <= 0.0){
//            GenericaMensagem.warn("Erro", "Não existe mais total para ser adicionado!");
//            return;
//        }

        if (pessoa.getId() == -1) {
            GenericaMensagem.warn("Erro", "PESQUISE ou CADASTRE uma pessoa para adicionar uma parcela!");
            return;
        }

        if (!listaContaOperacao.isEmpty() && listaContaOperacao.get(0).getDescription().equals("0")) {
            GenericaMensagem.warn("Erro", "Selecione uma CONTA PRIMÁRIA para adicionar parcela!");
            return;
        }

        ContaOperacao co = (ContaOperacao) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaContaOperacao.get(idContaOperacao).getDescription()), "ContaOperacao");
        FTipoDocumento td = (FTipoDocumento) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaFTipoMovimento.get(idFTipoMovimento).getDescription()), "FTipoDocumento");

        Plano5 pl5 = null;
        if (chkImposto) {
            pl5 = ((ContaTipoPlano5) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaContaTipoPlano5.get(idContaTipoPlano5).getDescription()), "ContaTipoPlano5")).getPlano5();
        } else {
            pl5 = co.getPlano5();
        }

        Movimento movimento = new Movimento(
                -1,
                null, // LOTE
                pl5,
                pessoa,
                null, // SERVICOS
                null, // BAIXA
                null, // TIPO SERVICO
                null, // ACORDO
                Moeda.converteUS$(valor),
                "", // REFERENCIA
                vencimento, // VENCIMENTO
                1, // QUANTIDADE
                true, // ATIVO
                es,
                false, // OBRIGACAO
                pessoa, // TITULAR
                pessoa, // BENEFICIARIO
                documentoMovimento, // DOCUMENTO
                "", // NR CTR BOLETO
                "", // VENCIMENTO ORIGINAL
                0, // DESCONTO ATE VENCIMENTO
                Moeda.converteUS$(correcao), // CORRECAO
                Moeda.converteUS$(juros), // JUROS
                Moeda.converteUS$(multa), // MULTA
                Moeda.converteUS$(desconto), // DESCONTO
                0, // TAXA
                0, // VALOR BAIXA
                td, // TIPO DOCUMENTO
                0, // REPASSE AUTOMATICO    
                null // MATRICULA SÓCIO
        );

        Usuario user = (Usuario) GenericaSessao.getObject("sessaoUsuario");
        float valor_t = Moeda.subtracaoValores(Moeda.somaValores(movimento.getValor(), Moeda.converteUS$(acrescimo)), movimento.getDesconto());
        listaParcela.add(new DataObject(
                listaParcela.size(),
                movimento,
                DataHoje.converteData(movimento.getDtVencimento()),
                Moeda.converteR$Float(movimento.getValor()),
                Moeda.converteR$(acrescimo), // ACRESCIMO
                Moeda.converteR$Float(movimento.getDesconto()), // DESCONTO
                Moeda.converteR$Float(valor_t), // VALOR PAGAMENTO
                "", // DATA PAGAMENTO
                new FiltroLancamento(),
                user.getPessoa().getNome().length() >= 30 ? user.getPessoa().getNome().substring(0, 30) + "..." : user.getPessoa().getNome(),
                "NÃO BAIXADO",
                "NÃO BAIXADO",
                Moeda.converteR$Float(movimento.getMulta()),
                Moeda.converteR$Float(movimento.getJuros()),
                Moeda.converteR$Float(movimento.getCorrecao()),
                null
        ));
        
        openModalAcrescimo();
        desconto = "0,00";
        chkImposto = false;
        GenericaMensagem.info("Sucesso", "Parcela adicionada!");
    }

    public void limpar() {
        LancamentoFinanceiroBean la = new LancamentoFinanceiroBean();
        la.setModalVisivel(true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lancamentoFinanceiroBean", la);
    }

    public void salvarPessoa() {
        SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
        if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("sem documento")) {
            if (opcaoCadastro.equals("juridica")) {
                if (pessoa.getNome().isEmpty()) {
                    GenericaMensagem.warn("Erro", "Digite um NOME para EMPRESA!");
                    return;
                }

                sv.abrirTransacao();

                pessoa.setTipoDocumento((TipoDocumento) sv.pesquisaCodigo(4, "TipoDocumento"));
                pessoa.setDocumento("0");
                pessoa.setNome(pessoa.getNome().toUpperCase());

                if (!sv.inserirObjeto(pessoa)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Pessoa!");
                    sv.desfazerTransacao();
                }

                Juridica juridica = new Juridica(-1, pessoa, pessoa.getNome().toUpperCase(), null, null, "", "", "", "", (Porte) sv.pesquisaCodigo(1, "Porte"), "", "", true, false);
                if (!sv.inserirObjeto(juridica)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Empresa!");
                    sv.desfazerTransacao();
                }

                sv.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Cadastro concluído!");
                opcaoCadastro = "";
            } else {
                if (pessoa.getNome().isEmpty()) {
                    GenericaMensagem.warn("Erro", "Digite um NOME para PESSOA!");
                    return;
                }

                sv.abrirTransacao();

                pessoa.setTipoDocumento((TipoDocumento) sv.pesquisaCodigo(4, "TipoDocumento"));
                pessoa.setDocumento("0");
                pessoa.setNome(pessoa.getNome().toUpperCase());

                if (!sv.inserirObjeto(pessoa)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Pessoa!");
                    sv.desfazerTransacao();
                }

                Fisica fisica = new Fisica(-1, pessoa, "", "", "", "", DataHoje.dataHoje(), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
                if (!sv.inserirObjeto(fisica)) {
                    GenericaMensagem.warn("Erro", "Erro ao Salvar Cadastro!");
                    sv.desfazerTransacao();
                }

                sv.comitarTransacao();
                GenericaMensagem.info("Sucesso", "Cadastro concluído!");
                opcaoCadastro = "";
            }
        } else if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("cnpj")) {
            if (!ValidaDocumentos.isValidoCNPJ(descricao.replace(".", "").replace("/", "").replace("-", ""))) {
                GenericaMensagem.warn("Erro", "Este CNPJ não é válido!");
                return;
            }

            JuridicaDB dbj = new JuridicaDBToplink();
            List listDocumento = dbj.pesquisaJuridicaPorDoc(descricao);
            for (int i = 0; i < listDocumento.size(); i++) {
                if (!listDocumento.isEmpty()) {
                    GenericaMensagem.warn("Erro", "Empresa já esta cadastrada no Sistema!");
                    return;
                }
            }

            if (pessoa.getNome().isEmpty()) {
                GenericaMensagem.warn("Erro", "Digite um NOME para EMPRESA!");
                return;
            }

            sv.abrirTransacao();

            pessoa.setTipoDocumento((TipoDocumento) sv.pesquisaCodigo(2, "TipoDocumento"));
            pessoa.setDocumento(descricao);
            pessoa.setNome(pessoa.getNome().toUpperCase());

            if (!sv.inserirObjeto(pessoa)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar Pessoa!");
                sv.desfazerTransacao();
            }

            Juridica juridica = new Juridica(-1, pessoa, pessoa.getNome().toUpperCase(), null, null, "", "", "", "", (Porte) sv.pesquisaCodigo(1, "Porte"), "", "", true, false);
            if (!sv.inserirObjeto(juridica)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar Empresa!");
                sv.desfazerTransacao();
            }

            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Cadastro concluído!");
            opcaoCadastro = "";
        } else {
            if (!ValidaDocumentos.isValidoCPF(descricao.replace(".", "").replace("/", "").replace("-", ""))) {
                GenericaMensagem.warn("Erro", "Este CPF não é válido!");
                return;
            }

            FisicaDB db = new FisicaDBToplink();
            List listDocumento = db.pesquisaFisicaPorDoc(descricao);
            if (!listDocumento.isEmpty()) {
                GenericaMensagem.warn("Erro", "CPF já esta cadastrada no Sistema!");
                return;
            }

            if (pessoa.getNome().isEmpty()) {
                GenericaMensagem.warn("Erro", "Digite um NOME para PESSOA!");
                return;
            }

            sv.abrirTransacao();

            pessoa.setTipoDocumento((TipoDocumento) sv.pesquisaCodigo(1, "TipoDocumento"));
            pessoa.setDocumento(descricao);
            pessoa.setNome(pessoa.getNome().toUpperCase());

            if (!sv.inserirObjeto(pessoa)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar Pessoa!");
                sv.desfazerTransacao();
            }

            Fisica fisica = new Fisica(-1, pessoa, "", "", "", "", DataHoje.dataHoje(), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            if (!sv.inserirObjeto(fisica)) {
                GenericaMensagem.warn("Erro", "Erro ao Salvar Cadastro!");
                sv.desfazerTransacao();
            }

            sv.comitarTransacao();
            GenericaMensagem.info("Sucesso", "Cadastro concluído!");
            opcaoCadastro = "";
        }
    }

    public void cadastrarPessoa(String param) {
        pessoa = new Pessoa();

        if (param.isEmpty()) {
            if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("cnpj")) {
                opcaoCadastro = "juridica";
            } else if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("cpf")) {
                opcaoCadastro = "fisica";
            } else {
                opcaoCadastro = "juridica";
            }
        } else {
            opcaoCadastro = param;
        }
    }

    public void pesquisarPessoa() {
        if (!opcaoCadastro.isEmpty()) {
            return;
        }

        if (pessoa.getId() != -1 && pessoa.getDocumento().equals(descricao)) {
            return;
        }

        if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("sem documento")) {
            return;
        }

        LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
        if (listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase().equals("cnpj")) {
            Juridica juridica = db.pesquisaJuridica(descricao);
            if (juridica != null) {
                pessoa = juridica.getPessoa();
            } else {
                pessoa = new Pessoa();
            }
            telaSalva = false;
        } else {
            Fisica fisica = db.pesquisaFisica(descricao);
            if (fisica != null) {
                pessoa = fisica.getPessoa();
            } else {
                pessoa = new Pessoa();
            }
            telaSalva = false;
        }
    }

    public void cancelarCadastro() {
        opcaoCadastro = "";
        descricao = "";
        pessoa = new Pessoa();
    }

    public void abreModal() {
        //modalVisivel = true;
        LancamentoFinanceiroBean la = new LancamentoFinanceiroBean();
        la.setModalVisivel(true);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("lancamentoFinanceiroBean", la);
    }

    public void fechaModal() {
        if (lote.getId() == -1){
            modalVisivel = false;
            telaSalva = true;
            return;
        }
        
        float soma = 0;
        for (DataObject dox : listaParcela) {
            Movimento movimento = (Movimento) dox.getArgumento1();
               soma = Moeda.somaValores(soma, movimento.getValor());
        }

        if (soma < Moeda.converteUS$(total)) {
            GenericaMensagem.warn("Erro", "Valor das Parcelas é MENOR que soma Total!");
            modalVisivel = true;
            telaSalva = false;
            return;
        } else if (soma > Moeda.converteUS$(total)) {
            GenericaMensagem.warn("Erro", "Valor das Parcelas é MAIOR que soma Total!");
            modalVisivel = true;
            telaSalva = false;
            return;
        }
        
        if (!telaSalva){
            GenericaMensagem.fatal("Erro", "Lançamento alterado SALVE este formulário antes de FECHAR!");
            modalVisivel = true;
            return;
        }
        modalVisivel = false;
    }

    public void atualizaComboES() {
        listaOperacao.clear();
        idOperacao = 0;

        listaCentroCusto.clear();
        idCentroCusto = 0;

        listaTipoCentroCusto.clear();
        idTipoCentroCusto = 0;

        listaContaOperacao.clear();
        idContaOperacao = 0;
    }

    public void atualizaComboOperacao() {
        listaCentroCusto.clear();
        idCentroCusto = 0;

        listaTipoCentroCusto.clear();
        idTipoCentroCusto = 0;

        listaContaOperacao.clear();
        idContaOperacao = 0;
    }

    public void atualizaComboCentroCusto() {
        listaTipoCentroCusto.clear();
        idTipoCentroCusto = 0;

        listaContaOperacao.clear();
        idContaOperacao = 0;
    }

    public void atualizaComboTipoCentroCusto() {
        listaContaOperacao.clear();
        idContaOperacao = 0;
    }

    public String alterarDesconto(int index){
//        float acre = Moeda.somaValores(
//                Moeda.somaValores(Moeda.converteUS$(listaParcela.get(index).getArgumento12().toString()), 
//                Moeda.converteUS$(listaParcela.get(index).getArgumento13().toString())), 
//                Moeda.converteUS$(listaParcela.get(index).getArgumento14().toString())
//        );
        float acre = Moeda.converteUS$(listaParcela.get(index).getArgumento4().toString());
        
        float desc = Moeda.converteUS$(listaParcela.get(index).getArgumento5().toString());        
        float valor_p = Moeda.converteUS$(listaParcela.get(index).getArgumento3().toString());        
        float soma = Moeda.subtracaoValores(Moeda.somaValores(acre, valor_p), desc);
        
        listaParcela.get(index).setArgumento5(Moeda.converteR$(String.valueOf(listaParcela.get(index).getArgumento5())));
        listaParcela.get(index).setArgumento6(Moeda.converteR$Float(soma));
        
        telaSalva = false;
        return null;
    }
    
    public String alterarAcrescimo(int index){
        listaParcela.get(index).setArgumento6(Moeda.converteR$(String.valueOf(listaParcela.get(index).getArgumento6())));
        
        multa = Moeda.converteR$(listaParcela.get(index).getArgumento12().toString());
        juros = Moeda.converteR$(listaParcela.get(index).getArgumento13().toString());
        correcao = Moeda.converteR$(listaParcela.get(index).getArgumento14().toString());
        indexAcrescimo = index;
        return null;
    }
    
    public String adicionarAcrescimo(){
        float acre = Moeda.somaValores(Moeda.somaValores(Moeda.converteUS$(multa), Moeda.converteUS$(juros)), Moeda.converteUS$(correcao));
        
        if (indexAcrescimo == -1){
            acrescimo = Moeda.converteR$Float(acre);
        }else{
            listaParcela.get(indexAcrescimo).setArgumento4(Moeda.converteR$Float(acre));
            listaParcela.get(indexAcrescimo).setArgumento12(Moeda.converteR$(multa));
            listaParcela.get(indexAcrescimo).setArgumento13(Moeda.converteR$(juros));
            listaParcela.get(indexAcrescimo).setArgumento14(Moeda.converteR$(correcao));
            
            float desc = Moeda.converteUS$(listaParcela.get(indexAcrescimo).getArgumento5().toString());
            float valor_p = Moeda.somaValores(Moeda.converteUS$(listaParcela.get(indexAcrescimo).getArgumento3().toString()), acre);
            
            listaParcela.get(indexAcrescimo).setArgumento6(Moeda.converteR$Float(Moeda.subtracaoValores(valor_p, desc)));
        }
        
        telaSalva = false;
        indexAcrescimo = -1;
        return null;
    }

    public void openModalAcrescimo(){
        multa = "0,00";
        juros = "0,00";
        correcao = "0,00";
        acrescimo = "0,00";
    }
    
    public void alterarListaLancamento(Usuario linha){
        if (linha != null){
            usuarioSelecionado = linha;
            
            if (((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId() == linha.getId())
                strVisualizando = "os Meus Lançamentos";
            else
                strVisualizando = "os Lançamentos de "+linha.getPessoa().getNome();
        }else{
            usuarioSelecionado = new Usuario();
            strVisualizando = "TODOS Lançamentos";
        }
        
        listaLancamento.clear();
    }
    
    public List<DataObject> getListaLancamento() {
        if (listaLancamento.isEmpty()) {
            LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();

            List<FiltroLancamento> select = new ArrayList<FiltroLancamento>();
            //((Usuario) GenericaSessao.getObject("sessaoUsuario")).getId()
            
            if (usuarioSelecionado.getId() == -1)    
                select = db.listaLancamento(-1, porPesquisa, description);
            else
                select = db.listaLancamento(usuarioSelecionado.getId(), porPesquisa, description);
             

            for (FiltroLancamento flx : select) {
                listaLancamento.add(new DataObject(flx, Moeda.converteR$Float(flx.getLote().getValor())));
            }
        }
        return listaLancamento;
    }

    public void setListaLancamento(List<DataObject> listaLancamento) {
        this.listaLancamento = listaLancamento;
    }

    public int getIdFilial() {
        return idFilial;
    }

    public void setIdFilial(int idFilial) {
        this.idFilial = idFilial;
    }

    public List<SelectItem> getListaFilial() {
        if (listaFilial.isEmpty()) {
            SalvarAcumuladoDB salvarAcumuladoDB = new SalvarAcumuladoDBToplink();
            List<Filial> list = (List<Filial>) salvarAcumuladoDB.listaObjeto("Filial", true);
            for (int i = 0; i < list.size(); i++) {
                listaFilial.add(new SelectItem(new Integer(i),
                        list.get(i).getFilial().getPessoa().getDocumento() + " / " + list.get(i).getFilial().getPessoa().getNome(),
                        Integer.toString(list.get(i).getId())));
            }
        }
        return listaFilial;
    }

    public void setListaFilial(List<SelectItem> listaFilial) {
        this.listaFilial = listaFilial;
    }

    public List<SelectItem> getListaTipoDocumento() {
        if (listaTipoDocumento.isEmpty()) {
            List<TipoDocumento> result = (new LancamentoFinanceiroDBToplink()).listaTipoDocumento();
            for (int i = 0; i < result.size(); i++) {
                listaTipoDocumento.add(
                        new SelectItem(i,
                                result.get(i).getDescricao(),
                                Integer.toString(result.get(i).getId())
                        )
                );
            }
        }
        return listaTipoDocumento;
    }

    public void setListaTipoDocumento(List<SelectItem> listaTipoDocumento) {
        this.listaTipoDocumento = listaTipoDocumento;
    }

    public int getIdTipoDocumento() {
        return idTipoDocumento;
    }

    public void setIdTipoDocumento(int idTipoDocumento) {
        this.idTipoDocumento = idTipoDocumento;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMascara() {
        return mascara = Mask.getMascaraPesquisa(listaTipoDocumento.get(idTipoDocumento).getLabel().toLowerCase(), true);
    }

    public void setMascara(String mascara) {
        this.mascara = mascara;
    }

    public String getEs() {
        return es;
    }

    public void setEs(String es) {
        this.es = es;
    }

    public int getIdFTipo() {
        return idFTipo;
    }

    public void setIdFTipo(int idFTipo) {
        this.idFTipo = idFTipo;
    }

    public int getIdFTipoMovimento() {
        return idFTipoMovimento;
    }

    public void setIdFTipoMovimento(int idFTipoMovimento) {
        this.idFTipoMovimento = idFTipoMovimento;
    }

    public List<SelectItem> getListaFTipo() {
        if (listaFTipo.isEmpty()) {
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<FTipoDocumento> select = db.pesquisaCodigoTipoDocumentoIDS("1,12,24,25");
            for (int i = 0; i < select.size(); i++) {
                listaFTipo.add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId()))
                );
            }
        }
        return listaFTipo;
    }

    public void setListaFTipo(List<SelectItem> listaFTipo) {
        this.listaFTipo = listaFTipo;
    }

    public List<SelectItem> getListaFTipoMovimento() {
        if (listaFTipoMovimento.isEmpty()) {
            FTipoDocumentoDB db = new FTipoDocumentoDBToplink();
            List<FTipoDocumento> select = db.pesquisaCodigoTipoDocumentoIDS("1,2,12,24,25");
            for (int i = 0; i < select.size(); i++) {
                listaFTipoMovimento.add(new SelectItem(
                        i,
                        select.get(i).getDescricao(),
                        Integer.toString(select.get(i).getId())
                ));
            }
        }
        return listaFTipoMovimento;
    }

    public void setListaFTipoMovimento(List<SelectItem> listaFTipoMovimento) {
        this.listaFTipoMovimento = listaFTipoMovimento;
    }

    public String getCondicao() {
        return condicao;
    }

    public void setCondicao(String condicao) {
        this.condicao = condicao;
    }

    public Pessoa getPessoa() {
        if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa") != null) {
            pessoa = ((Juridica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("juridicaPesquisa")).getPessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("juridicaPesquisa");
            descricao = pessoa.getDocumento();
            if (pessoa.getTipoDocumento().getId() == 1) {
                idTipoDocumento = 0;
            } else if (pessoa.getTipoDocumento().getId() == 2) {
                idTipoDocumento = 1;
            } else if (pessoa.getTipoDocumento().getId() == 4) {
                idTipoDocumento = 2;
            }
            opcaoCadastro = "";
        } else if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa") != null) {
            pessoa = ((Fisica) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("fisicaPesquisa")).getPessoa();
            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("fisicaPesquisa");
            descricao = pessoa.getDocumento();
            if (pessoa.getTipoDocumento().getId() == 1) {
                idTipoDocumento = 0;
            } else if (pessoa.getTipoDocumento().getId() == 2) {
                idTipoDocumento = 1;
            } else if (pessoa.getTipoDocumento().getId() == 4) {
                idTipoDocumento = 2;
            }
            opcaoCadastro = "";
        }
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getTotal() {
        if (contaTipoPlano.getId() != -1) {
            return total = getValorTotal();
        } else {
            return total = Moeda.converteR$(total);
        }
    }

    public void setTotal(String total) {
        this.total = Moeda.substituiVirgula(total);
    }

    public String getValor() {
        return Moeda.converteR$(valor);
    }

    public void setValor(String valor) {
        this.valor = Moeda.substituiVirgula(valor);
    }

    public String getOpcaoCadastro() {
        return opcaoCadastro;
    }

    public void setOpcaoCadastro(String opcaoCadastro) {
        this.opcaoCadastro = opcaoCadastro;
    }

    public List getListaParcela() {
        if (listaParcela.isEmpty() && lote.getId() != -1) {
            LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
            FiltroLancamento filtroLancamento = db.pesquisaFiltroLancamento(lote.getId());
            List<Movimento> selectMovimento = db.listaParcelaLote(lote.getId());
            float acrescimo = 0, desconto = 0, valor_quitado = 0;
            String data_quitacao = "";
            String caixa = "NÃO BAIXADO";
            String loteBaixa = "NÃO BAIXADO";
            for (Movimento mov : selectMovimento) {
                acrescimo = Moeda.somaValores(Moeda.somaValores(mov.getMulta(), mov.getJuros()), mov.getCorrecao());
                desconto = mov.getDesconto();
                
                if (mov.getBaixa() != null) {
                    valor_quitado = mov.getValorBaixa();
                    data_quitacao = mov.getBaixa().getBaixa();
                    caixa = mov.getBaixa().getCaixa() != null ? mov.getBaixa().getCaixa().getCaixa() + "" : "NÃO BAIXADO";
                    loteBaixa = mov.getBaixa() != null ? mov.getBaixa().getId() + "" : "NÃO BAIXADO";
                } else {
                    valor_quitado = Moeda.subtracaoValores(Moeda.somaValores(mov.getValor(), acrescimo), desconto);
                    data_quitacao = "";
                    caixa = "NÃO BAIXADO";
                    loteBaixa = "NÃO BAIXADO";
                }
                listaParcela.add(new DataObject(
                        listaParcela.size(),
                        mov,
                        DataHoje.converteData(mov.getDtVencimento()),
                        Moeda.converteR$Float(mov.getValor()),
                        Moeda.converteR$Float(acrescimo),
                        Moeda.converteR$Float(mov.getDesconto()),
                        Moeda.converteR$Float(valor_quitado),
                        data_quitacao,
                        filtroLancamento,
                        filtroLancamento.getUsuario().getPessoa().getNome().length() >= 30 ? filtroLancamento.getUsuario().getPessoa().getNome().substring(0, 30) + "..." : filtroLancamento.getUsuario().getPessoa().getNome(),
                        caixa,
                        loteBaixa,
                        Moeda.converteR$Float(mov.getMulta()), // 12
                        Moeda.converteR$Float(mov.getJuros()), // 13
                        Moeda.converteR$Float(mov.getCorrecao()), // 14
                        null
                ));

                
            }
        }
        return listaParcela;
    }

    public void setListaParcela(List listaParcela) {
        this.listaParcela = listaParcela;
    }

    public boolean isModalVisivel() {
        return modalVisivel;
    }

    public void setModalVisivel(boolean modalVisivel) {
        this.modalVisivel = modalVisivel;
    }

    public List<SelectItem> getListaOperacao() {
        if (listaOperacao.isEmpty()) {
            //List<Operacao> select = (new SalvarAcumuladoDBToplink()).listaObjeto("Operacao");
            List<Operacao> select = new ArrayList();
            if (es.equals("E")) {
                select = (new LancamentoFinanceiroDBToplink()).listaOperacao("1, 3");
            } else {
                select = (new LancamentoFinanceiroDBToplink()).listaOperacao("2, 3, 4, 5");
            }
            
            if (!select.isEmpty()){
                for (int i = 0; i < select.size(); i++) {
                    listaOperacao.add(new SelectItem(
                            i,
                            select.get(i).getDescricao(),
                            Integer.toString(select.get(i).getId()))
                    );
                }
            }else{
                listaOperacao.add(new SelectItem(0, "Nenhuma Operação Encontrada", "0"));
            }
        }
        return listaOperacao;
    }

    public void setListaOperacao(List<SelectItem> listaOperacao) {
        this.listaOperacao = listaOperacao;
    }

    public int getIdOperacao() {
        return idOperacao;
    }

    public void setIdOperacao(int idOperacao) {
        this.idOperacao = idOperacao;
    }

    public Lote getLote() {
        return lote;
    }

    public void setLote(Lote lote) {
        this.lote = lote;
    }

    public int getIdContaOperacao() {
        return idContaOperacao;
    }

    public void setIdContaOperacao(int idContaOperacao) {
        this.idContaOperacao = idContaOperacao;
    }

    public List<SelectItem> getListaContaOperacao() {
        if (listaOperacao.size() == 1 && listaOperacao.get(0).getDescription().equals("0")) {
            listaContaOperacao.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
            return listaContaOperacao;
        }
        if (listaContaOperacao.isEmpty()) {
            LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
            List<ContaOperacao> listaConta = new ArrayList();
            if (Integer.valueOf(listaOperacao.get(idOperacao).getDescription()) == 1 || Integer.valueOf(listaOperacao.get(idOperacao).getDescription()) == 2) {
                if (!listaTipoCentroCusto.isEmpty()) {
                    listaConta = db.listaContaOperacaoContabil(Integer.valueOf(getListaTipoCentroCusto().get(idTipoCentroCusto).getDescription()));
                }
            } else {
                listaConta = db.listaContaOperacao(Integer.valueOf(listaOperacao.get(idOperacao).getDescription()));
            }
            if (!listaConta.isEmpty()) {
                for (int i = 0; i < listaConta.size(); i++) {
                    listaContaOperacao.add(new SelectItem(
                            i,
                            listaConta.get(i).getPlano5().getConta(),
                            Integer.toString(listaConta.get(i).getId()))
                    );
                }
            } else {
                listaContaOperacao.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
            }
        }
        return listaContaOperacao;
    }

    public void setListaContaOperacao(List<SelectItem> listaContaOperacao) {
        this.listaContaOperacao = listaContaOperacao;
    }

    public int getIdCentroCusto() {
        return idCentroCusto;
    }

    public void setIdCentroCusto(int idCentroCusto) {
        this.idCentroCusto = idCentroCusto;
    }

    public int getIdTipoCentroCusto() {
        return idTipoCentroCusto;
    }

    public void setIdTipoCentroCusto(int idTipoCentroCusto) {
        this.idTipoCentroCusto = idTipoCentroCusto;
    }

    public List<SelectItem> getListaCentroCusto() {
        if (listaCentroCusto.isEmpty()) {
            List<CentroCusto> listaCentro = (new LancamentoFinanceiroDBToplink()).listaCentroCusto(Integer.valueOf(listaFilial.get(idFilial).getDescription()));
            if (!listaCentro.isEmpty()) {
                for (int i = 0; i < listaCentro.size(); i++) {
                    listaCentroCusto.add(new SelectItem(
                            i,
                            listaCentro.get(i).getDescricao(),
                            Integer.toString(listaCentro.get(i).getId()))
                    );
                }
            } else {
                listaCentroCusto.add(new SelectItem(0, "Nenhum Centro Custo Encontrado", "0"));
            }
        }
        return listaCentroCusto;
    }

    public void setListaCentroCusto(List<SelectItem> listaCentroCusto) {
        this.listaCentroCusto = listaCentroCusto;
    }

    public List<SelectItem> getListaTipoCentroCusto() {
        if (listaTipoCentroCusto.isEmpty()) {
            if (Integer.valueOf(getListaCentroCusto().get(idCentroCusto).getDescription()) == 0){
                GenericaMensagem.error("Erro", "Cadastre um Centro de Custo para fazer um Lançamento!");
                listaTipoCentroCusto.add(new SelectItem(0, "Nenhum Tipo Encontrado", "0"));
                return listaTipoCentroCusto;
            }
            
            CentroCusto centroCusto = (CentroCusto) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(getListaCentroCusto().get(idCentroCusto).getDescription()), "CentroCusto");
            List<CentroCustoContabilSub> listaCentroContabil = (new LancamentoFinanceiroDBToplink()).listaTipoCentroCusto(centroCusto.getCentroCustoContabil().getId(), es);
            if (!listaCentroContabil.isEmpty()) {
                for (int i = 0; i < listaCentroContabil.size(); i++) {
                    listaTipoCentroCusto.add(new SelectItem(
                            i,
                            listaCentroContabil.get(i).getDescricao(),
                            Integer.toString(listaCentroContabil.get(i).getId()))
                    );
                }
            } else {
                listaTipoCentroCusto.add(new SelectItem(0, "Nenhum Tipo Encontrado", "0"));
            }
        }
        return listaTipoCentroCusto;
    }

    public void setListaTipoCentroCusto(List<SelectItem> listaTipoCentroCusto) {
        this.listaTipoCentroCusto = listaTipoCentroCusto;
    }

    public Operacao getOperacao() {
        if (listaOperacao.size() != 1 && !listaOperacao.get(0).getDescription().equals("0")) {
            if (Integer.valueOf(listaOperacao.get(idOperacao).getDescription()) != operacao.getId()) {
                operacao = (Operacao) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaOperacao.get(idOperacao).getDescription()), "Operacao");
            }
        }
        return operacao;
    }

    public void setOperacao(Operacao operacao) {
        this.operacao = operacao;
    }

    public String getVencimento() {
        return vencimento;
    }

    public void setVencimento(String vencimento) {
        this.vencimento = vencimento;
    }

    public DataObject getLinhaSelecionada() {
        return linhaSelecionada;
    }

    public void setLinhaSelecionada(DataObject linhaSelecionada) {
        this.linhaSelecionada = linhaSelecionada;
    }

    public boolean isChkImposto() {
        if (chkImposto) {
            listaContaTipoPlano5.clear();
        }
        return chkImposto;
    }

    public void setChkImposto(boolean chkImposto) {
        this.chkImposto = chkImposto;
    }

    public List<SelectItem> getListaContaTipoPlano5() {
        if (listaContaTipoPlano5.isEmpty()) {
            //List<ContaTipoPlano5> select = (new SalvarAcumuladoDBToplink()).listaObjeto("ContaTipoPlano5");
            LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();

            List<ContaTipoPlano5> select = db.listaContaTipoPlano5(-1, 1);
            if (!select.isEmpty()) {
                for (int i = 0; i < select.size(); i++) {
                    listaContaTipoPlano5.add(new SelectItem(
                            i,
                            select.get(i).getPlano5().getConta(),
                            Integer.toString(select.get(i).getId()))
                    );
                }
            } else {
                listaContaTipoPlano5.add(new SelectItem(0, "Nenhuma Conta Encontrada", "0"));
            }
        }
        return listaContaTipoPlano5;
    }

    public void setListaContaTipoPlano5(List<SelectItem> listaContaTipoPlano5) {
        this.listaContaTipoPlano5 = listaContaTipoPlano5;
    }

    public int getIdContaTipoPlano5() {
        return idContaTipoPlano5;
    }

    public void setIdContaTipoPlano5(int idContaTipoPlano5) {
        this.idContaTipoPlano5 = idContaTipoPlano5;
    }

    public String getStrConta() {
        if (!listaContaOperacao.isEmpty() && !chkImposto) {
            strConta = listaContaOperacao.get(idContaOperacao).getLabel();
        } else if (!listaContaTipoPlano5.isEmpty() && chkImposto) {
            strConta = listaContaTipoPlano5.get(idContaTipoPlano5).getLabel();
        } else {
            //strConta = "SEM CONTA SELECIONADA.";
            strConta = getListaContaTipoPlano5().get(idContaTipoPlano5).getLabel();
        }
        return strConta;
    }

    public void setStrConta(String strConta) {
        this.strConta = strConta;
    }

    public boolean isDisabledConta() {
        return disabledConta;
    }

    public void setDisabledConta(boolean disabledConta) {
        this.disabledConta = disabledConta;
    }

    public String getDocumentoMovimento() {
        return documentoMovimento;
    }

    public void setDocumentoMovimento(String documentoMovimento) {
        this.documentoMovimento = documentoMovimento;
    }

    public ContaTipoPlano5 getContaTipoPlano() {
        if (!listaContaOperacao.isEmpty()) {
            ContaOperacao co = (ContaOperacao) (new SalvarAcumuladoDBToplink()).pesquisaCodigo(Integer.valueOf(listaContaOperacao.get(idContaOperacao).getDescription()), "ContaOperacao");

            if (co != null) {
                LancamentoFinanceiroDB db = new LancamentoFinanceiroDBToplink();
                List<ContaTipoPlano5> select = db.listaContaTipoPlano5(co.getPlano5().getId(), 2);
                if (!select.isEmpty()) {
                    contaTipoPlano = select.get(0);
                } else {
                    contaTipoPlano = new ContaTipoPlano5();
                }
            }
        }
        return contaTipoPlano;
    }

    public void setContaTipoPlano(ContaTipoPlano5 contaTipoPlano) {
        this.contaTipoPlano = contaTipoPlano;
    }

    public String getPorPesquisa() {
        return porPesquisa;
    }

    public void setPorPesquisa(String porPesquisa) {
        this.porPesquisa = porPesquisa;
    }

    public String getMaskSearch() {
        if (porPesquisa.equals("emissao")) {
            maskSearch = "99/99/9999";
        } else if (porPesquisa.equals("documento")) {
            if (idTipoDocumento == 0) {
                maskSearch = "999.999.999-99";
            } else if (idTipoDocumento == 1) {
                maskSearch = "99.999.999/9999-99";
            } else {
                maskSearch = "";
            }

        } else {
            maskSearch = "";
        }

        return maskSearch;
    }

    public void setMaskSearch(String maskSearch) {
        this.maskSearch = maskSearch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<DataObject> getListaParcelaSelecionada() {
        return listaParcelaSelecionada;
    }

    public void setListaParcelaSelecionada(List<DataObject> listaParcelaSelecionada) {
        this.listaParcelaSelecionada = listaParcelaSelecionada;
    }

    public DataObject getLinhaDO() {
        return linhaDO;
    }

    public void setLinhaDO(DataObject linhaDO) {
        this.linhaDO = linhaDO;
    }

    public int getIndexDO() {
        return indexDO;
    }

    public void setIndexDO(int indexDO) {
        this.indexDO = indexDO;
    }

    public String getAcrescimo() {
        return Moeda.converteR$(acrescimo);
    }

    public void setAcrescimo(String acrescimo) {
        this.acrescimo = Moeda.substituiVirgula(acrescimo);
    }

    public String getMulta() {
        return Moeda.converteR$(multa);
    }

    public void setMulta(String multa) {
        this.multa = Moeda.substituiVirgula(multa);
    }

    public String getJuros() {
        return Moeda.converteR$(juros);
    }

    public void setJuros(String juros) {
        this.juros = Moeda.substituiVirgula(juros);
    }

    public String getCorrecao() {
        return Moeda.converteR$(correcao);
    }

    public void setCorrecao(String correcao) {
        this.correcao = Moeda.substituiVirgula(correcao);
    }

    public String getDesconto() {
        return Moeda.converteR$(desconto);
    }
    
    public void setDesconto(String desconto) {
        this.desconto = Moeda.substituiVirgula(desconto);
    }

    public String getStrVisualizando() {
        return strVisualizando;
    }

    public void setStrVisualizando(String strVisualizando) {
        this.strVisualizando = strVisualizando;
    }

    public List<Usuario> getListaUsuarioLancamento() {
        if(listaUsuarioLancamento.isEmpty()){
            SalvarAcumuladoDB sv = new SalvarAcumuladoDBToplink();
            listaUsuarioLancamento = sv.listaObjeto("Usuario", true);
        }
        return listaUsuarioLancamento;
    }

    public void setListaUsuarioLancamento(List<Usuario> listaUsuarioLancamento) {
        this.listaUsuarioLancamento = listaUsuarioLancamento;
    }

    public String getMotivoEstorno() {
        return motivoEstorno;
    }

    public void setMotivoEstorno(String motivoEstorno) {
        this.motivoEstorno = motivoEstorno;
    }

}
