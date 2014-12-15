package br.com.rtools.associativo.db;

import br.com.rtools.associativo.Parentesco;
import java.util.List;
import javax.persistence.EntityManager;

public interface ParentescoDB {

    public boolean insert(Parentesco parentesco);

    public boolean update(Parentesco parentesco);

    public boolean delete(Parentesco parentesco);

    public EntityManager getEntityManager();

    public Parentesco pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodosSemTitular();
    
    public List<Parentesco> pesquisaTodosSemTitularCategoria(int id_categoria);
    
    public List<Parentesco> pesquisaTodosSemTitularCategoriaSemDesconto(int id_categoria, int id_categoria_desconto);
    public List<Parentesco> pesquisaTodosComTitularCategoriaSemDesconto(int id_categoria, int id_categoria_desconto);
    
    public List<Parentesco> pesquisaTodosSemTitularCategoriaSexo(int id_categoria, String sexo);

    //public List pesquisaTodosSemTitularPorSexo(String sexo);
}
