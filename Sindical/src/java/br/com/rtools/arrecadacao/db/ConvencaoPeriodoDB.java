package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.ConvencaoPeriodo;
import java.util.List;
import javax.persistence.EntityManager;

public interface ConvencaoPeriodoDB {

    public EntityManager getEntityManager();

    public List listaGrupoCidadePorConvencao(int idConvencao);

    public boolean convencaoPeriodoExiste(ConvencaoPeriodo convencaoPeriodo);

    public List<ConvencaoPeriodo> listaConvencaoPeriodo();

    public ConvencaoPeriodo convencaoPeriodoConvencaoGrupoCidade(int idConvencao, int idGrupoCidade);

    public ConvencaoPeriodo pesquisaCodigo(int id);
}
