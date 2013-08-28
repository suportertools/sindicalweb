
package br.com.rtools.agenda.db;


import br.com.rtools.agenda.GrupoAgenda;
import java.util.List;
import javax.persistence.EntityManager;


public interface GrupoAgendaDB {
    public boolean insert(GrupoAgenda grupoAgenda);
    public boolean update(GrupoAgenda GrupoAgenda);
    public boolean delete(GrupoAgenda GrupoAgenda);
    public EntityManager getEntityManager();
    public GrupoAgenda pesquisaCodigo(int id);
    public List pesquisaTodos();
    public GrupoAgenda idGrupoAgenda(GrupoAgenda des_grupoAgenda);
    public List pesquisaGrupoAgenda (String desc, String como);
}

