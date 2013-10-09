package br.com.rtools.associativo.db;

import br.com.rtools.associativo.CVenda;
import br.com.rtools.associativo.Reservas;
import br.com.rtools.financeiro.Movimento;
import java.util.List;
import javax.persistence.EntityManager;

public interface VendasCaravanaDB {

    public EntityManager getEntityManager();

    public CVenda pesquisaCodigo(int id);

    public List pesquisaTodos();

    public int qntReservas(int idEvento, int idGrupoEvento);

    public List<Integer> listaPoltronasUsadas(int idEvento);

    public List<Reservas> listaReservasVenda(int idVenda);

    public List<Movimento> listaMovCaravana(int idResponsavel, int idEvt);

    public List<Movimento> listaMovCaravanaBaixado(int idLoteBaixa);
}
