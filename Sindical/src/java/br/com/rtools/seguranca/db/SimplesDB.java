package br.com.rtools.seguranca.db;

import br.com.rtools.pessoa.Pessoa;
import br.com.rtools.seguranca.Usuario;
import java.util.List;
import javax.persistence.EntityManager;

public interface SimplesDB {

    public EntityManager getEntityManager();

    public Pessoa ValidaUsuarioWeb(String login, String senha);

    public Usuario ValidaUsuario(String login, String senha);

    public Usuario ValidaUsuarioSuporteWeb(String login, String senha);

    public Usuario pesquisaCodigo(int id);

    public List pesquisaTodos();

    public Usuario pesquisaLogin(String login, int idPessoa);

    public Usuario ValidaUsuarioContribuinte(int idUsuario);

    public Pessoa ValidaUsuarioContribuinteWeb(int idPessoa);

    public Usuario ValidaUsuarioContabilidade(int idUsuario);

    public Pessoa ValidaUsuarioContabilidadeWeb(int idPessoa);

    public void updateAcordoMovimento();
}
