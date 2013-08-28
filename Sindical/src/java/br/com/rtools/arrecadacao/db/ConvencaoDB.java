package br.com.rtools.arrecadacao.db;


import br.com.rtools.arrecadacao.Convencao;
import java.util.List;
import javax.persistence.EntityManager;


public interface ConvencaoDB {
    public boolean insert(Convencao convencao);
    public boolean update(Convencao convencao);
    public boolean delete(Convencao convencao);
    public EntityManager getEntityManager();
    public Convencao pesquisaCodigo(int id);
    public List pesquisaTodos();
    public Convencao pesquisaConvencaoDesc(String descricao);

}

