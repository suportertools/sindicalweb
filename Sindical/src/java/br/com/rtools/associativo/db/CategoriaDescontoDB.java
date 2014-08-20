package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Categoria;
import br.com.rtools.associativo.CategoriaDesconto;
import br.com.rtools.associativo.CategoriaDescontoDependente;
import java.util.List;
import javax.persistence.EntityManager;

public interface CategoriaDescontoDB {

    public boolean insert(CategoriaDesconto categoriaDesconto);

    public boolean update(CategoriaDesconto categoriaDesconto);

    public boolean delete(CategoriaDesconto categoriaDesconto);

    public EntityManager getEntityManager();

    public CategoriaDesconto pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<Categoria> pesquisaCategoriasSemServico(int idServicos);

    public List pesquisaTodosPorServico(int idServicos);

    public CategoriaDesconto pesquisaTodosPorServicoCategoria(int idServicos, int idCategoria);
    
    public List<CategoriaDescontoDependente> listaDescontoDependentePorCategoria(int id_categoria_desconto);
    
    public CategoriaDescontoDependente pesquisaDescontoDependentePorCategoria(int id_parentesco, int id_categoria_desconto);
}
