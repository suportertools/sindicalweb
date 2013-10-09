package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Evt;
import br.com.rtools.financeiro.Movimento;
import java.util.List;
import javax.persistence.EntityManager;

public interface EvtDB {

    public boolean insert(Evt evt);

    public boolean update(Evt evt);

    public boolean delete(Evt evt);

    public EntityManager getEntityManager();

    public Evt pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<Movimento> pesquisaMovimentoEvt(int idEvento, int idPessoa);
}
