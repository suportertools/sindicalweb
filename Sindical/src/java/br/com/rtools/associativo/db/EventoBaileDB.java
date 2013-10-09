package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaile;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public interface EventoBaileDB {

    public boolean insert(EventoBaile eventoBaile);

    public boolean update(EventoBaile eventoBaile);

    public boolean delete(EventoBaile eventoBaile);

    public EntityManager getEntityManager();

    public EventoBaile pesquisaCodigo(int id);

    public List pesquisaTodos();

    public String excluirBaile(int idEvento);

    public void abrirTransacao();

    public void comitarTransacao();

    public void desfazerTransacao();

    public AEndereco pesquisaEnderecoEvento(int idEvento);

    public List pesquisaTodosAtuais(Date data);

    public List pesquisaEventoDescricao(String desc, String como);
}
