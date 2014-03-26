package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaileMapa;
import java.util.Date;
import java.util.List;

public interface EventoBaileDB {

    public String excluirBaile(int idEvento);

    public AEndereco pesquisaEnderecoEvento(int idEvento);

    public List pesquisaTodosAtuais(Date data);

    public List pesquisaEventoDescricao(String desc, String como);

    public List listaBaileMapa(int id_baile);

    public EventoBaileMapa pesquisaMesaBaile(int id_baile, int mesa);

    public List listaBaileMapaDisponiveis(int id_baile);

    public List listaMesasEvento(int idEventoBaile);
}
