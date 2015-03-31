package br.com.rtools.homologacao.db;

import br.com.rtools.homologacao.CancelarHorario;
import br.com.rtools.homologacao.Horarios;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public interface CancelarHorarioDB {

    public EntityManager getEntityManager();

    public CancelarHorario pesquisaCancelamentoHorario(Date data, int idHorario, int idFilial);

    public List pesquisaTodos(int idFilial);

    public List<Horarios> listaTodosHorariosDisponiveisPorFilial(int idFilial, Date date, boolean isCancelados);

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal);

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, String horario);

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, Integer idSemana);

    public List<CancelarHorario> listaTodosHorariosCancelados(Integer idFilial, Date dataInicial, Date dataFinal, Integer idSemana, String horario);

    public boolean cancelarTodosHorariosPeriodo(int idFilial, Date dateInicial, Date dateFinal);
}
