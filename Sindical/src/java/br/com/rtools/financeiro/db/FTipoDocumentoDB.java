package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.FTipoDocumento;
import br.com.rtools.financeiro.TipoPagamento;
import java.util.List;
import javax.persistence.EntityManager;

public interface FTipoDocumentoDB {

    public EntityManager getEntityManager();

    public FTipoDocumento pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosStringsPartida(String desc);

    public List pesquisaTodosStringsCPartida();

    public FTipoDocumento pesquisaTipoDocPorDesc(String desc);

    public List pesquisaTodosStrings(String desc);

    public List pesquisaListaTipoExtrato();

    public TipoPagamento pesquisaCodigoTipoPagamento(int id);

    public List<TipoPagamento> pesquisaTodosTipoPagamento();

    public List<TipoPagamento> pesquisaCodigoTipoPagamentoIDS(String ids);
    
    public List<FTipoDocumento> pesquisaCodigoTipoDocumentoIDS(String ids);
}
