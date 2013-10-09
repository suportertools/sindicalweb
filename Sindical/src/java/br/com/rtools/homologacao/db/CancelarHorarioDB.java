package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public interface CancelarHorarioDB {

    public EntityManager getEntityManager();

    public boolean insert(CancelarHorario cancelarHorario);

    public boolean update(CancelarHorario cancelarHorario);

    public boolean delete(CancelarHorario cancelarHorario);

    public List pesquisaTodos();

    public CancelarHorario pesquisaCodigo(int id);

    public CancelarHorario pesquisaCancelamentoHorario(Date data, int idHorario, int idFilial);

    public List pesquisaTodos(int idFilial);

    public List<Horarios> listaTodosHorariosDisponiveisPorFilial(int idFilial, Date date, boolean isCancelados);

    public List<CancelarHorario> listaTodosHorariosCanceladosPorFilial(int idFilial, Date dataInicial, Date dataFinal);

    public boolean cancelarTodosHorariosPeriodo(int idFilial, Date dateInicial, Date dateFinal);
}
