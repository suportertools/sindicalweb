
package br.com.rtools.financeiro.db;

import br.com.rtools.financeiro.Lote;
import java.util.List;
import javax.persistence.EntityManager;


public interface LoteDB {
    public EntityManager getEntityManager();
    public Lote pesquisaCodigo(int id);
    public List pesquisaTodos();
    public int ultimoCodigo();
    public List pesquisarLoteEsp (String desc, String por, String como);
    public boolean delete(Lote lote);
    public List pesquisarLoteTransferência (String desc, String por, String como);
    public List dependentesTransferencia(int idLote);
    public boolean insert(Lote lote);
    public void commit();
    public void roolback();
}

