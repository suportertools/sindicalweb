package br.com.rtools.locadoraFilme.db;

import br.com.rtools.locadoraFilme.Catalogo;
import br.com.rtools.locadoraFilme.Titulo;
import br.com.rtools.pessoa.Filial;
import java.util.List;
import javax.persistence.EntityManager;


public interface CatalogoDB {

    public boolean insert(Catalogo catalogo);
    public boolean update(Catalogo catalogo);
    public boolean delete(Catalogo catalogo);
    public EntityManager getEntityManager();
    public Catalogo pesquisaCodigo(int id);
    public List pesquisaTodos();
    public boolean verificaFilial(Filial filial, Titulo titulo);
    public List<Catalogo> pesquisaCatalogo (Filial filial, Titulo titulo);
    public List<Catalogo> pesquisaCatalogo (Filial filial);
    public Catalogo pesquisaMatriz (int idTitulo);
}