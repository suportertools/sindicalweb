package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Acordo;
import br.com.rtools.financeiro.Historico;
import java.util.List;
import javax.persistence.EntityManager;

public interface AcordoDB {

    public boolean insert(Acordo acordo);

    public boolean update(Acordo acordo);

    public boolean delete(Acordo acordo);

    public EntityManager getEntityManager();

    public Acordo pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisaTodasFolhas();

    public Historico pesquisaHistorico(int id);

    public Historico pesquisaHistoricoBaixado(int idContaCobranca, String nrBoleto, int idServico);

    public Historico pesquisaHistoricoMov(int idContaCobranca, int idMovimento);

    public List listaHistoricoAgrupado(int id_acordo);
}
