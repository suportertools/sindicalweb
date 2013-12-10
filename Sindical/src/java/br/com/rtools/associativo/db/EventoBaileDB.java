package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaile;
import br.com.rtools.associativo.EventoBaileMapa;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;

public interface EventoBaileDB {
    public String excluirBaile(int idEvento);
    public AEndereco pesquisaEnderecoEvento(int idEvento);
    public List pesquisaTodosAtuais(Date data);
    public List pesquisaEventoDescricao(String desc, String como);
    public List listaBaileMapa(int id_baile);
    public EventoBaileMapa pesquisaMesaBaile(int id_baile, int mesa);
}
