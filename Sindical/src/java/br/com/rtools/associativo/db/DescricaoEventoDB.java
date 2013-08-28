package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEvento;
import br.com.rtools.associativo.DescricaoEvento;
import br.com.rtools.associativo.GrupoEvento;
import java.util.List;
import javax.persistence.EntityManager;

public interface DescricaoEventoDB {
    public boolean insert(DescricaoEvento descricaoEvento);
    public boolean update(DescricaoEvento descricaoEvento);
    public boolean delete(DescricaoEvento descricaoEvento);
    public EntityManager getEntityManager();
    public DescricaoEvento pesquisaCodigo(int id);
    public List pesquisaTodos();
    public List<GrupoEvento> listaGrupoEvento();
    public GrupoEvento pesquisaGrupoEvento(int idGrupoEvento);
    public List<DescricaoEvento> pesquisaDescricaoPorGrupo(int idGrupoEvento);
    public List<AEvento> listaEventoPorDescricao(int idDescEvento);
}
