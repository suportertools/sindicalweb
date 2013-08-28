package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Baixa;
import br.com.rtools.financeiro.BloqueiaServicoPessoa;
import br.com.rtools.financeiro.Cobranca;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import br.com.rtools.financeiro.Historico;
import br.com.rtools.financeiro.Lote;
import br.com.rtools.financeiro.Movimento;
import br.com.rtools.seguranca.Usuario;
import java.util.Date;
import java.util.List;
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
    public List<CobrancaLote> listaCobrancaLote();
    public List<CobrancaTipo> listaCobrancaTipoEnvio();
    public List<Cobranca> listaCobranca(int id_lote_cobranca);
    public CobrancaLote pesquisaCobrancaLote(int id_usuario, Date dataEmissao);
    public List listaNotificacao(int tipo_envio, int id_lote);
    public List<BloqueiaServicoPessoa> listaBloqueiaServicoPessoas(int id_pessoa);
    public BloqueiaServicoPessoa  pesquisaBloqueiaServicoPessoa(int id_pessoa, int id_servico, Date dt_inicial, Date dt_final);
}

