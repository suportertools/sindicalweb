package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.TipoServico;
import java.util.List;
import javax.persistence.EntityManager;

public interface TipoServicoDB {

    public boolean insert(TipoServico tipoServico);

    public boolean update(TipoServico tipoServico);

    public boolean delete(TipoServico tipoServico);

    public EntityManager getEntityManager();

    public TipoServico pesquisaCodigo(int id);

    public List pesquisaTodos();

    public TipoServico idTipoServico(TipoServico des_tipoServico);

    public List pesquisaTipoServico(String desc, String como);

    public List<TipoServico> pesquisaTodosComIds(List<Integer> arrayId);

    public List pesquisaTodosPeloContaCobranca();
}
