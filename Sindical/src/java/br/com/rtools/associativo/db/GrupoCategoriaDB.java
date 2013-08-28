package br.com.rtools.associativo.db;

import br.com.rtools.associativo.GrupoCategoria;
import java.util.List;
import javax.persistence.EntityManager;

public interface GrupoCategoriaDB {
    public boolean insert(GrupoCategoria grupoCategoria);
    public boolean update(GrupoCategoria grupoCategoria);
    public boolean delete(GrupoCategoria grupoCategoria);
    public EntityManager getEntityManager();
    public GrupoCategoria pesquisaCodigo(int id);
    public List pesquisaTodos();
}
