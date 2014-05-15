package br.com.rtools.pessoa.db;

import br.com.rtools.pessoa.DocumentoInvalido;
import java.util.List;
import java.util.Vector;
import javax.persistence.EntityManager;

public interface DocumentoInvalidoDB {

    public EntityManager getEntityManager();

    public boolean insert(DocumentoInvalido documentoInvalido);

    public boolean update(DocumentoInvalido documentoInvalido);

    public boolean delete(DocumentoInvalido documentoInvalido);

    public DocumentoInvalido pesquisaCodigo(int id);

    public List pesquisaTodos();

    public List<DocumentoInvalido> pesquisaNumeroBoleto(String numero);

    public List<DocumentoInvalido> pesquisaNumeroBoletoPessoa();
}
