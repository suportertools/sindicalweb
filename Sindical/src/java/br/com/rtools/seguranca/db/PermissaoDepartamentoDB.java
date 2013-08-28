
package br.com.rtools.seguranca.db;

import br.com.rtools.seguranca.PermissaoDepartamento;
import java.util.List;
import javax.persistence.EntityManager;

public interface PermissaoDepartamentoDB {
    public boolean insert(PermissaoDepartamento permissaoDepartamento);
    public boolean update(PermissaoDepartamento permissaoDepartamento);
    public boolean delete(PermissaoDepartamento permissaoDepartamento);
    public EntityManager getEntityManager();
    public PermissaoDepartamento pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List pesquisaPermissaoDptoIdEvento(int id);
    public List pesquisaPermissaDisponivel(String ids);
    public List pesquisaPermissaoAdc(int idDepto, int idNivel);
    public List pesquisaPermissaDepto(String ids);
}

