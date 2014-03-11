package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Convencao;
import java.util.List;
import javax.persistence.EntityManager;

public interface ConvencaoDB {

    public EntityManager getEntityManager();

    public List pesquisaTodos();

    public Convencao pesquisaConvencaoDesc(String descricao);
}
