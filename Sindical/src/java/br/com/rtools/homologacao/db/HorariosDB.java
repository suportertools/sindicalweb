package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.Horarios;
import java.util.List;
import javax.persistence.EntityManager;

public interface HorariosDB {

    public EntityManager getEntityManager();

    public boolean insert(Horarios horarios);

    public boolean update(Horarios horarios);

    public boolean delete(Horarios horarios);
    //public List pesquisaTodos();

    public Horarios pesquisaCodigo(int id);
    //public List pesquisaPorHorario(String horario);

    public List pesquisaTodosPorFilial(int idFilial, int idDiaSemana);

    public List pesquisaPorHorarioFilial(int idFilial, String horario, int idSemana);
}
