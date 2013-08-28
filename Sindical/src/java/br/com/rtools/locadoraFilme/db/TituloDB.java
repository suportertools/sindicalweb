package br.com.rtools.locadoraFilme.db;

import br.com.rtools.locadoraFilme.Genero;
import br.com.rtools.locadoraFilme.Titulo;
import java.util.List;
import javax.persistence.EntityManager;

public interface TituloDB {

    public boolean insert(Titulo titulo);
    public boolean update(Titulo titulo);
    public boolean delete(Titulo titulo);
    public EntityManager getEntityManager();
    public Titulo pesquisaCodigo(int id);
    public List pesquisaTodos();
    public String pesquisaTitulo(String des_titulo);
    public List pesquisaTitulos (String desc, String por, String como);
    public Genero pesquisaCodigoGenero(int id);
    public List pesquisaTodosGenero();
    public String pesquisaGenero(String des_genero);

}
