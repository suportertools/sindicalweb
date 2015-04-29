package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Feriados;
import br.com.rtools.pessoa.Filial;
import java.util.List;
import javax.persistence.EntityManager;

/**
 * Use FeriadosDao
 *
 * @author rtools2
 * @deprecated
 */
@Deprecated
public interface FeriadosDB {

    public EntityManager getEntityManager();

    public List pesquisarPorData(String data);

    public boolean exiteFeriadoCidade(Feriados feriados);

    public List pesquisarPorDataFilial(String data, Filial filial);

    public List pesquisarPorDataFilialEData(String data, Filial filial);

    public List pesquisaTodosFeriados();
}
