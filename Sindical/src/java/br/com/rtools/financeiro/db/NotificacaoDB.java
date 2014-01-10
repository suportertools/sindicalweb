package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Cobranca;
import br.com.rtools.financeiro.CobrancaEnvio;
import br.com.rtools.financeiro.CobrancaLote;
import br.com.rtools.financeiro.CobrancaTipo;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public interface NotificacaoDB {

    public Object[] listaParaNotificacao(int id_lote, String data, String id_empresa, String id_contabil, String id_cidade, boolean comContabil, boolean semContabil);

    public List listaNotificado(int id_movimento);

    public List<CobrancaLote> listaCobrancaLote();

    public List<CobrancaTipo> listaCobrancaTipoEnvio();

    public List<Cobranca> listaCobranca(int id_lote_cobranca);

    public CobrancaLote pesquisaCobrancaLote(int id_usuario, Date dataEmissao);

    public List listaNotificacaoEnvio(int tipo_envio, int id_lote);

    public List pollingEmail(int limite, int id_usuario);

    public List pollingEmailTrue();

    public List pollingEmailNovo(int limite);

    public List pesquisaUltimoLote();

    public CobrancaEnvio pesquisaCobrancaEnvio(int id_lote);
    
    public List<Vector> listaParaEtiqueta(String string_qry, CobrancaTipo ct);
}
