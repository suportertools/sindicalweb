package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.GrupoCategoria;
import java.util.List;
import javax.persistence.EntityManager;

public interface CategoriaDB {

    public boolean insert(Categoria categoria);

    public boolean update(Categoria categoria);

    public boolean delete(Categoria categoria);

    public EntityManager getEntityManager();

    public Categoria pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaCategoriaPorGrupo(int idGrupoCategoria);

    public List pesquisaCategoriaPorGrupoIds(String ids);

    public GrupoCategoria pesquisaGrupoPorCategoria(int idCategoria);
    
    public List<GrupoCategoria> pesquisaGrupoCategoriaOrdenada();
}
