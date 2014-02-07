package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.Convencao;
import br.com.rtools.arrecadacao.ConvencaoCidade;
import br.com.rtools.arrecadacao.GrupoCidade;
import java.util.List;
import javax.persistence.EntityManager;

public interface ConvencaoCidadeDB {

    public boolean insert(ConvencaoCidade ConvencaoCidade);

    public boolean update(ConvencaoCidade ConvencaoCidade);

    public boolean delete(ConvencaoCidade ConvencaoCidade);

    public EntityManager getEntityManager();

    public ConvencaoCidade pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List pesquisarGrupos(int idConvencao, int idGrupoCidade);

    public ConvencaoCidade pesquisarConvencao(int idConvencao, int idGrupoCidade);

    public List<GrupoCidade> pesquisarGruposPorConvencao(int idConvencao);

    public List ListaCidadesConvencao(int idConvencao, int idGrupoCidade);

    public GrupoCidade pesquisaGrupoCidadeJuridica(int idConvencao, int idCidade);

    public List pesquisarConvencaoCidade(List<Integer> listaParametro);

    public ConvencaoCidade pesquisarConvencaoCidade(int idPessoa);

    public List pesquisarConvencaoPorGrupos(int idGrupoCidade);
}
