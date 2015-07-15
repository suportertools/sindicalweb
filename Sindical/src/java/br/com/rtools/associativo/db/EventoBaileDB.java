package br.com.rtools.associativo.db;

import br.com.rtools.associativo.AEndereco;
import br.com.rtools.associativo.EventoBaileConvite;
import br.com.rtools.associativo.EventoBaileMapa;
import java.util.Date;
import java.util.List;

public interface EventoBaileDB {

    public String excluirBaile(int idEvento);

    public AEndereco pesquisaEnderecoEvento(int idEvento);

    public List pesquisaTodosAtuais(Date data);

    public List pesquisaEventoDescricao(String desc, String como);

    public List listaBaileMapa(int id_baile);
    
    public List listaBaileConvite(int id_baile);

    public EventoBaileMapa pesquisaMesaBaile(int id_baile, int mesa);
    
    public EventoBaileConvite pesquisaConviteBaile(int id_baile, int convite);

    public List<EventoBaileMapa> listaBaileMapaDisponiveis(int id_baile, Integer id_status, Integer id_pessoa, Integer id_venda);
    
    public List<EventoBaileConvite> listaBaileConviteDisponiveis(int id_baile, Integer id_status, Integer id_pessoa, Integer id_venda);
}
