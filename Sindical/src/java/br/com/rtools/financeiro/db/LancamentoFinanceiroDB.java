package br.com.rtools.financeiro.db;

import br.com.rtools.estoque.Pedido;
import br.com.rtools.financeiro.CentroCusto;
import br.com.rtools.financeiro.CentroCustoContabilSub;
import br.com.rtools.financeiro.ChequePag;
import br.com.rtools.financeiro.ContaBanco;
import br.com.rtools.financeiro.ContaOperacao;
import br.com.rtools.financeiro.ContaTipoPlano5;
import br.com.rtools.financeiro.FiltroLancamento;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.Operacao;
import br.com.rtools.financeiro.Plano5;
import br.com.rtools.pessoa.Fisica;
import br.com.rtools.pessoa.Juridica;
import br.com.rtools.pessoa.TipoDocumento;
import java.util.List;

public interface LancamentoFinanceiroDB {
    public List<TipoDocumento> listaTipoDocumento();
    public List<Operacao> listaOperacao(String ids);
    public Juridica pesquisaJuridica(String documento);
    public Fisica pesquisaFisica(String documento);
    public List<ContaOperacao> listaContaOperacao(int id_operacao);
    public List<ContaOperacao> listaContaOperacaoContabil(int id_centro_custo_contabil_sub);
    public List<CentroCusto> listaCentroCusto(int id_filial);
    public List<CentroCustoContabilSub> listaTipoCentroCusto(int id_centro_custo_contabil_sub, String es);
    public List<FiltroLancamento> listaLancamento(int userId, String forSearch, String description);
    public List<Movimento> listaParcelaLote(int id_lote);
    public List<ContaTipoPlano5> listaContaTipoPlano5(int id_plano5, int id_tipo);
    public FiltroLancamento pesquisaFiltroLancamento(int id_lote);
    public List<Pedido> listaPedido(int id_lote);
    public List<Plano5> listaComboPagamentoBaixa();
    public ChequePag pesquisaChequeConta(String numero, int id_plano);
}
