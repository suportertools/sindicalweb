package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.BloqueiaServicoPessoa;
import br.com.rtools.financeiro.Caixa;
import br.com.rtools.financeiro.ContaSaldo;
import br.com.rtools.financeiro.FormaPagamento;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.financeiro.SubGrupoFinanceiro;
import br.com.rtools.financeiro.TransferenciaCaixa;
import br.com.rtools.seguranca.Usuario;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;

public interface FinanceiroDB {
    public boolean insert(Object objeto);
    public boolean update(Object objeto);
    public boolean delete(Object objeto);
    public boolean delete(Movimento objeto);
    public EntityManager getEntityManager();
    public String insertHist(Object objeto);
    public boolean acumularObjeto(Object objeto);
    public void abrirTransacao();
    public void comitarTransacao();
    public void desfazerTransacao();
    public Movimento pesquisaCodigo(Movimento movimento);
    public Baixa pesquisaCodigo(Baixa loteBaixa);
    public Lote pesquisaCodigo(Lote lote);
    public int contarMovimentosPara(int idLote);
    public List<Movimento> pesquisaMovimentoOriginal(int idLoteBaixa);
    public Usuario pesquisaUsuario(int idUsuario);
    public Historico pesquisaHistorico(int idHistorico);
    public boolean executarQuery(String textoQuery);
    public List<BloqueiaServicoPessoa> listaBloqueiaServicoPessoas(int id_pessoa);
    public BloqueiaServicoPessoa pesquisaBloqueiaServicoPessoa(int id_pessoa, int id_servico, Date dt_inicial, Date dt_final);
    public List<Movimento> pesquisaMovimentoPorLote(int id_lote);
    public ContaSaldo pesquisaSaldoInicial(int id_caixa);
    public List<Caixa> listaCaixa();
    public List listaMovimentoCaixa(int id_caixa, String es);
    public List<TransferenciaCaixa> listaTransferenciaEntrada(int id_caixa);
    public List<TransferenciaCaixa> listaTransferenciaSaida(int id_caixa);
    public List<Baixa> listaBaixa(int id_fechamento_caixa);
    public List listaFechamentoCaixaTransferencia(int id_caixa);
    public List listaFechamentoCaixa(int id_caixa);
    public List<TransferenciaCaixa> listaTransferencia(int id_fechamento_caixa);
    public Caixa pesquisaCaixaUm();
    public List<Vector> listaDeCheques(int id_status);
    public List<Vector> listaMovimentoBancario(int id_plano5);
    public List<TransferenciaCaixa> listaTransferenciaDinheiro(int id_fechamento_caixa, int id_caixa);
    public List<TransferenciaCaixa> listaTransferenciaDinheiroEntrada(int id_fechamento_caixa, int id_caixa);
    public List<TransferenciaCaixa> listaTransferenciaDinheiroSaida(int id_fechamento_caixa, int id_caixa);
    public List<FormaPagamento> listaTransferenciaFormaPagamento(int id_fechamento_caixa, int id_caixa, String es);
    public List<Vector> pesquisaSaldoAtual(int id_caixa);
    public List<Vector> pesquisaSaldoAtualRelatorio(int id_caixa, int id_fechamento);
    public List<Vector> pesquisaUsuarioFechamento(int id_fechamento);
    public List<SubGrupoFinanceiro> listaSubGrupo(int id_grupo);
    public List<Vector> listaBoletoSocioAgrupado(String responsavel, String lote, String data);
    public List<Vector> listaBoletoSocio(String nr_ctr_boleto);
    public List<Vector> listaServicosSemCobranca();
    public List<Vector> listaPessoaSemComplemento(String referenciaVigoracao);
}
