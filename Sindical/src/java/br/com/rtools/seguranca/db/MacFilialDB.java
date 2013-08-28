package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.MacFilial;
import java.util.List;

public interface MacFilialDB {
    public boolean insert(MacFilial macFilial);
    public boolean update(MacFilial macFilial) ;
    public boolean delete(MacFilial macFilial);
    public MacFilial pesquisaCodigo(int id);
    public List pesquisaTodos();
    public MacFilial pesquisaMac(String mac);
}
