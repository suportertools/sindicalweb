package br.com.rtools.associativo.db;

import br.com.rtools.associativo.ServicoCategoria;
import java.util.List;
import javax.persistence.EntityManager;

public interface ServicoCategoriaDB {

    public boolean insert(ServicoCategoria servicoCategoria);

    public boolean update(ServicoCategoria servicoCategoria);

    public boolean delete(ServicoCategoria servicoCategoria);

    public EntityManager getEntityManager();

    public ServicoCategoria pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaServCatPorId(int idCategoria);

    public ServicoCategoria pesquisaPorParECat(int idParentesco, int idCategoria);
}
