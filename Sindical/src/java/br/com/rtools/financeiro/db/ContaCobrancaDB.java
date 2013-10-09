package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.ContaCobranca;
import br.com.rtools.financeiro.Layout;
import java.util.List;
import javax.persistence.EntityManager;

public interface ContaCobrancaDB {

    public boolean insert(ContaCobranca contaCobranca);

    public boolean update(ContaCobranca contaCobranca);

    public boolean delete(ContaCobranca contaCobranca);

    public EntityManager getEntityManager();

    public ContaCobranca pesquisaCodigo(int id);

    public ContaCobranca pesquisaServicoCobranca(int idServicos, int idTipoServico);

    public List pesquisaTodos();

    public ContaCobranca idContaCobranca(ContaCobranca des_contaCobranca);

    public List pesquisaLayouts();

    public Layout pesquisaLayoutId(int id);

    public ContaCobranca pesquisaCobrancaCedente(String codCedente);
}
