package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.TipoDocumento;
import java.util.List;
import javax.persistence.EntityManager;

public interface TipoDocumentoDB {

    public boolean insert(TipoDocumento tipoDocumento);

    public boolean update(TipoDocumento tipoDocumento);

    public boolean delete(TipoDocumento tipoDocumento);

    public EntityManager getEntityManager();

    public List<String> pesquisaTipoDocumento(String des_tipoDocumento);

    public List pesquisaTipoDocumento();

    public TipoDocumento idTipoDocumento(TipoDocumento d_tipoDocumento);

    public TipoDocumento pesquisaCodigo(int id);

    public List pesquisaTodos();
}
