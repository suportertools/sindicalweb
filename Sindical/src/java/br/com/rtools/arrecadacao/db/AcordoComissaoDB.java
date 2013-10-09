package br.com.rtools.arrecadacao.db;

import br.com.rtools.arrecadacao.AcordoComissao;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;

public interface AcordoComissaoDB {

    public boolean insert(AcordoComissao acordoComissao);

    public boolean update(AcordoComissao acordoComissao);

    public boolean delete(AcordoComissao acordoComissao);

    public EntityManager getEntityManager();

    public AcordoComissao pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<Date> pesquisaTodosFechamento();

    public boolean inserirAcordoComissao();

    public List<AcordoComissao> pesquisaData(String data);

    public boolean estornarAcordoComissao(String data);

    public List<Vector> listaAcordoComissao(String data);
}
